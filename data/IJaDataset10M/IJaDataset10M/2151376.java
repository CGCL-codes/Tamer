package org.xaware.shared.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import org.jdom.JDOMException;
import org.xaware.shared.util.logging.XAwareLogger;
import org.xaware.testing.util.BaseTestCase;
import org.xaware.testing.util.TestUtils;
import org.xaware.testing.util.XMLComparator;

public class WSDLFileUtilsDocLitTestCase extends BaseTestCase {

    private static final String DATA_DIR = "data/org/xaware/shared/util/wsdlDocLit/";

    private static final String NEW_WSDL_BASE_NAME = "new.wsdl";

    private static final String NEW_WSDL_FULL_NAME = DATA_DIR + NEW_WSDL_BASE_NAME;

    private static final String XAR_FILE_NAME = "Sample.xar";

    private static final String XAR_NO_PATH_FILE_NAME = "SampleRoot.xar";

    private static final String ADD_TO_WSDL_NAME = "addToExistingWsdl.wsdl";

    private static final String OVERWRITE_SERVICE_WSDL_NAME_EXP = "overwriteExistingService_exp.wsdl";

    private static final String ADD_TO_WSDL_CONFLICT_NAME = "addToExistingWsdlNamespaceConflict.wsdl";

    private static final String ADD_TO_WSDL_START_NAME = "addToExistingWsdlStart.wsdl";

    private static final String ADD_TO_WSDL_START_NO_PATH_NAME = "basicBdToWsdlAddRootStart.wsdl";

    private static final String BASIC_WSDL_NAME = "basicBdToWsdl.wsdl";

    private static final String BASIC_NO_PATH_WSDL_NAME = "basicBdToWsdlRoot.wsdl";

    private static final String MULTI_WSDL_NAME = "multipleBdsToWsdl.wsdl";

    private static final String PERSON1_XBD_NAME = "samples/Person1.xbd";

    private static final String PERSON_XBD_NAME = "samples/Person.xbd";

    private static final String PERSON_RSP_XBD_NAME = "samples/PersonRsp.xbd";

    private static final String PERSON_RSP_XBD_NAME_EXP = "PersonRsp_exp.wsdl";

    private static final String PERSON_NO_INPT_PARMS_XBD_NAME = "samples/PersonNoInputParams.xbd";

    private static final String PERSON_NO_INPT_PARMS_XBD_NAME_EXP = "PersonNoInputParams_exp.wsdl";

    private static final String PERSON_CONFLICT_XBD_NAME = "samples/PersonConflictNamespace.xbd";

    private static final String PERSON1_NO_PATH_XBD_NAME = "Person1.xbd";

    private static final String PERSON1_NO_PATH_XBD_NAME_EXP = "Person1_exp.wsdl";

    private static final String PERSON_NO_PATH_XBD_NAME = "Person.xbd";

    private static final String PERSON_NO_NAMESPACE_NO_INPT_RSP_XBD_NAME = "samples/PersonNoNamespaceNoInptRsp.xbd";

    private static final String PERSON_NO_NAMESPACE_NO_INPT_RSP_XBD_NAME_EXP = "PersonNoNamespaceNoInptRsp_exp.wsdl";

    private static final String PERSON_NO_NAMESPACE_SCHEMA_LOC_XBD_NAME = "samples/PersonNoNamespaceSchemaLoc.xbd";

    private static final String PERSON_NO_NAMESPACE_SCHEMA_LOC_XBD_NAME_EXP = "PersonNoNamespaceSchemaLoc_exp.wsdl";

    private static final String PERSON_INPUT_DIFF_FROM_ROOT_XBD_NAME = "samples/PersonInptDiffFromRoot.xbd";

    private static final String PERSON_INPUT_DIFF_FROM_ROOT_XBD_NAME_EXP = "PersonInptDiffFromRoot_exp.wsdl";

    private static final String PERSON_INPUT_DIFF_FROM_ROOT_NO_INPUT_PARAMS_XBD_NAME = "samples/PersonInptDiffFromRootNoInputParams.xbd";

    private static final String PERSON_INPUT_DIFF_FROM_ROOT_NO_INPUT_PARAMS_XBD_NAME_EXP = "PersonInptDiffFromRootNoInputParams_exp.wsdl";

