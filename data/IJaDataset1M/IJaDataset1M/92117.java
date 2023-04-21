package it.ancona.comune.ankonhippo.repository;

import hippo.webdav.client.PropertyWithValue;
import hippo.webdav.client.WebdavScript;
import hippo.webdav.client.actions.AddPrivilegeToAggregatePrivilegeAction;
import hippo.webdav.client.actions.CreatePrivilegeAction;
import hippo.webdav.client.actions.SetPropertyAction;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;

public class RepositoryInitializationMain extends WebdavScript {

    private Properties m_properties;

    private List m_unconfiguredOperations = new ArrayList();

    private List m_unconfiguredProperties = new ArrayList();

    @Override
    public String getRootUri() {
        return "http://" + getHost() + ":" + getPort() + "/" + getRootPath() + "/";
    }

    @Override
    public String getAuthenticationRealm() {
        return m_properties.getProperty("authenticationRealm");
    }

    public String getHost() {
        return m_properties.getProperty("host");
    }

    public String getCustomer() {
        return m_properties.getProperty("customer");
    }

    public String getPort() {
        return m_properties.getProperty("port");
    }

    public String getRootPath() {
        return m_properties.getProperty("rootPath");
    }

    public String getUsername() {
        return m_properties.getProperty("username");
    }

    public String getPassword() {
        return m_properties.getProperty("password");
    }

    @Override
    public String getAuthenticationHost() {
        return getHost();
    }

    public RepositoryInitializationMain(String filename) throws IOException {
        super();
        m_properties = new Properties();
        m_properties.load(new BufferedInputStream(new FileInputStream(filename)));
    }

    public static void main(String[] args) throws HttpException, IOException, TransformerException {
        if (args.length == 1) {
            RepositoryInitializationMain repositoryPreparationScript = new RepositoryInitializationMain(args[0]);
            repositoryPreparationScript.run();
        } else {
            System.out.println("Usage: RepositoryInitializationMain <filename>");
            System.out.println("The file must be a Java properties file containing the following keys:");
            System.out.println("* host     (http:// <host> :80/webdav/files/customer.preview/content/bulk/index.xml);");
            System.out.println("* port     (http://cms.local: <port> /webdav/files/customer.preview/content/bulk/index.xml);");
            System.out.println("* rootPath (http://cms.local:80/ <rootPath> /files/customer.preview/content/bulk/index.xml);");
            System.out.println("* customer (http://cms.local:80/webdav/files/ <customer> .preview/content/bulk/index.xml);");
            System.out.println("* authenticationRealm (Slide Realm, johan realm);");
            System.out.println("* username (root);");
            System.out.println("* password (geheim, wachtwoord).");
        }
    }

    protected void startScriptBody() throws HttpException, IOException, TransformerException {
        createAnkonHippoPrivilegesAndAssignToRoles();
        assignAnkonHippoHistoryPrivilegesToRoles();
    }

