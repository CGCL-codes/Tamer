package pt.xpand.xinx.players;

import java.util.Random;
import pt.xpand.xinx.entities.Board;
import pt.xpand.xinx.entities.Position;
import pt.xpand.xinx.entities.interfaces.IPlayer;

/**
 * @author Kiko
 * 
 * Implementation of the Red Player
 *
 */
public class PlayerBlindBat implements IPlayer {

    private String name;

    private String symbol;

    /**
	 * Constructor for PlayerRed
	 * 
	 * @param name
	 * @param color
	 */
    public PlayerBlindBat(String name, String symbol) {
        this.setName(name);
        this.setSymbol(symbol);
    }

    /**
	 * @param name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return name
	 */
    public String getName() {
        return name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    /**
	 * Decides the players next move
	 * 
	 * @return Vector2f containing the position to play
	 */
    public Position play(Board board) {
        Random generator = new Random();
        Position position = new Position(generator.nextInt(board.getSize()), generator.nextInt(board.getSize()));
        return position;
    }
}
