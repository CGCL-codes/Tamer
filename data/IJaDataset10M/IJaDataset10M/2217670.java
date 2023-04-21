package org.genxdm.xpath.v10;

public interface XPathToolkit {

    XPathCompiler newXPathCompiler();

    ExprContextStatic newExprContextStaticArgs();

    <N> ExprContextDynamicArgs<N> newExprContextDynamicArgs();
}
