package org.omegat.core.threads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.omegat.core.Core;
import org.omegat.core.data.IProject;
import org.omegat.core.data.ParseEntry;
import org.omegat.core.data.ProjectProperties;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.data.TransEntry;
import org.omegat.core.data.TransMemory;
import org.omegat.filters2.IParseCallback;
import org.omegat.filters2.TranslationException;
import org.omegat.filters2.master.FilterMaster;
import org.omegat.gui.search.SearchWindow;
import org.omegat.util.Log;
import org.omegat.util.OConsts;
import org.omegat.util.OStrings;
import org.omegat.util.StaticUtils;

/**
 * Each search window has its own search thread to actually do the
 * searching.
 * This prevents lockup of the UI during intensive searches
 *
 * @author Keith Godfrey
 * @author Henry Pijffers
 * @author Didier Briel
 * @author Martin Fleurke
 * @author Antonio Vilei
 */
public class SearchThread extends Thread {

    public SearchThread(SearchWindow window) {
        m_window = window;
        m_searchDir = null;
        m_searchRecursive = false;
        m_searching = false;
        m_tmSearch = false;
        m_entrySet = null;
        m_numFinds = 0;
        m_curFileName = "";
    }

    /**
     * Starts a search if another is not currently running.
     * To search current project only, set rootDir to null.
     *
     * @param text string to search for
     * @param rootDir folder to search in
     * @param recursive search in subfolders of rootDir too
     * @param exact search for a substring, including wildcards (*?)
     * @param keyword search for keywords, including wildcards (*?)
     * @param regex search based on regular expressions
     * @param caseSensitive search case sensitive
     * @param tm search in legacy and orphan TM strings too
     * @param allResults
     * @param searchSource search in source text
     * @param searchTarget search in target text
     * @param searchAuthor search for tmx segments modified by author id/name
     * @param author string to search for in TMX attribute modificationId
     * @param searchDateAfter search for translation segments modified after the given date
     * @param dateAfter the date after which the modification date has to be
     * @param searchDateBefore search for translation segments modified before the given date
     * @param dateBefore the date before which the modification date has to be
     * @internal The main loop (in the run method) waits for the variable 
     *           m_searching to be set to true. This variable is set to true
     *           in this function on successful setting of the search parameters.
     */
    public void requestSearch(String text, String rootDir, boolean recursive, boolean exact, boolean keyword, boolean regex, boolean caseSensitive, boolean tm, boolean allResults, boolean searchSource, boolean searchTarget, boolean searchAuthor, String author, boolean searchDateAfter, long dateAfter, boolean searchDateBefore, long dateBefore) {
        if (!m_searching) {
            m_searchDir = rootDir;
            m_searchRecursive = recursive;
            m_tmSearch = tm;
            m_allResults = allResults;
            m_searchSource = searchSource;
            m_searchTarget = searchTarget;
            m_searchAuthor = searchAuthor;
            m_searchDateAfter = searchDateAfter;
            m_searchDateBefore = searchDateBefore;
            m_entrySet = new HashSet<String>();
            m_matchers = new ArrayList<Matcher>();
            int flags = caseSensitive ? 0 : Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE;
            try {
                if (exact) {
                    text = StaticUtils.escapeNonRegex(text, false);
                    m_matchers.add(Pattern.compile(text, flags).matcher(""));
                } else if (regex) {
                    m_matchers.add(Pattern.compile(text, flags).matcher(""));
                } else {
                    text = text.trim();
                    if (text.length() > 0) {
                        int wordStart = 0;
                        while (wordStart < text.length()) {
                            int spacePos = text.indexOf(' ', wordStart);
                            String word = (spacePos == -1) ? text.substring(wordStart, text.length()).trim() : text.substring(wordStart, spacePos).trim();
                            if (word.length() > 0) {
                                if (!regex) word = StaticUtils.escapeNonRegex(word, false);
                                m_matchers.add(Pattern.compile(word, flags).matcher(""));
                            }
                            wordStart = (spacePos == -1) ? text.length() : spacePos + 1;
                        }
                    }
                }
                if (!regex) author = StaticUtils.escapeNonRegex(author, false);
                m_author = Pattern.compile(author, flags).matcher("");
            } catch (PatternSyntaxException e) {
                m_window.displayErrorRB(e, "ST_REGEXP_ERROR");
                m_window.setSearchControlFocus();
            }
            m_dateBefore = dateBefore;
            m_dateAfter = dateAfter;
            m_searching = true;
        }
    }

