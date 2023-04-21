package ExamplesJaCoP;

import java.util.ArrayList;
import JaCoP.constraints.SumWeight;
import JaCoP.constraints.XgteqC;
import JaCoP.constraints.knapsack.Knapsack;
import JaCoP.core.IntDomain;
import JaCoP.core.IntVar;
import JaCoP.core.Store;
import JaCoP.core.Var;

/**
 *
 * It specifies a simple diet problem.
 * 
 * Problem from http://www.mcs.vuw.ac.nz/courses/OPRE251/2006T1/Labs/lab09.pdf
 * 
 *  My diet requires that all the food I eat come from one of the four .basic 
 *  food groups. (chocolate cake, ice cream, soft drink, and cheesecake). 
 *  Each (large) slice of chocolate cake costs 50c, 
 *  each scoop of chocolate ice cream costs 20c, 
 *  each bottle of cola costs 30c, 
 *  and each piece of pineapple cheesecake costs 80c. 
 *
 *  Each day, I must ingest at least 500 calories, 
 *  6 oz of chocolate, 
 *  10 oz of sugar, 
 *  and 8 oz of fat.
 *  The nutritional content per unit of each food is shown in the table below. 
 * 
 *  Formulate a linear programming model that can be used to satisfy my daily 
 *  nutritional requirement at minimum cost.

 *  Type of                        Calories   Chocolate    Sugar    Fat
 *  Food                                      (ounces)     (ounces) (ounces)
 *  Chocolate Cake (1 slice)       400           3            2      2
 *  Chocolate ice cream (1 scoop)  200           2            2      4
 *  Cola (1 bottle)                150           0            4      1
 *  Pineapple cheesecake (1 piece) 500           0            4      5
 *
 * """  
 *
 * Compare with my MiniZinc model:
 * http://www.hakank.org/minizinc/diet1.mzn
 *
 */
public class Diet extends Example {

    IntVar[] x;

    int n = 4;

    int m = 4;

    String[] food = { "Chocolate Cake", "Chocolate ice cream", "Cola", "Pineapple cheesecake" };

    String[] ingredients = { "Calories", "Chocolate", "Sugar", "Fat" };

    int[] price = { 50, 20, 30, 80 };

    int[] limits = { 500, 6, 10, 8 };

    int[][] matrix = { { 400, 200, 150, 500 }, { 3, 2, 0, 0 }, { 2, 2, 4, 4 }, { 2, 4, 1, 5 } };

    /**
     *
     *  Imposes the model of the problem.
     *
     */
    @Override
    public void model() {
        store = new Store();
        x = new IntVar[m];
        for (int i = 0; i < m; i++) {
            x[i] = new IntVar(store, "x_" + i, 0, 10);
        }
        IntVar[] sums = new IntVar[n];
        for (int i = 0; i < n; i++) {
            sums[i] = new IntVar(store, "sums_" + i, 0, IntDomain.MaxInt);
            store.impose(new SumWeight(x, matrix[i], sums[i]));
            store.impose(new XgteqC(sums[i], limits[i]));
        }
        cost = new IntVar(store, "cost", 0, 120);
        store.impose(new SumWeight(x, price, cost));
        vars = new ArrayList<Var>();
        for (Var v : x) vars.add(v);
    }

    /**
    *
    *  Imposes the model of the problem.
    *
    */
    public void modelKnapsack() {
        store = new Store();
        x = new IntVar[m];
        for (int i = 0; i < m; i++) {
            x[i] = new IntVar(store, "x_" + i, 0, 10);
        }
        cost = new IntVar(store, "cost", 0, 120);
        for (int i = 0; i < n; i++) {
            IntVar minReq = new IntVar(store, "limit" + i, limits[i], IntDomain.MaxInt);
            if (i != 1) store.impose(new Knapsack(matrix[i], price, x, cost, minReq)); else {
                store.impose(new SumWeight(x, matrix[i], minReq));
            }
        }
        vars = new ArrayList<Var>();
        for (Var v : x) vars.add(v);
    }

    /**
     * It executes the program optimizing the diet.
     * @param args no argument is used.
     */
    public static void main(String args[]) {
        Diet diet = new Diet();
        diet.model();
        System.out.println("Searching for optimal using sum weight constraints");
        if (diet.searchOptimal()) {
            System.out.println("Cost: " + diet.cost.value());
            for (int i = 0; i < diet.m; i++) {
                System.out.println(diet.food[i] + ": " + diet.x[i].value());
            }
        } else {
            System.out.println("No solution.");
        }
        diet = new Diet();
        diet.modelKnapsack();
        System.out.println("Searching for optimal using knapsack constraints");
        if (diet.searchOptimal()) {
            System.out.println("Cost: " + diet.cost.value());
            for (int i = 0; i < diet.m; i++) {
                System.out.println(diet.food[i] + ": " + diet.x[i].value());
            }
        } else {
            System.out.println("No solution.");
        }
        diet = new Diet();
        diet.model();
        System.out.println("Searching for all solutions using sum weight constraints");
        if (diet.searchAllAtOnce()) {
            System.out.println("Cost: " + diet.cost.value());
            for (int i = 0; i < diet.m; i++) {
                System.out.println(diet.food[i] + ": " + diet.x[i].value());
            }
        } else {
            System.out.println("No solution.");
        }
        diet = new Diet();
        diet.modelKnapsack();
        System.out.println("Searching for all solutions using knapsack constraints");
        if (diet.searchAllAtOnce()) {
            System.out.println("Cost: " + diet.cost.value());
            for (int i = 0; i < diet.m; i++) {
                System.out.println(diet.food[i] + ": " + diet.x[i].value());
            }
        } else {
            System.out.println("No solution.");
        }
    }
}
