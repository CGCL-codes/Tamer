package org.twyna.midget.util;

import java.util.Date;

/**
 * Very basic logger class, currently just outputs to System.out, but could of course
 * more easily be extended to write to a file than directly calling System.out.println.
 * @author Tim Franssen
 * 
 ***** License
 * Twyna (Touch With Your Noodly Appendage) is an open source distribution
 * system to install and manage your machines.
 * Copyright (C) 2009  R. Hijmans & T.K.C. Franssen
 * 
 * This file is part of Twyna.
 * 
 * Twyna is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Twyna is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Twyna. If not, see <http://www.gnu.org/licenses/>.
 */
public class Logger {

    public static boolean VERBOSE = true;

    /**
	 * Write a message to a log file
	 * @param caller Who's doing the writing?
	 * @param msg The message to write
	 */
    public static void log(String caller, String msg) {
        if (!VERBOSE) return;
        String logmessage = new String("[" + (new Date()) + "] [" + caller + "] " + msg);
        System.out.println(logmessage);
    }
}
