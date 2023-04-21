package org.strophe.sph;

import java.util.Vector;

/**
 * Definition - ����������� ����, ������� ��� �������.
 */
public final class SphDefinition implements SphCodeScope {

    static final int CLASS_TYPE = 0;

    static final int CLASS_OBJECT = 1;

    static final int CLASS_PACKAGE = 2;

    static final int CLASS_FUNCTION = 3;

    static final int CLASS_FUNCTIONLIST = 4;

    static final int STATUS_INITIAL = 0;

    static final int STATUS_VALIDATING = 1;

    static final int STATUS_INVALID = 2;

    static final int STATUS_VALID = 3;

    static final int STATUS_LINKING = 4;

    static final int STATUS_DUMMY = 5;

    static final int STATUS_READY = 6;

    String sourceFile;

    int sourceLine;

    int definitionClass;

    int status;

    boolean codeValidated;

    String name;

    SphTypeCode typeCode;

    SphObjectCode code;

    SphDefinition[] tips;

    SphDefinition[] params;

    Vector useNames;

    SphDefinition[] usePackages;

    boolean isList;

    SphCodeScope codeScope;

    int index;

    int level;

    int idCount;

    SphType baseType;

    SphDefinition usePackageDefinition;

    SphDispatch usePackageDispatch;

    boolean usePackage;

    public SphDefinition(String sourceFile, int sourceLine) {
        this.sourceFile = sourceFile;
        this.sourceLine = sourceLine;
        tips = new SphDefinition[0];
        status = STATUS_INITIAL;
        idCount = 0;
        codeValidated = false;
        usePackage = false;
    }

    public void defineType(String name, Vector useNames, Vector paramNames, SphTypeCode typeCode) {
        definitionClass = CLASS_TYPE;
        this.name = name;
        this.useNames = useNames;
        this.typeCode = typeCode;
        if (paramNames != null) {
            tips = new SphDefinition[paramNames.size()];
            for (int i = 0; i < tips.length; i++) {
                String n = (String) paramNames.elementAt(i);
                tips[i] = new SphDefinition(sourceFile, sourceLine);
                tips[i].defineType(n, null, null, null);
                tips[i].index = i;
            }
        }
    }

    public void defineObject(String name, Vector useNames, SphTypeCode typeCode, SphObjectCode code) {
        definitionClass = CLASS_OBJECT;
        this.name = name;
        this.useNames = useNames;
        this.typeCode = typeCode;
        this.code = code;
    }

    public void definePackage(String name, Vector useNames, SphTypeCode typeCode, SphObjectCode code) {
        definitionClass = CLASS_PACKAGE;
        this.name = name;
        this.useNames = useNames;
        this.typeCode = typeCode;
        this.code = code;
    }

    public void defineFunction(String name, Vector useNames, Vector tipNames, Vector params, SphTypeCode typeCode, SphObjectCode code) {
        definitionClass = CLASS_FUNCTION;
        this.name = name;
        this.useNames = useNames;
        this.params = new SphDefinition[params.size()];
        if (tipNames != null) {
            tips = new SphDefinition[tipNames.size()];
            for (int i = 0; i < tips.length; i++) {
                String n = (String) tipNames.elementAt(i);
                tips[i] = new SphDefinition(sourceFile, sourceLine);
                tips[i].defineType(n, null, null, null);
                tips[i].index = i;
            }
        }
        for (int i = 0; i < this.params.length; i++) {
            SphDefinition d = (SphDefinition) params.elementAt(i);
            this.params[i] = d;
            this.params[i].index = i;
        }
        this.typeCode = typeCode;
        this.code = code;
        this.isList = false;
    }

    public void setList() {
        this.isList = true;
        this.definitionClass = CLASS_FUNCTIONLIST;
    }

