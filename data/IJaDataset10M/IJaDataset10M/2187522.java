package net.sf.jtmt.recognizers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import net.sf.jtmt.tokenizers.Token;
import net.sf.jtmt.tokenizers.TokenType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Combines co-located words into phrases by looking up a table of
 * common phrases.
 * @author Sujit Pal
 * @version $Revision: 2 $
 */
public class PhraseRecognizer implements IRecognizer {

    private final Log log = LogFactory.getLog(getClass());

    private JdbcTemplate jdbcTemplate;

    private Map<String, String> phraseMap;

    public PhraseRecognizer(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void init() throws Exception {
    }

    @SuppressWarnings("unchecked")
    public List<Token> recognize(List<Token> tokens) {
        List<Token> extractedTokens = new ArrayList<Token>();
        int numTokens = tokens.size();
        TOKEN_LOOP: for (int i = 0; i < numTokens; i++) {
            Token token = tokens.get(i);
            TokenType type = token.getType();
            if (type != TokenType.WORD) {
                extractedTokens.add(token);
                continue;
            }
            String word = token.getValue();
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("select coc_phrase, coc_num_words from my_colloc where coc_lead_word = ?", new String[] { StringUtils.lowerCase(word) });
            for (Map<String, Object> row : rows) {
                String phrase = (String) row.get("COC_PHRASE");
                int numWords = (Integer) row.get("COC_NUM_WORDS");
                if (numTokens > i + numWords) {
                    String inputPhrase = getInputPhrase(tokens, i, numWords);
                    if (phrase.equals(inputPhrase)) {
                        extractedTokens.add(new Token(phrase + "|||" + numWords, TokenType.PHRASE));
                        i += (numWords - 1);
                        continue TOKEN_LOOP;
                    }
                }
            }
            extractedTokens.add(token);
        }
        return extractedTokens;
    }

    private String getInputPhrase(List<Token> tokens, int start, int length) {
        List<Token> tokenSublist = tokens.subList(start, start + length);
        StringBuilder phraseBuilder = new StringBuilder();
        for (Token token : tokenSublist) {
            TokenType type = token.getType();
            phraseBuilder.append(token.getValue());
        }
        return phraseBuilder.toString();
    }
}
