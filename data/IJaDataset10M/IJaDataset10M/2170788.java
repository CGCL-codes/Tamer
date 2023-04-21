package configurator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

public class Configurator {

    private String fileConf;

    public Configurator(String fileConf) {
        this.fileConf = fileConf;
    }

    protected ArrayList<Entry<Object, Object>> configuration = new ArrayList<Entry<Object, Object>>();

    protected void init() {
        Properties props = new Properties();
        try {
            props.load(new FileReader(fileConf));
            Set<Entry<Object, Object>> keys = props.entrySet();
            Iterator<Entry<Object, Object>> it = keys.iterator();
            while (it.hasNext()) {
                Entry<Object, Object> entr = it.next();
                configuration.add(entr);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Entry<Object, Object>> getPathList() {
        return configuration;
    }
}
