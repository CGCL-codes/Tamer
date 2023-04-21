package atp.reporter.items.parameter.cits;

import atp.reporter.exception.*;
import atp.xml.Element;

public class RParameterInfo extends RParameterCITS {

    /**
     * ��� ��������
     */
    public static final String TYPE = "CITS.INFO";

    private static final String TABLE_TYPE = "TABLE_TYPE";

    private String tableType = "";

    protected void advancedToXML(Element element) {
        super.advancedToXML(element);
        element.setAttribute(TABLE_TYPE, "" + tableType);
    }

    protected void parseAdvancedProperties(Element element) throws RParseException {
        super.parseAdvancedProperties(element);
        String t = element.getValue(TABLE_TYPE);
        try {
            tableType = t;
        } catch (Exception e) {
            throw new RParseException("������� ������� ������� \"" + t + "\"");
        }
    }

    protected final String getTableType() {
        switch(getOKOParameter().type) {
            case 1:
                return "history_status";
            case 2:
                return "history_impulse";
            case 3:
            case 4:
                return "history_measure";
            case 8:
                return "_obj_1";
            case 16:
                return "_obj_2";
            case 32:
                return "_obj_3";
            default:
                return null;
        }
    }

    protected final String getTableName() {
        String table = getTableType();
        return getOKOParameter().type < 8 ? table + (tableType.equalsIgnoreCase("hour") ? "_hour" : tableType.equalsIgnoreCase("oper") ? "_oper" : "") : ((tableType.equalsIgnoreCase("hour") ? "var" : "const") + table);
    }

    public String getDescription() {
        return "�������� ��������� �� ������ INFO_";
    }

    public String getType() {
        return TYPE;
    }

    public void checkCompleteItem() throws RParseException {
        super.checkCompleteItem();
        if (tableType == null) throw new RParseException("�� ������� ������� [" + TABLE_TYPE + "]");
    }

    /**
     * ��������� ��� ��������� �� ������������
     * @param typeParam
     * @return
     */
    protected boolean isTypeParameterValid(int typeParam) {
        return typeParam == 1 || typeParam == 2 || typeParam == 3 || typeParam == 4 || typeParam == 8 || typeParam == 16 || typeParam == 32;
    }

    private class InfoSQL implements ParameterSQL {

        public final String text;

        public final boolean dateEnabled;

        InfoSQL(String cdng) {
            String tables = "";
            String videntify = "";
            String table;
            String valueField;
            String info;
            String IDField;
            String dateField;
            if (getOKOParameter().id == -4) {
                table = "object";
                valueField = "i.name";
                info = "";
                IDField = "id";
                dateField = "NULL";
                dateEnabled = false;
            } else {
                table = getTableName();
                info = "i.fid_dic=?";
                if (table.indexOf("const_obj") == 0) {
                    valueField = "i.value";
                    IDField = "fid_obj";
                    dateField = "NULL";
                    dateEnabled = false;
                } else if (table.indexOf("var_obj") == 0) {
                    valueField = "i.value";
                    IDField = "fid_obj";
                    dateField = "i.datet";
                    dateEnabled = true;
                } else {
                    valueField = "i.value";
                    IDField = "id";
                    dateField = "i.date_b";
                    dateEnabled = true;
                }
            }
            if (cdng != null) {
                tables = ", videntify v";
                videntify = " and i." + IDField + " between V.Id_Rep*10000 and (V.Id_Rep+1)*10000-1 and V.Name_Dept like '����-'||?";
            }
            String datePart = dateEnabled ? " " + dateField + " between to_date (?,'dd.mm.yyyy hh24:mi:ss') and to_date (?,'dd.mm.yyyy hh24:mi:ss') and " : "";
            text = "select " + IDField + " OBJECT, " + (dateEnabled ? "to_char (" + dateField + ",'dd.mm.yyyy hh24:mi:ss')" : "NULL") + " TIME, " + valueField + " VALUE from " + table + " i" + tables + " where" + datePart + info + videntify;
        }

        public String getText() {
            return text;
        }

        public boolean isDateEnabled() {
            return dateEnabled;
        }
    }

    protected ParameterSQL getParameterSQL(String cdng) {
        return new InfoSQL(cdng);
    }
}
