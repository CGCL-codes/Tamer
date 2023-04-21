package org.gudy.azureus2.plugins.ui.config;

/**
 * @author parg
 *
 */
public interface BooleanParameter extends EnablerParameter {

    public boolean getValue();

    public void setValue(boolean b);
}
