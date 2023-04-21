package org.gdal.ogr;

import org.gdal.osr.SpatialReference;

public interface ogrConstants {

    public static final int wkb25Bit = ogrJNI.wkb25Bit_get();

    public static final int wkbUnknown = ogrJNI.wkbUnknown_get();

    public static final int wkbPoint = ogrJNI.wkbPoint_get();

    public static final int wkbLineString = ogrJNI.wkbLineString_get();

    public static final int wkbPolygon = ogrJNI.wkbPolygon_get();

    public static final int wkbMultiPoint = ogrJNI.wkbMultiPoint_get();

    public static final int wkbMultiLineString = ogrJNI.wkbMultiLineString_get();

    public static final int wkbMultiPolygon = ogrJNI.wkbMultiPolygon_get();

    public static final int wkbGeometryCollection = ogrJNI.wkbGeometryCollection_get();

    public static final int wkbNone = ogrJNI.wkbNone_get();

    public static final int wkbLinearRing = ogrJNI.wkbLinearRing_get();

    public static final int wkbPoint25D = ogrJNI.wkbPoint25D_get();

    public static final int wkbLineString25D = ogrJNI.wkbLineString25D_get();

    public static final int wkbPolygon25D = ogrJNI.wkbPolygon25D_get();

    public static final int wkbMultiPoint25D = ogrJNI.wkbMultiPoint25D_get();

    public static final int wkbMultiLineString25D = ogrJNI.wkbMultiLineString25D_get();

    public static final int wkbMultiPolygon25D = ogrJNI.wkbMultiPolygon25D_get();

    public static final int wkbGeometryCollection25D = ogrJNI.wkbGeometryCollection25D_get();

    public static final int OFTInteger = ogrJNI.OFTInteger_get();

    public static final int OFTIntegerList = ogrJNI.OFTIntegerList_get();

    public static final int OFTReal = ogrJNI.OFTReal_get();

    public static final int OFTRealList = ogrJNI.OFTRealList_get();

    public static final int OFTString = ogrJNI.OFTString_get();

    public static final int OFTStringList = ogrJNI.OFTStringList_get();

    public static final int OFTWideString = ogrJNI.OFTWideString_get();

    public static final int OFTWideStringList = ogrJNI.OFTWideStringList_get();

    public static final int OFTBinary = ogrJNI.OFTBinary_get();

    public static final int OFTDate = ogrJNI.OFTDate_get();

    public static final int OFTTime = ogrJNI.OFTTime_get();

    public static final int OFTDateTime = ogrJNI.OFTDateTime_get();

    public static final int OJUndefined = ogrJNI.OJUndefined_get();

    public static final int OJLeft = ogrJNI.OJLeft_get();

    public static final int OJRight = ogrJNI.OJRight_get();

    public static final int wkbXDR = ogrJNI.wkbXDR_get();

    public static final int wkbNDR = ogrJNI.wkbNDR_get();

    public static final String OLCRandomRead = ogrJNI.OLCRandomRead_get();

    public static final String OLCSequentialWrite = ogrJNI.OLCSequentialWrite_get();

    public static final String OLCRandomWrite = ogrJNI.OLCRandomWrite_get();

    public static final String OLCFastSpatialFilter = ogrJNI.OLCFastSpatialFilter_get();

    public static final String OLCFastFeatureCount = ogrJNI.OLCFastFeatureCount_get();

    public static final String OLCFastGetExtent = ogrJNI.OLCFastGetExtent_get();

    public static final String OLCCreateField = ogrJNI.OLCCreateField_get();

    public static final String OLCTransactions = ogrJNI.OLCTransactions_get();

    public static final String OLCDeleteFeature = ogrJNI.OLCDeleteFeature_get();

    public static final String OLCFastSetNextByIndex = ogrJNI.OLCFastSetNextByIndex_get();

    public static final String ODsCCreateLayer = ogrJNI.ODsCCreateLayer_get();

    public static final String ODsCDeleteLayer = ogrJNI.ODsCDeleteLayer_get();

    public static final String ODrCCreateDataSource = ogrJNI.ODrCCreateDataSource_get();

    public static final String ODrCDeleteDataSource = ogrJNI.ODrCDeleteDataSource_get();
}
