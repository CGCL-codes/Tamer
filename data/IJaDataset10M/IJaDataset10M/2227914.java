package battleTank;

import java.util.Observer;

/**
 * This class is the "level1" map for the campaign mode of tanks. It contains a
 * unique ItemCreator and a preset locations to place the obstacles, PlayerTank,
 * and the EnemyTank.
 * 
 * @author Team Exception
 * 
 * @extends Map
 * 
 * @implements Observer
 * 
 * @see ItemCreator, TankView, PlayerTank, EnemyTank
 */
public class Level1 extends Map implements Observer {

    private ItemCreator creator;

    /**
	 * This is the Level1 class constructor and simply creates an ItemCreator
	 * and starts the ItemCreator thread to begin spawning items onto the field.
	 * 
	 * @category constructor
	 */
    public Level1() {
        super();
        startCreator();
    }

    public void startCreator() {
        creator = new ItemCreator(this);
        creator.start();
    }

    /**
	 * This method will pause the ItemCreator if the creator has been set to
	 * alive (or meaning that it is running).
	 * 
	 * @see ItemCreator
	 */
    public void interruptCreator() {
        if (creator.isAlive()) creator.interrupt();
    }

    /**
	 * This method actually sets up the predetermined map that has been designed
	 * as seen below. It will include adding all the five different obstacles
	 * onto the map including immovable blocks, crates, fire rings, TNT, and
	 * spike pits.
	 * 
	 * @see Crate, FireRing,ImmovableBlock, SpikePit, TNT
	 */
    public void setUpMap() {
        for (int i = 0; i < 750; i = i + 50) {
            ImmovableBlock b = new ImmovableBlock(new Point(i, 25), this);
            addObstacle(b);
        }
        for (int i = 30; i < 1000; i = i + 50) {
            ImmovableBlock b = new ImmovableBlock(new Point(665, i), this);
            addObstacle(b);
        }
        for (int i = 10; i < 750; i = i + 50) {
            ImmovableBlock b = new ImmovableBlock(new Point(i, 960), this);
            addObstacle(b);
        }
        for (int i = 60; i < 1000; i = i + 50) {
            ImmovableBlock b = new ImmovableBlock(new Point(25, i), this);
            addObstacle(b);
        }
        for (int i = 75; i < 275; i += 50) {
            Crate c = new Crate(new Point(i, 480), this);
            addObstacle(c);
        }
        for (int i = 465; i < 665; i += 50) {
            Crate c = new Crate(new Point(i, 480), this);
            addObstacle(c);
        }
        for (int i = 75; i < 325; i += 50) {
            ImmovableBlock b = new ImmovableBlock(new Point(i, 175), this);
            addObstacle(b);
        }
        for (int i = 415; i < 665; i += 50) {
            ImmovableBlock b = new ImmovableBlock(new Point(i, 810), this);
            addObstacle(b);
        }
        for (int i = 75; i < 175; i += 50) {
            TNT t = new TNT(new Point(75, i), this);
            addObstacle(t);
        }
        for (int i = 860; i < 960; i += 50) {
            TNT t = new TNT(new Point(615, i), this);
            addObstacle(t);
        }
        for (int i = 225; i < 325; i += 50) {
            SpikePit s = new SpikePit(new Point(75, i), this);
            addObstacle(s);
        }
        for (int i = 710; i < 810; i += 50) {
            SpikePit s = new SpikePit(new Point(615, i), this);
            addObstacle(s);
        }
    }

    /**
	 * This method returns the location that the PlayerTank is to start.
	 * 
	 * @return starting location of the PlayerTank
	 */
    @Override
    public Point playerStart() {
        return new Point(400, 100);
    }

    /**
	 * This method returns the location that the EnemyTank is to start.
	 * 
	 * @return starting location of the EnemyTank
	 */
    @Override
    public Point enemyStart() {
        return new Point(110, 880);
    }

    /**
	 * This method returns the number of the current level.
	 * 
	 * return level number of current level
	 */
    @Override
    public int getLevelNumber() {
        return 1;
    }
}
