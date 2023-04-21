package com.moesol.gwt.maps.client;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Provider;
import com.moesol.gwt.maps.client.units.Degrees;
import com.moesol.maps.tools.Placement;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ GWT.class, DOM.class })
public class DeclutterEngineTest {

    class IconListBuilder {

        private List<Icon> icons = new ArrayList<Icon>();

        public IconListBuilder add(double lat, double lng, String label) {
            Icon icon = new Icon();
            icon.setLocation(Degrees.geodetic(lat, lng));
            icon.setLabel(label);
            icon.setIconOffset(ViewCoords.builder().setX(-16).setY(-16).build());
            icons.add(icon);
            return this;
        }

        public List<Icon> build() {
            return icons;
        }
    }

    private static final int[] SEARCH_ROW_OFFSETS = { 0, -1, -2, -3, -4, 1, 2, 3, 4, -5, -6, -7, -8, 5, 6, 7, 8, 0, -1, -2, -3, -4, 1, 2, 3, 4, -5, -6, -7, -8, 5, 6, 7, 8 };

    private static final int[] SEARCH_COL_OFFSETS = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

    private IMapView m_mapView;

    private IProjection m_projection = new CylEquiDistProj(512, 180, 180);

    private Image m_imageMock;

    private Label m_labelMock;

    private ViewPort m_viewport = new ViewPort();

    private DeclutterEngine engine;

    private IconEngine iconEngine;

    private RecordingIconPlacer widgetPositioner = new RecordingIconPlacer();

    @Before
    public void before() {
        mockStatic(GWT.class);
        mockStatic(DOM.class);
        m_mapView = mock(IMapView.class);
        m_labelMock = mock(Label.class);
        m_imageMock = mock(Image.class);
        when(m_mapView.getViewport()).thenReturn(m_viewport);
        when(m_mapView.getProjection()).thenReturn(m_projection);
        when(m_mapView.getWidgetPositioner()).thenReturn(widgetPositioner);
        m_viewport.setProjection(m_projection);
        when(m_labelMock.getOffsetHeight()).thenReturn(16);
        when(m_labelMock.getOffsetWidth()).thenReturn(48);
        when(m_imageMock.getOffsetHeight()).thenReturn(32);
        when(m_imageMock.getOffsetWidth()).thenReturn(32);
        Icon.IMAGE_PROVIDER = new Provider<Image>() {

            @Override
            public Image get() {
                return m_imageMock;
            }
        };
        Icon.LABEL_PROVIDER = new Provider<Label>() {

            @Override
            public Label get() {
                return m_labelMock;
            }
        };
        Icon.LABEL_STYLER = mock(Icon.LabelStyler.class);
        engine = new DeclutterEngine(m_mapView);
        engine.cellWidth = 16;
        engine.cellHeight = 16;
        engine.searchRowOffsets = SEARCH_ROW_OFFSETS;
        engine.searchColOffsets = SEARCH_COL_OFFSETS;
        iconEngine = new IconEngine(m_mapView);
    }

    @Test
    public void testRoundUp() {
        assertEquals(1, DeclutterEngine.roundUp(1, 4));
        assertEquals(1, DeclutterEngine.roundUp(3, 4));
        assertEquals(1, DeclutterEngine.roundUp(4, 4));
        assertEquals(2, DeclutterEngine.roundUp(5, 4));
        assertEquals(2, DeclutterEngine.roundUp(7, 4));
        assertEquals(2, DeclutterEngine.roundUp(8, 4));
    }

    @Test
    public void testMakeBitSet() {
        int n = engine.makeBitSet();
        assertEquals(25, engine.m_nRowsInView);
        assertEquals(38, engine.m_nColsInView);
        assertEquals(25 * 38, n);
    }

    @Test
    public void testComputeIconCenterViewCoords() {
        Icon icon = new Icon();
        icon.setLocation(Degrees.geodetic(0, 0));
        icon.setIconOffset(ViewCoords.builder().setX(-8).setY(-8).build());
        ViewCoords vc = engine.computeIconCenterViewCoords(icon);
        assertEquals(300, vc.getX());
        assertEquals(200, vc.getY());
        icon.setIconOffset(ViewCoords.builder().setX(1).setY(-8).build());
        vc = engine.computeIconCenterViewCoords(icon);
        assertEquals(300, vc.getX());
        assertEquals(200, vc.getY());
    }

