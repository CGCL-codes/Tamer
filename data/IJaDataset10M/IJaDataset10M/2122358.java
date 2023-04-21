package org.xaware.schemanavigator.extensions;

import org.eclipse.ui.IViewPart;

/**
 * This is the required interface used by the Export extension when the action is invoked
 * 
 * @author hcurtis
 * 
 */
public interface ICustomExporter {

    /**
     * When the action of the supplied extension point is invoked, the action will execute the performExport method with
     * the supplied arguments
     * 
     * @param xmlInstance -
     *            A Byte array of the text representation of the selected XML instance to export
     * @param viewPart -
     *            The view the action was initiated from
     */
    void performExport(byte xmlInstance[], IViewPart viewPart);
}
