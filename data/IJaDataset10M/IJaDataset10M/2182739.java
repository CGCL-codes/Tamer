package de.mogwai.common.web.component.renderkit.html.layout.delegates;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import de.mogwai.common.web.component.layout.GridBagLayoutUtils;
import de.mogwai.common.web.component.layout.GridbagLayoutCellInfo;
import de.mogwai.common.web.component.layout.GridbagLayoutCellInfoVector;
import de.mogwai.common.web.component.layout.GridbagLayoutComponent;
import de.mogwai.common.web.component.layout.GridbagLayoutSizeDefinition;
import de.mogwai.common.web.component.layout.GridbagLayoutSizeDefinitionVector;

/**
 * Renderer for the GridbagLayout component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-10 18:35:06 $
 */
public class GridbagLayoutOldStyleDelegate extends GridbagLayoutDelegate {

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {
        GridbagLayoutComponent theComponent = (GridbagLayoutComponent) aComponent;
        ResponseWriter theWriter = aContext.getResponseWriter();
        theWriter.startElement("table", aComponent);
        theWriter.writeAttribute("id", theComponent.getClientId(aContext), null);
        int theBorder = theComponent.getBorder();
        theWriter.writeAttribute("border", theBorder, null);
        theWriter.writeAttribute("cellspacing", "0", null);
        theWriter.writeAttribute("cellpadding", "0", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeChildren(FacesContext aContext, UIComponent aComponent) throws IOException {
        GridbagLayoutComponent theComponent = (GridbagLayoutComponent) aComponent;
        GridbagLayoutCellInfoVector theCellInfos = generateCellInfo(aComponent);
        ResponseWriter theWriter = aContext.getResponseWriter();
        GridbagLayoutSizeDefinitionVector theRows = theComponent.getRows();
        GridbagLayoutSizeDefinitionVector theCols = theComponent.getCols();
        for (int y = 0; y < theRows.size(); y++) {
            GridbagLayoutSizeDefinition theRow = theRows.get(y);
            if (y == 0) {
                theWriter.startElement("tr", aComponent);
                theWriter.writeAttribute("height", "1", null);
                for (int x = 0; x < theCols.size(); x++) {
                    String theWidth = GridBagLayoutUtils.getSizeForColumn(theComponent, theCols, x);
                    theWriter.startElement("td", aComponent);
                    theWriter.writeAttribute("width", theWidth, null);
                    theWriter.endElement("td");
                }
                theWriter.endElement("tr");
            }
            theWriter.startElement("tr", aComponent);
            theWriter.writeAttribute("height", theRow.getSize(), null);
            for (int x = 0; x < theCols.size(); x++) {
                GridbagLayoutSizeDefinition theColDef = theCols.get(x);
                GridbagLayoutCellInfo theCell = theCellInfos.findCellInfo(x + 1, y + 1);
                if (theCell == null) {
                    theWriter.startElement("td", aComponent);
                    theWriter.writeAttribute("class", "mogwaiGridBagLayoutCell", null);
                    String theWidth = GridBagLayoutUtils.getSizeForColumn(theComponent, theCols, x);
                    theWriter.writeAttribute("width", theWidth, null);
                    theWriter.endElement("td");
                } else {
                    if (theCell.isAt(x + 1, y + 1)) {
                        theWriter.startElement("td", aComponent);
                        theWriter.writeAttribute("class", "mogwaiGridBagLayoutCell", null);
                        if (theCell.getWidth() == 1) {
                            String theWidth = GridBagLayoutUtils.getSizeForColumn(theComponent, theCols, x);
                            theWriter.writeAttribute("width", theWidth, null);
                        } else {
                            theWriter.writeAttribute("colspan", theCell.getWidth(), null);
                        }
                        String theAlign = "";
                        String theVerticalAlign = theCell.getValign();
                        if (theColDef.getAlign() != null) {
                            theWriter.writeAttribute("align", theColDef.getAlign(), null);
                            theAlign = theColDef.getAlign();
                        } else {
                            theAlign = theCell.getAlign();
                            theWriter.writeAttribute("align", theAlign, null);
                        }
                        theWriter.writeAttribute("style", "text-align:" + theAlign + ";vertical-align:" + theVerticalAlign + ";", null);
                        theWriter.writeAttribute("valign", theVerticalAlign, null);
                        if (theCell.getHeight() > 1) {
                            theWriter.writeAttribute("rowspan", theCell.getHeight(), null);
                        }
                        renderChild(aContext, theCell.getComponent());
                        theWriter.endElement("td");
                    }
                }
            }
            theWriter.endElement("tr");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {
        ResponseWriter theWriter = aContext.getResponseWriter();
        theWriter.endElement("table");
    }
}
