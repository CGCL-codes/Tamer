package xunome.graphics;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import xunome.MultiPlayerForm;
import xunome.xUnoME;

public class Menu extends GameCanvas implements Runnable {

    private int atualOption = 0;

    private String[][][] options = { { { "single", "multi", "help", "exit" }, { "2p", "3p", "4p", "cback" } }, { { "Singleplayer", "Multiplayer", "Help", "Exit" }, { "2 Players", "3 Players", "4 Players", "Back" } } };

    private boolean running = false;

    private final int SCREEN_TYPE;

    private Thread t;

    private xUnoME mid;

    private final int type;

    public static final int MAIN = 0;

    public static final int PLAYERS = 1;

    public Menu(int type, xUnoME mid) {
        super(true);
        setFullScreenMode(true);
        this.mid = mid;
        this.type = type;
        if ((getWidth() >= 220) && (getHeight() >= 300)) {
            SCREEN_TYPE = 0;
        } else {
            SCREEN_TYPE = 1;
        }
    }

    public void paint(Graphics g) {
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (SCREEN_TYPE == 0) {
            try {
                Image header = Image.createImage("/res/title.png");
                g.drawImage(header, getWidth() / 2, 40, Graphics.HCENTER | Graphics.TOP);
                for (int o = 0; o < options[0][type].length; o++) {
                    Image optionSrc = Image.createImage("/res/" + options[0][type][o] + ".png");
                    g.drawImage(optionSrc, getWidth() / 2, 40 * (o + 1) + 90, Graphics.HCENTER | Graphics.VCENTER);
                }
                Image selection = Image.createImage("/res/backcard.png");
                g.drawImage(selection, getWidth() / 2 - 80, 40 * (getAtualOption() + 1) + 90, Graphics.HCENTER | Graphics.VCENTER);
            } catch (IOException io) {
                System.out.println("Error in redenring main menu.");
            }
        } else {
            g.setColor(255, 255, 255);
            int x = getWidth() / 2;
            g.drawString("xUNO ME", x, 5, Graphics.HCENTER | Graphics.TOP);
            for (int o = 0; o < options[1][type].length; o++) {
                g.setColor(255, 0, 0);
                if (o == atualOption) {
                    g.setColor(255, 255, 255);
                }
                g.drawString(options[1][type][o], x - 30, (o + 1) * 15 + 20, Graphics.LEFT | Graphics.TOP);
            }
            g.setColor(255, 0, 0);
            g.drawString(">", x - 40, (atualOption + 1) * 15 + 20, Graphics.LEFT | Graphics.TOP);
        }
    }

    public int getAtualOption() {
        return atualOption;
    }

    public void run() {
        while (running) {
            System.out.println("Thread run: " + type);
            int key = getKeyStates();
            if ((key & GameCanvas.DOWN_PRESSED) != 0) {
                if (atualOption < options[SCREEN_TYPE][type].length - 1) {
                    atualOption++;
                } else {
                    atualOption = 0;
                }
                repaint();
            } else if ((key & GameCanvas.UP_PRESSED) != 0) {
                if (atualOption > 0) {
                    atualOption--;
                } else {
                    atualOption = options[SCREEN_TYPE][type].length - 1;
                }
                repaint();
            } else if ((key & FIRE_PRESSED) != 0) {
                if (type == 0) {
                    switch(atualOption) {
                        case 0:
                            showMenuPlayers();
                            System.out.println("Initing Menu Players");
                            break;
                        case 1:
                            showMultiPlayerForm();
                            break;
                        case 2:
                            showHelpForm();
                            break;
                        case 3:
                            running = false;
                            mid.notifyDestroyed();
                            break;
                    }
                } else if (type == 1) {
                    switch(atualOption) {
                        case 0:
                        case 1:
                        case 2:
                            try {
                                running = false;
                                mid.createSinglePlayerGame(atualOption + 2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            running = false;
                            mid.setMain();
                            break;
                    }
                }
            }
            try {
                Thread.sleep(500L);
            } catch (InterruptedException ie) {
            }
        }
        running = false;
        t = null;
    }

    public void startListening() {
        if (!running) {
            running = true;
            t = new Thread(this);
            t.start();
        }
    }

    public void stopListening() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    protected void pointerPressed(int x, int y) {
        System.out.println("-------Pressed---------");
        if (type == 0) {
            if (x >= 60 && x <= 180 && y >= 113 && y <= 142) {
                showMenuPlayers();
            } else if (x >= 67 && x <= 175 && y >= 157 && y <= 179) {
                showMultiPlayerForm();
            } else if (x >= 97 && x <= 142 && y >= 196 && y <= 219) {
                showHelpForm();
            } else if (x >= 101 && x <= 138 && y >= 239 && y <= 262) {
                running = false;
                mid.notifyDestroyed();
            }
        } else if (type == 1) {
            if (x >= 74 && x <= 165 && y >= 117 && y <= 138) {
                running = false;
                mid.createSinglePlayerGame(2);
            } else if (x >= 75 && x <= 165 && y >= 157 && y <= 179) {
                running = false;
                mid.createSinglePlayerGame(3);
            } else if (x >= 75 && x <= 167 && y >= 196 && y <= 220) {
                running = false;
                mid.createSinglePlayerGame(4);
            } else if (x >= 96 && x <= 146 && y >= 239 && y <= 261) {
                running = false;
                mid.setMain();
            }
        }
    }

    private void showMenuPlayers() {
        running = false;
        Menu m = new Menu(PLAYERS, mid);
        m.startListening();
        mid.getLCD().setCurrent(m);
        m.repaint();
    }

    private void showMultiPlayerForm() {
        running = false;
        mid.getLCD().setCurrent(new MultiPlayerForm(mid));
    }

    private void showHelpForm() {
        running = false;
        mid.setHelp();
    }
}
