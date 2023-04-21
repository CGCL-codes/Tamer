package whf.framework.lucene.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.TreeMap;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

/**
 * @author solo L
 * 
 */
@SuppressWarnings("unchecked")
public class CJKTokenizer extends Tokenizer {

    private static TreeMap simWords = null;

    private static final int IO_BUFFER_SIZE = 256;

    private int bufferIndex = 0;

    private int dataLen = 0;

    private final char[] ioBuffer = new char[IO_BUFFER_SIZE];

    private String tokenType = "word";

    public CJKTokenizer(Reader input) {
        this.input = input;
    }

    public Token next() throws IOException {
        loadWords();
        StringBuffer currentWord = new StringBuffer();
        while (true) {
            char c;
            Character.UnicodeBlock ub;
            if (bufferIndex >= dataLen) {
                dataLen = input.read(ioBuffer);
                bufferIndex = 0;
            }
            if (dataLen == -1) {
                if (currentWord.length() == 0) {
                    return null;
                } else {
                    break;
                }
            } else {
                c = ioBuffer[bufferIndex++];
                ub = Character.UnicodeBlock.of(c);
            }
            if (Character.isLetter(c) && ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                tokenType = "double";
                if (currentWord.length() == 0) {
                    currentWord.append(c);
                } else {
                    String temp = (currentWord.toString() + c).intern();
                    if (simWords.containsKey(temp)) {
                        currentWord.append(c);
                    } else {
                        bufferIndex--;
                        break;
                    }
                }
            }
        }
        Token token = new Token(currentWord.toString(), bufferIndex - currentWord.length(), bufferIndex, tokenType);
        currentWord.setLength(0);
        return token;
    }

    public void loadWords() {
        if (simWords != null) return;
        simWords = new TreeMap();
        try {
            InputStream words = CJKTokenizer.class.getResourceAsStream("simchinese.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(words, "UTF-8"));
            String word = null;
            while ((word = in.readLine()) != null) {
                if ((word.indexOf("#") == -1) && (word.length() < 5)) {
                    simWords.put(word.intern(), "1");
                    if (word.length() == 3) {
                        if (!simWords.containsKey(word.substring(0, 2).intern())) {
                            simWords.put(word.substring(0, 2).intern(), "2");
                        }
                    }
                    if (word.length() == 4) {
                        if (!simWords.containsKey(word.substring(0, 2).intern())) {
                            simWords.put(word.substring(0, 2).intern(), "2");
                        }
                        if (!simWords.containsKey(word.substring(0, 3).intern())) {
                            simWords.put(word.substring(0, 3).intern(), "2");
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
