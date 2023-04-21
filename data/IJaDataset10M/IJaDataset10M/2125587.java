package de.bea.services.vidya.client.datasource.types;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.util.exception.LocalizableExceptionAdapter;

public class ExplicantoWebService_removeTask_RequestStruct_SOAPBuilder implements SOAPInstanceBuilder {

    private de.bea.services.vidya.client.datasource.types.ExplicantoWebService_removeTask_RequestStruct _instance;

    private de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1;

    private de.bea.services.vidya.client.datasource.types.WSTask WSTask_2;

    private static final int myWSAUTHENTICATION_1_INDEX = 0;

    private static final int myWSTASK_2_INDEX = 1;

    public ExplicantoWebService_removeTask_RequestStruct_SOAPBuilder() {
    }

    public void setWSAuthentication_1(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1) {
        this.WSAuthentication_1 = WSAuthentication_1;
    }

    public void setWSTask_2(de.bea.services.vidya.client.datasource.types.WSTask WSTask_2) {
        this.WSTask_2 = WSTask_2;
    }

    public int memberGateType(int memberIndex) {
        switch(memberIndex) {
            case myWSAUTHENTICATION_1_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            case myWSTASK_2_INDEX:
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
                case myWSAUTHENTICATION_1_INDEX:
                    _instance.setWSAuthentication_1((de.bea.services.vidya.client.datasource.types.WSAuthentication) memberValue);
                    break;
                case myWSTASK_2_INDEX:
                    _instance.setWSTask_2((de.bea.services.vidya.client.datasource.types.WSTask) memberValue);
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
        _instance = (de.bea.services.vidya.client.datasource.types.ExplicantoWebService_removeTask_RequestStruct) instance;
    }

    public Object getInstance() {
        return _instance;
    }
}
