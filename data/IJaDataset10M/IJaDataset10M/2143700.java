package cscomp2011;

import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JOptionPane;

public class BoardAction {

    private BoardConfig bc;

    private TreeMap<Integer, TreeMap<Integer, Block>> entirePuzzle = new TreeMap<Integer, TreeMap<Integer, Block>>();

    private TreeMap<Integer, TreeMap<Integer, Block>> colorBlob = new TreeMap<Integer, TreeMap<Integer, Block>>();

    public TreeMap<Integer, BlockPointer> checkList = new TreeMap<Integer, BlockPointer>();

    private IOScoring ios;

    /**
	 *This constructor takes care of generating either a random board or one
	 *based on a file. The loops open a board file from a path specified in the
	 * configuration file and create a board configuration, or generate a random
	 * board config following the parameters of the .properties file.
	 */
    public BoardAction(BoardConfig bc) {
        this.bc = bc;
        buildPuzzle();
        if (boardCompleted()) {
            System.out.println("Board has no Moves.  Please Restart and check the number of colors and/or stored board being loaded");
            JOptionPane.showMessageDialog(null, "Board has no Moves.  Please Restart and check the number of colors and/or stored board being loaded");
            System.exit(0);
        }
    }

    /**
	 * Method to call the recursive makeMove method.  Returns true if board solved.  Returns false if board is not solved.
	 * @param numColor
	 * @return boolean 
	 */
    public boolean makeMove(int numColor) {
        buildPuzzle();
        calculateBlob();
        changeBlob(numColor);
        storeMove();
        return (boardCompleted());
    }

    /**
	 * Method checks if only one color is present on the board.  Returns true if only one color is present.
	 * @return
	 */
    private boolean boardCompleted() {
        TreeSet<Integer> temp = new TreeSet<Integer>();
        for (int x : entirePuzzle.keySet()) for (int y : entirePuzzle.get(x).keySet()) {
            temp.add(entirePuzzle.get(x).get(y).getNumColor());
        }
        if (temp.size() > 1) return false; else return true;
    }

    /**
	 * Method changes the color of the defined blob.
	 * @param numColor
	 */
    private void changeBlob(int numColor) {
        for (int x : colorBlob.keySet()) {
            for (int y : colorBlob.get(x).keySet()) {
                entirePuzzle.get(x).get(y).setNumColor(numColor);
            }
        }
    }

    /**
	 * Converts the Block[][] structure to a treemap to allow for easy and faster working with board config data
	 */
    private void buildPuzzle() {
        Block[][] block = bc.getBoard();
        for (int x = 0; x < block.length; x++) {
            for (int y = 0; y < block[x].length; y++) if (entirePuzzle.containsKey(x)) entirePuzzle.get(x).put(y, block[x][y]); else {
                TreeMap<Integer, Block> temp = new TreeMap<Integer, Block>();
                temp.put(y, block[x][y]);
                entirePuzzle.put(x, temp);
            }
        }
    }

    /**
	 * Method starts the blob calculation method
	 * @param numColor
	 */
    private void calculateBlob() {
        colorBlob.clear();
        int numOldColor = entirePuzzle.get(0).get(0).getNumColor();
        addBlockToIterativeList(0, 0);
        calculateBlobIterative(numOldColor);
    }

    public void calculateBlobIterative(int numOldColor) {
        while (checkList.size() > 0) {
            BlockPointer tempPointer = getNextBlockPointer();
            int x = tempPointer.getX();
            int y = tempPointer.getY();
            if (entirePuzzle.get(x).get(y).getNumColor() == numOldColor) {
                if (colorBlob.containsKey(x)) {
                    colorBlob.get(x).put(y, entirePuzzle.get(x).get(y));
                } else {
                    TreeMap<Integer, Block> temp = new TreeMap<Integer, Block>();
                    temp.put(y, entirePuzzle.get(x).get(y));
                    colorBlob.put(x, temp);
                }
                if (inEntirePuzzle(x + 1, y)) if (!inPuzzle(x + 1, y, colorBlob)) addBlockToIterativeList(x + 1, y);
                if (inEntirePuzzle(x - 1, y)) if (!inPuzzle(x - 1, y, colorBlob)) addBlockToIterativeList(x - 1, y);
                if (inEntirePuzzle(x, y + 1)) if (!inPuzzle(x, y + 1, colorBlob)) addBlockToIterativeList(x, y + 1);
                if (inEntirePuzzle(x, y - 1)) if (!inPuzzle(x, y - 1, colorBlob)) addBlockToIterativeList(x, y - 1);
            }
        }
    }

    private void addBlockToIterativeList(int x, int y) {
        checkList.put((x + (y * GameProperties.blkPerRow)), new BlockPointer(x, y));
    }

    private BlockPointer getNextBlockPointer() {
        return checkList.pollFirstEntry().getValue();
    }

    /**
	 * Check if the X and Y coordinates are within the bounds of the given puzzle.  Returns true if found within puzzle and false if not within puzzle.
	 * @param x Coordinate
	 * @param y Coordinate
	 * @param treemap of the puzzle to check bounds
	 * @return boolean
	 */
    private boolean inPuzzle(int x, int y, TreeMap<Integer, TreeMap<Integer, Block>> treemap) {
        if (inEntirePuzzle(x, y) && treemap.containsKey(x)) if (treemap.get(x).containsKey(y)) return true;
        return false;
    }

    /**
	 * Check if the X and Y coordinates are within the bounds of the entire puzzle.  Returns true if found within puzzle and false if not within puzzle.
	 * @param x Coordinate
	 * @param y Coordinate
	 * @param treemap of the puzzle to check bounds
	 * @return boolean
	 */
    private boolean inEntirePuzzle(int x, int y) {
        if (x < 0 || y < 0 || x >= GameProperties.blkPerCol || y >= GameProperties.blkPerRow) return false;
        return true;
    }

    /**
	 * Method for placing the puzzle back in the Board Config class.
	 */
    private void storeMove() {
        Block[][] block = bc.getBoard();
        for (int x = 0; x < entirePuzzle.size(); x++) for (int y = 0; y < entirePuzzle.get(x).size(); y++) block[x][y] = entirePuzzle.get(x).get(y);
        bc.setBoard(block);
    }

    /**
	 * Return the number of pixels in the board
	 * @return
	 */
    public int getNumPixels() {
        return GameProperties.blkPerCol * GameProperties.blkPerRow;
    }

    /**
	 * Returns the current board
	 * @return
	 */
    public Block[][] getBoard() {
        return bc.getBoard();
    }

    /**
	 * Return the current number of moves that have been made
	 * @return
	 */
    public int getNumMoves() {
        return ios.getMoves();
    }

    /**
	 * Set the object reference to the IOScoring class
	 * @param ios
	 */
    public void setIOScoring(IOScoring ios) {
        this.ios = ios;
    }

    /**
	 * Print a TreeMap as a 2D array
	 * @param map
	 */
    @SuppressWarnings("unused")
    private void printMap(TreeMap<Integer, TreeMap<Integer, Block>> map) {
        System.out.println("Printing Treemap");
        for (int x : map.keySet()) {
            for (int y : map.get(x).keySet()) {
                System.out.print(" " + map.get(x).get(y).getNumColor());
            }
            System.out.println();
        }
    }

    /**
	 * Helper class for tracking X and Y values of target blocks
	 * @author 200015231
	 *
	 */
    class BlockPointer {

        private int x;

        private int y;

        public BlockPointer(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
