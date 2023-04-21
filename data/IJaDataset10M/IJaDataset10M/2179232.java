package arqueologia.view;

import arqueologia.model.Area;
import arqueologia.model.MapaXML;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class MiniMapa extends JPanel {

    private int cuadriculaMiniMapa;

    private int anchoMiniMapa;

    private int cuadriculaMiniMapaOriginal;

    private int anchoMiniMapaOriginal;

    private MapaXML mapaXml;

    private JPanel jpCuadricula;

    private JPanel jpComponentes;

    private JPanel jpAreas;

    private JLayeredPane jlPane;

    private boolean anchoArea;

    private boolean mostrarAreas;

    private ArrayList<Point> locations;

    private boolean fondoTransparente;

    public MiniMapa(int ancho, MapaXML mapaXml, boolean anchoArea) {
        this.locations = new ArrayList<Point>();
        this.anchoArea = anchoArea;
        this.mapaXml = mapaXml;
        jpCuadricula = new JPanel();
        jpComponentes = new JPanel();
        jpAreas = new JPanel();
        jlPane = new JLayeredPane();
        jpComponentes.setLayout(null);
        jpComponentes.setOpaque(false);
        jpAreas.setLayout(null);
        jpAreas.setOpaque(false);
        jpCuadricula.setOpaque(false);
        jpComponentes.setOpaque(false);
        jlPane.setLocation(0, 0);
        jpCuadricula.setLocation(0, 0);
        jpComponentes.setLocation(0, 0);
        jpAreas.setLocation(0, 0);
        anchoMiniMapaOriginal = 0;
        cuadriculaMiniMapaOriginal = 0;
        cambiarAncho(ancho);
        dibujarCuadricula();
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        jlPane.add(jpCuadricula, 2);
        jlPane.add(jpAreas, 0);
        jlPane.add(jpComponentes, 1);
        this.add(jlPane, BorderLayout.CENTER);
        ocultarAreas();
    }

    public void fondoTransparente(boolean si_no) {
        this.fondoTransparente = si_no;
    }

    public void resize(float porcentaje) {
        int ancho;
        if (anchoArea) {
            ancho = Math.round(cuadriculaMiniMapaOriginal * porcentaje);
        } else {
            ancho = Math.round(anchoMiniMapaOriginal * porcentaje);
        }
        cambiarAncho(ancho);
        resizeSquares();
        resizeCuadricula();
    }

    private void cambiarAncho(int ancho) {
        if (anchoArea) {
            this.anchoMiniMapa = Math.round(mapaXml.getLargoPixeles() / (float) mapaXml.getCuadriculaPixeles()) * ancho;
            cuadriculaMiniMapa = ancho;
        } else {
            this.anchoMiniMapa = ancho;
            cuadriculaMiniMapa = Math.round(((float) ancho / mapaXml.getLargoPixeles()) * mapaXml.getCuadriculaPixeles());
        }
        if (anchoMiniMapaOriginal == 0 && cuadriculaMiniMapaOriginal == 0) {
            anchoMiniMapaOriginal = anchoMiniMapa;
            cuadriculaMiniMapaOriginal = cuadriculaMiniMapa;
        }
        jpComponentes.setSize(anchoMiniMapa, anchoMiniMapa);
        jpCuadricula.setSize(anchoMiniMapa, anchoMiniMapa);
        jpAreas.setSize(anchoMiniMapa, anchoMiniMapa);
        jlPane.setSize(anchoMiniMapa, anchoMiniMapa);
        this.setSize(anchoMiniMapa, anchoMiniMapa);
    }

    private void resizeSquares() {
        Component[] squares = jpComponentes.getComponents();
        for (int i = 0; i < squares.length; i++) {
            squares[i].setSize(cuadriculaMiniMapa - 1, cuadriculaMiniMapa - 1);
            squares[i].setLocation(locations.get(i).x * cuadriculaMiniMapa, locations.get(i).y * cuadriculaMiniMapa);
        }
    }

    public void addSquare(int i, int j, JComponent jcSquare) {
        int x = i * cuadriculaMiniMapa;
        int y = j * cuadriculaMiniMapa;
        int w = cuadriculaMiniMapa;
        int h = cuadriculaMiniMapa;
        jcSquare.setSize(w - 1, h - 1);
        jcSquare.setLocation(x, y);
        jpComponentes.add(jcSquare);
        locations.add(new Point(i, j));
    }

    public void clearSquares() {
        jpComponentes.removeAll();
        jpComponentes.repaint();
    }

    private void resizeCuadricula() {
        Component[] tiles = jpCuadricula.getComponents();
        try {
            BufferedImage tile = ImageIO.read(new File(mapaXml.getRutaNivel(0)));
            tile = Resize.image(tile, Math.round(tile.getWidth() * ((float) cuadriculaMiniMapa / mapaXml.getCuadriculaPixeles())), Math.round(tile.getWidth() * ((float) cuadriculaMiniMapa / mapaXml.getCuadriculaPixeles())));
            Graphics g = tile.getGraphics();
            g.setColor(Color.BLACK);
            g.drawLine(tile.getWidth() - 1, 0, tile.getWidth() - 1, tile.getHeight());
            g.drawLine(0, tile.getHeight() - 1, tile.getWidth() - 1, tile.getHeight() - 1);
            ImageIcon img = new ImageIcon(tile);
            for (int i = 0; i < tiles.length; i++) {
                JLabel jlTile = (JLabel) tiles[i];
                jlTile.setIcon(img);
            }
        } catch (Exception e) {
        }
    }

    private void dibujarCuadricula() {
        int Max = Math.round((float) anchoMiniMapa / cuadriculaMiniMapa);
        jpCuadricula.setLayout(new GridLayout(Max, Max));
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        try {
            BufferedImage tile = ImageIO.read(new File(mapaXml.getRutaNivel(0)));
            tile = Resize.image(tile, Math.round(tile.getWidth() * ((float) cuadriculaMiniMapa / mapaXml.getCuadriculaPixeles())), Math.round(tile.getWidth() * ((float) cuadriculaMiniMapa / mapaXml.getCuadriculaPixeles())));
            Graphics g = tile.getGraphics();
            g.setColor(Color.BLACK);
            g.drawLine(tile.getWidth() - 1, 0, tile.getWidth() - 1, tile.getHeight());
            g.drawLine(0, tile.getHeight() - 1, tile.getWidth() - 1, tile.getHeight() - 1);
            ImageIcon img = new ImageIcon(tile);
            for (int i = 0; i < Max * Max; i++) {
                JLabel jlFondo = new JLabel(img);
                jpCuadricula.add(jlFondo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dibujarFondo(BufferedImage imagen) {
        Graphics g = imagen.getGraphics();
        try {
            BufferedImage tile = ImageIO.read(new File(mapaXml.getRutaNivel(0)));
            tile = Resize.image(tile, Math.round(tile.getWidth() * ((float) cuadriculaMiniMapa / mapaXml.getCuadriculaPixeles())), Math.round(tile.getWidth() * ((float) cuadriculaMiniMapa / mapaXml.getCuadriculaPixeles())));
            int iMax = Math.round((float) anchoMiniMapa / tile.getWidth());
            int jMax = Math.round((float) anchoMiniMapa / tile.getWidth());
            int w = tile.getWidth();
            int h = tile.getHeight();
            for (int i = 0; i < iMax; i++) {
                for (int j = 0; j < jMax; j++) {
                    g.drawImage(tile, i * w, j * h, null);
                }
            }
        } catch (Exception e) {
        }
    }

    public Point[][] mostrarAreas() {
        BufferedImage c = new BufferedImage(anchoMiniMapa + 1, anchoMiniMapa + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = c.getGraphics();
        g.setColor(Color.YELLOW);
        JLabel jlAreas = new JLabel(new ImageIcon(c));
        jlAreas.setSize(anchoMiniMapa + 1, anchoMiniMapa + 1);
        jlAreas.setLocation(0, 0);
        float factor = (float) cuadriculaMiniMapa / mapaXml.getCuadriculaPixeles();
        Area[] areas = mapaXml.getAreas();
        Point[][] indices = new Point[areas.length][];
        for (int i = 0; i < areas.length; i++) {
            Area temp = new Area(areas[i].getX(), areas[i].getY(), areas[i].getAncho(), areas[i].getLargo());
            temp.transformar(mapaXml.getEscala());
            temp.transformar(factor);
            indices[i] = obtenerIndices(temp, g);
        }
        jpAreas.setVisible(true);
        jpAreas.add(jlAreas);
        return indices;
    }

    public void ocultarAreas() {
        jpAreas.setVisible(false);
        jpAreas.removeAll();
    }

    private Point[] obtenerIndices(Area area, Graphics g) {
        ArrayList<Point> indices = new ArrayList<Point>();
        int xMin = area.getX();
        int xMax = area.getX() + area.getAncho();
        int yMin = area.getY();
        int yMax = area.getY() + area.getLargo();
        int iMin = 0, iMax = 0, jMin = 0, jMax = 0;
        for (int i = 0; i < Math.round((float) anchoMiniMapa / cuadriculaMiniMapa); i++) {
            if (xMin >= i * cuadriculaMiniMapa) {
                iMin = i;
            }
            if (yMin >= i * cuadriculaMiniMapa) {
                jMin = i;
            }
            if (xMax > i * cuadriculaMiniMapa) {
                iMax = i;
            }
            if (yMax > i * cuadriculaMiniMapa) {
                jMax = i;
            }
        }
        g.drawRect(iMin * cuadriculaMiniMapa, jMin * cuadriculaMiniMapa, (iMax - iMin + 1) * cuadriculaMiniMapa, (jMax - jMin + 1) * cuadriculaMiniMapa);
        for (int i = iMin; i <= iMax; i++) {
            for (int j = jMin; j <= jMax; j++) {
                indices.add(new Point(i, j));
            }
        }
        Point[] temp = new Point[indices.size()];
        indices.toArray(temp);
        return temp;
    }

    public int getTamanoCuadricula() {
        return cuadriculaMiniMapa;
    }
}
