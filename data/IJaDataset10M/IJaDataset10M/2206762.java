package edu.gsbme.gyoza2d.visual.Declaration;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.w3c.dom.Element;
import edu.gsbme.gyoza2d.Control.DeclarationObject;
import edu.gsbme.gyoza2d.GraphGenerator.GraphGenerator;
import edu.gsbme.gyoza2d.UI.menu.ItemTemplate;
import edu.gsbme.gyoza2d.UI.menu.ItemTemplate.abstract_menu_type;
import edu.gsbme.gyoza2d.UI.menu.MenuTemplate.MenuSpace;
import edu.gsbme.gyoza2d.visual.UIText;
import edu.gsbme.menuactiondelegate.MenuActionDelegate;
import edu.gsbme.menuactiondelegate.MenuActionString;

public class DeclarationControl extends DeclarationObject {

    public DeclarationControl(Element element, MenuActionDelegate actionDelegate, GraphGenerator graphGenerator) {
        super(element, actionDelegate, graphGenerator);
    }

    @Override
    public String toString() {
        return "<html><center><b>Declaration</b></center></html>";
    }

    @Override
    protected void initVertexMenu() {
        ItemTemplate submenu = new ItemTemplate(UIText._new, abstract_menu_type.cascade);
        ItemTemplate menu = new ItemTemplate("Variable", abstract_menu_type.push);
        menu.setSelectionAdapter(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                actionDelegate.getAction(MenuActionString.Variable_add.toString()).run(returnElement());
            }
        });
        submenu.insertChild(menu);
        menu = new ItemTemplate("Equation", abstract_menu_type.push);
        menu.setSelectionAdapter(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                actionDelegate.getAction(MenuActionString.Expression_add.toString()).run(returnElement());
            }
        });
        submenu.insertChild(menu);
        menu = new ItemTemplate("Function", abstract_menu_type.push);
        menu.setSelectionAdapter(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                actionDelegate.getAction(MenuActionString.function_add.toString()).run(returnElement());
            }
        });
        submenu.insertChild(menu);
        menu = new ItemTemplate("Data", abstract_menu_type.push);
        menu.setSelectionAdapter(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                actionDelegate.getAction(MenuActionString.data_add.toString()).run(returnElement());
            }
        });
        submenu.insertChild(menu);
        menu = new ItemTemplate("Boundary Condition", abstract_menu_type.push);
        menu.setSelectionAdapter(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                actionDelegate.getAction(MenuActionString.boundary_condition_add.toString()).run(returnElement());
            }
        });
        submenu.insertChild(menu);
        menu = new ItemTemplate("Weak Term", abstract_menu_type.push);
        menu.setSelectionAdapter(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                actionDelegate.getAction(MenuActionString.weak_term_add.toString()).run(returnElement());
            }
        });
        submenu.insertChild(menu);
        getMenuTemplate().insertMenuAtEndOfSpace(submenu, MenuSpace.GENERAL_SPACE);
        menu = new ItemTemplate(UIText.Overview, abstract_menu_type.push);
        menu.setSelectionAdapter(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                actionDelegate.getAction(MenuActionString.Declaration_Overview.toString()).run(returnElement());
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
}
