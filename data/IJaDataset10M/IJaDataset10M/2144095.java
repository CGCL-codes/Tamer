package com.samskievert.cactusboom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.Point;
import java.lang.String;

public class CBBoardGen {

    final int[] DX = { 0, -1, 0, 1 };

    final int[] DY = { 1, 0, -1, 0 };

    ArrayList<Tile> moves;

    ArrayList<Tile> ghosts;

    ArrayList<Board> boards;

    int[] boardCount = { 0, 0, 0, 0, 0 };

    CBBoardGen() {
        boards = new ArrayList<Board>();
        for (int i = 0; i < 5; i++) {
            makeBoard(1);
            makeBoard(2);
            makeBoard(3);
            makeBoard(4);
            makeBoard(5);
        }
    }

    int[] getBoard(int diff) {
        System.out.println("Difficulty: " + diff);
        int test = 0;
        while (boardCount[diff - 1] == 0) {
            makeBoard(diff);
        }
        for (int i = 0; i < boards.size(); i++) {
            Board b = (Board) boards.get(i);
            if (b.difficulty == diff) {
                boards.remove(i);
                boardCount[diff - 1]--;
                String output = "{" + b.startup[0];
                for (int t = 1; t < 64; t++) {
                    output += "," + b.startup[t];
                }
                System.out.println(output + "};");
                return b.startup;
            }
        }
        return new int[] { 0 };
    }

    void makeBoard(int diff) {
        moves = new ArrayList<Tile>();
        ghosts = new ArrayList<Tile>();
        Random r = new Random();
        Tile t = new Tile(r.nextInt(7) + 1, r.nextInt(7) + 1, 3, false);
        moves.add(t);
        if (diff > 1) {
            while (moveItOrLoseIt(diff) != 0) {
            }
        } else {
            int num = r.nextInt(8) + 8;
            for (int i = 0; i < num; i++) {
                moveItOrLoseIt(1);
            }
        }
        int[] counts = new int[6];
        for (int i = 0; i < 6; i++) {
            counts[i] = 0;
        }
        for (int i = 0; i < moves.size(); i++) {
            Tile tt = (Tile) moves.get(i);
            if (!tt.gap) {
                counts[tt.value]++;
                counts[5]++;
            }
        }
        int difficulty = 0;
        if (counts[5] >= 56 && counts[0] >= 5) difficulty = 5; else if (counts[5] >= 44 && counts[5] <= 52 && counts[0] >= 4) difficulty = 4; else if (counts[5] >= 32 && counts[5] <= 40 && counts[0] >= 3) difficulty = 3; else if (counts[5] >= 20 && counts[5] <= 28 && counts[0] >= 2) difficulty = 2; else if (counts[5] >= 8 && counts[5] <= 16) difficulty = 1; else return;
        int[] rightOrder = new int[64];
        Arrays.fill(rightOrder, -1);
        for (int i = 0; i < moves.size(); i++) {
            Tile tt = (Tile) moves.get(i);
            if (tt.gap) rightOrder[tt.x + tt.y * 8] = -1; else rightOrder[tt.x + tt.y * 8] = tt.value;
        }
        boards.add(new Board(difficulty, rightOrder));
        boardCount[difficulty - 1]++;
    }

    class Board {

        int difficulty;

        int[] startup;

        Board(int d, int[] s) {
            difficulty = d;
            startup = s;
        }
    }

    class Tile {

        int x;

        int y;

        int value;

        boolean[] used = { false, false, false, false };

        boolean ghost;

        boolean gap;

        Tile(int _x, int _y, int v, boolean g) {
            x = _x;
            y = _y;
            value = v;
            ghost = g;
        }

        Tile(Tile ghost) {
            x = ghost.x;
            y = ghost.y;
            value = Math.max(0, 4 + ghost.value);
        }

        Tile(int _x, int _y) {
            x = _x;
            y = _y;
            used = new boolean[] { true, true, true, true };
            value = 0;
            ghost = false;
            gap = true;
        }
    }

    boolean inBounds(int x, int y) {
        if (x < 0 || y < 0 || x > 7 || y > 7) return false;
        return true;
    }

