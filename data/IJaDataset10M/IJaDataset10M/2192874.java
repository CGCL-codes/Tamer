package edu.mit.lcs.haystack;

import edu.mit.lcs.haystack.rdf.Literal;
import edu.mit.lcs.haystack.rdf.Resource;

/**
 * Namespace and resource constants used throughout Haystack.
 * @author Dennis Quan
 */
public final class Constants {

    public static final String s_acl_namespace = "http://haystack.lcs.mit.edu/schemata/acl#";

    public static final String s_ann_namespace = "http://haystack.lcs.mit.edu/schemata/annotation#";

    public static final String s_applet_namespace = "http://haystack.lcs.mit.edu/schemata/ozoneapplet#";

    public static final String s_cdo_namespace = "http://haystack.lcs.mit.edu/schemata/cdo#";

    public static final String s_collab_namespace = "http://haystack.lcs.mit.edu/interface/collaboration#";

    public static final String s_config_namespace = "http://haystack.lcs.mit.edu/schemata/config#";

    public static final String s_content_namespace = "http://haystack.lcs.mit.edu/schemata/content#";

    public static final String s_daml_namespace = "http://www.daml.org/2001/03/daml+oil#";

    public static final String s_dc_namespace = "http://purl.org/dc/elements/1.1/";

    public static final String s_editor_namespace = "http://haystack.lcs.mit.edu/ui/ozoneeditor#";

    public static final String s_federation_namespace = "http://haystack.lcs.mit.edu/schemata/federation#";

    public static final String s_frame_namespace = "http://haystack.lcs.mit.edu/ui/frame#";

    public static final String s_haystack_namespace = "http://haystack.lcs.mit.edu/schemata/haystack#";

    public static final String s_http_namespace = "http://schemas.xmlsoap.org/wsdl/http/";

    public static final String s_info_namespace = "http://haystack.lcs.mit.edu/schemata/information#";

    public static final String s_infoint_namespace = "http://haystack.lcs.mit.edu/interfaces/information#";

    public static final String s_ldap_namespace = "http://haystack.lcs.mit.edu/agents/ldap#";

    public static final String s_lensui_namespace = "http://haystack.lcs.mit.edu/ui/lens#";

    public static final String s_mail_namespace = "http://haystack.lcs.mit.edu/schemata/mail#";

    public static final String s_metaglue_namespace = "http://www.ai.mit.edu/projects/iroom/metaglue/schema#";

    public static final String s_note_namespace = "http://haystack.lcs.mit.edu/schemata/note#";

    public static final String s_nlp_namespace = "http://haystack.lcs.mit.edu/schemata/nlp#";

    public static final String s_ozone_namespace = "http://haystack.lcs.mit.edu/schemata/ozone#";

    public static final String s_query_namespace = "http://haystack.lcs.mit.edu/schemata/query#";

    public static final String s_rdf_namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static final String s_rdfs_namespace = "http://www.w3.org/2000/01/rdf-schema#";

    public static final String s_rdfstore_namespace = "http://haystack.lcs.mit.edu/agents/rdfstore#";

    public static final String s_scheduler_namespace = "http://haystack.lcs.mit.edu/agents/scheduler#";

    public static final String s_slide_namespace = "http://haystack.lcs.mit.edu/schemata/ozoneslide#";

    public static final String s_soap_namespace = "http://schemas.xmlsoap.org/wsdl/soap/";

    public static final String s_soapenc_namespace = "http://schemas.xmlsoap.org/soap/encoding/";

    public static final String s_status_namespace = "http://haystack.lcs.mit.edu/ui/status#";

    public static final String s_subscriptions_namespace = "http://haystack.lcs.mit.edu/agents/subscriptions#";

    public static final String s_text_namespace = "http://haystack.lcs.mit.edu/agents/text#";

    public static final String s_vcard_namespace = "http://haystack.lcs.mit.edu/schemata/vcard#";

    public static final String s_verb_namespace = "http://haystack.lcs.mit.edu/schemata/verb#";

    public static final String s_web_namespace = "http://haystack.lcs.mit.edu/schemata/web#";

    public static final String s_wildcard_namespace = "urn:haystack:wildcard:";

