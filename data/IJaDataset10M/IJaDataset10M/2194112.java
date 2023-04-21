package pl.wcislo.sbql4j.lang.tree.visitors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.db4o.internal.LocalTransaction;
import pl.wcislo.sbql4j.db4o.result.Db4oMethodBinder;
import pl.wcislo.sbql4j.db4o.result.Db4oObject;
import pl.wcislo.sbql4j.db4o.result.Db4oResultFactory;
import pl.wcislo.sbql4j.exception.SBQLException;
import pl.wcislo.sbql4j.java.model.runtime.ConstructorBinder;
import pl.wcislo.sbql4j.java.model.runtime.JavaClass;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.java.model.runtime.JavaPackage;
import pl.wcislo.sbql4j.java.model.runtime.MethodBinder;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectFactory;
import pl.wcislo.sbql4j.java.model.runtime.reflect.JavaComplexObjectReflect;
import pl.wcislo.sbql4j.java.utils.Pair;
import pl.wcislo.sbql4j.lang.parser.expression.AsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.CloseByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ComaExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConditionalExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConstructorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DerefExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DotExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.ForEachExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForallExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForanyExpression;
import pl.wcislo.sbql4j.lang.parser.expression.GroupAsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.parser.expression.LiteralExpression;
import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression.SortType;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorComma;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorType;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.StructSBQL;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.model.collections.Sequence;
import pl.wcislo.sbql4j.util.StructSortWrapper;
import pl.wcislo.sbql4j.util.Utils;
import pl.wcislo.sbql4j.xml.model.DBObject;
import pl.wcislo.sbql4j.xml.model.XmlId;
import pl.wcislo.sbql4j.xml.parser.store.XMLObjectStore;

public class Interpreter implements TreeVisitor {

    private static final Log log = LogFactory.getLog(Interpreter.class);

    private XMLObjectStore store;

    private Stack<List<? extends ENVSType>> envs = new Stack<List<? extends ENVSType>>();

    private Stack<QueryResult> qres = new Stack<QueryResult>();

    private JavaObjectFactory javaObjectFactory = JavaObjectAbstractFactory.getJavaObjectFactory();

    private List<String> usedPackages;

    private OperatorInterpreter opInterpreter = new OperatorInterpreter();

    public Interpreter() {
    }

    public Interpreter(XMLObjectStore store) {
        this.store = store;
        List<Binder> initS = new ArrayList<Binder>();
        for (DBObject o : store.getRootObj().getNestedObjects()) {
            initS.add(new Binder(o.getName(), o.getId()));
        }
        envs.push(initS);
    }

    public Interpreter(List<Pair<Object>> varList, List<Pair<Class>> staticList, List<String> usedPackages) {
        this.usedPackages = usedPackages;
        List<Binder> initClasses = new ArrayList<Binder>();
        for (Pair<Class> clazz : staticList) {
            CollectionResult jObj = javaObjectFactory.createJavaObject(clazz.val);
            for (QueryResult jo : jObj) {
                initClasses.add(new Binder(clazz.name, jo));
            }
        }
        envs.push(initClasses);
        List<Binder> initVar = new ArrayList<Binder>();
        for (Pair<Object> var : varList) {
            CollectionResult jObj = (CollectionResult) javaObjectFactory.createJavaObject(var.val);
            initVar.add(new Binder(var.name, jObj));
        }
        envs.push(initVar);
    }

    public Interpreter(List<Binder> varList, List<Binder> classList) {
        envs.push(classList);
        envs.push(varList);
    }

    public Object evaluateExpression(Expression expr) {
        try {
            expr.accept(this, null);
            if (!qres.isEmpty()) {
                return qres.pop();
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            System.err.println("expr=" + expr);
            e.printStackTrace();
            throw new SBQLException(e.getMessage(), e);
        }
    }

    @Override
    public Object visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, Object object) {
        Object o1 = expr.ex1.accept(this, object);
        Object o2 = expr.ex2.accept(this, object);
        expr.op.accept(opInterpreter, this, expr, expr.ex1, expr.ex2);
        return Utils.mergeExpressionParams(o1, o2);
    }

    @Override
    public Object visitUnaryExpression(UnarySimpleOperatorExpression expr, Object object) {
        expr.ex1.accept(this, object);
        expr.op.accept(opInterpreter, this, expr, expr.ex1, expr.genericExpression);
        return object;
    }

