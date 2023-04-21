package ssg.tools.common.fileUtilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import ssg.tools.common.fragmentedFile.EditOperation;
import ssg.tools.common.fragmentedFile.FileCmdEditor;
import ssg.tools.common.fragmentedFile.Fragment;
import ssg.tools.common.fragmentedFile.FragmentedFileInfo;
import ssg.tools.common.fragmentedFile.FragmentedInputStream;
import ssg.tools.common.fragmentedFile.StreamDataSource;

/**
 * FileComposer uses FileCmdEditor to process set of input files into single output file.
 * Processing is controlled via set of commands defined directly in command line or in separate file.
 *
 * FileComposer interprets several simple edit commands and generates proper EditOperation
 * collection and related data sources that are passed to FileCmdEditor.
 *
 * @author ssg
 */
public class FileComposer {

    public static void help(String s, CommandLineParser cmd) {
        echo("HELP");
        echo("" + FileComposer.class.getName() + ": create file based on 1 or more sources.");
        echo("Purpose: ");
        echo("  This utility provides command line interface to FileCmdEditor tool.");
        echo("  FileCmdEditor uses FragmentedFileInfo to prepare plan");
        echo("  of writing output file based on");
        echo("  set of simple edit operations from (optionally) various sources.");
        echo("  It allows to truncate file, cut, insert, overwrite byte blocks");
        echo("  in file using same or different  bytes source.");
        echo("Usage: java " + FileComposer.class.getName() + "-<option> [-<option> ...] <file name>");
        echo("Options: ");
        echo(" commands=<commands file name> : specifies file with commands.");
        echo("        Default=FileComposer.commands");
        echo(" command=<edit command> : specifies 1 of edit commands:");
        echo("   SOURCE <source name>,<source URI>");
        echo("   ADD <source name>,<source offset> : copy all source content");
        echo("       starting at source offset to end of output.");
        echo("   COPY <source name>,<target offset> : copy all source content");
        echo("       into target at target offset.");
        echo("   INSERT <source name>,<source offset>,<source size>,<target offset> :");
        echo("       copy source content from into target at target offset.");
        echo("   REPLACE <source name>,<source offset>,<source si<e>,<target offset>,<target size> :");
        echo("       replace source content from into target at target offset.");
        echo("   COPY <source name>,<target offset> : copy all source content");
        echo("       into target at target offset.");
        echo("   CUT <offset>,<size> : cut <size> bytes starting at <offset>.");
        echo(" Source name is any text. Special names are used for: ");
        echo("   __SOURCE__ : source file (optional)");
        echo("   __TARGET__ : target file");
    }

