package com.mindtree.techworks.infix.plugins.nsis.actions;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.StringUtils;
import com.mindtree.techworks.infix.plugins.nsis.model.FinishPageSettings;
import com.mindtree.techworks.infix.plugins.nsis.model.InstallerSettings;
import com.mindtree.techworks.infix.plugins.nsis.model.MuiPages;
import com.mindtree.techworks.infix.plugins.nsis.model.NsisProject;
import com.mindtree.techworks.infix.plugins.nsis.model.ProjectInfo;
import com.mindtree.techworks.infix.plugins.nsis.model.Section;
import com.mindtree.techworks.infix.plugins.nsis.model.ShortCut;
import com.mindtree.techworks.infix.plugins.nsis.model.StartMenuFolderPageSettings;
import com.mindtree.techworks.infix.plugins.nsis.model.WelcomePageSettings;
import com.mindtree.techworks.infix.plugins.nsis.resources.NsisDefaults;

/**
 * Loads the defaults into the parsed Nsis project for use
 * 
 * @author Bindul Bhowmik
 * @version $Revision: 79 $ $Date: 2010-12-05 08:50:09 -0500 (Sun, 05 Dec 2010) $
 */
@Component(role = NsisAction.class, hint = "load-default")
public class LoadDefaultsAction implements NsisAction {

    /**
	 * The Mojo information
	 */
    private MojoInfo mojoInfo;

    /**
	 * The NSIS Defaults
	 */
    @Requirement(role = NsisDefaults.class)
    private NsisDefaults nsisDefaults;

    @Override
    public void execute(MojoInfo mojoInfo) throws NsisActionExecutionException {
        NsisProject nsisProject = mojoInfo.getNsisProject();
        MavenProject mavenProject = mojoInfo.getProject();
        this.mojoInfo = mojoInfo;
        loadProjectInfoDefaults(nsisProject.getProjectInfo(), mavenProject);
        loadInstallerSettingDefaults(nsisProject, mavenProject);
        loadMuiPagesDefaults(nsisProject, mavenProject);
        loadSectionsDefaults(nsisProject, mavenProject);
    }

    /**
	 * Loads defaults into the installer setting section
	 * @param nsisProject The Nsis Project
	 * @param mavenProject The Maven Project
	 */
    private void loadInstallerSettingDefaults(NsisProject nsisProject, MavenProject mavenProject) {
        if (null == nsisProject.getInstallerSettings()) {
            nsisProject.setInstallerSettings(new InstallerSettings());
        }
        InstallerSettings instSettings = nsisProject.getInstallerSettings();
        if (null == instSettings.getOutFile()) {
            instSettings.setOutFile(mavenProject.getBuild().getFinalName() + "-setup.exe");
        }
        instSettings.setUninstallerIcon(resolveMavenVariables(instSettings.getUninstallerIcon(), mavenProject));
    }

    /**
	 * Loads defaults into the project info section
	 * @param projectInfo The project info object
	 * @param mavenProject The maven project
	 */
    private void loadProjectInfoDefaults(ProjectInfo projectInfo, MavenProject mavenProject) {
        if (null == projectInfo.getProductName()) {
            projectInfo.setProductName(StringUtils.capitalizeFirstLetter(mavenProject.getArtifactId()));
        }
        if (null == projectInfo.getProductVersion()) {
            projectInfo.setProductVersion(mavenProject.getVersion());
        }
        if (null == projectInfo.getPublisher()) {
            if (null != mavenProject.getOrganization()) {
                projectInfo.setPublisher(mavenProject.getOrganization().getName());
            } else {
                projectInfo.setPublisher(mavenProject.getGroupId());
            }
        }
        if (null == projectInfo.getWebSite()) {
            projectInfo.setWebSite(mavenProject.getUrl());
        }
    }

