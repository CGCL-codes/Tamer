package audit;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.io.PrintWriter;
import java.util.logging.*;

public class OutputFileWriter {

    static final Logger logger = Logger.getLogger(OutputFileWriter.class.getPackage().getName());

    private BufferedWriter bufferedWriter;

    private PrintWriter print_Writer;
}
