package net.sourceforge.squirrel_sql.plugins.derby.tokenizer;

import net.sourceforge.squirrel_sql.fw.sql.IQueryTokenizer;
import net.sourceforge.squirrel_sql.fw.sql.ITokenizerFactory;
import net.sourceforge.squirrel_sql.fw.sql.QueryTokenizer;

/**
 * This class is loaded by the Derby Plugin and registered with all Derby 
 * Sessions as the query tokenizer if the plugin is loaded.  It handles some
 * of the syntax allowed in ij scripts that would be hard to parse in a 
 * generic way for any database.  Specifically, it handles "run 'script'" 
 * commands which 
 *  
 * @author manningr
 */
public class DerbyQueryTokenizer extends QueryTokenizer implements IQueryTokenizer {

    private static final String DERBY_SCRIPT_INCLUDE_PREFIX = "run ";

    public DerbyQueryTokenizer(String sep, String linecomment, boolean removeMultiLineComment) {
        super(sep, linecomment, removeMultiLineComment);
    }

    public void setScriptToTokenize(String script) {
        super.setScriptToTokenize(script);
        expandFileIncludes(DERBY_SCRIPT_INCLUDE_PREFIX);
        _queryIterator = _queries.iterator();
    }

    /**
     * Sets the ITokenizerFactory which is used to create additional instances
     * of the IQueryTokenizer - this is used for handling file includes
     * recursively.  
     */
    protected void setFactory() {
        _tokenizerFactory = new ITokenizerFactory() {

            public IQueryTokenizer getTokenizer() {
                return new DerbyQueryTokenizer(DerbyQueryTokenizer.this._querySep, DerbyQueryTokenizer.this._lineCommentBegin, DerbyQueryTokenizer.this._removeMultiLineComment);
            }
        };
    }
}
