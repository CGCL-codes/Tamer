package org.mitre.rt.client.ui.references;

import java.util.*;
import org.apache.log4j.*;
import org.apache.xmlbeans.XmlObject;
import org.mitre.rt.rtclient.*;
import org.mitre.rt.client.xml.*;
import org.mitre.rt.rtclient.RTDocument.RT.*;
import org.mitre.rt.client.ui.*;

/**
 *
 * @author BWORRELL
 */
public class ReferenceRefsTableModel extends AbsXmlObjTableModel {

    private static Logger logger = Logger.getLogger(ReferenceRefsTableModel.class.getPackage().getName());

    public static final int NAME = 0, SOURCE = 1, CREATOR = 2, CREATED_DATE = 3;

    public static final int PUBLIC = 0, INTERNAL = 1;

    /**
   * @param parent
   * @param data    The container element of SharedIdTypes that point to References within an application
   */
    public ReferenceRefsTableModel(ReferencesType parent, XmlObject data) {
        super(parent, data);
    }

    @Override
    protected void createHeaders() {
        super.setColumnHeaders(new String[] { "Name", "Source", "Creator", "Date Added" });
    }

    @Override
    public void processData(XmlObject parent, XmlObject dataContainer) {
        logger.debug("Processing Reference Refs");
        ArrayList<ReferenceType> myData = null;
        List<SharedIdType> referenceRefs = ReferenceHelper.getReferenceRefs(dataContainer);
        ReferencesType references = (ReferencesType) super.getParent();
        try {
            myData = ReferenceHelper.getActiveReferences(references, referenceRefs);
        } catch (Exception ex) {
            logger.warn(ex);
            myData = new ArrayList<ReferenceType>();
        }
        super.setData(myData);
    }
}
