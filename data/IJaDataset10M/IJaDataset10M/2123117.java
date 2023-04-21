package org.fudaa.dodico.rubar.io;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import org.fudaa.ctulu.CtuluLibMessage;
import org.fudaa.dodico.fortran.FileOpReadCharSimpleAbstract;
import org.fudaa.dodico.h2d.H2DLib;
import org.fudaa.dodico.h2d.resource.H2dResource;
import org.fudaa.dodico.mesure.EvolutionReguliere;

/**
 * 
 * @author CANEL Christophe (Genesis)
 *
 */
public class RubarVENReader extends FileOpReadCharSimpleAbstract {

    private int nbElt = -1;

    protected Object internalRead() {
        if (nbElt <= 0) {
            analyze_.addFatalError(H2dResource.getS("Le nombre d'�lements n'est pas pr�cis�"));
        }
        final RubarVENResult r = new RubarVENResult();
        final int[] idxApp = new int[nbElt];
        r.eltEvolIdx = idxApp;
        int max = 0;
        int ie = 0;
        try {
            int[] fmt = new int[10];
            Arrays.fill(fmt, 8);
            final int nbFieldByLine = fmt.length;
            int tmpOnLine = 0;
            in_.readFields(fmt);
            for (ie = 0; ie < nbElt; ie++) {
                if (tmpOnLine == (nbFieldByLine)) {
                    in_.readFields(fmt);
                    tmpOnLine = 0;
                }
                idxApp[ie] = in_.intField(tmpOnLine) - 1;
                if (idxApp[ie] > max) {
                    max = idxApp[ie];
                }
                tmpOnLine++;
            }
            final int nbCourbe = Integer.parseInt(in_.readLine().trim());
            if (nbCourbe <= max) {
                analyze_.addFatalError(H2dResource.getS("Des �l�ments utilisent du vent non d�fini"));
                return null;
            }
            r.evolsX = new EvolutionReguliere[nbCourbe];
            r.evolsY = new EvolutionReguliere[nbCourbe];
            fmt = new int[] { 15, 15, 15 };
            for (int i = 0; i < nbCourbe; i++) {
                final int nbPts = Integer.parseInt(in_.readLine().trim());
                r.evolsX[i] = new EvolutionReguliere(nbPts);
                r.evolsY[i] = new EvolutionReguliere(nbPts);
                for (int j = 0; j < nbPts; j++) {
                    in_.readFields(fmt);
                    r.evolsX[i].add(in_.doubleField(2), in_.doubleField(0));
                    r.evolsY[i].add(in_.doubleField(2), in_.doubleField(1));
                }
            }
        } catch (final EOFException e) {
            if (CtuluLibMessage.DEBUG) {
                CtuluLibMessage.debug("Fin du fichier");
            }
        } catch (final IOException e) {
            analyze_.manageException(e);
            return null;
        } catch (final NumberFormatException e) {
            analyze_.manageException(e, in_.getLineNumber());
            return null;
        }
        return r;
    }

    /**
   * @return le nombre d'element a lire
   */
    public int getNbElt() {
        return nbElt;
    }

    /**
   * @param _nbElt le nombre d'elements � lire
   */
    public void setNbElt(final int _nbElt) {
        nbElt = _nbElt;
    }
}
