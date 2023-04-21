package com.yeep.universedesign.ui.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import com.yeep.basis.swing.widget.table.TableWidget;
import com.yeep.basis.swing.widget.table.TableWidgetModel;
import com.yeep.universedesign.model.Association;
import com.yeep.universedesign.ui.Message;

@SuppressWarnings("serial")
public class AssociationsPanel extends JPanel {

    private JPopupMenu popupMenu;

    private JScrollPane scrollPane;

    private TableWidget<Association> associationTableWidget;

    public AssociationsPanel() {
        super();
        loadUI();
    }

    /**
	 * load layout
	 */
    private void loadUI() {
        createWidgets();
        initLayout();
        initPanelAttributes();
    }

    /**
	 * Initialize Panel's attributes 
	 */
    private void initPanelAttributes() {
        setVisible(true);
    }

    /**
	 * initialize Layout
	 */
    private void initLayout() {
        setLayout(new BorderLayout());
        add(this.scrollPane);
    }

    /**
	 * Create Widgets
	 */
    private void createWidgets() {
        createPopupMenu();
        this.associationTableWidget = new TableWidget<Association>();
        this.scrollPane = new JScrollPane(this.associationTableWidget);
        this.associationTableWidget.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Association association = associationTableWidget.getSelected();
                    if (association != null) editAssociation(association);
                }
            }

            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    /**
	 * Render Widgets
	 */
    public void renderWidgets(List<Association> associations) {
        TableWidgetModel<Association> listModel = new TableWidgetModel<Association>(associations, new String[] { Message.getMessage("association.table.column.name"), Message.getMessage("association.table.column.sourceTable"), Message.getMessage("association.table.column.sourceColumn"), Message.getMessage("association.table.column.destTable"), Message.getMessage("association.table.column.destColumn") }, new String[] { "name", "sourceColumn.table.name", "sourceColumn.name", "destColumn.table.name", "destColumn.name" });
        this.associationTableWidget.setListModel(listModel);
    }

    /**
	 * Add the association to association panel
	 * @param association
	 */
    public void addAssociation(Association association) {
        this.associationTableWidget.addRow(association);
    }

    /**
	 * Add the association to association panel
	 * @param association
	 */
    public void deleteAssociation(Association association) {
        ActionHandler.getInstance().removeAssociationFromUniverse(association);
        this.associationTableWidget.removeRow(association);
    }

    /**
	 * Edit the association to association panel
	 * @param association
	 */
    public void editAssociation(Association association) {
        MainFrame.getInstance().openAssociationDialog(association);
    }

    /**
	 * create Popup Menu for ColumnList
	 */
    private void createPopupMenu() {
        this.popupMenu = new JPopupMenu();
        JMenuItem editAssociationMI = new JMenuItem(Message.getMessage("popupMenu.association.edit"));
        editAssociationMI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Association association = associationTableWidget.getSelected();
                if (association != null) editAssociation(association);
            }
        });
        this.popupMenu.add(editAssociationMI);
        JMenuItem deleteAssociationMI = new JMenuItem(Message.getMessage("popupMenu.association.delete"));
        deleteAssociationMI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Association association = associationTableWidget.getSelected();
                if (association != null) deleteAssociation(association);
            }
        });
        this.popupMenu.add(deleteAssociationMI);
    }
}
