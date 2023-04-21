package it.southdown.avana.gatyping;

import it.southdown.avana.patterns.*;
import it.southdown.avana.util.*;

public class ClusterSerializer {

    public String serializeClusterTree(Cluster rootCluster) {
        StringBuffer sb = new StringBuffer();
        sb.append("# Cluster Info\n\n");
        sb.append("Cluster Id,Parent Id,Sequence Count,RMS MI,Split Site Count\n");
        serializeSingleClusterInfo(rootCluster, sb);
        sb.append('\n');
        sb.append('\n');
        sb.append("# Characteristic Variants\n\n");
        sb.append("Cluster Id,Position,Variants\n");
        serializeVariantInfo(rootCluster, sb);
        return sb.toString();
    }

    private void serializeSingleClusterInfo(Cluster cluster, StringBuffer sb) {
        String mi = "";
        String splitSiteCount = "";
        if (!cluster.isLeaf()) {
            BinarySplit binarySplit = cluster.getSplit();
            mi = Double.toString(binarySplit.getMutualInfo().getRmsMutualInfo());
            splitSiteCount = Integer.toString(binarySplit.getSplitPattern().getSites().length);
        }
        Cluster parent = cluster.getParent();
        String parentId = (parent == null) ? "" : parent.getId();
        String[] clusterData = new String[] { cluster.getId(), parentId, Integer.toString(cluster.getAlignmentScan().getSequenceCount()), mi, splitSiteCount };
        String clusterDataLine = TextUtilities.stringArrayToString(clusterData);
        sb.append(clusterDataLine);
        sb.append('\n');
        if (!cluster.isLeaf()) {
            for (Cluster child : cluster.getChildren()) {
                serializeSingleClusterInfo(child, sb);
            }
        }
    }

    private void serializeVariantInfo(Cluster cluster, StringBuffer sb) {
        SetPattern pattern = cluster.getCharacteristicPattern();
        if (pattern != null) {
            SetPatternSite[] sites = pattern.getSites();
            for (int i = 0; i < sites.length; i++) {
                SetPatternSite site = sites[i];
                String[] siteData = new String[] { cluster.getId(), Integer.toString(site.getPosition()), site.getSignature() };
                String siteDataLine = TextUtilities.stringArrayToString(siteData);
                sb.append(siteDataLine);
                sb.append('\n');
            }
        }
        if (!cluster.isLeaf()) {
            for (Cluster child : cluster.getChildren()) {
                serializeVariantInfo(child, sb);
            }
        }
    }
}
