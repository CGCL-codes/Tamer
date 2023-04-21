package org.sourceforge.jvb3d.Loader;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.QuadArray;
import javax.vecmath.Point3d;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author �ukasz Krzy�ak
 * 
 * Implementacja fabryki tworz�cej obiekty BranchGroup na podstawie danych z
 * pliku z map�
 */
public class GraphObjectFactory {

    protected static final float wallDefaultWidth = 0.1f;

    protected static final float sceneScale = 0.1f;

    class BoxGeometry {

        GeometryArray[] geometry = new QuadArray[6];
    }

    protected static GraphObjectFactory instance = new GraphObjectFactory();

    protected static AppearanceFactory appearanceFactory = AppearanceFactory.getInstance();

    /**
	 * singleton - zwraca instancje
	 * @return instancja
	 */
    public static GraphObjectFactory getInstance() {
        return instance;
    }

    protected GraphObjectFactory() {
    }

    /**
	 * na podstawie danych z pliku mapy tworzy poszczeg�lne powierzchnie.
	 * Parsuje plik, na podstawie wsp�rz�dnych powoduje wygenerowanie geometrii,
	 * a nast�pnie przypisuje odpowiednie atrybuty dla danej powierzchni.
	 * 
	 * @param surfaceNode
	 * @return grupa zawieraj�ca wszystkie powierzchnie z pliku
	 */
    public BranchGroup newGraphObject(Node surfaceNode) {
        NodeList surfParams = surfaceNode.getChildNodes();
        BranchGroup surface = new BranchGroup();
        String surfaceID = surfaceNode.getAttributes().getNamedItem("id").getNodeValue();
        Surface surfaceData = null;
        for (int i = 0; i < surfParams.getLength(); i++) {
            if (surfParams.item(i).getNodeName() == "PlanarGeometry") {
                surfaceData = new Surface(parsePlanarGeometry(surfParams.item(i)), surfaceID);
            } else if (surfParams.item(i).getNodeName() == "Opening") {
                surfaceData.cutOpening(parseOpening(surfParams.item(i)));
            }
        }
        surfaceData.createTextureCoords();
        surface.addChild(surfaceData.createShape());
        surface.compile();
        return surface;
    }

    /**
	 * parsuje dane o pojedy�czej powierzchni i wywo�uje utworzenie odpowiednich
	 * grup dla ka�dej ze �cian powierzchni, 
	 * 
	 * @param geometry w�ze� z pliku xml opisuj�cego wsp�rz�dne
	 * @return tablica odczytanych punkt�w
	 */
    private Point3d[] parsePlanarGeometry(Node geometry) {
        NodeList vertices = geometry.getFirstChild().getChildNodes();
        Point3d[] coords = new Point3d[4];
        for (int i = 0; i < vertices.getLength(); i++) coords[i] = parseCoords(vertices.item(i));
        return coords;
    }

    private Point3d[] parseOpening(Node geometry) {
        NodeList elements = geometry.getChildNodes();
        for (int i = 0; i < elements.getLength(); i++) {
            if (elements.item(i).getNodeName() == "PlanarGeometry") return parsePlanarGeometry(elements.item(i));
        }
        return null;
    }

    private Point3d parseCoords(Node cartesianPoint) {
        NodeList coords = cartesianPoint.getChildNodes();
        float[] coordinates = new float[3];
        for (int i = 0; i < 3; i++) {
            coordinates[i] = new Float(coords.item(i).getChildNodes().item(0).getNodeValue()).floatValue();
        }
        return new Point3d(-coordinates[0], coordinates[2], coordinates[1]);
    }
}
