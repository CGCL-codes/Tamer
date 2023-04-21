package planning.model;

import java.util.List;

/**
 * Keeps a configuration for each collidable in the <code>entities</code>.
 * 
 * After making a collision check do not forget to revert the configuration of 
 * collidable to the original value. So other collision checks will 
 * 
 * @author phoad
 */
public class RobotConfiguration {

    private List<IConfiguration> nextConfigs;

    public RobotConfiguration(List<IConfiguration> nextConfigs) {
        this.nextConfigs = nextConfigs;
    }

    public List<IConfiguration> getConfigurations() {
        return nextConfigs;
    }

    public String toString() {
        StringBuffer result = new StringBuffer("RobotConfiguration : \n");
        for (IConfiguration nextConfig : nextConfigs) {
            result.append("\t" + nextConfig + "\n");
        }
        return result.toString();
    }
}
