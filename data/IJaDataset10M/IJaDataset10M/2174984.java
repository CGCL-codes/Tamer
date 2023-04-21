package org.hsqldb;

import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.IntValueHashMap;

/**
 * Defines and enumerates reserved and non-reserved SQL
 * keywords. <p>
 *
 * @author  Nitin Chauhan
 * @author  fredt@users
 * @since 1.7.2
 * @version 1.7.2
 */
public class Token {

    private static IntValueHashMap commandSet;

    static final String T_ASTERISK = "*";

    static final String T_COMMA = ",";

    static final String T_CLOSEBRACKET = ")";

    static final String T_EQUALS = "=";

    public static final String T_DIVIDE = "/";

    static final String T_OPENBRACKET = "(";

    static final String T_SEMICOLON = ";";

    static final String T_MULTIPLY = "*";

    static final String T_PERCENT = "%";

    static final String T_PLUS = "+";

    static final String T_QUESTION = "?";

    static final String T_ADD = "ADD";

    static final String T_ALL = "ALL";

    static final String T_ALLOCATE = "ALLOCATE";

    public static final String T_ALTER = "ALTER";

    static final String T_AND = "AND";

    static final String T_ANY = "ANY";

    static final String T_ARE = "ARE";

    static final String T_ARRAY = "ARRAY";

    static final String T_AS = "AS";

    static final String T_ASENSITIVE = "ASENSITIVE";

    static final String T_ASYMMETRIC = "ASYMMETRIC";

    static final String T_AT = "AT";

    static final String T_ATOMIC = "ATOMIC";

    static final String T_AUTHORIZATION = "AUTHORIZATION";

    static final String T_BEGIN = "BEGIN";

    static final String T_BETWEEN = "BETWEEN";

    static final String T_BIGINT = "BIGINT";

    public static final String T_BINARY = "BINARY";

    static final String T_BLOB = "BLOB";

    static final String T_BOOLEAN = "BOOLEAN";

    static final String T_BOTH = "BOTH";

    static final String T_BY = "BY";

    static final String T_CALL = "CALL";

    static final String T_CALLED = "CALLED";

    static final String T_CASCADED = "CASCADED";

    static final String T_CASE = "CASE";

    static final String T_CAST = "CAST";

    static final String T_CHAR = "CHAR";

    static final String T_CHARACTER = "CHARACTER";

    static final String T_CHECK = "CHECK";

    static final String T_CLOB = "CLOB";

    static final String T_CLOSE = "CLOSE";

    static final String T_COLLATE = "COLLATE";

    static final String T_COLUMN = "COLUMN";

    public static final String T_COMMIT = "COMMIT";

    static final String T_CONDITION = "CONDIITON";

    static final String T_CONNECT = "CONNECT";

    static final String T_CONSTRAINT = "CONSTRAINT";

    static final String T_CONTINUE = "CONTINUE";

    static final String T_CORRESPONDING = "CORRESPONDING";

    static final String T_CREATE = "CREATE";

    static final String T_CROSS = "CROSS";

    static final String T_CUBE = "CUBE";

    static final String T_CURRENT = "CURRENT";

    static final String T_CURRENT_DATE = "CURRENT_DATE";

    static final String T_CURRENT_DEFAULT_TRANS_GROUP = "CURRENT_DEFAULT_TRANSFORM_GROUP";

    static final String T_CURRENT_PATH = "CURRENT_PATH";

    static final String T_CURRENT_ROLE = "CURRENT_ROLE";

    static final String T_CURRENT_TIME = "CURRENT_TIME";

    static final String T_CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";

    static final String T_CURRENT_TRANS_TROUP_FOR_TYPE = "CURRENT_TRANSFORM_GROUP_FOR_TYPE";

    static final String T_CURRENT_USER = "CURRENT_USER";

    static final String T_CURSOR = "CURSOR";

    static final String T_CYCLE = "CYCLE";

    static final String T_DATE = "DATE";

    static final String T_DAY = "DAY";

    static final String T_DEALLOCATE = "DEALLOCATE";

    static final String T_DEC = "DEC";

    static final String T_DECIMAL = "DECIMAL";

    static final String T_DECLARE = "DECLARE";

    static final String T_DEFAULT = "DEFAULT";

    public static final String T_DELETE = "DELETE";

    static final String T_DEREF = "DEREF";

    static final String T_DESCRIBE = "DESCRIBE";

