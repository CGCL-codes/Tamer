package de.bea.services.vidya.client.datasource.types;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.util.exception.LocalizableExceptionAdapter;

public class ExplicantoWebService_unitPreview_RequestStruct_SOAPBuilder implements SOAPInstanceBuilder {

    private de.bea.services.vidya.client.datasource.types.ExplicantoWebService_unitPreview_RequestStruct _instance;

    private de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1;

    private long long_2;

    private long long_3;

    private long long_4;

    private long long_5;

    private java.lang.String string_6;

    private static final int myWSAUTHENTICATION_1_INDEX = 0;

    private static final int myLONG_2_INDEX = 1;

    private static final int myLONG_3_INDEX = 2;

    private static final int myLONG_4_INDEX = 3;

    private static final int myLONG_5_INDEX = 4;

    private static final int mySTRING_6_INDEX = 5;

    public ExplicantoWebService_unitPreview_RequestStruct_SOAPBuilder() {
    }

    public void setWSAuthentication_1(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1) {
        this.WSAuthentication_1 = WSAuthentication_1;
    }

    public void setLong_2(long long_2) {
        this.long_2 = long_2;
    }

    public void setLong_3(long long_3) {
        this.long_3 = long_3;
    }

    public void setLong_4(long long_4) {
        this.long_4 = long_4;
    }

    public void setLong_5(long long_5) {
        this.long_5 = long_5;
    }

    public void setString_6(java.lang.String string_6) {
        this.string_6 = string_6;
    }

    public int memberGateType(int memberIndex) {
        switch(memberIndex) {
            case myWSAUTHENTICATION_1_INDEX:
                return GATES_INITIALIZATION | REQUIRES_CREATION;
            case mySTRING_6_INDEX:
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
                case mySTRING_6_INDEX:
                    _instance.setString_6((java.lang.String) memberValue);
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
        _instance = (de.bea.services.vidya.client.datasource.types.ExplicantoWebService_unitPreview_RequestStruct) instance;
    }

    public Object getInstance() {
        return _instance;
    }
}
