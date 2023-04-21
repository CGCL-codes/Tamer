package be.lassi.ui.library;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import be.lassi.base.Holder;
import be.lassi.domain.AttributeDefinition;
import be.lassi.domain.FixtureDefinition;
import be.lassi.domain.PresetDefinition;
import be.lassi.library.Library;
import be.lassi.ui.util.LassiAction;

public class LibraryPresentationModel {

    private final Library library;

    private final Holder<FixtureDefinition> fixtureHolder = new Holder<FixtureDefinition>();

    private final Holder<AttributeDefinition> attributeHolder = new Holder<AttributeDefinition>();

    private final Holder<PresetDefinition> presetHolder = new Holder<PresetDefinition>();

    private final AttributesPresentationModel attributesPresentationModel;

    private final PresetsPresentationModel presetsPresentationModel;

    private final AttributeValuesPresentationModel attributeValuesPresentationModel;

    private final CategoryTree tree;

    private final LassiAction actionAddCategory = new ActionAddCategory();

    private final LassiAction actionRemoveCategory = new ActionRemoveCategory();

    private final LassiAction actionAddType = new ActionAddType();

    private final LassiAction actionRemoveType = new ActionRemoveType();

    private final LassiAction actionDuplicateType = new ActionDuplicateType();

    private final LassiAction actionSave = new ActionSave();

    private final LassiAction actionCancel = new ActionCancel();

    private final LibraryDialogs dialogs;

    public LibraryPresentationModel(final Library library, final LibraryDialogs dialogs) {
        this.library = library;
        this.dialogs = dialogs;
        attributesPresentationModel = new AttributesPresentationModel(fixtureHolder, presetHolder);
        presetsPresentationModel = new PresetsPresentationModel(fixtureHolder);
        attributeValuesPresentationModel = new AttributeValuesPresentationModel(attributeHolder);
        attributesPresentationModel.getSelectionModel().addListSelectionListener(new AttributeSelectionListener());
        presetsPresentationModel.getSelectionModel().addListSelectionListener(new PresetSelectionListener());
        library.getDirty().getModel().addValueChangeListener(new DirtyListener());
        tree = new CategoryTree(library);
        tree.getTreeSelectionModel().addTreeSelectionListener(new MyTreeSelectionListener());
        update();
    }

    public Action getActionAddCategory() {
        return actionAddCategory;
    }

    public Action getActionAddType() {
        return actionAddType;
    }

    public LassiAction getActionCancel() {
        return actionCancel;
    }

    public Action getActionDuplicateType() {
        return actionDuplicateType;
    }

    public Action getActionRemoveCategory() {
        return actionRemoveCategory;
    }

    public Action getActionRemoveType() {
        return actionRemoveType;
    }

    public LassiAction getActionSave() {
        return actionSave;
    }

    public AttributesPresentationModel getAttributesPresentationModel() {
        return attributesPresentationModel;
    }

    public AttributeValuesPresentationModel getAttributeValuesPresentationModel() {
        return attributeValuesPresentationModel;
    }

    public PresetsPresentationModel getPresetsPresentationModel() {
        return presetsPresentationModel;
    }

    public TreeModel getTreeModel() {
        return tree.getTreeModel();
    }

    public TreeSelectionModel getTreeSelectionModel() {
        return tree.getTreeSelectionModel();
    }

    private void actionCancel() {
        FixtureDefinition oldFixtureDefinition = fixtureHolder.getValue();
        String category = oldFixtureDefinition.getCategory();
        String type = oldFixtureDefinition.getType();
        readFixtureDefinition(category, type);
    }

    private void actionSave() {
        FixtureDefinition fixtureDefinition = fixtureHolder.getValue();
        library.put(fixtureDefinition);
    }

    private void readFixtureDefinition(final String category, final String type) {
        FixtureDefinition fixtureDefinition = library.get(category, type);
        fixtureHolder.setValue(fixtureDefinition);
        attributeHolder.setValue(null);
        presetHolder.setValue(null);
    }

    private void treeSelectionChanged() {
        boolean fixtureSelected = tree.isTypeSelected();
        if (fixtureSelected) {
            String category = tree.getSelectedCategory();
            String type = tree.getSelectedType();
            FixtureDefinition old = fixtureHolder.getValue();
            if (old == null || (!(old.getCategory().equals(category) && old.getType().equals(type)))) {
                if (library.getDirty().isDirty()) {
                    if (dialogs.isSaveConfirmed(old.getType())) {
                        actionSave();
                    }
                }
                readFixtureDefinition(category, type);
            }
        } else {
            if (library.getDirty().isDirty()) {
                FixtureDefinition old = fixtureHolder.getValue();
                if (dialogs.isSaveConfirmed(old.getType())) {
                    actionSave();
                }
            }
            library.getDirty().clear();
            fixtureHolder.setValue(null);
            attributeHolder.setValue(null);
            presetHolder.setValue(null);
        }
        update();
    }

