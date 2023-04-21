package uk.ac.shef.wit.runes.rune.nlp;

import uk.ac.shef.wit.commons.UtilCollections;
import uk.ac.shef.wit.runes.Runes;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchContent;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchStructure;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionRuneExecution;
import uk.ac.shef.wit.runes.rune.Rune;
import uk.ac.shef.wit.runes.runestone.Content;
import uk.ac.shef.wit.runes.runestone.Runestone;
import uk.ac.shef.wit.runes.runestone.Structure;
import uk.ac.shef.wit.runes.runestone.StructureAndContent;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Class implments Rune {@link Rune} interface which locates token position relative to a document and a page
 *
 * @author <a href="mailto:z.zhang@dcs.shef.ac.uk">Ziqi Zhang</a>
 * @version $Id: RuneTokenPositioner.java $
 */
public class RuneTokenPositioner implements Rune {

    private static final double VERSION = 0.6;

    private static final Logger log = Logger.getLogger(RuneTokenPositioner.class.getName());

    private String _pageBreak;

    private int _sectionsOfPage = 10;

    private int _sectionsOfDoc = 10;

    static {
        Runes.registerRune(RuneTokenPositioner.class, MessageFormat.format("[nlp] token positioner v{0}", VERSION));
    }

    /**
     * {@inheritDoc}
     * <br/><br/>
     * <strong>Required:</strong>
     * <pre>
     *  token_from_$
     *  $_has_text
     *  $_text
     *  page_break_marker,
     * page_total_number_of_sections,
     * token_has_start_offset_in_$,
     * start_offset_in_$
     * </pre>
     */
    public Set<String> analyseRequired(final Runestone stone) {
        return UtilCollections.add(new HashSet<String>(), "token_from_$", "$_has_text", "$_text", "page_break_marker", "page_total_number_of_sections", "token_has_start_offset_in_$", "start_offset_in_$");
    }

    /**
     * {@inheritDoc}
     * <br/><br/>
     * <strong>Provided:</strong>
     * <pre>
     *  "token_on_pagenum",
     * "pagenum",
     * "token_in_page_section",
     * "page_section"
     * </pre>
     */
    public Set<String> analyseProvided(final Runestone stone) {
        return UtilCollections.add(new HashSet<String>(), "page_total_number_of_sections", "$_token_from_page_number", "$_page_number", "$_token_from_page_section", "$_page_section", "$_token_from_document_section", "$_document_section");
    }