    static final String T_DETERMINISTIC = "DETERMINISTIC";

    static final String T_DISCONNECT = "DISCONNECT";

    static final String T_DISTINCT = "DISTINCT";

    static final String T_DO = "DO";

    static final String T_DOUBLE = "DOUBLE";

    static final String T_DOW = "DAYOFWEEK";

    static final String T_DROP = "DROP";

    static final String T_DYNAMIC = "DYNAMIC";

    static final String T_EACH = "EACH";

    static final String T_ELEMENT = "ELEMENT";

    static final String T_ELSE = "ELSE";

    static final String T_ELSEIF = "ELSEIF";

    static final String T_END = "END";

    static final String T_ESCAPE = "ESCAPE";

    static final String T_EXCEPT = "EXCEPT";

    static final String T_EXEC = "EXEC";

    static final String T_EXECUTE = "EXECUTE";

    static final String T_EXISTS = "EXISTS";

    static final String T_EXIT = "EXIT";

    static final String T_EXTERNAL = "EXTERNAL";

    static final String T_FALSE = "FALSE";

    static final String T_FETCH = "FETCH";

    static final String T_FILTER = "FILTER";

    static final String T_FLOAT = "FLOAT";

    static final String T_FOR = "FOR";

    static final String T_FOREIGN = "FOREIGN";

    static final String T_FREE = "FREE";

    static final String T_FROM = "FROM";

    static final String T_FULL = "FULL";

    static final String T_FUNCTION = "FUNCTION";

    static final String T_GET = "GET";

    static final String T_GLOBAL = "GLOBAL";

    static final String T_GRANT = "GRANT";

    static final String T_GROUP = "GROUP";

    static final String T_GROUPING = "GROUPING";

    static final String T_HANDLER = "HANDLER";

    static final String T_HAVING = "HAVING";

    static final String T_HEADER = "HEADER";

    static final String T_HOLD = "HOLD";

    static final String T_HOUR = "HOUR";

    static final String T_IDENTITY = "IDENTITY";

    static final String T_IF = "IF";

    static final String T_IMMEDIATE = "IMMEDIATE";

    static final String T_IN = "IN";

    static final String T_INDICATOR = "INDICATOR";

    static final String T_INNER = "INNER";

    static final String T_INOUT = "INOUT";

    static final String T_INPUT = "INPUT";

    static final String T_INSENSITIVE = "INSENSITIVE";

    public static final String T_INSERT = "INSERT";

    static final String T_INT = "INT";

    static final String T_INTEGER = "INTEGER";

    static final String T_INTERSECT = "INTERSECT";

    static final String T_INTERVAL = "INTERVAL";

    static final String T_INTO = "INTO";

    static final String T_IS = "IS";

    static final String T_ITERATE = "ITERATE";

    static final String T_JOIN = "JOIN";

    static final String T_LANGUAGE = "LANGUAGE";

    static final String T_LARGE = "LARGE";

    static final String T_LATERAL = "LATERAL";

    static final String T_LEADING = "LEADING";

    static final String T_LEAVE = "LEAVE";

    static final String T_LEFT = "LEFT";

    static final String T_LIKE = "LIKE";

    static final String T_LOCAL = "LOCAL";

    static final String T_LOCALTIME = "LOCALTIME";

    static final String T_LOCALTIMESTAMP = "LOCALTIMESTAMP";

    static final String T_LOOP = "LOOP";

    static final String T_MATCH = "MATCH";

    static final String T_MEMBER = "MEMBER";

    static final String T_METHOD = "METHOD";

    static final String T_MINUTE = "MINUTE";

    static final String T_MODIFIES = "MODIFIES";

    static final String T_MODULE = "MODULE";

    static final String T_MONTH = "MONTH";

    static final String T_MULTISET = "MULTISET";

    static final String T_NATIONAL = "NATIONAL";

    static final String T_NATURAL = "NAUTRAL";

    static final String T_NCHAR = "NCHAR";

    static final String T_NCLOB = "NCLOB";

    static final String T_NEW = "NEW";

    static final String T_NEXT = "NEXT";

    static final String T_NO = "NO";

    static final String T_NONE = "NONE";

    static final String T_NOT = "NOT";

    static final String T_NULL = "NULL";

    static final String T_NUMERIC = "NUMERIC";

    static final String T_OF = "OF";

    static final String T_OLD = "OLD";

    static final String T_ON = "ON";

