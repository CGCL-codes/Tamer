package com.techm.gisv.cqp.framework;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

/**
 * Loads a list of package names from a package name XML file.
 * 
 * @author Rick Giles
 * @version 4-Dec-2002
 */
public final class PackageNamesLoader extends AbstractLoader {

    private static Logger logger = Logger.getLogger(CustomLibrariesClassLoader.class);

    /** the public ID for the configuration dtd. */
    private static final String DTD_PUBLIC_ID = "-//Puppy Crawl//DTD Package Names 1.0//EN";

    /** the resource for the configuration dtd. */
    private static final String DTD_RESOURCE_NAME = "com/puppycrawl/tools/checkstyle/packages_1_0.dtd";

    /**
     * Name of default checkstyle package names resource file. The file must be
     * in the classpath.
     */
    private static final String DEFAULT_PACKAGES = "com/puppycrawl/tools/checkstyle/checkstyle_packages.xml";

    /** The loaded package names. */
    private Stack mPackageStack = new Stack();

    private static List sPackages;

    /**
     * Creates a new <code>PackageNamesLoader</code> instance.
     * 
     * @throws ParserConfigurationException if an error occurs
     * @throws SAXException if an error occurs
     */
    private PackageNamesLoader() throws ParserConfigurationException, SAXException {
        super(DTD_PUBLIC_ID, DTD_RESOURCE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void startElement(String aNamespaceURI, String aLocalName, String aQName, Attributes aAtts) throws SAXException {
        if (aQName.equals("package")) {
            final String name = aAtts.getValue("name");
            if (name == null) {
                throw new SAXException("missing package name");
            }
            mPackageStack.push(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void endElement(String aNamespaceURI, String aLocalName, String aQName) {
        if (aQName.equals("package")) {
            String packageName = getPackageName();
            if (!sPackages.contains(packageName)) {
                sPackages.add(packageName);
            }
            mPackageStack.pop();
        }
    }

    /**
     * Creates a full package name from the package names on the stack.
     * 
     * @return the full name of the current package.
     */
    private String getPackageName() {
        final StringBuffer buf = new StringBuffer();
        final Iterator it = mPackageStack.iterator();
        while (it.hasNext()) {
            final String subPackage = (String) it.next();
            buf.append(subPackage);
            if (!subPackage.endsWith(".")) {
                buf.append(".");
            }
        }
        return buf.toString();
    }

    /**
     * Returns the default list of package names.
     * 
     * @param aClassLoader the class loader that gets the default package names.
     * @return the default list of package names.
     */
    public static List getPackageNames(ClassLoader aClassLoader) {
        if (sPackages == null) {
            sPackages = new ArrayList();
            PackageNamesLoader nameLoader = null;
            try {
                nameLoader = new PackageNamesLoader();
                final InputStream stream = aClassLoader.getResourceAsStream(DEFAULT_PACKAGES);
                InputSource source = new InputSource(stream);
                nameLoader.parseInputSource(source);
            } catch (ParserConfigurationException e) {
                logger.error("unable to parse  ", e);
            } catch (SAXException e) {
                logger.error("unable to parse  ", e);
            } catch (IOException e) {
                logger.error("unable to read  ", e);
            }
            try {
                Enumeration packageFiles = aClassLoader.getResources("checkstyle_packages.xml");
                while (packageFiles.hasMoreElements()) {
                    URL aPackageFile = (URL) packageFiles.nextElement();
                    InputStream iStream = null;
                    try {
                        iStream = new BufferedInputStream(aPackageFile.openStream());
                        InputSource source = new InputSource(iStream);
                        nameLoader.parseInputSource(source);
                    } catch (SAXException e) {
                        logger.error("unable to parse  ", e);
                    } catch (IOException e) {
                        logger.error("unable to parse  ", e);
                    } finally {
                        if (iStream != null) {
                            iStream.close();
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("IO Exception  ", e);
            }
        }
        return sPackages;
    }

    /**
     * Refreshes the cached package names.
     */
    public static void refresh() {
        sPackages = null;
    }
}
