package com.tucows.oxrs.epprtk.rtk.xml.poll;

import com.tucows.oxrs.epprtk.rtk.xml.EPPXMLBase;
import com.tucows.oxrs.epprtk.rtk.xml.EPPDomainBase;
import org.openrtk.idl.epprtk.*;
import org.openrtk.idl.epprtk.domain.epp_DomainPanData;
import org.w3c.dom.*;

/**
 * An implementation of PollResData for contact transfer poll
 * response data.
 * Populates the epp_PollResData with the epp_PollResDataUnion
 * and m_domain_transfer member set.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/com/tucows/oxrs/epprtk/rtk/xml/poll/domainpanData.java,v 1.1 2004/12/07 22:44:07 ewang2004 Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2004/12/07 22:44:07 $<br>
 */
public class domainpanData extends EPPXMLBase implements PollResData {

    private epp_DomainPanData poll_res_data_;

    /**
     * Populates the poll res data with epp_PollDomainTransfer
     * data.
     */
    public void fromXML(Node res_data_node) throws epp_XMLException {
        String method_name = "fromXML(Node)";
        NodeList domain_pan_data_list = res_data_node.getChildNodes();
        poll_res_data_ = new epp_DomainPanData();
        debug(DEBUG_LEVEL_TWO, method_name, "domain:panData's node count [" + domain_pan_data_list.getLength() + "]");
        if (domain_pan_data_list.getLength() == 0) {
            throw new epp_XMLException("missing domain pan data");
        }
        String domain_name = EPPXMLBase.getPanData(domain_pan_data_list, res_data_node.getNamespaceURI(), (epp_PanData) poll_res_data_);
        poll_res_data_.setName(domain_name);
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
