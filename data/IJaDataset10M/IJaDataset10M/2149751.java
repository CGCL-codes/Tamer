package net.sf.julie.grammar;

import java.util.ArrayList;
import java.util.Collection;
import net.sf.julie.Interpretable;
import net.sf.julie.SchemeSystem;

public class VectorQQTemplate extends Vector implements QQElement {

    public VectorQQTemplate(SchemeSystem environment) {
        super(environment);
    }

    public VectorQQTemplate(SchemeSystem environment, Collection<? extends Interpretable> c) {
        super(environment, c);
    }

    public Interpretable interpret(int depth) {
        java.util.List<Interpretable> interpretedElements = new ArrayList<Interpretable>();
        for (Interpretable elem : this) {
            if (elem instanceof SplicingUnquotation) {
                List interpretedElement = (List) ((QQElement) elem).interpret(depth);
                for (Interpretable embeddedElem : interpretedElement) {
                    interpretedElements.add(embeddedElem);
                }
            } else if (elem instanceof QQElement) {
                Interpretable interpretedElement = elem;
                interpretedElement = ((QQElement) elem).interpret(depth);
                interpretedElements.add(interpretedElement);
            } else if (elem instanceof Quotation) {
                Interpretable template = ((Quotation) elem).getDatum();
                Interpretable interpretedTemplate = template.interpret();
                interpretedElements.add(new Quotation(schemeSystem, interpretedTemplate));
            } else {
                interpretedElements.add(elem);
            }
        }
        return new VectorQQTemplate(schemeSystem, interpretedElements);
    }
}
