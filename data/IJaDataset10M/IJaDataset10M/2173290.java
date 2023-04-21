package abstrasy;

import abstrasy.pcfx.*;
import java.util.*;

/**
 * Abstrasy Interpreter
 *
 * Copyright : Copyright (c) 2006-2012, Luc Bruninx.
 *
 * Concédée sous licence EUPL, version 1.1 uniquement (la «Licence»).
 *
 * Vous ne pouvez utiliser la présente oeuvre que conformément à la Licence.
 * Vous pouvez obtenir une copie de la Licence à l’adresse suivante:
 *
 *   http://www.osor.eu/eupl
 *
 * Sauf obligation légale ou contractuelle écrite, le logiciel distribué sous
 * la Licence est distribué "en l’état", SANS GARANTIES OU CONDITIONS QUELLES
 * QU’ELLES SOIENT, expresses ou implicites.
 *
 * Consultez la Licence pour les autorisations et les restrictions
 * linguistiques spécifiques relevant de la Licence.
 *
 *
 * @author Luc Bruninx
 * @version 1.0
 */
public class PCoder {

    /**
     * Liste des Program Code
     */
    public static int PC_ERR = 0;

    private static int PC_UID = PC_ERR + 1;

    public static final int PC_QUOTE = PC_UID++;

    public static final int PC_UNQUOTE = PC_UID++;

    public static final int PC_IMMEDIATE_QUOTE = PC_UID++;

    public static final int PC_IMMEDIATE_UNQUOTE = PC_UID++;

    public static final int PC_isQUOTED = PC_UID++;

    public static final int PC_QUOTE_ENC = PC_UID++;

    public static final int PC_QUOTE_DEC = PC_UID++;

    public static final int PC_isQUOTE_ENCODED = PC_UID++;

    public static final int PC_OPERATOR = PC_UID++;

    public static final int PC_isOPERATOR = PC_UID++;

    public static final int PC_SYMBOL = PC_UID++;

    public static final int PC_isSYMBOL = PC_UID++;

    public static final int PC_EXPR = PC_UID++;

    public static final int PC_isEXPR = PC_UID++;

    public static final int PC_FUNCTION = PC_UID++;

    public static final int PC_THIS = PC_UID++;

    public static final int PC_isFUNCTION = PC_UID++;

    public static final int PC_MEMOIZE = PC_UID++;

    public static final int PC_isMEMOIZE = PC_UID++;

    public static final int PC_isMEMOIZABLE = PC_UID++;

    public static final int PC_MEMOIZE_DUMP = PC_UID++;

    public static final int PC_LAZY = PC_UID++;

    public static final int PC_isLAZY = PC_UID++;

    public static final int PC_SERIALIZE = PC_UID++;

    public static final int PC_COMPILE = PC_UID++;

    public static final int PC_META_PROCESS = PC_UID++;

    public static final int PC_SUBSTITUTE = PC_UID++;

    public static final int PC_UNIFY = PC_UID++;

    public static final int PC_isMATCH = PC_UID++;

    public static final int PC_EVAL = PC_UID++;

    public static final int PC_EVALsucre = PC_UID++;

    public static final int PC_ATOMIC_TRUE_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FALSE_STATIC = PC_UID++;

    public static final int PC_ATOMIC_SETN_STATIC = PC_UID++;

    public static final int PC_ADD = PC_UID++;

    public static final int PC_SUB = PC_UID++;

    public static final int PC_MUL = PC_UID++;

    public static final int PC_DIV = PC_UID++;

    public static final int PC_MOD = PC_UID++;

    public static final int PC_ATOMIC_ADD_STATIC = PC_UID++;

    public static final int PC_ATOMIC_SUB_STATIC = PC_UID++;

    public static final int PC_ATOMIC_MUL_STATIC = PC_UID++;

    public static final int PC_ATOMIC_DIV_STATIC = PC_UID++;

    public static final int PC_SIN = PC_UID++;

    public static final int PC_COS = PC_UID++;

    public static final int PC_TAN = PC_UID++;

    public static final int PC_ROUND = PC_UID++;

    public static final int PC_POW = PC_UID++;

    public static final int PC_SQRT = PC_UID++;

    public static final int PC_ABS = PC_UID++;

    public static final int PC_LOG = PC_UID++;

    public static final int PC_LOG10 = PC_UID++;

    public static final int PC_DIGITS10 = PC_UID++;

    public static final int PC_EXP = PC_UID++;

    public static final int PC_ASIN = PC_UID++;

    public static final int PC_ACOS = PC_UID++;

    public static final int PC_ATAN = PC_UID++;

    public static final int PC_TODEGREES = PC_UID++;

    public static final int PC_TORADIANS = PC_UID++;

    public static final int PC_RANDOM = PC_UID++;

    public static final int PC_FLOOR = PC_UID++;

    public static final int PC_CEILING = PC_UID++;

    public static final int PC_TRUNCATE = PC_UID++;

    public static final int PC_SGN = PC_UID++;

    public static final int PC_FRACTIONAL = PC_UID++;

    public static final int PC_EQUALS = PC_UID++;

    public static final int PC_EQUIDENTITY = PC_UID++;

    public static final int PC_GT = PC_UID++;

    public static final int PC_LT = PC_UID++;

    public static final int PC_NEQUALS = PC_UID++;

    public static final int PC_GTE = PC_UID++;

    public static final int PC_LTE = PC_UID++;

    public static final int PC_isINTEGER = PC_UID++;

    public static final int PC_isREAL = PC_UID++;

    public static final int PC_isZERO = PC_UID++;

    public static final int PC_isPOSITIVE = PC_UID++;

    public static final int PC_isNEGATIVE = PC_UID++;

    public static final int PC_isODD = PC_UID++;

    public static final int PC_isEVEN = PC_UID++;

    public static final int PC_isNOTHING = PC_UID++;

    public static final int PC_isSOMETHING = PC_UID++;

    public static final int PC_NOT = PC_UID++;

    public static final int PC_ATOMIC_NOT_STATIC = PC_UID++;

    public static final int PC_COR = PC_UID++;

    public static final int PC_ATOMIC_OR_STATIC = PC_UID++;

    public static final int PC_XOR = PC_UID++;

    public static final int PC_ATOMIC_XOR_STATIC = PC_UID++;

    public static final int PC_CAND = PC_UID++;

    public static final int PC_ATOMIC_AND_STATIC = PC_UID++;

    public static final int PC_SHIFTL = PC_UID++;

    public static final int PC_ATOMIC_SHIFTL_STATIC = PC_UID++;

    public static final int PC_SHIFTR = PC_UID++;

    public static final int PC_ATOMIC_SHIFTR_STATIC = PC_UID++;

    public static final int PC_FUZZY_NOT = PC_UID++;

    public static final int PC_FUZZY_AND = PC_UID++;

    public static final int PC_FUZZY_OR = PC_UID++;

