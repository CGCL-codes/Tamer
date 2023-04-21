package com.volantis.mcs.papi.impl;

import com.davisor.data.AutoType;
import com.davisor.data.TextType;
import com.davisor.data.stream.JoinedDataBuffer;
import com.davisor.data.stream.ListDataBuffer;
import com.davisor.graphics.RenderAttributes;
import com.davisor.graphics.chart.ChannelAttributes;
import com.davisor.graphics.chart.ChartAttributesFactory;
import com.davisor.graphics.chart.ChartAxes;
import com.davisor.graphics.chart.ChartAxis;
import com.davisor.graphics.chart.ChartData;
import com.davisor.graphics.chart.ChartFactory;
import com.davisor.graphics.chart.ImageChart;
import com.davisor.graphics.chart.PlotRenderAttributes;
import com.davisor.graphics.data.PaintType;
import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.assets.ChartAsset;
import com.volantis.mcs.assets.GenericImageAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.ChartAttributes;
import com.volantis.mcs.papi.PAPIConstants;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.mcs.themes.values.AngleUnit;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.utilities.Convertors;
import com.volantis.mcs.utilities.MarinerURL;
import junitx.util.PrivateAccessor;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test the chart element.
 */
public class ChartElementImplTestCase extends BlockElementTestAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY = StyleValueFactory.getDefaultInstance();

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY = InternalDeviceFactory.getDefaultInstance();

    /**
     * The ChartElement being tested
     */
    private ChartElementImpl element;

    /**
     * The attributes associated with the Element being tested
     */
    private BlockAttributes attributes;

    /**
     * A pane
     */
    private Pane pane;

    /**
     * The pane instance associated with the pane
     */
    private PaneInstance paneInstance;

    /**
     * A spatial format iterator is needed so we can test pane indices.
     */
    private SpatialFormatIterator sfi;

    /**
     * The MarinerRequestContext
     */
    private MarinerRequestContext requestContext;

    /**
     * The MarinerPageContext
     */
    private TestMarinerPageContext pageContext;

    /**
     * The Protocol used to render the BlockElement
     */
    private VolantisProtocol protocol;

    /**
     * The Layout
     */
    private CanvasLayout canvasLayout;

    /**
     * The DeviceLayoutContext
     */
    private DeviceLayoutContext deviceLayoutContext;

    /**
     * The test environment context.
     */
    private TestEnvironmentContext environmentContext;

    /**
     * System temporary directory.
     */
    private final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    /**
     * The image attributes that were rendered through the protocol, if any.
     */
    private ImageAttributes imageAttributes;

    /**
     * Dummy device used for testing.
     */
    private InternalDevice device;

    protected void setUp() throws Exception {
        super.setUp();
        privateSetUp();
    }

    /**
     * Set up the test cases.
     */
    private void privateSetUp() {
        requestContext = new TestMarinerRequestContext();
        imageAttributes = null;
        protocol = new VolantisProtocolStub() {

            public void writeImage(ImageAttributes attributes) {
                imageAttributes = attributes;
            }
        };
        canvasLayout = new CanvasLayout();
        deviceLayoutContext = new TestDeviceLayoutContext();
        pane = new Pane(canvasLayout);
        pane.setName("pane");
        paneInstance = new PaneInstance(NDimensionalIndex.ZERO_DIMENSIONS);
        paneInstance.setDeviceLayoutContext(deviceLayoutContext);
        sfi = new SpatialFormatIterator(canvasLayout);
        sfi.setName("sfi");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "2");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "fixed");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "1");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "fixed");
        RuntimeDeviceLayout runtimeDeviceLayout = RuntimeDeviceLayoutTestHelper.activate(canvasLayout);
        pageContext = new TestMarinerPageContext();
        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);
        pageContext.setDeviceLayout(runtimeDeviceLayout);
        pageContext.pushDeviceLayoutContext(deviceLayoutContext);
        pageContext.setFormatInstance(paneInstance);
        pageContext.addPaneMapping(pane);
        environmentContext = new TestEnvironmentContext();
        ContextInternals.setEnvironmentContext(requestContext, environmentContext);
        ApplicationContext appContext = new ApplicationContext(requestContext) {

            public AssetURLRewriter getAssetURLRewriter() {
                return new AssetURLRewriter() {

                    public MarinerURL rewriteAssetURL(MarinerRequestContext requestContext, Asset asset, AssetGroup assetGroup, MarinerURL marinerURL) throws RepositoryException {
                        return marinerURL;
                    }
                };
            }
        };
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        ContextInternals.setApplicationContext(requestContext, appContext);
        protocol.setMarinerPageContext(pageContext);
        element = (ChartElementImpl) createTestablePAPIElement();
        attributes = createTestableBlockAttributes();
        final RuntimeProjectMock projectMock = new RuntimeProjectMock("runtimeProjectMock", expectations);
        requestContext.pushProject(projectMock);
        Map policies = new HashMap();
        policies.put("pixelsx", "120");
        policies.put("pixelsy", "165");
        device = INTERNAL_DEVICE_FACTORY.createInternalDevice(new DefaultDevice("dummy_device", policies, null));
        pageContext.setDevice(device);
    }

    protected BlockAttributes createTestableBlockAttributes() {
        return new ChartAttributes();
    }

    protected PAPIElement createTestablePAPIElement() {
        return new ChartElementImpl();
    }

    /**
     * Test what happens with an invalid chart - it has no data.
     *
     * todo Fix this really hokey test that uses a partially initialised element.
     */
    public void notestElementEndImplNoData() throws Exception {
        String chartName = "/chart.mcht";
        ChartAsset asset = new ChartAsset(chartName);
        pageContext.setAbsolutePageBaseURL("/");
        pageContext.setChartImageBase("/cib/");
        environmentContext.setRealPath(TEMP_DIR);
        ImageAsset fallbackAsset = new GenericImageAsset(chartName);
        fallbackAsset.setValue("fallback");
        ChartAttributes chartAttributes = (ChartAttributes) attributes;
        chartAttributes.setName(chartName);
        chartAttributes.setData(null);
        int result = element.elementEndImpl(requestContext, chartAttributes);
        assertEquals(PAPIConstants.CONTINUE_PROCESSING, result);
        verifyAttributes("fallback", null, null, null);
    }

    /**
     * Test what happens with an invalid chart - it has no valid encoding.
     *
     * todo Fix this really hokey test that uses a partially initialised element.
     */
    public void notestElementEndImplNoEncodingSet() throws Exception {
        String chartName = "/chart.mcht";
        ChartAsset asset = new ChartAsset(chartName);
        pageContext.setAbsolutePageBaseURL("/");
        pageContext.setChartImageBase("/cib/");
        environmentContext.setRealPath(TEMP_DIR);
        ImageAsset fallbackAsset = new GenericImageAsset("/fallback.mimg");
        fallbackAsset.setValue("fallback");
        ChartAttributes chartAttributes = (ChartAttributes) attributes;
        chartAttributes.setName(chartName);
        chartAttributes.setData("10 20 30 40 50");
        int result = element.elementEndImpl(requestContext, chartAttributes);
        assertEquals(PAPIConstants.CONTINUE_PROCESSING, result);
        verifyAttributes("fallback", null, null, null);
    }

    /**
     * Test what happens with an valid chart.
     *
     * todo Fix this really hokey test that uses a partially initialised element.
     */
    public void notestElementEndImplValidChart() throws Exception {
        String chartName = "/chart.mcht";
        ChartAsset asset = new ChartAsset(chartName);
        asset.setType("pie");
        pageContext.setAbsolutePageBaseURL("/");
        pageContext.setChartImageBase("/cib/");
        environmentContext.setRealPath(TEMP_DIR);
        ((DefaultDevice) device.getDevice()).setPolicyValue("jpeginpage", "true");
        ChartAttributes chartAttributes = (ChartAttributes) attributes;
        chartAttributes.setName(chartName);
        chartAttributes.setData("10 40 30 20 50");
        chartAttributes.setLabels("one two three four five");
        chartAttributes.setTitle("Wild Chart");
        chartAttributes.setStyleClass(".id");
        chartAttributes.setId("ID");
        chartAttributes.setTitle("Title");
        chartAttributes.setAltText("Alternative text");
        int result = element.elementEndImpl(requestContext, chartAttributes);
        assertEquals(PAPIConstants.CONTINUE_PROCESSING, result);
        verifyAttributes("/cib/dummy_device/chart.mcht.jpeg", "ID", "Title", "Alternative text");
        File file = new File(TEMP_DIR + File.separator + "cib" + File.separator + "dummy_device/chart.mcht.jpeg");
        assertTrue("Chart file should now exist: " + file.getPath(), file.exists());
        file.delete();
        assertFalse("Chart file should now be deleted", file.exists());
    }

    /**
     * Helper method for verifying attributes match.
     */
    private void verifyAttributes(String imageSrc, String id, String title, String altText) throws Exception {
        assertEquals("Image src:", imageSrc, imageAttributes.getSrc());
        assertEquals("ID:", id, imageAttributes.getId());
        assertEquals("Title:", title, imageAttributes.getTitle());
        assertEquals("AltText", altText, imageAttributes.getAltText());
    }

    /**
     * Test creating a chart in the temporary directory.
     */
    public void testChartCreation() throws Exception {
        String chartType = "pie";
        List series1 = new ArrayList();
        List series2 = new ArrayList();
        List labels = new ArrayList();
        series1.add(new Integer(10));
        series1.add(new Integer(20));
        series1.add(new Integer(30));
        series1.add(new Integer(40));
        series2.add(new Float(22.3));
        series2.add(new Float(45.0));
        series2.add(new Float(12.5));
        series2.add(new Float(18.8));
        labels.add("Label 1");
        labels.add("Label 2");
        labels.add("Label 3");
        labels.add("Label 4");
        ChartDefinition chartDefinition = new ChartDefinition();
        Color fgColors[] = chartDefinition.getFgColors();
        int size = "pie".equals(chartType) ? 4 : 3;
        ListDataBuffer[] buffers = new ListDataBuffer[size];
        buffers[0] = new ListDataBuffer(new AutoType("value", "test1", null), series1);
        buffers[1] = new ListDataBuffer(new AutoType("value", "test2", null), series2);
        buffers[2] = new ListDataBuffer(new TextType("label"), labels);
        if ("pie".equals(chartType)) {
            StringBuffer colors = new StringBuffer();
            for (int i = 0; i < fgColors.length; i++) {
                Color color = fgColors[i];
                colors.append(Convertors.colorToCSSColor(color)).append(' ');
            }
            buffers[3] = new ListDataBuffer(new PaintType("paint"), colors.toString(), ' ');
        }
        JoinedDataBuffer buffer = new JoinedDataBuffer(null, buffers, null);
        ChartData chartData = new ChartData(buffer);
        com.davisor.graphics.chart.ChartObjectAttributes chartAttributes = ChartAttributesFactory.getInstance().createChartObjectAttributes();
        chartAttributes.setChartType(chartType);
        chartAttributes.setWidth(new Integer(320), true);
        chartAttributes.setHeight(new Integer(240), true);
        chartAttributes.setContentType("image/jpeg");
        PlotRenderAttributes defaultPlotRenderAttributes = chartAttributes.getPlot();
        defaultPlotRenderAttributes.setValueFormat("{.value,float,#}");
        defaultPlotRenderAttributes.setFont(new Font("Arial", Font.PLAIN, 14));
        defaultPlotRenderAttributes.addChannelAttributes(chartData);
        ChannelAttributes channelAttributes = (ChannelAttributes) defaultPlotRenderAttributes.getChannelAttributes().get("test1");
        channelAttributes.setName("TEST 1");
        channelAttributes = (ChannelAttributes) defaultPlotRenderAttributes.getChannelAttributes().get("test2");
        channelAttributes.setName("TEST 2");
        ChartAxes chartAxes = (ChartAxes) chartAttributes.getAxes().get(0);
        ChartAxis xAxis = chartAxes.getAxis(0);
        xAxis.setTitleText("xTitle");
        RenderAttributes axisRender = xAxis.getRender();
        axisRender.setColor(Color.magenta);
        axisRender.setFont(new Font("Arial", Font.PLAIN, 14));
        ChartAxis yAxis = chartAxes.getAxis(1);
        yAxis.setTitleText("yTitle");
        axisRender = yAxis.getRender();
        axisRender.setColor(Color.cyan);
        axisRender.setFont(new Font("Arial", Font.PLAIN, 14));
        PlotRenderAttributes render = chartAxes.getPlot();
        render.setDefaults(defaultPlotRenderAttributes);
        ChartFactory factory = ChartFactory.getFactory(chartType);
        ImageChart chart = (ImageChart) factory.createChart(chartData, chartAttributes);
        String filename = TEMP_DIR + File.separator + "test.jpeg";
        chart.putImage(filename);
        File file = new File(filename);
        assertTrue("Chart file should now exist: " + filename, file.exists());
        file.delete();
        assertFalse("Chart file should now be deleted: " + filename, file.exists());
    }

    /**
     * Helper for getting the font size.
     */
    private int getFontSize(ChartElementImpl chartElement, StyleValue value) throws Throwable {
        Class[] params = { StyleValue.class };
        StyleValue[] arguments = { value };
        return ((Integer) PrivateAccessor.invoke(chartElement, "getFontSize", params, arguments)).intValue();
    }

    /**
     * Test the font size.
     */
    public void testGetFontSize() throws Throwable {
        ChartElementImpl chartElement = element;
        assertEquals(-1, getFontSize(chartElement, (null)));
        assertEquals(0, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX)));
        assertEquals(1, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 1, LengthUnit.PX)));
        assertEquals(128, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 128, LengthUnit.PX)));
        assertEquals(6, getFontSize(chartElement, FontSizeKeywords.XX_SMALL));
        assertEquals(8, getFontSize(chartElement, FontSizeKeywords.X_SMALL));
        assertEquals(10, getFontSize(chartElement, FontSizeKeywords.SMALL));
        assertEquals(12, getFontSize(chartElement, FontSizeKeywords.MEDIUM));
        assertEquals(14, getFontSize(chartElement, FontSizeKeywords.LARGE));
        assertEquals(16, getFontSize(chartElement, FontSizeKeywords.X_LARGE));
        assertEquals(18, getFontSize(chartElement, FontSizeKeywords.XX_LARGE));
        assertEquals(8, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 50)));
        assertEquals(8, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 51)));
        assertEquals(10, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 75)));
        assertEquals(10, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 76)));
        assertEquals(12, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 100)));
        assertEquals(12, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 101)));
        assertEquals(14, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 125)));
        assertEquals(14, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 126)));
        assertEquals(14, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 149.99)));
        assertEquals(16, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 150)));
        assertEquals(16, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 151)));
        assertEquals(18, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 200)));
        assertEquals(18, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 200.1)));
        assertEquals(18, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 201)));
        assertEquals(-1, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, -10)));
        assertEquals(-1, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 0)));
        assertEquals(-1, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 1)));
        assertEquals(-1, getFontSize(chartElement, STYLE_VALUE_FACTORY.getPercentage(null, 1.5)));
        assertEquals(10, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 10, LengthUnit.PX)));
        assertEquals(12, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 12, LengthUnit.PX)));
        assertEquals(-1, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, -1.02, LengthUnit.EM)));
        assertEquals(-1, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.EM)));
        assertEquals(-1, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 0.01, LengthUnit.EM)));
        assertEquals(6, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 0.25, LengthUnit.EM)));
        assertEquals(8, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 0.50, LengthUnit.EM)));
        assertEquals(10, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 0.75, LengthUnit.EM)));
        assertEquals(12, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 1, LengthUnit.EM)));
        assertEquals(14, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 1.25, LengthUnit.EM)));
        assertEquals(16, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 1.5, LengthUnit.EM)));
        assertEquals(16, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 1.7499999, LengthUnit.EM)));
        assertEquals(18, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 1.75, LengthUnit.EM)));
        assertEquals(18, getFontSize(chartElement, STYLE_VALUE_FACTORY.getLength(null, 1.7500001, LengthUnit.EM)));
    }

    /**
     * Test the conversion to degrees
     */
    public void testConvertToDegrees() throws Throwable {
        final float delta = 0.001f;
        try {
            invokeConvertToDegrees(null);
            fail("Expected NullPointerException.");
        } catch (Throwable throwable) {
        }
        assertEquals(0.0, invokeConvertToDegrees(STYLE_VALUE_FACTORY.getAngle(null, 0, AngleUnit.DEG)), delta);
        assertEquals(5.0, invokeConvertToDegrees(STYLE_VALUE_FACTORY.getAngle(null, 5, AngleUnit.DEG)), delta);
        assertEquals(5.24004, invokeConvertToDegrees(STYLE_VALUE_FACTORY.getAngle(null, 5.24004, AngleUnit.DEG)), delta);
        assertEquals(-1.4, invokeConvertToDegrees(STYLE_VALUE_FACTORY.getAngle(null, -1.4, AngleUnit.DEG)), delta);
        assertEquals(radians(0.0f), invokeConvertToDegrees(STYLE_VALUE_FACTORY.getAngle(null, 0, AngleUnit.RAD)), delta);
        assertEquals(radians(10.5f), invokeConvertToDegrees(STYLE_VALUE_FACTORY.getAngle(null, 10.5, AngleUnit.RAD)), delta);
        assertEquals(radians(0.12f), invokeConvertToDegrees(STYLE_VALUE_FACTORY.getAngle(null, 0.12, AngleUnit.RAD)), delta);
        assertEquals(gradians(0.0f), invokeConvertToDegrees(STYLE_VALUE_FACTORY.getAngle(null, 0, AngleUnit.GRAD)), delta);
        assertEquals(gradians(10.6f), invokeConvertToDegrees(STYLE_VALUE_FACTORY.getAngle(null, 10.6, AngleUnit.GRAD)), delta);
    }

    /**
     * Helper method that converts radians to degrees.
     * @param input angle in radians.
     * @return the converted angle in degrees.
     */
    private float radians(float input) {
        return (float) Math.toDegrees(input);
    }

    /**
     * Helper method that converts gradians to degrees.
     * @param input angle in gradians.
     * @return the converted angle in degrees.
     */
    private float gradians(float input) {
        return (float) (360.0 / 400.0) * input;
    }

    /**
     * Helper method.
     */
    private double invokeConvertToDegrees(StyleValue value) throws Throwable {
        Class[] params = { StyleValue.class };
        Float result = (Float) PrivateAccessor.invoke(element, "convertToDegrees", params, new StyleValue[] { value });
        return result.floatValue();
    }
}
