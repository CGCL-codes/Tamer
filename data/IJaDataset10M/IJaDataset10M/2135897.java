package org.openquant.backtest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.data.time.TimeSeries;

public class SimpleEquityReport {

    private Log log = LogFactory.getLog(SimpleEquityReport.class);

    private String directoryname;

    private PrintWriter writer;

    private TimeSeries series;

    private Collection<Position> positions;

    public SimpleEquityReport(Collection<Position> positions) {
        initialize();
    }

    private void initialize() {
        try {
            String filename = directoryname + "/trades.csv";
            FileWriter fileWriter = new FileWriter(new File(filename));
            BufferedWriter bufWriter = new BufferedWriter(fileWriter);
            writer = new PrintWriter(bufWriter, true);
            series = new TimeSeries("Equity Curve");
            createCSVHeader(writer);
        } catch (IOException e) {
            log.error(e, e);
        }
    }

    private void createCSVHeader(PrintWriter writer) {
        writer.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", "Date", "Order Side", "Symbol", "Shares", "Share Price", "Commission", "Slippage", "Balance", "Value"));
    }

    private void writePosition(Position position) {
        writer.println(String.format("%1$s, %2$s, %3$td %3$tb %3$ty, %4$s, %5$td %5$tb %5$ty, ", position.getSymbol(), position.getEntryPrice(), position.getEntryDate(), position.getExitDate(), position.getExitPrice()));
    }
}
