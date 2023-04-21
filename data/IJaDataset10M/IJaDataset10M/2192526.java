package org.fudaa.dodico.crue.io;

import org.fudaa.dodico.crue.io.common.CrueFileType;
import org.fudaa.dodico.crue.io.dao.CrueXmlReaderWriterImpl;
import org.fudaa.dodico.crue.io.optr.CrueConverterOPTR;
import org.fudaa.dodico.crue.io.optr.CrueDaoOPTR;
import org.fudaa.dodico.crue.io.optr.CrueDaoStructureOPTR;
import org.fudaa.dodico.crue.metier.emh.OrdPrtReseau;
import org.fudaa.dodico.crue.projet.coeur.CoeurConfigContrat;

public class CrueFileFormatBuilderOPTR implements CrueFileFormatBuilder<OrdPrtReseau> {

    @Override
    public Crue10FileFormat<OrdPrtReseau> getFileFormat(CoeurConfigContrat version) {
        return new Crue10FileFormat<OrdPrtReseau>(new CrueXmlReaderWriterImpl<CrueDaoOPTR, OrdPrtReseau>(CrueFileType.OPTR, version, new CrueConverterOPTR(), new CrueDaoStructureOPTR()));
    }
}