    @Override
    public Object visitLiteralExpression(LiteralExpression expr, Object object) {
        JavaObject jo = javaObjectFactory.createJavaComplexObject(expr.l);
        qres.push(jo);
        return object;
    }

    @Override
    public Object visitNameExpression(NameExpression expr, Object object) {
        QueryResult b = bind(expr.l.val);
        if (b != null) {
            qres.push(b);
        }
        return object;
    }

    public Stack<List<? extends ENVSType>> getEnvs() {
        return envs;
    }

    public Stack<QueryResult> getQres() {
        return qres;
    }

    public XMLObjectStore getStore() {
        return store;
    }

    protected CollectionResult bind(String name) {
        Sequence tmpRes = new Sequence<List<QueryResult>>();
        boolean found = false;
        boolean wasSequence = false;
        for (int i = envs.size() - 1; i >= 0 && !found; i--) {
            List<? extends ENVSType> o = envs.get(i);
            List<? extends ENVSType> section = (List<? extends ENVSType>) o;
            for (ENVSType secEl : section) {
                if (secEl instanceof Binder) {
                    Binder b = (Binder) secEl;
                    if (name.equals(b.name)) {
                        tmpRes.add(b.object);
                        found = true;
                        if (b.object instanceof Sequence) {
                            wasSequence = true;
                        }
                    }
                }
            }
        }
        if (tmpRes.isEmpty() && usedPackages.contains(name)) {
            tmpRes.add(new JavaPackage(name));
        }
        CollectionResult res;
        if (wasSequence) {
            return tmpRes;
        } else {
            res = new Bag();
            res.addAll(tmpRes);
            return res;
        }
    }

    protected QueryResult bindOne(String name) {
        CollectionResult res = bind(name);
        Iterator<QueryResult> it = res.iterator();
        if (it.hasNext()) {
            return it.next();
        } else {
            return null;
        }
    }

    @Override
    public Object visitWhereExpression(WhereExpression expr, Object object) {
        expr.ex1.accept(this, object);
        CollectionResult bag;
        QueryResult q = qres.pop();
        boolean wasBag = false;
        CollectionResult result;
        if (q instanceof CollectionResult) {
            bag = ((CollectionResult) q);
            result = ((CollectionResult) q).createSameType();
            wasBag = true;
        } else {
            bag = new Bag();
            result = new Bag();
            bag.add(q);
            wasBag = false;
        }
        int i = 0;
        for (QueryResult type : bag) {
            if (type instanceof XmlId) {
                type = store.get((XmlId) type);
            }
            List envsStack;
            if (type.nested() == null) {
                envsStack = new ArrayList();
            } else {
                envsStack = type.nested();
            }
            Binder loopIndexBinder = new Binder("$index", javaObjectFactory.createJavaComplexObject(new Integer(i++)));
            envsStack.add(loopIndexBinder);
            envs.push(envsStack);
            expr.ex2.accept(this, type);
            QueryResult t = qres.pop();
            if (t instanceof CollectionResult) {
                t = Utils.collectionToObject((CollectionResult) t);
            }
            boolean rightVal = (Boolean) Utils.toSimpleValue(t, store);
            if (rightVal) {
                result.add(type);
            }
            envs.pop();
        }
        if (!wasBag) {
            if (result.isEmpty()) {
                qres.push(null);
            } else {
                qres.push(result.iterator().next());
            }
        } else {
            qres.push(result);
        }
        return object;
    }

    @Override
    public Object visitDerefExpression(DerefExpression derefExpression, Object object) {
        derefExpression.ex1.accept(this, object);
        Bag result = new Bag();
        QueryResult o = qres.pop();
        if (o instanceof CollectionResult) {
            for (QueryResult oo : (CollectionResult) o) {
                if (oo instanceof XmlId) {
                    result.add(store.get((XmlId) oo));
                } else {
                    result.add(oo);
                }
            }
        } else {
            if (o instanceof XmlId) {
                result.add(store.get((XmlId) o));
            } else {
                result.add(o);
            }
        }
        qres.push(result);
        return object;
    }

