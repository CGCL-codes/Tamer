package com.intel.gpe.clients.api.workflow;

import org.w3c.dom.Element;
import com.intel.gpe.clients.api.FileSystem;
import com.intel.gpe.clients.api.StorageClient;
import com.intel.gpe.clients.api.WSRFClient;
import com.intel.util.xml.Namespaces;

/**
 * Base class for URL based data staging
 * 
 * @author Alexander Lukichev
 * @version $Id: URLBasedDataStagingSetup.java,v 1.16 2006/11/30 11:17:38 mlukichev Exp $
 */
public abstract class URLBasedDataStagingSetup implements DataStagingSetup {

    private Sequence sequence = new Sequence();

    private Namespaces namespaces;

    private Variable getSourceResponse;

    private Variable getTargetResponse;

    public URLBasedDataStagingSetup(Namespaces namespaces) {
        this.namespaces = namespaces;
        String id = genId();
        String jsdlns = namespaces.getPrefix(getJSDLNS());
        getSourceResponse = new Variable("urlDataStagingSetup" + id + "GetSourceResponse", "GetResourcePropertyResponse", "/child::node()[1]/" + jsdlns + ":Source");
        getTargetResponse = new Variable("urlDataStagingSetup" + id + "GetTargetResponse", "GetResourcePropertyResponse", "/child::node()[1]/" + jsdlns + ":Target");
    }

    public void getFileExportURL(Variable url, PartnerLink storage, String file) {
        FileImport fileImport = getFileImport(storage, file, namespaces);
        sequence.addAction(fileImport);
        GetResourceProperty getTarget = getTargetProperty(fileImport.getFileTransfer(), getTargetResponse, namespaces);
        sequence.addAction(getTarget);
        Assignment assignment = new Assignment(getTargetResponse, url);
        sequence.addAction(assignment);
        sequence.addAction(getDestroy(fileImport.getFileTransfer(), namespaces));
    }

    public void setupImport(Variable dataStaging, PartnerLink sourceStorage, String file) {
        String jsdlns = namespaces.getPrefix(getJSDLNS());
        FileExport fileExport = getFileExport(sourceStorage, file, namespaces);
        sequence.addAction(fileExport);
        GetResourceProperty getSource = getSourceProperty(fileExport.getFileTransfer(), getSourceResponse, namespaces);
        sequence.addAction(getSource);
        Assignment assignment = new Assignment(getSourceResponse, dataStaging.derive("/" + jsdlns + ":Source"));
        sequence.addAction(assignment);
        sequence.addAction(getDestroy(fileExport.getFileTransfer(), namespaces));
    }

    public void setupImport(PartnerLink sourceStorage, Variable jobDescription, String file) {
        Declaration dsDeclaration = new Declaration(getInitialValue());
        sequence.addAction(dsDeclaration);
        Variable dataStaging = dsDeclaration.getVariable();
        Declaration uriDeclaration = new Declaration(getInitialValue());
        sequence.addAction(uriDeclaration);
        Variable uri = uriDeclaration.getVariable();
        FileExport fileExport = getFileExport(sourceStorage, file, namespaces);
        GetResourceProperty getSource = getSourceProperty(fileExport.getFileTransfer(), getSourceResponse, namespaces);
        sequence.addAction(fileExport);
        sequence.addAction(getSource);
        sequence.addAction(new Assignment(getVariableData(getSourceResponse, "/descendant-or-self::text()[1]"), uri));
        sequence.addAction(getBuildDataStaging(uri, dataStaging, file, true));
        sequence.addAction(getInsertElement(jobDescription, dataStaging));
        sequence.addAction(getDestroy(fileExport.getFileTransfer(), namespaces));
    }

