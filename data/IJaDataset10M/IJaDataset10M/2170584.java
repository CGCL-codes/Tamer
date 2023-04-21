package com.cbsgmbh.xi.af.edifact.module.transform.bean;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import javax.naming.NamingException;
import com.cbsgmbh.xi.af.audit.helpers.Audit;
import com.cbsgmbh.xi.af.edifact.module.transform.configuration.ConfigurationFactory;
import com.cbsgmbh.xi.af.edifact.module.transform.configuration.ConfigurationSettings;
import com.cbsgmbh.xi.af.edifact.module.transform.util.RequestMessage;
import com.cbsgmbh.xi.af.edifact.module.transform.xmltoedifact.EdifactMsgBean;
import com.cbsgmbh.xi.af.edifact.module.transform.xmltoedifact.XmlToMessageController;
import com.cbsgmbh.xi.af.edifact.module.transform.xsdStore.SchemaStore;
import com.cbsgmbh.xi.af.edifact.util.EdifactUtil;
import com.cbsgmbh.xi.af.edifact.util.MessageIdMapper;
import com.cbsgmbh.xi.af.edifact.util.Transformer;
import com.cbsgmbh.xi.af.http.util.HTTPUtil;
import com.cbsgmbh.xi.af.trace.helpers.BaseTracer;
import com.cbsgmbh.xi.af.trace.helpers.BaseTracerSapImpl;
import com.cbsgmbh.xi.af.trace.helpers.Tracer;
import com.cbsgmbh.xi.af.trace.helpers.TracerCategories;
import com.sap.aii.af.mp.module.ModuleContext;
import com.sap.aii.af.mp.module.ModuleData;
import com.sap.aii.af.mp.module.ModuleException;
import com.sap.aii.af.ra.ms.api.InvalidParamException;
import com.sap.aii.af.ra.ms.api.Message;
import com.sap.aii.af.ra.ms.api.Payload;
import com.sap.aii.af.ra.ms.api.PayloadFormatException;
import com.sap.aii.af.ra.ms.api.RecoverableException;
import com.sap.aii.af.ra.ms.api.XMLPayload;
import com.sap.aii.af.service.cpa.CPAException;
import com.sap.aii.af.service.headermapping.HeaderMappingException;

public class MsgFromXiTransformController implements TransformController {

    protected SchemaStore schemaStore;

    protected Message message;

    protected ModuleData inputModuleData;

    protected ModuleContext moduleContext;

    protected Audit audit;

    protected RequestMessage requestMessage;

    protected XMLPayload payload;

    protected final MessageIdMapper mapper;

    protected final ConfigurationFactory configurationFactory;

    private static final String ADAPTER_STATUS_INACTIVE = "inactive";

    private static final String TRACE_EXC_RAISED = "Exception was raised in TRANSFORM Module: ";

    public static final String VERSION_ID = "$Id://OPI2_EDIFACT_TransformModule/com/cbsgmbh/opi2/xi/af/edifact/module/transform/MsgFromXiTransformController.java#1 $";

    private static final BaseTracer baseTracer = new BaseTracerSapImpl(VERSION_ID, TracerCategories.APP_TRANSFORM);

    public MsgFromXiTransformController(SchemaStore schemaStore, Message message, ModuleData inputModuleData, ModuleContext moduleContext, com.cbsgmbh.xi.af.audit.helpers.Audit audit, RequestMessage requestMessage, XMLPayload payload, MessageIdMapper mapper, ConfigurationFactory configurationFactory) {
        this.schemaStore = schemaStore;
        this.message = message;
        this.inputModuleData = inputModuleData;
        this.moduleContext = moduleContext;
        this.audit = audit;
        this.requestMessage = requestMessage;
        this.mapper = mapper;
        this.configurationFactory = configurationFactory;
    }

