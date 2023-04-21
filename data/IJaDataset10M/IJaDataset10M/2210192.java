package dolf.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import dolf.objects.*;
import dolf.objects.weapons.Weapon;
import dolf.window.*;

public class Player implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private java.awt.Color color;

    private int[][] scoreList;

    private String name;

    private int healthMax = Integer.parseInt(ConfigurationHandler.getInstance().getProperty("health"));

    private int health = healthMax;

    private Weapon aktWeapon;

    private Ball ball;

    private List<Weapon> weapons = new LinkedList<Weapon>();

    private Player player;

    private int money = Integer.parseInt(ConfigurationHandler.getInstance().getProperty("money"));

    private boolean pocketed = false;

    private boolean drowned = false;

    private JProgressBar ammoBar = new JProgressBar(0, 100);

    private JProgressBar progressBar;

    private static int aktPacour;

    private JPanel playerPane = new JPanel();

    public Player(String na, java.awt.Color co) {
        this.player = this;
        this.name = na;
        this.color = co;
        if (healthMax > 300) {
            healthMax = 300;
            health = healthMax;
        } else if (healthMax < 1) {
            healthMax = 1;
            health = healthMax;
        }
        progressBar = new JProgressBar(0, this.getMaxHP());
        progressBar.setForeground(new Color(0, 255, 50));
        progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        progressBar.setBorderPainted(true);
        ball = new Ball(new Point(0, 0), co, this);
        ball.setVisible(false);
        this.ammoBar.setValue(0);
        this.ammoBar.setStringPainted(false);
        this.ammoBar.setPreferredSize(new java.awt.Dimension(55, 10));
    }

    public void reset() {
        setHP(healthMax);
        aktWeapon = null;
        ball = new Ball(new Point(0, 0), this.color, this);
        ball.setVisible(false);
        pocketed = false;
    }

    public void setScoreList(int pacours) {
        this.scoreList = new int[pacours][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < pacours; j++) {
                this.scoreList[j][i] = 0;
            }
        }
    }

    public int[][] getScoreList() {
        return this.scoreList;
    }

    public Ball getBall() {
        return ball;
    }

    public void setColor(java.awt.Color co) {
        this.color = co;
        ball.setColor(co);
    }

    public java.awt.Color getColor() {
        return this.color;
    }

    public void setDrowned(boolean b) {
        this.drowned = b;
    }

    public void setName(String na) {
        this.name = na;
    }

    public String getName() {
        return this.name;
    }

    public void addWeapon(Weapon w) {
        System.out.println("add Weapon " + w.getName() + " to Player " + this.getName());
        weapons.add(w);
    }

    public void removeWeapon(Weapon w) {
        this.getWeaponList().remove(w);
    }

    public void removeWeapon(String s) {
        for (int i = 0; i < weapons.size(); i++) {
            if (weapons.get(i).getName().equals(s)) {
                weapons.remove(i);
                break;
            }
        }
    }

    public List<Weapon> getWeaponList() {
        return this.weapons;
    }

    public int getWeaponAmount(Weapon w) {
        int amount = 0;
        for (Weapon w1 : weapons) {
            if (w1.getName().compareTo(w.getName()) == 0) amount++;
        }
        return amount;
    }

    public int getWeaponAmount(String n) {
        int amount = 0;
        for (Weapon w1 : weapons) {
            if (w1.getName().compareTo(n) == 0) amount++;
        }
        return amount;
    }

    public boolean weaponAktivatable() {
        if (Level.getInstance().getAktualPlayer() == this) {
            if (this.aktWeapon == null || Level.getInstance().getPhase() == Game.PHASE_DIRECTION) {
                return true;
            }
        }
        return false;
    }

    public boolean useWeapon(Weapon w) {
        if (this.weaponAktivatable()) {
            if (this.aktWeapon != null) {
                this.aktWeapon.deInitialize();
            }
            this.aktWeapon = w;
            w.initialize(this);
            System.out.println("Weapon " + w.getName() + " initialized");
            return true;
        } else {
            System.err.println("weapon " + w.getName() + " could NOT be used");
            return false;
        }
    }

    public Weapon getAktWeapon() {
        return this.aktWeapon;
    }

    public void setAktWeapon(Weapon w) {
        this.aktWeapon = w;
    }

    public void setWeaponState() {
        if (this.aktWeapon == null) {
            this.ammoBar.setValue(0);
        } else {
            this.ammoBar.setValue((int) (this.aktWeapon.getState() * 100));
        }
    }

    public void setHP(int hp) {
        if (hp < this.getHP()) {
            BloodManager.getInstance().addBlood(this.getHP() - hp, this.getBall().getPosition());
        }
        if (hp <= 0) {
            this.health = 0;
            Level.getInstance().removePlayer(this);
            PlayerDisplay.getInstance().draw();
            this.scoreList[aktPacour - 1][0] = Level.getInstance().getLimit();
        } else {
            if (hp >= this.healthMax) {
                this.health = this.healthMax;
            } else {
                this.health = hp;
            }
            progressBar.setValue(this.getHP());
            int proz = (getHP() * 100) / healthMax;
            Color col;
            if (proz > 50) {
                col = new Color(-255 * proz / 50 + 512, 255, 50);
            } else {
                col = new Color(255, 255 * proz / 50, 0);
            }
            progressBar.setForeground(col);
        }
    }

    public void removeHP(int i) {
        this.setHP(this.getHP() - i);
    }

    public int getMaxHP() {
        return this.healthMax;
    }

    public void setMaxHP(int hp) {
        this.healthMax = hp;
    }

    public int getHP() {
        return this.health;
    }

    public static int getAktPacour() {
        return aktPacour;
    }

    public static void setAktPacour(int i) {
        aktPacour = i;
    }

    public static void incAktPacour() {
        aktPacour++;
    }

    public void addDamage(int dm) {
        this.scoreList[aktPacour - 1][1] += dm;
    }

    public void incHit() {
        this.scoreList[aktPacour - 1][0] += 1;
    }

    public int getHits() {
        return this.scoreList[aktPacour - 1][0];
    }

    public int getMoney() {
        return money;
    }

    public boolean isDrowned() {
        return this.drowned;
    }

    public void removeMoney(int m) {
        this.setMoney(this.getMoney() - m);
    }

    public void setMoney(int m) {
        money = m;
    }

    public void updatePlayerPane() {
        playerPane.removeAll();
        playerPane.setLayout(new BoxLayout(playerPane, BoxLayout.PAGE_AXIS));
        Border b1 = BorderFactory.createLineBorder(Color.BLACK);
        Border b2 = BorderFactory.createMatteBorder(5, 5, 5, 5, this.getColor());
        JLabel nameLabel;
        if (Level.getInstance().getAktualPlayer() == this) {
            nameLabel = new JLabel("<html><body><u><b>" + this.getName() + "</b></u></body></html>");
        } else {
            nameLabel = new JLabel("<html><body><b>" + this.getName() + "</b></body></html>");
        }
        playerPane.setBorder(BorderFactory.createCompoundBorder(b1, b2));
        playerPane.setBackground(this.getColor());
        playerPane.setOpaque(true);
        nameLabel.setForeground(new java.awt.Color(255 - this.getColor().getRed(), 255 - this.getColor().getGreen(), 255 - this.getColor().getBlue()));
        nameLabel.setOpaque(false);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        nameLabel.setPreferredSize(new Dimension(80, 25));
        JPanel healthPane = new JPanel();
        healthPane.setOpaque(false);
        healthPane.setPreferredSize(new java.awt.Dimension(90, 30));
        healthPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        healthPane.setAlignmentY(Component.TOP_ALIGNMENT);
        if (!pocketed) {
            if (this.getHP() <= 0) {
                JLabel deadLabel = new JLabel(ImageStore.getInstance().getIcon("dolf/gfx/window/dead.png"));
                healthPane.add(deadLabel);
            } else {
                progressBar.setValue(this.getHP());
                progressBar.setOpaque(false);
                progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
                progressBar.setAlignmentY(Component.TOP_ALIGNMENT);
                progressBar.setPreferredSize(new java.awt.Dimension(85, 20));
                healthPane.add(progressBar);
            }
        } else {
            JLabel finishLabel = new JLabel(ImageStore.getInstance().getIcon("dolf/gfx/window/pocketed.png"));
            healthPane.add(finishLabel);
        }
        JPanel ammoPane = new JPanel();
        final JPanel weaponPane = new JPanel(new GridLayout(0, 3, 5, 5));
        if (Level.getInstance().getAktualPlayer() == this) {
            ammoPane.setOpaque(false);
            ammoPane.setPreferredSize(new java.awt.Dimension(45, 15));
            ammoPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            ammoPane.setAlignmentY(Component.TOP_ALIGNMENT);
            if (this == Level.getInstance().getAktualPlayer() && this.aktWeapon != null) {
                this.setWeaponState();
                ammoPane.add(this.ammoBar);
            }
            weaponPane.setOpaque(false);
            weaponPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            weaponPane.setAlignmentY(Component.TOP_ALIGNMENT);
            weaponPane.setBorder(new EmptyBorder(5, 0, 5, 0));
            ArrayList<String> usedWeapons = new ArrayList<String>();
            for (final Weapon w : this.getWeaponList()) {
                if (!usedWeapons.contains(w.getName())) {
                    final JLayeredPane layerPane = new JLayeredPane();
                    layerPane.setPreferredSize(new Dimension(25, 25));
                    final JButton useWeapon = new JButton(w.getIcon());
                    useWeapon.setDisabledIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("dolf/gfx/window/cart_disabled.png")));
                    useWeapon.setContentAreaFilled(false);
                    useWeapon.setFocusPainted(false);
                    useWeapon.setMargin(new Insets(0, 0, 0, 0));
                    useWeapon.setToolTipText(w.getName() + ": " + w.getDescription());
                    useWeapon.setBounds(0, 0, 25, 25);
                    final JLabel textLabel = new JLabel(Integer.toString(player.getWeaponAmount(w)));
                    textLabel.setForeground(java.awt.Color.BLACK);
                    textLabel.setBounds(10, 10, 20, 20);
                    layerPane.add(useWeapon, JLayeredPane.DEFAULT_LAYER);
                    layerPane.add(textLabel, new Integer(JLayeredPane.DEFAULT_LAYER.intValue() + 1));
                    useWeapon.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                    useWeapon.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            Weapon aktW = getAktWeapon();
                            if (Level.getInstance().getPhase() == Game.PHASE_DIRECTION && aktW != null && aktW.getName() == w.getName()) {
                                aktW.deInitialize();
                                setAktWeapon(null);
                                useWeapon.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                                updatePlayerPane();
                            } else if (useWeapon(w)) {
                                if (aktWeapon != null && aktWeapon.getName() == w.getName()) {
                                    useWeapon.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255 - getColor().getRed(), 255 - getColor().getGreen(), 255 - getColor().getBlue())));
                                }
                                updatePlayerPane();
                            }
                        }
                    });
                    useWeapon.addMouseListener(new MouseListener() {

                        public void mouseEntered(MouseEvent arg0) {
                            if (Level.getInstance().getAktualPlayer() == player) {
                                if (aktWeapon != null && aktWeapon.getName() == w.getName()) useWeapon.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255 - getColor().getRed(), 255 - getColor().getGreen(), 255 - getColor().getBlue()))); else if (aktWeapon != null) useWeapon.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); else useWeapon.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.WHITE));
                            }
                        }

                        public void mouseExited(MouseEvent arg0) {
                            if (Level.getInstance().getAktualPlayer() == player) {
                                if (aktWeapon != null && aktWeapon.getName() == w.getName()) useWeapon.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255 - getColor().getRed(), 255 - getColor().getGreen(), 255 - getColor().getBlue()))); else useWeapon.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                            }
                        }

                        public void mousePressed(MouseEvent arg0) {
                        }

                        public void mouseReleased(MouseEvent arg0) {
                        }

                        public void mouseClicked(MouseEvent arg0) {
                        }
                    });
                    weaponPane.add(layerPane);
                    usedWeapons.add(w.getName());
                }
            }
        }
        playerPane.add(nameLabel);
        playerPane.add(healthPane);
        if (Level.getInstance().getAktualPlayer() == this) {
            playerPane.add(ammoPane);
            playerPane.add(weaponPane);
        }
        MainWindow.getInstance().actualize();
    }

    public JPanel getPlayerPane() {
        updatePlayerPane();
        return playerPane;
    }

    public boolean isInPocket() {
        return pocketed;
    }

    public void setPocketed(boolean p) {
        this.pocketed = p;
    }
}
