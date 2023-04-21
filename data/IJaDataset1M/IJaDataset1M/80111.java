package collector;

import gui.LongTask;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import parser.Main;

/**
 * The Collector is responsible for parsing and comparing all the code.  The result will be an object
 * model which can be displayed on the GUI or console.
 *
 * @author dotekien
 */
public class Collector {

    public enum DebugReport {

        FILE_LIST, STATEMENT_GROUP_LIST, PREPARING, MATRIX, CHAIN, MEMORY
    }

    private static final HashSet<DebugReport> s_debugOptions = new HashSet<DebugReport>();

    public static boolean isDebug(DebugReport option) {
        return s_debugOptions.contains(option);
    }

    public static boolean setDebug(DebugReport option, boolean value) {
        if (value) return s_debugOptions.add(option);
        return s_debugOptions.remove(option);
    }

    private final LongTask report;

    private final ArrayList<FileObj> files;

    private final HashMap<FileObj, Integer> file_startPoint = new HashMap<FileObj, Integer>();

    private final HashMap<Integer, FileObj> startPoint_fID = new HashMap<Integer, FileObj>();

    private Integer[] startPoints;

    private Matrix matrix;

    private int projectNumOfStatement;

    private final ArrayList<Chain> chains;

    private final HashMap<StatementGrouping, StatementGroup> statementGroups = new HashMap<StatementGrouping, StatementGroup>();

    private final Configuration configuration;

    public Collector(LongTask report, ArrayList<FileObj> files, Configuration configuration) {
        this.report = report;
        this.files = files;
        chains = new ArrayList<Chain>();
        for (StatementGrouping grouping : StatementGrouping.values()) {
            statementGroups.put(grouping, new StatementGroup(grouping));
        }
        this.configuration = configuration;
    }

    /**
     * Once the collector has been constructed, this method can be called to start the analysis.
     */
    public void findDuplicatedCode() {
        progress("Parsing " + files.size() + " files", 25);
        makeInvertedList();
        if (isDebug(DebugReport.FILE_LIST)) {
            testStateGroupList();
        }
        progress("Comparing", 50);
        prepare();
        if (isDebug(DebugReport.PREPARING)) {
            testPreparationStep();
        }
        memory();
        configuration.info("Statements: " + projectNumOfStatement);
        matrix = new Matrix(projectNumOfStatement);
        for (StatementGroup group : statementGroups.values()) {
            group.runCompare(this);
        }
        if (isDebug(DebugReport.MATRIX)) {
            printMatrix();
        }
        progress("Collecting", 60);
        collectChain();
        if (isDebug(DebugReport.CHAIN)) {
            reportChain();
        }
        progress("Done", 100);
    }

    private void memory() {
        if (isDebug(DebugReport.MEMORY)) {
            configuration.info("memory: " + Runtime.getRuntime().totalMemory());
        }
    }

    private void progress(final String s, final int i) {
        final String message = s + "....(" + i + "%)";
        if (configuration.isGui()) {
            report.setStatMessage(message);
            report.setCurrent(i);
        }
        configuration.info(message);
        memory();
    }

    private void reportChain() {
        Chain[] sortedChains = new Chain[chains.size()];
        chains.toArray(sortedChains);
        Arrays.sort(sortedChains, Chain.NumOfChunkIncrease);
        for (Chain chain : sortedChains) {
            configuration.info(chain.toString());
        }
        System.out.println("Number of unique chain:" + chains.size());
    }

    public ArrayList<Chain> getChains() {
        return chains;
    }

    private void testStateGroupList() {
        System.out.println("...statement group list");
        for (FileObj f : files) {
            configuration.info(f.toString());
        }
    }

    private void testPreparationStep() {
        configuration.info("...file ID - start point");
        for (FileObj fid : file_startPoint.keySet()) {
            configuration.info(fid + ": " + file_startPoint.get(fid));
        }
        configuration.info("...start points - file ID");
        for (Integer sp : startPoint_fID.keySet()) {
            configuration.info(sp + ": " + startPoint_fID.get(sp));
        }
        configuration.info("...sorted start points");
        for (Integer sp : startPoints) {
            System.out.print(sp + "; ");
        }
        configuration.info("...get random index on Matrix and then print out the statement");
        int i = new Random().nextInt(startPoints[startPoints.length - 1]);
        Statement s = getStatementOnMatrixAt(i);
        configuration.info("index: " + i);
        configuration.info("file: " + s.getFile());
        configuration.info("state ID: " + s.getId());
        configuration.info("string form: " + s.toString());
        configuration.info("file holding this state: " + s.getFile().getPath());
    }

