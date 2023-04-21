package net.sf.fit4oaw.fixture.oaw5.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.internal.xpand2.pr.ProtectedRegionResolver;
import org.eclipse.internal.xtend.util.Cache;
import org.eclipse.internal.xtend.util.Pair;
import org.eclipse.internal.xtend.util.Triplet;
import org.eclipse.internal.xtend.xtend.ast.Around;
import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.output.Output;
import org.eclipse.xtend.expression.Callback;
import org.eclipse.xtend.expression.ExceptionHandler;
import org.eclipse.xtend.expression.NullEvaluationHandler;
import org.eclipse.xtend.expression.Resource;
import org.eclipse.xtend.expression.ResourceManager;
import org.eclipse.xtend.expression.TypeSystemImpl;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.Type;

/**
 * This specific implementation is needed to enable registering of global
 * variables.
 * 
 * @author Karsten Thoms
 */
public class Fit4oawXpandExecutionContext extends XpandExecutionContextImpl {

    public Fit4oawXpandExecutionContext(Output output, ProtectedRegionResolver prs) {
        super(output, prs);
    }

    public Fit4oawXpandExecutionContext(ResourceManager resourceManager, Resource currentResource, TypeSystemImpl typeSystem, Map<String, Variable> vars, Map<String, Variable> globalVars, Output output, ProtectedRegionResolver protectedRegionResolver, ProgressMonitor monitor, ExceptionHandler exceptionHandler, List<Around> advices, NullEvaluationHandler nullEvaluationHandler, Map<Resource, Set<Extension>> allExtensionsPerResource, Callback callback, Cache<Triplet<Resource, String, List<Type>>, Extension> extensionsForNameAndTypesCache, Map<Pair<String, List<Type>>, Type> extensionsReturnTypeCache) {
        super(resourceManager, currentResource, typeSystem, vars, globalVars, output, protectedRegionResolver, monitor, exceptionHandler, advices, nullEvaluationHandler, allExtensionsPerResource, callback, extensionsForNameAndTypesCache, extensionsReturnTypeCache);
    }

    public XpandExecutionContextImpl cloneWithGlobalVar(Variable globalVar) {
        Map<String, Variable> globalVars = new HashMap<String, Variable>();
        globalVars.putAll(getGlobalVariables());
        globalVars.put(globalVar.getName(), globalVar);
        return new Fit4oawXpandExecutionContext(resourceManager, currentResource(), typeSystem, getVisibleVariables(), globalVars, output, protectedRegionResolver, getMonitor(), exceptionHandler, getExtensionAdvices(), nullEvaluationHandler, allExtensionsPerResource, callback, extensionsForNameAndTypesCache, extensionsReturnTypeCache);
    }

    @Override
    public XpandExecutionContextImpl cloneContext() {
        final Fit4oawXpandExecutionContext result = new Fit4oawXpandExecutionContext(resourceManager, currentResource(), typeSystem, getVisibleVariables(), getGlobalVariables(), output, protectedRegionResolver, getMonitor(), exceptionHandler, registeredExtensionAdvices, nullEvaluationHandler, allExtensionsPerResource, callback, extensionsForNameAndTypesCache, extensionsReturnTypeCache);
        result.getExtensionAdvices().addAll(registeredExtensionAdvices);
        return result;
    }
}