    public static final String s_wsdl_namespace = "http://schemas.xmlsoap.org/wsdl/";

    public static final String s_navView_namespace = "http://haystack.lcs.mit.edu/ui/navigationView#";

    public static final String s_xsd_namespace = "http://www.w3.org/2001/XMLSchema#";

    public static final String s_cv_namespace = "http://haystack.lcs.mit.edu/ui/collectionView#";

    public static final String s_layout_namespace = "http://haystack.lcs.mit.edu/schemata/layout#";

    public static final String s_dataProvider_namespace = "http://haystack.lcs.mit.edu/schemata/dataProvider#";

    public static final String s_summaryView_namespace = "http://haystack.lcs.mit.edu/ui/summaryView#";

    public static final String s_rdfLinks_namespace = "http://haystack.lcs.mit.edu/schemata/RDFLinks#";

    public static final Resource s_acl_anyone = new Resource(s_acl_namespace + "anyone");

    public static final Resource s_lensui_LensPart = new Resource(s_lensui_namespace + "LensPart");

    public static final Resource s_lensui_underlyingSource = new Resource(s_lensui_namespace + "underlyingSource");

    public static final Resource s_ann_description = new Resource(s_ann_namespace + "description");

    public static final Resource s_cdo_folder = new Resource(s_cdo_namespace + "folder");

    public static final Resource s_config_adenineMethod = new Resource(s_config_namespace + "adenineMethod");

    public static final Resource s_config_AdenineService = new Resource(s_config_namespace + "AdenineService");

    public static final Resource s_config_bindingDomain = new Resource(s_config_namespace + "bindingDomain");

    public static final Resource s_config_canConnectTo = new Resource(s_config_namespace + "canConnectTo");

    public static final Resource s_config_contentService = new Resource(s_config_namespace + "contentService");

    public static final Resource s_config_dependsOn = new Resource(s_config_namespace + "dependsOn");

    public static final Resource s_config_defaultInformationSource = new Resource(s_config_namespace + "defaultInformationSource");

    public static final Resource s_config_HaystackServer = new Resource(s_config_namespace + "HaystackServer");

    public static final Resource s_config_hostsService = new Resource(s_config_namespace + "hostsService");

    public static final Resource s_config_hostsTransport = new Resource(s_config_namespace + "hostsTransport");

    public static final Resource s_config_includes = new Resource(s_config_namespace + "includes");

    public static final Resource s_config_informationSource = new Resource(s_config_namespace + "informationSource");

    public static final Resource s_config_init = new Resource(s_config_namespace + "init");

    public static final Resource s_config_javaInterface = new Resource(s_config_namespace + "javaInterface");

    public static final Resource s_config_Method = new Resource(s_config_namespace + "Method");

    public static final Resource s_config_method = new Resource(s_config_namespace + "method");

    public static final Resource s_config_OntologyData = new Resource(s_config_namespace + "OntologyData");

    public static final Resource s_config_operation = new Resource(s_config_namespace + "operation");

    public static final Resource s_config_packages = new Resource(s_config_namespace + "packages");

    public static final Resource s_config_packageSet = new Resource(s_config_namespace + "packageSet");

    public static final Resource s_config_port = new Resource(s_config_namespace + "port");

    public static final Resource s_config_resourceLocation = new Resource(s_config_namespace + "resourceLocation");

    public static final Resource s_config_rootDir = new Resource(s_config_namespace + "rootDir");

    public static final Resource s_config_secondaryInformationSource = new Resource(s_config_namespace + "secondaryInformationSource");

    public static final Resource s_config_Service = new Resource(s_config_namespace + "Service");

    public static final Resource s_config_service = new Resource(s_config_namespace + "service");

    public static final Resource s_config_singleton = new Resource(s_config_namespace + "singleton");

    public static final Resource s_config_startEarly = new Resource(s_config_namespace + "startEarly");

    public static final Resource s_config_transferTo = new Resource(s_config_namespace + "transferTo");

    public static final Resource s_config_Transport = new Resource(s_config_namespace + "Transport");

    public static final Resource s_content_Content = new Resource(s_content_namespace + "Content");

    public static final Resource s_content_content = new Resource(s_content_namespace + "content");

