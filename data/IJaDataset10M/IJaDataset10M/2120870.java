package jlikeruby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rbstring extends Rbarray<String> {

    String chars = new String();

    Rbstring(StringBuffer stringBuffer) {
        setChars(stringBuffer.toString());
    }

    Rbstring(char[] chars) {
        setChars(new String(chars));
    }

    Rbstring(Rbstring rbstring) {
        setChars(rbstring.getChars());
    }

    public Rbstring(String string) {
        setChars(string);
    }

    @Override
    public String toString() {
        return new String(getChars());
    }

    protected String getChars() {
        return chars;
    }

    protected void setChars(String chars) {
        this.chars = chars;
    }

    @Override
    protected ArrayList<String> getElements() {
        ArrayList<String> elements = new ArrayList<String>();
        for (int i = 0; i < getChars().length(); i++) push(getChars().substring(i, i + 1));
        return elements;
    }

    protected void setElements(ArrayList elements) {
        Rbarray<String> strings = new Rbarray<String>(elements);
        setChars(strings.join());
    }

    public Rbstring downcase() {
        Rbstring s = new Rbstring(this);
        s.chars.toLowerCase();
        return s;
    }

    public Rbstring downcaseThis() {
        chars = downcase().chars;
        return this;
    }

    public boolean isEmpty() {
        return chars.isEmpty();
    }

    public Rbstring gsub(String pattern2, String replacement) {
        Rbstring string = new Rbstring(getChars());
        if (pattern2.startsWith("/") && pattern2.endsWith("/")) pattern2 = pattern2.substring(1, pattern2.length() - 1);
        Pattern replace = Pattern.compile(pattern2);
        Matcher matcher = replace.matcher(getChars());
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String rpc = new String(replacement);
            rpc.replaceAll("\1", matcher.group());
            matcher.appendReplacement(sb, rpc);
        }
        matcher.appendTail(sb);
        string.setChars(sb.toString());
        return string;
    }

    public int lenght() {
        return chars.length();
    }

    public Rbstring replace(Rbstring otherString) {
        this.chars = otherString.chars;
        return this;
    }

    public Rbstring reverse() {
        StringBuffer buffer = new StringBuffer(chars);
        return new Rbstring(buffer.reverse());
    }

    public Rbstring reverseThis() {
        StringBuffer buffer = new StringBuffer(chars);
        setChars(buffer.reverse().toString());
        return this;
    }

    public int size() {
        return lenght();
    }

    public Rbarray<String> split(String regex) {
        return new Rbarray<String>(chars.split(regex));
    }

    public Rbarray<String> split(String regex, int limit) {
        return new Rbarray<String>(chars.split(regex, limit));
    }

    public Float toF() {
        try {
            return Float.parseFloat(chars);
        } catch (Exception e) {
            return new Float(0);
        }
    }

    public Integer toI(int base) {
        try {
            return Integer.parseInt(chars, base);
        } catch (Exception e) {
            return new Integer(0);
        }
    }

    public Integer toI() {
        return toI(10);
    }

    public Rbstring upcase() {
        Rbstring s = new Rbstring(this);
        s.chars.toUpperCase();
        return s;
    }

    public Rbstring upcaseThis() {
        chars = upcase().chars;
        return this;
    }
}
