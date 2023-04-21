package iclab.core;

import iclab.core.ICAttribute.ICAttType;
import iclab.exceptions.ICInvalidMethodException;
import iclab.exceptions.ICParameterException;
import iclab.exceptions.ICParserException;
import iclab.parsers.ICArffParser;
import iclab.utils.ICDataUtils;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This object represents the datasets
 */
public class ICData {

    private String _name = "unnamed_dataset";

    private ArrayList<ICAttribute> _attList;

    private ArrayList<Integer> _classIndexes;

    private ArrayList<ICInstance> _instances;

    /**
	 * Constructor of the class based on a vector of instances. The compatibility of the instances has to be
	 * checked, increasing the computational time required to construct the object. For a faster method use the
	 * constructor based on an array of doubles
   * @param listOfInstances Vector containing objects of type ICInst
   * @throws ICParameterException
	 */
    public ICData(ArrayList<ICInstance> listOfInstances) throws ICParameterException {
        if (listOfInstances.size() <= 0) throw new ICParameterException("Cannot create a dataset from an empty set of instances!!!");
        _attList = listOfInstances.get(0).getAttList();
        _classIndexes = listOfInstances.get(0).getClassIndexes();
        _instances = new ArrayList<ICInstance>();
        _instances.add(listOfInstances.get(0));
        listOfInstances.remove(0);
        try {
            this.addInstances(listOfInstances);
        } catch (ICParameterException e) {
            throw new ICParameterException("The instances used to create the object are not compatible");
        }
    }

    /**
	 * Complete constructor
	 * @param instances List of values of the instances in the data set (first index is the instance index, the second the attribute index)
	 * @param listOfAttributes List of attributes of the data set
	 * @param classIndexes List of indexes of the classes
	 * @throws ICParameterException
	 */
    public ICData(double[][] instances, ArrayList<ICAttribute> listOfAttributes, ArrayList<Integer> classIndexes) throws ICParameterException {
        _attList = listOfAttributes;
        _classIndexes = classIndexes;
        if (instances != null) {
            _instances = new ArrayList<ICInstance>();
            double[] currentInstance;
            for (int inst = 0; inst < instances.length; inst++) {
                currentInstance = instances[inst];
                for (int index = 0; index < currentInstance.length; index++) if (!(listOfAttributes.get(index).getType() == ICAttType.numeric)) if (currentInstance[index] >= listOfAttributes.get(index).getAttCardinality()) throw new ICParameterException("The instances used to create the dataset are not compatible with the attribute list provided");
                _instances.add(new ICInstance(listOfAttributes, classIndexes, currentInstance, "N/A", 1));
            }
        }
    }

    /**
	 * This constructor makes a full copy of the ICData object
	 * @param dataset ICData to be copied
	 */
    public ICData(ICData dataset) {
        _name = "Copy.of." + dataset.getName();
        _attList = new ArrayList<ICAttribute>();
        Object[] attributes = dataset.attributes();
        for (int i = 0; i < attributes.length; i++) this._attList.add(new ICAttribute((ICAttribute) attributes[i]));
        _instances = new ArrayList<ICInstance>();
        Object[] instances = dataset.instances();
        for (int i = 0; i < instances.length; i++) this._instances.add(new ICInstance((ICInstance) instances[i]));
        if (dataset._classIndexes != null) {
            _classIndexes = new ArrayList<Integer>();
            Object[] classes = dataset.getClassIndexes().toArray();
            for (int i = 0; i < classes.length; i++) _classIndexes.add(((Integer) classes[i]).intValue());
        }
    }

    /**
	 * Constructor that creates a data set reading the information in an arff file
	 * @param arffFilePath Path of the file to load
	 * @throws ICParserException
	 * @throws ICParameterException
	 */
    public ICData(String arffFilePath) throws ICParserException, ICParameterException {
        try {
            ICData dataset = ICArffParser.load(new FileReader(arffFilePath));
            _attList = dataset._attList;
            _classIndexes = dataset._classIndexes;
            _instances = dataset._instances;
            _name = dataset._name;
        } catch (Exception e) {
            throw new ICParameterException("Problem while loading the dataset file: " + e.getMessage());
        }
    }

