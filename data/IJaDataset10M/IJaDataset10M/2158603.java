package net.sf.extcos.internal;

import static net.sf.extcos.util.StringUtils.append;
import static net.sf.extcos.util.StringUtils.isJavaPackage;
import net.sf.extcos.selector.Package;
import net.sf.extcos.util.Assert;

public class PackageImpl implements Package {

    private String name;

    public PackageImpl(String name) {
        Assert.notNull(name, IllegalArgumentException.class);
        Assert.isTrue(isJavaPackage(name), IllegalArgumentException.class, append(name, " is not a valid package name"));
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return append(name.replace('.', '/'), "/");
    }
}
