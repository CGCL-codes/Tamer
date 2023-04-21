package org.opt4j.gui;

import static org.opt4j.core.Objective.Sign.MIN;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.opt4j.config.Icons;
import org.opt4j.core.Archive;
import org.opt4j.core.Individual;
import org.opt4j.core.IndividualBuilder;
import org.opt4j.core.IndividualCollection;
import org.opt4j.core.IndividualCollectionListener;
import org.opt4j.core.IndividualStateListener;
import org.opt4j.core.Objective;
import org.opt4j.core.Objectives;
import org.opt4j.core.Population;
import org.opt4j.core.optimizer.Optimizer;
import org.opt4j.core.problem.Evaluator;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * A widget that monitors the {@code Population}.
 * 
 * @author lukasiewycz
 * 
 */
@Singleton
@WidgetParameters(frameTitle = "Population Monitor", frameIcon = Icons.POPULATION, buttonText = "Population", buttonIcon = Icons.POPULATION)
public class PopulationWidget implements IndividualStateListener, IndividualCollectionListener, Widget {

    private static final long serialVersionUID = 1L;

    protected static final int OFFSET = 2;

    protected final Archive archive;

    protected final Population population;

    protected final Optimizer optimizer;

    protected final IndividualBuilder individualBuilder;

    protected final Evaluator<?> evaluator;

    protected final List<Individual> individuals = Collections.synchronizedList(new ArrayList<Individual>());

    protected List<Objective> objectives = new ArrayList<Objective>();

    protected JPanel panel = new JPanel();

    protected JTable table;

    protected int size = 0;

    protected boolean isInit = false;

    protected DelayTask task = new DelayTask(40);

    /**
	 * The table.
	 * 
	 * @author lukasiewycz
	 * 
	 */
    protected class Table extends JTable {

        private static final long serialVersionUID = 1L;

        public Table(TableModel tableModel) {
            super(tableModel);
        }

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            String family = c.getFont().getFamily();
            int size = c.getFont().getSize();
            try {
                int model = row;
                Individual individual = individuals.get(model);
                if (individual != null) {
                    if (archive.contains(individual)) {
                        c.setFont(new Font(family, Font.BOLD, size));
                    }
                    if (individual.getState().isProcessing()) {
                        c.setForeground(Color.RED);
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
            }
            return c;
        }
    }

    /**
	 * The header.
	 * 
	 * @author lukasiewycz
	 * 
	 */
    protected class Header extends JTableHeader {

        private static final long serialVersionUID = 1L;

        public Header(TableColumnModel columnModel) {
            super(columnModel);
        }

        @Override
        public String getToolTipText(final MouseEvent e) {
            String tip = null;
            final java.awt.Point p = e.getPoint();
            final int index = columnModel.getColumnIndexAtX(p.x);
            final int realIndex = columnModel.getColumn(index).getModelIndex() - OFFSET;
            if (realIndex >= 0) {
                final Objective objective = objectives.get(realIndex);
                final String name = objective.getName();
                final String sign = ((objective.getSign() == MIN) ? "MIN" : "MAX");
                tip = "<html>" + name + "<br/>" + sign + "</html>";
            }
            return tip;
        }
    }

    /**
	 * The model.
	 * 
	 * @author lukasiewycz
	 * 
	 */
    protected class Model extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        public int getColumnCount() {
            return OFFSET + objectives.size();
        }

        public int getRowCount() {
            size = Math.max(size, individuals.size());
            return size;
        }

        @Override
        public String getColumnName(int col) {
            if (col == 0) {
                return "Individual";
            } else if (col == 1) {
                return "State";
            } else {
                final int index = col - OFFSET;
                final Objective objective = objectives.get(index);
                return objective.getName() + " (" + objective.getSign() + ")";
            }
        }

        public Object getValueAt(final int row, final int col) {
            try {
                final Individual individual = individuals.get(row);
                if (individual != null) {
                    if (col == 0) {
                        return row + 1;
                    } else if (col == 1) {
                        return individual.getState();
                    } else {
                        if (individual.getState() == Individual.State.EVALUATED) {
                            final int index = col - OFFSET;
                            final Objectives o = individual.getObjectives();
                            final Objective objective = objectives.get(index);
                            return o.get(objective).getValue();
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
            }
            return null;
        }
    }

    /**
	 * Constructs a {@code PopulationWidget}.
	 * 
	 * @param population
	 *            the {@code Population}
	 * @param archive
	 *            the {@code Archive}
	 * @param optimizer
	 *            the {@code Optimizer}
	 * @param individualBuilder
	 *            the {@code IndividualBuilder}
	 * @param evaluator
	 *            the {@code Evaluator}
	 */
    @SuppressWarnings("unchecked")
    @Inject
    public PopulationWidget(Population population, Archive archive, Optimizer optimizer, IndividualBuilder individualBuilder, Evaluator evaluator) {
        this.optimizer = optimizer;
        this.archive = archive;
        this.population = population;
        this.individualBuilder = individualBuilder;
        this.evaluator = evaluator;
    }

    public synchronized void init(Viewport viewport) {
        if (!isInit) {
            objectives.addAll(evaluator.getObjectives());
            Collections.sort(objectives);
            population.addListener(this);
            individualBuilder.addIndividualStateListener(this);
            Model model = new Model();
            table = new Table(model);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    Header header = new Header(table.getColumnModel());
                    table.setTableHeader(header);
                    JScrollPane scrollpane = new JScrollPane(table);
                    panel.setLayout(new BorderLayout());
                    panel.add(scrollpane, BorderLayout.CENTER);
                    for (int i = 0; i < 3; i++) {
                        try {
                            individuals.clear();
                            individuals.addAll(population);
                            break;
                        } catch (ConcurrentModificationException e) {
                        }
                    }
                    isInit = true;
                    table.revalidate();
                    table.repaint();
                }
            });
        }
    }

    public void inidividualStateChanged(Individual individual) {
        paint();
    }

    public synchronized void individualAdded(IndividualCollection collection, Individual individual) {
        individuals.add(individual);
        paint();
    }

    public synchronized void individualRemoved(IndividualCollection collection, Individual individual) {
        individuals.remove(individual);
        paint();
    }

    protected void paint() {
        if (isInit) {
            task.execute(new Thread() {

                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            table.revalidate();
                            table.repaint();
                        }
                    });
                }
            });
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