    public static void echo(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) throws IOException {
        CommandLineParser cmd = new CommandLineParser();
        cmd.addOption(new CommandLineParser.CMDOption("help", null));
        cmd.addOption(new CommandLineParser.CMDOption("?", null));
        cmd.addOption(new CommandLineParser.CMDOption("commands", "FileComposer.commands"));
        cmd.addOption(new CommandLineParser.CMDOption("command", true, true));
        cmd.parse(args);
        if (cmd.getArguments().length == 0 || cmd.getOption("help").isDefined() || cmd.getOption("?").isDefined()) {
            help("No arguments or help.", cmd);
            return;
        }
        String outputFileName = cmd.getArgument(0);
        List<EditOperation> operations = new LinkedList<EditOperation>();
        Map<String, URI> sourceURIs = new HashMap<String, URI>();
        List<ErrorInfo> errors = new LinkedList<ErrorInfo>();
        int lineNumber = 0;
        try {
            File f = (cmd.getOption("commands") != null && cmd.getOption("commands").getValue() != null) ? new File(cmd.getOption("commands").getValue()) : null;
            if (f != null && f.exists()) {
                LineNumberReader lnr = new LineNumberReader(new FileReader(f));
                lineNumber = lnr.getLineNumber();
                String s = null;
                while ((s = lnr.readLine()) != null) {
                    System.out.println("parsing from    file: " + s);
                    try {
                        EditOperation eop = OperationsParser.createOperation(s, sourceURIs);
                        if (eop != null) {
                            operations.add(eop);
                        }
                    } catch (ParseException pex) {
                        errors.add(new ErrorInfo(pex, lineNumber));
                    }
                }
                lnr.close();
            }
        } catch (IOException ioex) {
            errors.add(new ErrorInfo(ioex, lineNumber));
        }
        if (cmd.getOption("command").isDefined()) {
            for (String s : cmd.getOption("command").getValues()) {
                System.out.println("parsing from command: " + s);
                try {
                    EditOperation eop = OperationsParser.createOperation(s, sourceURIs);
                    if (eop != null) {
                        operations.add(eop);
                    }
                } catch (ParseException pex) {
                    errors.add(new ErrorInfo(pex, lineNumber));
                }
            }
        }
        if (errors.size() > 0) {
            System.out.println("Error detected while opening/parsing operations file " + cmd.getOption("commands").getValue());
            for (ErrorInfo ei : errors) {
                System.out.println("  [" + ei.lineNumber + "] " + ((ei.th.getMessage() != null) ? ei.th.getMessage() : ei.th.toString()));
            }
            return;
        }
        Map<String, StreamDataSource> dss = new HashMap<String, StreamDataSource>();
        for (String sourceName : sourceURIs.keySet()) {
            if (!dss.containsKey(sourceName)) {
                URI uri = sourceURIs.get(sourceName);
                URL url = null;
                File dsf = null;
                try {
                    url = uri.toURL();
                } catch (Exception muex) {
                    dsf = new File(URLDecoder.decode(uri.toString(), "UTF-8"));
                    if (!dsf.exists()) {
                        throw new IOException("Illegal source: File not found '" + uri.toString() + "'");
                    }
                }
                if (url != null) {
                    dss.put(sourceName, new StreamDataSource(sourceName, url, true));
                } else {
                    dss.put(sourceName, new StreamDataSource(sourceName, dsf));
                }
            }
        }
        final FileCmdEditor fe = new FileCmdEditor(null, outputFileName);
        Thread monitor = new Thread() {

            @Override
            public void run() {
                System.out.println("Monitor thread OPENED: " + (new Date()).getTime());
                long lastPosition = 0;
                int dotCounter = 0;
                try {
                    while (1 == 1) {
                        sleep(5);
                        try {
                            Fragment fr = fe.getLastProcessingFragment().fr;
                            long position = fe.getLastProcessingFragment().length;
                            EditOperation eop = (fr != null) ? fr.eop : null;
                            if (lastPosition != position) {
                                System.out.println("  monitoring [" + (new Date()).getTime() + "]: pos=" + position + ", processing " + fr + " [" + ((eop != null) ? eop.getStatus() : "?") + "]");
                            } else {
                                System.out.print(".");
                                dotCounter++;
                                if (dotCounter == 32) {
                                    dotCounter = 0;
                                    System.out.println();
                                }
                                System.out.flush();
                            }
                            lastPosition = position;
                        } catch (Throwable th) {
                        }
                    }
                } catch (InterruptedException iex) {
                    System.out.println("\nMonitor thread CLOSED: " + (new Date()).getTime());
                }
            }
        };
        monitor = null;
        if (monitor != null) {
            monitor.start();
        }
        if (1 == 0) {
            Date start = new Date();
            System.out.println("\nProcess via FragmentedInputStream [" + start + "]:");
            FragmentedFileInfo ffi = FileCmdEditor.buildFFI(operations, dss);
            InputStream fis = new BufferedInputStream(new FragmentedInputStream(ffi, dss), 1024 * 30);
            OutputStream fos = new BufferedOutputStream(new FileOutputStream(outputFileName + ".fis"), 1024 * 30);
            byte[] buf = new byte[1024 * 4];
            int cnt = 0;
            long cntT = 0;
            while ((cnt = fis.read(buf)) > 0) {
                fos.write(buf, 0, cnt);
                cntT += cnt;
            }
            fos.close();
            fis.close();
            Date end = new Date();
            System.out.println("Elapsed time: " + (end.getTime() - start.getTime()));
        }
        if (1 == 1) {
            Date start = new Date();
            System.out.println("\nProcess via FileCmdEditor [" + start + "]:");
            List<EditOperation> processedOperations = fe.processOperations(operations, dss, true);
            Date end = new Date();
            System.out.println("Elapsed time: " + (end.getTime() - start.getTime()));
            System.out.println("\nOperations processing results:");
            for (EditOperation eop : processedOperations) {
                System.out.println(eop.getStatus() + " for " + eop);
            }
        }
        if (monitor != null) {
            monitor.interrupt();
        }
    }

    /**
     * Parses a line of text and generates appropriate edit operation.
     * Operations:
     * COPY     source,offset           : copies all content from "source" to target starting at given "offset" in target
     * CUT      offset,length           : cuts in target "length" bytes starting from "offset"
     * REPLACE  source,source_offset,source_length,offset,length : replaces bytes
     * INSERT   source,source_offset,source_length,offset : inserts source bytes at given offset (===replace with 0target length)
     * ADD      source,source_offset    : copies all content from "source" starting at "source_offset" to target starting at the end
     * SOURCE   name,URI                : defines data source
     *
     * Notes:
     *   "source" in operations other than "SOURCE" is the source name defined in "SOURCE" operation.
     *   Special source names are:
     *     __SOURCE__ : input file
     *     __TARGET__ : output file
     *   Numeric values (offset, length) are long numbers defined in decimal of hexadecimal (0xnnn) forms.
     *   Special text values may be used:
     *     START      : 0
     *     CURRENT    : -1 (current position in target)
     *     END        : -2 (end position == file length)
     */
    public static class OperationsParser extends EditOperation {

        enum OPS {

            COPY, CUT, REPLACE, INSERT, ADD, SOURCE, _unknown_
        }

