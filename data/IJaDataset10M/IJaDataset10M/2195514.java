package org.peertrust.parser.peertrust;

/**
 * <p>
 * This class represents structure of token for both parsers
 * </p><p>
 * $Id: InputToken.java,v 1.2 2005/08/07 12:06:54 dolmedilla Exp $
 * <br/>
 * Date: 15-Aug-2004
 * <br/>
 * Last changed: $Date: 2005/08/07 12:06:54 $
 * by $Author: dolmedilla $
 * </p>
 * @author Bogdan Vlasenko
 */
public class InputToken {

    public int kind, beginLine, beginColumn, endLine, endColumn;

    public String image;
}
