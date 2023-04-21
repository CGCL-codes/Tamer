package org.tockit.conscript.parser.sectionparsers;

import java.io.IOException;
import java.util.logging.Level;
import org.tockit.conscript.model.CSCFile;
import org.tockit.conscript.model.QueryMap;
import org.tockit.conscript.parser.CSCParser;
import org.tockit.conscript.parser.CSCTokenizer;
import org.tockit.conscript.parser.DataFormatException;

class QueryMapParser extends CSCFileSectionParser {

    @Override
    public String getStartToken() {
        return "QUERY_MAP";
    }

    @Override
    public void parse(CSCTokenizer tokenizer, CSCFile file) throws IOException, DataFormatException {
        String queryMapId = tokenizer.popCurrentToken();
        QueryMap queryMap = getQueryMap(file, queryMapId);
        int line = tokenizer.getCurrentLine();
        tokenizer.consumeToken("=");
        while (tokenizer.getCurrentLine() == line) {
            tokenizer.advance();
        }
        while (!tokenizer.getCurrentToken().equals(";")) {
            tokenizer.consumeToken("(");
            String clause = tokenizer.popCurrentToken();
            tokenizer.consumeToken(",");
            String id = tokenizer.popCurrentToken();
            tokenizer.consumeToken(")");
            queryMap.addEntry(clause, id);
        }
        tokenizer.consumeToken(";");
        queryMap.setInitialized();
        CSCParser.logger.log(Level.FINER, "Query map added: '" + queryMap.getName() + "'");
    }
}
