package com.gcsf.books.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ArgsHandler {

    private HashMap<String, String> myArguments = null;

    private HashMap<String, String> myPossibleArguments = new HashMap<String, String>();

    /**
   * 
   * @param aArgs
   *          String[]
   */
    public ArgsHandler(String[] aArgs) {
        myArguments = new HashMap<String, String>();
        for (int i = 0; i < aArgs.length; i++) {
            int pos;
            if ((pos = aArgs[i].indexOf("=")) != -1 || (pos = aArgs[i].indexOf(":")) != -1) {
                String argName = aArgs[i].substring(0, pos);
                String argValue = aArgs[i].substring(pos + 1, aArgs[i].length());
                myArguments.put(argName, argValue);
            } else {
                myArguments.put(aArgs[i], null);
            }
        }
    }

    /**
   * @param aCmdLineArgument
   *          String
   * @return boolean
   */
    public boolean hasArgument(String aCmdLineArgument) {
        return myArguments.containsKey(aCmdLineArgument);
    }

    /**
   * @param aCmdLineArgument
   *          String
   * @return true if the value of the given command line argument is "true"
   *         (ignore case)
   */
    public boolean isArgumentTrue(String aCmdLineArgument) {
        boolean result = false;
        if (true == hasArgument(aCmdLineArgument)) {
            String value = getArgumentValue(aCmdLineArgument);
            if (null != value) {
                result = "true".equals(getArgumentValue(aCmdLineArgument).toLowerCase());
            } else {
                result = true;
            }
        }
        return result;
    }

    /**
   * @param aCmdLineArgument
   *          String
   * @param aCompareValue
   *          String
   * @return true if the value of the given command line argument is equal to
   *         the given one (no ignore case)
   */
    public boolean isArgumentValue(String aCmdLineArgument, String aCompareValue) {
        return aCompareValue.equals(getArgumentValue(aCmdLineArgument));
    }

    /**
   * @param aKey
   *          String
   * @param aValue
   *          String
   * @return String
   */
    public String setArgumentValue(String aKey, String aValue) {
        return myArguments.put(aKey, aValue);
    }

    /**
   * @param aKey
   *          String
   * @return String
   */
    public String getArgumentValue(String aKey) {
        return myArguments.get(aKey);
    }

    /**
   * @return List<String>
   */
    public List<String> getUnknownArguments() {
        List<String> unknownArguments = new ArrayList<String>();
        Iterator<?> iterator = myArguments.keySet().iterator();
        while (iterator.hasNext()) {
            String arg = (String) iterator.next();
            if (myPossibleArguments.containsKey(arg) == false) {
                unknownArguments.add(arg);
            }
        }
        return unknownArguments;
    }

    /**
   * @return Returns the myArguments.
   */
    public Map<?, ?> getArgumentMap() {
        return myArguments;
    }

    /**
   * @param aArgList
   *          HashMap<String, String>
   */
    public void setPossibleArguments(HashMap<String, String> aArgList) {
        myPossibleArguments = aArgList;
    }
}
