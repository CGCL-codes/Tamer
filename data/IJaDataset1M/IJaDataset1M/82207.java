package jgnash.ui.report;

import org.jfree.report.modules.gui.base.*;

/** Extend PreviewProxyBase to add helper methods
 * 
 * @author Craig Cavanaugh
 */
public class PreviewProxyBaseEx extends PreviewProxyBase {

    public PreviewProxyBaseEx(PreviewProxy proxy) {
        super(proxy);
    }

    /** Method to set zoom factor given a percentage
     * @param zoomFactor new zoomFactor
     */
    public void setZoomFactor(float zoomFactor) {
        for (int i = 0; i < ZOOM_FACTORS.length; i++) {
            if (ZOOM_FACTORS[i] == zoomFactor) {
                this.setZoomFactor(i);
            }
        }
    }
}
