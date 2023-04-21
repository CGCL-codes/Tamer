package ufrj.safcp.imageprocessing;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class ImageprocessingMCRFactory {

    /** Application key data */
    private static final byte[] sSessionKey = { 57, 48, 67, 68, 66, 54, 57, 52, 66, 53, 51, 54, 53, 48, 54, 48, 52, 66, 55, 56, 66, 55, 49, 55, 55, 56, 48, 65, 49, 65, 67, 53, 67, 54, 50, 55, 55, 67, 48, 57, 55, 51, 57, 55, 66, 56, 69, 66, 48, 51, 67, 54, 52, 65, 50, 55, 54, 57, 49, 67, 48, 70, 56, 48, 54, 56, 68, 55, 53, 57, 68, 65, 54, 67, 54, 56, 65, 65, 66, 68, 52, 66, 53, 69, 51, 66, 54, 52, 51, 51, 49, 53, 70, 69, 54, 54, 50, 68, 68, 48, 49, 55, 54, 52, 51, 51, 52, 68, 66, 65, 70, 53, 69, 52, 69, 69, 57, 54, 67, 70, 55, 68, 51, 50, 55, 56, 70, 70, 54, 49, 56, 51, 53, 66, 52, 67, 65, 68, 68, 65, 53, 52, 48, 51, 51, 65, 48, 54, 52, 55, 50, 52, 57, 65, 50, 54, 53, 54, 67, 56, 56, 48, 52, 56, 68, 49, 52, 52, 69, 49, 68, 49, 69, 69, 66, 66, 70, 49, 67, 50, 67, 70, 53, 50, 54, 55, 70, 52, 51, 52, 54, 51, 55, 53, 51, 65, 57, 54, 57, 65, 68, 65, 51, 54, 70, 55, 52, 54, 54, 53, 67, 65, 53, 48, 53, 50, 69, 69, 50, 66, 48, 55, 67, 52, 53, 53, 49, 53, 53, 54, 50, 68, 52, 55, 52, 55, 51, 50, 52, 54, 67, 54, 66, 50, 50, 70, 65, 65, 56, 67, 68, 70, 66, 67, 50, 50 };

    /** Public key data */
    private static final byte[] sPublicKey = { 51, 48, 56, 49, 57, 68, 51, 48, 48, 68, 48, 54, 48, 57, 50, 65, 56, 54, 52, 56, 56, 54, 70, 55, 48, 68, 48, 49, 48, 49, 48, 49, 48, 53, 48, 48, 48, 51, 56, 49, 56, 66, 48, 48, 51, 48, 56, 49, 56, 55, 48, 50, 56, 49, 56, 49, 48, 48, 67, 52, 57, 67, 65, 67, 51, 52, 69, 68, 49, 51, 65, 53, 50, 48, 54, 53, 56, 70, 54, 70, 56, 69, 48, 49, 51, 56, 67, 52, 51, 49, 53, 66, 52, 51, 49, 53, 50, 55, 55, 69, 68, 51, 70, 55, 68, 65, 69, 53, 51, 48, 57, 57, 68, 66, 48, 56, 69, 69, 53, 56, 57, 70, 56, 48, 52, 68, 52, 66, 57, 56, 49, 51, 50, 54, 65, 53, 50, 67, 67, 69, 52, 51, 56, 50, 69, 57, 70, 50, 66, 52, 68, 48, 56, 53, 69, 66, 57, 53, 48, 67, 55, 65, 66, 49, 50, 69, 68, 69, 50, 68, 52, 49, 50, 57, 55, 56, 50, 48, 69, 54, 51, 55, 55, 65, 53, 70, 69, 66, 53, 54, 56, 57, 68, 52, 69, 54, 48, 51, 50, 70, 54, 48, 67, 52, 51, 48, 55, 52, 65, 48, 52, 67, 50, 54, 65, 66, 55, 50, 70, 53, 52, 66, 53, 49, 66, 66, 52, 54, 48, 53, 55, 56, 55, 56, 53, 66, 49, 57, 57, 48, 49, 52, 51, 49, 52, 65, 54, 53, 70, 48, 57, 48, 66, 54, 49, 70, 67, 50, 48, 49, 54, 57, 52, 53, 51, 66, 53, 56, 70, 67, 56, 66, 65, 52, 51, 69, 54, 55, 55, 54, 69, 66, 55, 69, 67, 68, 51, 49, 55, 56, 66, 53, 54, 65, 66, 48, 70, 65, 48, 54, 68, 68, 54, 52, 57, 54, 55, 67, 66, 49, 52, 57, 69, 53, 48, 50, 48, 49, 49, 49 };

    /** Component's MATLAB path */
    private static final String[] sMatlabPath = { "imageprocess/", "$TOOLBOXDEPLOYDIR/", "safcp/workspace/imageprocessing_cardreader/scripts/", "$TOOLBOXMATLABDIR/general/", "$TOOLBOXMATLABDIR/ops/", "$TOOLBOXMATLABDIR/lang/", "$TOOLBOXMATLABDIR/elmat/", "$TOOLBOXMATLABDIR/randfun/", "$TOOLBOXMATLABDIR/elfun/", "$TOOLBOXMATLABDIR/specfun/", "$TOOLBOXMATLABDIR/matfun/", "$TOOLBOXMATLABDIR/datafun/", "$TOOLBOXMATLABDIR/polyfun/", "$TOOLBOXMATLABDIR/funfun/", "$TOOLBOXMATLABDIR/sparfun/", "$TOOLBOXMATLABDIR/scribe/", "$TOOLBOXMATLABDIR/graph2d/", "$TOOLBOXMATLABDIR/graph3d/", "$TOOLBOXMATLABDIR/specgraph/", "$TOOLBOXMATLABDIR/graphics/", "$TOOLBOXMATLABDIR/uitools/", "$TOOLBOXMATLABDIR/strfun/", "$TOOLBOXMATLABDIR/imagesci/", "$TOOLBOXMATLABDIR/iofun/", "$TOOLBOXMATLABDIR/audiovideo/", "$TOOLBOXMATLABDIR/timefun/", "$TOOLBOXMATLABDIR/datatypes/", "$TOOLBOXMATLABDIR/verctrl/", "$TOOLBOXMATLABDIR/codetools/", "$TOOLBOXMATLABDIR/helptools/", "$TOOLBOXMATLABDIR/winfun/", "$TOOLBOXMATLABDIR/winfun/NET/", "$TOOLBOXMATLABDIR/demos/", "$TOOLBOXMATLABDIR/timeseries/", "$TOOLBOXMATLABDIR/hds/", "$TOOLBOXMATLABDIR/guide/", "$TOOLBOXMATLABDIR/plottools/", "toolbox/local/", "toolbox/shared/dastudio/", "$TOOLBOXMATLABDIR/datamanager/", "toolbox/shared/mapgeodesy/", "toolbox/compiler/", "toolbox/images/colorspaces/", "toolbox/images/images/", "toolbox/images/imuitools/", "toolbox/images/iptformats/", "toolbox/images/iptutils/", "toolbox/shared/imageslib/", "toolbox/map/map/", "toolbox/map/mapdemos/", "toolbox/map/mapdisp/", "toolbox/map/mapproj/", "toolbox/shared/maputils/" };

    /** Items to be added to component's class path */
    private static final String[] sClassPath = { "java/jar/toolbox/images.jar" };

    /** Items to be added to component's lib path */
    private static final String[] sLibraryPath = { "bin/win32/" };

    /** MCR instance-specific runtime options */
    private static final String[] sApplicationOptions = {};

    /** MCR global runtime options */
    private static final String[] sRuntimeOptions = {};

    /** Runtime warning states */
    private static final String[] sSetWarningState = { "off:MATLAB:dispatcher:nameConflict" };

    /** Component's preference directory */
    private static final String sComponentPrefDir = "imageprocess_93C133B3E371E00260CD14A3D6FBB21A";

    /** Component name */
    private static final String sComponentName = "imageprocessing";

    /** Pointer to native component-data structure */
    private static final NativeComponentData sComponentData = new NativeComponentData(createComponentData(MWMCR.findComponentParentDirOnClassPath(sComponentName, "ufrj.safcp.imageprocessing")));

    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = new MWComponentOptions(MWCtfExtractLocation.EXTRACT_TO_CACHE, new MWCtfClassLoaderSource(ImageprocessingMCRFactory.class));

    /** Creates a native component-data structure */
    static NativePtr createComponentData(String pathToComponent) {
        try {
            return MWMCR.getNativeMCR().mclCreateComponentData(sPublicKey, sComponentName, "", sSessionKey, sMatlabPath, sClassPath, sLibraryPath, sApplicationOptions, sRuntimeOptions, sComponentPrefDir, pathToComponent, sSetWarningState);
        } catch (MWException e) {
            return NativePtr.NULL;
        }
    }

    private ImageprocessingMCRFactory() {
    }

    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(sComponentData, componentOptions, ImageprocessingMCRFactory.class, sComponentName, new int[] { 7, 11 });
    }

    public static MWMCR newInstance() throws MWException {
        return newInstance(sDefaultComponentOptions);
    }
}
