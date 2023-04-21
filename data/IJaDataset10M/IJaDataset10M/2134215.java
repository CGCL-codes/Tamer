package Logika;

import bluetooth.Client;
import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author mejcu
 */
public class Ekran extends Canvas {

    private Sprite przeszkoda = null;

    private Sprite[] czolg = new Sprite[4];

    private Sprite pocisk = null;

    Image gameOver;

    private static int[] rozkladSprite = { 0, 1, 2, 3, 4, 5, 6, 7 };

    private int[][] rozkladEkran = new int[Zmienne.SZEROKOSC][Zmienne.WYSOKOSC];

    private int postac, kierunek;

    private Client client;

    private long lastRequestTime = 0;

    private int currentDirection = Zmienne.TURN_NORTH;

    private int[] values = new int[3];

    private Tank[] tank_table = new Tank[5];

    private int ME = -1;

    private boolean first_time = true;

    int blockX, blockY;

    public boolean gameOverBool = false;

    private int clientCounter = 0;

    private Sprite life;

    Acc cT;

    public Ekran(Client _client, Acc _cT) {
        cT = _cT;
        client = _client;
        try {
            setFullScreenMode(true);
            blockX = getWidth() / 12;
            blockY = getHeight() / 16;
            life = new Sprite(getScaledImage(Image.createImage("/pics/bonus_life.png"), blockX, blockY), blockX, blockY);
            przeszkoda = new Sprite(getScaledImage(Image.createImage("/pics/background.png"), 8 * blockX, blockY), blockX, blockY);
            for (int i = 0; i < czolg.length; i++) {
                czolg[i] = new Sprite(getScaledImage(Image.createImage("/pics/8_" + (i + 1) + ".png"), blockX, blockY), blockX, blockY);
            }
            pocisk = new Sprite(getScaledImage(Image.createImage("/pics/missile.png"), blockX, blockY), blockX, blockY);
            przeszkoda.setFrameSequence(rozkladSprite);
            gameOver = getScaledImage(Image.createImage("/pics/game_over.png"), 200 * getWidth() / 240, 53 * getHeight() / 320);
        } catch (IOException e) {
            System.err.println("Niestety wystapił bład podczas inicjalizacji aplikacji");
            System.exit(-1);
        }
        for (int i = 0; i < 4; i++) {
            tank_table[i] = new Tank();
        }
        Thread painter = new Thread() {

            public void run() {
                while (true) {
                    repaint();
                    try {
                        sleep(50);
                    } catch (InterruptedException ex) {
                        System.out.println("server sleep");
                    }
                }
            }
        };
        painter.start();
    }

    public Image getScaledImage(Image img, float newW, float newH) {
        float oldH = img.getHeight(), oldW = img.getWidth();
        int[] sourceData = new int[(int) (oldH * oldW)];
        img.getRGB(sourceData, 0, (int) oldW, 0, 0, (int) oldW, (int) oldH);
        int[] outData = new int[(int) (newH * newW)];
        for (float i = 0; i < newH; i++) {
            for (float j = 0; j < newW; j++) {
                outData[(int) (i * newW + j)] = sourceData[(int) (i * oldH / newH) * (int) oldW + (int) (j * oldW / newW)];
            }
        }
        Image temp = Image.createRGBImage(outData, (int) newW, (int) newH, true);
        sourceData = null;
        outData = null;
        return temp;
    }

    public void update(int x, int y, int z) {
        if (x >= 15) {
            keyPressed(-4);
        }
        if (x <= -15) {
            keyPressed(-3);
        }
        if (y >= 15) {
            keyPressed(-2);
        }
        if (y <= -15) {
            keyPressed(-1);
        }
    }

    int tankCounter;

