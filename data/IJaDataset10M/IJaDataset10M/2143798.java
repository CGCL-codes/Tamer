package manager;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Contiene metodi di supporto utili per operazioni spesso ripetute in classi
 * diverse.
 */
final class Utills {

    /**
	 * Converte una lista di ponti in un poligono.
	 * 
	 * @param points
	 *            Lista di punti.
	 * 
	 * @return Poligono che rappresenta la lista di punti.
	 */
    public static Polygon PolygonFromPoints(ArrayList<Point2D> points) {
        Polygon tempPolygon = new Polygon();
        for (Point2D i : points) {
            tempPolygon.addPoint((int) i.getX(), (int) i.getY());
        }
        return tempPolygon;
    }
}
