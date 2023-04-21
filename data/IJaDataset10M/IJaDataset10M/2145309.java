package sourceforge.pebblesframewor.base.data;

import java.util.*;
import sourceforge.pebblesframewor.api.data.*;

/**
 * DataSetImpl:
 * 
 * @author JunSun Whang
 * @version $Id: DataSetImpl.java 118 2009-03-30 04:38:59Z junsunwhang $
 */
public class DataSetImpl implements DataSet {

    private static final long serialVersionUID = 0l;

    private ArrayList<DataElement> dataElements = new ArrayList<DataElement>();

    private ArrayList<Dimension> dimensionsX = new ArrayList<Dimension>();

    private ArrayList<Dimension> dimensionsY = new ArrayList<Dimension>();

    private ArrayList<Dimension> dimensionsZ = new ArrayList<Dimension>();

    private RawResultSet rawResultSet = null;

    private int sizeX = 0;

    private int sizeY = 0;

    public DataSetImpl() {
    }

    public DataSetImpl(RawResultSet rawResultSet) {
        this.rawResultSet = rawResultSet;
    }

    public List<DataElement> getDataElements() {
        return dataElements;
    }

    public void addDataElement(DataElement element) {
        dataElements.add(element);
    }

    public List<Dimension> getDimensions(long dimensionAxis) {
        return (List) findDimension(dimensionAxis);
    }

    public void addDimension(Dimension dimension, long dimensionAxis) {
        ArrayList relDimension = findDimension(dimensionAxis);
        relDimension.add(dimension);
    }

    public RawResultSet getRawResultset() {
        return this.rawResultSet;
    }

    public void setRawResultset(RawResultSet results) {
        this.rawResultSet = results;
    }

    private ArrayList<Dimension> findDimension(long dimensionAxis) {
        return null;
    }
}