    public static final Resource s_content_ContentService = new Resource(s_content_namespace + "ContentService");

    public static final Resource s_content_FilesystemContent = new Resource(s_content_namespace + "FilesystemContent");

    public static final Resource s_content_HttpContent = new Resource(s_content_namespace + "HttpContent");

    public static final Resource s_content_JavaClasspathContent = new Resource(s_content_namespace + "JavaClasspathContent");

    public static final Resource s_content_LiteralContent = new Resource(s_content_namespace + "LiteralContent");

    public static final Resource s_content_ServiceBackedContent = new Resource(s_content_namespace + "ServiceBackedContent");

    public static final Resource s_content_service = new Resource(s_content_namespace + "service");

    public static final Resource s_content_NullContent = new Resource(s_content_namespace + "NullContent");

    public static final Resource s_content_path = new Resource(s_content_namespace + "path");

    public static final Resource s_daml_DatatypeProperty = new Resource(s_daml_namespace + "DatatypeProperty");

    public static final Resource s_daml_first = new Resource(s_rdf_namespace + "first");

    public static final Resource s_daml_List = new Resource(s_rdf_namespace + "List");

    public static final Resource s_daml_nil = new Resource(s_rdf_namespace + "nil");

    public static final Resource s_daml_ObjectProperty = new Resource(s_daml_namespace + "ObjectProperty");

    public static final Resource s_daml_rest = new Resource(s_rdf_namespace + "rest");

    public static final Resource s_daml_Thing = new Resource(s_daml_namespace + "Thing");

    public static final Resource s_daml_equivalentTo = new Resource(s_daml_namespace + "equivalentTo");

    public static final Resource s_daml_UniqueProperty = new Resource(s_daml_namespace + "UniqueProperty");

    public static final Resource s_dc_creator = new Resource(s_dc_namespace + "creator");

    public static final Resource s_dc_date = new Resource(s_dc_namespace + "date");

    public static final Resource s_dc_description = new Resource(s_dc_namespace + "description");

    public static final Resource s_dc_format = new Resource(s_dc_namespace + "format");

    public static final Resource s_dc_title = new Resource(s_dc_namespace + "title");

    public static final Resource s_editor_disallowBlanks = new Resource(s_editor_namespace + "disallowBlanks");

    public static final Resource s_editor_metadataEditor = new Resource(s_editor_namespace + "metadataEditor");

    public static final Resource s_editor_multiline = new Resource(s_editor_namespace + "multiline");

    public static final Resource s_editor_onValueSet = new Resource(s_editor_namespace + "onValueSet");

    public static final Resource s_editor_propertiesToDisplay = new Resource(s_editor_namespace + "propertiesToDisplay");

    public static final Resource s_editor_propertyEditor = new Resource(s_editor_namespace + "propertyEditor");

    public static final Resource s_editor_target = new Resource(s_editor_namespace + "target");

    public static final Resource s_editor_titleSource = new Resource(s_editor_namespace + "titleSource");

    public static final Resource s_editor_valuesSource = new Resource(s_editor_namespace + "valuesSource");

    public static final Resource s_federation_priority = new Resource(s_federation_namespace + "priority");

    public static final Resource s_federation_service = new Resource(s_federation_namespace + "service");

    public static final Resource s_federation_source = new Resource(s_federation_namespace + "source");

    public static final Resource s_federation_suppliedPredicate = new Resource(s_federation_namespace + "suppliedPredicate");

    public static final Resource s_frame_tooltip = new Resource(s_frame_namespace + "tooltip");

    public static final Resource s_haystack_asserts = new Resource(s_haystack_namespace + "asserts");

    public static final Resource s_haystack_category = new Resource(s_haystack_namespace + "category");

    public static final Resource s_haystack_className = new Resource(s_haystack_namespace + "className");

    public static final Resource s_haystack_classView = new Resource(s_haystack_namespace + "classView");

    public static final Resource s_haystack_Collection = new Resource(s_haystack_namespace + "Collection");

    public static final Resource s_haystack_ContainmentProperty = new Resource(s_haystack_namespace + "ContainmentProperty");

    public static final Resource s_haystack_DisposablePackage = new Resource(s_haystack_namespace + "DisposablePackage");