    private static final String PERSON_INPUT_SAME_ROOT_NO_INPUT_PARAMS_XBD_NAME = "samples/PersonInptSameRoot.xbd";

    private static final String PERSON_INPUT_SAME_ROOT_NO_INPUT_PARAMS_XBD_NAME_EXP = "PersonInptSameRoot_exp.wsdl";

    private static final String PERSON_CONFLICT_NAMESPACE_XBD_NAME = "samples/PersonConflictNamespace.xbd";

    private static final String PERSON_CONFLICT_NAMESPACE_XBD_NAME_EXP = "PersonConflictNamespace_exp.wsdl";

    private static final String PERSON_CONFLICT_PREFIX_XBD_NAME = "samples/PersonConflictPrefix.xbd";

    private static final String PERSON_CONFLICT_PREFIX_XBD_NAME_EXP = "PersonConflictPrefix_exp.wsdl";

    private static final String PERSON_CONFLICT_PREFIX1_XBD_NAME = "samples/PersonConflictPrefix1.xbd";

    private static final String PERSON_CONFLICT_PREFIX1_XBD_NAME_EXP = "PersonConflictPrefix1_exp.wsdl";

    private static final String PERSON_CONFLICT_PREFIX2_XBD_NAME = "samples/PersonConflictPrefix2.xbd";

    private static final String PERSON_CONFLICT_PREFIX2_XBD_NAME_EXP = "PersonConflictPrefix2_exp.wsdl";

    private static final String PERSON_CONFLICT_NAMESPACE_DIFF_LOC_XBD_NAME = "samples/PersonConflictNamespaceDiffSchemaLoc.xbd";

    private static final String PERSON_CONFLICT_NAMESPACE_DIFF_LOC_XBD_NAME_EXP = "PersonConflictNamespaceDiffSchemaLoc_exp.wsdl";

    private static final String PERSON_USING_SLASH_INPUT_XBD_NAME = "samples/PersonSlashInput.xbd";

    private static final String PERSON_USING_SLASH_INPUT_XBD_NAME_EXP = "PersonSlashInput_exp.wsdl";

    private static final String PERSON_INPUT_RSP_DIFF_XBD_NAME = "samples/PersonInptRspDiff.xbd";

    private static final String PERSON_INPUT_RSP_DIFF_XBD_NAME_EXP = "PersonInptRspDiff_exp.wsdl";

    private static final String MISSING_NAMESPACE_FAIL_XBD_NAME = "samples/MissingNamespaceInSchemaLocFail.xbd";

    private static final String MISSING_NAMESPACE_FAIL_XBD_NAME_EXP = "JUNK_SHOULD_FAIL";

    private static final String PERSON_INPUT_RSP_DIFF_FAIL_XBD_NAME = "samples/PersonInptRspDiffFail.xbd";

    private static final String PERSON_INPUT_RSP_DIFF_XBD_NAME_FAIL_EXP = "JUNK_SHOULD_FAIL";

    private static final String PERSON_INPUT_RSP_DIFF_NAMESPACE_CONFLIC_XBD_NAME_FAIL = "samples/PersonInptRspDiffNamespaceConflictFail.xbd";

    private static final String PERSON_INPUT_RSP_DIFF_NAMESPACE_CONFLIC_XBD_NAME_FAIL_EXP = "PersonInptRspDiffNamespaceConflictFail_exp.wsdl";

    private static final String CONFLICTING_NAMESPACE_IN_SAME_XBD_NAME = "samples/ConflictingNamespacesInSameBizDoc.xbd";

    private static final String CONFLICTING_NAMESPACE_IN_SAME_XBD_NAME_EXP = "ConflictingNamespacesInSameBizDoc_exp.wsdl";

    private static final String MAP_REQUEST_TO_BDROOT_XBD_NAME = "samples/MapRequestToBDRoot.xbd";

    private static final String MAP_REQUEST_TO_BDROOT_XBD_NAME_EXP = "MapRequestToBDRoot_exp.wsdl";

    private static final String MAP_RESPONSE_TO_BDROOT_XBD_NAME = "samples/MapResponseToBDRoot.xbd";

    private static final String MAP_RESPONSE_TO_BDROOT_XBD_NAME_EXP = "MapResponseToBDRoot_exp.wsdl";

