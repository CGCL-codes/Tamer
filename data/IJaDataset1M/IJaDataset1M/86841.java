package org.spbu.pldoctoolkit.refactor;

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.spbu.pldoctoolkit.dialogs.SelectIntoInfElemDialog;
import org.spbu.pldoctoolkit.parser.DRLLang.DRLDocument;
import org.spbu.pldoctoolkit.parser.DRLLang.Element;
import org.spbu.pldoctoolkit.parser.DRLLang.LangElem;
import org.xml.sax.helpers.AttributesImpl;

public class SplitInfElem {

    public String elemId, elemName, refId;

    public ProjectContent project;

    public IEditorPart editor;

    public PositionInDRL position;

    public PositionInText splitPosition;

    public DRLDocument doc;

    private LangElem targetElem;

    private SelectIntoInfElem firstRefactor, secondRefactor;

    private String prefex;

    private HashMap<String, LangElem> nestsInFirstElem;

    private HashMap<String, LangElem> nestsInSecondElem;

    public SplitInfElem() {
    }

    public boolean validate() {
        position = doc.findByPosition(splitPosition);
        if (position.isInTag) return false;
        Element infElem = position.parent;
        UpwardIterator searchInfElemiterator = new UpwardIterator(infElem);
        while (infElem != null) {
            if (infElem instanceof LangElem) {
                LangElem langElem = (LangElem) infElem;
                if (langElem.tag.equals(LangElem.INFELEMENT)) break;
            }
            infElem = searchInfElemiterator.next();
        }
        if (infElem != null) {
            targetElem = (LangElem) infElem;
            init();
            boolean firstValid = firstRefactor.validate();
            boolean secondValid = secondRefactor.validate();
            boolean isValidSplit = firstValid && secondValid;
            if (!isValidSplit) return false;
            return !targetElemInXorGroup();
        }
        return false;
    }

    private void init() {
        firstRefactor = new SelectIntoInfElem();
        secondRefactor = new SelectIntoInfElem();
        PositionInText firstSonStartPos = targetElem.getChilds().get(0).getStartPos();
        PositionInText lastSonEndPos = targetElem.getChilds().get(targetElem.getChilds().size() - 1).getEndPos();
        firstRefactor.setValidationPararams(project, doc, firstSonStartPos, new PositionInText(splitPosition.line, splitPosition.column));
        secondRefactor.setValidationPararams(project, doc, new PositionInText(splitPosition.line, splitPosition.column), lastSonEndPos);
        firstRefactor.reset();
        secondRefactor.reset();
    }