    /**
     * parse all of the files
     */
    private void makeInvertedList() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Collection<Callable<Void>> tasks = new ArrayList<Callable<Void>>(files.size());
        for (final FileObj fob : files) {
            tasks.add(new Callable<Void>() {

                public Void call() throws Exception {
                    Main.parseFile(Collector.this, fob);
                    Statement.resetNumOfStat();
                    return null;
                }
            });
        }
        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (isDebug(DebugReport.STATEMENT_GROUP_LIST)) {
            for (StatementGrouping sg : StatementGrouping.values()) {
                configuration.info(sg.toString());
            }
        }
    }

    private void prepare() {
        ArrayList<Integer> st = new ArrayList<Integer>();
        int startpoint = 0;
        int fileNumOfStatement;
        for (FileObj f : files) {
            file_startPoint.put(f, startpoint);
            startPoint_fID.put(startpoint, f);
            st.add(startpoint);
            fileNumOfStatement = f.getNumberOfStatement();
            startpoint += fileNumOfStatement;
        }
        startPoints = new Integer[st.size()];
        st.toArray(startPoints);
        Arrays.sort(startPoints);
        projectNumOfStatement = startpoint;
    }

    public Statement getStatementOnMatrixAt(int index) {
        FileObj f = getFileForIndex(index);
        return f.getStatement(index - file_startPoint.get(f));
    }

    public FileObj getFileForIndex(int index) {
        int i = Arrays.binarySearch(startPoints, index);
        int spi;
        if (i < 0) {
            spi = -i - 1 - 1;
        } else {
            spi = i;
        }
        int startPoint = startPoints[spi];
        return startPoint_fID.get(startPoint);
    }

    public int getIndexForStat(Statement s) {
        return file_startPoint.get(s.getFile()) + s.getId();
    }

    public void setTrueToCell(int line, int column) {
        matrix.setTrueToCell(line, column);
    }

    private void printMatrix() {
        configuration.info("\n...matrix report");
        int dim = matrix.getDimension();
        configuration.info("dim: " + dim);
        for (int i = 0; i < dim; i++) {
            if (i != 0 && file_startPoint.containsValue(i)) {
                for (int j = 0; j < dim + files.size() + 3; j++) {
                    System.out.print("*");
                }
                configuration.info("");
            }
            StringBuffer blank = new StringBuffer();
            for (int k = 0; k < Integer.toString(dim).length() - Integer.toString(i).length(); k++) {
                blank.append(" ");
            }
            System.out.print(i + blank.toString());
            for (int j = 0; j < dim; j++) {
                if (j != 0 && file_startPoint.containsValue(j)) {
                    System.out.print("|");
                }
                if (i >= j) {
                    System.out.print(" ");
                } else {
                    if (matrix.getCellAt(i, j)) {
                        System.out.print("0");
                    } else {
                        System.out.print("-");
                    }
                }
            }
            configuration.info("*");
        }
    }

    private void collectChain() {
        for (int line = 0; line < projectNumOfStatement; line++) {
            for (int column = line + 1; column < projectNumOfStatement; column++) {
                if (matrix.getCellAt(line, column)) {
                    ArrayList<Cell> indices = new ArrayList<Cell>();
                    int maxLine = getMaxLine(line);
                    int maxCol = getMaxCol(column);
                    int x = line;
                    int y = column;
                    do {
                        indices.add(new Cell(x, y));
                        matrix.setFalseToCell(x, y);
                        x++;
                        y++;
                    } while (matrix.getCellAt(x, y) && y < maxCol && x < maxLine);
                    if (indices.size() >= configuration.minLengthForChunk) {
                        decideToMakeChunkAndChain(indices);
                    }
                }
            }
        }
    }

    public void decideToMakeChunkAndChain(ArrayList<Cell> indices) {
        int newChunk = 0;
        int length = indices.size();
        int spLine = indices.get(0).getLine();
        int spColumn = indices.get(0).getColumn();
        Chunk oldChunk = null;
        Chunk newChunk1 = null;
        Chunk newChunk2 = null;
        ArrayList<Chunk> chunk = new ArrayList<Chunk>();
        if (!isExist(spLine, length, chunk)) {
            newChunk1 = chunk.get(0);
            newChunk++;
        } else {
            oldChunk = chunk.get(0);
        }
        chunk.remove(0);
        if (!isExist(spColumn, length, chunk)) {
            newChunk2 = chunk.get(0);
            newChunk++;
        } else {
            oldChunk = chunk.get(0);
        }
        switch(newChunk) {
            case 2:
                Chain chain = new Chain(length, chains.size());
                chains.add(chain);
                chain.addChunk(newChunk1);
                chain.addChunk(newChunk2);
                chain.gatherStandardizingText();
                break;
            case 1:
                assert oldChunk != null;
                Chain oldChain = oldChunk.getChain();
                if (newChunk1 != null) {
                    oldChain.addChunk(newChunk1);
                } else {
                    oldChain.addChunk(newChunk2);
                }
                break;
            default:
                break;
        }
    }

    private boolean isExist(int spIndex, int lengthOfChunk, ArrayList<Chunk> chunkWrapper) {
        boolean isExist = false;
        FileObj file = getFileForIndex(spIndex);
        for (Chunk c : file.getChunks()) {
            if (spIndex == c.getStartIndexOnMatrix()) {
                if (c.getChain() != null && lengthOfChunk == c.getLength()) {
                    isExist = true;
                    chunkWrapper.add(c);
                    break;
                }
            }
        }
        if (!isExist) {
            Statement first = getStatementOnMatrixAt(spIndex);
            Statement last = getStatementOnMatrixAt(spIndex + lengthOfChunk - 1);
            Chunk c = new Chunk(file, first, last, spIndex);
            chunkWrapper.add(c);
        }
        return isExist;
    }

    public int getMaxLine(int line) {
        return getUpper(line);
    }

    public int getMaxCol(int column) {
        return getUpper(column);
    }

    private int getUpper(int index) {
        int i = Arrays.binarySearch(startPoints, index);
        int spi;
        if (i < 0) {
            spi = -i - 1;
        } else {
            spi = i + 1;
        }
        if (spi < startPoints.length) {
            return startPoints[spi];
        }
        FileObj file = startPoint_fID.get(startPoints[spi - 1]);
        return startPoints[spi - 1] + file.getNumberOfStatement();
    }

    public int getNumberOfPhysicalLines() {
        int lc = 0;
        for (FileObj file : files) {
            lc += file.lineCount;
        }
        return lc;
    }

    public int getProjectNumOfStatement() {
        return projectNumOfStatement;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(files);
        return sb.toString().intern();
    }

    /**
     * This is a call back from the AST parser to insert statements.
     *
     * @param group statementgroup
     * @param s     statement to add to group
     */
    public void insertStatement(final StatementGrouping group, final Statement s) {
        statementGroups.get(group).insertStatement(s);
    }
}
