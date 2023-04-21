package nps.compiler;

import nps.util.Utils;
import java.io.PrintWriter;

/**
 *  ֻ���wordlimit���ַ�,htmlreplaceΪtrueʱ��ʾ�滻�ı��е�<��>��&��"�����ı��������滻
 *   return = 0 �������Լ���д��
 *   return = -1 �����Ѿ�����
 *     ���wordlimit<=0�����������ַ�ĸ���
 *  a new publishing system
 *  Copyright (c) 2007
 *
 * @author jialin
 * @version 1.0
 */
public class WordLimitWriter {

    private PrintWriter out = null;

    private int wordcount = 0;

    private int wordlimit = 0;

    private boolean htmlreplace = false;

    private boolean bAppend = false;

    private String append = "";

    public WordLimitWriter(PrintWriter out) {
        this.out = out;
    }

    public WordLimitWriter(PrintWriter out, int wordlimit, String append) {
        this.out = out;
        this.wordlimit = wordlimit;
        this.append = append;
        if (append != null && append.length() > 0) bAppend = true;
    }

    public void SetHtmlReplaced(boolean isReplacable) {
        this.htmlreplace = isReplacable;
    }

    public int write(int c) {
        if ((wordlimit > 0) && (wordcount > wordlimit)) {
            append();
            return -1;
        }
        out.write(c);
        wordcount++;
        return 0;
    }

    public int write(char buf[], int off, int len) {
        if ((wordlimit > 0) && (wordcount > wordlimit)) {
            append();
            return -1;
        }
        if (wordlimit <= 0) {
            out.write(buf, off, len);
            return 0;
        }
        len = len < (wordlimit - wordcount) ? len : wordlimit - wordcount;
        if (htmlreplace) {
            String html_str = new String(buf, off, len);
            html_str = Utils.TransferToHtmlEntity(html_str);
            out.write(html_str);
        } else {
            out.write(buf, off, len);
        }
        wordcount += len;
        return 0;
    }

    public int write(char buf[]) {
        if ((wordlimit > 0) && (wordcount > wordlimit)) {
            append();
            return -1;
        }
        if (wordlimit <= 0) {
            out.write(buf);
            return 0;
        }
        int len = buf.length < (wordlimit - wordcount) ? buf.length : wordlimit - wordcount;
        if (htmlreplace) {
            this.write(buf, 0, len);
        } else {
            out.write(buf, 0, len);
        }
        wordcount += len;
        return 0;
    }

    public int write(String s, int off, int len) {
        if ((wordlimit > 0) && (wordcount > wordlimit)) {
            append();
            return -1;
        }
        if (wordlimit <= 0) {
            out.write(s, off, len);
            return 0;
        }
        len = len < (wordlimit - wordcount) ? len : wordlimit - wordcount;
        if (htmlreplace) {
            String html_str = s.substring(off, off + len);
            html_str = Utils.TransferToHtmlEntity(html_str);
            out.write(html_str);
        } else {
            out.write(s, off, len);
        }
        wordcount += len;
        return 0;
    }

    public int write(String s) {
        if ((wordlimit > 0) && (wordcount > wordlimit)) return -1;
        if (wordlimit <= 0 || (s.length() <= (wordlimit - wordcount))) {
            if (htmlreplace) {
                out.write(Utils.TransferToHtmlEntity(s));
                wordcount += s.length();
            } else {
                out.write(s);
            }
            return 0;
        } else {
            if (htmlreplace) {
                this.write(s, 0, wordlimit - wordcount);
                wordcount = wordlimit;
            } else {
                out.write(s, 0, wordlimit - wordcount);
            }
            append();
            return -1;
        }
    }

    private void append() {
        if (bAppend) {
            out.write(append);
            bAppend = false;
        }
    }
}
