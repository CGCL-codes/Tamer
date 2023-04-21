package com.wgo.precise.client.ui.controller;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Customized status for Precise plugin.
 * 
 * @see org.eclipse.core.runtime
 * 
 * @author Petter L. H. Eide <petter@eide.biz>
 * @version $Id: ModelStatus.java,v 1.1 2006-01-23 19:58:37 petterei Exp $
 */
public class ModelStatus extends Status implements IStatus {

    public ModelStatus(int severity, String message, Throwable exception) {
        super(severity, Registry.PLUGIN_ID, IStatus.OK, message, exception);
    }

    public ModelStatus(int severity, String message) {
        this(severity, message, null);
    }
}
