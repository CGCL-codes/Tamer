package com.knitml.validation.visitor.impl;

import java.util.Iterator;
import org.dom4j.Element;
import com.knitml.core.xml.translator.DefaultXmlToModelTranslator;
import com.knitml.core.xml.translator.XmlToModelTranslator;
import com.knitml.validation.common.KnittingEngineException;
import com.knitml.validation.common.NoGapFoundException;
import com.knitml.validation.common.NoMarkerFoundException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.visitor.ElementVisitor;
import com.knitml.validation.visitor.VisitorFactory;

public abstract class AbstractValidationVisitor implements ElementVisitor {

    private VisitorFactory visitorFactory;

    private XmlToModelTranslator translator = new DefaultXmlToModelTranslator();

    public XmlToModelTranslator getTranslator() {
        return translator;
    }

    public void setTranslator(XmlToModelTranslator translator) {
        this.translator = translator;
    }

    @SuppressWarnings("unchecked")
    protected void visitChildren(Element element, KnittingContext context) throws KnittingEngineException {
        Iterator<Element> it = element.elementIterator();
        while (it.hasNext()) {
            Element childElement = it.next();
            ElementVisitor visitor = getVisitorFactory().findVisitorFromTagName(childElement.getName());
            visitor.visit(childElement, context);
        }
    }

    protected int getStitchesBeforeEnd(KnittingContext context) {
        if (context.isNeedleInstructions()) {
            return context.getEngine().getStitchesRemainingOnCurrentNeedle();
        } else {
            return context.getEngine().getStitchesRemainingInRow();
        }
    }

    protected int getStitchesBeforeMarker(KnittingContext context) throws NoMarkerFoundException {
        return context.getEngine().getStitchesToNextMarker();
    }

    protected int getStitchesBeforeGap(KnittingContext context) throws NoGapFoundException {
        return context.getEngine().getStitchesToGap();
    }

    public VisitorFactory getVisitorFactory() {
        return visitorFactory;
    }

    public void setVisitorFactory(VisitorFactory visitorFactory) {
        this.visitorFactory = visitorFactory;
    }
}
