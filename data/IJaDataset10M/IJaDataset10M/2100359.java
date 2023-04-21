package com.ontotext.gate.edlin.sequence;

import java.util.ArrayList;
import java.util.List;
import sequence.SequenceInstance;
import types.Alphabet;
import types.SparseVector;

public class ElementSequence<E> extends SequenceInstance {

    List<Element<E>> elements;

    Integer size;

    public Integer getSize() {
        return elements.size() - 1;
    }

    private gate.Document document;

    private gate.AnnotationSet AS;

    public ElementSequence(Alphabet xAlphabet, Alphabet yAlphabet, SparseVector[] x, Object[] y, gate.Document document, gate.AnnotationSet AS) {
        super(xAlphabet, yAlphabet, x, y);
        elements = new ArrayList<Element<E>>();
        this.document = document;
        this.AS = AS;
    }

    public ElementSequence(List<Element<E>> c, Alphabet xAlphabet, Alphabet yAlphabet, SparseVector[] x, Object[] y, gate.Document document, gate.AnnotationSet AS) {
        super(xAlphabet, yAlphabet, x, y);
        elements = new ArrayList<Element<E>>(c);
        this.document = document;
        this.AS = AS;
    }

    public gate.Document getDocument() {
        return document;
    }

    public void setDocument(gate.Document document) {
        this.document = document;
    }

    public gate.AnnotationSet getAS() {
        return AS;
    }

    public void setAS(gate.AnnotationSet aS) {
        AS = aS;
    }

    public List<Element<E>> getElements() {
        return elements;
    }

    public void setElements(List<Element<E>> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("ElementSequence [elements=\n");
        for (Element<E> element : elements) {
            ret.append(element.toString() + "\n");
        }
        ret.append("]");
        ret.append("\n" + super.toString());
        return ret.toString();
    }
}
