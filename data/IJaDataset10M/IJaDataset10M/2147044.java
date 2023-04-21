package de.bea.services.vidya.client.datasource.types;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.util.exception.LocalizableExceptionAdapter;

public class WSMediaElem_SOAPBuilder implements SOAPInstanceBuilder {

    private de.bea.services.vidya.client.datasource.types.WSMediaElem _instance;

    private long uid;

    private int height;

    private java.lang.String longDesc;

    private java.lang.String mediaFileName;

    private java.lang.String origSrcFileName;

    private java.lang.String prevFileName;

    private java.lang.String shortDesc;

    private java.lang.String srcFileName;

    private java.lang.String type;

    private int width;

    private static final int myUID_INDEX = 0;

    private static final int myHEIGHT_INDEX = 1;

    private static final int myLONGDESC_INDEX = 2;

    private static final int myMEDIAFILENAME_INDEX = 3;

    private static final int myORIGSRCFILENAME_INDEX = 4;

    private static final int myPREVFILENAME_INDEX = 5;

    private static final int mySHORTDESC_INDEX = 6;

    private static final int mySRCFILENAME_INDEX = 7;

    private static final int myTYPE_INDEX = 8;

    private static final int myWIDTH_INDEX = 9;

    public WSMediaElem_SOAPBuilder() {
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLongDesc(java.lang.String longDesc) {
        this.longDesc = longDesc;
    }

    public void setMediaFileName(java.lang.String mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    public void setOrigSrcFileName(java.lang.String origSrcFileName) {
        this.origSrcFileName = origSrcFileName;
    }

    public void setPrevFileName(java.lang.String prevFileName) {
        this.prevFileName = prevFileName;
    }

    public void setShortDesc(java.lang.String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setSrcFileName(java.lang.String srcFileName) {
        this.srcFileName = srcFileName;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int memberGateType(int memberIndex) {
        switch(memberIndex) {
            case myLONGDESC_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            case myMEDIAFILENAME_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            case myORIGSRCFILENAME_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            case myPREVFILENAME_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            case mySHORTDESC_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            case mySRCFILENAME_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            case myTYPE_INDEX:
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
                case myLONGDESC_INDEX:
                    _instance.setLongDesc((java.lang.String) memberValue);
                    break;
                case myMEDIAFILENAME_INDEX:
                    _instance.setMediaFileName((java.lang.String) memberValue);
                    break;
                case myORIGSRCFILENAME_INDEX:
                    _instance.setOrigSrcFileName((java.lang.String) memberValue);
                    break;
                case myPREVFILENAME_INDEX:
                    _instance.setPrevFileName((java.lang.String) memberValue);
                    break;
                case mySHORTDESC_INDEX:
                    _instance.setShortDesc((java.lang.String) memberValue);
                    break;
                case mySRCFILENAME_INDEX:
                    _instance.setSrcFileName((java.lang.String) memberValue);
                    break;
                case myTYPE_INDEX:
                    _instance.setType((java.lang.String) memberValue);
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
        _instance = (de.bea.services.vidya.client.datasource.types.WSMediaElem) instance;
    }

    public Object getInstance() {
        return _instance;
    }
}
