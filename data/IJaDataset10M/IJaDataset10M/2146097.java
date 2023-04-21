package sequime.IO.SequenceFormat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * All sequence formats extend this class. It provides basic read and write access.
 * 
 * @author micha
 *
 */
public abstract class AbstractSequenceFormat {

    protected String path;

    protected double currentRowNumber;

    protected double numberOfRows;

    public AbstractSequenceFormat(String path) {
        this.path = path;
        try {
            this.setNumberOfRows();
        } catch (Exception e) {
        }
    }

    /**
	 * Sets the number of rows the target file contains.
	 * 
	 * @throws Exception If file couldnt be read.
	 */
    protected void setNumberOfRows() throws Exception {
        BufferedReader reader = this.createReader();
        while (reader.readLine() != null) this.numberOfRows++;
        reader.close();
    }

    /**
	 * Sets the current progress, by counting all lines, inside nodes ExecutionContext.
	 * @param exec ExecutionContext of current node.
	 */
    public void updateReadProgress(ExecutionContext exec) {
        this.currentRowNumber += 1.0;
        exec.setProgress(this.currentRowNumber / this.numberOfRows);
    }

    /**
	 * Creates a reader to the (in constructor) specified file.
	 * @return new BufferedReader.
	 * @throws Exception If BufferedReader could not be created.
	 */
    protected BufferedReader createReader() throws Exception {
        if (this.path.indexOf("http://") != -1) {
            URL url = new URL(path);
            return new BufferedReader(new InputStreamReader(url.openStream()));
        }
        return new BufferedReader(new FileReader(this.path));
    }

    /**
	 * Creates a writer to the (in constructor) specified file.
	 * @return New BufferedWriter.
	 * @throws Exception If BufferedWriter could not be created.
	 */
    protected BufferedWriter createWriter() throws Exception {
        return new BufferedWriter(new FileWriter(this.path));
    }

    /**
	 * Removes all spaces (" ") contained in the sequence.
	 * @param sequence Sequence which may contains spaces.
	 */
    protected void removeSpaces(StringBuffer sequence) {
        int index = sequence.indexOf(" ");
        while (index != -1) {
            sequence.deleteCharAt(index);
            index = sequence.indexOf(" ");
        }
    }

    /**
	 * Reads a sequence format.
	 * @param exec ExecutionContext of the current node.
	 * @return A new BufferedDataTable containing all information read.
	 * @throws Exception If an error occurred during reading.
	 */
    public abstract BufferedDataTable[] read(final ExecutionContext exec) throws Exception;

    /**
	 * Write a new sequence format.
	 * @param exec ExecutionContext of the current node.
	 * @param data BufferedDataTable containing the information you want to write.
	 * @throws Exception If an error occured during writing.
	 */
    public abstract void write(final ExecutionContext exec, BufferedDataTable data) throws Exception;

    /**
	 * Write a new sequence format.
	 * @param exec ExecutionContext of the current node.
	 * @param data BufferedDataTable containing the information you want to write.
	 * @param sequenceColumn Specifies the column containing the sequence.
	 * @throws Exception If an error occured during writing.
	 */
    public abstract void write(final ExecutionContext exec, BufferedDataTable data, String sequenceColumn) throws Exception;
}
