package orcajo.azada.core.datasources;

public interface DatasourcesConfigConts {

    public static final String NAME_DATASOURCES = "datasources.xml";

    static final String DATASOURCES = "DataSources";

    static final String DATASOURCE = "DataSource";

    static final String DATASOURCE_NAME = "DataSourceName";

    static final String DATASOURCE_DESCRIPTION = "DataSourceDescription";

    static final String DATASOURCE_INFO = "DataSourceInfo";

    static final String DATASOURCE_MAPS = "Maps";

    static final String DATASOURCE_MAP = "Map";

    static final String DATASOURCE_CATALOGS = "Catalogs";

    static final String DATASOURCE_CATALOG = "Catalog";

    static final String DATASOURCE_CATALOG_DEFINICION = "Definition";

    static final String DATASOURCE_CATALOG_NAME = "name";

    static final String DATASOURCE_MAP_SCHEMA = "schema";

    static final String DATASOURCE_MAP_CUBE = "cube";

    static final String DATASOURCE_MAP_HIERARCHY = "hierarchy";

    static final String DATASOURCE_MAP_SVG = "svg";

    static final String DATASOURCE_MAP_SVG_LEVEL = "level";

    static final String DATASOURCE_MAP_SVG_PATH = "path";

    static final String PROVIDER = "Provider";

    static final String DATASOURCE_PROVIDER_NAME = "ProviderName";

    static final String DATASOURCE_PROVIDER_TYPE = "ProviderType";

    static final String DATASOURCE_AUTHENTICATION_MODE = "AuthenticationMode";

    static final String PROVIDER_MONDRIAN_VALUE = "Mondrian";

    static final String PROVIDER_XMLA_VALUE = "XMLA";

    static final String DEFAULT_PROVIDER_NAME_VALUE = PROVIDER_MONDRIAN_VALUE;

    static final String TYPE_MDP = "MDP";

    static final String DEFAULT_PROVIDER_TYPE_VALUE = TYPE_MDP;

    static final String MODE_AUTHENTICATION_UN_AUTHENTICATION_ = "Unauthenticated";

    static final String DEFAULT_AUTHENTICATION_MODE_VALUE = MODE_AUTHENTICATION_UN_AUTHENTICATION_;

    static final String DTD = "/orcajo/azada/core/datasources/datasourcesconfig.dtd";
}
