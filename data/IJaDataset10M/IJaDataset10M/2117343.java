package main;

import basics.Board;
import basics.Hint;
import basics.Tile;
import java.io.IOException;
import org.apache.log4j.Logger;
import solver.EternitySingleThreadSolver;
import basics.EternityBoard;
import io.EternityBoardSaver;
import io.EternityTileReader;
import exceptions.AlgorithmInstantiationException;
import exceptions.EmptyFileException;
import exceptions.HintException;
import exceptions.NoFileException;
import exceptions.PlacingException;

public class SingleThreadedEternity {

    private static final Logger LOGGER = Logger.getLogger(SingleThreadedEternity.class);

    private EternitySingleThreadSolver eternitySolver;

    private Tile[] eternityTiles = null;

    private Hint[] eternityHints = null;

    private EternityTileReader eternityReader;

    private Board eternityBoard;

    private Board solution = null;

    public SingleThreadedEternity(String tileFile, String hintFile, int dimX, int dimY, String algorithmType) throws PlacingException, AlgorithmInstantiationException {
        this.eternityReader = new EternityTileReader();
        try {
            try {
                this.eternityTiles = this.eternityReader.getTiles(tileFile);
            } catch (EmptyFileException e) {
                e.printStackTrace();
            }
            this.eternityHints = this.eternityReader.getHints(hintFile);
        } catch (IOException e) {
            LOGGER.error("File not found: " + e.getMessage());
        } catch (HintException e) {
            LOGGER.error(e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NoFileException e) {
            e.printStackTrace();
        }
        this.eternityBoard = new EternityBoard(dimX, dimY);
        this.eternitySolver = new EternitySingleThreadSolver(algorithmType, this.eternityBoard, this.eternityTiles, this.eternityHints);
    }

    public void calculate() {
        this.solution = null;
        this.solution = this.eternitySolver.calculateSolution();
        if (this.solution != null) {
            EternityBoardSaver.saveBoard(this.solution, "Solution.txt");
            LOGGER.info("Solution found and saved to Solution.txt");
        } else {
            LOGGER.info("no Solution found");
        }
    }

    /**
	 * Signal whether the algorithm has successfully computed a solution
	 * @return
	 */
    public boolean solved() {
        return this.solution != null;
    }

    public Board getSolution() {
        return this.solution;
    }
}
