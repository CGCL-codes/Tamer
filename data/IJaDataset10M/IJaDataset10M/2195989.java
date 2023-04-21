package com.angel.io.processors.commands.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import com.angel.common.helpers.StringHelper;
import com.angel.io.descriptor.ExportFileProcessorDescriptor;
import com.angel.io.descriptor.ExportedFileDescriptor;
import com.angel.io.exceptions.InvalidFileAccessException;
import com.angel.io.processors.commands.exports.ExportFileProcessorCommand;
import com.angel.io.processors.commands.imports.ImportRowProcessorCommand;
import com.angel.io.separator.ColumnSeparator;
import com.angel.io.type.rows.ExportRowFile;
import com.angel.io.type.rows.impl.HeaderRowFile;
import com.angel.io.type.rows.impl.OutlookRowFile;

/**
 * @author William
 *
 */
public class OutlookFileProcessorCommand implements ExportFileProcessorCommand {

    public static final String FINAL_LINE_CHARACTER = "\n";

    private static final String VCS_FILE_EXTENSION = ".vcs";

    private ImportRowProcessorCommand command;

    public OutlookFileProcessorCommand() {
        super();
    }

    public OutlookFileProcessorCommand(ImportRowProcessorCommand command) {
        super();
        this.command = command;
    }

    /**
	 * @return the command
	 */
    public ImportRowProcessorCommand getCommand() {
        return command;
    }

    /**
	 * @param command the command to set
	 */
    public void setCommand(ImportRowProcessorCommand command) {
        this.command = command;
    }

    private String prepareHeader() {
        String header = "BEGIN:VCALENDAR\n";
        header += "METHOD:PUBLISH\n";
        header += "X-WR-TIMEZONE:US/Pacific\n";
        header += "PRODID:-//Apple Inc.//iCal 3.0//EN\n";
        header += "CALSCALE:GREGORIAN\n";
        header += "X-WR-CALNAME: Importacion.\n";
        header += "VERSION:2.0\n";
        header += "X-WR-RELCALID:F25432AC-0D07-48CB-AF89-1D2B23B80B8E\n";
        header += "X-APPLE-CALENDAR-COLOR:#F57802\n";
        header += "TRANSP:TRANSPARENT\n";
        header += "UID:\n";
        header += "DTSTART:\n";
        header += "DTEND:\n";
        header += "DESCRIPTION:\n";
        header += "STATUS:\n";
        header += "DTSTAMP:\n";
        header += "SUMMARY:\n";
        header += "CREATED:\n";
        header += "BEGIN:VALARM\n";
        header += "TRIGGER:PT60M\n";
        header += "ACTION:DISPLAY\n";
        header += "DESCRIPTION:Reminder\n";
        header += "END:VALARM\n";
        header += "END:VCALENDAR\n";
        return header;
    }

    public ExportRowFile createHeader(HeaderRowFile header, ColumnSeparator columnSeparator) {
        String headerString = this.prepareHeader();
        ExportRowFile exportHeader = new OutlookRowFile(headerString, ColumnSeparator.ENTER, header);
        for (int i = 0; i < header.quantityColumns().intValue(); i++) {
            String columnName = header.getColumnName(i);
            if (StringHelper.isNotEmpty(columnName)) {
                exportHeader.addStringValue(columnName, header.getColumnPosition(columnName));
            }
        }
        return exportHeader;
    }

    public void finish(ExportedFileDescriptor exportedFileDescriptor, ExportFileProcessorDescriptor fileProcessorDescriptor, HeaderRowFile header, List<ExportRowFile> exportRows) {
        try {
            OutputStream os = fileProcessorDescriptor.createOutputStream(exportedFileDescriptor, this.getFileExtension());
            OutputStreamWriter osw = new OutputStreamWriter(os);
            for (ExportRowFile exportRow : exportRows) {
                osw.write("BEGIN:EVENT");
                osw.write(exportRow.export());
                osw.write("END:EVENT");
            }
            osw.flush();
            osw.close();
        } catch (IOException e) {
            throw new InvalidFileAccessException("IO error ocurred when finish output stream. ", e);
        }
    }

    private String getFileExtension() {
        return VCS_FILE_EXTENSION;
    }

    public void initialize() {
    }

    public List<ExportRowFile> prepareFile(HeaderRowFile header, Integer quantityRows, ColumnSeparator columnSeparator) {
        List<ExportRowFile> exportRows = new ArrayList<ExportRowFile>();
        for (int i = 0; i < quantityRows; i++) {
            ExportRowFile exportHeader = new OutlookRowFile(columnSeparator, header.quantityColumns(), header);
            exportRows.add(exportHeader);
        }
        return exportRows;
    }
}