    public SphDefinition cloneDefinition() {
        SphDefinition d = new SphDefinition(sourceFile, sourceLine);
        d.definitionClass = this.definitionClass;
        d.status = this.status;
        d.name = this.name;
        d.typeCode = this.typeCode;
        d.code = null;
        d.tips = this.tips;
        d.params = null;
        d.useNames = this.useNames;
        d.usePackages = this.usePackages;
        d.codeScope = this.codeScope;
        d.index = this.index;
        d.level = this.level;
        d.baseType = null;
        d.usePackageDefinition = this.usePackageDefinition;
        d.usePackageDispatch = this.usePackageDispatch;
        d.usePackage = this.usePackage;
        return d;
    }

    public boolean isType() {
        return definitionClass == CLASS_TYPE || definitionClass == CLASS_PACKAGE;
    }

    public boolean isObject() {
        return definitionClass == CLASS_OBJECT || definitionClass == CLASS_PACKAGE;
    }

    public boolean isFunction() {
        return definitionClass == CLASS_FUNCTION || definitionClass == CLASS_FUNCTIONLIST;
    }

    public boolean isPackage() {
        return definitionClass == CLASS_PACKAGE;
    }

    SphTypeCode getTypeCode() {
        return typeCode;
    }

    SphCode getCode() {
        return code;
    }

    void setCodeScope(SphCodeScope parentCodeScope) {
        this.codeScope = parentCodeScope;
        this.level = parentCodeScope.sphCodeScope_getLevel();
        if (typeCode != null) typeCode.sphCode_setCodeScope(this);
        if (code != null) code.sphCode_setCodeScope(this);
        if (params != null) {
            for (int i = 0; i < this.params.length; i++) {
                params[i].setCodeScope(this);
            }
        }
    }

    public String getGlobalName() {
        if (isType() && typeCode == null) return "TIP(" + name + ")"; else return sphCodeScope_getGlobalName();
    }

    public int sphCodeScope_getNewId() {
        return idCount++;
    }

    public int sphCodeScope_getLevel() {
        return level + 1;
    }

    public String sphCodeScope_getGlobalName() {
        if (name == null) name = String.valueOf(codeScope.sphCodeScope_getNewId());
        if (codeScope != null) return codeScope.sphCodeScope_getGlobalName() + "." + this.name;
        return this.name;
    }

    public SphDefinition sphCodeScope_getDefinition(int definitionClass, String name, SphType[] tips, SphDefinition[] params) {
        if (this.definitionClass == CLASS_TYPE || this.definitionClass == CLASS_FUNCTION || this.definitionClass == CLASS_FUNCTIONLIST) {
            for (int i = 0; i < this.tips.length; i++) {
                SphDefinition d = this.tips[i];
                if (d.fitsCall(definitionClass, name, tips, params)) return d;
            }
        }
        if (this.definitionClass == CLASS_FUNCTION || this.definitionClass == CLASS_FUNCTIONLIST) {
            for (int i = 0; i < this.params.length; i++) {
                SphDefinition d = this.params[i];
                if (d.fitsCall(definitionClass, name, tips, params)) return d;
            }
        }
        for (int i = 0; i < usePackages.length; i++) {
            SphDefinition pd = usePackages[i];
            if (pd == this) continue;
            SphDefinition d = pd.baseType.sphType_getClass().sphClass_findMemberDefinition(definitionClass, name, tips, params);
            if (d != null) {
                if (d.usePackageDefinition == null) d.usePackageDefinition = pd;
                return d;
            }
        }
        if (codeScope != null) return codeScope.sphCodeScope_getDefinition(definitionClass, name, tips, params);
        return null;
    }

    protected final void message(String sourceFile, int sourceLine, String messageType, String messageText) {
        SphMashine.getMessageListener().sphMessageListener_message(new SphSourceMessage(messageType, sourceFile, sourceLine, 0, messageText));
    }

    protected void message(String messageType, String messageText) {
        message(sourceFile, sourceLine, messageType, messageText);
    }

    protected void message(SphScriptException e) {
        message(e.sourceFile, e.sourceLine, "Error", e.messageText);
    }

    private void validateTypeCode() {
        if (typeCode != null) try {
            typeCode.sphCode_validate();
        } catch (SphScriptException e) {
            message(e);
            status = STATUS_INVALID;
            baseType = typeCode;
        }
    }

