package org.fudaa.dodico.crue.io;

import org.fudaa.dodico.crue.io.common.CrueData;
import org.fudaa.dodico.crue.io.common.CrueFileType;
import org.fudaa.dodico.crue.io.dao.CrueXmlReaderWriterImpl;
import org.fudaa.dodico.crue.io.rpti.CrueConverterRPTI;
import org.fudaa.dodico.crue.io.rpti.CrueDaoRPTI;
import org.fudaa.dodico.crue.io.rpti.CrueDaoStructureRPTI;
import org.fudaa.dodico.crue.projet.coeur.CoeurConfigContrat;

public class CrueFileFormatBuilderRPTI implements CrueFileFormatBuilder<CrueData> {

    @Override
    public Crue10FileFormat<CrueData> getFileFormat(final CoeurConfigContrat version) {
        return new Crue10FileFormat<CrueData>(new CrueXmlReaderWriterImpl<CrueDaoRPTI, CrueData>(CrueFileType.RPTI, version, new CrueConverterRPTI(), new CrueDaoStructureRPTI()));
    }
}
