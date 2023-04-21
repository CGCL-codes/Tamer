package net.sf.refactorit.query.usage;

import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinClass;
import net.sf.refactorit.classmodel.BinConstructor;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinInterface;
import net.sf.refactorit.classmodel.BinLocalVariable;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.classmodel.BinTypeRefVisitor;
import net.sf.refactorit.classmodel.CompilationUnit;
import net.sf.refactorit.classmodel.SourceConstruct;
import net.sf.refactorit.classmodel.expressions.BinAnnotationExpression;
import net.sf.refactorit.classmodel.expressions.BinCITypeExpression;
import net.sf.refactorit.classmodel.expressions.BinCastExpression;
import net.sf.refactorit.classmodel.expressions.BinConstructorInvocationExpression;
import net.sf.refactorit.classmodel.expressions.BinMethodInvocationExpression;
import net.sf.refactorit.classmodel.expressions.BinNewExpression;
import net.sf.refactorit.common.util.Assert;
import net.sf.refactorit.parser.ASTImpl;
import net.sf.refactorit.query.usage.filters.BinClassSearchFilter;
import net.sf.refactorit.query.usage.filters.BinInterfaceSearchFilter;
import net.sf.refactorit.query.usage.filters.BinMethodSearchFilter;
import net.sf.refactorit.query.usage.filters.SearchFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Searches for:<UL>
 * <LI>Declaration of local variable, parameter of exactly
 * given type.
 * <LI><CODE>extends</CODE>, <CODE>implements</CODE>, <CODE>throws</CODE>,
 * <CODE>throw</CODE>, <CODE>catch</CODE>, <CODE>new</CODE>, <CODE>import</CODE>
 * - also exact type match.
 * <LI>Usage of member of this type: fields, methods, constructors, implicit
 * <CODE>toString()</CODE> call. Here sub/super filters apply.
 * <LI><TYPE>.<CODE>class</CODE>, <TYPE>.<CODE>super</CODE>,
 * <TYPE>.<CODE>this</CODE> expressions.
 * <LI>Casts to the given type.
 * <LI><CODE>instanceof</CODE> expressions.
 * </UL>
 *
 * @author Anton Safonov
 */
public class TypeIndexer extends TargetIndexer {

    private BinTypeRef objectRef = null;

    protected TypeRefVisitor typeRefVisitor;

    /** It used to fail on multi var declarations, like
   * Object a, b, c;
   * to prevent reporting Object node 3 times.
   */
    private final Set typesOfVariablesDeclarations = new HashSet(10);

    public class TypeRefVisitor extends BinTypeRefVisitor {

        private Object location;

        private SourceConstruct inConstruct;

        private Set alreadyAddedNodes;

        private final BinTypeRef target = getTypeRef();

        public final void init(Object location, SourceConstruct inConstruct, Set alreadyAddedNodes) {
            this.location = location;
            this.inConstruct = inConstruct;
            this.alreadyAddedNodes = alreadyAddedNodes;
        }

        public void visit(final BinTypeRef typeRef) {
            final BinTypeRef toCheck = typeRef.getTypeRef();
            if (target == toCheck || target.equals(toCheck)) {
                Object loc = this.location;
                if (loc == null) {
                    loc = getSupervisor().getCurrentLocation();
                }
                ASTImpl node = typeRef.getNode();
                if (alreadyAddedNodes != null) {
                    if (!alreadyAddedNodes.add(node)) {
                        node = null;
                    }
                }
                if (node != null) {
                    getSupervisor().addInvocation(getTarget(), loc, node, inConstruct);
                }
            }
            super.visit(typeRef);
        }
    }

    public TypeIndexer(final ManagingIndexer supervisor, final BinCIType target, final boolean includeSupertypes, final boolean includeSubtypes) {
        this(supervisor, target, new BinClassSearchFilter(includeSupertypes, includeSubtypes));
    }