    static final String T_ONLY = "ONLY";

    static final String T_OPEN = "OPEN";

    static final String T_OR = "OR";

    static final String T_ORDER = "ORDER";

    static final String T_OUT = "OUT";

    static final String T_OUTER = "OUTER";

    static final String T_OUTPUT = "OUTPUT";

    static final String T_OVER = "OVER";

    static final String T_OVERLAPS = "OVERLAPS";

    static final String T_PARAMETER = "PARAMETER";

    static final String T_PARTITION = "PARTITION";

    static final String T_PRECISION = "PRECISION";

    static final String T_PREPARE = "PREPARE";

    static final String T_PRIMARY = "PRIMARY";

    static final String T_PROCEDURE = "PROCEDURE";

    static final String T_RANGE = "RANGE";

    static final String T_READS = "READS";

    static final String T_REAL = "REAL";

    static final String T_RECURSIVE = "RECURSIVE";

    static final String T_REF = "REF";

    static final String T_REFERENCES = "REFERENCES";

    static final String T_REFERENCING = "REFERENCING";

    static final String T_RELEASE = "RELEASE";

    static final String T_REPEAT = "REPEAT";

    static final String T_RESIGNAL = "RESIGNAL";

    static final String T_RESULT = "RESULT";

    static final String T_RETURN = "RETURN";

    static final String T_RETURNS = "RETURNS";

    static final String T_REVOKE = "REVOKE";

    static final String T_RIGHT = "RIGHT";

    static final String T_ROLLBACK = "ROLLBACK";

    static final String T_ROLLUP = "ROLLUP";

    static final String T_ROW = "ROW";

    static final String T_ROWS = "ROWS";

    static final String T_SAVEPOINT = "SAVEPOINT";

    static final String T_SCOPE = "SCOPE";

    static final String T_SCROLL = "SCROLL";

    static final String T_SECOND = "SECOND";

    static final String T_SEARCH = "SEARCH";

    static final String T_SELECT = "SELECT";

    static final String T_SENSITIVE = "SENSITIVE";

    static final String T_SESSION_USER = "SESSION_USER";

    public static final String T_SET = "SET";

    static final String T_SIGNAL = "SIGNAL";

    static final String T_SIMILAR = "SIMILAR";

    static final String T_SMALLINT = "SMALLINT";

    static final String T_SOME = "SOME";

    static final String T_SPECIFIC = "SPECIFIC";

    static final String T_SPECIFICTYPE = "SPECIFICTYPE";

    static final String T_SQL = "SQL";

    static final String T_SQLEXCEPTION = "SQLEXCEPTION";

    static final String T_SQLSTATE = "SQLSTATE";

    static final String T_SQLWARNING = "SQLWARNING";

    static final String T_START = "START";

    static final String T_STATIC = "STATIC";

    static final String T_SUBMULTISET = "SUBMULTISET";

    static final String T_SYMMETRIC = "SYMMETRIC";

    static final String T_SYSTEM = "SYSTEM";

    static final String T_SYSTEM_USER = "SYSTEM_USER";

    public static final String T_TABLE = "TABLE";

    static final String T_TABLESAMPLE = "TABLESAMPLE";

    static final String T_THEN = "THEN";

    static final String T_TIME = "TIME";

    static final String T_TIMESTAMP = "TIMESTAMP";

    static final String T_TIMEZONE_HOUR = "TIMEZONE_HOUR";

    static final String T_TIMEZONE_MINUTE = "TIMEZONE_MINUTE";

    static final String T_TO = "TO";

    static final String T_TRAILING = "TRAILING";

    static final String T_TRANSLATION = "TRANSLATION";

    static final String T_TREAT = "TREAT";

    static final String T_TRIGGER = "TRIGGER";

    static final String T_TRUE = "TRUE";

    static final String T_UNDO = "UNDO";

    static final String T_UNION = "UNION";

    static final String T_UNIQUE = "UNIQUE";

    static final String T_UNKNOWN = "UNKNOWN";

    static final String T_UNNEST = "UNNEST";

    static final String T_UNTIL = "UNTIL";

    static final String T_UPDATE = "UPDATE";

    static final String T_USER = "USER";

    static final String T_USING = "USING";

    static final String T_VALUE = "VALUE";

    static final String T_VALUES = "VALUES";

    static final String T_VARCHAR = "VARCHAR";

    static final String T_VARYING = "VARYING";

