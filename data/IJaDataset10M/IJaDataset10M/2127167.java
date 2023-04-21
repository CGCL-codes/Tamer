package com.sodad.weka.gui.beans;

import java.beans.*;

/**
 * Bean info class for the text viewer
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version $Revision: 1.3 $
 */
public class TextViewerBeanInfo extends SimpleBeanInfo {

    /**
   * Get the event set descriptors for this bean
   *
   * @return an <code>EventSetDescriptor[]</code> value
   */
    public EventSetDescriptor[] getEventSetDescriptors() {
        try {
            EventSetDescriptor[] esds = { new EventSetDescriptor(TextViewer.class, "text", TextListener.class, "acceptText") };
            return esds;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
