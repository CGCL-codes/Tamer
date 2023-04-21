package cunei.lm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import cunei.config.Configuration;
import cunei.config.StringParameter;
import cunei.config.SystemConfig;
import cunei.util.Log;

public class LanguageModelSerializer {

    private static final Configuration CFG_LM = SystemConfig.getInstance("LanguageModel");

    private static final String FILE = "lmodel";

    private Configuration config;

    public LanguageModelSerializer(String name) {
        config = CFG_LM.getPath(name);
    }

    public Configuration getConfig() {
        return config;
    }

    private String getDirectory() {
        return StringParameter.get(config, "Directory", null).getValue();
    }

    public LanguageModel read() {
        LanguageModel result = null;
        String path = getDirectory();
        File file = new File(path, FILE);
        Log.getInstance().info("Loading language model (" + file + ")");
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            result = (LanguageModel) in.readObject();
            in.close();
        } catch (Exception e) {
            Log.getInstance().severe("Failed loading language model (" + file + "): " + e.getMessage());
        }
        if (result != null) {
            result.init(config);
            result.load(path);
        }
        return result;
    }

    public void write(LanguageModel model) {
        String path = getDirectory();
        File file = new File(path);
        if (!file.exists()) file.mkdirs();
        file = new File(file, FILE);
        Log.getInstance().info("Writing language model to disk (" + file + ")");
        model.save(path, FILE);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(model);
            out.close();
        } catch (IOException e) {
            Log.getInstance().severe("Failed writing language model: " + e.getMessage());
        }
    }
}