    void addTile(int tx, int ty) {
        evaluateMove(tx, ty);
        for (int i = 0; i < ghosts.size(); i++) {
            Tile t = (Tile) ghosts.get(i);
            if (t.x == tx && t.y == ty) {
                Tile newGuy = new Tile(t);
                moves.add(newGuy);
                int hungry = 4 - newGuy.value;
                for (int j = 0; j < moves.size(); j++) {
                    Tile m = (Tile) moves.get(j);
                    if (m.y > newGuy.y && m.x == newGuy.x && !m.used[2] && hungry > 0) {
                        m.used[2] = true;
                        hungry--;
                        fillGap(m, newGuy);
                    }
                    if (m.y < newGuy.y && m.x == newGuy.x && !m.used[0] && hungry > 0) {
                        m.used[0] = true;
                        hungry--;
                        fillGap(m, newGuy);
                    }
                    if (m.x > newGuy.x && m.y == newGuy.y && !m.used[1] && hungry > 0) {
                        m.used[1] = true;
                        hungry--;
                        fillGap(m, newGuy);
                    }
                    if (m.x < newGuy.x && m.y == newGuy.y && !m.used[3] && hungry > 0) {
                        m.used[3] = true;
                        hungry--;
                        fillGap(m, newGuy);
                    }
                }
                doGhosts();
                break;
            }
        }
    }

