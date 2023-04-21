package tttCore;

import java.util.Random;

/** EasyAI *****************************************************************
 * 
 * A class that implements the AI interface. The EasyAI does not try to win 
 * it places its piece at random.
 * 
 * ****************************************************************************/
public class EasyAI implements AI {

    private char aiPiece;

    /** Constructor *****************************************************************
	 * 
	 * Simply creates an object with a piece opposite of the player's.
	 * 
	 * @pre		-	
	 * @params	- aiIsX: True if the player wants to play as O
	 * @returns - 
	 * @post	- 
	 * ****************************************************************************/
    public EasyAI(boolean aiIsX) {
        if (aiIsX) aiPiece = 'X'; else aiPiece = 'O';
    }

    /** chooseMove *****************************************************************
	 * 
	 * Randomly places a piece in an open position.
	 * @pre		-	
	 * @params	- board: Used to check the current board's open positions
	 * @returns - The AI's Move with the corresponding row, col index and its piece.
	 * @post	-
	 * ****************************************************************************/
    public Move chooseMove(Board board) {
        Move aiMove;
        Random gen = new Random();
        int i = gen.nextInt(3);
        int j = gen.nextInt(3);
        boolean empty = false;
        while (!empty) if (board.get(i, j) != 'N') {
            i = gen.nextInt(3);
            j = gen.nextInt(3);
        } else {
            empty = true;
        }
        aiMove = new Move(i, j, aiPiece);
        return aiMove;
    }

    @Override
    public char getPiece() {
        return aiPiece;
    }
}
