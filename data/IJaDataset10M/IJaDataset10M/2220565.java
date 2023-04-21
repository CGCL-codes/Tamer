package org.omegat.gui.editor;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.data.IProject;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.data.TransEntry;
import org.omegat.core.data.IProject.FileInfo;
import org.omegat.core.data.SM;
import org.omegat.core.events.IEntryEventListener;
import org.omegat.core.events.IFontChangedEventListener;
import org.omegat.core.events.IProjectEventListener;
import org.omegat.core.statistics.StatisticsInfo;
import org.omegat.gui.editor.mark.Mark;
import org.omegat.gui.help.HelpFrame;
import org.omegat.gui.main.DockableScrollPane;
import org.omegat.gui.main.MainWindow;
import org.omegat.util.FileUtil;
import org.omegat.util.Log;
import org.omegat.util.OConsts;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.StaticUtils;
import org.omegat.util.StringUtil;
import org.omegat.util.Token;
import org.omegat.util.gui.UIThreadsUtil;

/**
 * Class for control all editor operations.
 * 
 * You can find good description of java text editor working at
 * http://java.sun.com/products/jfc/tsc/articles/text/overview/
 * 
 * @author Keith Godfrey
 * @author Benjamin Siband
 * @author Maxym Mykhalchuk
 * @author Kim Bruning
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 * @author Zoltan Bartko - bartkozoltan@bartkozoltan.com
 * @author Andrzej Sawula
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Didier Briel
 */
public class EditorController implements IEditor {

    /** Local logger. */
    private static final Logger LOGGER = Logger.getLogger(EditorController.class.getName());

    /** Dockable pane for editor. */
    private final DockableScrollPane pane;

    /** Editor instance. */
    public final EditorTextArea3 editor;

    /** Class for process marks for editor. */
    protected MarkerController markerController;

    private String introPaneTitle, emptyProjectPaneTitle;

    private JTextPane introPane, emptyProjectPane;

    protected final MainWindow mw;

    /** Currently displayed segments info. */
    protected SegmentBuilder[] m_docSegList;

    /** Current displayed file. */
    protected int displayedFileIndex, previousDisplayedFileIndex;

    /** Current active segment in current file, if there are segments in file (can be fale if filter active!)*/
    protected int displayedEntryIndex;

    /** Object which store history of moving by segments. */
    private SegmentHistory history = new SegmentHistory();

    protected final EditorSettings settings;

    protected Font font, fontb, fonti, fontbi;

    private enum SHOW_TYPE {

        INTRO, EMPTY_PROJECT, FIRST_ENTRY, NO_CHANGE
    }

    ;

    Document3.ORIENTATION currentOrientation;

    protected boolean sourceLangIsRTL, targetLangIsRTL;

    private List<Integer> entryFilterList;

