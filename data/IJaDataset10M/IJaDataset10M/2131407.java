package pierre.installation;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import pierre.system.PierreResources;

public class WindowsScriptCreator {

    private int serviceType;

    private boolean includeJRE;

    private File serviceConfigurationFile;

    private String propertiesFileName;

    private URL[] dependencyLibraries;

    private String classPath;

    private Integer jvmMegaBytes;

    public WindowsScriptCreator(boolean includeJRE, Integer jvmMegaBytes, File serviceConfigurationFile, File propertiesFile, URL[] dependencyLibraries) {
        this.includeJRE = includeJRE;
        this.jvmMegaBytes = jvmMegaBytes;
        this.serviceConfigurationFile = serviceConfigurationFile;
        String propertiesFilePath = propertiesFile.getAbsolutePath();
        this.propertiesFileName = propertiesFile.getName();
        this.dependencyLibraries = dependencyLibraries;
        determineClassPath();
    }

    private void determineClassPath() {
        StringBuffer buffer = new StringBuffer();
        String[] libraries = new String[dependencyLibraries.length];
        for (int i = 0; i < dependencyLibraries.length; i++) {
            String currentPath = dependencyLibraries[i].getFile();
            int index = currentPath.lastIndexOf("/");
            if (index != -1) {
                String currentFile = currentPath.substring(index + 1);
                currentFile = createLibraryFile(currentFile);
                libraries[i] = currentFile;
            }
        }
        Arrays.sort(libraries);
        for (int i = 0; i < libraries.length; i++) {
            if (libraries[i] != null) {
                if (i != 0) {
                    buffer.append(";");
                    buffer.append(libraries[i]);
                } else {
                    buffer.append(libraries[i]);
                }
            }
        }
        classPath = buffer.toString();
    }

    private String createLibraryFile(String currentFile) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(".");
        buffer.append(File.separator);
        buffer.append("lib");
        buffer.append(File.separator);
        buffer.append(currentFile);
        return buffer.toString();
    }

    public void createScript(File scriptFile, ServiceDeploymentType serviceType) throws Exception {
        FileOutputStream outputStream = new FileOutputStream(scriptFile);
        OutputStreamWriter out = new OutputStreamWriter(outputStream);
        if (serviceType == ServiceDeploymentType.COMMAND_LINE_SERVICE) {
            String commandLineServiceMessage = PierreResources.getMessage("pierre.installation.commandLineServiceNote");
            out.write(commandLineServiceMessage);
            out.write("\n\n");
        }
        if (includeJRE == true) {
            out.write(".");
            out.write(File.separator);
            out.write("jre");
            out.write(File.separator);
            out.write("bin");
            out.write(File.separator);
        }
        out.write("java");
        out.write(" -cp ");
        out.write(classPath);
        out.write(" ");
        if (serviceType == ServiceDeploymentType.COMMAND_LINE_SERVICE) {
            out.write(" pierre.commandLineService.CommandLineService");
        } else if (serviceType == ServiceDeploymentType.TEXT_MENU_APPLICATION) {
            out.write(" pierre.textui.MenuApp");
        } else if (serviceType == ServiceDeploymentType.STANDALONE_APPLICATION) {
            out.write(" pierre.application.PierreDialog");
        }
        out.write(" ");
        out.write("-propertiesFile ");
        out.write(propertiesFileName);
        out.flush();
        out.close();
    }
}
