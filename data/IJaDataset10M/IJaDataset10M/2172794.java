package code;

import java.util.*;

public class GeneticAIFinder {

    final boolean USE_GENETIC = false;

    int generation = 1;

    final int population = 16;

    double mutation_rate = 0.05;

    double[][] chromosomes = new double[population][7];

    int[] scores = new int[population];

    Random rnd;

    TetrisEngine tetris;

    int current = 0;

    public GeneticAIFinder(TetrisEngine tetris) {
        this.tetris = tetris;
        rnd = new Random();
        for (int i = 0; i < population; i++) {
            for (int j = 0; j < 7; j++) {
                chromosomes[i][j] = rnd.nextDouble() * 10 - 5;
            }
        }
    }

    void newGeneration() {
        int[] scores_ = new int[population];
        for (int i = 0; i < scores.length; i++) scores_[i] = scores[i];
        Arrays.sort(scores_);
        System.out.println("Generation " + generation + "; min = " + scores_[0] + "; med = " + scores_[population / 2] + "; max = " + scores_[population - 1]);
        List<double[]> winners = new ArrayList<double[]>();
        for (int i = 0; i < (population / 2); i++) {
            int c1score = scores[i];
            int c2score = scores[i + 1];
            int winner = c1score > c2score ? i : i + 1;
            winners.add(chromosomes[winner]);
        }
        int counter = 0;
        List<double[]> new_population = new ArrayList<double[]>();
        for (int i = 0; i < (winners.size() / 2); i++) {
            double[] winner1 = winners.get(i);
            double[] winner2 = winners.get(i + 1);
            for (int off = 0; off < 4; off++) {
                double[] child = new double[7];
                for (int j = 0; j < 7; j++) {
                    child[j] = rnd.nextInt(2) > 0 ? winner1[j] : winner2[j];
                    boolean mutate = rnd.nextDouble() < mutation_rate;
                    if (mutate) {
                        double change = rnd.nextDouble() * 10 - 5;
                        child[j] += change;
                    }
                }
                new_population.add(child);
                counter++;
            }
        }
        Collections.shuffle(new_population, rnd);
        for (int i = 0; i < population; i++) {
            for (int j = 0; j < 7; j++) chromosomes[i][j] = new_population.get(i)[j];
        }
        generation++;
        current = 0;
    }

    void setAIValues(TetrisAI ai) {
        if (!USE_GENETIC) return;
        ai._TOUCHING_EDGES = chromosomes[current][0];
        ai._TOUCHING_WALLS = chromosomes[current][1];
        ai._TOUCHING_FLOOR = chromosomes[current][2];
        ai._HEIGHT = chromosomes[current][3];
        ai._HOLES = chromosomes[current][4];
        ai._BLOCKADE = chromosomes[current][5];
        ai._CLEAR = chromosomes[current][6];
    }

    void sendScore(int score) {
        if (!USE_GENETIC) return;
        String s = aToS(chromosomes[current]);
        s = "Generation " + generation + "; Candidate " + (current + 1) + ": " + s + " Score = " + score;
        System.out.println(s);
        scores[current] = score;
        current++;
        if (current == population) newGeneration();
    }

    private String aToS(double[] a) {
        String s = "";
        for (int i = 0; i < a.length; i++) {
            s += Double.toString(((double) Math.round(a[i] * 100)) / 100);
            if (i != a.length - 1) s += ", ";
        }
        return "[" + s + "]";
    }
}
