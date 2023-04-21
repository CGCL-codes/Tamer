package net.community.chest.jmx.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import net.community.chest.dom.ElementIndicatorExceptionContainer;
import net.community.chest.io.FileUtil;
import net.community.chest.jmx.JMXUtils;
import net.community.chest.jmx.MBeanRegistrationResult;
import net.community.chest.jmx.ManagementFactoryBeanType;
import net.community.chest.jmx.dom.MBeanEntryDescriptor;
import net.community.chest.reflect.ClassUtil;
import net.community.chest.test.TestBase;
import org.w3c.dom.Element;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Aug 14, 2007 8:07:41 AM
 */
public class JMXTester extends TestBase implements JMXTesterMBean, MBeanRegistration {

    public JMXTester() {
        super();
    }

    @Override
    public void doVoidOperation() {
        System.out.println(getClass() + "#doVoidOperation()");
    }

    @Override
    public long getCurrentTime() {
        final long v = System.currentTimeMillis();
        System.out.println(ClassUtil.getArgumentsExceptionLocation(getClass(), "getCurrentTime", Long.valueOf(v)));
        return v;
    }

    @Override
    public long compareCurrentTime(long tStamp) {
        final long curTime = getCurrentTime(), diffTime = tStamp - curTime;
        System.out.println(ClassUtil.getArgumentsExceptionLocation(getClass(), "compareCurrentTime", Long.valueOf(tStamp), Long.valueOf(curTime)) + " ==> " + diffTime);
        return diffTime;
    }

    private String _value;

    @Override
    public String getStringValue() {
        System.out.println(ClassUtil.getArgumentsExceptionLocation(getClass(), "getStringValue", _value));
        return _value;
    }

    @Override
    public void setStringValue(String v) {
        System.out.println(ClassUtil.getArgumentsExceptionLocation(getClass(), "setStringValue", v));
        _value = v;
    }

    @Override
    public String toLowerCase(String v) {
        final String res = ((null == v) || (v.length() <= 0)) ? v : v.toLowerCase();
        System.out.println(ClassUtil.getArgumentsExceptionLocation(getClass(), "toLowerCase", v) + ": " + res);
        return res;
    }

    @Override
    public void postDeregister() {
        System.out.println(ClassUtil.getExceptionLocation(getClass(), "postDeregister"));
    }

    @Override
    public void postRegister(Boolean registrationDone) {
        System.out.println(ClassUtil.getArgumentsExceptionLocation(getClass(), "postRegister", registrationDone));
    }

    @Override
    public void preDeregister() throws Exception {
        System.out.println(ClassUtil.getExceptionLocation(getClass(), "preDeregister"));
    }

