package at.jku.rdfstats.expr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.jku.rdfstats.ParseException;
import at.jku.rdfstats.hist.Histogram;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.engine.binding.Binding0;
import com.hp.hpl.jena.sparql.engine.binding.BindingMap;
import com.hp.hpl.jena.sparql.expr.E_Equals;
import com.hp.hpl.jena.sparql.expr.E_GreaterThan;
import com.hp.hpl.jena.sparql.expr.E_GreaterThanOrEqual;
import com.hp.hpl.jena.sparql.expr.E_LessThan;
import com.hp.hpl.jena.sparql.expr.E_LessThanOrEqual;
import com.hp.hpl.jena.sparql.expr.E_LogicalAnd;
import com.hp.hpl.jena.sparql.expr.E_LogicalNot;
import com.hp.hpl.jena.sparql.expr.E_LogicalOr;
import com.hp.hpl.jena.sparql.expr.E_NotEquals;
import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprFunction;
import com.hp.hpl.jena.sparql.expr.ExprFunction1;
import com.hp.hpl.jena.sparql.expr.ExprFunction2;
import com.hp.hpl.jena.sparql.expr.ExprVar;
import com.hp.hpl.jena.sparql.expr.ExprVisitor;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueBoolean;

/**
 * @author dorgon
 *
 * these operators create coverages:
 * - logical operators: step-down and recursively set currentCov for &&, ||, !
 * - comparison operators: don't step down, but eval constant sub exprs (with no variables) for <, <=, ==, !=,  >=, >
 * 
 * all other operators are only evaluated for constant sub expressions of these operators and ignored otherwise
 * e.g. (?x > (5+15) && ?x < 50) is evaluated
 *      but (?x > 5 && bound(?x)) is not evaluated because it cannot be used for estimation
 *      (?x > 5 && ?y < 10) is also ignored since there are two variables but 1 histogram
 * 
 */
public class CoverageBuilder implements ExprVisitor {

    private static final Logger log = LoggerFactory.getLogger(CoverageBuilder.class);

    private Coverage currentCov = Coverage.createFull();

    private final Histogram histogram;

    /**
	 * 
	 */
    public CoverageBuilder(Histogram h) {
        this.histogram = h;
    }

    /**
	 * build coverage of a filter expression and return estimation
	 * 
	 * @param root
	 * @param histogram
	 * @return
	 * @throws CoverageException
	 */
    public static Integer estimate(Expr root, Histogram histogram) throws CoverageException {
        CoverageBuilder cb = new CoverageBuilder(histogram);
        root.visit(cb);
        return cb.getEstimate();
    }

    /**
	 * build coverage of a filter expression and return it
	 * 
	 * @param root
	 * @param histogram
	 * @return
	 * @throws CoverageException
	 */
    public static Coverage build(Expr root, Histogram histogram) throws CoverageException {
        CoverageBuilder cb = new CoverageBuilder(histogram);
        root.visit(cb);
        return cb.getCoverage();
    }

