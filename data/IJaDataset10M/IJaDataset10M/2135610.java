package com.vangent.hieos.xtest.transactions.xds;

import com.vangent.hieos.xtest.framework.BasicTransaction;
import com.vangent.hieos.xtest.framework.StepContext;
import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.xutil.hl7.date.Hl7Date;
import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xtest.framework.Linkage;
import com.vangent.hieos.xtest.framework.TestConfig;
import com.vangent.hieos.xutil.soap.SoapActionFactory;
import com.vangent.hieos.xutil.xml.Util;
import com.vangent.hieos.xutil.xml.XMLParser;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

/**
 *
 * @author thumbe
 */
public class StoredQueryTransaction extends QueryTransaction {

    OMElement expected_contents = null;

    OMElement metadata_ele = null;

    boolean is_xca = false;

    /**
     *
     * @param s_ctx
     * @param instruction
     * @param instruction_output
     */
    public StoredQueryTransaction(StepContext s_ctx, OMElement instruction, OMElement instruction_output) {
        super(s_ctx, instruction, instruction_output);
    }

    /**
     *
     * @throws XdsException
     */
    public void run() throws XdsException {
        parseParameters(s_ctx, instruction, instruction_output);
        parseMetadata();
        runSQ();
    }

    /**
     *
     * @param isXca
     */
    public void setIsXCA(boolean isXca) {
        is_xca = isXca;
    }

    /**
     *
     * @return
     * @throws XdsInternalException
     * @throws XdsException
     */
    protected OMElement runSQ() throws XdsInternalException, XdsException {
        OMElement result = null;
        if (this.getClass().getName().endsWith(".StoredQueryTransaction")) {
            parseRegistryEndpoint(TestConfig.defaultRegistry, "RegistryStoredQuery");
        }
        if (metadata_filename == null && metadata == null) {
            throw new XdsInternalException("No MetadataFile element or Metadata element found for QueryTransaction instruction within step " + s_ctx.get("step_id"));
        }
        if (use_id.size() > 0) {
            compileUseIdLinkage(metadata, use_id);
        }
        if (use_object_ref.size() > 0) {
            compileUseObjectRefLinkage(metadata, use_object_ref);
        }
        if (use_xpath.size() > 0) {
            compileUseXPathLinkage(metadata, use_xpath);
        }
        Linkage lnk = new Linkage(instruction_output, metadata);
        lnk.add("$now$", Hl7Date.now());
        lnk.add("$lastyear$", StoredQueryTransaction.lastyear());
        lnk.compileLinkage();
        s_ctx.add_name_value(instruction_output, "InputMetadata", Util.deep_copy(metadata_ele));
        OMNamespace ns = metadata_ele.getNamespace();
        String ns_uri = ns.getNamespaceURI();
        int metadata_type = 0;
        if (ns_uri.equals("urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0")) {
            metadata_type = MetadataTypes.METADATA_TYPE_SQ;
        } else {
            throw new XdsInternalException("Don't understand version of metadata (namespace on root element): " + ns_uri);
        }
        if (parse_metadata) {
            if (!metadata_ele.getLocalName().equals("AdhocQueryRequest")) {
                throw new XdsInternalException("Stored Query Transaction (as coded in testplan step '" + s_ctx.get("step_id") + "') must reference a file containing an AdhocQueryRequest");
            }
        }
        useMtom = false;
        useAddressing = true;
        try {
            this.setMetadata(metadata_ele);
            soapCall();
            result = getSoapResult();
            validate_registry_response_no_set_status(result, metadata_type);
            if (expected_contents != null) {
                String errors = validate_expected_contents(result, metadata_type, expected_contents);
                if (errors.length() > 0) {
                    fail(errors);
                }
            }
            add_step_status_to_output();
        } catch (Exception e) {
            fail(ExceptionUtil.exception_details(e));
        }
        return result;
    }

    /**
     * Return current time (minus 1 year) in HL7 format as YYYYMMDDHHMMSS.  This method
     * has no practical purpose beyond for test support.
     *
     * @return Current time (minus 1 year) in HL7 format.
     */
    private static String lastyear() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        Calendar c = new GregorianCalendar();
        formatter.format("%s%02d%02d%02d%02d%02d", c.get(Calendar.YEAR) - 1, c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        return sb.toString();
    }

    /**
     *
     * @return
     */
    protected String getRequestAction() {
        if (is_xca) {
            return SoapActionFactory.XCA_GATEWAY_CGQ_ACTION;
        } else {
            return SoapActionFactory.XDSB_REGISTRY_SQ_ACTION;
        }
    }

    /**
     * 
     * @throws XdsInternalException
     */
    protected void parseMetadata() throws XdsInternalException {
        if (metadata != null) {
            return;
        }
        if (metadata_filename != null && !metadata_filename.equals("")) {
            metadata_ele = XMLParser.fileToOM(metadata_filename);
        }
        metadata = MetadataParser.noParse(metadata_ele);
    }

    /**
     *
     * @param s_ctx
     * @param instruction
     * @param instruction_output
     * @throws XdsException
     */
    public void parseParameters(StepContext s_ctx, OMElement instruction, OMElement instruction_output) throws XdsException {
        Iterator elements = instruction.getChildElements();
        while (elements.hasNext()) {
            OMElement part = (OMElement) elements.next();
            parseParameter(s_ctx, instruction_output, part);
        }
    }

    /**
     *
     * @param s_ctx
     * @param instruction_output
     * @param part
     * @throws XdsException
     */
    public void parseParameter(StepContext s_ctx, OMElement instruction_output, OMElement part) throws XdsException {
        String part_name = part.getLocalName();
        if (part_name.equals("MetadataFile")) {
            metadata_filename = TestConfig.base_path + part.getText();
            s_ctx.add_name_value(instruction_output, "MetadataFile", metadata_filename);
        } else if (part_name.equals("Metadata")) {
            metadata_filename = "";
            metadata_ele = part.getFirstElement();
        } else if (part_name.equals("ExpectedContents")) {
            expected_contents = part;
            s_ctx.add_name_value(instruction_output, "ExpectedContents", part);
        } else if (part_name.equals("UseXPath")) {
            use_xpath.add(part);
            s_ctx.add_name_value(instruction_output, "UseXRef", part);
        } else if (part_name.equals("UseObjectRef")) {
            use_object_ref.add(part);
            s_ctx.add_name_value(instruction_output, "UseObjectRef", part);
        } else if (part_name.equals("SOAP11")) {
            soap_1_2 = false;
            s_ctx.add_name_value(instruction_output, "SOAP11", part);
        } else if (part_name.equals("SOAP12")) {
            soap_1_2 = true;
            s_ctx.add_name_value(instruction_output, "SOAP12", part);
        } else {
            BasicTransaction rt = this;
            rt.parse_instruction(part);
        }
    }
}
