package ao.dd.shell.sql;

import ao.dd.shell.sql.driver.ConsoleDbParser;
import ao.util.io.Dirs;
import ao.util.parse.flat.Csv;
import javolution.text.Text;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: aostrovsky
 * Date: 20-Jul-2009
 * Time: 7:23:49 AM
 */
public class Table implements Iterable<String[]>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Text DATA;

    private final ConsoleDbParser.Factory DRIVER;

    private transient ConsoleDbParser parser;

    private transient Map<String, Integer> headerIndexes;

    public Table(Text consoleData, ConsoleDbParser.Factory driver) {
        DATA = consoleData;
        DRIVER = driver;
    }

    private ConsoleDbParser parser() {
        if (parser == null) {
            parser = DRIVER.newInstance(DATA);
        }
        return parser;
    }

    public boolean isError() {
        return parser().columnNames() == null;
    }

    public boolean isEmpty() {
        return isError() || parser().rowCount() == 0;
    }

    public String[] header() {
        return parser().columnNames();
    }

    public String header(int column) {
        return parser().columnNames()[column];
    }

    public int column(String named) {
        if (headerIndexes == null) {
            headerIndexes = new HashMap<String, Integer>();
            for (int i = 0; i < parser().columnNames().length; i++) {
                headerIndexes.put(header(i).trim().toUpperCase(), i);
            }
        }
        return headerIndexes.get(named.trim().toUpperCase());
    }

    public Iterator<String[]> iterator() {
        return new Iterator<String[]>() {

            private int nextIndex = 0;

            public boolean hasNext() {
                return nextIndex < rowCount();
            }

            public String[] next() {
                return row(nextIndex++);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public int rowCount() {
        return parser().rowCount();
    }

    public String[] row(int rowIndex) {
        return parser().row(rowIndex);
    }

    public String cell(int row, int column) {
        return row(row)[column];
    }

    public void toCsv(String outFile) {
        toCsv(new File(outFile));
    }

    public void toCsv(File outFile) {
        PrintStream out = null;
        try {
            Dirs.get(outFile.getParentFile());
            out = new PrintStream(new BufferedOutputStream(new FileOutputStream(outFile)));
            toCsv(out);
        } catch (FileNotFoundException e) {
            throw new Error(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void toCsv(PrintStream out) {
        if (isError()) return;
        out.println(Csv.toCsv((Object[]) header()));
        for (int r = 0; r < rowCount(); r++) {
            out.println(Csv.toCsv((Object[]) row(r)));
        }
    }

    @Override
    public String toString() {
        return rowCount() + ": " + Arrays.toString(header());
    }
}
