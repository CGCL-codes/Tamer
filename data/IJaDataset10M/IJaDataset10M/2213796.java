package com.mockturtlesolutions.snifflib.util;

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**

*/
public interface Configuration {

    public HashMap getDefaultConfig();

    public HashSet getYesNoConfigs();

    public HashSet getTrueFalseConfigs();

    public HashSet getFileChooserConfigs();

    public HashSet getOnOffConfigs();

    public int size();

    public void initialize();

    public void show();

    public LinkedHashMap getConfig();

    public Set getConfigKeys();

    public void setSplitString(String splitter);

    public String getSplitString();

    public String[] getIllegalChars();

    public boolean hasIllegalChars(String value);

    public Object getConfigValue(String config);

    public void setConfigValue(String config, Object value);
}
