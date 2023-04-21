package com.triplea.rolap.plugins.filter.rules;

import java.util.*;

public class StringFunction extends Function {

    private static final int CHAR = 1;

    private static final int CLEAN = 2;

    private static final int CODE = 3;

    private static final int CONCATENATE = 4;

    private static final int EXACT = 5;

    private static final int LEFT = 6;

    private static final int LEN = 7;

    private static final int MID = 8;

    private static final int REPLACE = 9;

    private static final int REPT = 10;

    private static final int RIGHT = 11;

    private static final int SEARCH = 12;

    private static final int SUBSTITUTE = 13;

    private static final int TRIM = 14;

    private static final int LOWER = 15;

    private static final int UPPER = 16;

    private static final int PROPER = 17;

    private static final int VALUE = 18;

    private int id;

    StringFunction(int id) {
        super();
        this.id = id;
    }

    public static void addFunctions(RulesPlugin plugin) {
        plugin.addFunction(new StringFunction(CHAR));
        plugin.addFunction(new StringFunction(CLEAN));
        plugin.addFunction(new StringFunction(CODE));
        plugin.addFunction(new StringFunction(CONCATENATE));
        plugin.addFunction(new StringFunction(EXACT));
        plugin.addFunction(new StringFunction(LEFT));
        plugin.addFunction(new StringFunction(LEN));
        plugin.addFunction(new StringFunction(MID));
        plugin.addFunction(new StringFunction(REPLACE));
        plugin.addFunction(new StringFunction(REPT));
        plugin.addFunction(new StringFunction(RIGHT));
        plugin.addFunction(new StringFunction(SEARCH));
        plugin.addFunction(new StringFunction(SUBSTITUTE));
        plugin.addFunction(new StringFunction(TRIM));
        plugin.addFunction(new StringFunction(LOWER));
        plugin.addFunction(new StringFunction(UPPER));
        plugin.addFunction(new StringFunction(PROPER));
        plugin.addFunction(new StringFunction(VALUE));
    }

    public String getName() {
        switch(id) {
            case CHAR:
                return "CHAR";
            case CLEAN:
                return "CLEAN";
            case CODE:
                return "CODE";
            case CONCATENATE:
                return "CONCATENATE";
            case EXACT:
                return "EXACT";
            case LEFT:
                return "LEFT";
            case LEN:
                return "LEN";
            case MID:
                return "MID";
            case REPLACE:
                return "REPLACE";
            case REPT:
                return "REPT";
            case RIGHT:
                return "RIGHT";
            case SEARCH:
                return "SEARCH";
            case SUBSTITUTE:
                return "SUBSTITUTE";
            case TRIM:
                return "TRIM";
            case LOWER:
                return "LOWER";
            case UPPER:
                return "UPPER";
            case PROPER:
                return "PROPER";
            case VALUE:
                return "VALUE";
        }
        return null;
    }

    public int getMinArgumentsCount() {
        switch(id) {
            case CHAR:
            case CLEAN:
            case CODE:
            case LEN:
            case TRIM:
            case LOWER:
            case UPPER:
            case PROPER:
            case VALUE:
                return 1;
            case CONCATENATE:
            case EXACT:
            case LEFT:
            case REPT:
            case RIGHT:
            case SEARCH:
                return 2;
            case MID:
            case SUBSTITUTE:
                return 3;
            case REPLACE:
                return 4;
        }
        return 0;
    }

    public int getMaxArgumentsCount() {
        return getMinArgumentsCount();
    }

    public int getArgumentType(int arg) {
        int[] argTypes = null;
        switch(id) {
            case CHAR:
                return IValue.TYPE_NUMERIC;
            case CLEAN:
            case CODE:
            case CONCATENATE:
            case EXACT:
            case LEN:
            case SEARCH:
            case SUBSTITUTE:
            case TRIM:
            case LOWER:
            case UPPER:
            case PROPER:
            case VALUE:
                return IValue.TYPE_STRING;
            case LEFT:
            case MID:
            case REPT:
            case RIGHT:
                if (arg == 0) return IValue.TYPE_STRING; else return IValue.TYPE_NUMERIC;
            case REPLACE:
                if (arg == 0 || arg == 3) return IValue.TYPE_STRING; else return IValue.TYPE_NUMERIC;
        }
        return argTypes[arg];
    }

    private static class SearchState implements Comparable {

        public int startPos;

        public int checkPos;

        public int patternPos;

        public SearchState(int startPos, int checkPos, int patternPos) {
            this.startPos = startPos;
            this.checkPos = checkPos;
            this.patternPos = patternPos;
        }

        public String toString() {
            return "" + patternPos + " " + startPos + " " + checkPos;
        }

        public int compareTo(Object obj) {
            return toString().compareTo(obj.toString());
        }
    }

