package com.entelience.esis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import com.entelience.export.jasper.ReportHelper;
import com.entelience.export.jasper.XMLReportConfigurator;
import com.entelience.objects.module.ModuleReport;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;
import com.entelience.util.Arrays;
import com.entelience.util.Logs;

/**
 * class that manages jasper reports command lines
 *
 */
public class JasperReportsManager {

    private JasperReportsManager() {
    }

    private static org.apache.log4j.Logger _logger = Logs.getLogger();

    /**
     * Give a warning
     */
    private static boolean sayWarning() {
        System.out.println("\t***** WARNING ******\t");
        System.out.println("YOU ARE ABOUT TO DELETE OR UPDATE A REPORT");
        System.out.print("\nPLEASE CONFIRM THAT YOU WANT TO PROCEED : y/n >");
        boolean go = false;
        try {
            char ch = (char) System.in.read();
            if (ch == 'y' || ch == 'Y') {
                go = true;
            }
        } catch (IOException e) {
            _logger.debug("Exception ignored " + e.toString());
        }
        return go;
    }

    private static String getInputString() throws Exception {
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    public static void main(Db db, String argsIn[]) throws Exception {
        if (argsIn.length == 0) {
            usage();
            return;
        }
        String command = Arrays.head(argsIn);
        String args[] = Arrays.shift(argsIn);
        if ("add".equals(command)) {
            db.begin();
            if (args.length < 2) {
                usage();
                return;
            }
            addReport(db, args[0], args[1]);
            db.commit();
        } else if ("update".equals(command)) {
            db.begin();
            if (args.length < 1) {
                usage();
                return;
            }
            updateReport(db, args[0]);
            db.commit();
        } else if ("delete".equals(command)) {
            db.begin();
            if (args.length < 1) {
                usage();
                return;
            }
            deleteReport(db, args[0]);
            db.commit();
        } else if ("list".equals(command)) {
            System.out.println("Jasper Reports");
            listReports(db);
        } else if ("list_all".equals(command)) {
            System.out.println("Jasper Reports");
            listAllReports(db);
        } else if ("xml".equals(command)) {
            db.begin();
            if (args.length < 1) {
                usage();
                return;
            }
            if (args.length == 1) {
                importXml(db, args[0], null);
            } else {
                importXml(db, args[0], args[1]);
            }
            listReports(db);
            db.commit();
        } else {
            usage();
            return;
        }
    }

    public static void importXml(Db db, String xmlFile, String directory) throws Exception {
        try {
            db.enter();
            File directoryLocation;
            if (directory == null) directoryLocation = (new File(xmlFile)).getParentFile(); else directoryLocation = new File(directory);
            System.out.println("Configuring jasper Reports from [" + xmlFile + "]. Jasper files will be seek in " + directoryLocation);
            XMLReportConfigurator parser = XMLReportConfigurator.newParser();
            parser.parse(xmlFile, directoryLocation, db);
        } finally {
            db.exit();
        }
    }

    protected static void deleteReport(Db db, String name) throws Exception {
        try {
            db.enter();
            if (!ReportHelper.reportNameExists(db, name)) {
                throw new Exception("The report name [" + name + "] does not exists");
            }
            if (sayWarning()) {
                ReportHelper.deleteReport(db, name);
                String message = "Report [" + name + "] successfully deleted";
                _logger.warn(message);
                System.out.println(message);
            } else {
                System.out.println("\nAborted.");
            }
        } finally {
            db.exit();
        }
    }

    protected static void addReport(Db db, String jasperFile, String name) throws Exception {
        try {
            db.enter();
            if (ReportHelper.reportNameExists(db, name)) {
                throw new Exception("The report name [" + name + "] already exists");
            }
            System.out.print("Enter the module class name (leave blank if the report is not related to the module) >");
            String module = DbHelper.nullify(getInputString());
            System.out.print("Enter the report class name (leave blank if it is a subreport) >");
            String reportClass = DbHelper.nullify(getInputString());
            System.out.print("Enter the description (optionnal) >");
            String desc = DbHelper.nullify(getInputString());
            ModuleReport rep = new ModuleReport();
            rep.setName(name);
            rep.setModuleClassName(module);
            rep.setClassName(reportClass);
            rep.setDescription(desc);
            ReportHelper.addReport(db, rep, new File(jasperFile));
            String message = "Report [" + name + "] successfully added";
            _logger.warn(message);
            System.out.println(message);
        } finally {
            db.exit();
        }
    }

    protected static void updateReport(Db db, String name) throws Exception {
        try {
            db.enter();
            if (!ReportHelper.reportNameExists(db, name)) {
                throw new Exception("The report name [" + name + "] does not exists");
            }
            ModuleReport old = ReportHelper.getReport(db, name);
            System.out.println("Enter the module class name (set blank if the report is not related to the module)");
            System.out.println(" old value [" + old.getModuleClassName() + "]");
            System.out.print(" > ");
            String module = DbHelper.nullify(getInputString());
            System.out.println("Enter the report class name (set blank if it is a subreport)");
            System.out.println(" old value [" + old.getClassName() + "]");
            System.out.print(" > ");
            String reportClass = DbHelper.nullify(getInputString());
            System.out.println("Enter the description (optionnal)");
            System.out.println(" old value [" + old.getDescription() + "]");
            System.out.print(" > ");
            String desc = DbHelper.nullify(getInputString());
            System.out.println("Enter the new jasper file location (leave blank to keep the old jasper report)");
            System.out.print(" > ");
            String jasperFile = DbHelper.nullify(getInputString());
            File jasper = null;
            if (jasperFile != null) jasper = new File(jasperFile);
            ModuleReport rep = new ModuleReport();
            rep.setName(name);
            rep.setModuleClassName(module);
            rep.setClassName(reportClass);
            rep.setDescription(desc);
            if (sayWarning()) {
                ReportHelper.updateReport(db, rep, jasper);
                String message = "Report [" + name + "] successfully updated";
                _logger.warn(message);
                System.out.println(message);
            } else {
                System.out.println("\nAborted.");
            }
        } finally {
            db.exit();
        }
    }

    protected static void listReports(Db db) throws Exception {
        try {
            db.enter();
            printReportsList(ReportHelper.listReports(db));
        } finally {
            db.exit();
        }
    }

    protected static void listAllReports(Db db) throws Exception {
        try {
            db.enter();
            printReportsList(ReportHelper.listAllReports(db));
        } finally {
            db.exit();
        }
    }

    protected static void printReportsList(List<ModuleReport> reports) throws Exception {
        List<String[]> output = new ArrayList<String[]>();
        String title[] = { "Name", "Description", "Class Name", "Module" };
        String spacer[] = { "-", "-", "-", "-" };
        int maxlength[] = { 10, 20, 20, 20 };
        Iterator<ModuleReport> it = reports.iterator();
        while (it.hasNext()) {
            ModuleReport rep = it.next();
            String out[] = new String[title.length];
            out[0] = rep.getName();
            out[1] = rep.getDescription();
            out[2] = rep.getClassName();
            out[3] = rep.getModuleClassName();
            for (int j = 0; j < title.length; ++j) {
                if (out[j] != null && out[j].length() > maxlength[j]) maxlength[j] = out[j].length();
            }
            output.add(out);
        }
        System.out.println(Arrays.joinFixedWidth(title, maxlength, "   ", null));
        System.out.println(Arrays.joinFixedWidth(spacer, maxlength, "-+-", "-"));
        for (int i = 0; i < output.size(); ++i) {
            System.out.println(Arrays.joinFixedWidth(output.get(i), maxlength, " | ", null));
        }
        System.out.println(Arrays.joinFixedWidth(spacer, maxlength, "-+-", "-"));
    }

    public static void usage() {
        System.out.println("Usage");
        System.out.println("");
        System.out.println("add [jasper_file report_name] - add a Jasper file as a report");
        System.out.println("update [report_name] - update an existing report");
        System.out.println("delete [report_name] - delete an existing report");
        System.out.println("");
        System.out.println("list - list all reports");
        System.out.println("list_all - list all reports, including subreports");
        System.out.println("");
        System.out.println("xml [description_file] - add or update reports");
        System.out.println("xml ... [directory] - add or update reports, specifying the location of jasper files");
    }
}
