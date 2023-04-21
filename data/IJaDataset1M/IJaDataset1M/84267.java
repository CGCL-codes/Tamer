package org.dmd.dmg.generated.dmo;

import java.util.HashMap;
import java.util.Iterator;
import org.dmd.dmc.*;
import org.dmd.dms.generated.dmo.MetaDMSAG;
import org.dmd.dms.generated.enums.ClassTypeEnum;
import org.dmd.dms.generated.enums.DataTypeEnum;
import org.dmd.dms.generated.enums.ValueTypeEnum;

public class DmgDMSAG implements DmcCompactSchemaIF {

    static String schemaName = "dmg";

    static int schemaBaseID = 200;

    static int schemaIDRange = 50;

    static int schemaMaxID = 250;

    public static final DmcAttributeInfo __alias = new DmcAttributeInfo("alias", 206, "String", ValueTypeEnum.SINGLE, DataTypeEnum.PERSISTENT);

    public static final DmcAttributeInfo __configSuffix = new DmcAttributeInfo("configSuffix", 202, "String", ValueTypeEnum.MULTI, DataTypeEnum.PERSISTENT);

    public static final DmcAttributeInfo __genPackage = new DmcAttributeInfo("genPackage", 203, "String", ValueTypeEnum.SINGLE, DataTypeEnum.PERSISTENT);

    public static final DmcAttributeInfo __generator = new DmcAttributeInfo("generator", 200, "Generator", ValueTypeEnum.MULTI, DataTypeEnum.PERSISTENT);

    public static final DmcAttributeInfo __schemaToLoad = new DmcAttributeInfo("schemaToLoad", 201, "String", ValueTypeEnum.SINGLE, DataTypeEnum.PERSISTENT);

    public static final DmcAttributeInfo __upperConstantName = new DmcAttributeInfo("upperConstantName", 205, "String", ValueTypeEnum.SINGLE, DataTypeEnum.PERSISTENT);

    public static final DmcClassInfo __DmgConfig = new DmcClassInfo("DmgConfig", 201, ClassTypeEnum.STRUCTURAL, DataTypeEnum.PERSISTENT, null, null);

    static HashMap<Integer, DmcClassInfo> _CmAp;

    static HashMap<Integer, DmcAttributeInfo> _SmAp;

    static HashMap<String, DmcNameBuilderIF> _NmAp;

    static HashMap<String, DmcFilterBuilderIF> _FmAp;

    static HashMap<String, DmcSliceInfo> _SImAp;

    static {
        _CmAp = new HashMap<Integer, DmcClassInfo>();
        _SmAp = new HashMap<Integer, DmcAttributeInfo>();
        _NmAp = new HashMap<String, DmcNameBuilderIF>();
        _FmAp = new HashMap<String, DmcFilterBuilderIF>();
        _SImAp = new HashMap<String, DmcSliceInfo>();
        _SmAp.put(__alias.id, __alias);
        _SmAp.put(__configSuffix.id, __configSuffix);
        _SmAp.put(__genPackage.id, __genPackage);
        _SmAp.put(__generator.id, __generator);
        _SmAp.put(__schemaToLoad.id, __schemaToLoad);
        _SmAp.put(__upperConstantName.id, __upperConstantName);
        _CmAp.put(__DmgConfig.id, __DmgConfig);
        __DmgConfig.addMust(__generator);
        __DmgConfig.addMust(__genPackage);
        __DmgConfig.addMay(__schemaToLoad);
        __DmgConfig.addMay(__configSuffix);
        __DmgConfig.addMay(MetaDMSAG.__description);
        __DmgConfig.addMay(MetaDMSAG.__generatedFileHeader);
    }

    static DmgDMSAG instance;

    protected DmgDMSAG() {
    }

    public static synchronized DmgDMSAG instance() {
        if (instance == null) instance = new DmgDMSAG();
        return (instance);
    }

    public DmcClassInfo getClassInfo(Integer id) {
        return (_CmAp.get(id));
    }

    public DmcAttributeInfo getAttributeInfo(Integer id) {
        return (_SmAp.get(id));
    }

    public Iterator<DmcAttributeInfo> getAttributeInfo() {
        return (_SmAp.values().iterator());
    }

    public Iterator<DmcClassInfo> getClassInfo() {
        return (_CmAp.values().iterator());
    }

    public Iterator<DmcNameBuilderIF> getNameBuilders() {
        return (_NmAp.values().iterator());
    }

    public Iterator<DmcFilterBuilderIF> getFilterBuilders() {
        return (_FmAp.values().iterator());
    }

    public Iterator<DmcSliceInfo> getSliceInfo() {
        return (_SImAp.values().iterator());
    }

    public String getSchemaName() {
        return (schemaName);
    }

    public int getSchemaBaseID() {
        return (schemaBaseID);
    }

    public int getSchemaIDRange() {
        return (schemaIDRange);
    }

    public int getSchemaMaxID() {
        return (schemaMaxID);
    }
}
