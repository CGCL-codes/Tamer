package net.sf.opendf.cli;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import net.sf.opendf.config.AbstractConfig;
import net.sf.opendf.config.ConfigBoolean;
import net.sf.opendf.config.ConfigCLIParseFactory;
import net.sf.opendf.config.ConfigGroup;
import net.sf.opendf.config.ConfigString;
import net.sf.opendf.config.SimulationConfigGroup;
import net.sf.opendf.config.AbstractConfig.ConfigError;
import net.sf.opendf.util.json.DomToJson;
import net.sf.opendf.util.logging.Logging;
import org.json.JSONArray;
import org.w3c.dom.Node;

/**
 * @author mwipliez
 * 
 */
public class Cal2C extends PhasedSimulator {

    public Cal2C(ConfigGroup configuration, String[] args) {
        super(configuration);
        List<String> unparsed = ConfigCLIParseFactory.parseCLI(args, configuration);
        if (((ConfigBoolean) configuration.get(ConfigGroup.VERSION)).getValue().booleanValue()) {
            configuration.usage(Logging.user(), Level.INFO);
            return;
        }
        ConfigString topName = (ConfigString) configuration.get(ConfigGroup.TOP_MODEL_NAME);
        if (!topName.isUserSpecified()) {
            for (String arg : new ArrayList<String>(unparsed)) {
                if (!arg.startsWith("-")) {
                    unparsed.remove(arg);
                    topName.setValue(arg, true);
                }
            }
        }
        boolean valid = unparsed.isEmpty();
        for (AbstractConfig cfg : configuration.getConfigs().values()) {
            if (!cfg.validate()) {
                for (ConfigError err : cfg.getErrors()) {
                    Logging.user().severe(err.getMessage());
                    valid = false;
                }
            }
        }
        if (!valid) {
            Logging.user().info("Unknown args: " + unparsed);
            configuration.usage(Logging.user(), Level.INFO);
            return;
        }
        if (modelPath == null) modelPath = new String[] { "." };
        Logging.user().info("Model Path: " + Arrays.asList(modelPath));
        ClassLoader classLoader = new SimulationClassLoader(Simulator.class.getClassLoader(), modelPath, cachePath);
        try {
            Node doc = Elaborator.elaborateModel(configuration, null, classLoader);
            doc = Elaborator.elabPostProcess(doc, configuration, null, classLoader);
            JSONArray array = new DomToJson().jsonOfNode(doc.getFirstChild());
            String toto = array.toString();
            File file = File.createTempFile("cal2c_", ".json");
            System.out.println(file.getAbsolutePath());
            OutputStream os = new FileOutputStream(file);
            os.write(toto.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConfigGroup configuration = new SimulationConfigGroup();
        new Cal2C(configuration, args);
    }
}
