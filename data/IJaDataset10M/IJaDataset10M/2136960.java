package org.unicolet.axl.tests;

import junit.framework.TestCase;
import org.unicolet.axl.*;
import org.xml.sax.SAXException;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created:
 * User: unicoletti
 * Date: 7:19:41 PM Oct 16, 2005
 */
public class AXLReaderTestCase extends TestCase {

    public AXLReaderTestCase(String s) {
        super(s);
    }

    public void testReader() throws Exception {
        AXLFile file = AXLReader.parse(this.getClass().getClassLoader().getResourceAsStream("test.axl"), false);
        assertNotNull("File is null", file);
        assertNotNull("Map is null", file.getMap());
        assertNotNull("Envelope is null", file.getMap().getEnvelope());
        assertNotNull("MapUnits is null", file.getMap().getMapUnits());
        assertEquals("MapUnits is meters", "meters", file.getMap().getMapUnits().getUnits());
        assertEquals("Envelope minx", "1735656.772273527", file.getMap().getEnvelope().getMinx());
        assertTrue("Workspaces are 3", (3 == file.getMap().getWorkspaces().size()));
        assertEquals("Workspace 0 path", "/home/unicoletti/Workspace/Java/maps/samples/data/CTRVshp", ((Workspace) file.getMap().getWorkspaces().values().toArray()[0]).getDirectory());
        List layers = file.getMap().getLayers();
        assertNotNull("Layers are null", layers);
        assertTrue("Not five layers", (5 == layers.size()));
        checkLayer0((Layer) layers.get(0));
        checkLayer1((Layer) layers.get(1));
        checkLayer2((Layer) layers.get(2));
        checkLayer3((Layer) layers.get(3));
        checkLayer4((Layer) layers.get(4));
        checkLocale(file);
    }

