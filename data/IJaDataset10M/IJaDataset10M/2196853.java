package org.ztest.tests;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;
import org.ztest.classinfo.ZIClassInfo;
import org.ztest.jdom.ZXMLUtil;
import org.ztest.sets.ZIClassSet;

public class ZDisallowUsageTest implements ZITest {

    private final ZIClassInfo classInfo;

    private final ZIClassSet source;

    private final ZIClassSet target;

    private final boolean transitive;

    private final List<ZDisallowUsageFailure> failures = new ArrayList<ZDisallowUsageFailure>();

    public ZDisallowUsageTest(ZIClassInfo classInfo, ZIClassSet source, ZIClassSet target, boolean transitive) {
        this.classInfo = classInfo;
        this.source = source;
        this.target = target;
        this.transitive = transitive;
    }

    public void run() throws Exception {
        ZDisallowUsageFailure usageFailure = getUsageInfo(source, target, transitive);
        if (!usageFailure.getUsage().isEmpty()) {
            failures.add(usageFailure);
        }
    }

    private ZDisallowUsageFailure getUsageInfo(ZIClassSet user, ZIClassSet used, boolean transitive) throws Exception {
        ZDisallowUsageFailure ret = new ZDisallowUsageFailure(classInfo);
        for (String s1 : user.getClasses()) {
            for (String s2 : used.getClasses()) {
                if (classInfo.isSuccessor(s1, s2, null, transitive)) {
                    ret.add(s1, s2);
                }
            }
        }
        return ret;
    }

    public boolean isValid() {
        return failures.isEmpty();
    }

    public String toXML() throws Exception {
        return ZXMLUtil.toXML(toElement());
    }

    public Element toElement() throws Exception {
        Element ret = new Element("disallow-usage-test");
        ret.addContent(ZTestUtil.toElement(failures));
        return ret;
    }

    public List<ZDisallowUsageFailure> getFailures() {
        return failures;
    }
}
