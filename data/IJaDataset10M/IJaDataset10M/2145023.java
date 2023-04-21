package cantro.graphics;

import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.*;
import java.io.*;
import cantro.*;
import cantro.input.*;
import cantro.networking.GameConnection;
import cantro.entity.*;
import cantro.util.GraphicsUtil;
import java.awt.image.*;

public class GameScreen extends JPanel {

    Graphics2D bG;

    BufferedImage offscreen;

    public BufferedImage viewedImage;

    public BufferedImage skeletonImage;

    public BufferedImage bulletImage;

    public Image menuImage;

    public int viewedx = 0;

    public int viewedy = 0;

    public int width;

    public int height;

    public Image[][] playerPictures;

    public Image[][] zombiePictures;

    public Cantro owner;

    public Status status;

    public ArrayList<MenuButton> menuButtons;

    public String selectedName;

    public String selectedServer;

    public ServerBrowserBox browserBox;

    public GameScreen(Cantro c) {
        menuButtons = new ArrayList<MenuButton>();
        menuButtons.add(new MenuButton(200, 200, 400, 50, "Single Player", 22));
        menuButtons.add(new MenuButton(200, 275, 400, 50, "Server Browser", 22));
        menuButtons.add(new MenuButton(200, 350, 400, 50, "Options", 22));
        status = Status.MENU;
        owner = c;
        offscreen = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        bG = offscreen.createGraphics();
        bG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bG.setColor(Color.RED);
        try {
            File f = new File("src/cantro/resources/actualMaps/GrasslandMap.PNG");
            viewedImage = ImageIO.read(f);
            f = new File("src/cantro/resources/actualMaps/GrasslandSkeleton.PNG");
            skeletonImage = ImageIO.read(f);
            f = new File("src/cantro/resources/images/bullet.PNG");
            bulletImage = ImageIO.read(f);
            f = new File("src/cantro/resources/images/menu.PNG");
            menuImage = ImageIO.read(f);
            playerPictures = new Image[2][2];
            playerPictures[0][0] = Toolkit.getDefaultToolkit().getImage("src/cantro/resources/images/sprites/CRLB.gif");
            playerPictures[0][1] = Toolkit.getDefaultToolkit().getImage("src/cantro/resources/images/sprites/CSLB.gif");
            playerPictures[1][0] = Toolkit.getDefaultToolkit().getImage("src/cantro/resources/images/sprites/CRRB.gif");
            playerPictures[1][1] = Toolkit.getDefaultToolkit().getImage("src/cantro/resources/images/sprites/CSRB.gif");
            zombiePictures = new Image[2][2];
            zombiePictures[0][0] = Toolkit.getDefaultToolkit().getImage("src/cantro/resources/images/sprites/ZRunningLeft.gif");
            zombiePictures[0][1] = Toolkit.getDefaultToolkit().getImage("src/cantro/resources/images/sprites/MSLB.gif");
            zombiePictures[1][0] = Toolkit.getDefaultToolkit().getImage("src/cantro/resources/images/sprites/ZRunningRight.gif");
            zombiePictures[1][1] = Toolkit.getDefaultToolkit().getImage("src/cantro/resources/images/sprites/MSRB.gif");
        } catch (Exception e) {
            e.printStackTrace();
        }
        width = skeletonImage.getWidth();
        height = skeletonImage.getHeight();
        Player.ylim = skeletonImage.getHeight();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (status == Status.MENU) {
            bG.drawImage(menuImage, 0, 0, null);
            drawMenuButtons(bG);
        }
        if (status == Status.MENUSERVERBROWSER) {
            bG.drawImage(menuImage, 0, 0, null);
            drawMenuButtons(bG);
            bG.setColor(Color.RED);
            int[] pos = GraphicsUtil.centeringText(selectedName + " : " + selectedServer, bG.getFont(), 800, 1, bG);
            bG.drawString(selectedName + " : " + selectedServer, pos[0], 450);
        } else if (status == Status.SINGLEPLAYERGAME || status == Status.MULTIPLAYERGAME) {
            bG.drawImage(viewedImage.getSubimage(viewedx, viewedy, 800, 600), 0, 0, null);
            drawPlayer(owner.p, bG);
            for (Player p : Player.players) {
                drawPlayer(p, bG);
            }
            for (Zombie z : Zombie.zombies) {
                drawZombie(z, bG);
            }
            synchronized (Particle.particles) {
                for (Particle p : Particle.particles) {
                    drawParticle(p, bG);
                }
            }
            paintRedLine(owner.p, bG);
            if ((owner.p.x - 800 - viewedx) > -75 && viewedx < skeletonImage.getWidth() - 800) {
                viewedx += 5;
            }
            if ((owner.p.x - viewedx) < 75 && viewedx > 0) {
                viewedx -= 5;
            }
            if ((owner.p.y - 600 - viewedy) > -75 && viewedy < skeletonImage.getHeight() - 600) {
                viewedy += 10;
            }
            if ((owner.p.y - viewedy) < 75 && viewedy > 0) {
                viewedy -= 10;
            }
            synchronized (owner.p.bullets) {
                for (Bullet b : owner.p.bullets) {
                    paintBullet(b, bG);
                }
            }
        }
        g.drawImage(offscreen, 0, 0, this);
        owner.update();
    }