    /**
   *
   * @param nDiscVars
   * @param nContVars
   * @param nFuncs
   * @param nInstances
   * @throws ICInvalidMethodException
   */
    public ICData(long nDiscVars, long nContVars, long nFuncs, long nInstances) throws ICInvalidMethodException {
        for (long i = 0; i < nDiscVars; i++) {
            _attList.add(new ICAttribute("D" + i, ICAttribute.ICAttType.categorical));
        }
        for (long i = 0; i < nContVars; i++) {
            _attList.add(new ICAttribute("C" + i, ICAttribute.ICAttType.numeric));
        }
        for (long i = 0; i < nFuncs; i++) {
            _attList.add(new ICAttribute("F" + i, ICAttribute.ICAttType.numeric));
        }
    }

    /**
	 * This method returns the number of instances
	 * @return Number of instances
	 */
    public int numInstances() {
        int num = 0;
        if (_instances != null) num = _instances.size();
        return num;
    }

    /**
	 * This method returns the sum of all the instances weight
	 * @return Number of instances
	 */
    public double sumInstancesWeight() {
        double num = 0;
        if (_instances != null) for (int i = 0; i < _instances.size(); i++) num += _instances.get(i).weight();
        return num;
    }

    /**
	 * This method returns the number of attributes
	 * @return Number of attributes
	 */
    public int numAttributes() {
        return _attList.size();
    }

    /**
	 * This method returns a particular instance
	 * @param index Index of the instance to return
	 * @return Instance at the given index
	 */
    public ICInstance instance(int index) {
        ICInstance instance = null;
        if (_instances != null) instance = _instances.get(index);
        return instance;
    }

    /**
	 * This method returns a particular attribute
	 * @param index Index of the attribute to return
	 * @return Attribute at the given index
	 */
    public ICAttribute attribute(int index) {
        return _attList.get(index);
    }

    /**
	 * This method returns the name of the dataset
	 * @return Name of the dataset
	 */
    public String getName() {
        return _name;
    }

    /**
	 * This method returns the name of the list of attributes
	 * @return List of attributes
	 */
    public ArrayList<ICAttribute> getAttList() {
        return _attList;
    }

    /**
	 * Method to obtain an array containing the attributes
	 * @return Array of objects containing the attributes of the dataset
	 */
    public Object[] attributes() {
        return _attList.toArray();
    }

    /**
	 * Method to obtain an array containing the instances
	 * @return Array of objects containing the instances of the data set
	 */
    public Object[] instances() {
        Object[] instances = null;
        if (_instances != null) instances = _instances.toArray();
        return instances;
    }

    /**
	 * Method to obtain the list of indexes of the class
	 * @return Vector containing the list of indexes of the attributes that act as class
	 */
    public ArrayList<Integer> getClassIndexes() {
        return _classIndexes;
    }

    /**
	 * Method to set a single class index
	 * @param index Index of the class
	 */
    public void setClassIndex(int index) {
        int[] indexes = new int[1];
        indexes[0] = index;
        setClassIndexes(indexes);
    }

    /**
	 * Method to set the list of indexes of the class
	 * @param indexes List of indexes of the attributes that represent the class
	 */
    public void setClassIndexes(int[] indexes) {
        _classIndexes = new ArrayList<Integer>();
        for (int i = 0; i < indexes.length; i++) _classIndexes.add(indexes[i]);
        for (int i = 0; i < _instances.size(); i++) _instances.get(i).setClassIndexes(_classIndexes);
    }

    /**
	 * Method to set the list of indexes of the class
	 * @param indexes List of indexes of the attributes that represent the class
	 */
    public void setClassIndexes(ArrayList<Integer> indexes) {
        _classIndexes = indexes;
        for (int i = 0; i < _instances.size(); i++) _instances.get(i).setClassIndexes(indexes);
    }

    /**
	 * Method to set the name of the data set
	 * @param name new name of the data set
	 */
    public void setName(String name) {
        _name = name;
    }

