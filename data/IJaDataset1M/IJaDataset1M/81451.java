package net.frontlinesms.data.importexport;

import net.frontlinesms.csv.CsvImportReport;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class MessageCsvImportReport extends CsvImportReport {

    private int multimediaMessageCount;

    private MessageCsvImportReportState state;

    public MessageCsvImportReport(int multimediaMessageCount) {
        this.multimediaMessageCount = multimediaMessageCount;
        this.state = MessageCsvImportReportState.SUCCESS;
    }

    public MessageCsvImportReport(MessageCsvImportReportState state) {
        this.state = state;
    }

    public int getMultimediaMessageCount() {
        return this.multimediaMessageCount;
    }

    public MessageCsvImportReportState getState() {
        return state;
    }

    public enum MessageCsvImportReportState {

        FAILURE, SUCCESS
    }
}
