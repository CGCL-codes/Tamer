package models.bodies;

import java.util.ArrayList;
import models.Arena;
import models.Cell;
import models.Direction;
import models.Location;
import models.states.CollisionActionState;
import models.states.DefendState;

/**
 * @author subodhkolla
 * The Pacman object in this game is basically the user driven object who listens to
 * keyboard commmands. Pacman has a score, and a direction of travel. 
 * 
 */
public class Pacman implements Body, Creature {

    private int score;

    private Location location;

    private Direction nextDirection;

    private Direction currentDirection;

    private CollisionActionState state;

    private boolean death;

    private int lives;

    private int attackcount = 0;

    static final String ID = "PACMAN";

    public int getAttackCount() {
        return this.attackcount;
    }

    public void addAttackCount() {
        if (this.attackcount > 20) this.attackcount = attackcount + 10;
    }

    public void subCount() {
        if (this.attackcount <= 0) {
            this.setState(new DefendState());
            this.attackcount = 0;
        } else this.attackcount--;
    }

    public Pacman(Location location) {
        score = 0;
        this.setState(new DefendState());
        death = false;
        lives = 3;
        this.location = location;
        this.location.setDirection(Direction.WEST);
        this.setCurrentDirection(Direction.WEST);
        this.setNextDirection(Direction.WEST);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void minusLivesByOne() {
        this.lives = this.lives--;
    }

    public boolean getDeath() {
        return death;
    }

    public void setDeath(boolean d) {
        death = d;
    }

    /**
	 * @param s  the integer score needed to add to pacman current score
	 * @return a boolean expression for if the creature is moving
	 */
    public int addToScore(int s) {
        return this.score += s;
    }

    public String getID() {
        return ID;
    }

    public Location getLocation() {
        return location;
    }

    public CollisionActionState getState() {
        return state;
    }

    public void setLocation(Location L) {
        this.location = L;
    }

    public void setState(CollisionActionState S) {
        state = S;
    }

    /**
	 * @return the score
	 */
    public int getScore() {
        return score;
    }

    /**
	 * @param score the score to set
	 */
    public void setScore(int score) {
        this.score = score;
    }

    /**
	 * @return the nextDirection
	 */
    public Direction getNextDirection() {
        return nextDirection;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
	 * @param nextDirection the nextDirection to set
	 */
    public void setNextDirection(Direction nextDirection) {
        this.nextDirection = nextDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public boolean move(Arena A) {
        this.subCount();
        System.out.println("hey AWESOME IM GOING" + this.getNextDirection());
        Pacman Pac = (Pacman) A.getBodyById("PACMAN");
        System.out.println(Pac.getLocation().getX() + " " + Pac.getLocation().getY());
        Cell C = A.getCell(Pac.getLocation().getX(), Pac.getLocation().getY());
        boolean bMove = false;
        int mx;
        int my;
        if (C.isNeighborAvailable(this.getNextDirection())) {
            Cell neighbor = C.getNeighbor(this.getNextDirection());
            ArrayList<Body> b = neighbor.getBodies();
            this.setCurrentDirection(this.getNextDirection());
            boolean okMove = true;
            for (Body body : b) {
                boolean canMove = state.onCollisionEvent(this, body);
                if (!canMove) okMove = false;
            }
            if (okMove) {
                bMove = neighbor.addBody(this);
                mx = neighbor.getLocation().getX();
                my = neighbor.getLocation().getY();
                System.out.println("NEWLOCATION" + mx + " " + my);
                this.setLocation(new Location(mx, my, this.getNextDirection()));
            }
            C.removeBody(this);
            return bMove;
        } else {
            if (C.isNeighborAvailable(this.getCurrentDirection())) {
                this.setNextDirection(this.getCurrentDirection());
                return true;
            } else {
                System.out.println("HEEEEEEEEEEEEEEEEEEEEEEYYYYYY");
                return false;
            }
        }
    }
}