    public void transform() throws ModuleException, CPAException, RecoverableException, GeneralSecurityException, InvalidParamException, PayloadFormatException {
        final Tracer tracer = baseTracer.entering("transform()");
        XmlToMessageController xmlToMessageController = new XmlToMessageController(this.mapper);
        String messageType = xmlToMessageController.getMessageType(this.requestMessage.getBytes());
        if ((messageType == null) || (!messageType.equals(EdifactUtil.MSG_TYPE_EDIFACT))) {
            String errorMessage;
            if (messageType == null || messageType.trim().equals("")) errorMessage = "ERROR: MessageType is missing."; else errorMessage = "ERROR: Invalid MessageType. Value is " + messageType;
            audit.logError(TRACE_EXC_RAISED + errorMessage);
            ModuleException me = new ModuleException(errorMessage);
            tracer.error(TRACE_EXC_RAISED + errorMessage);
            tracer.throwing(me);
            throw me;
        }
        ConfigurationSettings configuration;
        try {
            configuration = this.configurationFactory.createConfiguration(this.schemaStore, this.moduleContext, this.message, this.inputModuleData);
        } catch (NamingException e) {
            tracer.error("JNDI lookup failed");
            tracer.catched(e);
            audit.logCatched(e);
            throw new ModuleException("Could not retrieve JNDI configuration", e);
        } catch (HeaderMappingException e) {
            tracer.error("Header mapping failed");
            tracer.catched(e);
            audit.logCatched(e);
            throw new ModuleException("Header mapping failed", e);
        } catch (Exception e) {
            tracer.catched(e);
            audit.logCatched(e);
            throw new ModuleException("Configuration lookup failed", e);
        }
        String adapterStatus = configuration.getAdapterStatus();
        if ((adapterStatus != null) && (adapterStatus.equalsIgnoreCase(ADAPTER_STATUS_INACTIVE))) {
            String errorMessage = "Message rejected by TransformModule. Channel status is set to inactive.";
            tracer.warn(errorMessage);
            RecoverableException rece = new RecoverableException(errorMessage);
            tracer.throwing(rece);
            throw rece;
        }
        EdifactMsgBean responseMessage = xmlToMessageController.convertXmlToMessage(this.requestMessage, configuration);
        byte[] response = responseMessage.getBytes();
        String unbControlNumber = responseMessage.getUnbControlNumber();
        if ((configuration.getAdapterType() != null) && (configuration.getAdapterType().equals(EdifactUtil.ADAPTER_NAME))) {
            response = Transformer.encodeBase64(response);
            tracer.info("BASE64 encoded data: {0}", new String(response));
        }
        this.message.getDocument().setContent(response);
        tracer.info("XMLPayload payload assigned to Message message main payload");
        this.inputModuleData.setPrincipalData(this.message);
        this.audit.logSuccess("XML has been transformed to flat format.");
        if ((configuration.getAdapterType() != null) && (configuration.getAdapterType().equals(EdifactUtil.ADAPTER_NAME))) {
            HashMap additionalData = new HashMap();
            additionalData.put(EdifactUtil.MSG_TYPE, messageType);
            tracer.info("HashMap additionalData.put({0}, {1})", new Object[] { EdifactUtil.MSG_TYPE, messageType });
            if (messageType.equals(EdifactUtil.MSG_TYPE_EDIFACT)) {
                additionalData.put(EdifactUtil.UNB_CTRL_NO, unbControlNumber);
                tracer.info("HashMap additionalData.put({0}, {1})", new Object[] { EdifactUtil.UNB_CTRL_NO, unbControlNumber });
            }
            additionalData.put(EdifactUtil.UNB_PARTY_FROM, configuration.getFromUnbParty());
            tracer.info("HashMap additionalData.put({0}, {1})", new Object[] { EdifactUtil.UNB_PARTY_FROM, configuration.getFromUnbParty() });
            additionalData.put(EdifactUtil.UNB_PARTY_TO, configuration.getToUnbParty());
            tracer.info("HashMap additionalData.put({0}, {1})", new Object[] { EdifactUtil.UNB_PARTY_TO, configuration.getToUnbParty() });
            additionalData.put(EdifactUtil.UNB_SENDER_QUALIFIER, configuration.getUnbSenderIdQualifier());
            tracer.info("HashMap additionalData.put({0}, {1})", new Object[] { EdifactUtil.UNB_SENDER_QUALIFIER, configuration.getUnbSenderIdQualifier() });
            additionalData.put(EdifactUtil.UNB_RECEIVER_QUALIFIER, configuration.getUnbReceiverIdQualifier());
            tracer.info("HashMap additionalData.put({0}, {1})", new Object[] { EdifactUtil.UNB_RECEIVER_QUALIFIER, configuration.getUnbReceiverIdQualifier() });
            additionalData.put(HTTPUtil.AS2_FROM, configuration.getFromAS2Party());
            tracer.info("HashMap additionalData.put({0}, {1})", new Object[] { HTTPUtil.AS2_FROM, configuration.getFromAS2Party() });
            additionalData.put(HTTPUtil.AS2_TO, configuration.getToAS2Party());
            tracer.info("HashMap additionalData.put({0}, {1})", new Object[] { HTTPUtil.AS2_TO, configuration.getToAS2Party() });
            Payload additionalDataAttachment = this.message.createPayload();
            additionalDataAttachment.setName(EdifactUtil.ATTACHMENT_CONFIG);
            additionalDataAttachment.setContent(additionalData.toString().getBytes());
            tracer.info("Attachment for additional data created {0}", new Object[] { additionalDataAttachment });
            this.message.addAttachment(additionalDataAttachment);
            tracer.info("message.addAttachment(additionalDataAttachment) with content " + additionalData.toString());
        }
        tracer.leaving();
    }
}
