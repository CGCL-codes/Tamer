package ch.ethz.mxquery.functions.xs;

import java.util.Vector;
import org.apache.xerces.impl.dv.xs.DayDV;
import org.apache.xerces.impl.dv.xs.MonthDV;
import org.apache.xerces.impl.dv.xs.MonthDayDV;
import org.apache.xerces.impl.dv.xs.YearDV;
import org.apache.xerces.impl.dv.xs.YearMonthDV;
import org.apache.xerces.xs.XSObjectList;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.MXQueryDate;
import ch.ethz.mxquery.datamodel.MXQueryDateTime;
import ch.ethz.mxquery.datamodel.MXQueryGregorian;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.types.TypeInfo;
import ch.ethz.mxquery.datamodel.xdm.GregorianToken;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.TypeException;
import ch.ethz.mxquery.functions.fn.DataValuesIterator;
import ch.ethz.mxquery.model.XDMIterator;

public class XSGregorian extends XSConstructorIterator {

    protected void init() throws MXQueryException {
        if (subIters[0] == null) {
            throw new IllegalArgumentException();
        }
        XDMIterator input = subIters[0];
        if (input instanceof DataValuesIterator) ((DataValuesIterator) input).setFnData(true);
        TokenInterface inputToken = input.next();
        int inputType = Type.getEventTypeSubstituted(inputToken.getEventType(), Context.getDictionary());
        checkParamType(inputType);
        if (resultType == inputType) {
            currentToken = inputToken;
        }
        XSObjectList facetsList = getFacetsList();
        int tz;
        MXQueryGregorian gVal = null;
        switch(inputType) {
            case Type.END_SEQUENCE:
                currentToken = Token.END_SEQUENCE_TOKEN;
                break;
            case Type.DATE_TIME:
                MXQueryDateTime dt = inputToken.getDateTime();
                if (dt.hasTimezone()) tz = dt.getTimezoneInMinutes(); else tz = MXQueryGregorian.UNDEFINED;
                gVal = new MXQueryGregorian(dt.getYear(), dt.getMonth(), dt.getDay(), tz, resultType);
                currentToken = new GregorianToken(null, gVal);
                if ((facetsList != null) && facetsList.getLength() > 0) checkFacets(new YearMonthDV(), currentToken.getValueAsString());
                break;
            case Type.DATE:
                MXQueryDate d = inputToken.getDate();
                if (d.hasTimezone()) tz = d.getTimezoneInMinutes(); else tz = MXQueryGregorian.UNDEFINED;
                gVal = new MXQueryGregorian(d.getYear(), d.getMonth(), d.getDay(), tz, resultType);
                currentToken = new GregorianToken(null, gVal);
                if ((facetsList != null) && facetsList.getLength() > 0) checkFacets(new YearMonthDV(), currentToken.getValueAsString());
                break;
            case Type.UNTYPED_ATOMIC:
            case Type.UNTYPED:
            case Type.STRING:
                String dataVal = inputToken.getValueAsString();
                if (facetsList != null && facetsList.getLength() > 0) dataVal = applyWhitespaceFacet(dataVal);
                gVal = new MXQueryGregorian(dataVal, resultType);
                currentToken = new GregorianToken(null, gVal);
                if ((facetsList != null) && facetsList.getLength() > 0) checkFacets(new YearMonthDV(), currentToken.getValueAsString());
                break;
            case Type.G_DAY:
                if ((facetsList != null) && facetsList.getLength() > 0) checkFacets(new DayDV(), currentToken.getValueAsString());
                break;
            case Type.G_MONTH:
                if ((facetsList != null) && facetsList.getLength() > 0) checkFacets(new MonthDV(), currentToken.getValueAsString());
                break;
            case Type.G_YEAR:
                if ((facetsList != null) && facetsList.getLength() > 0) checkFacets(new YearDV(), currentToken.getValueAsString());
                break;
            case Type.G_MONTH_DAY:
                if ((facetsList != null) && facetsList.getLength() > 0) checkFacets(new MonthDayDV(), currentToken.getValueAsString());
                break;
            case Type.G_YEAR_MONTH:
                if ((facetsList != null) && facetsList.getLength() > 0) checkFacets(new YearMonthDV(), currentToken.getValueAsString());
                break;
            default:
                throw new MXQueryException(ErrorCodes.A0009_EC_EVALUATION_NOT_POSSIBLE, "Type " + inputType + " should not come until here", null);
        }
        if (input.next() != Token.END_SEQUENCE_TOKEN) throw new TypeException(ErrorCodes.E0004_TYPE_INAPPROPRIATE_TYPE, "Could not cast sequence to atomic type " + Type.getTypeQName(resultType, Context.getDictionary()), loc);
    }

    private void checkParamType(int paramType) throws MXQueryException {
        if (resultType == paramType) return;
        if (paramType == Type.END_SEQUENCE || paramType == Type.UNTYPED_ATOMIC || paramType == Type.STRING || paramType == Type.UNTYPED || paramType == Type.DATE_TIME || paramType == Type.DATE) return;
        throw new TypeException(ErrorCodes.E0004_TYPE_INAPPROPRIATE_TYPE, "Could not cast type " + Type.getTypeQName(paramType, Context.getDictionary()) + " to type " + Type.getTypeQName(resultType, Context.getDictionary()), loc);
    }

    public TypeInfo getStaticType() {
        return new TypeInfo(resultType, Type.OCCURRENCE_IND_ZERO_OR_ONE);
    }

    protected XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        XSGregorian copy = new XSGregorian();
        copy.setReturnType(resultType);
        copy.setContext(context, true);
        copy.setSubIters(subIters);
        copy.setFacetsList(getFacetsList());
        copy.setMfacetsList(getMfacetsList());
        return copy;
    }
}
