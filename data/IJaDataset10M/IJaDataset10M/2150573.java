package org.gdal.ogr;

import org.gdal.osr.SpatialReference;
import org.gdal.osr.CoordinateTransformation;

class ogrJNI {

    public static final native int wkb25Bit_get();

    public static final native int wkbUnknown_get();

    public static final native int wkbPoint_get();

    public static final native int wkbLineString_get();

    public static final native int wkbPolygon_get();

    public static final native int wkbMultiPoint_get();

    public static final native int wkbMultiLineString_get();

    public static final native int wkbMultiPolygon_get();

    public static final native int wkbGeometryCollection_get();

    public static final native int wkbNone_get();

    public static final native int wkbLinearRing_get();

    public static final native int wkbPoint25D_get();

    public static final native int wkbLineString25D_get();

    public static final native int wkbPolygon25D_get();

    public static final native int wkbMultiPoint25D_get();

    public static final native int wkbMultiLineString25D_get();

    public static final native int wkbMultiPolygon25D_get();

    public static final native int wkbGeometryCollection25D_get();

    public static final native int OFTInteger_get();

    public static final native int OFTIntegerList_get();

    public static final native int OFTReal_get();

    public static final native int OFTRealList_get();

    public static final native int OFTString_get();

    public static final native int OFTStringList_get();

    public static final native int OFTWideString_get();

    public static final native int OFTWideStringList_get();

    public static final native int OFTBinary_get();

    public static final native int OFTDate_get();

    public static final native int OFTTime_get();

    public static final native int OFTDateTime_get();

    public static final native int OJUndefined_get();

    public static final native int OJLeft_get();

    public static final native int OJRight_get();

    public static final native int wkbXDR_get();

    public static final native int wkbNDR_get();

    public static final native String OLCRandomRead_get();

    public static final native String OLCSequentialWrite_get();

    public static final native String OLCRandomWrite_get();

    public static final native String OLCFastSpatialFilter_get();

    public static final native String OLCFastFeatureCount_get();

    public static final native String OLCFastGetExtent_get();

    public static final native String OLCCreateField_get();

    public static final native String OLCTransactions_get();

    public static final native String OLCDeleteFeature_get();

    public static final native String OLCFastSetNextByIndex_get();

    public static final native String ODsCCreateLayer_get();

    public static final native String ODsCDeleteLayer_get();

    public static final native String ODrCCreateDataSource_get();

    public static final native String ODrCDeleteDataSource_get();

    private static boolean available = false;

    static {
        try {
            System.loadLibrary("ogrjni");
            available = true;
        } catch (UnsatisfiedLinkError e) {
            available = false;
            System.err.println("Native library load failed.");
            System.err.println(e);
        }
    }

    public static boolean isAvailable() {
        return available;
    }

    public static final native String Driver_name_get(long jarg1);

    public static final native long Driver_CreateDataSource(long jarg1, String jarg2, java.util.Vector jarg3);

    public static final native long Driver_CopyDataSource(long jarg1, long jarg2, String jarg3, java.util.Vector jarg4);

    public static final native long Driver_Open(long jarg1, String jarg2, int jarg3);

    public static final native int Driver_DeleteDataSource(long jarg1, String jarg2);

    public static final native boolean Driver_TestCapability(long jarg1, String jarg2);

    public static final native String Driver_GetName(long jarg1);

    public static final native String DataSource_name_get(long jarg1);

    public static final native void delete_DataSource(long jarg1);

    public static final native int DataSource_GetRefCount(long jarg1);

    public static final native int DataSource_GetSummaryRefCount(long jarg1);

    public static final native int DataSource_GetLayerCount(long jarg1);

    public static final native long DataSource_GetDriver(long jarg1);

    public static final native String DataSource_GetName(long jarg1);

    public static final native int DataSource_DeleteLayer(long jarg1, int jarg2);

    public static final native long DataSource_CreateLayer(long jarg1, String jarg2, long jarg3, int jarg4, java.util.Vector jarg5);

