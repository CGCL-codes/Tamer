package configurator.PathConfigurator;

import java.util.ArrayList;
import java.util.Map.Entry;
import configurator.Configurator;

public class PathConfigurator extends Configurator {

    private static String confFilePath = "./config/PathToIndexConfig.properties";

    private ArrayList<PathConfBox> pathConf = new ArrayList<PathConfBox>();

    private ArrayList<String> pathToIndexList;

    private String storeIndexPath;

    public PathConfigurator() {
        super(confFilePath);
    }

    private void convertConfiguration() {
        for (int i = 0; i < configuration.size(); i++) {
            Entry<Object, Object> obj = configuration.get(i);
            PathConfBox objToInsert = new PathConfBox();
            objToInsert.configuration = (String) obj.getKey();
            objToInsert.path = (String) obj.getValue();
            pathConf.add(objToInsert);
        }
    }

    private void setConfigurationParameters() {
        pathToIndexList = new ArrayList<String>();
        for (int i = 0; i < pathConf.size(); i++) {
            if (pathConf.get(i).configuration.equals("IndexPath")) storeIndexPath = pathConf.get(i).path; else pathToIndexList.add(pathConf.get(i).path);
        }
    }

    public void readPathConfiguration() {
        init();
        convertConfiguration();
        setConfigurationParameters();
    }

    public ArrayList<PathConfBox> getPathConf() {
        return pathConf;
    }

    public ArrayList<String> getPathToIndexList() {
        return pathToIndexList;
    }

    public String getStoreIndexPath() {
        return storeIndexPath;
    }
}
