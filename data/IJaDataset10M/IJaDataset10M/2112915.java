package com.ivis.xprocess.core;

import com.ivis.xprocess.framework.Xelement;

/**
 * Redundant interface - use ParameterizedAction or OgnlExpression
 *
 */
@com.ivis.xprocess.framework.annotations.Element(designator = "OA")
public interface OgnlAction extends Xelement, Nameable {

    public static final String EXPRESSION = "EXPRESSION";
}
