package mySBML;

import java.util.ArrayList;
import mySBML.primitiveTypes.DoubleSbml;
import mySBML.utilities.SbmlProblem;
import mySBML.utilities.XmlProblem;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SpeciesReference extends SimpleSpeciesReference {

    private DoubleSbml stoichiometry = new DoubleSbml(this);

    private StoichiometryMath stoichiometryMath = null;

    public static final String ERROR_MSG_21113 = "Stoichiometry and StoichiometryMath are mutually exclusive";

    @Override
    public String tag() {
        return "speciesReference";
    }

    private static final String[] allowedChildren = new String[] { "notes", "annotation", "stoichiometryMath" };

    @Override
    public String[] getAllowedChildren() {
        return allowedChildren;
    }

    private static final String[] allowedAttributes = new String[] { "metaid", "sboTerm", "id", "name", "species", "stoichiometry" };

    @Override
    public String[] getAllowedAttributes() {
        return allowedAttributes;
    }

    @Override
    public void copy(AbstractSbml original) {
        if (original.getClass() != this.getClass()) return;
        SpeciesReference ref = (SpeciesReference) original;
        setId(ref.id.toString());
        setName(ref.name.toString());
        setSpeciesID(ref.speciesID.toString());
        setStoichiometry(ref.stoichiometry.toString());
        if (ref.stoichiometryMath != null) setStoichiometryMath(ref.getStoichiometryMath().copy()); else removeStoichiometryMath();
    }

    @Override
    public SpeciesReference copy() {
        SpeciesReference copy = new SpeciesReference();
        copy.copy(this);
        return copy;
    }

    public String getStoichiometries() {
        if (!stoichiometry.isEmpty()) return stoichiometry.toString();
        if (stoichiometryMath != null) return stoichiometryMath.getMath().toExpression();
        return "";
    }

    public DoubleSbml getStoichiometry() {
        return stoichiometry;
    }

    public void setStoichiometry(String stoichiometry) {
        this.stoichiometry.set(stoichiometry);
    }

    public StoichiometryMath getStoichiometryMath() {
        return stoichiometryMath;
    }

    public void setStoichiometryMath(StoichiometryMath stoichiometryMath) {
        this.stoichiometryMath = stoichiometryMath;
        this.stoichiometryMath.setParent(this);
    }

    public void removeStoichiometryMath() {
        if (stoichiometryMath != null) stoichiometryMath.setParent(null);
        stoichiometryMath = null;
    }

    @Override
    public ArrayList<XmlProblem> loadXML(Node node) {
        ArrayList<XmlProblem> problems = super.loadXML(node);
        if (node.getAttributes().getNamedItem("species") != null) setSpeciesID(node.getAttributes().getNamedItem("species").getNodeValue());
        if (node.getAttributes().getNamedItem("stoichiometry") != null) setStoichiometry(node.getAttributes().getNamedItem("stoichiometry").getNodeValue());
        int mathNodes = 0;
        for (int i = 0; i < node.getChildNodes().getLength(); i++) if (node.getChildNodes().item(i).getNodeName().equalsIgnoreCase("stoichiometryMath")) {
            if (mathNodes == 0) {
                setStoichiometryMath(new StoichiometryMath());
                problems.addAll(stoichiometryMath.loadXML(node.getChildNodes().item(i)));
            }
            mathNodes++;
        }
        if (mathNodes > 1) problems.add(XmlProblem.redundantChildElements(tag(), "stoichiometryMath"));
        return problems;
    }

    @Override
    public Element toSBML(Node parentNode) {
        Element node = super.toSBML(parentNode);
        node.setAttribute("species", getSpeciesID().toString());
        if (!stoichiometry.isEmpty()) node.setAttribute("stoichiometry", getStoichiometry().toString());
        if (stoichiometryMath != null) node.appendChild(stoichiometryMath.toSBML(node));
        return node;
    }

    @Override
    public ArrayList<SbmlProblem> getSbmlProblems() {
        ArrayList<SbmlProblem> problems = super.getSbmlProblems();
        if (!stoichiometry.isValid()) problems.add(createError(ERROR_MSG_INVALID_DOUBLE, getXPath() + " / @stoichiometry"));
        if (!stoichiometry.isEmpty() && stoichiometryMath != null) problems.add(createError(ERROR_MSG_21113, getXPath() + " / @stoichiometry"));
        if (stoichiometryMath != null) {
            problems.addAll(stoichiometryMath.getSbmlProblems());
            if (worstProblem < stoichiometryMath.getWorstProblem()) worstProblem = stoichiometryMath.getWorstProblem();
        }
        return problems;
    }

    @Override
    public String getXPath() {
        Reaction parent = (Reaction) this.getParent();
        String xpath = parent.getXPath();
        xpath += " // speciesReference[@species='" + getSpeciesID() + "']";
        return xpath;
    }
}
