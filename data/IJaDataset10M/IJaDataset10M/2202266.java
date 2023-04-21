package org.unitils.dataset;

import org.unitils.core.Unitils;
import org.unitils.dataset.structure.DataSetStructureGenerator;
import org.unitils.dataset.structure.DataSetStructureGeneratorFactory;
import java.io.File;
import static org.unitils.database.DatabaseUnitils.getDatabaseNames;

/**
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public class DataSetXSDGenerator {

    public static void generateDataSetXSDs() {
        for (String databaseName : getDatabaseNames()) {
            generateDataSetXSDs(databaseName);
        }
    }

    public static void generateDataSetXSDs(String databaseName) {
        getDataSetStructureGenerator().generateDataSetStructureAndTemplate(databaseName);
    }

    public static void generateDataSetXSDs(File targetDirectory) {
        for (String databaseName : getDatabaseNames()) {
            generateDataSetXSDs(databaseName, targetDirectory);
        }
    }

    public static void generateDataSetXSDs(String databaseName, File targetDirectory) {
        getDataSetStructureGenerator().generateDataSetStructureAndTemplate(databaseName, targetDirectory);
    }

    private static DataSetStructureGenerator getDataSetStructureGenerator() {
        DataSetModule dataSetModule = Unitils.getInstance().getModulesRepository().getModuleOfType(DataSetModule.class);
        DataSetStructureGeneratorFactory dataSetStructureGeneratorFactory = dataSetModule.getDataSetStructureGeneratorFactory();
        return dataSetStructureGeneratorFactory.getDataSetStructureGenerator();
    }
}
