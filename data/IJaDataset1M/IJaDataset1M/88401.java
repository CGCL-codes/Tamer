package org.xteam.sled.semantic;

import org.xteam.parser.runtime.Span;
import org.xteam.sled.model.BasePattern;
import org.xteam.sled.semantic.exp.Exp;

public interface IDenotable {

    Exp projectInt(IEvaluationContext context, Span span);

    BasePattern projectPattern(IEvaluationContext context, Span span);
}
