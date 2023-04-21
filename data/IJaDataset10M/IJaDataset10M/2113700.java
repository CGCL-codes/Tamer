package de.mogwai.common.web.component;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import de.mogwai.common.web.component.common.FacetComponent;

/**
 * Base list aware component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:35:58 $
 */
public class ListawareComponent extends UIData {

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        setRowIndex(-1);
        super.encodeBegin(context);
    }

    public FacetComponent getMogwaiFacet(String aName) {
        for (int i = 0; i < getChildCount(); i++) {
            UIComponent theComponent = (UIComponent) getChildren().get(i);
            if ((theComponent instanceof FacetComponent)) {
                FacetComponent theFacetComponent = (FacetComponent) theComponent;
                if (theFacetComponent.getName().equals(aName)) {
                    return theFacetComponent;
                }
            }
        }
        return null;
    }
}
