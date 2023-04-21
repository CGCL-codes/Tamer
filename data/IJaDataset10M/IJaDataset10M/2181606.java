package websiteschema.cluster.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import websiteschema.cluster.DocVectorConvertor;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.Dimension;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;
import static websiteschema.utils.PojoMapper.*;

/**
 * 用来分析新闻类型的网站
 * @author ray
 */
public class SimpleClusterAnalyzer implements ClusterAnalyzer {

    BasicAnalysisResult result = new BasicAnalysisResult();

    AnalyzerUtil analyzer = new AnalyzerUtil();

    /**
     * 对聚类结果进行基本分析
     * @param old
     * @param cm
     * @param samples
     * @return
     */
    public Map<String, String> analysis(Map<String, String> old, ClusterModel cm, List<Sample> samples) {
        if (null == cm || null == samples) {
            return old;
        }
        Map<String, String> ret = null != old ? old : new HashMap<String, String>();
        Cluster[] clusters = cm.getClusters();
        FeatureStatInfo statInfo = cm.getStatInfo();
        if (null != clusters) {
            List<DocVector> space = analyzer.convertSamples(samples, statInfo);
            findBasicParameter(ret, clusters, space, samples, statInfo);
            confirmClusterType(clusters, space, samples, statInfo);
        }
        return ret;
    }

    /**
     * 查找每个类中的相同节点，识别其中的有效节点、无效节点、标题前缀和后缀。
     * @param old 可以重用的存放返回参数的容器
     * @param clusters 经过聚类得到的结果
     * @param space 样本空间
     * @param samples 样本集合
     * @param statInfo 样本的统计信息
     * @return
     */
    private Map<String, String> findBasicParameter(Map<String, String> old, Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo) {
        Map<String, String> ret = null != old ? old : new HashMap<String, String>();
        for (Cluster cluster : clusters) {
            if (cluster.getSamples().size() > 1) {
                List<String> allSamples = cluster.getSamples();
                List<String> rowKeys = new ArrayList<String>();
                int count = allSamples.size() > 10 ? 10 : allSamples.size();
                for (int i = 0; i < count; i++) {
                    rowKeys.add(allSamples.get(i));
                }
                List<DocVector> vectors = analyzer.getVectors(rowKeys, space);
                if (null != vectors && !vectors.isEmpty()) {
                    List<Sample> clusterSamples = analyzer.getSamples(rowKeys, samples);
                    Set<String> commonNodes = analyzer.findCommonNodes(vectors, statInfo);
                    analyzer.findTitlePrefixAndSuffix(result.getTitlePrefix(), result.getTitleSuffix(), clusterSamples);
                    Set<String> invalidNodeSet = analyzer.findInvalidNodes(clusterSamples, commonNodes, 0.6);
                    result.getInvalidNodes().addAll(invalidNodeSet);
                    result.getValidNodes().addAll(commonNodes);
                    for (String xpath : result.getInvalidNodes()) {
                        result.getValidNodes().remove(xpath);
                    }
                }
            } else {
            }
        }
        try {
            ret.put("ValidNodes", toJson(result.getValidNodes()));
            ret.put("InvalidNodes", toJson(result.getInvalidNodes()));
            ret.put("TitlePrefix", toJson(result.getTitlePrefix()));
            ret.put("TitleSuffix", toJson(result.getTitleSuffix()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    private void confirmClusterType(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo) {
        for (Cluster cluster : clusters) {
            if (cluster.getSamples().size() > 0) {
                List<String> allSamples = cluster.getSamples();
                List<String> rowKeys = new ArrayList<String>();
                int count = allSamples.size() > 10 ? 10 : allSamples.size();
                for (int i = 0; i < count; i++) {
                    rowKeys.add(allSamples.get(i));
                }
                List<DocVector> vectors = analyzer.getVectors(rowKeys, space);
                if (null != vectors && !vectors.isEmpty()) {
                    double textWeight = 0.0;
                    int textCount = 0;
                    double anchorWeight = 0.0;
                    int anchorCount = 0;
                    for (DocVector v : vectors) {
                        Dimension dims[] = v.getDims();
                        for (Dimension dim : dims) {
                            int dimId = dim.getId();
                            int value = dim.getValue();
                            FeatureInfo feature = statInfo.getList()[dimId];
                            String xpath = feature.getName();
                            if (!result.getInvalidNodes().contains(xpath)) {
                                xpath = xpath.toLowerCase();
                                if (feature.getWeight() > 0) {
                                    if (xpath.endsWith("/a")) {
                                        anchorWeight += Math.log(feature.getWeight());
                                        anchorCount++;
                                    } else {
                                        textWeight += Math.log(feature.getWeight());
                                        textCount++;
                                    }
                                }
                            }
                        }
                    }
                    double ratio = textWeight / (textWeight + anchorWeight);
                    double countRatio = (double) textCount / (double) (textCount + anchorCount);
                    System.out.println("cluster: " + cluster.getCustomName() + " text ratio: " + ratio + "text count ratio: " + countRatio);
                    String type = "LINKS";
                    if ((textWeight + anchorWeight) < 0.01D) {
                        type = "INVALID";
                    } else if (ratio > 0.5) {
                        type = "DOCUMENT";
                    }
                    cluster.setType(type);
                } else {
                    cluster.setType("INVALID");
                }
            } else {
                String type = "INVALID";
                cluster.setType(type);
            }
        }
    }

    @Override
    public BasicAnalysisResult getBasicAnalysisResult() {
        return this.result;
    }

    @Override
    public void setFieldAnalyzers(List<IFieldAnalyzer> fieldAnalyzers) {
    }
}
