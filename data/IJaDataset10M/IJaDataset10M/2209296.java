package com.yeep.objanalyser.universedesigner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.yeep.basis.swing.layout.XYConstraints;
import com.yeep.basis.swing.layout.XYLayout;
import com.yeep.basis.swing.util.SwingUtility;
import com.yeep.basis.swing.widget.list.ListWidget;
import com.yeep.basis.swing.widget.list.ListWidgetModel;
import com.yeep.objanalyser.common.model.Association;
import com.yeep.objanalyser.common.model.ModelFactory;
import com.yeep.objanalyser.common.model.UnvColumn;
import com.yeep.objanalyser.common.model.UnvTable;
import com.yeep.objanalyser.common.util.Context;
import com.yeep.objanalyser.common.util.Message;
import edu.emory.mathcs.backport.java.util.Collections;

@SuppressWarnings("serial")
public class UnvTablePanel extends JPanel {

    private JPopupMenu columnPopupMenu;

    private JMenuItem createAssociationMI;

    private JMenuItem editAliasNameMI;

    private UnvTable table;

    private UnvColumn selectedColumn;

    private static final int P_WIDTH = 250;

    private static final int P_HEIGHT = 171;

    private Point origin = new Point();

    private JLabel deleteLabel;

    private JLabel titleLabel;

    private ListWidget<UnvColumn> columnListWidget;

    private JScrollPane columnListScrollPane;

    protected UnvTablePanel(UnvTable table) {
        super();
        this.table = table;
        loadUI();
    }

    /**
	 * Load UI
	 */
    private void loadUI() {
        createWidgets();
        initLayout();
        renderWidgets();
        initPanelAttributes();
    }

