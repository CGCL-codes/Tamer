package net.sourceforge.bricksviewer.brick.io;

import java.util.*;
import org.jdom.Document;
import org.jdom.Element;
import net.sourceforge.bricksviewer.brick.Material;
import net.sourceforge.bricksviewer.brick.MaterialPalette;
import net.sourceforge.bricksviewer.brick.MaterialLoadingException;
import net.sourceforge.bricksviewer.xml.*;

public class JarXMLMaterialLoader extends XMLDOMLoader {

    public static final String XML_PATH = "/bricks/MaterialPalette.xml";

    public static final String ID_ATTRIBUTE = "id";

    public static final String RED_ATTRIBUTE = "red";

    public static final String GREEN_ATTRIBUTE = "green";

    public static final String BLUE_ATTRIBUTE = "blue";

    public static final String ALPHA_ATTRIBUTE = "alpha";

    public MaterialPalette load() throws MaterialLoadingException {
        Document document = null;
        Element materialElement, paletteElement;
        Iterator it;
        List materialElements;
        MaterialPalette palette = new MaterialPalette();
        try {
            document = loadDocumentFromJar(XML_PATH);
        } catch (JarXMLLoadingException e) {
            throw new MaterialLoadingException("Problem loading material palette '" + XML_PATH + "'", e);
        }
        paletteElement = document.getRootElement();
        materialElements = paletteElement.getChildren();
        it = materialElements.iterator();
        while (it.hasNext()) {
            materialElement = (Element) it.next();
            addMaterial(palette, materialElement);
        }
        return palette;
    }

    protected void addMaterial(MaterialPalette palette, Element materialElement) throws MaterialLoadingException {
        float red, green, blue, alpha;
        Material material;
        String id;
        id = materialElement.getAttributeValue(ID_ATTRIBUTE);
        try {
            red = loadFloat(materialElement, RED_ATTRIBUTE);
            green = loadFloat(materialElement, GREEN_ATTRIBUTE);
            blue = loadFloat(materialElement, BLUE_ATTRIBUTE);
            alpha = loadFloat(materialElement, ALPHA_ATTRIBUTE);
            material = new Material(id, red, green, blue, alpha);
            palette.addMaterial(material);
        } catch (XMLLoadingException e) {
            throw new MaterialLoadingException("Material '" + id + "' contains invalid color data");
        }
    }
}
