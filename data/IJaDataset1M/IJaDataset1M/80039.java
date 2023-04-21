package ExamplesJaCoP;

import java.util.ArrayList;
import JaCoP.constraints.Alldiff;
import JaCoP.constraints.Sum;
import JaCoP.core.IntVar;
import JaCoP.core.Store;
import JaCoP.core.Var;

/**
 * 
 * It is program to solve Kakro puzzles. 
 * 
 * @author Radoslaw Szymanek
 * 
 * This is a program which uses Constraint Programming to find the solution to a
 * simple Kakro puzzle. For a moment the problem representation does not allow 
 * to model the problems with fields which are both origins of the row and column word.
 */
public class Kakro extends Example {

    public IntVar[][] elements;

    public int noRows = 4;

    public int noColumns = 4;

    int[][] rowDescription = { { 0, 0, 0, 0 }, { 3, 1, 1, 0 }, { 6, 1, 1, 1 }, { 0, 5, 1, 1 } };

    int[][] columnDescription = { { 0, -4, -7, 0 }, { 0, 1, 1, -3 }, { 0, 1, 1, 1 }, { 0, 0, 1, 1 } };

    @Override
    public void model() {
        store = new Store();
        vars = new ArrayList<Var>();
        elements = new IntVar[noRows][noColumns];
        IntVar zero = new IntVar(store, "0", 0, 0);
        for (int i = 0; i < noRows; i++) for (int j = 0; j < noColumns; j++) if (rowDescription[i][j] == 1) {
            assert (columnDescription[i][j] == 1) : "Contradiction between row and column descriptions.";
            elements[i][j] = new IntVar(store, "f" + i + "-" + j, 1, 9);
            vars.add(elements[i][j]);
        } else elements[i][j] = zero;
        for (int i = 0; i < noRows; i++) for (int j = 0; j < noColumns; j++) if (rowDescription[i][j] > 1) {
            IntVar sum = new IntVar(store, "sumAt" + i + "-" + j, rowDescription[i][j], rowDescription[i][j]);
            ArrayList<IntVar> row = new ArrayList<IntVar>();
            for (int m = j + 1; m < noColumns && rowDescription[i][m] == 1; m++) row.add(elements[i][m]);
            store.impose(new Sum(row, sum));
            store.impose(new Alldiff(row));
        }
        for (int i = 0; i < noRows; i++) for (int j = 0; j < noColumns; j++) if (columnDescription[i][j] < 0) {
            IntVar sum = new IntVar(store, "sumCol" + i + "-" + j, -columnDescription[i][j], -columnDescription[i][j]);
            ArrayList<IntVar> column = new ArrayList<IntVar>();
            for (int m = i + 1; m < noRows && columnDescription[m][j] == 1; m++) column.add(elements[m][j]);
            store.impose(new Sum(column, sum));
            store.impose(new Alldiff(column));
        }
    }

    /**
	 * It executes the program to solve simple Kakro puzzle.
	 * @param args
	 */
    public static void main(String args[]) {
        Kakro example = new Kakro();
        example.model();
        if (example.search()) {
            System.out.println("Solution(s) found");
            Example.printMatrix(example.elements, example.noRows, example.noColumns);
        }
    }
}
