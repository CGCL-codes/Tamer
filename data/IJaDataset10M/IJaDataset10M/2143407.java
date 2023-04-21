package com.angel.data.generator.sorter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.angel.data.generator.base.DataClassGenerator;
import com.angel.data.generator.interfaces.DataGenerator;

/**
 * @author William
 *
 */
public class DataGeneratorSorter {

    private List<DataClassGenerator> dataClassGenerators;

    public DataGeneratorSorter(Collection<DataClassGenerator> dataClassGenerators) {
        super();
        this.setDataClassGenerators(new ArrayList<DataClassGenerator>());
        this.getDataClassGenerators().addAll(dataClassGenerators);
    }

    /**
	 * @return the dataClassGenerators
	 */
    public List<DataClassGenerator> getDataClassGenerators() {
        return dataClassGenerators;
    }

    /**
	 * @param dataClassGenerators the dataClassGenerators to set
	 */
    public void setDataClassGenerators(List<DataClassGenerator> dataClassGenerators) {
        this.dataClassGenerators = dataClassGenerators;
    }

    public List<DataClassGenerator> sortDataClassGenerators() {
        List<DataClassGenerator> sortedDataClassGenerators = new ArrayList<DataClassGenerator>();
        DataClassGenerator[] dataClassGeneratorsArray = this.getDataClassGenerators().toArray(new DataClassGenerator[this.getDataClassGenerators().size()]);
        DataClassGenerator[] dataClassGeneratorsArraySorted = this.appliesOrder(dataClassGeneratorsArray);
        for (DataClassGenerator dcg : dataClassGeneratorsArraySorted) {
            sortedDataClassGenerators.add(dcg);
        }
        return sortedDataClassGenerators;
    }

    @SuppressWarnings("unchecked")
    protected DataClassGenerator[] appliesOrder(DataClassGenerator[] dataClassGenerators) {
        DataClassGenerator[] X = dataClassGenerators;
        int i, j;
        DataClassGenerator aux;
        for (i = 0; i < X.length; i++) {
            for (j = 0; j < X.length - 1; j++) {
                if (!X[j].dependsOn((Class<? extends DataGenerator>) X[j + 1].getDataGenerator().thisDataGeneratorClass())) {
                    aux = X[j];
                    X[j] = X[j + 1];
                    X[j + 1] = aux;
                }
            }
        }
        return X;
    }
}
