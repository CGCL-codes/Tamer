package same.alg;

import java.util.Vector;
import java.util.Enumeration;
import java.io.IOException;
import same.util.StringHashComputer;
import same.filter.SourceFilterFactory;

public class Same implements SameHashSummary.IListener {

    public interface IListener {

        void onStart();

        void onStartPreprocessFile(String a_fileName);

        void onPotentialMatchFound(FileLine a_fileLine1, FileLine a_fileLine2);

        void onErrorPreprocessFile(String a_fileName, Throwable a_exception);

        void onEndPreprocessFile(String a_fileName);

        void onEndPreprocess();

        void onStartMatches();

        void onTryMatch(FileLine a_fileLine1, FileLine a_fileLine2);

        void onFoundMatch(Match a_match);

        void onNoMatch(FileLine a_fileLine1, FileLine a_fileLine2);

        void onErrorMatch(FileLine a_fileLine1, FileLine a_fileLine2, Throwable a_exception);

        void onEndMatches();

        void onErrorCleanUp(Throwable a_exception);

        void onEnd();
    }

    private IListener m_listener;

    private final Vector m_fileNames = new Vector();

    private int m_fragmentSize;

    private String m_filterType;

    private ITextFileSystem m_fileSystem;

    private SameHashFinder m_dhlf;

    private SameHashSummary m_dhls;

    private int m_numberOfFiles;

    private int m_totalLines;

    private int m_relevantLines;

    private int m_numberOfComparisons;

    private int m_numberOfPotentialMatches;

    private Vector m_potentialMatches;

    private Vector m_matches;

    private Vector m_equivalentFileLines;

    private Vector m_equivalentMatches;

    public Same(IListener a_listener) {
        m_listener = a_listener;
        m_fragmentSize = 10;
        m_filterType = null;
    }

    public void setFragmentSize(int a_fragmentSize) {
        m_fragmentSize = a_fragmentSize;
    }

    public void setFilterType(String a_filterType) {
        m_filterType = a_filterType;
    }

    public String getFilterType() {
        return m_filterType;
    }

    public void setFileSystem(ITextFileSystem a_fileSystem) {
        m_fileSystem = a_fileSystem;
    }

    public void addFile(String a_fileName) {
        m_fileNames.addElement(a_fileName);
    }

    public boolean hasFiles() {
        return !m_fileNames.isEmpty();
    }

    public void run() {
        m_listener.onStart();
        initialize();
        processAllFiles();
        selectAndPrintRealMatches();
        computeEquivalentMatches();
        m_listener.onEnd();
    }

    public void onPotentialMatchFound(FileLine a_fileLine1, FileLine a_fileLine2) {
        m_numberOfPotentialMatches++;
        m_listener.onPotentialMatchFound(a_fileLine1, a_fileLine2);
    }

    private void initialize() {
        m_dhls = new SameHashSummary(this);
        m_dhlf = new SameHashFinder(m_fragmentSize, m_dhls, SourceFilterFactory.instance(), m_filterType, StringHashComputer.instance());
        m_totalLines = 0;
        m_relevantLines = 0;
        m_numberOfFiles = 0;
    }

    private void processAllFiles() {
        m_numberOfPotentialMatches = 0;
        for (Enumeration e = m_fileNames.elements(); e.hasMoreElements(); ) {
            String fileName = (String) e.nextElement();
            processFile(fileName);
        }
        m_potentialMatches = m_dhls.getPotentialMatches();
        m_dhls = null;
        m_listener.onEndPreprocess();
    }

    private void processFile(String a_fileName) {
        m_listener.onStartPreprocessFile(a_fileName);
        try {
            ITextReader reader = m_fileSystem.getReader(new FileLine(a_fileName, 1), '1');
            m_dhlf.startFile(a_fileName);
            while (true) {
                String line = reader.readLineFiltered();
                if (line == null) {
                    break;
                }
                m_dhlf.addLine(line);
                m_totalLines++;
                if (line.length() != 0) {
                    m_relevantLines++;
                }
            }
            m_fileSystem.returnReader(a_fileName, '1', reader);
            m_numberOfFiles++;
            m_listener.onEndPreprocessFile(a_fileName);
        } catch (IOException ex) {
            m_listener.onErrorPreprocessFile(a_fileName, ex);
        }
    }

    public int getNumberOfPotentialMatches() {
        return m_numberOfPotentialMatches;
    }

