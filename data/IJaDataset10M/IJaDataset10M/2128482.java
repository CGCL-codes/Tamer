package org.spbu.pldoctoolkit.refactor;

import java.util.ArrayList;
import org.spbu.pldoctoolkit.parser.DRLLang.DRLDocument;
import org.spbu.pldoctoolkit.parser.DRLLang.Element;
import org.spbu.pldoctoolkit.parser.DRLLang.LangElem;
import org.xml.sax.helpers.AttributesImpl;

public class MakeRefRequired {

    public ProjectContent project;

    public PositionInDRL from, to;

    public PositionInText startPosition, endPosition;

    public DRLDocument doc;

    private LangElem targetRef;

    private String targetRefId;

    private String prefex;

    public MakeRefRequired() {
    }

    public boolean validate() {
        init();
        from = doc.findByPosition(startPosition);
        to = doc.findByPosition(endPosition);
        boolean selectedBorderInTag = from.isInTag || to.isInTag;
        boolean selectedAllElem = to.parent == from.parent && from.next == to.prev;
        boolean selectedFstLine = to.parent == from.next;
        if (selectedBorderInTag) return false;
        if (selectedAllElem || selectedFstLine) {
            targetRef = (LangElem) from.next;
            String elementType = targetRef.tag;
            if (elementType.equals(LangElem.INFELEMREF)) {
                targetRefId = targetRef.attrs.getValue(LangElem.ID);
                if (!allProductsIncludeRef()) {
                    return false;
                }
                int optionalIdx = targetRef.attrs.getIndex(LangElem.OPTIONAL);
                if (optionalIdx != -1) return !targetRef.attrs.getValue(LangElem.OPTIONAL).toLowerCase().equals(LangElem.FALSE); else {
                    return false;
                }
            }
        }
        return false;
    }

    private void init() {
        from = doc.findByPosition(startPosition);
        to = doc.findByPosition(endPosition);
    }

    public void setValidationParams(ProjectContent projectContent, DRLDocument ldoc, PositionInText pos1, PositionInText pos2) {
        this.project = projectContent;
        this.doc = ldoc;
        this.startPosition = pos1;
        this.endPosition = pos2;
    }

    public void setParams(ProjectContent projectContent, DRLDocument ldoc, PositionInText pos1, PositionInText pos2) {
        this.project = projectContent;
        this.doc = ldoc;
        this.startPosition = pos1;
        this.endPosition = pos2;
    }

    public void perform() {
        if (validate()) {
            prefex = doc.DRLnsPrefix;
            if (!prefex.equals("")) prefex += ":";
            int optionalIdx = targetRef.attrs.getIndex(LangElem.OPTIONAL);
            if (optionalIdx != -1) ((AttributesImpl) targetRef.attrs).removeAttribute(optionalIdx);
            ArrayList<String> infProdIds = findUsingInfProducts();
            for (LangElem finalIP : project.finalInfPrs) {
                if (infProdIds.contains(finalIP.attrs.getValue(LangElem.INFPRODUCTID))) {
                    ArrayList<Element> adaptersForDelete = new ArrayList<Element>();
                    for (Element child : finalIP.getChilds()) {
                        if (child instanceof LangElem) {
                            LangElem adapter = (LangElem) child;
                            if (adapter.tag.equals(LangElem.ADAPTER) && adapter.attrs.getValue(LangElem.INFELEMREFID).equals(targetRefId)) {
                                if (elementHasNoContent(child)) adaptersForDelete.add(child);
                            }
                        }
                    }
                    finalIP.getChilds().removeAll(adaptersForDelete);
                }
            }
        }
    }

    private boolean elementHasNoContent(Element elem) {
        String representation = "";
        for (Element child : elem.getChilds()) {
            representation += child.getTextRepresentation();
        }
        return representation.trim().equals("");
    }

    private boolean allProductsIncludeRef() {
        ArrayList<String> infProds = findUsingInfProducts();
        for (LangElem finalIP : project.finalInfPrs) {
            if (infProds.contains(finalIP.attrs.getValue(LangElem.INFPRODUCTID))) {
                boolean refUsing = false;
                for (Element child : finalIP.getChilds()) {
                    if (child instanceof LangElem) {
                        LangElem adapter = (LangElem) child;
                        if (adapter.tag.equals(LangElem.ADAPTER) && adapter.attrs.getValue(LangElem.INFELEMREFID).equals(targetRefId)) {
                            refUsing = true;
                        }
                    }
                }
                if (!refUsing) {
                    return false;
                }
            }
        }
        return true;
    }

    private ArrayList<String> findUsingInfProducts() {
        ArrayList<String> infProdIds = new ArrayList<String>();
        for (LangElem infProd : project.infPrs) {
            TreeIterator treeIterator = new TreeIterator(infProd);
            while (treeIterator.hasNext()) {
                Element elem = treeIterator.next();
                if (elem instanceof LangElem) {
                    LangElem infElementRef = (LangElem) elem;
                    if (infElementRef.tag.equals(LangElem.INFELEMREF)) {
                        String refId = infElementRef.attrs.getValue(LangElem.ID);
                        if (refId.equals(targetRefId)) {
                            infProdIds.add(infProd.attrs.getValue(LangElem.ID));
                            break;
                        } else {
                            String infElemId = infElementRef.attrs.getValue(LangElem.INFELEMID);
                            for (LangElem infElem : project.infElems) {
                                if (infElem.attrs.getValue(LangElem.ID).equals(infElemId)) {
                                    treeIterator.stack.addAll(infElem.getChilds());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return infProdIds;
    }
}