    private void createAnkonHippoPrivilegesAndAssignToRoles() throws HttpException, IOException, TransformerException {
        PropertyWithValue privilegeNamespaceProp = new PropertyWithValue("privilege-namespace", "http://www.comune.ancona.it/cms/workflows/ankonhippo", "D", "DAV:");
        executeAction(new CreatePrivilegeAction("ankonhippo"));
        createPrivilege("ankonhippo/cancelDeletion", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/cancelDeletion"), "AnkonHippo, cancel deletion", "Beoordeelde acties, verwijdering annuleren");
        createPrivilege("ankonhippo/cancelPublication", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/cancelPublication"), "AnkonHippo, cancel publication", "Beoordeelde acties, publicatie annuleren");
        createPrivilege("ankonhippo/cancelPublicationForEditor", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/cancelPublicationForEditor"), "AnkonHippo, cancel publication (editor)", "Beoordeelde acties, publicatie annuleren (eindredacteur)");
        createPrivilege("ankonhippo/delete", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/delete"), "AnkonHippo, delete", "Beoordeelde acties, verwijderen");
        createPrivilege("ankonhippo/disapproveDeletion", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/disapproveDeletion"), "AnkonHippo, disapprove deletion", "Beoordeelde acties, verwijdering afkeuren");
        createPrivilege("ankonhippo/disapprovePublication", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/disapprovePublication"), "AnkonHippo, disapprove publication", "Beoordeelde acties, publicatie afkeuren");
        createPrivilege("ankonhippo/publish", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/publish"), "AnkonHippo, publish", "Beoordeelde acties, publiceer");
        createPrivilege("ankonhippo/requestDeletion", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/requestDeletion"), "AnkonHippo, request deletion", "Beoordeelde acties, verwijdering aanvragen");
        createPrivilege("ankonhippo/requestPublication", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/requestPublication"), "AnkonHippo, request publication", "Beoordeelde acties, publicatie aanvragen");
        createPrivilege("ankonhippo/save", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/save"), "AnkonHippo, save", "Beoordeelde acties, opslaan");
        createPrivilege("ankonhippo/saveForEditor", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/saveForEditor"), "AnkonHippo, save (editor)", "Beoordeelde acties, opslaan (eindredacteur)");
        createPrivilege("ankonhippo/saveForSupervisor", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/saveForSupervisor"), "AnkonHippo, save (supervisor)", "Beoordeelde acties, opslaan (eindredacteur)");
        createPrivilege("ankonhippo/unpublishNow", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/unpublishNow"), "AnkonHippo, unpublish now", "Beoordeelde acties, nu depubliceren");
        createPrivilege("ankonhippo/saveDraft", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/saveDraft"), "AnkonHippo, save draft", "Beoordeelde acties, klad opslaan");
        createPrivilege("ankonhippo/moveDocument", privilegeNamespaceProp);
        setActionLabels(getActionUri("ankonhippo/moveDocument"), "AnkonHippo, move document", "Beoordeelde acties, document verplaatsen");
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/save", "hippo-roles/author"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/requestPublication", "hippo-roles/author"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/cancelPublication", "hippo-roles/author"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/requestDeletion", "hippo-roles/author"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/cancelDeletion", "hippo-roles/author"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/save", "hippo-roles/editor"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/saveForEditor", "hippo-roles/editor"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/saveForSupervisor", "hippo-roles/editor"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/publish", "hippo-roles/editor"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/disapprovePublication", "hippo-roles/editor"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/cancelPublicationForEditor", "hippo-roles/editor"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/unpublishNow", "hippo-roles/editor"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/delete", "hippo-roles/editor"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/disapproveDeletion", "hippo-roles/editor"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/moveDocument", "hippo-roles/editor"));
    }

    private void setActionLabels(String uri, String englishLabel, String dutchLabel) throws HttpException, IOException, TransformerException {
        PropertyWithValue labelProp;
        labelProp = new PropertyWithValue("label-en", englishLabel, "H", "http://hippo.nl/cms/1.0");
        setProperty(uri, labelProp);
        labelProp = new PropertyWithValue("label-nl", dutchLabel, "H", "http://hippo.nl/cms/1.0");
        setProperty(uri, labelProp);
    }

    private void assignAnkonHippoHistoryPrivilegesToRoles() throws HttpException, IOException, TransformerException {
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/saveDraft", "hippo-roles/author"));
        executeAction(new AddPrivilegeToAggregatePrivilegeAction("ankonhippo/saveDraft", "hippo-roles/editor"));
    }

    private void setProperty(String uri, PropertyWithValue property) throws HttpException, IOException, TransformerException {
        executeAction(new SetPropertyAction(getRootCredentials(), uri, property));
    }

    private boolean isOperationEnabled(String operationName) {
        String propertyName = "operation." + operationName;
        String value = m_properties.getProperty(propertyName);
        if (value == null || value.equals("")) {
            m_unconfiguredOperations.add(propertyName);
        }
        return Boolean.valueOf(value).booleanValue();
    }

    private String rootUriWithoutTrailingSlashes;

    private synchronized String getRootUriWithoutTrailingSlashes() {
        if (rootUriWithoutTrailingSlashes == null) {
            rootUriWithoutTrailingSlashes = getRootUri();
            while (rootUriWithoutTrailingSlashes.endsWith("/")) {
                rootUriWithoutTrailingSlashes = rootUriWithoutTrailingSlashes.substring(0, rootUriWithoutTrailingSlashes.length() - 1);
            }
        }
        return rootUriWithoutTrailingSlashes;
    }

    @Override
    public UsernamePasswordCredentials getRootCredentials() {
        return new UsernamePasswordCredentials(getUsername(), getPassword());
    }

    @Override
    protected boolean doesUserWantToRunCleanup() {
        return false;
    }

    @Override
    protected void dispose() {
        super.dispose();
        if (!m_unconfiguredOperations.isEmpty()) {
            System.out.println();
            System.out.println("Unconfigured operations:");
            for (Iterator unconfiguredOperationsIterator = m_unconfiguredOperations.iterator(); unconfiguredOperationsIterator.hasNext(); ) {
                String unconfiguredOperation = (String) unconfiguredOperationsIterator.next();
                System.out.println(unconfiguredOperation);
            }
        }
        if (!m_unconfiguredProperties.isEmpty()) {
            System.out.println();
            System.out.println("Unconfigured properties:");
            for (Iterator unconfiguredPropertiesIterator = m_unconfiguredProperties.iterator(); unconfiguredPropertiesIterator.hasNext(); ) {
                String unconfiguredProperty = (String) unconfiguredPropertiesIterator.next();
                System.out.println(unconfiguredProperty);
            }
        }
    }

    private void createPrivilege(String relativePrivilegeUri, PropertyWithValue privilegeNamespaceProp) throws HttpException, IOException, TransformerException {
        executeAction(new CreatePrivilegeAction(relativePrivilegeUri));
        executeAction(new SetPropertyAction(getRootCredentials(), getActionUri(relativePrivilegeUri), privilegeNamespaceProp));
    }
}
