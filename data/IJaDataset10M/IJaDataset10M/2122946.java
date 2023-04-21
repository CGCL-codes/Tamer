package org.nexopenframework.ide.eclipse.ui.dialogs;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.nexopenframework.ide.eclipse.commons.io.FileUtils;
import org.nexopenframework.ide.eclipse.commons.log.Logger;
import org.nexopenframework.ide.eclipse.commons.xml.ContentHandlerCallback;
import org.nexopenframework.ide.eclipse.commons.xml.ContentHandlerTemplate;
import org.nexopenframework.ide.eclipse.commons.xml.XMLUtils;
import org.nexopenframework.ide.eclipse.ui.util.NexOpenProjectUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Tab control for Oracle DataSource configuration for for Bea WL 9.x</p>
 * 
 * @see org.nexopenframework.ide.eclipse.ui.dialogs.OracleTabControl
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
class WLOracleTabControl extends OracleTabControl {

    /**
	 * <p>Copy necessary onfiguration to artifact (EAR or WAR module) in orde to create an internal 
	 * <code>javax.sql.DataSource</code></p>
	 * 
	 * @see org.nexopenframework.ide.eclipse.ui.dialogs.OracleTabControl#performOkInternal(org.eclipse.core.resources.IProject,
	 *      org.nexopenframework.ide.eclipse.ui.dialogs.AppServerPropertyPage)
	 */
    protected void performOkInternal(final IProject project, final AppServerPropertyPage _page) {
        final WLPropertyPage page = (WLPropertyPage) _page;
        IFile file = project.getFile(new Path("src/main/config/weblogic/oracle-jdbc.xml"));
        final StringBuilder name = new StringBuilder();
        ContentHandlerTemplate.handle(file, new ContentHandlerCallback() {

            @SuppressWarnings("unchecked")
            public void processHandle(Document doc) {
                updateValues(name, doc);
            }
        });
        if (isAutoDeploy()) {
            final boolean webProject = NexOpenProjectUtils.isWebProject(project);
            if (webProject) {
                final StringBuilder message = new StringBuilder("This feature for WEB projects is not supported by NexOpen IDE. You must explicetly create ");
                message.append("a javax.sql.DataSource in your Bea WebLogic console. Nevertheless, it is supported by EAR NexOpen applications. ");
                message.append("In other versions of NexOpen IDE we will use JSR-88 for deploying to your Bea WebLogic.");
                MessageDialog.openInformation(_page.getShell(), "NexOpen Bea WebLogic Plugin", message.toString());
            } else {
                final IFile earResources = page.getProject().getFile("ear/src/main/resources");
                final File fResources = new File(earResources.getLocationURI().getPath());
                if (fResources != null && fResources.exists()) {
                    File jdbcConfig = new File(fResources, "jdbc");
                    if (!jdbcConfig.exists()) {
                        jdbcConfig.mkdir();
                    }
                    final File srcFile = new File(project.getLocationURI().getPath(), "src/main/config/weblogic/oracle-jdbc.xml");
                    final StringBuffer ds = new StringBuffer(project.getName());
                    ds.append("-").append("oracle-jdbc.xml");
                    final File oracleds = new File(jdbcConfig, ds.toString());
                    final boolean exists = oracleds.exists();
                    processOracleDS(project, exists, name, srcFile, ds, oracleds);
                    if (exists) {
                        final String location = new StringBuilder("ear/src/main/resources/jdbc/").append(ds).toString();
                        ContentHandlerTemplate.handle(project, location, new ContentHandlerCallback() {

                            public void processHandle(final Document doc) {
                                updateValues(null, doc);
                            }
                        });
                    }
                }
            }
        }
    }

    private void processOracleDS(final IProject project, final boolean exists, final StringBuilder name, final File srcFile, final StringBuffer ds, final File oracleds) {
        if (!exists) {
            try {
                FileUtils.copyFile(srcFile, oracleds);
            } catch (IOException e) {
                Logger.log(Logger.ERROR, "Could not copy the oracle datasource file to Bea WL", e);
            }
        }
        final IFile wlAppDD = project.getFile(new Path("ear/src/main/resources/META-INF/weblogic-application.xml"));
        if (!wlAppDD.exists()) {
            Logger.getLog().error("File weblogic-application.xml MUST exists at ear/src/main/resources/META-INF");
            throw new IllegalStateException("File weblogic-application.xml MUST exists at ear/src/main/resources/META-INF");
        }
        ContentHandlerTemplate.handle(wlAppDD, new ContentHandlerCallback() {

            @SuppressWarnings("unchecked")
            public void processHandle(Document doc) {
                final Element root = doc.getDocumentElement();
                final Element elemType = XMLUtils.getChildElementByTagName(root, "wls:type");
                final String elemCont = (elemType != null) ? elemType.getTextContent() : "";
                if (elemCont != null && (elemCont.length() > 0 && "JDBC".equals(elemCont))) {
                    Logger.log(Logger.INFO, "[CONF ERROR] you have defined previously a type. You can not define to types at same time");
                    Logger.log(Logger.INFO, "[CONF ERROR] we have decided to solve name :: " + name.toString());
                    Logger.log(Logger.INFO, "[CONF ERROR] we have decided to solve path :: " + ds.toString());
                    Element elemPath = XMLUtils.getChildElementByTagName(root, "wls:path");
                    elemPath.setTextContent(new StringBuilder("jdbc").append("/").append(ds.toString()).toString());
                    Element elemName = XMLUtils.getChildElementByTagName(root, "wls:name");
                    elemName.setTextContent(name.toString());
                    return;
                }
                Element module = doc.createElementNS("wls", "module");
                module.setPrefix("wls");
                Element _name = doc.createElementNS("wls", "name");
                _name.setPrefix("wls");
                _name.setTextContent(name.toString());
                module.appendChild(_name);
                Element type = doc.createElementNS("wls", "type");
                type.setPrefix("wls");
                type.setTextContent("JDBC");
                module.appendChild(type);
                Element path = doc.createElementNS("wls", "path");
                path.setPrefix("wls");
                path.setTextContent(new StringBuilder("jdbc").append("/").append(ds.toString()).toString());
                module.appendChild(path);
                Element preferApppackages = XMLUtils.getChildElementByTagName(root, "wls:prefer-application-packages");
                root.insertBefore(module, preferApppackages);
            }
        });
    }

    /**
	 * @param name
	 * @param doc
	 */
    @SuppressWarnings("unchecked")
    private void updateValues(final StringBuilder name, Document doc) {
        Element root = doc.getDocumentElement();
        Element elem_name = XMLUtils.getChildElementByTagName(root, "name");
        if (name != null) {
            name.append(elem_name.getTextContent());
        }
        Element localDatasource = XMLUtils.getChildElementByTagName(root, "jdbc-driver-params");
        Element connectionUrl = XMLUtils.getChildElementByTagName(localDatasource, "url");
        connectionUrl.setTextContent(WLOracleTabControl.this.getOracleURL());
        List<Element> properties = XMLUtils.getChildElementsByTagName(localDatasource, "property");
        for (Element elem : properties) {
            Element elem_name2 = XMLUtils.getChildElementByTagName(elem, "name");
            Element value = XMLUtils.getChildElementByTagName(elem, "value");
            if (elem_name2.getTextContent().trim().equals("user")) {
                value.setTextContent(WLOracleTabControl.this.oracle_username);
            } else if (elem_name2.getTextContent().trim().equals("password")) {
                value.setTextContent(WLOracleTabControl.this.oracle_password);
            }
        }
    }
}
