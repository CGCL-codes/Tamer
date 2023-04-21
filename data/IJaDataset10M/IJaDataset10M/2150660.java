package adqlParser;

import java.util.Vector;
import saadadb.exceptions.QueryException;
import saadadb.query.parser.UnitHandler;
import saadadb.util.RegExp;
import adqlParser.parser.ParseException;
import adqlParser.parser.SQLTranslator;
import adqlParser.query.ADQLColumn;
import adqlParser.query.ADQLConstantValue;
import adqlParser.query.ADQLConstraint;
import adqlParser.query.ADQLObject;
import adqlParser.query.ADQLOperand;
import adqlParser.query.ADQLType;
import adqlParser.query.SearchHandler;
import adqlParser.query.function.UserFunction;

public class UCDFunction extends UserFunction {

    protected String tableName;

    protected String ucd;

    protected String unit = DEFAULT_UNIT;

    public static final String DEFAULT_UNIT = "none";

    protected boolean printUcdAsAlias = false;

    protected ADQLColumnAndMeta replacementColumn = null;

    public UCDFunction(ADQLOperand[] params) throws ParseException {
        super("UCD", params);
        if (params == null || params.length < 2 || params.length > 3) throw new ParseException("The function UCD must have two or three parameters which corresponds to a table name/alias, a valid ucd and optionally the result unit !");
        if (params[0] instanceof ADQLConstantValue) {
            ADQLConstantValue tableOp = (ADQLConstantValue) params[0];
            if (tableOp.getType() == ADQLType.STRING) {
                tableName = tableOp.getValue();
                if (tableName == null || tableName.trim().length() <= 0) throw new ParseException("The first parameter of the UCD function can't be empty. It must be the name of a selected table !");
            } else throw new ParseException("\"" + tableOp + "\" is not a string and so it is not a valid table name !");
        } else if (params[0] instanceof ADQLColumn) tableName = ((ADQLColumn) params[0]).getColumn(); else throw new ParseException("The first parameter of the UCD function must be the name of a selected table !");
        if (params[1] instanceof ADQLConstantValue) {
            ADQLConstantValue ucdOp = (ADQLConstantValue) params[1];
            if (ucdOp.getType() == ADQLType.STRING) {
                if (ucdOp.getValue().matches(RegExp.UCD)) ucd = ucdOp.getValue(); else throw new ParseException("\"" + ucdOp.getValue() + "\" is not a valid UCD (for instance: \"em.wl\" is a valid UCD)");
            } else throw new ParseException("\"" + ucdOp + "\" is not a string and so it is not a valid UCD !");
        } else throw new ParseException("The given ucd in the function UCD must be a string. So \"" + params[1] + "\" is not valid !");
        if (params.length == 3) {
            if (params[2] instanceof ADQLConstantValue) {
                ADQLConstantValue unitOp = (ADQLConstantValue) params[2];
                if (unitOp.getType() == ADQLType.STRING) {
                    unit = unitOp.getValue();
                    if (unit == null || unit.trim().length() == 0) unit = DEFAULT_UNIT;
                } else throw new ParseException("\"" + unitOp + "\" is not a string and so it is not a valid unit !");
            } else throw new ParseException("The given unit in the function UCD must be a string. So \"" + params[2] + "\" is not valid !");
        }
    }

    protected UCDFunction(String tableName, String ucdName, String unit) throws ParseException {
        this(tableName, ucdName, unit, false);
    }

    protected UCDFunction(String tableName, String ucdName, String unit, boolean printUcd) throws ParseException {
        super("UCD", new ADQLOperand[] { new ADQLColumn(tableName), new ADQLConstantValue(ucdName), new ADQLConstantValue(unit) });
        this.tableName = tableName;
        ucd = ucdName;
        this.unit = unit;
        printUcdAsAlias = printUcd;
    }

    /**
	 * @return The printUcdAsAlias.
	 */
    public final boolean isPrintUcdAsAlias() {
        return printUcdAsAlias;
    }

    /**
	 * @param printUcdAsAlias The printUcdAsAlias to set.
	 */
    public final void setPrintUcdAsAlias(boolean printUcdAsAlias) {
        this.printUcdAsAlias = printUcdAsAlias;
    }

    @Override
    public String getName() {
        return "UCD";
    }

    public String getTable() {
        return tableName;
    }

    public String getUCD() {
        return ucd;
    }

    public void setColumn(ADQLColumnAndMeta column) {
        replacementColumn = column;
    }

