package controller;

import model.ChessboardModel;
import java.util.ArrayList;
import jpl.Query;
import jpl.Compound;
import jpl.Term;

/**
 * Clase de servicios para el proyecto
 * @author Samuel Chávez, Axel Mayorga
 */
public class UtilityManager {

    public static final String VERTICAL = "v";

    public static final String HORIZONTAL = "h";

    public static final String DIAGONAL_1 = "d1";

    public static final String DIAGONAL_2 = "d2";

    public static ArrayList<ChessboardModel> findChessboardState(Query q, boolean jf) {
        ArrayList<ChessboardModel> sols = new ArrayList<ChessboardModel>();
        if (q == null) {
            return sols;
        }
        while (q.hasMoreSolutions()) {
            ChessboardModel nuCS = new ChessboardModel();
            Term[] ta = ((Compound) q.nextSolution().get("Solution")).toTermArray();
            for (int i = 0; i < ta.length; i++) nuCS.addQueenAt(ta[i].intValue());
            sols.add(nuCS);
            if (jf) break;
        }
        return sols;
    }

    public static ArrayList<String> findFail(Query q) {
        ArrayList<String> computedFails = new ArrayList<String>();
        if (q == null) return computedFails;
        while (q.hasMoreSolutions()) computedFails.add(q.nextSolution().get("Solution").toString());
        return computedFails;
    }

    /**
     * Generates prolog string to get chessboard(cs) solutions 
     * @param cs Chessboard's state (queen positions)
     * @return SWI-Prolog query
     */
    public static String getQuerySol(ChessboardModel cs) {
        String sQuery = "getSolNQueens(@width, @height, @kQueens, @ChessboardState, Solution)";
        sQuery = sQuery.replace("@height", cs.getHeight() + "");
        sQuery = sQuery.replace("@width", cs.getWidth() + "");
        sQuery = sQuery.replace("@kQueens", cs.getkQueen() + "");
        sQuery = sQuery.replace("@ChessboardState", cs.getQueens().toString());
        return sQuery;
    }

    /**
     * Generates prolog string to check if new queen position does not enter in 
     * conflict with previously chessboard state.
     * @param cs Chessboard's state (queen positions)
     * @param newPosition new position's
     * @return SWI-Prolog query
     */
    public static String getQueryCheckFail(ChessboardModel cs, int newPosition) {
        String sQuery = "failNewPos(@newPosition, @width, @height, @ChessboardState, Solution)";
        sQuery = sQuery.replace("@width", cs.getWidth() + "");
        sQuery = sQuery.replace("@height", cs.getHeight() + "");
        sQuery = sQuery.replace("@newPosition", newPosition + "");
        sQuery = sQuery.replace("@ChessboardState", cs.getQueens().toString());
        return sQuery;
    }

    /**
     * Manejador de errores, es el método que muestra con colores las casillas en conflicto
     * @param fails
     * @param currentQueen
     * @param cm 
     */
    public static void failHandler(ArrayList<String> fails, QueenController currentQueen, ChessboardModel cm) {
        for (String fail : fails) {
            if (fail.equals(VERTICAL)) {
                cm.addInstructionAt("Vertical");
                failVertical(cm.getQueenControllers(), cm.getWidth(), cm.getHeight(), currentQueen.model.getNumber() % cm.getWidth());
            }
            if (fail.equals(HORIZONTAL)) {
                cm.addInstructionAt("Horizontal");
                failHorizontal(cm.getQueenControllers(), cm.getWidth(), currentQueen.model.getNumber() / cm.getWidth());
            }
            if (fail.equals(DIAGONAL_1)) {
                cm.addInstructionAt("Diagonal 1");
                failDiagonal_1(cm.getQueenControllers(), cm.getWidth(), cm.getHeight(), currentQueen.model.getNumber() % cm.getWidth(), currentQueen.model.getNumber() / cm.getWidth());
            }
            if (fail.equals(DIAGONAL_2)) {
                cm.addInstructionAt("Diagonal 2");
                failDiagonal_2(cm.getQueenControllers(), cm.getWidth(), cm.getHeight(), currentQueen.model.getNumber() % cm.getWidth(), currentQueen.model.getNumber() / cm.getWidth());
            }
        }
    }

    /**
     * Encargado de manejar los errores en posiciones verticales
     * @param queens
     * @param columns
     * @param rows
     * @param column 
     */
    private static void failVertical(ArrayList<QueenController> queens, int columns, int rows, int column) {
        for (int row = 0; row < rows; row++) {
            queens.get((row * columns) + column).view.showFail();
        }
    }

    /**
     * Encargado de manejar los errores en posiciones horizontales
     * @param queens
     * @param columns
     * @param row 
     */
    private static void failHorizontal(ArrayList<QueenController> queens, int columns, int row) {
        for (int column = 0; column < columns; column++) {
            queens.get(column + (columns * row)).view.showFail();
        }
    }

    /**
     * Encargado de manejar los errores en posición diagonal
     * @param queens
     * @param columns
     * @param rows
     * @param column
     * @param row 
     */
    private static void failDiagonal_1(ArrayList<QueenController> queens, int columns, int rows, int column, int row) {
        int rowPlus = row;
        int rowSub = row - 1;
        int colPlus = column;
        int colSub = column - 1;
        while (true) {
            if (rowSub >= 0 && colSub >= 0) {
                queens.get(colSub + (columns * rowSub)).view.showFail();
            }
            if (rowPlus < rows && colPlus < columns) {
                queens.get(colPlus + (columns * rowPlus)).view.showFail();
            }
            if ((rowPlus > rows || colPlus > columns) && (rowSub < 0 || colSub < 0)) {
                break;
            }
            rowPlus++;
            colPlus++;
            rowSub--;
            colSub--;
        }
    }

    /**
     * Encargado de manejar los errores en posición diagonal
     * @param queens
     * @param columns
     * @param rows
     * @param column
     * @param row 
     */
    private static void failDiagonal_2(ArrayList<QueenController> queens, int columns, int rows, int column, int row) {
        int rowPlus = row + 1;
        int rowSub = row;
        int colPlus = column;
        int colSub = column - 1;
        while (true) {
            if (rowSub >= 0 && colPlus < columns) {
                queens.get(colPlus + (columns * rowSub)).view.showFail();
            }
            if (rowPlus < rows && colSub >= 0) {
                queens.get(colSub + (columns * rowPlus)).view.showFail();
            }
            if ((rowPlus > rows || colSub < 0) && (rowSub < 0 || colPlus > columns)) {
                break;
            }
            rowPlus++;
            colPlus++;
            rowSub--;
            colSub--;
        }
    }

    public static void clearFails(ArrayList<QueenController> queens) {
        for (QueenController qc : queens) {
            qc.view.clearFail();
        }
    }
}
