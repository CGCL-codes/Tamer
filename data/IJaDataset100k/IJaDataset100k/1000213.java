package com.dynamicdartboard;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import com.dynamicdartboard.actionhandler.*;

public class Board implements Serializable {

    private Map<Integer, BoardNumber> numberMap;

    private int COLUMNS = 11;

    private int ROWS = 11;

    private Window owner = null;

    public Board() {
        init(COLUMNS, ROWS);
    }

    public Board(int columns, int rows, Window owner) {
        COLUMNS = columns;
        ROWS = rows;
        this.owner = owner;
        init(columns, rows);
    }

    public void init(int columns, int rows) {
        generateNumbers(columns * rows);
        List<Box> boxes = generateBoxes(columns, rows);
        populateNumberBoxes(boxes);
    }

    public int getColumns() {
        return COLUMNS;
    }

    public void setColumns(int c) {
        COLUMNS = c;
    }

    public int getRows() {
        return ROWS;
    }

    public void setRows(int r) {
        ROWS = r;
    }

    public BoardNumber getBoardNumber(int value) {
        return numberMap.get(Integer.valueOf(value));
    }

    public int numberOfBoardNumbersRemaining() {
        return numberMap.size();
    }

    public Set<Integer> getAvaliableNumbers() {
        return Collections.unmodifiableSet(numberMap.keySet());
    }

    public double getPercent(Integer num) {
        int total = COLUMNS * ROWS;
        double val = num.doubleValue() / total * 100.0;
        return val;
    }

    protected void generateNumbers(int size) {
        numberMap = new HashMap<Integer, BoardNumber>(size);
        ColorSequence colorSeq = new ColorSequence();
        for (int i = 1; i <= size; i++) {
            BoardNumber number = new BoardNumber(Integer.valueOf(i));
            if (getPercent(Integer.valueOf(i)) <= 15) {
                number.setActionHandler(new ChanceHandler());
                number.setDisplayOverride("?");
            }
            number.setColor(colorSeq.next());
            numberMap.put(number.getNumber(), number);
        }
    }

    protected List<Box> generateBoxes(int column, int row) {
        List<Box> boxes = new ArrayList<Box>(column * row);
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < column; x++) {
                boxes.add(new Box(x, y));
            }
        }
        return boxes;
    }

    protected void populateNumberBoxes(List<Box> boxes) {
        int pick;
        int size = boxes.size();
        for (int i = 1; i <= size; i++) {
            pick = (int) (Math.random() * boxes.size());
            if (pick >= boxes.size()) {
                pick = boxes.size() - 1;
            }
            numberMap.get(Integer.valueOf(i)).addBox(boxes.get(pick));
            boxes.remove(pick);
        }
    }

    public void drawBoard(Graphics g) {
        for (BoardNumber boardNumber : numberMap.values()) {
            boardNumber.drawNumber(g);
        }
    }

    public boolean removeNumber(Integer number) {
        BoardNumber num = (BoardNumber) numberMap.get(number);
        if (num == null) {
            System.out.println("number not in board");
            return false;
        }
        if (numberMap.values().size() == 1) {
            System.out.println("last Number.. not removing");
            return false;
        }
        numberMap.remove(number);
        distributeBoxes(num.getBoxes());
        clearMergedFlag();
        return true;
    }

    public void replaceNumber(Integer source, Integer replace) {
        BoardNumber bn = (BoardNumber) numberMap.remove(source);
        if (bn != null) {
            bn.setNumber(replace);
            if (numberMap.get(replace) != null) {
                removeNumber(replace);
            }
            numberMap.put(replace, bn);
        }
    }

    public BoardNumber removeBox(int x, int y) {
        BoardNumber hit = findBoardNumber(x, y);
        if (hit != null) {
            if (removeNumber(hit.getNumber())) {
                return hit;
            }
        }
        return null;
    }

    public BoardNumber findBoardNumber(int x, int y) {
        Box box = new Box(x, y);
        BoardNumber hit = null;
        for (Iterator i = numberMap.values().iterator(); i.hasNext(); ) {
            BoardNumber item = (BoardNumber) i.next();
            if (item.ownsBox(box)) {
                hit = item;
                break;
            }
        }
        return hit;
    }

    public void clearMergedFlag() {
        for (Iterator i = numberMap.values().iterator(); i.hasNext(); ) {
            BoardNumber item = (BoardNumber) i.next();
            item.setMerged(false);
        }
    }

    public void distributeBoxes(List boxes) {
        if (boxes == null) {
            return;
        }
        List<Box> temp = new ArrayList<Box>();
        while (boxes.size() > 0) {
            for (Iterator i = boxes.iterator(); i.hasNext(); ) {
                Box box = (Box) i.next();
                BoardNumber bestMatch = getBestMatch(box);
                if (bestMatch != null) {
                    bestMatch.addBox(box);
                    bestMatch.setMerged(true);
                } else {
                    temp.add(box);
                }
            }
            boxes = temp;
            temp = new ArrayList<Box>();
        }
    }

    public BoardNumber getBestMatch(Box box) {
        int c = box.getColumn();
        int r = box.getRow();
        BoardNumber best = null;
        BoardNumber northwest = getBoxOwner(new Box(r % 2 == 0 ? c - 1 : c, r - 1));
        BoardNumber northeast = getBoxOwner(new Box(r % 2 == 1 ? c + 1 : c, r - 1));
        BoardNumber east = getBoxOwner(new Box(c + 1, r));
        BoardNumber southeast = getBoxOwner(new Box(r % 2 == 1 ? c + 1 : c, r + 1));
        BoardNumber southwest = getBoxOwner(new Box(r % 2 == 0 ? c - 1 : c, r + 1));
        BoardNumber west = getBoxOwner(new Box(c - 1, r));
        if (northwest != null) {
            best = northwest;
        } else if (northeast != null) {
            best = northeast;
        } else if (east != null) {
            best = east;
        } else if (southeast != null) {
            best = southeast;
        } else if (southwest != null) {
            best = southwest;
        } else if (west != null) {
            best = west;
        }
        if (best == null) {
            return null;
        }
        best = compareBest(best, northwest);
        best = compareBest(best, northeast);
        best = compareBest(best, east);
        best = compareBest(best, southeast);
        best = compareBest(best, southwest);
        best = compareBest(best, west);
        return best;
    }

    public BoardNumber compareBest(BoardNumber source, BoardNumber compareTo) {
        if (compareTo == null) {
            return source;
        }
        if (source.isMerged() && !compareTo.isMerged()) {
            return compareTo;
        }
        if (!source.isMerged() && compareTo.isMerged()) {
            return source;
        }
        if (source.compareTo(compareTo) < 0) {
            return compareTo;
        } else {
            return source;
        }
    }

    public BoardNumber getBoxOwner(Box box) {
        for (Iterator i = numberMap.values().iterator(); i.hasNext(); ) {
            BoardNumber item = (BoardNumber) i.next();
            if (item.ownsBox(box)) {
                return item;
            }
        }
        return null;
    }
}