    private void checkLocale(AXLFile file) {
        Locale locale = file.getLocale();
        assertEquals("US", locale.getCountry());
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getVariant());
    }

    private void checkLayer0(Layer layer0) {
        assertEquals("Layer 0 name", "veget_a", layer0.getName());
        assertNotNull("Layer 0 dataset is null", layer0.getDataset());
        assertEquals("Layer 0 dataset's type", "polygon", layer0.getDataset().getType());
        assertNull("Layer 0 lineSymbol is not null", layer0.getLineSymbol());
        assertNull("Layer 0 markerSymbol is not null", layer0.getMarkerSymbol());
        assertNotNull("Layer 0 polySymbol is null", layer0.getPolygonSymbol());
        assertEquals("Polygon boundarytransparency", "1.0", layer0.getPolygonSymbol().getBoundarytransparency());
        assertEquals("Polygon fillcolor", "255,200,0", layer0.getPolygonSymbol().getFillcolor());
        assertNotNull("LabelRendererer is null", layer0.getLabelRenderer());
        assertEquals("LabelRenderer for PERIMETR", "PERIMETR", layer0.getLabelRenderer().getField());
    }

    private void checkLayer1(Layer layer1) {
        assertEquals("Layer 1 name", "infras_a", layer1.getName());
        assertEquals("Layer 1 maxscale", "1:17999999996", layer1.getMaxscale());
        assertNotNull("Layer 1 dataset is null", layer1.getDataset());
        assertEquals("Layer 1 dataset's type", "polygon", layer1.getDataset().getType());
        assertNull("Layer 1 lineSymbol is not null", layer1.getLineSymbol());
        assertNull("Layer 1 markerSymbol is not null", layer1.getMarkerSymbol());
        assertNotNull("Layer 1 polySymbol is null", layer1.getPolygonSymbol());
        assertEquals("Polygon boundarytransparency", "1.0", layer1.getPolygonSymbol().getBoundarytransparency());
        assertEquals("Polygon fillcolor", "27,27,27", layer1.getPolygonSymbol().getFillcolor());
    }

    private void checkLayer2(Layer layer2) {
        assertEquals("Layer 2 name", "vegetaz", layer2.getName());
        assertNotNull("Layer 2 dataset is null", layer2.getDataset());
        assertEquals("Layer 2 dataset's type", "line", layer2.getDataset().getType());
        assertNull("Layer 2 lineSymbol is not null", layer2.getLineSymbol());
        assertNull("Layer 2 markerSymbol is not null", layer2.getMarkerSymbol());
        assertNull("Layer 2 polySymbol is not null", layer2.getPolygonSymbol());
        assertNotNull("Layer 2 valueMapRenderer is null", layer2.getValueMapRenderer());
        assertNotNull("Layer 2 valueMapRenderer.range is null", layer2.getValueMapRenderer().getRanges());
        Range range0 = (Range) layer2.getValueMapRenderer().getRanges().get(0);
        assertNotNull("Layer 2 valueMapRenderer.range.polysymbol is null", range0.getPolygonSymbol());
        assertEquals("Layer2 vmrenderer.lookupfield", "AREA", layer2.getValueMapRenderer().getLookupfield());
        assertEquals("Layer 2 range.label", "Less than 2045181,0988", range0.getLabel());
        assertEquals("255,255,0", range0.getPolygonSymbol().getFillcolor());
    }

    private void checkLayer3(Layer layer) {
        assertEquals("Layer 3 name", "Fognatura", layer.getName());
        assertNotNull("Layer 3 dataset is null", layer.getDataset());
        assertEquals("Layer 3 dataset's type", "line", layer.getDataset().getType());
        assertNull("Layer 3 lineSymbol is not null", layer.getLineSymbol());
        assertNull("Layer 3 markerSymbol is not null", layer.getMarkerSymbol());
        assertNull("Layer 3 polySymbol is not null", layer.getPolygonSymbol());
        assertNotNull("Layer 3 valueMapRenderer is null", layer.getValueMapRenderer());
        assertEquals("LAYER", layer.getValueMapRenderer().getLookupfield());
        assertTrue("Layer 3 Ranges is not empty", layer.getValueMapRenderer().getRanges().isEmpty());
        assertTrue("Layer 3 Exact is empty", !layer.getValueMapRenderer().getExacts().isEmpty());
        Exact exact = (Exact) layer.getValueMapRenderer().getExacts().get(0);
        assertNotNull("Layer 3 exact is null", exact);
        assertNotNull("Layer 3 exact.linesymbol is null", exact.getLineSymbol());
    }

    private void checkLayer4(Layer layer) {
        assertEquals("Layer 4 name", "sim_idro", layer.getName());
        assertNotNull("Layer 4 dataset is null", layer.getDataset());
        assertEquals("Layer 4 dataset's type", "point", layer.getDataset().getType());
        assertNull("Layer 4 lineSymbol is not null", layer.getLineSymbol());
        assertNotNull("Layer 4 markerSymbol is null", layer.getMarkerSymbol());
        assertNull("Layer 4 polySymbol is not null", layer.getPolygonSymbol());
        assertNull("Layer 4 valueMapRenderer is null", layer.getValueMapRenderer());
        assertEquals("255,0,0", layer.getMarkerSymbol().getColor());
    }

    public void testClassHierarchy() throws IOException, SAXException {
        AXLFile file = AXLReader.parse(this.getClass().getClassLoader().getResourceAsStream("test.axl"), false);
        assertNotNull(file.getMap().getFile());
        assertTrue("File is not the same", file == file.getMap().getFile());
        assertNotNull("Layer 0 poly symbol", ((Layer) file.getMap().getLayers().get(0)).getPolygonSymbol().getLayer());
        assertNotNull("Layer 0 label renderer", ((Layer) file.getMap().getLayers().get(0)).getLabelRenderer().getLayer());
        assertNotNull("Layer 0 label renderer.textsymbol", ((Layer) file.getMap().getLayers().get(0)).getLabelRenderer().getTextSymbol().getLayer());
        assertNotNull("Layer 3 valueMapRenderer", ((Layer) file.getMap().getLayers().get(3)).getValueMapRenderer().getLayer());
        assertNotNull("Layer 3 valueMapRenderer.exact[0].lineSymbol", ((Exact) ((Layer) file.getMap().getLayers().get(3)).getValueMapRenderer().getExacts().get(0)).getLineSymbol().getLayer());
    }

    public void testNumberFormat() throws IOException, SAXException {
        AXLFile file = AXLReader.parse(this.getClass().getClassLoader().getResourceAsStream("test.axl"), false);
        try {
            file.getNumberFormat().parse("1.0");
        } catch (ParseException e) {
            fail("[US] Error while parsing correct US number");
        }
        try {
            file.getNumberFormat().parse("1,0");
        } catch (ParseException e) {
            System.out.println("[US] Correcly failed to parse IT number");
        }
        Locale locale = new Locale();
        locale.setCountry("IT");
        locale.setLanguage("it");
        file.setLocale(locale);
        try {
            file.getNumberFormat().parse("1,0");
        } catch (ParseException e) {
            fail("[IT] Error while parsing correct IT number");
        }
        try {
            file.getNumberFormat().parse("1.0");
        } catch (ParseException e) {
            System.out.println("[IT] Correcly failed to parse US number");
        }
    }

    public void testNumberOutputFormat() throws IOException, SAXException, ParseException {
        AXLFile file = AXLReader.parse(this.getClass().getClassLoader().getResourceAsStream("test.axl"), false);
        assertEquals(file.getMap().getEnvelope().getMinx(), file.getMap().getEnvelope().getMinxAsNumber());
    }

    public void testNumberZero() throws IOException, SAXException, ParseException {
        AXLFile file = AXLReader.parse(this.getClass().getClassLoader().getResourceAsStream("test.axl"), false);
        assertTrue("Zero is not zero", (0 == file.getNumberFormat().parse("0.0").intValue()));
    }

    public void testImageWorkspace() throws IOException, SAXException {
        AXLFile file = AXLReader.parse(this.getClass().getClassLoader().getResourceAsStream("test.axl"), false);
        assertTrue("3 Workspaces", file.getMap().getWorkspaces().values().size() == 3);
        Workspace ws = (Workspace) file.getMap().getWorkspaces().get("shp_ws-33");
        assertEquals("/home/unicoletti/Workspace/Java/maps/samples/data/ortofoto", ws.getDirectory());
    }

    public void testAxlWithMultipleSymbols() throws IOException, SAXException {
        AXLFile file = AXLReader.parse(this.getClass().getClassLoader().getResourceAsStream("test2.axl"), false);
        assertTrue("2 Workspaces", file.getMap().getWorkspaces().values().size() == 2);
        Layer l = (Layer) file.getMap().getLayers().get(0);
        assertNotNull("ValueMapRenderer for layer 0 is null", l.getValueMapRenderer());
        assertTrue("3 exacts expected, got " + l.getValueMapRenderer().getExacts().size(), l.getValueMapRenderer().getExacts().size() == 3);
        Iterator i = l.getValueMapRenderer().getExacts().iterator();
        while (i.hasNext()) {
            Exact exact = (Exact) i.next();
            if (exact.getValue() == "5") {
                assertTrue("Exact=5 has 4 symbols", exact.getSymbols().size() == 4);
            }
        }
    }

    public void testAxlGroupRendererWithMultipleSymbols() throws IOException, SAXException {
        AXLFile file = AXLReader.parse(this.getClass().getClassLoader().getResourceAsStream("test2.axl"), false);
        assertTrue("2 Workspaces", file.getMap().getWorkspaces().values().size() == 2);
        Layer l = (Layer) file.getMap().getLayers().get(2);
        assertTrue("2 symbols expected, got " + l.getSymbols().size(), l.getSymbols().size() == 2);
    }
}
