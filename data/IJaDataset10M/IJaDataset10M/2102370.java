package com.gorillalogic.dal.common.expr;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.dal.common.table.CommonTableFactory;
import java.util.Vector;

/**
 * <code>MethodExpr</code> provides access to method definitions by
 * wrapping <code>MethodBlock</code>s. For calls from Gosh, the 
 * target <code>MethodBlock</code> is selected in the constructor. 
 * To provide an easier method call from user Java code, the target 
 * <code>MethodBlock</code> is not selected until computeTable(), 
 * i.e. after all the actual arguments have been supplied. (This 
 * obviates the need to first specify the argument types in order 
 * to select the desired method, then provide the actual arguments.)
 *
 * @author <a href="mailto:Brendan@Gosh"></a>
 * @version 1.0
 */
abstract class MethodExpr extends CommonExpr implements CommonParameterizedExpr {

    private CommonType ownerType = null;

    MethodExpr(CommonType ownerType) throws AccessException {
        this.ownerType = ownerType;
    }

    public final String doPath(PathStrategy strategy) {
        String argPath = argPath(strategy);
        return getName() + '(' + argPath + ')';
    }

    abstract String argPath(PathStrategy strategy);

    abstract MethodBlock method() throws AccessException;

    CommonType ownerType() {
        return ownerType;
    }

    public String operator(PathStrategy strategy) {
        return "()";
    }

    public String getFormat() throws AccessException {
        return method().getFormat();
    }

    public String getBody() throws AccessException {
        return method().getBody();
    }

    public boolean collapsable() {
        return true;
    }

    public CommonType commonType() {
        try {
            CommonType rez = method().commonType();
            return rez;
        } catch (AccessException e) {
            throw new InternalException(e);
        }
    }

    public boolean isExplicitlyParenthesized() {
        return true;
    }

    private WithRowArgs withArg() throws AccessException {
        return new WithRowArgs(ownerType, getName());
    }

    public ParameterizedExpr arg(int value) throws AccessException {
        WithRowArgs pex = withArg();
        return pex.arg(value);
    }

    public ParameterizedExpr arg(long value) throws AccessException {
        WithRowArgs pex = withArg();
        return pex.arg(value);
    }

    public ParameterizedExpr arg(float value) throws AccessException {
        WithRowArgs pex = withArg();
        return pex.arg(value);
    }

    public ParameterizedExpr arg(double value) throws AccessException {
        WithRowArgs pex = withArg();
        return pex.arg(value);
    }

    public ParameterizedExpr arg(boolean value) throws AccessException {
        WithRowArgs pex = withArg();
        return pex.arg(value);
    }

    public ParameterizedExpr arg(String value) throws AccessException {
        WithRowArgs pex = withArg();
        return pex.arg(value);
    }

    public ParameterizedExpr arg(Object value) throws AccessException {
        WithRowArgs pex = withArg();
        return pex.arg(value);
    }

    public ParameterizedExpr arg(Table value) throws AccessException {
        WithRowArgs pex = withArg();
        return pex.arg(value);
    }

    public ParameterizedExpr arg(Expr value) throws AccessException {
        WithRowArgs pex = withArg();
        return pex.arg(value);
    }

    abstract CommonRow collectArgsByValue(CommonScope scope) throws AccessException;

    protected CommonTable readTypedTable(CommonScope scope, TableStrategy strategy) throws AccessException {
        return invoke(scope);
    }

    abstract MethodBlock resetMethod() throws AccessException;

    public final void compute(Scope scope) throws AccessException {
        CommonScope cs = ((ExtendedScope) scope).asCommonScope();
        invoke(cs);
    }

    private MethodBlock wiz(MethodBlock method) throws AccessException {
        if (method.outOfDate()) {
            MethodBlock oldBlock = method;
            method = resetMethod();
            if (method == null) {
                method = oldBlock;
                String nm = getName();
                String msg = "Expr resolving method \"" + nm + "\" is no longer valid";
                throw new OperationException.Invalid(msg);
            }
        }
        return method;
    }

    private long invocationCounter = 0;

