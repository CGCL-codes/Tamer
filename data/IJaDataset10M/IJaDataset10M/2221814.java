package com.once.servicescout.parser.query;

/**
 *
 * @author cwchen
 * @Created 2008-1-11
 * @Contact comain@gmail.com
 */
public interface FuncQueryParser {

    FuncQueryRequest compile(String q);
}
