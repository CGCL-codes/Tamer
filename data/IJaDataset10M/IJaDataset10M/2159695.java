package net.rptools.chartool.ui.charsheet;

import javax.swing.table.TableModel;
import net.rptools.chartool.model.property.PropertyDescriptorType;
import net.rptools.chartool.model.property.PropertyMap;
import net.rptools.chartool.ui.charsheet.component.CharSheetTable;
import net.rptools.chartool.ui.charsheet.component.CharSheetTable.CharSheetTableModel;
import com.jidesoft.grid.SortableTableModel;

/**
 * This delegate handles the {@link PropertyDescriptorType#MAP} type in a {@link CharSheetTable}.
 * 
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
public class MapCharSheetTableDelegate extends ComponentDelegate<CharSheetTable, PropertyMap> {

    /**
   * This delegate handles the {@link PropertyDescriptorType#MAP} type in a {@link CharSheetTable}.
   */
    MapCharSheetTableDelegate() {
        super(PropertyDescriptorType.MAP, CharSheetTable.class);
    }

    /**
   * @see net.rptools.inittool.charsheet.ComponentDelegate#getValue(C, V, String)
   */
    @Override
    @SuppressWarnings("all")
    public PropertyMap getValue(CharSheetTable aComponent, PropertyMap aCurrentValue) {
        return aCurrentValue;
    }

    /**
   * @see net.rptools.chartool.ui.charsheet.ComponentDelegate#needsSave(java.awt.Component, java.lang.Object)
   */
    @Override
    public boolean needsSave(CharSheetTable aComponent, PropertyMap aCurrentValue) {
        TableModel model = aComponent.getModel();
        if (model instanceof SortableTableModel) model = ((SortableTableModel) model).getActualModel();
        return ((CharSheetTableModel) model).isChanged();
    }

    /**
   * @see net.rptools.inittool.charsheet.ComponentDelegate#setValue(V, C)
   */
    @Override
    @SuppressWarnings("all")
    public void setValue(PropertyMap aValue, CharSheetTable aComponent) {
        if (aComponent.executeOnSetValueScript(aValue)) return;
        aComponent.setData(aValue);
    }

    /**
   * @see net.rptools.chartool.ui.charsheet.Delegate#getInstance(java.awt.Component, net.rptools.chartool.ui.charsheet.CharSheetController)
   */
    @Override
    public Delegate getInstance(CharSheetTable aComponent, CharSheetController aController) {
        aComponent.putClientProperty(ComponentDelegate.class.getName(), this);
        return super.getInstance(aComponent, aController);
    }

    /**
   * @see net.rptools.chartool.ui.charsheet.ComponentDelegate#update(java.awt.Component, net.rptools.chartool.ui.charsheet.CharSheetController)
   */
    @Override
    public void update(CharSheetTable aComponent, CharSheetController aController) {
        if (aController.getData() == null) return;
        PropertyMap value = (PropertyMap) aController.getData().get(aComponent.getName());
        if (!needsSave(aComponent, value)) return;
        aController.setFields();
    }
}
