package de.bea.services.vidya.client.datasource.types;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.util.exception.LocalizableExceptionAdapter;

public class ExplicantoWebService_deleteCSDEModel_ResponseStruct_SOAPBuilder implements SOAPInstanceBuilder {

    private de.bea.services.vidya.client.datasource.types.ExplicantoWebService_deleteCSDEModel_ResponseStruct _instance;

    private de.bea.services.vidya.client.datasource.types.WSResponse result;

    private static final int myRESULT_INDEX = 0;

    public ExplicantoWebService_deleteCSDEModel_ResponseStruct_SOAPBuilder() {
    }

    public void setResult(de.bea.services.vidya.client.datasource.types.WSResponse result) {
        this.result = result;
    }

    public int memberGateType(int memberIndex) {
        switch(memberIndex) {
            case myRESULT_INDEX:
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
                case myRESULT_INDEX:
                    _instance.setResult((de.bea.services.vidya.client.datasource.types.WSResponse) memberValue);
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
        _instance = (de.bea.services.vidya.client.datasource.types.ExplicantoWebService_deleteCSDEModel_ResponseStruct) instance;
    }

    public Object getInstance() {
        return _instance;
    }
}
