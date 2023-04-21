package ro.cuzma.books.ui.author;

import ro.cuzma.books.AuthorData;
import ro.cuzma.event.CustomEvent;
import ro.cuzma.event.CustomEventDispatcher;
import ro.cuzma.event.CustomEventListener;
import ro.cuzma.ui.TableWithColumnModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class AuthorPanelTable extends javax.swing.JPanel implements CustomEventListener {

    public static final String CE_AUTHOR_TYPE = "AUTHOR";

    public static final String CE_AUTHOR_VALUE_EDIT = "EDIT";

    {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JTextField jTextFieldFirst;

    private JButton jButtonApply;

    private JPanel jPanelNorth;

    private AuthorTable jTableAuthors;

    private JScrollPane jScrollPaneTable;

    private JLabel jLabelFirst;

    private JTextField jTextName;

    private JButton jButtonEdit;

    private JTextArea jTextAreaDetails = new JTextArea();

    ;

    private JPanel jPanelText;

    private JLabel jLabelName;

    private JTextField jTextFieldMiddle;

    private JLabel jLabelMiddle;

    private AuthorData rows;

    private void initialize() {
        BorderLayout thisLayout = new BorderLayout();
        this.setLayout(thisLayout);
        this.setSize(new Dimension(203, 59));
    }

    public AuthorPanelTable(AuthorData rows) {
        super();
        this.rows = rows;
        initialize();
        initGUI();
        jTableAuthors = new AuthorTable(rows, jTextAreaDetails);
        jScrollPaneTable.setViewportView(jTableAuthors);
        if (((TableWithColumnModel) jTableAuthors.getModel()).getEdit()) {
            jButtonEdit.setText("Unedit");
        } else {
            jButtonEdit.setText("Edit");
        }
        CustomEventDispatcher.getCustomEventDispatcher().addNewListener(this);
    }

    private void initGUI() {
        jScrollPaneTable = new JScrollPane();
        jScrollPaneTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(jScrollPaneTable, BorderLayout.CENTER);
        jScrollPaneTable.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
        {
        }
        {
            jPanelText = new JPanel();
            GridLayout jPanelTextLayout = new GridLayout(1, 1);
            jPanelTextLayout.setColumns(1);
            jPanelTextLayout.setHgap(5);
            jPanelTextLayout.setVgap(5);
            jPanelText.setLayout(jPanelTextLayout);
            this.add(jPanelText, BorderLayout.SOUTH);
            jPanelText.setMinimumSize(new java.awt.Dimension(10, 50));
            {
                jPanelText.add(jTextAreaDetails);
                jTextAreaDetails.setFocusCycleRoot(true);
                jTextAreaDetails.setMinimumSize(new java.awt.Dimension(30, 16));
                jTextAreaDetails.setPreferredSize(new java.awt.Dimension(643, 59));
                jTextAreaDetails.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
            }
        }
        {
            jPanelNorth = new JPanel();
            this.add(jPanelNorth, BorderLayout.NORTH);
            GridBagLayout jPanelNorthLayout = new GridBagLayout();
            jPanelNorthLayout.rowWeights = new double[] { 0.1, 0.1 };
            jPanelNorthLayout.rowHeights = new int[] { 7, 7 };
            jPanelNorthLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1 };
            jPanelNorthLayout.columnWidths = new int[] { 96, 32, 70, 35, 76, 27, 64, 7 };
            jPanelNorth.setLayout(jPanelNorthLayout);
            jPanelNorth.setPreferredSize(new java.awt.Dimension(643, 47));
            {
                jButtonApply = new JButton();
                jPanelNorth.add(jButtonApply, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0));
                jButtonApply.setText("Apply");
                jButtonApply.setSize(80, 23);
                jButtonApply.setPreferredSize(new java.awt.Dimension(80, 23));
                jButtonApply.addMouseListener(new MouseAdapter() {

                    public void mouseReleased(MouseEvent evt) {
                        jButtonApplyMouseReleased(evt);
                    }
                });
            }
            {
                jTextFieldFirst = new JTextField();
                jPanelNorth.add(jTextFieldFirst, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jTextFieldFirst.setPreferredSize(new java.awt.Dimension(70, 20));
                jTextFieldFirst.setSize(70, 20);
                jTextFieldFirst.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent evt) {
                        jKeyReleased(evt);
                    }
                });
            }
            {
                jLabelFirst = new JLabel();
                jPanelNorth.add(jLabelFirst, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabelFirst.setText("First");
                jLabelFirst.setInheritsPopupMenu(false);
            }
            {
                jLabelMiddle = new JLabel();
                jPanelNorth.add(jLabelMiddle, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabelMiddle.setText("Middle");
            }
            {
                jTextFieldMiddle = new JTextField();
                jPanelNorth.add(jTextFieldMiddle, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jTextFieldMiddle.setPreferredSize(new java.awt.Dimension(70, 20));
                jTextFieldMiddle.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent evt) {
                        jKeyReleased(evt);
                    }
                });
            }
            {
                jLabelName = new JLabel();
                jPanelNorth.add(jLabelName, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabelName.setText("Last");
            }
            {
                jTextName = new JTextField();
                jPanelNorth.add(jTextName, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jTextName.setPreferredSize(new java.awt.Dimension(70, 20));
                jTextName.setDragEnabled(true);
                jTextName.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent evt) {
                        jKeyReleased(evt);
                    }
                });
            }
            {
                jButtonEdit = new JButton();
                jPanelNorth.add(jButtonEdit, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jButtonEdit.setText("Edit");
                jButtonEdit.setFocusCycleRoot(true);
                jButtonEdit.setPreferredSize(new java.awt.Dimension(80, 23));
                jButtonEdit.setSize(80, 23);
                jButtonEdit.addMouseListener(new MouseAdapter() {

                    public void mouseReleased(MouseEvent evt) {
                        jButtonEditMouseReleased(evt);
                    }
                });
            }
        }
        this.setFocusCycleRoot(false);
        this.setPreferredSize(new java.awt.Dimension(864, 498));
    }

    public void showTable() {
        this.jTableAuthors.setNewData(this.jTableAuthors.reloadData());
        this.jTableAuthors.revalidate();
        this.jTableAuthors.repaint();
    }

    private void filterData() {
        rows.setFilter(jTextFieldFirst.getText(), jTextFieldMiddle.getText(), jTextName.getText());
        this.jTableAuthors.setNewData(rows.getSortedFilteredData());
        this.jScrollPaneTable.revalidate();
        this.jScrollPaneTable.repaint();
    }

    private void jButtonApplyMouseReleased(MouseEvent evt) {
        filterData();
    }

    private void jKeyReleased(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterData();
        }
    }

    public void setUnsetEdit() {
        if (((TableWithColumnModel) jTableAuthors.getModel()).setUnsetEdit()) {
            jButtonEdit.setText("Unedit");
        } else {
            jButtonEdit.setText("Edit");
        }
        jTableAuthors.revalidate();
        jTableAuthors.repaint();
        this.jScrollPaneTable.revalidate();
        this.jScrollPaneTable.repaint();
    }

    private void jButtonEditMouseReleased(MouseEvent evt) {
        setUnsetEdit();
    }

    /**
	 * @return the rows
	 * @uml.property name="rows"
	 */
    public AuthorData getRows() {
        return rows;
    }

    /**
	 * @param rows
	 *            the rows to set
	 * @uml.property name="rows"
	 */
    public void setRows(AuthorData rows) {
        this.rows = rows;
    }

    public AuthorTable getTableAuthors() {
        return jTableAuthors;
    }

    public void handleCustomEvent(CustomEvent ce) {
        if (ce.getEventType().equals(AuthorPanelTable.CE_AUTHOR_TYPE)) {
            if (ce.getEventValue().equals(AuthorPanelTable.CE_AUTHOR_VALUE_EDIT)) {
                if (((TableWithColumnModel) jTableAuthors.getModel()).getEdit()) {
                    jButtonEdit.setText("Unedit");
                } else {
                    jButtonEdit.setText("Edit");
                }
            }
        }
    }

    public AuthorTable getJTableAuthors() {
        return jTableAuthors;
    }
}