    public void visit(ExprFunction expr) {
        if (currentCov == null) return;
        try {
            if (expr instanceof ExprFunction1) {
                if (expr instanceof E_LogicalNot) {
                    ((E_LogicalNot) expr).getArg().visit(this);
                    if (currentCov != null) currentCov = currentCov.complement();
                    return;
                }
            } else if (expr instanceof ExprFunction2) {
                if (expr instanceof E_LogicalAnd) {
                    Coverage c1 = null;
                    Coverage c2 = null;
                    ((E_LogicalAnd) expr).getArg1().visit(this);
                    c1 = currentCov;
                    if (c1 != null) {
                        ((E_LogicalAnd) expr).getArg2().visit(this);
                        c2 = currentCov;
                    }
                    if (c1 != null && c2 != null) currentCov = c1.and(c2); else currentCov = null;
                    return;
                } else if (expr instanceof E_LogicalOr) {
                    Coverage c1 = null;
                    Coverage c2 = null;
                    ((E_LogicalOr) expr).getArg1().visit(this);
                    c1 = currentCov;
                    if (c1 != null) {
                        ((E_LogicalOr) expr).getArg2().visit(this);
                        c2 = currentCov;
                    }
                    if (c1 != null && c2 != null) currentCov = c1.or(c2); else currentCov = null;
                    return;
                } else if (expr instanceof E_LessThan) {
                    Expr e1 = expr.getArg(1);
                    Expr e2 = expr.getArg(2);
                    if (e1 instanceof ExprVar) {
                        if (e2 instanceof ExprVar && e1.asVar().equals(e2.asVar())) {
                            currentCov = Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof NodeValue) {
                            Node val = e2.getConstant().asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(InfinitableValue.NEGATIVE_INFINITY, true, obj, false);
                                return;
                            }
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            Node val = e2.eval(new BindingMap(), null).asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(InfinitableValue.NEGATIVE_INFINITY, true, obj, false);
                                return;
                            }
                        }
                    } else if (e1 instanceof NodeValue) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.getConstant().asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(obj, false, InfinitableValue.POSITIVE_INFINITY, true);
                                return;
                            }
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    } else if (e1 instanceof ExprFunction && e1.getVarsMentioned().isEmpty()) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.eval(new BindingMap(), null).asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(obj, false, InfinitableValue.POSITIVE_INFINITY, true);
                                return;
                            }
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    }
                } else if (expr instanceof E_LessThanOrEqual) {
                    Expr e1 = expr.getArg(1);
                    Expr e2 = expr.getArg(2);
                    if (e1 instanceof ExprVar) {
                        if (e2 instanceof ExprVar && e1.asVar().equals(e2.asVar())) {
                            currentCov = Coverage.createFull();
                            return;
                        } else if (e2 instanceof NodeValue) {
                            Node val = e2.getConstant().asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(InfinitableValue.NEGATIVE_INFINITY, true, obj, true);
                                return;
                            }
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            Node val = e2.eval(new BindingMap(), null).asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(InfinitableValue.NEGATIVE_INFINITY, true, obj, true);
                                return;
                            }
                        }
                    } else if (e1 instanceof NodeValue) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.getConstant().asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(obj, true, InfinitableValue.POSITIVE_INFINITY, true);
                                return;
                            }
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    } else if (e1 instanceof ExprFunction && e1.getVarsMentioned().isEmpty()) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.eval(new BindingMap(), null).asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(obj, true, InfinitableValue.POSITIVE_INFINITY, true);
                                return;
                            }
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    }
                } else if (expr instanceof E_Equals) {
                    Expr e1 = expr.getArg(1);
                    Expr e2 = expr.getArg(2);
                    if (e1 instanceof ExprVar) {
                        if (e2 instanceof ExprVar && e1.asVar().equals(e2.asVar())) {
                            currentCov = Coverage.createFull();
                            return;
                        } else if (e2 instanceof NodeValue) {
                            Node val = e2.getConstant().asNode();
                            Object obj = histogram.parseNodeValue(val);
                            currentCov = Coverage.create(obj);
                            return;
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            Node val = e2.eval(new BindingMap(), null).asNode();
                            Object obj = histogram.parseNodeValue(val);
                            currentCov = Coverage.create(obj);
                            return;
                        }
                    } else if (e1 instanceof NodeValue) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.getConstant().asNode();
                            Object obj = histogram.parseNodeValue(val);
                            currentCov = Coverage.create(obj);
                            return;
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    } else if (e1 instanceof ExprFunction && e1.getVarsMentioned().isEmpty()) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.eval(new BindingMap(), null).asNode();
                            Object obj = histogram.parseNodeValue(val);
                            currentCov = Coverage.create(obj);
                            return;
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    }
                } else if (expr instanceof E_NotEquals) {
                    Expr e1 = expr.getArg(1);
                    Expr e2 = expr.getArg(2);
                    if (e1 instanceof ExprVar) {
                        if (e2 instanceof ExprVar && e1.asVar().equals(e2.asVar())) {
                            currentCov = Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof NodeValue) {
                            Node val = e2.getConstant().asNode();
                            Object obj = histogram.parseNodeValue(val);
                            Coverage cov = Coverage.createFullWithout(obj);
                            currentCov = cov;
                            return;
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            Node val = e2.eval(new BindingMap(), null).asNode();
                            Object obj = histogram.parseNodeValue(val);
                            Coverage cov = Coverage.createFullWithout(obj);
                            currentCov = cov;
                            return;
                        }
                    } else if (e1 instanceof NodeValue) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.getConstant().asNode();
                            Object obj = histogram.parseNodeValue(val);
                            Coverage cov = Coverage.createFullWithout(obj);
                            currentCov = cov;
                            return;
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    } else if (e1 instanceof ExprFunction && e1.getVarsMentioned().isEmpty()) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.eval(new BindingMap(), null).asNode();
                            Object obj = histogram.parseNodeValue(val);
                            Coverage cov = Coverage.createFullWithout(obj);
                            currentCov = cov;
                            return;
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    }
                } else if (expr instanceof E_GreaterThanOrEqual) {
                    Expr e1 = expr.getArg(1);
                    Expr e2 = expr.getArg(2);
                    if (e1 instanceof ExprVar) {
                        if (e2 instanceof ExprVar && e1.asVar().equals(e2.asVar())) {
                            currentCov = Coverage.createFull();
                            return;
                        } else if (e2 instanceof NodeValue) {
                            Node val = e2.getConstant().asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(obj, true, InfinitableValue.POSITIVE_INFINITY, true);
                                return;
                            }
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            Node val = e2.eval(new BindingMap(), null).asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(obj, true, InfinitableValue.POSITIVE_INFINITY, true);
                                return;
                            }
                        }
                    } else if (e1 instanceof NodeValue) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.getConstant().asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(InfinitableValue.NEGATIVE_INFINITY, true, obj, true);
                                return;
                            }
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    } else if (e1 instanceof ExprFunction && e1.getVarsMentioned().isEmpty()) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.eval(new BindingMap(), null).asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(InfinitableValue.NEGATIVE_INFINITY, true, obj, true);
                                return;
                            }
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    }
                } else if (expr instanceof E_GreaterThan) {
                    Expr e1 = expr.getArg(1);
                    Expr e2 = expr.getArg(2);
                    if (e1 instanceof ExprVar) {
                        if (e2 instanceof ExprVar && e1.asVar().equals(e2.asVar())) {
                            currentCov = Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof NodeValue) {
                            Node val = e2.getConstant().asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(obj, false, InfinitableValue.POSITIVE_INFINITY, true);
                                return;
                            }
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            Node val = e2.eval(new BindingMap(), null).asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(obj, false, InfinitableValue.POSITIVE_INFINITY, true);
                                return;
                            }
                        }
                    } else if (e1 instanceof NodeValue) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.getConstant().asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(InfinitableValue.NEGATIVE_INFINITY, true, obj, false);
                                return;
                            }
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    } else if (e1 instanceof ExprFunction && e1.getVarsMentioned().isEmpty()) {
                        if (e2 instanceof ExprVar) {
                            Node val = e1.eval(new BindingMap(), null).asNode();
                            Object parsed = histogram.parseNodeValue(val);
                            if (parsed instanceof Comparable) {
                                Comparable<Object> obj = (Comparable<Object>) parsed;
                                currentCov = Coverage.create(InfinitableValue.NEGATIVE_INFINITY, true, obj, false);
                                return;
                            }
                        } else if (e2 instanceof NodeValue) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        } else if (e2 instanceof ExprFunction && e2.getVarsMentioned().isEmpty()) {
                            currentCov = isTrue(expr) ? Coverage.createFull() : Coverage.createEmpty();
                            return;
                        }
                    }
                }
            }
        } catch (ParseException e) {
            log.warn("Failed to build range coverage for " + expr + ": " + e.getMessage());
        }
        currentCov = null;
    }

    private boolean isTrue(ExprFunction expr) {
        Expr eval = expr.eval(new Binding0(), null);
        return (eval instanceof NodeValueBoolean && ((NodeValueBoolean) eval).getBoolean());
    }

    public void visit(NodeValue nv) {
    }

    public void visit(ExprVar v) {
    }

    public void startVisit() {
    }

    public void finishVisit() {
    }

    public Integer getEstimate() {
        if (currentCov == null) return null; else return currentCov.getEstimate(histogram);
    }

    /**
	 * @return the currentCov
	 */
    public Coverage getCoverage() {
        return currentCov;
    }
}
