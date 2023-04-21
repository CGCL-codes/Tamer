package com.serena.xmlbridge.adapter.qc9.gen;

import com4j.*;

/**
 * IAnalysis Interface.
 */
@IID("{557AF6E9-FB13-4934-9A8F-6620BA38C547}")
public interface IAnalysis extends Com4jObject {

    /**
     * property Filter : analysis filter.
     */
    @VTID(7)
    java.lang.String filterText();

    /**
     * property Filter : analysis filter.
     */
    @VTID(8)
    void filterText(java.lang.String pVal);

    /**
     * property Type: analysis object type.
     */
    @VTID(9)
    java.lang.String type();

    /**
     * property Type: analysis object type.
     */
    @VTID(10)
    void type(java.lang.String pVal);

    /**
     * Gets distinct values for specified fields.
     */
    @VTID(11)
    @ReturnValue(type = NativeType.VARIANT)
    java.lang.Object getDistinctValues(@MarshalAs(NativeType.VARIANT) java.lang.Object fields);

    /**
     * Gets summary data.
     */
    @VTID(12)
    @ReturnValue(type = NativeType.VARIANT)
    java.lang.Object getSummaryData(@MarshalAs(NativeType.VARIANT) java.lang.Object fields, @MarshalAs(NativeType.VARIANT) java.lang.Object dataFields);

    /**
     * property JoinCondition: analysis join condition.
     */
    @VTID(13)
    java.lang.String joinCondition();

    /**
     * property JoinCondition: analysis join condition.
     */
    @VTID(14)
    void joinCondition(java.lang.String pVal);
}
