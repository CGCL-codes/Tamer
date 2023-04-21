package net.sf.refactorit.query.usage;

import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinClass;
import net.sf.refactorit.classmodel.BinConstructor;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinInterface;
import net.sf.refactorit.classmodel.BinItem;
import net.sf.refactorit.classmodel.BinLocalVariable;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinPackage;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.classmodel.BinVariable;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.classmodel.SourceConstruct;
import net.sf.refactorit.classmodel.expressions.BinFieldInvocationExpression;
import net.sf.refactorit.classmodel.expressions.BinLogicalExpression;
import net.sf.refactorit.classmodel.expressions.BinMethodInvocationExpression;
import net.sf.refactorit.classmodel.statements.BinLabeledStatement;
import net.sf.refactorit.common.util.Assert;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.parser.ASTImpl;
import net.sf.refactorit.query.ProgressMonitor;
import net.sf.refactorit.query.usage.filters.BinClassSearchFilter;
import net.sf.refactorit.query.usage.filters.BinInterfaceSearchFilter;
import net.sf.refactorit.query.usage.filters.BinMethodSearchFilter;
import net.sf.refactorit.query.usage.filters.BinPackageSearchFilter;
import net.sf.refactorit.query.usage.filters.BinVariableSearchFilter;
import net.sf.refactorit.query.usage.filters.SearchFilter;
import net.sf.refactorit.ui.module.RefactorItActionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anton Safonov
 */
public final class Finder {

    /** time measurement */
    private static long lastTime = 0;

    private static final Map invocationMap = new HashMap();

    private Finder() {
    }

    public static final List getInvocations(final Project project, final Object bin, final SearchFilter filter) {
        if (Assert.enabled && !IDEController.runningTest()) {
            System.err.println("Invocations of: " + bin);
        }
        printTimeElapsed(null);
        ManagingIndexer supervisor = new ManagingIndexer();
        Object target = bin;
        List result = null;
        if (bin instanceof Object[]) {
            Object[] data = (Object[]) bin;
            for (int i = 0; i < data.length; i++) {
                registerIndexers(supervisor, data[i], filter);
            }
        } else {
            registerIndexers(supervisor, target, filter);
        }
        supervisor.callVisit(RefactorItActionUtils.unwrapTarget(bin), filter.isIncludeSupertypes());
        result = supervisor.getInvocations();
        supervisor = null;
        if (Assert.enabled && !IDEController.runningTest()) {
            printTimeElapsed("Found usages in");
        }
        return result;
    }

    private static void registerIndexers(final ManagingIndexer supervisor, final Object target, SearchFilter filter) {
        if (target instanceof BinConstructor) {
            new ConstructorIndexer(supervisor, (BinConstructor) target);
        } else if (target instanceof BinMethod) {
            if (!(filter instanceof BinMethodSearchFilter)) {
                filter = new BinMethodSearchFilter(filter);
            }
            new MethodIndexer(supervisor, (BinMethod) target, (BinMethodSearchFilter) filter);
        } else if (target instanceof BinInterface) {
            if (!(filter instanceof BinInterfaceSearchFilter)) {
                filter = new BinInterfaceSearchFilter((BinInterface) target, filter);
            }
            new TypeIndexer(supervisor, (BinCIType) target, filter);
        } else if (target instanceof BinCIType) {
            if (!(filter instanceof BinClassSearchFilter)) {
                filter = new BinClassSearchFilter(filter);
            }
            new TypeIndexer(supervisor, (BinCIType) target, filter);
        } else if (target instanceof BinPackage) {
            boolean includeSubPackages = false;
            if (filter instanceof BinPackageSearchFilter) {
                includeSubPackages = ((BinPackageSearchFilter) filter).isIncludeSubPackages();
            }
            new PackageIndexer(supervisor, (BinPackage) target, filter.isIncludeSubtypes(), filter.isIncludeSupertypes(), includeSubPackages);
        } else if (target instanceof BinField) {
            new FieldIndexer(supervisor, (BinField) target, filter.isIncludeSubtypes());
        } else if (target instanceof BinLabeledStatement) {
            new LabelIndexer(supervisor, (BinLabeledStatement) target);
        } else if (target instanceof BinLocalVariable) {
            new LocalVariableIndexer(supervisor, (BinLocalVariable) target);
        } else if (target instanceof BinFieldInvocationExpression) {
            new FieldIndexer(supervisor, (BinFieldInvocationExpression) target, filter.isIncludeSubtypes(), filter.isIncludeSupertypes());
        } else if (target instanceof BinMethodInvocationExpression) {
            if (!(filter instanceof BinMethodSearchFilter)) {
                filter = new BinMethodSearchFilter(filter);
            }
            new MethodIndexer(supervisor, ((BinMethodInvocationExpression) target).getMethod(), ((BinMethodInvocationExpression) target).getInvokedOn().getBinCIType(), (BinMethodSearchFilter) filter);
        } else {
            if (Assert.enabled) {
                Assert.must(false, "no indexer found for: " + target + " (" + target.getClass() + ")");
            }
        }
    }

