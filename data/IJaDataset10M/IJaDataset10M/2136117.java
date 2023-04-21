package org.gwt.mosaic.xul.client.ui;

import com.google.gwt.user.client.ui.Widget;

/**
 * A group of listcol elements. There should be only one listcols element in a
 * listbox.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class Listcols extends Container {

    private static final long serialVersionUID = 1001654698402852511L;

    @Override
    public Element add(Element element, int index) {
        if (element instanceof Listcol) {
            return super.add(element, index);
        }
        return null;
    }

    @Override
    protected Widget createUI() {
        return null;
    }
}
