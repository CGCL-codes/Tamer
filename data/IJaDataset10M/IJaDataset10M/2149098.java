package org.opennms.netmgt.config;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Category;
import org.opennms.core.utils.IPSorter;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.config.collectd.ExcludeRange;
import org.opennms.netmgt.config.collectd.IncludeRange;
import org.opennms.netmgt.config.collectd.Package;
import org.opennms.netmgt.config.collectd.Service;
import org.opennms.netmgt.filter.FilterDaoFactory;

public class CollectdPackage {

    private Package m_pkg;

    private List<String> m_ipList;

    private List<IncludeURL> m_includeURLs;

    public CollectdPackage(Package pkg, String localServer, boolean verifyServer) {
        m_pkg = pkg;
        m_includeURLs = new LinkedList<IncludeURL>();
        createIpList(localServer, verifyServer);
        createIncludeURLs(pkg);
    }

    private void createIncludeURLs(Package pkg) {
        Enumeration<String> urlEnum = pkg.enumerateIncludeUrl();
        while (urlEnum.hasMoreElements()) {
            m_includeURLs.add(new IncludeURL(urlEnum.nextElement()));
        }
    }

    public Package getPackage() {
        return m_pkg;
    }

    /**
	 * Returns true if the service is part of the package and the status of the
	 * service is set to "on". Returns false if the service is not in the
	 * package or it is but the status of the service is set to "off".
	 * 
	 * @param pkg
	 *            The package to lookup up service.
	 * @param svcName
	 *            The service name to lookup.
	 */
    public boolean serviceInPackageAndEnabled(String svcName) {
        Package pkg = getPackage();
        boolean result = false;
        Enumeration<Service> esvcs = pkg.enumerateService();
        while (result == false && esvcs.hasMoreElements()) {
            Service tsvc = esvcs.nextElement();
            if (tsvc.getName().equalsIgnoreCase(svcName)) {
                String status = tsvc.getStatus();
                if (status.equals("on")) result = true;
            }
        }
        return result;
    }

    boolean hasSpecific(long addr) {
        Package pkg = getPackage();
        boolean has_specific = false;
        Enumeration<String> espec = pkg.enumerateSpecific();
        while (!has_specific && espec.hasMoreElements()) {
            long speca = IPSorter.convertToLong(espec.nextElement());
            if (speca == addr) has_specific = true;
        }
        return has_specific;
    }

    public boolean hasIncludeRange(long addr) {
        Package pkg = getPackage();
        boolean has_range_include = pkg.getIncludeRangeCount() == 0 && pkg.getSpecificCount() == 0;
        Enumeration<IncludeRange> eincs = pkg.enumerateIncludeRange();
        while (!has_range_include && eincs.hasMoreElements()) {
            IncludeRange rng = eincs.nextElement();
            long start = IPSorter.convertToLong(rng.getBegin());
            if (addr > start) {
                long end = IPSorter.convertToLong(rng.getEnd());
                if (addr <= end) {
                    has_range_include = true;
                }
            } else if (addr == start) {
                has_range_include = true;
            }
        }
        return has_range_include;
    }

    public String getName() {
        return m_pkg.getName();
    }

    boolean hasExcludeRange(long addr, boolean has_specific) {
        Package pkg = getPackage();
        boolean has_range_exclude = false;
        Enumeration<ExcludeRange> eex = pkg.enumerateExcludeRange();
        while (!has_range_exclude && !has_specific && eex.hasMoreElements()) {
            ExcludeRange rng = eex.nextElement();
            long start = IPSorter.convertToLong(rng.getBegin());
            if (addr > start) {
                long end = IPSorter.convertToLong(rng.getEnd());
                if (addr <= end) {
                    has_range_exclude = true;
                }
            } else if (addr == start) {
                has_range_exclude = true;
            }
        }
        return has_range_exclude;
    }

    public void putIpList(List<String> ipList) {
        m_ipList = ipList;
    }

    public List<String> getIpList() {
        return m_ipList;
    }

