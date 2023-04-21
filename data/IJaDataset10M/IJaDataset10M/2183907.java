package version12;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class Ball {

    /**
     * Poprzednie polozenie pileczki. Uzycie przy fizyce ruchu
     */
    private Point2D poprzedniePolozenie;

    /**
     * aktualne polozenie pileczki
     */
    private Point2D polozenie;

    /**
     * obrazek elementu
     */
    private BufferedImage img = null;

    /**
     * krawedz 'x'
     */
    private int boundsX;

    /**
     * krawedz 'y'
     */
    private int boundsY;

    /**
     * opoznienie ruchu
     */
    private int a;

    /**
     * zmienne odpowiedzialne za ustalanie predkosci v pileczki
     */
    private double sinus;

    private double cosinus;

    private double vx;

    private double vy;

    private double v;

    /**
     * flaga okreslajaca czy pilka jest w dolku
     */
    private boolean inTheHole = false;

    /**
     * stala, okreslajaca jak czesto odbywa sie odrysowywanie
     */
    private static final double TIME = 0.1;

    /**
     * sta�a okreslajaca przeszkode
     */
    private boolean obstacle = false;

    /**
     * Konstuktor klasy <code>Ball</code>
     * @param x polozenie 'x' pileczki
     * @param y polozenie 'y' pileczki
     */
    public Ball(int x, int y) {
        polozenie = new Point2D.Double((double) x, (double) y);
        poprzedniePolozenie = new Point2D.Double((double) x, (double) y);
    }

    /**
     * Metoda ustawiajaca krawedz.
     * @param i dlugosc krawedzi 'x'
     */
    public void setBoundsX(int i) {
        boundsX = i;
    }

    /**
     * Metoda ustawiajaca krawedz
     * @param i dlugosc krawedzi 'y'
     */
    public void setBoundsY(int i) {
        boundsY = i;
    }

    /**
     * metoda zwracajaca polozenie 'y'
     * @return polozenie 'y'
     */
    public void setXY(int x, int y) {
        polozenie.setLocation(x, y);
    }

    /**
     * metoda zwracaja polozenie 'x'
     * @return polozenie 'x'
     */
    public double getX() {
        return polozenie.getX();
    }

    /**
     * metoda zwracajaca polozenie 'y'
     * @return polozenie 'y'
     */
    public double getY() {
        return polozenie.getY();
    }

    /**
     * Zaladowanie obrazka pileczki
     * @return obrazek, jesli istnieje
     * gdy nie znaleziono - odpowiedni komunikat
     */
    public BufferedImage getBallImage() {
        URL url = null;
        try {
            url = getClass().getClassLoader().getResource("res/pilka.gif");
            img = ImageIO.read(url);
            return img;
        } catch (java.io.IOException e) {
            System.out.println("Obrazek nieznaleziony");
            return null;
        }
    }

    /**
     * ustalenie wektora predkosci
     * @param mouse polozenie kursora myszki
     */
    public void setV(Point mouse) {
        vx = polozenie.getX() + img.getWidth() / 2 - mouse.getX();
        vy = polozenie.getY() + img.getWidth() / 2 - mouse.getY();
        v = Math.sqrt(vx * vx + vy * vy);
        sinus = vy / v;
        cosinus = vx / v;
    }

    /**
     * ustawienie predkosci
     * @param v predkosc
     */
    public void setV(double v) {
        this.v = v;
    }

    /**
     * pobieranie predkosci
     * @return predkosc
     */
    public double getV() {
        return v;
    }

    /**
     * metoda odpowiedzialna za ruch pileczki
     * @return true jesli ruch byl prawidlowy
     * false jesli ruch byl nieprawidlowy (pileczka wpadla do wody)
     * 
     * TODO: MAKU!!! JAK COS, TO TUTAJ TRZEBA DAC TE WARUNKI DLA PRZESZKODY <---------------------------------
     */
    public boolean move() {
        double dX, dY;
        double promien = getR();
        if (v >= 0.1) {
            dX = (vx * TIME - (a * TIME * TIME) / 2);
            dY = (vy * TIME - (a * TIME * TIME) / 2);
            polozenie.setLocation(polozenie.getX() + dX, polozenie.getY() + dY);
            if (a != 0 && obstacle == false) {
                if (polozenie.getX() < 0) {
                    polozenie.setLocation(0, polozenie.getY());
                    cosinus = -cosinus;
                }
                if (polozenie.getX() + promien > boundsX) {
                    polozenie.setLocation(boundsX - promien, polozenie.getY());
                    cosinus = -cosinus;
                }
                if (polozenie.getY() < 0) {
                    polozenie.setLocation(polozenie.getX(), 0);
                    sinus = -sinus;
                }
                if (polozenie.getY() - (promien) > boundsY) {
                    polozenie.setLocation(polozenie.getX(), boundsY - (promien));
                    sinus = -sinus;
                }
            }
            poprzedniePolozenie.setLocation(polozenie);
            v = v - a * TIME;
            vx = v * cosinus;
            vy = v * sinus;
        } else {
            v = 0;
        }
        return true;
    }

    public void setA() {
        a = 8;
    }

    /**
     * pobiera dlugosc promienia obiektu
     * @return dlugosc promienia
     */
    public double getR() {
        return getBallImage().getWidth() / 2 + 3;
    }

    /**
     * sprawdzenie czy pilka jest w dolku
     * @return false - pileczki nie ma w dolku
     * true - pileczka jest w dolku
     */
    public boolean isInTheHole() {
        return inTheHole;
    }

    /**
     * zmienia flage inTheHole w zaleznosci od tego czy pileczka jest w dolku, czy tez jej tam nie ma.
     * @param isInTheHole czy pileczka jest w dolku
     */
    public void setInTheHole(boolean isInTheHole) {
        this.inTheHole = isInTheHole;
    }
}
