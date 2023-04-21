package com.xavax.jsf.vms;

public interface MessageFormatter {

    /**
   * Returns a formatted message string created by replacing positional
   * parameters in the pattern with values from the array of parameters.
   * 
   * @param pattern  the message format pattern.
   * @param params   the message parameters.
   * @return a formatted message string.
   */
    public String format(String pattern, Object[] params);
}