    public static final int PC_FUZZY_PROBABILITY_AND = PC_UID++;

    public static final int PC_FUZZY_PROBABILITY_OR = PC_UID++;

    public static final int PC_FUZZY_DEGREE_OF = PC_UID++;

    public static final int PC_FUZZY_MEMBER_OF = PC_UID++;

    public static final int PC_INCREASING_TO = PC_UID++;

    public static final int PC_DECREASING_TO = PC_UID++;

    public static final int PC_isVALUE = PC_UID++;

    public static final int PC_isLIST = PC_UID++;

    public static final int PC_isTUPLE = PC_UID++;

    public static final int PC_isSTRING = PC_UID++;

    public static final int PC_isNUMBER = PC_UID++;

    public static final int PC_isOBJECT = PC_UID++;

    public static final int PC_isCLASS = PC_UID++;

    public static final int PC_isEXTERNAL = PC_UID++;

    public static final int PC_isDEFINED = PC_UID++;

    public static final int PC_isUNDEFINED = PC_UID++;

    public static final int PC_isARGS = PC_UID++;

    public static final int PC_isFINAL = PC_UID++;

    public static final int PC_isHIDDEN = PC_UID++;

    public static final int PC_isPRIVATE = PC_UID++;

    public static final int PC_isIMMUTABLE = PC_UID++;

    public static final int PC_isIMMUABLE = PC_UID++;

    public static final int PC_isUSEONCE = PC_UID++;

    public static final int PC_isKEY = PC_UID++;

    public static final int PC_LOCATE_KEY = PC_UID++;

    public static final int PC_isPAIR = PC_UID++;

    public static final int PC_IF = PC_UID++;

    public static final int PC_INFER = PC_UID++;

    public static final int PC_EACH_TIME = PC_UID++;

    public static final int PC_ONCE = PC_UID++;

    public static final int PC_FOREACH_PERM = PC_UID++;

    public static final int PC_FOREACH_COMBIN = PC_UID++;

    public static final int PC_FOREACH_PART = PC_UID++;

    public static final int PC_FOREACH_CIRC = PC_UID++;

    public static final int PC_FOREACH_ROW = PC_UID++;

    public static final int PC_FOREACH_COL = PC_UID++;

    public static final int PC_FOREACH_DIAG = PC_UID++;

    public static final int PC_FOREACH_SUM = PC_UID++;

    public static final int PC_FOREACH_PRODUCT = PC_UID++;

    public static final int PC_FOREACH_ALL_DIFFERENT = PC_UID++;

    public static final int PC_SWITCH = PC_UID++;

    public static final int PC_THEN = PC_UID++;

    public static final int PC_ELSE = PC_UID++;

    public static final int PC_ELIF = PC_UID++;

    public static final int PC_COND = PC_UID++;

    public static final int PC_DO = PC_UID++;

    public static final int PC_INVOKE = PC_UID++;

    public static final int PC_WHILE = PC_UID++;

    public static final int PC_UNTIL = PC_UID++;

    public static final int PC_FOREVER = PC_UID++;

    public static final int PC_COMPLETE_LOOP = PC_UID++;

    public static final int PC_BREAK_LOOP = PC_UID++;

    public static final int PC_SKIP_LOOP = PC_UID++;

    public static final int PC_isCOMPLETE_LOOP = PC_UID++;

    public static final int PC_FOREACH = PC_UID++;

    public static final int PC_MAP = PC_UID++;

    public static final int PC_REDUCE = PC_UID++;

    public static final int PC_RETURN = PC_UID++;

    public static final int PC_IMMEDIATE_RETURN = PC_UID++;

    public static final int PC_DELEGATE = PC_UID++;

    public static final int PC_OPTIONAL = PC_UID++;

    public static final int PC_REQUIRE = PC_UID++;

    public static final int PC_CONTRACT = PC_UID++;

    public static final int PC_PRECOND = PC_UID++;

    public static final int PC_POSTCOND = PC_UID++;

    public static final int PC_SUPPLIER = PC_UID++;

    public static final int PC_ASSERT = PC_UID++;

    public static final int PC_isCONTRACTS = PC_UID++;

    public static final int PC_USE_CONTRACTS = PC_UID++;

    public static final int PC_AGREE = PC_UID++;

    public static final int PC_DEFINE = PC_UID++;

    public static final int PC_LABEL = PC_UID++;

    public static final int PC_SET = PC_UID++;

    public static final int PC_LET = PC_UID++;

    public static final int PC_NEW = PC_UID++;

    public static final int PC_SELF = PC_UID++;

    public static final int PC_FINAL = PC_UID++;

    public static final int PC_CLASS = PC_UID++;

    public static final int PC_CLASSID = PC_UID++;

    public static final int PC_SLOTS = PC_UID++;

    public static final int PC_REF = PC_UID++;

    public static final int PC_REFsucre = PC_UID++;

    public static final int PC_DEREF = PC_UID++;

    public static final int PC_DEREFsucre = PC_UID++;

    public static final int PC_SWAP = PC_UID++;

    public static final int PC_IS_A = PC_UID++;

    public static final int PC_HAS_A = PC_UID++;

    public static final int PC_MIXIN = PC_UID++;

    public static final int PC_FROM = PC_UID++;

    public static final int PC_ALL = PC_UID++;

    public static final int PC_OTHERS = PC_UID++;

    public static final int PC_ALIAS = PC_UID++;

    public static final int PC_OF = PC_UID++;

    public static final int PC_IN = PC_UID++;

    public static final int PC_TO = PC_UID++;

    public static final int PC_AT = PC_UID++;

    public static final int PC_BY = PC_UID++;

    public static final int PC_FOR = PC_UID++;

    public static final int PC_WITH = PC_UID++;

    public static final int PC_OBJECT = PC_UID++;

    public static final int PC_LIBRARY = PC_UID++;

    public static final int PC_REGISTER = PC_UID++;

    public static final int PC_isREGISTER = PC_UID++;

    public static final int PC_UNREGISTER = PC_UID++;

    public static final int PC_RESOURCE = PC_UID++;

    public static final int PC_EXTERNAL = PC_UID++;

    public static final int PC_EXTCALL = PC_UID++;

    public static final int PC_IMPORT = PC_UID++;

    public static final int PC_INCLUDE = PC_UID++;

    public static final int PC_isIS_A = PC_UID++;

    public static final int PC_isHAS_A = PC_UID++;

    public static final int PC_USING = PC_UID++;

    public static final int PC_ARGS = PC_UID++;

    public static final int PC_DEFAULT_ARGS = PC_UID++;

    public static final int PC_DEFAULT_OPTS = PC_UID++;

    public static final int PC_KEY = PC_UID++;

    public static final int PC_VALUE = PC_UID++;

    public static final int PC_SET_VALUE = PC_UID++;

    public static final int PC_ATOMIC_INCR_STATIC = PC_UID++;