    public void drawPlayer(Player p, Graphics2D bG) {
        Color reset = bG.getColor();
        bG.drawImage(playerPictures[p.isFacingRight][p.isMoving], p.x - viewedx, p.y - viewedy, this);
        bG.setColor(Color.BLACK);
        bG.drawRect(p.x - viewedx - 1, p.y - viewedy - 4, p.width + 1, 4);
        bG.setColor(new Color(1.0f, 0.0f, 0.0f, 1.0f));
        bG.fillRect(p.x - viewedx, p.y - viewedy - 3, p.width, 3);
        bG.setColor(new Color(0.0f, 1.0f, 0.0f, 1.0f));
        bG.fillRect(p.x - viewedx, p.y - viewedy - 3, (int) (((double) p.health / (double) p.maxHealth) * p.width), 3);
        bG.setColor(reset);
    }

    public void drawZombie(Player p, Graphics2D bG) {
        Color reset = bG.getColor();
        bG.drawImage(zombiePictures[p.isFacingRight][p.isMoving], p.x - viewedx, p.y - viewedy, this);
        bG.setColor(Color.BLACK);
        bG.drawRect(p.x - viewedx - 1, p.y - viewedy - 4, p.width + 1, 4);
        bG.setColor(new Color(1.0f, 0.0f, 0.0f, 1.0f));
        bG.fillRect(p.x - viewedx, p.y - viewedy - 3, p.width, 3);
        bG.setColor(new Color(0.0f, 1.0f, 0.0f, 1.0f));
        bG.fillRect(p.x - viewedx, p.y - viewedy - 3, (int) (((double) p.health / (double) p.maxHealth) * p.width), 3);
        bG.setColor(reset);
    }

    public void drawParticle(Particle p, Graphics2D bG) {
        if (p.type == 0) {
            Particle.paintMinusOne(bG, p.x, p.y, p.size, p.status);
        }
        if (p.type == 1) {
            Particle.paintSmoke(bG, p.x, p.y, p.size, p.status);
        }
        if (p.type == 2) {
            Particle.paintCoin(bG, p.x, p.y, p.size, p.status);
        }
    }

    public void applyInput(MouseInput mi) {
        if (mi.down) {
            mi.down = false;
            synchronized (menuButtons) {
                for (MenuButton mb : menuButtons) {
                    if (mb.clickInside(mi.clickx, mi.clicky)) {
                        System.out.println(mb.text);
                        if (status == Status.MENU) {
                            if (mb.text.equals("Single Player")) {
                                transitionToSinglePlayer();
                                break;
                            } else if (mb.text.equals("Server Browser")) {
                                transitionToBrowser();
                                break;
                            }
                        } else if (status == Status.MENUSERVERBROWSER) {
                            if (mb.text.equals("Back")) {
                                transitionToMenu();
                                break;
                            } else if (mb.text.equals("Select Server")) {
                                Thread x = new Thread() {

                                    public void run() {
                                        if (browserBox == null) {
                                            browserBox = new ServerBrowserBox(owner.cantroFrame, false);
                                            browserBox.setLocationRelativeTo(owner.cantroFrame);
                                        }
                                        browserBox.setVisible(true);
                                    }
                                };
                                x.start();
                                break;
                            } else if (mb.text.equals("Select Name")) {
                                Thread x = new Thread() {

                                    public void run() {
                                        String tmp = JOptionPane.showInputDialog(null, "Your name please:");
                                        if (tmp != null && tmp.contains(":")) JOptionPane.showMessageDialog(null, "No colons allowed!"); else if (tmp != null && !tmp.trim().equals("")) selectedName = tmp;
                                    }
                                };
                                x.start();
                                break;
                            } else if (mb.text.equals("Connect")) {
                            }
                        }
                    }
                }
            }
        }
    }

    public void transitionToSinglePlayer() {
        for (int i = 0; i < 100; i++) new Zombie((int) (Math.random() * 1100) + 100, 0, owner);
        owner.gameScreen.setCursor(Cantro.blankCursor);
        owner.bulletCheckerThread.start();
        status = Status.SINGLEPLAYERGAME;
    }

    public void transitionToMenu() {
        menuButtons.clear();
        menuButtons.add(new MenuButton(200, 200, 400, 50, "Single Player", 22));
        menuButtons.add(new MenuButton(200, 275, 400, 50, "Server Browser", 22));
        menuButtons.add(new MenuButton(200, 350, 400, 50, "Options", 22));
        status = Status.MENU;
    }

    public void transitionToBrowser() {
        menuButtons.clear();
        menuButtons.add(new MenuButton(250, 200, 300, 50, "Select Server", 22));
        menuButtons.add(new MenuButton(250, 300, 300, 50, "Select Name", 22));
        menuButtons.add(new MenuButton(550, 400, 200, 50, "Connect", 22));
        menuButtons.add(new MenuButton(50, 400, 200, 50, "Back", 22));
        status = Status.MENUSERVERBROWSER;
    }

