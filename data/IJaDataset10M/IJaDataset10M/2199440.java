package com.neurogrid.database;

import java.lang.Exception;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class defines SQLStringNotAvailableException and is thrown when an SQL template
 * requested cannot be found<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   4/may/2001    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */
public class SQLStringNotAvailableException extends Exception {

    public static final String cvsInfo = "$Id: SQLStringNotAvailableException.java,v 1.2 2002/03/26 11:23:56 samjoseph Exp $";

    public SQLStringNotAvailableException(String p_name, String p_class) {
        super("Template " + p_name + " not found in " + p_class);
    }
}
