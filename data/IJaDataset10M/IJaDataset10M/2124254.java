package org.fudaa.dodico.crue.io.log;

import org.fudaa.ctulu.CtuluLog;
import org.fudaa.dodico.crue.io.dao.CrueXmlReaderWriterImpl;

/**
 * @author CANEL Christophe (Genesis)
 *
 */
public class CrueLOGReaderWriter extends CrueXmlReaderWriterImpl<CrueDaoLOG, CtuluLog> {

    public CrueLOGReaderWriter(String version) {
        super("log", version, new CrueConverterLOG(), new CrueDaoStructureLOG());
    }
}
