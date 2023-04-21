package mybridge.core.util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MysqlServerDef {

    public static final int COM_QUIT = 1;

    public static final int COM_QUERY = 3;

    public static final int COM_INIT_DB = 2;

    public static final int COM_FIELD_LIST = 4;

    public static List<String> variableList = new ArrayList<String>();

    public static List<String> coolationList = new ArrayList<String>();

    public static HashMap<Integer, String> index2Charset = new HashMap<Integer, String>();

    public static final int COM_BINLOG_DUMP = 18;

    public static final int COM_CHANGE_USER = 17;

    public static final int COM_CLOSE_STATEMENT = 25;

    public static final int COM_CONNECT_OUT = 20;

    public static final int COM_END = 29;

    public static final int COM_EXECUTE = 23;

    public static final int COM_FETCH = 28;

    public static final int COM_LONG_DATA = 24;

    public static final int COM_PREPARE = 22;

    public static final int COM_REGISTER_SLAVE = 21;

    public static final int COM_RESET_STMT = 26;

    public static final int COM_SET_OPTION = 27;

    public static final int COM_TABLE_DUMP = 19;

    public static final int CONNECT = 11;

    public static final int CREATE_DB = 5;

    public static final int DEBUG = 13;

    public static final int DELAYED_INSERT = 16;

    public static final int DROP_DB = 6;

    public static final int FIELD_LIST = 4;

    public static final int FIELD_TYPE_BIT = 16;

    public static final int FIELD_TYPE_BLOB = 252;

    public static final int FIELD_TYPE_DATE = 10;

    public static final int FIELD_TYPE_DATETIME = 12;

    public static final int FIELD_TYPE_DECIMAL = 0;

    public static final int FIELD_TYPE_DOUBLE = 5;

    public static final int FIELD_TYPE_ENUM = 247;

    public static final int FIELD_TYPE_FLOAT = 4;

    public static final int FIELD_TYPE_GEOMETRY = 255;

    public static final int FIELD_TYPE_INT24 = 9;

    public static final int FIELD_TYPE_LONG = 3;

    public static final int FIELD_TYPE_LONG_BLOB = 251;

    public static final int FIELD_TYPE_LONGLONG = 8;

    public static final int FIELD_TYPE_MEDIUM_BLOB = 250;

    public static final int FIELD_TYPE_NEW_DECIMAL = 246;

    public static final int FIELD_TYPE_NEWDATE = 14;

    public static final int FIELD_TYPE_NULL = 6;

    public static final int FIELD_TYPE_SET = 248;

    public static final int FIELD_TYPE_SHORT = 2;

    public static final int FIELD_TYPE_STRING = 254;

    public static final int FIELD_TYPE_TIME = 11;

    public static final int FIELD_TYPE_TIMESTAMP = 7;

    public static final int FIELD_TYPE_TINY = 1;

    public static final int FIELD_TYPE_TINY_BLOB = 249;

    public static final int FIELD_TYPE_VAR_STRING = 253;

    public static final int FIELD_TYPE_VARCHAR = 15;

    public static final int SLEEP = 0;

    public static final int STATISTICS = 9;

    public static final int TIME = 15;

    public static int javaTypeToMysql(int javaType) {
        int mysqlType;
        switch(javaType) {
            case Types.DECIMAL:
                mysqlType = FIELD_TYPE_DECIMAL;
                break;
            case Types.SMALLINT:
                mysqlType = FIELD_TYPE_SHORT;
                break;
            case Types.TINYINT:
                mysqlType = FIELD_TYPE_TINY;
                break;
            case Types.INTEGER:
                mysqlType = FIELD_TYPE_LONG;
                break;
            case Types.REAL:
                mysqlType = FIELD_TYPE_FLOAT;
                break;
            case Types.DOUBLE:
                mysqlType = FIELD_TYPE_DOUBLE;
                break;
            case Types.NULL:
                mysqlType = FIELD_TYPE_NULL;
                break;
            case Types.TIMESTAMP:
                mysqlType = FIELD_TYPE_TIMESTAMP;
                break;
            case Types.BIGINT:
                mysqlType = FIELD_TYPE_LONGLONG;
                break;
            case Types.DATE:
                mysqlType = FIELD_TYPE_NEWDATE;
                break;
            case Types.TIME:
                mysqlType = FIELD_TYPE_TIME;
                break;
            case Types.CHAR:
                mysqlType = FIELD_TYPE_STRING;
                break;
            case Types.LONGVARBINARY:
                mysqlType = FIELD_TYPE_BLOB;
                break;
            case Types.VARCHAR:
                mysqlType = FIELD_TYPE_VARCHAR;
                break;
            case Types.BINARY:
                mysqlType = FIELD_TYPE_GEOMETRY;
                break;
            case Types.BIT:
                mysqlType = FIELD_TYPE_BIT;
                break;
            default:
                mysqlType = FIELD_TYPE_VARCHAR;
        }
        return mysqlType;
    }

    static {
        variableList.add("character_set_client,utf8");
        variableList.add("character_set_connection,utf8");
        variableList.add("character_set_database,utf8");
        variableList.add("character_set_filesystem,utf8");
        variableList.add("character_set_results,utf8");
        variableList.add("character_set_server,utf8");
        variableList.add("character_set_system,utf8");
        variableList.add("collation_connection,utf8_general_ci");
        variableList.add("max_allowed_packet,1048576");
        coolationList.add("Collation,Charset,Id,Default,Compiled,Sortlen");
        coolationList.add("dec8_swedish_ci,dec8,3,Yes,,0");
        coolationList.add("dec8_bin,dec8,69,,,0");
        coolationList.add("cp850_general_ci,cp850,4,Yes,,0");
        coolationList.add("cp850_bin,cp850,80,,,0");
        coolationList.add("hp8_english_ci,hp8,6,Yes,,0");
        coolationList.add("hp8_bin,hp8,72,,,0");
        coolationList.add("koi8r_general_ci,koi8r,7,Yes,,0");
        coolationList.add("koi8r_bin,koi8r,74,,,0");
        coolationList.add("latin1_german1_ci,latin1,5,,Yes,1");
        coolationList.add("latin1_swedish_ci,latin1,8,Yes,Yes,1");
        coolationList.add("latin1_danish_ci,latin1,15,,Yes,1");
        coolationList.add("latin1_german2_ci,latin1,31,,Yes,2");
        coolationList.add("latin1_bin,latin1,47,,Yes,1");
        coolationList.add("latin1_general_ci,latin1,48,,Yes,1");
        coolationList.add("latin1_general_cs,latin1,49,,Yes,1");
        coolationList.add("latin1_spanish_ci,latin1,94,,Yes,1");
        coolationList.add("latin2_general_ci,latin2,9,Yes,,0");
        coolationList.add("latin2_hungarian_ci,latin2,21,,,0");
        coolationList.add("latin2_croatian_ci,latin2,27,,,0");
        coolationList.add("latin2_bin,latin2,77,,,0");
        coolationList.add("swe7_swedish_ci,swe7,10,Yes,,0");
        coolationList.add("swe7_bin,swe7,82,,,0");
        coolationList.add("ascii_general_ci,ascii,11,Yes,,0");
        coolationList.add("ascii_bin,ascii,65,,,0");
        coolationList.add("hebrew_general_ci,hebrew,16,Yes,,0");
        coolationList.add("hebrew_bin,hebrew,71,,,0");
        coolationList.add("koi8u_general_ci,koi8u,22,Yes,,0");
        coolationList.add("koi8u_bin,koi8u,75,,,0");
        coolationList.add("greek_general_ci,greek,25,Yes,,0");
        coolationList.add("greek_bin,greek,70,,,0");
        coolationList.add("cp1250_general_ci,cp1250,26,Yes,,0");
        coolationList.add("cp1250_croatian_ci,cp1250,44,,,0");
        coolationList.add("cp1250_bin,cp1250,66,,,0");
        coolationList.add("cp1250_polish_ci,cp1250,99,,,0");
        coolationList.add("latin5_turkish_ci,latin5,30,Yes,,0");
        coolationList.add("latin5_bin,latin5,78,,,0");
        coolationList.add("armscii8_general_ci,armscii8,32,Yes,,0");
        coolationList.add("armscii8_bin,armscii8,64,,,0");
        coolationList.add("utf8_general_ci,utf8,33,Yes,Yes,1");
        coolationList.add("utf8_bin,utf8,83,,Yes,1");
        coolationList.add("utf8_unicode_ci,utf8,192,,Yes,8");
        coolationList.add("utf8_icelandic_ci,utf8,193,,Yes,8");
        coolationList.add("utf8_latvian_ci,utf8,194,,Yes,8");
        coolationList.add("utf8_romanian_ci,utf8,195,,Yes,8");
        coolationList.add("utf8_slovenian_ci,utf8,196,,Yes,8");
        coolationList.add("utf8_polish_ci,utf8,197,,Yes,8");
        coolationList.add("utf8_estonian_ci,utf8,198,,Yes,8");
        coolationList.add("utf8_spanish_ci,utf8,199,,Yes,8");
        coolationList.add("utf8_swedish_ci,utf8,200,,Yes,8");
        coolationList.add("utf8_turkish_ci,utf8,201,,Yes,8");
        coolationList.add("utf8_czech_ci,utf8,202,,Yes,8");
        coolationList.add("utf8_danish_ci,utf8,203,,Yes,8");
        coolationList.add("utf8_lithuanian_ci,utf8,204,,Yes,8");
        coolationList.add("utf8_slovak_ci,utf8,205,,Yes,8");
        coolationList.add("utf8_spanish2_ci,utf8,206,,Yes,8");
        coolationList.add("utf8_roman_ci,utf8,207,,Yes,8");
        coolationList.add("utf8_persian_ci,utf8,208,,Yes,8");
        coolationList.add("utf8_esperanto_ci,utf8,209,,Yes,8");
        coolationList.add("utf8_hungarian_ci,utf8,210,,Yes,8");
        coolationList.add("cp866_general_ci,cp866,36,Yes,,0");
        coolationList.add("cp866_bin,cp866,68,,,0");
        coolationList.add("keybcs2_general_ci,keybcs2,37,Yes,,0");
        coolationList.add("keybcs2_bin,keybcs2,73,,,0");
        coolationList.add("macce_general_ci,macce,38,Yes,,0");
        coolationList.add("macce_bin,macce,43,,,0");
        coolationList.add("macroman_general_ci,macroman,39,Yes,,0");
        coolationList.add("macroman_bin,macroman,53,,,0");
        coolationList.add("cp852_general_ci,cp852,40,Yes,,0");
        coolationList.add("cp852_bin,cp852,81,,,0");
        coolationList.add("latin7_estonian_cs,latin7,20,,,0");
        coolationList.add("latin7_general_ci,latin7,41,Yes,,0");
        coolationList.add("latin7_general_cs,latin7,42,,,0");
        coolationList.add("latin7_bin,latin7,79,,,0");
        coolationList.add("cp1251_bulgarian_ci,cp1251,14,,,0");
        coolationList.add("cp1251_ukrainian_ci,cp1251,23,,,0");
        coolationList.add("cp1251_bin,cp1251,50,,,0");
        coolationList.add("cp1251_general_ci,cp1251,51,Yes,,0");
        coolationList.add("cp1251_general_cs,cp1251,52,,,0");
        coolationList.add("cp1256_general_ci,cp1256,57,Yes,,0");
        coolationList.add("cp1256_bin,cp1256,67,,,0");
        coolationList.add("cp1257_lithuanian_ci,cp1257,29,,,0");
        coolationList.add("cp1257_bin,cp1257,58,,,0");
        coolationList.add("cp1257_general_ci,cp1257,59,Yes,,0");
        coolationList.add("binary,binary,63,Yes,Yes,1");
        coolationList.add("geostd8_general_ci,geostd8,92,Yes,,0");
        coolationList.add("geostd8_bin,geostd8,93,,,0");
        index2Charset.put(8, "latin1");
    }
}