    public static final Resource s_haystack_JavaClass = new Resource(s_haystack_namespace + "JavaClass");

    public static final Resource s_haystack_javaImplementation = new Resource(s_haystack_namespace + "javaImplementation");

    public static final Resource s_haystack_lastPackageUse = new Resource(s_haystack_namespace + "lastPackageUse");

    public static final Resource s_haystack_list = new Resource(s_haystack_namespace + "list");

    public static final Resource s_haystack_List = new Resource(s_haystack_namespace + "List");

    public static final Resource s_haystack_md5 = new Resource(s_haystack_namespace + "md5");

    public static final Resource s_haystack_member = new Resource(s_haystack_namespace + "member");

    public static final Resource s_haystack_Package = new Resource(s_haystack_namespace + "Package");

    public static final Resource s_haystack_packageStatement = new Resource(s_haystack_namespace + "packageStatement");

    public static final Resource s_haystack_PasswordProperty = new Resource(s_haystack_namespace + "PasswordProperty");

    public static final Resource s_haystack_Person = new Resource(s_haystack_namespace + "Person");

    public static final Resource s_haystack_pluginName = new Resource(s_haystack_namespace + "pluginName");

    public static final Resource s_haystack_ProprietalProperty = new Resource(s_haystack_namespace + "ProprietalProperty");

    public static final Resource s_haystack_publicKey = new Resource(s_haystack_namespace + "publicKey");

    public static final Resource s_haystack_RelationalProperty = new Resource(s_haystack_namespace + "RelationalProperty");

    public static final Resource s_haystack_Service = new Resource(s_haystack_namespace + "Service");

    public static final Resource s_haystack_Storage = new Resource(s_haystack_namespace + "Storage");

    public static final Resource s_haystack_user = new Resource(s_haystack_namespace + "user");

    public static final Resource s_haystack_view = new Resource(s_haystack_namespace + "view");

    public static final Resource s_haystack_Visitation = new Resource(s_haystack_namespace + "Visitation");

    public static final Resource s_haystack_visitedBy = new Resource(s_haystack_namespace + "visitedBy");

    public static final Resource s_haystack_visitedResource = new Resource(s_haystack_namespace + "visitedResource");

    public static final Resource s_haystack_visitTime = new Resource(s_haystack_namespace + "visitTime");

    public static final Resource s_haystack_reversiblePred = new Resource(s_haystack_namespace + "reversiblePred");

    public static final Resource s_http_address = new Resource(s_http_namespace + "address");

    public static final Resource s_http_Binding = new Resource(s_http_namespace + "Binding");

    public static final Resource s_http_verb = new Resource(s_http_namespace + "verb");

    public static final Resource s_info_knowsAbout = new Resource(s_info_namespace + "knowsAbout");

    public static final Resource s_infoint_query = new Resource(s_infoint_namespace + "query");

    public static final Resource s_ldap_baseDN = new Resource(s_ldap_namespace + "baseDN");

    public static final Resource s_ldap_child = new Resource(s_ldap_namespace + "child");

    public static final Resource s_ldap_host = new Resource(s_ldap_namespace + "host");

    public static final Resource s_ldap_password = new Resource(s_ldap_namespace + "password");

    public static final Resource s_ldap_port = new Resource(s_ldap_namespace + "port");

    public static final Resource s_ldap_user = new Resource(s_ldap_namespace + "user");

    public static final Resource s_mail_AliasEndpoint = new Resource(s_mail_namespace + "AliasEndpoint");

    public static final Resource s_mail_AsynchronousMessage = new Resource(s_mail_namespace + "AsynchronousMessage");

    public static final Resource s_mail_Attachment = new Resource(s_mail_namespace + "Attachment");

    public static final Resource s_mail_attachment = new Resource(s_mail_namespace + "attachment");

    public static final Resource s_mail_away = new Resource(s_mail_namespace + "away");

    public static final Resource s_mail_bcc = new Resource(s_mail_namespace + "bcc");

    public static final Resource s_mail_body = new Resource(s_mail_namespace + "body");

    public static final Resource s_mail_cc = new Resource(s_mail_namespace + "cc");

    public static final Resource s_mail_chat = new Resource(s_mail_namespace + "chat");

