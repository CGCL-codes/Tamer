package test.integration.backendfactories;

import static java.lang.String.format;
import static org.sqlsplatter.tinyhorror.values.DataType.TYPE_BOOL;
import static org.sqlsplatter.tinyhorror.values.DataType.TYPE_CHAR;
import static org.sqlsplatter.tinyhorror.values.DataType.TYPE_DATE;
import static org.sqlsplatter.tinyhorror.values.DataType.TYPE_NUMERIC;
import static org.sqlsplatter.tinyhorror.values.DataType.TYPE_DOUBLE;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.sqlsplatter.tinyhorror.MemDriver;
import org.sqlsplatter.tinyhorror.objects.AbstractFactory;
import org.sqlsplatter.tinyhorror.objects.MemFactory;
import org.sqlsplatter.tinyhorror.other.definitions.ColumnDef;
import org.sqlsplatter.tinyhorror.other.exceptions.THSException;
import org.sqlsplatter.tinyhorror.values.ConstantValue;
import test.integration.backendfactories.randomfarm.BaseRandomFarm;
import tools.TestingTools;

/**
 * Factory for dbf backend.
 *
 * @author Saverio Miroddi
 */
public class MemBackend implements IBackend {

    private static final Class DRV_CLASS = MemDriver.class;

    private static final String SOURCE = "test_data/temp_bk_txt";

    /** Thou shalt not forget the column positions */
    private final ColumnDef[] DEFAULT_COL_DATA = { new ColumnDef(getColName(0), TYPE_BOOL, null, null), new ColumnDef(getColName(1), TYPE_CHAR, 32, null), new ColumnDef(getColName(2), TYPE_DATE, null, null), new ColumnDef(getColName(3), TYPE_NUMERIC, 20, 5), new ColumnDef(getColName(4), TYPE_DOUBLE, null, null) };

    private final BaseRandomFarm _rndFarm;

    public AbstractFactory getFactory() {
        return MemFactory.getInstance();
    }

    public String getSource() {
        return SOURCE;
    }

    public MemBackend() {
        _rndFarm = new BaseRandomFarm();
    }

    public void init() throws Exception {
        Class.forName(DRV_CLASS.getName());
        File dataDir = new File(SOURCE);
        if (dataDir != null) {
            if (!dataDir.exists()) dataDir.mkdirs();
            TestingTools.deleteDirContent(dataDir);
        }
    }

    /**
	 * Return a list of column definitions of all the data types of this
	 * backend.
	 */
    public List<ColumnDef> getAllColumnDefTypes() throws THSException {
        List<ColumnDef> colDefs = new ArrayList<ColumnDef>();
        for (int i = 0; i < DEFAULT_COL_DATA.length; i++) {
            ColumnDef colDef = DEFAULT_COL_DATA[i];
            colDefs.add(colDef);
        }
        return colDefs;
    }

    public String getColName(int pos) {
        return format("COL%02d", pos);
    }

    public ConstantValue getRandomValue(ColumnDef colDef) {
        return _rndFarm.getRandomValue(colDef);
    }

    public List<ConstantValue[]> genTestData(List<ColumnDef> colDefs, int rowsNum) throws THSException {
        return _rndFarm.genTestData(colDefs, rowsNum);
    }

    /** Debug method */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
