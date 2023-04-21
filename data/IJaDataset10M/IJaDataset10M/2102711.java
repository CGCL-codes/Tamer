package iclab.filtering.attribute;

import java.util.ArrayList;
import iclab.core.ICAttribute;
import iclab.core.ICData;
import iclab.core.ICInstance;
import iclab.exceptions.ICParameterException;
import iclab.filtering.ICDataFilter;
import iclab.utils.ICUtils;

public class ICFilterAttributes implements ICDataFilter {

    private int[] _attributeList;

    private boolean _remove;

    /**
	 * Constructor.
	 * @param list - List of attributes to retain/remove
	 * @param remove - If true, the attributes in "list" are remove. Otherwise, all the attributes
	 * except those in "list" are removed
	 */
    public ICFilterAttributes(int[] list, boolean remove) {
        _attributeList = ICUtils.sort(list, false);
        _remove = remove;
    }

    /**
	 * Method that performs the transformation of the data (the elimination of attributes)
	 * @param data - Original data set
	 * @return Transformed data set
	 * @throws ICParameterException - Thrown when attributes that are not categorical are used in a product
	 */
    @Override
    public ICData filter(ICData data) {
        ICData newData = new ICData(data);
        ArrayList<Integer> newClassIndexes = null;
        if (newData.getClassIndexes() != null) {
            ArrayList<Integer> oldClassIndexes = newData.getClassIndexes();
            for (int c = 0; c < oldClassIndexes.size(); c++) {
                int a = 0;
                int currentIndex = oldClassIndexes.get(c);
                while (a < _attributeList.length - 1 && _attributeList[a] < currentIndex) a++;
                if (_attributeList[a] == currentIndex && !_remove) {
                    if (newClassIndexes == null) newClassIndexes = new ArrayList<Integer>();
                    newClassIndexes.add(a);
                } else if (_attributeList[a] != currentIndex && _remove) {
                    if (newClassIndexes == null) newClassIndexes = new ArrayList<Integer>();
                    newClassIndexes.add(currentIndex - (a + 1));
                }
            }
        }
        ArrayList<ICAttribute> newAttList = new ArrayList<ICAttribute>();
        if (_remove) {
            int b = 0;
            for (int a = 0; a < newData.numAttributes(); a++) {
                if (b < _attributeList.length && a == _attributeList[b]) b++; else newAttList.add(newData.attribute(a));
            }
        } else {
            for (int a = 0; a < _attributeList.length; a++) newAttList.add(newData.attribute(_attributeList[a]));
        }
        ArrayList<ICInstance> newListOfInstances = new ArrayList<ICInstance>();
        for (int i = 0; i < newData.numInstances(); i++) {
            double[] instance = newData.instance(i).getInstanceValues();
            double[] newInstance = new double[newAttList.size()];
            if (_remove) {
                int removed = 0;
                for (int a = 0; a < newData.numAttributes(); a++) {
                    if (removed < _attributeList.length && a == _attributeList[removed]) removed++; else newInstance[a - removed] = instance[a];
                }
            } else {
                for (int a = 0; a < _attributeList.length; a++) newInstance[a] = instance[_attributeList[a]];
            }
            newListOfInstances.add(new ICInstance(newAttList, newClassIndexes, newInstance, newData.instance(i).getId(), newData.instance(i).weight()));
        }
        try {
            newData = new ICData(newListOfInstances);
        } catch (ICParameterException e) {
            e.printStackTrace();
        }
        newData.setClassIndexes(newClassIndexes);
        if (_remove) newData.setName(data.getName() + " attributes " + _attributeList + " removed"); else newData.setName(data.getName() + " attributes " + _attributeList + " kept");
        return newData;
    }
}
