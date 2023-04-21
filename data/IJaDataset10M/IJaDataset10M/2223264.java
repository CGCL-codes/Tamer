package org.ashkelon.manager.cmd;

import com.martiansoftware.jsap.*;
import org.ashkelon.API;
import java.util.LinkedList;
import java.util.Collection;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 10, 2005
 * Time: 6:37:28 PM
 */
public class HtmlCmd extends ProcessAPICmd {

    public String getName() {
        return "html";
    }

    public String getDescription() {
        return "Process and API to produce static html files (somewhat like javadoc)";
    }

    public String getExample() {
        return "ashkelon html -d javadocs apis/junit.xml";
    }

    public String getNote() {
        return "This html command is a new feature in ashkelon.  It processes " + "ashkelon's JSPs directly to produce static html pages.  The look " + "of the pages can be easily customized by modifying the JSP templates";
    }

    public String docletClassName() {
        return "org.ashkelon.manager.staticdoclet.TemplateDoclet";
    }

    public void registerParameters() throws JSAPException {
        super.registerParameters();
        FlaggedOption outputdirOption = new FlaggedOption("outputdir").setRequired(false).setShortFlag('d').setLongFlag("outputdir");
        outputdirOption.setHelp("Output directory:  base directory where produced " + " static html pages will be written to.");
        registerParameter(outputdirOption);
        Option apiOption = new UnflaggedOption("api").setRequired(true);
        apiOption.setHelp("API to populate, specified as an api.xml file or " + "as a maven project.xml file (does not work with all maven projects)");
        registerParameter(apiOption);
    }

    public void invoke(JSAPResult arguments) {
        super.invoke(arguments);
        String apifilename = arguments.getString("api");
        if (!new File(apifilename).exists()) {
            log.error("No such file: " + apifilename);
            return;
        }
        log.debug("api file name: " + apifilename);
        try {
            String sourcepath = arguments.getString("sourcepath");
            API api = API.unmarshal(apifilename, sourcepath);
            log.debug("api unmarshalled; name is: " + api.getName());
            fetchSource(api);
            String[] javadocargs = constructJavadocArgslist(arguments, apifilename, api);
            com.sun.tools.javadoc.Main.execute("ashkelon", docletClassName(), javadocargs);
        } catch (FileNotFoundException ex) {
            log.error("File " + apifilename + " not found.  Aborting");
            return;
        } catch (java.text.ParseException ex) {
            log.error("Exception: " + ex.getMessage());
            ex.printStackTrace(log.getWriter());
            return;
        }
    }

    private String[] constructJavadocArgslist(JSAPResult arguments, String apifilename, API api) {
        LinkedList javadocargslist = new LinkedList();
        javadocargslist.addLast("-sourcepath");
        String sourcepath = arguments.getString("sourcepath");
        if (sourcepath != null) {
            javadocargslist.addLast(api.sourcepath() + ":" + sourcepath);
        } else {
            javadocargslist.addLast(api.sourcepath());
        }
        String classpath = arguments.getString("classpath");
        if (classpath != null) {
            javadocargslist.addLast("-classpath");
            javadocargslist.addLast(classpath);
        }
        String source = arguments.getString("source");
        if (source != null) {
            javadocargslist.addLast("-source");
            javadocargslist.addLast(source);
        }
        String encoding = arguments.getString("encoding");
        if (encoding != null) {
            javadocargslist.addLast("-encoding");
            javadocargslist.addLast(encoding);
        }
        String outputdir = arguments.getString("outputdir");
        if (outputdir != null) {
            javadocargslist.addLast("-d");
            javadocargslist.addLast(outputdir);
        }
        javadocargslist.addLast("-api");
        javadocargslist.addLast(apifilename);
        Collection packagenames = api.getPackagenames();
        javadocargslist.addAll(packagenames);
        log.debug("argslist before calling javadoc: " + javadocargslist);
        String[] addlist = new String[javadocargslist.size()];
        return (String[]) javadocargslist.toArray(addlist);
    }
}