    @Override
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        System.out.println(ClassUtil.getArgumentsExceptionLocation(getClass(), "preRegister", name));
        return name;
    }

    private static final int testMBean(final PrintStream out, final BufferedReader in, final MBeanServerConnection s, final ObjectName name, final MBeanInfo mbi) {
        for (; ; ) {
            final String ans = getval(out, in, name + " [A]ttributes/(O)perations/(M)odify/(I)nvoke/(Q)uit");
            if (isQuit(ans)) break;
            final char op = ((null == ans) || (ans.length() <= 0)) ? 'A' : Character.toUpperCase(ans.charAt(0));
            try {
                switch(op) {
                    case 'A':
                        {
                            final MBeanAttributeInfo[] attrs = mbi.getAttributes();
                            if ((attrs != null) && (attrs.length > 0)) {
                                for (final MBeanAttributeInfo a : attrs) {
                                    final String attrName = a.getName();
                                    out.println("\t" + attrName + "[" + a.getType() + "](" + (a.isReadable() ? "R" : "") + (a.isWritable() ? "W" : "") + (a.isIs() ? "B" : "") + "): " + a.getDescription());
                                    if (a.isReadable()) {
                                        try {
                                            final Object aValue = s.getAttribute(name, attrName);
                                            out.println("\t\t" + aValue);
                                        } catch (Exception e) {
                                            System.err.println("testMBean(" + attrName + ") " + e.getClass().getName() + ": " + e.getMessage());
                                        }
                                    }
                                }
                            } else out.println("No attributes found");
                        }
                        break;
                    case 'O':
                        {
                            final MBeanOperationInfo[] opers = mbi.getOperations();
                            if ((opers != null) && (opers.length > 0)) {
                                for (final MBeanOperationInfo o : opers) {
                                    out.println("\t" + o.getReturnType() + " <= " + o.getName() + ": " + o.getDescription());
                                    final MBeanParameterInfo[] params = o.getSignature();
                                    if ((params != null) && (params.length > 0)) {
                                        for (final MBeanParameterInfo p : params) out.println("\t\t" + p.getName() + "[" + p.getType() + "]: " + p.getDescription());
                                    }
                                }
                            } else out.println("No operations found");
                        }
                        break;
                    case 'M':
                        {
                            final AttributeList aList = new AttributeList();
                            for (; ; ) {
                                final String attrName = getval(out, in, "attribute name (ENTER/Quit=end)");
                                if ((null == attrName) || (attrName.length() <= 0) || isQuit(attrName)) break;
                                final String attrValue = getval(out, in, "attribute value (ENTER=null)/Quit");
                                if (isQuit(attrValue)) break;
                                final String attrType = getval(out, in, "attribute type [S]tring/(I)nteger/(L)ong/(B)oolean/(Q)uit");
                                final char aType = ((null == attrType) || (attrType.length() <= 0)) ? 'S' : Character.toUpperCase(attrType.charAt(0));
                                final Object aValue;
                                switch(aType) {
                                    case 'S':
                                        aValue = attrValue;
                                        break;
                                    case 'I':
                                        aValue = Integer.valueOf(attrValue);
                                        break;
                                    case 'L':
                                        aValue = Long.valueOf(attrValue);
                                        break;
                                    case 'B':
                                        aValue = Boolean.valueOf(attrValue);
                                        break;
                                    default:
                                        aValue = null;
                                }
                                aList.add(new Attribute(attrName, aValue));
                            }
                            s.setAttributes(name, aList);
                        }
                        break;
                    default:
                }
            } catch (Exception e) {
                System.err.println("testMBean(" + ans + ") " + e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return 0;
    }

    private static final void testXMBeansAdd(final PrintStream out, final BufferedReader in, final MBeanServerConnection s) {
        try {
            final Map<ManagementFactoryBeanType, MBeanRegistrationResult> mbm = ManagementFactoryBeanType.registerAllMBeans((MBeanServer) s);
            final Collection<? extends Map.Entry<ManagementFactoryBeanType, MBeanRegistrationResult>> mel = ((null == mbm) || (mbm.size() <= 0)) ? null : mbm.entrySet();
            if ((null == mel) || (mel.size() <= 0)) {
                out.println("No MBeans registered");
                return;
            }
            for (final Map.Entry<ManagementFactoryBeanType, MBeanRegistrationResult> re : mel) {
                final ManagementFactoryBeanType mbt = (null == re) ? null : re.getKey();
                final MBeanRegistrationResult mbr = (null == re) ? null : re.getValue();
                final Map<String, ? extends ObjectInstance> rml = (null == mbr) ? null : mbr.getRegisteredMBeans();
                final Collection<? extends ObjectInstance> mbl = (null == rml) ? null : rml.values();
                final int numMBeans = (null == mbl) ? 0 : mbl.size();
                out.println("\tRegistered " + numMBeans + " instances for MXBean=" + mbt);
                if (numMBeans > 0) {
                    for (final ObjectInstance oi : mbl) {
                        final ObjectName mn = (null == oi) ? null : oi.getObjectName();
                        if (null == mn) continue;
                        out.println("\t\t" + mn);
                    }
                }
                final Map<String, ? extends Exception> eel = (null == mbr) ? null : mbr.getRegistrationExceptions();
                final Collection<? extends Map.Entry<String, ? extends Exception>> eml = (null == eel) ? null : eel.entrySet();
                if ((eml != null) && (eml.size() > 0)) {
                    for (final Map.Entry<String, ? extends Exception> eme : eml) {
                        final String objName = (null == eme) ? null : eme.getKey();
                        final Exception objErr = (null == eme) ? null : eme.getValue();
                        if (null == objErr) continue;
                        System.err.println("\t\t" + objName + " - " + objErr.getClass().getName() + ": " + objErr.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + " while instantiate MXBeans: " + e.getMessage());
        }
    }

    private static final int testMBeansAdd(final PrintStream out, final BufferedReader in, final MBeanServerConnection s) {
        for (String path = null; ; ) {
            if ((null == (path = getval(out, in, "(F)actory/XML file path/(Q)uit)"))) || (path.length() <= 0)) continue;
            if (isQuit(path)) break;
            if ("f".equalsIgnoreCase(path) || "factory".equalsIgnoreCase(path)) {
                testXMBeansAdd(out, in, s);
                continue;
            }
            final Collection<MBeanEntryDescriptor> descs = new LinkedList<MBeanEntryDescriptor>();
            try {
                final Collection<? extends ElementIndicatorExceptionContainer> errs = MBeanEntryDescriptor.updateDefaultMBeans(descs, path);
                if ((errs != null) && (errs.size() > 0)) {
                    for (final ElementIndicatorExceptionContainer ind : errs) {
                        final Element elem = ind.getObjectValue();
                        final Throwable t = ind.getCause();
                        System.err.println("\tupdateDefaultMBeans(" + elem.getTagName() + "[" + MBeanEntryDescriptor.NAME_ATTR + "=" + elem.getAttribute(MBeanEntryDescriptor.NAME_ATTR) + "] " + t.getClass().getName() + ": " + t.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("testMBeansAdd(" + path + ") " + e.getClass().getName() + ": " + e.getMessage());
            }
            for (final MBeanEntryDescriptor d : descs) {
                final String objName = d.getObjectName();
                out.println("\t" + objName + "[" + d.getClassName() + "]");
                final String ans = getval(out, in, "add MBean [y]/n/q");
                if (isQuit(ans)) break;
                if ((ans != null) && (ans.length() > 0) && (Character.toLowerCase(ans.charAt(0)) != 'y')) continue;
                try {
                    final String mbClassName = d.getClassName();
                    final ObjectName name = new ObjectName(objName);
                    final ObjectInstance inst = s.createMBean(mbClassName, name);
                    if (null == inst) throw new NoSuchElementException("No object instance created");
                    JMXUtils.setAttributesValues(s, name, d.getAttributes());
                } catch (Exception e) {
                    System.err.println("testMBeansAdd(" + d.getObjectName() + ") " + e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
        return 0;
    }

    private static final int testMBeanServer(final PrintStream out, final BufferedReader in, final MBeanServerConnection s) throws IOException {
        for (; ; ) {
            out.println(s.getDefaultDomain() + ": " + s.getMBeanCount() + " registered MBean");
            final String ans = getval(out, in, "[N]ames/(F)ind/(A)dd/(Q)uit");
            if (isQuit(ans)) break;
            final char op = ((null == ans) || (ans.length() <= 0)) ? 'N' : Character.toUpperCase(ans.charAt(0));
            try {
                switch(op) {
                    case 'N':
                        {
                            final Collection<? extends ObjectName> names = s.queryNames(null, null);
                            final int numNames = (null == names) ? 0 : names.size();
                            if (numNames > 0) {
                                for (final ObjectName n : names) out.println("\t" + n);
                            } else out.println("No names found");
                        }
                        break;
                    case 'F':
                        {
                            for (String name = null; ; ) {
                                if ((null == (name = getval(out, in, "MBean name (or Quit)"))) || (name.length() <= 0)) continue;
                                if (isQuit(name)) break;
                                try {
                                    final ObjectName n = new ObjectName(name);
                                    final MBeanInfo mbi = s.getMBeanInfo(n);
                                    if (null == mbi) throw new InstanceNotFoundException("No MBeanInfo instance found");
                                    testMBean(out, in, s, n, mbi);
                                } catch (Exception e) {
                                    System.err.println("testMBeanServer(" + name + ") " + e.getClass().getName() + ": " + e.getMessage());
                                }
                            }
                        }
                        break;
                    case 'A':
                        testMBeansAdd(out, in, s);
                        break;
                    default:
                }
            } catch (Exception e) {
                System.err.println("testMBeanServer(" + ans + ") " + e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return 0;
    }

    public static final int testMBeanServer(final PrintStream out, final BufferedReader in, final String... args) {
        final int numArgs = (null == args) ? 0 : args.length;
        for (int aIndex = 0; ; aIndex++) {
            final String ans = (aIndex < numArgs) ? args[aIndex] : getval(out, in, "[S]ervers/(C)reate/(F)ind/(R)emote/(Q)uit");
            if (isQuit(ans)) break;
            final char op = ((null == ans) || (ans.length() <= 0)) ? 'S' : Character.toUpperCase(ans.charAt(0));
            try {
                switch(op) {
                    case 'C':
                        {
                            final String dmName = getval(out, in, "domain name (ENTER=default)");
                            final MBeanServer s = ((null == dmName) || (dmName.length() <= 0)) ? MBeanServerFactory.createMBeanServer() : MBeanServerFactory.createMBeanServer(dmName);
                            if (s != null) testMBeanServer(out, in, s); else throw new UnsupportedOperationException("No server created");
                        }
                        break;
                    case 'F':
                    case 'S':
                        {
                            final String agentId = ('S' == op) ? null : getval(out, in, "agent ID (ENTER=all)");
                            final Collection<? extends MBeanServer> servers = MBeanServerFactory.findMBeanServer(((null == agentId) || (agentId.length() <= 0)) ? null : agentId);
                            final int numServers = (null == servers) ? 0 : servers.size();
                            if (numServers > 0) {
                                for (final MBeanServer s : servers) testMBeanServer(out, in, s);
                            } else out.println("\tNo servers found");
                        }
                        break;
                    case 'R':
                        {
                            final String accURL = getval(out, in, "access URL (or Quit)");
                            if (isQuit(accURL)) break;
                            final JMXServiceURL u = new JMXServiceURL(accURL);
                            JMXConnector c = null;
                            try {
                                c = JMXConnectorFactory.connect(u);
                                testMBeanServer(out, in, c.getMBeanServerConnection());
                            } finally {
                                FileUtil.closeAll(c);
                            }
                        }
                        break;
                    default:
                }
            } catch (Exception e) {
                System.err.println("testMBeanServer(" + ans + ") " + e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        final BufferedReader in = getStdin();
        final int nErr = testMBeanServer(System.out, in, args);
        if (nErr != 0) System.err.println("test failed (err=" + nErr + ")"); else System.out.println("OK");
    }
}
