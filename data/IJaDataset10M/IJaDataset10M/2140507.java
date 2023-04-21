package de.pallo.joti.sys;

/**
 * -- Copyright (C) 2004 M. Pallo <markus@pallo.de>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307,
 *
 *
 * $Revision: 1.1.1.1 $
 * Author: M. Pallo
 * last changed by $Author: baskote $
 */
public class SimpleLog {

    public static boolean DEBUG = true;

    private SimpleLog() {
    }

    public static void logDebug(Object msg) {
        if (DEBUG) System.out.println("D:" + msg);
    }

    public static void logError(Object msg) {
        System.out.println("E:" + msg);
    }

    public static void logFatal(Object msg) {
        logFatal(msg, null);
    }

    public static void logFatal(Object msg, Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
        System.out.println("F:" + msg);
    }
}
