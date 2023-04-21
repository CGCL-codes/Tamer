package net.chrisrichardson.cloudtools.maven.plugins;

import net.chrisrichardson.ec2deployer.cluster.ClusterManager;
import net.chrisrichardson.ec2deployer.cluster.SimpleCluster;

/**
 * Restart apache
 * @goal updatestaticcontent
 * @author cer
 *
 */
public class UpdateStaticContentMojo extends AbstractClusterMojo {

    @Override
    protected void executeWithCluster(ClusterManager manager, SimpleCluster cluster) {
        cluster.getClusterInstance().getWebServerTier().updateStaticContent();
    }
}
