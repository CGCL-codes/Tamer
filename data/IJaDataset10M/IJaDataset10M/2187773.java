package tron;

import java.awt.*;
import java.io.*;
import java.util.*;
import tron.Snake.Direction;

public class Field implements Serializable {

    private int field[][];

    private int size;

    private int countOfSnakes;

    private Snake snakes[];

    public int count() {
        return countOfSnakes;
    }

    public Field(int size, int countOfSnakes) {
        if (size <= 0 || countOfSnakes < 1 || countOfSnakes > 4) throw new Error();
        this.size = size;
        field = new int[size + 2][];
        for (int i = 0; i <= size + 1; ++i) field[i] = new int[size + 2];
        this.countOfSnakes = countOfSnakes;
    }

    public void init() {
        for (int i = 0; i <= size + 1; ++i) {
            Arrays.fill(field[i], 0);
        }
        initWall();
        initSnakes();
        updateField();
    }

    public int getSize() {
        return size;
    }

    public int get(int x, int y) {
        return field[y][x];
    }

    public void update() {
        for (int i = 1; i <= countOfSnakes; ++i) {
            if (snakes[i].isAlive) killCollide(i);
            if (snakes[i].isAlive) snakes[i].updatePoint();
        }
        updateField();
    }

    public Snake getSnake(int number) {
        if (number <= 0 || number > countOfSnakes) return null;
        return snakes[number];
    }

    public int winner() {
        int alive = 0;
        for (int i = 1; i <= countOfSnakes; ++i) if (snakes[i].isAlive) {
            if (alive != 0) return -1;
            alive = i;
        }
        return alive;
    }

    private void killCollide(int number) {
        Point next = snakes[number].getNextPoint();
        int cell = field[next.y][next.x];
        if (cell > 0) {
            if (snakes[cell].isHead(next.x, next.y)) snakes[cell].kill();
            snakes[number].kill();
        } else if (cell < 0) snakes[number].kill();
        for (int i = 1; i <= countOfSnakes; ++i) {
            if (i != number && snakes[i].getNextPoint().equals(next)) {
                snakes[i].kill();
                snakes[number].kill();
                field[next.y][next.x] = -1;
            }
        }
    }

    private void initWall() {
        for (int i = 0; i <= size + 1; ++i) {
            field[0][i] = -1;
            field[i][0] = -1;
            field[size + 1][i] = -1;
            field[i][size + 1] = -1;
        }
    }

    private void initSnakes() {
        snakes = new Snake[5];
        int shift = 5;
        snakes[1] = new Snake(1, shift, shift, Snake.Direction.RIGHT);
        snakes[2] = new Snake(2, size - shift, size - shift, Snake.Direction.LEFT);
        snakes[3] = new Snake(3, shift, size - shift, Snake.Direction.UP);
        snakes[4] = new Snake(4, size - shift, shift, Snake.Direction.DOWN);
    }

    private void updateField() {
        for (int i = 1; i <= countOfSnakes; ++i) {
            int y = (int) snakes[i].point.y;
            int x = (int) snakes[i].point.x;
            field[y][x] = snakes[i].number;
        }
    }
}
