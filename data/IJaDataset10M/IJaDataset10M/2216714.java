package org.objectstyle.cayenne.modeler.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import org.objectstyle.cayenne.CayenneRuntimeException;
import org.objectstyle.cayenne.map.DbEntity;
import org.objectstyle.cayenne.map.DbJoin;
import org.objectstyle.cayenne.map.DbRelationship;
import org.objectstyle.cayenne.map.Entity;
import org.objectstyle.cayenne.map.Relationship;
import org.objectstyle.cayenne.map.event.MapEvent;
import org.objectstyle.cayenne.map.event.RelationshipEvent;
import org.objectstyle.cayenne.modeler.Application;
import org.objectstyle.cayenne.modeler.util.CayenneDialog;
import org.objectstyle.cayenne.modeler.util.CayenneTable;
import org.objectstyle.cayenne.modeler.util.CayenneWidgetFactory;
import org.objectstyle.cayenne.modeler.util.ModelerUtil;
import org.objectstyle.cayenne.modeler.util.PanelFactory;
import org.objectstyle.cayenne.modeler.util.ProjectUtil;
import org.objectstyle.cayenne.project.NamedObjectFactory;
import org.objectstyle.cayenne.util.Util;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Editor of DbRelationship joins.
 */
public class ResolveDbRelationshipDialog extends CayenneDialog {

    protected DbRelationship relationship;

    protected DbRelationship reverseRelationship;

    protected JTextField name;

    protected JTextField reverseName;

    protected CayenneTable table;

    protected JButton addButton;

    protected JButton removeButton;

    protected JButton saveButton;

    protected JButton cancelButton;

    private boolean cancelPressed;

    public ResolveDbRelationshipDialog(DbRelationship relationship) {
        super(Application.getFrame(), "", true);
        initView();
        initController();
        initWithModel(relationship);
        this.pack();
        this.centerWindow();
    }

    /**
     * Creates graphical components.
     */
    private void initView() {
        name = new JTextField(25);
        reverseName = new JTextField(25);
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        saveButton = new JButton("Done");
        cancelButton = new JButton("Cancel");
        table = new AttributeTable();
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getContentPane().setLayout(new BorderLayout());
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(new FormLayout("right:max(50dlu;pref), 3dlu, fill:min(150dlu;pref), 3dlu, fill:min(150dlu;pref)", "p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, top:14dlu, 3dlu, top:p:grow"));
        builder.setDefaultDialogBorder();
        builder.addSeparator("DbRelationship Information", cc.xywh(1, 1, 5, 1));
        builder.addLabel("Relationship:", cc.xy(1, 3));
        builder.add(name, cc.xywh(3, 3, 1, 1));
        builder.addLabel("Reverse Relationship", cc.xy(1, 5));
        builder.add(reverseName, cc.xywh(3, 5, 1, 1));
        builder.addSeparator("Joins", cc.xywh(1, 7, 5, 1));
        builder.add(new JScrollPane(table), cc.xywh(1, 9, 3, 3, "fill, fill"));
        builder.add(addButton, cc.xywh(5, 9, 1, 1));
        builder.add(removeButton, cc.xywh(5, 11, 1, 1));
        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        getContentPane().add(PanelFactory.createButtonPanel(new JButton[] { saveButton, cancelButton }), BorderLayout.SOUTH);
    }

    private void initWithModel(DbRelationship aRelationship) {
        if (aRelationship.getSourceEntity() == null) {
            throw new CayenneRuntimeException("Null source entity: " + aRelationship);
        }
        if (aRelationship.getTargetEntity() == null) {
            throw new CayenneRuntimeException("Null target entity: " + aRelationship);
        }
        if (aRelationship.getSourceEntity().getDataMap() == null) {
            throw new CayenneRuntimeException("Null DataMap: " + aRelationship.getSourceEntity());
        }
        relationship = aRelationship;
        reverseRelationship = relationship.getReverseRelationship();
        setTitle("DbRelationship Info: " + relationship.getSourceEntity().getName() + " to " + relationship.getTargetEntityName());
        table.setModel(new DbJoinTableModel(relationship, getMediator(), this, true));
        TableColumn sourceColumn = table.getColumnModel().getColumn(DbJoinTableModel.SOURCE);
        sourceColumn.setMinWidth(150);
        JComboBox comboBox = CayenneWidgetFactory.createComboBox(ModelerUtil.getDbAttributeNames(getMediator(), (DbEntity) relationship.getSourceEntity()), true);
        comboBox.setEditable(false);
        sourceColumn.setCellEditor(new DefaultCellEditor(comboBox));
        TableColumn targetColumn = table.getColumnModel().getColumn(DbJoinTableModel.TARGET);
        targetColumn.setMinWidth(150);
        comboBox = CayenneWidgetFactory.createComboBox(ModelerUtil.getDbAttributeNames(getMediator(), (DbEntity) relationship.getTargetEntity()), true);
        comboBox.setEditable(false);
        targetColumn.setCellEditor(new DefaultCellEditor(comboBox));
        if (reverseRelationship != null) {
            reverseName.setText(reverseRelationship.getName());
        }
        name.setText(relationship.getName());
    }

