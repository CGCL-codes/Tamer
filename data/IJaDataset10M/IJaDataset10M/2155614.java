package com.butterfill.opb.plsql.translation;

import static com.butterfill.opb.plsql.translation.OpbComment.*;
import com.butterfill.opb.util.OpbToStringHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a PL/SQL call.
 * <br/>
 * This class is not intended for use outside the translation package.
 * 
 * @author Peter Butterfill
 */
class PlsqlCall {

    /**
     * The name of this class.
     */
    public static final String CLASS_NAME = PlsqlCall.class.getName();

    /**
     * The logger for this class.
     */
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    /**
     * The translastion helper used by this class.
     */
    private final PlsqlTranslationHelper translationHelper = new PlsqlTranslationHelper();

    /**
     * The Opb comment associated with this call.
     */
    private OpbComment opbComment;

    /**
     * The comment for this call.
     */
    private List<String> commentLines;

    /**
     * The Java name of this call.
     */
    private String name;

    /**
     * The SQL name of this call.
     */
    private String sqlName;

    /**
     * Set to true if this call is a function.
     */
    private boolean function;

    /**
     * Set to true if this call is a procedure.
     */
    private boolean procedure;

    /**
     * The parameters of this call.
     * The return parameter will never be in this list.
     */
    private final List<PlsqlCallParameter> params = new ArrayList<PlsqlCallParameter>();

    /**
     * The return value.
     */
    private PlsqlCallParameter returnValue;

    /**
     * Set to true if we should clear all cached data objects following a
     * successfull call.
     */
    private boolean clearCachedAll;

    /**
     * Set to true if we should clear this object from cache following a
     * successfull call.
     */
    private boolean clearCachedThis;

    /**
     * List of classes we should clear from cache following a
     * successfull call.
     */
    private final List<String> clearCached = new ArrayList<String>();

    /**
     * Set to true if we should invalidate all cached data objects following a
     * successfull call.
     */
    private boolean invalidateCachedAll;

    /**
     * Set to true if we should invalidate this object following a
     * successfull call.
     */
    private boolean invalidateCachedThis;

    /**
     * List of classes we should invalidate following a
     * successfull call.
     */
    private final List<String> invalidateCached = new ArrayList<String>();

    /**
     * Creates a new PlsqlCall.
     */
    public PlsqlCall() {
    }

    /**
     * Returns a String representation of this PlsqlCall and it's values.
     * @return String representation of this PlsqlCall
     */
    @Override
    public String toString() {
        return OpbToStringHelper.toString(this);
    }

    /**
     * Sets the Opb comment for this call.
     * @param comment An Opb comment.
     */
    public void setOpbComment(final OpbComment comment) {
        this.opbComment = comment;
    }

    /**
     * Returns the comment for this call.
     * @return The comment for this call.
     */
    public List<String> getCommentLines() {
        return commentLines;
    }

    /**
     * Sets the comment for this call.
     * @param commentLines The comment for this call.
     */
    public void setCommentLines(final List<String> commentLines) {
        this.commentLines = commentLines;
    }

    /**
     * Returns the Java name of this call.
     * @return the Java name of this call.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the SQL name of this call.
     * @return the SQL name of this call.
     */
    public String getSqlName() {
        return sqlName;
    }

    /**
     * Sets the SQL name of this call.
     * @param sqlName The SQL name of this call.
     */
    public void setSqlName(final String sqlName) {
        final String methodName = "setSqlName(String)";
        logger.entering(CLASS_NAME, methodName);
        logger.logp(Level.FINER, CLASS_NAME, methodName, "sqlName={0}", sqlName);
        this.sqlName = sqlName;
        this.name = translationHelper.toJavaMemberName(sqlName);
    }

    /**
     * Returns true if this call is a function, false otehrwise.
     * @return true if this call is a function.
     */
    public boolean isFunction() {
        return function;
    }

