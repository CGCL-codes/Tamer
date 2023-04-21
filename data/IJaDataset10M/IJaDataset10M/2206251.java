package org.fudaa.dodico.crue.io;

import org.fudaa.dodico.crue.io.common.CrueData;
import org.fudaa.dodico.crue.io.common.CrueFileType;
import org.fudaa.dodico.crue.io.dao.CrueXmlReaderWriterImpl;
import org.fudaa.dodico.crue.io.dcsp.CrueConverterDCSP;
import org.fudaa.dodico.crue.io.dcsp.CrueDaoDCSP;
import org.fudaa.dodico.crue.io.dcsp.CrueDaoStructureDCSP;
import org.fudaa.dodico.crue.projet.coeur.CoeurConfigContrat;

public class CrueFileFormatBuilderDCSP implements CrueFileFormatBuilder<CrueData> {

    @Override
    public Crue10FileFormat<CrueData> getFileFormat(CoeurConfigContrat version) {
        return new Crue10FileFormat<CrueData>(new CrueXmlReaderWriterImpl<CrueDaoDCSP, CrueData>(CrueFileType.DCSP, version, new CrueConverterDCSP(), new CrueDaoStructureDCSP(version.getXsdVersion())));
    }
}
