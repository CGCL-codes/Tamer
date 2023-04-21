package ge.forms.etx.model.flex;

import ge.forms.etx.model.Inspection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author dimitri
 */
public class InspectionSkin {

    public Element convert(Document doc, Inspection inspection) {
        Element root = doc.createElement("inspection");
        if (inspection.getId() != null) Utils.append(doc, root, "id", String.valueOf(inspection.getId()));
        Utils.append(doc, root, "name", inspection.getName());
        Utils.append(doc, root, "description", inspection.getDescription());
        return root;
    }
}
