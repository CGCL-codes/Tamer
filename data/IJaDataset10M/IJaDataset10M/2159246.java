package com.kopiright.tanit.runtime.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.sql.SQLException;
import com.kopiright.vkopi.lib.util.PrintException;
import com.kopiright.vkopi.lib.visual.VException;
import com.kopiright.xkopi.lib.base.Cursor;

/**
 * To represent a query with different level.
 * The level 1 is the highest level.
 */
public class LevelQuery {

    private LevelQuery(PartPrinter printer) {
        int[] queryAscendant = printer.getQueryAscendant();
        this.printer = printer;
        this.cursor = printer.getRootCursor();
        this.level = -1;
        this.firstTime = true;
        this.nbCursors = queryAscendant.length;
        this.cursors = new Cursor[nbCursors];
        this.descendants = new List[nbCursors];
        this.queryMinLevel = printer.getQueryMinLevel();
        this.queryMaxLevel = printer.getQueryMaxLevel();
        this.cursors[0] = this.cursor;
        for (int i = 0; i < nbCursors; ++i) {
            descendants[i] = new ArrayList();
        }
        for (int i = 1; i < nbCursors; ++i) {
            descendants[queryAscendant[i]].add(new Integer(i));
        }
    }

    public static void print(PartPrinter printer) throws SQLException, VException, PrintException {
        LevelQuery lq = new LevelQuery(printer);
        lq.print(0);
    }

    protected void print(int query) throws SQLException, VException, PrintException {
        Cursor cursor = cursors[query];
        int minLevel = queryMinLevel[query];
        int maxLevel = queryMaxLevel[query];
        int previousLevel = minLevel;
        boolean firstIteration = true;
        boolean end = false;
        Iterator desc;
        if (firstTime) {
            printer.updateValues(query, cursors);
        } else {
            if (cursor.next()) {
                printer.updateValues(query, cursors);
            } else {
                end = true;
                firstIteration = false;
            }
        }
        for (level = minLevel; level <= maxLevel; level++) {
            printer.printPreHeader(level);
            printer.printHeader(level);
            if (level != maxLevel && !end) {
                printer.printBody(level);
            }
        }
        level = maxLevel;
        while (firstTime || firstIteration || (!end && cursor.next())) {
            previousLevel = level;
            level = printer.computeLevel(query, cursors);
            if (!firstTime && !firstIteration) {
                printer.updateValues(query, cursors);
            }
            firstTime = false;
            firstIteration = false;
            while (previousLevel > level) {
                printer.printFooter(previousLevel);
                previousLevel--;
            }
            printer.printBody(level);
            while (level < maxLevel) {
                level++;
                printer.printPreHeader(level);
                printer.printHeader(level);
                printer.printBody(level);
            }
            desc = descendants[query].iterator();
            while (desc.hasNext()) {
                Integer descendant = (Integer) desc.next();
                cursors[descendant.intValue()] = printer.openCursor(descendant.intValue());
                print(descendant.intValue());
                cursors[descendant.intValue()].close();
            }
        }
        if (level > maxLevel) {
            level = maxLevel;
        }
        while (level >= minLevel) {
            printer.printFooter(level);
            level--;
        }
    }

    /**
   * Get the current level
   */
    public int getLevel() {
        return level;
    }

    /**
   * Get the associated cursor
   */
    public Cursor getCursor() {
        return cursor;
    }

    /**
   * Get the number of queries
   */
    public int getNbCursors() {
        return nbCursors;
    }

    protected Cursor cursor;

    protected int nbCursors;

    protected Cursor[] cursors;

    protected int level;

    protected boolean firstTime;

    protected List[] descendants;

    protected int[] queryMinLevel;

    protected int[] queryMaxLevel;

    protected PartPrinter printer;
}
