package com.jeantessier.dependency;

public interface SelectionCriteria {

    public boolean isMatchingPackages();

    public boolean isMatchingClasses();

    public boolean isMatchingFeatures();

    public boolean matches(PackageNode node);

    public boolean matches(ClassNode node);

    public boolean matches(FeatureNode node);

    public boolean matchesPackageName(String name);

    public boolean matchesClassName(String name);

    public boolean matchesFeatureName(String name);
}
