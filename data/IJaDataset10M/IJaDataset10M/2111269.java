package nz.ac.waikato.mcennis.rat.dataAquisition;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ParserFactory;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**

 * Reads FOAF files in the given directory in one pass.

 * FIXME: Parse web-pages, reviews, and blogs.

 *

 * @author Daniel McEnnis

 * 

 */
public class FileReader extends ModelShell implements DataAquisition {

    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();

    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    private Graph graph = null;

    /** Creates a new instance of MakeRecommendation */
    public FileReader() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("File Reader");
        properties.add(name);
        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("File Reader");
        properties.add(name);
        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("File Crawler");
        properties.add(name);
        name = ParameterFactory.newInstance().create("Directory", File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, File.class);
        name.setRestrictions(syntax);
        properties.add(name);
        name = ParameterFactory.newInstance().create("Anonymizing", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        properties.add(name);
    }

    @Override
    public void start() {
        fireChange(Scheduler.SET_GRAPH_COUNT, 1);
        properties.set("ParserClass", "FOAF");
        Parser[] parser = new Parser[] { ParserFactory.newInstance().create(properties) };
        parser[0].set(graph);
        FileListCrawler crawler = new FileListCrawler();
        crawler.set(parser);
        java.io.File directory = (File) properties.get("Directory").get();
        java.io.File[] files = directory.listFiles();
        fireChange(Scheduler.SET_ALGORITHM_COUNT, files.length);
        for (int i = 0; i < files.length; ++i) {
            try {
                if (i % 100 == 0) {
                    Logger.getLogger(FileReader.class.getName()).log(Level.FINE, i + " of " + files.length);
                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);
                }
                crawler.crawl(files[i].getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        graph = ((Graph) crawler.getParser()[0].get());
        if ((Boolean) properties.get("Anonymizing").get()) {
            graph.anonymize();
        }
    }

    @Override
    public void set(Graph g) {
        graph = g;
    }

    @Override
    public Graph get() {
        return graph;
    }

    @Override
    public void cancel() {
    }

    @Override
    public List<IODescriptor> getInputType() {
        return input;
    }

    @Override
    public List<IODescriptor> getOutputType() {
        return output;
    }

    @Override
    public Properties getParameter() {
        return properties;
    }

    @Override
    public Parameter getParameter(String param) {
        return properties.get(param);
    }

    /**

     * Initializes the FileReader object with up to 4 parameters.  

     * 

     * <br><b>parameter[0]</b>

     * <br><i>Key-name:</i>'name'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Name of this component.

     * <br>

     * <br><b>parameter[1]</b>

     * <br><i>Key-name:</i>'foafDirectory'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Directory where the FOAF xml files are located

     * <br>

     * <br><b>parameter[2]</b>

     * <br><i>Key-name:</i>'pageDirectory'

     * <br><i>Type:</i>java.lang.String

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Directory where the web-page files are located 

     * <br>FIXME: page parsing not currently implemented

     * <br>

     * <br><b>parameter[3]</b>

     * <br><i>Key-name:</i>'anonymizing'

     * <br><i>Type:</i>java.lang.Boolean

     * <br><i>Structural:</i> true

     * <br><i>Description:</i> Should users be anonymized.

     *  

     * 

     * 

     * @param map key-value pairs used to set parameters.

     */
    public void init(Properties map) {
        if (properties.check(map)) {
            properties.merge(map);
            IODescriptor desc = IODescriptorFactory.newInstance().create(Type.ACTOR, (String) properties.get("Name").get(), "User", null, null, "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.LINK, (String) properties.get("Name").get(), "Knows", null, null, "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "foaf:title", "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "foaf:phone", "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "foaf:gender", "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "ya:country", "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "ya:city", "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "foaf:dateOfBirth", "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "foaf:aimChatID", "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "foaf:msnChatID", "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "ya:bio", "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "interest", "");
            output.add(desc);
            desc = IODescriptorFactory.newInstance().create(Type.ACTOR_PROPERTY, (String) properties.get("Name").get(), "User", null, "foaf:phone", "");
            output.add(desc);
        }
    }

    public FileReader prototype() {
        return new FileReader();
    }
}
