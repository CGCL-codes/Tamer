package ca.ucalgary.cpsc.ebe.fitClipse.core.results.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ca.ucalgary.cpsc.ebe.fitClipse.core.data.IColumnTestTable;
import ca.ucalgary.cpsc.ebe.fitClipse.core.data.IFixtureInfo;
import ca.ucalgary.cpsc.ebe.fitClipse.core.data.impl.FixtureInfo;
import ca.ucalgary.cpsc.ebe.fitClipse.core.results.IResultTable;
import ca.ucalgary.cpsc.ebe.fitClipse.core.results.ResultState;

public class ColumnResultTable extends ResultTable implements IResultTable {

    private final IFixtureInfo fixtures;

    private String id;

    private final Map<String, List<List<String>>> inputs;

    private final List<String> methods;

    private final List<String> properties;

    private final List<ResultCell> results;

    public ColumnResultTable(final List<IColumnTestTable> tables) {
        fixtures = new FixtureInfo();
        methods = new ArrayList<String>();
        properties = new ArrayList<String>();
        inputs = new LinkedHashMap<String, List<List<String>>>();
        results = new ArrayList<ResultCell>();
        for (final IColumnTestTable table : tables) {
            addResult(table);
        }
    }

    private boolean addResult(final IColumnTestTable table) {
        if (id == null) {
            id = table.getUniqueID();
        }
        if (!id.equals(table.getUniqueID())) {
            return false;
        }
        for (final String fixture : table.getFixtures()) {
            fixtures.addFixture(fixture);
        }
        if (properties.isEmpty()) {
            for (final String header : table.getHeaders()) {
                if (!header.matches(".*?\\(\\)$")) {
                    properties.add(header);
                } else if (!methods.contains(header)) {
                    methods.add(header);
                }
            }
        }
        final String tableFixture = table.getFixtures().get(0);
        if (!inputs.keySet().contains(tableFixture)) {
            final List<List<String>> inputMatrix = new ArrayList<List<String>>();
            for (int i = 0; i < table.dataSetCount(); i++) {
                inputMatrix.add(table.getDataSet(i));
            }
            inputs.put(tableFixture, inputMatrix);
        }
        for (int row = 0; row < table.dataSetCount(); row++) {
            for (int i = 0; i < methods.size(); i++) {
                final int column = properties.size() + i;
                putResult(methods.get(i), table.getFixtures().get(0), table.getResult(row, column), parseResult(table.getResultRepresentation(row, column)));
            }
        }
        return true;
    }

    public String getId() {
        return id;
    }

    private String getResult(final String fixture, final String method, final String value) {
        for (final ResultCell cell : results) {
            if (cell.equals(new ResultCell(fixture, method, 0, value))) {
                return cell.toString();
            }
        }
        return "";
    }

    private int parseResult(final String color) {
        if (color.matches(".*green.*") || color.matches(".*AAFFAA.*")) {
            return ResultState.PASSED;
        } else if (color.matches(".*yellow.*") || color.matches(".*FFFFAA.*")) {
            return ResultState.EXCEPTION;
        } else if (color.matches(".*red.*") || color.matches(".*FFAAAA.*")) {
            return ResultState.FAILED;
        } else if (color.matches("ignore") || color.matches("AAAAAA")) {
            return ResultState.IGNORED;
        } else {
            return -1;
        }
    }

    private void putResult(final String method, final String fixture, final String value, final int output) {
        results.add(new ResultCell(fixture, method, output, value));
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuffer result = new StringBuffer();
        result.append("<table>\n");
        result.append("\t<tr>\n");
        for (int i = 0; i < properties.size(); i++) {
            result.append("\t\t<td></td>\n");
        }
        if (methods.size() > 1) {
            for (final String fixture : fixtures.getFixtures()) {
                result.append("\t\t<td colspan=\"" + methods.size() + "\">" + fixture + "</td>\n");
            }
        } else {
            for (final String fixture : fixtures.getFixtures()) {
                result.append("\t\t<td>" + fixture + "</td>\n");
            }
        }
        result.append("\t</tr>\n");
        result.append("\t<tr>\n");
        for (final String property : properties) {
            result.append("\t\t<td>" + property + "</td>\n");
        }
        for (int i = 0; i < fixtures.getFixtures().size(); i++) {
            for (final String method : methods) {
                result.append("\t\t<td>" + method + "</td>\n");
            }
        }
        result.append("\t</tr>\n");
        final List<List<String>> inputMatrix = inputs.get(fixtures.getFixtures().get(0));
        final int numOfRows = inputMatrix.size();
        for (int row = 0; row < numOfRows; row++) {
            result.append("\t<tr>\n");
            final List<String> inputRow = inputMatrix.get(row);
            for (int prop = 0; prop < properties.size(); prop++) {
                result.append("\t\t<td>" + inputRow.get(prop) + "</td>\n");
            }
            for (int fixCount = 0; fixCount < fixtures.getFixtures().size(); fixCount++) {
                for (int i = 0; i < methods.size(); i++) {
                    final int column = properties.size() + i;
                    final String fixture = fixtures.getFixtures().get(fixCount);
                    result.append("\t\t" + getResult(fixture, methods.get(i), inputs.get(fixture).get(row).get(column)) + "\n");
                }
            }
            result.append("\t</tr>\n");
        }
        result.append("</table>\n");
        return result.toString();
    }

    @Deprecated
    public void updateFailure() {
    }
}
