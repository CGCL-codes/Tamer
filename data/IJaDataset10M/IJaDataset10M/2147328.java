package pedro.io;

import pedro.mda.model.*;
import pedro.system.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class XMLSubmissionFileWriter {

    private PedroFormContext pedroFormContext;

    private RecordModelFactory recordModelFactory;

    private boolean writeChildRecords;

    private RecordModel topRecordModel;

    private boolean writeTemplate;

    private boolean omitModelStamp;

    public XMLSubmissionFileWriter(PedroFormContext pedroFormContext, boolean writeTemplate) {
        this.pedroFormContext = pedroFormContext;
        writeChildRecords = true;
        this.writeTemplate = writeTemplate;
    }

    /**
	* model stamp is not written to the data file
	*/
    public void omitModelStamp() {
        omitModelStamp = true;
    }

    public void writeFile(OutputStream stream, RecordModel recordModel, boolean writeChildRecords) throws IOException {
        PedroDataFileWriter pedroDataFileWriter = new PedroDataFileWriter(pedroFormContext, writeTemplate, writeChildRecords);
        if (omitModelStamp == true) {
            pedroDataFileWriter.omitModelStamp();
        }
        pedroDataFileWriter.write(stream, recordModel);
    }

    public void writeFile(File file, RecordModel recordModel, boolean writeChildRecords) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        writeFile(fileOutputStream, recordModel, writeChildRecords);
    }

    public void writeFile(URL url, RecordModel recordModel, boolean writeChildRecords) throws IOException {
        URLConnection urlConnection = url.openConnection();
        OutputStream outputStream = urlConnection.getOutputStream();
        writeFile(outputStream, recordModel, writeChildRecords);
    }
}
