package org.iaccess.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import org.iaccess.accesscontrol.IAccessManager;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author hristo koshutanski
 */
public class LogicLayerConfiguration {

    /** If true, the algorithm selects minimal sets with first weight-minimality then set-cardinality,
    * else first minimal set-cardinality and then weight-minimality.
    * The field is static and shared among all objects of this class. It is initialized once.
    */
    boolean CriterionRoleMinimalitySetCardinality = true;

    String PatternContentStr = "\\$\\{content\\}", PatternLeftBraceStr = "\\(", PatternRightBraceStr = "\\)", PatternStr2LeftBraceStr = "\\\\(", PatternStr2RightBraceStr = "\\\\)", PatternWhiteSpacesStr = "\\s", hypothesesFileExt = ".hyp", observationsFileExt = ".obs", CredentialsDelimiter = "\\,", OSFileDelimiter = "\\", Prefix4DeclinedCred = "declined", PredicateName4SystemTimeNow = null, PredicateName4TNTimeByNow = null, Rule4NotChainCred = null, Rule4NotChainCert = null, WorkingDirectory = null, DLVDirectory = null, DLVExecutableName = null, DLVDeductionCommandPromptOptions = null, DLVAbductionCommandPromptOptions = null, DLVComputeAbduciblesCommandPromptOptions = null, DLVOneStepDeductionCommandPromptOptions = null, DLVKeywords4SUccessfulDeduction = null, DLVTemplate4Credential = null, DLVExtendedTemplate4Credential = null, DLVTemplate4Certificate = null, DLVTemplate4Service = null, DLVTemplate4ServiceRequest = null, DLVOneStepPrefix4Credentials = null, DLVAbductionOneStepCommandPromptOptions = null, configFilePath = null, iAccessRootDir = null;

    private int osType = 1;

    String[] DeductionServicePolicyFiles = null, DeductionCredentialPolicyFiles = null, ComputeAbduciblesPolicyFiles = null, AbductionPolicyFiles = null, AbductionCredentialPolicyFiles = null, OneStepAbductionPolicyFiles = null, DisclosurePolicyFiles = null;

    Pattern PatternLeftBrace = null, PatternRightBrace = null, PatternContent = null, PatternWhiteSpace = null, Patt4CountingSetCardinality = null, PattComma = null;

    public LogicLayerConfiguration(String dlvWrapperConfigFile, boolean minimalityCriterion, String iAccessRootDir, int osType) {
        this.configFilePath = dlvWrapperConfigFile;
        this.CriterionRoleMinimalitySetCardinality = minimalityCriterion;
        this.iAccessRootDir = iAccessRootDir;
        this.osType = osType;
    }

