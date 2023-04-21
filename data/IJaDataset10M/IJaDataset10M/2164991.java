package net.sourceforge.argval.packageinfo;

import junit.framework.TestCase;

public class SimplePackageVisitorTest extends TestCase {

    public void test() {
        PackageVisitor visitor = new SimplePackageVisitor();
        PackageInfoManager manager = new PackageInfoManager();
        manager.addPackage(Package.getPackages());
        manager.accept(visitor);
        String packageInfoStr = visitor.toString();
        System.out.println(visitor);
        assertTrue(packageInfoStr.indexOf("Java Platform API Specification") > -1);
    }
}
