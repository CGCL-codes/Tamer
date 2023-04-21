package com.grt192.rsh;

/**
 * Interface for generic command interpretation with rsh
 * @author ajc
 */
public interface Commandable {

    public void command(String[] args);
}
