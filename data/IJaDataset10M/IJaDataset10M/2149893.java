package de.knowwe.core.report;

import de.knowwe.core.kdom.AbstractType;

/**
 * Abstract class for a message denoting a serious error in the
 * parsing/compilation process
 * 
 * Will be rendered by the ErrorRenderer specified by getErrorRenderer() of the
 * Type
 * 
 * @see @link {@link AbstractType}
 * 
 * 
 * @author Jochen
 * 
 */
public abstract class KDOMError extends KDOMReportMessage {

    @Override
    public abstract String getVerbalization();
}
