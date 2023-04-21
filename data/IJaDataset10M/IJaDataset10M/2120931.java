package com.gregor.opennms.graphconverter;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationContext;
import org.opennms.netmgt.dao.support.PropertiesGraphDao;
import org.opennms.netmgt.model.PrefabGraph;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import com.gregor.jrobin.xml.Def;
import com.gregor.jrobin.xml.RrdGraphDef;
import com.gregor.rrd.graph.parser.Tokenizer;
import com.gregor.rrd.graph.parser.XmlCommandJRobinDefConverter;
import com.gregor.rrd.graph.parser.xml.CastorXmlConverter;
import com.gregor.util.TextUtils;

public class ConvertOnmsGraphPropertiesToXml {

    public static void main(String[] argv) throws Exception {
        if (!Logger.getRootLogger().getAllAppenders().hasMoreElements()) {
            BasicConfigurator.configure();
            Logger.getLogger(ValidationContext.class).setLevel(Level.INFO);
            Logger.getLogger(Marshaller.class).setLevel(Level.INFO);
        }
        if (argv.length != 1) {
            log().fatal("Incorrect number of command line arguments.  Expected 1 got " + argv.length);
            log().fatal("Usage: java -jar <JAR file> <path to snmp-graph.properties>");
            System.exit(1);
        }
        String propertiesFilename = argv[0];
        PropertiesGraphDao dao = new PropertiesGraphDao();
        dao.setAdhocConfigs(new HashMap<String, Resource>(0));
        Resource fileSystemResource = new FileSystemResource(propertiesFilename);
        dao.setPrefabConfigs(Collections.singletonMap("snmp", fileSystemResource));
        dao.afterPropertiesSet();
        CastorXmlConverter converter = new CastorXmlConverter();
        converter.init();
        for (PrefabGraph graph : dao.getAllPrefabGraphs()) {
            log().info("Processing graph for " + graph.getName());
            String[] commandArray = Tokenizer.tokenize(graph.getCommand().trim(), " \t", true);
            RrdGraphDef def = converter.convertToXml(commandArray, new File(""));
            String[] backAgain = converter.convertToCommandArray(def);
            log().debug("\"" + TextUtils.join("\", \"", backAgain) + "\"");
            for (Def d : def.getDatasources().getDef()) {
                d.setRrd(null);
            }
            new File("target/output").mkdir();
            FileWriter w = new FileWriter("target/output/" + graph.getName() + ".xml");
            try {
                def.marshal(w);
            } finally {
                w.close();
            }
            XmlCommandJRobinDefConverter.createRrdGraphDef(def);
        }
    }

    private static Logger log() {
        return Logger.getLogger(ConvertOnmsGraphPropertiesToXml.class);
    }
}
