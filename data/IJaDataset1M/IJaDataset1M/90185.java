package org.jplate.parser.tabular;

import org.jplate.parser.JPlateParserIfc;

/**
 
    Defines a parser for tabular data.  Tabular data is data organized in records
    and columns.  Each record has a delimiter as do columns - typically but both
    are not the same delimiters.

    <pre>
Modifications:
    $Date: 2007-09-19 15:23:51 -0400 (Wed, 19 Sep 2007) $
    $Revision: 348 $
    $Author: sfloess $
    $HeadURL: http://jplate.svn.sourceforge.net/svnroot/jplate/branches/parser-0.2/src/java/org/jplate/parser/tabular/TabularParserIfc.java $
    </pre>

    @param <B> A builder who can build tabular values.

*/
public interface TabularParserIfc<B extends TabularBuilderIfc> extends JPlateParserIfc<B> {
}
