package com.antlersoft.ilanalyze.parseildasm;

import com.antlersoft.ilanalyze.DBDriver;
import com.antlersoft.query.BasicBase;
import com.antlersoft.parser.ParseState;
import com.antlersoft.parser.Parser;
import com.antlersoft.parser.Symbol;

/**
 * Base class for parser that parse output of resource reader
 * @author Michael A. MacDonald
 *
 */
public class ResourceParserBase extends Parser {

    DBDriver m_driver;

    static Symbol literalString = BasicBase.literalString;

    static DBDriver Driver(Parser p) {
        return ((ResourceParserBase) p).m_driver;
    }

    /**
	 * @param states
	 */
    public ResourceParserBase(ParseState[] states) {
        super(states);
    }

    void setDriver(DBDriver driver) {
        m_driver = driver;
    }
}
