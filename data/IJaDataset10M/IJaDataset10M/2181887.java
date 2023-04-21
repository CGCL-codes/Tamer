package com.fastaop.advice.builder.impl;

import java.lang.annotation.Annotation;
import com.fastaop.advice.IPointcut;
import com.fastaop.advice.Pointcut;
import com.fastaop.advice.builder.IMatcherProvider;
import com.fastaop.advice.builder.IMethodAnnotationBuilder;
import com.fastaop.advice.builder.IMethodBuilder;
import com.fastaop.advice.matcher.And;
import com.fastaop.advice.matcher.IMatcher;
import com.fastaop.advice.matcher.MethodMatcher;
import com.fastaop.advice.matcher.Not;

public class MethodBuilder implements IMethodBuilder, IMatcherProvider {

    private IMatcher methodLevelMatcher = null;

    private IMatcher methodLevelNotMatcher = null;

    private IMatcherProvider methodAnnotationLevelMatcher = null;

    private final Pointcut root;

    public MethodBuilder(Pointcut root) {
        this.root = root;
    }

    public IMethodBuilder andNot(String methodPattern) {
        if (this.methodLevelNotMatcher == null) {
            this.methodLevelNotMatcher = new Not(new MethodMatcher());
        }
        this.methodLevelNotMatcher.addExperession(methodPattern);
        return this;
    }

    public IMethodAnnotationBuilder annotatedWith(Class<? extends Annotation> annotationPattern) {
        return annotatedWith(annotationPattern.getName());
    }

    public IMethodAnnotationBuilder annotatedWith(String annotationPattern) {
        MethodAnnotationBuilder back = new MethodAnnotationBuilder(root, annotationPattern);
        this.methodAnnotationLevelMatcher = back;
        return back;
    }

    public IMethodBuilder or(String methodPattern) {
        if (this.methodLevelMatcher == null) {
            this.methodLevelMatcher = new MethodMatcher();
        }
        this.methodLevelMatcher.addExperession(methodPattern);
        return this;
    }

    public IPointcut compile() {
        return root.compile();
    }

    public IMatcher getMatcher() {
        IMatcher matcher = new And(methodLevelMatcher, methodLevelNotMatcher);
        if (methodAnnotationLevelMatcher != null) {
            matcher = new And(matcher, methodAnnotationLevelMatcher.getMatcher());
        }
        return matcher;
    }
}
