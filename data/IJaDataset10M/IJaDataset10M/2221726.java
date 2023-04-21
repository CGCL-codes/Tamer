package com.tucows.oxrs.epp0402.rtk.xml.poll;

import com.tucows.oxrs.epp0402.rtk.xml.EPPXMLBase;
import com.tucows.oxrs.epp0402.rtk.xml.EPPContactBase;
import org.openrtk.idl.epp0402.*;
import org.openrtk.idl.epp0402.contact.epp_ContactTrnData;
import org.w3c.dom.*;

/**
 * An implementation of PollResData for contact transfer poll
 * response data.
 * Populates the epp_PollResData with the epp_PollResDataUnion
 * and m_contact_transfer member set.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/com/tucows/oxrs/epp0402/rtk/xml/poll/contacttrnData.java,v 1.1 2003/03/21 16:35:37 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/21 16:35:37 $<br>
 */
public class contacttrnData extends EPPXMLBase implements PollResData {

    private epp_ContactTrnData poll_res_data_;

    /**
     * Populates the poll res data with epp_PollContactTransfer
     * data.
     */
    public void fromXML(Node res_data_sub_node) throws epp_XMLException {
        String method_name = "fromXML(Node)";
        NodeList contact_transfer_data_list = res_data_sub_node.getChildNodes();
        poll_res_data_ = new epp_ContactTrnData();
        debug(DEBUG_LEVEL_TWO, method_name, "contact:trnData's node count [" + contact_transfer_data_list.getLength() + "]");
        if (contact_transfer_data_list.getLength() == 0) {
            throw new epp_XMLException("missing contact transfer data");
        }
        poll_res_data_ = EPPContactBase.getTrnData(contact_transfer_data_list);
    }

    /**
     * Returns the epp_PollResData private member.
     * Should only be called after a successful fromXML()
     * otherwise, the poll res data will be null.
     */
    public epp_PollResData getPollResData() {
        return poll_res_data_;
    }
}