    public void setupExport(PartnerLink storage, Variable jobDescription, String file) {
        Declaration dsDeclaration = new Declaration(getInitialValue());
        sequence.addAction(dsDeclaration);
        Variable dataStaging = dsDeclaration.getVariable();
        Declaration uriDeclaration = new Declaration(getInitialValue());
        sequence.addAction(uriDeclaration);
        Variable uri = uriDeclaration.getVariable();
        FileImport fileImport = getFileImport(storage, file, namespaces);
        GetResourceProperty getTarget = getTargetProperty(fileImport.getFileTransfer(), getTargetResponse, namespaces);
        sequence.addAction(fileImport);
        sequence.addAction(getTarget);
        sequence.addAction(new Assignment(getVariableData(getTargetResponse, "/descendant-or-self::text()[1]"), uri));
        sequence.addAction(getBuildDataStaging(uri, dataStaging, file, false));
        sequence.addAction(getInsertElement(jobDescription, dataStaging));
        sequence.addAction(getDestroy(fileImport.getFileTransfer(), namespaces));
    }

    public void setupImportFromExpression(PartnerLink storage, Variable jobDescription, String expression) {
        Declaration dsDeclaration = new Declaration(getInitialValue());
        sequence.addAction(dsDeclaration);
        Variable dataStaging = dsDeclaration.getVariable();
        Declaration uriDeclaration = new Declaration(getInitialValue());
        sequence.addAction(uriDeclaration);
        Variable uri = uriDeclaration.getVariable();
        FileExportFromExpression fileExport = getFileExportFromExpression(storage, expression, namespaces);
        GetResourceProperty getSource = getSourceProperty(fileExport.getFileTransfer(), getSourceResponse, namespaces);
        sequence.addAction(fileExport);
        sequence.addAction(getSource);
        sequence.addAction(new Assignment(getVariableData(getSourceResponse, "/descendant-or-self::text()[1]"), uri));
        sequence.addAction(getBuildDataStagingFromExpression(uri, dataStaging, expression, true));
        sequence.addAction(getInsertElement(jobDescription, dataStaging));
        sequence.addAction(getDestroy(fileExport.getFileTransfer(), namespaces));
    }

    public void setupExportFromExpression(PartnerLink storage, Variable jobDescription, String expression) {
        Declaration dsDeclaration = new Declaration(getInitialValue());
        sequence.addAction(dsDeclaration);
        Variable dataStaging = dsDeclaration.getVariable();
        Declaration uriDeclaration = new Declaration(getInitialValue());
        sequence.addAction(uriDeclaration);
        Variable uri = uriDeclaration.getVariable();
        FileImportFromExpression fileImport = getFileImportFromExpression(storage, expression, namespaces);
        GetResourceProperty getTarget = getTargetProperty(fileImport.getFileTransfer(), getTargetResponse, namespaces);
        sequence.addAction(fileImport);
        sequence.addAction(getTarget);
        sequence.addAction(new Assignment(getVariableData(getTargetResponse, "/descendant-or-self::text()[1]"), uri));
        sequence.addAction(getBuildDataStagingFromExpression(uri, dataStaging, expression, false));
        sequence.addAction(getInsertElement(jobDescription, dataStaging));
        sequence.addAction(getDestroy(fileImport.getFileTransfer(), namespaces));
    }

    public void setupImport(Variable dataStaging, StorageClient targetSystem, String file) {
    }

    public void setupExport(Variable dataStaging, PartnerLink targetStorage, String file) {
        String jsdlns = namespaces.getPrefix(getJSDLNS());
        FileImport fileImport = getFileImport(targetStorage, file, namespaces);
        sequence.addAction(fileImport);
        GetResourceProperty getTarget = getTargetProperty(fileImport.getFileTransfer(), getTargetResponse, namespaces);
        sequence.addAction(getTarget);
        Assignment assignment = new Assignment(getTargetResponse, dataStaging.derive("/" + jsdlns + ":Target"));
        sequence.addAction(assignment);
        sequence.addAction(getDestroy(fileImport.getFileTransfer(), namespaces));
    }

