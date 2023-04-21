package org.tolven.assembler.ear;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.Extension.Parameter;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.ExtensionPoint.ParameterDefinition;
import org.java.plugin.registry.PluginDescriptor;
import org.tolven.plugin.TolvenCommandPlugin;
import org.tolven.tools.ant.TolvenJar;

/**
 * This plugin is the main EAR assembler. It uses a number of subassemblers to complete its task
 * 
 * @author Joseph Isaac
 *
 */
public class EARAssembler extends TolvenCommandPlugin {

    public static final String ATTRIBUTE_TEMPLATE_APPXML = "template-applicationxml";

    public static final String CMD_DESTDIR_OPT = "destDir";

    public static final String CMD_EAR_FILE_OPT = "earFile";

    public static final String CMD_EAR_PLUGIN_OPT = "earPlugin";

    public static final String EXNPT_ABSTRACT_EAR = "abstractEAR";

    public static final String EXNPT_CONNECTORMODULE_ADPTR = "connectorModuleProduct-adaptor";

    @Deprecated
    public static final String EXNPT_CONNECTORMODULE_PRODUCT = "connectorModuleProduct";

    @Deprecated
    public static final String EXNPT_EJBMODULE = "ejbModule";

    public static final String EXNPT_EJBMODULE_ADPTR = "ejbModule-adaptor";

    public static final String EXNPT_EJBMODULE_DECL = "ejbModule-declaration";

    @Deprecated
    public static final String EXNPT_EJBMODULE_PRODUCT = "ejbModuleProduct";

    public static final String EXNPT_EJBMODULEPROD_ADPTR = "ejbModuleProduct-adaptor";

    public static final String EXNPT_ID = "extension-point";

    @Deprecated
    public static final String EXNPT_JAVAMODULE = "javaModule";

    @Deprecated
    public static final String EXNPT_JAVAMODULE_PRODUCT = "javaModuleProduct";

    @Deprecated
    public static final String EXNPT_JAVAMODULE_PRODUCT_PLUGIN = "javaModuleProductPlugin";

    public static final String EXNPT_LIBPROD_ADPTR = "libProduct-adaptor";

    @Deprecated
    public static final String EXNPT_LOCALEMODULE = "localeModule";

    public static final String EXNPT_LOCALEMODULE_ADPTR = "localeModule-adaptor";

    @Deprecated
    public static final String EXNPT_LOCALEMODULE_PRODUCT = "localeModuleProduct";

    public static final String EXNPT_LOCALEMODULEPROD_ADPTR = "localeModuleProduct-adaptor";

    @Deprecated
    public static final String EXNPT_METAINF = "META-INF";

    public static final String EXNPT_METAINF_ADPTR = "META-INF-adaptor";

    public static final String EXNPT_SECURITY_ROLE = "security-role";

    @Deprecated
    public static final String EXNPT_WARMODULE = "warModule";

    public static final String EXNPT_WARMODULE_ADPTR = "warModule-adaptor";

    public static final String EXNPT_WARMODULE_DECL = "warModule-declaration";

    public static final String EXNPT_WARMODULEPROD_ADPTR = "warModuleProduct-adaptor";

    @Deprecated
    public static final String EXNPT_WARMODULEPRODUCT = "warModuleProduct";

    public static final String SRC_PLUGIN_ID = "source-plugin-id";

    private Logger logger = Logger.getLogger(EARAssembler.class);

