package memops.metamodel;

import java.util.*;
import memops.general.MemopsException;

public class MetaConstant extends MetaModelElement {

    private static final String containerType = "constant";

    private static final Map allowedTags;

    private static final List metaParameterNames;

    private static final List booleanMetaParameters;

    private static final List fixedMetaParameters;

    private static final Map metaParameterDefaults;

    static {
        Map t = (Map) (TaggedValues.allowedTags.get("MetaConstant"));
        allowedTags = Util.cloneMap(t);
        Object[] s1 = { valueString, dataTypeString };
        metaParameterNames = Util.copyList(s1);
        Object[] s2 = {};
        booleanMetaParameters = Util.copyList(s2);
        Object[] s3 = {};
        fixedMetaParameters = Util.copyList(s3);
        Object[] k = {};
        Object[] v = {};
        metaParameterDefaults = Util.cloneMap(k, v);
    }

    public Object value = null;

    public MetaDataType dataType = null;

    public MetaPackage _package;

    public MetaConstant(MetaPackage container, Map params) throws MemopsException {
        initialiser(container, params);
        _package = container;
        container.addConstant(this);
    }

    public void checkValid() throws MemopsException {
        super.checkValid();
    }
}
