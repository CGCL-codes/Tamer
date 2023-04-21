package net.sf.xpontus.utils;

import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author pfnguyen
 */
public class StringGridBagConstraints extends GridBagConstraints {

    private static final String NUMBER_REGEX = "[0-9]*\\.?[0-9]+";

    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_REGEX);

    private static final HashMap<String, Integer> anchorConstants = new HashMap<String, Integer>();

    private static final HashMap<String, Integer> fillConstants = new HashMap<String, Integer>();

    private static final HashMap<String, Integer> gridConstants = new HashMap<String, Integer>();

    private static final HashMap<String, Map<String, Integer>> constants = new HashMap<String, Map<String, Integer>>();

    static {
        anchorConstants.put("C", GridBagConstraints.CENTER);
        anchorConstants.put("CENTER", GridBagConstraints.CENTER);
        anchorConstants.put("NORTH", GridBagConstraints.NORTH);
        anchorConstants.put("N", GridBagConstraints.NORTH);
        anchorConstants.put("NORTHEAST", GridBagConstraints.NORTHEAST);
        anchorConstants.put("NE", GridBagConstraints.NORTHEAST);
        anchorConstants.put("NORTHWEST", GridBagConstraints.NORTHWEST);
        anchorConstants.put("NW", GridBagConstraints.NORTHWEST);
        anchorConstants.put("EAST", GridBagConstraints.EAST);
        anchorConstants.put("E", GridBagConstraints.EAST);
        anchorConstants.put("WEST", GridBagConstraints.WEST);
        anchorConstants.put("W", GridBagConstraints.WEST);
        anchorConstants.put("SOUTH", GridBagConstraints.SOUTH);
        anchorConstants.put("S", GridBagConstraints.SOUTH);
        anchorConstants.put("SOUTHEAST", GridBagConstraints.SOUTHEAST);
        anchorConstants.put("SE", GridBagConstraints.SOUTHEAST);
        anchorConstants.put("SOUTHWEST", GridBagConstraints.SOUTHWEST);
        anchorConstants.put("SW", GridBagConstraints.SOUTHWEST);
        anchorConstants.put("PAGE_START", GridBagConstraints.PAGE_START);
        anchorConstants.put("PS", GridBagConstraints.PAGE_START);
        anchorConstants.put("PAGE_END", GridBagConstraints.PAGE_END);
        anchorConstants.put("PE", GridBagConstraints.PAGE_END);
        anchorConstants.put("LINE_START", GridBagConstraints.LINE_START);
        anchorConstants.put("LS", GridBagConstraints.LINE_START);
        anchorConstants.put("LINE_END", GridBagConstraints.LINE_END);
        anchorConstants.put("LE", GridBagConstraints.LINE_END);
        anchorConstants.put("PAGESTART", GridBagConstraints.PAGE_START);
        anchorConstants.put("PAGEEND", GridBagConstraints.PAGE_END);
        anchorConstants.put("LINESTART", GridBagConstraints.LINE_START);
        anchorConstants.put("LINEEND", GridBagConstraints.LINE_END);
        anchorConstants.put("FLS", GridBagConstraints.FIRST_LINE_START);
        anchorConstants.put("FIRST_LINE_START", GridBagConstraints.FIRST_LINE_START);
        anchorConstants.put("FLE", GridBagConstraints.FIRST_LINE_END);
        anchorConstants.put("FIRST_LINE_END", GridBagConstraints.FIRST_LINE_END);
        anchorConstants.put("FIRSTLINESTART", GridBagConstraints.FIRST_LINE_START);
        anchorConstants.put("FIRSTLINEEND", GridBagConstraints.FIRST_LINE_END);
        anchorConstants.put("LLS", GridBagConstraints.LAST_LINE_START);
        anchorConstants.put("LAST_LINE_START", GridBagConstraints.LAST_LINE_START);
        anchorConstants.put("LLE", GridBagConstraints.LAST_LINE_END);
        anchorConstants.put("LAST_LINE_END", GridBagConstraints.LAST_LINE_END);
        anchorConstants.put("LASTLINESTART", GridBagConstraints.LAST_LINE_START);
        anchorConstants.put("LASTLINEEND", GridBagConstraints.LAST_LINE_END);
        fillConstants.put("NONE", GridBagConstraints.NONE);
        fillConstants.put("BOTH", GridBagConstraints.BOTH);
        fillConstants.put("B", GridBagConstraints.BOTH);
        fillConstants.put("HORIZONTAL", GridBagConstraints.HORIZONTAL);
        fillConstants.put("H", GridBagConstraints.HORIZONTAL);
        fillConstants.put("VERTICAL", GridBagConstraints.VERTICAL);
        fillConstants.put("V", GridBagConstraints.VERTICAL);
        gridConstants.put("REL", GridBagConstraints.RELATIVE);
        gridConstants.put("RELATIVE", GridBagConstraints.RELATIVE);
        gridConstants.put("REMAINDER", GridBagConstraints.REMAINDER);
        gridConstants.put("REM", GridBagConstraints.REMAINDER);
        copyKeys(anchorConstants, constants);
        copyKeys(fillConstants, constants);
        copyKeys(gridConstants, constants);
    }

    public StringGridBagConstraints(String constraints) {
        parse(constraints);
    }

    private static void copyKeys(Map<String, Integer> map, Map<String, Map<String, Integer>> keys) {
        for (String key : map.keySet()) keys.put(key, map);
    }

    /**
     * Utility method to load the specified constraints into the constraints
     * object.
     * @param constraints
     * @param c
     */
    public static void parseParams(String constraints, GridBagConstraints c) {
        String[] params = constraints.split(",");
        boolean gotX = false;
        boolean gotY = false;
        boolean gotWidth = false;
        boolean gotHeight = false;
        boolean gotIpadX = false;
        boolean gotWeightX = false;
        for (int i = 0, j = params.length; i < j; i++) {
            String param = params[i].trim();
            if (NUMBER_PATTERN.matcher(param).matches()) {
                if (param.indexOf(".") != -1) {
                    double value = Double.parseDouble(param);
                    if (gotWeightX) {
                        c.weighty = value;
                    } else {
                        c.weightx = value;
                        gotWeightX = true;
                    }
                } else {
                    int value = Integer.parseInt(param);
                    if (!gotX) {
                        c.gridx = value;
                        gotX = true;
                    } else if (!gotY) {
                        c.gridy = value;
                        gotY = true;
                    } else if (!gotWidth) {
                        c.gridwidth = value;
                        gotWidth = true;
                    } else if (!gotHeight) {
                        c.gridheight = value;
                        gotHeight = true;
                    } else if (!gotIpadX) {
                        c.ipadx = value;
                        gotIpadX = true;
                    } else {
                        c.ipady = value;
                    }
                }
            } else if (param.startsWith("(")) {
                if (param.endsWith(")")) {
                    int value = Integer.parseInt(param.substring(1, param.length() - 1));
                    c.insets.top = value;
                    c.insets.left = value;
                    c.insets.bottom = value;
                    c.insets.right = value;
                } else {
                    boolean hitEnd = false;
                    for (int pos = 0; !hitEnd; pos++, i++) {
                        param = params[i].trim();
                        if (param.startsWith("(")) {
                            param = param.substring(1);
                        }
                        if (param.endsWith(")")) {
                            param = param.substring(0, param.length() - 1);
                            hitEnd = true;
                        }
                        int value = Integer.parseInt(param);
                        switch(pos) {
                            case 0:
                                c.insets.top = c.insets.bottom = value;
                                break;
                            case 1:
                                c.insets.left = c.insets.right = value;
                                break;
                            case 2:
                                c.insets.bottom = value;
                                break;
                            case 3:
                                c.insets.right = value;
                                break;
                        }
                    }
                    i--;
                }
            } else {
                param = param.toUpperCase();
                if (!constants.containsKey(param)) {
                    throw new IllegalArgumentException(param);
                }
                if (param.startsWith("REL")) {
                    int value = gridConstants.get(param);
                    if (!gotX) {
                        c.gridx = value;
                        gotX = true;
                    } else if (!gotY) {
                        c.gridy = value;
                        gotY = true;
                    } else if (!gotWidth) {
                        c.gridwidth = value;
                        gotWidth = true;
                    } else if (!gotHeight) {
                        c.gridheight = value;
                        gotHeight = true;
                    } else {
                        throw new IllegalArgumentException(String.format("Not expecting '%s' at index %d", param, i));
                    }
                } else {
                    Map<String, Integer> map = constants.get(param);
                    int value = map.get(param);
                    if (map == gridConstants) {
                        if (!gotWidth) {
                            c.gridwidth = value;
                            gotWidth = true;
                        } else if (!gotHeight) {
                            c.gridheight = value;
                            gotHeight = true;
                        } else {
                            throw new IllegalArgumentException(String.format("Not expecting '%s' at index %d", param, i));
                        }
                    } else if (map == anchorConstants) {
                        c.anchor = value;
                    } else if (map == fillConstants) {
                        c.fill = value;
                    } else {
                        throw new IllegalArgumentException(String.format("Not expecting '%s' at index %d", param, i));
                    }
                }
            }
        }
    }

    /**
     * A convenience method to allow re-using previously set constraints, loads
     * up the specified constraints while leaving the previously set values.
     * @param constraints
     */
    public void parse(String constraints) {
        parseParams(constraints, this);
    }
}
