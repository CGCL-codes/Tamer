package org.zxframework.expression;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jdom.Element;
import org.zxframework.Attribute;
import org.zxframework.ZXBO;
import org.zxframework.ZXCollection;
import org.zxframework.ZXException;
import org.zxframework.ZXObject;
import org.zxframework.zXType;
import org.zxframework.exception.NestableRuntimeException;
import org.zxframework.property.BooleanProperty;
import org.zxframework.property.DateProperty;
import org.zxframework.property.DoubleProperty;
import org.zxframework.property.LongProperty;
import org.zxframework.property.Property;
import org.zxframework.property.StringProperty;
import org.zxframework.util.DateUtil;
import org.zxframework.util.StringUtil;

/**
 * The (in)famous zX expression handler.
 * 
 * <pre>
 * 
 * Change    : BD19FEB03
 * Why       : Added BO context concept
 * 
 * Change    : BD4APR03
 * Why       : Added execute / setContext / setExpression / clearExpressions
 *             methods
 * 
 * Change    : BD7APR04
 * Why       : Added error handling + user message in parsing
 * 
 * Change    : BD12MAY03
 * Why       : No longer use local BO context; use zX BO context instead
 * 
 * Change    : BD28OCT03
 * Why       : Support empty expressions; return null
 * 
 * Change    : BD26NOV03
 * Why       : Added support for liveDump (for better real-time tracing)
 * 
 * Change    : BD3DEC03
 * Why       : Added support for unCompress (ie nice-ify an expression)
 *             and compress (undo the nice-ify-ing of an expression)
 * 
 * Change    : BD21JAN04
 * Why       : - Added support for configurable function handlers (read from
 *               the configuration file)
 *             - Added support for exprssion popup
 * 
 * Change    : DGS15JUN2004
 * Why       : Small but important change to populateFunctions.
 * 
 * Change    : BD18JUN04
 * Why       : Added liveDump for context variables
 * 
 * Change    : BD15SEP04
 * Why       : Made checkSyntax use verbose instead of just parse. A simple
 *             parse would only check for syntax. Verbose also looks at
 *             contents. This does mean that all relevant expression
 *             handlers must be registered
 *
 * Change    : DGS21OCT2004
 * Why       : Minor change to populateFunctions to improve visible info in drop-down.
 *
 * Change    : BD15DEC04 - V1.4:7
 * Why       : - Complete review of go function so it is easier to add
 *               control keywords
 *             - Made uncompress less error prone
 *             - Improved error messages
 * 
 * Change    : BD17JAN05 - V1.4:20
 * Why       : - See clsExpression for details
 *
 * Change    : BD14FEB05 - 40
 * Why       : - Added expression tracing
 *             - Added support for iterators
 *             - Added support for user defined functions
 *             - Fixed minor bugs and improved error handling
 * 
 * Change    : BD10JAN06 - V1.5:78
 * Why       : Fixed problem in _choose statement
 * 
 * </pre>
 * 
 * @author Michael Brewer
 * @author Bertus Dispa
 * @author David Swann
 * @version 0.01
 */
public class ExpressionHandler extends ZXObject {

    private ExprFHDefault defaultFH;

    /** A collection of loaded function handlers **/
    private Map fh;

    /** A collection (Expression) of stored expressions **/
    private Map expressions;

    /** The expression context. An expression will use this to set local variables. **/
    private Map context;

    private String parseMsg;

    /** A cache of parsed expression tokens **/
    private Map cache;

    /** Whether all of the function handlers have been loaded, we only want to do this once. **/
    private boolean handlersLoaded;

    /** A map collection of expression iterators. **/
    private Map iterators;

    /** 
     * The position that the parser is currently on. 
     * This is used to replace ByRef used in the VB version.
     * NOTE : This makes this statefull, so go() method may be unpredictable.
     **/
    private int tokenPostion = 0;

    /** The full filname of the trace file to write to **/
    private String traceFile;

    /** The out stream to write to. **/
    private PrintWriter traceStream;

    /** Whether expression tracing it active. **/
    private boolean tracingActive;

    /** Whether to append to the existing trace file. **/
    private boolean traceAppend;

    /** Whether to trace the bo context information aswell. **/
    private boolean traceDumpBOContext;

    /** Whether to dump the qs values. **/
    private boolean traceDumpQS;

    /**
     * Contructor for the ExpressioHandler.
     */
    public ExpressionHandler() {
        super();
        setDefaultFH(new ExprFHDefault());
        setCache(new HashMap());
        setIterators(new HashMap());
        setContext(new HashMap());
        setFh(new ZXCollection());
        getFh().put("", getDefaultFH());
        Element objElement = getZx().configXMLNode("//expressionTrace");
        if (objElement != null) {
            String strFilename = objElement.getChildText("filename");
            if (StringUtil.len(strFilename) == 0) {
                this.traceFile = getZx().fullPathName("trace" + File.separatorChar + getZx().getAppName() + " expression.trc");
            } else {
                this.traceFile = getZx().fullPathName(strFilename);
            }
            this.traceAppend = StringUtil.booleanValue(objElement.getChildText("append"));
            this.traceDumpBOContext = StringUtil.booleanValue(objElement.getChildText("dumpBO"));
            this.traceDumpQS = StringUtil.booleanValue(objElement.getChildText("dumpQS"));
            if (StringUtil.booleanValue(objElement.getChildText("active"))) {
                try {
                    startTracing();
                    traceMsg("Started expression tracing");
                    traceMsg("==========================");
                    traceMsg("");
                } catch (Exception e) {
                    getZx().trace.addError("Unable to start expression tracing", e);
                }
            }
        }
    }

    /**
     * Message with parse error; this will be set by clsExprExpression and is thus a bit volatile.
     * 
     * @return Returns the parseMsg.
     */
    public String getParseMsg() {
        return parseMsg;
    }

    /**
     * Message with parse error; this will be set by clsExprExpression and is thus a bit volatile.
     * 
     * @param parseMsg The parseMsg to set.
     */
    public void setParseMsg(String parseMsg) {
        this.parseMsg = parseMsg;
    }

    /**
     * @return Returns the iterators.
     */
    public Map getIterators() {
        return iterators;
    }

    /**
     * @param iterators The iterators to set.
     */
    public void setIterators(Map iterators) {
        this.iterators = iterators;
    }

    /**
     * @return Returns the defaultFH.
     */
    public ExprFHDefault getDefaultFH() {
        return defaultFH;
    }

    /**
     * @param defaultFH The defaultFH to set.
     */
    public void setDefaultFH(ExprFHDefault defaultFH) {
        this.defaultFH = defaultFH;
    }

    /**
     * Indicates whether the handlers have been initialised and loaded.
     * 
     * @return Returns the handlersLoaded.
     */
    private boolean isHandlersLoaded() {
        return handlersLoaded;
    }

    /**
     * @param handlersLoaded The handlersLoaded to set.
     */
    private void setHandlersLoaded(boolean handlersLoaded) {
        this.handlersLoaded = handlersLoaded;
    }

    /**
     * @return Returns the cache.
     */
    public Map getCache() {
        return cache;
    }

    /**
     * @param cache The cache to set.
     */
    public void setCache(HashMap cache) {
        this.cache = cache;
    }

    /**
     * @return Returns the context.
     */
    public Map getContext() {
        return context;
    }

    /**
     * @param context The context to set.
     */
    public void setContext(Map context) {
        this.context = context;
    }

    /**
     * Collection of pre-parsed expressions.
     * 
     * @return Returns the expressions.
     */
    public Map getExpressions() {
        return expressions;
    }

