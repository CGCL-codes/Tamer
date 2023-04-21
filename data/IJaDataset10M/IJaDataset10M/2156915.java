package de.grogra.imp2d.graphs;

import java.util.*;
import java.util.regex.*;
import javax.vecmath.Color3f;
import de.grogra.util.*;
import de.grogra.graph.*;
import de.grogra.graph.GraphFilter.AccessorBridge;
import de.grogra.imp2d.objects.Attributes;
import de.grogra.imp2d.*;
import de.grogra.reflect.Type;
import de.grogra.imp2d.layout.Edge;
import de.grogra.imp2d.layout.Node;

public class HighlightFilter extends AttributeOverwritingFilter {

    /**
	 * Contains the descriptor which created this graph filter. The
	 * descriptor contains some information which parametrizes the
	 * behaviour of this filter.
	 */
    private final HighlightFilterDescriptor descr;

    private HighlightFilterData hfd;

    private Hashtable accessorBridges;

    private boolean hasFoldedNode;

    private String foldedNodeName;

    public HighlightFilter(Graph source, HighlightFilterDescriptor descr) {
        super(source);
        this.descr = descr;
        hfd = new HighlightFilterData(descr.xmlFile);
        hasFoldedNode = false;
        foldedNodeName = "";
        initAttributeOverwritingFilter();
    }

    @Override
    protected void initNodeAccessors(AccessorMap accessors) {
        accessorBridges = new Hashtable();
        LinkedList allChangingAttributesNames = hfd.getAllChangingAttributesNames();
        for (int i = 0; i < allChangingAttributesNames.size(); i++) {
            AccessorBridge abTemp = new AccessorBridge(Attribute.forName((String) allChangingAttributesNames.get(i)), true);
            accessors.add(abTemp);
            accessorBridges.put(allChangingAttributesNames.get(i), abTemp);
        }
    }

    @Override
    protected int getInt(Object object, AccessorBridge accessor, GraphState gs) {
        Integer test = (Integer) getHighlightResult(object, accessor, null, gs);
        return test.intValue();
    }

    @Override
    protected Object getObject(Object object, AccessorBridge accessor, Object placeIn, GraphState gs) {
        return getHighlightResult(object, accessor, placeIn, gs);
    }

    protected Object getHighlightResult(Object object, AccessorBridge accessor, Object placeIn, GraphState gs) {
        LinkedList filterHighlights = hfd.getFilterHighlights();
        LinkedList filterFoldings = hfd.getFilterFoldings();
        LinkedList filterHides = hfd.getFilterHides();
        if (accessorBridges.contains(accessor)) {
            Enumeration e = accessorBridges.keys();
            while (e.hasMoreElements()) {
                String abKey = (String) e.nextElement();
                if (accessorBridges.get(abKey) == accessor) {
                    for (int i = 0; i < filterHides.size(); i++) {
                        FilterHide fh = (FilterHide) filterHides.get(i);
                        boolean subnodes = fh.getSubnodes();
                        if (checkCriteria(fh.getHideCriteria(), object, gs) && abKey.equals("de.grogra.imp.fillColor") && !subnodes) {
                            return new Color3f(0, 0, 0);
                        }
                    }
                    for (int i = 0; i < filterHighlights.size(); i++) {
                        FilterHighlight fhl = (FilterHighlight) filterHighlights.get(i);
                        if (checkCriteria(fhl.getHiglightCriteria(), object, gs) && fhl.getHighlightResults().containsKey(abKey)) {
                            return fhl.getHighlightResults().get(abKey);
                        }
                    }
                    for (int i = 0; i < filterFoldings.size(); i++) {
                        FilterFolding ff = (FilterFolding) filterFoldings.get(i);
                        if (checkCriteria(ff.getFoldingCriteria(), object, gs) && !ff.getSubnodes() && abKey.equals("de.grogra.imp.fillColor")) {
                            return new Color3f(0, 0, 0);
                        }
                    }
                }
            }
        }
        if (accessor.getType().toString().equals("int")) {
            return new Integer(super.getInt(object, accessor, gs));
        } else {
            return super.getObject(object, accessor, placeIn, gs);
        }
    }

