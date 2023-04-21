package preprocessing.methods.DataReduction;

import game.utils.CrossRoad;
import preprocessing.methods.BasePreprocessor;
import preprocessing.methods.DataReduction.Chen.ChenCondensingAlgorithm;
import preprocessing.methods.DataReduction.common.NDInstance;
import java.util.List;

/**
 * @author Helena Krkoskova
 */
public class RSP3CondensingReduce extends CondensingReducePreprocessor {

    public RSP3CondensingReduce() {
        super();
        methodName = "RSP3 condensing method";
        methodDescription = "Condensing algorithm";
        methodTree = "Data reduction.";
        baseConfig = new RSP3CondensingReduceConfig();
        type = BasePreprocessor.Type.GLOBAL;
    }

    public boolean run() {
        if (store.getNumberOfIgnoredAttributes() != 0) {
            logger.error("There are ignored attributes in the store. This method can not work with ignored attributes :(.");
            return false;
        }
        final List<NDInstance> instanceList = loadInstances();
        if (instanceList == null) return false;
        if (instanceList.size() <= 1) {
            logger.warn(methodName + " - no instances to act on. Can not continue");
            return true;
        }
        final int classesCount = store.getNumberOfOutputAttributes();
        try {
            final List<NDInstance> result = ChenCondensingAlgorithm.rsp3(instanceList, classesCount);
            saveInstances(result);
        } catch (IllegalArgumentException iae) {
            logger.error(iae.getMessage(), iae);
            if (CrossRoad.getInstance().inBatchMode()) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
}
