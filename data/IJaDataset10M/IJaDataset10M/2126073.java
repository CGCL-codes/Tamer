package org.unitils.dataset.structure;

import org.unitils.dataset.database.DataSourceWrapperFactory;
import java.io.File;

/**
 * Generator for XSDs, DTDs or other structure descriptors of database schemas.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public interface DataSetStructureGenerator {

    /**
     * @param dataSourceWrapperFactory The factory for the data source wrappers, not null
     * @param defaultTargetDirectory   The default target directory, null if generation should be skipped
     */
    void init(DataSourceWrapperFactory dataSourceWrapperFactory, String defaultTargetDirectory);

    /**
     * Generates both the XSDs or DTDs and the template xml using the default target directory.
     * If the default target directory is null, generation will be skipped.
     *
     * The files will be created in a folder   targetDirectory/databaseName  (targetDirectory if database name is null)
     * Make sure that the database name does not contain any invalid directory name characters.
     *
     * @param databaseName The name of the database to generate the schemas for, null for the default
     */
    void generateDataSetStructureAndTemplate(String databaseName);

    /**
     * Generates both the XSDs or DTDs and the template xml.
     *
     * The files will be created in a folder   targetDirectory/databaseName  (targetDirectory if database name is null)
     * Make sure that the database name does not contain any invalid directory name characters.
     *
     * @param databaseName    The name of the database to generate the schemas for, null for the default
     * @param targetDirectory The target directory for the files, not null
     */
    void generateDataSetStructureAndTemplate(String databaseName, File targetDirectory);

    /**
     * Generates the XSDs that describe the structure of the database schemas.
     *
     * The files will be created in a folder   targetDirectory/databaseName  (targetDirectory if database name is null)
     * Make sure that the database name does not contain any invalid directory name characters.
     *
     * @param databaseName    The name of the database to generate the schemas for, null for the default
     * @param targetDirectory The target directory for the files, not null
     */
    void generateDataSetStructure(String databaseName, File targetDirectory);

    /**
     * Generates a sample template xml file that can be used as a starting point for writing a data set file.
     * This is an xml file with the correct namespace declarations for using the XSDs.
     *
     * The files will be created in a folder   targetDirectory/databaseName  (targetDirectory if database name is null)
     * Make sure that the database name does not contain any invalid directory name characters.
     *
     * @param databaseName    The name of the database to generate the schemas for, null for the default
     * @param targetDirectory The target directory for the files, not null
     */
    void generateDataSetTemplateXmlFile(String databaseName, File targetDirectory);
}
