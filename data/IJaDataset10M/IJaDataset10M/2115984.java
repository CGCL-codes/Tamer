package com.trackerdogs.ui.servlet.skin;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import java.net.*;

/**
 * Provides an interface for html skins
 */
public interface SkinInterface {

    /**********************************************************************
     * Returns the value of the variable
     *
     * @param variable the variable name
     */
    public String getVariableValue(String variable);

    /************************************************************
     * Excecute the specified function with parameters
     *
     * @param func the function name
     * @param params an array of Strings (the parameters)
     *
     * @param return the result
     */
    public String getFunctionValue(String func, String[] params);

    /************************************************************
     * Sometimes it is usefull to specify a loop that has no
     * predifined number of loops (calculated at runtime)
     *
     * @param blockName the block name
     *
     * @return true if this block has to be executed one more time
     */
    public boolean loopBlock(String blockName);
}