    /**
	 * Method to add a set of instances to the data set
   * @param instances Instances to add
   * @throws ICParameterException
	 */
    public void addInstances(ArrayList<ICInstance> instances) throws ICParameterException {
        if (_instances == null) _instances = new ArrayList<ICInstance>();
        for (int i = 0; i < instances.size(); i++) {
            ICInstance currentInstance = instances.get(i);
            currentInstance.setClassIndexes(_classIndexes);
            ArrayList<ICAttribute> listOfAttributes = ICDataUtils.merge(currentInstance.getAttList(), _attList);
            if (listOfAttributes == null) {
                throw new ICParameterException("The list of attributes in the instances and in " + _name + " are not compatible");
            } else {
                if (!ICDataUtils.areEqual(listOfAttributes, _attList)) {
                    _attList = listOfAttributes;
                    for (int j = 0; j < _instances.size(); j++) _instances.get(j).setAttList(_attList);
                } else {
                    _instances.add(instances.get(i));
                }
            }
        }
    }

    /**
	 * Method for removing instances
	 * @param indexes List of indexes to keep / remove
	 * @param remove If true the instances pointed by "indexes" are remove, otherwise they are conserved
	 */
    public void filterInstances(ArrayList<Integer> indexes, boolean remove) {
        int size;
        size = indexes.size();
        if (_instances != null) {
            ArrayList<ICInstance> instances = new ArrayList<ICInstance>();
            for (int i = 0; i < size; i++) instances.add(_instances.get(indexes.get(i)));
            if (remove) _instances.removeAll(instances); else _instances.retainAll(instances);
        }
    }

    /**
	 * Method for combining two data sets. The method checks that the list of attributes are equal in both datasets. If they are not the method returns null
	 * If the list of classes is not the same in both data sets the list is taken from the dataset that invokes the method.
	 * @param dataset Data set to merge with
	 * @return New data set (or null if the two data sets are not compatible)
	 */
    public ICData merge(ICData dataset) {
        ICData newData = null;
        try {
            newData = new ICData(this);
            newData.addInstances(dataset._instances);
            newData.setName("merge.of." + _name + ".and." + dataset.getName());
        } catch (ICParameterException e) {
        }
        return newData;
    }

    /**
   *
   * @param dataset
   */
    public void join(ICData dataset) {
        try {
            this.addInstances(dataset._instances);
            this.setName("merge.of." + _name + ".and." + dataset.getName());
        } catch (ICParameterException e) {
        }
        return;
    }

    /**
	 * Method to obtain the data in the data set as a matrix
	 * @return Matrix containing the information in the data set
	 */
    public double[][] getData() {
        double[][] data = null;
        if (_instances != null) {
            data = new double[numInstances()][];
            for (int i = 0; i < _instances.size(); i++) data[i] = _instances.get(i).getInstanceValues();
        }
        return data;
    }

    /**
   * Method to obtain a subset of the list of instances
   * @param instIndexes ArrayList containing the indexes of the instances to recover
   * @return ArrayList with the desired instances
   */
    public ArrayList<ICInstance> getInstances(ArrayList<Integer> instIndexes) {
        ArrayList<ICInstance> instancesSubset;
        int size = instIndexes.size();
        instancesSubset = new ArrayList<ICInstance>();
        for (int i = 0; i < size; i++) instancesSubset.add(this._instances.get(instIndexes.get(i)));
        return instancesSubset;
    }

    /**
   * Method to obtain the list of instances of the data set. Bear in mind that the 
   * result is the object stored in the dataset and not a copy of it (thus, any further
   * modification of the returned object will affect the data set)
   * @return List of instances of the ICData
   */
    public ArrayList<ICInstance> getInstances() {
        return _instances;
    }

    /**
	 * This method outputs a String containing the info about the dataset in arff format
	 * @return Data set in arff format
	 */
    @Override
    public String toString() {
        return ICArffParser.print(this);
    }

    /**
   *
   * @param comparatorClass
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws Exception
   */
    public void sort(Comparator<ICInstance> comparatorClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
        Collections.sort(_instances, comparatorClass);
        throw new Exception();
    }
}
