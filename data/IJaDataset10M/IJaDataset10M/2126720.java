package net.sourceforge.transmogrify.symtab.test;

import junit.framework.*;
import junit.extensions.*;
import net.sourceforge.transmogrify.symtab.*;
import net.sourceforge.transmogrify.symtab.parser.*;
import java.io.*;

public class DefinitionLookupTest extends TestCase {

    private File[] _files;

    protected QueryEngine query;

    protected SymbolTable table;

    public DefinitionLookupTest(String name) {
        super(name);
    }

    public QueryEngine createQueryEngine(File[] files) throws Exception {
        FileParser parser = null;
        _files = files;
        parser = new FileParser();
        for (int i = 0; i < _files.length; i++) {
            parser.doFile(_files[i]);
        }
        TableMaker maker = new TableMaker((SymTabAST) (parser.getTree()));
        table = maker.getTable();
        query = new QueryEngine(table);
        return query;
    }

    protected IDefinition getDefinition(File file, String name, int lineNumber, int columnNumber) {
        return query.getDefinition(name, new Occurrence(file, lineNumber, columnNumber));
    }
}
