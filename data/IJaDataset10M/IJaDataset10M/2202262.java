package com.volantis.mcs.expression.functions.diselect;

import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.functions.FunctionTable;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.pipeline.Namespace;

/**
 * The set of diselect related functions.
 */
public class DISelectFunctions {

    private static final String DISELECT_FN = "dco";

    private static final String DISELECT_FN_URI = Namespace.DISELECT.getURI();

    private static final ImmutableExpandedName CSSMQ_WIDTH = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-width");

    private static final Function CSSMQ_WIDTH_FUNCTION = new DIWidthFunction();

    private static final ImmutableExpandedName CSSMQ_HEIGHT = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-height");

    private static final Function CSSMQ_HEIGHT_FUNCTION = new DIHeightFunction();

    private static final ImmutableExpandedName CSSMQ_DEVICE_WIDTH = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-device-width");

    private static final Function CSSMQ_DEVICE_WIDTH_FUNCTION = new DIDeviceWidthFunction();

    private static final ImmutableExpandedName CSSMQ_DEVICE_HEIGHT = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-device-height");

    private static final Function CSSMQ_DEVICE_HEIGHT_FUNCTION = new DIDeviceHeightFunction();

    private static final ImmutableExpandedName CSSMQ_DEVICE_ASPECT_RATIO = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-device-aspect-ratio");

    private static final Function CSSMQ_DEVICE_ASPECT_RATIO_FUNCTION = new DIAspectRatioFunction();

    private static final ImmutableExpandedName CSSMQ_DEVICE_ASPECT_RATIO_WIDTH = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-device-aspect-ratio-width");

    private static final Function CSSMQ_DEVICE_ASPECT_RATIO_WIDTH_FUNCTION = new DIAspectRatioWidthFunction();

    private static final ImmutableExpandedName CSSMQ_DEVICE_ASPECT_RATIO_HEIGHT = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-device-aspect-ratio-height");

    private static final Function CSSMQ_DEVICE_ASPECT_RATIO_HEIGHT_FUNCTION = new DIAspectRatioHeightFunction();

    private static final ImmutableExpandedName CSSMQ_COLOR = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-color");

    private static final Function CSSMQ_COLOR_FUNCTION = new DIColorFunction();

    private static final ImmutableExpandedName CSSMQ_COLOR_INDEX = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-color-index");

    private static final Function CSSMQ_COLOR_INDEX_FUNCTION = new DIColorIndexFunction();

    private static final ImmutableExpandedName CSSMQ_MONOCHROME = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-monochrome");

    private static final Function CSSMQ_MONOCHROME_FUNCTION = new DIMonochromeFunction();

    private static final ImmutableExpandedName CSSMQ_RESOLUTION = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-resolution");

    private static final Function CSSMQ_RESOLUTION_FUNCTION = new DIResolutionFunction();

    private static final ImmutableExpandedName CSSMQ_SCAN = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-scan");

    private static final Function CSSMQ_SCAN_FUNCTION = new DIScanFunction();

    private static final ImmutableExpandedName CSSMQ_GRID = new ImmutableExpandedName(DISELECT_FN_URI, "cssmq-grid");

    private static final Function CSSMQ_GRID_FUNCTION = new DIGridFunction();

    /**
     * The table of functions.
     */
    public static final FunctionTable FUNCTION_TABLE;

    static {
        ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
        FunctionTableBuilder builder = factory.createFunctionTableBuilder();
        builder.addDefaultPrefixMappings(DISELECT_FN, DISELECT_FN_URI);
        builder.addFunction(CSSMQ_WIDTH, CSSMQ_WIDTH_FUNCTION);
        builder.addFunction(CSSMQ_HEIGHT, CSSMQ_HEIGHT_FUNCTION);
        builder.addFunction(CSSMQ_DEVICE_WIDTH, CSSMQ_DEVICE_WIDTH_FUNCTION);
        builder.addFunction(CSSMQ_DEVICE_HEIGHT, CSSMQ_DEVICE_HEIGHT_FUNCTION);
        builder.addFunction(CSSMQ_DEVICE_ASPECT_RATIO, CSSMQ_DEVICE_ASPECT_RATIO_FUNCTION);
        builder.addFunction(CSSMQ_DEVICE_ASPECT_RATIO_HEIGHT, CSSMQ_DEVICE_ASPECT_RATIO_HEIGHT_FUNCTION);
        builder.addFunction(CSSMQ_DEVICE_ASPECT_RATIO_WIDTH, CSSMQ_DEVICE_ASPECT_RATIO_WIDTH_FUNCTION);
        builder.addFunction(CSSMQ_COLOR, CSSMQ_COLOR_FUNCTION);
        builder.addFunction(CSSMQ_COLOR_INDEX, CSSMQ_COLOR_INDEX_FUNCTION);
        builder.addFunction(CSSMQ_MONOCHROME, CSSMQ_MONOCHROME_FUNCTION);
        builder.addFunction(CSSMQ_RESOLUTION, CSSMQ_RESOLUTION_FUNCTION);
        builder.addFunction(CSSMQ_SCAN, CSSMQ_SCAN_FUNCTION);
        builder.addFunction(CSSMQ_GRID, CSSMQ_GRID_FUNCTION);
        FUNCTION_TABLE = builder.buildTable();
    }
}