    private void validateCode() {
        if (codeValidated) return;
        if (code != null) try {
            code.sphCode_validate();
        } catch (SphScriptException e) {
            message(e);
            status = STATUS_DUMMY;
            code = new SphCodeNull(sourceFile, sourceLine, null);
            baseType = new SphTypeNull();
        }
        codeValidated = true;
    }

    private void getUsePackages() {
        if (useNames != null && !(sphCodeScope_getGlobalName().equals("standard.lang"))) {
            usePackages = new SphDefinition[useNames.size()];
            for (int i = 0; i < useNames.size(); i++) {
                Vector names = (Vector) useNames.elementAt(i);
                SphDefinition pd = null;
                for (int j = 0; j < names.size(); j++) {
                    String name = (String) names.elementAt(j);
                    SphDefinition parent = pd;
                    if (j == 0) pd = codeScope.sphCodeScope_getDefinition(CLASS_OBJECT, name, null, null); else pd = parent.baseType.sphType_getClass().sphClass_findMemberDefinition(CLASS_OBJECT, name, null, null);
                    if (pd == null) {
                        message("Error", "Use package not found: " + name);
                        usePackages = null;
                        return;
                    }
                    if (!pd.isPackage()) {
                        message("Error", "Use not a package: " + name);
                        usePackages = null;
                        return;
                    }
                    pd.validate();
                    pd.usePackage = true;
                    if (pd.usePackageDefinition == null) pd.usePackageDefinition = parent;
                }
                usePackages[i] = pd;
            }
        } else usePackages = new SphDefinition[0];
        return;
    }

    void validate() {
        if (status == STATUS_VALIDATING) {
            message("Error", "Recursive validation: " + name);
            baseType = new SphTypeNull();
            status = STATUS_INVALID;
            return;
        }
        if (status != STATUS_INITIAL) return; else status = STATUS_VALIDATING;
        getUsePackages();
        if (usePackages == null) {
            status = STATUS_INVALID;
            baseType = new SphTypeNull();
            return;
        }
        if (definitionClass == CLASS_TYPE) {
            if (typeCode != null) {
                for (int i = 1; i < tips.length; i++) for (int j = 0; j < i; j++) {
                    String ni = tips[i].name;
                    String nj = tips[j].name;
                    if (ni.equals(nj)) {
                        message("Error", "Repeated tip name: " + ni);
                        baseType = new SphTypeNull();
                        status = STATUS_INVALID;
                        return;
                    }
                }
                validateTypeCode();
                if (status != STATUS_INVALID) baseType = typeCode;
            } else {
                baseType = null;
            }
        } else if (definitionClass == CLASS_OBJECT || definitionClass == CLASS_PACKAGE) {
            if (typeCode != null) {
                validateTypeCode();
                if (status != STATUS_INVALID) baseType = typeCode;
            } else if (code != null && typeCode == null) {
                validateCode();
                if (status != STATUS_DUMMY) baseType = code.sphObjectCode_getType(); else {
                    baseType = new SphTypeNull();
                    status = STATUS_INVALID;
                }
            } else {
                message("Error", "Unknown type for object: " + name);
                status = STATUS_INVALID;
                baseType = new SphTypeNull();
                return;
            }
        } else if (definitionClass == CLASS_FUNCTION || definitionClass == CLASS_FUNCTIONLIST) {
            for (int i = 1; i < tips.length; i++) for (int j = 0; j < i; j++) {
                String ni = tips[i].name;
                String nj = tips[j].name;
                if (ni.equals(nj)) {
                    message("Error", "Repeated tip name: " + ni);
                    status = STATUS_INVALID;
                    baseType = new SphTypeNull();
                    return;
                }
            }
            for (int i = 1; i < params.length; i++) for (int j = 0; j < i; j++) {
                String ni = params[i].name;
                String nj = params[j].name;
                if (ni.equals(nj)) {
                    message("Error", "Repeated parameter name: " + ni);
                    status = STATUS_INVALID;
                    baseType = new SphTypeNull();
                    return;
                }
            }
            for (int i = 0; i < params.length; i++) {
                params[i].validate();
                if (params[i].status != STATUS_VALID && status == STATUS_VALIDATING) status = STATUS_DUMMY;
            }
            if (typeCode != null) {
                validateTypeCode();
                if (status != STATUS_INVALID) baseType = typeCode;
            } else if (code != null && typeCode == null) {
                validateCode();
                if (status != STATUS_DUMMY) baseType = code.sphObjectCode_getType(); else {
                    status = STATUS_INVALID;
                    baseType = new SphTypeNull();
                }
            } else {
                message("Error", "Unknown type for function: " + name);
                status = STATUS_INVALID;
                baseType = new SphTypeNull();
                return;
            }
        }
        if (status == STATUS_VALIDATING) status = STATUS_VALID;
    }

