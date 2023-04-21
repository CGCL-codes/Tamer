package com.google.gwt.dev.jdt;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.javac.CompilationState;
import com.google.gwt.dev.javac.CompilationUnit;
import com.google.gwt.dev.javac.CompiledClass;
import com.google.gwt.dev.javac.JdtCompiler.CompilationUnitAdapter;
import com.google.gwt.dev.jdt.FindDeferredBindingSitesVisitor.MessageSendSite;
import com.google.gwt.dev.jjs.impl.FragmentLoaderCreator;
import com.google.gwt.dev.util.Empty;
import com.google.gwt.dev.util.JsniRef;
import com.google.gwt.dev.util.Util;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides a reusable front-end based on the JDT compiler that incorporates
 * GWT-specific concepts such as JSNI and deferred binding.
 */
public class WebModeCompilerFrontEnd extends AbstractCompiler {

    private final FragmentLoaderCreator fragmentLoaderCreator;

    private final RebindPermutationOracle rebindPermOracle;

    /**
   * Construct a WebModeCompilerFrontEnd. The reason a
   * {@link FragmentLoaderCreator} needs to be passed in is that it uses
   * generator infrastructure, and therefore needs access to more parts of the
   * compiler than WebModeCompilerFrontEnd currently has.
   */
    public WebModeCompilerFrontEnd(CompilationState compilationState, RebindPermutationOracle rebindPermOracle, FragmentLoaderCreator fragmentLoaderCreator) {
        super(compilationState, false);
        this.rebindPermOracle = rebindPermOracle;
        this.fragmentLoaderCreator = fragmentLoaderCreator;
    }

    /**
   * Build the initial set of compilation units.
   */
    public CompilationUnitDeclaration[] getCompilationUnitDeclarations(TreeLogger logger, String[] seedTypeNames) throws UnableToCompleteException {
        TypeOracle oracle = compilationState.getTypeOracle();
        Set<JClassType> intfTypes = oracle.getSingleJsoImplInterfaces();
        Map<String, CompiledClass> classMapBySource = compilationState.getClassFileMapBySource();
        Set<CompilationUnit> alreadyAdded = new HashSet<CompilationUnit>();
        List<ICompilationUnit> icus = new ArrayList<ICompilationUnit>(seedTypeNames.length + intfTypes.size());
        for (String seedTypeName : seedTypeNames) {
            CompilationUnit unit = getUnitForType(logger, classMapBySource, seedTypeName);
            if (alreadyAdded.add(unit)) {
                icus.add(new CompilationUnitAdapter(unit));
            } else {
                logger.log(TreeLogger.WARN, "Duplicate compilation unit '" + unit.getDisplayLocation() + "'in seed types");
            }
        }
        for (JClassType intf : intfTypes) {
            String implName = oracle.getSingleJsoImpl(intf).getQualifiedSourceName();
            CompilationUnit unit = getUnitForType(logger, classMapBySource, implName);
            if (alreadyAdded.add(unit)) {
                icus.add(new CompilationUnitAdapter(unit));
                logger.log(TreeLogger.SPAM, "Forced compilation of unit '" + unit.getDisplayLocation() + "' becasue it contains a SingleJsoImpl type");
            }
        }
        CompilationUnitDeclaration[] cuds = compile(logger, icus.toArray(new ICompilationUnit[icus.size()]));
        return cuds;
    }

    public RebindPermutationOracle getRebindPermutationOracle() {
        return rebindPermOracle;
    }

    @Override
    protected void doCompilationUnitDeclarationValidation(CompilationUnitDeclaration cud, TreeLogger logger) {
    }

    /**
   * Pull in types referenced only via JSNI.
   */
    @Override
    protected String[] doFindAdditionalTypesUsingJsni(TreeLogger logger, CompilationUnitDeclaration cud) {
        FindJsniRefVisitor v = new FindJsniRefVisitor();
        cud.traverse(v, cud.scope);
        Set<String> jsniRefs = v.getJsniRefs();
        Set<String> dependentTypeNames = new HashSet<String>();
        for (String jsniRef : jsniRefs) {
            JsniRef parsed = JsniRef.parse(jsniRef);
            if (parsed != null) {
                dependentTypeNames.add(parsed.className());
            }
        }
        return dependentTypeNames.toArray(Empty.STRINGS);
    }