    static final String T_WHEN = "WHEN";

    static final String T_WHENEVER = "WHENEVER";

    static final String T_WHERE = "WHERE";

    static final String T_WHILE = "WHILE";

    static final String T_WINDOW = "WINDOW";

    static final String T_WITH = "WITH";

    static final String T_WITHIN = "WITHIN";

    static final String T_WITHOUT = "WITHOUT";

    static final String T_YEAR = "YEAR";

    static final String T_ALWAYS = "ALWAYS";

    static final String T_ACTION = "ACTION";

    static final String T_ADMIN = "ADMIN";

    static final String T_AFTER = "AFTER";

    static final String T_ALIAS = "ALIAS";

    static final String T_ASC = "ASC";

    static final String T_AUTOCOMMIT = "AUTOCOMMIT";

    static final String T_AVG = "AVG";

    static final String T_BACKUP = "BACKUP";

    static final String T_BEFORE = "BEFORE";

    static final String T_CACHED = "CACHED";

    static final String T_CASCADE = "CASCADE";

    static final String T_CASEWHEN = "CASEWHEN";

    static final String T_CHECKPOINT = "CHECKPOINT";

    static final String T_CLASS = "CLASS";

    static final String T_COALESCE = "COALESCE";

    static final String T_COLLATION = "COLLATION";

    static final String T_COMPACT = "COMPACT";

    public static final String T_COMPRESSED = "COMPRESSED";

    static final String T_CONVERT = "CONVERT";

    static final String T_COUNT = "COUNT";

    static final String T_DATABASE = "DATABASE";

    static final String T_DEFRAG = "DEFRAG";

    static final String T_DESC = "DESC";

    static final String T_EVERY = "EVERY";

    static final String T_EXPLAIN = "EXPLAIN";

    static final String T_EXTRACT = "EXTRACT";

    static final String T_GENERATED = "GENERATED";

    static final String T_IFNULL = "IFNULL";

    static final String T_IGNORECASE = "IGNORECASE";

    static final String T_IMMEDIATELY = "IMMEDIATELY";

    static final String T_INCREMENT = "INCREMENT";

    static final String T_INDEX = "INDEX";

    static final String T_INITIAL = "INITIAL";

    static final String T_KEY = "KEY";

    static final String T_LIMIT = "LIMIT";

    static final String T_LOGSIZE = "LOGSIZE";

    static final String T_MAX = "MAX";

    static final String T_MAXROWS = "MAXROWS";

    static final String T_MEMORY = "MEMORY";

    static final String T_MERGE = "MERGE";

    static final String T_MIN = "MIN";

    static final String T_MINUS = "MINUS";

    static final String T_NOW = "NOW";

    static final String T_NOWAIT = "NOWAIT";

    static final String T_NULLIF = "NULLIF";

    static final String T_NVL = "NVL";

    static final String T_OFFSET = "OFFSET";

    static final String T_PASSWORD = "PASSWORD";

    public static final String T_SCHEMA = "SCHEMA";

    static final String T_PLAN = "PLAN";

    static final String T_PRESERVE = "PRESERVE";

    static final String T_PRIVILEGES = "PRIVILEGES";

    static final String T_POSITION = "POSITION";

    static final String T_PROPERTY = "PROPERTY";

    static final String T_PUBLIC = "PUBLIC";

    static final String T_QUEUE = "QUEUE";

    static final String T_READONLY = "READONLY";

    static final String T_REFERENTIAL_INTEGRITY = "REFERENTIAL_INTEGRITY";

    static final String T_RENAME = "RENAME";

    static final String T_RESTART = "RESTART";

    static final String T_RESTRICT = "RESTRICT";

    static final String T_ROLE = "ROLE";

    static final String T_SCRIPT = "SCRIPT";

    static final String T_SCRIPTFORMAT = "SCRIPTFORMAT";

    static final String T_SEQUENCE = "SEQUENCE";

    static final String T_SHUTDOWN = "SHUTDOWN";

    public static final String T_SOURCE = "SOURCE";

    static final String T_STDDEV_POP = "STDDEV_POP";

    static final String T_STDDEV_SAMP = "STDDEV_SAMP";

    static final String T_SUBSTRING = "SUBSTRING";

    static final String T_SUM = "SUM";

    static final String T_SYSDATE = "SYSDATE";

    static final String T_TEMP = "TEMP";

    static final String T_TEMPORARY = "TEMPORARY";

