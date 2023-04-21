package com.seguim.geroglifics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Col {

    private List symbols;

    public int width = 0;

    public int height = 0;

    public Col() {
        symbols = new ArrayList();
    }

    public List getSymbols() {
        return symbols;
    }

    public void addSymbol(Symbol symbol) {
        symbols.add(symbol);
    }

    public int getWidth() {
        if (width == 0 || height == 0) {
            computeSize();
        }
        return width;
    }

    public int getHeight() {
        if (width == 0 || height == 0) {
            computeSize();
        }
        return height;
    }

    public void computeSize() {
        int colWidth = 0;
        int colHeight = 0;
        for (Iterator symbolIter = symbols.iterator(); symbolIter.hasNext(); ) {
            Symbol symbol = (Symbol) symbolIter.next();
            int iconWidth = symbol.getIcon().getIconWidth();
            int iconHeight = symbol.getIcon().getIconHeight();
            if (colWidth < iconWidth) {
                colWidth = iconWidth;
            }
            colHeight += iconHeight;
        }
        width = colWidth;
        height = colHeight;
    }
}
