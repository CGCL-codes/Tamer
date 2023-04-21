package com.serena.xmlbridge.adapter.qc9.gen;

import com4j.*;

/**
 * Services for managing tests.
 */
@IID("{5BB6D957-669D-47B2-8324-981492EDC42E}")
public interface ITestFactory extends com.serena.xmlbridge.adapter.qc9.gen.IBaseFactoryEx {

    /**
     * Creates summary graph showing number of tests executed in a test set according to test and test run.
     */
    @VTID(17)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject buildSummaryGraph(java.lang.String xAxisField, java.lang.String groupByField, java.lang.String sumOfField, int maxCols, @MarshalAs(NativeType.VARIANT) java.lang.Object filter, boolean forceRefresh, boolean showFullPath);

    /**
     * Creates graph showing number of tests in a test set executed over a period of time.
     */
    @VTID(18)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject buildProgressGraph(java.lang.String groupByField, java.lang.String sumOfField, int majorSkip, int minorSkip, int maxCols, @MarshalAs(NativeType.VARIANT) java.lang.Object filter, @MarshalAs(NativeType.VARIANT) java.lang.Object frDate, boolean forceRefresh, boolean showFullPath);

    /**
     * An ExtendedStorage object connected by default to the test repository folder on the server side. Enables file system operations with a test repository.
     */
    @VTID(19)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject repositoryStorage();

    /**
     * Creates graph that shows the number of defect status changes over time.
     */
    @VTID(20)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject buildTrendGraph(java.lang.String groupByField, java.lang.String sumOfField, int maxCols, @MarshalAs(NativeType.VARIANT) java.lang.Object filter, @MarshalAs(NativeType.VARIANT) java.lang.Object frDate, boolean forceRefresh, boolean showFullPath);

    /**
     * Creates Performance Graph.
     */
    @VTID(21)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject buildPerfGraph(java.lang.String groupByField, java.lang.String sumOfField, int maxCols, @MarshalAs(NativeType.VARIANT) java.lang.Object filter, @MarshalAs(NativeType.VARIANT) java.lang.Object frDate, boolean forceRefresh, boolean showFullPath);

    /**
     * Ignore hidden user group data when retrieving tests from the server.
     */
    @VTID(22)
    boolean ignoreDataHiding();

    /**
     * Ignore hidden user group data when retrieving tests from the server.
     */
    @VTID(23)
    void ignoreDataHiding(boolean pVal);

    /**
     * Creates graph showing number of tests in a test set executed over a period of time.
     */
    @VTID(24)
    @ReturnValue(type = NativeType.Dispatch)
    com4j.Com4jObject buildProgressGraphEx(java.lang.String groupByField, java.lang.String sumOfField, boolean byHistory, int majorSkip, int minorSkip, int maxCols, @MarshalAs(NativeType.VARIANT) java.lang.Object filter, @MarshalAs(NativeType.VARIANT) java.lang.Object frDate, boolean forceRefresh, boolean showFullPath);
}