    public static final String T_TEXT = "TEXT";

    static final String T_TODAY = "TODAY";

    static final String T_TOP = "TOP";

    static final String T_TRIM = "TRIM";

    static final String T_VAR_POP = "VAR_POP";

    static final String T_VAR_SAMP = "VAR_SAMP";

    static final String T_VIEW = "VIEW";

    static final String T_WORK = "WORK";

    static final String T_WRITE_DELAY = "WRITE_DELAY";

    public static final String T_OFF = "OFF";

    static final int ADD = 1;

    static final int ALL = 2;

    static final int ALLOCATE = 3;

    static final int ALTER = 4;

    static final int AND = 5;

    static final int ANY = 6;

    static final int ARE = 7;

    static final int ARRAY = 8;

    static final int AS = 9;

    static final int ASENSITIVE = 10;

    static final int ASYMMETRIC = 11;

    static final int AT = 12;

    static final int ATOMIC = 13;

    static final int AUTHORIZATION = 14;

    static final int BEGIN = 15;

    static final int BETWEEN = 16;

    static final int BIGINT = 17;

    static final int BINARY = 18;

    static final int BLOB = 19;

    static final int BOOLEAN = 20;

    static final int BOTH = 21;

    static final int BY = 22;

    public static final int CALL = 23;

    static final int CALLED = 24;

    static final int CASCADED = 25;

    static final int CASE = 26;

    static final int CAST = 27;

    static final int CHAR = 28;

    static final int CHARACTER = 29;

    static final int CHECK = 30;

    static final int CLOB = 31;

    static final int CLOSE = 32;

    static final int COLLATE = 33;

    static final int COLUMN = 34;

    static final int COMMIT = 35;

    static final int CONDITION = 36;

    static final int CONNECT = 37;

    static final int CONSTRAINT = 38;

    static final int CONTINUE = 39;

    static final int CORRESPONDING = 40;

    static final int CREATE = 41;

    static final int CROSS = 42;

    static final int CUBE = 43;

    static final int CURRENT = 44;

    static final int CURRENT_DATE = 45;

    static final int CURRENT_DEFAULT_TRANSFORM_GRO = 46;

    static final int CURRENT_PATH = 47;

    static final int CURRENT_ROLE = 48;

    static final int CURRENT_TIME = 49;

    static final int CURRENT_TIMESTAMP = 50;

    static final int CURRENT_TRANSFORM_GROUP_FOR_T = 51;

    static final int CURRENT_USER = 52;

    static final int CURSOR = 53;

    static final int CYCLE = 54;

    static final int DATE = 55;

    static final int DAY = 56;

    static final int DEALLOCATE = 57;

    static final int DEC = 58;

    static final int DECIMAL = 59;

    static final int DECLARE = 60;

    static final int DEFAULT = 61;

    public static final int DELETE = 62;

    static final int DEREF = 63;

    static final int DESCRIBE = 64;

    static final int DETERMINISTIC = 65;

    static final int DISCONNECT = 66;

    static final int DISTINCT = 67;

    static final int DO = 68;

    static final int DOUBLE = 69;

    static final int DROP = 70;

    static final int DYNAMIC = 71;

    static final int EACH = 72;

    static final int ELEMENT = 73;

    static final int ELSE = 74;

    static final int ELSEIF = 75;

    static final int END = 76;

    static final int ESCAPE = 77;

    static final int EXCEPT = 78;

    static final int EXEC = 79;

    static final int EXECUTE = 80;

    static final int EXISTS = 81;

    static final int EXIT = 82;

    static final int EXTERNAL = 83;

    static final int FALSE = 84;

    static final int FETCH = 85;

    static final int FILTER = 86;

    static final int FLOAT = 87;

    static final int FOR = 88;

    static final int FOREIGN = 89;

    static final int FREE = 90;

    static final int FROM = 91;

    static final int FULL = 92;

    static final int FUNCTION = 93;

    static final int GET = 94;

    static final int GLOBAL = 95;

    static final int GRANT = 96;

    static final int GROUP = 97;

    static final int GROUPING = 98;

    static final int HANDLER = 99;

    static final int HAVING = 100;

    static final int HOLD = 101;

    static final int HOUR = 102;

    static final int IDENTITY = 103;

    static final int IF = 104;

    static final int IMMEDIATE = 105;

    static final int IN = 106;

    static final int INDICATOR = 107;

