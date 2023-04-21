package serene.validation.schema.parsed;

import java.util.Map;
import serene.bind.util.DocumentIndexedData;

public class DefinitionCopier implements ParsedComponentVisitor {

    Definition copy;

    public DefinitionCopier() {
    }

    public Definition copy(Definition definition) {
        copy = null;
        definition.accept(this);
        return copy;
    }

    public void visit(Define define) {
        int xmlBase = define.getXmlBaseRecordIndex();
        int ns = define.getNsRecordIndex();
        int datatypeLibrary = define.getDatatypeLibraryRecordIndex();
        int name = define.getNameRecordIndex();
        int combine = define.getCombineRecordIndex();
        ParsedComponent[] children = define.getChildren();
        int recordIndex = define.getRecordIndex();
        DocumentIndexedData did = define.getDocumentIndexedData();
        copy = new Define(xmlBase, ns, datatypeLibrary, name, combine, children, recordIndex, did);
    }

    public void visit(Start start) {
        int xmlBase = start.getXmlBaseRecordIndex();
        int ns = start.getNsRecordIndex();
        int datatypeLibrary = start.getDatatypeLibraryRecordIndex();
        int combine = start.getCombineRecordIndex();
        ParsedComponent[] children = start.getChildren();
        int recordIndex = start.getRecordIndex();
        DocumentIndexedData did = start.getDocumentIndexedData();
        copy = new Start(xmlBase, ns, datatypeLibrary, combine, children, recordIndex, did);
    }

    public void visit(Param param) {
        throw new IllegalStateException();
    }

    public void visit(Include include) {
        throw new IllegalStateException();
    }

    public void visit(ExceptPattern exceptPattern) {
        throw new IllegalStateException();
    }

    public void visit(ExceptNameClass exceptNameClass) {
        throw new IllegalStateException();
    }

    public void visit(DivGrammarContent div) {
        throw new IllegalStateException();
    }

    public void visit(DivIncludeContent div) {
        throw new IllegalStateException();
    }

    public void visit(Name name) {
        throw new IllegalStateException();
    }

    public void visit(AnyName anyName) {
        throw new IllegalStateException();
    }

    public void visit(NsName nsName) {
        throw new IllegalStateException();
    }

    public void visit(ChoiceNameClass choice) {
        throw new IllegalStateException();
    }

    public void visit(ElementWithNameClass element) {
        throw new IllegalStateException();
    }

    public void visit(ElementWithNameInstance element) {
        throw new IllegalStateException();
    }

    public void visit(AttributeWithNameClass attribute) {
        throw new IllegalStateException();
    }

    public void visit(AttributeWithNameInstance attribute) {
        throw new IllegalStateException();
    }

    public void visit(ChoicePattern choice) {
        throw new IllegalStateException();
    }

    public void visit(Interleave interleave) {
        throw new IllegalStateException();
    }

    public void visit(Group group) {
        throw new IllegalStateException();
    }

    public void visit(ZeroOrMore zeroOrMore) {
        throw new IllegalStateException();
    }

    public void visit(OneOrMore oneOrMore) {
        throw new IllegalStateException();
    }

    public void visit(Optional optional) {
        throw new IllegalStateException();
    }

    public void visit(ListPattern list) {
        throw new IllegalStateException();
    }

    public void visit(Mixed mixed) {
        throw new IllegalStateException();
    }

    public void visit(Empty empty) {
        throw new IllegalStateException();
    }

    public void visit(Text text) {
        throw new IllegalStateException();
    }

    public void visit(NotAllowed notAllowed) {
        throw new IllegalStateException();
    }

    public void visit(ExternalRef externalRef) {
        throw new IllegalStateException();
    }

    public void visit(Ref ref) {
        throw new IllegalStateException();
    }

    public void visit(ParentRef parentRef) {
        throw new IllegalStateException();
    }

    public void visit(Value value) {
        throw new IllegalStateException();
    }

    public void visit(Data data) {
        throw new IllegalStateException();
    }

    public void visit(Grammar grammar) {
        throw new IllegalStateException();
    }

    public void visit(Dummy dummy) {
        throw new IllegalStateException();
    }

    public void visit(ForeignComponent fg) {
        throw new IllegalStateException();
    }
}