    protected void addConnectorModule(String rarFilename, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:if");
        writer.writeAttribute("test", "count(module/connector[text() = '" + rarFilename + "']) = 0");
        writer.writeCharacters("\n");
        writer.writeStartElement("module");
        writer.writeCharacters("\n");
        writer.writeStartElement("connector");
        writer.writeCharacters(rarFilename);
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addConnectorModuleTemplates(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws XMLStreamException, IOException {
        writer.writeStartElement("xsl:template");
        writer.writeAttribute("name", "addConnectorModules");
        writer.writeCharacters("\n");
        assembleConnectorModuleProducts(pd, writer);
        assembleConnectorModuleProductAdaptors(pd, localDestDir, writer);
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addEJBModuleTemplates(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws XMLStreamException, IOException {
        writer.writeStartElement("xsl:template");
        writer.writeAttribute("name", "addEJBModules");
        writer.writeCharacters("\n");
        assembleEJBModuleAdaptors(pd, writer);
        assembleEJBModuleProducts(pd, localDestDir, writer);
        assembleEJBModuleProductAdaptors(pd, localDestDir, writer);
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addEJBModuleXMLEntry(String jarFilename, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:if");
        writer.writeAttribute("test", "count(module/ejb[text() = '" + jarFilename + "']) = 0");
        writer.writeCharacters("\n");
        writer.writeStartElement("module");
        writer.writeCharacters("\n");
        writer.writeStartElement("ejb");
        writer.writeCharacters(jarFilename);
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addJavaLibraryTemplates(PluginDescriptor pd, File localDestDir) throws IOException {
        assembleLocaleModules(pd, localDestDir);
        assembleLocaleModuleProducts(pd, localDestDir);
        assembleJavaModules(pd, localDestDir);
        assembleJavaModuleProducts(pd, localDestDir);
        assembleJavaModuleProductPlugins(pd, localDestDir);
        assembleLibProductAdaptors(pd, localDestDir);
        assembleLocaleModuleProductAdaptors(pd, localDestDir);
    }

    protected void addMainTemplate(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:template");
        writer.writeAttribute("match", "/ | * | @* | text() | comment()");
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:copy");
        writer.writeAttribute("select", ".");
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:apply-templates");
        writer.writeAttribute("select", "* | @* | text() | comment()");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addRootConnectorModuleSelects(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:for-each");
        writer.writeAttribute("select", "tp:module/tp:connector");
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:copy-of");
        writer.writeAttribute("select", ".");
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:apply-templates");
        writer.writeAttribute("select", "* | @* | text() | comment()");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:call-template");
        writer.writeAttribute("name", "addConnectorModules");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addRootDisplayName(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:if");
        writer.writeAttribute("test", "count(display-name) = 0");
        writer.writeStartElement("xsl:element");
        writer.writeAttribute("name", "display-name");
        writer.writeCharacters("Tolven Application");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addRootEJBModuleSelects(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:for-each");
        writer.writeAttribute("select", "tp:module/tp:ejb");
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:copy-of");
        writer.writeAttribute("select", ".");
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:apply-templates");
        writer.writeAttribute("select", "* | @* | text() | comment()");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:call-template");
        writer.writeAttribute("name", "addEJBModules");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addRootSecurityRoleSelects(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:for-each");
        writer.writeAttribute("select", "tp:security-role");
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:copy-of");
        writer.writeAttribute("select", ".");
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:apply-templates");
        writer.writeAttribute("select", "* | @* | text() | comment()");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:call-template");
        writer.writeAttribute("name", "addSecurityRoles");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addRootTemplate(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:template");
        writer.writeAttribute("match", "tp:application");
        writer.writeCharacters("\n");
        writer.writeStartElement("application");
        writer.writeAttribute("version", "{@version}");
        writer.writeAttribute("xmlns", "http://java.sun.com/xml/ns/javaee");
        writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        writer.writeAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/application_6.xsd");
        writer.writeCharacters("\n");
        addRootDisplayName(writer);
        addRootConnectorModuleSelects(writer);
        addRootEJBModuleSelects(writer);
        addRootWEBModuleSelects(writer);
        addRootSecurityRoleSelects(writer);
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addRootWEBModuleSelects(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:for-each");
        writer.writeAttribute("select", "tp:module/tp:web");
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:copy-of");
        writer.writeAttribute("select", ".");
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:apply-templates");
        writer.writeAttribute("select", "* | @* | text() | comment()");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeStartElement("xsl:call-template");
        writer.writeAttribute("name", "addWEBModules");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addSecurityRoles(String description, String roleName, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:if");
        writer.writeAttribute("test", "count(security-role/role-name[text() = '" + roleName + "']) = 0");
        writer.writeCharacters("\n");
        writer.writeStartElement("security-role");
        writer.writeCharacters("\n");
        if (description != null) {
            writer.writeStartElement("description");
            writer.writeCharacters(description);
            writer.writeEndElement();
            writer.writeCharacters("\n");
        }
        writer.writeStartElement("role-name");
        writer.writeCharacters(roleName);
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addSecurityRoleTemplates(PluginDescriptor pd, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:template");
        writer.writeAttribute("name", "addSecurityRoles");
        writer.writeCharacters("\n");
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_SECURITY_ROLE);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                String description = null;
                if (exn.getParameter("description") != null) {
                    description = exn.getParameter("description").valueAsString();
                }
                String roleName = exn.getParameter("role-name").valueAsString();
                addSecurityRoles(description, roleName, writer);
            }
            writer.writeEndElement();
            writer.writeCharacters("\n");
        }
    }

    protected void addWARModuleTemplates(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws XMLStreamException, IOException {
        writer.writeStartElement("xsl:template");
        writer.writeAttribute("name", "addWEBModules");
        writer.writeCharacters("\n");
        assembleWARModules(pd, writer);
        assembleWARModuleProducts(pd, localDestDir, writer);
        assembleWARModuleProductsPlugins(pd, localDestDir, writer);
        assembleWARModuleAdaptors(pd, writer);
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    protected void addWebModules(String webURI, String contextRoot, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("xsl:if");
        writer.writeAttribute("test", "count(module/web-uri[text() = '" + webURI + "']) = 0");
        writer.writeCharacters("\n");
        writer.writeStartElement("module");
        writer.writeCharacters("\n");
        writer.writeStartElement("web");
        writer.writeCharacters("\n");
        writer.writeStartElement("web-uri");
        writer.writeCharacters(webURI);
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeStartElement("context-root");
        writer.writeCharacters(contextRoot);
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    /**
     * Add connector jars located by extension-point connectorModuleProduct-adaptor to an EAR
     * 
     * @param pd
     * @param writer
     * @throws IOException
     * @throws XMLStreamException
     */
    protected void assembleConnectorModuleProductAdaptors(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws IOException, XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_CONNECTORMODULE_ADPTR);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                for (File src : getAdaptorFiles(exn)) {
                    File dest = new File(localDestDir, "/" + src.getName());
                    dest.getParentFile().mkdirs();
                    logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                    FileUtils.copyFile(src, dest);
                    addConnectorModule(dest.getName(), writer);
                }
            }
        }
    }

    protected void assembleConnectorModuleProducts(PluginDescriptor pd, XMLStreamWriter writer) throws XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_CONNECTORMODULE_PRODUCT);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                String rarName = exn.getParameter("rar").valueAsString();
                addConnectorModule(rarName, writer);
            }
        }
    }

    protected void assembleEAR(PluginDescriptor pd, String earFilename, File destDir) throws IOException, XMLStreamException {
        File localDestDir = new File(getPluginTmpDir(), pd.getId());
        localDestDir.mkdirs();
        File srcXML = new File(localDestDir, "META-INF/application.xml");
        String templateFilename = getDescriptor().getAttribute(ATTRIBUTE_TEMPLATE_APPXML).getValue();
        File templateFile = getFilePath(templateFilename);
        if (!templateFile.exists()) {
            throw new RuntimeException("Could not locate: '" + templateFile.getPath() + "' in " + getDescriptor().getId());
        }
        srcXML.getParentFile().mkdirs();
        logger.debug("Copy " + templateFile + " to " + srcXML);
        FileUtils.copyFile(templateFile, srcXML);
        assemblerMetaInf(pd, localDestDir);
        assemblerMetaInfAdaptors(pd, localDestDir);
        StringBuffer originalXML = new StringBuffer();
        originalXML.append(FileUtils.readFileToString(srcXML));
        String xslt = getXSLT(pd, localDestDir);
        File applicationxmlXSLT = new File(getPluginTmpDir(), "applicationxml-xslt.xml");
        logger.debug("Write application.xml XSLT to " + applicationxmlXSLT.getPath());
        FileUtils.writeStringToFile(applicationxmlXSLT, xslt);
        String translatedXMLString = getTranslatedXML(originalXML.toString(), xslt);
        srcXML.getParentFile().mkdirs();
        logger.debug("Write translated application.xml to " + srcXML.getPath());
        FileUtils.writeStringToFile(srcXML, translatedXMLString);
        buildEAR(earFilename, localDestDir, destDir);
    }

    /**
     * Assemble an ejb jar located by ejbModule-adaptor to an EAR
     * 
     * @param pd
     * @param writer
     * @throws IOException
     * @throws XMLStreamException
     */
    protected void assembleEJBModuleAdaptors(PluginDescriptor pd, XMLStreamWriter writer) throws IOException, XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_EJBMODULE_ADPTR);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                String ejbFilename = exn.getParameter("ejbFile").valueAsString();
                if (ejbFilename == null || ejbFilename.length() == 0) {
                    throw new RuntimeException(exn.getUniqueId() + " must have a value for ejbFile");
                }
                addEJBModuleXMLEntry(ejbFilename, writer);
            }
        }
    }

    /**
     * Add ejb jars located by extension-point ejbModuleProduct-adaptor to an EAR
     * 
     * @param pd
     * @param writer
     * @throws IOException
     * @throws XMLStreamException
     */
    protected void assembleEJBModuleProductAdaptors(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws IOException, XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_EJBMODULEPROD_ADPTR);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                for (File src : getAdaptorFiles(exn)) {
                    File dest = new File(localDestDir, "/" + src.getName());
                    dest.getParentFile().mkdirs();
                    logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                    FileUtils.copyFile(src, dest);
                    addEJBModuleXMLEntry(dest.getName(), writer);
                }
            }
        }
    }

