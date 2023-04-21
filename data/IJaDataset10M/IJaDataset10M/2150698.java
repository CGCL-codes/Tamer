package com.luxoft.fitpro.htmleditor.adapters.fit;

import java.lang.reflect.Method;
import com.luxoft.fitpro.htmleditor.adapters.messages.AdaptersMessages;
import com.luxoft.fitpro.htmleditor.core.parser.SyntaxCheckResult;
import fit.Parse;
import fitlibrary.table.Row;
import fitlibrary.table.Table;
import fitlibrary.table.Tables;
import fitlibrary.utility.ExtendedCamelCase;

public class FitFileSyntaxChecker {

    private SyntaxCheck[] allSyntaxCheckers = { new ColumnFixtureSyntaxCheck() };

    public FitFileSyntaxChecker() {
    }

    public final boolean isDoFixture(final Parse parse) {
        Tables tables = new Tables(parse);
        Table table = tables.table(0);
        if (table.rowExists(0) && !table.rowExists(1)) {
            Row row = table.row(0);
            if (row.cellExists(0) && !row.cellExists(1)) {
                return true;
            }
        }
        return false;
    }

    public final String getFixtureName(final Table table) {
        String fixtureName = null;
        if (table.rowExists(0)) {
            Row row = table.row(0);
            if (row.cellExists(0)) {
                fixtureName = row.cell(0).text();
                if (fixtureName.equals("fit.ActionFixture")) {
                    fixtureName = getActionFixtureName(table);
                }
            }
        }
        return fixtureName;
    }

    private String getActionFixtureName(final Table table) {
        String fixtureName = null;
        if (table.rowExists(1)) {
            Row row = table.row(1);
            if (row.cellExists(1)) {
                if (row.cell(0).text().equalsIgnoreCase("start")) {
                    fixtureName = row.cell(1).text();
                }
            }
        }
        return fixtureName;
    }

    public final boolean isFixtureFound(final String fixtureName) {
        try {
            Class.forName(fixtureName);
            return true;
        } catch (ClassNotFoundException ce) {
            return false;
        }
    }

    public final boolean isFixtureMatch(final String fixtureName, final Class clazz) {
        try {
            Class c = Class.forName(fixtureName);
            if (c == clazz) {
                return true;
            } else {
                Class[] supers = c.getInterfaces();
                for (Class s : supers) {
                    if (s == clazz) {
                        return true;
                    }
                }
                return false;
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public final boolean isDoFixtureMethodMatch(final String methodName, final Class clazz) {
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().equalsIgnoreCase(methodName)) {
                return true;
            }
        }
        return false;
    }

    public final String getFlowFunctionName(final Row row) {
        if (row != null) {
            StringBuffer functionName = new StringBuffer();
            for (int i = 0; i < row.size(); i += 2) {
                String textFrag = row.text(i);
                if (i > 0) {
                    functionName.append(' ');
                }
                functionName.append(textFrag);
            }
            return ExtendedCamelCase.camel(functionName.toString());
        } else {
            return null;
        }
    }

    public final SyntaxCheckResult checkSyntax(final Parse parse) throws ClassNotFoundException {
        SyntaxCheckResult result = new SyntaxCheckResult();
        if (isDoFixture(parse)) {
            Tables tables = new Tables(parse);
            Table firstTable = tables.table(0);
            String fixtureName = getFixtureName(firstTable);
            if (isFixtureFound(fixtureName)) {
                Class fixtureClass = Class.forName(fixtureName);
                for (int i = 1; i < tables.size(); i++) {
                    Table t = tables.table(i);
                    for (int j = 0; j < t.size(); j++) {
                        Row r = t.row(j);
                        String functionName = getFlowFunctionName(r);
                        boolean methodMatch = isDoFixtureMethodMatch(functionName, fixtureClass);
                        if (methodMatch) {
                            checkMethodArgumentMatch(r, fixtureClass, result);
                        } else {
                            break;
                        }
                    }
                    for (SyntaxCheck sc : allSyntaxCheckers) {
                        if (sc.checkStructure(t, result)) {
                            sc.checkArgument(t, result);
                        }
                    }
                }
            } else {
                result.error(AdaptersMessages.getMessage("htmleditor.fixture_class_not_found"));
            }
        } else {
            Tables tables = new Tables(parse);
            for (int i = 0; i < tables.size(); i++) {
                Table t = tables.table(i);
                for (SyntaxCheck sc : allSyntaxCheckers) {
                    if (sc.checkStructure(t, result)) {
                        sc.checkArgument(t, result);
                    }
                }
            }
        }
        return result;
    }

    private boolean methodArgumentTypesMatch(final Class[] argTypes, final Row row, final SyntaxCheckResult result) {
        boolean matched = true;
        for (int j = 0; j < argTypes.length; j++) {
            String paramValue = row.text(j * 2 + 1);
            if (argTypes[j].isPrimitive()) {
                if (argTypes[j] == Integer.TYPE) {
                    try {
                        Integer.parseInt(paramValue);
                    } catch (NumberFormatException nfe) {
                        result.error(AdaptersMessages.getMessage("htmleditor.unable_to_convert_to_number"));
                        matched = false;
                    }
                } else if (argTypes[j] == Boolean.TYPE) {
                    if (!(paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("yes") || paramValue.equalsIgnoreCase("false") || paramValue.equalsIgnoreCase("no"))) {
                        result.error(AdaptersMessages.getMessage("htmleditor.unable_to_convert_to_boolean"));
                        matched = false;
                    }
                } else if (argTypes[j] == Short.TYPE) {
                    try {
                        Short.parseShort(paramValue);
                    } catch (NumberFormatException nfe) {
                        result.error(AdaptersMessages.getMessage("htmleditor.unable_to_convert_to_number"));
                        matched = false;
                    }
                } else if (argTypes[j] == Double.TYPE) {
                    try {
                        Double.parseDouble(paramValue);
                    } catch (NumberFormatException nfe) {
                        result.error(AdaptersMessages.getMessage("htmleditor.unable_to_convert_to_number"));
                        matched = false;
                    }
                } else if (argTypes[j] == Long.TYPE) {
                    try {
                        Long.parseLong(paramValue);
                    } catch (NumberFormatException nfe) {
                        result.error(AdaptersMessages.getMessage("htmleditor.unable_to_convert_to_long"));
                        matched = false;
                    }
                }
            } else {
                if (argTypes[j] != String.class) {
                    result.error(AdaptersMessages.getMessage("htmleditor.unable_to_convert_to_non-primitive_data_type"));
                    matched = false;
                }
            }
        }
        return matched;
    }

    public final void checkMethodArgumentMatch(final Row r, final Class fixtureClass, final SyntaxCheckResult result) {
        int parms = r.size() / 2;
        String name = r.text(0);
        for (int i = 2; i < r.size(); i += 2) {
            name += " " + r.text(i);
        }
        String functionName = ExtendedCamelCase.camel(name);
        Method[] methods = fixtureClass.getMethods();
        boolean methodNotFound = true;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(functionName)) {
                Class[] parameterTypes = methods[i].getParameterTypes();
                if (parameterTypes.length == parms && methodArgumentTypesMatch(parameterTypes, r, result)) {
                    methodNotFound = false;
                }
            }
        }
        if (methodNotFound) {
            result.error(AdaptersMessages.getMessage("htmleditor.matching_method_not_found"));
        }
    }
}
