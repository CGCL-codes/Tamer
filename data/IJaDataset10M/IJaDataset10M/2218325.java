package org.jmlspecs.jir.ast.jdt.dom;

import java.io.PrintWriter;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.Type;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAllFieldsHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAllFieldsStaticHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAnnBinaryNameHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAnnConvHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAnnGenCastHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAnnPosHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAnnTypeBinaryNameHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAnnTypeHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAnnVarArgsHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAnonymousArrayInstanceHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirAnonymousClassHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirDurationHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirDurationStaticHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirElemHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirElemTypeHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirEquivHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirEverythingHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirFieldHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirFieldStaticHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirFreshHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirImpliesHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirInformalPredHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirInstanceOfHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirInvariantForHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirInvokeConsHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirInvokeHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirInvokeStaticHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirIsInitializedHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirLabelNegHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirLabelPosHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirLocalHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirLockOrderHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirMaxHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirNonNullElementsHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirNotAssignedHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirNotEquivHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirNotModifiedHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirNotSpecifiedHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirNothingHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirOldHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirOnlyAccessedHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirOnlyAssignedHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirOnlyCapturedHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirQuantificationHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirQuantifiedVarHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirReachHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirResultHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirRevImpliesHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirSameHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirSetComprehensionHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirSpaceHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirStrictLockOrderHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirSubtypeHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirThizHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirTypeCastHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirTypeHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirTypeOfHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirWorkingSpaceHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.JirWorkingSpaceStaticHandler;
import org.jmlspecs.jir.ast.jdt.dom.handler.MethodDispatcher;
import org.jmlspecs.jir.binding.BindingFactory;
import org.jmlspecs.jir.binding.IBindingManager;
import org.jmlspecs.jir.binding.JirArrayBinding;
import org.jmlspecs.jir.binding.JirMethodBinding;
import org.jmlspecs.jir.binding.JirOpBinding;
import org.jmlspecs.jir.binding.JirPositionInfo;
import org.jmlspecs.jir.binding.JirTypeBinding;
import org.jmlspecs.jir.binding.JirVariableBinding;

public class JdtJirBindingManager implements IBindingManager<Expression, Type> {

    class Visitor extends ASTVisitor {

        MethodDispatcher md = new MethodDispatcher(JdtJirBindingManager.this, this);

        public void reset() {
            this.md.clearResult();
        }

        @Override
        public boolean visit(final MethodInvocation node) {
            final String methodId = node.getName().getIdentifier();
            if (!this.md.dispatch(methodId, node)) {
                assert false;
                JdtJirBindingManager.this.pwErr.println("Failed to handle: " + node.toString());
            }
            return false;
        }
    }

    protected final PrintWriter pwOut;

    protected final PrintWriter pwErr;

    public static final String JIR_TYPE = "jir.type";

    public static final String JIR_OP = "jir.op";

    public static final String JIR_POS = "jir.pos";

    public static final String JIR_VAR = "jir.var";

    public static final String JIR_ARRAY = "jir.array";

    public static final String JIR_METHOD = "jir.method";

    public static JirTypeBinding<Type> getTypeBinding(final Type t, final String binaryName) {
        if (t.isPrimitiveType()) {
            return BindingFactory.getPrimitiveTypeBinding(t.toString());
        } else if (t.isArrayType()) {
            final ArrayType at = (ArrayType) t;
            final JirTypeBinding<Type> jtb = JdtJirBindingManager.getTypeBinding(at.getElementType(), binaryName == null ? null : binaryName.substring(1));
            return BindingFactory.getTypeBinding(jtb.getTypeName().getBinaryName(), jtb.getTypeName().getOptionalSourceFullyQualifiedName(), at.getDimensions());
        } else {
            final String sourceTypeName = t.toString();
            return BindingFactory.getTypeBinding(binaryName == null ? 'L' + sourceTypeName.replace('.', '/') + ';' : binaryName, sourceTypeName);
        }
    }

    Visitor v = new Visitor();