    private void selectAndPrintRealMatches() {
        m_listener.onStartMatches();
        ISourceFilter filter = SourceFilterFactory.instance().createFilter(m_filterType);
        m_matches = new Vector();
        m_numberOfComparisons = 0;
        for (Enumeration e = m_potentialMatches.elements(); e.hasMoreElements(); ) {
            Vector startingPoints = (Vector) e.nextElement();
            for (int j = 1; j < startingPoints.size(); j++) {
                FileLine fileLine2 = (FileLine) startingPoints.elementAt(j);
                for (int i = 0; i < j; i++) {
                    FileLine fileLine1 = (FileLine) startingPoints.elementAt(i);
                    if (isPartOfExistingMatch(fileLine1, fileLine2)) {
                        continue;
                    }
                    m_listener.onTryMatch(fileLine1, fileLine2);
                    MatchFinder mf = new MatchFinder(fileLine1, fileLine2, m_fileSystem);
                    try {
                        mf.run();
                    } catch (IOException ex) {
                        m_listener.onErrorMatch(fileLine1, fileLine2, ex);
                    }
                    Match match = mf.getResult();
                    if (match.getLength() >= m_fragmentSize) {
                        m_matches.addElement(match);
                        m_listener.onFoundMatch(match);
                        match.clearFragments();
                    } else {
                        m_listener.onNoMatch(fileLine1, fileLine2);
                    }
                    m_numberOfComparisons++;
                }
            }
        }
        try {
            m_fileSystem.disconnect();
        } catch (IOException ex) {
            m_listener.onErrorCleanUp(ex);
        }
        m_listener.onEndMatches();
    }

    private boolean isPartOfExistingMatch(FileLine a_fileLine1, FileLine a_fileLine2) {
        for (Enumeration e = m_matches.elements(); e.hasMoreElements(); ) {
            Match existingMatch = (Match) e.nextElement();
            if (existingMatch.contains(a_fileLine1, a_fileLine2)) {
                return true;
            }
        }
        return false;
    }

    /** This uses the m_matches list to compute some useful statistics.
     */
    private void computeEquivalentMatches() {
        m_equivalentFileLines = new Vector();
        m_equivalentMatches = new Vector();
        for (Enumeration e = m_matches.elements(); e.hasMoreElements(); ) {
            Match match = (Match) e.nextElement();
            addToEquivalentMatches(match);
        }
    }

    private void addToEquivalentMatches(Match a_match) {
        int length = a_match.getLength();
        FileLine fileLine1 = a_match.getFragment1().getStartingPoint();
        FileLine fileLine2 = a_match.getFragment2().getStartingPoint();
        Enumeration ee = getEquivalentMatches();
        for (Enumeration e = getEquivalentFileLines(); e.hasMoreElements(); ) {
            Vector fileLines = (Vector) e.nextElement();
            Match match = (Match) ee.nextElement();
            if (match.getLength() != length) {
                continue;
            }
            boolean fileLine1found = false;
            boolean fileLine2found = false;
            for (Enumeration f = fileLines.elements(); f.hasMoreElements(); ) {
                FileLine fileLine = (FileLine) f.nextElement();
                if (fileLine.equals(fileLine1)) {
                    fileLine1found = true;
                }
                if (fileLine.equals(fileLine2)) {
                    fileLine2found = true;
                }
            }
            if (fileLine1found && fileLine2found) {
                return;
            }
            if (fileLine1found || fileLine2found) {
                FileLine fileLineToBeAdded = (fileLine1found ? fileLine2 : fileLine1);
                fileLines.addElement(fileLineToBeAdded);
                return;
            }
        }
        Vector v = new Vector();
        v.addElement(fileLine1);
        v.addElement(fileLine2);
        m_equivalentFileLines.addElement(v);
        m_equivalentMatches.addElement(a_match);
    }

    /** For each set of matching fragments, returns an arbitrary Match.
     */
    public Enumeration getEquivalentMatches() {
        return m_equivalentMatches.elements();
    }

    public Enumeration getEquivalentFileLines() {
        return m_equivalentFileLines.elements();
    }

    public int getNumberOfFiles() {
        return m_numberOfFiles;
    }

    public int getTotalNumberOfLines() {
        return m_totalLines;
    }

    public int getRelevantNumberOfLines() {
        return m_relevantLines;
    }

    public int getNumberOfEquivalentMatches() {
        return m_equivalentMatches.size();
    }

    public int getNumberOfComparisons() {
        return m_numberOfComparisons;
    }

    public int getNumberOfSuperfluousLines() {
        int result = 0;
        Enumeration ee = m_equivalentMatches.elements();
        for (Enumeration e = m_equivalentFileLines.elements(); e.hasMoreElements(); ) {
            Vector fileLines = (Vector) e.nextElement();
            Match match = (Match) ee.nextElement();
            result = result + (fileLines.size() - 1) * match.getLength();
        }
        return result;
    }
}