    public static final native long DataSource_CopyLayer(long jarg1, long jarg2, String jarg3, java.util.Vector jarg4);

    public static final native long DataSource_GetLayerByIndex(long jarg1, int jarg2);

    public static final native long DataSource_GetLayerByName(long jarg1, String jarg2);

    public static final native boolean DataSource_TestCapability(long jarg1, String jarg2);

    public static final native long DataSource_ExecuteSQL(long jarg1, String jarg2, long jarg3, String jarg4);

    public static final native void DataSource_ReleaseResultSet(long jarg1, long jarg2);

    public static final native int Layer_GetRefCount(long jarg1);

    public static final native void Layer_SetSpatialFilter(long jarg1, long jarg2);

    public static final native void Layer_SetSpatialFilterRect(long jarg1, double jarg2, double jarg3, double jarg4, double jarg5);

    public static final native long Layer_GetSpatialFilter(long jarg1);

    public static final native int Layer_SetAttributeFilter(long jarg1, String jarg2);

    public static final native void Layer_ResetReading(long jarg1);

    public static final native String Layer_GetName(long jarg1);

    public static final native long Layer_GetFeature(long jarg1, int jarg2);

    public static final native long Layer_GetNextFeature(long jarg1);

    public static final native int Layer_SetNextByIndex(long jarg1, int jarg2);

    public static final native int Layer_SetFeature(long jarg1, long jarg2);

    public static final native int Layer_CreateFeature(long jarg1, long jarg2);

    public static final native int Layer_DeleteFeature(long jarg1, int jarg2);

    public static final native int Layer_SyncToDisk(long jarg1);

    public static final native long Layer_GetLayerDefn(long jarg1);

    public static final native int Layer_GetFeatureCount(long jarg1, int jarg2);

    public static final native int Layer_GetExtent(long jarg1, double[] jarg2, int jarg3);

    public static final native boolean Layer_TestCapability(long jarg1, String jarg2);

    public static final native int Layer_CreateField(long jarg1, long jarg2, int jarg3);

    public static final native int Layer_StartTransaction(long jarg1);

    public static final native int Layer_CommitTransaction(long jarg1);

    public static final native int Layer_RollbackTransaction(long jarg1);

    public static final native long Layer_GetSpatialRef(long jarg1);

    public static final native long Layer_GetFeatureRead(long jarg1);

    public static final native void delete_Feature(long jarg1);

    public static final native long new_Feature(long jarg1);

    public static final native long Feature_GetDefnRef(long jarg1);

    public static final native int Feature_SetGeometry(long jarg1, long jarg2);

    public static final native int Feature_SetGeometryDirectly(long jarg1, long jarg2);

    public static final native long Feature_GetGeometryRef(long jarg1);

    public static final native long Feature_Clone(long jarg1);

    public static final native boolean Feature_Equal(long jarg1, long jarg2);

    public static final native int Feature_GetFieldCount(long jarg1);

    public static final native long Feature_GetFieldDefnRef__SWIG_0(long jarg1, int jarg2);

    public static final native long Feature_GetFieldDefnRef__SWIG_1(long jarg1, String jarg2);

    public static final native String Feature_GetFieldAsString__SWIG_0(long jarg1, int jarg2);

    public static final native String Feature_GetFieldAsString__SWIG_1(long jarg1, String jarg2);

    public static final native int Feature_GetFieldAsInteger__SWIG_0(long jarg1, int jarg2);

    public static final native int Feature_GetFieldAsInteger__SWIG_1(long jarg1, String jarg2);

    public static final native double Feature_GetFieldAsDouble__SWIG_0(long jarg1, int jarg2);

    public static final native double Feature_GetFieldAsDouble__SWIG_1(long jarg1, String jarg2);

    public static final native boolean Feature_IsFieldSet__SWIG_0(long jarg1, int jarg2);

    public static final native boolean Feature_IsFieldSet__SWIG_1(long jarg1, String jarg2);