    public static final int PC_ATOMIC_DECR_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_INCR_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_DECR_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_ADD_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_SUB_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_MUL_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_DIV_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_TRUE_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_FALSE_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_NOT_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_OR_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_AND_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_XOR_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_SHIFTL_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_SHIFTR_STATIC = PC_UID++;

    public static final int PC_ATOMIC_FETCH_AND_SETN_STATIC = PC_UID++;

    public static final int PC_APPEND_STATIC = PC_UID++;

    public static final int PC_POP_STATIC = PC_UID++;

    public static final int PC_DEQUEUE_STATIC = PC_UID++;

    public static final int PC_APPEND = PC_UID++;

    public static final int PC_STORE_STATIC = PC_UID++;

    public static final int PC_STORE = PC_UID++;

    public static final int PC_INSERT_STATIC = PC_UID++;

    public static final int PC_INSERT = PC_UID++;

    public static final int PC_MERGE_STATIC = PC_UID++;

    public static final int PC_MERGE = PC_UID++;

    public static final int PC_LENGTH = PC_UID++;

    public static final int PC_CLONE = PC_UID++;

    public static final int PC_CLONEsucre = PC_UID++;

    public static final int PC_STRING = PC_UID++;

    public static final int PC_NUMBER = PC_UID++;

    public static final int PC_NOTHING = PC_UID++;

    public static final int PC_HEX_STRING = PC_UID++;

    public static final int PC_BIN_STRING = PC_UID++;

    public static final int PC_OCT_STRING = PC_UID++;

    public static final int PC_RAW_STRING = PC_UID++;

    public static final int PC_isLIKE_STRING = PC_UID++;

    public static final int PC_LIST = PC_UID++;

    public static final int PC_TUPLE = PC_UID++;

    public static final int PC_PAIR = PC_UID++;

    public static final int PC_SELECT_FIRST = PC_UID++;

    public static final int PC_SELECT_REST = PC_UID++;

    public static final int PC_MAX = PC_UID++;

    public static final int PC_MIN = PC_UID++;

    public static final int PC_RANGE = PC_UID++;

    public static final int PC_COMPACT = PC_UID++;

    public static final int PC_COMPACT_STATIC = PC_UID++;

    public static final int PC_FLATTEN = PC_UID++;

    public static final int PC_FLATTEN_STATIC = PC_UID++;

    public static final int PC_REMOVE_STATIC = PC_UID++;

    public static final int PC_REMOVE = PC_UID++;

    public static final int PC_SELECT_HEAD = PC_UID++;

    public static final int PC_SELECT_TAIL = PC_UID++;

    public static final int PC_SELECT = PC_UID++;

    public static final int PC_SELECT_MIN = PC_UID++;

    public static final int PC_SELECT_MAX = PC_UID++;

    public static final int PC_REVERSE_STATIC = PC_UID++;

    public static final int PC_REVERSE = PC_UID++;

    public static final int PC_UPPER_STRING = PC_UID++;

    public static final int PC_LOWER_STRING = PC_UID++;

    public static final int PC_TRIM_STRING = PC_UID++;

    public static final int PC_FILLING_STRING = PC_UID++;

    public static final int PC_ORD = PC_UID++;

    public static final int PC_CHR = PC_UID++;

    public static final int PC_SPLIT = PC_UID++;

    public static final int PC_JOIN = PC_UID++;

    public static final int PC_FIND = PC_UID++;

    public static final int PC_COUNT = PC_UID++;

    public static final int PC_REPLACE_STATIC = PC_UID++;

    public static final int PC_REPLACE = PC_UID++;

    public static final int PC_SORT_STATIC = PC_UID++;

    public static final int PC_SORT = PC_UID++;

    public static final int PC_KEYS = PC_UID++;

    public static final int PC_VALUES = PC_UID++;

    public static final int PC_DIFF = PC_UID++;

    public static final int PC_CROSSOVER_PMX = PC_UID++;

    public static final int PC_CROSSOVER_PX = PC_UID++;

    public static final int PC_CROSSOVER_CX = PC_UID++;

    public static final int PC_CROSSOVER_OX = PC_UID++;

    public static final int PC_CROSSOVER_ERX = PC_UID++;

    public static final int PC_POPULATE_RANDOM = PC_UID++;

    public static final int PC_MUTATE_RANDOM = PC_UID++;

    public static final int PC_MUTATE = PC_UID++;

    public static final int PC_ACTOR = PC_UID++;

    public static final int PC_MUTEX = PC_UID++;

    public static final int PC_isMUTEX = PC_UID++;

    public static final int PC_LOCK = PC_UID++;

    public static final int PC_ATOMIC = PC_UID++;

    public static final int PC_isLOCKED = PC_UID++;

    public static final int PC_KILL = PC_UID++;

    public static final int PC_SUSPEND = PC_UID++;

    public static final int PC_isSUSPENDED = PC_UID++;

    public static final int PC_RESUME = PC_UID++;

    public static final int PC_ACTORS = PC_UID++;

    public static final int PC_MY_ACTOR_ID = PC_UID++;

    public static final int PC_SUPERVISOR_ID = PC_UID++;

    public static final int PC_SEND = PC_UID++;

    public static final int PC_RECEIVE = PC_UID++;

    public static final int PC_isMESSAGES = PC_UID++;

    public static final int PC_PENDING = PC_UID++;

    public static final int PC_POSTPONE = PC_UID++;

    public static final int PC_isPENDING = PC_UID++;

    public static final int PC_isPOSTPONED = PC_UID++;

    public static final int PC_isMATCH_REGEX = PC_UID++;

    public static final int PC_SPLIT_REGEX = PC_UID++;

    public static final int PC_FIND_REGEX = PC_UID++;

    public static final int PC_LIST_REGEX = PC_UID++;

    public static final int PC_REPLACE_REGEX = PC_UID++;

    public static final int PC_DISPLAY = PC_UID++;

    public static final int PC_DISPLAY_NOLINE = PC_UID++;

    public static final int PC_DISPLAY_AT = PC_UID++;

    public static final int PC_DISPLAY_CLR = PC_UID++;

    public static final int PC_RAW_INPUT = PC_UID++;

    public static final int PC_VOID = PC_UID++;

    public static final int PC_PERFORM = PC_UID++;

    public static final int PC_RESTART = PC_UID++;

    public static final int PC_TIMED = PC_UID++;

    public static final int PC_TRY = PC_UID++;

    public static final int PC_RETRY = PC_UID++;

    public static final int PC_ABORT = PC_UID++;

    public static final int PC_CATCH = PC_UID++;

    public static final int PC_RAISE = PC_UID++;

    public static final int PC_SLEEP = PC_UID++;

    public static final int PC_DUMP_HEAP = PC_UID++;

    public static final int PC_EXIT = PC_UID++;

    public static final int PC_ENSURE = PC_UID++;

