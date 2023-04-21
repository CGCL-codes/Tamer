package es.eucm.eadventure.editor.gui.structurepanel.structureelements;

import javax.swing.ImageIcon;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.control.controllers.item.ItemsListDataControl;
import es.eucm.eadventure.editor.gui.structurepanel.StructureElementFactory;
import es.eucm.eadventure.editor.gui.structurepanel.StructureListElement;
import es.eucm.eadventure.editor.gui.structurepanel.StructureElement;

public class ItemsListStructureElement extends StructureListElement {

    public ItemsListStructureElement(DataControl dataControl) {
        super(TC.get("ItemsList.Title"), dataControl);
        icon = new ImageIcon("img/icons/items.png");
    }

    @Override
    public int getChildCount() {
        return ((ItemsListDataControl) dataControl).getItems().size();
    }

    @Override
    public StructureElement getChild(int i) {
        return StructureElementFactory.getStructureElement(((ItemsListDataControl) dataControl).getItems().get(i), this);
    }
}