    protected boolean checkCriteria(LinkedList criteria, Object object, GraphState gs) {
        Pattern starPattern = Pattern.compile("\\*");
        Attribute[] objectAttr = source.getAttributes(object, true);
        boolean matchesCriterium = false;
        for (int i = 0; i < criteria.size(); i++) {
            matchesCriterium = false;
            Hashtable criterion = (Hashtable) criteria.get(i);
            Enumeration e = criterion.keys();
            while (e.hasMoreElements()) {
                String criterionName = (String) e.nextElement();
                Object criterionValue = (String) ((CompareValue) criterion.get(criterionName)).getValue();
                char compare = (char) ((CompareValue) criterion.get(criterionName)).getCompare();
                for (int j = 0; j < objectAttr.length; j++) {
                    if (objectAttr[j].getType().toString().equals("java.lang.String")) if (criterionName.equals(objectAttr[j].getKey())) {
                        switch(compare) {
                            case '=':
                            case '!':
                                if (objectAttr[j].getType().toString().equals("java.lang.String")) {
                                    Matcher m = starPattern.matcher((String) criterionValue);
                                    if (m.find() && objectAttr[j].getType().toString().equals("java.lang.String")) {
                                        Pattern pointPattern = Pattern.compile("\\.");
                                        Matcher n = pointPattern.matcher((String) criterionValue);
                                        String newCriterionValue = n.replaceAll("\\\\.");
                                        String[] test = starPattern.split(newCriterionValue);
                                        String regExp = "";
                                        if (((String) criterionValue).charAt(0) != '*') {
                                            regExp += "^";
                                        } else {
                                            regExp += "^.*?";
                                        }
                                        regExp += test[0];
                                        for (int k = 1; k < test.length; k++) {
                                            if (test[k].length() > 0) {
                                                regExp += ".*?" + test[k];
                                            }
                                        }
                                        if (((String) criterionValue).charAt(((String) criterionValue).length() - 1) != '*') {
                                            regExp += "$";
                                        } else {
                                            regExp += ".*?$";
                                        }
                                        Pattern regExpPattern = Pattern.compile(regExp);
                                        Matcher regExpMatcher = regExpPattern.matcher((String) objectAttr[j].get(object, true, getSourceState(gs)));
                                        boolean regExpFound = regExpMatcher.find();
                                        if (objectAttr[j].getKey().equals(criterionName) && ((compare == '=' && regExpFound == true) || (compare == '!' && regExpFound == false))) {
                                            matchesCriterium = true;
                                        } else {
                                        }
                                        m.reset();
                                        n.reset();
                                        regExpMatcher.reset();
                                    } else {
                                        if ((objectAttr[j].getKey().equals(criterionName)) && ((objectAttr[j].get(object, true, getSourceState(gs)).equals(criterionValue) && compare == '=') || (!objectAttr[j].get(object, true, getSourceState(gs)).equals(criterionValue) && compare == '!'))) {
                                            matchesCriterium = true;
                                        }
                                    }
                                } else {
                                    if (objectAttr[j].getKey().equals(criterionName) && ((objectAttr[j].get(object, true, getSourceState(gs)).equals(criterionValue) && compare == '=') || (!objectAttr[j].get(object, true, getSourceState(gs)).equals(criterionValue) && compare == '!'))) {
                                        matchesCriterium = true;
                                    }
                                }
                                break;
                            case '<':
                                if (objectAttr[j].getType().toString().equals("int") || objectAttr[j].getType().toString().equals("float") || objectAttr[j].getType().toString().equals("double")) {
                                    if (((Double) objectAttr[j].get(object, true, gs)).doubleValue() < ((Double) criterionValue).doubleValue()) {
                                        matchesCriterium = true;
                                    }
                                } else {
                                }
                                break;
                            case '>':
                                if (objectAttr[j].getType().toString().equals("int") || objectAttr[j].getType().toString().equals("float") || objectAttr[j].getType().toString().equals("double")) {
                                    if (((Double) objectAttr[j].get(object, true, gs)).doubleValue() > ((Double) criterionValue).doubleValue()) {
                                        matchesCriterium = true;
                                    }
                                } else {
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            if (matchesCriterium) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getLifeCycleState(Object object, boolean asNode) {
        GraphState gs = GraphState.current(this);
        LinkedList filterHides = hfd.getFilterHides();
        for (int i = 0; i < filterHides.size(); i++) {
            FilterHide fh = (FilterHide) (filterHides.get(i));
            if (checkCriteria(fh.getHideCriteria(), object, gs) && fh.getSubnodes()) {
                return TRANSIENT;
            } else if (checkCriteria(fh.getHideCriteria(), object, gs) && !fh.getSubnodes()) {
                return TRANSIENT;
            }
        }
        LinkedList filterFoldings = hfd.getFilterFoldings();
        for (int i = 0; i < filterFoldings.size(); i++) {
            FilterFolding ff = (FilterFolding) filterFoldings.get(i);
            if (checkCriteria(ff.getFoldingCriteria(), object, gs) && ff.getSubnodes()) {
                return TRANSIENT;
            } else if (checkCriteria(ff.getFoldingCriteria(), object, gs) && !ff.getSubnodes()) {
            }
        }
        return super.getLifeCycleState(object, asNode);
    }

    public void changeSubNodes(Object node, String abKey, Hashtable highlightResults) {
    }
}
