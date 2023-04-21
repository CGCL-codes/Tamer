package net.sf.openforge.frontend.slim.app;

import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import net.sf.openforge.app.*;
import net.sf.openforge.frontend.slim.builder.*;
import net.sf.openforge.lim.*;
import net.sf.openforge.lim.naming.*;
import net.sf.openforge.optimize.*;
import net.sf.openforge.schedule.*;
import net.sf.openforge.util.naming.*;
import net.sf.openforge.verilog.translate.*;

/**
 * This class is used for testing/debug only, XLIMCompiler sequences
 * the neccessary steps to realize the compilation of XLIM source to
 * HDL. 
 */
public class SLIMCompiler {

    private SLIMCompiler() {
    }

    private void compile(GenericJob job) {
        final SLIMEngine engine = new SLIMEngine(job);
        final File[] targetFiles = job.getTargetFiles();
        assert targetFiles.length == 1 : "XLIM Compilation supports exactly one target file.  Found: " + targetFiles.length;
        final File input = targetFiles[0];
        Document document = null;
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
            System.out.println("RUN A SCHEMA ON ME!");
            document = domBuilder.parse(input);
        } catch (ParserConfigurationException pce) {
            throw new SLIMBuilderException("error in configuration " + pce);
        } catch (org.xml.sax.SAXException saxe) {
            throw new SLIMBuilderException("sax error " + saxe);
        } catch (IOException ioe) {
            throw new SLIMBuilderException("io error " + ioe);
        }
        Design design = new SLIMBuilder().build(document);
        System.out.println("Optimizing");
        (new Optimizer()).optimize(design);
        System.out.println("Scheduling");
        Scheduler.schedule(design);
        System.out.println("Naming");
        LIMLogicalNamer.setNames(design);
        System.out.println("PassThroughRemover");
        design.accept(new PassThroughComponentRemover());
        System.out.println("RE-naming");
        VerilogNaming naming = new VerilogNaming();
        naming.visit(design);
        System.out.println("Writing");
        try {
            FileOutputStream fos = new FileOutputStream(new File("foo.v"));
            VerilogTranslator.translate(design, fos);
        } catch (IOException ioe) {
            System.out.println("Writing failed " + ioe);
        }
    }

    public static void main(String args[]) {
        GenericJob job = new GenericJob();
        try {
            job.setOptionValues(args);
        } catch (NewJob.ForgeOptionException foe) {
            System.err.println("Command line option error: " + foe.getMessage());
            System.err.println("");
            System.err.println(OptionRegistry.usage(false));
            System.exit(-1);
        }
        SLIMCompiler compiler = new SLIMCompiler();
        compiler.compile(job);
    }
}