    @Override
    public Object visitDotExpression(DotExpression expr, Object object) {
        expr.ex1.accept(this, object);
        QueryResult q = qres.lastElement();
        CollectionResult dotLeftCol = Utils.objectToCollection(q);
        CollectionResult dotResult = dotLeftCol.createSameType();
        int i = 0;
        for (Object o : dotLeftCol) {
            QueryResult type = (QueryResult) o;
            if (type instanceof XmlId) {
                type = store.get((XmlId) type);
            }
            List envsStack;
            if (type.nested() == null) {
                envsStack = new ArrayList();
            } else {
                envsStack = type.nested();
            }
            Binder loopIndexBinder = new Binder("$index", javaObjectFactory.createJavaComplexObject(new Integer(i++)));
            envsStack.add(loopIndexBinder);
            envs.push(envsStack);
            expr.ex2.accept(this, o);
            CollectionResult b = Utils.objectToCollection(qres.pop());
            for (QueryResult oo : b) {
                dotResult.add(oo);
            }
            envs.pop();
        }
        qres.pop();
        qres.push(dotResult);
        return object;
    }

    @Override
    public Object visitAsExpression(AsExpression expr, Object object) {
        expr.ex1.accept(this, object);
        QueryResult qr = qres.pop();
        CollectionResult result;
        if (qr instanceof Sequence) {
            result = new Sequence();
        } else {
            result = new Bag();
        }
        CollectionResult b = Utils.objectToCollection(qr);
        for (QueryResult o : b) {
            result.add(new Binder(expr.identifier.val, o));
        }
        qres.push(Utils.collectionToObject(result));
        return object;
    }

    @Override
    public Object visitGroupAsExpression(GroupAsExpression expr, Object object) {
        expr.ex1.accept(this, object);
        CollectionResult bag = Utils.objectToCollection(qres.pop());
        qres.push(new Binder(expr.identifier.val, bag));
        return object;
    }

    @Override
    public Object visitComaExpression(ComaExpression expr, Object object) {
        Object o1 = expr.ex1.accept(this, object);
        Object o2 = expr.ex2.accept(this, object);
        OperatorComma op = new OperatorComma(OperatorType.COMA);
        op.accept(opInterpreter, this, expr, new Expression[] { expr.ex1, expr.ex2 });
        return Utils.mergeExpressionParams(o1, o2);
    }

    @Override
    public Object visitJoinExpression(JoinExpression expr, Object object) {
        expr.ex1.accept(this, object);
        CollectionResult c1 = Utils.objectToCollection(qres.lastElement());
        Bag result = new Bag();
        for (QueryResult rightObj : c1) {
            QueryResult rightObjId = rightObj;
            if (rightObj instanceof XmlId) {
                rightObj = store.get((XmlId) rightObj);
            }
            envs.push(rightObj.nested());
            expr.ex2.accept(this, rightObj);
            CollectionResult b = Utils.objectToCollection(qres.pop());
            if (b.size() > 0) {
                for (QueryResult leftObj : b) {
                    result.add(Utils.cartesianProduct(rightObjId, leftObj));
                }
            } else {
                StructSBQL s = new StructSBQL();
                s.add(rightObjId);
                result.add(s);
            }
            envs.pop();
        }
        qres.pop();
        qres.push(Utils.collectionToObject(result));
        return object;
    }

    @Override
    public Object visitForallExpression(ForallExpression expr, Object object) {
        expr.ex1.accept(this, object);
        CollectionResult c1 = Utils.objectToCollection(qres.lastElement());
        Boolean result = true;
        int i = 0;
        for (QueryResult leftObj : c1) {
            if (leftObj instanceof XmlId) {
                leftObj = store.get((XmlId) leftObj);
            }
            List envsStack;
            if (leftObj.nested() == null) {
                envsStack = new ArrayList();
            } else {
                envsStack = leftObj.nested();
            }
            Binder loopIndexBinder = new Binder("$index", javaObjectFactory.createJavaComplexObject(new Integer(i++)));
            envsStack.add(loopIndexBinder);
            envs.push(envsStack);
            expr.ex2.accept(this, leftObj);
            envs.pop();
            QueryResult rightObj = qres.pop();
            boolean b = (Boolean) Utils.toSimpleValue(rightObj, store);
            if (!b) {
                result = false;
                break;
            }
        }
        qres.pop();
        qres.push(javaObjectFactory.createJavaComplexObject(result));
        return object;
    }

    @Override
    public Object visitForanyExpression(ForanyExpression expr, Object object) {
        expr.ex1.accept(this, object);
        CollectionResult c1 = Utils.objectToCollection(qres.lastElement());
        Boolean result = false;
        for (QueryResult leftObj : c1) {
            if (leftObj instanceof XmlId) {
                leftObj = store.get((XmlId) leftObj);
            }
            envs.push(leftObj.nested());
            expr.ex2.accept(this, leftObj);
            envs.pop();
            QueryResult rightObj = qres.pop();
            boolean b = (Boolean) Utils.toSimpleValue(rightObj, store);
            if (b) {
                result = true;
                break;
            }
        }
        qres.pop();
        qres.push(javaObjectFactory.createJavaComplexObject(result));
        return object;
    }

