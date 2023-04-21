package net.sf.openforge.backend.hdl;

import java.io.*;
import net.sf.openforge.app.*;
import net.sf.openforge.backend.*;
import net.sf.openforge.backend.edk.*;
import net.sf.openforge.lim.*;
import net.sf.openforge.verilog.translate.*;

/**
 * VerilogTranslateEngine is the {@link OutputEngine} for translation
 * of the design to Verilog HDL.
 *
 * <p>Created: Fri Mar 17 20:50:55 2006
 *
 * @author imiller, last modified by $Author: imiller $
 * @version $Id: VerilogTranslateEngine.java 424 2007-02-26 22:36:09Z imiller $
 */
public class VerilogTranslateEngine implements OutputEngine {

    private static final String _RCS_ = "$Rev: 103 $";

    /** The Verilog HDL output */
    public static final ForgeFileKey VERILOG = new ForgeFileKey("Verilog HDL");

    /** An additional Verilog HDL output which simply includes the
     * main design and the simulation primitives */
    public static final ForgeFileKey SIMINCL = new ForgeFileKey("Verilog HDL simulation includes");

    /** An additional Verilog HDL output which simply includes the
     * main design and the synthesis primitives */
    public static final ForgeFileKey SYNINCL = new ForgeFileKey("Verilog HDL synthesis includes");

    /**
     * An HDL output file which contains ONLY the synthesis primitives
     * and does NOT contain an inclusion of the generated HDL file.
     */
    public static final ForgeFileKey SYNPRIMINCL = new ForgeFileKey("Verilog HDL synthesis includes");

    public void initEnvironment() {
        ForgeFileHandler fileHandler = EngineThread.getGenericJob().getFileHandler();
        final boolean suppressIncludes = EngineThread.getGenericJob().getUnscopedBooleanOptionValue(OptionRegistry.NO_INCLUDE_FILES);
        if (fileHandler.isRegistered(ForgeCoreDescriptor.EDK_HDL_VER_DIR)) {
            File base = fileHandler.getFile(ForgeCoreDescriptor.EDK_HDL_VER_DIR);
            fileHandler.registerFile(VERILOG, base, fileHandler.buildName("", "v"));
            if (!suppressIncludes) {
                fileHandler.registerFile(SIMINCL, base, fileHandler.buildName("_sim", "v"));
                fileHandler.registerFile(SYNINCL, base, fileHandler.buildName("_synth", "v"));
            }
        } else {
            fileHandler.registerFile(VERILOG, fileHandler.buildName("", "v"));
            if (!suppressIncludes) {
                fileHandler.registerFile(SIMINCL, fileHandler.buildName("_sim", "v"));
                fileHandler.registerFile(SYNINCL, fileHandler.buildName("_synth", "v"));
            }
        }
    }

    /**
     * Causes the synthesis primitive (only) include file to be
     * registered, and consequently, to be generated during the
     * translate phase.  This method is safe to be called multiple
     * times. 
     */
    public static void registerSynPrimIncl() {
        final ForgeFileHandler fileHandler = EngineThread.getGenericJob().getFileHandler();
        if (!fileHandler.isRegistered(SYNPRIMINCL)) {
            fileHandler.registerFile(SYNPRIMINCL, fileHandler.buildName("_synincl", "v"));
        }
    }

    public void translate(Design design) throws IOException {
        GenericJob gj = EngineThread.getGenericJob();
        ForgeFileHandler fileHandler = gj.getFileHandler();
        final File vFile = fileHandler.getFile(VERILOG);
        VerilogNaming naming = new VerilogNaming();
        naming.visit(design);
        gj.info("writing " + vFile.getAbsolutePath());
        gj.inc();
        final boolean suppressAppModule = gj.getUnscopedBooleanOptionValue(OptionRegistry.SUPPRESS_APP_MODULE);
        VerilogTranslator vt = new VerilogTranslator(design, suppressAppModule);
        if (!vFile.getParentFile().exists()) {
            vFile.getParentFile().mkdirs();
        }
        FileOutputStream vFos = new FileOutputStream(vFile);
        vt.writeDocument(vFos);
        vFos.close();
        if (fileHandler.isRegistered(SIMINCL)) {
            FileOutputStream simFos = new FileOutputStream(fileHandler.getFile(SIMINCL));
            vt.outputSimInclude(vFile, simFos);
            simFos.close();
        }
        if (fileHandler.isRegistered(SYNINCL)) {
            FileOutputStream synthFos = new FileOutputStream(fileHandler.getFile(SYNINCL));
            vt.outputSynthInclude(vFile, synthFos);
            synthFos.close();
        }
        if (fileHandler.isRegistered(SYNPRIMINCL)) {
            FileOutputStream synPrimFos = new FileOutputStream(fileHandler.getFile(SYNPRIMINCL));
            vt.outputSynthInclude(null, synPrimFos);
            synPrimFos.close();
        }
        gj.dec();
    }

    /**
     * Returns a string which uniquely identifies this phase of the
     * compiler output.
     *
     * @return a non-empty, non-null String
     */
    public String getOutputPhaseId() {
        return "Verilog HDL";
    }
}
