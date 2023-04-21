package net.sourceforge.argval.internal.packageinfo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.sourceforge.argval.collection.CollectionFilter;
import net.sourceforge.argval.collection.CollectionUtil;
import net.sourceforge.argval.collection.CollectionUtilConfig;
import net.sourceforge.argval.collection.CollectionUtilConfigFactory;
import net.sourceforge.argval.collection.Filter;
import net.sourceforge.argval.packageinfo.IPackageInfo;
import net.sourceforge.argval.packageinfo.IPackageInfoVisitor;
import net.sourceforge.argval.utils.StringUtil;

/**
 * Creates a plain text which shows the visited PackageInfo title 
 * and (implementation) version. And shows all the dot separated package
 * names, which are associated with the <code>Package</code>. 
 * 
 * @author $Author: $
 * @version $Revision: $
 * Last modified: $Date: $
 */
public final class SimplePackageInfoVisitor implements IPackageInfoVisitor {

    public static final int MAX_PACKAGE_NAME_DEPTH = Integer.MAX_VALUE;

    private static final String PACKAGE_NAME_SEPARATOR = ".";

    private static final String CONFIG_PACKAGE_NAME = "package name";

    private StringBuffer strBuf = new StringBuffer();

    private int packageNameDepth = MAX_PACKAGE_NAME_DEPTH;

    private CollectionFilter collectionFilter = null;

    private CollectionUtilConfigFactory configFactory = null;

    /**
	 * Create a plain text, which will include all package names assosiated with the
	 * visited <code>PackageInfo</code> instances.
	 */
    public SimplePackageInfoVisitor(CollectionUtilConfigFactory configFactory, CollectionFilter collectionFilter) {
        super();
        this.configFactory = configFactory;
        this.collectionFilter = collectionFilter;
        configFactory.create(CONFIG_PACKAGE_NAME, PACKAGE_NAME_SEPARATOR);
    }

    public SimplePackageInfoVisitor() {
        this(new CollectionUtilConfigFactory(), new CollectionFilter());
    }

    /**
	 * Create a plain text, of all visited PackageInfo instances. With or without the
	 * package names (Java language dot separated names, like 
	 * <code>net.sourceforge.argval.packageinfo</code> ). 
	 *  
	 * @param  isIncludePackageNameActive  When <code>true</code> all package names 
	 *         assosiated with this <code>Package</code> are added to the text.
	 */
    public SimplePackageInfoVisitor(boolean isIncludePackageNameActive) {
        this((isIncludePackageNameActive) ? MAX_PACKAGE_NAME_DEPTH : 0);
    }

    public SimplePackageInfoVisitor(int packageNameDepth) {
        this(packageNameDepth, null);
    }

    public SimplePackageInfoVisitor(Filter filter) {
        this(MAX_PACKAGE_NAME_DEPTH, filter);
    }

    public SimplePackageInfoVisitor(int packageNameDepth, Filter filter) {
        this();
        this.packageNameDepth = packageNameDepth;
        this.collectionFilter = new CollectionFilter(filter);
    }

    /** {@inheritDoc} 
	 *
	 * Adds the details (title and version) of each <code>PackageInfo</code> instance and 
	 * all the package names (The Java language dot separated package names), which are 
	 * associated with this package.
	 */
    public void visit(IPackageInfo packageInfo) {
        if (IPackageInfo.UNKNOWN_PACKAGE_TITLE.equals(packageInfo.getTitle())) {
            if (packageNameDepth > 0) {
                strBuf.append("Loaded packages, from which no Specification/Implementation title (and version) is known");
            }
        } else {
            strBuf.append(packageInfo.getTitle());
        }
        if (packageInfo.getImplementationVersion() != null) {
            strBuf.append("  [").append(packageInfo.getImplementationVersion()).append("]");
        }
        strBuf.append(StringUtil.LINE_SEPARATOR);
        if (packageNameDepth > 0) {
            Set<String> compactNameSet = (packageNameDepth == MAX_PACKAGE_NAME_DEPTH) ? convert(packageInfo.getNameSet()) : chop(convert(packageInfo.getNameSet()), configFactory.getConfig(CONFIG_PACKAGE_NAME), packageNameDepth);
            Set<String> filteredSet = collectionFilter.filter(compactNameSet);
            for (Iterator<String> nameIter = filteredSet.iterator(); nameIter.hasNext(); ) {
                strBuf.append("  ").append(nameIter.next()).append(StringUtil.LINE_SEPARATOR);
            }
        }
        strBuf.append(StringUtil.LINE_SEPARATOR);
    }

    private TreeSet<String> convert(Set<?> originalSet) {
        TreeSet<String> newSet = new TreeSet<String>();
        for (Object object : originalSet) {
            newSet.add(object.toString());
        }
        return newSet;
    }

    private static TreeSet<String> chop(Set<String> nameSet, CollectionUtilConfig config, int maxDepth) {
        TreeSet<String> compactNameSet = new TreeSet<String>();
        for (Iterator<String> iter = nameSet.iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            List<String> nameList = CollectionUtil.stringToList(name, PACKAGE_NAME_SEPARATOR);
            while (nameList.size() > maxDepth) {
                nameList.remove(nameList.size() - 1);
            }
            String compactName = CollectionUtil.toString(nameList, config);
            compactNameSet.add(compactName);
        }
        return compactNameSet;
    }

    /** {@inheritDoc} */
    public String toString() {
        return strBuf.toString();
    }
}
