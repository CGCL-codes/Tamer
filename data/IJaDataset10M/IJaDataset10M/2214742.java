package org.axcommunity.niagara.bql;

import javax.baja.sys.*;
import javax.baja.status.*;
import javax.baja.file.*;
import javax.baja.naming.*;
import java.io.*;

/*************************************************************************************************
 * This Code will Export a History file and save it as a .csv.  
 *
 * The "history" will be the bql query call for the history file you wish to create.
 * The "historyName" will be the file name that it is saved as. 
 * The file is saved in the "Path" location. 
 * The "Path" must include a final "/" ,and the folder that you are saving to must already exist.
 * @author  CMH, Xex-com		07/31/09
 */
public class BHistoryToCSV extends BComponent {

    /**Enter the the bql query call for the history file you wish to create.*/
    public static final Property history = newProperty(Flags.SUMMARY, new BStatusString());

    public BStatusString getHistory() {
        return (BStatusString) get(history);
    }

    public void setHistory(BStatusString v) {
        set(history, v);
    }

    /**Enter the filename of the export*/
    public static final Property historyName = newProperty(Flags.SUMMARY, new BStatusString());

    public BStatusString getHistoryName() {
        return (BStatusString) get(historyName);
    }

    public void setHistoryName(BStatusString v) {
        set(historyName, v);
    }

    /**Enter the path for the .CSV file.  Default is the Daemon folder.  Must include a final "/" ,and the folder that you are saving to must already exist.*/
    public static final Property path = newProperty(Flags.SUMMARY, new BStatusString());

    public BStatusString getPath() {
        return (BStatusString) get(path);
    }

    public void setPath(BStatusString v) {
        set(path, v);
    }

    /**
	 * This action:
	 *   - executes bql query which returns a ITable
	 *   - formats the table as an in-memory CSV file
	 *   - saves the CSV file as an file
	 */
    public static final Action execute = newAction(0);

    public void execute() {
        invoke(execute, null);
    }

    public void doExecute() {
        try {
            OrdTarget table = query();
            String csv = exportToCsv(table);
            hist(getHistoryName() + ".csv", csv);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
	 * Perform a bql query which returns an OrdTarget for a BITable
	 */
    private OrdTarget query() throws Exception {
        return BOrd.make(getHistory().getValue()).resolve();
    }

    /**
	 * Run the CSV exporter against the specified table to build an
	 * in memory representation of the table as a CSV file.
	 */
    private String exportToCsv(OrdTarget table) throws Exception {
        BExporter exporter = (BExporter) Sys.getType("file:ITableToCsv").getInstance();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExportOp op = ExportOp.make(table, out);
        exporter.export(op);
        return new String(out.toByteArray());
    }

    /**
	 * This action saves the converted History file in the Path specified using the History name as the file name. 
	 */
    private void hist(String fileName, String attachment) throws Exception {
        try {
            FileOutputStream fos = new FileOutputStream(getPath().getValue() + getHistoryName().getValue() + ".csv", false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(attachment);
            oos.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println(" Unable to find " + getHistoryName() + ".csv");
        }
    }

    public BIcon getIcon() {
        return icon;
    }

    private static final BIcon icon = BIcon.make("local:|module://axCommunity/org/axcommunity/niagara/graphics/XENCOM_LogoMini.png");

    public static final Type TYPE = Sys.loadType(BHistoryToCSV.class);

    public Type getType() {
        return TYPE;
    }
}