    static final int INNER = 108;

    static final int INOUT = 109;

    static final int INPUT = 110;

    static final int INSENSITIVE = 111;

    public static final int INSERT = 112;

    static final int INT = 113;

    static final int INTEGER = 114;

    static final int INTERSECT = 115;

    static final int INTERVAL = 116;

    static final int INTO = 117;

    static final int IS = 118;

    static final int ITERATE = 119;

    static final int JOIN = 120;

    static final int LANGUAGE = 121;

    static final int LARGE = 122;

    static final int LATERAL = 123;

    static final int LEADING = 124;

    static final int LEAVE = 125;

    static final int LEFT = 126;

    static final int LIKE = 127;

    static final int LOCAL = 128;

    static final int LOCALTIME = 129;

    static final int LOCALTIMESTAMP = 130;

    static final int LOOP = 131;

    static final int MATCH = 132;

    static final int MEMBER = 133;

    static final int MERGE = 134;

    static final int METHOD = 135;

    static final int MINUTE = 136;

    static final int MODIFIES = 137;

    static final int MODULE = 138;

    static final int MONTH = 139;

    static final int MULTISET = 140;

    static final int NATIONAL = 141;

    static final int NATURAL = 142;

    static final int NCHAR = 143;

    static final int NCLOB = 144;

    static final int NEW = 145;

    static final int NO = 146;

    static final int NONE = 147;

    static final int NOT = 148;

    static final int NULL = 149;

    static final int NUMERIC = 150;

    static final int OF = 151;

    static final int OLD = 152;

    static final int ON = 153;

    static final int ONLY = 154;

    static final int OPEN = 155;

    static final int OR = 156;

    static final int ORDER = 157;

    static final int OUT = 158;

    static final int OUTER = 159;

    static final int OUTPUT = 160;

    static final int OVER = 161;

    static final int OVERLAPS = 162;

    static final int PARAMETER = 163;

    static final int PARTITION = 164;

    static final int PRECISION = 165;

    static final int PREPARE = 166;

    static final int PRIMARY = 167;

    static final int PROCEDURE = 168;

    static final int RANGE = 169;

    static final int READS = 170;

    static final int REAL = 171;

    static final int RECURSIVE = 172;

    static final int REF = 173;

    static final int REFERENCES = 174;

    static final int REFERENCING = 175;

    static final int RELEASE = 176;

    static final int REPEAT = 177;

    static final int RESIGNAL = 178;

    static final int RESULT = 179;

    static final int RETURN = 180;

    static final int RETURNS = 181;

    static final int REVOKE = 182;

    static final int RIGHT = 183;

    static final int ROLLBACK = 184;

    static final int ROLLUP = 185;

    static final int ROW = 186;

    static final int ROWS = 187;

    static final int SAVEPOINT = 188;

    static final int SCOPE = 189;

    static final int SCROLL = 190;

    static final int SEARCH = 191;

    static final int SECOND = 192;

    public static final int SELECT = 193;

    static final int SENSITIVE = 194;

    static final int SESSION_USER = 195;

    static final int SET = 196;

    static final int SIGNAL = 197;

    static final int SIMILAR = 198;

    static final int SMALLINT = 199;

    static final int SOME = 200;

    static final int SPECIFIC = 201;

    static final int SPECIFICTYPE = 202;

    static final int SQL = 203;

    static final int SQLEXCEPTION = 204;

    static final int SQLSTATE = 205;

    static final int SQLWARNING = 206;

    static final int START = 207;

    static final int STATIC = 208;

    static final int SUBMULTISET = 209;

    static final int SYMMETRIC = 210;

    static final int SYSTEM = 211;

    static final int SYSTEM_USER = 212;

    static final int TABLE = 213;

    static final int TABLESAMPLE = 214;

    static final int THEN = 215;

    static final int TIME = 216;

    static final int TIMESTAMP = 217;

    static final int TIMEZONE_HOUR = 218;

    static final int TIMEZONE_MINUTE = 219;

    static final int TO = 220;

    static final int TRAILING = 221;

    static final int TRANSLATION = 222;

    static final int TREAT = 223;

    static final int TRIGGER = 224;

    static final int TRUE = 225;

    static final int UNDO = 226;

    static final int UNION = 227;

    static final int UNIQUE = 228;

    static final int UNKNOWN = 229;

    static final int UNNEST = 220;

    static final int UNTIL = 221;

