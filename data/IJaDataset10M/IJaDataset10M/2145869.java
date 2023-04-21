package edu.gsbme.gyoza2d.visual.ModelML;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.w3c.dom.Element;
import edu.gsbme.gyoza2d.Control.ModelMLObject;
import edu.gsbme.gyoza2d.GraphGenerator.GraphGenerator;
import edu.gsbme.gyoza2d.UI.menu.ItemTemplate;
import edu.gsbme.gyoza2d.UI.menu.ItemTemplate.abstract_menu_type;
import edu.gsbme.gyoza2d.UI.menu.MenuTemplate.MenuSpace;
import edu.gsbme.gyoza2d.visual.UIText;
import edu.gsbme.menuactiondelegate.MenuActionDelegate;
import edu.gsbme.menuactiondelegate.MenuActionString;

public class ImportPropertyControl extends ModelMLObject {

    public ImportPropertyControl(Element element, MenuActionDelegate actionDelegate, GraphGenerator graphGenerator) {
        super(element, actionDelegate, graphGenerator);
    }

    @Override
    protected void initVertexMenu() {
        ItemTemplate menu = new ItemTemplate(UIText.edit, abstract_menu_type.push);
        menu.setSelectionAdapter(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                actionDelegate.getAction(MenuActionString.Import_Property_edit.toString()).run(returnElement());
            }
        });
        getMenuTemplate().insertMenuAtEndOfSpace(menu, MenuSpace.GENERAL_SPACE);
        menu = new ItemTemplate(UIText.delete, abstract_menu_type.push);
        menu.setSelectionAdapter(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                actionDelegate.getAction(MenuActionString.Delete.toString()).run(returnElement());
            }
        });
        getMenuTemplate().insertMenuAtEndOfSpace(menu, MenuSpace.GENERAL_SPACE);
    }

    @Override
    public String toString() {
        return "<html><center><b>Import Properties</b></center></html>";
    }
}
