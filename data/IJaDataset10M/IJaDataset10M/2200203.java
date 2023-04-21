package org.jplate.kvp.parser;

import java.util.Map;
import org.jplate.foundation.parser.JPlateMapBuilderIfc;
import org.jplate.kvp.KvpIfc;

/**
 
    A map builder called when KVP (key value pair) tokens have been parsed.

    <pre>
Modifications:
    $Date: 2008-12-02 12:32:45 -0500 (Tue, 02 Dec 2008) $
    $Revision: 479 $
    $Author: sfloess $
    $HeadURL: http://jplate.svn.sourceforge.net/svnroot/jplate/trunk/src/dev/java/org/jplate/kvp/parser/KvpMapBuilderIfc.java $
    </pre>

*/
public interface KvpMapBuilderIfc extends KvpBuilderIfc<Map<String, String>>, JPlateMapBuilderIfc<String, String> {
}
