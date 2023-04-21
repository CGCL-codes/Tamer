package org.dbe.studio.core.security.controls;

import org.eclipse.swt.widgets.Listener;

/**
 * @author <a href="mailto:chatark@cs.tcd.ie">Khalid Chatar</a>
 * @author <a href="mailto:Dominik.Dahlem@cs.tcd.ie">Dominik Dahlem</a>
 */
public interface IVirtualControl {

    void clearAll();

    boolean isEnabled();

    boolean isValid();

    boolean performWork();

    boolean performDefault();

    boolean performCancel();

    void setEnabled(boolean p_enable);

    void addListener(int type, Listener p_listener);

    void removeListener(int type, Listener p_listener);
}