    public static final int UPDATE = 222;

    static final int USER = 223;

    static final int USING = 224;

    static final int VALUE = 225;

    static final int VALUES = 226;

    static final int VARCHAR = 227;

    static final int VARYING = 228;

    static final int WHEN = 229;

    static final int WHENEVER = 230;

    static final int WHERE = 231;

    static final int WHILE = 232;

    static final int WINDOW = 233;

    static final int WITH = 234;

    static final int WITHIN = 235;

    static final int WITHOUT = 236;

    static final int YEAR = 237;

    public static final int UNKNOWNTOKEN = -1;

    static final int ALIAS = 300;

    static final int AUTOCOMMIT = 301;

    static final int CACHED = 302;

    static final int CHECKPOINT = 303;

    static final int EXPLAIN = 304;

    static final int IGNORECASE = 305;

    static final int INDEX = 306;

    static final int LOGSIZE = 307;

    static final int MATCHED = 308;

    static final int MAXROWS = 309;

    static final int MEMORY = 310;

    static final int MINUS = 311;

    static final int NEXT = 312;

    static final int OPENBRACKET = 313;

    static final int PASSWORD = 314;

    static final int PLAN = 315;

    static final int PROPERTY = 316;

    static final int READONLY = 317;

    static final int REFERENTIAL_INTEGRITY = 318;

    static final int RENAME = 319;

    static final int RESTART = 320;

    static final int SCRIPT = 321;

    static final int SCRIPTFORMAT = 322;

    static final int SEMICOLON = 323;

    static final int SEQUENCE = 324;

    static final int SHUTDOWN = 325;

    static final int SOURCE = 326;

    static final int TEMP = 327;

    static final int TEXT = 328;

    static final int VIEW = 329;

    static final int WRITE_DELAY = 330;

    static final int VAR_POP = 330;

    static final int VAR_SAMP = 331;

    static final int STDDEV_POP = 332;

    static final int STDDEV_SAMP = 333;

    static final int DEFRAG = 334;

    static final int INCREMENT = 335;

    static final int TOCHAR = 336;

    static final int DATABASE = 337;

    static final int SCHEMA = 338;

    static final int ROLE = 339;

    static final int DOW = 340;

    static final int INITIAL = 341;

    static {
        commandSet = newCommandSet();
    }

