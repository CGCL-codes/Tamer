package jogamp.graph.font.typecast.ot.table;

import java.io.DataInput;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:davidsch@dev.java.net">David Schweinsberg</a>
 * @version $Id: LangSys.java,v 1.2 2007-01-24 09:47:47 davidsch Exp $
 */
public class LangSys {

    private int _lookupOrder;

    private int _reqFeatureIndex;

    private int _featureCount;

    private int[] _featureIndex;

    /** Creates new LangSys */
    protected LangSys(DataInput di) throws IOException {
        _lookupOrder = di.readUnsignedShort();
        _reqFeatureIndex = di.readUnsignedShort();
        _featureCount = di.readUnsignedShort();
        _featureIndex = new int[_featureCount];
        for (int i = 0; i < _featureCount; i++) {
            _featureIndex[i] = di.readUnsignedShort();
        }
    }

    public int getLookupOrder() {
        return _lookupOrder;
    }

    public int getReqFeatureIndex() {
        return _reqFeatureIndex;
    }

    public int getFeatureCount() {
        return _featureCount;
    }

    public int getFeatureIndex(int i) {
        return _featureIndex[i];
    }

    protected boolean isFeatureIndexed(int n) {
        for (int i = 0; i < _featureCount; i++) {
            if (_featureIndex[i] == n) {
                return true;
            }
        }
        return false;
    }
}
