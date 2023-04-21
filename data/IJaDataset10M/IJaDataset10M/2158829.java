package nutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;

/** Given a file in matrix format, this transposes the file. */
public class TransposeFile {

    private static final int SentinelNumLines = 200000;

    String[][] mFileData;

    String mOutFilename;

    public TransposeFile(String inFilename, String outFilename) {
        mFileData = IOUtils.readAllLinesFromFileAsMatrix(inFilename, "\\t");
        mOutFilename = outFilename;
    }

    public void performTranspose() {
        String[][] fileDataTransposed = new String[mFileData[0].length][mFileData.length];
        performTranspose(mFileData, fileDataTransposed);
        BufferedWriter out = IOUtils.getBufferedWriter(mOutFilename);
        IOUtils.writeOutputFile(out, fileDataTransposed, '\t');
        IOUtils.closeBufferedWriter(out);
    }

    /** Returns the transposed data. */
    public static String[][] performTranspose(String[][] fileData, String[][] fileDataTransposed) {
        CompareUtils.ensureTrue(fileDataTransposed.length == fileData[0].length, "ERROR: Dimensions of output file not correctly set!");
        CompareUtils.ensureTrue(fileDataTransposed[0].length == fileData.length, "ERROR: Dimensions of output file not correctly set!");
        for (int row = 0; row < fileData.length; row++) {
            for (int col = 0; col < fileData[row].length; col++) {
                fileDataTransposed[col][row] = fileData[row][col];
            }
        }
        return fileDataTransposed;
    }

    /** Performs a transpose when columns are very few. */
    public static void performTranspose_FewColumns(String inFilename, String outFilename) {
        int numLinesInFile = IOUtils.countNumberLinesInFile(inFilename);
        if (numLinesInFile == 0) return;
        String tempFileSuffix = ".aJ84Dx33.txt";
        ArrayList<String> tempFilenames = FileSplit.splitFile(inFilename, tempFileSuffix, SentinelNumLines);
        ArrayList<String> tempOutFilenames = new ArrayList<String>();
        for (String tempFilename : tempFilenames) {
            String tempOutFilename = tempFilename + ".out.txt";
            tempOutFilenames.add(tempOutFilename);
            performTranspose_FewColumns_NoSplit(tempFilename, tempOutFilename);
        }
        String[] pasteFilesArgs = PasteFiles.constructArgs(tempOutFilenames, 0, outFilename);
        PasteFiles.main(pasteFilesArgs);
        IOUtils.deleteAllFiles(tempOutFilenames);
        IOUtils.deleteAllFiles(tempFilenames);
    }

    /** Performs a transpose for one file with no file splitting. */
    public static void performTranspose_FewColumns_NoSplit(String inFilename, String outFilename) {
        BufferedReader in = IOUtils.getBufferedReader(inFilename);
        String line = IOUtils.getNextLineInBufferedReader(in);
        CompareUtils.ensureTrue(line != null, "ERROR: Null line!");
        char separatorChar = '\t';
        String regexStr = "\\t";
        String[] components = line.split(regexStr);
        ArrayList<StringBuilder> sbList = new ArrayList<StringBuilder>();
        for (int i = 0; i < components.length; i++) {
            StringBuilder sb = new StringBuilder(65536);
            sb.append(components[i]);
            sbList.add(sb);
        }
        while ((line = IOUtils.getNextLineInBufferedReader(in)) != null) {
            components = line.split(regexStr);
            for (int i = 0; i < components.length; i++) {
                sbList.get(i).append(separatorChar).append(components[i]);
            }
        }
        IOUtils.closeBufferedReader(in);
        BufferedWriter out = IOUtils.getBufferedWriter(outFilename);
        for (StringBuilder sb : sbList) {
            IOUtils.writeToBufferedWriter(out, sb.toString(), true);
        }
        IOUtils.closeBufferedWriter(out);
    }

    /** Performs a transpose using a more efficient means. */
    public static void performTranspose(String inFilename, String outFilename) {
        BufferedReader in = IOUtils.getBufferedReader(inFilename);
        ArrayList<String> tempFilenames = new ArrayList<String>();
        int tempFilenameSuffixCounter = 1000;
        String tempFilenamePrefix = inFilename + "_aIxuT93x0dwe_";
        String line;
        int lineCounter = -1;
        while ((line = IOUtils.getNextLineInBufferedReader(in)) != null) {
            String tempOutFilename = tempFilenamePrefix + ++tempFilenameSuffixCounter + ".txt";
            BufferedWriter out = IOUtils.getBufferedWriter(tempOutFilename);
            IOUtils.writeToBufferedWriter(out, line.split("\\t"));
            IOUtils.closeBufferedWriter(out);
            tempFilenames.add(tempOutFilename);
        }
        PasteFiles.main(PasteFiles.constructArgs(tempFilenames, 0, outFilename));
        IOUtils.deleteAllFiles(tempFilenames);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java TransposeFile inFilename outFilename");
        } else {
            performTranspose_FewColumns(args[0], args[1]);
        }
    }
}
