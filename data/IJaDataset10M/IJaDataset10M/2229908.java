package SimpleViz;

import general.ExclusionMutua;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import util.MyJSONReader;

/**
 * <dl>
 * <dt>Purpose: Profiled Users Visualization
 * <dd>
 * 
 * <dt>Description:
 * <dd>Generates a visualization with all existing users in a certain moment of
 * time.
 * 
 */
public class ProfiledUsersVisualization extends AGraphGenerator {

    static Logger logger = Logger.getLogger(ProfiledUsersVisualization.class.getName());

    static {
        PropertyConfigurator.configure("logger.configuration");
    }

    public static void main(String args[]) {
        ProfiledUsersVisualization vis;
        for (int i = 1; i < 50; i++) {
            logger.info("DATA PROFILED USERS VISUALIZATION ITERACION:" + i);
            vis = new ProfiledUsersVisualization();
            vis.fillData("PUV" + String.format("%03d", i), i);
        }
        for (int i = 1; i < 50; i++) {
            logger.info("GRAPHIC PROFILED USERS VISUALIZATION ITERACION:" + i);
            vis = new ProfiledUsersVisualization();
            vis.fillGraph("PUV" + String.format("%03d", i), i);
        }
    }

    public ProfiledUsersVisualization() {
        super();
        GRAPH_GENERATOR = MyJSONReader.getInstance().readConfigStr("DotGeneratorPath");
    }

    public void CreateProfiledUserDataAndVisualizationForTime(int i) {
        logger.debug("Creating ProfiledUsersVisualization for Time: " + i);
        fillData("PUV" + String.format("%03d", i), i);
        fillGraph("PUV" + String.format("%03d", i), i);
    }

    public void CreateProfiledUserVisualizationForTime(int i) {
        logger.debug("Creating ProfiledUsersVisualization for Time: " + i);
        fillGraph("PUV" + String.format("%03d", i), i);
    }

    public void CreateProfiledUserDataForTime(int i) {
        logger.debug("Creating ProfiledUsersVisualization for Time: " + i);
        fillData("PUV" + String.format("%03d", i), i);
        fillGraph("PUV" + String.format("%03d", i), i);
    }

    @Override
    public String graphData() {
        return "Vacio";
    }

    @Override
    public String graphData(int i) {
        return GraphsDataGenerator.usrAndFriendsGraphOutput(i, ExclusionMutua.getIdRedActiva());
    }
}
