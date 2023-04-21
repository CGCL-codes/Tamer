package net.sf.adatagenerator.febrl.testutils;

import net.sf.adatagenerator.api.FieldDependency;
import net.sf.adatagenerator.api.FieldDependencyManager;
import net.sf.adatagenerator.febrl.generators.FebrlFirstNameGenerator;

public class MockFebrlFirstNameGenerator<B> extends FebrlFirstNameGenerator<B> {

    public static final String RESOURCE_GIVENNAME_FREQ = "fake-givenname-freq.csv";

    public static final String RESOURCE_GIVENNAME_SEX_CULTURE = "fake-givenname-lookup.tbl";

    public MockFebrlFirstNameGenerator(FieldDependencyManager<B> existingDependencies) {
        this(buildFebrlDependency(existingDependencies));
    }

    public MockFebrlFirstNameGenerator(FieldDependency<B> fd) {
        super(FebrlTestUtil.getFullyQualifiedDataName(RESOURCE_GIVENNAME_FREQ), FebrlTestUtil.getFullyQualifiedDataName(RESOURCE_GIVENNAME_SEX_CULTURE), MockFebrlFirstNameGenerator.class.getClassLoader(), fd);
    }
}
