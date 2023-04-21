package org.jcvi.vics.web.gwt.detail.client.bse;

/**
 * The class encapsulates the data and operations needed to render GenericPanel
 *
 * @author Tareq Nabeel
 */
public class GenericPanel extends BSEntityPanel {

    private static final String DETAIL_TYPE = "GenericService";

    /**
     * The label to display for entity id for TitleBox and error/debug messages e.g. "ORF" or "NCBI"
     *
     * @return The label to display for entity id for TitleBox and error/debug messages
     */
    public String getDetailTypeLabel() {
        return DETAIL_TYPE;
    }
}