    void doGhosts() {
        ghosts = new ArrayList<Tile>();
        int[][] boardState = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardState[i][j] = 0;
            }
        }
        for (int i = 0; i < moves.size(); i++) {
            Tile t = (Tile) moves.get(i);
            if (!t.ghost) boardState[t.x][t.y] = 1;
            for (int j = 0; j < 4; j++) {
                if (!t.used[j]) {
                    int tx = t.x;
                    int ty = t.y;
                    while (inBounds(tx + DX[j], ty + DY[j])) {
                        tx += DX[j];
                        ty += DY[j];
                        if (boardState[tx][ty] <= 0) boardState[tx][ty]--;
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardState[i][j] < 0) ghosts.add(new Tile(i, j, boardState[i][j], true));
            }
        }
    }

    void fillGap(Tile a, Tile b) {
        for (int i = 0; i < ghosts.size(); i++) {
            Tile c = (Tile) ghosts.get(i);
            if (a.y == b.y) {
                if (c.y == a.y && ((c.x > a.x && c.x < b.x) || (c.x < a.x && c.x > b.x))) moves.add(new Tile(c.x, c.y));
            }
            if (a.x == b.x) {
                if (c.x == a.x && ((c.y > a.y && c.y < b.y) || (c.y < a.y && c.y > b.y))) moves.add(new Tile(c.x, c.y));
            }
        }
        doGhosts();
    }

    int gapCount(Tile a, Tile b) {
        int gC = 0;
        for (int i = 0; i < ghosts.size(); i++) {
            Tile c = (Tile) ghosts.get(i);
            if (a.y == b.y) {
                if (c.y == a.y && ((c.x > a.x && c.x < b.x) || (c.x < a.x && c.x > b.x))) gC++;
            }
            if (a.x == b.x) {
                if (c.x == a.x && ((c.y > a.y && c.y < b.y) || (c.y < a.y && c.y > b.y))) gC++;
            }
        }
        return gC++;
    }

    int evaluateMove(int tx, int ty) {
        int[] gaps = { 0, 0, 0, 0 };
        for (int i = 0; i < ghosts.size(); i++) {
            Tile t = (Tile) ghosts.get(i);
            if (t.x == tx && t.y == ty) {
                Tile newGuy = new Tile(t);
                int hungry = 4 - newGuy.value;
                for (int j = 0; j < moves.size(); j++) {
                    Tile m = (Tile) moves.get(j);
                    if (m.y > newGuy.y && m.x == newGuy.x && !m.used[2] && hungry > 0) {
                        if (gaps[2] < gapCount(m, newGuy)) gaps[2] = gapCount(m, newGuy);
                        hungry--;
                    }
                    if (m.y < newGuy.y && m.x == newGuy.x && !m.used[0] && hungry > 0) {
                        if (gaps[0] < gapCount(m, newGuy)) gaps[0] = gapCount(m, newGuy);
                        hungry--;
                    }
                    if (m.x > newGuy.x && m.y == newGuy.y && !m.used[1] && hungry > 0) {
                        if (gaps[1] < gapCount(m, newGuy)) gaps[1] = gapCount(m, newGuy);
                        hungry--;
                    }
                    if (m.x < newGuy.x && m.y == newGuy.y && !m.used[3] && hungry > 0) {
                        if (gaps[3] < gapCount(m, newGuy)) gaps[3] = gapCount(m, newGuy);
                        hungry--;
                    }
                }
                break;
            }
        }
        int totalGaps = gaps[0] + gaps[1] + gaps[2] + gaps[3];
        return totalGaps;
    }

    int moveItOrLoseIt(int difficulty) {
        doGhosts();
        int threeCount = 0;
        if (difficulty == 1) threeCount = 4;
        if (difficulty == 2) threeCount = 7;
        if (difficulty == 3) threeCount = 10;
        if (difficulty == 4) threeCount = 13;
        if (difficulty == 5) threeCount = 16;
        int numThrees = 0;
        for (int i = 0; i < moves.size(); i++) {
            Tile tt = moves.get(i);
            if (tt.value == 3) numThrees++;
        }
        int[] gA = { 1, 0, 0, 0 };
        if (difficulty == 4) gA = new int[] { 2, 1, 0, 0 };
        if (difficulty == 3) gA = new int[] { 4, 3, 2, 1 };
        if (difficulty <= 2) gA = new int[] { 8, 8, 8, 8 };
        ArrayList<Point> greats = new ArrayList<Point>();
        ArrayList<Point> okays = new ArrayList<Point>();
        ArrayList<Point> decents = new ArrayList<Point>();
        ArrayList<Point> sads = new ArrayList<Point>();
        for (int i = 0; i < ghosts.size(); i++) {
            Tile t = (Tile) ghosts.get(i);
            if (t.value <= -4) greats.add(new Point(t.x, t.y));
            if (t.value == -3) okays.add(new Point(t.x, t.y));
            if (t.value == -2) decents.add(new Point(t.x, t.y));
            if (t.value == -1) sads.add(new Point(t.x, t.y));
        }
        Random rand = new Random();
        if (greats.size() > 0) {
            int numTries = 0;
            while (numTries < 20) {
                numTries++;
                int r = rand.nextInt(greats.size());
                Point p = (Point) greats.get(r);
                if (evaluateMove(p.x, p.y) <= gA[0]) {
                    addTile(p.x, p.y);
                    if (numThrees < threeCount) addThree(gA[3]);
                    return -4;
                }
            }
        }
        if (okays.size() > 0) {
            int numTries = 0;
            while (numTries < 20) {
                numTries++;
                int r = rand.nextInt(okays.size());
                Point p = (Point) okays.get(r);
                if (evaluateMove(p.x, p.y) <= gA[1]) {
                    addTile(p.x, p.y);
                    if (numThrees < threeCount) addThree(gA[3]);
                    return -3;
                }
            }
        }
        if (decents.size() > 0) {
            int numTries = 0;
            while (numTries < 30) {
                numTries++;
                int r = rand.nextInt(decents.size());
                Point p = (Point) decents.get(r);
                if (evaluateMove(p.x, p.y) <= gA[2]) {
                    addTile(p.x, p.y);
                    if (numThrees < threeCount) addThree(gA[3]);
                    return -2;
                }
            }
        }
        if (sads.size() > 0) {
            int numTries = 0;
            while (numTries < 50) {
                numTries++;
                int r = rand.nextInt(sads.size());
                Point p = (Point) sads.get(r);
                if (evaluateMove(p.x, p.y) <= gA[3]) {
                    addTile(p.x, p.y);
                    return -1;
                }
            }
        }
        return 0;
    }

    void addThree(int roamDist) {
        doGhosts();
        Random rand = new Random();
        ArrayList<Point> sads = new ArrayList<Point>();
        for (int i = 0; i < ghosts.size(); i++) {
            Tile t = (Tile) ghosts.get(i);
            if (t.value == -1) sads.add(new Point(t.x, t.y));
        }
        if (sads.size() > 0) {
            int numTries = 0;
            while (numTries < 50) {
                numTries++;
                int r = rand.nextInt(sads.size());
                Point p = (Point) sads.get(r);
                if (evaluateMove(p.x, p.y) == 0) {
                    addTile(p.x, p.y);
                    return;
                }
            }
        }
    }
}
