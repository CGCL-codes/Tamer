package com.fujitsu.arcon.njs.admin;

import java.io.BufferedReader;
import java.io.PrintWriter;
import com.fujitsu.arcon.njs.logger.Logger;
import com.fujitsu.arcon.njs.logger.LoggerManager;

/**
 * Methods provided by all admin commands
 *
 * @author Sven van den Berghe, fujitsu
 *
 * Copyright 2001 Fujitsu European Centre for Information Technology Ltd.
 *
 * @version $Revision: 1.2 $ $Date: 2004/06/30 13:45:27 $
 *
 **/
public abstract class AdminCommand {

    protected Logger logger;

    protected AdminCommand(String cmd_name) {
        logger = LoggerManager.get("admin");
        name = cmd_name.toLowerCase();
    }

    private String name;

    public String getName() {
        return name;
    }

    public abstract void process(String cmd_line, BufferedReader in, PrintWriter out);

    protected String short_help;

    protected String long_help;

    public String getShortHelp() {
        return short_help;
    }
}
