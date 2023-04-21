package org.ontoware.rdf2go.impl.jena26;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.ontoware.rdf2go.GenericTest;
import org.ontoware.rdf2go.model.ModelTest;
import org.ontoware.rdf2go.model.NotifyingModelTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ GenericTest.class, ModelTest.class, NotifyingModelTest.class, DataTypeTesting.class, TestResourceLoading.class })
public class AllTests {
}
