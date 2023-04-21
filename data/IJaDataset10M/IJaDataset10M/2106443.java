package org.grandtestauto.test;

import org.grandtestauto.*;
import org.grandtestauto.test.dataconstants.org.grandtestauto.*;
import java.util.*;

/**
 * Unit test for <code>TestFinder</code>.
 *
 * @author Tim Lavers
 */
public class AutoLoadTestFinderTest extends CFTestBase {

    private AutoLoadTestFinder altf;

    protected void init(String archiveName, String packageName) throws Exception {
        super.init(archiveName, packageName);
        altf = new AutoLoadTestFinder(packageName, classesDir);
    }

    public boolean foundSomeClassesTest() throws Exception {
        init(Grandtestauto.test1_zip, "a1.test");
        assert !altf.foundSomeClasses();
        altf.processClass("UnitTester");
        assert !altf.foundSomeClasses();
        init(Grandtestauto.test5_zip, "a5.test");
        altf.processClass("XTest");
        assert !altf.foundSomeClasses();
        init(Grandtestauto.test37_zip, "a37.b.loadtest");
        altf.processClass("BugsBunny");
        assert altf.foundSomeClasses();
        return true;
    }

    public boolean processClassTest() throws Exception {
        SortedSet<String> expected = new TreeSet<String>();
        init(Grandtestauto.test37_zip, "a37.b.loadtest");
        altf.processClass("BugsBunny");
        expected.add("BugsBunny");
        assert altf.classesInPackage().equals(expected);
        altf.processClass("DaffyDuck");
        altf.processClass("ElmerFudd");
        expected.add("DaffyDuck");
        expected.add("ElmerFudd");
        assert altf.classesInPackage().equals(expected);
        altf.processClass("NotLoadTest");
        assert altf.classesInPackage().equals(expected);
        init(Grandtestauto.test20_zip, "a20.test");
        altf.processClass("XTest");
        assert altf.classesInPackage().isEmpty();
        init(Grandtestauto.test41_zip, "a41.functiontest");
        assert altf.classesInPackage().isEmpty();
        altf.processClass("FT1");
        assert altf.classesInPackage().isEmpty();
        altf.processClass("FT2");
        assert altf.classesInPackage().isEmpty();
        altf.processClass("FT3");
        assert altf.classesInPackage().isEmpty();
        altf.processClass("FT4");
        assert altf.classesInPackage().isEmpty();
        return true;
    }

    public boolean classesInPackageTest() throws Exception {
        init(Grandtestauto.test37_zip, "a37.b.loadtest");
        assert altf.classesInPackage().isEmpty();
        SortedSet<String> expected = new TreeSet<String>();
        expected.add("DaffyDuck");
        expected.add("ElmerFudd");
        expected.add("BugsBunny");
        altf.seek();
        assert altf.classesInPackage().equals(expected);
        return true;
    }

    public boolean constructorTest() {
        return true;
    }
}
