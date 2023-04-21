package be.lassi.ui.library;

import be.lassi.base.Holder;
import be.lassi.base.Listener;
import be.lassi.domain.AttributeDefinition;

public class AttributeValuesPresentationModel extends TablePresentationModel {

    private final Holder<AttributeDefinition> attributeHolder;

    public AttributeValuesPresentationModel(final Holder<AttributeDefinition> attributeHolder) {
        super(new AttributeValuesTableModel(attributeHolder));
        this.attributeHolder = attributeHolder;
        attributeHolder.add(new HolderListener());
        update();
    }

    @Override
    protected void actionAdd() {
        AttributeDefinition attributeDefinition = attributeHolder.getValue();
        attributeDefinition.addValue("", 0, 0);
        getTableModel().fireTableDataChanged();
        update();
    }

    @Override
    public void actionRemove() {
        AttributeDefinition attributeDefinition = attributeHolder.getValue();
        int index = getSelectionModel().getSelectedRow();
        attributeDefinition.removeValue(index);
        getSelectionModel().clearSelection();
        getTableModel().fireTableDataChanged();
        update();
    }

    @Override
    protected void update() {
        AttributeDefinition fixtureDefinition = attributeHolder.getValue();
        if (fixtureDefinition == null) {
            getActionAdd().setEnabled(false);
            getActionRemove().setEnabled(false);
        } else {
            getActionAdd().setEnabled(true);
            getActionRemove().setEnabled(!getSelectionModel().isSelectionEmpty());
        }
    }

    private class HolderListener implements Listener {

        public void changed() {
            getSelectionModel().clearSelection();
            getTableModel().fireTableDataChanged();
            update();
        }
    }
}