    @Override
    public Object visitMethodExpression(MethodExpression expr, Object object) {
        Object[] params = new Object[0];
        if (expr.paramsExpression != null) {
            expr.paramsExpression.accept(this, object);
            StructSBQL s = Utils.objectToStruct(qres.pop());
            Collection javaParams = Utils.sbqlStuct2JavaList(s);
            params = javaParams.toArray();
        }
        MethodBinder methodBinder = bindMethod(expr.methodName, params);
        if (methodBinder == null) {
            System.err.println("nie ma bindera do metody " + expr.methodName);
        }
        Object result = null;
        QueryResult resultSbql = null;
        try {
            result = methodBinder.execute(params);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        if (result == null) {
            resultSbql = new Bag();
        } else {
            Bag b = new Bag();
            CollectionResult methResSbql = javaObjectFactory.createJavaObject(result);
            if (methodBinder instanceof Db4oMethodBinder) {
                Db4oMethodBinder dbMeth = (Db4oMethodBinder) methodBinder;
                Db4oResultFactory fac = new Db4oResultFactory();
                for (QueryResult q : methResSbql) {
                    if (q instanceof JavaComplexObjectReflect) {
                        JavaComplexObjectReflect jco = (JavaComplexObjectReflect) q;
                        LocalTransaction trans = dbMeth.getTransaction();
                        b.add(fac.createDb4oPersistentObject(jco, trans));
                    } else {
                        b.add(q);
                    }
                }
            } else {
                b.addAll(methResSbql);
            }
            resultSbql = b;
        }
        qres.push(resultSbql);
        return object;
    }

    @Override
    public Object visitConstructorExpression(ConstructorExpression expr, Object object) {
        JavaClass ownerClass = null;
        if (expr.classNameExpr != null) {
            expr.classNameExpr.accept(this, object);
            ownerClass = (JavaClass) Utils.collectionToObject(qres.pop());
        } else if (expr.classNameLiteral != null) {
            ownerClass = (JavaClass) Utils.collectionToObject(bindOne(expr.classNameLiteral));
        }
        Object[] params = new Object[0];
        if (expr.paramsExpression != null) {
            expr.paramsExpression.accept(this, object);
            StructSBQL s = Utils.objectToStruct(qres.pop());
            Collection javaParams = Utils.sbqlStuct2JavaList(s);
            params = javaParams.toArray();
        }
        envs.push(ownerClass.nested());
        ConstructorBinder cb = bindConstructor(ownerClass.value, params);
        envs.pop();
        if (cb == null) {
            System.err.println("nie ma bindera do konstruktora " + ownerClass.value);
        }
        Object result = null;
        QueryResult resultSbql = null;
        try {
            result = cb.execute(params);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        if (result == null) {
            resultSbql = new Bag();
        } else {
            resultSbql = javaObjectFactory.createJavaComplexObject(result);
        }
        qres.push(resultSbql);
        return object;
    }

    @Override
    public Object visitOrderByExpression(OrderByExpression expr, Object object) {
        expr.ex1.accept(this, object);
        CollectionResult c1 = Utils.objectToCollection(qres.lastElement());
        List<StructSortWrapper> result = new ArrayList<StructSortWrapper>();
        List<SortType> stList = new ArrayList<SortType>();
        List<Comparator> comparatorList = new ArrayList<Comparator>();
        for (OrderByParamExpression paramExpr : expr.paramExprs) {
            stList.add(paramExpr.sortType);
            Expression comparatorExpr = paramExpr.comparatorExpression;
            if (comparatorExpr != null) {
                comparatorExpr.accept(this, object);
                QueryResult compQR = qres.pop();
                Object compObj = Utils.toSimpleValue(compQR, store);
                Comparator comp = (Comparator) compObj;
                comparatorList.add(comp);
            } else {
                comparatorList.add(null);
            }
        }
        for (QueryResult leftObj : c1) {
            leftObj = Utils.objectDeref(leftObj, store);
            envs.push(leftObj.nested());
            StructSortWrapper s = new StructSortWrapper(store);
            s.add(leftObj);
            for (int i = 0; i < expr.paramExprs.size(); i++) {
                OrderByParamExpression expr2 = expr.paramExprs.get(i);
                expr2.paramExpression.accept(this, object);
                QueryResult param = qres.pop();
                SortType sortType = stList.get(i);
                Comparator comp = comparatorList.get(i);
                s.add(param, sortType, comp);
            }
            envs.pop();
            result.add(s);
        }
        Collections.sort(result);
        Sequence res = new Sequence();
        for (StructSortWrapper s : result) {
            res.add(s.get(0));
        }
        qres.pop();
        qres.push(res);
        return object;
    }

    @Override
    public Object visitOrderByParamExpression(OrderByParamExpression expr, Object object) {
        expr.paramExpression.accept(this, object);
        QueryResult param = qres.pop();
        qres.push(param);
        return object;
    }

    @Override
    public Object visitCloseByExpression(CloseByExpression expr, Object object) {
        expr.ex1.accept(this, object);
        List<QueryResult> bag = new ArrayList<QueryResult>();
        QueryResult q = qres.pop();
        if (q instanceof Bag) {
            bag.addAll((Bag) q);
        } else {
            bag.add(q);
        }
        Bag result = new Bag<Collection<QueryResult>>();
        result.addAll(bag);
        int i = 0;
        while (i < bag.size()) {
            QueryResult type = bag.get(i);
            if (type instanceof XmlId) {
                type = store.get((XmlId) type);
            }
            if (type.nested() == null) {
                envs.push(new ArrayList());
            } else {
                envs.push(type.nested());
            }
            expr.ex2.accept(this, type);
            QueryResult res = qres.pop();
            if (res instanceof CollectionResult) {
                result.addAll((CollectionResult) res);
                bag.addAll((CollectionResult) res);
            } else {
                result.add(res);
                bag.add(res);
            }
            envs.pop();
            i++;
        }
        qres.push(result);
        return object;
    }

    private MethodBinder bindMethod(String methodName, Object[] params) {
        for (int i = envs.size() - 1; i >= 0; i--) {
            List<? extends ENVSType> o = envs.get(i);
            List<? extends ENVSType> section = (List<? extends ENVSType>) o;
            for (ENVSType secEl : section) {
                if (secEl instanceof MethodBinder) {
                    MethodBinder methodBinder = (MethodBinder) secEl;
                    if (methodBinder.isApplicableTo(methodName, params)) {
                        return methodBinder;
                    }
                }
            }
        }
        return null;
    }

    private ConstructorBinder bindConstructor(Class constrOwner, Object[] params) {
        for (int i = envs.size() - 1; i >= 0; i--) {
            List<? extends ENVSType> o = envs.get(i);
            List<? extends ENVSType> section = (List<? extends ENVSType>) o;
            for (ENVSType secEl : section) {
                if (secEl instanceof ConstructorBinder) {
                    ConstructorBinder constrBinder = (ConstructorBinder) secEl;
                    if (constrBinder.isApplicableTo(constrOwner, params)) {
                        return constrBinder;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Object visitForEachExpression(ForEachExpression expr, Object object) {
        expr.toIterate.accept(this, object);
        Bag bag;
        QueryResult q = qres.pop();
        if (q instanceof Bag) {
            bag = (Bag) q;
        } else {
            bag = new Bag();
            bag.add(q);
        }
        for (QueryResult t : bag) {
            if (t instanceof XmlId) {
                t = store.get((XmlId) t);
            }
            for (Expression e : expr.exprs) {
                if (t.nested() == null) {
                    envs.push(new ArrayList());
                } else {
                    envs.push(t.nested());
                }
                e.accept(this, object);
                envs.pop();
                qres.pop();
            }
        }
        return null;
    }

    @Override
    public Object visitRangeExpression(RangeExpression expr, Object object) {
        StructSBQL res = new StructSBQL();
        expr.ex1.accept(this, object);
        QueryResult lowBound = qres.pop();
        res.add(lowBound);
        if (expr.ex2 != null && !expr.isUpperUnbounded) {
            expr.ex2.accept(this, object);
            QueryResult upBound = qres.pop();
            res.add(upBound);
        }
        qres.push(res);
        return object;
    }

    @Override
    public Object visitConditionalExpression(ConditionalExpression expr, Object object) {
        expr.conditionExpr.accept(this, object);
        QueryResult cond = qres.pop();
        Boolean b = (Boolean) Utils.toSimpleValue(cond, store);
        QueryResult res;
        if (b) {
            expr.trueExpr.accept(this, object);
            res = qres.pop();
        } else {
            expr.falseExpr.accept(this, object);
            res = qres.pop();
        }
        qres.push(res);
        return object;
    }
}
