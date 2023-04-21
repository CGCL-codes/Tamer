package com.knitml.validation.visitor;

import org.dom4j.Element;
import com.knitml.validation.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;

public interface ElementVisitor {

    void visit(Element element, KnittingContext context) throws KnittingEngineException;
}
