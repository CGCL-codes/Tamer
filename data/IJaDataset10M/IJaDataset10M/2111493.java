package pack;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.util.exception.LocalizableExceptionAdapter;

public class IExhibitSrv_GetShortDemandInfo_RequestStruct_SOAPBuilder implements SOAPInstanceBuilder {

    private pack.IExhibitSrv_GetShortDemandInfo_RequestStruct _instance;

    private int demandID;

    private pack.TShortOfferInfo shortInfo;

    private static final int myDEMANDID_INDEX = 0;

    private static final int mySHORTINFO_INDEX = 1;

    public IExhibitSrv_GetShortDemandInfo_RequestStruct_SOAPBuilder() {
    }

    public void setDemandID(int demandID) {
        this.demandID = demandID;
    }

    public void setShortInfo(pack.TShortOfferInfo shortInfo) {
        this.shortInfo = shortInfo;
    }

    public int memberGateType(int memberIndex) {
        switch(memberIndex) {
            case mySHORTINFO_INDEX:
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
                case mySHORTINFO_INDEX:
                    _instance.setShortInfo((pack.TShortOfferInfo) memberValue);
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
        _instance = (pack.IExhibitSrv_GetShortDemandInfo_RequestStruct) instance;
    }

    public java.lang.Object getInstance() {
        return _instance;
    }
}
