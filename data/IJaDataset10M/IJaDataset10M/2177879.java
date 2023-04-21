package de.fhg.igd.semoa.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import de.fhg.igd.semoa.server.AgentLauncher;
import de.fhg.igd.util.ClassResource;

/**
 * This class defines constant names for the various
 * mandatory files required to manage SeMoA Agents.<p>
 *
 * The general structure of the agent consists of these
 * directories:
 * <dl>
 * <dt> <b>META-INF</B>
 * <dd> This directory holds the manifest file of the agent
 *   as well as the various signatures.
 * <dt> <b>static</b>
 * <dd> The agent's static data is grouped in this directory.
 *   The classes are <i>not</i> included here.
 * <dt> <b>mutable</b>
 * <dd> The agent's mutable data.
 * </dl>
 * <p>
 * Please note that the constant strings defined in this class
 * which denote path names into the agent structure use a slash
 * ('/') as the path element separator, regardless of the OS
 * on which the software is running.
 *
 * @author Volker Roth
 * @author Jan Peters
 * @version "$Id: AgentStructure.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class AgentStructure extends Object {

    /**
     * The name of the file that stores the default values
     * for the properties defined herein. 
     *
     * Value: <tt>de/fhg/igd/semoa/security/defaults.map</tt>     
     */
    public static final String DEFAULTS = "defaults.map";

    /**
     * The digest algorithm that is used to compute <i>implicit
     * </i> names of agents. Implicit names are used for secure
     * and scalable tracing of agents.
     *
     * Value: <tt>SHA1</tt>     
     */
    public static final String IMPLICIT_NAME_ALG = "SHA1";

    /**
     * The MAC algorithm used for checking the authorisation
     * codes in the INSTALL.MF file.
     *
     * Value: <tt>HmacSHA1</tt>     
     */
    public static final String MAC = "HmacSHA1";

    /**
     * The default algorithm to use when encrypting
     * agent data is DESede/CBC/PKCS5Padding
     *
     * Value: <tt>DESede/CBC/PKCS5Padding</tt>     
     */
    public static final String DEFAULT_CALG = "DESede/CBC/PKCS5Padding";

    /**
     * The default strength to use when encrypting
     * agent data is 168 (key size of triple-DES).
     *
     * Value: <tt>168</tt>     
     */
    public static final int DEFAULT_STRENGTH = 168;

    /**
     * The name of the META-INF folder plus a slash
     * as path separator.
     *
     * Value: <tt>META-INF/</tt>     
     */
    public static final String META_INF = "META-INF/";

    /**
     * The path to the MANIFEST file
     *
     * Value: <tt>META-INF/MANIFEST.MF</tt>     
     */
    public static final String MANIFEST = "META-INF/MANIFEST.MF";

    /**
     * Contains the key database files as well as the
     * cipher files, and the SEALED.MF file.
     *
     * Value: <tt>SEAL-INF/</tt>     
     */
    public static final String SEAL_INF = "SEAL-INF/";

    /**
     * Contains the mappings from cipher files onto the
     * names of the keys required to decrypt them. One such
     * file is in the static, and one is in the mutable
     * part.
     *
     * Value: <tt>SEAL-INF/INSTALL.MF</tt>     
     */
    public static final String INSTALL_MF = "SEAL-INF/INSTALL.MF";

    /**
     * The version to write to the properties as a comment.
     *
     * Value: <tt>Creator: SeMoA</tt>     
     */
    public static final String VERSION = "Creator: SeMoA";

    /**
     * The path to the agent's static part relative to
     * the its base directory.
     *
     * Value: <tt>static/</tt>     
     */
    public static final String PATH_STATIC = "static/";

    /**
     * The path to the agent's mutable part relative to
     * its base directory.
     *
     * Value: <tt>mutable/</tt>     
     */
    public static final String PATH_MUTABLE = "mutable/";

    /**
     * Contains the serialised instance graph of the agent
     * (its persistent state with regard to objects).
     *
     * Value: <tt>mutable/instances.ser</tt>     
     */
    public static final String INSTANCES = "mutable/instances.ser";

    /**
     * Contains the logging information of the agent.
     *
     * Value: <tt>mutable/agent.log</tt>     
     */
    public static final String LOGS = "mutable/agent.log";

    /**
     * Contains the agent's properties such as its unique
     * name.
     *
     * Value: <tt>static/properties</tt>     
     */
    public static final String PROPERTIES = "static/properties";

    /**
     * The reserved name for group entries in the
     * INSTALL.MF file.
     *
     * Value: <tt>GROUPS</tt>     
     */
    public static final String GROUPS = "GROUPS";

    /**
     * The extension used to identify the files that store
     * the per-group bulk encryption keys.
     *
     * Value: <tt>.P7</tt>     
     */
    public static final String EXT_P7 = ".P7";

    /**
     * The extension used to identify the encrypted
     * archives.
     *
     * Value: <tt>.EAR</tt>     
     */
    public static final String EXT_EAR = ".EAR";

    /**
     * Contains the topmost signature that binds the agent's
     * static part to its mutable part. This signature is
     * computed on two manifests. First, the manifest of the
     * static part and second, the manifest of the mutable
     * part.
     *
     * Value: <tt>SENDER</tt>     
     */
    public static final String SENDER = "SENDER";

    /**
     * Contains the signature of the agent's owner. The scope
     * of this signature is the agent's static part. The
     * signature is computed on the manifest of the static part.
     *
     * Value: <tt>OWNER</tt>     
     */
    public static final String OWNER = "OWNER";

    /**
     * The property key identifying an agent's alias or nickname.
     *
     * Value: <tt>agent.alias</tt>     
     */
    public static final String PROP_AGENT_ALIAS = "agent.alias";

    /**
     * The property key identifying an agent's description
     *
     * Value: <tt>agent.description</tt>     
     */
    public static final String PROP_AGENT_DESCRIPTION = "agent.description";

    /**
     * The property key identifying the name of the agent's
     * class. This property is mandatory. If the agent does
     * not contain serialised instances then this name is used
     * to create an instance of the class denoted by it. If
     * it does have serialised instances then the main class
     * is compared to this name in order to prohibit attackers
     * from providing a fake agent instance.
     *
     * Value: <tt>agent.class</tt>     
     */
    public static final String PROP_AGENT_CLASS = "agent.class";

    /**
     * The property key that identifies the preferred system
     * of this agent.
     *
     * Value: <tt>agent.system</tt>     
     */
    public static final String PROP_AGENT_SYSTEM = "agent.system";

    /**
     * The agent type. In conjunction with the agent system
     * property this should be sufficient to determine the
     * lifecycle required for this agent.
     *
     * Value: <tt>agent.type</tt>     
     */
    public static final String PROP_AGENT_TYPE = "agent.type";

    /**
     * This property gives the preferred agent communication
     * language of the agent.<p>
     *
     * Communication capabilities are added to the agent only
     * if this property is defined. Currently, its value is
     * ignored.
     */
    public static final String PROP_AGENT_ACL = "agent.acl";

    /**
     * The classpath that is used when the agent is created. The
     * value consists of a list of paths separated by <code>
     * File.pathSeparator</code>. A path can point to a directory,
     * ZIP, or JAR archive.
     *
     * Value: <tt>agent.classpath</tt>     
     */
    public static final String PROP_AGENT_CLASSPATH = "agent.classpath";

    /**
     * The mode of class import. Classes can be bundled with
     * the agent structure as bytecode, or only the digests of
     * referenced classes can be imported. Legal values are
     * declared e.g., in class <code>AgentLauncher</code>.
     *
     * Value: <tt>agent.import</tt>     
     *
     * @see AgentLauncher
     */
    public static final String PROP_AGENT_IMPORT = "agent.import";

    /**
     * The include classes. This is a list of classes that
     * that shall explicitly be digested or imported into the 
     * agent together with their corresponding class closure. 
     * This list is automatically extended by the the agent's
     * class given by <tt>agent.class</tt>.
     * Use dots to seperate the packages and class names.
     * The given class entries are separated by colons, 
     * semicolons, or commas.
     *
     * This parameter might be used to include e.g. algorithms
     * which are not referenced by the agent in a static manner,
     * but by using e.g. <code>Class.forName()</code>.
     *
     * Value: <tt>agent.include</tt>     
     *
     * @see de.fhg.igd.util.ClassClosure
     */
    public static final String PROP_AGENT_INCLUDE = "agent.include";

    /**
     * The exclude path. This is a list of package prefixes
     * that shall not be digested or imported into the agent. 
     * Use the Java's import statement convention to define 
     * package prefixes. The given package prefix entries 
     * are separated by colons, semicolons, or commas.
     *
     * Value: <tt>agent.exclude</tt>     
     *
     * @see de.fhg.igd.util.ClassClosure
     */
    public static final String PROP_AGENT_EXCLUDE = "agent.exclude";

    /**
     * The path of the agent's icon file. The icon file should
     * be available and also contain a supported image format, 
     * such as GIF, JPEG or PNG.
     * 
     * Value: <tt>agent.icon</tt>     
     */
    public static final String PROP_AGENT_ICON = "agent.icon";

    /**
     * The prefix of codesource URLs. The actual properties
     * have a running count starting from one following the
     * prefix.
     *
     * Value: <tt>codesource.url.</tt>     
     */
    public static final String PROP_CODESOURCE = "codesource.url.";

    /**
     * The group definition property. The value of this
     * property consists of a list of arbitrary but simple
     * group names separated by commas, colons, or semicolons.
     * <p>
     * Groups are defined as follows where P is the prefix
     * and <i>n</i> is a running count that starts with 0:
     * <p>
     * P = <i>name1,name2,</i>...
     * <br>
     * P.<i>name.n</i> = principal<sub><i>n</i></sub>
     * <p>
     * When the agent is created, the given principals are
     * resolved to encrypting certificates where the X.501
     * subject distinguished name corresponds to the
     * principal. In general, certificates are located by
     * means of a <code>CertificateFinder</code> service.
     *
     * Value: <tt>crypt.group</tt>     
     */
    public static final String PROP_GROUP = "crypt.group";

    /**
     * The folder to group assignment property prefix.
     * Assignment properties consist of this prefix followed
     * by the group name, a period, and running count, starting
     * with 0. The value is the path that shall be assigned to
     * that group. Paths use slashes as separators and refer to
     * the agent's structure. Let P be the prefix then
     * <p>
     * P<i>name.n</i> = path<sub><i>n</i></sub>
     * <p>
     * defines the <i>n</i>'s path assigned to group <i>
     * name</i>. Please note that the prefix ends with a
     * period.
     *
     * Value: <tt>crypt.assign.</tt>     
     */
    public static final String PROP_ASSIGN = "crypt.assign.";

    /**
     * The property name of the property that identifies the
     * bulk encryption algorithm that is used to protect the
     * contents of agents.
     *
     * Value: <tt>crypt.algorithm</tt>     
     */
    public static final String PROP_ALGORITHM = "crypt.algorithm";

    /**
     * The property name of the property that gives the strength
     * of the bulk encryption algorithm that is used to protect
     * the contents of agents.
     *
     * Value: <tt>crypt.strength</tt>     
     */
    public static final String PROP_STRENGTH = "crypt.strength";

    /**
     * The well-known system name for SeMoA.
     *
     * Value: <tt>SeMoA</tt>     
     */
    public static final String SYSTEM_SEMOA = "SeMoA";

    /**
     * The well-known system name for Jade.
     *
     * Value: <tt>Jade</tt>     
     */
    public static final String SYSTEM_JADE = "Jade";

    /**
     * The well-known system name for Tracy.
     *
     * Value: <tt>Tracy</tt>     
     */
    public static final String SYSTEM_TRACY = "Tracy";

    /**
     * The well-known system name for enago Mobile (Grasshopper).
     *
     * Value: <tt>Grasshopper</tt>     
     */
    public static final String SYSTEM_GRASSHOPPER = "Grasshopper";

    /**
     * The well-known system name for Aglets.
     *
     * Value: <tt>Aglets</tt>     
     */
    public static final String SYSTEM_AGLETS = "Aglets";

    /**
     * The well-known system name for OSGi.
     *
     * Value: <tt>OSGi</tt>     
     */
    public static final String SYSTEM_OSGI = "OSGi";

    /**
     * The well-known type name for Java agents.
     *
     * Value: <tt>Java</tt>     
     */
    public static final String TYPE_JAVA = "Java";

    /**
     * The well-known type name for shell script agents.
     *
     * Value: <tt>shell</tt>     
     */
    public static final String TYPE_SHELL = "shell";

    /**
     * The well-known agent communiation language name for FIPA-ACL.
     *
     * Value: <tt>FIPA-ACL</tt>     
     */
    public static final String ACL_FIPAACL = "FIPA-ACL";

    /**
     * The well-known agent communication language name for KQML.
     *
     * Value: <tt>KQML</tt>     
     */
    public static final String ACL_KQML = "KQML";

    /**
     * The list of prefixes that are filtered out from the
     * properties before they are saved to the agent props.
     */
    private static String[] filter_ = { PROP_GROUP, PROP_ASSIGN, PROP_ALGORITHM, PROP_STRENGTH, PROP_AGENT_CLASSPATH, PROP_AGENT_IMPORT, PROP_AGENT_INCLUDE, PROP_AGENT_EXCLUDE };

    /**
     * The default properties. These properties are loaded
     * by means of a static initializer.
     */
    private static Properties defaults_ = new Properties();

    static {
        InputStream in;
        in = (InputStream) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                return ClassResource.getRelativeResourceAsStream(AgentStructure.class, DEFAULTS);
            }
        });
        if (in != null) {
            try {
                defaults_.load(in);
                in.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Filters the given properties and returns a new list of
     * properties that does not contain those properties that
     * are relevant only for creating the agent.
     *
     * @param props The properties that shall be filtered.
     * @return The filtered properties.
     */
    public static Properties filter(Properties props) {
        Map.Entry entry;
        Iterator i;
        String key;
        int n;
        props = (Properties) props.clone();
        for (i = props.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            key = (String) entry.getKey();
            for (n = 0; n < filter_.length; n++) {
                if (key != null && key.startsWith(filter_[n])) {
                    i.remove();
                    break;
                }
            }
        }
        return props;
    }

    /**
     * @return A shallow clone of the default properties.
     */
    public static Properties defaults() {
        return (Properties) defaults_.clone();
    }

    /**
     * No-one creates instances.
     */
    private AgentStructure() {
    }

    /**
     * Prints the default properties to the console.
     */
    public static void main(String[] argv) {
        try {
            AgentStructure.defaults().list(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
