package franklinmath.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * A random game implemented as an easter egg feature.
 * @author Allen Jordan
 */
public class GamePanel extends javax.swing.JPanel implements ActionListener {

    protected int windowWidth, windowHeight;

    protected int xLocation, yLocation, speed, circleDiameter;

    protected int playerHP;

    protected int playerHPMax;

    protected int attackTimer, attackTime, attackSize;

    protected boolean spaceReleasedSinceLastAttack;

    protected double enemySpawnRate;

    protected long playerScore;

    protected int maxEnemies;

    protected Vector<Enemy> enemyList;

    protected boolean[] keys;

    protected Image doubleBufferImage;

    protected Graphics2D doubleBufferGraphics;

    protected int doubleBufferWidth, doubleBufferHeight;

    protected javax.swing.Timer timer;

    protected long startTime;

    protected class Enemy {

        public float xPosition, yPosition;

        public int state;

        public int power;

        public int sightDistance;

        public int deathTimer;

        public static final int ENEMY_STOPPED = 0;

        public static final int ENEMY_ATTACKING = 1;

        public static final int ENEMY_DYING = 2;

        public Enemy() {
            xPosition = 0;
            yPosition = 0;
            power = 4;
            sightDistance = 50;
            deathTimer = 10;
            state = ENEMY_STOPPED;
        }
    }

    ;

