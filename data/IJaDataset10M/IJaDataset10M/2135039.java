package org.kalypso.nofdpidss.flood.risk.core;

import javax.xml.namespace.QName;
import org.kalypso.risk.model.schema.KalypsoRiskSchemaCatalog;

public interface INofdpFloodRiskConstants {

    public static String BASE_FOLDER = "floodRisk";

    public static String MODEL_FOLDER = "models";

    public static String GLOBAL_FLOOD_RISK_TEMPLATES = "floodRisk/templates";

    public static String RASTER_DATA_MODEL_NAME = "RasterDataModel.gml";

    public static String RASTER_CONTROL_MODEL_NAME = "RasterizationControlModel.gml";

    public static String VECTOR_DATA_MODEL_NAME = "VectorDataModel.gml";

    public static final String INPUT_FOLDER = "input";

    public static final String RASTER_FOLDER = "raster";

    public static final QName PROP_LANDUSE_COLORS_COLLECTION = new QName(KalypsoRiskSchemaCatalog.NS_PREDEFINED_DATASET, "landuseClassesDefaultColorsCollection");

    public static final QName PROP_DAMAGE_FUNCTION_COLLECTION = new QName(KalypsoRiskSchemaCatalog.NS_PREDEFINED_DATASET, "damageFunctionsCollection");

    public static final QName PROP_ASSET_VALUES_CLASSES_COLLECTION = new QName(KalypsoRiskSchemaCatalog.NS_PREDEFINED_DATASET, "assetValueClassesCollection");
}