    public static final int PC_TERMINATION = PC_UID++;

    public static final int PC_TESTPC = PC_UID++;

    /**
     * séparateur de symboles
     */
    public static final char SEP = ':';

    public static final String SEP_str = ":";

    /**
     * identifiant de registre interne
     */
    public static final String _Rx_ = "R.";

    public static final String _rx_ = "r.";

    /**
     * listes d'arguments
     */
    public static final String ARGV = "argv";

    /**
     * identifiant de registre interne (masqué)
     */
    public static final char REG = '®';

    public static final String REG_str = "®";

    public static final String REG_RETURN = REG + "RETURN";

    public static final String REG_ARGV = REG + "ARGV";

    /**
     * identifiant de symbole masqué (hidden)
     */
    public static final char HIDDEN = '&';

    public static final String HIDDEN_str = "&";

    /**
     * identifiant de symbole privé (private)
     */
    public static final char PRIVATE = '$';

    public static final String PRIVATE_str = "$";

    /**
     * identifiant de symbole à lecture destructive (use-once)
     */
    public static final char USEONCE = '^';

    public static final String USEONCE_str = "^";

    /**
     * caractères syntaxiques réservés:
     * -------------------------------
     */
    public static final char COMMENT = '#';

    public static final char COMMENT_BLOCK_1 = '/';

    public static final char COMMENT_BLOCK_2 = '*';

    /**
     * symboles réservés
     */
    public static final String IMPORTS_PATH = "imports-path";

    public static final String IMPORTS_BASE_URL = "imports-base-url";

    public static final String IMPORTS_RELPATH = "imports";

    public static final String LIBRARIES_PATH = "libraries-path";

    public static final String LIBRARIES_BASE_URL = "libraries-base-url";

    public static final String LIBRARIES_RELPATH = "libraries";

    public static final String LIBRARY_PROPERTIES = "library.properties";

    public static final String RESOURCES_PATH = "resources-path";

    public static final String RESOURCES_BASE_URL = "resources-base-url";

    public static final String RESOURCES_RELPATH = "resources";

    public static final String RESULT = "RESULT";

    public static final String HAS_RESULT = "HAS_RESULT";

    public static final String CONSENT = "CONSENT";

    public static final String GLOBAL = "GLOBAL";

    public static final String REGISTRY = "REGISTRY";

    /**
     * slots d'objet...
     */
    public static final String SELF = "SELF";

    public static final String PARENT = "PARENT";

    public static final String CHILD = "CHILD";

    public static final String CLASS_ID = "CLASS_ID";

    public static final String EXTERNAL = "External";

    public static final String REQUIREMENTS_ = "requirements";

    public static final String INVARIANTS_ = "invariants";

    public static final String SERIALIZE_ = "serialize";

    public static final String COMPARE_ = "compare";

    public static final String INDEX_ = "index";

    public static final String STRING_ = "string";

    public static final String NUMBER_ = "number";

    public static final String LIST_ = "list";

    public static final String LENGTH_ = "length";

    /**
     * constantes
     */
    public static final String THIS = "THIS";

    public static final String NOTHING = "NOTHING";

    public static final String TRUE = "TRUE";

    public static final String FALSE = "FALSE";

    public static final String PI = "PI";

    public static final String NAPIER = "NAPIER";

    public static final String EULER = "EULER";

    public static final String MAIN = "MAIN";

    public static final String INTERPRETER_REVISION = "INTERPRETER_REVISION";

    private static final String[] UNOPTIMIZABLE_SYMBOLS = { SELF };

    public static final boolean isOptimizableSymbols(String symbol) {
        for (String s : UNOPTIMIZABLE_SYMBOLS) {
            if (s.equals(symbol)) {
                return false;
            }
        }
        return true;
    }

    static final String[] ORDER_FROM_ALL = { SELF, PARENT, CHILD, CLASS_ID, EXTERNAL, REQUIREMENTS_, INVARIANTS_, SERIALIZE_, COMPARE_, STRING_, NUMBER_, LIST_, INDEX_ };

    private static final String[] UNMIXABLE_FROM_ALL = { SELF, CHILD, PARENT, CLASS_ID };

