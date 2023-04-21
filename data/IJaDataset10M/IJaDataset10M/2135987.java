package org.fudaa.dodico.crue.io.neuf;

import gnu.trove.TObjectIntHashMap;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.fudaa.dodico.crue.io.common.CrueData;
import org.fudaa.dodico.crue.io.neuf.STOSequentialReader.DimensionsModele;
import org.fudaa.dodico.crue.io.neuf.STOSequentialReader.ParametresGeneraux;

/**
 * Classe permettant de lire le fichier STR en le parcourant une première fois pour repérer la position de chaque profil
 * et leur nom associé
 * 
 * @author cde
 */
@SuppressWarnings("PMD.SystemPrintln")
public class STRReader extends AbstractCrueBinaryReader<STRSequentialReader> {

    @Override
    protected STRSequentialReader internalReadResu() throws IOException {
        final STRSequentialReader data = new STRSequentialReader();
        final CrueData crueData = this.getDataLinked();
        if (crueData == null || crueData.getSto() == null) {
            analyze_.addFatalError("io.str.lg.enreg.indefinie.error");
            return null;
        }
        final STOSequentialReader sto = crueData.getSto();
        final ParametresGeneraux paramsGen = sto.getParametresGeneraux();
        final DimensionsModele dimension = sto.getDimensionsModele();
        if (paramsGen == null || dimension == null) {
            analyze_.addFatalError("io.str.paramgen.dimension.null.error");
            return null;
        }
        data.byteOrder = sto.getOrder();
        data.npo = paramsGen.getNpo();
        data.nbStr = paramsGen.getNbstr();
        data.nbLitMax = paramsGen.getNblitmax();
        data.nbHaut = paramsGen.getNbhaut();
        data.file = file;
        int longueurEnregistrement = 49 + 2 * data.npo + (17 + 2 * data.nbStr) * data.nbLitMax + 9 * data.nbHaut;
        longueurEnregistrement = longueurEnregistrement * 4;
        final int longueurEnregistrementSTO = dimension.getLReclProf() * 4;
        if (longueurEnregistrement != longueurEnregistrementSTO) {
            analyze_.addWarn("io.str.lg.enreg.diff.error");
        }
        data.longueurEnregistrement = longueurEnregistrementSTO;
        final int nbProfil = (int) (helper.getChannel().size() / data.longueurEnregistrement);
        final int nbProfilStoRes = dimension.getNbRecProf();
        if (nbProfilStoRes != nbProfil) {
            analyze_.addError("io.str.nbProfil.wrong.error", Integer.valueOf(nbProfil));
            return null;
        }
        data.nomProfils = new ArrayList<String>(nbProfil);
        data.nomProfilPosition = new TObjectIntHashMap(nbProfil);
        final int lengthNom = 16;
        for (int i = 0; i < nbProfil; i++) {
            final ByteBuffer bf = ByteBuffer.allocate(lengthNom);
            helper.getChannel().read(bf, i * data.longueurEnregistrement);
            bf.rewind();
            String nom = STRSequentialReader.getStringFromBuffer(bf, lengthNom);
            data.nomProfils.add(nom);
            data.nomProfilPosition.put(nom, i);
        }
        return data;
    }
}
