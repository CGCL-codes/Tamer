package org.silentsquare.codejam.y2008.round2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Algorithm
 * Tree implies recurrence.
 * 
 * @author wjfang
 *
 */
public class BooleanTree {

    private static final String PATH = "./src/org/silentsquare/codejam/y2008/round2/";

    private Scanner in;

    private PrintWriter out;

    public void solveSmall() throws IOException {
        _solve("small");
    }

    public void solveLarge() throws IOException {
        _solve("large");
    }

    public void _solve(String name) throws IOException {
        System.out.println("Solving the " + name + " dataset ...");
        long begin = System.currentTimeMillis();
        in = new Scanner(new File(PATH + "A-" + name + "-practice.in"));
        out = new PrintWriter(new BufferedWriter(new FileWriter(PATH + "A-" + name + "-practice.out")));
        int tests = in.nextInt();
        for (int i = 0; i < tests; i++) {
            readCase();
            String answer = solveCase();
            out.printf("Case #%d: %s\n", (i + 1), answer);
            System.out.printf("Case #%d: %s\n", (i + 1), answer);
        }
        in.close();
        out.close();
        System.out.println("Solving the " + name + " dataset: " + (System.currentTimeMillis() - begin) + "ms");
    }

    enum Type {

        AND, OR, INPUT;

        Type change() {
            switch(this) {
                case AND:
                    return OR;
                case OR:
                    return AND;
                default:
                    return INPUT;
            }
        }
    }

    class Gate {

        Type type;

        boolean changeable;

        boolean value;

        boolean terminated;

        Gate(Type type, boolean changeable) {
            this.type = type;
            this.changeable = changeable;
        }

        Gate(boolean value) {
            this.type = Type.INPUT;
            this.value = value;
            this.terminated = true;
        }
    }

    Gate[] booleanTree;

    boolean desiredValue;

    private void readCase() {
        int m = in.nextInt();
        booleanTree = new Gate[m];
        desiredValue = readBoolean();
        for (int i = 0; i < (m - 1) / 2; i++) {
            Type type = in.nextInt() == 1 ? Type.AND : Type.OR;
            boolean changeable = readBoolean();
            Gate gate = new Gate(type, changeable);
            booleanTree[i] = gate;
        }
        for (int i = (m - 1) / 2; i < m; i++) {
            boolean value = readBoolean();
            Gate gate = new Gate(value);
            booleanTree[i] = gate;
        }
    }

    private boolean readBoolean() {
        if (in.nextInt() == 0) return false; else return true;
    }

    private String solveCase() {
        calcInitialValue();
        int min = cheat(0, desiredValue);
        if (min >= MAX_CHANGES) return "IMPOSSIBLE"; else return "" + min;
    }

    private void calcInitialValue() {
        for (int i = (booleanTree.length - 1) / 2 - 1; i >= 0; i--) {
            Gate me = booleanTree[i];
            Gate left = booleanTree[2 * i + 1];
            Gate right = booleanTree[2 * i + 2];
            me.value = gate(me.type, left.value, right.value);
            me.terminated = !me.changeable && left.terminated && right.terminated;
        }
    }

    boolean gate(Type type, boolean leftValue, boolean rightValue) {
        switch(type) {
            case AND:
                return leftValue && rightValue;
            default:
                return leftValue || rightValue;
        }
    }

    static final int MAX_CHANGES = 10000;

    private int cheat(int idx, boolean dvalue) {
        Gate me = booleanTree[idx];
        if (me.value == dvalue) return 0;
        if (me.terminated) return MAX_CHANGES;
        Gate left = booleanTree[2 * idx + 1];
        Gate right = booleanTree[2 * idx + 2];
        if (me.changeable && gate(me.type.change(), left.value, right.value) == dvalue) return 1;
        int changes = MAX_CHANGES;
        int leftChanges = cheat(2 * idx + 1, !left.value);
        int rightChanges = cheat(2 * idx + 2, !right.value);
        if (gate(me.type, !left.value, right.value) == dvalue && changes > leftChanges) changes = leftChanges;
        if (gate(me.type, left.value, !right.value) == dvalue && changes > rightChanges) changes = rightChanges;
        if (gate(me.type, !left.value, !right.value) == dvalue && changes > leftChanges + rightChanges) changes = leftChanges + rightChanges;
        if (me.changeable) {
            if (gate(me.type.change(), !left.value, right.value) == dvalue && changes > leftChanges + 1) changes = leftChanges + 1;
            if (gate(me.type.change(), left.value, !right.value) == dvalue && changes > rightChanges + 1) changes = rightChanges + 1;
            if (gate(me.type.change(), !left.value, !right.value) == dvalue && changes > leftChanges + rightChanges + 1) changes = leftChanges + rightChanges + 1;
        }
        return changes;
    }

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        new BooleanTree().solveSmall();
        new BooleanTree().solveLarge();
    }
}
