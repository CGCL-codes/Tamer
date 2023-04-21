package net.sourceforge.unitth.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UnitTHProperties {

    private boolean execTimeGraphs = true;

    private String htmlReportPath = ".";

    private String reportDir = "report.th";

    private String xmlReportFilter = "TEST-";

    private String useAbsolutePaths = "false";

    public void setExecTimeGraphs(boolean execTimeGraphs) {
        this.execTimeGraphs = execTimeGraphs;
    }

    public void setHtmlReportPath(String htmlReportPath) {
        this.htmlReportPath = htmlReportPath;
    }

    public void setReportDir(String reportDir) {
        this.reportDir = reportDir;
    }

    public void setXmlReportFilter(String xmlReportFilter) {
        this.xmlReportFilter = xmlReportFilter;
    }

    public void setUseAbsolutePaths(String abs) {
        this.useAbsolutePaths = abs;
    }

    /**
	 * Writes to given folder in param loc. File output will be unitth.prioperties.
	 * @param loc Location to write the properties to.
	 * @return worked?
	 */
    public boolean writeFileToLocation(String loc) {
        String location = System.getProperty(loc);
        if (location == null) {
            location = loc;
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(location + "/unitth.properties"));
            out.write("#");
            out.newLine();
            out.write("# Properties file created by UnitTH test framework.");
            out.newLine();
            out.write("#");
            out.newLine();
            out.write("unitth.html.report.path=" + this.htmlReportPath);
            out.newLine();
            out.write("unitth.report.dir=" + this.reportDir);
            out.newLine();
            out.write("unitth.xml.report.filter=" + this.xmlReportFilter);
            out.newLine();
            out.write("unitth.generate.exectimegraphs=" + this.execTimeGraphs);
            out.newLine();
            out.write("unitth.use.absolute.paths=" + this.useAbsolutePaths);
            out.newLine();
            out.close();
            return true;
        } catch (IOException e) {
            System.out.println("Exception ");
            return false;
        }
    }

    public void renameFileInWorkingDir(String newName) {
        String location = System.getProperty("user.dir");
        renamePropertiesFile(location, newName);
    }

    public void renameFileInHomeDir(String newName) {
        String location = System.getProperty("user.home");
        renamePropertiesFile(location, newName);
    }

    public void renamePropertiesFile(String location, String newName) {
        rename(location, "unitth.properties", newName);
    }

    public void restorePropsInWorkingDir(String oldName) {
        String location = System.getProperty("user.dir");
        rename(location, oldName, "unitth.properties");
    }

    public void restorePropsInHomeDir(String oldName) {
        String location = System.getProperty("user.home");
        rename(location, oldName, "unitth.properties");
    }

    private void rename(String location, String fromName, String toName) {
        File f1 = new File(location, fromName);
        File f2 = new File(location, toName);
        if (f1.exists()) {
            if (f2.exists()) {
                f2.delete();
            }
            f1.renameTo(f2);
        }
    }

    public void restorePropertiesFile(String location, String currentName) {
        rename(location, currentName, "unitth.properties");
    }

    public void restorePropsFileWorkingDir(String currentName) {
        String location = System.getProperty("user.dir");
        restorePropertiesFile(location, currentName);
    }

    public void restorePropsFileHomeDir(String currentName) {
        String location = System.getProperty("user.home");
        restorePropertiesFile(location, currentName);
    }
}
