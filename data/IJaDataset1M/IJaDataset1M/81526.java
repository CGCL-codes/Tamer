package org.iseriestoolkit.util;

import java.text.DecimalFormat;

/*******************************************************************************  
 * Copyright (C) 2001  David M. Morris                                         *  
 * This file is part of the iSeries-toolkit                                    *  
 * The iSeries-toolkit is free software; you can redistribute it and/or        *  
 * modify it under the terms of the GNU General Public License version 2,      *  
 * as published by the Free Software Foundation.                               *  
 * The iseries toolkit is distributed in the hope that it will be useful,      *  
 * but WITHOUT ANY WARRANTY; without even the implied warranty of              *  
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                        *  
 * See the GNU General Public License for more details.                        *  
 *                                                                             *  
 * You should have received a copy of the GNU General Public License           *  
 * along with this file; if not, write to the Free Software Foundation,        *  
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.              *  
 * You might find a version at http://www.gnu.org/licenses/gpl.txt             *
 *******************************************************************************
 * Formatting methods
 * @author		David Morris
 */
public class Format {

    /**
     * Format a numeric value to a String
     * Creation date: (6/26/01 2:20:29 PM)
     * @return java.lang.String
     * @param number double
     * @param pattern java.lang.String
     */
    public static String decimalFormat(final double number, String pattern) {
        return new DecimalFormat(pattern).format(number);
    }

    /**
     * Format a string to be left justified within a fixed width column
     * Creation date: (6/26/01 2:20:29 PM)
     * @return java.lang.String
     * @param inpString java.lang.String
     * @param width int
     * @param padValue java.lang.String
     */
    public static String formatColumn(String inpString, int width, String padValue) {
        return (formatColumn(inpString, width, padValue, 'L'));
    }

    /**
     * Format a string within a fixed width column
     * Creation date: (6/26/01 2:20:29 PM)
     * @return java.lang.String
     * @param inpString java.lang.String
     * @param width int
     * @param padValue java.lang.String
     * @param justify char <b>L</b>eft/<b>R</b>ight
     */
    public static String formatColumn(String inpString, int width, String padValue, char justify) {
        String outString;
        if (inpString.length() < width) {
            StringBuffer buffer = new StringBuffer();
            if (justify == 'L') {
                buffer.append(inpString);
            }
            for (int i = inpString.length(); i < width; ++i) buffer.append(padValue);
            if (justify == 'R') {
                buffer.append(inpString);
            }
            outString = buffer.toString();
        } else {
            outString = inpString.substring(0, width);
        }
        return outString;
    }

    /**
     * Set the first character in a string to upper case
     * Creation date: (6/26/01 2:20:29 PM)
     * @return java.lang.String
     * @param inpString java.lang.String
     */
    public static String initUpperCase(String inpString) {
        return inpString.substring(0, 1).toUpperCase() + inpString.substring(1);
    }

    /**
     * Replace a portion of a string with another string
     * @param target	The string to perform the replace on
     * @param arg		  The string occuring in the target to replace
     * @param source	The string to replace the arg
     * @return			  The replaced string
     */
    public static String replace(String target, String arg, String source) {
        int i = 0;
        i = target.indexOf(arg);
        while (i != -1) {
            String left = target.substring(0, i);
            String right = target.substring(i + arg.length());
            target = new String(left + source + right);
            i = target.indexOf(arg);
        }
        return target;
    }
}