    private static final String PERSON_INPUT_RSP_DIFF_NAMESPACE_CONFLIC_XBD_NAME = "samples/PersonInptRspDiffNamespaceConflict.xbd";

    private static final String PERSON_INPUT_RSP_DIFF_NAMESPACE_CONFLIC_XBD_NAME_EXP = "PersonInptRspDiffNamespaceConflict_exp.wsdl";

    private static final String EMPTY_INPUT_PARAM_XBD_NAME = "samples/EmptyInputParam.xbd";

    private static final String EMPTY_INPUT_PARAM_XBD_NAME_EXP = "EmptyInputParam_exp.wsdl";

    private static final String DATATYPE_TEST_XBD_NAME = "samples/TestDatatypes.xbd";

    private static final String DATATYPE_TEST_XBD_NAME_EXP = "TestDatatypes_exp.wsdl";

    private static final String PERSON_RSP_ANY_XBD_NAME = "samples/PersonInptDiffFromRootRspAny.xbd";

    private static final String PERSON_RSP_ANY_XBD_NAME_EXP = "PersonInptDiffFromRootRspAny_exp.wsdl";

    private static final String SERVICE1_NAME = "MyService1";

    private static final String SERVICE2_NAME = "MyService2";

    private XAwareLogger log = null;

    String listenerURI = null;

    File newWsdlFile = null;

    public WSDLFileUtilsDocLitTestCase(final String name) {
        super(name);
        log = XAwareLogger.getXAwareLogger(this.getClass().getName());
    }