    public EditorController(final MainWindow mainWindow) {
        this.mw = mainWindow;
        editor = new EditorTextArea3(this);
        setFont(Core.getMainWindow().getApplicationFont());
        markerController = new MarkerController(this);
        pane = new DockableScrollPane("EDITOR", " ", editor, false);
        pane.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setMinimumSize(new Dimension(100, 100));
        Core.getMainWindow().addDockable(pane);
        settings = new EditorSettings(this);
        CoreEvents.registerProjectChangeListener(new IProjectEventListener() {

            public void onProjectChanged(PROJECT_CHANGE_TYPE eventType) {
                SHOW_TYPE showType;
                switch(eventType) {
                    case CREATE:
                    case LOAD:
                        history.clear();
                        removeFilter();
                        if (!Core.getProject().getAllEntries().isEmpty()) {
                            showType = SHOW_TYPE.FIRST_ENTRY;
                        } else {
                            showType = SHOW_TYPE.EMPTY_PROJECT;
                        }
                        markerController.reset(0);
                        setInitialOrientation();
                        break;
                    case CLOSE:
                        history.clear();
                        removeFilter();
                        markerController.reset(0);
                        showType = SHOW_TYPE.INTRO;
                        deactivateWithoutCommit();
                        break;
                    default:
                        showType = SHOW_TYPE.NO_CHANGE;
                }
                if (showType != SHOW_TYPE.NO_CHANGE) {
                    updateState(showType);
                }
            }
        });
        CoreEvents.registerEntryEventListener(new IEntryEventListener() {

            public void onNewFile(String activeFileName) {
                updateState(SHOW_TYPE.NO_CHANGE);
            }

            public void onEntryActivated(SourceTextEntry newEntry) {
            }
        });
        createAdditionalPanes();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                updateState(SHOW_TYPE.INTRO);
                pane.requestFocus();
            }
        });
        CoreEvents.registerFontChangedEventListener(new IFontChangedEventListener() {

            public void onFontChanged(Font newFont) {
                setFont(newFont);
                ViewLabel.fontHeight = 0;
                editor.revalidate();
                editor.repaint();
                emptyProjectPane.setFont(font);
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, Throwable e) {
                LOGGER.log(Level.SEVERE, "Uncatched exception in thread [" + t.getName() + "]", e);
            }
        });
        EditorPopups.init(this);
    }

    private void updateState(SHOW_TYPE showType) {
        UIThreadsUtil.mustBeSwingThread();
        Component data = null;
        String title = null;
        switch(showType) {
            case INTRO:
                data = introPane;
                title = introPaneTitle;
                break;
            case EMPTY_PROJECT:
                data = emptyProjectPane;
                title = emptyProjectPaneTitle;
                break;
            case FIRST_ENTRY:
                displayedFileIndex = 0;
                displayedEntryIndex = 0;
                title = StaticUtils.format(OStrings.getString("GUI_SUBWINDOWTITLE_Editor"), getCurrentFile());
                data = editor;
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        loadDocument();
                        activateEntry();
                    }
                });
                break;
            case NO_CHANGE:
                title = StaticUtils.format(OStrings.getString("GUI_SUBWINDOWTITLE_Editor"), getCurrentFile());
                data = editor;
                break;
        }
        pane.setName(title);
        if (pane.getViewport().getView() != data) {
            pane.setViewportView(data);
        }
    }

    private void setFont(final Font font) {
        this.font = font;
        this.fontb = new Font(font.getFontName(), Font.BOLD, font.getSize());
        this.fonti = new Font(font.getFontName(), Font.ITALIC, font.getSize());
        this.fontbi = new Font(font.getFontName(), Font.BOLD | Font.ITALIC, font.getSize());
        editor.setFont(font);
    }

    /**
     * Decide what document orientation should be default for source/target
     * languages.
     */
    private void setInitialOrientation() {
        String sourceLang = Core.getProject().getProjectProperties().getSourceLanguage().getLanguageCode();
        String targetLang = Core.getProject().getProjectProperties().getTargetLanguage().getLanguageCode();
        sourceLangIsRTL = EditorUtils.isRTL(sourceLang);
        targetLangIsRTL = EditorUtils.isRTL(targetLang);
        if (sourceLangIsRTL != targetLangIsRTL) {
            currentOrientation = Document3.ORIENTATION.DIFFER;
        } else {
            if (sourceLangIsRTL) {
                currentOrientation = Document3.ORIENTATION.RTL;
            } else {
                currentOrientation = Document3.ORIENTATION.LTR;
            }
        }
        applyOrientationToEditor();
    }

    /**
     * Define editor's orientation by target language orientation.
     */
    private void applyOrientationToEditor() {
        ComponentOrientation targetOrientation = null;
        switch(currentOrientation) {
            case LTR:
                targetOrientation = ComponentOrientation.LEFT_TO_RIGHT;
                break;
            case RTL:
                targetOrientation = ComponentOrientation.RIGHT_TO_LEFT;
                break;
            case DIFFER:
                if (targetLangIsRTL) {
                    targetOrientation = ComponentOrientation.RIGHT_TO_LEFT;
                } else {
                    targetOrientation = ComponentOrientation.LEFT_TO_RIGHT;
                }
        }
        editor.setComponentOrientation(targetOrientation);
    }

    /**
     * Toggle component orientation: LTR, RTL, language dependent.
     */
    protected void toggleOrientation() {
        Document3.ORIENTATION newOrientation = currentOrientation;
        switch(currentOrientation) {
            case LTR:
                newOrientation = Document3.ORIENTATION.RTL;
                break;
            case RTL:
                if (sourceLangIsRTL != targetLangIsRTL) {
                    newOrientation = Document3.ORIENTATION.DIFFER;
                } else {
                    newOrientation = Document3.ORIENTATION.LTR;
                }
                break;
            case DIFFER:
                newOrientation = Document3.ORIENTATION.LTR;
                break;
        }
        LOGGER.info("Switch document orientation from " + currentOrientation + " to " + newOrientation);
        currentOrientation = newOrientation;
        applyOrientationToEditor();
        int activeSegment = displayedEntryIndex;
        loadDocument();
        displayedEntryIndex = activeSegment;
        activateEntry();
    }

    /**
     * {@inheritDoc}
     */
    public void requestFocus() {
        pane.getViewport().getView().requestFocusInWindow();
    }

    /**
     * {@inheritDoc}
     */
    public SourceTextEntry getCurrentEntry() {
        if (m_docSegList == null || displayedEntryIndex < 0 || m_docSegList.length <= displayedEntryIndex) {
            return null;
        }
        if (m_docSegList[displayedEntryIndex] == null) {
            return null;
        }
        return m_docSegList[displayedEntryIndex].ste;
    }

    /**
     * {@inheritDoc}
     */
    public String getCurrentFile() {
        if (Core.getProject().getProjectFiles().isEmpty()) {
            return null;
        }
        if (displayedFileIndex < Core.getProject().getProjectFiles().size()) {
            return Core.getProject().getProjectFiles().get(displayedFileIndex).filePath;
        } else {
            return null;
        }
    }

    /**
     * Displays all segments in current document.
     * <p>
     * Displays translation for each segment if it's available, otherwise
     * displays source text. Also stores length of each displayed segment plus
     * its starting offset.
     */
    protected void loadDocument() {
        UIThreadsUtil.mustBeSwingThread();
        IProject.FileInfo file = Core.getProject().getProjectFiles().get(displayedFileIndex);
        Document3 doc = new Document3(this);
        ArrayList<SegmentBuilder> temp_docSegList2 = new ArrayList<SegmentBuilder>(file.entries.size());
        for (int i = 0; i < file.entries.size(); i++) {
            SourceTextEntry ste = file.entries.get(i);
            if (isInFilter(new Integer(ste.entryNum()))) {
                SegmentBuilder sb = new SegmentBuilder(this, doc, settings, ste, ste.entryNum());
                temp_docSegList2.add(sb);
                sb.createSegmentElement(false);
                SegmentBuilder.addSegmentSeparator(doc);
            }
        }
        m_docSegList = temp_docSegList2.toArray(new SegmentBuilder[temp_docSegList2.size()]);
        doc.setDocumentFilter(new DocumentFilter3());
        Locale targetLocale = Core.getProject().getProjectProperties().getTargetLanguage().getLocale();
        editor.setLocale(targetLocale);
        editor.setDocument(doc);
        doc.addUndoableEditListener(editor.undoManager);
        doc.addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                showLengthMessage();
                onTextChanged();
            }

            public void insertUpdate(DocumentEvent e) {
                showLengthMessage();
                onTextChanged();
            }

            public void removeUpdate(DocumentEvent e) {
                showLengthMessage();
                onTextChanged();
            }
        });
        markerController.reset(m_docSegList.length);
        markerController.process(m_docSegList);
        editor.repaint();
    }

    /**
     * Activates the current entry (if available) by displaying source text and 
     * embedding displayed text in markers.
     * <p>
     * Also moves document focus to current entry, and makes sure fuzzy info
     * displayed if available.
     */
    public void activateEntry() {
        UIThreadsUtil.mustBeSwingThread();
        SourceTextEntry ste = getCurrentEntry();
        if (ste == null) {
            return;
        }
        if (pane.getViewport().getView() != editor) {
            return;
        }
        if (!Core.getProject().isProjectLoaded()) return;
        markerController.resetEntryMarks(displayedEntryIndex);
        m_docSegList[displayedEntryIndex].createSegmentElement(true);
        markerController.process(displayedEntryIndex, m_docSegList[displayedEntryIndex]);
        editor.cancelUndo();
        history.insertNew(m_docSegList[displayedEntryIndex].segmentNumberInProject);
        mw.menu.gotoHistoryBackMenuItem.setEnabled(history.hasPrev());
        mw.menu.gotoHistoryForwardMenuItem.setEnabled(history.hasNext());
        showStat();
        showLengthMessage();
        if (Preferences.isPreference(Preferences.EXPORT_CURRENT_SEGMENT)) {
            exportCurrentSegment(ste);
        }
        scrollForDisplayNearestSegments(editor.getOmDocument().getTranslationStart());
        if (previousDisplayedFileIndex != displayedFileIndex) {
            previousDisplayedFileIndex = displayedFileIndex;
            CoreEvents.fireEntryNewFile(Core.getProject().getProjectFiles().get(displayedFileIndex).filePath);
        }
        editor.repaint();
        CoreEvents.fireEntryActivated(ste);
    }

    /**
     * Display length of source and translation parts in the status bar.
     */
    void showLengthMessage() {
        Document3 doc = editor.getOmDocument();
        String trans = doc.extractTranslation();
        if (trans != null) {
            SourceTextEntry ste = m_docSegList[displayedEntryIndex].ste;
            String lMsg = " " + ste.getSrcText().length() + "/" + trans.length() + " ";
            Core.getMainWindow().showLengthMessage(lMsg);
        }
    }

    /**
     * Called on the text changed in document. Required for recalculate marks
     * for active segment.
     */
    void onTextChanged() {
        Document3 doc = editor.getOmDocument();
        if (doc.isEditMode()) {
            m_docSegList[displayedEntryIndex].onActiveEntryChanged();
            markerController.process(displayedEntryIndex, m_docSegList[displayedEntryIndex]);
        }
    }

    /**
     * Display some segments before and after when user on the top or bottom of
     * page.
     */
    private void scrollForDisplayNearestSegments(final int requiredPosition) {
        int lookNext, lookPrev;
        try {
            SegmentBuilder prev = m_docSegList[displayedEntryIndex - 3];
            lookPrev = prev.getStartPosition();
        } catch (IndexOutOfBoundsException ex) {
            lookPrev = 0;
        }
        try {
            SegmentBuilder next = m_docSegList[displayedEntryIndex + 4];
            lookNext = next.getStartPosition() - 1;
        } catch (IndexOutOfBoundsException ex) {
            lookNext = editor.getOmDocument().getLength();
        }
        final int p = lookPrev;
        final int n = lookNext;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    editor.setCaretPosition(n);
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            try {
                                editor.setCaretPosition(p);
                                SwingUtilities.invokeLater(new Runnable() {

                                    public void run() {
                                        editor.setCaretPosition(requiredPosition);
                                    }
                                });
                            } catch (IllegalArgumentException iae) {
                            }
                        }
                    });
                } catch (IllegalArgumentException iae) {
                }
            }
        });
    }

    /**
     * Export the current source and target segments in text files.
     */
    private void exportCurrentSegment(final SourceTextEntry ste) {
        String s1 = ste.getSrcText();
        TransEntry te = Core.getProject().getTranslation(ste);
        String s2 = te != null ? te.translation : "";
        FileUtil.writeScriptFile(s1, OConsts.SOURCE_EXPORT);
        FileUtil.writeScriptFile(s2, OConsts.TARGET_EXPORT);
    }

    /**
     * Calculate statistic for file, request statistic for project and display
     * in status bar.
     */
    private void showStat() {
        IProject project = Core.getProject();
        IProject.FileInfo fi = project.getProjectFiles().get(displayedFileIndex);
        int translatedInFile = 0;
        for (SourceTextEntry ste : fi.entries) {
            if (project.getTranslation(ste) != null) {
                translatedInFile++;
            }
        }
        StatisticsInfo stat = project.getStatistics();
        String pMsg = " " + Integer.toString(translatedInFile) + "/" + Integer.toString(fi.entries.size()) + " (" + Integer.toString(stat.numberofTranslatedSegments) + "/" + Integer.toString(stat.numberOfUniqueSegments) + ", " + Integer.toString(stat.numberOfSegmentsTotal) + ") ";
        Core.getMainWindow().showProgressMessage(pMsg);
    }

    protected void goToSegmentAtLocation(int location) {
        int segmentAtLocation = getSegmentIndexAtLocation(location);
        if (displayedEntryIndex != segmentAtLocation) {
            commitAndDeactivate();
            displayedEntryIndex = segmentAtLocation;
            activateEntry();
        }
    }

    protected int getSegmentIndexAtLocation(int location) {
        int segmentAtLocation = m_docSegList.length - 1;
        for (int i = 0; i < m_docSegList.length; i++) {
            if (location < m_docSegList[i].getStartPosition()) {
                segmentAtLocation = i - 1;
                break;
            }
        }
        return segmentAtLocation;
    }

    /**
     * Commits the translation. Reads current entry text and commit it to memory
     * if it's changed. Also clears out segment markers while we're at it.
     * <p>
     * Since 1.6: Translation equal to source may be validated as OK translation
     * if appropriate option is set in Workflow options dialog.
     * <p>
     * All displayed segments with the same source text updated also.
     * 
     * @param forceCommit
     *            If false, the translation will not be saved
     */
    public void commitAndDeactivate() {
        UIThreadsUtil.mustBeSwingThread();
        Document3 doc = editor.getOmDocument();
        if (doc == null) {
            return;
        }
        if (!doc.isEditMode()) {
            return;
        }
        markerController.resetEntryMarks(displayedEntryIndex);
        String newTrans = doc.extractTranslation();
        doc.stopEditMode();
        if (newTrans != null) {
            SourceTextEntry entry = m_docSegList[displayedEntryIndex].ste;
            TransEntry oldTE = Core.getProject().getTranslation(entry);
            String old_translation = oldTE != null ? oldTE.translation : "";
            if (newTrans.equals(entry.getSrcText()) && !Preferences.isPreference(Preferences.ALLOW_TRANS_EQUAL_TO_SRC)) {
                Core.getProject().setTranslation(entry, "");
                newTrans = "";
            } else {
                Core.getProject().setTranslation(entry, newTrans);
            }
            m_docSegList[displayedEntryIndex].createSegmentElement(false);
            if (!newTrans.equals(old_translation)) {
                for (int i = 0; i < m_docSegList.length; i++) {
                    if (i == displayedEntryIndex) {
                        continue;
                    }
                    if (m_docSegList[i].ste.getSrcText().equals(entry.getSrcText())) {
                        m_docSegList[i].createSegmentElement(false);
                    }
                }
            }
        }
        markerController.process(displayedEntryIndex, m_docSegList[displayedEntryIndex]);
        editor.cancelUndo();
    }

    /**
     * Deactivate active translation without save. Required on project close
     * postprocessing, for example.
     */
    protected void deactivateWithoutCommit() {
        UIThreadsUtil.mustBeSwingThread();
        Document3 doc = editor.getOmDocument();
        if (doc == null) {
            return;
        }
        doc.stopEditMode();
    }

    /**
     * {@inheritDoc}
     */
    public void commitAndLeave() {
        commitAndDeactivate();
        activateEntry();
    }

    public void nextEntry() {
        UIThreadsUtil.mustBeSwingThread();
        if (!Core.getProject().isProjectLoaded()) return;
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        Cursor oldCursor = this.editor.getCursor();
        this.editor.setCursor(hourglassCursor);
        commitAndDeactivate();
        List<FileInfo> files = Core.getProject().getProjectFiles();
        SourceTextEntry ste;
        int startFileIndex = displayedFileIndex;
        int startEntryIndex = displayedEntryIndex;
        boolean looped = false;
        do {
            displayedEntryIndex++;
            if (displayedEntryIndex >= m_docSegList.length) {
                displayedFileIndex++;
                displayedEntryIndex = 0;
                if (displayedFileIndex >= files.size()) {
                    displayedFileIndex = 0;
                    looped = true;
                }
                loadDocument();
            }
            ste = getCurrentEntry();
        } while (ste == null && (!looped || !(displayedFileIndex == startFileIndex && displayedEntryIndex >= startEntryIndex)));
        activateEntry();
        this.editor.setCursor(oldCursor);
    }

    public void prevEntry() {
        UIThreadsUtil.mustBeSwingThread();
        if (!Core.getProject().isProjectLoaded()) return;
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        Cursor oldCursor = this.editor.getCursor();
        this.editor.setCursor(hourglassCursor);
        commitAndDeactivate();
        List<FileInfo> files = Core.getProject().getProjectFiles();
        SourceTextEntry ste;
        int startFileIndex = displayedFileIndex;
        int startEntryIndex = displayedEntryIndex;
        boolean looped = false;
        do {
            displayedEntryIndex--;
            if (displayedEntryIndex < 0) {
                displayedFileIndex--;
                if (displayedFileIndex < 0) {
                    displayedFileIndex = files.size() - 1;
                    looped = true;
                }
                loadDocument();
                displayedEntryIndex = m_docSegList.length - 1;
            }
            ste = getCurrentEntry();
        } while (ste == null && (!looped || !(displayedFileIndex == startFileIndex && displayedEntryIndex <= startEntryIndex)));
        activateEntry();
        this.editor.setCursor(oldCursor);
    }

    /**
     * Finds the next untranslated entry in the document.
     */
    public void nextUntranslatedEntry() {
        UIThreadsUtil.mustBeSwingThread();
        if (Core.getProject().isProjectLoaded() == false) return;
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        Cursor oldCursor = this.editor.getCursor();
        this.editor.setCursor(hourglassCursor);
        commitAndDeactivate();
        List<FileInfo> files = Core.getProject().getProjectFiles();
        SourceTextEntry ste;
        int startFileIndex = displayedFileIndex;
        int startEntryIndex = displayedEntryIndex;
        boolean looped = false;
        do {
            displayedEntryIndex++;
            if (displayedEntryIndex >= m_docSegList.length) {
                displayedFileIndex++;
                displayedEntryIndex = 0;
                if (displayedFileIndex >= files.size()) {
                    displayedFileIndex = 0;
                    looped = true;
                }
                loadDocument();
            }
            ste = getCurrentEntry();
        } while ((ste == null || Core.getProject().getTranslation(ste) != null) && (!looped || !(displayedFileIndex == startFileIndex && displayedEntryIndex <= startEntryIndex)));
        activateEntry();
        this.editor.setCursor(oldCursor);
    }

    /**
     * {@inheritDoc}
     */
    public int getCurrentEntryNumber() {
        SourceTextEntry e = getCurrentEntry();
        return e != null ? e.entryNum() : 0;
    }

    /**
     * {@inheritDoc}
     */
    public void gotoFile(int fileIndex) {
        UIThreadsUtil.mustBeSwingThread();
        if (!Core.getProject().isProjectLoaded()) return;
        if (m_docSegList == null) {
            return;
        }
        commitAndDeactivate();
        displayedFileIndex = fileIndex;
        displayedEntryIndex = 0;
        loadDocument();
        activateEntry();
    }

    /**
     * {@inheritDoc}
     */
    public void gotoEntry(final int entryNum) {
        UIThreadsUtil.mustBeSwingThread();
        if (!Core.getProject().isProjectLoaded()) return;
        if (m_docSegList == null) {
            return;
        }
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        Cursor oldCursor = this.editor.getCursor();
        this.editor.setCursor(hourglassCursor);
        commitAndDeactivate();
        if (entryNum == 0) {
            displayedFileIndex = 0;
            displayedEntryIndex = 0;
            loadDocument();
        } else {
            IProject dataEngine = Core.getProject();
            for (int i = 0; i < dataEngine.getProjectFiles().size(); i++) {
                IProject.FileInfo fi = dataEngine.getProjectFiles().get(i);
                SourceTextEntry firstEntry = fi.entries.get(0);
                SourceTextEntry lastEntry = fi.entries.get(fi.entries.size() - 1);
                if (firstEntry.entryNum() <= entryNum && lastEntry.entryNum() >= entryNum) {
                    if (i != displayedFileIndex) {
                        displayedFileIndex = i;
                        loadDocument();
                    }
                    for (int j = 0; j < m_docSegList.length; j++) {
                        if (m_docSegList[j].segmentNumberInProject >= entryNum) {
                            displayedEntryIndex = j;
                            break;
                        }
                    }
                    break;
                }
            }
        }
        activateEntry();
        this.editor.setCursor(oldCursor);
    }

    /**
     * Change case of the selected text or if none is selected, of the current
     * word.
     * 
     * @param toWhat
     *            : lower, title, upper or cycle
     */
    public void changeCase(CHANGE_CASE_TO toWhat) {
        UIThreadsUtil.mustBeSwingThread();
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();
        int caretPosition = editor.getCaretPosition();
        int translationStart = editor.getOmDocument().getTranslationStart();
        int translationEnd = editor.getOmDocument().getTranslationEnd();
        if (end < translationStart || start > translationEnd) return;
        if (start < translationStart && end <= translationEnd) start = translationStart;
        if (end > translationEnd && start >= translationStart) end = translationEnd;
        try {
            if (start == end) {
                start = EditorUtils.getWordStart(editor, start);
                end = EditorUtils.getWordEnd(editor, end);
            }
            editor.setSelectionStart(start);
            editor.setSelectionEnd(end);
            String selectionText = editor.getText(start, end - start);
            Token[] tokenList = Core.getProject().getTargetTokenizer().tokenizeWordsForSpelling(selectionText);
            StringBuffer buffer = new StringBuffer(selectionText);
            if (toWhat == CHANGE_CASE_TO.CYCLE) {
                int lower = 0;
                int upper = 0;
                int title = 0;
                int other = 0;
                for (Token token : tokenList) {
                    String word = token.getTextFromString(selectionText);
                    if (StringUtil.isLowerCase(word)) {
                        lower++;
                        continue;
                    }
                    if (StringUtil.isTitleCase(word)) {
                        title++;
                        continue;
                    }
                    if (StringUtil.isUpperCase(word)) {
                        upper++;
                        continue;
                    }
                    other++;
                }
                if (lower == 0 && title == 0 && upper == 0 && other == 0) return;
                if (lower != 0 && title == 0 && upper == 0) toWhat = CHANGE_CASE_TO.TITLE;
                if (lower == 0 && title != 0 && upper == 0) toWhat = CHANGE_CASE_TO.UPPER;
                if (lower == 0 && title == 0 && upper != 0) toWhat = CHANGE_CASE_TO.LOWER;
                if (other != 0) toWhat = CHANGE_CASE_TO.UPPER;
            }
            int lengthIncrement = 0;
            for (Token token : tokenList) {
                String result = doChangeCase(token.getTextFromString(selectionText), toWhat);
                buffer.replace(token.getOffset() + lengthIncrement, token.getLength() + token.getOffset() + lengthIncrement, result);
                lengthIncrement += result.length() - token.getLength();
            }
            editor.replaceSelection(buffer.toString());
            editor.setCaretPosition(caretPosition);
            editor.setSelectionStart(start);
            editor.setSelectionEnd(end);
        } catch (BadLocationException ble) {
            Log.log("bad location exception when changing case");
            Log.log(ble);
        }
    }

    /**
     * perform the case change. Lowercase becomes titlecase, titlecase becomes
     * uppercase, uppercase becomes lowercase. if the text matches none of these
     * categories, it is uppercased.
     * 
     * @param input
     *            : the string to work on
     * @param toWhat
     *            : one of the CASE_* values - except for case CASE_CYCLE.
     */
    private String doChangeCase(String input, CHANGE_CASE_TO toWhat) {
        Locale locale = Core.getProject().getProjectProperties().getTargetLanguage().getLocale();
        switch(toWhat) {
            case LOWER:
                return input.toLowerCase(locale);
            case UPPER:
                return input.toUpperCase(locale);
            case TITLE:
                return Character.toTitleCase(input.charAt(0)) + input.substring(1).toLowerCase(locale);
        }
        return input.toUpperCase(locale);
    }

    /**
     * {@inheritDoc}
     */
    public void replaceEditText(final String text) {
        UIThreadsUtil.mustBeSwingThread();
        int start = editor.getOmDocument().getTranslationStart();
        int end = editor.getOmDocument().getTranslationEnd();
        editor.select(start, end);
        editor.replaceSelection(text);
    }

    /**
     * {@inheritDoc}
     */
    public void insertText(final String text) {
        UIThreadsUtil.mustBeSwingThread();
        editor.checkAndFixCaret();
        editor.replaceSelection(text);
    }

    /**
     * {@inheritDoc}
     */
    public void gotoHistoryBack() {
        UIThreadsUtil.mustBeSwingThread();
        int prevValue = history.back();
        if (prevValue != -1) {
            gotoEntry(prevValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void gotoHistoryForward() {
        UIThreadsUtil.mustBeSwingThread();
        int nextValue = history.forward();
        if (nextValue != -1) {
            gotoEntry(nextValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    public EditorSettings getSettings() {
        return settings;
    }

    /**
     * {@inheritDoc}
     */
    public void undo() {
        UIThreadsUtil.mustBeSwingThread();
        try {
            if (editor.undoManager.canUndo()) {
                editor.undoManager.undo();
            }
        } catch (CannotUndoException cue) {
            Log.log(cue);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void redo() {
        UIThreadsUtil.mustBeSwingThread();
        try {
            if (editor.undoManager.canRedo()) {
                editor.undoManager.redo();
            }
        } catch (CannotRedoException cue) {
            Log.log(cue);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getSelectedText() {
        UIThreadsUtil.mustBeSwingThread();
        return editor.getSelectedText();
    }

    /** Loads Instant start article */
    private void createAdditionalPanes() {
        introPaneTitle = OStrings.getString("DOCKING_INSTANT_START_TITLE");
        try {
            String language = detectInstantStartLanguage();
            introPane = new JTextPane();
            introPane.setComponentOrientation(EditorUtils.isRTL(language) ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT);
            introPane.setEditable(false);
            introPane.setPage(HelpFrame.getHelpFileURL(language, OConsts.HELP_INSTANT_START));
        } catch (IOException e) {
        }
        emptyProjectPaneTitle = OStrings.getString("TF_INTRO_EMPTYPROJECT_FILENAME");
        emptyProjectPane = new JTextPane();
        emptyProjectPane.setEditable(false);
        emptyProjectPane.setText(OStrings.getString("TF_INTRO_EMPTYPROJECT"));
        emptyProjectPane.setFont(Core.getMainWindow().getApplicationFont());
    }

    /**
     * Detects the language of the instant start guide (checks if present in
     * default locale's language).
     * 
     * If there is no instant start guide in the default locale's language, "en"
     * (English) is returned, otherwise the acronym for the default locale's
     * language.
     * 
     * @author Henry Pijffers (henry.pijffers@saxnot.com)
     */
    private String detectInstantStartLanguage() {
        String language = Locale.getDefault().getLanguage().toLowerCase(Locale.ENGLISH);
        String country = Locale.getDefault().getCountry().toUpperCase(Locale.ENGLISH);
        if (HelpFrame.getHelpFileURL(language + "_" + country, OConsts.HELP_INSTANT_START) != null) {
            return language + "_" + country;
        }
        if (HelpFrame.getHelpFileURL(language, OConsts.HELP_INSTANT_START) != null) {
            return language;
        }
        return "en";
    }

    /**
     * {@inheritDoc}
     */
    public void remarkOneMarker(final String markerClassName) {
        int mi = markerController.getMarkerIndex(markerClassName);
        markerController.process(m_docSegList, mi);
    }

    /**
     * {@inheritDoc}
     */
    public void markActiveEntrySource(final SourceTextEntry requiredActiveEntry, final List<Mark> marks, final String markerClassName) {
        UIThreadsUtil.mustBeSwingThread();
        for (Mark m : marks) {
            if (m.entryPart != Mark.ENTRY_PART.SOURCE) {
                throw new RuntimeException("Mark must be for source only");
            }
        }
        SourceTextEntry realActive = m_docSegList[displayedEntryIndex].ste;
        if (realActive != requiredActiveEntry) {
            return;
        }
        int mi = markerController.getMarkerIndex(markerClassName);
        markerController.setEntryMarks(displayedEntryIndex, m_docSegList[displayedEntryIndex], marks, mi);
    }

    public void registerPopupMenuConstructors(int priority, IPopupMenuConstructor constructor) {
        editor.registerPopupMenuConstructors(priority, constructor);
    }

    /**
     * {@inheritdoc}
     * Document is reloaded to immediately have the filter being effective.
     */
    public void addFilter(List<Integer> entryList) {
        this.entryFilterList = entryList;
        int curEntryNum = getCurrentEntryNumber();
        Document3 doc = editor.getOmDocument();
        IProject project = Core.getProject();
        if (doc != null && project != null && project.getProjectFiles() != null) {
            loadDocument();
            if (isInFilter(curEntryNum)) {
                gotoEntry(curEntryNum);
            } else {
                for (int j = 0; j < m_docSegList.length; j++) {
                    if (m_docSegList[j].segmentNumberInProject >= curEntryNum) {
                        displayedEntryIndex = j - 1;
                        break;
                    }
                }
                nextEntry();
            }
        }
    }

    /**
     * {@inheritdoc}
     * Document is reloaded if appropriate to immediately remove the filter;
     */
    public void removeFilter() {
        this.entryFilterList = null;
        int curEntryNum = getCurrentEntryNumber();
        Document3 doc = editor.getOmDocument();
        IProject project = Core.getProject();
        if (doc != null && project != null) {
            List<FileInfo> files = project.getProjectFiles();
            if (files != null && !files.isEmpty()) {
                loadDocument();
                gotoEntry(curEntryNum);
            }
        }
    }

    /**
     * Returns if the given entry is part of the filtered entries.
     * @param entry project-wide entry number
     * @return true if entry belongs to the filtered entries, or if there is 
     *         no filter in place, false otherwise.
     */
    public boolean isInFilter(Integer entry) {
        if (this.entryFilterList == null) return true; else return this.entryFilterList.contains(entry);
    }

    /**
     * Splits the active segment at the position of the caret.
     */
    public void splitSegments() {
        int curPos = ((org.omegat.gui.editor.EditorController) Core.getEditor()).editor.getCaretPosition();
        SourceTextEntry ste = Core.getEditor().getCurrentEntry();
        int entryNum = Core.getEditor().getCurrentEntryNumber();
        if (((org.omegat.gui.editor.EditorController) Core.getEditor()).m_docSegList[entryNum - 1].isInsideSegment(curPos)) {
            int start = ((org.omegat.gui.editor.EditorController) Core.getEditor()).m_docSegList[entryNum - 1].getStartPosition();
            int rel = curPos - start - 1;
            if (rel > ste.getSrcText().length()) {
                javax.swing.JOptionPane.showMessageDialog(null, OStrings.getString("MW_SPLIT_SOURCE_ERROR"), OStrings.getString("TF_ERROR"), javax.swing.JOptionPane.WARNING_MESSAGE);
            } else {
                String afterText = ste.getSrcText().substring(rel);
                String beforeText = ste.getSrcText().substring(0, rel);
                if (!SM.sm.smExists()) SM.sm.createSmFile();
                SM.sm.writeSMRule(SM.SPLIT, beforeText);
                String beforeText2 = beforeText.trim();
                SourceTextEntry newSte = new SourceTextEntry(afterText, entryNum);
                SourceTextEntry oldSte = new SourceTextEntry(beforeText2, entryNum - 1);
                IProject.FileInfo file = Core.getProject().getProjectFiles().get(displayedFileIndex);
                List<SourceTextEntry> entries = file.entries;
                entries.set(entryNum - 1, oldSte);
                entries.add(entryNum, newSte);
                org.omegat.gui.main.ProjectUICommands.projectReload();
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, OStrings.getString("MW_SPLIT_ACTIVE_ERROR"), OStrings.getString("TF_ERROR"), javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshDisplay(int entryNum) {
        loadDocument();
        gotoEntry(entryNum);
        requestFocus();
    }

    /**
     * Merges the current active segment with the following segment.
     * <p>The last segment cannot be merged with the first segment of the next
     * file. But the second to last segment can be merged with the last.
     */
    public void mergeSegments() {
        int entNum = displayedEntryIndex;
        int lastIndex = m_docSegList.length - 1;
        if (entNum >= lastIndex) {
            javax.swing.JOptionPane.showMessageDialog(null, OStrings.getString("MW_MERGE_ERROR"), OStrings.getString("TF_ERROR"), javax.swing.JOptionPane.WARNING_MESSAGE);
        } else {
            IProject.FileInfo file = Core.getProject().getProjectFiles().get(displayedFileIndex);
            List<SourceTextEntry> entries = file.entries;
            String nowText = entries.get(entNum).getSrcText();
            String nextText = entries.get(entNum + 1).getSrcText();
            nowText = nowText.trim();
            nextText = nextText.trim();
            if (!SM.sm.smExists()) SM.sm.createSmFile();
            SM.sm.writeSMRule(SM.MERGE, nowText);
            if (nowText.endsWith(".") || nowText.endsWith("!") || nowText.endsWith("?") || nowText.endsWith(",") || nowText.endsWith(":") || nowText.endsWith(";")) nowText += " ";
            SourceTextEntry ste = new SourceTextEntry(nowText + nextText, entNum);
            entries.set(entNum, ste);
            entries.remove(entNum + 1);
            for (int i = 0; i < entries.size(); i++) {
                SourceTextEntry tmpSte = entries.get(i);
                tmpSte.setEntryNum(i);
            }
            int isDisp = entries.indexOf(ste);
            displayedEntryIndex = isDisp;
            org.omegat.gui.main.ProjectUICommands.projectReload();
        }
    }
}