    public void setupExportFromExpression(Variable dataStaging, PartnerLink targetStorage, String expression) {
        String jsdlns = namespaces.getPrefix(getJSDLNS());
        FileImportFromExpression fileImport = getFileImportFromExpression(targetStorage, expression, namespaces);
        sequence.addAction(fileImport);
        GetResourceProperty getTarget = getTargetProperty(fileImport.getFileTransfer(), getTargetResponse, namespaces);
        sequence.addAction(getTarget);
        Assignment assignment = new Assignment(getTargetResponse, dataStaging.derive("/" + jsdlns + ":Target"));
        sequence.addAction(assignment);
        sequence.addAction(getDestroy(fileImport.getFileTransfer(), namespaces));
    }

    public void setupImportFromExpression(Variable dataStaging, PartnerLink targetStorage, String expression) {
        String jsdlns = namespaces.getPrefix(getJSDLNS());
        FileExportFromExpression fileExport = getFileExportFromExpression(targetStorage, expression, namespaces);
        sequence.addAction(fileExport);
        GetResourceProperty getSource = getSourceProperty(fileExport.getFileTransfer(), getSourceResponse, namespaces);
        sequence.addAction(getSource);
        Assignment assignment = new Assignment(getSourceResponse, dataStaging.derive("/" + jsdlns + ":Source"));
        sequence.addAction(assignment);
        sequence.addAction(getDestroy(fileExport.getFileTransfer(), namespaces));
    }

    public void setupExportFromArray(PartnerLink storage, Variable jobDescriprtion, Variable array, String path) {
        String unigridsns = namespaces.getPrefix(getUnigridsNS());
        Declaration cDeclaration = new Declaration(getInitialValue());
        sequence.addAction(cDeclaration);
        Variable composite = cDeclaration.getVariable();
        sequence.addAction(new Assignment(jobDescriprtion, composite));
        Declaration itemDeclaration = new Declaration(getInitialValue());
        sequence.addAction(itemDeclaration);
        Variable item = itemDeclaration.getVariable();
        Declaration dsDeclaration = new Declaration(getInitialValue());
        sequence.addAction(dsDeclaration);
        Variable dataStaging = dsDeclaration.getVariable();
        Declaration uriDeclaration = new Declaration(getInitialValue());
        sequence.addAction(uriDeclaration);
        Variable uri = uriDeclaration.getVariable();
        Declaration iDeclaration = new Declaration(getIteratorInitialIValue());
        sequence.addAction(iDeclaration);
        Variable iteratorI = iDeclaration.getVariable();
        String expression = getVariableData(item, "/" + unigridsns + ":Path/child::text()[1]");
        FileImportFromExpression fileImport = getFileImportFromExpression(storage, expression, namespaces);
        GetResourceProperty getSource = getSourceProperty(fileImport.getFileTransfer(), getTargetResponse, namespaces);
        Sequence whileBody = new Sequence();
        whileBody.addAction(getArrayItem(array, iteratorI, item));
        whileBody.addAction(fileImport);
        whileBody.addAction(getSource);
        whileBody.addAction(new Assignment(getVariableData(getSourceResponse, "/descendant-or-self::text()[1]"), uri));
        whileBody.addAction(getBuildDataStaging(uri, dataStaging, path, false));
        whileBody.addAction(getInsertElement(composite, dataStaging));
        whileBody.addAction(getIncreaseIterator(iteratorI));
        whileBody.addAction(getDestroy(fileImport.getFileTransfer(), namespaces));
        sequence.addAction(new While(getWhileCondition(iteratorI, array), whileBody));
        sequence.addAction(new Assignment(composite, jobDescriprtion));
    }

