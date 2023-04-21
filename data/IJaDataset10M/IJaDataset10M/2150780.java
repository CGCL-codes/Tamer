package net.chrisrichardson.cloudtools.maven.plugins;

import net.chrisrichardson.ec2deployer.cluster.ClusterManager;
import net.chrisrichardson.ec2deployer.cluster.SimpleCluster;

/**
 * Display a be-right-back pacge
 * @goal enablebrb
 * @author cer
 *
 */
public class EnableBrbMojo extends AbstractClusterMojo {

    /**
   * Brb page in static content
   * 
   * @parameter expression="${cloudtools.brb.page}" 
   * @required
   */
    protected String brbPage;

    @Override
    protected void executeWithCluster(ClusterManager manager, SimpleCluster cluster) {
        cluster.getClusterInstance().getWebServerTier().enableBrb(brbPage);
    }
}
