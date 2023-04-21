package preprocessing.methods.DataReduction;

import preprocessing.Parameters.ParameterDouble;
import preprocessing.methods.BasePreprocessorConfig;

/**
 * Created by IntelliJ IDEA.
 * User: lagon
 * Date: Aug 31, 2007
 * Time: 11:17:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class LeaveOutNeighboursConfig extends BasePreprocessorConfig {

    protected void setInitialValues() {
        addConfigKey(new ParameterDouble("Maximum distance between neighbours", 0.01, 0.05, 0.00, 1.00));
    }
}
