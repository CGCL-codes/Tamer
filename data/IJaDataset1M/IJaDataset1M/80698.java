package qurtext.client;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.allen_sauer.gwt.voices.client.handler.PlaybackCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SectionView extends BaseView {

    private static final int targetLinkCount = 9;

    private static final int endLinkCount = 2;

    private Hyperlink closeUpdateLink = new Hyperlink("Chapters", "");

    private MainPresenter mainPresenter;

    private final Button initSectionButton = new Button("Initialize Section");

    private final Button initLiteralButton = new Button("Initialize Literals");

    private final Button reciteAllButton = new Button("Recite Section");

    private final Label sectionText = new Label("Click on a button!");

    private HorizontalPanel sectionNavigationBar = new HorizontalPanel();

    private final HorizontalPanel sectionPanel = new HorizontalPanel();

    private final HorizontalPanel sectionAdminBar = new HorizontalPanel();

    private VerticalPanel sectionMainPanel = new VerticalPanel();

    private SoundController soundController = new SoundController();

    private Sound sound;

    private String currentToken;

    private ArrayList<String> playList = new ArrayList<String>();

    private TreeMap<String, ArrayList<HTML>> textHtmlMap = new TreeMap<String, ArrayList<HTML>>();

    private TreeMap<String, HTML> translationHtmlMap = new TreeMap<String, HTML>();

    private SoundHandler soundHandler = new SoundHandler() {

        @Override
        public void onSoundLoadStateChange(SoundLoadStateChangeEvent event) {
        }

        @Override
        public void onPlaybackComplete(PlaybackCompleteEvent event) {
            if (null == currentToken) return;
            ArrayList<HTML> textHtmls = textHtmlMap.get(currentToken);
            if (null == textHtmls) return;
            for (HTML textHTML : textHtmls) {
                textHTML.removeStyleName("selected");
            }
            HTML translationHtml = translationHtmlMap.get(currentToken);
            if (null == translationHtml) return;
            translationHtml.removeStyleName("selected");
            currentToken = null;
            sound = null;
            if (playList.size() > 0) recite();
        }
    };

    private ClickHandler playAudioHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            HTML source = (HTML) event.getSource();
            String newToken = source.getElement().getAttribute("name").trim();
            playList.clear();
            playList.add(newToken);
            stopReciting();
            recite();
        }
    };

    private void stopReciting() {
        if (null != sound) {
            sound.stop();
            soundHandler.onPlaybackComplete(null);
        }
    }

    private ClickHandler updateClickHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            Hyperlink sender = (Hyperlink) event.getSource();
            String historyToken = sender.getTargetHistoryToken();
            mainPresenter.handleHistoryToken(historyToken);
        }
    };

    public MainPresenter getMainPresenter() {
        return mainPresenter;
    }

    public void setMainPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    public void showSection(ClientSection section) {
        mainPresenter.showSectionPanel();
        sectionNavigationBar.clear();
        sectionNavigationBar.add(closeUpdateLink);
        sectionNavigationBar.addStyleName("navbar");
        if (mainPresenter.getAllSections() == null) return;
        int sectionNo = section.sectionNo;
        int start = 0;
        int end = mainPresenter.getAllSections().size();
        if (sectionNo <= start + targetLinkCount / 2) {
            end = start + (targetLinkCount - endLinkCount);
        } else if (sectionNo >= end - targetLinkCount / 2) {
            start = end - (targetLinkCount - endLinkCount);
        } else {
            start = (sectionNo - (targetLinkCount / 2 - endLinkCount));
            end = (sectionNo + (targetLinkCount / 2 - endLinkCount) + 1);
        }
        addPreviousSectionLink(section, sectionNo - 1);
        if (start > 0) {
            addSectionLink(section, 0);
        }
        if (start > 1) {
            sectionNavigationBar.add(spacer());
            sectionNavigationBar.add(new HTML("..."));
        }
        for (int i = start; i < end; i++) {
            addSectionLink(section, i);
        }
        if (end < mainPresenter.getAllSections().size() - 1) {
            sectionNavigationBar.add(spacer());
            sectionNavigationBar.add(new HTML("..."));
        }
        if (end < mainPresenter.getAllSections().size()) {
            addSectionLink(section, mainPresenter.getAllSections().size() - 1);
        }
        addNextSectionLink(section, sectionNo + 1);
        sectionNavigationBar.add(reciteAllButton);
    }

    private void addSectionLink(ClientSection currentSection, int i) {
        ClientSection section = mainPresenter.getAllSections().get(i);
        sectionNavigationBar.add(spacer());
        String label = "" + section.verse;
        if (1 == section.verse) label = "" + section.chapter + ":" + label;
        addSectionLink(currentSection, section, label);
    }

    private void addPreviousSectionLink(ClientSection currentSection, int i) {
        if (i < 0) i = 0;
        ClientSection section = mainPresenter.getAllSections().get(i);
        sectionNavigationBar.add(spacer());
        String label = "<<";
        addSectionLink(currentSection, section, label);
    }

    private void addNextSectionLink(ClientSection currentSection, int i) {
        if (i >= mainPresenter.getAllSections().size()) i = mainPresenter.getAllSections().size() - 1;
        ClientSection section = mainPresenter.getAllSections().get(i);
        sectionNavigationBar.add(spacer());
        String label = ">>";
        addSectionLink(currentSection, section, label);
    }

    private Hyperlink addSectionLink(ClientSection currentSection, ClientSection section, String label) {
        Hyperlink sectionLink = null;
        if (currentSection.equals(section)) {
            sectionNavigationBar.add(new HTML("<b>" + label + "</b>"));
            mainPresenter.getSectionPresenter().updateSectionPanel(section);
        } else {
            sectionLink = new Hyperlink(label, "" + section.chapter + ":" + section.verse);
            sectionLink.addClickHandler(updateClickHandler);
            sectionNavigationBar.add(sectionLink);
        }
        return sectionLink;
    }

    /**
	 * initialize all widgets for the first time
	 */
    public void onSectionViewLoad() {
        closeUpdateLink.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                mainPresenter.showJumpPanel();
            }
        });
        initSectionButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                mainPresenter.getSectionPresenter().onInitSection();
            }
        });
        initLiteralButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                mainPresenter.getSectionPresenter().onInitLiteral();
            }
        });
        reciteAllButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                playList.clear();
                stopReciting();
                ClientSection section = mainPresenter.getCurrentSection();
                for (ClientVerse verse : section.verses.values()) {
                    if (verse.verseNo == 1 && verse.chapterNo != 1 && verse.chapterNo != 9) {
                        playList.add("1:1");
                    }
                    playList.add("" + verse.chapterNo + ":" + verse.verseNo);
                }
                recite();
            }
        });
        sectionAdminBar.setVisible(false);
        sectionAdminBar.add(initSectionButton);
        sectionAdminBar.add(spacer());
        sectionAdminBar.add(initLiteralButton);
        sectionAdminBar.add(spacer());
        sectionAdminBar.add(sectionText);
        sectionAdminBar.setWidth("100%");
        sectionNavigationBar.setWidth("100%");
        sectionPanel.setWidth("100%");
        sectionMainPanel.setWidth("100%");
        sectionMainPanel.add(sectionAdminBar);
        sectionMainPanel.add(sectionNavigationBar);
        sectionMainPanel.add(sectionPanel);
    }

    public void sectionPanelClear() {
        sectionPanel.clear();
    }

    public void showText(TreeMap<Integer, ClientVerse> verses) {
        FlowPanel textPanel = new FlowPanel();
        textPanel.setWidth("600");
        textPanel.addStyleName("arabic");
        textPanel.getElement().setAttribute("dir", "rtl");
        int chapterNo = verses.values().iterator().next().chapterNo;
        showChapterArabicName(chapterNo, textPanel);
        for (ClientVerse verse : verses.values()) {
            if (verse.verseNo == 1 && verse.chapterNo != 1 && verse.chapterNo != 9) {
                FlowPanel basmallahPanel = new FlowPanel();
                basmallahPanel.setWidth("100%");
                basmallahPanel.addStyleName("basmallah");
                showText(basmallahPanel, mainPresenter.getSectionPresenter().getBasmallah());
                textPanel.add(basmallahPanel);
            }
            showText(textPanel, verse);
            String token = "" + verse.chapterNo + ":" + verse.verseNo;
            InlineHTML number = new InlineHTML("<font color='gray'><sup><nobr>{" + arabicNumber(verse.verseNo) + "}</nobr></sup></font> ");
            addPlayRecitationHandler(token, number, true);
            textPanel.add(number);
        }
        sectionPanel.add(textPanel);
    }

    private boolean showChapterArabicName(int chapterNo, FlowPanel textPanel) {
        ClientChapter chapter = mainPresenter.getChapter(chapterNo);
        if (null == chapter) return false;
        textPanel.add(new HTML("سورة " + chapter.title));
        return true;
    }

    private String arabicNumber(int verseNo) {
        StringBuffer result = new StringBuffer();
        while (verseNo > 0) {
            char c = (char) (1632 + (verseNo % 10));
            result.insert(0, c);
            verseNo = verseNo / 10;
        }
        return result.toString();
    }

    private void addPlayRecitationHandler(String token, HTML widget, boolean showTitle) {
        widget.addClickHandler(playAudioHandler);
        if (showTitle) widget.setTitle("Click to recite");
        widget.getElement().setAttribute("name", token);
    }

    private static class ArabicWord {

        public String text;

        public String transliteration;

        public String literal;

        public String style;
    }

    private ArrayList<ArabicWord> getArabicWords(ClientVerse verse) {
        String[] arabicWords = rearrangeDiacritics(verse.text).split("[ ]");
        String[] transliterationWords = verse.transliteration.split("[ ]");
        String[] literalWords = htmlEscape(verse.literal).split("[ ]");
        ArrayList<ArabicWord> results = new ArrayList<ArabicWord>(arabicWords.length);
        int i = 0;
        for (String word : arabicWords) {
            if ("".equals(word)) break;
            ArabicWord result = new ArabicWord();
            result.text = word;
            if (word.length() == 1) {
                if ("ۖ".compareTo(word) <= 0 && "ۜ".compareTo(word) >= 0) {
                    result.style = "sign";
                } else if ("۩".equals(word) || "۞".equals(word)) {
                    result.style = "internal-sign";
                }
            } else {
                result.transliteration = transliterationWords[i];
                result.literal = literalWords[i];
                i++;
            }
            results.add(result);
        }
        return results;
    }

    /**
	 * @param literal
	 * @return
	 */
    private String htmlEscape(String literal) {
        return literal.replaceAll("[<]", "&lt;").replaceAll("[&]", "&amp;");
    }

    private void showText(FlowPanel textPanel, ClientVerse verse) {
        if (null == verse) return;
        String arabicText = verse.text;
        if (null == arabicText) return;
        String token = "" + verse.chapterNo + ":" + verse.verseNo;
        ArrayList<ArabicWord> arabicWords = getArabicWords(verse);
        ArrayList<HTML> textHtmls = new ArrayList<HTML>(arabicWords.size());
        for (ArabicWord arabicWord : arabicWords) {
            InlineHTML text = new InlineHTML(arabicWord.text + " ");
            textHtmls.add(text);
            if (null == arabicWord.transliteration) {
                if (null != arabicWord) text.addStyleName(arabicWord.style);
                addPlayRecitationHandler(token, text, true);
            } else {
                TooltipHandler handler = new TooltipHandler(arabicWord.transliteration.replaceAll("[<][s][>]", "<font color='gray'><s>").replaceAll("[<][/][s][>]", "</s></font>") + "<br/>" + arabicWord.literal, 10000, "tooltip");
                text.addMouseOverHandler(handler);
                text.addMouseOutHandler(handler);
                addPlayRecitationHandler(token, text, false);
            }
            textPanel.add(text);
        }
        textHtmlMap.put("" + verse.chapterNo + ":" + verse.verseNo, textHtmls);
    }

    /**
	 * allow more readable rendering of arabic text, such as:
	 * - KASRA and KASRATAN should be always under the base, instead of under the SHADDA
	 * This is on best-effort basis. Some system might keep the KASRA under the SHADDA
	 */
    private String rearrangeDiacritics(String text) {
        text = text.replaceAll("[ّ][ِ]", "ِّ");
        text = text.replaceAll("[ّ][ٍ]", "ٍّ");
        return text;
    }

    public void showTranslation(TreeMap<Integer, ClientVerse> verses) {
        FlowPanel textPanel = new FlowPanel();
        int chapterNo = verses.values().iterator().next().chapterNo;
        showChapterTranslationText(textPanel, chapterNo);
        TreeSet<String> topics = new TreeSet<String>();
        for (ClientVerse verse : verses.values()) {
            if (verse.verseNo == 1 && verse.chapterNo != 1 && verse.chapterNo != 9) {
                ClientVerse basmallah = mainPresenter.getSectionPresenter().getBasmallah();
                if (null == basmallah) break;
                String token = "" + basmallah.chapterNo + ":" + basmallah.verseNo;
                String text = basmallah.translation + " ";
                InlineHTML translationHTML = new InlineHTML(text);
                translationHtmlMap.put(token, translationHTML);
                addPlayRecitationHandler(token, translationHTML, true);
                textPanel.add(translationHTML);
            }
            String token = "" + verse.chapterNo + ":" + verse.verseNo;
            InlineHTML number = new InlineHTML("<font color='gray'><sup>" + token + "</sup></font> ");
            addPlayRecitationHandler(token, number, true);
            textPanel.add(number);
            String text = verse.translation + " ";
            InlineHTML translationHTML = new InlineHTML(text);
            translationHtmlMap.put(token, translationHTML);
            addPlayRecitationHandler(token, translationHTML, true);
            textPanel.add(translationHTML);
            if (null != verse.topics) {
                for (String topic : verse.topics.split("[,]")) {
                    if (topic.length() > 0) topics.add(topic);
                }
            }
        }
        textPanel.addStyleName("translation");
        StringBuffer topicString = new StringBuffer();
        for (String topic : topics) {
            topicString.append(topic);
            topicString.append(", ");
        }
        if (topicString.length() > 0) topicString.setLength(topicString.length() - ", ".length());
        textPanel.add(new HTML("Topics: " + topicString));
        sectionPanel.add(textPanel);
    }

    private boolean showChapterTranslationText(FlowPanel textPanel, int chapterNo) {
        ClientChapter chapter = mainPresenter.getChapter(chapterNo);
        if (null == chapter) return false;
        textPanel.add(new HTML("Chapter " + chapterNo + ": " + chapter.transliteration));
        return true;
    }

    public void setStatusText(String text) {
        sectionText.setText(text);
    }

    public void showAdminTab() {
        sectionAdminBar.setVisible(true);
        initSectionButton.setVisible(true);
        initLiteralButton.setVisible(true);
        sectionText.setVisible(true);
    }

    public Widget getPanel() {
        return sectionMainPanel;
    }

    private void recite() {
        if (playList.size() <= 0) return;
        currentToken = playList.remove(0);
        String[] tokens = currentToken.split("[:]");
        String filename = "000".substring(tokens[0].length()) + tokens[0] + "000".substring(tokens[1].length()) + tokens[1] + ".mp3";
        sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG, "media/" + filename);
        sound.addEventHandler(soundHandler);
        sound.play();
        ArrayList<HTML> textHtmls = textHtmlMap.get(currentToken);
        if (null == textHtmls) return;
        for (HTML textHTML : textHtmls) {
            textHTML.addStyleName("selected");
        }
        HTML translationHtml = translationHtmlMap.get(currentToken);
        if (null == translationHtml) return;
        translationHtml.addStyleName("selected");
    }
}