    private boolean targetElemInXorGroup() {
        String targetElemId = targetElem.attrs.getValue(LangElem.ID);
        for (LangElem ref : project.infElemRefs) {
            int groupIdIdx = ref.attrs.getIndex(LangElem.GROUPID);
            String infElem = ref.attrs.getValue(LangElem.INFELEMID);
            if (groupIdIdx != -1 && infElem.equals(targetElemId)) {
                String targetGruopId = ref.attrs.getValue(groupIdIdx);
                for (LangElem group : project.infElemRefGroups) {
                    String groupId = group.attrs.getValue(LangElem.ID);
                    String modifier = group.attrs.getValue(LangElem.MODIFIER);
                    if (groupId.equals(targetGruopId) && modifier.toUpperCase().equals(LangElem.XOR)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setValidationParams(ProjectContent projectContent, DRLDocument ldoc, PositionInText pos1) {
        this.project = projectContent;
        this.doc = ldoc;
        this.splitPosition = pos1;
    }

    public void setParams(ProjectContent projectContent, DRLDocument ldoc, IEditorPart editor, PositionInText splitPosition) {
        this.project = projectContent;
        this.doc = ldoc;
        this.splitPosition = splitPosition;
        this.editor = editor;
    }

    public void perform() {
        if (validate()) {
            prefex = doc.DRLnsPrefix;
            if (!prefex.equals("")) prefex += ":";
        }
        SelectIntoInfElemDialog dialog1 = new SelectIntoInfElemDialog(editor.getSite().getShell());
        SelectIntoInfElemDialog dialog2 = new SelectIntoInfElemDialog(editor.getSite().getShell());
        for (LangElem refs : project.infElemRefs) {
            dialog1.addRefId(refs.attrs.getValue("id"));
            dialog2.addRefId(refs.attrs.getValue("id"));
        }
        for (LangElem elems : project.infElems) {
            dialog1.addId(elems.attrs.getValue("id"));
            dialog2.addId(elems.attrs.getValue("id"));
        }
        int res = dialog1.open();
        if (res != Window.OK) return;
        firstRefactor.elemId = dialog1.getInfElemId();
        firstRefactor.elemName = dialog1.getInfElemName();
        firstRefactor.refId = dialog1.getInfElemRefId();
        res = dialog2.open();
        if (res != Window.OK) return;
        secondRefactor.elemId = dialog2.getInfElemId();
        secondRefactor.elemName = dialog2.getInfElemName();
        secondRefactor.refId = dialog2.getInfElemRefId();
        ArrayList<Element> firstElemChilds = new ArrayList<Element>();
        ArrayList<Element> secondElemChilds = new ArrayList<Element>();
        for (Element elem : targetElem.getChilds()) {
            if (inFirstElem(elem)) {
                firstElemChilds.add(elem);
            } else {
                secondElemChilds.add(elem);
            }
        }
        distributeNests();
        LangElem parent = (LangElem) targetElem.getParent();
        int idx = parent.getChilds().indexOf(targetElem);
        parent.getChilds().remove(idx);
        LangElem firstInfElem = createNewInfElem(parent, firstRefactor.elemId, firstRefactor.elemName);
        parent.getChilds().add(idx, firstInfElem);
        firstInfElem.setChilds(new ArrayList<Element>());
        firstInfElem.getChilds().addAll(firstElemChilds);
        LangElem secondInfElem = createNewInfElem(parent, secondRefactor.elemId, secondRefactor.elemName);
        Util.addNewLine(parent, idx + 1);
        parent.getChilds().add(idx + 2, secondInfElem);
        secondInfElem.setChilds(new ArrayList<Element>());
        Util.addNewLine(secondInfElem);
        secondInfElem.getChilds().addAll(secondElemChilds);
        for (LangElem infElemRef : getInfElemRefs()) {
            boolean isOptional = false;
            int optionalIdx = infElemRef.attrs.getIndex(LangElem.OPTIONAL);
            if (optionalIdx != -1) {
                isOptional = infElemRef.attrs.getValue(optionalIdx).toLowerCase().equals(LangElem.TRUE);
            }
            String elemRefIdToFind = infElemRef.attrs.getValue(LangElem.ID);
            LangElem refParent = (LangElem) infElemRef.getParent();
            int refIdx = refParent.getChilds().indexOf(infElemRef);
            refParent.getChilds().remove(refIdx);
            LangElem firstRef = createNewInfElemRef(refParent, firstRefactor.refId, firstRefactor.elemId);
            refParent.getChilds().add(refIdx, firstRef);
            LangElem secondRef = createNewInfElemRef(refParent, secondRefactor.refId, secondRefactor.elemId);
            Util.addNewLine(refParent, refIdx + 1);
            refParent.getChilds().add(refIdx + 2, secondRef);
            if (isOptional) {
                ((AttributesImpl) firstRef.attrs).addAttribute(LangElem.OPTIONAL, LangElem.OPTIONAL, LangElem.OPTIONAL, LangElem.OPTIONAL, LangElem.TRUE);
                ((AttributesImpl) secondRef.attrs).addAttribute(LangElem.OPTIONAL, LangElem.OPTIONAL, LangElem.OPTIONAL, LangElem.OPTIONAL, LangElem.TRUE);
            }
            for (LangElem adapter : project.adapters) {
                if (adapter.attrs.getValue(LangElem.INFELEMREFID).equals(elemRefIdToFind)) {
                    ArrayList<LangElem> firstElemNestsRefs = new ArrayList<LangElem>();
                    ArrayList<LangElem> secondElemNestsRefs = new ArrayList<LangElem>();
                    for (Element elem : adapter.getChilds()) {
                        if (elem instanceof LangElem) {
                            LangElem nestRef = (LangElem) elem;
                            String nestid = nestRef.attrs.getValue(LangElem.NESTID);
                            if (nestsInFirstElem.get(nestid) != null) firstElemNestsRefs.add(nestRef);
                            if (nestsInSecondElem.get(nestid) != null) secondElemNestsRefs.add(nestRef);
                        }
                    }
                    LangElem adapterParent = (LangElem) adapter.getParent();
                    int adapterIdx = adapterParent.getChilds().indexOf(adapter);
                    adapterParent.getChilds().remove(adapterIdx);
                    LangElem newAdapter = createNewAdapter(adapterParent, firstRefactor.refId);
                    adapterParent.getChilds().add(adapterIdx, newAdapter);
                    newAdapter.setChilds(new ArrayList<Element>());
                    newAdapter.getChilds().addAll(firstElemNestsRefs);
                    Util.addNewLine(newAdapter);
                    newAdapter = createNewAdapter(adapterParent, secondRefactor.refId);
                    Util.addNewLine(adapterParent, adapterIdx + 1);
                    adapterParent.getChilds().add(adapterIdx + 2, newAdapter);
                    newAdapter.setChilds(new ArrayList<Element>());
                    newAdapter.getChilds().addAll(secondElemNestsRefs);
                    Util.addNewLine(newAdapter);
                }
            }
        }
    }

    private boolean inFirstElem(Element elem) {
        PositionInText startPos = elem.getStartPos();
        if (startPos.line < splitPosition.line) return true;
        if (startPos.line > splitPosition.line) return false;
        return startPos.column < splitPosition.column;
    }

    public ArrayList<LangElem> getInfElemRefs() {
        ArrayList<LangElem> infElemRefs = new ArrayList<LangElem>();
        String idTofind = ((LangElem) targetElem).attrs.getValue(LangElem.ID);
        for (LangElem infElemRef : project.infElemRefs) {
            if (infElemRef.attrs.getValue("infelemid").equals(idTofind)) infElemRefs.add(infElemRef);
        }
        return infElemRefs;
    }

    private void distributeNests() {
        nestsInFirstElem = new HashMap<String, LangElem>();
        nestsInSecondElem = new HashMap<String, LangElem>();
        for (Element cur : targetElem.getChilds()) {
            if (cur instanceof LangElem) {
                LangElem langElem = (LangElem) cur;
                if (langElem.tag.equals(LangElem.NEST)) {
                    String nestId = langElem.attrs.getValue(LangElem.ID);
                    if (inFirstElem(langElem)) {
                        nestsInFirstElem.put(nestId, langElem);
                    } else {
                        nestsInSecondElem.put(nestId, langElem);
                    }
                }
                TreeIterator treeIterator = new TreeIterator(cur);
                while (treeIterator.hasNext()) {
                    Element elem = treeIterator.next();
                    if (elem instanceof LangElem) {
                        LangElem nest = (LangElem) elem;
                        if (nest.tag.equals(LangElem.NEST)) {
                            String nestId = nest.attrs.getValue(LangElem.ID);
                            if (inFirstElem(elem)) {
                                nestsInFirstElem.put(nestId, langElem);
                            } else {
                                nestsInSecondElem.put(nestId, langElem);
                            }
                        }
                    }
                }
            }
        }
    }

    private LangElem createNewAdapter(LangElem parent, String infElemRefId) {
        LangElem newAdapter = new LangElem("Adapter", prefex + "Adapter", null, parent, parent.getDRLDocument(), new AttributesImpl());
        ((AttributesImpl) newAdapter.attrs).addAttribute("infelemrefid", "infelemrefid", "infelemrefid", "", infElemRefId);
        return newAdapter;
    }

    private LangElem createNewInfElem(LangElem parent, String id, String name) {
        LangElem newInfElem = new LangElem("InfElement", prefex + "InfElement", null, parent, parent.getDRLDocument(), new AttributesImpl());
        ((AttributesImpl) newInfElem.attrs).addAttribute("id", "id", "id", "", id);
        ((AttributesImpl) newInfElem.attrs).addAttribute("name", "name", "name", "", name);
        return newInfElem;
    }

    private LangElem createNewInfElemRef(LangElem parent, String id, String infelemid) {
        LangElem newInfElem = new LangElem("InfElemRef", prefex + "InfElemRef", null, parent, parent.getDRLDocument(), new AttributesImpl());
        ((AttributesImpl) newInfElem.attrs).addAttribute("id", "id", "id", "", id);
        ((AttributesImpl) newInfElem.attrs).addAttribute("infelemid", "infelemid", "infelemid", "", infelemid);
        return newInfElem;
    }
}