        /**
         * Parses text and returns edit operation instance or null if not valid or does not reult in operation.
         * Updates "sources" if source is defined.
         *
         * @param s
         * @param sources
         * @return
         */
        public static EditOperation createOperation(String s, Map<String, URI> sources) throws ParseException {
            if (s == null) {
                return null;
            } else {
                s = s.trim();
                if (s.startsWith("#") || s.startsWith("//")) {
                    return null;
                }
            }
            int idxSP = s.indexOf(' ');
            if (idxSP == -1) {
                return null;
            }
            try {
                OPS op = OPS._unknown_;
                try {
                    op = OPS.valueOf(s.substring(0, idxSP).trim());
                    s = s.substring(idxSP).trim();
                } catch (Throwable th) {
                }
                String[] params = s.split(",");
                for (int i = 0; i < params.length; i++) {
                    params[i] = params[i].trim();
                }
                long[] paramsL = new long[params.length];
                for (int i = 0; i < params.length; i++) {
                    try {
                        if (params[i].startsWith("0x") || params[i].startsWith("0X")) {
                            paramsL[i] = Long.parseLong(params[i].substring(2), 16);
                        } else {
                            paramsL[i] = Long.parseLong(params[i]);
                        }
                    } catch (Throwable th) {
                        if (params[i].toUpperCase().equals("START")) {
                            paramsL[i] = EditOperation.POSITION_START;
                        } else if (params[i].toUpperCase().equals("CURRENT")) {
                            paramsL[i] = EditOperation.POSITION_CURRENT;
                        } else if (params[i].toUpperCase().equals("END")) {
                            paramsL[i] = EditOperation.POSITION_END;
                        } else {
                            paramsL[i] = EditOperation.POSITION_PARSE_ERROR;
                        }
                    }
                }
                switch(op) {
                    case COPY:
                        if (paramsL[1] == EditOperation.POSITION_PARSE_ERROR) {
                            throw new Exception(op + ": number parse error: source offset=" + params[1]);
                        }
                        return EditOperation.createCopyAllOperation(params[0], paramsL[1]);
                    case CUT:
                        if (paramsL[0] == EditOperation.POSITION_PARSE_ERROR || paramsL[1] == EditOperation.POSITION_PARSE_ERROR) {
                            throw new Exception(op + ": number parse error: target offset=" + params[0] + ", target length=" + params[1]);
                        }
                        return EditOperation.createCutOperation(paramsL[0], paramsL[1]);
                    case REPLACE:
                        if (paramsL[1] == EditOperation.POSITION_PARSE_ERROR || paramsL[2] == EditOperation.POSITION_PARSE_ERROR || paramsL[3] == EditOperation.POSITION_PARSE_ERROR || paramsL[4] == EditOperation.POSITION_PARSE_ERROR) {
                            throw new Exception(op + ": number parse error: source offset=" + params[1] + ", source length=" + params[2] + ", target offset=" + params[3] + ", target length=" + params[4]);
                        }
                        return EditOperation.createReplaceOperation(params[0], paramsL[1], paramsL[2], paramsL[3], paramsL[4]);
                    case INSERT:
                        if (paramsL[1] == EditOperation.POSITION_PARSE_ERROR || paramsL[2] == EditOperation.POSITION_PARSE_ERROR || paramsL[3] == EditOperation.POSITION_PARSE_ERROR) {
                            throw new Exception(op + ": number parse error: source offset=" + params[1] + ", source length=" + params[2] + ", target offset=" + params[3]);
                        }
                        return EditOperation.createReplaceOperation(params[0], paramsL[1], paramsL[2], paramsL[3], 0);
                    case ADD:
                        if (paramsL[1] == EditOperation.POSITION_PARSE_ERROR) {
                            throw new Exception(op + ": offset parse error: " + params[1]);
                        }
                        return EditOperation.createReplaceOperation(params[0], paramsL[1], EditOperation.POSITION_END, EditOperation.POSITION_END, 0);
                    case SOURCE:
                        if (params[0].equals(EditOperation.SOURCE_IS_ORIGINAL) || params[0].equals(EditOperation.SOURCE_IS_TARGET)) {
                        } else {
                            if (sources != null) {
                                URI oldURI = sources.get(params[0]);
                                URI newURI = (params[1].indexOf("://") != -1) ? new URI(params[1]) : new URI(URLEncoder.encode(params[1], "UTF-8"));
                                if (oldURI == null || oldURI.equals(newURI)) {
                                    sources.put(params[0], newURI);
                                } else {
                                    throw new Exception(op + ": Incompatible duplicate source definition: old=" + oldURI + ", new=" + newURI);
                                }
                            }
                        }
                        break;
                    case _unknown_:
                        break;
                    default:
                        break;
                }
            } catch (Throwable th) {
                throw new ParseException("Error while parsing operation for " + s + ": " + th.getMessage(), -1);
            }
            return null;
        }
    }

    static class ErrorInfo {

        Throwable th;

        int lineNumber;

        public ErrorInfo(Throwable th, int lineNumber) {
            this.th = th;
            this.lineNumber = lineNumber;
        }
    }
}
