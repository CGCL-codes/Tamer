package org.merlotxml.util.xml.xerces;

import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLContentSpec;
import org.apache.xerces.impl.dtd.XMLElementDecl;
import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.xni.grammars.Grammar;
import org.merlotxml.merlot.MerlotDebug;
import org.merlotxml.util.xml.GrammarComplexType;
import org.merlotxml.util.xml.GrammarDocument;

public class DTDGrammarDocumentImpl implements GrammarDocument {

    private DTDGrammarComplexTypeImpl[] _complexTypes = new DTDGrammarComplexTypeImpl[0];

    private DTDGrammar _grammar = null;

    private Grammar[] _grammars = null;

    private Hashtable<String, GrammarComplexType> _namedComplexTypes = new Hashtable<String, GrammarComplexType>();

    public DTDGrammarDocumentImpl(Grammar[] grammars) {
        _grammars = grammars;
        _grammar = (DTDGrammar) _grammars[0];
        int index = _grammar.getFirstElementDeclIndex();
        MerlotDebug.msg("First element decl index: " + index);
        Vector<DTDGrammarComplexTypeImpl> complexTypes = new Vector<DTDGrammarComplexTypeImpl>();
        while (index != -1) {
            XMLElementDecl elementDecl = new XMLElementDecl();
            boolean found = _grammar.getElementDecl(index, elementDecl);
            if (found) {
                DTDGrammarComplexTypeImpl complexType = new DTDGrammarComplexTypeImpl(this, index, elementDecl);
                complexTypes.add(complexType);
                _namedComplexTypes.put(complexType.getName(), complexType);
            }
            index = _grammar.getNextElementDeclIndex(index);
            MerlotDebug.msg("Next element decl index: " + index);
        }
        _complexTypes = complexTypes.toArray(_complexTypes);
    }

    public GrammarComplexType getGrammarComplexType(GrammarComplexType parent, String name) {
        return getTopLevelGrammarComplexType(name);
    }

    public GrammarComplexType getTopLevelGrammarComplexType(String name) {
        return _namedComplexTypes.get(name);
    }

    public GrammarComplexType[] getTopLevelGrammarComplexTypes() {
        return _complexTypes;
    }

    protected void analyzeContentSpecs() {
        int index = 0;
        XMLContentSpec spec = new XMLContentSpec();
        boolean found = _grammar.getContentSpec(index, spec);
        while (found) {
            String class1 = "";
            if (spec.value != null) {
                class1 = spec.value.getClass().getName();
                if (spec.value instanceof CMNode) MerlotDebug.msg(class1 + " is a CMNode");
            }
            String class2 = "";
            if (spec.otherValue != null) {
                class2 = spec.otherValue.getClass().getName();
                if (spec.otherValue instanceof CMNode) MerlotDebug.msg(class2 + " is a CMNode");
            }
            MerlotDebug.msg("XMLContentSpec: type=" + spec.type + " value=" + class1 + "(" + spec.value + ") otherValue=" + class2 + "(" + spec.otherValue + ")");
            index++;
            found = _grammar.getContentSpec(index, spec);
        }
    }

    protected DTDGrammar getGrammar() {
        return _grammar;
    }
}