    public void setupImportFromArray(PartnerLink storage, Variable jobDescription, Variable array, String path) {
        String unigridsns = namespaces.getPrefix(getUnigridsNS());
        Declaration cDeclaration = new Declaration(getInitialValue());
        sequence.addAction(cDeclaration);
        Variable composite = cDeclaration.getVariable();
        sequence.addAction(new Assignment(jobDescription, composite));
        Declaration itemDeclaration = new Declaration(getInitialValue());
        sequence.addAction(itemDeclaration);
        Variable item = itemDeclaration.getVariable();
        Declaration dsDeclaration = new Declaration(getInitialValue());
        sequence.addAction(dsDeclaration);
        Variable dataStaging = dsDeclaration.getVariable();
        Declaration uriDeclaration = new Declaration(getInitialValue());
        sequence.addAction(uriDeclaration);
        Variable uri = uriDeclaration.getVariable();
        Declaration iDeclaration = new Declaration(getIteratorInitialIValue());
        sequence.addAction(iDeclaration);
        Variable iteratorI = iDeclaration.getVariable();
        String expression = getVariableData(item, "/" + unigridsns + ":Path/child::text()[1]");
        FileExportFromExpression fileExport = getFileExportFromExpression(storage, expression, namespaces);
        GetResourceProperty getSource = getSourceProperty(fileExport.getFileTransfer(), getSourceResponse, namespaces);
        Sequence whileBody = new Sequence();
        whileBody.addAction(getArrayItem(array, iteratorI, item));
        whileBody.addAction(fileExport);
        whileBody.addAction(getSource);
        whileBody.addAction(new Assignment(getVariableData(getSourceResponse, "/descendant-or-self::text()[1]"), uri));
        whileBody.addAction(getBuildDataStaging(uri, dataStaging, path, true));
        whileBody.addAction(getInsertElement(composite, dataStaging));
        whileBody.addAction(getIncreaseIterator(iteratorI));
        whileBody.addAction(getDestroy(fileExport.getFileTransfer(), namespaces));
        sequence.addAction(new While(getWhileCondition(iteratorI, array), whileBody));
        sequence.addAction(new Assignment(composite, jobDescription));
    }

    /**
     * Creates expression to get variable data
     * @param var The variable
     * @param expression The path expression
     * @return Expression to get variable data
     */
    private String getVariableData(Variable var, String expression) {
        String bpws = namespaces.getPrefix(getBPWSNS());
        String result = bpws + ":getVariableData('" + var.getName() + "','" + var.getPart() + "')";
        if (!expression.equals("")) {
            result += expression;
        }
        return result;
    }

    /**
     * Gets action to increase iterator variable value
     * @param iterator The iteration variable
     * @return Action to increase iterator variable value
     */
    private Action getIncreaseIterator(Variable iterator) {
        return new Assignment(getVariableData(iterator, "+1"), iterator);
    }

    /**
     * Gets an array item and puts it into given variable
     * @param array The array
     * @param i The iterator variable
     * @param item The variable to store obtained value
     * @return
     */
    private Action getArrayItem(Variable array, Variable i, Variable item) {
        String expression = getVariableData(array, "") + "/child::node()[" + getVariableData(i, "") + "]";
        return new Assignment(expression, item);
    }

    /**
     * Gets action that inserts item value into container value 
     * @param container The cotainer
     * @param item The item to be inserted
     * @return 
     */
    private Action getInsertElement(Variable container, Variable item) {
        String wes = namespaces.getPrefix(getWESNS());
        String insertElement = wes + ":insertElement(" + getVariableData(container, "") + ", " + getVariableData(item, "") + ")";
        return new Assignment(insertElement, container);
    }

    /**
     * Gets action that builds data staging section 
     * @param uri The file uri
     * @param result The variable to store builded element 
     * @param expression the expression to obtain target system path
     * @param isImport Is data staging used for import
     * @return
     */
    private Action getBuildDataStagingFromExpression(Variable uri, Variable result, String expression, boolean isImport) {
        String wes = namespaces.getPrefix(getWESNS());
        String lastIndex = wes + ":lastIndexOf(" + getVariableData(uri, "") + ", '/')";
        String getFileName = "concat(" + expression + ", '/'" + ",substring(" + getVariableData(uri, "") + ", " + lastIndex + "+2))";
        String buildElementExpression = wes + ":buildElement('" + getDataStagingText(isImport) + "', 'uri', " + getVariableData(uri, "") + ", 'fileSystemName', '" + FileSystem.WORK + "'" + ", 'fileName', " + getFileName + ")";
        return new Assignment(buildElementExpression, result);
    }

