package grammarscope.editor.component;

import grammarscope.message.Event;
import grammarscope.message.Firer;
import grammarscope.message.IListener;
import grammarscope.message.Listenee;
import grammarscope.message.Request;
import grammarscope.parser.MutableGrammaticalRelation;
import grammarscope.parser.RelationModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

/**
 * @author bbou
 */
public class RelationModelView extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
	 * Relation table
	 */
    private RelationTable theTable;

    /**
	 * The target expression editor
	 */
    private JTextField theTargetEdit;

    /**
	 * 'Start with' checkbox
	 */
    private JCheckBox theStartWithCheckBox;

    /**
	 * Relation model
	 */
    private RelationModel theRelationModel;

    /**
	 * Request firer
	 */
    private final Firer<Request> theFirer;

    /**
	 * Event listener
	 */
    private final IListener<Event> theEventListener;

    /**
	 * Selection listener
	 */
    public ListSelectionListener theSelectionListener;

    /**
	 * Constructor
	 */
    public RelationModelView() {
        this.theFirer = new Firer<Request>();
        this.theRelationModel = null;
        initialize();
        this.theEventListener = new IListener<Event>() {

            @Override
            public void notified(final Event thisEvent) {
                switch(thisEvent.getType()) {
                    case RELATIONMODELCHANGED:
                        set((RelationModel) thisEvent.getObject());
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
	 * Add listener
	 * 
	 * @param thisRequestListener
	 *            request listener
	 */
    public void addListener(final IListener<Request> thisRequestListener) {
        this.theFirer.addListener(thisRequestListener);
    }

    /**
	 * Listen to event source
	 * 
	 * @param thisListenee
	 *            event source
	 */
    public void listen(final Listenee<Event> thisListenee) {
        thisListenee.addListener(this.theEventListener);
    }

    /**
	 * Get event listener
	 * 
	 * @return event listener
	 */
    public IListener<Event> getEventListener() {
        return this.theEventListener;
    }

    /**
	 * Initialize
	 */
    private void initialize() {
        final JPanel thisSearchPanel = new JPanel();
        thisSearchPanel.setLayout(new FlowLayout());
        thisSearchPanel.add(new JLabel(new ImageIcon(BalloonTip.class.getResource("images/filter.png"))));
        this.theTargetEdit = makeEdit();
        thisSearchPanel.add(this.theTargetEdit);
        this.theStartWithCheckBox = makeStartWithCheckBox();
        thisSearchPanel.add(this.theStartWithCheckBox);
        this.theTable = makeTable();
        setLayout(new BorderLayout());
        this.add(thisSearchPanel, BorderLayout.SOUTH);
        this.add(new JScrollPane(this.theTable), BorderLayout.CENTER);
        this.theSelectionListener = new ListSelectionListener() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void valueChanged(final ListSelectionEvent thisEvent) {
                if (!thisEvent.getValueIsAdjusting()) {
                    final int thisIndex = RelationModelView.this.theTable.getSelectedRow();
                    if (thisIndex != -1) {
                        final MutableGrammaticalRelation thisRelation = (MutableGrammaticalRelation) RelationModelView.this.theTable.getValueAt(thisIndex, 0);
                        RelationModelView.this.theFirer.fire(new Request(Request.Type.EDITRELATION, thisRelation));
                    }
                }
            }
        };
    }

    /**
	 * Set select firing
	 * 
	 * @param thisFlag
	 *            whether to fire select events
	 */
    public void setSelectFiring(final boolean thisFlag) {
        if (thisFlag) {
            this.theTable.getSelectionModel().addListSelectionListener(this.theSelectionListener);
        } else {
            this.theTable.getSelectionModel().removeListSelectionListener(this.theSelectionListener);
        }
    }

    /**
	 * Make mouse listener
	 * 
	 * @param thisTable
	 *            table
	 * @return mouse listener
	 */
    private MouseListener makeMouseListener(final JTable thisTable) {
        return new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                popUp(e, thisTable);
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                popUp(e, thisTable);
            }

            /**
			 * Make popup
			 * 
			 * @param thisRelation
			 *            relation
			 * @return pop up menu
			 */
            private JPopupMenu makePopup(final MutableGrammaticalRelation thisRelation) {
                final JPopupMenu thisPopup = new JPopupMenu();
                final JMenuItem thisEditMenuItem = new JMenuItem("Edit");
                if (thisRelation == null) {
                    thisEditMenuItem.setEnabled(false);
                }
                thisEditMenuItem.addActionListener(new ActionListener() {

                    @SuppressWarnings("synthetic-access")
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        RelationModelView.this.theFirer.fire(new Request(Request.Type.EDITRELATION, thisRelation));
                    }
                });
                thisPopup.add(thisEditMenuItem);
                final JMenuItem thisAddMenuItem = new JMenuItem("Add new");
                thisAddMenuItem.addActionListener(new ActionListener() {

                    @SuppressWarnings("synthetic-access")
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        RelationModelView.this.theFirer.fire(new Request(Request.Type.NEWRELATION, thisRelation));
                    }
                });
                thisPopup.add(thisAddMenuItem);
                final JMenuItem thisDuplicateMenuItem = new JMenuItem("Duplicate");
                thisDuplicateMenuItem.addActionListener(new ActionListener() {

                    @SuppressWarnings("synthetic-access")
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        RelationModelView.this.theFirer.fire(new Request(Request.Type.DUPLICATERELATION, thisRelation));
                    }
                });
                thisPopup.add(thisDuplicateMenuItem);
                final JMenuItem thisDeleteMenuItem = new JMenuItem("Remove");
                if (thisRelation == null || thisRelation.getChildren().size() != 0) {
                    thisDeleteMenuItem.setEnabled(false);
                }
                thisDeleteMenuItem.addActionListener(new ActionListener() {

                    @SuppressWarnings("synthetic-access")
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        RelationModelView.this.theFirer.fire(new Request(Request.Type.REMOVERELATION, thisRelation));
                    }
                });
                thisPopup.add(thisDeleteMenuItem);
                thisPopup.setOpaque(true);
                thisPopup.setLightWeightPopupEnabled(true);
                return thisPopup;
            }

            /**
			 * Show pop up menu
			 * 
			 * @param e
			 *            event
			 * @param thatTable
			 *            table
			 */
            private void popUp(final MouseEvent e, final JTable thatTable) {
                if (e.isPopupTrigger()) {
                    final int thisRow = thatTable.rowAtPoint(e.getPoint());
                    final MutableGrammaticalRelation thisRelation = thisRow == -1 ? null : (MutableGrammaticalRelation) thatTable.getValueAt(thisRow, 0);
                    final JPopupMenu thisPopup = makePopup(thisRelation);
                    thisPopup.show((JComponent) e.getSource(), e.getX(), e.getY());
                }
            }
        };
    }

    /**
	 * Make edit field
	 * 
	 * @return edit field
	 */
    private JTextField makeEdit() {
        final JTextField thisEdit = new JTextField();
        thisEdit.setColumns(8);
        thisEdit.addActionListener(new ActionListener() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void actionPerformed(final ActionEvent e) {
                filter();
            }
        });
        thisEdit.getDocument().addDocumentListener(new DocumentListener() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void changedUpdate(final DocumentEvent e) {
                filter();
            }

            @SuppressWarnings("synthetic-access")
            @Override
            public void insertUpdate(final DocumentEvent e) {
                filter();
            }

            @SuppressWarnings("synthetic-access")
            @Override
            public void removeUpdate(final DocumentEvent e) {
                filter();
            }
        });
        return thisEdit;
    }

    /**
	 * Make 'start with' checkbox
	 * 
	 * @return 'start with' checkbox
	 */
    private JCheckBox makeStartWithCheckBox() {
        final JCheckBox thisCheckBox = new JCheckBox("initial");
        thisCheckBox.setSelected(true);
        thisCheckBox.addActionListener(new ActionListener() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void actionPerformed(final ActionEvent e) {
                filter();
            }
        });
        return thisCheckBox;
    }

    /**
	 * Make relation table
	 * 
	 * @return relation table
	 */
    private RelationTable makeTable() {
        final RelationTable thisTable = new RelationTable(new RelationTableModel());
        thisTable.initialize();
        final MouseListener thisMouseListener = makeMouseListener(thisTable);
        thisTable.addMouseListener(thisMouseListener);
        thisTable.getTableHeader().addMouseListener(thisMouseListener);
        return thisTable;
    }

    /**
	 * Set model
	 */
    public void set(final RelationModel thisRelationModel) {
        this.theRelationModel = thisRelationModel;
        final RelationTableModel thisModel = new RelationTableModel(this.theRelationModel);
        this.theTable.setModel(thisModel);
        this.theTable.initialize();
        this.theTable.addMouseListener(makeMouseListener(this.theTable));
        filter();
    }

    /**
	 * Filter
	 */
    @SuppressWarnings("unchecked")
    private void filter() {
        try {
            String thisRegExpr = this.theTargetEdit.getText();
            if (this.theStartWithCheckBox.isSelected()) {
                thisRegExpr = "^" + thisRegExpr;
            }
            final RowFilter<RelationTableModel, Object> thisFilter = RowFilter.regexFilter(thisRegExpr, 0);
            final TableRowSorter<RelationTableModel> thisSorter = (TableRowSorter<RelationTableModel>) this.theTable.getRowSorter();
            thisSorter.setRowFilter(thisFilter);
        } catch (final java.util.regex.PatternSyntaxException e) {
        }
    }
}
