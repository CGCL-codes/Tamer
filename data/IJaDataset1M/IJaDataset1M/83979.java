package org.ws4d.java.configuration;

/**
 * Class holds property name and value
 */
public class Property {

    public static final String PROP_DEVICE_UUID = "DeviceUuid";

    public static final String PROP_METADATA_VERSION = "MetadataVersion";

    public static final String PROP_SEND_WSDL = "SendWSDL";

    public static final String PROP_SERVICE_SECURED = "ServiceSecured";

    public static final String PROP_ASYNC_ONEWAY_OPS = "AsyncOnewayOperations";

    public static final String PROP_BROADCAST_ADDRESS = "BroadcastAddress";

    public static final String PROP_DEVICE_IP_ADDRESS = "DeviceIPAddress";

    public static final String PROP_DEVICE_IP_CHECK = "DeviceIPCheck";

    public static final String PROP_DEVICE_START_TIME = "DeviceStartTime";

    public static final String PROP_DPWS_MCAST_MODE = "MulticastMode";

    public static final String PROP_FORCE_SHUTDOWN = "ForceShutdown";

    public static final String PROP_PROXY_USE_DISABLED = "ProxyUseDisabled";

    public static final String PROP_UDP_USE_TIMED_MESSAGEID_BUFFER = "UDPUseTimedMessageIDBuffer";

    public static final String PROP_WSDL_MEMORIZE = "WSDLMemorize";

    public static final String PROP_BUFFERED_IN_TIMEOUT_REPEAT = "BufferedInputTimeoutRepeat";

    public static final String PROP_BUFFERED_IN_TIMEOUT_WAIT = "BufferedInputTimeoutWait";

    public static final String PROP_BUFFERED_IN_USE_TIMEOUT = "BufferedInputUseTimeout";

    public static final String PROP_BUFFERED_IN_BUF_SIZE = "BufferedInputBufSize";

    public static final String PROP_BUFFERED_WRITER_BUF_SIZE = "BufferedWriterBufSize";

    public static final String PROP_BYTE_BUFFER_BUF_SIZE = "ByteBufferBufSize";

    public static final String PROP_HTTP_CLIENT_READ_TIMEOUT_REPEAT = "HttpClientReadTimeoutRepeat";

    public static final String PROP_HTTP_CLIENT_READ_TIMEOUT_WAIT = "HttpClientReadTimeoutWait";

    public static final String PROP_HTTP_SERVER_MAX_THREADS = "HttpServerMaxThreads";

    public static final String PROP_HTTP_SERVER_USE_THREAD_POOL = "HttpServerUseThreadPool";

    public static final String PROP_HTTP_SERVER_PORT = "HttpServerPort";

    public static final String PROP_HTTPS_SERVER_PORT = "HttpsServerPort";

    public static final String PROP_HTTP_CONNECTION_TIMEOUT = "HttpConnectionTimeout";

    public static final String PROP_SEARCH_CACHE_ENABLED = "SearchCacheEnabled";

    public static final String PROP_SEARCH_CACHE_ENTRY_EXPIRATION_TIME = "SearchCacheEntryExpirationTime";

    public static final String PROP_MAX_SEARCH_CACHE_THREAD_NUMBER = "MaxSearchCacheThreadNumber";

    public static final String PROP_MAX_SEARCH_CACHE_ENTRIES = "MaxSearchCacheEntries";

    public static final String PROP_SEARCH_CACHE_ENTRIES_REDUCTION = "SearchCacheEntriesReduction";

    public static final String PROP_SEARCH_CACHE_COMPARE_XADDRS = "CacheCompareXAddrs";

    public static final String PROP_PRESENTATION_URL_USE_STYLESHEET = "PresentationURLUseStylesheet";

    public static final String PROP_PRESENTATION_URL_ATTACHMENT_DURATION = "PresentationURLAttachmentDuration";

    public static final String PROP_PRESENTATION_URL_USE_FILECACHING = "PresentationURLUseFileCaching";

    public static final String PROP_PRESENTATION_URL_SHOW_RESOURCES_DATABOX = "PresentationURLShowRessourcesDatabox";

    public static final String PROP_PRESENTATION_URL_SHOW_SESSION_DATABOX = "PresentationURLShowSessionDatabox";

    public static final String PROP_PRESENTATION_URL_HIDE_DEFAULT_RESOURCES = "PresentationURLHideDefaultRessources";

    public static final String PROP_WSDLREPOSITORY_NAME = "WSDLRepositoryName";

    public static final String PROP_WSDLREPOSITORY_WSDL_AUTOADD = "WSDLRepositoryWSDLAutoAdd";

    public static final String PROP_SCHEMA_DOWNLOAD = "SchemaDownload";

    public static final String PROP_XML_INDENT = "XMLIndent";

    public static final String PROP_XML_VERSION = "XMLVersion";

    public static final String PROP_RESOLVER_CACHE_TIMEOUT = "ResolverCacheTimeout";

    public static final String PROP_RESOLVER_NO_CACHE = "ResolverNoCache";

    public static final String PROP_EVENTING_MAXTRIES = "EventingMaxTries";

    public static final String PROP_LOG_LEVEL = "LogLevel";

    public static final String PROP_LOG_TIMESTAMP = "LogTimestamp";

    public static final String PROP_LOG_XML_OUTPUT = "LogXMLOutput";

    public static final String PROP_DEVICE_SE_NETWORKINTERFACE = "DeviceSENetworkInterface";

    public static final String PROP_ENCODING = "Encoding";

    public static final String PROP_KEYSTORE_FILE = "KeyStoreFile";

    public static final String PROP_KEYSTORE_PASSWORD = "KeyStorePassword";

    public static final String PROP_DEVICE_ADMIN_SERVICE = "DeviceAdminService";

    public String key;

    public String value;

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return "<" + key + ">=<" + value + ">";
    }
}
