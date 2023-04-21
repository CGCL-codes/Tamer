package ru;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class ATLine {

    public String line = "";

    public Vector samplers = null;

    public boolean in_string1 = false;

    public boolean in_string2 = false;

    public boolean in_comment = false;

    public ATLine(String ln, ATLine prev_line) {
        if (prev_line != null) {
            this.in_comment = prev_line.in_comment;
            this.in_string1 = prev_line.in_string1;
            this.in_string2 = prev_line.in_string2;
        }
        this.line = ln;
        this.samplers = new Vector();
    }

    public void correctHeight() {
    }

    public Style getStyle(int pos, StyleSheet st) {
        for (Enumeration e = this.samplers.elements(); e.hasMoreElements(); ) {
            int tmp = ((Sampler) e.nextElement()).getStyle(pos);
            if (tmp != -1) return st.getStyle(tmp);
        }
        return st.getStyle(StyleSheet.ST_DEFAULT);
    }

    public void resetLine(String s, Language lg) {
        this.line = s;
        this.reSample(lg);
    }

    public void draw(Graphics g, StyleSheet st, int y, int num) {
        for (int i = 0; i < this.line.length(); i++) {
            char tc = this.line.charAt(i);
            Style stl = this.getStyle(i, st);
            stl.drawCharacter(g, tc, i * stl.font_w, y * stl.font_h);
        }
    }

    public void insertString(String tc, int pos) {
        this.line = this.line.substring(0, pos) + tc + this.line.substring(pos);
    }

    public void remChar(int pos) {
        if (pos > 0) this.line = this.line.substring(0, pos - 1) + this.line.substring(pos);
    }

    public void reSample(Language lg) {
        this.correctHeight();
        this.in_string1 = false;
        this.in_string2 = false;
        boolean in_line_comment = false;
        boolean is_last = false;
        boolean escape = false;
        String buff = "";
        this.samplers.removeAllElements();
        int str_len = this.line.length();
        for (int i = 0; i < str_len; i++) {
            char tc = this.line.charAt(i);
            if (i == str_len - 1) is_last = true;
            if (is_last) {
                if (this.in_string1) {
                    buff += tc;
                    this.addSample(i + 1, buff, lg, StyleSheet.ST_QUOTE_1);
                    continue;
                } else if (this.in_string2) {
                    buff += tc;
                    this.addSample(i + 1, buff, lg, StyleSheet.ST_QUOTE_2);
                    continue;
                }
            }
            if (this.in_string1 || this.in_string2) {
                if (lg.isEscape(tc)) {
                    buff += tc;
                    escape = true;
                    continue;
                }
            }
            if (this.in_string1) {
                if (tc == '\'' && !escape) {
                    buff += tc;
                    this.addSample(i + 1, buff, lg, StyleSheet.ST_QUOTE_1);
                    buff = "";
                    this.in_string1 = false;
                } else {
                    if (escape) escape = false;
                    buff += tc;
                }
                continue;
            } else if (this.in_string2) {
                if (tc == '"' && !escape) {
                    buff += tc;
                    this.addSample(i + 1, buff, lg, StyleSheet.ST_QUOTE_2);
                    this.in_string2 = false;
                    buff = "";
                } else {
                    if (escape) escape = false;
                    buff += tc;
                }
                continue;
            } else if (this.in_comment) {
                continue;
            } else if (in_line_comment) {
                if (is_last) {
                    this.addSample(i, buff, lg, StyleSheet.ST_LINE_COMMET);
                    buff = "";
                } else {
                    buff += tc;
                    if (is_last) {
                        this.addSample(i, buff, lg, 0);
                    }
                }
                continue;
            }
            switch(tc) {
                case '\t':
                case '\n':
                case ' ':
                    this.addSample(i, buff, lg, 0);
                    buff = "";
                    break;
                case '"':
                    this.addSample(i, buff, lg, 0);
                    buff = "";
                    buff += tc;
                    if (is_last) {
                        this.addSample(i, buff, lg, StyleSheet.ST_QUOTE_2);
                    }
                    this.in_string2 = true;
                    break;
                case '\'':
                    this.addSample(i, buff, lg, 0);
                    buff = "";
                    buff += tc;
                    if (is_last) {
                        this.addSample(i, buff, lg, StyleSheet.ST_QUOTE_1);
                    }
                    this.in_string1 = true;
                    break;
                default:
                    if (lg.isOperator(tc)) {
                        this.addSample(i + 1, 1, lg, StyleSheet.ST_OPERATOR);
                        this.addSample(i, buff, lg, 0);
                        buff = "";
                    } else {
                        buff += tc;
                        if (is_last) this.addSample(i + 1, buff, lg, 0);
                    }
                    break;
            }
        }
    }

    private void addSample(int st, int len, Language lg, int type) {
        if (len == 0) return;
        this.samplers.addElement(new Sampler(st - len, len, type));
    }

    private void addSample(int st, String s, Language lg, int type) {
        int len = s.length();
        if (len == 0) return;
        int tp = type != 0 ? type : lg.getStringStyle(s);
        this.samplers.addElement(new Sampler(st - len, len, tp));
    }
}
