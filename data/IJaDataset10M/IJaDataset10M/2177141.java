package com.intel.gpe.services.tss.common.staticproperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import org.jdom.JDOMException;
import org.jdom.adapters.XercesDOMAdapter;
import org.jdom.input.DOMBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.intel.gpe.constants.XMLIDBConstants;
import com.intel.gpe.services.tss.common.incarnations.Context;
import com.intel.gpe.services.tss.common.incarnations.Field;
import com.intel.gpe.tsi.common.User;
import com.intel.util.xml.ElementUtil;

/**
 * @author Dmitry Petrov
 * @version $Id: SimpleStaticPropertiesStorage.java,v 1.38 2006/12/13 11:43:06 dnpetrov Exp $
 */
public class SimpleStaticPropertiesStorage implements StaticPropertiesStorage, Serializable {

    static final long serialVersionUID = -3418379615336228851L;

    private static Logger logger = Logger.getLogger("com.intel.gpe");

    private String name;

    private String descr;

    private String uSpaceRoot;

    private String fileSeparator;

    private double overhead;

    private Element operatingSystem;

    private Element processor;

    private Element memory;

    private HashMap<String, NumericInfoResource> numericInfo;

    private HashMap<String, TextInfoResource> textInfo;

    private int maxNumberOfJobs;

    private int numberOfNodes;

    private double networkLatency;

    private double networkBandwidth;

    private PropertyBlock tsiInit;

    private Vector<RegistryListItem> registries;

    private HashMap<String, StorageTemplate> storageTemplates;

    private HashMap<String, Application> applications;

    private HashMap<String, PropertyBlock> transferProtocols;

    private DistinguishedNameMap users;

    private Context globalFields;

    public static interface Reader {

        public String getName();

        public String getDescr();

        public String getUSpaceRoot();

        public String getFileSeparator();

        public double getOverhead();

        public Element getOperatingSystem();

        public Element getProcessor();

        public Element getMemory();

        public int getMaxNumberOfJobs();

        public int getNumberOfNodes();

        public double getNetworkLatency();

        public double getNetworkBandwidth();

        public boolean hasNextNumericInfoResourceReader();

        public NumericInfoResource.Reader nextNumericInfoResourceReader();

        public boolean hasNextTextInfoResourceReader();

        public TextInfoResource.Reader nextTextInfoResourceReader();

        public boolean hasTSIClientInitReader();

        public PropertyBlock.Reader getTSIClientInitReader();

        public boolean hasNextRegistryReader();

        public RegistryListItem.Reader nextRegistryReader();

        public boolean hasNextStorageTemplateReader();

        public StorageTemplate.Reader nextStorageTemplateReader();

        public boolean hasNextApplicationReader();

        public Application.Reader nextApplicationReader();

        public boolean hasNextTranferProtocolReader();

        public PropertyBlock.Reader nextTransferProtocolReader();

        public boolean hasNextUserMapEntryReader();

        public UserEntry.Reader nextUserMapEntryReader();

        public boolean hasNextFieldReader();

        public Field.Reader nextFieldReader();
    }

    public SimpleStaticPropertiesStorage() {
        numericInfo = new HashMap<String, NumericInfoResource>();
        textInfo = new HashMap<String, TextInfoResource>();
        registries = new Vector<RegistryListItem>();
        storageTemplates = new HashMap<String, StorageTemplate>();
        applications = new HashMap<String, Application>();
        transferProtocols = new HashMap<String, PropertyBlock>();
        users = new DistinguishedNameMap('/');
        globalFields = new Context();
    }

