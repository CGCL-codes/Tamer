package Juego;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;
import Entidades.*;

/**
 * Clase que gestiona la Interfaz de Usuario durante el juego.
 * @author Gamelari Rude Klub
 */
public class GUI {

    private final int SPRITE_MISIL = 0;

    private final int SPRITE_VIDA = 1;

    private final int SPRITE_ASTRONAUTA = 2;

    private final int SPRITE_FUEL = 3;

    private final int SPRITE_CRISTAL = 4;

    private Sprite sprGUI;

    private Nave nave;

    private Humano humano;

    private boolean mostrarGUINave = true, mostrarVidas = true, mostrarAstronautas = false;

    private int vidasX, vidasY;

    private int fuelX, fuelY;

    private int misilesX, misilesY;

    private int astrX, astrY;

    private int anchoImg;

    private int numVidas, numAstronautas;

    private int fuelMax = 125;

    private int ancho = 50;

    private int alto = 12;

    private int cristalX, cristalY;

    /**
     * Crea una nueva instancia de GUI
     * @param x Coordenada x
     * @param y Coordenada y
     */
    public GUI(int x, int y) {
        sprGUI = new Sprite(Recursos.getImgGUI(), 9, 12);
        anchoImg = sprGUI.getWidth();
        fuelX = 3;
        fuelY = 3;
        vidasX = 0;
        vidasY = y - 12;
        astrY = y - 12;
        astrX = x;
        misilesX = x;
        misilesY = 1;
        cristalX = 3;
        cristalY = 3;
    }

    /**
     * Dibuja el GUI en pantalla.
     * @param g Contexto gráfico.
     */
    public void Dibujar(Graphics g) {
        if (mostrarGUINave) {
            int fuel = nave.GetCombustible();
            sprGUI.setFrame(SPRITE_FUEL);
            sprGUI.setPosition(fuelX, fuelY);
            sprGUI.paint(g);
            g.setColor(255, 255, 255);
            g.drawRect(fuelX + anchoImg + 3, fuelY, ancho, alto);
            int verde = 255 * fuel / fuelMax;
            g.setColor(255 - verde, verde, 0);
            g.fillRect(fuelX + anchoImg + 4, fuelY + 1, (ancho - 1) * fuel / fuelMax, alto - 1);
            sprGUI.setFrame(SPRITE_MISIL);
            int numMisiles = nave.GetNumMisiles();
            for (int i = 1; i <= numMisiles; i++) {
                sprGUI.setPosition(misilesX - i * (anchoImg + 2), misilesY);
                sprGUI.paint(g);
            }
        } else {
            if (humano.TieneCristal()) {
                sprGUI.setFrame(SPRITE_CRISTAL);
                sprGUI.setPosition(cristalX, cristalY);
                sprGUI.paint(g);
            }
        }
        if (mostrarVidas) {
            sprGUI.setFrame(SPRITE_VIDA);
            for (int i = 1; i <= numVidas; i++) {
                sprGUI.setPosition(vidasX + i * (anchoImg + 2), vidasY);
                sprGUI.paint(g);
            }
        }
        if (mostrarAstronautas) {
            sprGUI.setFrame(SPRITE_ASTRONAUTA);
            for (int i = 1; i <= numAstronautas; i++) {
                sprGUI.setPosition(astrX - i * (anchoImg + 2), astrY);
                sprGUI.paint(g);
            }
        }
    }

    /**
     * Libera la memoria.
     */
    public void Destruir() {
        sprGUI = null;
        Recursos.freeImgGUI();
    }

    /**
     * Establece el número de vidas
     * @param newNum Número de vidas
     */
    public void SetNumVidas(int newNum) {
        numVidas = newNum;
    }

    /**
     * Establece el número de astronautas.
     * @param newNum Número de astronautas
     */
    public void SetNumAstronautas(int newNum) {
        numAstronautas = newNum;
    }

    /**
     * Establece la nave.
     * @param n Nave
     */
    public void SetNave(Nave n) {
        nave = n;
    }

    /**
     * Muestra/oculta el GUI de la nave.
     * @param mostrar true - muestra, false - oculta
     */
    public void MostrarGUINave(boolean mostrar) {
        mostrarGUINave = mostrar;
    }

    /**
     * Muestra/oculta el número de vidas.
     * @param mostrar true - muestra, false - oculta
     */
    public void MostrarVidas(boolean mostrar) {
        mostrarVidas = mostrar;
    }

    /**
     * Muestra/oculta el número de astronautas.
     * @param mostrar true - muestra, false - oculta
     */
    public void MostrarAstronautas(boolean mostrar) {
        mostrarAstronautas = mostrar;
    }

    /**
     * Establece el humano.
     * @param hum Humano
     */
    public void SetHumano(Humano hum) {
        humano = hum;
    }
}
