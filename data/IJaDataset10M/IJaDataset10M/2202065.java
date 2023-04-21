package ru.artlebedev.typograf.rule.word;

import ru.artlebedev.typograf.info.CharsInfo;
import ru.artlebedev.typograf.model.Word;
import ru.artlebedev.typograf.rule.Rule;

/**
 * Created by IntelliJ IDEA.
 * User: anton
 * Date: 16.06.2009
 * Time: 13:45:23
 *
 * текст 12 212 212 млрд куб. м текст
 * текст 121 212 212 млрд кв. м текст
 */
public class MeasureRule extends Rule implements WordRule {

    private static final Word cu = new Word("куб.");

    private static final Word sq = new Word("кв.");

    private static final Word bn = new Word("млрд");

    public void process() {
        if (p.word.value.length() > 4) {
            return;
        }
        if ((p.word.equals(cu) || p.word.equals(sq)) && p.c == CharsInfo.space) {
            p.source[p.charIndex] = CharsInfo.noBreakSpace;
            return;
        }
        if (p.word.equals(bn) && p.c == CharsInfo.space && p.charIndex - 5 > 0 && p.source[p.charIndex - 5] == CharsInfo.space) {
            p.source[p.charIndex - 5] = CharsInfo.noBreakSpace;
        }
    }
}
