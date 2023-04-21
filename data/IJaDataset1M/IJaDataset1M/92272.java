package eu.keep.controller.emulatorConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Template builder interface
 * Templates offer a flexible and generic way of creating custom configurations
 * for emulators, without having to write any language-specific code.
 * This interface defines the methods that the different classes of template builders
 * (simple for basic configuration such as 'autorun', 'floppy disks'; complex for 
 * 'memory size', 'cpu bits', etc.) should implement to be able to generate a configuration
 * for an emulator.
 * Each emulator should have at least a CLI (command line interface) template file, and in addition may have an XML or
 * properties template file. A combination of these will generate the command line
 * argument (and parameters) as well as (optionally) a XML or properties configuration
 * file that completely describes the emulator's configuration 
 * 
 * @author Bram Lohman
 *
 */
public interface TemplateBuilder {

    /**
     * Load a specific template to be used for configuration generation. The template
     * has to adhere to the template interface defined by the TemplateBuilder class.
     * @param templateName Name of the template file
     * @throws IOException If a FreeTemplate error occurs
     */
    public void loadTemplate(String templateName) throws IOException;

    /**
     * Retrieve the template group names, along with their variables and default arguments. This can be
     * used as a basis to define the configuration settings passed to {@code generateConfig}
     * method 
     * @return Map of template group names (e.g. 'floppyDisk'), with each template group containing a Map of 
     * variable/value pairs (e.g. inserted=false)
     * @throws IOException If a FreeTemplate error occurs
     */
    public Map<String, Map<String, String>> getTemplateArgs() throws IOException;

    /**
     * Generate the configuration defined in the template loaded in {@code loadTemplate}
     * @param params Map of template group names (e.g. 'floppyDisk'), with each template group containing a List of Maps of 
     * variable/value pairs (e.g. inserted=false). The use of a List of Maps per template group allows multiple 
     * definitions of components, e.g. a if the group 'floppyDisk' has a list containing multiple Maps each defining a 
     * floppy disk (e.g. num=A, inserted=true, digObj=file.ext), the template will generate output for the 'floppyDisk' group
     * multiple times
     * @return A list of three items containing the configuration parts: list item 0 contains the output for the method 'preamble'
     * (if it exists, otherwise it will be empty); list item 1 contains all methods except preamble and postscript; list item 2
     * contains the output for the method 'postscript' (if it exists, otherwise it will be empty); 
     * @throws IOException
     */
    public List<String> generateConfig(Map<String, List<Map<String, String>>> params) throws IOException;
}
