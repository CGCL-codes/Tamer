package tbl.tokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tbl.data.TokenWithOffset;

public class WhiteSpaceTokenizer {

    private static final String SINGLE_TOK_P = "([\\p{Punct}&&[^.-]]|(?:http|ftp|file):[\\.\\w\\+-/=&;]+[\\w/]|[\\w\\-\\.]+)(.*)";

    private static final String LAST_TOK_P = "([\\p{Punct}]|(?:http|ftp|file):[\\.\\w\\+-/=&;]+[\\w/]|[\\w\\-]+)(.*)";

    private static final Pattern tokPatt = Pattern.compile(SINGLE_TOK_P);

    private static final Pattern lastTokPatt = Pattern.compile(LAST_TOK_P);

    private static final String TEST = "The big, brown (\"cow\") jumped 'over' the full-moon\nin http://a.b.c/ and A123BCD. And this is another sentence!";

    public WhiteSpaceTokenizer() {
    }

    public String[] simplyTokenize(String in) {
        return in.split("\\s+");
    }

    public List<String> reTokenize(String[] wsts) {
        List<String> finalToks = new ArrayList<String>();
        Matcher m = null;
        String toDo;
        for (int i = 0; i < (wsts.length - 1); i++) {
            toDo = wsts[i];
            m = tokPatt.matcher(toDo);
            while (m.matches()) {
                finalToks.add(m.group(1));
                toDo = m.group(2);
                m = tokPatt.matcher(toDo);
            }
        }
        toDo = wsts[wsts.length - 1];
        m = lastTokPatt.matcher(toDo);
        while (m.matches()) {
            finalToks.add(m.group(1));
            toDo = m.group(2);
            m = lastTokPatt.matcher(toDo);
        }
        return finalToks;
    }

    public List<TokenWithOffset> alignToText(List<String> tokSeq, String wholeText) {
        int end = -1, start = 0;
        List<TokenWithOffset> toReturn = new ArrayList<TokenWithOffset>();
        for (String token : tokSeq) {
            start = wholeText.indexOf(token, end);
            end = start + token.length() - 1;
            toReturn.add(new TokenWithOffset(token, start));
        }
        return toReturn;
    }

    public List<TokenWithOffset> tokenizeText(String in) {
        String sentenceList = in;
        String[] sentArray = sentenceList.split("[\n\r\f]");
        List<String> allToks = new ArrayList<String>();
        for (String sent : sentArray) {
            allToks.addAll(reTokenize(simplyTokenize(sent)));
        }
        return alignToText(allToks, in);
    }

    public List<TokenWithOffset> tokenizeSentence(String in) {
        return alignToText(reTokenize(simplyTokenize(in)), in);
    }

    public static void main(String[] argv) throws IOException {
        WhiteSpaceTokenizer wst = new WhiteSpaceTokenizer();
        List<TokenWithOffset> toks = wst.tokenizeText(StdInReader.readFromStdIn());
        for (TokenWithOffset t : toks) {
            System.out.println(t.getText());
        }
    }
}