    public void carve(final Runestone stone) throws RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent, RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        updateOptionalParameters(stone);
        final Content<String> contents = stone.getContent("$_text");
        final Structure tokens = stone.getStructure("token_from_$");
        final Structure tokenStartOffset = stone.getStructure("token_has_start_offset_in_$");
        final Content<Integer> starts = stone.getContent("start_offset_in_$");
        final Structure tokenOnPage = stone.getStructure("$_token_from_page_number");
        final StructureAndContent<Integer> pagenum = stone.getStructureAndContent("$_page_number");
        final Structure tokenInSection = stone.getStructure("$_token_from_page_section");
        final StructureAndContent<Integer> pageSection = stone.getStructureAndContent("$_page_section");
        final Structure tokenFromDocSec = stone.getStructure("$_token_from_document_section");
        final StructureAndContent<Integer> docSection = stone.getStructureAndContent("$_document_section");
        for (int i = 0; i < _sectionsOfPage; i++) pageSection.encode(i + 1);
        for (int i = 0; i < _sectionsOfDoc; i++) docSection.encode(i + 1);
        for (final int[] id : stone.getStructure("$_has_text")) {
            final int contextId = id[0];
            final int contentId = id[1];
            final String content = contents.retrieve(contentId);
            final List<Integer> pageBreaks = findPageBreakOffsets(content);
            final Map<Integer, int[]> dsections = findDocIntersectionPages(pageBreaks.size());
            for (int i = 0; i < pageBreaks.size(); i++) pagenum.encode(i + 1);
            for (int[] tokenFromDoc : tokens) {
                if (tokenFromDoc[1] == contextId) {
                    int offset = starts.retrieve(tokenStartOffset.follow(tokenFromDoc[0]));
                    int page = 1;
                    int section = 1;
                    int adocsec = 1;
                    for (int i = 0; i < pageBreaks.size(); i++) {
                        if (offset <= pageBreaks.get(i)) {
                            page = i + 1;
                            int prevPageOffset = i == 0 ? 0 : pageBreaks.get(i - 1);
                            section = findPageSection(prevPageOffset, pageBreaks.get(i), offset);
                            adocsec = findDocumentSection(page, dsections);
                            break;
                        }
                    }
                    tokenOnPage.inscribe(tokenFromDoc[0], pagenum.encode(page));
                    tokenInSection.inscribe(tokenFromDoc[0], pageSection.encode(section));
                    tokenFromDocSec.inscribe(tokenFromDoc[0], docSection.encode(adocsec));
                }
            }
        }
    }

    private int findDocumentSection(int page, Map<Integer, int[]> sections) {
        for (Map.Entry<Integer, int[]> e : sections.entrySet()) {
            int[] range = e.getValue();
            if (page >= range[0] && page <= range[1]) return e.getKey();
        }
        return 1;
    }

    private int findPageSection(int prevOffset, int thisOffset, int tokenOffset) {
        int increment = (thisOffset - prevOffset) / _sectionsOfPage;
        for (int i = 0; i < _sectionsOfPage; i++) {
            if (tokenOffset <= (thisOffset - increment * (_sectionsOfPage - i - 1))) return i + 1;
        }
        return 1;
    }

    private List<Integer> findPageBreakOffsets(final String content) {
        List<Integer> offsets = new ArrayList<Integer>();
        int from = 0;
        int start;
        while ((start = content.indexOf(_pageBreak, from)) != -1) {
            offsets.add(start);
            from = start + _pageBreak.length();
        }
        if (offsets.size() == 0) offsets.add(content.length() - 1);
        Collections.sort(offsets);
        return offsets;
    }

    private Map<Integer, int[]> findDocIntersectionPages(int totalPages) {
        Map<Integer, int[]> map = new HashMap<Integer, int[]>();
        if (totalPages == 1) {
            map.put(1, new int[] { 1, 1 });
            return map;
        }
        int pagesPersec = totalPages / _sectionsOfDoc;
        int left = totalPages - (pagesPersec * _sectionsOfDoc);
        int firststart = 1;
        int firstend = pagesPersec <= 1 ? 1 : pagesPersec;
        int lastend = totalPages;
        int laststart = pagesPersec == 0 ? lastend : lastend - pagesPersec + 1;
        int firstSectionMargin = 0;
        int lastSectionMargin = 0;
        if (left > 3) {
            firstSectionMargin = left / 2;
            lastSectionMargin = left / 2;
            if (pagesPersec == 0) firstend = firstend + firstSectionMargin - 1; else firstend = firstend + firstSectionMargin;
            if (left - firstSectionMargin * 2 > 0) firstend += left - firstSectionMargin * 2;
            if (pagesPersec == 0) laststart = laststart - lastSectionMargin + 1; else laststart = laststart - lastSectionMargin;
        }
        map.put(1, new int[] { firststart, firstend });
        map.put(_sectionsOfDoc, new int[] { laststart, lastend });
        int start = 0, end = 0, count = 0, secid = 2;
        for (int i = firstend + 1; i < laststart; i++) {
            if (start == 0) start = i;
            count++;
            if (count == pagesPersec) {
                end = i;
                map.put(secid, new int[] { start, end });
                start = 0;
                end = 0;
                secid++;
                count = 0;
            }
            if (pagesPersec == 0) {
                map.put(secid, new int[] { start, start });
            }
        }
        return map;
    }

    /**
     * @param stone
     */
    private void updateOptionalParameters(final Runestone stone) {
        try {
            _pageBreak = stone.<String>getContent("page_break_marker").iterator().next();
        } catch (RunesExceptionNoSuchContent ignore) {
        } catch (NoSuchElementException ignore) {
        }
        try {
            _sectionsOfPage = stone.<Integer>getContent("page_total_number_of_sections").iterator().next();
        } catch (RunesExceptionNoSuchContent ignore) {
        } catch (NoSuchElementException ignore) {
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RuneTokenPositioner;
    }

    @Override
    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    @Override
    public String toString() {
        return "token positioner";
    }
}