    /**
     * Gets action that builds data staging section 
     * @param uri The file uri
     * @param temp The variable to store builded element 
     * @param path The target system path
     * @param isImport Is data staging used for import
     * @return
     */
    private Action getBuildDataStaging(Variable uri, Variable temp, String path, boolean isImport) {
        String wes = namespaces.getPrefix(getWESNS());
        String lastIndex = wes + ":lastIndexOf(" + getVariableData(uri, "") + ", '/')";
        String getFileName = "concat('" + path + "/'" + ",substring(" + getVariableData(uri, "") + ", " + lastIndex + "+2))";
        String buildElementExpression = wes + ":buildElement('" + getDataStagingText(isImport) + "', 'uri', " + getVariableData(uri, "") + ", 'fileSystemName', '" + FileSystem.WORK + "'" + ", 'fileName', " + getFileName + ")";
        return new Assignment(buildElementExpression, temp);
    }

    private String getDataStagingText(boolean isImport) {
        String s = isImport ? "Source" : "Target";
        String jsdlns = namespaces.getPrefix(getJSDLNS());
        return "<" + jsdlns + ":DataStaging xmlns:" + jsdlns + "=\"" + getJSDLNS() + "\">" + "<" + jsdlns + ":FileName>@fileName</" + jsdlns + ":FileName>" + "<" + jsdlns + ":FilesystemName>@fileSystemName</" + jsdlns + ":FilesystemName>" + "<" + jsdlns + ":CreationFlag>overwrite</" + jsdlns + ":CreationFlag>" + "<" + jsdlns + ":" + s + ">" + "<" + jsdlns + ":URI>@uri</" + jsdlns + ":URI>" + "</" + jsdlns + ":" + s + ">" + "</" + jsdlns + ":DataStaging>";
    }

    private String getWhileCondition(Variable iterator, Variable array) {
        String arrayExpression = getVariableData(array, "");
        String i = getVariableData(iterator, "");
        return i + " <= count(" + arrayExpression + "/child::node())";
    }

    protected abstract String getJSDLNS();

    protected abstract String getBPWSNS();

    protected abstract String getWESNS();

    protected abstract String getUnigridsNS();

    protected abstract FileExport getFileExport(PartnerLink sourceStorage, String file, Namespaces namespaces);

    protected abstract FileImport getFileImport(PartnerLink targetStorage, String file, Namespaces namespaces);

    protected abstract FileImportFromExpression getFileImportFromExpression(PartnerLink targetStorage, String expression, Namespaces namespaces);

    protected abstract FileExportFromExpression getFileExportFromExpression(PartnerLink sourceStorage, String expression, Namespaces namespaces);

    protected abstract GetResourceProperty getSourceProperty(PartnerLink resource, Variable response, Namespaces namespaces);

    protected abstract GetResourceProperty getTargetProperty(PartnerLink resource, Variable response, Namespaces namespaces);

    protected abstract Action getDestroy(PartnerLink resource, Namespaces namespaces);

    protected abstract String getFileTransferNS();

    protected abstract VariableValue getGetSourceResponse();

    protected abstract VariableValue getGetTargetResponse();

    protected abstract VariableValue getIteratorInitialIValue();

    protected abstract VariableValue getInitialValue();

    protected abstract PartnerLinkValue getPLValue(WSRFClient client);

    public void emitCode(Scope scope, Element target) throws Exception {
        scope.addVariable(getSourceResponse, getGetSourceResponse());
        scope.addVariable(getTargetResponse, getGetTargetResponse());
        sequence.emitCode(scope, target);
    }

    private static int id = 0;

    private static String genId() {
        return Integer.toString(++id);
    }
}