    public final SphType getType(SphType[] tipTypes) {
        if (baseType == null) {
            return null;
        }
        if (tipTypes != null) return baseType.sphType_call(tips, tipTypes); else return baseType;
    }

    public boolean isPrimaryType() {
        if (baseType != null) return baseType.sphType_isPrimary();
        return true;
    }

    public boolean fitsCall(int definitionClass, String name, SphType[] tipTypes, SphDefinition[] params) {
        if (this.name == null || name == null) return false;
        if (definitionClass != this.definitionClass) {
            if (definitionClass == CLASS_FUNCTION || definitionClass == CLASS_FUNCTIONLIST) return false;
            if (this.definitionClass != CLASS_PACKAGE) return false;
        }
        if (!this.name.equals(name)) return false;
        {
            if (this.status == STATUS_INITIAL) validate();
            if (this.status == STATUS_INVALID) return false;
        }
        if (definitionClass == CLASS_FUNCTION) {
            if (params == null) return false;
            if (this.params == null) return false;
            if (!isList) {
                if (params.length != this.params.length) return false;
                SphType[] tipTypes2 = tipTypes;
                if (tipTypes2 == null) tipTypes2 = getTipsForCall(params);
                if (tipTypes2 == null) return false;
                if (tipTypes2.length != this.tips.length) return false;
                for (int i = 0; i < this.params.length; i++) {
                    SphDefinition d1 = this.params[i];
                    SphDefinition d2 = params[i];
                    SphType t1 = d1.baseType.sphType_call(this.tips, tipTypes2);
                    SphType t2 = d2.baseType;
                    if (!typesEqual(t1, t2)) return false;
                }
            } else {
                SphType[] tipTypes2 = tipTypes;
                if (tipTypes2 == null) tipTypes2 = getTipsForCall(params);
                if (tipTypes2 == null) return false;
                if (tipTypes2.length != this.tips.length) return false;
                for (int i = 0; i < 1; i++) {
                    SphDefinition d1 = this.params[0];
                    SphDefinition d2 = params[i];
                    SphType t1 = d1.baseType.sphType_call(this.tips, tipTypes2);
                    SphType t2 = d2.baseType;
                    if (!typesEqual(t1, t2)) return false;
                }
            }
        }
        return true;
    }

    public SphType[] getTipsForCall(SphDefinition[] avctParams) {
        Vector lvctTipDefinitions = new Vector();
        Vector lvctTipTypes = new Vector();
        for (int i = 0; i < this.params.length; i++) {
            SphDefinition d = this.params[i];
            SphDefinition d2 = avctParams[i];
            d.getTipsForInstance(d2, lvctTipDefinitions, lvctTipTypes);
        }
        SphType[] larrNewTypes = new SphType[this.tips.length];
        for (int i = 0; i < this.tips.length; i++) {
            for (int j = 0; j < lvctTipDefinitions.size(); j++) {
                SphDefinition d = (SphDefinition) lvctTipDefinitions.elementAt(j);
                if (d == this.tips[i]) {
                    larrNewTypes[i] = (SphType) lvctTipTypes.elementAt(j);
                    break;
                }
            }
            if (larrNewTypes[i] == null) return null;
        }
        return larrNewTypes;
    }

