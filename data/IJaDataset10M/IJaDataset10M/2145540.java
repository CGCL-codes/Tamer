package org.mitre.mrald.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldConnection;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldOutFile;

public class PivotAggregateFilter extends AbstractPivotFilter {

    public PivotAggregateFilter() throws MraldException {
        super();
    }

    public PivotAggregateFilter(MsgObject msg) throws MraldException {
        super(msg);
    }

    @Override
    public ArrayList<String> buildFrom(ArrayList<String> currentFromList) throws MraldException {
        String having_operator = nameValues.getValue(FormTags.HAVING_OPERATOR_TAG)[0];
        String having_value = nameValues.getValue(FormTags.HAVING_VALUE_TAG)[0];
        if (having_operator.equals("") || having_value.equals("") || operator.equals("")) {
        } else {
            String valueTable = nameValues.getValue(FormTags.VALUE_TABLE_TAG)[0];
            if (!currentFromList.contains(valueTable)) currentFromList.add(valueTable);
        }
        return currentFromList;
    }

    @Override
    public ArrayList<String> buildWhere(ArrayList<String> currentWhereList) throws MraldException {
        String having_operator = nameValues.getValue(FormTags.HAVING_OPERATOR_TAG)[0];
        String having_value = nameValues.getValue(FormTags.HAVING_VALUE_TAG)[0];
        if (having_operator.equals("") || having_value.equals("") || operator.equals("")) {
        } else {
            StringBuffer clause = new StringBuffer();
            buildOuter(clause);
            buildInner(clause);
            currentWhereList.add(clause.toString());
        }
        return currentWhereList;
    }

    private void buildInner(StringBuffer clause) {
        String entityTable = nameValues.getValue(FormTags.ENTITY_TABLE_TAG)[0];
        String entityField = nameValues.getValue(FormTags.ENTITY_FIELD_TAG)[0];
        clause.append("(SELECT ");
        clause.append(entityField);
        clause.append(" FROM ");
        clause.append(entityTable);
        clause.append(" WHERE ");
        buildInnerWhere(clause);
        clause.append(" GROUP BY ");
        clause.append(entityField);
        clause.append(" HAVING ");
        buildHaving(clause);
        clause.append(")");
    }

    private void buildInnerWhere(StringBuffer clause) {
        String valueField = nameValues.getValue(FormTags.VALUE_FIELD_TAG)[0];
        String value = nameValues.getValue(FormTags.VALUE_TAG)[0];
        clause.append(valueField);
        clause.append(" ");
        clause.append(operator);
        if (operator.indexOf("NULL") < 0) {
            clause.append(" ");
            buildValue(clause, FormTags.VALUE_TAG, value, operator.endsWith("IN"));
        }
    }

    private void buildHaving(StringBuffer clause) {
        String having_operator = nameValues.getValue(FormTags.HAVING_OPERATOR_TAG)[0];
        String having_value = nameValues.getValue(FormTags.HAVING_VALUE_TAG)[0];
        clause.append("COUNT(*) ");
        clause.append(having_operator);
        clause.append(" ");
        clause.append(having_value);
    }

    public static String populateOptions(String pivot, String datasource) {
        String[] pieces = parse(pivot);
        StringBuffer result = new StringBuffer();
        MraldConnection db = new MraldConnection(datasource);
        Statement stmt = db.createStatement();
        try {
            String sql = "SELECT COUNT(*) FROM (SELECT DISTINCT " + pieces[ATTRIBUTE_FIELD] + " FROM " + pieces[ATTRIBUTE_TABLE] + ") AS T";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                result.append("<option/>");
                int numAttr = rs.getInt(1);
                for (int i = 1; i <= numAttr; i++) {
                    result.append("<option value=\"");
                    result.append(FormTags.HAVING_VALUE_TAG);
                    result.append(FormTags.NAMEVALUE_TOKEN_STR);
                    result.append(i);
                    buildEAVinfo(result, pieces);
                    result.append("\">").append(i).append("</option>");
                }
            }
        } catch (SQLException e) {
            MraldOutFile.appendToFile(e.getMessage());
        }
        return result.toString();
    }

    public static void main(String[] args) throws MraldException {
        PivotAggregateFilter test = new PivotAggregateFilter();
        test.nameValues.setValue(FormTags.ENTITY_TABLE_TAG, "sf36summaryscores");
        test.nameValues.setValue(FormTags.ENTITY_FIELD_TAG, "abt_id");
        test.nameValues.setValue(FormTags.ENTITY_TYPE_TAG, "Numeric");
        test.nameValues.setValue(FormTags.ATTRIBUTE_TABLE_TAG, "sf36summaryscores");
        test.nameValues.setValue(FormTags.ATTRIBUTE_FIELD_TAG, "sf36_code");
        test.nameValues.setValue(FormTags.ATTRIBUTE_TYPE_TAG, "String");
        test.nameValues.setValue(FormTags.VALUE_TABLE_TAG, "sf36summaryscores");
        test.nameValues.setValue(FormTags.VALUE_FIELD_TAG, "sf36_value");
        test.nameValues.setValue(FormTags.VALUE_TYPE_TAG, "Numeric");
        test.nameValues.setValue(FormTags.HAVING_OPERATOR_TAG, ">=");
        test.nameValues.setValue(FormTags.HAVING_VALUE_TAG, "3");
        test.nameValues.setValue(FormTags.OPERATOR_TAG, "=");
        test.nameValues.setValue(FormTags.VALUE_TAG, "0");
        test.setOperator();
        ArrayList result = test.buildWhere(new ArrayList<String>());
        for (Object name : test.nameValues.getNames()) {
            for (String value : test.nameValues.getValue((String) name)) {
                System.out.println(name + " == " + value);
            }
        }
        for (Object sql : result) {
            System.out.println(sql);
        }
    }
}
