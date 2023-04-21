package iclab.filtering.attribute;

import java.util.ArrayList;
import iclab.core.ICAttribute;
import iclab.core.ICData;
import iclab.core.ICInstance;
import iclab.core.ICAttribute.ICAttType;
import iclab.exceptions.ICParameterException;
import iclab.filtering.ICDataFilter;

public class ICAttributeProduct implements ICDataFilter {

    private int[][] _products;

    /**
	 * Constructor. 
	 * @param products - List of attribute products to generate
	 * @throws ICParameterException - Thrown when the list of products is not valid (negative indexes or null values)
	 */
    public ICAttributeProduct(int[][] products) throws ICParameterException {
        _products = products;
        if (products != null) for (int a = 0; a < products.length; a++) if (products[a] == null | products[a].length < 1) throw new ICParameterException("The definition of products provided is not valid");
    }

    /**
	 * Method that performs the transformation of the data. The name of the new attributes is the name of the original
	 * attributes separated by the underscore character and in the same order as in the parameter "products" in the constructor.
	 * The attribute values are also the combination of the values of the original attributes separated by the underscore character
	 * and in the same order as in "products"
	 * @param data - Original data set
	 * @return Transformed data set
	 * @throws ICParameterException - Thrown when attributes that are not categorical are used in a product
	 */
    @Override
    public ICData filter(ICData data) throws ICParameterException {
        ICData newData;
        if (_products == null) {
            newData = data;
        } else {
            ArrayList<ICAttribute> attList = new ArrayList<ICAttribute>();
            for (int a = 0; a < _products.length; a++) {
                ICAttribute newAtt;
                if (_products[a].length == 1) {
                    newAtt = new ICAttribute(data.attribute(_products[a][0]));
                } else {
                    String name = "Prod";
                    for (int b = 0; b < _products[a].length; b++) {
                        if (data.attribute(_products[a][b]).getType() != ICAttType.categorical) throw new ICParameterException("Only categorical attributes can be used in the attribute product filter");
                        name += "_" + data.attribute(_products[a][b]).getName();
                    }
                    ArrayList<String> values = getListOfValues(_products[a], data);
                    newAtt = new ICAttribute(name, ICAttType.categorical, values);
                }
                attList.add(newAtt);
            }
            double[][] instValues = new double[data.numInstances()][attList.size()];
            for (int i = 0; i < data.numInstances(); i++) {
                double[] iValues = data.instance(i).getInstanceValues();
                for (int a = 0; a < _products.length; a++) {
                    if (_products[a].length == 1) {
                        instValues[i][a] = iValues[_products[a][0]];
                    } else {
                        double[] values = new double[_products[a].length];
                        for (int v = 0; v < values.length; v++) values[v] = iValues[_products[a][v]];
                        instValues[i][a] = this.getJointIndex((int[]) _products[a], values, data);
                    }
                }
            }
            newData = new ICData(instValues, attList, null);
        }
        return newData;
    }

    /**
	 * This is a private method to generate the list of possible values of the product
	 * @param indexes - Indexes of the attribues to combine
	 * @param data - Dataset containing the attribute list
	 * @return - List of the values of the product of the attributes (the value of each attribute separated by
	 * underscore, in the same order as in "indexes")
	 */
    private ArrayList<String> getListOfValues(int[] indexes, ICData data) {
        ArrayList<String> values = new ArrayList<String>();
        if (indexes.length == 1) {
            ArrayList<String> attValues = data.attribute(indexes[0]).getValues();
            for (int v = 0; v < attValues.size(); v++) values.add(attValues.get(v));
        } else {
            int[] newIndexes = new int[indexes.length - 1];
            for (int i = 1; i < indexes.length; i++) newIndexes[i - 1] = indexes[i];
            ArrayList<String> attValues = data.attribute(indexes[0]).getValues();
            ArrayList<String> rest = getListOfValues(newIndexes, data);
            for (int v = 0; v < attValues.size(); v++) for (int r = 0; r < rest.size(); r++) values.add(attValues.get(v) + "_" + rest.get(r));
        }
        return values;
    }

    /**
	 * Private method to obtain the joint index in the product of attributes
	 * @param indexes - List of indexes of the attributes in the product
	 * @param values - Values of the attributes
	 * @return Index of the combination of attribute values
	 * @throws ICParameterException - Thrown when the number of attributes and the number of values do not match
	 */
    private int getJointIndex(int[] indexes, double[] values, ICData data) throws ICParameterException {
        if (values.length != indexes.length) throw new ICParameterException("The size of the list of parents and the array of values have to be the same!!!");
        ArrayList<ICAttribute> attList = data.getAttList();
        int index = 0;
        if (indexes != null) {
            for (int p = 0; p < indexes.length - 1; p++) {
                int cardinalityNextParent = 0;
                cardinalityNextParent = attList.get(indexes[p + 1]).getAttCardinality();
                index += values[p];
                index *= cardinalityNextParent;
            }
            index += values[values.length - 1];
        }
        return index;
    }
}
