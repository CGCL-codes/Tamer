package com.serena.xmlbridge.adapter.qc9.gen;

import com4j.*;

/**
 * Services for managing test sets.
 */
@IID("{6199FE11-C44D-4712-99DF-4F5EF3F80A29}")
public interface ITestSetFactory extends com.serena.xmlbridge.adapter.qc9.gen.IBaseFactory {

    /**
     * Creates graph showing the number of requirements reported according to the defect tracking information specified.
     */
    @VTID(16)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject buildSummaryGraph(int testSetID, java.lang.String xAxisField, java.lang.String groupByField, java.lang.String sumOfField, int maxCols, @MarshalAs(NativeType.VARIANT) java.lang.Object filter, boolean forceRefresh, boolean showFullPath);

    /**
     * Creates graph showing number of tests in all test sets executed over a period of time.
     */
    @VTID(17)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject buildProgressGraph(int testSetID, java.lang.String groupByField, java.lang.String sumOfField, int majorSkip, int minorSkip, int maxCols, @MarshalAs(NativeType.VARIANT) java.lang.Object filter, @MarshalAs(NativeType.VARIANT) java.lang.Object frDate, boolean forceRefresh, boolean showFullPath);

    /**
     * Creates graph showing the number of status changes over a time period.
     */
    @VTID(18)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject buildTrendGraph(int testSetID, java.lang.String groupByField, java.lang.String sumOfField, int maxCols, @MarshalAs(NativeType.VARIANT) java.lang.Object filter, @MarshalAs(NativeType.VARIANT) java.lang.Object frDate, boolean forceRefresh, boolean showFullPath);

    /**
     * Creates Performance Graph.
     */
    @VTID(19)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject buildPerfGraph(int testSetID, java.lang.String groupByField, java.lang.String sumOfField, int maxCols, @MarshalAs(NativeType.VARIANT) java.lang.Object filter, @MarshalAs(NativeType.VARIANT) java.lang.Object frDate, boolean forceRefresh, boolean showFullPath);

    /**
     * Creates progress graph for specified test set.
     */
    @VTID(20)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject buildProgressGraphEx(int testSetID, java.lang.String groupByField, java.lang.String sumOfField, boolean byHistory, int majorSkip, int minorSkip, int maxCols, @MarshalAs(NativeType.VARIANT) java.lang.Object filter, @MarshalAs(NativeType.VARIANT) java.lang.Object frDate, boolean forceRefresh, boolean showFullPath);
}