    private void initController() {
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DbJoinTableModel model = (DbJoinTableModel) table.getModel();
                model.addRow(new DbJoin(relationship));
                table.select(model.getRowCount() - 1);
            }
        });
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DbJoinTableModel model = (DbJoinTableModel) table.getModel();
                stopEditing();
                int row = table.getSelectedRow();
                model.removeRow(model.getJoin(row));
            }
        });
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelPressed = false;
                save();
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelPressed = true;
                setVisible(false);
            }
        });
    }

    public boolean isCancelPressed() {
        return cancelPressed;
    }

    private void stopEditing() {
        int col_index = table.getEditingColumn();
        if (col_index >= 0) {
            TableColumn col = table.getColumnModel().getColumn(col_index);
            col.getCellEditor().stopCellEditing();
        }
    }

    private void save() {
        String sourceEntityName = name.getText();
        if (sourceEntityName.length() == 0) {
            sourceEntityName = null;
        }
        if (sourceEntityName == null) {
            sourceEntityName = NamedObjectFactory.createName(DbRelationship.class, relationship.getSourceEntity());
        }
        if (!validateName(relationship.getSourceEntity(), relationship, sourceEntityName)) {
            return;
        }
        String targetEntityName = reverseName.getText().trim();
        if (targetEntityName.length() == 0) {
            targetEntityName = null;
        }
        if (targetEntityName == null) {
            targetEntityName = NamedObjectFactory.createName(DbRelationship.class, relationship.getTargetEntity());
        }
        DbJoinTableModel model = (DbJoinTableModel) table.getModel();
        boolean updatingReverse = model.getObjectList().size() > 0;
        if (updatingReverse && !validateName(relationship.getTargetEntity(), reverseRelationship, targetEntityName)) {
            return;
        }
        if (!Util.nullSafeEquals(sourceEntityName, relationship.getName())) {
            String oldName = relationship.getName();
            ProjectUtil.setRelationshipName(relationship.getSourceEntity(), relationship, sourceEntityName);
            getMediator().fireDbRelationshipEvent(new RelationshipEvent(this, relationship, relationship.getSourceEntity(), oldName));
        }
        model.commit();
        if (relationship.isToDependentPK() && !relationship.isValidForDepPk()) {
            relationship.setToDependentPK(false);
        }
        if (updatingReverse) {
            if (reverseRelationship == null) {
                reverseRelationship = new DbRelationship(targetEntityName);
                reverseRelationship.setSourceEntity(relationship.getTargetEntity());
                reverseRelationship.setTargetEntity(relationship.getSourceEntity());
                reverseRelationship.setToMany(!relationship.isToMany());
                relationship.getTargetEntity().addRelationship(reverseRelationship);
                if (relationship.getSourceEntity() == relationship.getTargetEntity()) {
                    getMediator().fireDbRelationshipEvent(new RelationshipEvent(this, reverseRelationship, reverseRelationship.getSourceEntity(), MapEvent.ADD));
                }
            } else if (!Util.nullSafeEquals(targetEntityName, reverseRelationship.getName())) {
                String oldName = reverseRelationship.getName();
                ProjectUtil.setRelationshipName(reverseRelationship.getSourceEntity(), reverseRelationship, targetEntityName);
                getMediator().fireDbRelationshipEvent(new RelationshipEvent(this, reverseRelationship, reverseRelationship.getSourceEntity(), oldName));
            }
            Collection reverseJoins = getReverseJoins();
            reverseRelationship.setJoins(reverseJoins);
            if (!relationship.isToDependentPK()) {
                Iterator it = reverseJoins.iterator();
                if (it.hasNext()) {
                    boolean toDepPK = true;
                    while (it.hasNext()) {
                        DbJoin join = (DbJoin) it.next();
                        if (!join.getTarget().isPrimaryKey()) {
                            toDepPK = false;
                            break;
                        }
                    }
                    reverseRelationship.setToDependentPK(toDepPK);
                }
            }
        }
        getMediator().fireDbRelationshipEvent(new RelationshipEvent(this, relationship, relationship.getSourceEntity()));
        dispose();
    }

    private boolean validateName(Entity entity, Relationship aRelationship, String newName) {
        Relationship existing = entity.getRelationship(newName);
        if (existing != null && (aRelationship == null || aRelationship != existing)) {
            JOptionPane.showMessageDialog(this, "There is an existing relationship named \"" + newName + "\". Select a different name.");
            return false;
        }
        return true;
    }

    private Collection getReverseJoins() {
        Collection joins = relationship.getJoins();
        if ((joins == null) || (joins.size() == 0)) {
            return Collections.EMPTY_LIST;
        }
        List reverseJoins = new ArrayList(joins.size());
        Iterator it = joins.iterator();
        while (it.hasNext()) {
            DbJoin pair = (DbJoin) it.next();
            DbJoin reverseJoin = pair.createReverseJoin();
            reverseJoin.setRelationship(reverseRelationship);
            reverseJoins.add(reverseJoin);
        }
        return reverseJoins;
    }

    final class AttributeTable extends CayenneTable {

        final Dimension preferredSize = new Dimension(203, 100);

        public Dimension getPreferredScrollableViewportSize() {
            return preferredSize;
        }
    }
}
