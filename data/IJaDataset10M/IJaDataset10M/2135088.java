package fmpp.setting;

/**
 * Encapsulates operations that dependend on J2SE 1.4 XML related classes.
 * These are separated to prevent linkage errors when XML related
 * classes are not available.
 */
interface XmlDependentOps {

    Object createCatalogResolver(String catalogs, Boolean preferPublic, Boolean allowCatalogPI);

    boolean isXmlDataLoaderOption(String optionName);
}
