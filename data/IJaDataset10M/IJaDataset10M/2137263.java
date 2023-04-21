package com.angel.architecture.io.dataGenerators;

import java.io.FileNotFoundException;
import java.io.InputStream;
import com.angel.architecture.constants.ArchitectureResourcesLocator;
import com.angel.architecture.io.rowProcessors.ConfigurationParameterAnnotationRowProcessorCommand;
import com.angel.architecture.persistence.beans.ConfigurationParameter;
import com.angel.common.helpers.FileHelper;
import com.angel.data.generator.annotations.Generator;
import com.angel.data.generator.annotations.importFileProcessorRunner.ImportFileProcessorRunnerBuilder;
import com.angel.data.generator.annotations.inputStream.InputStreamBuilder;
import com.angel.data.generator.exceptions.DataGeneratorException;
import com.angel.io.descriptor.FileProcessorDescriptor;
import com.angel.io.processors.commands.impl.ExcelFileProcessorCommand;
import com.angel.io.processors.runners.imports.impl.ImportFileAnnotationProcessorRunner;
import com.angel.io.processors.runners.imports.impl.ImportFileProcessorRunner;

/**
 * @author Guillermo Salazar
 * @since 27/Agosto/2009
 *
 */
@Generator(objectClass = ConfigurationParameter.class, dependencies = { ParameterAreaAnnotationDataGenerator.class }, daoName = "configurationParameterDAO", pages = {  })
public class ConfigurationParameterAnnotationDataGenerator {

    @ImportFileProcessorRunnerBuilder(fileProcessorDescriptor = FileProcessorDescriptor.class, name = "Import Configuration Parameters")
    public ImportFileProcessorRunner prepareImportFileProcessorRunner(FileProcessorDescriptor fileProcessorDescriptor) {
        return new ImportFileAnnotationProcessorRunner(fileProcessorDescriptor, new ExcelFileProcessorCommand(), new ConfigurationParameterAnnotationRowProcessorCommand());
    }

    @InputStreamBuilder
    public InputStream prepareInputStream() {
        try {
            return FileHelper.findInputStreamInClasspath(ArchitectureResourcesLocator.CONFIGURATION_PARAMETERS_EXCEL_FILE);
        } catch (FileNotFoundException e) {
            throw new DataGeneratorException("File not found [" + ArchitectureResourcesLocator.CONFIGURATION_PARAMETERS_EXCEL_FILE + "].", e);
        }
    }
}