    public TypeIndexer(final ManagingIndexer supervisor, final BinCIType target, final SearchFilter filter) {
        super(supervisor, target, target, filter);
        registerMemberDelegates();
        objectRef = target.getProject().getObjectRef();
        typeRefVisitor = new TypeRefVisitor();
        typeRefVisitor.setIncludeNewExpressions(((BinClassSearchFilter) getFilter()).isIncludeNewExpressions());
        typeRefVisitor.setCheckTypeSelfDeclaration(false);
    }

    /**
   * @param source source file
   */
    public void visit(final CompilationUnit source) {
        typeRefVisitor.init(source, null, null);
        source.accept(typeRefVisitor);
    }

    /** this covers extends and implements statements */
    public final void visit(final BinCIType type) {
        this.typesOfVariablesDeclarations.clear();
        super.visit(type);
        typeRefVisitor.init(type.getTypeRef(), null, null);
        type.accept(typeRefVisitor);
        if (type.isClass() && !isSearchForNames()) {
            if (getTypeRef().equals(objectRef) && type.getTypeRef().getSuperclass().equals(objectRef)) {
                boolean foundExplicitObject = false;
                List typeRefs = type.getSpecificSuperTypeRefs();
                if (typeRefs != null) {
                    for (int i = 0, max = typeRefs.size(); i < max; i++) {
                        if (((BinTypeRef) typeRefs.get(i)).getTypeRef().equals(objectRef)) {
                            foundExplicitObject = true;
                            break;
                        }
                    }
                }
                if (!foundExplicitObject) {
                    getSupervisor().addInvocation(getTarget(), type.getTypeRef(), type.getNameAstOrNull());
                }
            }
        }
    }

    /** this covers cast statements */
    public final void visit(final BinCastExpression x) {
        typeRefVisitor.init(null, x, null);
        x.accept(typeRefVisitor);
    }

    /**
   * All such expressions are checked by ConstructorIndexer now, since
   * we always have at least one (default) constructor in the list
   *
   * @param x expression of new instance creation
   */
    public final void visit(final BinNewExpression x) {
        boolean oldVal = typeRefVisitor.isIncludeNewExpressions();
        if (x.getTypeRef().isArray()) {
            typeRefVisitor.setIncludeNewExpressions(true);
        }
        typeRefVisitor.init(getSupervisor().getCurrentLocation(), null, null);
        x.accept(typeRefVisitor);
        typeRefVisitor.setIncludeNewExpressions(oldVal);
    }

    /** field declarations:
   *  <pre>
   *  A a = null;
   *  B b;
   *  </pre>
   */
    public final void visit(final BinField field) {
        typeRefVisitor.init(getSupervisor().getCurrentType(), field, this.typesOfVariablesDeclarations);
        field.accept(typeRefVisitor);
    }

    public final void visit(final BinLocalVariable variable) {
        typeRefVisitor.init(getSupervisor().getCurrentLocation(), variable, this.typesOfVariablesDeclarations);
        variable.accept(typeRefVisitor);
    }

    /**
   * This is correct one for static fields and methods invocations
   */
    public final void visit(final BinCITypeExpression x) {
        typeRefVisitor.init(null, x, null);
        x.accept(typeRefVisitor);
    }

    /** type usage in method declaration */
    public final void visit(final BinMethod method) {
        typeRefVisitor.init(method, null, null);
        method.accept(typeRefVisitor);
    }

    public final void visit(final BinAnnotationExpression x) {
        typeRefVisitor.init(null, x, null);
        x.accept(typeRefVisitor);
    }

    /** throws clause */
    public final void visit(final BinMethod.Throws exception) {
        typeRefVisitor.init(exception.getParent(), null, null);
        exception.accept(typeRefVisitor);
    }

    /**
   * Check usage of inherited and not overriden methods.
   * Declared methods are checked by appropriate method indexer.
   */
    public void visit(final BinMethodInvocationExpression x) {
        final BinMethod method = x.getMethod();
        final BinTypeRef invokedOn;
        if (method.isStatic()) {
            invokedOn = method.getOwner();
        } else {
            invokedOn = x.getInvokedOn();
        }
        BinTypeRef typeRef = getTypeRef();
        if ((invokedOn == typeRef || invokedOn.equals(typeRef)) && getType().getDeclaredMethod(method.getName(), method.getParameters()) == null) {
            getSupervisor().addInvocation(method, getSupervisor().getCurrentLocation(), x.getNameAst());
        }
        typeRefVisitor.init(null, x, null);
        x.accept(typeRefVisitor);
    }

