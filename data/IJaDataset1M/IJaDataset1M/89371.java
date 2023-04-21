package researchgrants.parts.pi;

import researchgrants.parts.pi.filters.PisListFilters;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import researchgrants.utils.MyDialog;

/**
 * A view class for displaying a list of grant requests (PiList).
 * @author  ohad
 */
public class PisListView extends MyDialog {

    Window parent;

    JPanel pnlPisList = new JPanel();

    PisList originalList;

    PisList list;

    JTextField freeTextFilter;

    /** Creates new form GrantRequestsListView */
    public PisListView(Window parent, PisList list, String title) {
        super(parent);
        this.list = list;
        this.parent = parent;
        setTitle(title);
        initComponents();
        this.originalList = list;
        reloadOriginalList();
        JPanel pnlFilters = new JPanel(new BorderLayout(0, 0));
        splitContent.setTopComponent(pnlFilters);
        JLabel lblFiltersCaption = new JLabel("Filters");
        lblFiltersCaption.setHorizontalAlignment(0);
        MyDialog.changeToTitleLabel(lblFiltersCaption);
        pnlFilters.add(lblFiltersCaption, BorderLayout.NORTH);
        JPanel pnlFilters2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlFilters.add(pnlFilters2, BorderLayout.CENTER);
        pnlFilters2.setMinimumSize(new Dimension(0, 0));
        JPanel pnlFilters3 = new JPanel(new GridBagLayout());
        pnlFilters2.add(pnlFilters3);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        {
            JLabel lblCaption = new JLabel("Free text search: ");
            pnlFilters3.add(lblCaption, gridBagConstraints);
            gridBagConstraints.gridx++;
            freeTextFilter = new JTextField("");
            freeTextFilter.setColumns(10);
            freeTextFilter.addKeyListener(new KeyListener() {

                public void keyTyped(KeyEvent e) {
                    redoFilters();
                }

                public void keyPressed(KeyEvent e) {
                    redoFilters();
                }

                public void keyReleased(KeyEvent e) {
                    redoFilters();
                }
            });
            pnlFilters3.add(freeTextFilter, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy++;
        }
        JPanel pnlListContent = new JPanel(new BorderLayout(0, 0));
        splitContent.setBottomComponent(pnlListContent);
        JLabel lblListCaption = new JLabel("Records");
        lblListCaption.setHorizontalAlignment(0);
        MyDialog.changeToTitleLabel(lblListCaption);
        pnlListContent.add(lblListCaption, BorderLayout.NORTH);
        JPanel pnlListContent2 = new JPanel(new BorderLayout(0, 0));
        pnlPisList = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlListContent2.add(pnlPisList, BorderLayout.NORTH);
        pnlListContent.add(pnlListContent2, BorderLayout.CENTER);
        pnlPisList.setMinimumSize(new Dimension(0, 0));
        redoFilters();
    }

    private void reloadOriginalList() {
        try {
            list = (PisList) originalList.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(PisListView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void redoFilters() {
        reloadOriginalList();
        if (!freeTextFilter.getText().equals("")) {
            PisListFilters.filterByFreeText(list, freeTextFilter.getText());
        }
        initDisplay();
    }

    private void initDisplay() {
        JLabel lblTemp = new JLabel();
        Font headerFont = new Font(lblTemp.getFont().getName(), Font.BOLD, lblTemp.getFont().getSize());
        Font regularFont = new Font(lblTemp.getFont().getName(), Font.PLAIN, lblTemp.getFont().getSize());
        pnlPisList.removeAll();
        GridBagLayout gridBagLayout = new GridBagLayout();
        pnlPisList.setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        lblTemp = new JLabel("ID", SwingConstants.CENTER);
        lblTemp.setFont(headerFont);
        pnlPisList.add(lblTemp, gridBagConstraints);
        gridBagConstraints.gridx++;
        lblTemp = new JLabel("Name", SwingConstants.CENTER);
        lblTemp.setFont(headerFont);
        pnlPisList.add(lblTemp, gridBagConstraints);
        gridBagConstraints.gridx++;
        lblTemp = new JLabel("Phone", SwingConstants.CENTER);
        lblTemp.setFont(headerFont);
        pnlPisList.add(lblTemp, gridBagConstraints);
        gridBagConstraints.gridx++;
        lblTemp = new JLabel("Department", SwingConstants.CENTER);
        lblTemp.setFont(headerFont);
        pnlPisList.add(lblTemp, gridBagConstraints);
        gridBagConstraints.gridx++;
        lblTemp = new JLabel("Action", SwingConstants.CENTER);
        lblTemp.setFont(headerFont);
        pnlPisList.add(lblTemp, gridBagConstraints);
        int columnCount = gridBagConstraints.gridx;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy++;
        final int MAX_LINES = 20;
        Iterator<Pi> itPis = list.iterator();
        while (itPis.hasNext() && gridBagConstraints.gridy <= MAX_LINES) {
            Pi pi = itPis.next();
            lblTemp = new JLabel(Integer.toString(pi.getId()), SwingConstants.LEFT);
            lblTemp.setFont(regularFont);
            pnlPisList.add(lblTemp, gridBagConstraints);
            gridBagConstraints.gridx++;
            lblTemp = new JLabel(pi.getCurrentName(), SwingConstants.LEFT);
            lblTemp.setFont(regularFont);
            pnlPisList.add(lblTemp, gridBagConstraints);
            gridBagConstraints.gridx++;
            lblTemp = new JLabel(pi.getCurrentTelephone(), SwingConstants.LEFT);
            lblTemp.setFont(regularFont);
            pnlPisList.add(lblTemp, gridBagConstraints);
            gridBagConstraints.gridx++;
            lblTemp = new JLabel(pi.getCurrentDepartment().toListValue(), SwingConstants.LEFT);
            lblTemp.setFont(regularFont);
            pnlPisList.add(lblTemp, gridBagConstraints);
            gridBagConstraints.gridx++;
            JButton btn = new JButton("View");
            btn.addActionListener(new ActionListenerImpl(pi, this));
            pnlPisList.add(btn, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy++;
        }
        if (itPis.hasNext()) {
            gridBagConstraints.gridwidth = columnCount;
            pnlPisList.add(new JLabel("--- More records exist than shown ---"), gridBagConstraints);
        }
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        splitContent = new javax.swing.JSplitPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form");
        splitContent.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitContent.setName("splitContent");
        getContentPane().add(splitContent, java.awt.BorderLayout.CENTER);
        pack();
    }

    private static class ActionListenerImpl implements ActionListener {

        private final Pi pi;

        private final PisListView parent;

        public ActionListenerImpl(Pi pi, PisListView parent) {
            this.pi = pi;
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e) {
            pi.show(parent);
            parent.initDisplay();
        }
    }

    private javax.swing.JSplitPane splitContent;
}
