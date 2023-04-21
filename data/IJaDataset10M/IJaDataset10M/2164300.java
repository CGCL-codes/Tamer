package pl.rdk.vision2.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/** abstrakcyjna klasa reprezentująca figury wyświetlane w teście */
public abstract class Figure {

    /** jasność 0-bez zmian, ujemna-ściemnienie, dodatnia-rozjaśnienie*/
    private int brightness;

    /**
     * Ustawia jasność obrazka
     * @param brightness nowa jasność
     * @return prostokąt do odświeżenia
     */
    public Rectangle setBrightness(int brightness) {
        if (this.brightness != brightness) {
            this.brightness = brightness;
            return getBounds();
        } else return new Rectangle(0, 0, 0, 0);
    }

    /**
     * Zwraca aktualną jasność obrazka.
     * @return jasność
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * Oblicza nowy kolor na podstawie jasności obrazka (parametr brightness)
     * @param oldColor Kolor orginalny obrazka
     * @return Nowy kolor pixela obliczony na podstawie jasności obrazka
     */
    protected Color applyBrightness(Color oldColor) {
        Color newColor = oldColor;
        if (brightness >= 0) {
            for (int i = 0; i < brightness; i++) newColor = newColor.brighter();
        } else {
            for (int i = 0; i > brightness; i--) newColor = newColor.darker();
        }
        return newColor;
    }

    /** współrzędna x lewego górnego rogu */
    public int getX() {
        return getBounds().x;
    }

    /** współrzędna y lewego górnego rogu */
    public int getY() {
        return getBounds().y;
    }

    /** szerokość (ilość pixeli) prostokąta opisanego na figurze */
    public int getWidth() {
        return getBounds().width;
    }

    /** wysokość (ilość pixeli) prostokąta opisanego na figurze */
    public int getHeight() {
        return getBounds().height;
    }

    public abstract void paintComponent(Graphics g);

    public abstract Rectangle getBounds();

    public abstract Rectangle setX(int x);

    public abstract Rectangle setY(int y);

    public abstract Rectangle setEye(int eye);

    public abstract int getEye();
}
