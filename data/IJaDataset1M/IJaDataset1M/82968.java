package org.antdepo.common;

/**
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 15, 2005
 * Time: 1:32:31 PM
 */
public interface IObject extends IContext {

    boolean isTypeContext();

    boolean isDepotContext();

    boolean isEmptyContext();
}