    public GamePanel() {
        initComponents();
        xLocation = 20;
        yLocation = 20;
        speed = 2;
        circleDiameter = 10;
        playerHP = 100;
        playerHPMax = 100;
        attackTimer = 0;
        attackTime = 8;
        attackSize = 10;
        spaceReleasedSinceLastAttack = true;
        enemySpawnRate = 0.05;
        playerScore = 0;
        maxEnemies = 20;
        enemyList = new Vector<Enemy>();
        doubleBufferWidth = 0;
        doubleBufferHeight = 0;
        Enemy startingEnemy = new Enemy();
        startingEnemy.xPosition = 80;
        startingEnemy.yPosition = 80;
        startingEnemy.power = 5;
        enemyList.add(startingEnemy);
        startTime = System.currentTimeMillis();
        keys = new boolean[500];
        for (int i = 0; i < 500; i++) {
            keys[i] = false;
        }
        doubleBufferImage = null;
        this.requestFocus();
        timer = new javax.swing.Timer(30, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        this.update(this.getGraphics());
    }

    public double DistanceBetween(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    @Override
    public void update(Graphics g) {
        windowWidth = this.getWidth();
        windowHeight = this.getHeight();
        if (windowWidth < 100) {
            windowWidth = 100;
        }
        if (windowHeight < 100) {
            windowHeight = 100;
        }
        if ((doubleBufferImage == null) || (doubleBufferWidth != windowWidth) || (doubleBufferHeight != windowHeight)) {
            doubleBufferWidth = windowWidth;
            doubleBufferHeight = windowHeight;
            doubleBufferImage = this.createImage(doubleBufferWidth, doubleBufferHeight);
            doubleBufferGraphics = (Graphics2D) doubleBufferImage.getGraphics();
        }
        if (playerHP <= 0) {
            doubleBufferGraphics.setColor(Color.RED);
            doubleBufferGraphics.fillRect(0, 0, windowWidth, windowHeight);
            doubleBufferGraphics.setColor(Color.WHITE);
            doubleBufferGraphics.drawString("Game Over", windowWidth / 2 - 9 * 6 / 2, windowHeight / 2 - 6 / 2);
            doubleBufferGraphics.drawString("Score: " + playerScore, 10, 20);
        } else {
            if (keys[KeyEvent.VK_LEFT]) {
                xLocation -= speed;
            }
            if (keys[KeyEvent.VK_RIGHT]) {
                xLocation += speed;
            }
            if (keys[KeyEvent.VK_UP]) {
                yLocation -= speed;
            }
            if (keys[KeyEvent.VK_DOWN]) {
                yLocation += speed;
            }
            if (xLocation < 0) {
                xLocation = 0;
            } else if (xLocation > (windowWidth - circleDiameter)) {
                xLocation = windowWidth - circleDiameter;
            }
            if (yLocation < 0) {
                yLocation = 0;
            } else if (yLocation > (windowHeight - circleDiameter)) {
                yLocation = windowHeight - circleDiameter;
            }
            if ((keys[KeyEvent.VK_SPACE]) && (attackTimer == 0) && (spaceReleasedSinceLastAttack)) {
                spaceReleasedSinceLastAttack = false;
                attackTimer = attackTime;
            }
            if ((!keys[KeyEvent.VK_SPACE]) && (attackTimer == 0)) {
                spaceReleasedSinceLastAttack = true;
            }
            doubleBufferGraphics.setColor(Color.WHITE);
            doubleBufferGraphics.setStroke(new BasicStroke(1));
            doubleBufferGraphics.fillRect(0, 0, windowWidth, windowHeight);
            doubleBufferGraphics.setColor(Color.BLACK);
            doubleBufferGraphics.drawOval(xLocation, yLocation, circleDiameter, circleDiameter);
            if (attackTimer > 0) {
                attackTimer--;
                double circleAngle = 2 * Math.PI * (attackTime - attackTimer) / attackTime;
                int xOffset = xLocation + circleDiameter / 2;
                int yOffset = yLocation + circleDiameter / 2;
                for (double angle = 0; angle < circleAngle; angle += 0.5) {
                    double cosPosition = Math.cos(angle);
                    double sinPosition = Math.sin(angle) * (-1);
                    doubleBufferGraphics.drawLine((int) ((circleDiameter / 2 + 1) * cosPosition + xOffset), (int) ((circleDiameter / 2 + 1) * sinPosition + yOffset), (int) ((circleDiameter + attackSize) * cosPosition + xOffset), (int) ((circleDiameter + attackSize) * sinPosition + yOffset));
                }
                double cosPosition = Math.cos(circleAngle);
                double sinPosition = Math.sin(circleAngle) * (-1);
                doubleBufferGraphics.drawLine((int) ((circleDiameter / 2 + 1) * cosPosition + xOffset), (int) ((circleDiameter / 2 + 1) * sinPosition + yOffset), (int) ((circleDiameter + attackSize) * cosPosition + xOffset), (int) ((circleDiameter + attackSize) * sinPosition + yOffset));
            }
            if ((Math.random() < enemySpawnRate) && (enemyList.size() < maxEnemies)) {
                Enemy newEnemy = new Enemy();
                newEnemy.power = 5;
                if (Math.random() < enemySpawnRate * 5) {
                    newEnemy.power += Math.random() * 20 + 5;
                }
                newEnemy.sightDistance = 50 + newEnemy.power;
                do {
                    newEnemy.xPosition = (float) (Math.random() * (windowWidth - circleDiameter));
                    newEnemy.yPosition = (float) (Math.random() * (windowHeight - circleDiameter));
                } while (DistanceBetween(newEnemy.xPosition, newEnemy.yPosition, xLocation, yLocation) < circleDiameter * 5);
                enemyList.add(newEnemy);
            }
            doubleBufferGraphics.setColor(Color.RED);
            int centerXLocation = xLocation + circleDiameter / 2;
            int centerYLocation = yLocation + circleDiameter / 2;
            for (int i = 0; i < enemyList.size(); i++) {
                Enemy enemy = enemyList.get(i);
                float centerEnemyX = enemy.xPosition + ((float) enemy.power) / 2;
                float centerEnemyY = enemy.yPosition + ((float) enemy.power) / 2;
                if (enemy.state == Enemy.ENEMY_DYING) {
                    enemy.deathTimer--;
                    if (enemy.deathTimer <= 0) {
                        enemyList.remove(i);
                        i--;
                    } else {
                        int size = (int) (((double) enemy.deathTimer) / 10 * enemy.power);
                        doubleBufferGraphics.drawOval((int) enemy.xPosition, (int) enemy.yPosition, size, size);
                    }
                } else {
                    double enemyDistanceToMainCharacter = DistanceBetween(centerEnemyX, centerEnemyY, centerXLocation, centerYLocation);
                    if (enemyDistanceToMainCharacter < enemy.sightDistance) {
                        enemy.state = Enemy.ENEMY_ATTACKING;
                    } else {
                        enemy.state = Enemy.ENEMY_STOPPED;
                    }
                    if (attackTimer > 0) {
                        if (enemyDistanceToMainCharacter < (((double) enemy.power) / 2 + ((double) circleDiameter) / 2 + attackSize)) {
                            enemy.state = Enemy.ENEMY_DYING;
                            playerHP++;
                            if (playerHP > playerHPMax) {
                                playerHP = playerHPMax;
                            }
                            playerScore += enemy.power;
                        }
                    } else {
                        if (enemyDistanceToMainCharacter < (enemy.power / 2 + circleDiameter / 2)) {
                            playerHP--;
                        }
                    }
                    if (enemy.state == Enemy.ENEMY_STOPPED) {
                        doubleBufferGraphics.drawOval((int) enemy.xPosition, (int) enemy.yPosition, enemy.power, enemy.power);
                    } else if (enemy.state == Enemy.ENEMY_ATTACKING) {
                        doubleBufferGraphics.fillOval((int) enemy.xPosition, (int) enemy.yPosition, enemy.power, enemy.power);
                        float horizDirec = centerXLocation - centerEnemyX;
                        float vertDirec = centerYLocation - centerEnemyY;
                        float angle = (float) Math.atan2(vertDirec, horizDirec);
                        float yComponent = (float) (((float) enemy.power) / 8 * Math.sin(angle));
                        float xComponent = (float) (((float) enemy.power) / 8 * Math.cos(angle));
                        enemy.xPosition += xComponent;
                        enemy.yPosition += yComponent;
                    }
                }
            }
            doubleBufferGraphics.setColor(Color.GREEN);
            doubleBufferGraphics.setStroke(new BasicStroke(4));
            doubleBufferGraphics.drawLine(0, 0, (int) (((float) windowWidth * playerHP) / playerHPMax), 0);
            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) >= 15000) {
                startTime = currentTime;
                enemySpawnRate += 0.01;
                maxEnemies += 5;
                attackSize += 5;
                if (enemySpawnRate > 0.4) {
                    enemySpawnRate = 0.4;
                }
                if (maxEnemies > 150) {
                    maxEnemies = 150;
                }
                if (attackSize > windowWidth / 2) {
                    attackSize = windowWidth / 2;
                }
            }
        }
        g.drawImage(doubleBufferImage, 0, 0, this);
    }

    public void GameClosing() {
        timer.stop();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        if ((keyCode >= 0) && (keyCode < 500)) {
            keys[keyCode] = true;
        }
    }

    private void formKeyReleased(java.awt.event.KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        if ((keyCode >= 0) && (keyCode < 500)) {
            keys[keyCode] = false;
        }
    }

    private void formKeyTyped(java.awt.event.KeyEvent evt) {
        return;
    }
}
