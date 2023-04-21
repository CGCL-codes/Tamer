package org.xmi.xml;

public class ParserConstants {

    protected static final String DEFAULT_ENCODING = "UTF-8";

    protected static final String DEFAULT_VERSION = "2.1";

    protected static final String XMI_NAMESPACE_2_1 = "http://schema.omg.org/spec/XMI/2.1";

    protected static final String DEFAULT_PREFIX = "xmi";

    protected static final String ID = "id";

    protected static final String TYPE = "type";

    protected static final String VERSION = "version";

    protected static final String XMI = "XMI";

    protected static final String xmi = "xmi";

    public static final String ELEMENT = "element";

    public static final String MODEL_ELEMENT = "ModelElement";

    public static final String NAME = "name";

    public static final String TAG = "Tag";

    public static final String TAG_ELEMENTS = "Tag.elements";

    public static final String TAG_VALUES = "Tag.values";

    public static final String URI = "uri";

    public static final String VALUE = "value";

    public static final String XMI_DOCUMENTATION = "XMI.documentation";

    public static final String XMI_IDREF = "xmi.idref";

    public static final String XMI_METAMODEL = "XMI.metamodel";

    public static final String XMI_MODEL = "XMI.model";

    public static final String XMI_NAME = "xmi.name";

    public static final String XMI_VERSION = "xmi.version";

    public static final ModelParser[] parsers = new ModelParser[] { new XMIVersion2Parser(), new XMIVersion1Parser() };
}