    public static final boolean isUnMixableFromAll(String symbol) {
        for (String s : UNMIXABLE_FROM_ALL) {
            if (s.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    /**
     * controle pour l'héritage d'objet à objet...
     * Les instances suivantes ne peuvent être héritées.
     */
    private static final String[] UNINERITABLE_FROM_SYMBOL = { SELF, PARENT, CHILD, CLASS_ID };

    public static final boolean isUnInheritableFromSymbol(String symbol) {
        for (String s : UNINERITABLE_FROM_SYMBOL) {
            if (s.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    /**
     * controle pour autoriser (extends ...)...
     * On ne peut pas étendre un objet déjà dérivé...
     */
    private static final String[] ACCEPT_FOR_EXTENDS = { SELF, PARENT, CHILD, CLASS_ID };

    public static final boolean isAcceptedForExtend(String symbol) {
        for (String s : ACCEPT_FOR_EXTENDS) {
            if (s.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    private static final String[] UNINERITABLE_FROM_MODULE = { SELF, CLASS_ID };

    public static final boolean isUnInheritableFromModule(String symbol) {
        for (String s : UNINERITABLE_FROM_MODULE) {
            if (s.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Liste des pcodes...
     */
    static final PCode[] PCODES = { new PCode("", PC_ERR, new PCFx_TEMP(PC_ERR)), new PCode("quote", PC_QUOTE, new PCFx_quote()), new PCode("unquote", PC_UNQUOTE, new PCFx_unquote()), new PCode("!quote", PC_IMMEDIATE_QUOTE, new PCFx_immediate_quote()), new PCode("!unquote", PC_IMMEDIATE_UNQUOTE, new PCFx_immediate_unquote()), new PCode("quoted?", PC_isQUOTED, new PCFx_is__Quoted()), new PCode("quote-enc", PC_QUOTE_ENC, new PCFx_quote_enc()), new PCode("quote-dec", PC_QUOTE_DEC, new PCFx_quote_dec()), new PCode("quote-encoded?", PC_isQUOTE_ENCODED, new PCFx_is__Quote_Encoded()), new PCode("operator", PC_OPERATOR, new PCFx_operator()), new PCode("symbol", PC_SYMBOL, new PCFx_meta_symbol()), new PCode("expr", PC_EXPR, new PCFx_meta_expr()), new PCode("lazy", PC_LAZY, new PCFx_lazy()), new PCode("operator?", PC_isOPERATOR, new PCFx_is__operator()), new PCode("symbol?", PC_isSYMBOL, new PCFx_is__Symbol()), new PCode("lazy?", PC_isLAZY, new PCFx_is__Lazy()), new PCode("expr?", PC_isEXPR, new PCFx_is__Expr()), new PCode("compile", PC_COMPILE, new PCFx_compile()), new PCode("eval", PC_EVAL, new PCFx_eval()), new PCode("~", PC_EVALsucre, new PCFx_eval()), new PCode("serialize", PC_SERIALIZE, new PCFx_serialize()), new PCode("meta-process", PC_META_PROCESS, new PCFx_meta_process()), new PCode("substitute", PC_SUBSTITUTE, new PCFx_substitute()), new PCode("unify", PC_UNIFY, new PCFx_unify()), new PCode("match?", PC_isMATCH, new PCFx_is_match()), new PCode("+", PC_ADD, new PCFx_add()), new PCode("-", PC_SUB, new PCFx_sub()), new PCode("*", PC_MUL, new PCFx_mul()), new PCode("/", PC_DIV, new PCFx_div()), new PCode("mod", PC_MOD, new PCFx_mod()), new PCode("sin", PC_SIN, new PCFx_sin()), new PCode("cos", PC_COS, new PCFx_cos()), new PCode("tan", PC_TAN, new PCFx_tan()), new PCode("round", PC_ROUND, new PCFx_round()), new PCode("pow", PC_POW, new PCFx_pow()), new PCode("sqrt", PC_SQRT, new PCFx_sqrt()), new PCode("abs", PC_ABS, new PCFx_abs()), new PCode("log", PC_LOG, new PCFx_log()), new PCode("log10", PC_LOG10, new PCFx_log10()), new PCode("digits10", PC_DIGITS10, new PCFx_digits10()), new PCode("exp", PC_EXP, new PCFx_exp()), new PCode("asin", PC_ASIN, new PCFx_asin()), new PCode("acos", PC_ACOS, new PCFx_acos()), new PCode("atan", PC_ATAN, new PCFx_atan()), new PCode("toradians", PC_TORADIANS, new PCFx_toradians()), new PCode("todegrees", PC_TODEGREES, new PCFx_todegrees()), new PCode("random", PC_RANDOM, new PCFx_random()), new PCode("floor", PC_FLOOR, new PCFx_floor()), new PCode("ceil", PC_CEILING, new PCFx_ceil()), new PCode("truncate", PC_TRUNCATE, new PCFx_truncate()), new PCode("sgn", PC_SGN, new PCFx_sgn()), new PCode("fractional", PC_FRACTIONAL, new PCFx_fractional()), new PCode("not", PC_NOT, new PCFx_not()), new PCode("and", PC_CAND, new PCFx_and()), new PCode("or", PC_COR, new PCFx_or()), new PCode("xor", PC_XOR, new PCFx_xor()), new PCode("lshift", PC_SHIFTL, new PCFx_shiftl()), new PCode("rshift", PC_SHIFTR, new PCFx_shiftr()), new PCode("f-not", PC_FUZZY_NOT, new PCFx_f_not()), new PCode("f-and", PC_FUZZY_AND, new PCFx_f_and()), new PCode("f-or", PC_FUZZY_OR, new PCFx_f_or()), new PCode("p-and", PC_FUZZY_PROBABILITY_AND, new PCFx_p_and()), new PCode("p-or", PC_FUZZY_PROBABILITY_OR, new PCFx_p_or()), new PCode("degree-of", PC_FUZZY_DEGREE_OF, new PCFx_f_degree_of()), new PCode("member-of", PC_FUZZY_MEMBER_OF, new PCFx_f_member_of()), new PCode("increasing-to", PC_INCREASING_TO, new PCFx_TEMP(PC_INCREASING_TO)), new PCode("decreasing-to", PC_DECREASING_TO, new PCFx_TEMP(PC_DECREASING_TO)), new PCode("==?", PC_EQUIDENTITY, new PCFx_is__EquIdentity()), new PCode("=?", PC_EQUALS, new PCFx_is__Equals()), new PCode(">?", PC_GT, new PCFX_is__GT()), new PCode("<?", PC_LT, new PCFx_is__LT()), new PCode("<>?", PC_NEQUALS, new PCFx_is__NEquals()), new PCode(">=?", PC_GTE, new PCFx_is__GTE()), new PCode("<=?", PC_LTE, new PCFx_is__LTE()), new PCode("integer?", PC_isINTEGER, new PCFx_is__Integer()), new PCode("real?", PC_isREAL, new PCFx_is__Real()), new PCode("zero?", PC_isZERO, new PCFx_is__Zero()), new PCode("positive?", PC_isPOSITIVE, new PCFx_is__Positive()), new PCode("negative?", PC_isNEGATIVE, new PCFx_is__Negative()), new PCode("even?", PC_isEVEN, new PCFx_is__Even()), new PCode("odd?", PC_isODD, new PCFx_is__Odd()), new PCode("nothing?", PC_isNOTHING, new PCFx_is__Nothing()), new PCode("something?", PC_isSOMETHING, new PCFx_is__Something()), new PCode("value?", PC_isVALUE, new PCFx_is__Value()), new PCode("list?", PC_isLIST, new PCFx_is__List()), new PCode("tuple?", PC_isTUPLE, new PCFx_is__Tuple()), new PCode("string?", PC_isSTRING, new PCFx_is__String()), new PCode("number?", PC_isNUMBER, new PCFx_is__Number()), new PCode("key", PC_KEY, new PCFx_key()), new PCode("value", PC_VALUE, new PCFx_value()), new PCode("set-value!", PC_SET_VALUE, new PCFx_set_value()), new PCode("pair?", PC_isPAIR, new PCFx_is__Pair()), new PCode("key?", PC_isKEY, new PCFx_is__Key()), new PCode("locate-key", PC_LOCATE_KEY, new PCFx_locate_key()), new PCode("defined?", PC_isDEFINED, new PCFx_is__Defined()), new PCode("undefined?", PC_isUNDEFINED, new PCFx_is__Undefined()), new PCode("final?", PC_isFINAL, new PCFx_is__Final()), new PCode("hidden?", PC_isHIDDEN, new PCFx_is__Hidden()), new PCode("private?", PC_isPRIVATE, new PCFx_is__Private()), new PCode("immutable?", PC_isIMMUTABLE, new PCFx_is__Immutable()), new PCode("immuable?", PC_isIMMUABLE, new PCFx_is__Immuable()), new PCode("use-once?", PC_isUSEONCE, new PCFx_is__Use_Once()), new PCode("like-string?", PC_isLIKE_STRING, new PCFx_is__Like_string()), new PCode("match-regex?", PC_isMATCH_REGEX, new PCFx_is__Match_regex()), new PCode("split-regex", PC_SPLIT_REGEX, new PCFx_split_regex()), new PCode("find-regex", PC_FIND_REGEX, new PCFx_find_regex()), new PCode("list-regex", PC_LIST_REGEX, new PCFx_list_regex()), new PCode("replace-regex", PC_REPLACE_REGEX, new PCFx_replace_regex()), new PCode("if", PC_IF, new PCFx_if()), new PCode("then", PC_THEN, new PCFx_TEMP(PC_THEN)), new PCode("else", PC_ELSE, new PCFx_TEMP(PC_ELSE)), new PCode("elif", PC_ELIF, new PCFx_TEMP(PC_ELIF)), new PCode("infer", PC_INFER, new PCFx_infer()), new PCode("each-time", PC_EACH_TIME, new PCFx_TEMP(PC_EACH_TIME)), new PCode("once", PC_ONCE, new PCFx_TEMP(PC_ONCE)), new PCode("cond", PC_COND, new PCFx_TEMP(PC_COND)), new PCode("switch", PC_SWITCH, new PCFx_switch()), new PCode("invoke", PC_INVOKE, new PCFx_invoke()), new PCode("while", PC_WHILE, new PCFx_while()), new PCode("until", PC_UNTIL, new PCFx_until()), new PCode("forever", PC_FOREVER, new PCFx_forever()), new PCode("foreach", PC_FOREACH, new PCFx_foreach()), new PCode("foreach-perm", PC_FOREACH_PERM, new PCFx_foreach_perm()), new PCode("foreach-combin", PC_FOREACH_COMBIN, new PCFx_foreach_combin()), new PCode("foreach-part", PC_FOREACH_PART, new PCFx_foreach_part()), new PCode("foreach-circ", PC_FOREACH_CIRC, new PCFx_foreach_circ()), new PCode("foreach-row", PC_FOREACH_ROW, new PCFx_foreach_row()), new PCode("foreach-col", PC_FOREACH_COL, new PCFx_foreach_col()), new PCode("foreach-diag", PC_FOREACH_DIAG, new PCFx_foreach_diag()), new PCode("foreach-product", PC_FOREACH_PRODUCT, new PCFx_foreach_product()), new PCode("foreach-sum", PC_FOREACH_SUM, new PCFx_foreach_sum()), new PCode("foreach-all-different", PC_FOREACH_ALL_DIFFERENT, new PCFx_foreach_all_different()), new PCode("do", PC_DO, new PCFx_do()), new PCode("complete-loop", PC_COMPLETE_LOOP, new PCFx_complete_loop()), new PCode("complete-loop?", PC_isCOMPLETE_LOOP, new PCFx_is__Complete_loop()), new PCode("break-loop", PC_BREAK_LOOP, new PCFx_break_loop()), new PCode("skip-loop", PC_SKIP_LOOP, new PCFx_skip_loop()), new PCode("map", PC_MAP, new PCFx_map()), new PCode("reduce", PC_REDUCE, new PCFx_reduce()), new PCode("perform", PC_PERFORM, new PCFx_perform()), new PCode("restart", PC_RESTART, new PCFx_restart()), new PCode("void", PC_VOID, new PCFx_void()), new PCode("exit", PC_EXIT, new PCFx_exit()), new PCode("timed", PC_TIMED, new PCFx_timed()), new PCode("try", PC_TRY, new PCFx_try()), new PCode("retry", PC_RETRY, new PCFx_retry()), new PCode("abort", PC_ABORT, new PCFx_abort()), new PCode("catch", PC_CATCH, new PCFx_TEMP(PC_CATCH)), new PCode("raise", PC_RAISE, new PCFx_raise()), new PCode("assert", PC_ASSERT, new PCFx_assert()), new PCode("ensure", PC_ENSURE, new PCFx_ensure()), new PCode("termination", PC_TERMINATION, new PCFx_TEMP(PC_TERMINATION)), new PCode("define", PC_DEFINE, new PCFx_define()), new PCode("label", PC_LABEL, new PCFx_label()), new PCode("set!", PC_SET, new PCFx_set_static()), new PCode("let!", PC_LET, new PCFx_let_static()), new PCode("ref", PC_REF, new PCFx_ref()), new PCode("@", PC_REFsucre, new PCFx_ref()), new PCode("deref", PC_DEREF, new PCFx_deref()), new PCode(",", PC_DEREFsucre, new PCFx_deref()), new PCode("swap!", PC_SWAP, new PCFx_swap()), new PCode("clone", PC_CLONE, new PCFx_clone()), new PCode(";", PC_CLONEsucre, new PCFx_clone()), new PCode("args", PC_ARGS, new PCFx_args()), new PCode("default-args", PC_DEFAULT_ARGS, new PCFx_default_args()), new PCode("default-opts", PC_DEFAULT_OPTS, new PCFx_default_opts()), new PCode("function", PC_FUNCTION, new PCFx_function()), new PCode("this", PC_THIS, new PCFx_this()), new PCode("memoize", PC_MEMOIZE, new PCFx_memoize()), new PCode("memoize?", PC_isMEMOIZE, new PCFx_is__memoize()), new PCode("memoizable?", PC_isMEMOIZABLE, new PCFx_is__memoizable()), new PCode("memoize-dump", PC_MEMOIZE_DUMP, new PCFx_memoize_dump()), new PCode("function?", PC_isFUNCTION, new PCFx_is__Function()), new PCode("args?", PC_isARGS, new PCFx_is__Args()), new PCode("optional", PC_OPTIONAL, new PCFx_optional()), new PCode("return", PC_RETURN, new PCFx_return()), new PCode("!return", PC_IMMEDIATE_RETURN, new PCFx_immediate_return()), new PCode("delegate", PC_DELEGATE, new PCFx_delegate()), new PCode("require", PC_REQUIRE, new PCFx_require()), new PCode("contract", PC_CONTRACT, new PCFx_contract()), new PCode("precond", PC_PRECOND, new PCFx_TEMP(PC_PRECOND)), new PCode("postcond", PC_POSTCOND, new PCFx_TEMP(PC_POSTCOND)), new PCode("contracts?", PC_isCONTRACTS, new PCFx_is__Contracts()), new PCode("use-contracts", PC_USE_CONTRACTS, new PCFx_use_contracts()), new PCode("supplier", PC_SUPPLIER, new PCFx_TEMP(PC_SUPPLIER)), new PCode("class", PC_CLASS, new PCFx_class()), new PCode("class-id", PC_CLASSID, new PCFx_class_id()), new PCode("new", PC_NEW, new PCFx_new()), new PCode("final", PC_FINAL, new PCFx_TEMP(PC_FINAL)), new PCode("self", PC_SELF, new PCFx_self()), new PCode("is-a", PC_IS_A, new PCFx_is_a()), new PCode("has-a", PC_HAS_A, new PCFx_has_a()), new PCode("mixin", PC_MIXIN, new PCFx_mixin()), new PCode("object", PC_OBJECT, new PCFx_object()), new PCode("object?", PC_isOBJECT, new PCFx_is__Object()), new PCode("class?", PC_isCLASS, new PCFx_is__Class()), new PCode("is-a?", PC_isIS_A, new PCFx_is__is_a()), new PCode("has-a?", PC_isHAS_A, new PCFx_is__has_a()), new PCode("using", PC_USING, new PCFx_using()), new PCode("slots", PC_SLOTS, new PCFx_slots()), new PCode("from", PC_FROM, new PCFx_TEMP(PC_FROM)), new PCode("all", PC_ALL, new PCFx_TEMP(PC_ALL)), new PCode("...", PC_OTHERS, new PCFx_TEMP(PC_OTHERS)), new PCode("alias", PC_ALIAS, new PCFx_TEMP(PC_ALIAS)), new PCode("of", PC_OF, new PCFx_TEMP(PC_OF)), new PCode("in", PC_IN, new PCFx_TEMP(PC_IN)), new PCode("to", PC_TO, new PCFx_TEMP(PC_TO)), new PCode("at", PC_AT, new PCFx_TEMP(PC_AT)), new PCode("by", PC_BY, new PCFx_TEMP(PC_BY)), new PCode("for", PC_FOR, new PCFx_TEMP(PC_FOR)), new PCode("with", PC_WITH, new PCFx_TEMP(PC_WITH)), new PCode("library", PC_LIBRARY, new PCFx_library()), new PCode("register", PC_REGISTER, new PCFx_register()), new PCode("register?", PC_isREGISTER, new PCFx_is__register()), new PCode("unregister", PC_UNREGISTER, new PCFx_unregister()), new PCode("resource", PC_RESOURCE, new PCFx_resource()), new PCode("import", PC_IMPORT, new PCFx_import()), new PCode("include", PC_INCLUDE, new PCFx_include()), new PCode("extcall", PC_EXTCALL, new PCFx_extcall()), new PCode("external?", PC_isEXTERNAL, new PCFx_is__External()), new PCode("actor", PC_ACTOR, new PCFx_actor()), new PCode("mutex", PC_MUTEX, new PCFx_mutex()), new PCode("mutex?", PC_isMUTEX, new PCFx_is__mutex()), new PCode("atomic", PC_ATOMIC, new PCFx_atomic()), new PCode("lock", PC_LOCK, new PCFx_lock()), new PCode("locked?", PC_isLOCKED, new PCFx_is__locked()), new PCode("suspend", PC_SUSPEND, new PCFx_suspend()), new PCode("resume", PC_RESUME, new PCFx_resume()), new PCode("kill", PC_KILL, new PCFx_kill()), new PCode("suspended?", PC_isSUSPENDED, new PCFx_is__Suspended()), new PCode("actors", PC_ACTORS, new PCFx_actors()), new PCode("my-actor-id", PC_MY_ACTOR_ID, new PCFx_my_actor_id()), new PCode("supervisor-id", PC_SUPERVISOR_ID, new PCFx_supervisor_id()), new PCode("sleep", PC_SLEEP, new PCFx_sleep()), new PCode("send", PC_SEND, new PCFx_send()), new PCode("receive", PC_RECEIVE, new PCFx_receive()), new PCode("has-message?", PC_isMESSAGES, new PCFx_is__Messages()), new PCode("pending", PC_PENDING, new PCFx_pending()), new PCode("postpone", PC_POSTPONE, new PCFx_postpone()), new PCode("pending?", PC_isPENDING, new PCFx_is__Pending()), new PCode("postponed?", PC_isPOSTPONED, new PCFx_is__Postponed()), new PCode("display", PC_DISPLAY, new PCFx_display()), new PCode("display..", PC_DISPLAY_NOLINE, new PCFx_display_noline()), new PCode("display-clr", PC_DISPLAY_CLR, new PCFx_display_clr()), new PCode("raw-input", PC_RAW_INPUT, new PCFx_raw_input()), new PCode("true!", PC_ATOMIC_TRUE_STATIC, new PCFx_atomic_true_static()), new PCode("false!", PC_ATOMIC_FALSE_STATIC, new PCFx_atomic_false_static()), new PCode("setn!", PC_ATOMIC_SETN_STATIC, new PCFx_atomic_setn_static()), new PCode("+!", PC_ATOMIC_ADD_STATIC, new PCFx_atomic_add_static()), new PCode("-!", PC_ATOMIC_SUB_STATIC, new PCFx_atomic_sub_static()), new PCode("*!", PC_ATOMIC_MUL_STATIC, new PCFx_atomic_mul_static()), new PCode("/!", PC_ATOMIC_DIV_STATIC, new PCFx_atomic_div_static()), new PCode("incr!", PC_ATOMIC_INCR_STATIC, new PCFx_atomic_incr_static()), new PCode("decr!", PC_ATOMIC_DECR_STATIC, new PCFx_atomic_decr_static()), new PCode("or!", PC_ATOMIC_OR_STATIC, new PCFx_atomic_or_static()), new PCode("and!", PC_ATOMIC_AND_STATIC, new PCFx_atomic_and_static()), new PCode("not!", PC_ATOMIC_NOT_STATIC, new PCFx_atomic_not_static()), new PCode("xor!", PC_ATOMIC_XOR_STATIC, new PCFx_atomic_xor_static()), new PCode("lshift!", PC_ATOMIC_SHIFTL_STATIC, new PCFx_atomic_shiftl_static()), new PCode("rshift!", PC_ATOMIC_SHIFTR_STATIC, new PCFx_atomic_shiftr_static()), new PCode("fetch&incr!", PC_ATOMIC_FETCH_AND_INCR_STATIC, new PCFx_atomic_fetch_and_incr_static()), new PCode("fetch&decr!", PC_ATOMIC_FETCH_AND_DECR_STATIC, new PCFx_atomic_fetch_and_decr_static()), new PCode("fetch&+!", PC_ATOMIC_FETCH_AND_ADD_STATIC, new PCFx_atomic_fetch_and_add_static()), new PCode("fetch&-!", PC_ATOMIC_FETCH_AND_SUB_STATIC, new PCFx_atomic_fetch_and_sub_static()), new PCode("fetch&*!", PC_ATOMIC_FETCH_AND_MUL_STATIC, new PCFx_atomic_fetch_and_mul_static()), new PCode("fetch&/!", PC_ATOMIC_FETCH_AND_DIV_STATIC, new PCFx_atomic_fetch_and_div_static()), new PCode("fetch&true!", PC_ATOMIC_FETCH_AND_TRUE_STATIC, new PCFx_atomic_fetch_and_true_static()), new PCode("fetch&false!", PC_ATOMIC_FETCH_AND_FALSE_STATIC, new PCFx_atomic_fetch_and_false_static()), new PCode("fetch&setn!", PC_ATOMIC_FETCH_AND_SETN_STATIC, new PCFx_atomic_fetch_and_setn_static()), new PCode("fetch&or!", PC_ATOMIC_FETCH_AND_OR_STATIC, new PCFx_atomic_fetch_and_or_static()), new PCode("fetch&and!", PC_ATOMIC_FETCH_AND_AND_STATIC, new PCFx_atomic_fetch_and_and_static()), new PCode("fetch&not!", PC_ATOMIC_FETCH_AND_NOT_STATIC, new PCFx_atomic_fetch_and_not_static()), new PCode("fetch&xor!", PC_ATOMIC_FETCH_AND_XOR_STATIC, new PCFx_atomic_fetch_and_xor_static()), new PCode("fetch&lshift!", PC_ATOMIC_FETCH_AND_SHIFTL_STATIC, new PCFx_atomic_fetch_and_shiftl_static()), new PCode("fetch&rshift!", PC_ATOMIC_FETCH_AND_SHIFTR_STATIC, new PCFx_atomic_fetch_and_shiftr_static()), new PCode("select-first", PC_SELECT_FIRST, new PCFx_select_first()), new PCode("select-rest", PC_SELECT_REST, new PCFx_select_rest()), new PCode("select-head", PC_SELECT_HEAD, new PCFx_select_head()), new PCode("select-tail", PC_SELECT_TAIL, new PCFx_select_tail()), new PCode("select", PC_SELECT, new PCFx_select()), new PCode("select-min", PC_SELECT_MIN, new PCFx_select_min()), new PCode("select-max", PC_SELECT_MAX, new PCFx_select_max()), new PCode("crossover-pmx", PC_CROSSOVER_PMX, new PCFx_crossover_pmx()), new PCode("crossover-px", PC_CROSSOVER_PX, new PCFx_crossover_px()), new PCode("crossover-cx", PC_CROSSOVER_CX, new PCFx_crossover_cx()), new PCode("crossover-ox", PC_CROSSOVER_OX, new PCFx_crossover_ox()), new PCode("crossover-erx", PC_CROSSOVER_ERX, new PCFx_crossover_erx()), new PCode("populate-random", PC_POPULATE_RANDOM, new PCFx_populate_random()), new PCode("mutate-random", PC_MUTATE_RANDOM, new PCFx_mutate_random()), new PCode("mutate", PC_MUTATE, new PCFx_mutate()), new PCode("store!", PC_STORE_STATIC, new PCFx_store_static()), new PCode("store", PC_STORE, new PCFx_store()), new PCode("append!", PC_APPEND_STATIC, new PCFx_append_static()), new PCode("append", PC_APPEND, new PCFx_append()), new PCode("insert!", PC_INSERT_STATIC, new PCFx_insert_static()), new PCode("insert", PC_INSERT, new PCFx_insert()), new PCode("replace!", PC_REPLACE_STATIC, new PCFx_replace_static()), new PCode("replace", PC_REPLACE, new PCFx_replace()), new PCode("reverse!", PC_REVERSE_STATIC, new PCFx_reverse_static()), new PCode("reverse", PC_REVERSE, new PCFx_reverse()), new PCode("remove!", PC_REMOVE_STATIC, new PCFx_remove_static()), new PCode("remove", PC_REMOVE, new PCFx_remove()), new PCode("merge!", PC_MERGE_STATIC, new PCFx_merge_static()), new PCode("merge", PC_MERGE, new PCFx_merge()), new PCode("length", PC_LENGTH, new PCFx_length()), new PCode("find", PC_FIND, new PCFx_find()), new PCode("count", PC_COUNT, new PCFx_count()), new PCode("pop!", PC_POP_STATIC, new PCFx_pop_static()), new PCode("dequeue!", PC_DEQUEUE_STATIC, new PCFx_dequeue_static()), new PCode("range", PC_RANGE, new PCFx_range()), new PCode("compact", PC_COMPACT, new PCFx_compact()), new PCode("compact!", PC_COMPACT_STATIC, new PCFx_compact_static()), new PCode("flatten", PC_FLATTEN, new PCFx_flatten()), new PCode("flatten!", PC_FLATTEN_STATIC, new PCFx_flatten_static()), new PCode("keys", PC_KEYS, new PCFx_keys()), new PCode("values", PC_VALUES, new PCFx_values()), new PCode("diff", PC_DIFF, new PCFx_diff()), new PCode("string", PC_STRING, new PCFx_string()), new PCode("number", PC_NUMBER, new PCFx_number()), new PCode("nothing", PC_NOTHING, new PCFx_nothing()), new PCode("external", PC_EXTERNAL, new PCFx_external()), new PCode("hex-string", PC_HEX_STRING, new PCFx_hex_string()), new PCode("bin-string", PC_BIN_STRING, new PCFx_bin_string()), new PCode("oct-string", PC_OCT_STRING, new PCFx_oct_string()), new PCode("raw-string", PC_RAW_STRING, new PCFx_raw_string()), new PCode("list", PC_LIST, new PCFx_list()), new PCode("tuple", PC_TUPLE, new PCFx_tuple()), new PCode("pair", PC_PAIR, new PCFx_pair()), new PCode("max", PC_MAX, new PCFx_max()), new PCode("min", PC_MIN, new PCFx_min()), new PCode("upper-string", PC_UPPER_STRING, new PCFx_upper_string()), new PCode("lower-string", PC_LOWER_STRING, new PCFx_lower_string()), new PCode("ord", PC_ORD, new PCFx_ord()), new PCode("chr", PC_CHR, new PCFx_chr()), new PCode("trim-string", PC_TRIM_STRING, new PCFx_trim_string()), new PCode("filling-string", PC_FILLING_STRING, new PCFx_filling_string()), new PCode("split", PC_SPLIT, new PCFx_split()), new PCode("join", PC_JOIN, new PCFx_join()), new PCode("sort!", PC_SORT_STATIC, new PCFx_sort_static()), new PCode("sort", PC_SORT, new PCFx_sort()), new PCode("DUMP", PC_DUMP_HEAP, new PCFx_dump_heap()), new PCode("TESTPC", PC_TESTPC, new PCFx_test_pc()) };

    private static Hashtable PCODES_HASH = new Hashtable();

    static {
        for (int i = 0; i < PCODES.length; i++) {
            PCODES_HASH.put(PCODES[i].keyWord, PCODES[i]);
        }
    }

    public PCoder() {
    }

    public static int getPCode(String key) {
        int rpc = PC_ERR;
        PCode tmp = (PCode) PCODES_HASH.get(key);
        if (tmp != null) {
            rpc = tmp.pCode;
        }
        return rpc;
    }

    public static PCFx getPCFx(String key) {
        PCFx rpc = null;
        PCode tmp = (PCode) PCODES_HASH.get(key);
        if (tmp != null) {
            rpc = tmp.compiled.factory();
        }
        return rpc;
    }

    public static String getKey(int rpc) {
        String key = null;
        int i = 0;
        while (i < PCODES.length) {
            if (PCODES[i].pCode == rpc) {
                key = PCODES[i].keyWord;
                i = PCODES.length;
            }
            i++;
        }
        return key;
    }

    public static String selfing(String symbol) {
        return SELF + SEP + symbol;
    }
}