    public static final Resource s_mail_deliveredForMessage = new Resource(s_mail_namespace + "deliveredForMessage");

    public static final Resource s_mail_dnd = new Resource(s_mail_namespace + "dnd");

    public static final Resource s_mail_EmailAddress = new Resource(s_mail_namespace + "EmailAddress");

    public static final Resource s_mail_Endpoint = new Resource(s_mail_namespace + "Endpoint");

    public static final Resource s_mail_endpoint = new Resource(s_mail_namespace + "endpoint");

    public static final Resource s_mail_EndpointSpec = new Resource(s_mail_namespace + "EndpointSpec");

    public static final Resource s_mail_from = new Resource(s_mail_namespace + "from");

    public static final Resource s_mail_hasEndpoint = new Resource(s_mail_namespace + "hasEndpoint");

    public static final Resource s_mail_inReplyTo = new Resource(s_mail_namespace + "inReplyTo");

    public static final Resource s_mail_MailAgent = new Resource(s_mail_namespace + "MailAgent");

    public static final Resource s_mail_Message = new Resource(s_mail_namespace + "Message");

    public static final Resource s_mail_messageID = new Resource(s_mail_namespace + "messageID");

    public static final Resource s_mail_nearby = new Resource(s_mail_namespace + "nearby");

    public static final Resource s_mail_offline = new Resource(s_mail_namespace + "offline");

    public static final Resource s_mail_onlineStatus = new Resource(s_mail_namespace + "onlineStatus");

    public static final Resource s_mail_read = new Resource(s_mail_namespace + "read");

    public static final Resource s_mail_received = new Resource(s_mail_namespace + "received");

    public static final Resource s_mail_replyTo = new Resource(s_mail_namespace + "replyTo");

    public static final Resource s_mail_resolvedEndpointSpec = new Resource(s_mail_namespace + "resolvedEndpointSpec");

    public static final Resource s_mail_resource = new Resource(s_mail_namespace + "resource");

    public static final Resource s_mail_sentDtTm = new Resource(s_mail_namespace + "sentDtTm");

    public static final Resource s_mail_subject = new Resource(s_mail_namespace + "subject");

    public static final Resource s_mail_SynchronousMessage = new Resource(s_mail_namespace + "SynchronousMessage");

    public static final Resource s_mail_targetCollection = new Resource(s_mail_namespace + "targetCollection");

    public static final Resource s_mail_textRenderer = new Resource(s_mail_namespace + "textRenderer");

    public static final Resource s_mail_Thread = new Resource(s_mail_namespace + "Thread");

    public static final Resource s_mail_thread = new Resource(s_mail_namespace + "thread");

    public static final Resource s_mail_to = new Resource(s_mail_namespace + "to");

    public static final Resource s_mail_xaway = new Resource(s_mail_namespace + "xaway");

    public static final Resource s_metaglue_agentName = new Resource(s_metaglue_namespace + "agentName");

    public static final Resource s_metaglue_designation = new Resource(s_metaglue_namespace + "designation");

    public static final Resource s_nlp_meaningContent = new Resource(s_nlp_namespace + "meaningContent");

    public static final Resource s_nlp_meaningPredicate = new Resource(s_nlp_namespace + "meaningPredicate");

    public static final Resource s_nlp_meaningType = new Resource(s_nlp_namespace + "meaningType");

    public static final Resource s_nlp_part = new Resource(s_nlp_namespace + "part");

    public static final Resource s_nlp_querySpecification = new Resource(s_nlp_namespace + "querySpecification");

    public static final Resource s_nlp_verbcategory = new Resource(s_nlp_namespace + "verbcategory");

    public static final Resource s_nlp_verbform = new Resource(s_nlp_namespace + "verbform");

    public static final Resource s_nlp_Word = new Resource(s_nlp_namespace + "Word");

    public static final Resource s_nlp_word = new Resource(s_nlp_namespace + "word");

    public static final Resource s_note_Note = new Resource(s_note_namespace + "Note");

    public static final Resource s_query_AndCondition = new Resource(s_query_namespace + "AndCondition");

    public static final Resource s_query_ContentContainsCondition = new Resource(s_query_namespace + "ContentContainsCondition");

