package org.sqlutils.export;

import com.lowagie.text.Font;
import java.io.Serializable;

/**
 * <p>Title: GuestsHome application</p>
 * <p>Description: This class is used when exporting data in PDF format:
 * it can be extended to override some methods.</p>
 * <p>Copyright: Copyright (C) 2009 Informatici senza frontiere</p>
 *
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * @author Mauro Carniel
 * @version 1.0
 *
 */
public class ExportToPDFAdapter implements Serializable {

    /**
   * @return Font to set for the specified generic cell
   */
    public Font getGenericComponentFont(int row, int col, Object value) {
        return new Font(Font.HELVETICA, 6, Font.NORMAL);
    }

    /**
   * @return Font to set for the title
   */
    public Font getFontTitle() {
        return new Font(Font.HELVETICA);
    }

    /**
   * @param attributeName attribute name that identify column header
   * @return Font to set for the specified column header
   */
    public Font getHeaderFont(String attributeName) {
        return new Font(Font.HELVETICA, 6, Font.BOLD);
    }

    /**
   * @return gray fill to use in cells of column headers
   */
    public float getHeaderGrayFill() {
        return 0.75f;
    }

    /**
   * @param row row index of current locked row to color
   * @return gray fill to use in cells of locked rows on top of the grid
   */
    public float getTopRowsGrayFill(int row) {
        return 0.95f;
    }

    /**
   * @return gray fill to use in cells of even rows
   */
    public float getEvenRowsGrayFill() {
        return 0.95f;
    }

    /**
   * @return gray fill to use in cells of odd rows
   */
    public float getOddRowsGrayFill() {
        return 1.00f;
    }

    /**
   * @param row row index of current locked row to color
   * @return gray fill to use in cells of locked rows on the bottom of the grid
   */
    public float getBottomRowsGrayFill(int row) {
        return 0.95f;
    }

    /**
   * @param attributeName attribute name that identify current column
   * @return Font to set for the specified cell
   */
    public Font getRowFont(String attributeName) {
        return new Font(Font.HELVETICA, 6, Font.NORMAL);
    }
}