    private void update() {
        boolean categorySelected = tree.isCategorySelected();
        boolean fixtureSelected = tree.isTypeSelected();
        actionAddCategory.setEnabled(true);
        actionRemoveCategory.setEnabled(categorySelected);
        actionAddType.setEnabled(categorySelected || fixtureSelected);
        actionRemoveType.setEnabled(fixtureSelected);
        actionDuplicateType.setEnabled(fixtureSelected);
        actionSave.setEnabled(library.getDirty().isDirty());
        actionCancel.setEnabled(library.getDirty().isDirty());
    }

    private class ActionAddCategory extends LassiAction {

        private ActionAddCategory() {
            super("library.action.addCategory");
        }

        @Override
        public void action() {
            String category = dialogs.getNewCategory("Category");
            if (category != null) {
                library.addCategory(category);
                tree.addCategory(category);
            }
        }
    }

    private class ActionAddType extends LassiAction {

        private ActionAddType() {
            super("library.action.addFixture");
        }

        @Override
        public void action() {
            String type = getNewType();
            if (type != null) {
                String category = tree.getSelectedCategory();
                FixtureDefinition fixtureDefinition = new FixtureDefinition(library.getDirty(), category, type);
                library.put(fixtureDefinition);
                tree.addType(type);
                fixtureHolder.setValue(fixtureDefinition);
            }
        }

        private String getNewType() {
            String category = tree.getSelectedCategory();
            String type = tree.getSelectedType();
            String newType = dialogs.getNewType(category, type);
            return newType;
        }
    }

    private class ActionCancel extends LassiAction {

        private ActionCancel() {
            super("library.action.cancel");
        }

        @Override
        public void action() {
            actionCancel();
        }
    }

    private class ActionDuplicateType extends LassiAction {

        private static final long serialVersionUID = 1L;

        private ActionDuplicateType() {
            super("library.action.duplicateFixture");
        }

        @Override
        public void action() {
            String type = getDuplicateFixture();
            if (type != null) {
                FixtureDefinition fixtureDefinition = fixtureHolder.getValue();
                fixtureDefinition.setType(type);
                library.put(fixtureDefinition);
                tree.addType(type);
                fixtureHolder.setValue(fixtureDefinition);
            }
        }

        private String getDuplicateFixture() {
            String category = tree.getSelectedCategory();
            String fixture = tree.getSelectedType();
            String newFixture = dialogs.getDuplicateType(category, fixture);
            return newFixture;
        }
    }

    private class ActionRemoveCategory extends LassiAction {

        private ActionRemoveCategory() {
            super("library.action.removeCategory");
            setEnabled(false);
        }

        @Override
        public void action() {
            String category = tree.getSelectedCategory();
            if (dialogs.isCategoryRemoveConfirmed(category)) {
                library.deleteCategory(category);
                tree.removeSelectedNode();
            }
        }
    }

    private class ActionRemoveType extends LassiAction {

        private ActionRemoveType() {
            super("library.action.removeFixture");
        }

        @Override
        public void action() {
            String category = tree.getSelectedCategory();
            String type = tree.getSelectedType();
            if (dialogs.isTypeRemoveConfirmed(category, type)) {
                library.removeType(category, type);
                tree.removeSelectedNode();
            }
        }
    }

    private class ActionSave extends LassiAction {

        private ActionSave() {
            super("library.action.save");
        }

        @Override
        public void action() {
            actionSave();
        }
    }

    private class AttributeSelectionListener implements ListSelectionListener {

        public void valueChanged(final ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int row = attributesPresentationModel.getSelectionModel().getSelectedRow();
                if (row >= 0) {
                    AttributeDefinition attribute = fixtureHolder.getValue().getAttribute(row);
                    attributeHolder.setValue(attribute);
                }
            }
        }
    }

    private class PresetSelectionListener implements ListSelectionListener {

        public void valueChanged(final ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                PresetDefinition preset = null;
                int row = presetsPresentationModel.getSelectionModel().getSelectedRow();
                if (row >= 0) {
                    preset = fixtureHolder.getValue().getPreset(row);
                }
                presetHolder.setValue(preset);
            }
        }
    }

    private class DirtyListener implements PropertyChangeListener {

        public void propertyChange(final PropertyChangeEvent evt) {
            update();
        }
    }

    private class MyTreeSelectionListener implements TreeSelectionListener {

        public void valueChanged(final TreeSelectionEvent e) {
            treeSelectionChanged();
        }
    }
}
