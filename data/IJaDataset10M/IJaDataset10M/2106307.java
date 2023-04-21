package com.google.code.javastorage.cli.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author thomas.scheuchzer@gmail.com
 * 
 */
public abstract class AbstractSessionBasedCommand extends AbstractCommand {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(String[] args) {
        try {
            if (ctx.getSession() == null) {
                printError("connect to a online storage first!");
                return;
            }
            doExecute(args);
        } catch (Exception e) {
            log.info("An error occured.", e);
        }
    }

    public abstract void doExecute(String[] args);
}
