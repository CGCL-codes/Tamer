package ch.ethz.mxquery.iterators;

import java.util.Vector;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.Identifier;
import ch.ethz.mxquery.datamodel.MXQueryDouble;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.types.TypeInfo;
import ch.ethz.mxquery.datamodel.xdm.BooleanToken;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;
import ch.ethz.mxquery.exceptions.StaticException;
import ch.ethz.mxquery.exceptions.TypeException;
import ch.ethz.mxquery.model.Constants;
import ch.ethz.mxquery.model.DataflowAnalysis;
import ch.ethz.mxquery.model.TokenBasedIterator;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.util.KXmlSerializer;
import ch.ethz.mxquery.util.ObjectObjectPair;
import ch.ethz.mxquery.util.Set;

/**
 * 
 * @author Matthias Braun
 * @author Tim Kraska
 * 
 */
public class CompareIterator extends TokenBasedIterator {

    private int comparator = -1;

    private int compareType = -1;

    public CompareIterator(Context ctx, int compareType, int comparator, XDMIterator[] subIters, QueryLocation location) throws MXQueryException {
        super(ctx, subIters, 2, location);
        if (comparator < 0) {
            throw new IllegalArgumentException();
        }
        this.comparator = comparator;
        this.compareType = compareType;
        this.subIters = subIters;
    }

    protected void init() throws MXQueryException {
        boolean v = false;
        if (Constants.COMP_VALUE == compareType) {
            TokenInterface r1 = subIters[0].next();
            TokenInterface r2 = subIters[1].next();
            if (r1.getEventType() == Type.END_SEQUENCE || r2.getEventType() == Type.END_SEQUENCE) {
                currentToken = Token.END_SEQUENCE_TOKEN;
                return;
            }
            if ((r1.getEventType() == Type.QNAME || r2.getEventType() == Type.QNAME) && !(comparator == Constants.COMP_EQ || comparator == Constants.COMP_NE)) throw new TypeException(ErrorCodes.E0004_TYPE_INAPPROPRIATE_TYPE, "Only eq and neq defined for QName types", loc);
            if ((r1.getEventType() == Type.DURATION || r2.getEventType() == Type.DURATION) && !(comparator == Constants.COMP_EQ || comparator == Constants.COMP_NE)) throw new TypeException(ErrorCodes.E0004_TYPE_INAPPROPRIATE_TYPE, "Only eq and neq defined for Duration types", loc);
            if (((r1.getEventType() == Type.DAY_TIME_DURATION && r2.getEventType() == Type.YEAR_MONTH_DURATION) || (r2.getEventType() == Type.DAY_TIME_DURATION && r1.getEventType() == Type.YEAR_MONTH_DURATION)) && !(comparator == Constants.COMP_EQ || comparator == Constants.COMP_NE)) throw new TypeException(ErrorCodes.E0004_TYPE_INAPPROPRIATE_TYPE, "Only eq and neq defined for Duration types", loc);
            int result = r1.compareTo(r2);
            if (subIters[0].next().getEventType() != Type.END_SEQUENCE || subIters[1].next().getEventType() != Type.END_SEQUENCE) {
                throw new TypeException(ErrorCodes.E0004_TYPE_INAPPROPRIATE_TYPE, "Expected single value for the value comparison and not a sequence", loc);
            }
            v = returnBooleanValue(result);
        }
        if (compareType == Constants.COMP_GENERAL) {
            Vector tokenSeq1 = new Vector();
            Vector tokenSeq2 = new Vector();
            TokenInterface tok1 = subIters[0].next();
            while (tok1.getEventType() != Type.END_SEQUENCE) {
                tokenSeq1.addElement(tok1);
                tok1 = subIters[0].next();
            }
            TokenInterface tok2 = this.subIters[1].next();
            while (tok2.getEventType() != Type.END_SEQUENCE) {
                tokenSeq2.addElement(tok2);
                tok2 = this.subIters[1].next();
            }
            int i = 0;
            TokenInterface token1, token2;
            while (!v && i < tokenSeq1.size()) {
                token1 = (TokenInterface) tokenSeq1.elementAt(i);
                int t1 = token1.getEventType();
                int j = 0;
                while (!v && j < tokenSeq2.size()) {
                    token2 = (TokenInterface) tokenSeq2.elementAt(j);
                    int t2 = token2.getEventType();
                    if (t1 == Type.UNTYPED_ATOMIC || t2 == Type.UNTYPED_ATOMIC || t1 == Type.UNTYPED || t2 == Type.UNTYPED) {
                        int result = 0;
                        if (Type.isNumericPrimitiveType(t1) || Type.isNumericPrimitiveType(t2)) {
                            result = new MXQueryDouble(token1.getValueAsString()).compareTo(new MXQueryDouble(token2.getValueAsString()));
                        } else if (Type.STRING == t1 || Type.STRING == t2 || (Type.UNTYPED_ATOMIC == t1 && Type.UNTYPED_ATOMIC == t2) || t1 == Type.UNTYPED || t2 == Type.UNTYPED) {
                            result = token1.getText().compareTo(token2.getText());
                            if (result > 1) result = 1;
                            if (result < -1) result = -1;
                        } else {
                            if (t1 == Type.UNTYPED || t1 == Type.UNTYPED_ATOMIC) {
                                CastAsIterator ca = new CastAsIterator(context, new TokenIterator(context, token1, loc, false), new TypeInfo(token2.getEventType(), Type.OCCURRENCE_IND_EXACTLY_ONE), false, true, loc);
                                token1 = ca.next();
                            } else {
                                CastAsIterator ca = new CastAsIterator(context, new TokenIterator(context, token2, loc, false), new TypeInfo(token1.getEventType(), Type.OCCURRENCE_IND_EXACTLY_ONE), false, true, loc);
                                token2 = ca.next();
                            }
                            result = token1.compareTo(token2);
                            if (result > 1) result = 1;
                            if (result < -1) result = -1;
                        }
                        v = returnBooleanValue(result);
                    } else {
                        int result = Token.compare(token1, token2, token1.getEventType(), token2.getEventType());
                        v = returnBooleanValue(result);
                    }
                    j++;
                }
                i++;
            }
        }
        if (this.compareType == Constants.COMP_NODE) {
            TokenInterface t0 = subIters[0].next();
            TokenInterface t1 = subIters[1].next();
            if (!v && t0.getEventType() != Type.END_SEQUENCE && !v && t1.getEventType() != Type.END_SEQUENCE) {
                if (!Type.isNode(t0.getEventType()) && !Type.isNode(t1.getEventType())) throw new TypeException(ErrorCodes.E0004_TYPE_INAPPROPRIATE_TYPE, "Node comparison not possible on non-node types", loc);
                Identifier i0 = t0.getNodeId();
                Identifier i1 = t1.getNodeId();
                int i = i0.compare(i1);
                if (i == 0) {
                    switch(comparator) {
                        case Constants.COMP_EQ:
                            currentToken = BooleanToken.TRUE_TOKEN;
                            break;
                        case Constants.COMP_LT:
                            currentToken = BooleanToken.FALSE_TOKEN;
                            break;
                        case Constants.COMP_GT:
                            currentToken = BooleanToken.FALSE_TOKEN;
                    }
                }
                if (i <= -1) {
                    switch(comparator) {
                        case Constants.COMP_EQ:
                            currentToken = BooleanToken.FALSE_TOKEN;
                            break;
                        case Constants.COMP_LT:
                            currentToken = BooleanToken.FALSE_TOKEN;
                            break;
                        case Constants.COMP_GT:
                            currentToken = BooleanToken.TRUE_TOKEN;
                    }
                }
                if (i >= 1) {
                    switch(comparator) {
                        case Constants.COMP_EQ:
                            currentToken = BooleanToken.FALSE_TOKEN;
                            break;
                        case Constants.COMP_LT:
                            currentToken = BooleanToken.TRUE_TOKEN;
                            break;
                        case Constants.COMP_GT:
                            currentToken = BooleanToken.FALSE_TOKEN;
                            break;
                    }
                }
            } else {
                currentToken = Token.END_SEQUENCE_TOKEN;
            }
            return;
        }
        this.subIters[0].close(true);
        this.subIters[1].close(true);
        if (v) currentToken = BooleanToken.TRUE_TOKEN; else currentToken = BooleanToken.FALSE_TOKEN;
        return;
    }

