package edu.asu.vogon.embryo.transform.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import edu.asu.vogon.embryo.embryomodel.EmbryoAnnotation;
import edu.asu.vogon.embryo.embryomodel.EmphasizedString;
import edu.asu.vogon.embryo.embryomodel.HighlightedString;
import edu.asu.vogon.embryo.embryomodel.LinkString;
import edu.asu.vogon.embryo.transform.Activator;
import edu.asu.vogon.embryo.transform.Constants;
import edu.asu.vogon.model.Annotation;
import edu.asu.vogon.model.Relation;
import edu.asu.vogon.model.SpecificTextTerm;
import edu.asu.vogon.util.properties.ExtendedPropertyHandler;
import edu.asu.vogon.util.properties.PropertyHandlerRegistry;

public class TextMarkupCreator {

    private String ITALICS_START = "<i>";

    private String ITALICS_END = "</i>";

    private String BOLD_START = "<b>";

    private String BOLD_END = "</b>";

    private String UNDERLINE_START = "<u>";

    private String UNDERLINE_END = "</u>";

    private String NEWLINE = "<br>";

    public String createMarkupedText(String text, List<EmphasizedString> emphasizes, List<Annotation> annotations, List<LinkString> linkStrings, boolean putLinksInNewLine) {
        List<EmphasizedString> copies = new ArrayList<EmphasizedString>();
        for (EmphasizedString es : emphasizes) copies.add(es);
        List<LinkString> linkCopies = new ArrayList<LinkString>();
        for (LinkString ls : linkStrings) linkCopies.add(ls);
        Collections.sort(copies, new Comparator<EmphasizedString>() {

            public int compare(EmphasizedString o1, EmphasizedString o2) {
                return o1.getStart() - o2.getStart();
            }
        });
        List<Markup> markups = new ArrayList<Markup>();
        for (EmphasizedString es : copies) {
            if (es.isBold()) {
                Markup bMarkup = new Markup(es.getStart(), BOLD_START, Markup.BOLD);
                markups.add(bMarkup);
                bMarkup.endMarkup = new Markup(es.getStart() + es.getLength(), BOLD_END, Markup.BOLD);
            }
            if (es.isItalics()) {
                Markup iMarkup = new Markup(es.getStart(), ITALICS_START, Markup.ITALICS);
                markups.add(iMarkup);
                iMarkup.endMarkup = new Markup(es.getStart() + es.getLength(), ITALICS_END, Markup.ITALICS);
            }
            if (es.isUnderlined()) {
                Markup uMarkup = new Markup(es.getStart(), UNDERLINE_START, Markup.UNDERLINED);
                markups.add(uMarkup);
                uMarkup.endMarkup = new Markup(es.getStart() + es.getLength(), UNDERLINE_END, Markup.UNDERLINED);
            }
        }
        ExtendedPropertyHandler handler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(Activator.PLUGIN_ID, Constants.PROPERTIES_FILE);
        for (LinkString ls : linkCopies) {
            String startTag = putLinksInNewLine ? NEWLINE + handler.getExtendedProperty("_external_link_start", new String[] { ls.getUrl() }) : handler.getExtendedProperty("_external_link_start", new String[] { ls.getUrl() });
            Markup lMarkup = new Markup(ls.getStart(), startTag, Markup.LINK);
            markups.add(lMarkup);
            lMarkup.endMarkup = new Markup(ls.getStart() + ls.getLength(), handler.getProperty("_external_link_end"), Markup.LINK);
        }
        Collections.sort(markups, new Comparator<Markup>() {

            public int compare(Markup o1, Markup o2) {
                return o1.index - o2.index;
            }
        });
        if (markups.size() > 1) {
            List<Markup> tempMarkupList = new ArrayList<Markup>();
            tempMarkupList.add(markups.get(0));
            for (int i = 1; i < markups.size(); i++) {
                Markup matchingMarkup = getConntectedMarkup(tempMarkupList, markups.get(i));
                if (matchingMarkup == null) tempMarkupList.add(markups.get(i)); else {
                    matchingMarkup.endMarkup = markups.get(i).endMarkup;
                }
            }
            markups = tempMarkupList;
        }
        TransformationPropertiesProvider provider = new TransformationPropertiesProvider();
        List<Integer> endHighlightsList = new ArrayList<Integer>();
        if (annotations != null) {
            for (Annotation an : annotations) {
                if (an instanceof EmbryoAnnotation) {
                    if (an.getFirstTextTerm() != null && an.getFirstTextTerm() instanceof SpecificTextTerm) {
                        String relationsStr = "";
                        for (Relation rel : ((EmbryoAnnotation) an).getRelations()) {
                            relationsStr += relationsStr.equals("") ? rel.getName() : "; " + rel.getName();
                        }
                        String href = "";
                        if (((EmbryoAnnotation) an).getPid() != null && !((EmbryoAnnotation) an).getPid().trim().equals("")) {
                            href = provider.getPIDPrefix() + ((EmbryoAnnotation) an).getPid();
                        } else {
                            String url = ((EmbryoAnnotation) an).getUrl();
                            if (url != null) href = url;
                        }
                        if (!href.equals("")) {
                            href = handler.getExtendedProperty("_embryo_link_href", new String[] { href });
                        }
                        String relationClass = "";
                        if (!relationsStr.equals("")) {
                            relationClass = handler.getExtendedProperty("_embryo_link_class", new String[] { relationsStr });
                        }
                        String aStart = "";
                        if (!href.equals("") || !relationClass.equals("")) aStart = handler.getExtendedProperty("_embryo_link_start", new String[] { relationClass, href });
                        SpecificTextTerm annotatedTerm = (SpecificTextTerm) an.getFirstTextTerm();
                        Markup anMarkup = new Markup(annotatedTerm.getStartIndex(), aStart, Markup.LINK);
                        markups.add(anMarkup);
                        String aEnd = "";
                        if (!aStart.equals("")) aEnd = handler.getProperty("_embryo_link_end");
                        anMarkup.endMarkup = new Markup(annotatedTerm.getEndIndex(), aEnd, Markup.LINK);
                        endHighlightsList.add(annotatedTerm.getEndIndex());
                    }
                }
            }
        }
        Collections.sort(markups, new Comparator<Markup>() {

            public int compare(Markup o1, Markup o2) {
                int result = o1.index - o2.index;
                if (result != 0) return result;
                return o2.endMarkup.index - o1.endMarkup.index;
            }
        });
        StringBuffer sb = new StringBuffer();
        Markup firstMarkup = null;
        if (markups.size() > 0) {
            firstMarkup = markups.get(0);
            markups.remove(0);
        }
        List<Markup> closingMarkups = new ArrayList<Markup>();
        boolean removeMode = false;
        HTMLTranslationMap translationMap = new HTMLTranslationMap();
        for (int i = 0; i < text.length(); i++) {
            while (true) {
                if (closingMarkups.size() > 0) {
                    Markup firstClosingMarkup = closingMarkups.get(0);
                    if (firstClosingMarkup.index == i) {
                        sb.append(firstClosingMarkup.markup);
                        closingMarkups.remove(0);
                    } else break;
                } else break;
            }
            if (firstMarkup != null) {
                while (firstMarkup != null && firstMarkup.index == i) {
                    sb.append(firstMarkup.markup);
                    if (firstMarkup.endMarkup != null) {
                        int endIndex = firstMarkup.endMarkup.index;
                        int insertAt = 0;
                        if (closingMarkups.size() > 0) {
                            int pointerClosingIndex = closingMarkups.get(0).index;
                            while (endIndex > pointerClosingIndex) {
                                insertAt++;
                                if (closingMarkups.size() > insertAt) pointerClosingIndex = closingMarkups.get(insertAt).index; else break;
                            }
                        }
                        if (closingMarkups.size() > insertAt) closingMarkups.add(insertAt, firstMarkup.endMarkup); else closingMarkups.add(firstMarkup.endMarkup);
                    }
                    firstMarkup = null;
                    if (markups.size() > 0) {
                        firstMarkup = markups.get(0);
                        markups.remove(0);
                    }
                }
            }
            if (text.length() > i + 1) {
                if (text.charAt(i + 1) == handler.getProperty("_opening_bracket_relationships").charAt(0)) {
                    if (endHighlightsList.contains(i) || endHighlightsList.contains(i - 1) || endHighlightsList.contains(i - 2)) {
                        removeMode = true;
                    }
                }
            }
            if (!removeMode) {
                char insert = text.charAt(i);
                String translation = translationMap.getCode(insert);
                if (translation == null) sb.append(text.charAt(i)); else sb.append(translation);
            }
            if (text.charAt(i) == handler.getProperty("_closing_bracket_relationships").charAt(0)) {
                if (removeMode) {
                    removeMode = false;
                }
            }
        }
        String markupedText = sb.toString();
        String[] paragraphs = markupedText.split("\n");
        sb = new StringBuffer();
        for (String para : paragraphs) {
            sb.append(handler.getProperty("_paragraph_start"));
            sb.append(para);
            sb.append(handler.getProperty("_paragraph_end"));
        }
        return sb.toString();
    }

    protected Markup getConntectedMarkup(List<Markup> prevMarkups, Markup nextMarkup) {
        Collections.reverse(prevMarkups);
        for (Markup m : prevMarkups) {
            if (m.markupType == nextMarkup.markupType && (m.endMarkup.index == nextMarkup.index - 1 || m.endMarkup.index == nextMarkup.index)) return m;
        }
        return null;
    }

    class Markup {

        public static final int ITALICS = 0;

        public static final int BOLD = 2;

        public static final int UNDERLINED = 3;

        public static final int LINK = 4;

        public int index;

        public String markup;

        public int markupType;

        public String url;

        public Markup endMarkup;

        public Markup(int index, String markup, int markupType) {
            this.index = index;
            this.markup = markup;
            this.markupType = markupType;
        }
    }
}
