package model;

import java.util.*;
import java.io.*;

/**
 * GameModel is the main class of the game's model. It can see and control
 * all aspects of the game, and can notify the view and controller of changes
 * to the game state by managing a list of GameListeners.
 * Its functions include loading gameLevels from file, loading them up, controlling
 * the game Characters, executing move functions and beginning and ending the game.
 * Note: GameModel can function in single level mode if no LevelPack is provided,
 * but a GameLevel is.
 * 
 * @author Alex Dinardo
 * @author Alexander Craig
 * @author Robert Gillespie
 */
public class GameModel {

    /**The number of lives a player is to start the game with.*/
    private static final int PLAYERS_STARTING_LIVES = 3;

    /** The game's main human controlled Character.*/
    private Player player;

    /** A List of Ghost Characters.*/
    private List<Ghost> ghosts;

    /** The current GameLevel that is loaded.*/
    private GameLevel level;

    /** A List of all Level files the game can access and load.*/
    private LevelPack levels;

    /** A list of all listeners to this game object */
    private List<GameListener> listeners;

    /** stores whether pacman can make a move at this point */
    private boolean inProgress;

    /**
	 * Loads a game using the given LevelPack, and loads the first level.
	 * @param levels	the level pack to use for this instance of the game.
	 */
    public GameModel(LevelPack levels) {
        listeners = new ArrayList<GameListener>();
        this.levels = levels;
        loadLevel(this.levels.getNextLevel());
        ghosts = new ArrayList<Ghost>();
        inProgress = true;
        levels = null;
        resetGame();
    }

    /**
	 * Loads a game using the given GameLevel. This function is mostly
	 * for testing purposes, as the full game will use a list of
	 * file names to generate levels.
	 * @param levelListFilename
	 */
    public GameModel(GameLevel level) {
        this.level = level;
        ghosts = new ArrayList<Ghost>();
        for (int i = 0; i < level.getNumSpawnPoints(); i++) {
            ghosts.add(new Ghost(level.getSpawnPoint(i).getSpawnTile(), player));
        }
        inProgress = true;
        levels = null;
        listeners = new ArrayList<GameListener>();
        resetGame();
    }

    /**
	 * Loads a level from file and loads all of its attributes. Does nothing if no file exists.
	 * @param fileName The name of the file to load from.
	 */
    public void loadLevel(String fileName) {
        File levelFile = new File("levels\\" + fileName);
        this.level = GameLevel.importLevel(levelFile);
    }

    /**
	 * Makes the Player move in the specified Direction, then immediately 
	 * calls updateGhosts() to move the ghosts.
	 * @param dir the Direction to move in.
	 * @return True if the move was executed. False if the move couldn't execute 
	 * 			(ie. there is a wall in that direction).
	 */
    public boolean makeMove(Direction dir) {
        if (player.isDead() || !inProgress) {
            return false;
        }
        boolean hasMoved = false;
        if (player.move(dir)) {
            hasMoved = true;
        }
        if (hasMoved || dir == null) {
            updateGhosts();
            for (GameListener l : listeners) {
                l.playerMove(new GameEvent(this));
            }
            stateCheck();
        }
        return hasMoved;
    }

    /**
	 * @return	true if the player is currently on the last level of the level pack
	 */
    public boolean lastLevel() {
        if (levels != null) {
            return levels.isLastLevel();
        } else {
            return true;
        }
    }

    /**
	 * After a Player has moved, this method iterates through the ghosts List and calls
	 * upon their update methods (ie. determine direction of travel, then move self.)
	 */
    private void updateGhosts() {
        for (Ghost g : ghosts) {
            Tile next = g.getNextMove();
            for (Ghost otherGhost : ghosts) {
                if (otherGhost != g && next == otherGhost.getTile()) {
                    next = null;
                    g.setLastMove(null);
                }
            }
            g.setTile(next);
        }
    }

