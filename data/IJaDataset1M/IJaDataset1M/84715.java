package de.bea.services.vidya.client.datasource.types;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.util.exception.LocalizableExceptionAdapter;

public class WSLanguage_SOAPBuilder implements SOAPInstanceBuilder {

    private de.bea.services.vidya.client.datasource.types.WSLanguage _instance;

    private java.lang.String langName;

    private java.lang.String langSuffix;

    private java.lang.String localsDef;

    private int position;

    private static final int myLANGNAME_INDEX = 0;

    private static final int myLANGSUFFIX_INDEX = 1;

    private static final int myLOCALSDEF_INDEX = 2;

    private static final int myPOSITION_INDEX = 3;

    public WSLanguage_SOAPBuilder() {
    }

    public void setLangName(java.lang.String langName) {
        this.langName = langName;
    }

    public void setLangSuffix(java.lang.String langSuffix) {
        this.langSuffix = langSuffix;
    }

    public void setLocalsDef(java.lang.String localsDef) {
        this.localsDef = localsDef;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int memberGateType(int memberIndex) {
        switch(memberIndex) {
            case myLANGNAME_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            case myLANGSUFFIX_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            case myLOCALSDEF_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void construct() {
    }

    public void setMember(int index, Object memberValue) {
        try {
            switch(index) {
                case myLANGNAME_INDEX:
                    _instance.setLangName((java.lang.String) memberValue);
                    break;
                case myLANGSUFFIX_INDEX:
                    _instance.setLangSuffix((java.lang.String) memberValue);
                    break;
                case myLOCALSDEF_INDEX:
                    _instance.setLocalsDef((java.lang.String) memberValue);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DeserializationException(new LocalizableExceptionAdapter(e));
        }
    }

    public void initialize() {
    }

    public void setInstance(Object instance) {
        _instance = (de.bea.services.vidya.client.datasource.types.WSLanguage) instance;
    }

    public Object getInstance() {
        return _instance;
    }
}