    public static int search(String str, String pattern) {
        TreeSet<SearchState> setState = new TreeSet<SearchState>();
        for (int i = 0; i < str.length(); i++) setState.add(new SearchState(i, i, 0));
        while (!setState.isEmpty()) {
            SearchState next = setState.first();
            setState.remove(next);
            if (next.patternPos == pattern.length()) return next.startPos;
            char chPattern = pattern.charAt(next.patternPos);
            if (chPattern == '*') {
                if (next.checkPos < str.length() - 1) {
                    setState.add(new SearchState(next.startPos, next.checkPos + 1, next.patternPos));
                    setState.add(new SearchState(next.startPos, next.checkPos + 1, next.patternPos + 1));
                }
                setState.add(new SearchState(next.startPos, next.checkPos, next.patternPos + 1));
            } else if (chPattern == '?') setState.add(new SearchState(next.startPos, next.checkPos + 1, next.patternPos + 1)); else {
                boolean process = true;
                if (chPattern == '\\') {
                    if (next.patternPos + 1 < pattern.length()) {
                        next.patternPos++;
                        chPattern = pattern.charAt(next.patternPos);
                    } else process = false;
                }
                if (process && next.checkPos < str.length() && str.charAt(next.checkPos) == chPattern) setState.add(new SearchState(next.startPos, next.checkPos + 1, next.patternPos + 1));
            }
        }
        return -1;
    }

    public Object calculate(RuleContext context, IValue[] args) throws RuleException {
        Object result = null;
        switch(id) {
            case CHAR:
                {
                    int i = ((Double) (args[0].calculate(context))).intValue();
                    char ch = Character.toChars(i)[0];
                    result = "" + ch;
                    break;
                }
            case CLEAN:
                {
                    String strArg = args[0].calculate(context).toString();
                    StringBuffer r = new StringBuffer();
                    for (int i = 0; i < strArg.length(); i++) {
                        char ch = strArg.charAt(i);
                        if (!Character.isISOControl(ch)) r.append(ch);
                    }
                    result = r.toString();
                    break;
                }
            case CODE:
                {
                    String str = args[0].calculate(context).toString();
                    result = new Double(Character.codePointAt(str.toCharArray(), 0));
                    break;
                }
            case CONCATENATE:
                result = args[0].calculate(context).toString() + args[1].calculate(context).toString();
                break;
            case EXACT:
                result = new Double(args[0].calculate(context).toString().equalsIgnoreCase(args[1].calculate(context).toString()) ? 1 : 0);
                break;
            case LEFT:
                {
                    String arg = args[0].calculate(context).toString();
                    int len = ((Double) (args[1].calculate(context))).intValue();
                    result = arg.substring(0, len);
                    break;
                }
            case LEN:
                {
                    String arg = args[0].calculate(context).toString();
                    result = new Double(arg.length());
                    break;
                }
            case MID:
                {
                    String arg = args[0].calculate(context).toString();
                    int start = ((Double) (args[1].calculate(context))).intValue() - 1;
                    int len = ((Double) (args[2].calculate(context))).intValue();
                    if (start + len > arg.length()) len = arg.length() - start;
                    result = arg.substring(start, start + len);
                    break;
                }
            case REPLACE:
                {
                    String arg = args[0].calculate(context).toString();
                    int start = ((Double) (args[1].calculate(context))).intValue() - 1;
                    int len = ((Double) (args[2].calculate(context))).intValue();
                    String by = args[3].calculate(context).toString();
                    String prefix = arg.substring(0, start);
                    String postfix = arg.substring(start + len);
                    result = prefix + by + postfix;
                    break;
                }
            case REPT:
                {
                    String arg = args[0].calculate(context).toString();
                    int count = ((Double) (args[1].calculate(context))).intValue();
                    StringBuffer buf = new StringBuffer();
                    while (count > 0) {
                        buf.append(arg);
                        count--;
                    }
                    result = buf.toString();
                    break;
                }
            case RIGHT:
                {
                    String arg = args[0].calculate(context).toString();
                    int len = ((Double) (args[1].calculate(context))).intValue();
                    result = arg.substring(arg.length() - len, arg.length());
                    break;
                }
            case SEARCH:
                {
                    String argPattern = args[0].calculate(context).toString();
                    String arg = args[1].calculate(context).toString();
                    result = new Double(search(arg, argPattern) != -1 ? 1 : 0);
                    break;
                }
            case SUBSTITUTE:
                {
                    String arg = args[0].calculate(context).toString();
                    String strOld = args[1].calculate(context).toString();
                    String strNew = args[2].calculate(context).toString();
                    int pos = arg.indexOf(strOld);
                    while (pos != -1) {
                        String prefix = arg.substring(0, pos);
                        String postfix = arg.substring(pos + strOld.length(), arg.length());
                        arg = prefix + strNew + postfix;
                        pos = arg.indexOf(strOld);
                    }
                    result = arg;
                    break;
                }
            case TRIM:
                result = args[0].calculate(context).toString().trim();
                break;
            case LOWER:
                result = args[0].calculate(context).toString().toLowerCase();
                break;
            case UPPER:
                result = args[0].calculate(context).toString().toUpperCase();
                break;
            case PROPER:
                {
                    String str = args[0].calculate(context).toString().toLowerCase();
                    if (str.length() > 0) str = str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
                    result = str;
                }
                break;
            case VALUE:
                {
                    String str = args[0].calculate(context).toString();
                    result = new Double(Double.parseDouble(str));
                    break;
                }
        }
        return result;
    }

    public int getResultType() {
        switch(id) {
            case CHAR:
            case CLEAN:
            case CONCATENATE:
            case LEFT:
            case MID:
            case REPLACE:
            case REPT:
            case RIGHT:
            case SEARCH:
            case SUBSTITUTE:
            case TRIM:
            case LOWER:
            case UPPER:
            case PROPER:
                return IValue.TYPE_STRING;
            case CODE:
            case EXACT:
            case LEN:
            case VALUE:
                return IValue.TYPE_NUMERIC;
        }
        return 0;
    }
}
