package jreceiver.j2me.common.rec.source;

import java.util.Vector;
import jreceiver.j2me.common.rec.Rec;

/**
 * An interface describing an object that possesses a collection of
 * file identifiers.
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:08 $
 */
public interface SourceList extends Rec {

    public static final String HKEY_PL_SRC_ID = "PL_SRC_ID";

    public static final String HKEY_SOURCE_IDS = "SOURCE_IDS";

    public Vector getSourceIds();

    public void setSourceIds(Vector source_ids);
}