    public static final native int Feature_GetFieldIndex(long jarg1, String jarg2);

    public static final native int Feature_GetFID(long jarg1);

    public static final native int Feature_SetFID(long jarg1, int jarg2);

    public static final native void Feature_DumpReadable(long jarg1);

    public static final native void Feature_UnsetField__SWIG_0(long jarg1, int jarg2);

    public static final native void Feature_UnsetField__SWIG_1(long jarg1, String jarg2);

    public static final native void Feature_SetField__SWIG_0(long jarg1, int jarg2, String jarg3);

    public static final native void Feature_SetField__SWIG_1(long jarg1, String jarg2, String jarg3);

    public static final native void Feature_SetField__SWIG_2(long jarg1, int jarg2, int jarg3);

    public static final native void Feature_SetField__SWIG_3(long jarg1, String jarg2, int jarg3);

    public static final native void Feature_SetField__SWIG_4(long jarg1, int jarg2, double jarg3);

    public static final native void Feature_SetField__SWIG_5(long jarg1, String jarg2, double jarg3);

    public static final native void Feature_SetField__SWIG_6(long jarg1, int jarg2, int jarg3, int jarg4, int jarg5, int jarg6, int jarg7, int jarg8, int jarg9);

    public static final native void Feature_SetField__SWIG_7(long jarg1, String jarg2, int jarg3, int jarg4, int jarg5, int jarg6, int jarg7, int jarg8, int jarg9);

    public static final native int Feature_SetFrom(long jarg1, long jarg2, int jarg3);

    public static final native String Feature_GetStyleString(long jarg1);

    public static final native void Feature_SetStyleString(long jarg1, String jarg2);

    public static final native int Feature_GetFieldType__SWIG_0(long jarg1, int jarg2);

    public static final native int Feature_GetFieldType__SWIG_1(long jarg1, String jarg2);

    public static final native void delete_FeatureDefn(long jarg1);

    public static final native long new_FeatureDefn(String jarg1);

    public static final native String FeatureDefn_GetName(long jarg1);

    public static final native int FeatureDefn_GetFieldCount(long jarg1);

    public static final native long FeatureDefn_GetFieldDefn(long jarg1, int jarg2);

    public static final native int FeatureDefn_GetFieldIndex(long jarg1, String jarg2);

    public static final native void FeatureDefn_AddFieldDefn(long jarg1, long jarg2);

    public static final native int FeatureDefn_GetGeomType(long jarg1);

    public static final native void FeatureDefn_SetGeomType(long jarg1, int jarg2);

    public static final native int FeatureDefn_GetReferenceCount(long jarg1);

    public static final native void delete_FieldDefn(long jarg1);

    public static final native long new_FieldDefn(String jarg1, int jarg2);

    public static final native String FieldDefn_GetName(long jarg1);

    public static final native String FieldDefn_GetNameRef(long jarg1);

    public static final native void FieldDefn_SetName(long jarg1, String jarg2);

    public static final native int FieldDefn_GetFieldType(long jarg1);

    public static final native void FieldDefn_SetType(long jarg1, int jarg2);

    public static final native int FieldDefn_GetJustify(long jarg1);

    public static final native void FieldDefn_SetJustify(long jarg1, int jarg2);

    public static final native int FieldDefn_GetWidth(long jarg1);

    public static final native void FieldDefn_SetWidth(long jarg1, int jarg2);

    public static final native int FieldDefn_GetPrecision(long jarg1);

    public static final native void FieldDefn_SetPrecision(long jarg1, int jarg2);

    public static final native String FieldDefn_GetFieldTypeName(long jarg1, int jarg2);

    public static final native long CreateGeometryFromWkb(byte[] jarg1, long jarg3);

    public static final native long CreateGeometryFromWkt(String jarg1, long jarg2);

    public static final native long CreateGeometryFromGML(String jarg1);

    public static final native void delete_Geometry(long jarg1);

    public static final native int Geometry_ExportToWkt(long jarg1, String[] jarg2);

