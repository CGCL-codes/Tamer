package com.android.ide.eclipse.tests.functests.layoutRendering;

import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.KeyboardStateQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.NavigationMethodQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.PixelDensityQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenDimensionQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenOrientationQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenRatioQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenSizeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.TextInputMethodQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.TouchScreenQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.KeyboardStateQualifier.KeyboardState;
import com.android.ide.eclipse.adt.internal.resources.configurations.NavigationMethodQualifier.NavigationMethod;
import com.android.ide.eclipse.adt.internal.resources.configurations.PixelDensityQualifier.Density;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenOrientationQualifier.ScreenOrientation;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenRatioQualifier.ScreenRatio;
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenSizeQualifier.ScreenSize;
import com.android.ide.eclipse.adt.internal.resources.configurations.TextInputMethodQualifier.TextInputMethod;
import com.android.ide.eclipse.adt.internal.resources.configurations.TouchScreenQualifier.TouchScreenType;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.LayoutBridge;
import com.android.ide.eclipse.tests.SdkTestCase;
import com.android.layoutlib.api.ILayoutResult;
import com.android.layoutlib.api.IProjectCallback;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ApiDemosRenderingTest extends SdkTestCase {

    /**
     * Custom parser that implements {@link IXmlPullParser} (which itself extends
     * {@link XmlPullParser}).
     */
    private static final class TestParser extends KXmlParser implements IXmlPullParser {

        /**
         * Since we're not going to go through the result of the rendering/layout, we can return
         * null for the View Key.
         */
        public Object getViewKey() {
            return null;
        }
    }

    private static final class ProjectCallBack implements IProjectCallback {

        private int mIdCounter = 0x7f000000;

        private Map<Integer, String[]> mResourceMap = new HashMap<Integer, String[]>();

        private boolean mCustomViewAttempt = false;

        public String getNamespace() {
            return "com.example.android.apis";
        }

        public Integer getResourceValue(String type, String name) {
            Integer result = ++mIdCounter;
            mResourceMap.put(result, new String[] { name, type });
            return result;
        }

        @SuppressWarnings("unchecked")
        public Object loadView(String name, Class[] constructorSignature, Object[] constructorArgs) throws ClassNotFoundException, Exception {
            mCustomViewAttempt = true;
            return null;
        }

        public String[] resolveResourceValue(int id) {
            return mResourceMap.get(id);
        }

        public String resolveResourceValue(int[] id) {
            return null;
        }
    }

    public void testApiDemos() throws IOException, XmlPullParserException {
        findApiDemos();
    }

    private void findApiDemos() throws IOException, XmlPullParserException {
        IAndroidTarget[] targets = getSdk().getTargets();
        for (IAndroidTarget target : targets) {
            String path = target.getPath(IAndroidTarget.SAMPLES);
            File samples = new File(path);
            if (samples.isDirectory()) {
                File[] files = samples.listFiles();
                for (File file : files) {
                    if ("apidemos".equalsIgnoreCase(file.getName())) {
                        testSample(target, file);
                        return;
                    }
                }
            }
        }
        fail("Failed to find ApiDemos!");
    }

    private void testSample(IAndroidTarget target, File sampleProject) throws IOException, XmlPullParserException {
        AndroidTargetData data = getSdk().getTargetData(target);
        if (data == null) {
            fail("No AndroidData!");
        }
        LayoutBridge bridge = data.getLayoutBridge();
        if (bridge.status != LoadStatus.LOADED || bridge.bridge == null) {
            fail("Fail to load the bridge");
        }
        File resFolder = new File(sampleProject, SdkConstants.FD_RES);
        if (resFolder.isDirectory() == false) {
            fail("Sample project has no res folder!");
        }
        File layoutFolder = new File(resFolder, SdkConstants.FD_LAYOUT);
        if (layoutFolder.isDirectory() == false) {
            fail("Sample project has no layout folder!");
        }
        ProjectResources framework = ResourceManager.getInstance().loadFrameworkResources(target);
        ProjectResources project = new ProjectResources(false);
        ResourceManager.getInstance().loadResources(project, resFolder);
        FolderConfiguration config = getConfiguration();
        Map<String, Map<String, IResourceValue>> configuredFramework = framework.getConfiguredResources(config);
        Map<String, Map<String, IResourceValue>> configuredProject = project.getConfiguredResources(config);
        boolean saveFiles = System.getenv("save_file") != null;
        File[] layouts = layoutFolder.listFiles();
        for (File layout : layouts) {
            TestParser parser = new TestParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(new FileReader(layout));
            System.out.println("Rendering " + layout.getName());
            ProjectCallBack projectCallBack = new ProjectCallBack();
            ILayoutResult result = bridge.bridge.computeLayout(parser, null, 320, 480, false, 160, 160, 160, "Theme", false, configuredProject, configuredFramework, projectCallBack, null);
            if (result.getSuccess() != ILayoutResult.SUCCESS) {
                if (projectCallBack.mCustomViewAttempt == false) {
                    System.out.println("FAILED");
                    fail(String.format("Rendering %1$s: %2$s", layout.getName(), result.getErrorMessage()));
                } else {
                    System.out.println("Ignore custom views for now");
                }
            } else {
                if (saveFiles) {
                    File tmp = File.createTempFile(layout.getName(), ".png");
                    ImageIO.write(result.getImage(), "png", tmp);
                }
                System.out.println("Success!");
            }
        }
    }

    /**
     * Returns a config. This must be a valid config like a device would return. This is to
     * prevent issues where some resources don't exist in all cases and not in the default
     * (for instance only available in hdpi and mdpi but not in default).
     * @return
     */
    private FolderConfiguration getConfiguration() {
        FolderConfiguration config = new FolderConfiguration();
        config.addQualifier(new ScreenSizeQualifier(ScreenSize.NORMAL));
        config.addQualifier(new ScreenRatioQualifier(ScreenRatio.NOTLONG));
        config.addQualifier(new ScreenOrientationQualifier(ScreenOrientation.PORTRAIT));
        config.addQualifier(new PixelDensityQualifier(Density.MEDIUM));
        config.addQualifier(new TouchScreenQualifier(TouchScreenType.FINGER));
        config.addQualifier(new KeyboardStateQualifier(KeyboardState.HIDDEN));
        config.addQualifier(new TextInputMethodQualifier(TextInputMethod.QWERTY));
        config.addQualifier(new NavigationMethodQualifier(NavigationMethod.TRACKBALL));
        config.addQualifier(new ScreenDimensionQualifier(480, 320));
        return config;
    }
}
