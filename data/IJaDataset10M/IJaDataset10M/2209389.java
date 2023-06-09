package com.google.gwt.core.ext;

import com.google.gwt.core.ext.linker.Artifact;
import com.google.gwt.core.ext.linker.GeneratedResource;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.resource.ResourceOracle;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * An abstract generator context class which delegates all methods to a provided
 * baseContext. Implementing classes can selectively override individual
 * methods.
 */
public abstract class DelegatingGeneratorContext implements GeneratorContext {

    private final GeneratorContext baseContext;

    /**
   * Get a new instance wrapped from a base {@link GeneratorContext}
   * implementation.
   */
    public DelegatingGeneratorContext(GeneratorContext baseContext) {
        this.baseContext = baseContext;
    }

    @Override
    public boolean checkRebindRuleAvailable(String sourceTypeName) {
        return baseContext.checkRebindRuleAvailable(sourceTypeName);
    }

    @Override
    public void commit(TreeLogger logger, PrintWriter pw) {
        baseContext.commit(logger, pw);
    }

    @Override
    public void commitArtifact(TreeLogger logger, Artifact<?> artifact) throws UnableToCompleteException {
        baseContext.commitArtifact(logger, artifact);
    }

    @Override
    public GeneratedResource commitResource(TreeLogger logger, OutputStream os) throws UnableToCompleteException {
        return baseContext.commitResource(logger, os);
    }

    @Override
    public CachedGeneratorResult getCachedGeneratorResult() {
        return baseContext.getCachedGeneratorResult();
    }

    @Override
    public PropertyOracle getPropertyOracle() {
        return baseContext.getPropertyOracle();
    }

    @Override
    public ResourceOracle getResourcesOracle() {
        return baseContext.getResourcesOracle();
    }

    @Override
    public TypeOracle getTypeOracle() {
        return baseContext.getTypeOracle();
    }

    @Override
    public boolean isGeneratorResultCachingEnabled() {
        return baseContext.isGeneratorResultCachingEnabled();
    }

    @Override
    public boolean isProdMode() {
        return baseContext.isProdMode();
    }

    @Override
    public PrintWriter tryCreate(TreeLogger logger, String packageName, String simpleName) {
        return baseContext.tryCreate(logger, packageName, simpleName);
    }

    @Override
    public OutputStream tryCreateResource(TreeLogger logger, String partialPath) throws UnableToCompleteException {
        return baseContext.tryCreateResource(logger, partialPath);
    }

    @Override
    public boolean tryReuseTypeFromCache(String typeName) {
        return baseContext.tryReuseTypeFromCache(typeName);
    }
}