    boolean interfaceInFilter(String iface) {
        boolean filterPassed = false;
        List<String> ipList = getIpList();
        if (ipList != null && ipList.size() > 0) {
            filterPassed = ipList.contains(iface);
        } else {
            log().debug("interfaceInFilter: ipList contains no data");
        }
        if (!filterPassed) log().debug("interfaceInFilter: Interface " + iface + " passed filter for package " + getName() + "?: false");
        return filterPassed;
    }

    Category log() {
        return ThreadCategory.getInstance(getClass());
    }

    public List<IncludeURL> getIncludeURLs() {
        return m_includeURLs;
    }

    boolean hasSpecificUrl(String iface, boolean has_specific) {
        for (Iterator<IncludeURL> it = getIncludeURLs().iterator(); it.hasNext() && !has_specific; ) {
            IncludeURL includeURL = it.next();
            has_specific = includeURL.interfaceInUrl(iface);
        }
        return has_specific;
    }

    /**
	 * This method is used to determine if the named interface is included in
	 * the passed package definition. If the interface belongs to the package
	 * then a value of true is returned. If the interface does not belong to the
	 * package a false value is returned.
	 * 
	 * <strong>Note: </strong>Evaluation of the interface against a package
	 * filter will only work if the IP is already in the database.
	 * @param iface
	 *            The interface to test against the package.
	 * @param pkg
	 *            The package to check for the inclusion of the interface.
	 * @param factory TODO
	 * 
	 * @return True if the interface is included in the package, false
	 *         otherwise.
	 */
    public boolean interfaceInPackage(String iface) {
        boolean filterPassed = interfaceInFilter(iface);
        if (!filterPassed) return false;
        long addr = IPSorter.convertToLong(iface);
        boolean has_range_include = hasIncludeRange(addr);
        boolean has_specific = hasSpecific(addr);
        has_specific = hasSpecificUrl(iface, has_specific);
        boolean has_range_exclude = hasExcludeRange(addr, has_specific);
        boolean packagePassed = has_specific || (has_range_include && !has_range_exclude);
        if (packagePassed) {
            log().info("interfaceInPackage: Interface " + iface + " passed filter and specific/range for package " + getName() + "?: " + packagePassed);
        } else {
            log().debug("interfaceInPackage: Interface " + iface + " passed filter and specific/range for package " + getName() + "?: " + packagePassed);
        }
        return packagePassed;
    }

    String getFilterRule(String localServer, boolean verifyServer) {
        Package pkg = getPackage();
        String filter = pkg.getFilter().getContent();
        StringBuffer filterRules = new StringBuffer(filter);
        if (verifyServer) {
            filterRules.append(" & (serverName == ");
            filterRules.append('\"');
            filterRules.append(localServer);
            filterRules.append('\"');
            filterRules.append(")");
        }
        return filterRules.toString();
    }

    void createIpList(String localServer, boolean verifyServer) {
        Package pkg = getPackage();
        String filterRules = getFilterRule(localServer, verifyServer);
        if (log().isDebugEnabled()) log().debug("createPackageIpMap: package is " + pkg.getName() + ". filer rules are  " + filterRules);
        try {
            List<String> ipList = FilterDaoFactory.getInstance().getIPList(filterRules);
            if (ipList.size() > 0) {
                putIpList(ipList);
            }
        } catch (Throwable t) {
            log().error("createPackageIpMap: failed to map package: " + pkg.getName() + " to an IP List: " + t, t);
        }
    }

    public Service getService(final String svcName) {
        final List<Service> pkgSvcs = m_pkg.getServiceCollection();
        for (Service svc : pkgSvcs) {
            if (svc.getName().equalsIgnoreCase(svcName)) return svc;
        }
        throw new RuntimeException("Service name not part of package!");
    }

    public String storeByIfAlias() {
        return getPackage().getStoreByIfAlias();
    }

    public String ifAliasComment() {
        return getPackage().getIfAliasComment();
    }

    public String getStorFlagOverride() {
        return getPackage().getStorFlagOverride();
    }

    public String ifAliasDomain() {
        return getPackage().getIfAliasDomain();
    }

    public String storeByNodeId() {
        return getPackage().getStoreByNodeID();
    }
}
