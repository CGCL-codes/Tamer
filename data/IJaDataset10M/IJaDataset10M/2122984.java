package uk.gov.dti.og.fox.dbinterface2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.gov.dti.og.fox.XFUtil;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.dom.DOMList;
import uk.gov.dti.og.fox.ex.ExBadPath;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.track.Track;

/**
 * A parsed module statement container
 */
public class DbStatement extends Track {

    public static final String BINDSTRING = " ? ";

    public static final String QUERY = "query";

    public static final String API = "api";

    private static final String REGEX_MULTI_LINE_COMMENT = "/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/";

    private static final String REGEX_SINGLE_LINE_COMMENT = "--.*";

    private static final String REGEX_SINGLE_QUOTES = "\'(.|[\\r\\n])*?\'";

    private static final String REGEX_DOUBLE_QUOTES = "\\\"(.|[\\r\\n])*?\\\"";

    private static final String REGEX_BIND_VARIABLE = ":[\\w]*";

    private static final String REGEX_FULL = REGEX_MULTI_LINE_COMMENT + "|" + REGEX_SINGLE_LINE_COMMENT + "|" + REGEX_SINGLE_QUOTES + "|" + REGEX_DOUBLE_QUOTES + "|" + "(" + REGEX_BIND_VARIABLE + ")";

    private static final Pattern BIND_PATTERN = Pattern.compile(REGEX_FULL, Pattern.MULTILINE);

    public final String mStatementName;

    public final String mDBInterface;

    public final String mStatementType;

    private final DOM mDbStatementXML;

    private String mRawStatement;

    private String mProcessedStatement;

    private String mTargetPath;

    private final LinkedHashMap mFoxBinds = new LinkedHashMap();

    private final LinkedHashMap mFoxIntos = new LinkedHashMap();

    private final ArrayList mOracleBinds = new ArrayList();

    private String mPaginationDefinitionName;

    private boolean mStatementInitialised = false;

    private int mColCount;

    private DbResultColumn[] mResultColumnArr;

    protected DbStatement(DOM pDbStatementXML) {
        if (pDbStatementXML == null) {
            throw new ExInternal("New DbStatement requires a DOM and null was passed.");
        }
        mDbStatementXML = pDbStatementXML;
        String lStatementType = mDbStatementXML.getName();
        if (lStatementType == "fm:query") {
            mStatementType = QUERY;
        } else if (lStatementType == "fm:api") {
            mStatementType = API;
        } else {
            throw new ExInternal("Unknown statement type " + lStatementType);
        }
        try {
            mDBInterface = mDbStatementXML.xpath1S("../@name", null);
        } catch (Exception e) {
            throw new ExInternal("Error getting dbinterface name");
        }
        try {
            mStatementName = mDbStatementXML.xpath1S("@name", null);
        } catch (Exception e) {
            throw new ExInternal("Error getting " + mStatementType + " name");
        }
        mTargetPath = mDbStatementXML.xpath1SNoEx("fm:target-path/@match", null);
        if (XFUtil.isNull(mTargetPath)) {
            mTargetPath = ".";
        }
        mPaginationDefinitionName = mDbStatementXML.xpath1SNoEx("fm:pagination-definition", null);
        parseStatement();
    }

    /**
   * Get an array of the fox binds required to run this statement
   * 
   * @return An ordered Array of fox DbBindType for this statement
   */
    public DbBindType[] getFoxBindTypeArr() {
        DbBindType[] lFoxBindTypeArr = new DbBindType[mFoxBinds.size()];
        System.arraycopy(mFoxBinds.values().toArray(), 0, lFoxBindTypeArr, 0, mFoxBinds.size());
        return lFoxBindTypeArr;
    }

    public LinkedHashMap getFoxIntos() {
        return mFoxIntos;
    }

    /**
   * Get an array of the oracle binds required to run this statement
   * 
   * @return An ordered Array of oracle DbBindType for this statement. This will 
   * contain duplicate objects if binds are used multiple times
   */
    public DbBindType[] getOracleBindTypeArr() {
        DbBindType[] lOracleBindTypeArr = new DbBindType[mOracleBinds.size()];
        System.arraycopy(mOracleBinds.toArray(), 0, lOracleBindTypeArr, 0, mOracleBinds.size());
        return lOracleBindTypeArr;
    }

    /**
   * Create a new Execute set for this statement
   * 
   * @return the new DbExecuteSet
   */
    public DbExecuteSet newDbExecuteSet() {
        return new DbExecuteSet(this);
    }

    /**
   * Get the raw statement containing fox binds
   * 
   * @return The statement
   */
    public String getRawStatement() {
        return mRawStatement;
    }

    /**
   * Get the raw statement containing fox binds
   * 
   * @return The statement
   */
    public String getProcessedStatement() {
        return mProcessedStatement;
    }

    /**
   * Flag indicating if the statement has been initalised. When the statement id 
   * first run this will be set to true and th eColCount etc. will become 
   * available
   * 
   * @return true if initalised false if not
   */
    public boolean isStatementInitialised() {
        return mStatementInitialised;
    }

    /**
   * If the statement has been initalised it will show the number of columns 
   * returned by the statement. If the statement has not been initalised it will 
   * throw an error.
   * 
   * @return The number of columns
   */
    public int getColCount() {
        if (isStatementInitialised()) {
            return mColCount;
        } else {
            throw new ExInternal("Cannnot call getColCount before DbStatement has been executed");
        }
    }

