package websiteschema.cluster;

import java.util.*;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;

/**
 *
 * @author ray
 */
public class FeatureStatHelper {

    private Map<String, Integer> mapDim = new HashMap<String, Integer>();

    private List<FeatureInfo> list = new ArrayList<FeatureInfo>();

    public FeatureInfo getDim(String dimName) {
        if (mapDim.containsKey(dimName)) {
            return list.get(mapDim.get(dimName));
        } else {
            FeatureInfo feature = new FeatureInfo();
            feature.setName(dimName);
            int dim = list.size();
            mapDim.put(dimName, dim);
            list.add(feature);
            return feature;
        }
    }

    public int getDimId(String dimName) {
        if (mapDim.containsKey(dimName)) {
            return mapDim.get(dimName);
        } else {
            return -1;
        }
    }

    public FeatureStatInfo getFinalResults() {
        if (!list.isEmpty()) {
            FeatureStatInfo ret = new FeatureStatInfo();
            ret.setMapDim(mapDim);
            ret.setList(list.toArray(new FeatureInfo[0]));
            return ret;
        }
        return null;
    }
}
