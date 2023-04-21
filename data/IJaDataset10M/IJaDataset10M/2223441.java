package com.schwidder.nucleus.runtime;

/**
 * @author kai@schwidder.com
 * @version 1.0
 */
public class QuitCommandImpl extends CommandImpl {

    public int getType() {
        return CommandMode.QUIT;
    }
}