    @Override
    public void run() {
        boolean firstPass = true;
        setPriority(Thread.MIN_PRIORITY);
        try {
            while (!interrupted()) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    interrupt();
                }
                if (firstPass) {
                    firstPass = false;
                    m_window.setSearchControlFocus();
                }
                if (m_searching) {
                    if (m_searchDir == null) {
                        searchProject();
                    } else {
                        try {
                            searchFiles();
                        } catch (IOException e) {
                            Log.logErrorRB(e, "ST_FILE_SEARCH_ERROR");
                            Core.getMainWindow().displayErrorRB(e, "ST_FILE_SEARCH_ERROR");
                        } catch (TranslationException te) {
                            Log.logErrorRB(te, "ST_FILE_SEARCH_ERROR");
                            Core.getMainWindow().displayErrorRB(te, "ST_FILE_SEARCH_ERROR");
                        }
                    }
                    if (m_numFinds == 0) {
                        m_window.postMessage(OStrings.getString("ST_NOTHING_FOUND"));
                    }
                    m_window.displayResults();
                    m_searching = false;
                    m_entrySet = null;
                }
            }
        } catch (RuntimeException re) {
            Log.logErrorRB(re, "ST_FATAL_ERROR");
            Core.getMainWindow().displayErrorRB(re, "ST_FATAL_ERROR");
            m_window.threadDied();
        }
    }

    /**
     * Queue found string.
     * Removes duplicate segments (by Henry Pijffers)
     * except if m_allResults = true
     */
    private void foundString(int entryNum, String intro, String src, String target) {
        if (m_numFinds++ > OConsts.ST_MAX_SEARCH_RESULTS) {
            return;
        }
        if (entryNum >= 0) {
            if (!m_entrySet.contains(src + target) || m_allResults) {
                m_window.addEntry(entryNum + 1, null, (entryNum + 1) + "> " + src, target);
                if (!m_allResults) m_entrySet.add(src + target);
            }
        } else {
            m_window.addEntry(entryNum, intro, src, target);
        }
        if (m_numFinds >= OConsts.ST_MAX_SEARCH_RESULTS) {
            m_window.postMessage(StaticUtils.format(OStrings.getString("SW_MAX_FINDS_REACHED"), new Object[] { new Integer(OConsts.ST_MAX_SEARCH_RESULTS) }));
        }
    }

    private void searchProject() {
        IProject project = Core.getProject();
        m_numFinds = 0;
        IProject dataEngine = Core.getProject();
        for (int i = 0; i < project.getAllEntries().size(); i++) {
            SourceTextEntry ste = dataEngine.getAllEntries().get(i);
            String srcText = ste.getSrcText();
            TransEntry te = Core.getProject().getTranslation(ste);
            String locText = te != null ? te.translation : "";
            if (((m_searchSource && searchString(srcText)) || (m_searchTarget && searchString(locText))) && (!m_searchAuthor || te != null && searchAuthor(te)) && (!m_searchDateBefore || te != null && te.changeDate != 0 && te.changeDate < m_dateBefore) && (!m_searchDateAfter || te != null && te.changeDate != 0 && te.changeDate > m_dateAfter)) {
                foundString(i, null, srcText, locText);
            }
            if (m_numFinds >= OConsts.ST_MAX_SEARCH_RESULTS) break;
        }
        if (m_tmSearch) {
            String file = OStrings.getString("CT_ORPHAN_STRINGS");
            for (Map.Entry<String, TransEntry> en : Core.getProject().getOrphanedSegments().entrySet()) {
                String srcText = en.getKey();
                TransEntry te = en.getValue();
                if (((m_searchSource && searchString(srcText)) || (m_searchTarget && searchString(te.translation))) && (!m_searchAuthor || searchAuthor(te)) && (!m_searchDateBefore || te.changeDate != 0 && te.changeDate < m_dateBefore) && (!m_searchDateAfter || te.changeDate != 0 && te.changeDate > m_dateAfter)) {
                    foundString(-1, file, srcText, te.translation);
                    if (m_numFinds >= OConsts.ST_MAX_SEARCH_RESULTS) {
                        break;
                    }
                }
            }
            if (!m_searchAuthor && !m_searchDateAfter && !m_searchDateBefore) {
                for (Map.Entry<String, List<TransMemory>> tmEn : Core.getProject().getTransMemories().entrySet()) {
                    file = tmEn.getKey();
                    for (TransMemory tm : tmEn.getValue()) {
                        String srcText = tm.source;
                        String locText = tm.target;
                        if ((m_searchSource && searchString(srcText)) || (m_searchTarget && searchString(locText))) {
                            foundString(-1, file, srcText, locText);
                            if (m_numFinds >= OConsts.ST_MAX_SEARCH_RESULTS) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void searchFiles() throws IOException, TranslationException {
        List<String> fileList = new ArrayList<String>(256);
        if (!m_searchDir.endsWith(File.separator)) m_searchDir += File.separator;
        StaticUtils.buildFileList(fileList, new File(m_searchDir), m_searchRecursive);
        FilterMaster fm = FilterMaster.getInstance();
        for (String filename : fileList) {
            m_curFileName = filename.substring(m_searchDir.length());
            fm.loadFile(filename, new SearchCallback(Core.getProject().getProjectProperties()) {

                protected void addSegment(String id, short segmentIndex, String segmentSource, String segmentTranslation, String comment) {
                    searchText(segmentSource);
                }

                public void addFileTMXEntry(String source, String translation) {
                }
            });
        }
    }

    protected abstract class SearchCallback extends ParseEntry implements IParseCallback {

        public SearchCallback(ProjectProperties config) {
            super(config);
        }
    }

    /**
      * Looks for an occurrence of the search string(s) in the supplied text string.
      *
      * @param text   The text string to search in
      *
      * @return True if the text string contains all search strings
      *
      * @author Henry Pijffers (henry.pijffers@saxnot.com)
      */
    private boolean searchString(String text) {
        if (text == null || m_matchers == null || m_matchers.isEmpty()) return false;
        for (Matcher matcher : m_matchers) {
            matcher.reset(text);
            if (!matcher.find()) return false;
        }
        return true;
    }

    /**
     * Looks for an occurrence of the author search string in the supplied text string.
     *
     * @param author The text string to search in
     *
     * @return True if the text string contains the search string
     */
    private boolean searchAuthor(TransEntry te) {
        if (te == null || m_author == null) return false;
        String author = te.changeId;
        if (author == null) return false;
        m_author.reset(author);
        if (!m_author.find()) return false;
        return true;
    }

    public void searchText(String seg) {
        if (m_numFinds >= OConsts.ST_MAX_SEARCH_RESULTS) return;
        if (searchString(seg)) {
            foundString(-1, m_curFileName, seg, null);
        }
    }

    private SearchWindow m_window;

    private boolean m_searching;

    private String m_searchDir;

    private boolean m_searchRecursive;

    private String m_curFileName;

    private boolean m_tmSearch;

    private boolean m_allResults;

    private boolean m_searchSource;

    private boolean m_searchTarget;

    private boolean m_searchAuthor;

    private boolean m_searchDateAfter;

    private boolean m_searchDateBefore;

    private Set<String> m_entrySet;

    private List<Matcher> m_matchers;

    private Matcher m_author;

    private long m_dateBefore;

    private long m_dateAfter;

    private int m_numFinds;
}