    /**
     * Collection of pre-parsed expressions.
     * 
     * @param expressions The expressions to set.
     */
    public void setExpressions(HashMap expressions) {
        this.expressions = expressions;
    }

    /**
     * Collection of function handlers.
     * 
     * NOTE : This init the collection when needed
     * 
     * @return Returns the fh.
     */
    public Map getFh() {
        return fh;
    }

    /**
     * Collection of function handlers.
     * 
     * @param fh The fh to set.
     */
    public void setFh(Map fh) {
        this.fh = fh;
    }

    /**
    * Evaluate an expression.
    *
    * @param pstrExpression The expression to parse 
    * @return Returns the evaluted expression as a Property. 
    * @throws ExpressionParseException Thrown if eval fails. 
    */
    public Property eval(String pstrExpression) throws ExpressionParseException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrExpression", pstrExpression);
        }
        Property eval = null;
        try {
            Expression objExpression = getFromCache(pstrExpression);
            eval = go(objExpression);
            if (this.tracingActive) {
                traceExpression(objExpression);
            }
            return eval;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Evaluate an expression", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrExpression = " + pstrExpression);
            }
            if (getZx().throwException) throw new ExpressionParseException(e);
            return eval;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(eval);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * liveDump an expression.
     *
     *<pre>
     *
     * Assumes   :
     *   Expression has been executed before
     * </pre>
     * 
     * @param pstrExpression The expression to get a dump of 
     * @return Returns a dump of an expression. 
     * @throws ZXException Thrown if liveDump fails. 
     */
    public String liveDump(String pstrExpression) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrExpression", pstrExpression);
        }
        String liveDump = "";
        try {
            Expression objExpression = getFromCache(pstrExpression);
            if (objExpression != null) {
                liveDump = objExpression.liveDump();
            }
            return liveDump;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : liveDump an expression", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrExpression = " + pstrExpression);
            }
            if (getZx().throwException) throw new ZXException(e);
            return liveDump;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(liveDump);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Get an expression from the cache to avoid having to parse expresions over and over again.
     *
     * @param pstrExpression The expression to get from the cache 
     * @return Returns an expression from the expression cache
     * @throws ExpressionParseException Thrown if getFromCache fails. 
     */
    private Expression getFromCache(String pstrExpression) throws ExpressionParseException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrExpression", pstrExpression);
        }
        Expression getFromCache = null;
        try {
            getFromCache = (Expression) getCache().get(pstrExpression);
            if (getFromCache == null) {
                getFromCache = new Expression();
                if (!getFromCache.parse(pstrExpression).equals(zXType.rc.rcOK)) {
                    throw new ExpressionParseException("Failed to parse expression");
                }
                getCache().put(pstrExpression, getFromCache);
            }
            return getFromCache;
        } catch (ExpressionParseException e) {
            getZx().trace.addError("Failed to : Get an expression from the cache to avoid having to parse expresions over and over again", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrExpression = " + pstrExpression);
            }
            if (getZx().throwException) throw new ExpressionParseException(e);
            return getFromCache;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(getFromCache);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Describe an expression.
     *
     * @param pstrExpression The expression to describe 
     * @return Returns a descriptions of the expression at runtime
     * @throws ZXException Thrown if describe fails. 
     */
    public String describe(String pstrExpression) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrExpression", pstrExpression);
        }
        String describe = null;
        try {
            Expression objExpression = new Expression();
            if (!objExpression.parse(pstrExpression).equals(zXType.rc.rcOK)) {
                throw new Exception("Unable to parse expression");
            }
            this.tokenPostion = 0;
            describe = go(objExpression, zXType.exprPurpose.epDescribe).getStringValue();
            return describe;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Describe an expression", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrExpression = " + pstrExpression);
            }
            if (getZx().throwException) throw new ZXException(e);
            return describe;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(describe);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Evaluate an expression. 
     * 
     * @param pobjExpression The expression to evaluate.
     * @return Returns a property from the evaluted expression.
     * @see ExpressionHandler#go(Expression, zXType.exprPurpose)
     */
    private Property go(Expression pobjExpression) {
        this.tokenPostion = 0;
        return go(pobjExpression, null);
    }

    /**
     * Evaluate an expression. 
     * 
     * <pre>
     * TODO : Should use the ByRefInt object to better handle the tokenPosition
     * 
     * Used internally where expression is a tokenised expression
     * 
     * Reviewed for V1.5:73
     *</pre>
     *
     * @param pobjExpression value is to be used 
     * @param penmPurpose Purpose of handling expression 
     * @return Returns a Property result of the expression 
     */
    private Property go(Expression pobjExpression, zXType.exprPurpose penmPurpose) {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjExpression", pobjExpression);
            getZx().trace.traceParam("penmPurpose", penmPurpose);
        }
        Property go = null;
        if (penmPurpose == null) {
            penmPurpose = zXType.exprPurpose.epEval;
        }
        try {
            ExprToken objToken;
            ArrayList colTokens = pobjExpression.getTokens();
            objToken = (ExprToken) colTokens.get(this.tokenPostion);
            while (peekTokenType(colTokens, this.tokenPostion).pos == zXType.exprTokenType.ettComment.pos && this.tokenPostion <= colTokens.size()) {
                this.tokenPostion++;
                if (this.tokenPostion > colTokens.size()) {
                    return go;
                }
                objToken = (ExprToken) colTokens.get(this.tokenPostion);
            }
            if (objToken.getTokenType().pos == zXType.exprTokenType.ettDate.pos) {
                Date date = DateUtil.parse(getZx().getDateFormat(), objToken.getToken());
                go = new DateProperty(date, false);
            } else if (objToken.getTokenType().pos == zXType.exprTokenType.ettContext.pos) {
                if (penmPurpose.equals(zXType.exprPurpose.epEval)) {
                    go = (Property) getContext().get(objToken.getToken());
                } else if (penmPurpose.equals(zXType.exprPurpose.epDescribe)) {
                    go = new StringProperty("[" + objToken.getToken() + "]", false);
                } else if (penmPurpose.equals(zXType.exprPurpose.epAPI)) {
                    go = new StringProperty("[str]", false);
                }
            } else if (objToken.getTokenType().pos == zXType.exprTokenType.ettDouble.pos) {
                go = new DoubleProperty(Double.parseDouble(objToken.getToken()), false);
            } else if (objToken.getTokenType().pos == zXType.exprTokenType.ettInteger.pos) {
                go = new LongProperty(new Long(objToken.getToken()), false);
            } else if (objToken.getTokenType().pos == zXType.exprTokenType.ettString.pos) {
                go = new StringProperty(objToken.getToken(), false);
            } else if (objToken.getTokenType().pos == zXType.exprTokenType.ettId.pos || (objToken.getTokenType().pos == zXType.exprTokenType.ettExternalId.pos)) {
                if (expectTokenType(zXType.exprTokenType.ettStartParmList, colTokens, this.tokenPostion + 1).pos == zXType.rc.rcError.pos) {
                    throw new Exception("No parameters found for this expression");
                }
                String strToken = objToken.getToken();
                if (strToken.equalsIgnoreCase("_if")) {
                    go = handleControlKeywordIF(pobjExpression, penmPurpose);
                } else if (strToken.equalsIgnoreCase("_loopover")) {
                    go = handleControlKeywordLOOPOVER(pobjExpression, penmPurpose);
                } else if (strToken.equalsIgnoreCase("_and")) {
                    go = handleControlKeywordAND(pobjExpression, penmPurpose);
                } else if (strToken.equalsIgnoreCase("_or")) {
                    go = handleControlKeywordOR(pobjExpression, penmPurpose);
                } else if (strToken.equalsIgnoreCase("_select")) {
                    go = handleControlKeywordSELECT(pobjExpression, penmPurpose);
                } else if (strToken.equalsIgnoreCase("_choose")) {
                    go = handleControlKeywordCHOOSE(pobjExpression, penmPurpose);
                } else {
                    ArrayList colArgs = new ArrayList();
                    Property objArg;
                    this.tokenPostion = this.tokenPostion + 2;
                    while (peekTokenType(colTokens, this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                        if (peekTokenType(colTokens, this.tokenPostion).pos != zXType.exprTokenType.ettComment.pos) {
                            objArg = go(pobjExpression, penmPurpose);
                            if (objArg == null) {
                                throw new Exception("Unable to execute function");
                            }
                            colArgs.add(objArg);
                        }
                        this.tokenPostion++;
                        if (peekTokenType(colTokens, this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                            this.tokenPostion++;
                        }
                    }
                    if (objToken.getTokenType().pos == zXType.exprTokenType.ettId.pos) {
                        go = getDefaultFH().go(objToken.getToken(), colArgs, penmPurpose);
                        if (go == null) {
                            throw new Exception("Unable to evaluate function : " + objToken.getToken());
                        }
                    } else {
                        String strHandler = objToken.getToken().substring(0, objToken.getToken().indexOf('.'));
                        String strFunction = objToken.getToken().substring(objToken.getToken().indexOf('.') + 1);
                        if (strHandler.equalsIgnoreCase("usr")) {
                            go = resolveUsrFunction(strFunction, colArgs, penmPurpose);
                            if (go == null) {
                                throw new Exception("Unable to execute user defined function : " + objToken.getToken());
                            }
                        } else {
                            if (!isHandlersLoaded()) {
                                loadHandlers();
                                setHandlersLoaded(true);
                            }
                            ExprFH objFH = (ExprFH) getFh().get(strHandler);
                            if (objFH == null) {
                                throw new Exception("Function handler not found : " + objToken.getToken());
                            }
                            int intTmp = this.tokenPostion;
                            go = objFH.go(strFunction, colArgs, penmPurpose);
                            this.tokenPostion = intTmp;
                            if (go == null) {
                                throw new ZXException("Unable to execute function : " + objToken.getToken());
                            }
                        }
                    }
                }
                objToken.setFunctionResult(go.getStringValue());
            }
            if (go == null) {
                go = new StringProperty("", true);
            }
            return go;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Evaluate an expression.", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pobjExpression = " + pobjExpression);
                getZx().log.error("Parameter : penmPurpose = " + penmPurpose);
            }
            go = new StringProperty("Failed to execute function : " + e.getMessage(), true);
            return go;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(go);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Check whether a specific token is at the specified slot in the token collection.
     * 
     * @param pstrToken The token to inspect.
     * @param pcolTokens The collection of tokens.
     * @param pintOffset The position.
     * @return Returns the return code of the method
     */
    protected zXType.rc expectToken(String pstrToken, ArrayList pcolTokens, int pintOffset) {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrToken", pstrToken);
            getZx().trace.traceParam("pcolTokens", pcolTokens);
            getZx().trace.traceParam("pintOffset", pintOffset);
        }
        zXType.rc expectToken = zXType.rc.rcOK;
        try {
            if (pintOffset > pcolTokens.size()) {
                throw new Exception("Unexpected end of expression found. Expected '" + pstrToken + "'");
            }
            ExprToken objToken = (ExprToken) pcolTokens.get(pintOffset);
            if (!objToken.getToken().equalsIgnoreCase(pstrToken)) {
                throw new Exception("Unexpected token at position " + objToken.getPosition() + " Expected '" + pstrToken + "'");
            }
            return expectToken;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Check whether a specific token is at the specified slot in the token collection", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrToken = " + pstrToken);
                getZx().log.error("Parameter : pcolTokens = " + pcolTokens);
                getZx().log.error("Parameter : pintOffset = " + pintOffset);
            }
            expectToken = zXType.rc.rcError;
            return expectToken;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Check whether the n-th token in token collection is of token-type xyz.
     * 
     * @param penmTokenType The token type to look for.
     * @param pcolTokens The tokens to look into.
     * @param pintOffset The position to seek to.
     * @return Returns the return code of the method
     */
    public zXType.rc expectTokenType(zXType.exprTokenType penmTokenType, ArrayList pcolTokens, int pintOffset) {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("penmTokenType", penmTokenType);
            getZx().trace.traceParam("pcolTokens", pcolTokens);
            getZx().trace.traceParam("pintOffset", pintOffset);
        }
        zXType.rc expectToken = zXType.rc.rcOK;
        try {
            if (pintOffset > pcolTokens.size()) {
                throw new Exception("Unexpected end of expression found");
            }
            ExprToken objToken = (ExprToken) pcolTokens.get(pintOffset);
            if (!objToken.getTokenType().equals(penmTokenType)) {
                throw new Exception("Unexpected token at position " + objToken.getPosition() + "Is'" + objToken.getToken() + "' (type" + zXType.valueOf(objToken.getTokenType()) + " expect type " + zXType.valueOf(penmTokenType) + ")");
            }
            return expectToken;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Check whether the n-th token in token collection is of token-type xyz", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : penmTokenType = " + penmTokenType);
                getZx().log.error("Parameter : pcolTokens = " + pcolTokens);
                getZx().log.error("Parameter : pintOffset = " + pintOffset);
            }
            expectToken = zXType.rc.rcError;
            return expectToken;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Return token from a specific position in token collection.
     *
     * @param pcolTokens The collection of expression token
     * @param pintOffset The postion of the you want to retrieve 
     * @return Returns token from a specific position in the token collection
     * @throws ZXException Thrown if peekToken fails.
     */
    protected String peekToken(ArrayList pcolTokens, int pintOffset) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pcolTokens", pcolTokens);
            getZx().trace.traceParam("pintOffset", pintOffset);
        }
        String peekToken = "";
        try {
            if (pintOffset < pcolTokens.size()) {
                ExprToken objToken = (ExprToken) pcolTokens.get(pintOffset);
                peekToken = objToken.getToken();
            }
            return peekToken;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Return token from a specific position in token collection", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pcolTokens = " + pcolTokens);
                getZx().log.error("Parameter : pintOffset = " + pintOffset);
            }
            if (getZx().throwException) throw new ZXException(e);
            return peekToken;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(peekToken);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Return type of n-th token from token collection. 
     * null if outside of range.
     * 
     * @param pcolTokens The collection of expression token
     * @param pintOffset The postion of the you want to retrieve
     * @return Returns token from a specific position in the token collection. Default is "ettEndParmList".
     * @throws ZXException Thrown if peekTokenType fails.
     */
    protected zXType.exprTokenType peekTokenType(ArrayList pcolTokens, int pintOffset) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pcolTokens", pcolTokens);
            getZx().trace.traceParam("pintOffset", pintOffset);
        }
        zXType.exprTokenType peekTokenType = zXType.exprTokenType.ettEndParmList;
        try {
            if (pintOffset < pcolTokens.size()) {
                ExprToken objToken = (ExprToken) pcolTokens.get(pintOffset);
                peekTokenType = objToken.getTokenType();
            }
            return peekTokenType;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Return type of n-th token from token collection", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pcolTokens = " + pcolTokens);
                getZx().log.error("Parameter : pintOffset = " + pintOffset);
            }
            if (getZx().throwException) throw new ZXException(e);
            return peekTokenType;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(peekTokenType);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Return true if the token represents a value (ie not a parenthesis, comma or anything like that)
     * 
     * @param penmTokenType 
     * @return Returns token from a specific position in the token collection
     * @throws ZXException Thrown if isValueTokenType fails.
     */
    protected boolean isValueTokenType(zXType.exprTokenType penmTokenType) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("penmTokenType", penmTokenType);
        }
        boolean isValueTokenType = false;
        try {
            if (penmTokenType == null || penmTokenType.pos == zXType.exprTokenType.ettEndParmList.pos || penmTokenType.pos == zXType.exprTokenType.ettNextParm.pos || penmTokenType.pos == zXType.exprTokenType.ettStartParmList.pos || penmTokenType.pos == zXType.exprTokenType.ettUnknown.pos) {
                isValueTokenType = false;
            } else {
                isValueTokenType = true;
            }
            return isValueTokenType;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : RReturn true if the token represents a value", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : penmTokenType = " + penmTokenType);
            }
            if (getZx().throwException) throw new ZXException(e);
            return isValueTokenType;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(isValueTokenType);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Register a function handler.
     *
     * @param pstrName The name of the function handler to add
     * @param pobjFH The actual function handler to add.
     */
    public void registerFH(String pstrName, ExprFH pobjFH) {
        pobjFH.setAlias(pstrName);
        getFh().put(pstrName, pobjFH);
    }

    /**
     * Clear all registered function handlers.
     *
     * @return Returns the return code of the method.
     */
    public zXType.rc resetFH() {
        zXType.rc resetFH = zXType.rc.rcOK;
        setFh(new ZXCollection());
        return resetFH;
    }

    /**
     * Clear expressions collection.
     *
     * @return Returns the return code of the method 
     */
    public zXType.rc clearExpressions() {
        zXType.rc clearExpressions = zXType.rc.rcOK;
        setExpressions(new HashMap());
        return clearExpressions;
    }

    /**
     * Clear context collection.
     *
     * @return Returns the return code of the method.
     */
    public zXType.rc clearContext() {
        zXType.rc clearContext = zXType.rc.rcOK;
        setContext(new HashMap());
        return clearContext;
    }

    /**
     * Add an entry to the context.
     * 
     * <pre>
     * 
     *  Assumes   :
     *    If no value is given, assume that
     *    you want to remove entry from context
     * </pre>
     *
     * @param pstrName  Name of the context.
     * @param pobjValue The value.Optional, default is null.
     * @return Returns the return code of the method
     */
    public zXType.rc setContext(String pstrName, Property pobjValue) {
        zXType.rc setContext = zXType.rc.rcOK;
        if (pobjValue != null) {
            getContext().put(pstrName, pobjValue);
        } else {
            getContext().remove(pstrName);
        }
        return setContext;
    }

    /**
     * Add expression to collection for execution later.
     * 
     * <pre>
     * 
     * Assumes   :
     *    If no expression is given, assume that
     *    you want to remove entry from context
     * </pre>
     *
     * @param pstrName Name of the expression 
     * @param pstrExpression The actual expression 
     * @return Returns the return code of the method.
     * @throws ZXException Thrown if setExpression fails. 
     */
    public zXType.rc setExpression(String pstrName, String pstrExpression) throws ZXException {
        zXType.rc setExpression = zXType.rc.rcOK;
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrName", pstrName);
            getZx().trace.traceParam("pstrExpression", pstrExpression);
        }
        try {
            Expression objExpression = new Expression();
            objExpression.parse(pstrExpression);
            getExpressions().put(pstrName, objExpression);
            return setExpression;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Add expression to collection for execution later", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrName = " + pstrName);
                getZx().log.error("Parameter : pstrExpression = " + pstrExpression);
            }
            if (getZx().throwException) throw new ZXException(e);
            setExpression = zXType.rc.rcError;
            return setExpression;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(setExpression);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Execute an expression that is in the collection of parsed expressions.
     *
     * @param pstrName The name of the expression 
     * @return Returns a populated property as the result 
     * @throws ZXException Thrown if execute fails. 
     * @deprecated This does not seem to be used.
     */
    public Property execute(String pstrName) throws ZXException {
        Property execute = null;
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrName", pstrName);
        }
        try {
            if (getExpressions() != null) {
                Expression objExpression = (Expression) getExpressions().get(pstrName);
                if (objExpression == null) {
                    throw new Exception("Stored expression not found");
                }
                execute = go(objExpression);
                if (execute == null) {
                    throw new Exception("Unable to execute expression");
                }
            }
            return execute;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Execute an expression that is in the collection of parsed expressions", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrName = " + pstrName);
            }
            if (getZx().throwException) throw new ZXException(e);
            return execute;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(execute);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Check the syntax of an expression.
     *
     * @param pstrExpression The expression to check the syntax of 
     * @return Returns the return code of the method. 
     * @throws ZXException Thrown if checkSyntax fails. 
     */
    public zXType.rc checkSyntax(String pstrExpression) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrExpression", pstrExpression);
        }
        zXType.rc checkSyntax = zXType.rc.rcOK;
        try {
            Expression objExpression = new Expression();
            if (!objExpression.parse(pstrExpression).equals(zXType.rc.rcOK)) {
                throw new Exception("Unable to parse expression : " + pstrExpression);
            }
            Property objProperty = null;
            try {
                objProperty = go(objExpression, zXType.exprPurpose.epDescribe);
            } catch (Exception e) {
                getZx().trace.addError("Failed to check syntax of an expression.", pstrExpression, e);
            }
            if (objProperty == null) {
                checkSyntax = zXType.rc.rcError;
            } else {
                checkSyntax = zXType.rc.rcOK;
            }
            return checkSyntax;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Check the syntax of an expression", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrExpression = " + pstrExpression);
            }
            if (getZx().throwException) throw new ZXException(e);
            checkSyntax = zXType.rc.rcError;
            return checkSyntax;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(checkSyntax);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Uncompress me.
     * 
     * <pre>
     * 
     * i.e. print in readable form
     * </pre>
     *
     * @param pstrExpression The expression  
     * @param pstrNewLine What the new line character is
     * @param pstrIndent What the indent character is.
     * @return Returns an uncompressed version of the expression
     * @throws ZXException Thrown if unCompress fails. 
     */
    public String unCompress(String pstrExpression, String pstrNewLine, String pstrIndent) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrExpression", pstrExpression);
            getZx().trace.traceParam("pstrNewLine", pstrNewLine);
            getZx().trace.traceParam("pstrIndent", pstrIndent);
        }
        String unCompress = null;
        try {
            Expression objExpression = getFromCache(pstrExpression);
            unCompress = objExpression.unCompress(pstrNewLine, pstrIndent);
            return unCompress;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Uncompress me", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrExpression = " + pstrExpression);
                getZx().log.error("Parameter : pstrNewLine = " + pstrNewLine);
                getZx().log.error("Parameter : pstrIndent = " + pstrIndent);
            }
            if (getZx().throwException) throw new ZXException(e);
            return unCompress;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(unCompress);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Compress an expression .
     * 
     * ie get rid of all non critical white spaces
     *
     * @param pstrExpression The expression to compress 
     * @return Returns a  Compress an expression
     * @throws ZXException  Thrown if compress fails. 
     */
    public String compress(String pstrExpression) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrExpression", pstrExpression);
        }
        String compress = null;
        try {
            Expression objExpression = getFromCache(pstrExpression);
            compress = objExpression.unCompress("", "");
            return compress;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Compress an expression", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrExpression = " + pstrExpression);
            }
            if (getZx().throwException) throw new ZXException(e);
            return compress;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(compress);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Load the handlers.
     *
     * @return Returns the return code of the function
     * @throws ZXException Thrown if loadHandlers fails. 
     */
    private zXType.rc loadHandlers() throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
        }
        zXType.rc loadHandlers = zXType.rc.rcOK;
        ExprFH objHandler;
        try {
            Element objXMLNode = getZx().configXMLNode("//exprFH");
            if (objXMLNode == null) {
                throw new Exception("Failed to get exprFH from the xml config file");
            }
            Iterator iter = objXMLNode.getChildren().iterator();
            Element objXMLElement;
            while (iter.hasNext()) {
                objXMLElement = (Element) iter.next();
                String strName = objXMLElement.getChildText("name");
                String strHandler = objXMLElement.getChildText("object");
                objHandler = (ExprFH) getZx().createObject(strHandler);
                if (objHandler == null) {
                    throw new Exception("Failed to create the function handler :" + strHandler);
                }
                registerFH(strName, objHandler);
            }
            return loadHandlers;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Load the handlers", e);
            if (getZx().throwException) throw new ZXException(e);
            loadHandlers = zXType.rc.rcError;
            return loadHandlers;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(loadHandlers);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Populate pulldown list with available functions.
     * 
     * <pre>
     * 
     * Used by expression editor popup.
     * </pre>
     *
     * @param pobjExprEdit The business object to populate with expression names
     * @return Returns the return code of the function
     * @throws ZXException Thrown if populateFunctions fails. 
     */
    public zXType.rc populateFunctions(ZXBO pobjExprEdit) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjExprEdit", pobjExprEdit);
        }
        zXType.rc populateFunctions = zXType.rc.rcOK;
        try {
            Attribute objAttr = pobjExprEdit.getDescriptor().getAttribute("fnctn");
            if (objAttr != null && objAttr.getOptions().size() == 0) {
                ExprFH objFH;
                Map colSupports;
                Iterator iter = getZx().getExpressionHandler().getFh().values().iterator();
                while (iter.hasNext()) {
                    objFH = (ExprFH) iter.next();
                    colSupports = objFH.supports();
                    objAttr.getOptions().putAll(colSupports);
                }
            }
            return populateFunctions;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Populate pulldown list with available functions", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pobjExprEdit = " + pobjExprEdit);
            }
            if (getZx().throwException) throw new ZXException(e);
            populateFunctions = zXType.rc.rcError;
            return populateFunctions;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(populateFunctions);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Add an iterator to the collection.
     * 
     * @param pstrName The name of the iterator.
     * @param pobjIterator The iterator to store.
     */
    public void setIterator(String pstrName, ExprFHI pobjIterator) {
        if (pobjIterator == null) {
            getIterators().remove(pstrName);
        } else {
            pobjIterator.setName(pstrName);
            getIterators().put(pstrName, pobjIterator);
        }
    }

    /**
     * Get an iterator from the collection.
     * 
     * @param pstrName The name of the iterator.
     * @return Returns the iterator
     */
    public ExprFHI getIterator(String pstrName) {
        return (ExprFHI) getIterators().get(pstrName);
    }

    /**
     * Clear the collection of iterators.
     */
    public void clearIterators() {
        setIterators(new HashMap());
    }

    /**
     * Activate expression tracing based on the current settings.
     */
    private void startTracing() {
        try {
            if (this.tracingActive) {
                stopTracing();
            }
            File file = new File(this.traceFile).getAbsoluteFile();
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            FileWriter fileWriter = new FileWriter(this.traceFile, this.traceAppend);
            traceStream = new PrintWriter(fileWriter, true);
            this.tracingActive = true;
        } catch (Exception e) {
            throw new NestableRuntimeException(e);
        }
    }

    /**
     * Stop tracing and clean up.
     */
    private void stopTracing() {
        if (this.tracingActive) {
            this.traceStream.close();
            this.tracingActive = false;
        }
    }

    /**
     * Trace a single msg.
     * 
     * @param pstrMsg The message to trace.
     */
    private void traceMsg(String pstrMsg) {
        if (this.tracingActive) {
            this.traceStream.println(new Date().toString() + " - " + pstrMsg);
        }
    }

    /**
     * Trace an expression.
     * 
     * @param pobjExpression The expression to trace.
     */
    private void traceExpression(Expression pobjExpression) {
        try {
            if (this.tracingActive) {
                traceMsg("Expression : " + pobjExpression.getExpression());
                traceMsg("zX context : " + getZx().getActionContext());
                traceMsg("Live dump  : " + liveDump(pobjExpression.getExpression()));
                if (this.traceDumpQS) {
                    traceMsg("QS dump    : \n" + getZx().getQuickContext().dump());
                }
                if (this.traceDumpBOContext) {
                    traceMsg("BO dump    : \n" + getZx().getBOContext().dump());
                }
                traceMsg(">>>");
            }
        } catch (Exception e) {
            throw new NestableRuntimeException(e);
        }
    }

    /**
	 * Handle the _and control keyword.
	 * 
	 * <pre>
	 * 
	 * Reviewed for V1.5:73
	 * </pre>
	 * 
	 * @param pobjExpression value is to be used 
	 * @param penmPurpose Purpose of handling expression 
	 * @return Returns the resultant property
	 * @throws ZXException Thrown if handleControlKeywordAND fails. 
	 */
    private Property handleControlKeywordAND(Expression pobjExpression, zXType.exprPurpose penmPurpose) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjExpression", pobjExpression);
            getZx().trace.traceParam("penmPurpose", penmPurpose);
        }
        Property handleControlKeywordAND = null;
        try {
            Property objArg;
            if (penmPurpose.equals(zXType.exprPurpose.epEval)) {
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                handleControlKeywordAND = new BooleanProperty(true);
                while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                    eatComments(pobjExpression);
                    objArg = go(pobjExpression, penmPurpose);
                    if (objArg == null) {
                        throw new Exception("Unable to execute function");
                    }
                    this.tokenPostion++;
                    eatComments(pobjExpression);
                    if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                        this.tokenPostion++;
                    }
                    if (!objArg.booleanValue()) {
                        handleControlKeywordAND = new BooleanProperty(false);
                        eatComments(pobjExpression);
                        while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                            eatComments(pobjExpression);
                            eatParameter(pobjExpression, penmPurpose);
                            this.tokenPostion++;
                            eatComments(pobjExpression);
                            if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                                this.tokenPostion++;
                            }
                        }
                    }
                }
            } else if (penmPurpose.equals(zXType.exprPurpose.epDescribe)) {
                StringBuffer strTmp = new StringBuffer();
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                    eatComments(pobjExpression);
                    objArg = go(pobjExpression, penmPurpose);
                    if (objArg == null) {
                        throw new Exception("Unable to execute function");
                    }
                    if (strTmp.length() == 0) {
                        strTmp.append(" ( ").append(objArg.getStringValue()).append(" ) ");
                    } else {
                        strTmp.append(" and (").append(objArg.getStringValue()).append(" ) ");
                    }
                    this.tokenPostion++;
                    eatComments(pobjExpression);
                    if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                        this.tokenPostion++;
                    }
                }
                handleControlKeywordAND = new StringProperty("[control] " + strTmp.toString());
            } else if (penmPurpose.equals(zXType.exprPurpose.epAPI)) {
                handleControlKeywordAND = new StringProperty("bln,bln,bln,...");
            }
            return handleControlKeywordAND;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Handle the _and control keyword.", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pobjExpression = " + pobjExpression);
                getZx().log.error("Parameter : penmPurpose = " + penmPurpose);
            }
            if (getZx().throwException) throw new ZXException(e);
            return handleControlKeywordAND;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(handleControlKeywordAND);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Handle the _choose control keyword.
     * 
     * <pre>
     * 
     * Reviewed for V1.5:73
     * Reviewed for V1.5:78
     * </pre>
     * 
     * @param pobjExpression value is to be used 
     * @param penmPurpose Purpose of handling expression 
     * @return Returns the resultant property
     * @throws ZXException Thrown if handleControlKeywordCHOOSE fails. 
     */
    private Property handleControlKeywordCHOOSE(Expression pobjExpression, zXType.exprPurpose penmPurpose) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjExpression", pobjExpression);
            getZx().trace.traceParam("penmPurpose", penmPurpose);
        }
        Property handleControlKeywordCHOOSE = new StringProperty("", true);
        try {
            Property objArg;
            int intArg = 0;
            if (penmPurpose.equals(zXType.exprPurpose.epEval)) {
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                if (!isValueTokenType(peekTokenType(pobjExpression.getTokens(), this.tokenPostion))) {
                    String strErrorToken = this.tokenPostion < pobjExpression.getTokens().size() ? ((ExprToken) pobjExpression.getTokens().get(this.tokenPostion)).getToken() : "";
                    throw new ExpressionParseException("Cannot find first value for _choose function", strErrorToken, this.tokenPostion);
                }
                objArg = go(pobjExpression, penmPurpose);
                if (objArg == null) {
                    throw new Exception("Unable to evaluate first value for _choose function");
                }
                this.tokenPostion++;
                eatComments(pobjExpression);
                if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                    this.tokenPostion++;
                }
                int intFirstValue = objArg.intValue();
                if (intFirstValue < 1) {
                    throw new Exception("The first value of a choose function should be greater than 0.");
                }
                while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                    intArg++;
                    if (intArg == intFirstValue) {
                        handleControlKeywordCHOOSE = go(pobjExpression, penmPurpose);
                        if (handleControlKeywordCHOOSE == null) {
                            throw new Exception("Unable to execute function");
                        }
                    } else {
                        eatParameter(pobjExpression, penmPurpose);
                    }
                    this.tokenPostion++;
                    eatComments(pobjExpression);
                    if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                        this.tokenPostion++;
                    }
                }
            } else if (penmPurpose.equals(zXType.exprPurpose.epDescribe)) {
                StringBuffer strTmp = new StringBuffer();
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                    intArg++;
                    objArg = go(pobjExpression, penmPurpose);
                    if (objArg == null) {
                        throw new Exception("Unable to execute function");
                    }
                    if (intArg == 1) {
                        strTmp.append("get value number ( ").append(objArg.getStringValue()).append(" ) of ");
                    } else {
                        strTmp.append(" ( ").append(objArg.getStringValue()).append(" ) ");
                    }
                    this.tokenPostion++;
                    eatComments(pobjExpression);
                    if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                        this.tokenPostion++;
                    }
                }
                handleControlKeywordCHOOSE = new StringProperty("[control] " + strTmp.toString());
            } else if (penmPurpose.equals(zXType.exprPurpose.epAPI)) {
                handleControlKeywordCHOOSE = new StringProperty("bln,bln|dat|str|int,bln,bln|dat|str|int,...");
            }
            return handleControlKeywordCHOOSE;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Handle the _choose control keyword.", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pobjExpression = " + pobjExpression);
                getZx().log.error("Parameter : penmPurpose = " + penmPurpose);
            }
            if (getZx().throwException) throw new ZXException(e);
            return handleControlKeywordCHOOSE;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(handleControlKeywordCHOOSE);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Handle the _if control keyword.
     * 
     * <pre>
     * 
     * Reviewed for V1.5:73
     * </pre>
     * 
     * @param pobjExpression value is to be used 
     * @param penmPurpose Purpose of handling expression 
     * @return Returns the resultant property
     * @throws ZXException Thrown if handleControlKeywordIF fails. 
     */
    private Property handleControlKeywordIF(Expression pobjExpression, zXType.exprPurpose penmPurpose) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjExpression", pobjExpression);
            getZx().trace.traceParam("penmPurpose", penmPurpose);
        }
        Property handleControlKeywordIF = null;
        try {
            Property objArg1;
            if (penmPurpose.equals(zXType.exprPurpose.epEval)) {
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                objArg1 = go(pobjExpression, penmPurpose);
                if (objArg1 == null) {
                    throw new Exception("Unable to evaluate first value for _if function");
                }
                if (!objArg1.booleanValue()) {
                    this.tokenPostion = this.tokenPostion + 2;
                    eatParameter(pobjExpression, penmPurpose);
                }
                if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion + 1).pos != zXType.exprTokenType.ettNextParm.pos) {
                    String strErrorToken = ((ExprToken) pobjExpression.getTokens().get(this.tokenPostion + 1)).getToken();
                    throw new ExpressionParseException("Expected parameter not found for _if (should have 3 parameters)", strErrorToken, this.tokenPostion + 1);
                }
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                handleControlKeywordIF = go(pobjExpression, penmPurpose);
                if (handleControlKeywordIF == null) {
                    throw new Exception("Unable to evaluate parameter for _if control");
                }
                if (objArg1.booleanValue()) {
                    this.tokenPostion = this.tokenPostion + 2;
                    eatParameter(pobjExpression, penmPurpose);
                }
                this.tokenPostion++;
            } else if (penmPurpose.equals(zXType.exprPurpose.epDescribe)) {
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                objArg1 = go(pobjExpression, penmPurpose);
                if (objArg1 == null) {
                    throw new Exception("Unable to evaluate parameter 1 for _if control");
                }
                if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion + 1).pos != zXType.exprTokenType.ettNextParm.pos) {
                    String strErrorToken = ((ExprToken) pobjExpression.getTokens().get(this.tokenPostion + 1)).getToken();
                    throw new ExpressionParseException("3 parameters required for _if control", strErrorToken, this.tokenPostion + 1);
                }
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                Property objArg2 = go(pobjExpression, penmPurpose);
                if (objArg2 == null) {
                    throw new Exception("Unable to evaluate parameter 2 for _if control");
                }
                if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion + 1).pos != zXType.exprTokenType.ettNextParm.pos) {
                    String strErrorToken = ((ExprToken) pobjExpression.getTokens().get(this.tokenPostion + 1)).getToken();
                    throw new ExpressionParseException("3 parameters required for _if control", strErrorToken, this.tokenPostion + 1);
                }
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                Property objArg3 = go(pobjExpression, penmPurpose);
                if (objArg3 == null) {
                    throw new Exception("Unable to evaluate parameter 2 for _if control");
                }
                handleControlKeywordIF = new StringProperty("[control] if (" + objArg1.getStringValue() + ") then (" + objArg2.getStringValue() + ") else (" + objArg3.getStringValue() + ")");
                this.tokenPostion++;
            } else if (penmPurpose.equals(zXType.exprPurpose.epAPI)) {
                handleControlKeywordIF = new StringProperty("bln,str|int|dat|bln|dbl,str|int|dat|bln|dbl");
            }
            return handleControlKeywordIF;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Handle the _if control keyword.", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pobjExpression = " + pobjExpression);
                getZx().log.error("Parameter : penmPurpose = " + penmPurpose);
            }
            if (getZx().throwException) throw new ZXException(e);
            return handleControlKeywordIF;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(handleControlKeywordIF);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Handle the _loopover control keyword.
     * 
     * <pre>
     * 
     * Reviewed for V1.5:73
     * </pre>
     * 
     * @param pobjExpression value is to be used 
     * @param penmPurpose Purpose of handling expression 
     * @return Returns the resultant property
     * @throws ZXException Thrown if handleControlKeywordLOOPOVER fails. 
     */
    private Property handleControlKeywordLOOPOVER(Expression pobjExpression, zXType.exprPurpose penmPurpose) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjExpression", pobjExpression);
            getZx().trace.traceParam("penmPurpose", penmPurpose);
        }
        Property handleControlKeywordLOOPOVER = null;
        try {
            if (penmPurpose.equals(zXType.exprPurpose.epEval)) {
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                handleControlKeywordLOOPOVER = go(pobjExpression, penmPurpose);
                if (handleControlKeywordLOOPOVER == null) {
                    throw new Exception("Unable to evaluate first value for _loopover function");
                }
                ExprFHI objIterator = getIterator(handleControlKeywordLOOPOVER.getStringValue());
                if (objIterator == null) {
                    throw new Exception("Unable to find iterator");
                }
                this.tokenPostion = this.tokenPostion + 2;
                objIterator.reset();
                objIterator.doNext();
                while (objIterator.hasNext()) {
                    eatComments(pobjExpression);
                    while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                        eatComments(pobjExpression);
                        handleControlKeywordLOOPOVER = go(pobjExpression, penmPurpose);
                        if (handleControlKeywordLOOPOVER == null) {
                            throw new Exception("Unable to execute function");
                        }
                        this.tokenPostion++;
                        eatComments(pobjExpression);
                        if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                            this.tokenPostion++;
                        }
                    }
                    objIterator.doNext();
                }
                this.tokenPostion++;
            } else if (penmPurpose.equals(zXType.exprPurpose.epDescribe)) {
                this.tokenPostion++;
            } else if (penmPurpose.equals(zXType.exprPurpose.epAPI)) {
                handleControlKeywordLOOPOVER = new StringProperty("str,str|int|dat|bln|dbl,str|int|dat|bln|dbl");
            }
            return handleControlKeywordLOOPOVER;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Handle the _loopover control keyword.", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pobjExpression = " + pobjExpression);
                getZx().log.error("Parameter : penmPurpose = " + penmPurpose);
            }
            if (getZx().throwException) throw new ZXException(e);
            return handleControlKeywordLOOPOVER;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(handleControlKeywordLOOPOVER);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Handle the _or control keyword.
     * 
     * <pre>
     * 
     * Reviewed for V1.5:73
     * </pre>
     * 
     * @param pobjExpression value is to be used 
     * @param penmPurpose Purpose of handling expression 
     * @return Returns the resultant property
     * @throws ZXException Thrown if handleControlKeywordOR fails. 
     */
    private Property handleControlKeywordOR(Expression pobjExpression, zXType.exprPurpose penmPurpose) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjExpression", pobjExpression);
            getZx().trace.traceParam("penmPurpose", penmPurpose);
        }
        Property handleControlKeywordOR = null;
        try {
            Property objArg;
            if (penmPurpose.equals(zXType.exprPurpose.epEval)) {
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                handleControlKeywordOR = new BooleanProperty(false);
                while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                    eatComments(pobjExpression);
                    objArg = go(pobjExpression, penmPurpose);
                    if (objArg == null) {
                        throw new Exception("Unable to execute function");
                    }
                    this.tokenPostion++;
                    eatComments(pobjExpression);
                    if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                        this.tokenPostion++;
                    }
                    if (objArg.booleanValue()) {
                        handleControlKeywordOR = new BooleanProperty(true);
                        eatComments(pobjExpression);
                        while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                            eatParameter(pobjExpression, penmPurpose);
                            this.tokenPostion++;
                            eatComments(pobjExpression);
                            if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                                this.tokenPostion++;
                            }
                        }
                    }
                }
            } else if (penmPurpose.equals(zXType.exprPurpose.epDescribe)) {
                StringBuffer strTmp = new StringBuffer();
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                    eatComments(pobjExpression);
                    objArg = go(pobjExpression, penmPurpose);
                    if (objArg == null) {
                        throw new Exception("Unable to execute function");
                    }
                    if (strTmp.length() == 0) {
                        strTmp.append(" ( ").append(objArg.getStringValue()).append(" ) ");
                    } else {
                        strTmp.append(" or (").append(objArg.getStringValue()).append(" ) ");
                    }
                    this.tokenPostion++;
                    eatComments(pobjExpression);
                    if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                        this.tokenPostion++;
                    }
                }
                handleControlKeywordOR = new StringProperty("[control] " + strTmp.toString());
            } else if (penmPurpose.equals(zXType.exprPurpose.epAPI)) {
                handleControlKeywordOR = new StringProperty("bln,bln,bln,...");
            }
            return handleControlKeywordOR;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Handle the _or control keyword.", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pobjExpression = " + pobjExpression);
                getZx().log.error("Parameter : penmPurpose = " + penmPurpose);
            }
            if (getZx().throwException) throw new ZXException(e);
            return handleControlKeywordOR;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(handleControlKeywordOR);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Handle the _select control keyword.
     * 
     * <pre>
     * 
     * Reviewed for V1.5:73
     * </pre>
     * 
     * @param pobjExpression value is to be used 
     * @param penmPurpose Purpose of handling expression 
     * @return Returns the resultant property
     * @throws ZXException Thrown if handleControlKeywordSELECT fails. 
     */
    private Property handleControlKeywordSELECT(Expression pobjExpression, zXType.exprPurpose penmPurpose) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjExpression", pobjExpression);
            getZx().trace.traceParam("penmPurpose", penmPurpose);
        }
        Property handleControlKeywordSELECT = null;
        try {
            Property objArg;
            int intArg = 0;
            if (penmPurpose.equals(zXType.exprPurpose.epEval)) {
                this.tokenPostion = tokenPostion + 2;
                eatComments(pobjExpression);
                handleControlKeywordSELECT = new BooleanProperty(false);
                while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                    intArg++;
                    eatComments(pobjExpression);
                    objArg = go(pobjExpression, penmPurpose);
                    if (objArg == null) {
                        throw new Exception("Unable to execute function");
                    }
                    this.tokenPostion++;
                    eatComments(pobjExpression);
                    if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                        this.tokenPostion++;
                    }
                    if (objArg.booleanValue()) {
                        if (!isValueTokenType(peekTokenType(pobjExpression.getTokens(), this.tokenPostion))) {
                            throw new Exception("No value found associated with expression (argument " + intArg + ")");
                        }
                        eatComments(pobjExpression);
                        handleControlKeywordSELECT = go(pobjExpression, penmPurpose);
                        if (handleControlKeywordSELECT == null) {
                            throw new Exception("Unable to execute function");
                        }
                        while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                            eatParameter(pobjExpression, penmPurpose);
                            this.tokenPostion++;
                            eatComments(pobjExpression);
                            if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                                this.tokenPostion++;
                            }
                        }
                    }
                }
            } else if (penmPurpose.equals(zXType.exprPurpose.epDescribe)) {
                StringBuffer strTmp = new StringBuffer();
                this.tokenPostion = this.tokenPostion + 2;
                eatComments(pobjExpression);
                while (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos != zXType.exprTokenType.ettEndParmList.pos) {
                    intArg++;
                    if (intArg > 1) {
                        strTmp.append(" else ");
                    }
                    eatComments(pobjExpression);
                    objArg = go(pobjExpression, penmPurpose);
                    if (objArg == null) {
                        throw new Exception("Unable to execute function");
                    }
                    strTmp.append("if ( ").append(objArg.getStringValue()).append(" ) ");
                    this.tokenPostion++;
                    eatComments(pobjExpression);
                    if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                        this.tokenPostion++;
                    }
                    if (!isValueTokenType(peekTokenType(pobjExpression.getTokens(), this.tokenPostion))) {
                        throw new Exception("No value found associated with expression (argument " + intArg + ")");
                    }
                    objArg = go(pobjExpression, penmPurpose);
                    if (objArg == null) {
                        throw new Exception("Unable to execute function");
                    }
                    strTmp.append(" then ( ").append(objArg.getStringValue()).append(" ) ");
                    this.tokenPostion++;
                    eatComments(pobjExpression);
                    if (peekTokenType(pobjExpression.getTokens(), this.tokenPostion).pos == zXType.exprTokenType.ettNextParm.pos) {
                        this.tokenPostion++;
                    }
                }
                handleControlKeywordSELECT = new StringProperty("[control] " + strTmp.toString());
            } else if (penmPurpose.equals(zXType.exprPurpose.epAPI)) {
                handleControlKeywordSELECT = new StringProperty("bln,bln|dat|str|int,bln,bln|dat|str|int,...");
            }
            return handleControlKeywordSELECT;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Handle the _select control keyword.", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pobjExpression = " + pobjExpression);
                getZx().log.error("Parameter : penmPurpose = " + penmPurpose);
            }
            if (getZx().throwException) throw new ZXException(e);
            return handleControlKeywordSELECT;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(handleControlKeywordSELECT);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
	 * Nibble away a parameter without doing anything with it.
     *  
     * This is used by control keywords that. have been introduced 
     * specially to allow parts of a script to be ignored if certain conditions are met
	 *
	 * @param pobjExpression value is to be used 
	 * @param penmPurpose Purpose of handling expression 
	 * @return Returns the return code of the method. 
	 * @throws ZXException Thrown if eatParameter fails. 
	 */
    private zXType.rc eatParameter(Expression pobjExpression, zXType.exprPurpose penmPurpose) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pobjExpression", pobjExpression);
            getZx().trace.traceParam("penmPurpose", penmPurpose);
        }
        zXType.rc eatParameter = zXType.rc.rcOK;
        try {
            eatComments(pobjExpression);
            ArrayList colTokens = pobjExpression.getTokens();
            ExprToken objToken = (ExprToken) colTokens.get(this.tokenPostion);
            if (objToken.getTokenType().pos == zXType.exprTokenType.ettId.pos || objToken.getTokenType().pos == zXType.exprTokenType.ettExternalId.pos) {
                if (expectToken("(", colTokens, this.tokenPostion + 1).pos == zXType.rc.rcError.pos) {
                    eatParameter = zXType.rc.rcError;
                    return eatParameter;
                }
                this.tokenPostion = this.tokenPostion + 2;
                while (peekToken(colTokens, this.tokenPostion).charAt(0) != ')') {
                    if (eatParameter(pobjExpression, penmPurpose).pos != zXType.rc.rcOK.pos) {
                        eatParameter = zXType.rc.rcError;
                        return eatParameter;
                    }
                    this.tokenPostion++;
                    if (peekToken(colTokens, this.tokenPostion).charAt(0) == ',') {
                        this.tokenPostion++;
                    }
                }
            } else {
            }
            eatComments(pobjExpression);
            return eatParameter;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Nibble away a parameter without doing anything with it. This is used by control keywords that", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pobjExpression = " + pobjExpression);
                getZx().log.error("Parameter : penmPurpose = " + penmPurpose);
            }
            if (getZx().throwException) throw new ZXException(e);
            eatParameter = zXType.rc.rcError;
            return eatParameter;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(eatParameter);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
	 * Eat away any comment tokens.
	 * 
	 * @param pobjExpression A handle to the expression.
	 * @return Returns the return code of the method.
	 */
    private zXType.rc eatComments(Expression pobjExpression) {
        zXType.rc eatComments = zXType.rc.rcOK;
        ExprToken objToken;
        ArrayList colTokens = pobjExpression.getTokens();
        while (this.tokenPostion < colTokens.size()) {
            objToken = (ExprToken) colTokens.get(this.tokenPostion);
            if (objToken.getTokenType().pos != zXType.exprTokenType.ettComment.pos) {
                return eatComments;
            }
            this.tokenPostion++;
        }
        return eatComments;
    }

    /**
	 * Resolve a user defined function.
     * This is normally stored in the database.
	 *
	 * @param pstrFunction Name of the function 
	 * @param pcolArgs Collection of arguments 
	 * @param penmPurpose Purpose of call 
	 * @return Returns the resultant property.
	 * @throws ZXException Thrown if resolveUsrFunction fails. 
	 */
    public Property resolveUsrFunction(String pstrFunction, ArrayList pcolArgs, zXType.exprPurpose penmPurpose) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrFunction", pstrFunction);
            getZx().trace.traceParam("pcolArgs", pcolArgs);
            getZx().trace.traceParam("penmPurpose", penmPurpose);
        }
        Property resolveUsrFunction = new StringProperty("", false);
        try {
            setContext("usrNumberP", new LongProperty(pcolArgs.size(), false));
            for (int i = 0; i < pcolArgs.size(); i++) {
                setContext("usrP" + i, (Property) pcolArgs.get(i));
            }
            ZXBO objDfntn = getZx().createBO("zXUsrFnctnDfntn");
            if (objDfntn == null) {
                throw new Exception("Failed to creagte business object : zXUsrFnctnDfntn");
            }
            objDfntn.setValue("nme", new StringProperty(pstrFunction));
            objDfntn.setValue("nmbrOfPrmtrs", new LongProperty(pcolArgs.size(), false));
            if (objDfntn.loadBO("*", "nme,nmbrOfPrmtrs", false).pos != zXType.rc.rcError.pos) {
                getZx().trace.addError("Unable to load function definition", pstrFunction + " with " + pcolArgs.size() + " parameters");
                throw new ExpressionParseException("Failed to load function definition", pstrFunction, this.tokenPostion);
            }
            if (penmPurpose.equals(zXType.exprPurpose.epEval)) {
                resolveUsrFunction = eval(objDfntn.getValue("dfntn").getStringValue());
            } else if (penmPurpose.equals(zXType.exprPurpose.epDescribe)) {
                Property objDcrbe = objDfntn.getValue("dscrbe");
                if (objDcrbe.isNull) {
                    resolveUsrFunction = new StringProperty(describe(objDcrbe.getStringValue()));
                } else {
                    resolveUsrFunction = eval(objDcrbe.getStringValue());
                }
            } else {
                resolveUsrFunction = objDfntn.getValue("api");
            }
            return resolveUsrFunction;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Resolve a user defined function.", e);
            if (getZx().log.isErrorEnabled()) {
                getZx().log.error("Parameter : pstrFunction = " + pstrFunction);
                getZx().log.error("Parameter : pcolArgs = " + pcolArgs);
                getZx().log.error("Parameter : penmPurpose =s " + penmPurpose);
            }
            if (getZx().throwException) throw new ZXException(e);
            return resolveUsrFunction;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(resolveUsrFunction);
                getZx().trace.exitMethod();
            }
        }
    }
}
