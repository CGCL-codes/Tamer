package de.srcml.ant;

import de.srcml.analyzer.Analyzer;
import de.srcml.analyzer.AnalyzerResult;
import de.srcml.config.Plugin;
import de.srcml.config.PluginConfig;
import de.srcml.config.PluginException;
import de.srcml.config.PluginManager;
import de.srcml.dom.SrcML;
import de.srcml.parser.Parser;
import de.srcml.util.OutputSrcML;
import de.srcml.util.OutputString;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * This class implements the Analyzer Ant task, which makes the AnalyzerPlatform
 * accessible through Ant.
 * 
 * Example build.xml:<br/>
 * [...]<br/>
 * &lt;taskdef name="analyzer" classname="de.srcml.ant.AntAnalyzer"/&gt; <br/>
 * &lt;target name="analyze" description="analyze"&gt;<br/>
 * &lt;analyzer&gt;<br/>
 * &lt;fileset dir="." casesensitive="yes"&gt;<br/>
 * &lt;include name="sternstern/stern.java"/&gt;<br/>
 * &lt;/fileset&gt;<br/>
 * &lt;plugin name="noprint" version="0.2.0"&gt;<br/>
 * &lt;config param="verbose" value="true"/&gt;<br/>
 * &lt;config param="systemout" value="true"/&gt;<br/>
 * &lt;/plugin&gt;<br/>
 * &lt;/analyzer&gt;<br/>
 * &lt;/target&gt;<br/>
 *
 * @author Leif Bladt
 * @version $Revision: 1.12 $
 */
public class AntAnalyzer extends Task {

    private List<FileSet> m_filesets = new Vector<FileSet>();

    private Analyzer analyzer;

    private List<AnalyzerResult> results;

    private List<AntPluginElement> m_plugins = new Vector<AntPluginElement>();

    private int m_priority;

    private int m_counter = 0;

    private static java.util.logging.Logger logger = de.srcml.Logger.getLogger();

    /**
     * The create method for the nested element PluginElement (<plugin/>)
     */
    public AntPluginElement createPlugin() {
        AntPluginElement plg = new AntPluginElement();
        m_plugins.add(plg);
        return plg;
    }

    /**
     * Setter-method for the priority attribute
     *
     * @param f_priority the ant task outputs only results with
     * priorities above this value
     */
    public void setPriority(int f_priority) {
        m_priority = f_priority;
    }

    /**
     * Setter-method for the fileset
     *
     * @param f_fileset the fileset
     */
    public void addFileset(FileSet f_fileset) {
        m_filesets.add(f_fileset);
    }

    /**
     * This method implements the task itself.
     */
    public void execute() throws BuildException {
        analyzer = new Analyzer();
        log("parsing files...");
        addFiles();
        if (m_plugins.size() == 0) {
            getAvailablePlugins();
        }
        try {
            for (AntPluginElement pa : m_plugins) {
                Plugin plugin = analyzer.setAnalyzerPlugin(pa.getName(), pa.getVersion());
                if (plugin != null) {
                    for (AntConfigElement ce : pa.getConfigElements()) {
                        if (ce.getParam() != "" && ce.getValue() != "") {
                            plugin.setParameter(ce.getParam(), ce.getValue());
                        }
                    }
                    results = analyzer.execute();
                    for (AnalyzerResult obj : results) {
                        if (obj.getPriority() >= m_priority) {
                            log(obj.toString());
                            m_counter++;
                        }
                    }
                } else {
                    logger.severe("Could not load plugin: " + pa.getName() + " (" + pa.getVersion() + ")");
                }
            }
        } catch (PluginException e) {
            e.printStackTrace();
        }
        if (m_counter > 0) {
            log("Warnings: " + m_counter);
        } else {
            log("Done. No warnings found!");
        }
    }

    private SrcML parse(File f_file) {
        Parser parser = new Parser();
        SrcML srcml = null;
        try {
            parser.setOutputPlugin("SrcML", null);
            parser.setInputPlugin("file", null);
            parser.setParserPlugin("java", null);
            parser.getInputPlugin().setParameter("filename", f_file.toString());
            parser.setFilename(f_file.toString());
            parser.parse();
            srcml = ((OutputSrcML) parser.getOutputPlugin()).getSrcML();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return srcml;
    }

    private void addFiles() {
        for (FileSet fs : m_filesets) {
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            String[] includedFiles = ds.getIncludedFiles();
            for (int i = 0; i < includedFiles.length; i++) {
                String filename = includedFiles[i].replace('\\', '/');
                filename = filename.substring(filename.lastIndexOf("/") + 1);
                File base = ds.getBasedir();
                File found = new File(base, includedFiles[i]);
                analyzer.addDocument(parse(found));
            }
        }
    }

    private void getAvailablePlugins() {
        ArrayList<PluginConfig> plugins = PluginManager.getPluginList(PluginManager.TYPE_ANALYZER);
        for (PluginConfig plugin : plugins) {
            AntPluginElement pa = new AntPluginElement();
            pa.setName(plugin.getName());
            pa.setVersion(plugin.getVersion());
            m_plugins.add(pa);
        }
    }
}