    /**
     * Set to true if this call is a function, false otehrwise.
     * @param function true if this call is a function.
     */
    public void setFunction(final boolean function) {
        this.function = function;
    }

    /**
     * Returns true if this call is a procedure, false otehrwise.
     * @return true if this call is a procedure.
     */
    public boolean isProcedure() {
        return procedure;
    }

    /**
     * Set to true if this call is a procedure, false otehrwise.
     * @param procedure true if this call is a procedure.
     */
    public void setProcedure(final boolean procedure) {
        this.procedure = procedure;
    }

    /**
     * Returns the return parameter of this call.
     * @return The return parameter of this call.
     */
    public PlsqlCallParameter getReturn() {
        return returnValue;
    }

    /**
     * Returns the parameters of this call.
     * @return The parameters of this call.
     */
    public List<PlsqlCallParameter> getParams() {
        return params;
    }

    /**
     * Returns the parameters of this call that have been mapped (by Opb comment
     * information).
     * @return The parameters of this call that have been mapped.
     */
    public List<PlsqlCallParameter> getMappedParams() {
        List<PlsqlCallParameter> result = new ArrayList<PlsqlCallParameter>();
        for (PlsqlCallParameter p : params) {
            if (p.getMappedTo() != null) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Returns the parameters of this call that have not been mapped .
     * @return The parameters of this call that have not been mapped.
     */
    public List<PlsqlCallParameter> getUnMappedParams() {
        List<PlsqlCallParameter> result = new ArrayList<PlsqlCallParameter>();
        for (PlsqlCallParameter p : params) {
            if (p.getMappedTo() == null) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Returns true if at least one parameter has been mapped 
     * (by Opb comment information), false otherwise.
     * @return true if at least one parameter has been mapped.
     */
    public boolean getAtLeastOneParamIsMapped() {
        for (PlsqlCallParameter p : params) {
            if (p.getMappedTo() != null) {
                return true;
            }
        }
        return function && returnValue.getMappedTo() != null;
    }

    /**
     * Completes the configuration of the specified parameter.
     * This includes setting the Java name of the parameter and other attributes
     * read from appropriate Opb comment elements.
     * @param p The parameter to process.
     */
    private void processParam(final PlsqlCallParameter p) {
        final String methodName = "processParam(PlsqlCallParameter)";
        logger.entering(CLASS_NAME, methodName);
        if (opbComment == null) {
            return;
        }
        for (Map<String, String> map : opbComment.getElements()) {
            String type = map.get(OPB_COMMENT_ELEMENT_TYPE_KEY);
            String paramName = map.get("name");
            if ("param".equals(type) && p.getSqlName().equalsIgnoreCase(paramName)) {
                opbComment.applyElement(map, p);
            }
        }
    }

    /**
     * Adds a parameter to this call.
     * @param p The parameter to add.
     */
    public void addParameter(final PlsqlCallParameter p) {
        params.add(p);
        p.setIndex(params.size() + ((function) ? 1 : 0));
        processParam(p);
    }

    /**
     * Creates a parameter to hold return value information and sets it's type.
     * @param sqlReturnType The SQL type returned by this function.
     */
    public void setSqlReturnType(final String sqlReturnType) {
        final String methodName = "setSqlReturnType(String)";
        logger.entering(CLASS_NAME, methodName);
        logger.logp(Level.FINER, CLASS_NAME, methodName, "sqlReturnType={0}", sqlReturnType);
        returnValue = new PlsqlCallParameter("RETURN", sqlReturnType, false, false);
        returnValue.setIndex(1);
        processParam(returnValue);
    }

    /**
     * Returns true if we should clear all cached data objects following a
     * successfull call.
     *
     * @return true if we should clear all cached data objects following a
     * successfull call.
     */
    public boolean isClearCachedAll() {
        return clearCachedAll;
    }

    /**
     * Returns true if we should clear this object from cache following a
     * successfull call.
     * 
     * @return true if we should clear this object from cache following a
     * successfull call.
     */
    public boolean isClearCachedThis() {
        return !isClearCachedAll() && clearCachedThis;
    }

    /**
     * Returns classes we should clear from cache following a
     * successfull call.
     * 
     * @return Classes we should clear from cache following a
     * successfull call.
     */
    public List<String> getClearCached() {
        return (isClearCachedAll()) ? null : clearCached;
    }

    /**
     * Returns true if we should invalidate all cached data objects following a
     * successfull call.
     *
     * @return true if we should invalidate all cached data objects following a
     * successfull call.
     */
    public boolean isInvalidateCachedAll() {
        return !isClearCachedAll() && invalidateCachedAll;
    }

    /**
     * Returns true if we should invalidate this object following a
     * successfull call.
     * 
     * @return true if we should invalidate this object following a
     * successfull call.
     */
    public boolean isInvalidateCachedThis() {
        return !isInvalidateCachedAll() && !isClearCachedThis() && invalidateCachedThis;
    }

    /**
     * Returns classes we should invalidate following a
     * successfull call.
     * 
     * @return Classes we should invalidate following a
     * successfull call.
     */
    public List<String> getInvalidateCached() {
        return (isClearCachedAll() || isInvalidateCachedAll()) ? null : invalidateCached;
    }

    /**
     * Returns true if this call has at least one out parameter, false 
     * otherwise.
     * @return true if this call has at least one out parameter, false 
     * otherwise.
     */
    private boolean isAnyParamOut() {
        for (PlsqlCallParameter p : params) {
            if (p.getOut()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate the use of the data object cache for the specified parameter.
     * This method applies default values if use data object cache has not yet 
     * been set.
     * @param param A parameter of this call (could be RETURN).
     */
    private void validateUseDataObjectCache(final PlsqlCallParameter param) {
        final String methodName = "validateUseDataObjectCache(PlsqlCallParameter)";
        logger.entering(CLASS_NAME, methodName);
        if (param.isUseDataObjectCache() == null) {
            param.setUseDataObjectCache(param.isCursor() && param.getSqlDatatype().indexOf("?") != -1);
        } else if (param.isUseDataObjectCache()) {
            if (!param.isCursor()) {
                param.setUseDataObjectCache(false);
                logger.logp(Level.SEVERE, CLASS_NAME, methodName, "Non-cursor results cannot use the data object " + "cache. Parameter '" + param.getSqlName() + "' of call '" + sqlName + "' will not use the data object cache");
            } else {
                if (param.getSqlDatatype().indexOf("?") == -1) {
                    param.setUseDataObjectCache(false);
                    logger.logp(Level.SEVERE, CLASS_NAME, methodName, "Cursor results that do not specify which " + "type of object the cursor will contain " + "cannot use the data object cache. Parameter '" + param.getSqlName() + "' of call '" + sqlName + "' will not use the data object cache");
                }
            }
        }
    }

    /**
     * Validate the use of the result cache for the specified parameter.
     * This method applies default values if use result cache has not yet 
     * been set.
     * @param param A parameter of this call (could be RETURN).
     */
    private void validateUseResultCache(final PlsqlCallParameter param) {
        final String methodName = "validateUseResultCache(PlsqlCallParameter)";
        logger.entering(CLASS_NAME, methodName);
        if (param.isUseResultCache() == null) {
            param.setUseResultCache(param.isCursor() && param.isReturn() && !isAnyParamOut());
        } else if (param.isUseResultCache()) {
            if (param.isReturn()) {
                if (!param.isCursor()) {
                    param.setUseResultCache(false);
                    logger.logp(Level.SEVERE, CLASS_NAME, methodName, "The return value of '" + sqlName + "' cannot use the result cache as it is not of " + "cursor type");
                }
                if (isAnyParamOut()) {
                    param.setUseResultCache(false);
                    logger.logp(Level.SEVERE, CLASS_NAME, methodName, "Functions that use OUT parameters cannot use the" + " result cache. The return value of '" + sqlName + "' will not use the result cache");
                }
            } else {
                param.setUseResultCache(false);
                logger.logp(Level.SEVERE, CLASS_NAME, methodName, "Parameter '" + param.getSqlName() + "' of call '" + sqlName + "' is configured to use the result cache." + " Only RETURN can use the result cache. This " + "parameter will not use the result cache");
            }
        }
    }

    /**
     * Validate the use of the scalar result cache for the specified parameter.
     * This method applies default values if use scalar result cache has not yet 
     * been set.
     * @param param A parameter of this call (could be RETURN).
     */
    private void validateUseScalarResultCache(final PlsqlCallParameter param) {
        final String methodName = "validateUseScalarResultCache(PlsqlCallParameter)";
        logger.entering(CLASS_NAME, methodName);
        if (param.isUseScalarResultCache() == null) {
            param.setUseScalarResultCache(false);
        } else if (param.isUseScalarResultCache()) {
            if (param.isReturn()) {
                if (param.isCursor()) {
                    param.setUseScalarResultCache(false);
                    logger.logp(Level.SEVERE, CLASS_NAME, methodName, "The return value of '" + sqlName + "' cannot use the scalar result cache as it is " + "a cursor");
                }
                if (isAnyParamOut()) {
                    param.setUseScalarResultCache(false);
                    logger.logp(Level.SEVERE, CLASS_NAME, methodName, "Functions that use OUT parameters cannot use the" + " scalar result cache. The return value of '" + sqlName + "' will not use the scalar result cache");
                }
            } else {
                param.setUseScalarResultCache(false);
                logger.logp(Level.SEVERE, CLASS_NAME, methodName, "Parameter '" + param.getSqlName() + "' of call '" + sqlName + "' is configured to use the scalar result " + "cache. Only RETURN can use the scalar result cache. " + "This parameter will not use the scalar result cache");
            }
        }
    }

    /**
     * Validates the datatype of a parameter, returning true if the parameter
     * is ok, false otherwise.
     * @param param A parameter of this call (could be RETURN).
     * @return true if the parameter is ok, false otherwise.
     */
    private boolean validateDatatype(final PlsqlCallParameter param) {
        final String methodName = "validateDatatype(PlsqlCallParameter)";
        logger.entering(CLASS_NAME, methodName);
        if (param.getDatatype() == null || (param.getOut() && param.getWrappedDatatype() == null)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to translate datatype '");
            sb.append(param.getSqlDatatype());
            sb.append("' of '");
            sb.append(param.getSqlName());
            sb.append("' in call '");
            sb.append(sqlName);
            sb.append("'. ");
            if (!param.getOriginalSqlDatatype().equals(param.getSqlDatatype())) {
                sb.append("The original datatype '");
                sb.append(param.getOriginalSqlDatatype());
                sb.append("' was overridden by an opb comment. ");
            }
            sb.append("This call will be ignored");
            logger.logp(Level.SEVERE, CLASS_NAME, methodName, sb.toString());
            return false;
        }
        return true;
    }

    /**
     * Validates this call returning false if this call is not valid.
     * <br/>
     * This method pulls clear_cached and invalidate_cached properties from the
     * Opb comment and checks that;
     * <ul>
     * <li>All datatypes could be translated,</li>
     * <li>Cursors are not used as in parameters,</li>
     * <li>Arrays are not used as out parameters unless they are passing
     * binary data,</li>
     * <li>The element types of the Opb comment are valid (clear_cached, 
     * invalidate_cached or param) and</li>
     * <li>All parameter mappings in the Opb comment have a corresponding 
     * parameter in this call.</li>
     * </ul>
     * 
     * @return true If this call is valid, false otherwise.
     */
    public boolean validate() {
        final String methodName = "validate()";
        logger.entering(CLASS_NAME, methodName);
        boolean result = true;
        if (function) {
            if (!validateDatatype(returnValue)) {
                return false;
            }
            if (returnValue.isPlsqlIndexTable()) {
                result = false;
                logger.logp(Level.SEVERE, CLASS_NAME, methodName, "PL/SQL index-by tables cannot be used as function " + "return values. Function '" + sqlName + "' will be ignored");
            }
            validateUseDataObjectCache(returnValue);
            validateUseResultCache(returnValue);
            validateUseScalarResultCache(returnValue);
            if (returnValue.isCursor() && returnValue.isUseScalarResultCache()) {
                returnValue.setUseScalarResultCache(false);
                logger.logp(Level.SEVERE, CLASS_NAME, methodName, "Non-scalar results cannot use the scalar result " + "cache. Function '" + sqlName + "' will not use the scalar result cache.");
            }
        }
        for (PlsqlCallParameter p : params) {
            if (!validateDatatype(p)) {
                return false;
            }
            validateUseDataObjectCache(p);
            validateUseResultCache(p);
            validateUseScalarResultCache(p);
            if (p.isCursor() && p.getIn()) {
                result = false;
                logger.logp(Level.SEVERE, CLASS_NAME, methodName, "Cursors cannot be used as IN parameters. Found '" + p.getSqlName() + "' in '" + sqlName + "'. This call will be ignored");
            }
            if (p.getOut() && p.isPlsqlIndexTable()) {
                result = false;
                logger.logp(Level.SEVERE, CLASS_NAME, methodName, "PL/SQL index-by tables cannot be used as out " + "parameters. Found '" + p.getSqlName() + "' in '" + sqlName + "'. This call will be ignored");
            }
        }
        if (opbComment == null) {
            logger.logp(Level.FINER, CLASS_NAME, methodName, "call has no Opb comment. returning");
            return result;
        }
        opbComment.checkElementTypes(new String[] { "clear_cached", "invalidate_cached", "param" });
        for (Map<String, String> element : opbComment.getElements()) {
            String type = element.get(OPB_COMMENT_ELEMENT_TYPE_KEY);
            String paramName = element.get("name");
            if ("clear_cached".equals(type)) {
                if ("all".equalsIgnoreCase(paramName)) {
                    clearCachedAll = true;
                } else if ("this".equalsIgnoreCase(paramName)) {
                    clearCachedThis = true;
                } else {
                    clearCached.add(translationHelper.toJavaClassName(paramName));
                }
            } else if ("invalidate_cached".equals(type)) {
                if ("all".equalsIgnoreCase(paramName)) {
                    invalidateCachedAll = true;
                } else if ("this".equalsIgnoreCase(paramName)) {
                    invalidateCachedThis = true;
                } else {
                    invalidateCached.add(translationHelper.toJavaClassName(paramName));
                }
            }
        }
        List<String> mappedParamNames = new ArrayList<String>();
        for (Map<String, String> map : opbComment.getElements()) {
            String type = map.get(OPB_COMMENT_ELEMENT_TYPE_KEY);
            if ("param".equals(type)) {
                String paramName = map.get("name");
                if (function && "RETURN".equalsIgnoreCase(paramName)) {
                    continue;
                }
                mappedParamNames.add(paramName);
            }
        }
        List<String> paramNames = new ArrayList<String>();
        for (PlsqlCallParameter p : params) {
            paramNames.add(p.getSqlName());
        }
        logger.logp(Level.FINER, CLASS_NAME, methodName, "pre-remove mappedParamNames={0}", mappedParamNames);
        logger.logp(Level.FINER, CLASS_NAME, methodName, "paramNames={0}", paramNames);
        mappedParamNames.removeAll(paramNames);
        logger.logp(Level.FINER, CLASS_NAME, methodName, "post-remove mappedParamNames={0}", mappedParamNames);
        for (String paramName : mappedParamNames) {
            logger.logp(Level.WARNING, CLASS_NAME, methodName, "{0} {1} but {2} does not have a parameter called {1}", new Object[] { "Found param mapping for", paramName, ((sqlName == null) ? "this call" : sqlName) });
        }
        return result;
    }
}
