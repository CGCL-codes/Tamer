package undecided;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import View.MasterView;
import View.MasterViewPanel;
import View.Views;
import rectangles.BubbleShieldRectangle;
import rectangles.CrateRectangle;
import rectangles.FireRingRectangle;
import rectangles.IceBlockRectangle;
import rectangles.ImmovableBlockRectangle;
import rectangles.ProjectileRectangle;
import rectangles.SpeedBoostRectangle;
import rectangles.SpikePitRectangle;
import rectangles.TNTRectangle;
import rectangles.TankRectangle;

/**
 * 
 * This class is the primary battle view in which the player controls a single
 * tank that can move through the arrow keys and shoot through the mouse.
 * 
 * @author Team Exception
 * 
 * @extends MasterViewPanel
 * 
 * @implements Observer
 * 
 * @see MasterViewPanel, MasterView, LanView, TitleView
 * 
 */
public class TankView extends MasterViewPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private JPanel panel;

    private Map currentMap;

    private Image dbImage;

    private Graphics dbg;

    private PlayerTank player;

    private EnemyTank enemy;

    private LinkedList<Projectile> projectileList;

    private LinkedList<Obstacle> obstacleList;

    private LinkedList<PlayerTank> tankList;

    private LinkedList<Item> itemList;

    private LinkedList<EnemyTank> enemyList;

    private LinkedList<Explosion> explosionList;

    java.util.Vector<Projectile> pVector;

    private boolean won, lost, gameOver;

    private Image camo, wheel, steel, gold, grass, ice, sand;

    /**
	 * This is the class constructor for the TankView Class. It contains a call
	 * to the superclass MasterView and the map to which the TankView is to
	 * include. It not only contains the battlefield that the tanks will fight
	 * on but also the amount of lives the player still has (this will be
	 * campaign mode), the level the user is on, and the active items on the
	 * field.
	 * 
	 * @param m
	 *            this is the MasterView
	 * @param map
	 *            the map to which this TankView is to include
	 */
    public TankView(MasterView m, Map map) {
        super(m);
        currentMap = map;
        camo = new ImageIcon("images/camo.png").getImage();
        wheel = new ImageIcon("images/wheel-md.png").getImage();
        steel = new ImageIcon("images/steel.png").getImage();
        gold = new ImageIcon("images/gold.png").getImage();
        grass = new ImageIcon("images/grass.png").getImage();
        ice = new ImageIcon("images/ice.png").getImage();
        sand = new ImageIcon("images/sand.png").getImage();
        won = false;
        lost = false;
        gameOver = false;
        currentMap.addObserver(this);
        tankList = currentMap.getPlayers();
        explosionList = currentMap.getExplosions();
        projectileList = currentMap.getProjectiles();
        obstacleList = currentMap.getObstacles();
        enemyList = currentMap.getEnemies();
        itemList = currentMap.getItems();
        currentMap.enemyList.getFirst().startEnemyTank();
        GameThread gt = new GameThread();
        gt.start();
        this.setFocusable(true);
        panel = new JPanel();
        player = tankList.getFirst();
        add(panel);
        addKeyListener(new moveAndShootListener());
        Handlerclass handler = new Handlerclass();
        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
        this.setVisible(true);
    }

    /**
	 * This is basically the private inner class that handles the mouse listener
	 * and mouse motion listener methods that controls the direction and whether
	 * or not the tank is shooting.
	 * 
	 * @author Team Exception
	 * 
	 * @implements MouseListener, MouseMotionListener
	 * 
	 * @see MouseListener, MouseMotionListener
	 * 
	 */
    private class Handlerclass implements MouseListener, MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent arg0) {
        }

        @Override
        public void mouseMoved(MouseEvent arg0) {
        }

        @Override
        public void mouseClicked(MouseEvent arg0) {
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
        }

        /**
		 * This method implements the MouseMotionListener and whenever the mouse
		 * is pressed, it will calculate the best fit direction in which the
		 * projectile is to travel.
		 * 
		 * @param arg0
		 *            mouse event argument
		 */
        public void mousePressed(MouseEvent arg0) {
            int count = 0;
            for (Projectile p : currentMap.getProjectiles()) {
                if (p instanceof PlayerProjectile) {
                    count++;
                }
            }
            if (count == 0) {
                int xdiff = arg0.getX() - player.getLocation().col;
                int ydiff = arg0.getY() - player.getLocation().row;
                double length = Math.sqrt(xdiff * xdiff + ydiff * ydiff);
                player.shoot(new Point(player.getLocation().row, player.getLocation().col), (int) (xdiff * (5 / length)), (int) (ydiff * (5 / length)));
            }
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
        }
    }

    /**
	 * This method paints the TankView graphics when called.
	 * 
	 * @param g
	 *            Graphics component for this panel
	 */
    public void paint(Graphics g) {
        try {
            dbImage = createImage(getWidth(), getHeight());
            dbg = dbImage.getGraphics();
            paintComponent(dbg);
            g.drawImage(dbImage, 0, 0, this);
        } catch (Exception e) {
        }
    }

    /**
	 * This method will paint all the components of the TankView when called
	 * including obstacles, items, the tanks, and the field background along
	 * with the score board on the right hand side of the screen.
	 * 
	 * @param g
	 *            graphics component that java uses to paint components. It will
	 *            repaint all the components including the projectile, the tanks
	 *            in the tank list, and all the objects in the obstacle list.
	 */
    public void paintComponent(Graphics g) {
        if (MasterView.currentLevel == 1 || MasterView.currentLevel == 2) {
            for (int i = 0; i < 700; i += 50) {
                for (int j = 0; j < 1200; j += 50) {
                    g.drawImage(grass, j, i, null);
                }
            }
        }
        if (MasterView.currentLevel == 3 || MasterView.currentLevel == 4) {
            for (int i = 0; i < 700; i += 50) {
                for (int j = 0; j < 1200; j += 50) {
                    g.drawImage(sand, j, i, null);
                }
            }
        }
        if (MasterView.currentLevel == 5) {
            for (int i = 0; i < 700; i += 50) {
                for (int j = 0; j < 1200; j += 50) {
                    g.drawImage(ice, j, i, null);
                }
            }
        }
        for (int i = 0; i < obstacleList.size(); i++) {
            Obstacle p = obstacleList.get(i);
            if (p instanceof SpikePit) {
                SpikePit sp = (SpikePit) p;
                SpikePitRectangle spRect = sp.getRectangle();
                g.drawImage(spRect.getImage(), spRect.xCoord(), spRect.yCoord(), null);
            }
        }
        for (Item p : itemList) {
            if (p instanceof SpeedBoost) {
                SpeedBoost s = (SpeedBoost) p;
                SpeedBoostRectangle tRect = s.getRectangle();
                g.drawImage(tRect.getImage(), tRect.xCoord(), tRect.yCoord(), null);
            }
            if (p instanceof IceBlock) {
                IceBlock s = (IceBlock) p;
                IceBlockRectangle tRect = s.getRectangle();
                g.drawImage(tRect.getImage(), tRect.xCoord(), tRect.yCoord(), null);
            }
            if (p instanceof BubbleShield) {
                BubbleShield s = (BubbleShield) p;
                BubbleShieldRectangle tRect = s.getRectangle();
                g.drawImage(tRect.getImage(), tRect.xCoord(), tRect.yCoord(), null);
            }
        }
        for (int i = 0; i < obstacleList.size(); i++) {
            Obstacle p = obstacleList.get(i);
            if (p instanceof Crate) {
                Crate c = (Crate) p;
                CrateRectangle cRect = c.getRectangle();
                g.drawImage(cRect.getImage(), cRect.xCoord(), cRect.yCoord(), null);
            }
            if (p instanceof ImmovableBlock) {
                ImmovableBlock ib = (ImmovableBlock) p;
                ImmovableBlockRectangle ibRect = ib.getRectangle();
                g.drawImage(ibRect.getImage(), ibRect.xCoord(), ibRect.yCoord(), null);
            }
            if (p instanceof FireRing) {
                FireRing fr = (FireRing) p;
                FireRingRectangle frRect = fr.getRectangle();
                g.setColor(frRect.setColor());
                g.drawImage(fr.getImage(), frRect.xCoord(), frRect.yCoord(), null);
            }
            if (p instanceof TNT) {
                TNT tnt = (TNT) p;
                TNTRectangle tntRect = tnt.getRectangle();
                g.drawImage(tntRect.getImage(), tntRect.xCoord(), tntRect.yCoord(), null);
            }
        }
        for (PlayerTank p : tankList) {
            TankRectangle tRect = p.getRectangle();
            g.drawImage(p.getImage(), tRect.xCoord(), tRect.yCoord(), null);
        }
        for (EnemyTank p : enemyList) {
            TankRectangle tRect = p.getRectangle();
            g.drawImage(p.getImage(), tRect.xCoord(), tRect.yCoord(), null);
        }
        for (Projectile p : projectileList) {
            if (p instanceof PlayerProjectile) {
                PlayerProjectile s = (PlayerProjectile) p;
                ProjectileRectangle rect = s.getRectangle();
                g.drawImage(rect.getImage(), rect.xCoord(), rect.yCoord(), null);
            }
            if (p instanceof EnemyProjectile) {
                EnemyProjectile s = (EnemyProjectile) p;
                ProjectileRectangle rect = s.getRectangle();
                g.drawImage(rect.getImage(), rect.xCoord(), rect.yCoord(), null);
            }
        }
        for (Explosion p : explosionList) {
            g.drawImage(p.getImage(), p.getLocation().col, p.getLocation().row, null);
        }
        if (won == true) {
            Font font = new Font("Times New Roman", Font.BOLD, 28);
            String jb = "Mission Complete!";
            AttributedString att = new AttributedString(jb);
            att.addAttribute(TextAttribute.FOREGROUND, Color.YELLOW);
            att.addAttribute(TextAttribute.FONT, font);
            g.drawString(att.getIterator(), 400, 350);
        }
        if (lost == true) {
            Font font = new Font("Times New Roman", Font.BOLD, 28);
            String jb = "Mission Failed!";
            AttributedString att = new AttributedString(jb);
            att.addAttribute(TextAttribute.FOREGROUND, Color.RED);
            att.addAttribute(TextAttribute.FONT, font);
            g.drawString(att.getIterator(), 400, 350);
        }
        if (gameOver == true) {
            Font font = new Font("Times New Roman", Font.BOLD, 44);
            String jb = "Game Over!";
            AttributedString att = new AttributedString(jb);
            att.addAttribute(TextAttribute.FOREGROUND, Color.ORANGE);
            att.addAttribute(TextAttribute.FONT, font);
            g.drawString(att.getIterator(), 370, 350);
        }
        for (int i = 0; i < 700; i += 50) {
            for (int j = 985; j < 1200; j += 50) {
                if (i == 150 || i == 200 || i == 350 || i == 400) {
                    g.drawImage(steel, j, i, null);
                } else {
                    g.drawImage(camo, j, i, null);
                }
            }
        }
        for (int i = 690; i < 900; i += 20) {
            for (int j = 0; j < 1200; j += 20) {
                g.drawImage(gold, j, i, null);
            }
        }
        for (int i = 0; i < 700; i += 20) {
            for (int j = 985; j < 1200; j += 20) {
                if (i == 0 || i == 680 || j == 985 || j == 1165) {
                    g.drawImage(gold, j, i, null);
                }
            }
        }
        for (int i = 0; i < 900; i += 20) {
            for (int j = 1180; j < 1500; j += 20) {
                g.drawImage(gold, j, i, null);
            }
        }
        Font font = new Font("Times New Roman", Font.BOLD, 20);
        String lives = "Lives Remaning";
        AttributedString att = new AttributedString(lives);
        att.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
        att.addAttribute(TextAttribute.FONT, font);
        g.drawString(att.getIterator(), 1018, 44);
        for (int i = 0; i < MasterView.playerLives; i++) {
            for (int j = 0; j < MasterView.playerLives * 50; j += 55) {
                g.drawImage(wheel, 1005 + j, 65, null);
            }
        }
        String curr = "Current Level: " + currentMap.getLevelNumber();
        AttributedString att3 = new AttributedString(curr);
        att3.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
        att3.addAttribute(TextAttribute.FONT, font);
        g.drawString(att3.getIterator(), 1013, 300);
        String item = "Active Items";
        AttributedString att6 = new AttributedString(item);
        att6.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
        att6.addAttribute(TextAttribute.FONT, font);
        g.drawString(att6.getIterator(), 1030, 485);
        if (player.isActiveShield()) {
            g.drawImage(new BubbleShieldRectangle(-10, -10).getImage(), 1020, 515, null);
        }
        if (player.isActiveBoost()) {
            g.drawImage(new SpeedBoostRectangle(-10, -10).getImage(), 1100, 515, null);
        }
    }

    /**
	 * This private inner class controls the player controlled tank allowing it
	 * to move via key listeners and shoot through the mouse lister. The tank
	 * will move on key pressed.
	 * 
	 * @author Team Exception
	 * 
	 * @see KeyListener
	 * 
	 */
    private class moveAndShootListener implements KeyListener {

        /**
		 * This method will call the methods to move the PlayerTank depending on
		 * which key is pressed. NOTE: The keys for up, down, right, and left
		 * movement are w, s, d, and s respectively.
		 * 
		 * @param e
		 *            keyaction event for when a key is pressed
		 */
        @Override
        public void keyPressed(KeyEvent e) {
            int keyEvent = e.getKeyCode();
            if (keyEvent == KeyEvent.VK_W) {
                player.moveUp();
            }
            if (keyEvent == KeyEvent.VK_S) {
                player.moveDown();
            }
            if (keyEvent == KeyEvent.VK_A) {
                player.moveLeft();
            }
            if (keyEvent == KeyEvent.VK_D) {
                player.moveRight();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    /**
	 * This method will be notified when the observed are called and will either
	 * remove dead obstacles and repaint the projectiles.
	 * 
	 * @param v
	 *            observable variable
	 * 
	 * @param o
	 *            object that is to be analyzed for update
	 */
    public synchronized void update(Observable v, Object o) {
        if (o == null) {
            repaint();
        }
        if (o instanceof Point) {
            Point p = (Point) o;
            Explosion et = new Explosion(p, currentMap);
            currentMap.addExplosion(et);
        }
    }

    /**
	 * This method will deal with the primary tank view thread. When the battle
	 * is to begin, this thread is started, constructing the map, turning on the
	 * ItemCreator, and the AI tank movement.
	 * 
	 * @category inner class
	 * 
	 * @author Team Exception
	 * 
	 * @extends Thread
	 * 
	 * @see Map, ItemCreator, PlayerTank, EnemyTank
	 * 
	 */
    private class GameThread extends Thread {

        private boolean exists;

        public GameThread() {
            exists = true;
        }

        /**
		 * This method is the thread that is to be run
		 */
        @Override
        public synchronized void run() {
            while (exists) {
                if (currentMap.getPlayers().size() == 0) {
                    lost = true;
                    MasterView.playerLives--;
                    if (MasterView.playerLives == -1) {
                        lost = false;
                        gameOver = true;
                        repaint();
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                        }
                        m.changeView(Views.TITLE, null);
                        MasterView.currentLevel = 1;
                        MasterView.playerLives = 3;
                        break;
                    }
                    repaint();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    m.changeView(Views.TANKVIEW, null);
                    exists = false;
                } else if (currentMap.getEnemies().size() == 0) {
                    won = true;
                    repaint();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    MasterView.currentLevel++;
                    m.changeView(Views.TANKVIEW, null);
                    exists = false;
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