    private static void printTimeElapsed(String message) {
        long curTime = System.currentTimeMillis();
        if (lastTime > 0 && message != null) {
            System.err.println(message + ": " + (curTime - lastTime) + "ms");
        }
        lastTime = curTime;
    }

    public static final List getInvocations(BinItem item) {
        return getInvocations(item, null);
    }

    public static final List getInvocations(BinItem item, SearchFilter filter) {
        List invocations = (List) invocationMap.get(item);
        if (invocations == null) {
            if (invocationMap.size() > 500) {
                invocationMap.clear();
            }
            ManagingIndexer supervisor = null;
            if (item instanceof BinCIType) {
                supervisor = new ManagingIndexer(ProgressMonitor.Progress.FULL);
            } else {
                supervisor = new ManagingIndexer(ProgressMonitor.Progress.DONT_SHOW);
            }
            if (filter == null) {
                if (item instanceof BinVariable) {
                    filter = new BinVariableSearchFilter(true, true, true, false, false, true);
                } else if (item instanceof BinMethod) {
                    filter = new BinMethodSearchFilter(true, false, true, true, true, false, true, false, false);
                } else if (item instanceof BinClass) {
                    filter = new BinClassSearchFilter(true, true, true, true, false, false, true, false, false, false, false);
                } else if (item instanceof BinInterface) {
                    filter = new BinInterfaceSearchFilter((BinInterface) item, true, true, true, true, false, false, true, true, false, false, false, false);
                } else if (item instanceof BinPackage) {
                    filter = new BinPackageSearchFilter(true, true, true, false, false, false, false, true, false, false);
                } else {
                    Assert.must(false, "Unhandled member: " + item + ", member type: " + item.getClass());
                }
            }
            registerIndexers(supervisor, item, filter);
            supervisor.callVisit(item, filter.isIncludeSupertypes());
            invocations = supervisor.getInvocations();
            invocationMap.put(item, invocations);
        }
        return invocations;
    }

    public static final void clearInvocationMap() {
        invocationMap.clear();
    }
}

final class EqualsIndexer extends TargetIndexer {

    private final BinTypeRef ref;

    public EqualsIndexer(final ManagingIndexer supervisor, Project project) {
        super(supervisor, null, null);
        ref = project.getTypeRefForName("net.sf.refactorit.classmodel.BinTypeRef");
    }

    public final void visit(final BinLogicalExpression expression) {
        BinTypeRef rightType = expression.getRightExpression().getReturnType();
        BinTypeRef leftType = expression.getLeftExpression().getReturnType();
        if (((rightType != null && rightType.isDerivedFrom(ref)) || (leftType != null && leftType.isDerivedFrom(ref))) && !(rightType == null || leftType == null)) {
            addInvocation(expression.getRootAst(), expression);
        }
    }

    private void addInvocation(final ASTImpl ast, final SourceConstruct construct) {
        getSupervisor().addInvocation(getTarget(), getSupervisor().getCurrentLocation(), ast, construct);
    }
}
