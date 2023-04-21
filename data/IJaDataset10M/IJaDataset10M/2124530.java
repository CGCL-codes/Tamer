package com.intel.gpe.clients.api.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import com.intel.gpe.clients.api.StorageClient;
import com.intel.gpe.clients.api.WSRFClient;
import com.intel.util.xml.Namespaces;

/**
 * Create data staging setup code only for supported transfer mechanisms.

 * @author Alexander Lukichev
 * @version $Id: CustomizableDataStagingSetup.java,v 1.13 2006/11/30 11:17:38 mlukichev Exp $
 */
public abstract class CustomizableDataStagingSetup implements DataStagingSetup {

    private Map dataStagingSetupMap = new HashMap();

    private Sequence sequence;

    private Variable protocolsResponse;

    private List<String> protocols;

    private Namespaces namespaces;

    public CustomizableDataStagingSetup(List<String> protocols, Namespaces namespaces) {
        sequence = new Sequence();
        this.protocols = protocols;
        this.namespaces = namespaces;
        String id = genId();
        this.protocolsResponse = new Variable("customizableDataStagingSetup" + id + "ProtocolsResponse", "GetResourcePropertyResponse", "");
    }

    private List getDataStagingSetupList(PartnerLink storage) {
        if (dataStagingSetupMap.containsKey(storage)) {
            return (List) dataStagingSetupMap.get(storage);
        } else {
            List<DataStagingSetup> dataStagingSetupList = new ArrayList<DataStagingSetup>();
            for (int i = 0; i < protocols.size(); i++) {
                dataStagingSetupList.add(getDataStagingSetup(protocols.get(i), namespaces));
            }
            String bpwsns = namespaces.getPrefix(getBPWSNS());
            String unigridsns = namespaces.getPrefix(getUnigridsNS());
            GetResourceProperty getProtocols = getProtocolsProperty(storage, protocolsResponse, namespaces);
            Switch.Case[] cases = new Switch.Case[protocols.size()];
            for (int i = 0; i < protocols.size(); i++) {
                cases[i] = new Switch.Case("count(" + bpwsns + ":getVariableData('" + protocolsResponse.getName() + "','" + protocolsResponse.getPart() + "')[" + unigridsns + ":Protocol='" + protocols.get(i) + "'])>0", dataStagingSetupList.get(i));
            }
            Throw noprotocols = new Throw(new QName(getWESNS(), "NoDataStagingProtocolFound"));
            Switch selection = new Switch(cases, noprotocols);
            sequence.addAction(getProtocols);
            sequence.addAction(selection);
            dataStagingSetupMap.put(storage, dataStagingSetupList);
            return dataStagingSetupList;
        }
    }

    public void getFileExportURL(Variable url, PartnerLink storage, String file) {
        List dataStagingSetupList = getDataStagingSetupList(storage);
        for (int i = 0; i < dataStagingSetupList.size(); i++) {
            ((DataStagingSetup) dataStagingSetupList.get(i)).getFileExportURL(url, storage, file);
        }
    }

    public void setupImport(Variable dataStaging, PartnerLink storage, String file) {
        List dataStagingSetupList = getDataStagingSetupList(storage);
        for (int i = 0; i < dataStagingSetupList.size(); i++) {
            ((DataStagingSetup) dataStagingSetupList.get(i)).setupImport(dataStaging, storage, file);
        }
    }

    public void setupImport(Variable dataStaging, StorageClient client, String file) {
        PartnerLinkValue plValue = getPLValue(client);
        Declaration plDeclaration = new Declaration(plValue);
        sequence.addAction(plDeclaration);
        setupImport(dataStaging, plDeclaration.getPartnerLink(), file);
    }

    public void setupExport(Variable dataStaging, PartnerLink storage, String file) {
        List dataStagingSetupList = getDataStagingSetupList(storage);
        for (int i = 0; i < dataStagingSetupList.size(); i++) {
            ((DataStagingSetup) dataStagingSetupList.get(i)).setupExport(dataStaging, storage, file);
        }
    }

    public void setupExportFromExpression(Variable dataStaging, PartnerLink storage, String expression) {
        List dataStagingSetupList = getDataStagingSetupList(storage);
        for (int i = 0; i < dataStagingSetupList.size(); i++) {
            ((DataStagingSetup) dataStagingSetupList.get(i)).setupExportFromExpression(dataStaging, storage, expression);
        }
    }

    public void setupImportFromExpression(Variable dataStaging, PartnerLink storage, String expression) {
        List dataStagingSetupList = getDataStagingSetupList(storage);
        for (int i = 0; i < dataStagingSetupList.size(); i++) {
            ((DataStagingSetup) dataStagingSetupList.get(i)).setupImportFromExpression(dataStaging, storage, expression);
        }
    }

    public void setupExportFromArray(PartnerLink storage, Variable jobDescriprtion, Variable array, String path) {
        List dataStagingSetupList = getDataStagingSetupList(storage);
        for (int i = 0; i < dataStagingSetupList.size(); i++) {
            ((DataStagingSetup) dataStagingSetupList.get(i)).setupExportFromArray(storage, jobDescriprtion, array, path);
        }
    }

    public void setupImportFromArray(PartnerLink storage, Variable jobDescription, Variable array, String path) {
        List dataStagingSetupList = getDataStagingSetupList(storage);
        for (int i = 0; i < dataStagingSetupList.size(); i++) {
            ((DataStagingSetup) dataStagingSetupList.get(i)).setupImportFromArray(storage, jobDescription, array, path);
        }
    }

    public void emitCode(Scope scope, Element target) throws Exception {
        sequence.emitCode(scope, target);
    }

    protected abstract DataStagingSetup getDataStagingSetup(String protocol, Namespaces namespaces);

    protected abstract GetResourceProperty getProtocolsProperty(PartnerLink storage, Variable protocolsResponse, Namespaces namespaces);

    protected abstract String getBPWSNS();

    protected abstract String getUnigridsNS();

    protected abstract String getWESNS();

    protected abstract PartnerLinkValue getPLValue(WSRFClient client);

    private static int id = 0;

    private static String genId() {
        return Integer.toString(++id);
    }
}
