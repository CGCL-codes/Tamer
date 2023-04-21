package visualbiology.reactionEditor.model;

import mySBML.SimpleSpeciesReference;
import mySBML.SpeciesReference;
import mySBML.utilities.DomUtilities;
import org.eclipse.draw2d.geometry.Rectangle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SpeciesNode extends SbmlGefNode {

    private String speciesId;

    private SimpleSpeciesReference speciesReference;

    private boolean reactant = false;

    private boolean product = false;

    private boolean modifier = false;

    public static final int DEFAULT_HEIGHT = 60;

    public static final int DEFAULT_WIDTH = 100;

    public SpeciesNode(String speciesId) {
        super();
        this.speciesId = speciesId;
        getLayout().width = SpeciesNode.DEFAULT_WIDTH;
        getLayout().height = SpeciesNode.DEFAULT_HEIGHT;
    }

    public void setReactant() {
        reactant = true;
        product = false;
        modifier = false;
    }

    public void setProduct() {
        reactant = false;
        product = true;
        modifier = false;
    }

    public void setModifier() {
        reactant = false;
        product = false;
        modifier = true;
    }

    public boolean isReactant() {
        return reactant;
    }

    public boolean isProduct() {
        return product;
    }

    public boolean isModifier() {
        return modifier;
    }

    public void update() {
        speciesId = speciesReference.getSpeciesID().toString();
        getListeners().firePropertyChange(PROPERTY_RENAME, null, null);
    }

    public String getStoichiometry() {
        if (speciesReference instanceof SpeciesReference) return ((SpeciesReference) speciesReference).getStoichiometries();
        return null;
    }

    public void setStoichiometry(String stoichiometry) {
        if (speciesReference instanceof SpeciesReference) {
            if (getSourceConnections().size() > 0) ((ConnectionSpecies) getSourceConnections().get(0)).setStoichiometry(stoichiometry);
            if (getTargetConnections().size() > 0) ((ConnectionSpecies) getTargetConnections().get(0)).setStoichiometry(stoichiometry);
        }
    }

    public SimpleSpeciesReference getSpeciesReference() {
        return speciesReference;
    }

    public void setSpeciesReference(SimpleSpeciesReference speciesReference) {
        this.speciesReference = speciesReference;
    }

    @Override
    public String getId() {
        return speciesId;
    }

    @Override
    public void setId(String id) {
        speciesId = id;
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SpeciesNode emp = new SpeciesNode(speciesId);
        emp.reactant = this.reactant;
        emp.product = this.product;
        emp.modifier = this.modifier;
        emp.setParent(this.getParent());
        emp.setLayout(new Rectangle(getLayout().x + 10, getLayout().y + 10, getLayout().width, getLayout().height));
        return emp;
    }

    public boolean loadXML(Node xmlNode) {
        Rectangle rect = new Rectangle();
        try {
            DomUtilities.removeEmptyTextChildren(xmlNode);
            String x = xmlNode.getAttributes().getNamedItem("x").getNodeValue();
            String y = xmlNode.getAttributes().getNamedItem("y").getNodeValue();
            String width = xmlNode.getAttributes().getNamedItem("width").getNodeValue();
            String height = xmlNode.getAttributes().getNamedItem("height").getNodeValue();
            rect.x = Integer.parseInt(x);
            rect.y = Integer.parseInt(y);
            rect.width = Integer.parseInt(width);
            rect.height = Integer.parseInt(height);
            setLayout(rect);
            return true;
        } catch (Exception e) {
            rect.x = 0;
            rect.y = 0;
            rect.width = DEFAULT_WIDTH;
            rect.height = DEFAULT_HEIGHT;
            setLayout(rect);
            return false;
        }
    }

    Node toXML(Node parentNode) {
        Element node = parentNode.getOwnerDocument().createElement("speciesNode");
        node.setAttribute("speciesID", speciesId);
        if (getStoichiometry() != null) node.setAttribute("stoichiometry", getStoichiometry());
        node.setAttribute("x", (new Integer(getLayout().x)).toString());
        node.setAttribute("y", (new Integer(getLayout().y)).toString());
        node.setAttribute("width", (new Integer(getLayout().width)).toString());
        node.setAttribute("height", (new Integer(getLayout().height)).toString());
        return node;
    }
}
