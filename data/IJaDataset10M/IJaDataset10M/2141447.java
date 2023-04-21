package org.ztest.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jdom.Element;
import org.ztest.classinfo.ZClassUtils;
import org.ztest.classinfo.ZIClassFilter;
import org.ztest.classinfo.ZIClassInfo;
import org.ztest.jdom.ZXMLUtil;
import org.ztest.sets.ZIClassSet;

public abstract class ZClassSetTest<F extends ZITestFailure> implements ZIClassSetTest, ZITest {

    private final ZIClassInfo classInfo;

    private final String name;

    private final ZIClassSet classSet;

    private ZIClassFilter filter;

    private final List<F> failures = new ArrayList<F>();

    public ZClassSetTest(String name, ZIClassInfo classInfo, ZIClassSet classSet) {
        super();
        this.name = name;
        this.classInfo = classInfo;
        this.classSet = classSet;
    }

    public String toXML() throws Exception {
        return ZXMLUtil.toXML(toElement());
    }

    public Element toElement() throws Exception {
        Element ret = new Element(name);
        ret.setAttribute("class-set", classSet.getName());
        ret.addContent(ZTestUtil.toElement(failures));
        return ret;
    }

    public List<F> getFailures() {
        return failures;
    }

    public ZIClassInfo getClassInfo() {
        return classInfo;
    }

    public Set<String> getFilteredClasses() throws Exception {
        return ZClassUtils.filter(classSet.getClasses(), filter);
    }

    public List<String> getFilteredClassesSorted() throws Exception {
        List<String> ret = new ArrayList<String>(getFilteredClasses());
        Collections.sort(ret);
        return ret;
    }

    public ZIClassFilter getFilter() {
        return filter;
    }

    public void setFilter(ZIClassFilter filter) {
        this.filter = filter;
    }

    public String getName() {
        return name;
    }

    public ZIClassSet getClassSet() {
        return classSet;
    }
}
