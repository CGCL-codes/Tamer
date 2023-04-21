package text_adventure;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class PlayerCharacter extends Actor {

    public static Armour noArmour = new Armour("Nothing", 0, 0);

    public static Weapon noWeapon = new Weapon("Nothing", 0, 0, 0);

    public static int DEFAULT_ACTION_POINTS = 5;

    public static int DEFAULT_HEALTH = 10;

    private ArrayList<Actor> inventory;

    private Armour equippedArmour;

    private Weapon equippedWeapon;

    private LimitedValue actionPoints;

    private LimitedValue health;

    private Tile currentTile;

    private char representation;

    private boolean turnDone;

    private World w;

    private boolean newSpawn;

    private int kills, deaths;

    public PlayerCharacter(String name, Tile startingTile, World w) {
        super(name, 10);
        inventory = new ArrayList<Actor>();
        this.w = w;
        turnDone = false;
        kills = 0;
        deaths = 0;
        newSpawn = false;
        currentTile = startingTile;
        currentTile.addItem(this);
        representation = '1';
        equippedArmour = PlayerCharacter.noArmour;
        equippedWeapon = PlayerCharacter.noWeapon;
        actionPoints = new LimitedValue("AP", PlayerCharacter.DEFAULT_ACTION_POINTS);
        health = new LimitedValue("HP", PlayerCharacter.DEFAULT_HEALTH);
        this.addItem(new WeaponPistol());
    }

    @Override
    public String toString() {
        String output = "Player: " + super.toString() + ", " + this.health + ", " + this.actionPoints;
        if (this.equippedArmour != PlayerCharacter.noArmour) {
            output += "    Equipped Armour: " + equippedArmour.getName();
        }
        if (this.equippedWeapon != PlayerCharacter.noWeapon) {
            output += "    Equipped Weapon: " + equippedWeapon.toString();
        }
        return output;
    }

    public World getWorld() {
        return w;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addKill() {
        kills += 1;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public ArrayList<Actor> getInventory() {
        return inventory;
    }

    public void turnFinished() {
        this.turnDone = true;
    }

    public char getRepresentation() {
        return representation;
    }

    public Armour getEquippedArmour() {
        return equippedArmour;
    }

    public boolean isDead() {
        if (health.getCurrentValue() == 0) {
            return true;
        }
        return false;
    }

    public void reload() {
        if (actionPoints.getCurrentValue() != 0) {
            if (this.equippedWeapon != PlayerCharacter.noWeapon) {
                this.equippedWeapon.reload();
                actionPoints.decrementValue();
            } else {
                JOptionPane.showMessageDialog(null, "You do not have a weapon equipped to reload");
            }
        } else {
            notEnoughActionPoints();
        }
    }

    public void equipArmour(Armour equippedArmour) {
        if (actionPoints.getCurrentValue() != 0) {
            if (this.equippedArmour != equippedArmour) {
                this.equippedArmour = equippedArmour;
                actionPoints.decrementValue();
            } else {
                JOptionPane.showMessageDialog(null, "You already have the " + equippedArmour.getName() + " equipped");
            }
        } else {
            notEnoughActionPoints();
        }
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void equipWeapon(Weapon equippedWeapon) {
        if (actionPoints.getCurrentValue() != 0) {
            if (this.equippedWeapon != equippedWeapon) {
                this.equippedWeapon = equippedWeapon;
                actionPoints.decrementValue();
            } else {
                JOptionPane.showMessageDialog(null, "You already have the " + equippedWeapon.getName() + " equipped");
            }
        } else {
            notEnoughActionPoints();
        }
    }

    public LimitedValue getActionPoints() {
        return actionPoints;
    }

    private void notEnoughActionPoints() {
        JOptionPane.showMessageDialog(null, "You don't have enough Action Points (AP) to do that");
    }

    public LimitedValue getHealth() {
        return health;
    }

    public void shoot(Direction d, World w) {
        if (actionPoints.getCurrentValue() > 0) {
            if (equippedWeapon != PlayerCharacter.noWeapon) {
                equippedWeapon.shoot(d, w);
                actionPoints.decrementValue();
            } else {
                JOptionPane.showMessageDialog(null, "No weapon equipped");
            }
        } else {
            notEnoughActionPoints();
        }
    }

    public void takeDamage(int damage) {
        this.health.decrementValue(damage - this.getEquippedArmour().getArmourValue());
    }

    public boolean hasItem(String name) {
        for (Actor a : this.inventory) {
            if (a.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Actor getItem(String name) {
        for (Actor a : this.inventory) {
            if (a.name.equals(name)) {
                return a;
            }
        }
        return null;
    }

    public void dropItem(Actor item) {
        if (actionPoints.getCurrentValue() > 0) {
            currentTile.addItem(item);
            this.inventory.remove(item);
            actionPoints.decrementValue();
        } else {
            notEnoughActionPoints();
        }
    }

    public void addItem(Actor item) {
        if (actionPoints.getCurrentValue() != 0) {
            this.inventory.add(item);
            actionPoints.decrementValue();
        } else {
            notEnoughActionPoints();
        }
    }

    public void outputInventory() {
        System.out.println(name + "'s Inventory:");
        for (Actor a : this.inventory) {
            System.out.println(a);
        }
    }

    public void move(Direction d) {
        if (actionPoints.getCurrentValue() > 0) {
            boolean invalidMove = false;
            int x = currentTile.getX();
            int y = currentTile.getY();
            if (d == Direction.NORTH) {
                y -= 1;
            } else if (d == Direction.EAST) {
                x += 1;
            } else if (d == Direction.SOUTH) {
                y += 1;
            } else if (d == Direction.WEST) {
                x -= 1;
            }
            if (x < 0 || x >= World.WORLD_Y_SIZE || y < 0 || y >= World.WORLD_X_SIZE) {
                invalidMove = true;
            }
            if (!invalidMove) {
                Tile newTile = w.getTiles().get(y).get(x);
                if (newTile.hasPlayer()) {
                    invalidMove = true;
                } else if (newTile.isImpassable()) {
                    invalidMove = true;
                }
                if (!invalidMove) {
                    actionPoints.decrementValue();
                    currentTile.removeItem(this);
                    newTile.addItem(this);
                    currentTile = newTile;
                }
            }
        } else {
            notEnoughActionPoints();
        }
    }

    public void beDead() {
        currentTile.getStuff().addAll(this.inventory);
        this.inventory = new ArrayList<Actor>();
        this.equippedWeapon = noWeapon;
        this.equippedArmour = noArmour;
        currentTile.removeItem(this);
        this.currentTile = World.DEAD_TILE;
        deaths += 1;
        this.w.getWindow().updateButtons();
    }

    public void takeTurn(World w) {
        if (!(isDead())) {
            if (newSpawn) {
                this.currentTile = w.getNextPlayerStart().getLocation();
                currentTile.addItem(this);
                newSpawn = false;
                this.w.getWindow().updateButtons();
            }
            this.turnDone = false;
            this.actionPoints.resetValue();
            w.getWindow().setTitleText(this.toString());
            while (!turnDone) {
            }
        } else {
            this.health.resetValue();
            this.inventory.add(new WeaponPistol());
            newSpawn = true;
        }
    }
}