    public static final native String Geometry_ExportToGML(long jarg1);

    public static final native void Geometry_AddPoint(long jarg1, double jarg2, double jarg3, double jarg4);

    public static final native int Geometry_AddGeometryDirectly(long jarg1, long jarg2);

    public static final native int Geometry_AddGeometry(long jarg1, long jarg2);

    public static final native long Geometry_Clone(long jarg1);

    public static final native int Geometry_GetGeometryType(long jarg1);

    public static final native String Geometry_GetGeometryName(long jarg1);

    public static final native double Geometry_GetArea(long jarg1);

    public static final native int Geometry_GetPointCount(long jarg1);

    public static final native double Geometry_GetX(long jarg1, int jarg2);

    public static final native double Geometry_GetY(long jarg1, int jarg2);

    public static final native double Geometry_GetZ(long jarg1, int jarg2);

    public static final native int Geometry_GetGeometryCount(long jarg1);

    public static final native void Geometry_SetPoint(long jarg1, int jarg2, double jarg3, double jarg4, double jarg5);

    public static final native long Geometry_GetGeometryRef(long jarg1, int jarg2);

    public static final native long Geometry_GetBoundary(long jarg1);

    public static final native long Geometry_ConvexHull(long jarg1);

    public static final native long Geometry_Buffer(long jarg1, double jarg2, int jarg3);

    public static final native long Geometry_Intersection(long jarg1, long jarg2);

    public static final native long Geometry_Union(long jarg1, long jarg2);

    public static final native long Geometry_Difference(long jarg1, long jarg2);

    public static final native long Geometry_SymmetricDifference(long jarg1, long jarg2);

    public static final native double Geometry_Distance(long jarg1, long jarg2);

    public static final native void Geometry_Empty(long jarg1);

    public static final native boolean Geometry_Intersect(long jarg1, long jarg2);

    public static final native boolean Geometry_Equal(long jarg1, long jarg2);

    public static final native boolean Geometry_Disjoint(long jarg1, long jarg2);

    public static final native boolean Geometry_Touches(long jarg1, long jarg2);

    public static final native boolean Geometry_Crosses(long jarg1, long jarg2);

    public static final native boolean Geometry_Within(long jarg1, long jarg2);

    public static final native boolean Geometry_Contains(long jarg1, long jarg2);

    public static final native boolean Geometry_Overlaps(long jarg1, long jarg2);

    public static final native int Geometry_TransformTo(long jarg1, long jarg2);

    public static final native int Geometry_Transform(long jarg1, long jarg2);

    public static final native long Geometry_GetSpatialReference(long jarg1);

    public static final native void Geometry_AssignSpatialReference(long jarg1, long jarg2);

    public static final native void Geometry_CloseRings(long jarg1);

    public static final native void Geometry_FlattenTo2D(long jarg1);

    public static final native void Geometry_GetEnvelope(long jarg1, double[] jarg2);

    public static final native int Geometry_WkbSize(long jarg1);

    public static final native int Geometry_GetCoordinateDimension(long jarg1);

    public static final native void Geometry_SetCoordinateDimension(long jarg1, int jarg2);

    public static final native int Geometry_GetDimension(long jarg1);

    public static final native long new_Geometry(int jarg1, String jarg2, byte[] jarg3, String jarg5);

    public static final native int Geometry_ExportToWkb(long jarg1, byte[] jarg2, int jarg4);

    public static final native long Geometry_Centroid(long jarg1);

    public static final native int GetDriverCount();

    public static final native int GetOpenDSCount();

    public static final native int SetGenerate_DB2_V72_BYTE_ORDER(int jarg1);

    public static final native void RegisterAll();

    public static final native long GetOpenDS(int jarg1);

    public static final native long Open(String jarg1, int jarg2);

    public static final native long OpenShared(String jarg1, int jarg2);

    public static final native long GetDriverByName(String jarg1);

    public static final native long GetDriver(int jarg1);

    public static final native int sum(int jarg1, int jarg2);
}
