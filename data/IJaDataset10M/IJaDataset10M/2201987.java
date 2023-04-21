package TangramBase;

import TangramCore.Database;
import TangramCore.Hladina;
import TangramCore.HladinaObrys;
import TangramCore.Obrazec;
import TangramCore.Obrys;
import TangramCore.Skladacka;
import TangramCore.Database.ObrazecFilter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

public class FilterFrame extends JDialog implements ItemListener, DocumentWindow, ActionListener, KeyListener, Database.DatabaseChangeListener {

    static final long serialVersionUID = 24362462L;

    private JMenuItem mniThis = null;

    private int iWindow = -1;

    private static final int ID_FILTEROK = 20, ID_FILTERADD = 30, ID_FILTERINTERSECT = 21, ID_FILTERSUBTRACT = 22, ID_FILTERCANCEL = 23;

    private TangramFrame frmDatabase;

    private Database database;

    Database.ObrazecData od;

    Database.ObrazecFilter of = new Database.ObrazecFilter();

    int showed = -1;

    HladinaObrys ho;

    Obrazec obrNahled;

    Skladacka skl;

    JSlider sldTolerance;

    Vector<String> allKeys, selKeys;

    JTextField txtContaining = new JTextField("");

    JTextField txtFrom = new JTextField("01.01.1970 00:00:00");

    JTextField txtTo = new JTextField("01.01.2070 00:00:00");

    JTree treeKeywords;

    JComboBox cmbFrom, cmbTo;

    JList lstSelKeywords = new JList();

    JButton cmdFilter = new JButton("Filter");

    JButton cmdCancel = new JButton("Cancel");

    JButton cmdIntersect = new JButton("Intersect");

    JButton cmdAdd = new JButton("Add");

    JButton cmdSubtract = new JButton("Subtract");

    JCheckBox chkSimilar = new JCheckBox("apply filter");

    JCheckBox chkContaining = new JCheckBox("apply filter");

    JCheckBox chkNazev = new JCheckBox("search in name", true);

    JCheckBox chkPopis = new JCheckBox("search in description", true);

    JCheckBox chkAutor = new JCheckBox("search in author", true);

    JCheckBox chkKeywords = new JCheckBox("apply filter");

    JCheckBox chkDate = new JCheckBox("apply filter");

    JCheckBox chkFrom = new JCheckBox("from");

    JCheckBox chkTo = new JCheckBox("to");

    JRadioButton optShape = new JRadioButton("shape", true);

    JRadioButton optSolution = new JRadioButton("solution");

    ButtonGroup groupSimilar = new ButtonGroup();

