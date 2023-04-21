package org.t2framework.recl.expression;

import org.t2framework.recl.ValidationContext;
import org.t2framework.recl.visitor.Visitor;

public interface Function<CTX extends ValidationContext, RET> {

    RET apply(CTX context);

    void accept(Visitor<CTX> visitor, CTX context);
}
