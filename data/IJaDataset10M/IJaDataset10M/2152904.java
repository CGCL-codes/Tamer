package org.mobicents.slee.resource.diameter.gq;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.gq.GqAvpFactory;
import net.java.slee.resource.diameter.gq.events.avp.BindingInformation;
import net.java.slee.resource.diameter.gq.events.avp.BindingInputList;
import net.java.slee.resource.diameter.gq.events.avp.BindingOutputList;
import net.java.slee.resource.diameter.gq.events.avp.FlowGrouping;
import net.java.slee.resource.diameter.gq.events.avp.Flows;
import net.java.slee.resource.diameter.gq.events.avp.GloballyUniqueAddress;
import net.java.slee.resource.diameter.gq.events.avp.MediaComponentDescription;
import net.java.slee.resource.diameter.gq.events.avp.MediaSubComponent;
import net.java.slee.resource.diameter.gq.events.avp.V4TransportAddress;
import net.java.slee.resource.diameter.gq.events.avp.V6TransportAddress;
import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.BindingInformationImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.BindingInputListImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.BindingOutputListImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.DiameterGqAvpCodes;
import org.mobicents.slee.resource.diameter.gq.events.avp.FlowGroupingImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.FlowsImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.GloballyUniqueAddressImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.MediaComponentDescriptionImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.MediaSubComponentImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.V4TransportAddressImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.V6TransportAddressImpl;

/**
 * Implementation of {@link GqAvpFactory}.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Web Ukraine </a>
 */
public class GqAvpFactoryImpl extends DiameterAvpFactoryImpl implements GqAvpFactory {

    DiameterAvpFactory baseAvpFactory;

    public GqAvpFactoryImpl(DiameterAvpFactory baseAvpFactory) {
        this.baseAvpFactory = baseAvpFactory;
    }

    @Override
    public DiameterAvpFactory getBaseFactory() {
        return baseAvpFactory;
    }

    @Override
    public BindingInformation createBindingInformation() {
        return (BindingInformation) AvpUtilities.createAvp(DiameterGqAvpCodes.ETSI_BINDING_INFORMATION, DiameterGqAvpCodes.ETSI_VENDOR_ID, null, BindingInformationImpl.class);
    }

    @Override
    public BindingInputList createBindingInputList() {
        return (BindingInputList) AvpUtilities.createAvp(DiameterGqAvpCodes.ETSI_BINDING_INPUT_LIST, DiameterGqAvpCodes.ETSI_VENDOR_ID, null, BindingInputListImpl.class);
    }

    @Override
    public BindingOutputList createBindingOutputList() {
        return (BindingOutputList) AvpUtilities.createAvp(DiameterGqAvpCodes.ETSI_BINDING_OUTPUT_LIST, DiameterGqAvpCodes.ETSI_VENDOR_ID, null, BindingOutputListImpl.class);
    }

    @Override
    public FlowGrouping createFlowGrouping() {
        return (FlowGrouping) AvpUtilities.createAvp(DiameterGqAvpCodes.TGPP_FLOW_GROUPING, DiameterGqAvpCodes.TGPP_VENDOR_ID, null, FlowGroupingImpl.class);
    }

    @Override
    public Flows createFlows() {
        return (Flows) AvpUtilities.createAvp(DiameterGqAvpCodes.TGPP_FLOWS, DiameterGqAvpCodes.TGPP_VENDOR_ID, null, FlowsImpl.class);
    }

    @Override
    public GloballyUniqueAddress createGloballyUniqueAddress() {
        return (GloballyUniqueAddress) AvpUtilities.createAvp(DiameterGqAvpCodes.ETSI_GLOBALLY_UNIQUE_ADDRESS, DiameterGqAvpCodes.ETSI_VENDOR_ID, null, GloballyUniqueAddressImpl.class);
    }

    @Override
    public MediaComponentDescription createMediaComponentDescription() {
        return (MediaComponentDescription) AvpUtilities.createAvp(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_DESCRIPTION, DiameterGqAvpCodes.TGPP_VENDOR_ID, null, MediaComponentDescriptionImpl.class);
    }

    @Override
    public MediaSubComponent createMediaSubComponent() {
        return (MediaSubComponent) AvpUtilities.createAvp(DiameterGqAvpCodes.TGPP_MEDIA_SUB_COMPONENT, DiameterGqAvpCodes.TGPP_VENDOR_ID, null, MediaSubComponentImpl.class);
    }

    @Override
    public V4TransportAddress createV4TransportAddress() {
        return (V4TransportAddress) AvpUtilities.createAvp(DiameterGqAvpCodes.ETSI_V4_TRANSPORT_ADDRESS, DiameterGqAvpCodes.ETSI_VENDOR_ID, null, V4TransportAddressImpl.class);
    }

    @Override
    public V6TransportAddress createV6TransportAddress() {
        return (V6TransportAddress) AvpUtilities.createAvp(DiameterGqAvpCodes.ETSI_V4_TRANSPORT_ADDRESS, DiameterGqAvpCodes.ETSI_VENDOR_ID, null, V6TransportAddressImpl.class);
    }
}
