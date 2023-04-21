package cb_commonobjects.datastore.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import cb_commonobjects.datastore.AbstractDataProvider;
import cb_commonobjects.datastore.AbstractSQLDataProvider;
import cb_commonobjects.datastore.AbstractTable;
import cb_commonobjects.datastore.AttributeTypeEnum;
import cb_commonobjects.logging.GlobalLog;

/**
 *
 * @author B1
 */
public class SQLiteTable extends AbstractTable {

    public SQLiteTable(String thisTableName) {
        super(thisTableName);
    }

    @Override
    protected String _getTypeName(AttributeTypeEnum thisType, int thisFieldLength1, boolean thisIsAutoIncr) {
        if (thisType == AttributeTypeEnum.ATT_TYPE_INTEGER || thisType == AttributeTypeEnum.ATT_TYPE_ID) {
            return "INTEGER";
        } else if (thisType == AttributeTypeEnum.ATT_TYPE_BINARY) {
            return "BLOB";
        } else if (thisType == AttributeTypeEnum.ATT_TYPE_BOOLEAN) {
            return "INTEGER";
        } else if (thisType == AttributeTypeEnum.ATT_TYPE_STRING) {
            return "TEXT";
        } else if (thisType == AttributeTypeEnum.ATT_TYPE_DOUBLE) {
            return "REAL";
        } else {
            System.out.println("Warning: SQL Type not supported !");
            return "NONE";
        }
    }

    protected AttributeTypeEnum _getTypeName(String thisType, int thisFieldLength1) {
        String myTempStr = thisType.trim().toUpperCase();
        if (myTempStr.equals("INTEGER") && thisFieldLength1 != -1 && thisFieldLength1 != 1) {
            return AttributeTypeEnum.ATT_TYPE_INTEGER;
        } else if (myTempStr.equals("BLOB")) {
            return AttributeTypeEnum.ATT_TYPE_BINARY;
        } else if (myTempStr.equals("INTEGER") && thisFieldLength1 == 1) {
            return AttributeTypeEnum.ATT_TYPE_BOOLEAN;
        } else if (myTempStr.equals("REAL")) {
            return AttributeTypeEnum.ATT_TYPE_DOUBLE;
        } else if (myTempStr.equals("INTEGER") && thisFieldLength1 == -1) {
            return AttributeTypeEnum.ATT_TYPE_ID;
        } else if (myTempStr.equals("TEXT")) {
            return AttributeTypeEnum.ATT_TYPE_STRING;
        } else {
            return AttributeTypeEnum.ATT_TYPE_UNKNOWN;
        }
    }

    @Override
    protected String _getConstraints(TableField thisField) {
        String myReturnStr = "";
        if (thisField.getType() == AttributeTypeEnum.ATT_TYPE_BOOLEAN) {
            myReturnStr += "(1)";
        } else {
            if (thisField.getType() != AttributeTypeEnum.ATT_TYPE_INTEGER) {
                if (thisField.getTypeLength1() != -1 && thisField.getTypeLength2() == -1) {
                    myReturnStr += "(" + Integer.toString(thisField.getTypeLength1()) + ")";
                } else if (thisField.getTypeLength1() != -1 && thisField.getTypeLength2() != -1) {
                    myReturnStr += "(" + Integer.toString(thisField.getTypeLength1()) + "," + Integer.toString(thisField.getTypeLength2()) + ")";
                }
            }
        }
        myReturnStr += (thisField.getIsPK() ? " primary key " : "") + (thisField.getIsAutoIncrement() ? " autoincrement " : "");
        return myReturnStr;
    }

    @Override
    public boolean initTable(AbstractDataProvider thisProvider) {
        boolean myReturnVal = false;
        myProvider = thisProvider;
        if (myProvider instanceof AbstractSQLDataProvider) {
            if (((AbstractSQLDataProvider) myProvider).checkTableExists(myTableName)) {
                myReturnVal = ((AbstractSQLDataProvider) myProvider).checkTable(this);
                if (myReturnVal) {
                    String mySQL = "pragma table_info(" + myTableName + ")";
                    ResultSet myRes = ((AbstractSQLDataProvider) myProvider).selectQuery(mySQL);
                    try {
                        while (myRes.next()) {
                            AbstractTable.TableField myTableField;
                            String myFieldName = myRes.getString(2);
                            if (!this.existsField(myFieldName)) {
                                String myTempAttType = myRes.getString(3);
                                int myFieldLength1 = -1;
                                int myFieldLength2 = -1;
                                if (myTempAttType.indexOf("(") != -1) {
                                    String myTempStr = myTempAttType.substring(myTempAttType.indexOf("(") + 1, myTempAttType.indexOf(")"));
                                    if (myTempStr.indexOf(",") != -1) {
                                        myFieldLength1 = Integer.parseInt(myTempStr.substring(0, myTempStr.indexOf(",")));
                                        myFieldLength2 = Integer.parseInt(myTempStr.substring(myTempStr.indexOf(",") + 1));
                                    } else {
                                        myFieldLength1 = Integer.parseInt(myTempStr);
                                    }
                                    myTempAttType = myTempAttType.substring(0, myTempAttType.indexOf("(")).trim();
                                }
                                AttributeTypeEnum myAttType = _getTypeName(myTempAttType, myFieldLength1);
                                myTableField = this.getNewTableField(myFieldName, myAttType, myFieldLength1, myFieldLength2);
                                myTableField.setIsPK(myRes.getBoolean(6));
                                myTableField.setIsAutoIncrement(myTableField.getIsPK());
                                this.myFields.add(myTableField);
                            }
                        }
                    } catch (SQLException ex) {
                        GlobalLog.logError(ex);
                    } finally {
                        ((AbstractSQLDataProvider) myProvider).closeResultSet(myRes);
                    }
                    myReturnVal = true;
                } else {
                    myReturnVal = false;
                }
            } else {
                myReturnVal = ((AbstractSQLDataProvider) myProvider).createTable(this);
            }
        } else {
            myReturnVal = false;
        }
        return myReturnVal;
    }
}
