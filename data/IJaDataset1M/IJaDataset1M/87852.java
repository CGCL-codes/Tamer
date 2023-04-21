package pack;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.util.exception.LocalizableExceptionAdapter;

public class IExhibitSrv_GetOwnInfoStat_ResponseStruct_SOAPBuilder implements SOAPInstanceBuilder {

    private pack.IExhibitSrv_GetOwnInfoStat_ResponseStruct _instance;

    private int[] ownInfoStat;

    private boolean _return;

    private static final int myOWNINFOSTAT_INDEX = 0;

    private static final int my_RETURN_INDEX = 1;

    public IExhibitSrv_GetOwnInfoStat_ResponseStruct_SOAPBuilder() {
    }

    public void setOwnInfoStat(int[] ownInfoStat) {
        this.ownInfoStat = ownInfoStat;
    }

    public void set_return(boolean _return) {
        this._return = _return;
    }

    public int memberGateType(int memberIndex) {
        switch(memberIndex) {
            case myOWNINFOSTAT_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void construct() {
    }

    public void setMember(int index, java.lang.Object memberValue) {
        try {
            switch(index) {
                case myOWNINFOSTAT_INDEX:
                    _instance.setOwnInfoStat((int[]) memberValue);
                    break;
                default:
                    throw new java.lang.IllegalArgumentException();
            }
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new DeserializationException(new LocalizableExceptionAdapter(e));
        }
    }

    public void initialize() {
    }

    public void setInstance(java.lang.Object instance) {
        _instance = (pack.IExhibitSrv_GetOwnInfoStat_ResponseStruct) instance;
    }

    public java.lang.Object getInstance() {
        return _instance;
    }
}