    /**
	 * Checks the various events (player death, level complete) that can
	 * happen during the game, and sends events accordingly.
	 */
    private void stateCheck() {
        for (Ghost g : ghosts) {
            if (g.getTile() == player.getTile()) {
                if (!player.loseLife()) {
                    for (GameListener l : listeners) {
                        l.playerDeath(new GameEvent(this));
                    }
                } else {
                    for (GameListener l : listeners) {
                        l.gameOver(new GameEvent(this));
                    }
                }
                inProgress = false;
                break;
            }
        }
        if (level.getMaze().isComplete()) {
            for (GameListener l : listeners) {
                l.levelComplete(new GameEvent(this));
            }
        }
    }

    /**
	 * @return The Player Character
	 */
    public Player getPlayer() {
        return player;
    }

    /**
	 * @return	The level currently being played
	 */
    public GameLevel getLevel() {
        return level;
    }

    /**
	 * @param id The index of the Ghost within the list.
	 * @return The Ghost at the specified index.
	 */
    public Ghost getGhost(int id) {
        if (id < 0 || id >= ghosts.size()) return null;
        return ghosts.get(id);
    }

    /**
	 * @return the number of ghosts currently in the maze.
	 */
    public int getNumGhosts() {
        return ghosts.size();
    }

    /**
	 * Resets the entire game (reloads the level, and resets the player's lives to 3).
	 */
    public void resetGame() {
        player = new Player(level.getPlayerStart(), PLAYERS_STARTING_LIVES);
        this.resetLevel();
        for (GameListener l : listeners) {
            l.newGame(new GameEvent(this));
        }
    }

    /**
	 * Loads the next level, if one exists.
	 */
    public void loadNextLevel() {
        if (levels != null) {
            String nextLevel = levels.getNextLevel();
            if (nextLevel == null) {
                for (GameListener l : listeners) {
                    l.gameOver(new GameEvent(this));
                }
            } else {
                loadLevel(nextLevel);
                if (level == null) {
                    System.err.println("Required level file " + nextLevel + " does not exist.");
                    System.exit(1);
                }
                resetLevel();
                player.nextLevel();
            }
        }
    }

    /**
	 * Has the level reset to its initial load state (ie. Characters return
	 * start positions, all Tiles have dots put back in them.)
	 */
    public void resetLevel() {
        player.setTile(level.getPlayerStart());
        player.setLastMove(null);
        ghosts.clear();
        for (int i = 0; i < level.getNumSpawnPoints(); i++) {
            SpawnPoint ghostSpawn = level.getSpawnPoint(i);
            switch(ghostSpawn.getSpawnType()) {
                case GHOST_HORIZONTAL:
                    ghosts.add(new Ghost(ghostSpawn.getSpawnTile(), player, Ghost.HORIZONTAL));
                    break;
                case GHOST_VERTICAL:
                    ghosts.add(new Ghost(ghostSpawn.getSpawnTile(), player, Ghost.VERTICAL));
                    break;
                case GHOST_RANDOM:
                    ghosts.add(new Ghost(ghostSpawn.getSpawnTile(), player, Ghost.RANDOM));
                    break;
                default:
                    break;
            }
        }
        level.getMaze().resetMaze();
        for (GameListener l : listeners) {
            l.playerMove(new GameEvent(this));
            l.levelReset(new GameEvent(this));
        }
        inProgress = true;
    }

    /**
	 * Adds a game listener to the list of objects listening
	 * on this GameModel.
	 * @param listener	the listener to add to the list
	 */
    public void addGameListener(GameListener listener) {
        listeners.add(listener);
    }

    /**
	 * Removes a game listener to the list of objects listening
	 * on this GameModel.
	 * @param listener	the listener to remove the list
	 */
    public void removeGameListener(GameListener listener) {
        listeners.remove(listener);
    }

    /**
	 * Removes all listeners of this GameModel.
	 */
    public void removeAllListeners() {
        listeners.clear();
    }

    /**
	 * @return The currently loaded Maze.
	 */
    public Maze getMaze() {
        return level.getMaze();
    }
}
