package org.gbif.ecat.lucene.analysis.sciname;

import org.gbif.ecat.voc.Rank;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class SciNameFilter extends TokenFilter {

    class NamePart {

        public Rank rank;

        public String marker;

        public String namepart;
    }

    private TermAttribute termAtt;

    private OffsetAttribute offsetAtt;

    private SciNameAttribute sciNameAtt;

    private PunctuationAttribute punctAtt;

    private Collection<String> supraGenera = new HashSet<String>();

    private Collection<String> genera = new HashSet<String>();

    private Collection<String> generaAmbigous = new HashSet<String>();

    private Collection<String> epitheta = new HashSet<String>();

    private Collection<String> epithetaAmbigous = new HashSet<String>();

    private final int maxYear = Calendar.getInstance().get(Calendar.YEAR) + 2;

    private StringBuffer verbatim = new StringBuffer();

    private int score;

    private int startOffset = -1;

    private int endOffset = -1;

    private boolean abbreviated = false;

    private String genus;

    private String subgenus;

    private LinkedList<NamePart> parts = new LinkedList<NamePart>();

    private boolean forceRelease = false;

    private static final Pattern RANK_MARKER;

    static {
        String allMarker = "(notho)? *(" + StringUtils.join(Rank.RANK_MARKER_MAP.keySet(), "|") + ")\\.?";
        RANK_MARKER = Pattern.compile("^" + allMarker + "$");
    }

    private Map<String, String> genusAbbreviations = new HashMap<String, String>();

    private int sciNameCounter = 0;

    private String IGNORE_CHARS = "«»?×\"+()[]";

    public SciNameFilter(TokenStream input, Collection<String> authoritySupraGenera, Collection<String> authorityGenera, Collection<String> authorityEpitheta, Collection<String> ambigGenera) {
        super(input);
        sciNameAtt = addAttribute(SciNameAttribute.class);
        offsetAtt = getAttribute(OffsetAttribute.class);
        termAtt = getAttribute(TermAttribute.class);
        punctAtt = getAttribute(PunctuationAttribute.class);
        this.supraGenera = authoritySupraGenera;
        this.genera = authorityGenera;
        this.generaAmbigous = ambigGenera;
        this.epitheta = authorityEpitheta;
    }

    private void concatVerbatim(String text) {
        if (verbatim.length() > 0) {
            verbatim.append(" ");
        }
        verbatim.append(text);
        if (startOffset < 0) {
            startOffset = offsetAtt.startOffset();
        }
        endOffset = offsetAtt.endOffset();
    }

    @Override
    public final boolean incrementToken() throws IOException {
        while (input.incrementToken()) {
            String normedTerm = normalizeTerm(termAtt.term());
            if (punctAtt.getPunctuation() != ' ') {
                forceRelease = true;
            }
            if (genus == null) {
                if (genera.contains(normedTerm)) {
                    genus = normedTerm;
                    keepAbreviatedGenus(normedTerm);
                    concatVerbatim(termAtt.term());
                    if (!isCapitalized(termAtt.term())) {
                        score = -10;
                    }
                    if (!forceRelease) {
                        continue;
                    }
                } else if (normedTerm.length() <= 2 && Character.isLetter(normedTerm.charAt(0)) && isCapitalized(termAtt.term()) && punctAtt.getPunctuation() != '>' && punctAtt.getPunctuation() != '<') {
                    if (genusAbbreviations.containsKey(normedTerm)) {
                        abbreviated = true;
                        genus = genusAbbreviations.get(normedTerm);
                        concatVerbatim(termAtt.term() + (punctAtt.getPunctuation() == ' ' ? "" : punctAtt.getPunctuation()));
                        forceRelease = false;
                        continue;
                    }
                } else if (supraGenera.contains(normedTerm)) {
                    genus = normedTerm;
                    concatVerbatim(termAtt.term());
                    if (!isCapitalized(termAtt.term())) {
                        score = -10;
                    }
                    release();
                    return true;
                } else if (Rank.inferRank(normedTerm, null, null, null, null) != Rank.Unranked && StringUtils.isAlpha(normedTerm) && StringUtils.isAsciiPrintable(normedTerm)) {
                    if (isCapitalized(termAtt.term())) {
                        score = -10;
                    } else {
                        score = -20;
                    }
                    genus = normedTerm;
                    concatVerbatim(termAtt.term());
                    release();
                    return true;
                } else {
                    forceRelease = false;
                }
            } else {
                if (subgenus == null && parts.isEmpty() && normedTerm.startsWith("(") && normedTerm.endsWith(")")) {
                    normedTerm = StringUtils.substring(normedTerm, 1, -1).trim();
                    if (genera.contains(normedTerm)) {
                        subgenus = normedTerm;
                        concatVerbatim(termAtt.term());
                        if (!forceRelease) {
                            continue;
                        }
                    }
                } else {
                    if (RANK_MARKER.matcher(normedTerm).matches()) {
                        NamePart p = new NamePart();
                        p.marker = normedTerm;
                        parts.add(p);
                        if (punctAtt.getPunctuation() == '.') {
                            forceRelease = false;
                        }
                        concatVerbatim(termAtt.term() + (punctAtt.getPunctuation() == ' ' ? "" : punctAtt.getPunctuation()));
                        if (!forceRelease) {
                            continue;
                        }
                    } else {
                        if (epitheta.contains(normedTerm)) {
                            NamePart p = parts.peekLast();
                            if (p == null || p.namepart != null) {
                                p = new NamePart();
                                parts.add(p);
                            }
                            p.namepart = normedTerm;
                            concatVerbatim(termAtt.term());
                            if (!forceRelease) {
                                continue;
                            }
                        }
                    }
                }
                if (!abbreviated || !parts.isEmpty()) {
                    release();
                    return true;
                }
                clearMatches();
            }
        }
        return false;
    }

    private void clearMatches() {
        abbreviated = false;
        startOffset = -1;
        endOffset = -1;
        verbatim = new StringBuffer();
        genus = null;
        subgenus = null;
        parts.clear();
        score = 0;
        forceRelease = false;
    }

    private boolean isCapitalized(String term) {
        return StringUtils.capitalize(term).equals(term);
    }

    private void keepAbreviatedGenus(String genus) {
        genusAbbreviations.put(genus.substring(0, 1), genus);
        genusAbbreviations.put(genus.substring(0, 2), genus);
    }

    private String normalizeTerm(String term) {
        return term.toLowerCase().replaceAll("æ", "ae").replaceAll("œ", "oe");
    }

    private void release() {
        StringBuffer name = new StringBuffer(StringUtils.capitalize(genus));
        if (abbreviated) {
            score += 5;
        } else {
            score += genus.length() * 5;
            if (generaAmbigous.contains(genus)) {
                score -= 25;
            }
        }
        if (subgenus != null) {
            name.append(" (" + StringUtils.capitalize(subgenus) + ")");
            score += 10 + subgenus.length() * 3;
        }
        for (NamePart p : parts) {
            if (p.marker != null) {
                name.append(" " + p.marker + ".");
                score += 25;
            }
            if (p.namepart != null) {
                name.append(" " + p.namepart);
                score += 10 + p.namepart.length() * 3;
            }
        }
        if (score > 100) {
            score = 100;
        } else if (score < 0) {
            score = 0;
        }
        termAtt.setTermBuffer(name.toString());
        offsetAtt.setOffset(startOffset, endOffset);
        sciNameAtt.setAttributes(false, score, verbatim.toString());
        clearMatches();
    }
}
