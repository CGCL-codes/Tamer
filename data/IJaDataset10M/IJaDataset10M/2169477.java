package LogToDb;

import java.io.*;

class LogToDb {

    public static void main(String args[]) {
        String inFile = args[0];
        String outDb = args[1];
        String matchData = args[2];
        LogToDb lp = new LogToDb(inFile, outDb, matchData);
        lp.process();
    }

    public LogToDb(String inFile, String outDb, String mFileName) {
        logFileName = inFile;
        memory = new Memory(outDb, mFileName, logFileName);
        parser = new LineParser();
    }

    public void process() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(logFileName));
            System.out.println("Reading log data...");
            String str;
            boolean inProgress = true;
            ParsedLine parsed = null;
            while (inProgress && ((str = in.readLine()) != null)) {
                parsed = parser.parse(str);
                inProgress = memory.extract(parsed);
            }
        } catch (Exception e) {
            System.err.println("Error processing the log file: " + e);
        }
    }

    public ParsedLine parse(String line) throws IOException {
        return parser.parse(line);
    }

    public void convert(ParsedLine line) {
        memory.extract(line);
    }

    private String logFileName, dbFileName;

    private Memory memory;

    private LineParser parser;
}