    public static final Resource s_query_EqualityCondition = new Resource(s_query_namespace + "EqualityCondition");

    public static final Resource s_query_existential = new Resource(s_query_namespace + "existential");

    public static final Resource s_query_Indexable = new Resource(s_query_namespace + "Indexable");

    public static final Resource s_query_luceneAgent = new Resource(s_query_namespace + "luceneAgent");

    public static final Resource s_query_object = new Resource(s_query_namespace + "object");

    public static final Resource s_query_OrCondition = new Resource(s_query_namespace + "OrCondition");

    public static final Resource s_query_parameter = new Resource(s_query_namespace + "parameter");

    public static final Resource s_query_predicate = new Resource(s_query_namespace + "predicate");

    public static final Resource s_query_Query = new Resource(s_query_namespace + "Query");

    public static final Resource s_query_QueryAgent = new Resource(s_query_namespace + "QueryAgent");

    public static final Resource s_query_QueryInterpreter = new Resource(s_query_namespace + "QueryInterpreter");

    public static final Resource s_query_QuerySource = new Resource(s_query_namespace + "QuerySource");

    public static final Resource s_query_results = new Resource(s_query_namespace + "results");

    public static final Resource s_query_StatementCondition = new Resource(s_query_namespace + "StatementCondition");

    public static final Resource s_query_subCondition = new Resource(s_query_namespace + "subCondition");

    public static final Resource s_query_subject = new Resource(s_query_namespace + "subject");

    public static final Resource s_query_target = new Resource(s_query_namespace + "target");

    public static final Resource s_query_targetExistential = new Resource(s_query_namespace + "targetExistential");

    public static final Resource s_query_terms = new Resource(s_query_namespace + "terms");

    public static final Resource s_query_text = new Resource(s_query_namespace + "text");

    public static final Resource s_query_naturalLanguage = new Resource(s_query_namespace + "naturalLanguage");

    public static final Resource s_rdf_object = new Resource(s_rdf_namespace + "object");

    public static final Resource s_rdf_predicate = new Resource(s_rdf_namespace + "predicate");

    public static final Resource s_rdf_Property = new Resource(s_rdf_namespace + "Property");

    public static final Resource s_rdf_Statement = new Resource(s_rdf_namespace + "Statement");

    public static final Resource s_rdf_subject = new Resource(s_rdf_namespace + "subject");

    public static final Resource s_rdf_type = new Resource(s_rdf_namespace + "type");

    public static final Resource s_rdfs_comment = new Resource(s_rdfs_namespace + "comment");

    public static final Resource s_rdfs_domain = new Resource(s_rdfs_namespace + "domain");

    public static final Resource s_rdfs_isDefinedBy = new Resource(s_rdfs_namespace + "isDefinedBy");

    public static final Resource s_rdfs_label = new Resource(s_rdfs_namespace + "label");

    public static final Resource s_rdfs_range = new Resource(s_rdfs_namespace + "range");

    public static final Resource s_rdfs_subClassOf = new Resource(s_rdfs_namespace + "subClassOf");

    public static final Resource s_rdfstore_databaseURL = new Resource(s_rdfstore_namespace + "databaseURL");

    public static final Resource s_rdfstore_jdbcDriver = new Resource(s_rdfstore_namespace + "jdbcDriver");

    public static final Resource s_rdfstore_onStatementAdded = new Resource(s_rdfstore_namespace + "onStatementAdded");

    public static final Resource s_rdfstore_onStatementRemoved = new Resource(s_rdfstore_namespace + "onStatementRemoved");

    public static final Resource s_rdfstore_password = new Resource(s_rdfstore_namespace + "password");

    public static final Resource s_rdfstore_username = new Resource(s_rdfstore_namespace + "username");

    public static final Resource s_scheduler_frequency = new Resource(s_scheduler_namespace + "frequency");

    public static final Resource s_scheduler_timeOfDay = new Resource(s_scheduler_namespace + "timeOfDay");

    public static final Resource s_scheduler_lastRun = new Resource(s_scheduler_namespace + "lastRun");

    public static final Resource s_scheduler_performScheduledTask = new Resource(s_scheduler_namespace + "performScheduledTask");

