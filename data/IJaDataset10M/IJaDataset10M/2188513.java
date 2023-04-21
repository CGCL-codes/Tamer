package com.angel.webapp.services.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.angel.architecture.services.impl.GenericServiceImpl;
import com.angel.io.descriptor.ExportFileProcessorDescriptor;
import com.angel.io.descriptor.ExportedFileDescriptor;
import com.angel.io.processors.commands.exports.ExportFileProcessorCommand;
import com.angel.io.processors.commands.exports.ExportRowProcessorCommand;
import com.angel.io.processors.runners.exports.ExportProcessorRunner;
import com.angel.io.processors.runners.exports.impl.ExportFileProcessorRunner;
import com.angel.io.separator.ColumnSeparator;
import com.angel.webapp.services.ExportFilesService;

/**
 * @author William
 *
 */
@Service
public class ExportFilesServiceImpl extends GenericServiceImpl implements ExportFilesService {

    public ExportedFileDescriptor doExportFile(List<?> entitiesToExports, ExportFileProcessorCommand fileProcessorCommand, ExportRowProcessorCommand rowProcessorCommand, List<String> columnsNames) {
        ExportProcessorRunner processRunner = new ExportFileProcessorRunner(new ExportFileProcessorDescriptor("Export", ColumnSeparator.COMMA), rowProcessorCommand, fileProcessorCommand);
        int i = 0;
        for (String columnName : columnsNames) {
            if (i == 0) {
                processRunner.addFirstColumn(columnName);
            } else {
                processRunner.addNextColumn(columnName);
            }
            i++;
        }
        return processRunner.runProcess(entitiesToExports);
    }
}