    {
        this.v.md.registerHandler(JirLocalHandler.class);
        this.v.md.registerHandler(JirThizHandler.class);
        this.v.md.registerHandler(JirFieldHandler.class);
        this.v.md.registerHandler(JirFieldStaticHandler.class);
        this.v.md.registerHandler(JirAllFieldsHandler.class);
        this.v.md.registerHandler(JirAllFieldsStaticHandler.class);
        this.v.md.registerHandler(JirElemHandler.class);
        this.v.md.registerHandler(JirEverythingHandler.class);
        this.v.md.registerHandler(JirNotSpecifiedHandler.class);
        this.v.md.registerHandler(JirNothingHandler.class);
        this.v.md.registerHandler(JirSameHandler.class);
        this.v.md.registerHandler(JirFreshHandler.class);
        this.v.md.registerHandler(JirNonNullElementsHandler.class);
        this.v.md.registerHandler(JirOldHandler.class);
        this.v.md.registerHandler(JirReachHandler.class);
        this.v.md.registerHandler(JirEquivHandler.class);
        this.v.md.registerHandler(JirNotEquivHandler.class);
        this.v.md.registerHandler(JirImpliesHandler.class);
        this.v.md.registerHandler(JirRevImpliesHandler.class);
        this.v.md.registerHandler(JirSubtypeHandler.class);
        this.v.md.registerHandler(JirInstanceOfHandler.class);
        this.v.md.registerHandler(JirTypeCastHandler.class);
        this.v.md.registerHandler(JirNotAssignedHandler.class);
        this.v.md.registerHandler(JirNotModifiedHandler.class);
        this.v.md.registerHandler(JirOnlyAccessedHandler.class);
        this.v.md.registerHandler(JirOnlyAssignedHandler.class);
        this.v.md.registerHandler(JirOnlyCapturedHandler.class);
        this.v.md.registerHandler(JirInformalPredHandler.class);
        this.v.md.registerHandler(JirSetComprehensionHandler.class);
        this.v.md.registerHandler(JirLockOrderHandler.class);
        this.v.md.registerHandler(JirStrictLockOrderHandler.class);
        this.v.md.registerHandler(JirMaxHandler.class);
        this.v.md.registerHandler(JirResultHandler.class);
        this.v.md.registerHandler(JirInvariantForHandler.class);
        this.v.md.registerHandler(JirIsInitializedHandler.class);
        this.v.md.registerHandler(JirSpaceHandler.class);
        this.v.md.registerHandler(JirInvokeHandler.class);
        this.v.md.registerHandler(JirInvokeStaticHandler.class);
        this.v.md.registerHandler(JirInvokeConsHandler.class);
        this.v.md.registerHandler(JirDurationHandler.class);
        this.v.md.registerHandler(JirDurationStaticHandler.class);
        this.v.md.registerHandler(JirWorkingSpaceHandler.class);
        this.v.md.registerHandler(JirWorkingSpaceStaticHandler.class);
        this.v.md.registerHandler(JirTypeHandler.class);
        this.v.md.registerHandler(JirTypeOfHandler.class);
        this.v.md.registerHandler(JirElemTypeHandler.class);
        this.v.md.registerHandler(JirLabelPosHandler.class);
        this.v.md.registerHandler(JirLabelNegHandler.class);
        this.v.md.registerHandler(JirQuantifiedVarHandler.class);
        this.v.md.registerHandler(JirQuantificationHandler.class);
        this.v.md.registerHandler(JirAnonymousClassHandler.class);
        this.v.md.registerHandler(JirAnonymousArrayInstanceHandler.class);
        this.v.md.registerHandler(JirAnnVarArgsHandler.class);
        this.v.md.registerHandler(JirAnnPosHandler.class);
        this.v.md.registerHandler(JirAnnBinaryNameHandler.class);
        this.v.md.registerHandler(JirAnnTypeBinaryNameHandler.class);
        this.v.md.registerHandler(JirAnnTypeHandler.class);
        this.v.md.registerHandler(JirAnnGenCastHandler.class);
        this.v.md.registerHandler(JirAnnConvHandler.class);
    }

    public JdtJirBindingManager() {
        this.pwOut = new PrintWriter(System.out);
        this.pwErr = new PrintWriter(System.err);
    }

    public JdtJirBindingManager(final PrintWriter pwOut, final PrintWriter pwErr) {
        this.pwOut = pwOut;
        this.pwErr = pwErr;
    }

    @Override
    public JirArrayBinding<Type> getArrayBinding(final Expression e) {
        @SuppressWarnings("unchecked") final JirArrayBinding<Type> result = (JirArrayBinding<Type>) e.getProperty(JdtJirBindingManager.JIR_ARRAY);
        return result;
    }

