package org.monet.kernel.model.definition;

import java.util.ArrayList;
import java.util.HashMap;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "field-section")
public class SectionFieldDeclaration extends FieldDeclaration {

    public static class IsExtensible {
    }

    public static class IsConditional {
    }

    @Element(name = "is-extensible", required = false)
    protected IsExtensible isExtensible;

    @Element(name = "is-conditional", required = false)
    protected IsConditional isConditional;

    @ElementList(inline = true)
    protected ArrayList<BooleanFieldDeclaration> booleanFieldDeclarationList = new ArrayList<BooleanFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<CheckFieldDeclaration> checkFieldDeclarationList = new ArrayList<CheckFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<DateFieldDeclaration> dateFieldDeclarationList = new ArrayList<DateFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<FileFieldDeclaration> fileFieldDeclarationList = new ArrayList<FileFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<LinkFieldDeclaration> linkFieldDeclarationList = new ArrayList<LinkFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<MemoFieldDeclaration> memoFieldDeclarationList = new ArrayList<MemoFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<NodeFieldDeclaration> nodeFieldDeclarationList = new ArrayList<NodeFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<NumberFieldDeclaration> numberFieldDeclarationList = new ArrayList<NumberFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<PatternFieldDeclaration> patternFieldDeclarationList = new ArrayList<PatternFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<PictureFieldDeclaration> pictureFieldDeclarationList = new ArrayList<PictureFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<SectionFieldDeclaration> sectionFieldDeclarationList = new ArrayList<SectionFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<SelectFieldDeclaration> selectFieldDeclarationList = new ArrayList<SelectFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<SerialFieldDeclaration> serialFieldDeclarationList = new ArrayList<SerialFieldDeclaration>();

    @ElementList(inline = true)
    protected ArrayList<TextFieldDeclaration> textFieldDeclarationList = new ArrayList<TextFieldDeclaration>();

    public boolean isExtensible() {
        return (isExtensible != null);
    }

    public boolean isConditional() {
        return (isConditional != null);
    }

    public ArrayList<BooleanFieldDeclaration> getBooleanFieldDeclarationList() {
        return booleanFieldDeclarationList;
    }

    public ArrayList<CheckFieldDeclaration> getCheckFieldDeclarationList() {
        return checkFieldDeclarationList;
    }

    public ArrayList<DateFieldDeclaration> getDateFieldDeclarationList() {
        return dateFieldDeclarationList;
    }

    public ArrayList<FileFieldDeclaration> getFileFieldDeclarationList() {
        return fileFieldDeclarationList;
    }

    public ArrayList<LinkFieldDeclaration> getLinkFieldDeclarationList() {
        return linkFieldDeclarationList;
    }

    public ArrayList<MemoFieldDeclaration> getMemoFieldDeclarationList() {
        return memoFieldDeclarationList;
    }

    public ArrayList<NodeFieldDeclaration> getNodeFieldDeclarationList() {
        return nodeFieldDeclarationList;
    }

    public ArrayList<NumberFieldDeclaration> getNumberFieldDeclarationList() {
        return numberFieldDeclarationList;
    }

    public ArrayList<PatternFieldDeclaration> getPatternFieldDeclarationList() {
        return patternFieldDeclarationList;
    }

    public ArrayList<PictureFieldDeclaration> getPictureFieldDeclarationList() {
        return pictureFieldDeclarationList;
    }

    public ArrayList<SectionFieldDeclaration> getSectionFieldDeclarationList() {
        return sectionFieldDeclarationList;
    }

    public ArrayList<SelectFieldDeclaration> getSelectFieldDeclarationList() {
        return selectFieldDeclarationList;
    }

    public ArrayList<SerialFieldDeclaration> getSerialFieldDeclarationList() {
        return serialFieldDeclarationList;
    }

    public ArrayList<TextFieldDeclaration> getTextFieldDeclarationList() {
        return textFieldDeclarationList;
    }

    protected ArrayList<FieldDeclaration> fieldDeclarationList;

    protected void createFieldDeclarationList() {
        fieldDeclarationList = new ArrayList<FieldDeclaration>();
        fieldDeclarationList.addAll(booleanFieldDeclarationList);
        fieldDeclarationList.addAll(checkFieldDeclarationList);
        fieldDeclarationList.addAll(dateFieldDeclarationList);
        fieldDeclarationList.addAll(fileFieldDeclarationList);
        fieldDeclarationList.addAll(linkFieldDeclarationList);
        fieldDeclarationList.addAll(memoFieldDeclarationList);
        fieldDeclarationList.addAll(nodeFieldDeclarationList);
        fieldDeclarationList.addAll(numberFieldDeclarationList);
        fieldDeclarationList.addAll(patternFieldDeclarationList);
        fieldDeclarationList.addAll(pictureFieldDeclarationList);
        fieldDeclarationList.addAll(sectionFieldDeclarationList);
        fieldDeclarationList.addAll(selectFieldDeclarationList);
        fieldDeclarationList.addAll(serialFieldDeclarationList);
        fieldDeclarationList.addAll(textFieldDeclarationList);
    }

    public ArrayList<FieldDeclaration> getFieldDeclarationList() {
        if (fieldDeclarationList == null) createFieldDeclarationList();
        return fieldDeclarationList;
    }

    protected HashMap<String, FieldDeclaration> fieldDeclarationMap;

    protected void createFieldDeclarationMap() {
        fieldDeclarationMap = new HashMap<String, FieldDeclaration>();
        if (fieldDeclarationList == null) createFieldDeclarationList();
        for (FieldDeclaration item : fieldDeclarationList) {
            fieldDeclarationMap.put(item.getCode(), item);
        }
    }

    public FieldDeclaration getFieldDeclaration(String code) {
        if (fieldDeclarationMap == null) createFieldDeclarationMap();
        return fieldDeclarationMap.get(code);
    }
}