    /**
   * If the statement has been initalised it will return an array of column 
   * types for this statement. If the statement has not been initalised it will 
   * throw an error.
   * 
   * @return DbResultColumn array for the statement
   */
    public DbResultColumn[] getResultColumnArr() {
        if (isStatementInitialised()) {
            return mResultColumnArr;
        } else {
            throw new ExInternal("Cannnot call getResultColumnArr before DbStatement has been executed");
        }
    }

    public String getTargetPath() {
        return mTargetPath;
    }

    public String getPaginationDefnitionName() {
        return mPaginationDefinitionName;
    }

    private void parseStatement() {
        DOMList lFoxBindsXML;
        DOMList lFoxIntosXML;
        try {
            lFoxBindsXML = mDbStatementXML.xpathUL("fm:using", null);
            lFoxIntosXML = mDbStatementXML.xpathUL("fm:into", null);
        } catch (ExBadPath e) {
            throw new ExInternal("Error getting bind variables or fm:into");
        }
        try {
            mRawStatement = mDbStatementXML.xpath1S("fm:select|fm:statement/text()", null);
        } catch (Exception e) {
            throw new ExInternal("Error getting statement");
        }
        mProcessedStatement = mRawStatement;
        for (int i = 0; i < lFoxBindsXML.getLength(); i++) {
            String lBindName = lFoxBindsXML.item(i).getAttrOrNull("name");
            lBindName = ":" + lBindName.replaceAll(":", "");
            if (mFoxBinds.containsKey(lBindName)) {
                throw new ExInternal("Bind variable " + lBindName + " already defined in " + mStatementType + " " + mDBInterface + "." + mStatementName);
            } else {
                DbBindType lDbBindType;
                String lSqlDataType = lFoxBindsXML.item(i).getAttrOrNull("sql-type");
                String lFoxDataType = lFoxBindsXML.item(i).getAttrOrNull("datadom-type");
                String lRelativeXpath = lFoxBindsXML.item(i).getAttrOrNull("datadom-location");
                lDbBindType = new DbBindType(lBindName, lSqlDataType, lFoxDataType, lRelativeXpath, i, this);
                mFoxBinds.put(lBindName, lDbBindType);
            }
        }
        for (int i = 0; i < lFoxIntosXML.getLength(); i++) {
            String lIntoName = lFoxIntosXML.item(i).getAttrOrNull("name");
            if (mFoxIntos.containsKey(lIntoName)) {
                throw new ExInternal("Into '" + lIntoName + "' already defined in " + mStatementType + " " + mDBInterface + "." + mStatementName);
            } else {
                DbBindType lDbBindType;
                String lSqlDataType = lFoxIntosXML.item(i).getAttrOrNull("sql-type");
                if (XFUtil.isNull(lSqlDataType)) {
                    lSqlDataType = DbBindType.SQLBIND_STRING;
                }
                String lFoxDataType = lFoxIntosXML.item(i).getAttrOrNull("datadom-type");
                String lRelativeXpath = lFoxIntosXML.item(i).getAttrOrNull("datadom-location");
                lDbBindType = new DbBindType(lIntoName, lSqlDataType, lFoxDataType, lRelativeXpath, i, this);
                mFoxIntos.put(lIntoName, lDbBindType);
            }
        }
        Matcher matcher = BIND_PATTERN.matcher(mRawStatement);
        int lOffset = 0;
        while (matcher.find()) {
            if (matcher.group(6) == null) {
                continue;
            }
            String lBindName = mProcessedStatement.substring(matcher.start() + lOffset, matcher.end() + lOffset);
            String lpt1 = mProcessedStatement.substring(0, matcher.start() + lOffset);
            String lpt2 = mProcessedStatement.substring(matcher.end() + lOffset, mProcessedStatement.length());
            mProcessedStatement = lpt1 + BINDSTRING + lpt2;
            lOffset = lOffset - lBindName.length() + BINDSTRING.length();
            DbBindType lDbBindType = (DbBindType) mFoxBinds.get(lBindName);
            if (lDbBindType == null) {
                throw new ExInternal("Bind variable " + lBindName + " not defined in " + mStatementType + " " + mDBInterface + "." + mStatementName);
            }
            mOracleBinds.add(lDbBindType);
        }
        Iterator i = mFoxBinds.keySet().iterator();
        while (i.hasNext()) {
            DbBindType lDbBindType = (DbBindType) mFoxBinds.get(i.next());
            if (!(mOracleBinds.contains(lDbBindType))) {
                throw new ExInternal("Bind variable " + lDbBindType.mBindName + " not found in " + mStatementType + " " + mDBInterface + "." + mStatementName);
            }
        }
    }

    /**
   * Called from DbResultSet on first run to set result values
   * @param pColCount The number of columns returned by statement
   * @param pResultColumnArr Array of column definitions
   */
    protected void initialiseStatement(int pColCount, DbResultColumn[] pResultColumnArr) {
        if (isStatementInitialised()) {
            throw new ExInternal("This Statement has already been initialised");
        } else {
            mColCount = pColCount;
            mResultColumnArr = pResultColumnArr;
            mStatementInitialised = true;
        }
    }
}