    public void loadConfiguration() throws Exception {
        PatternLeftBrace = Pattern.compile(PatternLeftBraceStr);
        PatternRightBrace = Pattern.compile(PatternRightBraceStr);
        PatternContent = Pattern.compile(PatternContentStr);
        PatternWhiteSpace = Pattern.compile(PatternWhiteSpacesStr);
        Patt4CountingSetCardinality = Pattern.compile("\\)\\s+?");
        PattComma = Pattern.compile("\\s*+\\,\\s*+");
        String ConfigRootDir = IAccessManager.IACCESS_ROOT;
        File configFile = new File(configFilePath);
        if (!configFile.exists()) throw new Exception("The specified DLVConfigFile does not exist! (" + configFile + ")");
        try {
            BufferedReader configFileToModify = new BufferedReader(new FileReader(configFilePath));
            String readLine, newConfigFile = "";
            while ((readLine = configFileToModify.readLine()) != null) newConfigFile += readLine;
            String myrootDir = iAccessRootDir;
            if (osType == 2) {
                myrootDir = myrootDir.replace('\\', ';');
                newConfigFile = Pattern.compile(ConfigRootDir).matcher(newConfigFile).replaceAll(myrootDir);
                newConfigFile = newConfigFile.replace(';', '\\');
            } else {
                newConfigFile = Pattern.compile(ConfigRootDir).matcher(newConfigFile).replaceAll(myrootDir);
                newConfigFile = newConfigFile.replace(';', '/');
            }
            newConfigFile = Pattern.compile(ConfigRootDir).matcher(newConfigFile).replaceAll(myrootDir);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(newConfigFile)));
            WorkingDirectory = doc.getDocumentElement().getElementsByTagName("WorkingDirectory").item(0).getFirstChild().getNodeValue().trim();
            DLVDirectory = doc.getDocumentElement().getElementsByTagName("DLVDirectory").item(0).getFirstChild().getNodeValue().trim();
            DLVExecutableName = doc.getDocumentElement().getElementsByTagName("DLVExecutableName").item(0).getFirstChild().getNodeValue().trim();
            DLVDeductionCommandPromptOptions = doc.getDocumentElement().getElementsByTagName("DLVDeductionCommandPromptOptions").item(0).getFirstChild().getNodeValue().trim();
            DLVAbductionCommandPromptOptions = doc.getDocumentElement().getElementsByTagName("DLVAbductionCommandPromptOptions").item(0).getFirstChild().getNodeValue().trim();
            DLVComputeAbduciblesCommandPromptOptions = doc.getDocumentElement().getElementsByTagName("DLVComputeAbduciblesCommandPromptOptions").item(0).getFirstChild().getNodeValue().trim();
            String DeductionServicePolicyFilesStr = doc.getDocumentElement().getElementsByTagName("DeductionServicePolicyFiles").item(0).getFirstChild().getNodeValue().trim();
            String DeductionCredentialPolicyFilesStr = doc.getDocumentElement().getElementsByTagName("DeductionCredentialPolicyFiles").item(0).getFirstChild().getNodeValue().trim();
            String ComputeAbduciblesPolicyFilesStr = doc.getDocumentElement().getElementsByTagName("ComputeAbduciblesPolicyFiles").item(0).getFirstChild().getNodeValue().trim();
            String AbductionPolicyFilesStr = doc.getDocumentElement().getElementsByTagName("AbductionPolicyFiles").item(0).getFirstChild().getNodeValue().trim();
            String AbductionCredentialPolicyFilesStr = doc.getDocumentElement().getElementsByTagName("AbductionCredentialPolicyFiles").item(0).getFirstChild().getNodeValue().trim();
            String OneStepAbductionPolicyFilesStr = doc.getDocumentElement().getElementsByTagName("OneStepAbductionPolicyFiles").item(0).getFirstChild().getNodeValue().trim();
            DLVKeywords4SUccessfulDeduction = doc.getDocumentElement().getElementsByTagName("DLVKeywords4SUccessfulDeduction").item(0).getFirstChild().getNodeValue().trim();
            DLVTemplate4Credential = doc.getDocumentElement().getElementsByTagName("DLVTemplate4Credential").item(0).getFirstChild().getNodeValue().trim();
            DLVExtendedTemplate4Credential = doc.getDocumentElement().getElementsByTagName("DLVExtendedTemplate4Credential").item(0).getFirstChild().getNodeValue().trim();
            DLVTemplate4Certificate = doc.getDocumentElement().getElementsByTagName("DLVTemplate4Certificate").item(0).getFirstChild().getNodeValue().trim();
            DLVTemplate4Service = doc.getDocumentElement().getElementsByTagName("DLVTemplate4Service").item(0).getFirstChild().getNodeValue().trim();
            DLVTemplate4ServiceRequest = doc.getDocumentElement().getElementsByTagName("DLVTemplate4ServiceRequest").item(0).getFirstChild().getNodeValue().trim();
            DLVAbductionOneStepCommandPromptOptions = doc.getDocumentElement().getElementsByTagName("DLVAbductionOneStepCommandPromptOptions").item(0).getFirstChild().getNodeValue().trim();
            DLVOneStepDeductionCommandPromptOptions = doc.getDocumentElement().getElementsByTagName("DLVOneStepDeductionCommandPromptOptions").item(0).getFirstChild().getNodeValue().trim();
            DLVOneStepPrefix4Credentials = doc.getDocumentElement().getElementsByTagName("DLVOneStepPrefix4Credentials").item(0).getFirstChild().getNodeValue().trim();
            PredicateName4SystemTimeNow = doc.getDocumentElement().getElementsByTagName("PredicateName4SystemTimeNow").item(0).getFirstChild().getNodeValue().trim();
            PredicateName4TNTimeByNow = doc.getDocumentElement().getElementsByTagName("PredicateName4TNTimeByNow").item(0).getFirstChild().getNodeValue().trim();
            AbductionPolicyFiles = PattComma.split(AbductionPolicyFilesStr);
            DisclosurePolicyFiles = PattComma.split(ComputeAbduciblesPolicyFilesStr);
            DeductionServicePolicyFiles = PattComma.split(DeductionServicePolicyFilesStr);
            DeductionCredentialPolicyFiles = PattComma.split(DeductionCredentialPolicyFilesStr);
            AbductionCredentialPolicyFiles = PattComma.split(AbductionCredentialPolicyFilesStr);
            OneStepAbductionPolicyFiles = PattComma.split(OneStepAbductionPolicyFilesStr);
            DLVDirectory = new File(DLVDirectory).getCanonicalPath();
            for (String fi : AbductionPolicyFiles) fi = new File(fi).getCanonicalPath();
            for (String fi : DisclosurePolicyFiles) fi = new File(fi).getCanonicalPath();
            for (String fi : DeductionServicePolicyFiles) fi = new File(fi).getCanonicalPath();
            for (String fi : DeductionCredentialPolicyFiles) fi = new File(fi).getCanonicalPath();
            for (String fi : AbductionCredentialPolicyFiles) fi = new File(fi).getCanonicalPath();
            for (String fi : OneStepAbductionPolicyFiles) fi = new File(fi).getCanonicalPath();
        } catch (Exception e) {
            throw new Exception("Parsing the DLV config file exception: " + e);
        }
        switch(osType) {
            case 1:
                DLVDirectory += "/linux";
                DLVExecutableName += ".bin";
                OSFileDelimiter = "/";
                break;
            case 2:
                DLVDirectory += "\\windows";
                DLVExecutableName += ".exe";
                OSFileDelimiter = "\\";
                break;
            case 3:
                DLVDirectory += "/macosx";
                DLVExecutableName += ".bin";
                OSFileDelimiter = "/";
                break;
            default:
                throw new Exception("Unsupported Operating System Type.");
        }
        File workDir = new File(WorkingDirectory);
        if (!workDir.isDirectory() && !workDir.mkdirs()) {
            throw new Exception("The specified Working Directory does not exist!");
        }
        WorkingDirectory = workDir.getCanonicalPath();
        File DLVDir = new File(DLVDirectory);
        if (DLVDir.isDirectory()) DLVDirectory = DLVDir.getCanonicalPath(); else throw new Exception("The specified DLV directory does not exist!");
        File dlvFile = new File(DLVDirectory + OSFileDelimiter + DLVExecutableName);
        if (!dlvFile.exists()) throw new Exception("DLV file not found: " + dlvFile.getCanonicalPath()); else if (!dlvFile.canExecute() && !dlvFile.setExecutable(true, false)) throw new Exception("DLV file is not executable: " + dlvFile.getCanonicalPath());
        String tmpCred = PatternContent.matcher(DLVTemplate4Credential).replaceAll("X,Y,Z");
        String tmpCert = PatternContent.matcher(DLVTemplate4Certificate).replaceAll("X,Y");
        Rule4NotChainCred = tmpCred + ":-" + DLVOneStepPrefix4Credentials + tmpCred + ", not " + Prefix4DeclinedCred + DLVOneStepPrefix4Credentials + tmpCred + ".";
        Rule4NotChainCert = tmpCert + ":-" + DLVOneStepPrefix4Credentials + tmpCert + ", not " + Prefix4DeclinedCred + DLVOneStepPrefix4Credentials + tmpCert + ".";
    }
}
