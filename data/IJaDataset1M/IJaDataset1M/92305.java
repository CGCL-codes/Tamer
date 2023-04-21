package fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp;

/**
 * A representation for a range in a document where an enumeration literal (i.e.,
 * a set of static strings) is expected.
 */
public class AlfExpectedEnumerationTerminal extends fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.mopp.AlfAbstractExpectedElement {

    private fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.grammar.AlfEnumerationTerminal enumerationTerminal;

    public AlfExpectedEnumerationTerminal(fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.grammar.AlfEnumerationTerminal enumerationTerminal) {
        super(enumerationTerminal.getMetaclass());
        this.enumerationTerminal = enumerationTerminal;
    }

    public java.util.Set<String> getTokenNames() {
        java.util.Set<String> tokenNames = new java.util.LinkedHashSet<String>();
        java.util.Map<String, String> mapping = enumerationTerminal.getLiteralMapping();
        for (String literalName : mapping.keySet()) {
            String text = mapping.get(literalName);
            if (text != null && !"".equals(text)) {
                tokenNames.add("'" + text + "'");
            }
        }
        return tokenNames;
    }

    public fr.inria.papyrus.uml4tst.emftext.alf.resource.alf.grammar.AlfEnumerationTerminal getEnumerationTerminal() {
        return this.enumerationTerminal;
    }
}
