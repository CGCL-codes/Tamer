package be.smals.bowling.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import be.smals.bowling.bo.Bowl;
import be.smals.bowling.bo.Game;
import be.smals.bowling.exception.InvalidBowlException;

/**
 * Console to play bowling game. Implementation is not testable.
 * 
 * @author tdr
 * 
 * @since 1.0.0
 * 
 */
public class ConsoleNotTestable {

    public void start(Game game) {
        System.out.println("Starting Bowling game ...");
        while (!game.isGameOver()) {
            try {
                BufferedReader in = null;
                in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter how many pins you hit with your throw?");
                int lastBowled = Integer.parseInt(in.readLine());
                Bowl bowled = new Bowl(lastBowled);
                game.bowl(bowled);
            } catch (NumberFormatException e) {
                System.err.println("Not a valid number. Try again");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidBowlException e) {
                System.err.println("Not a valid Bowl. " + e.getMessage() + " \nTry again ");
            }
        }
    }
}