    @Deprecated
    protected void assembleEJBModuleProducts(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws IOException, XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_EJBMODULE_PRODUCT);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                String ejbModuleJARName = exn.getParameter("jar").valueAsString();
                File src = getFilePath(exn.getDeclaringPluginDescriptor(), ejbModuleJARName);
                File dest = new File(localDestDir, "/" + ejbModuleJARName);
                dest.getParentFile().mkdirs();
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                FileUtils.copyFile(src, dest);
                addEJBModuleXMLEntry(ejbModuleJARName, writer);
            }
        }
    }

    @Deprecated
    protected void assembleJavaModuleProductPlugins(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_JAVAMODULE_PRODUCT_PLUGIN);
        if (exnPt != null) {
            for (Extension exn : pd.getExtensionPoint(EXNPT_JAVAMODULE_PRODUCT_PLUGIN).getConnectedExtensions()) {
                String pluginId = exn.getParameter("target-plugin-id").valueAsString();
                PluginDescriptor targetPD = getManager().getRegistry().getPluginDescriptor(pluginId);
                Parameter exnPtParam = exn.getParameter("extension-point");
                if (exnPtParam == null || exnPtParam.valueAsString() == null) {
                    throw new RuntimeException(exn + " must have a parameter called extension-point with a value");
                }
                String exnPtId = exnPtParam.valueAsString();
                ExtensionPoint targetExnPt = targetPD.getExtensionPoint(exnPtId);
                if (targetExnPt == null) {
                    throw new RuntimeException(targetPD + " must have an extension point " + exnPtId);
                }
                for (Parameter libParam : exnPtParam.getSubParameters("name")) {
                    String defaultValueParam = libParam.valueAsString();
                    String defaultValue = targetExnPt.getParameterDefinition(defaultValueParam).getDefaultValue();
                    if (defaultValue == null) {
                        throw new RuntimeException(targetExnPt + " must have a parameter-def " + defaultValueParam);
                    }
                    File src = getFilePath(targetPD, defaultValue);
                    File dest = new File(localDestDir, "/lib/" + src.getName());
                    dest.getParentFile().mkdirs();
                    logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                    FileUtils.copyFile(src, dest);
                }
            }
        }
    }

    @Deprecated
    protected void assembleJavaModuleProducts(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_JAVAMODULE_PRODUCT);
        if (exnPt != null) {
            assembleLibraries(exnPt, localDestDir);
        }
    }

    @Deprecated
    protected void assembleJavaModules(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_JAVAMODULE);
        if (exnPt != null) {
            assembleLibraries(exnPt, localDestDir);
        }
    }

    /**
     * Add libraries located by extension-point libProduct-adaptor to an EAR
     * 
     * @param pd
     * @throws IOException
     */
    protected void assembleLibProductAdaptors(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_LIBPROD_ADPTR);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                for (File src : getAdaptorFiles(exn)) {
                    File dest = new File(localDestDir, "/lib/" + src.getName());
                    dest.getParentFile().mkdirs();
                    logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                    FileUtils.copyFile(src, dest);
                }
            }
        }
    }

    private void assembleLibraries(ExtensionPoint exnPt, File localDestDir) throws IOException {
        for (Extension exn : exnPt.getConnectedExtensions()) {
            List<File> srcs = new ArrayList<File>();
            Parameter jarParam = exn.getParameter("jar");
            if (jarParam != null) {
                String filename = jarParam.valueAsString();
                File src = getFilePath(exn.getDeclaringPluginDescriptor(), filename);
                srcs.add(src);
            }
            Parameter dirParam = exn.getParameter("dir");
            if (dirParam != null) {
                String dirname = dirParam.valueAsString();
                File srcDir = getFilePath(exn.getDeclaringPluginDescriptor(), dirname);
                for (Object obj : FileUtils.listFiles(srcDir, new String[] { "jar" }, false)) {
                    File src = (File) obj;
                    srcs.add(src);
                }
            }
            for (File src : srcs) {
                File dest = new File(localDestDir, "/lib/" + src.getName());
                dest.getParentFile().mkdirs();
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                FileUtils.copyFile(src, dest);
            }
        }
    }

    /**
     * Add locales (as libraries) located by extension-point libProduct-adaptor to an EAR
     * 
     * @param pd
     * @throws IOException
     */
    protected void assembleLocaleModuleProductAdaptors(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_LOCALEMODULEPROD_ADPTR);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                for (File src : getAdaptorFiles(exn)) {
                    File dest = new File(localDestDir, "/lib/" + src.getName());
                    dest.getParentFile().mkdirs();
                    logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                    FileUtils.copyFile(src, dest);
                }
            }
        }
    }

    @Deprecated
    protected void assembleLocaleModuleProducts(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_LOCALEMODULE_PRODUCT);
        if (exnPt != null) {
            for (Extension localeModuleProductExtension : exnPt.getConnectedExtensions()) {
                String srcName = localeModuleProductExtension.getParameter("jar").valueAsString();
                File src = getFilePath(localeModuleProductExtension.getDeclaringPluginDescriptor(), srcName);
                File dest = new File(localDestDir, "/lib/" + src.getName());
                dest.getParentFile().mkdirs();
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                FileUtils.copyFile(src, dest);
            }
        }
    }

    @Deprecated
    protected void assembleLocaleModules(PluginDescriptor pd, File localDestDir) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_LOCALEMODULE);
        if (exnPt != null) {
            for (Extension localeModuleExtension : exnPt.getConnectedExtensions()) {
                File localeModulePluginTmpDir = getPluginTmpDir(localeModuleExtension.getDeclaringPluginDescriptor());
                String srcName = localeModuleExtension.getParameter("jar").valueAsString();
                File src = new File(localeModulePluginTmpDir, srcName);
                File dest = new File(localDestDir, "/lib/" + src.getName());
                dest.getParentFile().mkdirs();
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                FileUtils.copyFile(src, dest);
            }
        }
    }

    @Deprecated
    protected void assemblerMetaInf(PluginDescriptor pd, File localDestDir) {
        ExtensionPoint metaInfExnPt = pd.getExtensionPoint(EXNPT_METAINF);
        if (metaInfExnPt != null) {
            for (Extension exn : metaInfExnPt.getConnectedExtensions()) {
                Parameter metaInfParam = exn.getParameter("dir");
                if (metaInfParam == null || metaInfParam.valueAsString().trim().length() == 0) {
                    throw new RuntimeException(exn.getUniqueId() + " must supply a value for the dir parameter");
                }
                PluginDescriptor metaInfPD = exn.getDeclaringPluginDescriptor();
                File src = getFilePath(metaInfPD, metaInfParam.valueAsString());
                File dest = new File(localDestDir, "/META-INF");
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                try {
                    FileUtils.copyDirectory(src, dest);
                } catch (IOException ex) {
                    throw new RuntimeException("Could not copy meta-inf files from " + src.getPath() + " to " + dest.getPath(), ex);
                }
            }
        }
    }

    /**
     * Add directory contents located by extension-point META-INF-adaptor to an EAR
     * 
     * @param pd
     */
    protected void assemblerMetaInfAdaptors(PluginDescriptor pd, File localDestDir) {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_METAINF_ADPTR);
        if (exnPt != null) {
            File dest = new File(localDestDir, "/META-INF");
            for (Extension exn : exnPt.getConnectedExtensions()) {
                for (File src : getAdaptorFiles(exn)) {
                    logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                    try {
                        FileUtils.copyDirectory(src, dest);
                    } catch (IOException ex) {
                        throw new RuntimeException("Could not copy meta-inf files from " + src.getPath() + " to " + dest.getPath(), ex);
                    }
                }
            }
        }
    }

    protected void assembleWARModuleAdaptors(PluginDescriptor pd, XMLStreamWriter writer) throws XMLStreamException {
        ExtensionPoint warAdptrExnPt = pd.getExtensionPoint(EXNPT_WARMODULE_ADPTR);
        if (warAdptrExnPt != null) {
            for (Extension warExn : warAdptrExnPt.getConnectedExtensions()) {
                String contextId = null;
                Parameter contextIdParam = warExn.getParameter("parent-context-id");
                if (contextIdParam != null) {
                    contextId = contextIdParam.valueAsString();
                }
                String pluginId = warExn.getParameter(SRC_PLUGIN_ID).valueAsString();
                if (pluginId == null || pluginId.trim().length() == 0) {
                    throw new RuntimeException("No parameter value for " + SRC_PLUGIN_ID + " found in " + warExn.getUniqueId());
                }
                PluginDescriptor warPD = getManager().getRegistry().getPluginDescriptor(pluginId);
                if (getWarModuleDeclarationExtensionPoint(warPD, contextId) == null) {
                    throw new RuntimeException(warPD.getId() + " is does not define a war-declaration extension-point for: " + warExn.getUniqueId());
                }
                String webURI = warExn.getParameter("web-uri").valueAsString();
                String contextRoot = warExn.getParameter("context-root").valueAsString();
                addWebModules(webURI, contextRoot, writer);
            }
        }
    }

    @Deprecated
    protected void assembleWARModuleProducts(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws IOException, XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_WARMODULEPRODUCT);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                String webURI = exn.getParameter("web-uri").valueAsString();
                File src = getFilePath(exn.getDeclaringPluginDescriptor(), webURI);
                File dest = new File(localDestDir, "/" + src.getName());
                dest.getParentFile().mkdirs();
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                FileUtils.copyFile(src, dest);
                String contextRoot = exn.getParameter("context-root").valueAsString();
                addWebModules(webURI, contextRoot, writer);
            }
        }
    }

    protected void assembleWARModuleProductsPlugins(PluginDescriptor pd, File localDestDir, XMLStreamWriter writer) throws IOException, XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_WARMODULEPROD_ADPTR);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                for (File src : getAdaptorFiles(exn)) {
                    File dest = new File(localDestDir, "/" + src.getName());
                    dest.getParentFile().mkdirs();
                    logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                    FileUtils.copyFile(src, dest);
                    String webURI = exn.getParameter("web-uri").valueAsString();
                    String contextRoot = exn.getParameter("context-root").valueAsString();
                    addWebModules(webURI, contextRoot, writer);
                }
            }
        }
    }

    @Deprecated
    protected void assembleWARModules(PluginDescriptor pd, XMLStreamWriter writer) throws XMLStreamException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_WARMODULE);
        if (exnPt != null) {
            for (Extension exn : exnPt.getConnectedExtensions()) {
                String webURI = exn.getParameter("web-uri").valueAsString();
                String contextRoot = exn.getParameter("context-root").valueAsString();
                addWebModules(webURI, contextRoot, writer);
            }
        }
    }

    protected void buildEAR(String earFilename, File localDestDir, File destDir) {
        File earFile = new File(destDir, earFilename);
        earFile.getParentFile().mkdirs();
        logger.debug("Jar " + localDestDir.getPath() + " to " + earFile.getPath());
        TolvenJar.jar(localDestDir, earFile);
    }

    @Override
    protected void doStart() throws Exception {
        logger.debug("*** start ***");
        logger.debug("deleting: " + getPluginTmpDir());
        FileUtils.deleteDirectory(getPluginTmpDir());
        getPluginTmpDir().mkdirs();
    }

    @Override
    protected void doStop() throws Exception {
        logger.debug("*** stop ***");
    }

    @Override
    public void execute(String[] args) throws Exception {
        logger.debug("*** execute ***");
        CommandLine commandLine = getCommandLine(args);
        String pluginId = commandLine.getOptionValue(CMD_EAR_PLUGIN_OPT);
        String earFilename = commandLine.getOptionValue(CMD_EAR_FILE_OPT);
        String destDirname = commandLine.getOptionValue(CMD_DESTDIR_OPT);
        if (destDirname == null) {
            destDirname = new File(getStageDir(), pluginId).getPath();
        }
        File destDir = new File(destDirname);
        destDir.mkdirs();
        PluginDescriptor pd = getManager().getRegistry().getPluginDescriptor(pluginId);
        executeRequiredPlugins(pd);
        assembleEAR(pd, earFilename, destDir);
    }

    @Deprecated
    protected void executeRequiredConnectorModuleProducts(PluginDescriptor pd) throws Exception {
        ExtensionPoint abstractExnPt = getAbstractEARPluginDescriptor().getExtensionPoint(EXNPT_CONNECTORMODULE_PRODUCT);
        for (ExtensionPoint exnPt : abstractExnPt.getDescendants()) {
            if (exnPt.getDeclaringPluginDescriptor().getId().equals(pd.getId())) {
                File destDir = new File(getPluginTmpDir(), pd.getId());
                destDir.mkdirs();
                for (Extension exn : exnPt.getConnectedExtensions()) {
                    PluginDescriptor rarPluginDescriptor = exn.getDeclaringPluginDescriptor();
                    String rarFilename = exn.getParameter("rar").valueAsString();
                    File src = getFilePath(rarPluginDescriptor, rarFilename);
                    logger.debug("Copy " + src.getPath() + " to " + destDir.getPath());
                    FileUtils.copyFileToDirectory(src, destDir);
                }
            }
        }
    }

    protected void executeRequiredEJBModuleAdaptors(PluginDescriptor pd) throws Exception {
        ExtensionPoint abstractExnPt = getAbstractEARPluginDescriptor().getExtensionPoint(EXNPT_EJBMODULE_ADPTR);
        File destDir = new File(getPluginTmpDir(), pd.getId());
        destDir.mkdirs();
        for (ExtensionPoint exnPt : abstractExnPt.getDescendants()) {
            if (exnPt.getDeclaringPluginDescriptor().getId().equals(pd.getId())) {
                for (Extension exn : exnPt.getConnectedExtensions()) {
                    String pluginId = exn.getParameter(SRC_PLUGIN_ID).valueAsString();
                    if (pluginId == null || pluginId.trim().length() == 0) {
                        throw new RuntimeException("No parameter value for " + SRC_PLUGIN_ID + " found in " + exn.getUniqueId());
                    }
                    PluginDescriptor ejbPD = getManager().getRegistry().getPluginDescriptor(pluginId);
                    if (ejbPD.getExtensionPoint(EXNPT_EJBMODULE_DECL) == null) {
                        throw new RuntimeException(ejbPD.getId() + " does not define a ejbModule-declaration extension-point");
                    }
                    String ejbFilename = exn.getParameter("ejbFile").valueAsString();
                    String argString = "-ejbPlugin " + ejbPD.getId() + " -ejbFile " + ejbFilename + " -destDir " + destDir.getPath();
                    execute("org.tolven.assembler.ejbmodule", argString.split(" "));
                }
            }
        }
    }

    protected void executeRequiredPlugins(PluginDescriptor pd) throws Exception {
        String[] args = new String[0];
        execute("org.tolven.assembler.localemodule", args);
        execute("org.tolven.assembler.javamodule", args);
        executeRequiredConnectorModuleProducts(pd);
        executeRequiredEJBModuleAdaptors(pd);
        executeRequiredWarModulePlugins(pd);
        executeRequiredWarModuleAdaptors(pd);
    }

    protected void executeRequiredWarModuleAdaptors(PluginDescriptor pd) throws Exception {
        ExtensionPoint warAdptrExnPt = pd.getExtensionPoint(EXNPT_WARMODULE_ADPTR);
        if (warAdptrExnPt != null) {
            File destDir = new File(getPluginTmpDir(), pd.getId());
            destDir.mkdirs();
            for (Extension warExn : warAdptrExnPt.getConnectedExtensions()) {
                String warPluginId = warExn.getParameter(SRC_PLUGIN_ID).valueAsString();
                if (warPluginId == null || warPluginId.trim().length() == 0) {
                    throw new RuntimeException("No parameter value for " + SRC_PLUGIN_ID + " found in " + warExn.getUniqueId());
                }
                PluginDescriptor warPD = getManager().getRegistry().getPluginDescriptor(warPluginId);
                Parameter parentContextIdParam = warExn.getParameter("parent-context-id");
                String parentContextId = null;
                if (parentContextIdParam != null) {
                    parentContextId = parentContextIdParam.valueAsString();
                }
                ExtensionPoint declExnPt = getWarModuleDeclarationExtensionPoint(warPD, parentContextId);
                if (declExnPt == null) {
                    throw new RuntimeException(warPD.getId() + " does not define a warModule-declaration extension-point with contextId: " + parentContextId);
                }
                String contextId = declExnPt.getParameterDefinition("context-id").getDefaultValue();
                String webURI = warExn.getParameter("web-uri").valueAsString();
                String argString = "-warPlugin " + warPD.getId() + " -webURI " + webURI + " -contextId " + contextId + " -destDir " + destDir.getPath();
                execute("org.tolven.assembler.war", argString.split(" "));
            }
        }
    }

    protected void executeRequiredWarModulePlugins(PluginDescriptor pd) throws Exception {
        ExtensionPoint abstractExnPt = getAbstractEARPluginDescriptor().getExtensionPoint(EXNPT_WARMODULE);
        File destDir = new File(getPluginTmpDir(), pd.getId());
        destDir.mkdirs();
        for (ExtensionPoint exnPt : abstractExnPt.getDescendants()) {
            if (exnPt.getDeclaringPluginDescriptor().getId().equals(pd.getId())) {
                for (Extension exn : exnPt.getConnectedExtensions()) {
                    PluginDescriptor warPluginDescriptor = exn.getDeclaringPluginDescriptor();
                    String webURI = exn.getParameter("web-uri").valueAsString();
                    String argString = "-warPlugin " + warPluginDescriptor.getId() + " -webURI " + webURI + " -destDir " + destDir.getPath();
                    execute("org.tolven.assembler.war", argString.split(" "));
                }
            }
        }
    }

    protected PluginDescriptor getAbstractEARPluginDescriptor() {
        ExtensionPoint exnPt = getDescriptor().getExtensionPoint(EXNPT_ABSTRACT_EAR);
        String pluginId = exnPt.getParentPluginId();
        PluginDescriptor pd = getManager().getRegistry().getPluginDescriptor(pluginId);
        return pd;
    }

    private List<File> getAdaptorFiles(Extension exn) {
        String pluginId = exn.getParameter(SRC_PLUGIN_ID).valueAsString();
        if (pluginId == null || pluginId.trim().length() == 0) {
            throw new RuntimeException("No parameter value for " + SRC_PLUGIN_ID + " found in " + exn.getUniqueId());
        }
        String exnPtId = exn.getParameter(EXNPT_ID).valueAsString();
        if (exnPtId == null || exnPtId.trim().length() == 0) {
            throw new RuntimeException("No parameter value for " + EXNPT_ID + " found in " + exn.getUniqueId());
        }
        ExtensionPoint exnPt = getManager().getRegistry().getExtensionPoint(pluginId + "@" + exnPtId);
        List<File> files = new ArrayList<File>();
        for (ParameterDefinition paramDef : exnPt.getParameterDefinitions()) {
            String filename = paramDef.getDefaultValue();
            if (filename == null || filename.trim().length() == 0) {
                throw new RuntimeException("No default-value for parameter-def found in " + exnPt.getUniqueId());
            }
            File src = getFilePath(exnPt.getDeclaringPluginDescriptor(), filename);
            files.add(src);
        }
        return files;
    }

    private CommandLine getCommandLine(String[] args) {
        GnuParser parser = new GnuParser();
        try {
            return parser.parse(getCommandOptions(), args, true);
        } catch (ParseException ex) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(getClass().getName(), getCommandOptions());
            throw new RuntimeException("Could not parse command line for: " + getClass().getName(), ex);
        }
    }

    private Options getCommandOptions() {
        Options cmdLineOptions = new Options();
        Option warPluginOption = new Option(CMD_EAR_PLUGIN_OPT, CMD_EAR_PLUGIN_OPT, true, "ear plugin");
        warPluginOption.setRequired(true);
        cmdLineOptions.addOption(warPluginOption);
        Option webURIPluginOption = new Option(CMD_EAR_FILE_OPT, CMD_EAR_FILE_OPT, true, "ear filename");
        webURIPluginOption.setRequired(true);
        cmdLineOptions.addOption(webURIPluginOption);
        Option destDirPluginOption = new Option(CMD_DESTDIR_OPT, CMD_DESTDIR_OPT, true, "destination directory");
        cmdLineOptions.addOption(destDirPluginOption);
        return cmdLineOptions;
    }

    private ExtensionPoint getWarModuleDeclarationExtensionPoint(PluginDescriptor pd, String contextId) {
        List<ExtensionPoint> declExnPts = getWarModuleDeclarationExtensionPoints(pd);
        if (declExnPts.isEmpty()) {
            throw new RuntimeException(pd.getId() + " does not define a warModule-declaration extension-point");
        }
        if (contextId == null) {
            if (declExnPts.size() > 1) {
                throw new RuntimeException(pd.getId() + " has more than one warModule-declaration. A contextId must be provided");
            }
            if (declExnPts.get(0).getParameterDefinition("context-id") == null) {
                throw new RuntimeException(declExnPts.get(0).getUniqueId() + " must define a context-id parameter");
            }
            contextId = declExnPts.get(0).getParameterDefinition("context-id").getDefaultValue();
        }
        for (ExtensionPoint declExnPt : declExnPts) {
            if (declExnPt.getParameterDefinition("context-id") == null) {
                throw new RuntimeException(declExnPt.getUniqueId() + " must define a context-id parameter");
            }
            if (contextId.equals(declExnPt.getParameterDefinition("context-id").getDefaultValue())) {
                return declExnPt;
            }
        }
        return null;
    }

    private List<ExtensionPoint> getWarModuleDeclarationExtensionPoints(PluginDescriptor pd) {
        List<ExtensionPoint> declExnPts = new ArrayList<ExtensionPoint>();
        for (ExtensionPoint exnPt : pd.getExtensionPoints()) {
            if (EXNPT_WARMODULE_DECL.equals(exnPt.getParentExtensionPointId())) {
                declExnPts.add(exnPt);
            }
        }
        return declExnPts;
    }

    protected String getXSLT(PluginDescriptor pd, File localDestDir) throws XMLStreamException, IOException {
        StringWriter xslt = new StringWriter();
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = null;
        try {
            writer = factory.createXMLStreamWriter(xslt);
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");
            writer.writeStartElement("xsl:stylesheet");
            writer.writeAttribute("version", "2.0");
            writer.writeNamespace("xsl", "http://www.w3.org/1999/XSL/Transform");
            writer.writeNamespace("tp", "http://java.sun.com/xml/ns/javaee");
            writer.writeCharacters("\n");
            writer.writeStartElement("xsl:output");
            writer.writeAttribute("method", "xml");
            writer.writeAttribute("indent", "yes");
            writer.writeAttribute("encoding", "UTF-8");
            writer.writeAttribute("omit-xml-declaration", "no");
            writer.writeEndElement();
            writer.writeCharacters("\n");
            addMainTemplate(writer);
            addRootTemplate(writer);
            addJavaLibraryTemplates(pd, localDestDir);
            addConnectorModuleTemplates(pd, localDestDir, writer);
            addEJBModuleTemplates(pd, localDestDir, writer);
            addWARModuleTemplates(pd, localDestDir, writer);
            addSecurityRoleTemplates(pd, writer);
            writer.writeEndDocument();
            writer.writeEndDocument();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return xslt.toString();
    }
}