    /**
	 * Loads the defaults into the MUI Pages defaults
	 */
    private void loadMuiPagesDefaults(NsisProject nsisProject, MavenProject mavenProject) {
        MuiPages muiPages = nsisProject.getMuiPages();
        if (null == muiPages) {
            muiPages = new MuiPages();
            nsisProject.setMuiPages(muiPages);
        }
        if (null == muiPages.getPageHeaderText()) {
            muiPages.setPageHeaderText(nsisDefaults.getPageHeaderText());
        }
        if (null == muiPages.getPageHeaderSubText()) {
            muiPages.setPageHeaderSubText(nsisDefaults.getPageHeaderSubText());
        }
        if (null != muiPages.getWelcomePageSettings()) {
            WelcomePageSettings welcomePageSettings = muiPages.getWelcomePageSettings();
            if (null == welcomePageSettings.getTitle()) {
                welcomePageSettings.setTitle(nsisDefaults.getWelcomePageTitle());
            }
        }
        if (null != muiPages.getStartMenuFolderPageSettings()) {
            StartMenuFolderPageSettings startMenuSettings = muiPages.getStartMenuFolderPageSettings();
            if (null == startMenuSettings.getDefaultFolder()) {
                startMenuSettings.setDefaultFolder(nsisDefaults.getStartMenuDefaultFolder());
            }
            if (null == startMenuSettings.getRegistryRoot()) {
                startMenuSettings.setRegistryRoot(nsisDefaults.getStartMenuRegistryRoot());
            }
            if (null == startMenuSettings.getRegistryKey()) {
                startMenuSettings.setRegistryKey(nsisDefaults.getStartMenuRegistryKey());
            }
            if (null == startMenuSettings.getRegistryValueName()) {
                startMenuSettings.setRegistryValueName(nsisDefaults.getStartMenuRegistryValue());
            }
        }
        if (null != muiPages.getFinishPageSettings()) {
            FinishPageSettings finishSettings = muiPages.getFinishPageSettings();
            if (null != finishSettings.getRun()) {
                finishSettings.setRun(resolveMavenVariables(finishSettings.getRun(), mavenProject));
            }
        }
    }

    /**
	 * Loads the defaults for the sections
	 */
    private void loadSectionsDefaults(NsisProject nsisProject, MavenProject mavenProject) throws NsisActionExecutionException {
        if (null == nsisProject.getSections()) {
            throw new NsisActionExecutionException("Sections is required");
        }
        if (null == nsisProject.getSections().getSections() || nsisProject.getSections().getSections().isEmpty()) {
            throw new NsisActionExecutionException("At least one section is required");
        }
        for (Section section : nsisProject.getSections().getSections()) {
            if (section.isHidden() && section.isBold()) {
                throw new NsisActionExecutionException("A section cannot be both hidden and bold. Section: " + section.getName());
            }
            if (null != section.getShortcuts()) {
                for (ShortCut shortCut : section.getShortcuts()) {
                    shortCut.setTargetFile(resolveMavenVariables(shortCut.getTargetFile(), mavenProject));
                }
            }
        }
    }

    /**
	 * Resolves all variables starting with <code>${project.</code> with maven
	 * dependencies.
	 * 
	 * @param rawString The string containing the variable
	 * @param mavenProject The maven project used to resolve the dependencies
	 * @return The resolved string.
	 */
    private String resolveMavenVariables(String rawString, MavenProject mavenProject) {
        String returnString = rawString;
        if (rawString.contains("${project.")) {
            int start = rawString.indexOf("${project.");
            int end = rawString.indexOf("}", start);
            if (start != -1 && end != -1) {
                String variable = rawString.substring(start, end + 1);
                String rawVariable = variable;
                mojoInfo.getLog().debug("Trying to resolve :" + variable);
                variable = variable.substring("${project.".length());
                variable = variable.substring(0, variable.length() - 1);
                variable = variable.replaceAll("\\.", "/");
                Pointer variablePointer = JXPathContext.newContext(mavenProject).createPath(variable);
                if (null != variablePointer) {
                    String value = variablePointer.getValue().toString();
                    mojoInfo.getLog().debug("Using replacement value: " + value);
                    returnString = new StringBuilder(returnString).replace(start, end + 1, value).toString();
                    mojoInfo.getLog().debug("Replaced [" + rawVariable + "] in [" + rawString + "] with [" + value + "] to form [" + returnString + "]");
                    returnString = resolveMavenVariables(returnString, mavenProject);
                }
            }
        }
        return returnString;
    }
}
