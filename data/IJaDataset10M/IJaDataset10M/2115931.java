package com.meidusa.amoeba.parser;

import java.util.Map;
import com.meidusa.amoeba.parser.function.Function;
import com.meidusa.amoeba.parser.dbobject.Schema;
import com.meidusa.amoeba.parser.statement.Statement;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public interface Parser {

    public Statement doParse() throws ParseException;

    public void setDefaultSchema(Schema schema);

    public void setFunctionMap(Map<String, Function> funMap);
}