    @Test
    public void when_two_labels_at_same_position_then_second_is_offset() {
        List<Icon> icons = builder().add(0, 0, "Label1").add(0, 0, "Label2").build();
        engine.declutter(icons);
        assertEquals(36, icons.get(0).getDeclutterOffset().getX());
        assertEquals(-8, icons.get(0).getDeclutterOffset().getY());
        assertEquals(36, icons.get(1).getDeclutterOffset().getX());
        assertEquals(-24, icons.get(1).getDeclutterOffset().getY());
    }

    @Test
    public void when_two_labels_not_near_then_neither_is_offset() {
        List<Icon> icons = builder().add(0, 0, "Label1").add(5, 0, "Label2").build();
        engine.declutter(icons);
        assertEquals(36, icons.get(0).getDeclutterOffset().getX());
        assertEquals(-8, icons.get(0).getDeclutterOffset().getY());
        assertEquals(36, icons.get(1).getDeclutterOffset().getX());
        assertEquals(-10, icons.get(1).getDeclutterOffset().getY());
    }

    @Test
    public void testAllRightHandLabels() {
        IconListBuilder builder = builder();
        for (int i = 0; i < 17; i++) {
            builder.add(0, 0, "label" + i);
        }
        List<Icon> icons = builder.build();
        engine.declutter(icons);
        int n = 0;
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-8, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-24, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-40, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-56, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-72, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(8, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(24, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(40, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(56, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-88, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-104, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-120, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-136, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(72, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(88, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(104, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(36, icons.get(n).getDeclutterOffset().getX());
        assertEquals(120, icons.get(n++).getDeclutterOffset().getY());
    }

    @Test
    public void testAllLeftHandLabels() throws IOException {
        IconListBuilder builder = builder();
        for (int i = 0; i < 34; i++) {
            builder.add(0, 0, "label" + i);
        }
        List<Icon> icons = builder.build();
        engine.declutter(icons);
        int n = 17;
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-8, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-24, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-40, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-56, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-72, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(8, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(24, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(40, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(56, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-88, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-104, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-120, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(-136, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(72, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(88, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(104, icons.get(n++).getDeclutterOffset().getY());
        assertEquals(-92, icons.get(n).getDeclutterOffset().getX());
        assertEquals(120, icons.get(n++).getDeclutterOffset().getY());
    }

    @Test
    public void testRenderAllLeftHandLabels() throws IOException {
        m_viewport.setSize(512, 512);
        IconListBuilder builder = builder();
        for (int i = 0; i < 34; i++) {
            builder.add(0, 0, "label" + i);
        }
        List<Icon> icons = builder.build();
        engine.declutter(icons);
        renderToPng(icons, "allLeft.png");
    }

    @Test
    public void testRenderVerticalIcons() throws IOException {
        m_viewport.setSize(512, 512);
        IconListBuilder builder = builder();
        builder.add(0, 0, "label");
        builder.add(1, 0, "label");
        builder.add(2, 0, "label");
        builder.add(3, 0, "label");
        List<Icon> icons = builder.build();
        engine.declutter(icons);
        renderToPng(icons, "vertical.png");
    }

    @Test
    public void testRenderHorizontalIcons() throws IOException {
        m_viewport.setSize(512, 512);
        IconListBuilder builder = builder();
        builder.add(0, 0, "label");
        builder.add(0, 10, "label");
        builder.add(0, 20, "label");
        builder.add(0, 30, "label");
        List<Icon> icons = builder.build();
        engine.declutter(icons);
        renderToPng(icons, "horizontal.png");
    }

    @Test
    public void testRenderHorizontalWithInterleavedSearchIcons() throws IOException {
        int[] INTERLEAVED_ROW_OFFSETS = { 0, -1, 1, -2, 2, -3, 3, -4, 4, -5, 5, -6, 6, -7, 7, -8, 8, 0, -1, 1, -2, 2, -3, 3, -4, 4, -5, 5, -6, 6, -7, 7, -8, 8 };
        engine.searchRowOffsets = INTERLEAVED_ROW_OFFSETS;
        m_viewport.setSize(512, 512);
        IconListBuilder builder = builder();
        builder.add(0, 0, "label");
        builder.add(0, 10, "label");
        builder.add(0, 20, "label");
        builder.add(0, 30, "label");
        List<Icon> icons = builder.build();
        engine.declutter(icons);
        renderToPng(icons, "horizontal-interleaved.png");
    }

    @Test
    public void testRenderHorizontal4x8Icons() throws IOException {
        m_viewport.setSize(512, 512);
        engine.cellWidth = 4;
        engine.cellHeight = 8;
        IconListBuilder builder = builder();
        builder.add(0, 0, "label");
        builder.add(0, 10, "label");
        builder.add(0, 20, "label");
        builder.add(0, 30, "label");
        List<Icon> icons = builder.build();
        engine.declutter(icons);
        renderToPng(icons, "horizontal4x8.png");
    }

    @Test
    public void testRenderDiamonIcons() throws IOException {
        m_viewport.setSize(512, 512);
        IconListBuilder builder = builder();
        builder.add(0, 0, "label");
        builder.add(10, 0, "label");
        builder.add(-10, 0, "label");
        builder.add(0, 10, "label");
        builder.add(0, -10, "label");
        List<Icon> icons = builder.build();
        engine.declutter(icons);
        renderToPng(icons, "diamond.png");
    }

    @Test
    public void testRender4x8LeftHandLabels() throws IOException {
        m_viewport.setSize(512, 512);
        engine.cellWidth = 4;
        engine.cellHeight = 8;
        IconListBuilder builder = builder();
        for (int i = 0; i < 34; i++) {
            builder.add(0, 0, "label" + i);
        }
        List<Icon> icons = builder.build();
        engine.declutter(icons);
        renderToPng(icons, "all4x8.png");
    }

    private IconListBuilder builder() {
        return new IconListBuilder();
    }

    private void renderToPng(List<Icon> icons, String name) throws IOException {
        File imageTarget = new File("target", "images");
        imageTarget.mkdirs();
        BufferedImage img = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 512, 512);
        for (int y = 0; y < 512; y += engine.cellHeight) {
            for (int x = 0; x < 512; x += engine.cellWidth) {
                int row = y / engine.cellHeight;
                int column = x / engine.cellWidth;
                if (engine.isCellUsed(row, column)) {
                    graphics.setColor(new Color(0, 255, 0, 64));
                    graphics.fillRect(x, y, engine.cellWidth, engine.cellHeight);
                    graphics.setColor(Color.BLUE);
                    graphics.drawRect(x, y, engine.cellWidth, engine.cellHeight);
                } else {
                    graphics.setColor(Color.BLUE);
                    graphics.drawRect(x, y, engine.cellWidth, engine.cellHeight);
                }
            }
        }
        for (Icon icon : icons) {
            iconEngine.positionOneIcon(icon);
        }
        graphics.setColor(Color.RED);
        for (Placement p : widgetPositioner.images) {
            graphics.drawRect(p.x, p.y, p.width, p.height);
        }
        graphics.setColor(Color.YELLOW);
        for (Placement p : widgetPositioner.labels) {
            graphics.drawRect(p.x, p.y, p.width, p.height);
        }
        graphics.setStroke(new BasicStroke(1.0f));
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < widgetPositioner.images.size(); i++) {
            Placement pImage = widgetPositioner.images.get(i);
            Placement pLabel = widgetPositioner.labels.get(i);
            int imageHalfWidth = pImage.width / 2;
            int imageHalfHeight = pImage.height / 2;
            int labelHalfHeight = pLabel.height / 2;
            if (pImage.x < pLabel.x) {
                graphics.drawLine(pImage.x + imageHalfWidth, pImage.y + imageHalfHeight, pLabel.x - engine.cellWidth, pLabel.y + labelHalfHeight);
                graphics.drawLine(pLabel.x - engine.cellWidth, pLabel.y + labelHalfHeight, pLabel.x, pLabel.y + labelHalfHeight);
            } else {
                graphics.drawLine(pImage.x + imageHalfWidth, pImage.y + imageHalfHeight, pLabel.x + pLabel.width + engine.cellWidth, pLabel.y + labelHalfHeight);
                graphics.drawLine(pLabel.x + pLabel.width + engine.cellWidth, pLabel.y + labelHalfHeight, pLabel.x + pLabel.width, pLabel.y + labelHalfHeight);
            }
        }
        ImageIO.write(img, "png", new File(imageTarget, name));
        BufferedImage generated = ImageIO.read(new File(imageTarget, name));
        BufferedImage expected = ImageIO.read(new File("src/test/resources/expectedImages", name));
        assertTrue(name, compareImages(expected, generated));
    }

    boolean compareImages(BufferedImage i1, BufferedImage i2) {
        return compareDataBuffers(i1.getData().getDataBuffer(), i2.getData().getDataBuffer());
    }

    boolean compareDataBuffers(DataBuffer d1, DataBuffer d2) {
        if (d1.getSize() != d2.getSize()) {
            return false;
        }
        int size = d1.getSize();
        for (int i = 0; i < size; i++) {
            int p1 = d1.getElem(i);
            int p2 = d2.getElem(i);
            if (p1 != p2) {
                return false;
            }
        }
        return true;
    }
}
