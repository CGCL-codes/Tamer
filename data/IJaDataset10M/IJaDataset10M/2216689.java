package fr.inria.uml4tst.papyrus.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestsCore.class, TestsModelGetter.class, TestsProjectGetter.class })
public class AllCoreTests {
}