    public void paint(Graphics g) {
        try {
            if (rozkladEkran[tank_table[ME].zwrocPolozenieX()][tank_table[ME].zwrocPolozenieY()] == 0 || rozkladEkran[tank_table[ME].zwrocPolozenieX()][tank_table[ME].zwrocPolozenieY()] == 4) {
                gameOverBool = true;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("ArrayIndexOutOfBoundsException");
        }
        tankCounter = 0;
        tank_table[0].toString();
        for (int i = 0; i < Zmienne.WYSOKOSC; i++) {
            for (int j = 0; j < Zmienne.SZEROKOSC; j++) {
                if (rozkladEkran[j][i] > 100) {
                    values = getObjectDescription(rozkladEkran[j][i]);
                    postac = values[2];
                } else {
                    postac = rozkladEkran[j][i];
                }
                switch(postac) {
                    case Zmienne.TANK:
                        setDirection(values[1], czolg[values[0] - 1]);
                        czolg[values[0] - 1].setPosition(i * blockX, j * blockY);
                        czolg[values[0] - 1].paint(g);
                        tankCounter++;
                        break;
                    case Zmienne.BONUS_LIFE + Zmienne.WOOD:
                        life.setPosition(i * blockX, j * blockY);
                        life.paint(g);
                        przeszkoda.setFrame(Zmienne.TANK_IN_WOOD);
                        przeszkoda.setPosition(i * blockX, j * blockY);
                        przeszkoda.paint(g);
                        break;
                    case Zmienne.BONUS_LIFE:
                        life.setPosition(i * blockX, j * blockY);
                        life.paint(g);
                        break;
                    case Zmienne.TANK_IN_WOOD:
                        setDirection(values[1], czolg[values[0] - 1]);
                        przeszkoda.setFrame(postac);
                        czolg[values[0] - 1].setPosition(i * blockX, j * blockY);
                        czolg[values[0] - 1].paint(g);
                        przeszkoda.setPosition(i * blockX, j * blockY);
                        przeszkoda.paint(g);
                        tankCounter++;
                        break;
                    default:
                        if (postac > 18) {
                            setDirection(postac / 10, pocisk);
                            pocisk.setPosition(i * blockX, j * blockY);
                            pocisk.paint(g);
                        } else {
                            przeszkoda.setFrame(postac);
                            przeszkoda.setPosition(i * blockX, j * blockY);
                            przeszkoda.paint(g);
                        }
                        break;
                }
            }
        }
        g.fillRect(blockX * 12, 0, getWidth() - blockX * 12, getHeight());
        g.fillRect(0, blockY * 16, getWidth(), getHeight() - blockY * 16);
        if (gameOverBool) {
            g.drawImage(gameOver, gameOverX, gameOverY, 0);
            if (gameOverX > 0 && gameOverX < (getWidth() - gameOver.getWidth())) {
                gameOverX += gameOverDX;
            } else {
                gameOverDX = -gameOverDX;
                gameOverX += gameOverDX;
            }
            if (gameOverY > 0 && gameOverY < getHeight() - gameOver.getHeight()) {
                gameOverY += gameOverDY;
            } else {
                gameOverDY = -gameOverDY;
                gameOverY += gameOverDY;
            }
        }
    }

    int gameOverX = 1, gameOverY = 1, gameOverDX = 1, gameOverDY = 2;

    public synchronized void keyPressed(int key) {
        System.out.println(key);
        String komunikat = "";
        switch(getGameAction(key)) {
            case Canvas.LEFT:
                komunikat += (char) Zmienne.TURN_WEST;
                break;
            case Canvas.RIGHT:
                komunikat += (char) Zmienne.TURN_EAST;
                break;
            case Canvas.DOWN:
                komunikat += (char) Zmienne.TURN_SOUTH;
                break;
            case Canvas.UP:
                komunikat += (char) Zmienne.TURN_NORTH;
                break;
            case Canvas.FIRE:
                komunikat += (char) Zmienne.FIRE;
                break;
            default:
                cT.startAcc();
                komunikat += (char) Zmienne.FIRE;
                break;
        }
        if (checkTimeInterval()) {
            client.sendData(komunikat);
        } else {
            System.out.println("Teraz nic nie wysylam");
        }
    }

    public void dekodowanie(String komunikat) {
        if (komunikat.length() > 7) {
            initialize(komunikat);
        } else {
            rozkladEkran[(int) komunikat.charAt(0)][(int) komunikat.charAt(1)] = (int) komunikat.charAt(2);
            if ((int) komunikat.charAt(2) > 100) {
                values = getObjectDescription((int) komunikat.charAt(2));
                tank_table[values[0]].setX((int) komunikat.charAt(0));
                tank_table[values[0]].setY((int) komunikat.charAt(1));
                tank_table[values[0]].pobierzZwrot(values[1]);
            }
            if (komunikat.length() == 6) {
                rozkladEkran[(int) komunikat.charAt(3)][(int) komunikat.charAt(4)] = (int) komunikat.charAt(5);
            }
        }
    }

    private void initialize(String data) {
        System.out.println("Ilosc wyslanego komunikatu " + data.length());
        int k = 0;
        for (int i = 0; i < Zmienne.SZEROKOSC; i++) {
            for (int j = 0; j < Zmienne.WYSOKOSC; j++) {
                rozkladEkran[i][j] = (int) data.charAt(k++);
                if ((int) data.charAt(2) > 100) {
                    values = getObjectDescription((int) data.charAt(2));
                    tank_table[values[0]].setX((int) data.charAt(0));
                    tank_table[values[0]].setY((int) data.charAt(1));
                    tank_table[values[0]].pobierzZwrot(values[1]);
                }
            }
        }
        if (data.length() == 196) {
            clientCounter++;
            int polozenieX = (int) data.charAt(k++);
            int polozenieY = (int) data.charAt(k++);
            int direction = (int) data.charAt(k++);
            int whichInstance = (int) data.charAt(k);
            tank_table[whichInstance].setX(polozenieX);
            tank_table[whichInstance].setY(polozenieY);
            tank_table[whichInstance].pobierzZwrot(direction);
            if (first_time) {
                ME = whichInstance;
                first_time = false;
            }
        }
    }

    private void setDirection(int direction, Sprite sprite) {
        switch(direction) {
            case Zmienne.TURN_NORTH:
                sprite.setTransform(Sprite.TRANS_NONE);
                break;
            case Zmienne.TURN_EAST:
                sprite.setTransform(Sprite.TRANS_ROT90);
                break;
            case Zmienne.TURN_SOUTH:
                sprite.setTransform(Sprite.TRANS_ROT180);
                break;
            case Zmienne.TURN_WEST:
                sprite.setTransform(Sprite.TRANS_ROT270);
                break;
        }
    }

    private boolean checkTimeInterval() {
        long diff;
        diff = System.currentTimeMillis() - lastRequestTime;
        if (diff > Zmienne.SLEEP_INTERVAL) {
            lastRequestTime = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    private int[] getObjectDescription(int value) {
        values[0] = value / 100;
        int temp_value = value / 10;
        values[1] = temp_value % 10;
        values[2] = value % 10;
        return values;
    }
}
