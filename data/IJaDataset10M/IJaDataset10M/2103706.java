package org.jogre.dots.common;

import java.util.Enumeration;
import nanoxml.XMLElement;
import org.jogre.common.comm.CommTableMessage;
import org.jogre.common.games.Card;
import org.jogre.dots.client.DotsCell;

/**
 * Dots communication snapshot object
 *
 * @author Garrett Lehman (Gman)
 * @version Alpha 0.2.3
 */
public class CommDotsSnapShot extends CommTableMessage {

    public static final String XML_NAME = "dots_snapshot";

    private static final String XML_ATT_LAST_MOVE_COL = "col";

    private static final String XML_ATT_LAST_MOVE_ROW = "row";

    private static final String XML_ATT_LAST_MOVE_LOCATION = "location";

    private static final String XML_ATT_DATA = "data";

    private int cols = 6;

    private int rows = 6;

    private int lastMoveCol = -1;

    private int lastMoveRow = -1;

    private int lastMoveLocation = -1;

    private DotsCell[][] data = null;

    /**
	 * Constructor that takes column, row and location
	 *
	 * @param lastMoveCol
	 * @param lastMoveRow
	 * @param lastMoveLocation
	 * @param data
	 */
    public CommDotsSnapShot(int cols, int rows, int lastMoveCol, int lastMoveRow, int lastMoveLocation, DotsCell[][] data) {
        super();
        this.cols = cols;
        this.rows = rows;
        this.lastMoveCol = lastMoveCol;
        this.lastMoveRow = lastMoveRow;
        this.lastMoveLocation = lastMoveLocation;
        this.data = data;
    }

    /**
	 * Constructor that takes xml element
	 *
	 * @param message
	 */
    public CommDotsSnapShot(int cols, int rows, XMLElement message) {
        super(message);
        this.cols = cols;
        this.rows = rows;
        this.lastMoveCol = message.getIntAttribute(XML_ATT_LAST_MOVE_COL);
        this.lastMoveRow = message.getIntAttribute(XML_ATT_LAST_MOVE_ROW);
        this.lastMoveLocation = message.getIntAttribute(XML_ATT_LAST_MOVE_LOCATION);
        this.data = new DotsCell[this.cols][this.rows];
        for (int c = 0; c < this.cols; c++) {
            for (int r = 0; r < this.rows; r++) this.data[c][r] = new DotsCell(c, r);
        }
        Enumeration e = message.enumerateChildren();
        while (e.hasMoreElements()) {
            XMLElement childMessage = (XMLElement) e.nextElement();
            if (childMessage.getName().equals(DotsCell.XML_NAME)) {
                DotsCell cell = new DotsCell(childMessage);
                this.data[cell.getColumn()][cell.getRow()] = cell;
            }
        }
    }

    /**
	 * Get last move column
	 *
	 * @return last move column
	 */
    public int getLastMoveColumn() {
        return this.lastMoveCol;
    }

    /**
	 * Get last move row
	 *
	 * @return last move row
	 */
    public int getLastMoveRow() {
        return this.lastMoveRow;
    }

    /**
	 * Get last move location
	 *
	 * @return last move location
	 */
    public int getLastMoveLocation() {
        return this.lastMoveLocation;
    }

    /**
	 * Get dot cell data
	 *
	 * @return data
	 */
    public DotsCell[][] getData() {
        return this.data;
    }

    /**
	 * Flatten the String.
	 *
	 * @see org.jogre.common.comm.ITransmittable#flatten()
	 */
    public XMLElement flatten() {
        XMLElement message = super.flatten(XML_NAME);
        message.setIntAttribute(XML_ATT_LAST_MOVE_COL, this.lastMoveCol);
        message.setIntAttribute(XML_ATT_LAST_MOVE_ROW, this.lastMoveRow);
        message.setIntAttribute(XML_ATT_LAST_MOVE_LOCATION, this.lastMoveLocation);
        int cols = this.data.length;
        int rows = this.data[0].length;
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                if (data[c][r].filledLocations() > 0) message.addChild(data[c][r].flatten());
            }
        }
        return message;
    }
}