    public void visit(final BinConstructorInvocationExpression x) {
        typeRefVisitor.init(null, x, null);
        x.accept(typeRefVisitor);
    }

    protected void registerMemberDelegates() {
        final BinCIType type = (BinCIType) getTarget();
        if (((BinClassSearchFilter) getFilter()).isFieldUsages()) {
            final BinField[] fields = type.getDeclaredFields();
            for (int pos = 0, max = fields.length; pos < max; pos++) {
                new FieldIndexer(getSupervisor(), fields[pos], getFilter().isIncludeSubtypes());
            }
        }
        if ((type.isClass() || type.isEnum()) && !type.isTypeParameter() && !type.isWildcard()) {
            try {
                final BinConstructor[] constructors = ((BinClass) type).getConstructors();
                for (int pos = 0, max = constructors.length; pos < max; pos++) {
                    new ConstructorIndexer(getSupervisor(), constructors[pos]);
                }
            } catch (ClassCastException ex) {
                if (Assert.enabled) {
                    System.err.println("tried to cast to BinClass type: " + type);
                    ex.printStackTrace();
                }
            }
        }
        if (((BinClassSearchFilter) getFilter()).isMethodUsages()) {
            final BinMethod[] methods = type.getDeclaredMethods();
            for (int pos = 0, max = methods.length; pos < max; pos++) {
                new MethodIndexer(getSupervisor(), methods[pos], new BinMethodSearchFilter(true, true, getFilter().isIncludeSupertypes(), getFilter().isIncludeSubtypes(), getFilter().isShowDuplicates(), getFilter().isGoToSingleUsage(), true, getFilter().isRunWithDefaultSettings(), getFilter().isSkipSelf()));
            }
        }
        final BinTypeRef[] types = type.getDeclaredTypes();
        for (int pos = 0, max = types.length; pos < max; pos++) {
            final BinClassSearchFilter filter;
            if (types[pos].getBinCIType().isClass() || types[pos].getBinCIType().isEnum()) {
                filter = new BinClassSearchFilter(((BinClassSearchFilter) getFilter()).isUsages(), ((BinClassSearchFilter) getFilter()).isImportStatements(), ((BinClassSearchFilter) getFilter()).isMethodUsages(), ((BinClassSearchFilter) getFilter()).isFieldUsages(), getFilter().isIncludeSupertypes(), getFilter().isIncludeSubtypes(), getFilter().isShowDuplicates(), getFilter().isGoToSingleUsage(), getFilter().isSearchNonJavaFiles(), getFilter().isRunWithDefaultSettings(), ((BinClassSearchFilter) getFilter()).isSkipSelf());
            } else {
                filter = new BinInterfaceSearchFilter((BinInterface) types[pos].getBinCIType(), ((BinClassSearchFilter) getFilter()).isUsages(), ((BinClassSearchFilter) getFilter()).isImportStatements(), ((BinClassSearchFilter) getFilter()).isMethodUsages(), ((BinClassSearchFilter) getFilter()).isFieldUsages(), getFilter().isIncludeSupertypes(), getFilter().isIncludeSubtypes(), ((getFilter() instanceof BinInterfaceSearchFilter) ? ((BinInterfaceSearchFilter) getFilter()).isImplementers() : true), getFilter().isShowDuplicates(), getFilter().isGoToSingleUsage(), getFilter().isSearchNonJavaFiles(), getFilter().isRunWithDefaultSettings(), ((BinClassSearchFilter) getFilter()).isSkipSelf());
            }
            filter.setIncludeNewExpressions(((BinClassSearchFilter) getFilter()).isIncludeNewExpressions());
            new TypeIndexer(getSupervisor(), types[pos].getBinCIType(), filter);
        }
    }
}
