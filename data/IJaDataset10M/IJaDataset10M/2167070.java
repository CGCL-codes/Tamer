package com.luxoft.fitpro.htmleditor.adapters.fit;

import java.util.Set;
import java.util.TreeSet;
import com.luxoft.fitpro.htmleditor.adapters.messages.AdaptersMessages;
import com.luxoft.fitpro.htmleditor.core.parser.ParseException;
import com.luxoft.fitpro.htmleditor.core.parser.TestFileParser;
import fit.Parse;
import fit.exception.FitParseException;
import fitlibrary.table.Table;
import fitlibrary.table.Tables;

public class FitFileParser implements TestFileParser {

    private final FitFileSyntaxChecker checker = new FitFileSyntaxChecker();

    public FitFileParser() {
    }

    public Set<String> getFixtureClasses(String html) throws ParseException {
        Set<String> fixtureClasses = new TreeSet<String>();
        try {
            Parse parse = new Parse(html);
            Tables tables = new Tables(parse);
            if (checker.isDoFixture(parse)) {
                String fixtureClass = checker.getFixtureName(tables.table(0));
                if (fixtureClass != null) {
                    fixtureClasses.add(fixtureClass);
                }
            } else {
                for (int i = 0; i < tables.size(); i++) {
                    Table table = tables.table(i);
                    String fixtureClass = checker.getFixtureName(table);
                    if (fixtureClass != null) {
                        fixtureClasses.add(fixtureClass);
                    }
                }
            }
        } catch (FitParseException e) {
            throw new ParseException(AdaptersMessages.getMessage("htmleditor.unable_to_parse_tables"), e);
        }
        return fixtureClasses;
    }
}