    /**
     * Retrieves a new map from set of string tokens to numeric tokens for
     * commonly encountered database command token occurences.
     *
     * @return a new map for the database command token set
     */
    private static IntValueHashMap newCommandSet() {
        IntValueHashMap commandSet;
        commandSet = new IntValueHashMap(67);
        commandSet.put(T_ADD, ADD);
        commandSet.put(T_ALIAS, ALIAS);
        commandSet.put(T_ALTER, ALTER);
        commandSet.put(T_AUTOCOMMIT, AUTOCOMMIT);
        commandSet.put(T_CACHED, CACHED);
        commandSet.put(T_CALL, CALL);
        commandSet.put(T_CHECK, CHECK);
        commandSet.put(T_CHECKPOINT, CHECKPOINT);
        commandSet.put(T_COLUMN, COLUMN);
        commandSet.put(T_COMMIT, COMMIT);
        commandSet.put(T_CONNECT, CONNECT);
        commandSet.put(T_CONSTRAINT, CONSTRAINT);
        commandSet.put(T_CREATE, CREATE);
        commandSet.put(T_DATABASE, DATABASE);
        commandSet.put(T_DELETE, DELETE);
        commandSet.put(T_DEFRAG, DEFRAG);
        commandSet.put(T_DISCONNECT, DISCONNECT);
        commandSet.put(T_DROP, DROP);
        commandSet.put(T_EXCEPT, EXCEPT);
        commandSet.put(T_EXPLAIN, EXPLAIN);
        commandSet.put(T_FOREIGN, FOREIGN);
        commandSet.put(T_GRANT, GRANT);
        commandSet.put(T_IGNORECASE, IGNORECASE);
        commandSet.put(T_INCREMENT, INCREMENT);
        commandSet.put(T_INDEX, INDEX);
        commandSet.put(T_INITIAL, INITIAL);
        commandSet.put(T_INSERT, INSERT);
        commandSet.put(T_INTERSECT, INTERSECT);
        commandSet.put(T_LOGSIZE, LOGSIZE);
        commandSet.put(T_MAXROWS, MAXROWS);
        commandSet.put(T_MEMORY, MEMORY);
        commandSet.put(T_MINUS, MINUS);
        commandSet.put(T_NEXT, NEXT);
        commandSet.put(T_NOT, NOT);
        commandSet.put(T_OPENBRACKET, OPENBRACKET);
        commandSet.put(T_PASSWORD, PASSWORD);
        commandSet.put(T_PLAN, PLAN);
        commandSet.put(T_PRIMARY, PRIMARY);
        commandSet.put(T_PROPERTY, PROPERTY);
        commandSet.put(T_READONLY, READONLY);
        commandSet.put(T_REFERENTIAL_INTEGRITY, REFERENTIAL_INTEGRITY);
        commandSet.put(T_RELEASE, RELEASE);
        commandSet.put(T_RENAME, RENAME);
        commandSet.put(T_RESTART, RESTART);
        commandSet.put(T_REVOKE, REVOKE);
        commandSet.put(T_ROLE, ROLE);
        commandSet.put(T_ROLLBACK, ROLLBACK);
        commandSet.put(T_SAVEPOINT, SAVEPOINT);
        commandSet.put(T_SCRIPT, SCRIPT);
        commandSet.put(T_SCRIPTFORMAT, SCRIPTFORMAT);
        commandSet.put(T_SELECT, SELECT);
        commandSet.put(T_SEMICOLON, SEMICOLON);
        commandSet.put(T_SEQUENCE, SEQUENCE);
        commandSet.put(T_SET, SET);
        commandSet.put(T_SHUTDOWN, SHUTDOWN);
        commandSet.put(T_SOURCE, SOURCE);
        commandSet.put(T_TABLE, TABLE);
        commandSet.put(T_TEMP, TEMP);
        commandSet.put(T_TEXT, TEXT);
        commandSet.put(T_TRIGGER, TRIGGER);
        commandSet.put(T_UNIQUE, UNIQUE);
        commandSet.put(T_UPDATE, UPDATE);
        commandSet.put(T_UNION, UNION);
        commandSet.put(T_USER, USER);
        commandSet.put(T_VALUES, VALUES);
        commandSet.put(T_VIEW, VIEW);
        commandSet.put(T_WRITE_DELAY, WRITE_DELAY);
        commandSet.put(T_SCHEMA, SCHEMA);
        return commandSet;
    }

    public static int get(String token) {
        return commandSet.get(token, -1);
    }

    private static HashSet keywords;

    static IntValueHashMap valueTokens;

    static {
        keywords = new HashSet(67);
        String[] keyword = { Token.T_AS, Token.T_AND, Token.T_ALL, Token.T_ANY, Token.T_AVG, Token.T_BY, Token.T_BETWEEN, Token.T_BOTH, Token.T_CALL, Token.T_CASE, Token.T_CASEWHEN, Token.T_CAST, Token.T_CONVERT, Token.T_COUNT, Token.T_COALESCE, Token.T_DISTINCT, Token.T_ELSE, Token.T_END, Token.T_EVERY, Token.T_EXISTS, Token.T_EXCEPT, Token.T_EXTRACT, Token.T_FOR, Token.T_FROM, Token.T_GROUP, Token.T_HAVING, Token.T_IF, Token.T_INTO, Token.T_IFNULL, Token.T_IS, Token.T_IN, Token.T_INTERSECT, Token.T_JOIN, Token.T_INNER, Token.T_LEADING, Token.T_LIKE, Token.T_MAX, Token.T_MIN, Token.T_NEXT, Token.T_NULLIF, Token.T_NOT, Token.T_NVL, Token.T_MINUS, Token.T_ON, Token.T_ORDER, Token.T_OR, Token.T_OUTER, Token.T_POSITION, Token.T_PRIMARY, Token.T_SELECT, Token.T_SET, Token.T_SOME, Token.T_STDDEV_POP, Token.T_STDDEV_SAMP, Token.T_SUBSTRING, Token.T_SUM, Token.T_THEN, Token.T_TO, Token.T_TRAILING, Token.T_TRIM, Token.T_UNIQUE, Token.T_UNION, Token.T_VALUES, Token.T_VAR_POP, Token.T_VAR_SAMP, Token.T_WHEN, Token.T_WHERE };
        for (int i = 0; i < keyword.length; i++) {
            keywords.add(keyword[i]);
        }
    }

    public static boolean isKeyword(String token) {
        return keywords.contains(token);
    }
}