    ActionListener updateDateTo = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            now.setTime(System.currentTimeMillis());
            txtTo.setText(df.format(now));
        }
    };

    ActionListener updateDateFrom = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            now.setTime(System.currentTimeMillis());
            txtFrom.setText(df.format(now));
        }
    };

    public FilterFrame(TangramFrame tf, Database d) {
        frmDatabase = tf;
        database = d;
        skl = database.getActSet();
        od = new Database.ObrazecData(skl, skl.getDilky());
        setTitle();
        ArrayList<Hladina> a = new ArrayList<Hladina>(1);
        ho = new HladinaObrys(od.d, od.o, TangramFrame.vDilky[TangramFrame.iVzhled], TangramFrame.vObrys[TangramFrame.iVzhled], null, Hladina.CENTER, true, new Rectangle(-5, -5, -90, -90));
        a.add(ho);
        obrNahled = new Obrazec(skl, Color.white, a, null, null, null, null);
        obrNahled.setPreferredSize(new Dimension(275, 275));
        treeKeywords = new JTree(frmDatabase.treeModel);
        Font font = txtFrom.getFont();
        JPanel pnlWest = new JPanel(new BorderLayout());
        JPanel pnlSimilar = new JPanel(new BorderLayout());
        pnlSimilar.setBorder(BorderFactory.createTitledBorder("Similar"));
        groupSimilar.add(optShape);
        groupSimilar.add(optSolution);
        JPanel pnlSimilarType = new JPanel(new GridLayout(1, 3));
        chkSimilar.setFont(font);
        optShape.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ho.prepniVzhled(Hladina.VZHLED_OBRYS);
                obrNahled.update();
            }
        });
        optSolution.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ho.prepniVzhled(Hladina.VZHLED_DILKY);
                obrNahled.update();
            }
        });
        pnlSimilarType.add(chkSimilar);
        pnlSimilarType.add(optShape);
        pnlSimilarType.add(optSolution);
        pnlSimilar.add(pnlSimilarType, BorderLayout.NORTH);
        sldTolerance = new JSlider();
        pnlSimilar.add(sldTolerance, BorderLayout.SOUTH);
        pnlSimilar.add(obrNahled);
        pnlWest.add(pnlSimilar, BorderLayout.CENTER);
        JPanel pnlDescr = new JPanel(new GridLayout(2, 1));
        JPanel pnlContaining = new JPanel();
        pnlContaining.setLayout(new GridLayout(5, 1));
        pnlContaining.setBorder(BorderFactory.createTitledBorder("Containing text"));
        chkContaining.setFont(font);
        chkNazev.setFont(font);
        chkPopis.setFont(font);
        chkAutor.setFont(font);
        pnlContaining.add(chkContaining);
        pnlContaining.add(txtContaining);
        pnlContaining.add(chkNazev);
        pnlContaining.add(chkPopis);
        pnlContaining.add(chkAutor);
        pnlDescr.add(pnlContaining);
        JPanel pnlDate = new JPanel();
        pnlDate.setLayout(new GridLayout(5, 1));
        pnlDate.setBorder(BorderFactory.createTitledBorder("Dated"));
        chkDate.setFont(font);
        pnlDate.add(chkDate);
        chkFrom.setFont(font);
        pnlDate.add(chkFrom);
        JPanel pnlFrom = new JPanel(new BorderLayout());
        pnlFrom.add(txtFrom, BorderLayout.CENTER);
        cmbFrom = new JComboBox(cmbItems);
        cmbFrom.setFont(font);
        cmbFrom.addItemListener(this);
        pnlFrom.add(cmbFrom, BorderLayout.EAST);
        pnlDate.add(pnlFrom);
        txtFrom.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DescriptionFrame.validateDate(txtFrom, cmbFrom, false);
            }
        });
        txtFrom.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                DescriptionFrame.validateDate(txtFrom, cmbFrom, false);
            }
        });
        txtTo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DescriptionFrame.validateDate(txtTo, cmbTo, false);
            }
        });
        txtTo.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                DescriptionFrame.validateDate(txtTo, cmbTo, false);
            }
        });
        chkTo.setFont(font);
        pnlDate.add(chkTo);
        JPanel pnlTo = new JPanel(new BorderLayout());
        pnlTo.add(txtTo, BorderLayout.CENTER);
        cmbTo = new JComboBox(cmbItems);
        cmbTo.setFont(font);
        cmbTo.addItemListener(this);
        pnlTo.add(cmbTo, BorderLayout.EAST);
        pnlDate.add(pnlTo);
        pnlDescr.add(pnlDate);
        pnlWest.add(pnlDescr, BorderLayout.SOUTH);
        selKeys = new Vector<String>();
        treeKeywords.setBorder(txtFrom.getBorder());
        MouseListener mlSel = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    TreePath treePath = treeKeywords.getPathForLocation(e.getPoint().x, e.getPoint().y);
                    if (treePath == null) return;
                    Object obj[] = treePath.getPath();
                    for (int i = 1; i < obj.length; i++) {
                        String s = ((DefaultMutableTreeNode) obj[i]).toString();
                        if (!selKeys.contains(s)) selKeys.add(s);
                    }
                    Collections.<String>sort(selKeys);
                    lstSelKeywords.setListData(selKeys);
                }
            }
        };
        treeKeywords.addMouseListener(mlSel);
        JScrollPane scrAll = new JScrollPane(treeKeywords);
        scrAll.setBorder(BorderFactory.createTitledBorder("Available keywords"));
        scrAll.setPreferredSize(new Dimension(200, 200));
        lstSelKeywords.setFont(font);
        lstSelKeywords.setBorder(txtFrom.getBorder());
        MouseListener mlDesel = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = lstSelKeywords.locationToIndex(e.getPoint());
                    if (index > -1) {
                        selKeys.remove(index);
                        lstSelKeywords.setListData(selKeys);
                    }
                }
            }
        };
        lstSelKeywords.addMouseListener(mlDesel);
        JPanel pnlSel = new JPanel(new BorderLayout());
        pnlSel.setBorder(BorderFactory.createTitledBorder("Containing keywords"));
        JScrollPane scrSel = new JScrollPane(lstSelKeywords);
        scrSel.setPreferredSize(new Dimension(150, 200));
        scrSel.setBorder(BorderFactory.createEmptyBorder());
        chkKeywords.setFont(font);
        pnlSel.add(chkKeywords, BorderLayout.NORTH);
        pnlSel.add(scrSel, BorderLayout.CENTER);
        JPanel pnlSouth = new JPanel(new FlowLayout());
        cmdFilter.setActionCommand(String.valueOf(ID_FILTEROK));
        cmdFilter.addActionListener(this);
        cmdCancel.setActionCommand(String.valueOf(ID_FILTERCANCEL));
        cmdCancel.addActionListener(this);
        cmdIntersect.setActionCommand(String.valueOf(ID_FILTERINTERSECT));
        cmdIntersect.addActionListener(this);
        cmdAdd.setActionCommand(String.valueOf(ID_FILTERADD));
        cmdAdd.addActionListener(this);
        cmdSubtract.setActionCommand(String.valueOf(ID_FILTERSUBTRACT));
        cmdSubtract.addActionListener(this);
        pnlSouth.add(cmdFilter);
        pnlSouth.add(cmdIntersect);
        pnlSouth.add(cmdAdd);
        pnlSouth.add(cmdSubtract);
        pnlSouth.add(cmdCancel);
        Container pane = getContentPane();
        pane.add(pnlWest, BorderLayout.WEST);
        pane.add(scrAll, BorderLayout.CENTER);
        pane.add(pnlSel, BorderLayout.EAST);
        pane.add(pnlSouth, BorderLayout.SOUTH);
        int w = 700, h = 700;
        setSize(w, h);
        TangramFrame.centerOnScreen(this);
        chkSimilar.addKeyListener(this);
        optSolution.addKeyListener(this);
        optShape.addKeyListener(this);
        obrNahled.addKeyListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                cmdCancel.doClick();
            }
        });
    }

    public void show(int i, Database.ObrazecData odNew) {
        showed = i;
        if (odNew == null) odNew = od;
        if (i > -1) odNew = database.getObrazecData(showed);
        if (odNew != null) od = odNew;
        if (od == null) od = database.getDefaultObrazec();
        ho.update(od.d, od.o);
        setTitle();
        obrNahled.update();
    }

    public boolean apply(Database.ObrazecFilter of) {
        Date dateFrom = DescriptionFrame.validateDate(txtFrom, cmbFrom, true);
        Date dateTo = DescriptionFrame.validateDate(txtTo, cmbTo, true);
        if (dateFrom == null || dateTo == null) return false;
        of.skladacka = skl;
        of.filterSimilar = chkSimilar.isSelected() ? (optShape.isSelected() ? 1 : 2) : 0;
        if (of.filterSimilar == 2) of.dSimilar = skl.getDilky(od.d);
        of.oSimilar = od.o;
        double tolval = (double) sldTolerance.getValue() / 100;
        of.tol = Obrys.TOLERANCE_AREA2 + tolval * (5.0 - Obrys.TOLERANCE_AREA2);
        of.tola = Obrys.TOLERANCE_UHEL_RAD + tolval * (Math.toRadians(skl.ROTACE_KROK));
        of.filterContaining = chkContaining.isSelected();
        of.sContaining = txtContaining.getText().toLowerCase().split(" ");
        of.searchAutor = chkAutor.isSelected();
        of.searchNazev = chkNazev.isSelected();
        of.searchPopis = chkPopis.isSelected();
        of.filterDated = chkDate.isSelected();
        of.datedFrom = chkFrom.isSelected();
        of.datedTo = chkTo.isSelected();
        of.dateFrom = dateFrom;
        of.dateTo = dateTo;
        of.filterKeywords = chkKeywords.isSelected();
        of.sKeywords = new Vector<String>(selKeys);
        return true;
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource().equals(cmbTo)) DescriptionFrame.setComboItem(e, updateDateTo, txtTo); else if (e.getSource().equals(cmbFrom)) DescriptionFrame.setComboItem(e, updateDateFrom, txtFrom);
    }

    public void setVisible(boolean jak) {
        if (jak == false) {
            if (cmbFrom.getSelectedIndex() == 2) DescriptionFrame.removeTimerListener(updateDateFrom);
            if (cmbTo.getSelectedIndex() == 2) DescriptionFrame.removeTimerListener(updateDateTo);
        } else {
            if (cmbFrom.getSelectedIndex() == 2) DescriptionFrame.addTimerListener(updateDateFrom);
            if (cmbTo.getSelectedIndex() == 2) DescriptionFrame.addTimerListener(updateDateTo);
        }
        super.setVisible(jak);
    }

    private boolean filter(Database.ObrazecFilter of) {
        if (apply(of)) {
            setEnabled(false);
            frmDatabase.filter(of);
            setEnabled(true);
            showDocument(false);
            return true;
        }
        return false;
    }

    public void actionPerformed(ActionEvent e) {
        int actionID = Integer.parseInt(e.getActionCommand());
        switch(actionID) {
            case ID_FILTEROK:
                of.filterType = ObrazecFilter.FILTER_NEW;
                filter(of);
                break;
            case ID_FILTERINTERSECT:
                of.filterType = ObrazecFilter.FILTER_INTERSECT;
                filter(of);
                break;
            case ID_FILTERADD:
                of.filterType = ObrazecFilter.FILTER_ADD;
                filter(of);
                break;
            case ID_FILTERSUBTRACT:
                of.filterType = ObrazecFilter.FILTER_SUBTRACT;
                filter(of);
                break;
            case ID_FILTERCANCEL:
                showDocument(false);
                break;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_PAGE_DOWN:
                showNext();
                break;
            case KeyEvent.VK_PAGE_UP:
                showPrev();
                break;
        }
    }

    private void showNext() {
        if (showed < 0) {
            show(0, null);
            return;
        }
        if (showed < 0 || showed > database.getSize() - 2) return;
        show(showed + 1, null);
    }

    private void showPrev() {
        if (showed < 1) return;
        show(showed - 1, null);
    }

    private void setTitle() {
        super.setTitle("Filter figures" + sDash + skl.nazev + sLFigureBracket + od.nazev + sRFigureBracket);
        setMenuItemText();
    }

    private void setMenuItemText() {
        if (mniThis != null) mniThis.setText(sLNumberBracket + iWindow + sRNumberBracket + getTitle());
    }

    public void setMenuItem(int i, JMenuItem mni) {
        iWindow = i;
        mniThis = mni;
        setMenuItemText();
    }

    public void restoreState() {
        toFront();
    }

    public boolean showDocument(boolean jak) {
        boolean ret = jak ? frmDatabase.windowOpened(this) : frmDatabase.windowClosed(this);
        setVisible(jak);
        if (jak) toFront(); else dispose();
        return ret;
    }

    public boolean databaseChanged(int what, int i) {
        if (showed < 0) return true;
        boolean myChange = i < 0 || i == showed;
        if (i < 0) showed = database.getIndex(od);
        if (myChange && (what & Database.CHANGED_OBRAZEC) != 0) show(showed, null);
        setTitle();
        return true;
    }

    public boolean hasChanged() {
        return false;
    }
}
