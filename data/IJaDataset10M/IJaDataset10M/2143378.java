package ie.ucd.clops.generation;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import ie.ucd.clops.dsl.generatedinterface.CLODSLOptionsInterface.Builtin;
import ie.ucd.clops.dsl.structs.DSLInformation;

/**
 * A class to generate the documentation.
 * <BON>
 * class_chart DOCUMENT_GENERATION
 * explanation
 *   "Generation of documentation from the descriptions of options"
 * command
 *   "Generate Documentation"
 * end
 * </BON>
 */
public class DocumentGenerator extends AGenerator {

    /** Explanation for the error message: "Document generation". */
    public static final String EXPLANATION = "Document generation";

    public static String keys[] = { "htmldev", "html", "manpage", "usage", "help" };

    private final HashMap<Builtin, String> templateLib = new HashMap<Builtin, String>();

    ;

    /**
   * Creates a document generator from the collected informations.
   * @param info the infos from the DSL
   * @throws Exception in case the initialisation of Velocity fails
   */
    public DocumentGenerator(final DSLInformation info) throws Exception {
        super(info);
        init();
    }

    private void init() {
        templateLib.put(Builtin.htmldev, TEMPLATE_BASE + "htmldev.vm");
        templateLib.put(Builtin.html, TEMPLATE_BASE + "html.vm");
        templateLib.put(Builtin.manpage, TEMPLATE_BASE + "manpage.vm");
        templateLib.put(Builtin.usage, TEMPLATE_BASE + "usage.vm");
        templateLib.put(Builtin.help, TEMPLATE_BASE + "help.vm");
    }

    public void generateDefault(File output) {
        generate(output, templateLib.get(Builtin.html), EXPLANATION);
    }

    public void generateBuiltin(File output, List<Builtin> builtins) {
        for (Builtin builtin : builtins) {
            generate(output, templateLib.get(builtin), EXPLANATION);
        }
    }

    public void generateCustom(File output, List<File> customz) {
        for (File f : customz) {
            generate(output, f.getAbsolutePath(), EXPLANATION);
        }
    }
}
