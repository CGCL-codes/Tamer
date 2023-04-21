package com.tresys.slide.plugin.builder;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.QualifiedName;
import com.tresys.slide.plugin.SLIDEPlugin;
import com.tresys.slide.utility.ErrorStringHandler;
import com.tresys.slide.utility.ExecStringGobbler;
import com.tresys.slide.utility.policyxmlparser.PolicyXMLParser;

public class SLIDEProjectNature implements IProjectNature {

    /**
	 * This is the unique ID for this nature
	 */
    public static final String NATURE_ID = SLIDEPlugin.PLUGIN_ID + ".SLIDEProject";

    /**
	 * Project to add the nature to
	 */
    private IProject project;

    public static final QualifiedName BASE_POLICY = new QualifiedName("basepolicy", "basepolicy");

    private Map interfaceMap = null;

    private Map templateMap = null;

    private Map layerMap = null;

    private Map moduleMap = null;

    public void configure() {
    }

    public void deconfigure() {
    }

    public IProject getProject() {
        return project;
    }

    public void setProject(IProject project) {
        this.project = project;
        parsePolicyXML();
    }

    public Map getInterfaceMap() {
        return interfaceMap;
    }

    public Map getModuleMap() {
        return moduleMap;
    }

    public Map getLayerMap() {
        return layerMap;
    }

    public Map getTemplateMap() {
        return templateMap;
    }

    public File GetBuildConfPath() {
        File buildConf = null;
        try {
            if (project.hasNature(PolicyModuleProjectNature.NATURE_ID)) {
                PolicyModuleProjectNature nat = (PolicyModuleProjectNature) project.getNature(PolicyModuleProjectNature.NATURE_ID);
                buildConf = nat.GetBuildConfPath();
            } else if (project.hasNature(PolicyProjectNature.NATURE_ID)) {
                PolicyProjectNature nat = (PolicyProjectNature) project.getNature(PolicyProjectNature.NATURE_ID);
                buildConf = nat.GetBuildConfPath();
            } else if (project.hasNature(HeaderModuleProjectNature.NATURE_ID)) {
                HeaderModuleProjectNature nat = (HeaderModuleProjectNature) project.getNature(HeaderModuleProjectNature.NATURE_ID);
                buildConf = nat.GetBuildConfPath();
            }
        } catch (CoreException ce) {
        }
        return buildConf;
    }

    public IResource getModulesPath() {
        IResource modulesPath = null;
        try {
            String sPath = null;
            if (project.hasNature(PolicyModuleProjectNature.NATURE_ID)) {
                sPath = "policy" + IPath.SEPARATOR + "modules";
            } else if (project.hasNature(PolicyProjectNature.NATURE_ID)) {
                sPath = "policy" + IPath.SEPARATOR + "modules";
            } else if (project.hasNature(HeaderModuleProjectNature.NATURE_ID)) {
                sPath = ".";
            }
            IFolder modDir = project.getFolder(sPath);
            IPath modPath = modDir.getProjectRelativePath();
            int nSegment = modPath.segmentCount() - 1;
            while (nSegment >= 0 && !modDir.exists()) {
                IFolder dir = project.getFolder(modPath.removeLastSegments(nSegment));
                if (!dir.exists()) dir.create(false, true, null);
                nSegment--;
            }
            modulesPath = modDir;
        } catch (CoreException ce) {
        }
        return (modulesPath);
    }

    protected void parsePolicyXML() {
        File policyXMLFile = null;
        try {
            if (project.hasNature(PolicyModuleProjectNature.NATURE_ID)) {
                policyXMLFile = ((PolicyModuleProjectNature) project.getNature(PolicyModuleProjectNature.NATURE_ID)).GetPolicyXMLPath();
            } else if (project.hasNature(PolicyProjectNature.NATURE_ID)) {
                policyXMLFile = ((PolicyProjectNature) project.getNature(PolicyProjectNature.NATURE_ID)).GetPolicyXMLPath();
            } else if (project.hasNature(HeaderModuleProjectNature.NATURE_ID)) {
                policyXMLFile = ((HeaderModuleProjectNature) project.getNature(HeaderModuleProjectNature.NATURE_ID)).GetPolicyXMLPath();
            }
        } catch (CoreException ce) {
        }
        if (policyXMLFile != null && !policyXMLFile.exists()) {
            File directory = null;
            try {
                if (project.getDescription().getLocation() == null) directory = new File(project.getWorkspace().getRoot().getLocation() + project.getFullPath().toOSString() + "/"); else directory = new File(project.getDescription().getLocation().toOSString() + "/");
            } catch (CoreException ce) {
            }
            if (directory != null && directory.exists()) {
                try {
                    Process process = Runtime.getRuntime().exec("make xml", new String[] {}, directory);
                    ErrorStringHandler errorGobbler = new ErrorStringHandler(process.getErrorStream(), new ErrorHandler(getProject()));
                    errorGobbler.start();
                    ExecStringGobbler outputGobbler = new ExecStringGobbler(process.getInputStream());
                    outputGobbler.start();
                    process.waitFor();
                } catch (IOException ioe) {
                } catch (InterruptedException ie) {
                }
            }
        }
        if (policyXMLFile != null && policyXMLFile.exists()) {
            PolicyXMLParser policyXMLParser = new PolicyXMLParser(policyXMLFile, getProject());
            this.moduleMap = policyXMLParser.getModules();
            this.interfaceMap = policyXMLParser.getInterfaces();
            this.templateMap = policyXMLParser.getTemplates();
            this.layerMap = policyXMLParser.getLayers();
        }
    }
}
