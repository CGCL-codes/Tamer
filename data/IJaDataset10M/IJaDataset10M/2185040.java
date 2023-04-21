package net.sf.jvibes.ui.views;

import net.sf.jvibes.kernel.elements.Body;
import net.sf.jvibes.kernel.elements.Element;
import net.sf.jvibes.kernel.elements.Link;
import net.sf.jvibes.kernel.elements.Model;

public class GridViewFactory {

    public static ElementGridView<? extends Element> createGridView(Element element) {
        switch(element.getCategory()) {
            case Body:
                return new BodyGridView((Body) element);
            case Link:
                return new LinkGridView((Link) element);
            case System:
                return new GridView((Model) element);
            default:
                return null;
        }
    }
}
