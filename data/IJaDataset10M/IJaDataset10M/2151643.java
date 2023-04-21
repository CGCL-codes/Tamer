package de.schlund.pfixcore.util.basicapp.basics;

import de.schlund.pfixcore.util.basicapp.helper.AppWorker;
import de.schlund.pfixcore.util.basicapp.objects.Project;

/**
 * Just a main for running the app
 * 
 * @author <a href="mailto:rapude@schlund.de">Ralf Rapude</a>
 * @version $Id: InitNewPfixProject.java 1081 2004-06-04 14:46:13Z rrapude $
 */
public final class InitNewPfixProject {

    public static void main(String[] args) {
        AppWorker.initLogging();
        CreateProjectSettings settings = new CreateProjectSettings();
        settings.runGetSettings();
        CreateProject createPrj = new CreateProject(settings.getCurrentProject());
        createPrj.runCreateProject();
        HandleXMLFiles handleFiles = new HandleXMLFiles(settings.getCurrentProject());
        handleFiles.runHandleXMLFiles();
        System.out.println("\nYour project has been successfully created.");
        System.out.println("To see how it works type in \"ant\".");
        System.out.println("Afterwards restart Apache httpd and Tomcat.");
        System.out.println("Then type in \"http://" + Project.getStaticPrjName() + ".HOSTNAME.DOMAIN\"");
    }
}