    @Override
    public JirMethodBinding getMethodBinding(final Expression e) {
        return (JirMethodBinding) e.getProperty(JdtJirBindingManager.JIR_METHOD);
    }

    @Override
    public JirOpBinding getOpBinding(final Expression e) {
        assert e instanceof MethodInvocation;
        return (JirOpBinding) e.getProperty(JdtJirBindingManager.JIR_OP);
    }

    @Override
    public JirPositionInfo getPositionInfo(final Expression e) {
        return (JirPositionInfo) e.getProperty(JdtJirBindingManager.JIR_POS);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JirTypeBinding<Type> getTypeBinding(final Expression e) {
        JirTypeBinding<Type> result = (JirTypeBinding<Type>) e.getProperty(JdtJirBindingManager.JIR_TYPE);
        if (result == null) {
            final ASTVisitor visitor = new ASTVisitor() {

                @Override
                public boolean visit(final ParenthesizedExpression node) {
                    final JirTypeBinding<Type> tb = getTypeBinding(node.getExpression());
                    e.setProperty(JdtJirBindingManager.JIR_TYPE, tb);
                    return true;
                }
            };
            e.accept(visitor);
            result = (JirTypeBinding<Type>) e.getProperty(JdtJirBindingManager.JIR_TYPE);
        }
        return result;
    }

    @Override
    public JirVariableBinding<Type> getVariableBinding(final Expression e) {
        @SuppressWarnings("unchecked") final JirVariableBinding<Type> result = (JirVariableBinding<Type>) e.getProperty(JdtJirBindingManager.JIR_VAR);
        return result;
    }

    void m(final Object[] i) {
    }

    @Override
    public Expression resolveAndTransform(final Expression e) {
        e.accept(this.v);
        final Expression result = this.v.md.getResult(e);
        this.v.reset();
        return result;
    }

    @Override
    public void setArrayBinding(final Expression e, final JirArrayBinding<Type> arrayBinding) {
        e.setProperty(JdtJirBindingManager.JIR_ARRAY, arrayBinding);
        final ASTVisitor parenVisitor = new ASTVisitor() {

            @Override
            public boolean visit(final ParenthesizedExpression node) {
                final Expression inParenExp = node.getExpression();
                setArrayBinding(inParenExp, arrayBinding);
                inParenExp.accept(this);
                return false;
            }
        };
        e.accept(parenVisitor);
    }

    @Override
    public void setMethodBinding(final Expression e, final JirMethodBinding methodBinding) {
        e.setProperty(JdtJirBindingManager.JIR_METHOD, methodBinding);
    }

    @Override
    public void setOpBinding(final Expression e, final JirOpBinding opBinding) {
        e.setProperty(JdtJirBindingManager.JIR_OP, opBinding);
    }

    @Override
    public void setPositionInfo(final Expression e, final JirPositionInfo posBinding) {
        e.setProperty(JdtJirBindingManager.JIR_POS, posBinding);
        final ASTVisitor parenVisitor = new ASTVisitor() {

            @Override
            public boolean visit(final ParenthesizedExpression node) {
                final Expression inParenExp = node.getExpression();
                setPositionInfo(inParenExp, posBinding);
                inParenExp.accept(this);
                return false;
            }
        };
        e.accept(parenVisitor);
    }

    @Override
    public void setTypeBinding(final Expression e, final JirTypeBinding<Type> typeBinding) {
        e.setProperty(JdtJirBindingManager.JIR_TYPE, typeBinding);
        final ASTVisitor parenVisitor = new ASTVisitor() {

            @Override
            public boolean visit(final ParenthesizedExpression node) {
                final Expression inParenExp = node.getExpression();
                setTypeBinding(inParenExp, typeBinding);
                inParenExp.accept(this);
                return false;
            }
        };
        e.accept(parenVisitor);
    }

    @Override
    public void setVariableBinding(final Expression e, final JirVariableBinding<Type> varBinding) {
        e.setProperty(JdtJirBindingManager.JIR_VAR, varBinding);
        final ASTVisitor parenVisitor = new ASTVisitor() {

            @Override
            public boolean visit(final ParenthesizedExpression node) {
                final Expression inParenExp = node.getExpression();
                setVariableBinding(inParenExp, varBinding);
                inParenExp.accept(this);
                return false;
            }
        };
        e.accept(parenVisitor);
    }
}