    /**
	 * Initialize Panel's attributes 
	 */
    private void initPanelAttributes() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setSize(P_WIDTH, P_HEIGHT);
        PanelDragListener dragListener = new PanelDragListener();
        addMouseListener(dragListener);
        addMouseMotionListener(dragListener);
        setLocation(table.getPosX(), table.getPosY());
        setVisible(true);
    }

    /**
	 * initialize Layout
	 */
    private void initLayout() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new XYLayout());
        panel.add(this.titleLabel, new XYConstraints(0, 0, 230, 20));
        panel.add(this.deleteLabel, new XYConstraints(230, 0, 20, 20));
        add(panel, BorderLayout.NORTH);
        add(this.columnListScrollPane, BorderLayout.CENTER);
    }

    /**
	 * Create widgets
	 */
    private void createWidgets() {
        ImageIcon deleteIcon = new ImageIcon(((URLClassLoader) getClass().getClassLoader()).getResource("com/yeep/objanalyser/common/res/NodeDelete.GIF"));
        this.deleteLabel = new JLabel(deleteIcon);
        this.deleteLabel.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                deleteUnvTable();
            }
        });
        this.titleLabel = new JLabel(this.table.getName() + " (" + this.table.getAliasName() + ")");
        this.titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buildColumnPopupMenu();
        this.columnListWidget = new ListWidget<UnvColumn>();
        this.columnListScrollPane = new JScrollPane(this.columnListWidget);
        this.columnListWidget.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                MainFrame.getInstance().moveTablePanelToFront(UnvTablePanel.this);
                showPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    SwingUtility.setUIComponentVisible(createAssociationMI, columnListWidget.getSelected() != null);
                    columnPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        this.columnListWidget.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if ((!e.getValueIsAdjusting()) || (e.getFirstIndex() == -1)) return;
                selectedColumn = columnListWidget.getSelected();
            }
        });
    }

    /**
	 * create Popup Menu for ColumnList
	 */
    private void buildColumnPopupMenu() {
        this.columnPopupMenu = new JPopupMenu();
        this.createAssociationMI = new JMenuItem(Message.getMessage("ud.menu.association.create"));
        createAssociationMI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                createAssociation();
            }
        });
        this.columnPopupMenu.add(this.createAssociationMI);
        this.editAliasNameMI = new JMenuItem(Message.getMessage("ud.menu.table.aliasName.edit"));
        this.editAliasNameMI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String associationName = (String) JOptionPane.showInputDialog(null, Message.getMessage("ud.unvTable.msg.editAliasName"), Message.getMessage("ud.unvTable.aliasName"), JOptionPane.QUESTION_MESSAGE, null, null, table.getAliasName());
                table.setAliasName(associationName == null ? "" : associationName);
                titleLabel.setText(table.getName() + " (" + table.getAliasName() + ")");
            }
        });
        this.columnPopupMenu.add(this.editAliasNameMI);
    }

    /**
	 * Render Widgets
	 */
    private void renderWidgets() {
        if (this.table == null) return;
        if (this.columnListWidget != null) {
            List<UnvColumn> columns = new ArrayList<UnvColumn>(this.table.getColumns());
            Collections.sort(columns);
            ListWidgetModel<UnvColumn> listModel = new ListWidgetModel<UnvColumn>(columns, new String[] { "name" });
            this.columnListWidget.setListModel(listModel);
        }
    }

    /**
	 * Create Association
	 */
    private void createAssociation() {
        String associationName = (String) JOptionPane.showInputDialog(null, Message.getMessage("ud.association.msg.inputAssociationName"), Message.getMessage("ud.association.msg.createAssociation"), JOptionPane.QUESTION_MESSAGE, null, null, "");
        Association association = ModelFactory.createAssociation();
        association.setName(associationName);
        association.setSourceColumn(this.selectedColumn);
        MainFrame.getInstance().openAssociationDialog(association);
    }

    private void deleteUnvTable() {
        ActionHandler.getInstance().deleteUnvTable(this.table);
        MainFrame.getInstance().removeTablePanelFromContainerPanel(this);
        MainFrame.getInstance().renderAssociationPanel(Context.getUniverse().getAvailableAssociations());
    }

    /**
	 * Listener for handle dragging
	 * @author Yiro
	 */
    class PanelDragListener extends MouseAdapter {

        boolean dragFlag = false;

        boolean isResize = false;

        public void mousePressed(MouseEvent e) {
            if (e == null) return;
            origin.x = e.getX();
            origin.y = e.getY();
            MainFrame.getInstance().moveTablePanelToFront(UnvTablePanel.this);
        }

        public void mouseDragged(MouseEvent e) {
            if (e == null) return;
            if (isResize) {
                int originWidth = UnvTablePanel.this.getWidth();
                int originHeight = UnvTablePanel.this.getHeight();
                int offset = e.getY() - originHeight;
                UnvTablePanel.this.setSize(originWidth, (originHeight + offset));
                int originListWidth = UnvTablePanel.this.columnListScrollPane.getWidth();
                int originListHeight = UnvTablePanel.this.columnListScrollPane.getHeight();
                UnvTablePanel.this.columnListScrollPane.setPreferredSize(new Dimension(originListWidth, (originListHeight + offset)));
                UnvTablePanel.this.revalidate();
                UnvTablePanel.this.repaint();
            } else {
                Point locationPoint = UnvTablePanel.this.getLocation();
                int posX = locationPoint.x + e.getX() - origin.x;
                int posY = locationPoint.y + e.getY() - origin.y;
                UnvTablePanel.this.setLocation(posX, posY);
                UnvTablePanel.this.table.setPosX(posX);
                UnvTablePanel.this.table.setPosY(posY);
            }
        }

        public void mouseMoved(MouseEvent e) {
            if (e == null) return;
            isResize = false;
            if (((UnvTablePanel.this.getHeight() - e.getY()) < 5) && ((UnvTablePanel.this.getHeight() - e.getY()) > -5)) {
                UnvTablePanel.this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                isResize = true;
            } else {
                UnvTablePanel.this.setCursor(Cursor.getDefaultCursor());
            }
        }

        public void mouseExited(MouseEvent e) {
            UnvTablePanel.this.setCursor(Cursor.getDefaultCursor());
        }

        public void mouseReleased(MouseEvent e) {
            UnvTablePanel.this.setCursor(Cursor.getDefaultCursor());
        }
    }
}
