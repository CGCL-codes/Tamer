package org.in4j.search.analyzer.sandbox;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

/**
 *
 * @author yangfan
 * @version 1.0 2007-6-15 下午11:05:53,创建
 */
public class In4jTokenizer extends TokenStream {

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(In4jTokenizer.class);

    private static final int MAX_COUNT = 8;

    private final Reader input;

    private final String text;

    private int pos = 0;

    private Dictionary dic;

    public In4jTokenizer(Reader input) {
        this.input = input;
        this.text = readToString(input);
    }

    private static String readToString(Reader reader) {
        StringBuffer sb = new StringBuffer(100);
        char[] buf = new char[128];
        int len;
        try {
            while ((len = reader.read(buf)) != -1) {
                sb.append(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            sb.setLength(0);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    public Token next() throws IOException {
        StringBuffer sb = new StringBuffer();
        final int length = text.length();
        int start = pos;
        out: while (pos < length) {
            int max = Math.min(start + MAX_COUNT, length);
            for (; pos < max; pos++) {
                char ch = text.charAt(pos);
                if (isSep(ch)) {
                    if (sb.length() > 0) {
                        break;
                    } else {
                        start = pos + 1;
                        max = Math.min(start + MAX_COUNT, length);
                        continue;
                    }
                }
                sb.append(ch);
            }
            if (isStartWithNumber(sb.toString())) {
                while (!isNumber(sb.toString())) {
                    if (sb.length() <= 1) break out;
                    sb.deleteCharAt(sb.length() - 1);
                    pos--;
                }
                if (isNumber(sb.toString())) {
                    break;
                }
            } else {
                while (!dic.isCJKWord(sb.toString())) {
                    if (sb.length() <= 1) break out;
                    sb.deleteCharAt(sb.length() - 1);
                    pos--;
                }
                if (dic.isCJKWord(sb.toString())) {
                    break;
                }
            }
        }
        if (sb.length() > 0) return new Token(sb.toString(), start, pos); else return null;
    }

    private boolean isStartWithNumber(String str) {
        return Pattern.matches("\\d.*", str);
    }

    private boolean isNumber(String str) {
        return Pattern.matches("\\d+", str);
    }

    private boolean isSep(char ch) {
        String str = ",.;\r\n《》？，。、：“；‘’”『』【】－―—─＝÷＋§·～！◎＃￥％…※×（）　";
        return str.indexOf(ch) != -1;
    }

    private boolean isChineseChar(char ch) {
        return Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
    }

    public void setDic(Dictionary dic) {
        this.dic = dic;
    }

    public static void main(String[] args) {
        String input = "自动分词自动分词.自动分词.自动分词.自动分词.自动分词.自动分词.自动分词.自动分词..";
        StringReader sr = new StringReader(input);
        String str = In4jTokenizer.readToString(sr);
        assert str != input;
        System.out.println(str);
    }
}