    public ADQLColumnAndMeta getColumn() {
        return replacementColumn;
    }

    @Override
    public void negativate(boolean neg) throws ParseException {
        super.negativate(neg);
    }

    @Override
    public String primaryToSQL() throws ParseException {
        if (replacementColumn != null) {
            String sql = replacementColumn.toSQL();
            if (!unit.equalsIgnoreCase(DEFAULT_UNIT)) {
                try {
                    if (replacementColumn.getMeta().getUcd() == null || replacementColumn.getMeta().getUcd().trim().length() == 0) throw new ParseException("Incompatible units: the column \"" + replacementColumn.getColumn() + "\" has no unit but the function UCD forces a unit conversion into \"" + unit + "\" !"); else sql = UnitHandler.getConvFunction(unit, replacementColumn.getMeta().getUnit(), sql);
                } catch (QueryException e) {
                    throw new ParseException("The unit \"" + unit + "\" of the UCD function \"" + toString() + "\" is incorrect !");
                } catch (ArithmeticException e) {
                    throw new ParseException("Incompatible units: the unit \"" + unit + "\" of the UCD function \"" + toString() + "\" is not compatible with the unit \"" + replacementColumn.getMeta().getUnit() + "\" of the column \"" + replacementColumn.getColumn() + "\" !");
                }
            }
            if (alias != null && alias.trim().length() > 0) sql += " AS \"" + alias + "\""; else if (printUcdAsAlias) sql += " AS \"" + ucd + (unit.equalsIgnoreCase(DEFAULT_UNIT) ? "" : (" [" + unit + "]")) + "\"";
            return sql;
        } else return super.primaryToSQL();
    }

    @Override
    public String primaryToSQL(SQLTranslator translator) throws ParseException {
        if (replacementColumn != null) {
            String sql = replacementColumn.toSQL(translator);
            if (!unit.equalsIgnoreCase(DEFAULT_UNIT)) {
                try {
                    if (replacementColumn.getMeta().getUcd() == null || replacementColumn.getMeta().getUcd().trim().length() == 0) throw new ParseException("Incompatible units: the column \"" + replacementColumn.getColumn() + "\" has no unit but the function UCD forces a unit conversion into \"" + unit + "\" !"); else sql = UnitHandler.getConvFunction(unit, replacementColumn.getMeta().getUnit(), sql);
                } catch (QueryException e) {
                    throw new ParseException("The unit \"" + unit + "\" of the UCD function \"" + toString() + "\" is incorrect !");
                } catch (ArithmeticException e) {
                    throw new ParseException("Incompatible units: the unit \"" + unit + "\" of the UCD function \"" + toString() + "\" is not compatible with the unit \"" + replacementColumn.getMeta().getUnit() + "\" of the column \"" + replacementColumn.getColumn() + "\" !");
                }
            }
            if (alias != null && alias.trim().length() > 0) sql += " AS \"" + alias + "\""; else if (printUcdAsAlias) sql += " AS \"" + ucd + (unit.equalsIgnoreCase(DEFAULT_UNIT) ? "" : (" [" + unit + "]")) + "\"";
            return sql;
        } else return super.primaryToSQL(translator);
    }

    @Override
    public ADQLConstraint primaryGetCopy() throws ParseException {
        UCDFunction copy = new UCDFunction(tableName, ucd, unit, printUcdAsAlias);
        if (replacementColumn != null) copy.setColumn((ADQLColumnAndMeta) replacementColumn.getCopy());
        copy.setAlias(alias);
        copy.negativate(negative);
        return copy;
    }

    @Override
    public Vector<ADQLObject> primaryGetAll(SearchHandler searchCondition) {
        Vector<ADQLObject> vMatched = new Vector<ADQLObject>();
        if (replacementColumn != null) {
            if (searchCondition.match(replacementColumn)) vMatched.add(replacementColumn); else {
                Vector<ADQLObject> temp = replacementColumn.getAll(searchCondition);
                if (temp != null && temp.size() > 0) vMatched.addAll(temp);
            }
        }
        return vMatched;
    }

    @Override
    public ADQLObject primaryGetFirst(SearchHandler searchCondition) {
        ADQLObject matchedObj = null;
        if (replacementColumn != null) {
            if (searchCondition.match(replacementColumn)) matchedObj = replacementColumn; else matchedObj = replacementColumn.getFirst(searchCondition);
        }
        return matchedObj;
    }
}
