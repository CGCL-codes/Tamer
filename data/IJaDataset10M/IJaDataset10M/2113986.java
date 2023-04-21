package com.roslan.games.moo3d;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.openmali.FastMath;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point2i;

/**
 * Comments go here.
 * 
 * @author Roslan Amir
 * @version 1.0 - Aug 1, 2009
 */
public class StarManager {

    /**
	 * Log4J object for logging purposes.
	 */
    private static final Logger logger = Logger.getLogger("com.roslan.games");

    /**
	 * Singleton instance of this class.
	 */
    private static final StarManager starManager = new StarManager();

    /**
	 * Default no-arg constructor. Made private.
	 */
    private StarManager() {
    }

    /**
	 * Returns the singleton instance of this class.
	 * 
	 * @return StarManager
	 */
    public static StarManager getStarManager() {
        return starManager;
    }

    /**
	 * The minimum distance (in parsecs) that should be between the stars.
	 */
    private static final int PARSEC_OFFSET = GameManager.PARSEC_SIZE * 2;

    /**
	 * The radius of the ring representing the star highlight.
	 */
    public static final float STAR_HIGHLIGHT_RADIUS = 4.5f;

    /**
	 * The reference to the player's home star node.
	 */
    public StarNode playerHomeStarNode;

    /**
	 * The reference to the currently selected star or colony node.
	 */
    public StarNode currentStarNode;

    /**
	 * The node representing the ring to highlight the currently selected star.
	 */
    public Highlight starHighlight;

    /**
	 * The list containing all the stars in the game.
	 */
    private StarList starList;

    /**
	 * The reference to the singleton GameManager object.
	 */
    private GameManager gameManager;

    /**
	 * The reference to the singleton FleetManager object.
	 */
    private FleetManager fleetManager;

    /**
	 * Initialization method.
	 */
    public void init() {
        gameManager = GameManager.getGameManager();
        fleetManager = FleetManager.getFleetManager();
        starHighlight = new Highlight(STAR_HIGHLIGHT_RADIUS, Colorf.YELLOW);
        gameManager.rootNode.addChild(starHighlight);
    }

    /**
	 * Create all the stars and the nodes representing them.
	 */
    public void createStars() {
        starList = new StarList(gameManager.starCount);
        boolean[][] bitmap = new boolean[gameManager.galaxyWidth + 1][gameManager.galaxyHeight + 1];
        int x = FastMath.randomInt(PARSEC_OFFSET, gameManager.galaxyWidth - PARSEC_OFFSET);
        int y = FastMath.randomInt(PARSEC_OFFSET, gameManager.galaxyHeight - PARSEC_OFFSET);
        updateBitmap(bitmap, x, y);
        StarNode orionNode = StarNode.createOrionNode(x, y);
        starList.add(orionNode);
        ArrayList<Point2i> homePoints = new ArrayList<Point2i>(6);
        int sectorWidth = gameManager.galaxyWidth / 3;
        int sectorHeight = gameManager.galaxyHeight / 2;
        int minX = PARSEC_OFFSET;
        int maxX = sectorWidth - PARSEC_OFFSET;
        int minY;
        int maxY;
        for (int col = 0; col < 3; col++) {
            minY = PARSEC_OFFSET;
            maxY = sectorHeight - PARSEC_OFFSET;
            for (int row = 0; row < 2; row++) {
                do {
                    x = FastMath.randomInt(minX, maxX);
                    y = FastMath.randomInt(minY, maxY);
                } while (bitmap[x][y]);
                homePoints.add(new Point2i(x, y));
                updateBitmap(bitmap, x, y);
                minY += sectorHeight;
                maxY += sectorHeight;
            }
            minX += sectorWidth;
            maxX += sectorWidth;
        }
        Point2i point = homePoints.remove(FastMath.randomInt(0, homePoints.size()));
        playerHomeStarNode = StarNode.createHomeStarNode(point.x(), point.y(), gameManager.playerRace);
        StarNameText text = new StarNameText(playerHomeStarNode.getName());
        playerHomeStarNode.addChild(text);
        ScannerCircle circle = new ScannerCircle(gameManager.playerRace.getScannerRange() * GameManager.PARSEC_SIZE, gameManager.playerRace.getRaceScannerColor());
        playerHomeStarNode.addChild(circle);
        starList.add(playerHomeStarNode);
        orionNode.setRange(playerHomeStarNode);
        for (int index = 1; index < gameManager.races.length; index++) {
            point = homePoints.remove(FastMath.randomInt(0, homePoints.size()));
            StarNode aiRaceHomeNode = StarNode.createHomeStarNode(point.x(), point.y(), gameManager.races[index]);
            starList.add(aiRaceHomeNode);
            aiRaceHomeNode.setRange(playerHomeStarNode);
        }
        minX = PARSEC_OFFSET;
        maxX = gameManager.galaxyWidth - PARSEC_OFFSET;
        minY = PARSEC_OFFSET;
        maxY = gameManager.galaxyHeight - PARSEC_OFFSET;
        for (int index = gameManager.opponentCount + 2; index < gameManager.starCount; index++) {
            do {
                x = FastMath.randomInt(minX, maxX);
                y = FastMath.randomInt(minY, maxY);
            } while (bitmap[x][y]);
            updateBitmap(bitmap, x, y);
            StarNode starNode = StarNode.createRandomStar(x, y);
            starList.add(starNode);
            starNode.setRange(playerHomeStarNode);
        }
        currentStarNode = playerHomeStarNode;
    }

    /**
	 * Once a star is placed, mark its tile and the surrounding tiles as being unavailable.
	 * 
	 * @param bitmap the bitmap array
	 * @param x the x-coordinate of the star
	 * @param y the y-coordinate of the star
	 */
    private void updateBitmap(boolean[][] bitmap, int x, int y) {
        int threshold = PARSEC_OFFSET * PARSEC_OFFSET;
        for (int i = x - PARSEC_OFFSET; i <= x + PARSEC_OFFSET; i++) {
            for (int j = y - PARSEC_OFFSET; j <= y + PARSEC_OFFSET; j++) {
                if ((x - i) * (x - i) + (y - j) * (y - j) < threshold) {
                    bitmap[i][j] = true;
                }
            }
        }
    }

    /**
	 * Comment for method.
	 */
    public void updateStarHiglight() {
        if (!starHighlight.isRenderable()) {
            starHighlight.setRenderable(true);
            fleetManager.fleetHighlight.setRenderable(false);
        }
        starHighlight.setPosition(starManager.currentStarNode.getPosition());
        starHighlight.updateTransform();
    }

    /**
	 * Find the previous colony belonging to the player.
	 */
    public void findPreviousColony() {
        logger.info("Inside findPreviousColony()");
        int currentStarIndex = starList.indexOf(currentStarNode);
        do {
            if (currentStarIndex == 0) currentStarIndex = starList.size();
            currentStarIndex--;
            currentStarNode = starList.get(currentStarIndex);
        } while (currentStarNode.getStar().getOwner() != gameManager.playerRace);
    }

    /**
	 * Find the next colony belonging to the player.
	 */
    public void findNextColony() {
        logger.info("Inside findNextColony()");
        int currentStarIndex = starList.indexOf(currentStarNode);
        do {
            currentStarIndex++;
            if (currentStarIndex == starList.size()) currentStarIndex = 0;
            currentStarNode = starList.get(currentStarIndex);
        } while (currentStarNode.getStar().getOwner() != gameManager.playerRace);
    }
}
