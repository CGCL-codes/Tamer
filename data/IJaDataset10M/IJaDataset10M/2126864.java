package conga;

import java.io.*;

/**
 *
 * @author Geetha
 */
public class Game {

    public static enum Player {

        A, B, NONE
    }

    public AgentInterface PlayerA, PlayerB, currentPlayer;

    Board boardState;

    int numWinsA = 0, numForever = 0, totalMoves = 0, avgNumOfMovesToWin = 0;

    int totalNumberOfMovesMade = 0, sumOfAvgNumberOfMovesExploredPerMove = 0;

    long time;

    FileOutputStream out;

    PrintStream p;

    public Game() {
        PlayerA = new AgentAlphaBeta2(Player.A, this, 5);
        PlayerB = new AgentRandom(Player.B, this);
        this.init();
    }

    /** Creates a new instance of Game */
    public Game(boolean garbage) {
        try {
            out = new FileOutputStream("Stats" + System.currentTimeMillis() + ".txt");
            p = new PrintStream(out);
            int numTrials = 5;
            long startTime;
            for (int d = 3; d < 4; d++) {
                for (int i = 0; i < numTrials; i++) {
                    startTime = System.currentTimeMillis();
                    PlayerA = new AgentB(Player.A, this, d);
                    PlayerB = new AgentRandom(Player.B, this);
                    this.init();
                    time += System.currentTimeMillis() - startTime;
                }
                time = time / numTrials;
                avgNumOfMovesToWin = totalMoves / (numWinsA);
                print(PlayerA.toString(), PlayerB.toString(), PlayerA.depth, numWinsA, numForever, avgNumOfMovesToWin, sumOfAvgNumberOfMovesExploredPerMove / numWinsA, time);
                resetFields();
            }
        } catch (Exception e) {
            System.err.println("Error writing to file: " + e.toString());
        } finally {
            p.close();
        }
    }

    public void resetFields() {
        numWinsA = 0;
        numForever = 0;
        totalMoves = 0;
        avgNumOfMovesToWin = 0;
        totalNumberOfMovesMade = 0;
        sumOfAvgNumberOfMovesExploredPerMove = 0;
        time = 0;
    }

    public void print(String a, String b, int depth, int numAWins, int numOfNoWins, double avgNumberOfMovesToWin, double AvgOfAvgNumberOfMovesExploredPerMove, long time) throws IOException {
        printMe("", a + " vs " + b);
        printMe("Depth: ", depth + "");
        printMe("Number of A Wins: ", numAWins + "");
        printMe("Average Number of Moves Explored/Move on a win: ", AvgOfAvgNumberOfMovesExploredPerMove + "");
        printMe("Average Number of Moves Made until Win: ", avgNumberOfMovesToWin + "");
        printMe("Time: ", time + "");
        p.print("\n");
    }

    public void printMe(String a, String s) {
        System.out.println(a + s);
        p.print(s + "\t");
    }

    public void init() {
        currentPlayer = PlayerA;
        boardState = new Board();
        beginGame();
    }

    public void beginGame() {
        boolean gameOver = false;
        int i = 0;
        boardState.print();
        while (!gameOver && i < 1000) {
            Move playerMove = currentPlayer.getMove(boardState);
            i++;
            if (playerMove != null && isLegal(boardState, currentPlayer.myPlayer, playerMove)) {
                System.out.println(i + " move:  Player " + currentPlayer.myPlayer.toString() + " Makes Move: " + playerMove.toString() + "  " + playerMove.eval);
                makeMove(boardState, currentPlayer.myPlayer, playerMove);
                boardState.print();
                currentPlayer = nextPlayer(currentPlayer);
                gameOver = isGameOver(boardState, currentPlayer.myPlayer);
            } else {
                gameOver = true;
            }
        }
        if (i >= 1000) numForever++; else {
            totalMoves += i;
            totalNumberOfMovesMade += PlayerA.numberOfMoves;
            if (PlayerA.numberOfMoves > 0) {
                sumOfAvgNumberOfMovesExploredPerMove += PlayerA.totalNumberOfMovesExplored / PlayerA.numberOfMoves;
                numWinsA++;
            }
        }
        System.out.println("Game over");
    }

    public static void makeMove(Board board, Game.Player player, Move move) {
        Block currentBlock, nextBlock, nextNextBlock, lastBlock;
        currentBlock = board.board[move.squareX][move.squareY];
        nextBlock = getNextBlock(board, move);
        if (nextBlock == null) return;
        moveStones(currentBlock, nextBlock, 1, currentBlock.player);
        nextNextBlock = getNextBlock(board, new Move(move.direction, nextBlock.x, nextBlock.y));
        if (nextNextBlock == null || !isLegal(board, player, new Move(move.direction, nextBlock))) {
            moveStones(currentBlock, nextBlock, currentBlock.numberOfStones, currentBlock.player);
            return;
        }
        moveStones(currentBlock, nextNextBlock, 2, currentBlock.player);
        lastBlock = getNextBlock(board, new Move(move.direction, nextNextBlock.x, nextNextBlock.y));
        if (nextNextBlock == null || !isLegal(board, player, new Move(move.direction, nextNextBlock))) {
            moveStones(currentBlock, nextNextBlock, currentBlock.numberOfStones, currentBlock.player);
            return;
        }
        moveStones(currentBlock, lastBlock, currentBlock.numberOfStones, currentBlock.player);
    }

    public static void moveStones(Block fromBlock, Block toBlock, int number, Player p) {
        if (number > 0 && fromBlock.numberOfStones >= number) {
            fromBlock.numberOfStones -= number;
            if (fromBlock.numberOfStones == 0) fromBlock.player = Player.NONE;
            toBlock.numberOfStones += number;
            toBlock.player = p;
        }
    }

    public AgentInterface nextPlayer(AgentInterface currentPlayer) {
        if (currentPlayer.myPlayer.equals(Player.A)) return PlayerB; else if (currentPlayer.myPlayer.equals(Player.B)) return PlayerA; else return null;
    }

    public static boolean isGameOver(Board board, Game.Player player) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Move.Direction[] dir = Move.Direction.values();
                for (int k = 0; k < dir.length; k++) {
                    if (isLegal(board, player, new Move(dir[k], i, j))) return false;
                }
            }
        }
        return true;
    }

    public static boolean isLegal(Board board, Game.Player player, Move move) {
        Block nextBlock = getNextBlock(board, move);
        Block currentBlock = board.board[move.squareX][move.squareY];
        if (currentBlock.numberOfStones > 0 && currentBlock.player.equals(player) && nextBlock != null && (nextBlock.player.equals(Game.Player.NONE) || nextBlock.player.equals(player))) {
            return true;
        }
        return false;
    }

    public static Block getNextBlock(Board board, Move move) {
        int x = 0, y = 0;
        if (move.direction == Move.Direction.N || move.direction == Move.Direction.NW || move.direction == Move.Direction.NE) y = -1;
        if (move.direction == Move.Direction.S || move.direction == Move.Direction.SW || move.direction == Move.Direction.SE) y = 1;
        if (move.direction == Move.Direction.W || move.direction == Move.Direction.SW || move.direction == Move.Direction.NW) x = -1;
        if (move.direction == Move.Direction.E || move.direction == Move.Direction.SE || move.direction == Move.Direction.NE) x = 1;
        if (move.squareX + y >= 0 && move.squareY + x >= 0 && move.squareX + y < 4 && move.squareY + x < 4) return board.board[move.squareX + y][move.squareY + x];
        return null;
    }
}