    private CommonTable invoke(CommonScope scope) throws AccessException {
        CommonScope argScope = FnExpr.resetArgumentScope(scope);
        CommonRow actualArgs = collectArgsByValue(argScope);
        MethodBlock method = method();
        CommonType type = method.commonType();
        boolean returnValue = type != CommonType.XVOID;
        String pname = method.getName() + "_args" + invocationCounter++;
        actualArgs.setName(pname);
        CommonTable rez = null;
        CommonItr itr = scope.commonData().commonLoopLock();
        while (itr.next()) {
            if (returnValue && rez != null) {
                throw new UnsupportedException("Invoking value-returning methods on a multi-row table");
            }
            CommonScope ns = scope.invocation().makeNestedScope(itr.asCommonRow(), actualArgs);
            method = itr.mostDerivedCommonForm().commonType().method(method.getName(), null, true);
            method = wiz(method);
            if (returnValue) {
                rez = method.computeTable(ns);
                if (rez == null) {
                    String msg = "Attempt to accessing null return value from method \"" + method.getName() + "\"";
                    throw new StructureException(msg);
                }
                String fp = method.getName() + "(...)";
                rez = CommonTable.commonFactory.renameAs(rez, fp);
            } else {
                method.compute(ns);
            }
        }
        if (!returnValue) {
            rez = CommonType.XVOID.commonExtent();
        }
        return rez;
    }

    /**
	 * <code>WithExprArgs</code> takes arguments supplied at creation as
	 * an <code>Expr</code>. This form is used from Gosh.
	 *
	 */
    static class WithExprArgs extends MethodExpr {

        private CommonExpr args = null;

        private MethodBlock method = null;

        WithExprArgs(CommonType ownerType, MethodBlock block, CommonExpr args) throws AccessException {
            super(ownerType);
            this.method = block;
            this.args = args;
            check();
        }

        WithExprArgs(CommonType ownerType, String methodName, CommonExpr args) throws AccessException {
            super(ownerType);
            if (args == null) {
                args = new VoidExpr();
            }
            this.args = args;
            CommonType formals = args.asFormalParameters();
            this.method = ownerType.method(methodName, formals, true);
            check();
        }

        private void check() {
            if (method == null) {
                throw new InternalException("Null block in MethodExpr");
            }
        }

        MethodBlock method() {
            return method;
        }

        MethodBlock resetMethod() throws AccessException {
            String methodName = method.getName();
            CommonType formals = args.asFormalParameters();
            return method = ownerType().method(methodName, formals, true);
        }

        public String getName() {
            return method().getName();
        }

        String argPath(PathStrategy strategy) {
            String sa = "";
            if (args != null) sa = args.path(strategy);
            return sa;
        }

        CommonRow collectArgsByValue(CommonScope scope) throws AccessException {
            CommonRow row = CommonTable.commonFactory.makeTempCommonRow();
            args.bindArgByValue(scope, row);
            return row;
        }
    }

    /**
	 * <code>WithRowArgs</code> is used to collect expliclity-supplied
	 * arg() values, which are supplied as arguments. This form can be
	 * called programmatically from Java.
	 *
	 */
    static class WithRowArgs extends MethodExpr {

        private CommonRow row = CommonTable.commonFactory.makeTempCommonRow();

        private String methodName;

        private MethodBlock method = null;

        WithRowArgs(CommonType ownerType, String methodName) throws AccessException {
            super(ownerType);
            this.methodName = methodName;
        }

        public String getName() {
            return methodName;
        }

        MethodBlock method() throws AccessException {
            if (method == null) {
                method = ownerType().method(methodName, row, true);
            }
            return method;
        }

        MethodBlock resetMethod() throws AccessException {
            return method = ownerType().method(methodName, row, true);
        }

        String argPath(PathStrategy strategy) {
            return row.getContentAsString();
        }

        CommonRow collectArgsByValue(CommonScope scope) {
            return row;
        }

        private void reset() {
            method = null;
        }

        private String nm() {
            return String.valueOf(row.columnCount());
        }

        public ParameterizedExpr arg(int value) throws AccessException {
            row.add(nm(), value);
            reset();
            return this;
        }

        public ParameterizedExpr arg(long value) throws AccessException {
            row.add(nm(), value);
            reset();
            return this;
        }

        public ParameterizedExpr arg(float value) throws AccessException {
            row.add(nm(), value);
            reset();
            return this;
        }

        public ParameterizedExpr arg(double value) throws AccessException {
            row.add(nm(), value);
            reset();
            return this;
        }

        public ParameterizedExpr arg(boolean value) throws AccessException {
            row.add(nm(), value);
            reset();
            return this;
        }

        public ParameterizedExpr arg(String value) throws AccessException {
            row.add(nm(), value);
            reset();
            return this;
        }

        public ParameterizedExpr arg(Object value) throws AccessException {
            row.add(nm(), value);
            reset();
            return this;
        }

        public ParameterizedExpr arg(Table value) throws AccessException {
            row.add(nm(), value);
            reset();
            return this;
        }

        public ParameterizedExpr arg(Expr value) throws AccessException {
            row.add(nm(), value);
            reset();
            return this;
        }
    }
}
