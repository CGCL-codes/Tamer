package com.listentothesong.print;

import com.listentothesong.bookmarks.LyricsDescriptor;
import com.listentothesong.bookmarks.LyricsDescriptor.ChallengeStatus;
import com.listentothesong.bookmarks.LyricsDescriptor.TextToken;

public class ListenToTheSongPage extends BasePage {

    private final String title;

    private final String subtitle;

    private final LyricsDescriptor lyricsDescriptor;

    public ListenToTheSongPage(String title, String subtitle, LyricsDescriptor lyricsDescriptor) {
        this.title = title;
        this.subtitle = subtitle;
        this.lyricsDescriptor = lyricsDescriptor;
    }

    @Override
    public String getHTMLDocument(Device device) {
        StringBuffer songHTML = new StringBuffer();
        songHTML.append("<p align='left'>");
        for (TextToken textAtom : lyricsDescriptor.getTokens()) {
            if (textAtom.toString().equals("\n")) songHTML.append("</p><p>");
            if (textAtom.toString().equals(" ")) songHTML.append("&nbsp;"); else if (textAtom.getChallengeStatus() == null) songHTML.append(textAtom); else songHTML.append(textAtom.toHiddenString('_'));
            songHTML.append(" ");
        }
        songHTML.append("</p>");
        return getHeading(device, title, subtitle) + songHTML + getTrailing();
    }
}
