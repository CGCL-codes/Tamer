package com.leff.midi.event.meta;

public class Lyrics extends TextualMetaEvent {

    public Lyrics(long tick, long delta, String lyric) {
        super(tick, delta, MetaEvent.LYRICS, lyric);
    }

    public void setLyric(String t) {
        setText(t);
    }

    public String getLyric() {
        return getText();
    }
}