    private boolean returnBooleanValue(int compareResult) {
        switch(comparator) {
            case Constants.COMP_EQ:
                if (compareResult == 0) {
                    return true;
                } else {
                    return false;
                }
            case Constants.COMP_NE:
                if (compareResult != 0) {
                    return true;
                } else {
                    return false;
                }
            case Constants.COMP_LT:
                if (compareResult == -1) {
                    return true;
                } else {
                    return false;
                }
            case Constants.COMP_LE:
                if (compareResult <= 0 && compareResult > -2) {
                    return true;
                } else {
                    return false;
                }
            case Constants.COMP_GT:
                if (compareResult == 1) {
                    return true;
                } else {
                    return false;
                }
            case Constants.COMP_GE:
                if (compareResult >= 0 && compareResult < 2) {
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    public void setResettable(boolean r) throws MXQueryException {
        this.resettable = r;
        subIters[0].setResettable(r);
        if (compareType == Constants.COMP_GENERAL) {
            subIters[1].setResettable(true);
        } else {
            subIters[1].setResettable(r);
        }
    }

    public int getComparator() {
        return comparator;
    }

    public int getCompareType() {
        return compareType;
    }

    protected KXmlSerializer createIteratorStartTag(KXmlSerializer serializer) throws Exception {
        super.createIteratorStartTag(serializer);
        serializer.attribute(null, "comparator", Constants.getCompareString(comparator, compareType));
        return serializer;
    }

    public XDMIterator getLeftPart() {
        return subIters[0];
    }

    public XDMIterator getRightPart() {
        return subIters[1];
    }

    protected XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        return new CompareIterator(context, compareType, comparator, subIters, loc);
    }

    public TypeInfo getStaticType() {
        return new TypeInfo(Type.BOOLEAN, Type.OCCURRENCE_IND_ZERO_OR_ONE);
    }

    public ObjectObjectPair getProjectionPaths() throws StaticException {
        Set returned = new Set();
        Set used = new Set();
        for (int i = 0; i < subIters.length; i++) {
            ObjectObjectPair cur = subIters[i].getProjectionPaths();
            used.addAll((Set) cur.getFirst());
            used.addAll((Set) cur.getSecond());
        }
        return new ObjectObjectPair(returned, used);
    }

    public XDMIterator require(int requiredOptions) throws MXQueryException {
        if (compareType == Constants.COMP_NODE) requiredOptions = requiredOptions | DataflowAnalysis.NODEID_ORDER;
        return super.require(requiredOptions);
    }
}
