package edu.gsbme.gyoza2d.visual.ModelML;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.gyoza2d.Control.ModelMLObject;
import edu.gsbme.gyoza2d.GraphGenerator.GraphGenerator;
import edu.gsbme.gyoza2d.UI.menu.ItemTemplate;
import edu.gsbme.gyoza2d.UI.menu.ItemTemplate.abstract_menu_type;
import edu.gsbme.gyoza2d.UI.menu.MenuTemplate.MenuSpace;
import edu.gsbme.gyoza2d.visual.UIText;
import edu.gsbme.menuactiondelegate.MenuActionDelegate;
import edu.gsbme.menuactiondelegate.MenuActionString;

public class ImportEquationControl extends ModelMLObject {

    public ImportEquationControl(Element element, MenuActionDelegate actionDelegate, GraphGenerator graphGenerator) {
        super(element, actionDelegate, graphGenerator);
    }

    @Override
    protected void initVertexMenu() {
        ItemTemplate menu = new ItemTemplate(UIText.delete, abstract_menu_type.push);
        menu.setSelectionAdapter(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                actionDelegate.getAction(MenuActionString.Delete.toString()).run(returnElement());
            }
        });
        getMenuTemplate().insertMenuAtEndOfSpace(menu, MenuSpace.GENERAL_SPACE);
    }

    @Override
    public String toString() {
        return "<html><center><b>Import Equation<p><p>" + returnElement().getAttribute(Attributes.ref.toString()) + "</b></center></html>";
    }
}
