package com.siemens.ct.exi.helpers;

import com.siemens.ct.exi.EXIFactory;
import com.siemens.ct.exi.GrammarFactory;
import com.siemens.ct.exi.SchemaIdResolver;
import com.siemens.ct.exi.exceptions.EXIException;
import com.siemens.ct.exi.grammar.Grammar;

/**
 * 
 * This is the default implementation of an <code>SchemaIdResolver</code> class.
 * 
 * <p>SchemaId is interpreted as file location</p>
 * 
 * @see EXIFactory
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */
public class DefaultSchemaIdResolver implements SchemaIdResolver {

    protected GrammarFactory gf;

    public DefaultSchemaIdResolver() {
    }

    protected GrammarFactory getGrammarFactory() {
        if (gf == null) {
            gf = GrammarFactory.newInstance();
        }
        return gf;
    }

    public Grammar resolveSchemaId(String schemaId) throws EXIException {
        if (schemaId == null) {
            return getGrammarFactory().createSchemaLessGrammar();
        } else if ("".equals(schemaId)) {
            return getGrammarFactory().createXSDTypesOnlyGrammar();
        } else {
            return getGrammarFactory().createGrammar(schemaId);
        }
    }
}