    private void getTipsForInstance(SphDefinition adefInstance, Vector avctTipDefinitions, Vector avctTipTypes) {
        if (adefInstance.definitionClass != this.definitionClass) return;
        if (this.isFunction()) {
            if (adefInstance.tips.length != this.tips.length) return;
            if (adefInstance.params.length != this.params.length) return;
            for (int i = 0; i < this.params.length; i++) {
                SphDefinition d = this.params[i];
                SphDefinition d2 = adefInstance.params[i];
                d.getTipsForInstance(d2, avctTipDefinitions, avctTipTypes);
            }
        }
        this.baseType.sphType_getTipsForInstance(adefInstance.baseType, avctTipDefinitions, avctTipTypes);
    }

    public void validateCall(SphType[] tipTypes, SphDefinition[] params) throws SphScriptException {
        int tipsNumber = 0;
        if (tipTypes != null) tipsNumber = tipTypes.length;
        if (tipsNumber != this.tips.length) throw new SphScriptException("Wrong tips number for \"" + name + "\",	found: " + tipsNumber + ",	expected: " + this.tips.length, -1, -1);
        if (isList) {
            SphType[] tipTypes2 = tipTypes;
            if (tipTypes2 == null) tipTypes2 = new SphType[0];
            SphDefinition d1 = this.params[0];
            SphType t1 = d1.baseType.sphType_call(this.tips, tipTypes2);
            for (int i = 1; i < params.length; i++) {
                SphDefinition d2 = params[i];
                SphType t2 = d2.baseType;
                if (!typesEqual(t1, t2)) {
                    String s1 = typeToString(t1);
                    String s2 = typeToString(t2);
                    throw new SphScriptException("Wrong parameter type for \"" + name + "\",	found: " + s2 + ",	expected: " + s1, -1, -1);
                }
            }
        } else {
        }
    }

    public static String typeToString(SphType type) {
        String s = null;
        if (type == null) s = "Empty type"; else s = type.sphType_toString();
        if (s == null) s = "?no string for type";
        return s;
    }

    public static boolean typesEqual(SphType type0, SphType type1) {
        String s0 = typeToString(type0);
        String s1 = typeToString(type1);
        if (s0.equals(s1)) return true;
        if (s0.equals("NULL")) return true;
        if (s1.equals("NULL")) return true;
        return false;
    }

    void link() {
        validate();
        if (status != STATUS_VALID) return; else status = STATUS_LINKING;
        if (definitionClass == CLASS_TYPE) {
        } else if (definitionClass == CLASS_OBJECT) {
            validateCode();
            if (status == STATUS_DUMMY) return; else if (code != null && typeCode != null) {
                if (!typesEqual(baseType, code.sphObjectCode_getType())) {
                    String s1 = typeToString(baseType);
                    String s2 = typeToString(code.sphObjectCode_getType());
                    message("Error", "Wrong type for: " + name + ",	found: " + s2 + ",	expected: " + s1);
                    status = STATUS_DUMMY;
                    return;
                }
            }
        } else if (definitionClass == CLASS_FUNCTION || definitionClass == CLASS_FUNCTIONLIST) {
            validateCode();
            if (status == STATUS_DUMMY) return; else if (code != null && typeCode != null) {
                if (!typesEqual(baseType, code.sphObjectCode_getType())) {
                    String s1 = typeToString(baseType);
                    String s2 = typeToString(code.sphObjectCode_getType());
                    message("Error", "Wrong type for: " + name + ",	found: " + s2 + ",	expected: " + s1);
                    status = STATUS_DUMMY;
                    return;
                }
            }
        }
        if (code != null) try {
            code.sphCode_link();
        } catch (SphScriptException e) {
            message(e);
            status = STATUS_DUMMY;
        }
        if (status == STATUS_LINKING) status = STATUS_READY;
    }
}
