package ca.etsmtl.ihe.xdsitest.test;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.jaxen.JaxenException;
import ca.etsmtl.ihe.xdsitest.util.AXIOMUtil;
import ca.etsmtl.ihe.xdsitest.util.FailedException;

public class AnalyzeTestLog_Consumer_01 extends TestAnalyzer {

    private final AnalysisRuntime runtime;

    private final AnalyzeCommon analyzeCommon;

    private final String imageCtnAeTitle;

    public AnalyzeTestLog_Consumer_01(AnalysisRuntime runtime, String imageCtnAeTitle) throws FailedException {
        this.runtime = runtime;
        this.analyzeCommon = new AnalyzeCommon(this.runtime);
        this.imageCtnAeTitle = imageCtnAeTitle;
    }

    public OMElement analyze(InputStream logXml) throws FailedException {
        OMElement executionLog = this.analyzeCommon.parseExecutionLog(logXml);
        return buildEvaluation(executionLog);
    }

    @SuppressWarnings("unchecked")
    private OMElement buildEvaluation(OMElement executionLog) throws FailedException {
        boolean testPassed = false;
        OMElement evaluation = omFactory.createOMElement(new QName("evaluation"));
        try {
            AXIOMXPath xpath;
            xpath = new AXIOMXPath(" http_request" + "     [@xxxx_stage = '1'][@service = 'registry'][requestType = 'AdhocQueryRequest']" + "/responseFile");
            List<String> evidenceStage1 = AXIOMUtil.omElementListToStringList(xpath.selectNodes(executionLog));
            xpath = new AXIOMXPath(" http_request" + "     [@xxxx_stage = '2'][@service = 'fileserver']");
            OMElement evidenceStage2 = AXIOMUtil.createNewParentForOMElements(xpath.selectNodes(executionLog));
            xpath = new AXIOMXPath(" imagectn_moved" + "     [@xxxx_stage = '4']" + "/sopInstance");
            Set<String> evidenceStage3 = AXIOMUtil.omElementListToStringSet(xpath.selectNodes(executionLog));
            for (String fileResponse : evidenceStage1) {
                OMElement elementFileResponse = omFactory.createOMElement(new QName("fileResponse"));
                elementFileResponse.addAttribute(omFactory.createOMAttribute("value", null, fileResponse));
                evaluation.addChild(elementFileResponse);
                List<String> listUriKosd = this.analyzeCommon.extractListUriKosdFromAdhocQueryResponse(new File(fileResponse));
                for (String uriKosd : listUriKosd) {
                    OMElement elementUriKosd = omFactory.createOMElement(new QName("uriKosd"));
                    elementUriKosd.addAttribute(omFactory.createOMAttribute("value", null, uriKosd));
                    elementFileResponse.addChild(elementUriKosd);
                    xpath = new AXIOMXPath(" *" + "     [requestUrl = '" + uriKosd.trim() + "']" + "/contentFile");
                    List<String> listFileKosd = AXIOMUtil.omElementListToStringList(xpath.selectNodes(evidenceStage2));
                    for (String fileKosd : listFileKosd) {
                        OMElement elementFileKosd = omFactory.createOMElement(new QName("fileKosd"));
                        elementFileKosd.addAttribute(omFactory.createOMAttribute("value", null, fileKosd));
                        elementUriKosd.addChild(elementFileKosd);
                        List<OMElement> listSopInstance = null;
                        {
                            String drop = null;
                            File fileKosdXml = this.runtime.nextWorkFile("kosd.xml");
                            if (!this.analyzeCommon.convertDicom2Xml(new File(fileKosd), fileKosdXml)) {
                                drop = "conversion to XML failed";
                            } else {
                                OMElement infoKosd = null;
                                OMElement elementSopClassUid;
                                String sopClassUid;
                                infoKosd = this.analyzeCommon.extractInfoFromKosd(fileKosdXml);
                                elementSopClassUid = infoKosd.getFirstChildWithName(new QName("sopClassUid"));
                                sopClassUid = (elementSopClassUid != null) ? elementSopClassUid.getText() : null;
                                if (!"1.2.840.10008.5.1.4.1.1.88.59".equals(sopClassUid)) {
                                    drop = "SOPClassUID = '" + sopClassUid + "'";
                                } else if ("error".equals(infoKosd.getLocalName())) {
                                    drop = infoKosd.getAttributeValue(new QName("reason"));
                                } else {
                                    listSopInstance = AXIOMUtil.convertToOMElementList(infoKosd.getChildrenWithName(new QName("sopInstance")));
                                }
                            }
                            if (drop != null) {
                                elementFileKosd.addAttribute(omFactory.createOMAttribute("drop", null, drop));
                                continue;
                            }
                        }
                        for (OMElement sopInstance : listSopInstance) {
                            OMElement elementSopInstance = sopInstance.cloneOMElement();
                            elementFileKosd.addChild(elementSopInstance);
                            {
                                String drop = null;
                                String retrieveAeTitle = sopInstance.getFirstChildWithName(new QName("retrieveAeTitle")).getText();
                                String sopInstanceUid = sopInstance.getFirstChildWithName(new QName("sopInstanceUid")).getText();
                                if (!this.imageCtnAeTitle.equals(retrieveAeTitle)) {
                                    drop = "reference points to other application entity (me: '" + this.imageCtnAeTitle + "')";
                                } else if (!evidenceStage3.contains(sopInstanceUid)) {
                                    drop = "not retrieved";
                                }
                                if (drop != null) {
                                    elementSopInstance.addAttribute(omFactory.createOMAttribute("drop", null, drop));
                                    continue;
                                }
                            }
                            testPassed = true;
                        }
                    }
                }
            }
            evaluation.addAttribute(omFactory.createOMAttribute("result", null, (testPassed ? "PASSED" : "FAILED")));
            AXIOMUtil.prettyPrintElement(evaluation, this.runtime.getLog());
            System.out.println("AnalyzeTestLog_Consumer.buildEvaluation: evaluation content: " + evaluation.toString());
            this.runtime.getLog().println("Evaluation result: " + evaluation.getAttributeValue(new QName("result")));
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return evaluation;
    }
}
