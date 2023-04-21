package com.c2b2.ipoint.presentation.table;

import java.util.Collection;
import java.util.LinkedList;

/**
  * $Id: Row.java,v 1.2 2006/05/18 17:07:37 steve Exp $
  * 
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  * 
  * This class represents a Row in a table to be rendered.
  * 
  * @author $Author: steve $
  * @version $Revision: 1.2 $
  * $Date: 2006/05/18 17:07:37 $
  * 
  */
public class Row {

    public Row() {
        myCells = new LinkedList();
    }

    public Collection getCells() {
        return myCells;
    }

    public Cell addCell() {
        Cell result = new Cell();
        myCells.addLast(result);
        return result;
    }

    public Cell addCell(String text, String href, String hreftitle) {
        Cell result = new Cell();
        myCells.addLast(result);
        result.setHref(href);
        result.setText(text);
        result.setHrefTitle(hreftitle);
        return result;
    }

    public Cell addCell(String text) {
        return this.addCell(text, null, null);
    }

    private LinkedList myCells;
}