    /**
     * Per-test setup
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        deleteIfExists(newWsdlFile);
        listenerURI = UserPrefs.getInstance().getDefaultServerHost() + "/" + UserPrefs.getInstance().getDefaultSoapServlet(UserPrefs.DOC_LIT_SOAP_TYPE);
    }

    /**
     * Per-test teardown
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        deleteIfExists(newWsdlFile);
    }

    private void deleteIfExists(final File fileToDelete) throws IOException {
        if (fileToDelete != null && fileToDelete.exists()) {
            if (!fileToDelete.delete()) {
                throw new IOException("failed to delete " + fileToDelete.getAbsolutePath());
            }
        }
    }

    private void createAndTestNewWSDL(final boolean overwrite, final List bizDocPaths, final String serviceName, final String bizDocBaseName, final File expectedWsdlFile, final String pathToArchive) throws WSDLException, IOException, JDOMException, XAwareException {
        final WSDLFileUtils_DocLit wsdlUtils = new WSDLFileUtils_DocLit();
        wsdlUtils.createNewWSDL(NEW_WSDL_FULL_NAME, XAwareConstants.XAWARE_NAMESPACE, serviceName, bizDocPaths, listenerURI, overwrite, pathToArchive);
        newWsdlFile = new File(NEW_WSDL_FULL_NAME);
        compareWsdlFiles(expectedWsdlFile, newWsdlFile, bizDocBaseName);
    }

    private void addToExistingTestWSDL(final boolean overwrite, final List bizDocPaths, final String serviceName, final String bizDocBaseName, final File expectedWsdlFile, final File fileToCopy, final String pathToArchive) throws IOException, WSDLException, JDOMException, XAwareException {
        TestUtils.copyFile(fileToCopy, new File(NEW_WSDL_FULL_NAME));
        final WSDLFileUtils_DocLit wsdlUtils = new WSDLFileUtils_DocLit();
        wsdlUtils.addToExistingWSDL(NEW_WSDL_FULL_NAME, XAwareConstants.XAWARE_NAMESPACE, serviceName, bizDocPaths, listenerURI, overwrite, pathToArchive);
        newWsdlFile = new File(NEW_WSDL_FULL_NAME);
        compareWsdlFiles(expectedWsdlFile, newWsdlFile, bizDocBaseName);
    }

    private void compareWsdlFiles(final File expectedWsdlFile, final File actualWsdlFile, final String newDefnName) throws WSDLException, IOException {
        final String wsdlFileName = actualWsdlFile.getAbsolutePath();
        final Definition def = WSDLFileUtils.getDefinition(wsdlFileName);
        def.setQName(new QName(newDefnName));
        WSDLFileUtils.writeDefinitionToWsdlFile(def, wsdlFileName);
        final XMLComparator comparator = new XMLComparator(log);
        comparator.compareExpected(expectedWsdlFile.getAbsolutePath(), actualWsdlFile.getAbsolutePath());
        log.info("Successfully compared expected results with WSDL output for " + expectedWsdlFile.getName());
    }

    /**
     * input_type specified a prefix where there was not namespace declaration. The test fails to create a WSDL
     * indicating missing namespace.
     */
    public void testResponseTypeXmlAny() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_RSP_ANY_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_RSP_ANY_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_RSP_ANY_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * input_type specified a prefix where there was not namespace declaration. The test fails to create a WSDL
     * indicating missing namespace.
     */
    public void testMissingNamespaceForImportStatementsFail() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + MISSING_NAMESPACE_FAIL_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(MISSING_NAMESPACE_FAIL_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, MISSING_NAMESPACE_FAIL_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            if (e instanceof XAwareException == false) {
                fail(e);
            }
        }
    }

    /**
     * Multiple namespaces with same ending. Ensure both prefixes and namespaces are properly represented in WSDL.
     */
    public void testConflictingNamespacesInSameBizDoc() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + CONFLICTING_NAMESPACE_IN_SAME_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(CONFLICTING_NAMESPACE_IN_SAME_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, CONFLICTING_NAMESPACE_IN_SAME_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Input_type matches root and response_type is different. Both are represented in WSDL.
     */
    public void testInputRspDiff() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_INPUT_RSP_DIFF_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_INPUT_RSP_DIFF_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_INPUT_RSP_DIFF_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Test Bizdoc with input_type namespace that is different from the root forcing Response to be same as root.
     */
    public void testInputRspDiffFail() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_INPUT_RSP_DIFF_XBD_NAME_FAIL_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_INPUT_RSP_DIFF_FAIL_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_INPUT_RSP_DIFF_XBD_NAME_FAIL_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            if (e instanceof XAwareException == false) {
                fail(e);
            }
        }
    }

    /**
     * Detect input xml required via // within bizdoc and ensure that it is specified in the bizdoc
     */
    public void testSlashInput() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_USING_SLASH_INPUT_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_USING_SLASH_INPUT_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_USING_SLASH_INPUT_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Test Bizdoc with input_type namespace that is different from the root forcing Response to be XML_ANY.
     */
    public void testInputSameAsRootNoInputParams() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_INPUT_SAME_ROOT_NO_INPUT_PARAMS_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_INPUT_SAME_ROOT_NO_INPUT_PARAMS_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_INPUT_SAME_ROOT_NO_INPUT_PARAMS_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Test Bizdoc with input_type namespace that is different from the root forcing Response to be SAME AS ROOT.
     * 
     */
    public void testInputDiffFromRootNoInputParams() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_INPUT_DIFF_FROM_ROOT_NO_INPUT_PARAMS_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_INPUT_DIFF_FROM_ROOT_NO_INPUT_PARAMS_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_INPUT_DIFF_FROM_ROOT_NO_INPUT_PARAMS_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Test Bizdoc with input_type namespace that is different from the root forcing Response to be SAME AS ROOT. This
     * one has input parameters
     */
    public void testInputDiffFromRoot() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_INPUT_DIFF_FROM_ROOT_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_INPUT_DIFF_FROM_ROOT_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_INPUT_DIFF_FROM_ROOT_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * This is the basic bizdoc template with no schema information. It creates a WSDL with request and response of
     * XML_ANY.
     */
    public void testEmptyInputParameter() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + EMPTY_INPUT_PARAM_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(EMPTY_INPUT_PARAM_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, EMPTY_INPUT_PARAM_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * The schema is specified with noNamespaceSchemaLocation so everything defaults to XML_ANY.
     */
    public void testNoNamespaceSchemaLoc() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_NO_NAMESPACE_SCHEMA_LOC_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_NO_NAMESPACE_SCHEMA_LOC_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_NO_NAMESPACE_SCHEMA_LOC_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * input_type matches root and response_type is not specified. Response is XML_ANY
     */
    public void testCreateNewWSDLStandAloneSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + BASIC_WSDL_NAME);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, BASIC_WSDL_NAME, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * input_type matches root and response_type is not specified. Response is XML_ANY. The input parameters are
     * specified correctly.
     */
    public void testCreateNewWSDLStandAloneNoParametersSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_NO_INPT_PARMS_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_NO_INPT_PARMS_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_NO_INPT_PARMS_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Test createNewWSDL against a stand-alone BizDoc, Multiple BizDocs in same service with multiple namespaces and
     * schemas
     */
    public void testCreateNewWSDLWithMultipleBDsSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + MULTI_WSDL_NAME);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_XBD_NAME);
            bizDocPaths.add(PERSON1_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, MULTI_WSDL_NAME, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Test createNewWSDL against a stand-alone BizDoc, Multiple BizDocs in same service with multiple namespaces and
     * schemas
     */
    public void testCreateNewWSDLNoPathBdSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + BASIC_NO_PATH_WSDL_NAME);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_NO_PATH_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, BASIC_NO_PATH_WSDL_NAME, expectedRsltsFile, DATA_DIR + XAR_NO_PATH_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Test createNewWSDL against a stand-alone BizDoc, Multiple BizDocs in same service with multiple namespaces and
     * schemas overwriting existing wsdl
     */
    public void testCreateNewWSDLNoPathBdOverwriteSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + BASIC_NO_PATH_WSDL_NAME);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_NO_PATH_XBD_NAME);
            final File fileToCopy = new File(DATA_DIR + MULTI_WSDL_NAME);
            newWsdlFile = new File(NEW_WSDL_FULL_NAME);
            TestUtils.copyFile(fileToCopy, newWsdlFile);
            final boolean overwrite = true;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, BASIC_NO_PATH_WSDL_NAME, expectedRsltsFile, DATA_DIR + XAR_NO_PATH_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Add to existing WSDL overwriting service with different BizDoc, namespace and schemas
     */
    public void testAddToExistingWSDLOverwriteServiceSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + OVERWRITE_SERVICE_WSDL_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON1_XBD_NAME);
            final boolean overwrite = true;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE1_NAME, OVERWRITE_SERVICE_WSDL_NAME_EXP, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NAME), DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Add to existing WSDL a second service with different BizDoc, namespace and schemas causing conficting prefix
     * problems
     */
    public void testAddToExistingPrefixConflictWSDLSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_CONFLICT_PREFIX_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_CONFLICT_PREFIX_XBD_NAME);
            final boolean overwrite = false;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE2_NAME, PERSON_CONFLICT_PREFIX_XBD_NAME_EXP, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NAME), DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Add to existing WSDL a second service with different BizDoc, namespace and schemas causing conficting prefix
     * problems
     */
    public void testAddToExistingPrefixConflict1WSDLSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_CONFLICT_PREFIX1_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_CONFLICT_PREFIX1_XBD_NAME);
            final boolean overwrite = false;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE2_NAME, PERSON_CONFLICT_PREFIX1_XBD_NAME_EXP, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NAME), DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Add to existing WSDL a second service with different BizDoc, namespace and schemas causing conficting prefix
     * problems
     */
    public void testAddToExistingPrefixConflict2WSDLSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_CONFLICT_PREFIX2_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_CONFLICT_PREFIX2_XBD_NAME);
            final boolean overwrite = false;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE2_NAME, PERSON_CONFLICT_PREFIX2_XBD_NAME_EXP, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NAME), DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Add to existing WSDL a second service with different BizDoc, namespace and schemas
     */
    public void testAddToExistingNamespaceConflictWSDLSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_CONFLICT_NAMESPACE_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_CONFLICT_NAMESPACE_XBD_NAME);
            final boolean overwrite = false;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE2_NAME, PERSON_CONFLICT_NAMESPACE_XBD_NAME_EXP, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NAME), DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Add to existing WSDL a second service with different BizDoc, namespace and schemas
     */
    public void testAddToExistingNamespaceConflictDiffLocWSDLSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_CONFLICT_NAMESPACE_DIFF_LOC_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_CONFLICT_NAMESPACE_DIFF_LOC_XBD_NAME);
            final boolean overwrite = false;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE2_NAME, PERSON_CONFLICT_NAMESPACE_DIFF_LOC_XBD_NAME_EXP, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NAME), DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Add to existing WSDL a second service with different BizDoc, namespace and schemas
     */
    public void testAddToExistingWSDLSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + ADD_TO_WSDL_NAME);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON1_XBD_NAME);
            final boolean overwrite = false;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE2_NAME, ADD_TO_WSDL_NAME, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NAME), DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Add to existing WSDL a second service with different BizDoc, the namespace is the same but pointing to a
     * different schema
     */
    public void testAddToExistingConflictingNamespace() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + ADD_TO_WSDL_CONFLICT_NAME);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_CONFLICT_XBD_NAME);
            final boolean overwrite = false;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE2_NAME, ADD_TO_WSDL_CONFLICT_NAME, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NAME), DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Add to existing WSDL a second service with different BizDoc, namespace and schemas also mixing XAR files
     * containing BizDocs and use of paths
     */
    public void testAddToExistingWSDLMixingPathNoPathFail() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON1_NO_PATH_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON1_NO_PATH_XBD_NAME);
            final boolean overwrite = false;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE2_NAME, PERSON1_NO_PATH_XBD_NAME_EXP, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NO_PATH_NAME), DATA_DIR + XAR_NO_PATH_FILE_NAME);
        } catch (final Exception e) {
            if (e instanceof XAwareException == false) {
                fail(e);
            }
        }
    }

    /**
     * Add to existing WSDL a second service with different BizDoc, namespace and schemas
     */
    public void testAddToExistingInputRspDiffNamespaceConflictWSDLFail() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_INPUT_RSP_DIFF_NAMESPACE_CONFLIC_XBD_NAME_FAIL_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_INPUT_RSP_DIFF_NAMESPACE_CONFLIC_XBD_NAME_FAIL);
            final boolean overwrite = false;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE2_NAME, PERSON_INPUT_RSP_DIFF_NAMESPACE_CONFLIC_XBD_NAME_FAIL_EXP, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NAME), DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            if (e instanceof XAwareException == false) {
                fail(e);
            }
        }
    }

    /**
     * Add to existing WSDL a second service with different BizDoc, namespace and schemas
     */
    public void testAddToExistingInputRspDiffNamespaceConflictWSDLSuccess() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_INPUT_RSP_DIFF_NAMESPACE_CONFLIC_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_INPUT_RSP_DIFF_NAMESPACE_CONFLIC_XBD_NAME);
            final boolean overwrite = false;
            addToExistingTestWSDL(overwrite, bizDocPaths, SERVICE2_NAME, PERSON_INPUT_RSP_DIFF_NAMESPACE_CONFLIC_XBD_NAME_EXP, expectedRsltsFile, new File(DATA_DIR + ADD_TO_WSDL_START_NAME), DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * test a bizdoc with no namespace assigned for input, response or on the root element of the bizdoc.
     */
    public void testPersonNoNamespaceNoInptRsp() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_NO_NAMESPACE_NO_INPT_RSP_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_NO_NAMESPACE_NO_INPT_RSP_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_NO_NAMESPACE_NO_INPT_RSP_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * test all valid xaware datatype conversion to xsd datatypes
     * 
     */
    public void testInputParametersWithAllDatatypes() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + DATATYPE_TEST_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(DATATYPE_TEST_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, DATATYPE_TEST_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Input_type is different from root, response is mapped to root element of BizDoc. Namespace name doesn't match
     * element in root
     * 
     */
    public void testRequestElementMappedToBDRoot() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + MAP_REQUEST_TO_BDROOT_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(MAP_REQUEST_TO_BDROOT_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, MAP_REQUEST_TO_BDROOT_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Response_type is different from root, and there is // input detected so input xml is defaulted to the root type.
     * 
     */
    public void testResponseElementMappedToBDRoot() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + MAP_RESPONSE_TO_BDROOT_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(MAP_RESPONSE_TO_BDROOT_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, MAP_RESPONSE_TO_BDROOT_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }

    /**
     * Response_type is the same as root, and // input detected so input xml is defaulted XML_ANY.
     * 
     */
    public void testResponseSameAsRoot() {
        try {
            final File expectedRsltsFile = new File(DATA_DIR + PERSON_RSP_XBD_NAME_EXP);
            final List bizDocPaths = new ArrayList();
            bizDocPaths.add(PERSON_RSP_XBD_NAME);
            final boolean overwrite = false;
            createAndTestNewWSDL(overwrite, bizDocPaths, SERVICE1_NAME, PERSON_RSP_XBD_NAME_EXP, expectedRsltsFile, DATA_DIR + XAR_FILE_NAME);
        } catch (final Exception e) {
            fail(e);
        }
    }
}
