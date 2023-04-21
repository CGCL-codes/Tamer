package net.sf.indricotherium.sql.dml.bnf;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ReservedWordsEnum implements Serializable, Comparable {

    public static final ReservedWordsEnum ABSOLUTE = new ReservedWordsEnum("ABSOLUTE");

    public static final ReservedWordsEnum ACTION = new ReservedWordsEnum("ACTION");

    public static final ReservedWordsEnum ADD = new ReservedWordsEnum("ADD");

    public static final ReservedWordsEnum AFTER = new ReservedWordsEnum("AFTER");

    public static final ReservedWordsEnum ALL = new ReservedWordsEnum("ALL");

    public static final ReservedWordsEnum ALLOCATE = new ReservedWordsEnum("ALLOCATE");

    public static final ReservedWordsEnum ALTER = new ReservedWordsEnum("ALTER");

    public static final ReservedWordsEnum AND = new ReservedWordsEnum("AND");

    public static final ReservedWordsEnum ANY = new ReservedWordsEnum("ANY");

    public static final ReservedWordsEnum ARE = new ReservedWordsEnum("ARE");

    public static final ReservedWordsEnum ARRAY = new ReservedWordsEnum("ARRAY");

    public static final ReservedWordsEnum AS = new ReservedWordsEnum("AS");

    public static final ReservedWordsEnum ASC = new ReservedWordsEnum("ASC");

    public static final ReservedWordsEnum ASSERTION = new ReservedWordsEnum("ASSERTION");

    public static final ReservedWordsEnum AT = new ReservedWordsEnum("AT");

    public static final ReservedWordsEnum AUTHORIZATION = new ReservedWordsEnum("AUTHORIZATION");

    public static final ReservedWordsEnum BEFORE = new ReservedWordsEnum("BEFORE");

    public static final ReservedWordsEnum BEGIN = new ReservedWordsEnum("BEGIN");

    public static final ReservedWordsEnum BETWEEN = new ReservedWordsEnum("BETWEEN");

    public static final ReservedWordsEnum BINARY = new ReservedWordsEnum("BINARY");

    public static final ReservedWordsEnum BIT = new ReservedWordsEnum("BIT");

    public static final ReservedWordsEnum BLOB = new ReservedWordsEnum("BLOB");

    public static final ReservedWordsEnum BOOLEAN = new ReservedWordsEnum("BOOLEAN");

    public static final ReservedWordsEnum BOTH = new ReservedWordsEnum("BOTH");

    public static final ReservedWordsEnum BREADTH = new ReservedWordsEnum("BREADTH");

    public static final ReservedWordsEnum BY = new ReservedWordsEnum("BY");

    public static final ReservedWordsEnum CALL = new ReservedWordsEnum("CALL");

    public static final ReservedWordsEnum CASCADE = new ReservedWordsEnum("CASCADE");

    public static final ReservedWordsEnum CASCADED = new ReservedWordsEnum("CASCADED");

    public static final ReservedWordsEnum CASE = new ReservedWordsEnum("CASE");

    public static final ReservedWordsEnum CAST = new ReservedWordsEnum("CAST");

    public static final ReservedWordsEnum CATALOG = new ReservedWordsEnum("CATALOG");

    public static final ReservedWordsEnum CHAR = new ReservedWordsEnum("CHAR");

    public static final ReservedWordsEnum CHARACTER = new ReservedWordsEnum("CHARACTER");

    public static final ReservedWordsEnum CHECK = new ReservedWordsEnum("CHECK");

    public static final ReservedWordsEnum CLOB = new ReservedWordsEnum("CLOB");

    public static final ReservedWordsEnum CLOSE = new ReservedWordsEnum("CLOSE");

    public static final ReservedWordsEnum COLLATE = new ReservedWordsEnum("COLLATE");

    public static final ReservedWordsEnum COLLATION = new ReservedWordsEnum("COLLATION");

    public static final ReservedWordsEnum COLUMN = new ReservedWordsEnum("COLUMN");

    public static final ReservedWordsEnum COMMIT = new ReservedWordsEnum("COMMIT");

    public static final ReservedWordsEnum CONDITION = new ReservedWordsEnum("CONDITION");

    public static final ReservedWordsEnum CONNECT = new ReservedWordsEnum("CONNECT");

    public static final ReservedWordsEnum CONNECTION = new ReservedWordsEnum("CONNECTION");

    public static final ReservedWordsEnum CONSTRAINT = new ReservedWordsEnum("CONSTRAINT");

    public static final ReservedWordsEnum CONSTRAINTS = new ReservedWordsEnum("CONSTRAINTS");

    public static final ReservedWordsEnum CONSTRUCTOR = new ReservedWordsEnum("CONSTRUCTOR");

    public static final ReservedWordsEnum CONTINUE = new ReservedWordsEnum("CONTINUE");

    public static final ReservedWordsEnum CORRESPONDING = new ReservedWordsEnum("CORRESPONDING");

    public static final ReservedWordsEnum CREATE = new ReservedWordsEnum("CREATE");

    public static final ReservedWordsEnum CROSS = new ReservedWordsEnum("CROSS");

    public static final ReservedWordsEnum CUBE = new ReservedWordsEnum("CUBE");

    public static final ReservedWordsEnum CURRENT = new ReservedWordsEnum("CURRENT");

    public static final ReservedWordsEnum CURRENT_DATE = new ReservedWordsEnum("CURRENT_DATE");

    public static final ReservedWordsEnum CURRENT_DEFAULT_TRANSFORM_GROUP = new ReservedWordsEnum("CURRENT_DEFAULT_TRANSFORM_GROUP");

    public static final ReservedWordsEnum CURRENT_TRANSFORM_GROUP_FOR_TYPE = new ReservedWordsEnum("CURRENT_TRANSFORM_GROUP_FOR_TYPE");

    public static final ReservedWordsEnum CURRENT_PATH = new ReservedWordsEnum("CURRENT_PATH");

    public static final ReservedWordsEnum CURRENT_ROLE = new ReservedWordsEnum("CURRENT_ROLE");

    public static final ReservedWordsEnum CURRENT_TIME = new ReservedWordsEnum("CURRENT_TIME");

    public static final ReservedWordsEnum CURRENT_TIMESTAMP = new ReservedWordsEnum("CURRENT_TIMESTAMP");

    public static final ReservedWordsEnum CURRENT_USER = new ReservedWordsEnum("CURRENT_USER");

    public static final ReservedWordsEnum CURSOR = new ReservedWordsEnum("CURSOR");

    public static final ReservedWordsEnum CYCLE = new ReservedWordsEnum("CYCLE");

    public static final ReservedWordsEnum DATA = new ReservedWordsEnum("DATA");

    public static final ReservedWordsEnum DATE = new ReservedWordsEnum("DATE");

    public static final ReservedWordsEnum DAY = new ReservedWordsEnum("DAY");

    public static final ReservedWordsEnum DEALLOCATE = new ReservedWordsEnum("DEALLOCATE");

    public static final ReservedWordsEnum DEC = new ReservedWordsEnum("DEC");

    public static final ReservedWordsEnum DECIMAL = new ReservedWordsEnum("DECIMAL");

    public static final ReservedWordsEnum DECLARE = new ReservedWordsEnum("DECLARE");

    public static final ReservedWordsEnum DEFAULT = new ReservedWordsEnum("DEFAULT");

    public static final ReservedWordsEnum DEFERRABLE = new ReservedWordsEnum("DEFERRABLE");

    public static final ReservedWordsEnum DEFERRED = new ReservedWordsEnum("DEFERRED");

    public static final ReservedWordsEnum DELETE = new ReservedWordsEnum("DELETE");

    public static final ReservedWordsEnum DEPTH = new ReservedWordsEnum("DEPTH");

    public static final ReservedWordsEnum DEREF = new ReservedWordsEnum("DEREF");

    public static final ReservedWordsEnum DESC = new ReservedWordsEnum("DESC");

    public static final ReservedWordsEnum DESCRIBE = new ReservedWordsEnum("DESCRIBE");

    public static final ReservedWordsEnum DESCRIPTOR = new ReservedWordsEnum("DESCRIPTOR");

    public static final ReservedWordsEnum DETERMINISTIC = new ReservedWordsEnum("DETERMINISTIC");

    public static final ReservedWordsEnum DIAGNOSTICS = new ReservedWordsEnum("DIAGNOSTICS");

    public static final ReservedWordsEnum DISCONNECT = new ReservedWordsEnum("DISCONNECT");

    public static final ReservedWordsEnum DISTINCT = new ReservedWordsEnum("DISTINCT");

    public static final ReservedWordsEnum DO = new ReservedWordsEnum("DO");

    public static final ReservedWordsEnum DOMAIN = new ReservedWordsEnum("DOMAIN");

    public static final ReservedWordsEnum DOUBLE = new ReservedWordsEnum("DOUBLE");

    public static final ReservedWordsEnum DROP = new ReservedWordsEnum("DROP");

    public static final ReservedWordsEnum DYNAMIC = new ReservedWordsEnum("DYNAMIC");

    public static final ReservedWordsEnum EACH = new ReservedWordsEnum("EACH");

    public static final ReservedWordsEnum ELSE = new ReservedWordsEnum("ELSE");

    public static final ReservedWordsEnum ELSEIF = new ReservedWordsEnum("ELSEIF");

    public static final ReservedWordsEnum END = new ReservedWordsEnum("END");

    public static final ReservedWordsEnum ENDEXEC = new ReservedWordsEnum("END");

    public static final ReservedWordsEnum EQUALS = new ReservedWordsEnum("EQUALS");

    public static final ReservedWordsEnum ESCAPE = new ReservedWordsEnum("ESCAPE");

    public static final ReservedWordsEnum EXCEPT = new ReservedWordsEnum("EXCEPT");

    public static final ReservedWordsEnum EXCEPTION = new ReservedWordsEnum("EXCEPTION");

    public static final ReservedWordsEnum EXEC = new ReservedWordsEnum("EXEC");

    public static final ReservedWordsEnum EXECUTE = new ReservedWordsEnum("EXECUTE");

    public static final ReservedWordsEnum EXISTS = new ReservedWordsEnum("EXISTS");

    public static final ReservedWordsEnum EXIT = new ReservedWordsEnum("EXIT");

    public static final ReservedWordsEnum EXTERNAL = new ReservedWordsEnum("EXTERNAL");

    public static final ReservedWordsEnum FALSE = new ReservedWordsEnum("FALSE");

    public static final ReservedWordsEnum FETCH = new ReservedWordsEnum("FETCH");

    public static final ReservedWordsEnum FIRST = new ReservedWordsEnum("FIRST");

    public static final ReservedWordsEnum FLOAT = new ReservedWordsEnum("FLOAT");

    public static final ReservedWordsEnum FOR = new ReservedWordsEnum("FOR");

    public static final ReservedWordsEnum FOREIGN = new ReservedWordsEnum("FOREIGN");

    public static final ReservedWordsEnum FOUND = new ReservedWordsEnum("FOUND");

    public static final ReservedWordsEnum FROM = new ReservedWordsEnum("FROM");

    public static final ReservedWordsEnum FREE = new ReservedWordsEnum("FREE");

    public static final ReservedWordsEnum FULL = new ReservedWordsEnum("FULL");

    public static final ReservedWordsEnum FUNCTION = new ReservedWordsEnum("FUNCTION");

    public static final ReservedWordsEnum GENERAL = new ReservedWordsEnum("GENERAL");

    public static final ReservedWordsEnum GET = new ReservedWordsEnum("GET");

    public static final ReservedWordsEnum GLOBAL = new ReservedWordsEnum("GLOBAL");

    public static final ReservedWordsEnum GO = new ReservedWordsEnum("GO");

    public static final ReservedWordsEnum GOTO = new ReservedWordsEnum("GOTO");

    public static final ReservedWordsEnum GRANT = new ReservedWordsEnum("GRANT");

    public static final ReservedWordsEnum GROUP = new ReservedWordsEnum("GROUP");

    public static final ReservedWordsEnum GROUPING = new ReservedWordsEnum("GROUPING");

    public static final ReservedWordsEnum HANDLE = new ReservedWordsEnum("HANDLE");

    public static final ReservedWordsEnum HAVING = new ReservedWordsEnum("HAVING");

    public static final ReservedWordsEnum HOLD = new ReservedWordsEnum("HOLD");

    public static final ReservedWordsEnum HOUR = new ReservedWordsEnum("HOUR");

    public static final ReservedWordsEnum IDENTITY = new ReservedWordsEnum("IDENTITY");

    public static final ReservedWordsEnum IF = new ReservedWordsEnum("IF");

    public static final ReservedWordsEnum IMMEDIATE = new ReservedWordsEnum("IMMEDIATE");

    public static final ReservedWordsEnum IN = new ReservedWordsEnum("IN");

    public static final ReservedWordsEnum INDICATOR = new ReservedWordsEnum("INDICATOR");

    public static final ReservedWordsEnum INITIALLY = new ReservedWordsEnum("INITIALLY");

    public static final ReservedWordsEnum INNER = new ReservedWordsEnum("INNER");

    public static final ReservedWordsEnum INOUT = new ReservedWordsEnum("INOUT");

    public static final ReservedWordsEnum INPUT = new ReservedWordsEnum("INPUT");

    public static final ReservedWordsEnum INSERT = new ReservedWordsEnum("INSERT");

    public static final ReservedWordsEnum INT = new ReservedWordsEnum("INT");

    public static final ReservedWordsEnum INTEGER = new ReservedWordsEnum("INTEGER");

    public static final ReservedWordsEnum INTERSECT = new ReservedWordsEnum("INTERSECT");

    public static final ReservedWordsEnum INTERVAL = new ReservedWordsEnum("INTERVAL");

    public static final ReservedWordsEnum INTO = new ReservedWordsEnum("INTO");

    public static final ReservedWordsEnum IS = new ReservedWordsEnum("IS");

    public static final ReservedWordsEnum ISOLATION = new ReservedWordsEnum("ISOLATION");

    public static final ReservedWordsEnum JOIN = new ReservedWordsEnum("JOIN");

    public static final ReservedWordsEnum KEY = new ReservedWordsEnum("KEY");

    public static final ReservedWordsEnum LANGUAGE = new ReservedWordsEnum("LANGUAGE");

    public static final ReservedWordsEnum LARGE = new ReservedWordsEnum("LARGE");

    public static final ReservedWordsEnum LAST = new ReservedWordsEnum("LAST");

    public static final ReservedWordsEnum LATERAL = new ReservedWordsEnum("LATERAL");

    public static final ReservedWordsEnum LEADING = new ReservedWordsEnum("LEADING");

    public static final ReservedWordsEnum LEAVE = new ReservedWordsEnum("LEAVE");

    public static final ReservedWordsEnum LEFT = new ReservedWordsEnum("LEFT");

    public static final ReservedWordsEnum LEVEL = new ReservedWordsEnum("LEVEL");

    public static final ReservedWordsEnum LIKE = new ReservedWordsEnum("LIKE");

    public static final ReservedWordsEnum LOCAL = new ReservedWordsEnum("LOCAL");

    public static final ReservedWordsEnum LOCALTIME = new ReservedWordsEnum("LOCALTIME");

    public static final ReservedWordsEnum LOCALTIMESTAMP = new ReservedWordsEnum("LOCALTIMESTAMP");

    public static final ReservedWordsEnum LOCATOR = new ReservedWordsEnum("LOCATOR");

    public static final ReservedWordsEnum LOOP = new ReservedWordsEnum("LOOP");

    public static final ReservedWordsEnum MAP = new ReservedWordsEnum("MAP");

    public static final ReservedWordsEnum MATCH = new ReservedWordsEnum("MATCH");

    public static final ReservedWordsEnum METHOD = new ReservedWordsEnum("METHOD");

    public static final ReservedWordsEnum MINUTE = new ReservedWordsEnum("MINUTE");

    public static final ReservedWordsEnum MODIFIES = new ReservedWordsEnum("MODIFIES");

    public static final ReservedWordsEnum MODULE = new ReservedWordsEnum("MODULE");

    public static final ReservedWordsEnum MONTH = new ReservedWordsEnum("MONTH");

    public static final ReservedWordsEnum NAMES = new ReservedWordsEnum("NAMES");

    public static final ReservedWordsEnum NATIONAL = new ReservedWordsEnum("NATIONAL");

    public static final ReservedWordsEnum NATURAL = new ReservedWordsEnum("NATURAL");

    public static final ReservedWordsEnum NCHAR = new ReservedWordsEnum("NCHAR");

    public static final ReservedWordsEnum NCLOB = new ReservedWordsEnum("NCLOB");

    public static final ReservedWordsEnum NESTING = new ReservedWordsEnum("NESTING");

    public static final ReservedWordsEnum NEW = new ReservedWordsEnum("NEW");

    public static final ReservedWordsEnum NEXT = new ReservedWordsEnum("NEXT");

    public static final ReservedWordsEnum NO = new ReservedWordsEnum("NO");

    public static final ReservedWordsEnum NONE = new ReservedWordsEnum("NONE");

    public static final ReservedWordsEnum NOT = new ReservedWordsEnum("NOT");

    public static final ReservedWordsEnum NULL = new ReservedWordsEnum("NULL");

    public static final ReservedWordsEnum NUMERIC = new ReservedWordsEnum("NUMERIC");

    public static final ReservedWordsEnum OBJECT = new ReservedWordsEnum("OBJECT");

    public static final ReservedWordsEnum OF = new ReservedWordsEnum("OF");

    public static final ReservedWordsEnum OLD = new ReservedWordsEnum("OLD");

    public static final ReservedWordsEnum ON = new ReservedWordsEnum("ON");

    public static final ReservedWordsEnum ONLY = new ReservedWordsEnum("ONLY");

    public static final ReservedWordsEnum OPEN = new ReservedWordsEnum("OPEN");

    public static final ReservedWordsEnum OPTION = new ReservedWordsEnum("OPTION");

    public static final ReservedWordsEnum OR = new ReservedWordsEnum("OR");

    public static final ReservedWordsEnum ORDER = new ReservedWordsEnum("ORDER");

    public static final ReservedWordsEnum ORDINALITY = new ReservedWordsEnum("ORDINALITY");

    public static final ReservedWordsEnum OUT = new ReservedWordsEnum("OUT");

    public static final ReservedWordsEnum OUTER = new ReservedWordsEnum("OUTER");

    public static final ReservedWordsEnum OUTPUT = new ReservedWordsEnum("OUTPUT");

    public static final ReservedWordsEnum OVERLAPS = new ReservedWordsEnum("OVERLAPS");

    public static final ReservedWordsEnum PAD = new ReservedWordsEnum("PAD");

    public static final ReservedWordsEnum PARAMETER = new ReservedWordsEnum("PARAMETER");

    public static final ReservedWordsEnum PARTIAL = new ReservedWordsEnum("PARTIAL");

    public static final ReservedWordsEnum PATH = new ReservedWordsEnum("PATH");

    public static final ReservedWordsEnum PRECISION = new ReservedWordsEnum("PRECISION");

    public static final ReservedWordsEnum PREPARE = new ReservedWordsEnum("PREPARE");

    public static final ReservedWordsEnum PRESERVE = new ReservedWordsEnum("PRESERVE");

    public static final ReservedWordsEnum PRIMARY = new ReservedWordsEnum("PRIMARY");

    public static final ReservedWordsEnum PRIOR = new ReservedWordsEnum("PRIOR");

    public static final ReservedWordsEnum PRIVILEGES = new ReservedWordsEnum("PRIVILEGES");

    public static final ReservedWordsEnum PROCEDURE = new ReservedWordsEnum("PROCEDURE");

    public static final ReservedWordsEnum PUBLIC = new ReservedWordsEnum("PUBLIC");

    public static final ReservedWordsEnum READ = new ReservedWordsEnum("READ");

    public static final ReservedWordsEnum READS = new ReservedWordsEnum("READS");

    public static final ReservedWordsEnum REAL = new ReservedWordsEnum("REAL");

    public static final ReservedWordsEnum RECURSIVE = new ReservedWordsEnum("RECURSIVE");

    public static final ReservedWordsEnum REDO = new ReservedWordsEnum("REDO");

    public static final ReservedWordsEnum REF = new ReservedWordsEnum("REF");

    public static final ReservedWordsEnum REFERENCES = new ReservedWordsEnum("REFERENCES");

    public static final ReservedWordsEnum REFERENCING = new ReservedWordsEnum("REFERENCING");

    public static final ReservedWordsEnum RELATIVE = new ReservedWordsEnum("RELATIVE");

    public static final ReservedWordsEnum RELEASE = new ReservedWordsEnum("RELEASE");

    public static final ReservedWordsEnum REPEAT = new ReservedWordsEnum("REPEAT");

    public static final ReservedWordsEnum RESIGNAL = new ReservedWordsEnum("RESIGNAL");

    public static final ReservedWordsEnum RESTRICT = new ReservedWordsEnum("RESTRICT");

    public static final ReservedWordsEnum RESULT = new ReservedWordsEnum("RESULT");

    public static final ReservedWordsEnum RETURN = new ReservedWordsEnum("RETURN");

    public static final ReservedWordsEnum RETURNS = new ReservedWordsEnum("RETURNS");

    public static final ReservedWordsEnum REVOKE = new ReservedWordsEnum("REVOKE");

    public static final ReservedWordsEnum RIGHT = new ReservedWordsEnum("RIGHT");

    public static final ReservedWordsEnum ROLE = new ReservedWordsEnum("ROLE");

    public static final ReservedWordsEnum ROLLBACK = new ReservedWordsEnum("ROLLBACK");

    public static final ReservedWordsEnum ROLLUP = new ReservedWordsEnum("ROLLUP");

    public static final ReservedWordsEnum ROUTINE = new ReservedWordsEnum("ROUTINE");

    public static final ReservedWordsEnum ROW = new ReservedWordsEnum("ROW");

    public static final ReservedWordsEnum ROWS = new ReservedWordsEnum("ROWS");

    public static final ReservedWordsEnum SAVEPOINT = new ReservedWordsEnum("SAVEPOINT");

    public static final ReservedWordsEnum SCHEMA = new ReservedWordsEnum("SCHEMA");

    public static final ReservedWordsEnum SCROLL = new ReservedWordsEnum("SCROLL");

    public static final ReservedWordsEnum SEARCH = new ReservedWordsEnum("SEARCH");

    public static final ReservedWordsEnum SECOND = new ReservedWordsEnum("SECOND");

    public static final ReservedWordsEnum SECTION = new ReservedWordsEnum("SECTION");

    public static final ReservedWordsEnum SELECT = new ReservedWordsEnum("SELECT");

    public static final ReservedWordsEnum SESSION = new ReservedWordsEnum("SESSION");

    public static final ReservedWordsEnum SESSION_USER = new ReservedWordsEnum("SESSION_USER");

    public static final ReservedWordsEnum SET = new ReservedWordsEnum("SET");

    public static final ReservedWordsEnum SETS = new ReservedWordsEnum("SETS");

    public static final ReservedWordsEnum SIGNAL = new ReservedWordsEnum("SIGNAL");

    public static final ReservedWordsEnum SIMILAR = new ReservedWordsEnum("SIMILAR");

    public static final ReservedWordsEnum SIZE = new ReservedWordsEnum("SIZE");

    public static final ReservedWordsEnum SMALLINT = new ReservedWordsEnum("SMALLINT");

    public static final ReservedWordsEnum SOME = new ReservedWordsEnum("SOME");

    public static final ReservedWordsEnum SPACE = new ReservedWordsEnum("SPACE");

    public static final ReservedWordsEnum SPECIFIC = new ReservedWordsEnum("SPECIFIC");

    public static final ReservedWordsEnum SPECIFICTYPE = new ReservedWordsEnum("SPECIFICTYPE");

    public static final ReservedWordsEnum SQL = new ReservedWordsEnum("SQL");

    public static final ReservedWordsEnum SQLEXCEPTION = new ReservedWordsEnum("SQLEXCEPTION");

    public static final ReservedWordsEnum SQLSTATE = new ReservedWordsEnum("SQLSTATE");

    public static final ReservedWordsEnum SQLWARNING = new ReservedWordsEnum("SQLWARNING");

    public static final ReservedWordsEnum START = new ReservedWordsEnum("START");

    public static final ReservedWordsEnum STATE = new ReservedWordsEnum("STATE");

    public static final ReservedWordsEnum STATIC = new ReservedWordsEnum("STATIC");

    public static final ReservedWordsEnum SYSTEM_USER = new ReservedWordsEnum("SYSTEM_USER");

    public static final ReservedWordsEnum TABLE = new ReservedWordsEnum("TABLE");

    public static final ReservedWordsEnum TEMPORARY = new ReservedWordsEnum("TEMPORARY");

    public static final ReservedWordsEnum THEN = new ReservedWordsEnum("THEN");

    public static final ReservedWordsEnum TIME = new ReservedWordsEnum("TIME");

    public static final ReservedWordsEnum TIMESTAMP = new ReservedWordsEnum("TIMESTAMP");

    public static final ReservedWordsEnum TIMEZONE_HOUR = new ReservedWordsEnum("TIMEZONE_HOUR");

    public static final ReservedWordsEnum TIMEZONE_MINUTE = new ReservedWordsEnum("TIMEZONE_MINUTE");

    public static final ReservedWordsEnum TO = new ReservedWordsEnum("TO");

    public static final ReservedWordsEnum TRAILING = new ReservedWordsEnum("TRAILING");

    public static final ReservedWordsEnum TRANSACTION = new ReservedWordsEnum("TRANSACTION");

    public static final ReservedWordsEnum TRANSLATION = new ReservedWordsEnum("TRANSLATION");

    public static final ReservedWordsEnum TREAT = new ReservedWordsEnum("TREAT");

    public static final ReservedWordsEnum TRIGGER = new ReservedWordsEnum("TRIGGER");

    public static final ReservedWordsEnum TRUE = new ReservedWordsEnum("TRUE");

    public static final ReservedWordsEnum UNDER = new ReservedWordsEnum("UNDER");

    public static final ReservedWordsEnum UNDO = new ReservedWordsEnum("UNDO");

    public static final ReservedWordsEnum UNION = new ReservedWordsEnum("UNION");

    public static final ReservedWordsEnum UNIQUE = new ReservedWordsEnum("UNIQUE");

    public static final ReservedWordsEnum UNKNOWN = new ReservedWordsEnum("UNKNOWN");

    public static final ReservedWordsEnum UNNEST = new ReservedWordsEnum("UNNEST");

    public static final ReservedWordsEnum UNTIL = new ReservedWordsEnum("UNTIL");

    public static final ReservedWordsEnum UPDATE = new ReservedWordsEnum("UPDATE");

    public static final ReservedWordsEnum USAGE = new ReservedWordsEnum("USAGE");

    public static final ReservedWordsEnum USER = new ReservedWordsEnum("USER");

    public static final ReservedWordsEnum USING = new ReservedWordsEnum("USING");

    public static final ReservedWordsEnum VALUE = new ReservedWordsEnum("VALUE");

    public static final ReservedWordsEnum VALUES = new ReservedWordsEnum("VALUES");

    public static final ReservedWordsEnum VARCHAR = new ReservedWordsEnum("VARCHAR");

    public static final ReservedWordsEnum VARYING = new ReservedWordsEnum("VARYING");

    public static final ReservedWordsEnum VIEW = new ReservedWordsEnum("VIEW");

    public static final ReservedWordsEnum WHEN = new ReservedWordsEnum("WHEN");

    public static final ReservedWordsEnum WHENEVER = new ReservedWordsEnum("WHENEVER");

    public static final ReservedWordsEnum WHERE = new ReservedWordsEnum("WHERE");

    public static final ReservedWordsEnum WHILE = new ReservedWordsEnum("WHILE");

    public static final ReservedWordsEnum WITH = new ReservedWordsEnum("WITH");

    public static final ReservedWordsEnum WITHOUT = new ReservedWordsEnum("WITHOUT");

    public static final ReservedWordsEnum WORK = new ReservedWordsEnum("WORK");

    public static final ReservedWordsEnum WRITE = new ReservedWordsEnum("WRITE");

    public static final ReservedWordsEnum YEAR = new ReservedWordsEnum("YEAR");

    public static final ReservedWordsEnum ZONE = new ReservedWordsEnum("ZONE");

    private static final long serialVersionUID = -5875915178110384661L;

    private final transient String word;

    private static final ReservedWordsEnum[] fValues = { ABSOLUTE, ACTION, ADD, AFTER, ALL, ALLOCATE, ALTER, AND, ANY, ARE, ARRAY, AS, ASC, ASSERTION, AT, AUTHORIZATION, BEFORE, BEGIN, BETWEEN, BINARY, BIT, BLOB, BOOLEAN, BOTH, BREADTH, BY, CALL, CASCADE, CASCADED, CASE, CAST, CATALOG, CHAR, CHARACTER, CHECK, CLOB, CLOSE, COLLATE, COLLATION, COLUMN, COMMIT, CONDITION, CONNECT, CONNECTION, CONSTRAINT, CONSTRAINTS, CONSTRUCTOR, CONTINUE, CORRESPONDING, CREATE, CROSS, CUBE, CURRENT, CURRENT_DATE, CURRENT_DEFAULT_TRANSFORM_GROUP, CURRENT_TRANSFORM_GROUP_FOR_TYPE, CURRENT_PATH, CURRENT_ROLE, CURRENT_TIME, CURRENT_TIMESTAMP, CURRENT_USER, CURSOR, CYCLE, DATA, DATE, DAY, DEALLOCATE, DEC, DECIMAL, DECLARE, DEFAULT, DEFERRABLE, DEFERRED, DELETE, DEPTH, DEREF, DESC, DESCRIBE, DESCRIPTOR, DETERMINISTIC, DIAGNOSTICS, DISCONNECT, DISTINCT, DO, DOMAIN, DOUBLE, DROP, DYNAMIC, EACH, ELSE, ELSEIF, END, ENDEXEC, EQUALS, ESCAPE, EXCEPT, EXCEPTION, EXEC, EXECUTE, EXISTS, EXIT, EXTERNAL, FALSE, FETCH, FIRST, FLOAT, FOR, FOREIGN, FOUND, FROM, FREE, FULL, FUNCTION, GENERAL, GET, GLOBAL, GO, GOTO, GRANT, GROUP, GROUPING, HANDLE, HAVING, HOLD, HOUR, IDENTITY, IF, IMMEDIATE, IN, INDICATOR, INITIALLY, INNER, INOUT, INPUT, INSERT, INT, INTEGER, INTERSECT, INTERVAL, INTO, IS, ISOLATION, JOIN, KEY, LANGUAGE, LARGE, LAST, LATERAL, LEADING, LEAVE, LEFT, LEVEL, LIKE, LOCAL, LOCALTIME, LOCALTIMESTAMP, LOCATOR, LOOP, MAP, MATCH, METHOD, MINUTE, MODIFIES, MODULE, MONTH, NAMES, NATIONAL, NATURAL, NCHAR, NCLOB, NESTING, NEW, NEXT, NO, NONE, NOT, NULL, NUMERIC, OBJECT, OF, OLD, ON, ONLY, OPEN, OPTION, OR, ORDER, ORDINALITY, OUT, OUTER, OUTPUT, OVERLAPS, PAD, PARAMETER, PARTIAL, PATH, PRECISION, PREPARE, PRESERVE, PRIMARY, PRIOR, PRIVILEGES, PROCEDURE, PUBLIC, READ, READS, REAL, RECURSIVE, REDO, REF, REFERENCES, REFERENCING, RELATIVE, RELEASE, REPEAT, RESIGNAL, RESTRICT, RESULT, RETURN, RETURNS, REVOKE, RIGHT, ROLE, ROLLBACK, ROLLUP, ROUTINE, ROW, ROWS, SAVEPOINT, SCHEMA, SCROLL, SEARCH, SECOND, SECTION, SELECT, SESSION, SESSION_USER, SET, SETS, SIGNAL, SIMILAR, SIZE, SMALLINT, SOME, SPACE, SPECIFIC, SPECIFICTYPE, SQL, SQLEXCEPTION, SQLSTATE, SQLWARNING, START, STATE, STATIC, SYSTEM_USER, TABLE, TEMPORARY, THEN, TIME, TIMESTAMP, TIMEZONE_HOUR, TIMEZONE_MINUTE, TO, TRAILING, TRANSACTION, TRANSLATION, TREAT, TRIGGER, TRUE, UNDER, UNDO, UNION, UNIQUE, UNKNOWN, UNNEST, UNTIL, UPDATE, USAGE, USER, USING, VALUE, VALUES, VARCHAR, VARYING, VIEW, WHEN, WHENEVER, WHERE, WHILE, WITH, WITHOUT, WORK, WRITE, YEAR, ZONE };

    public static final List LIST = Collections.unmodifiableList(Arrays.asList(fValues));

    private static int fNextOrdinal = 1;

    private final int fOrdinal = fNextOrdinal++;

    private ReservedWordsEnum(String character) {
        this.word = character;
    }

    public static ReservedWordsEnum valueOf(String character) {
        Iterator iter = LIST.iterator();
        while (iter.hasNext()) {
            ReservedWordsEnum ch = (ReservedWordsEnum) iter.next();
            if (character.equals(ch.toString())) {
                return ch;
            }
        }
        throw new IllegalArgumentException("Cannot be parsed into an enum element : '" + character + "'");
    }

    public String toString() {
        return word + CharactersEnum.SPACE.toString();
    }

    /**
	 * @return
	 * @throws ObjectStreamException
	 * @since
	 */
    private Object readResolve() throws ObjectStreamException {
        return fValues[fOrdinal];
    }

    /**
	 * @param aObject
	 * @return
	 * @since
	 */
    public int compareTo(Object aObject) {
        return fOrdinal - ((ReservedWordsEnum) aObject).fOrdinal;
    }

    public String append(ReservedWordsEnum res) {
        return this.word + CharactersEnum.SPACE.toString() + res.word;
    }

    public String append(String res) {
        return this.word + CharactersEnum.SPACE.toString() + res + CharactersEnum.SPACE.toString();
    }

    public String addSpace() {
        return this.word + CharactersEnum.SPACE.toString();
    }
}
