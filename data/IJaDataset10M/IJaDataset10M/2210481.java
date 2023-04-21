package org.jplate.parser.tabular.tdv;

import org.jplate.parser.tabular.TabularParserFactoryIfc;

/**
 
    Factory for creating implementations of TdvParserIfc's.


    <pre>
Modifications:
    $Date: 2007-09-19 15:23:51 -0400 (Wed, 19 Sep 2007) $
    $Revision: 348 $
    $Author: sfloess $
    $HeadURL: http://jplate.svn.sourceforge.net/svnroot/jplate/branches/parser-0.2/src/java/org/jplate/parser/tabular/tdv/TdvParserFactoryIfc.java $
    </pre>

*/
public interface TdvParserFactoryIfc<V extends TdvParserIfc> extends TabularParserFactoryIfc<V> {
}
