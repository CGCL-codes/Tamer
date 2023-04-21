package be.vds.jtbdive.core.view.panel;

import info.clearthought.layout.TableLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import be.vds.jtbdive.core.model.DiveTank;
import be.vds.jtbdive.core.utils.ResourceManager;
import be.vds.jtbdive.core.view.listener.ModificationListener;
import be.vds.jtbdive.core.view.panel.listenable.ModificationListenableJPanel;
import be.vds.jtbdive.core.view.util.LocaleUpdatable;

public class DiveTanksPanel extends ModificationListenableJPanel implements LocaleUpdatable, ModificationListener {

    private static final Dimension BUTTON_DIM = new Dimension(30, 30);

    private JScrollPane tanksScroll;

    private JButton removeTankButton;

    private JPanel diveTanksPanel;

    private DiveTankPanel selectedTankPanel;

    private List<DiveTankPanel> diveTankPanels = new ArrayList<DiveTankPanel>();

    private Window parentWindow;

    public DiveTanksPanel(Window parentWindow) {
        this.parentWindow = parentWindow;
        init();
    }

    private void init() {
        this.setPreferredSize(new Dimension(210, 200));
        double[] cols = { TableLayout.FILL };
        double[] rows = { TableLayout.PREFERRED, 5, TableLayout.FILL };
        TableLayout tl = new TableLayout(cols, rows);
        this.setLayout(tl);
        this.add(createButtonsPanel(), "0, 0");
        this.add(createDiveTanksPanel(), "0, 2");
    }

    private Component createButtonsPanel() {
        JXPanel panel = new JXPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addTankButton = new JButton();
        addTankButton.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addDiveTank(new DiveTank());
            }
        });
        addTankButton.setIcon(ResourceManager.getImageIcon("tankplus24.png"));
        addTankButton.setPreferredSize(BUTTON_DIM);
        addTankButton.setMaximumSize(BUTTON_DIM);
        addTankButton.setContentAreaFilled(false);
        removeTankButton = new JButton(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTankPanel.removeAllModificationListener();
                diveTanksPanel.remove(selectedTankPanel);
                diveTankPanels.remove(selectedTankPanel);
                selectedTankPanel = null;
                removeTankButton.setEnabled(false);
                tanksScroll.validate();
                tanksScroll.repaint();
                notifyModificationListeners(true);
            }
        });
        removeTankButton.setIcon(ResourceManager.getImageIcon("tankmoins24.png"));
        removeTankButton.setPreferredSize(BUTTON_DIM);
        removeTankButton.setMaximumSize(BUTTON_DIM);
        removeTankButton.setContentAreaFilled(false);
        removeTankButton.setEnabled(false);
        panel.add(addTankButton);
        panel.add(removeTankButton);
        return panel;
    }

    private JComponent createDiveTanksPanel() {
        diveTanksPanel = new JPanel();
        diveTanksPanel.setLayout(new VerticalLayout());
        tanksScroll = new JScrollPane(diveTanksPanel);
        tanksScroll.getHorizontalScrollBar().setUnitIncrement(20);
        tanksScroll.addMouseListener(new TankSelectorMouseListener());
        return tanksScroll;
    }

    private void addDiveTank(DiveTank diveTank) {
        DiveTankPanel dtp = new DiveTankPanel(parentWindow);
        dtp.setDiveTank(diveTank);
        dtp.addMouseListener(new TankSelectorMouseListener());
        dtp.addModificationListener(this);
        diveTankPanels.add(dtp);
        diveTanksPanel.add(dtp);
        tanksScroll.validate();
        tanksScroll.repaint();
        notifyModificationListeners(true);
    }

    @Override
    public void updateLocale() {
        for (DiveTankPanel dtp : diveTankPanels) {
            dtp.updateLocale();
        }
    }

    public class TankSelectorMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getComponent().equals(tanksScroll)) {
                if (null != selectedTankPanel) {
                    selectedTankPanel.setBorder(new LineBorder(Color.DARK_GRAY));
                    selectedTankPanel = null;
                    removeTankButton.setEnabled(false);
                }
            } else {
                if (null != selectedTankPanel) {
                    selectedTankPanel.setBorder(new LineBorder(Color.DARK_GRAY));
                }
                DiveTankPanel tp = (DiveTankPanel) e.getComponent();
                tp.setBorder(new LineBorder(Color.RED));
                selectedTankPanel = tp;
                removeTankButton.setEnabled(true);
            }
            removeTankButton.setEnabled(null != selectedTankPanel);
        }
    }

    public List<DiveTank> getDiveTanks() {
        List<DiveTank> result = new ArrayList<DiveTank>();
        for (DiveTankPanel dtp : diveTankPanels) {
            result.add(dtp.getDiveTank());
        }
        return result;
    }

    public void setDiveTanks(List<DiveTank> diveTanks) {
        diveTanksPanel.removeAll();
        diveTankPanels.removeAll(diveTankPanels);
        selectedTankPanel = null;
        for (DiveTank diveTank : diveTanks) {
            addDiveTank(diveTank);
        }
    }

    @Override
    public void isModified(JComponent component, boolean isModified) {
        notifyModificationListeners(isModified);
    }
}