    public SimpleStaticPropertiesStorage(Reader reader) {
        name = reader.getName();
        descr = reader.getDescr();
        uSpaceRoot = reader.getUSpaceRoot();
        fileSeparator = reader.getFileSeparator();
        overhead = reader.getOverhead();
        maxNumberOfJobs = reader.getMaxNumberOfJobs();
        numberOfNodes = reader.getNumberOfNodes();
        networkLatency = reader.getNetworkLatency();
        networkBandwidth = reader.getNetworkBandwidth();
        operatingSystem = reader.getOperatingSystem();
        processor = reader.getProcessor();
        memory = reader.getMemory();
        numericInfo = new HashMap<String, NumericInfoResource>();
        while (reader.hasNextNumericInfoResourceReader()) putNumericInfoResource(new NumericInfoResource(reader.nextNumericInfoResourceReader()));
        textInfo = new HashMap<String, TextInfoResource>();
        while (reader.hasNextTextInfoResourceReader()) putTextInfoResource(new TextInfoResource(reader.nextTextInfoResourceReader()));
        if (reader.hasTSIClientInitReader()) {
            tsiInit = new PropertyBlock(reader.getTSIClientInitReader());
        } else {
            tsiInit = null;
        }
        registries = new Vector<RegistryListItem>();
        while (reader.hasNextRegistryReader()) {
            registries.add(new RegistryListItem(reader.nextRegistryReader()));
        }
        storageTemplates = new HashMap<String, StorageTemplate>();
        while (reader.hasNextStorageTemplateReader()) putStorage(new StorageTemplate(reader.nextStorageTemplateReader()));
        applications = new HashMap<String, Application>();
        while (reader.hasNextApplicationReader()) putApplication(new Application(reader.nextApplicationReader()));
        transferProtocols = new HashMap<String, PropertyBlock>();
        while (reader.hasNextTranferProtocolReader()) {
            putTransferProtocol(new PropertyBlock(reader.nextTransferProtocolReader()));
        }
        users = new DistinguishedNameMap('/');
        while (reader.hasNextUserMapEntryReader()) {
            putUser(new UserEntry(reader.nextUserMapEntryReader()));
        }
        globalFields = new Context();
        while (reader.hasNextFieldReader()) {
            globalFields.putField(new Field(reader.nextFieldReader()));
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getUSpaceRoot() {
        return this.uSpaceRoot;
    }

    public void setUSpaceRoot(String uSpaceRoot) {
        this.uSpaceRoot = uSpaceRoot;
    }

    public String getFileSeparator() {
        return this.fileSeparator;
    }

    public void setFileSeparator(String fileSeparator) {
        this.fileSeparator = fileSeparator;
    }

    public double getOverhead() {
        return this.overhead;
    }

    public void setOverhead(double overhead) {
        this.overhead = overhead;
    }

    public Element getOperatingSystem() {
        return this.operatingSystem;
    }

    public void setOperatingSystem(Element os) {
        this.operatingSystem = os;
    }

    public Element getProcessor() {
        return this.processor;
    }

    public void setProcessor(Element processor) {
        this.processor = processor;
    }

    public Element getIndividualPhysicalMemory() {
        return this.memory;
    }

    public void setIndividualPhysicalMemory(Element memory) {
        this.memory = memory;
    }

    public void putNumericInfoResource(NumericInfoResource nir) {
        numericInfo.put(nir.getName(), nir);
    }

    public void putTextInfoResource(TextInfoResource tir) {
        textInfo.put(tir.getName(), tir);
    }

    public NumericInfoResource getNumericInfoResource(String propName) {
        return numericInfo.get(propName);
    }

    public TextInfoResource getTextInfoResource(String propName) {
        return textInfo.get(propName);
    }

    public Collection<NumericInfoResource> getNumericInfoResources() {
        return numericInfo.values();
    }

    public Collection<TextInfoResource> getTextInfoResources() {
        return textInfo.values();
    }

    public PropertyBlock getTSIClientInit() {
        return this.tsiInit;
    }

    public void setTSIClientInit(PropertyBlock tsiInit) {
        this.tsiInit = tsiInit;
    }

    public Collection<RegistryListItem> getRegistryList() {
        return this.registries;
    }

    public void putRegistry(RegistryListItem registry) {
        registries.add(registry);
    }

    public void putStorage(StorageTemplate st) {
        storageTemplates.put(st.getName(), st);
    }

    public StorageTemplate getStorage(String stName) {
        return storageTemplates.get(stName);
    }

    public Map<String, StorageTemplate> getStorageTemplates() {
        return storageTemplates;
    }

    public void putApplication(Application app) {
        applications.put(app.getName(), app);
    }

    public Application getApplication(String name) {
        return applications.get(name);
    }

    public void removeApplication(String applicationName, String applicationVersion) {
        applications.remove(applicationName);
    }

    public Map<String, Application> getApplications() {
        return applications;
    }

    public PropertyBlock getTransferProtocol(String protocolName) {
        return transferProtocols.get(protocolName);
    }

    public void putTransferProtocol(PropertyBlock protocol) {
        transferProtocols.put(protocol.getName(), protocol);
    }

    public void putUser(UserEntry entry) {
        users.addUserEntry(entry);
    }

    public void addUser(String subject, String name, String group, String role, Collection<Field> fields) {
        UserEntry ue = new UserEntry(subject, name, group, role);
        for (Field fld : fields) {
            ue.getContext().putField(fld);
        }
        users.addUserEntry(ue);
    }

    public User getUserBySubject(String subject) {
        UserEntry entry = users.getUserEntry(subject);
        if (entry == null) {
            return null;
        } else {
            return entry.getUser();
        }
    }

    public UserEntry getUserEntry(String dname) {
        return users.getUserEntry(dname);
    }

    public void removeUser(String userSubject) {
        users.removeUserEntry(userSubject);
    }

    public Collection<UserEntry> getUserMap() {
        return users.getUserEntries();
    }

    public Element buildElement(Document doc) {
        Element result = doc.createElementNS(XMLIDBConstants.IDB_NS, XMLIDBConstants.STATIC_PROPERTIES_TAG);
        if (name != null) {
            result.appendChild(ElementUtil.simpleElt(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.NAME_TAG, name));
        }
        if (descr != null) {
            result.appendChild(ElementUtil.simpleCDATA(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.DESCRIPTION_TAG, descr));
        }
        Element executionSystemElement = doc.createElementNS(XMLIDBConstants.IDB_NS, XMLIDBConstants.EXECUTION_SYSTEM_TAG);
        executionSystemElement.appendChild(ElementUtil.simpleCDATA(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.USPACE_ROOT_TAG, uSpaceRoot));
        executionSystemElement.appendChild(ElementUtil.simpleCDATA(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.FILE_SEPARATOR_TAG, fileSeparator));
        executionSystemElement.appendChild(ElementUtil.simpleElt(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.OVERHEAD_TAG, Double.toString(overhead)));
        if (operatingSystem != null) {
            Node osDoc = doc.importNode(operatingSystem, true);
            executionSystemElement.appendChild(osDoc);
        }
        if (processor != null) {
            Node procDoc = doc.importNode(processor, true);
            executionSystemElement.appendChild(procDoc);
        }
        if (memory != null) {
            Node memDoc = doc.importNode(memory, true);
            executionSystemElement.appendChild(memDoc);
        }
        executionSystemElement.appendChild(ElementUtil.simpleElt(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.MAXNUMBEROFJOBS_TAG, Integer.toString(maxNumberOfJobs)));
        executionSystemElement.appendChild(ElementUtil.simpleElt(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.NUMBEROFNODES_TAG, Integer.toString(numberOfNodes)));
        executionSystemElement.appendChild(ElementUtil.simpleElt(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.NETWORKLATENCY_TAG, Double.toString(networkLatency)));
        executionSystemElement.appendChild(ElementUtil.simpleElt(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.NETWORKBANDWIDTH_TAG, Double.toString(networkBandwidth)));
        for (Field f : globalFields.fields().values()) {
            executionSystemElement.appendChild(f.buildElement(doc));
        }
        result.appendChild(executionSystemElement);
        for (NumericInfoResource nir : numericInfo.values()) {
            result.appendChild(nir.buildElement(doc));
        }
        for (TextInfoResource tir : textInfo.values()) {
            result.appendChild(tir.buildElement(doc));
        }
        if (tsiInit != null) {
            result.appendChild(tsiInit.buildElement(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.TSI_CLIENT_TAG));
        }
        for (RegistryListItem registry : registries) {
            result.appendChild(registry.buildElement(doc));
        }
        for (StorageTemplate storageTemplate : storageTemplates.values()) {
            result.appendChild(storageTemplate.buildElement(doc));
        }
        for (Application application : applications.values()) {
            result.appendChild(application.buildElement(doc));
        }
        for (PropertyBlock transferProtocol : transferProtocols.values()) {
            result.appendChild(transferProtocol.buildElement(doc, XMLIDBConstants.IDB_NS, XMLIDBConstants.TRANSFER_PROTOCOL_TAG));
        }
        for (UserEntry user : users.getUserEntries()) {
            result.appendChild(user.buildElement(doc));
        }
        return result;
    }

    public Element buildElement() throws JDOMException {
        return buildElement(new XercesDOMAdapter().createDocument());
    }

    public void writeToFile(String fileName) throws JDOMException, IOException {
        File outputFile = new File(fileName);
        FileOutputStream os = new FileOutputStream(outputFile);
        writeToStream(os);
    }

    public void writeToStream(OutputStream os) throws JDOMException, IOException {
        Document doc = new XercesDOMAdapter().createDocument();
        XMLOutputter fmt = new XMLOutputter(Format.getPrettyFormat());
        DOMBuilder builder = new DOMBuilder();
        doc.appendChild(buildElement(doc));
        org.jdom.Document jdomDoc = builder.build(doc);
        fmt.output(jdomDoc, os);
        os.close();
    }

    public Map<String, PropertyBlock> getTransferProtocols() {
        return transferProtocols;
    }

    public void setMaxNumberOfJobs(int maxNumberOfJobs) {
        this.maxNumberOfJobs = maxNumberOfJobs;
    }

    public void setNetworkBandwidth(double networkBandwidth) {
        this.networkBandwidth = networkBandwidth;
    }

    public void setNetworkLatency(double networkLatency) {
        this.networkLatency = networkLatency;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public int getMaxNumberOfJobs() {
        return maxNumberOfJobs;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public double getNetworkLatency() {
        return networkLatency;
    }

    public double getNetworkBandwidth() {
        return networkBandwidth;
    }

    public Context getGlobalContext() {
        return globalFields;
    }
}