    /**
   * Pull in types implicitly referenced through rebind answers.
   */
    @Override
    protected String[] doFindAdditionalTypesUsingRebinds(TreeLogger logger, CompilationUnitDeclaration cud) {
        Set<String> dependentTypeNames = new HashSet<String>();
        FindDeferredBindingSitesVisitor v = new FindDeferredBindingSitesVisitor();
        cud.traverse(v, cud.scope);
        Map<String, MessageSendSite> requestedTypes = v.getSites();
        for (String reqType : requestedTypes.keySet()) {
            MessageSendSite site = requestedTypes.get(reqType);
            try {
                String[] resultTypes = rebindPermOracle.getAllPossibleRebindAnswers(logger, reqType);
                for (int i = 0; i < resultTypes.length; ++i) {
                    String typeName = resultTypes[i];
                    ReferenceBinding type = resolvePossiblyNestedType(typeName);
                    if (type == null) {
                        FindDeferredBindingSitesVisitor.reportRebindProblem(site, "Rebind result '" + typeName + "' could not be found");
                        continue;
                    }
                    if (!type.isClass()) {
                        FindDeferredBindingSitesVisitor.reportRebindProblem(site, "Rebind result '" + typeName + "' must be a class");
                        continue;
                    }
                    if (type.isAbstract()) {
                        FindDeferredBindingSitesVisitor.reportRebindProblem(site, "Rebind result '" + typeName + "' cannot be abstract");
                        continue;
                    }
                    if (type.isNestedType() && !type.isStatic()) {
                        FindDeferredBindingSitesVisitor.reportRebindProblem(site, "Rebind result '" + typeName + "' cannot be a non-static nested class");
                        continue;
                    }
                    if (type.isLocalType()) {
                        FindDeferredBindingSitesVisitor.reportRebindProblem(site, "Rebind result '" + typeName + "' cannot be a local class");
                        continue;
                    }
                    MethodBinding noArgCtor = type.getExactConstructor(TypeBinding.NO_PARAMETERS);
                    if (noArgCtor == null) {
                        FindDeferredBindingSitesVisitor.reportRebindProblem(site, "Rebind result '" + typeName + "' has no default (zero argument) constructors");
                        continue;
                    }
                    dependentTypeNames.add(typeName);
                }
                Util.addAll(dependentTypeNames, resultTypes);
            } catch (UnableToCompleteException e) {
                FindDeferredBindingSitesVisitor.reportRebindProblem(site, "Failed to resolve '" + reqType + "' via deferred binding");
            }
        }
        for (MessageSendSite site : v.getRunAsyncSites()) {
            FragmentLoaderCreator loaderCreator = fragmentLoaderCreator;
            String resultType;
            try {
                resultType = loaderCreator.create(logger);
                dependentTypeNames.add(resultType);
            } catch (UnableToCompleteException e) {
                FindDeferredBindingSitesVisitor.reportRebindProblem(site, "Failed to create a runAsync fragment loader");
            }
        }
        return dependentTypeNames.toArray(Empty.STRINGS);
    }

    /**
   * Get the CompilationUnit for a named type or throw an
   * UnableToCompleteException.
   */
    private CompilationUnit getUnitForType(TreeLogger logger, Map<String, CompiledClass> classMapBySource, String typeName) throws UnableToCompleteException {
        CompiledClass compiledClass = classMapBySource.get(typeName);
        if (compiledClass == null) {
            logger.log(TreeLogger.ERROR, "Unable to find compilation unit for type '" + typeName + "'");
            throw new UnableToCompleteException();
        }
        assert compiledClass.getUnit() != null;
        return compiledClass.getUnit();
    }
}
