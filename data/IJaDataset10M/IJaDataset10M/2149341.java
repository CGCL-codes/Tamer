package org.gocha.text.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author gocha
 */
public class PTools {

    /**
     * Фильтрует токены в списке.
     * @param toks токены
     * @param whiteSpaceTokens список игнорируемых токенов
     * @return Очищенный список
     */
    public static List<Token> filter(List<Token> toks, Class... whiteSpaceTokens) {
        if (toks == null) {
            throw new IllegalArgumentException("toks==null");
        }
        List<Token> removed = new ArrayList<Token>();
        Stack<Integer> r = new Stack<Integer>();
        for (Integer i = 0; i < toks.size(); i++) {
            Token t = toks.get(i);
            if (t == null) {
                r.push(i);
            } else {
                Class cls = t.getClass();
                for (Class wsC : whiteSpaceTokens) {
                    if (cls.equals(wsC)) {
                        r.push(i);
                    }
                }
            }
        }
        while (!r.empty()) {
            int i = r.pop();
            removed.add(toks.get(i));
            toks.remove(i);
        }
        return removed;
    }

    /**
     * Возвращает символ в указанной позиции текста
     * @param str Исходный текст
     * @param off Смещение в тексте
     * @param nullc Возвращаемый символ если превышены границы текста
     * @return Символ
     */
    public static char lookup(String str, int off, char nullc) {
        if (str == null) return nullc;
        if (off < 0) return nullc;
        if (str.length() == 0) return nullc;
        if (off >= str.length()) return nullc;
        return str.charAt(off);
    }

    /**
     * Проверяет текст на совпадение
     * @param text Искомый текст
     * @param src Исходный текст
     * @param offset Смещение в исходном тексте
     * @param ignoreCase Игнорировать регистр букв
     * @return true - Совпал; false - не совпал
     */
    public static boolean match(String text, String src, int offset, boolean ignoreCase) {
        if (text == null) return false;
        if (src == null) return false;
        if (offset >= src.length()) return false;
        if (offset < 0) return false;
        if (text.length() > (src.length() - offset)) return false;
        if (text.length() == 0) return true;
        for (int i = 0; i < text.length(); i++) {
            char ct = text.charAt(i);
            char cs = lookup(src, i + offset, ct);
            if (ignoreCase) {
                if (Character.isLetter(ct) && Character.isLetter(cs)) {
                    ct = Character.toLowerCase(ct);
                    cs = Character.toLowerCase(cs);
                    if (ct != cs) return false;
                } else {
                    if (ct != cs) return false;
                }
            } else {
                if (ct != cs) return false;
            }
        }
        return true;
    }
}