    public static final Resource s_scheduler_service = new Resource(s_scheduler_namespace + "service");

    public static final Resource s_scheduler_runNow = new Resource(s_scheduler_namespace + "runNow");

    public static final Resource s_scheduler_Task = new Resource(s_scheduler_namespace + "Task");

    public static final Resource s_soap_address = new Resource(s_soap_namespace + "address");

    public static final Resource s_soap_encodingStyle = new Resource(s_soap_namespace + "encodingStyle");

    public static final Resource s_soap_namespace_ = new Resource(s_soap_namespace + "namespace");

    public static final Resource s_status_Error = new Resource(s_status_namespace + "Error");

    public static final Resource s_status_error = new Resource(s_status_namespace + "error");

    public static final Resource s_status_JavaExceptionError = new Resource(s_status_namespace + "JavaExceptionError");

    public static final Resource s_status_stackTrace = new Resource(s_status_namespace + "stackTrace");

    public static final Resource s_status_TextError = new Resource(s_status_namespace + "TextError");

    public static final Resource s_subscriptions_source = new Resource(s_subscriptions_namespace + "source");

    public static final Resource s_text_extractedText = new Resource(s_text_namespace + "extractedText");

    public static final Resource s_text_summary = new Resource(s_text_namespace + "summary");

    public static final Resource s_text_contentService = new Resource(s_text_namespace + "contentService");

    public static final Resource s_text_contains = new Resource(s_text_namespace + "contains");

    public static final Resource s_text_textToCopy = new Resource(s_text_namespace + "textToCopy");

    public static final Resource s_verb_adenineService = new Resource(s_verb_namespace + "adenineService");

    public static final Resource s_verb_domain = new Resource(s_verb_namespace + "domain");

    public static final Resource s_verb_partDomain = new Resource(s_verb_namespace + "partDomain");

    public static final Resource s_verb_titleGenerator = new Resource(s_verb_namespace + "titleGenerator");

    public static final Resource s_web_WebPage = new Resource(s_web_namespace + "WebPage");

    public static final Resource s_wsdl_Binding = new Resource(s_wsdl_namespace + "Binding");

    public static final Resource s_wsdl_binding = new Resource(s_wsdl_namespace + "binding");

    public static final Resource s_wsdl_bindingOperation = new Resource(s_wsdl_namespace + "bindingOperation");

    public static final Resource s_wsdl_input = new Resource(s_wsdl_namespace + "input");

    public static final Resource s_wsdl_inputBinding = new Resource(s_wsdl_namespace + "inputBinding");

    public static final Resource s_wsdl_output = new Resource(s_wsdl_namespace + "output");

    public static final Resource s_wsdl_operation = new Resource(s_wsdl_namespace + "operation");

    public static final Resource s_wsdl_operationBinding = new Resource(s_wsdl_namespace + "operationBinding");

    public static final Resource s_wsdl_part = new Resource(s_wsdl_namespace + "part");

    public static final Resource s_wsdl_Port = new Resource(s_wsdl_namespace + "Port");

    public static final Resource s_wsdl_port = new Resource(s_wsdl_namespace + "port");

    public static final Resource s_wsdl_PortType = new Resource(s_wsdl_namespace + "PortType");

    public static final Resource s_wsdl_type = new Resource(s_wsdl_namespace + "type");

    public static final Resource s_xsd_dateTime = new Resource(s_xsd_namespace + "dateTime");

    public static final Resource s_xsd_float = new Resource(s_xsd_namespace + "float");

    public static final Resource s_xsd_int = new Resource(s_xsd_namespace + "int");

    public static final Resource s_xsd_string = new Resource(s_xsd_namespace + "string");

    public static final Resource s_xsd_boolean = new Resource(s_xsd_namespace + "boolean");

    public static final Literal s_emptyLiteral = new Literal("");

    private static String osname = System.getProperty("os.name");

    public static final boolean isMacOSX = "Mac OS X".equals(osname);

    public static final boolean isWindows = "Windows".equals(osname);

    public static final boolean isLinux = "Linux".equals(osname);

    public static final Resource s_startingPoints = new Resource("http://haystack.lcs.mit.edu/data/operations#startingPoints");
}