    public void transitionToOnlineGame(String server, String name) {
        owner.connection = new GameConnection(owner);
        owner.connection.connectTo("localhost", "James");
        owner.gameScreen.setCursor(Cantro.blankCursor);
        owner.bulletCheckerThread.start();
        owner.posUpdaterThread.start();
    }

    public void drawMenuButtons(Graphics2D bG) {
        Color reset = bG.getColor();
        for (MenuButton b : menuButtons) {
            bG.setColor(new Color(0.0f, 0.0f, 0.0f, .5f));
            bG.fillRect(b.x, b.y, b.width, b.height);
            bG.setColor(Color.RED);
            bG.drawRect(b.x, b.y, b.width, b.height);
            int[] pos = GraphicsUtil.centeringText(b.text, bG.getFont(), b.width, b.height, bG);
            bG.setFont(new Font("Arial", 0, b.fontSize));
            bG.drawString(b.text, pos[0] + b.x, pos[1] + b.y);
        }
        bG.setColor(reset);
    }

    public void paintRedLine(Player p, Graphics2D bG) {
        int x_mid = p.x + p.width / 2;
        if (owner.mouseInput.hoverx + viewedx < x_mid) {
            if (p.isFacingRight == 1) {
                p.isFacingRight = 0;
                p.xShootOff = 0;
            }
        } else {
            if (p.isFacingRight == 0) {
                p.isFacingRight = 1;
                p.xShootOff = 30;
            }
        }
        int x = p.x + p.xShootOff;
        int y = p.y + p.yShootOff;
        if (x != owner.mouseInput.hoverx) {
            if (x < owner.mouseInput.hoverx + viewedx) {
                paintRedLineNormalTheta(p, x, y);
            } else {
                paintRedLineInverseTheta(p, x, y);
            }
        } else {
            if (y > owner.mouseInput.hovery) bG.drawLine(x, y, x, 0); else bG.drawLine(x, y, x, 600);
        }
    }

    public void paintRedLineNormalTheta(Player p, int x, int y) {
        double theta = Math.atan((double) ((owner.mouseInput.hovery - (y - viewedy))) / (double) ((owner.mouseInput.hoverx - (x - viewedx))));
        double testx = x;
        double testy = y;
        double terminatex = testx;
        double terminatey = testy;
        int detail = 20;
        for (int i = 0; i < 1000; i += detail) {
            testx += detail * Math.cos(theta);
            testy += detail * Math.sin(theta);
            int rgb = getRGBofSkeleton((int) testx, (int) testy);
            if (rgb == -65281) {
                terminatex = testx;
                terminatey = testy;
                i = 1000;
            } else if (i == 1000 - detail) {
                terminatex = testx;
                terminatey = testy;
            }
        }
        bG.drawLine(x - viewedx, y - viewedy, (int) terminatex - viewedx, (int) terminatey - viewedy);
        p.aimTheta = theta;
    }

    public void paintRedLineInverseTheta(Player p, int x, int y) {
        double theta = Math.atan((double) ((owner.mouseInput.hovery - (y - viewedy))) / (double) ((owner.mouseInput.hoverx - (x - viewedx))));
        double testx = x;
        double testy = y;
        double terminatex = testx;
        double terminatey = testy;
        int detail = 20;
        for (int i = 0; i < 1000; i += detail) {
            testx -= detail * Math.cos(theta);
            testy -= detail * Math.sin(theta);
            int rgb = getRGBofSkeleton((int) testx, (int) testy);
            if (rgb == -65281) {
                terminatex = testx;
                terminatey = testy;
                i = 1000;
            } else if (i == 1000 - detail) {
                terminatex = testx;
                terminatey = testy;
            }
        }
        bG.drawLine(x - viewedx, y - viewedy, (int) terminatex - viewedx, (int) terminatey - viewedy);
        p.aimTheta = theta + Math.PI;
    }

    public void paintBullet(Bullet b, Graphics2D bG) {
        bG.drawImage(GraphicsUtil.rotate(bulletImage, b.rotation), (int) b.x - viewedx, (int) b.y - viewedy, null);
    }

    public int getRGBofSkeletonWithoutBoundCheck(int x, int y) {
        return skeletonImage.getRGB(x, y);
    }

    public int getRGBofSkeleton(int x, int y) {
        if (x < 0) x = 0;
        if (x >= skeletonImage.getWidth()) x = skeletonImage.getWidth() - 1;
        if (y < 0) y = 0;
        if (y >= skeletonImage.getHeight()) y = skeletonImage.getHeight() - 1;
        return skeletonImage.getRGB(x, y);
    }

    public void getRGBofSkeleton(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize) {
        int yoff = offset;
        int off;
        for (int y = startY; y < startY + h; y++, yoff += scansize) {
            off = yoff;
            for (int x = startX; x < startX + w; x++) {
                int xwithoff = x;
                int ywithoff = y;
                if (xwithoff >= 0 && xwithoff < skeletonImage.getWidth() && ywithoff >= 0 && ywithoff < skeletonImage.getHeight()) rgbArray[off++] = skeletonImage.getRGB(x, y); else rgbArray[off++] = -65281;
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
