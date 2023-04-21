package org.fudaa.dodico.mascaret;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.fudaa.dodico.corba.mascaret.SResultatsTemporelSpatial;
import org.fudaa.dodico.corba.mascaret.SResultatsTemporelSpatialBief;
import org.fudaa.dodico.fortran.FortranBinaryOutputStream;

/**
 * Classe permettant d'�crire un fichier r�sultat au format RUBENS non permanent
 * (binaire) � partir d'un ��SResultatsTemporelSpatial��.
 * @version      $Revision: 1.8 $ $Date: 2007-11-20 11:43:01 $ by $Author: bmarchan $
 * @author       Jean-Marc Lacombe
 */
public class Rubens1DNonPermWriter {

    private FortranBinaryOutputStream os_;

    private FileOutputStream fos_;

    private static final String NOM_FIN = "FIN ";

    private static final String NOM_IDEB_BIEF = "I1  ";

    private static final String NOM_IFIN_BIEF = "I2  ";

    private static final String TITRE = "                                                                        ";

    private int nbSectionTotal_ = 0;

    public Rubens1DNonPermWriter(File file) throws FichierMascaretException {
        try {
            fos_ = new FileOutputStream(file);
            os_ = new FortranBinaryOutputStream(new BufferedOutputStream(fos_), true);
        } catch (IOException ex) {
            throw new FichierMascaretException("", "Probl�me d'entr�e-sortie sur le fichier : " + file.getName());
        }
    }

    public void write(SResultatsTemporelSpatial sres) throws FichierMascaretException {
        try {
            writeStringRecord(TITRE);
            writeStringRecord(TITRE);
            writeStringRecord(TITRE);
            writeStringRecord(NOM_IDEB_BIEF);
            writeStringRecord(NOM_IFIN_BIEF);
            writeStringRecord(NOM_FIN);
            boolean resultatsMascaret = (!sres.resultatsCasier && !sres.resultatsLiaison);
            if (resultatsMascaret) {
                int nbBief = sres.resultatsBiefs.length;
                writeIntegerRecord(nbBief, nbBief);
                int[] numSectionOrigine = getNumSectionOrigine(sres.resultatsBiefs);
                writeIntegerRecord(numSectionOrigine);
                int[] numSectionFin = getNumSectionFin(sres.resultatsBiefs);
                writeIntegerRecord(numSectionFin);
                ArrayList indicesVarIndependantTps = new ArrayList();
                ArrayList indicesVarDependantTps = new ArrayList();
                int nbVarTotal = sres.variables.length;
                ecritureNomsVarIndepTps(sres, indicesVarIndependantTps, indicesVarDependantTps, nbVarTotal);
                nbSectionTotal_ = getNbSectionTotal(sres.resultatsBiefs);
                ecritureValeursIndepTps(sres, indicesVarIndependantTps);
                int nbVarDependantTps = indicesVarDependantTps.size();
                ecritureNomsvarDepTps(sres, indicesVarDependantTps, nbVarDependantTps);
                ecritureValeursDepTps(sres, indicesVarDependantTps, nbVarDependantTps);
            } else {
                writeIntegerRecord(1, 1);
                writeIntegerRecord(1);
                int nbCasierLiaison = sres.resultatsBiefs.length;
                writeIntegerRecord(nbCasierLiaison);
                ArrayList indicesVarIndependantTps = new ArrayList();
                ArrayList indicesVarDependantTps = new ArrayList();
                int nbVarTotal = sres.variables.length;
                ecritureNomsVarIndepTps(sres, indicesVarIndependantTps, indicesVarDependantTps, nbVarTotal);
                nbSectionTotal_ = -1;
                ecritureValeursIndepTpsCasierLiaison(sres, indicesVarIndependantTps);
                int nbVarDependantTps = indicesVarDependantTps.size();
                ecritureNomsvarDepTps(sres, indicesVarDependantTps, nbVarDependantTps);
                ecritureValeursDepTpsCasierLiaison(sres, indicesVarDependantTps, nbVarDependantTps);
            }
            os_.close();
            fos_.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new FichierMascaretException(null, ex.getMessage());
        }
    }

    private void ecritureValeursDepTps(SResultatsTemporelSpatial sres, ArrayList indicesVarDependantTps, int nbVarDependantTps) throws IOException {
        for (int i = 0; i < sres.pasTemps.length; i++) {
            writeIntegerRecord(i + 1, i + 1);
            float tps = (float) sres.pasTemps[i];
            writeFloatRecord(tps, tps);
            writeIntegerRecord(nbSectionTotal_, nbSectionTotal_);
            for (int j = 0; j < nbVarDependantTps; j++) {
                int indiceVar = ((Integer) indicesVarDependantTps.get(j)).intValue();
                float[] valeurs = getValeursVar(indiceVar, i, sres.resultatsBiefs);
                writeFloatRecord(valeurs);
            }
        }
    }

    private void ecritureNomsvarDepTps(SResultatsTemporelSpatial sres, ArrayList indicesVarDependantTps, int nbVarDependantTps) throws IOException {
        for (int i = 0; i < nbVarDependantTps; i++) {
            int indiceVar = ((Integer) indicesVarDependantTps.get(i)).intValue();
            String nomVar = sres.variables[indiceVar].nomCourt;
            writeStringRecord(to4Char(nomVar));
        }
        writeStringRecord(NOM_FIN);
    }

    private void ecritureValeursIndepTps(SResultatsTemporelSpatial sres, ArrayList indicesVarIndependantTps) throws IOException {
        writeIntegerRecord(nbSectionTotal_, nbSectionTotal_);
        float[] abscisses = getAbscisse(sres.resultatsBiefs);
        writeFloatRecord(abscisses);
        int nbVarIndependantTps = indicesVarIndependantTps.size();
        for (int i = 0; i < nbVarIndependantTps; i++) {
            int indiceVar = ((Integer) indicesVarIndependantTps.get(i)).intValue();
            float[] valeurs = getValeursVar(indiceVar, 0, sres.resultatsBiefs);
            writeFloatRecord(valeurs);
        }
    }

    private void ecritureNomsVarIndepTps(SResultatsTemporelSpatial sres, ArrayList indicesVarIndependantTps, ArrayList indicesVarDependantTps, int nbVarTotal) throws IOException {
        writeStringRecord(to4Char("X"));
        for (int i = 0; i < nbVarTotal; i++) {
            String nomVar = sres.variables[i].nomCourt;
            if (DescriptionVariables.isIndependantTps(nomVar)) {
                writeStringRecord(to4Char(nomVar));
                indicesVarIndependantTps.add(new Integer(i));
            } else {
                indicesVarDependantTps.add(new Integer(i));
            }
        }
        writeStringRecord(NOM_FIN);
    }

    private void ecritureValeursIndepTpsCasierLiaison(SResultatsTemporelSpatial sres, ArrayList indicesVarIndependantTps) throws IOException {
        int nbCasierLiaison = sres.resultatsBiefs.length;
        writeIntegerRecord(nbCasierLiaison, nbCasierLiaison);
        for (int i = 0; i < nbCasierLiaison; i++) {
            os_.writeReal((i + 1));
        }
        os_.writeRecord();
        int nbVar = indicesVarIndependantTps.size();
        for (int i = 0; i < nbVar; i++) {
            for (int j = 0; j < nbCasierLiaison; j++) {
                SResultatsTemporelSpatialBief resCasierLiaisoni = sres.resultatsBiefs[j];
                int indiceVar = ((Integer) indicesVarIndependantTps.get(i)).intValue();
                float val = (float) resCasierLiaisoni.valeursVariables[indiceVar][0][0];
                os_.writeReal(val);
            }
            os_.writeRecord();
        }
    }

    private void ecritureValeursDepTpsCasierLiaison(SResultatsTemporelSpatial sres, ArrayList indicesVarIndependantTps, int nbVarDependantTps) throws IOException {
        int nbCasierLiaison = sres.resultatsBiefs.length;
        int nbVar = indicesVarIndependantTps.size();
        int nbPasTps = sres.pasTemps.length;
        for (int i = 0; i < nbPasTps; i++) {
            writeIntegerRecord(i + 1, i + 1);
            float tps = (float) sres.pasTemps[i];
            writeFloatRecord(tps, tps);
            writeIntegerRecord(nbCasierLiaison, nbCasierLiaison);
            for (int j = 0; j < nbVar; j++) {
                for (int k = 0; k < nbCasierLiaison; k++) {
                    SResultatsTemporelSpatialBief resCasierLiaisoni = sres.resultatsBiefs[k];
                    int indiceVar = ((Integer) indicesVarIndependantTps.get(j)).intValue();
                    float val = (float) resCasierLiaisoni.valeursVariables[indiceVar][i][0];
                    os_.writeReal(val);
                }
                os_.writeRecord();
            }
        }
    }

    private int[] getNumSectionOrigine(SResultatsTemporelSpatialBief[] tabResBief) {
        int nbBief = tabResBief.length;
        int[] res = new int[nbBief];
        int sommeSection = 0;
        for (int i = 0; i < nbBief; i++) {
            res[i] = sommeSection + 1;
            sommeSection += tabResBief[i].abscissesSections.length;
        }
        return res;
    }

    private int[] getNumSectionFin(SResultatsTemporelSpatialBief[] tabResBief) {
        int nbBief = tabResBief.length;
        int[] res = new int[nbBief];
        int sommeSection = 0;
        for (int i = 0; i < nbBief; i++) {
            sommeSection += tabResBief[i].abscissesSections.length;
            res[i] = sommeSection;
        }
        return res;
    }

    private int getNbSectionTotal(SResultatsTemporelSpatialBief[] tabResBief) {
        int res = 0;
        for (int i = 0; i < tabResBief.length; i++) {
            res += tabResBief[i].abscissesSections.length;
        }
        return res;
    }

    private float[] getAbscisse(SResultatsTemporelSpatialBief[] tabResBief) {
        float[] res = new float[nbSectionTotal_];
        int indiceRes = 0;
        for (int i = 0; i < tabResBief.length; i++) {
            SResultatsTemporelSpatialBief resBiefi = tabResBief[i];
            double[] abscisses = resBiefi.abscissesSections;
            for (int j = 0; j < abscisses.length; j++) {
                res[indiceRes] = (float) abscisses[j];
                indiceRes++;
            }
        }
        return res;
    }

    private float[] getValeursVar(int indiceVar, int indicePasTps, SResultatsTemporelSpatialBief[] tabResBief) {
        float[] res = new float[nbSectionTotal_];
        int indiceRes = 0;
        for (int i = 0; i < tabResBief.length; i++) {
            SResultatsTemporelSpatialBief resBiefi = tabResBief[i];
            double[] valeurs = resBiefi.valeursVariables[indiceVar][indicePasTps];
            for (int j = 0; j < valeurs.length; j++) {
                res[indiceRes] = (float) valeurs[j];
                indiceRes++;
            }
        }
        return res;
    }

    private void writeStringRecord(String chaine) throws IOException {
        os_.writeCharacter(chaine);
        os_.writeRecord();
    }

    private void writeIntegerRecord(int entier) throws IOException {
        os_.writeInteger(entier);
        os_.writeRecord();
    }

    private void writeIntegerRecord(int entier1, int entier2) throws IOException {
        os_.writeInteger(entier1);
        os_.writeInteger(entier2);
        os_.writeRecord();
    }

    private void writeIntegerRecord(int[] tab) throws IOException {
        for (int i = 0; i < tab.length; i++) {
            os_.writeInteger(tab[i]);
        }
        os_.writeRecord();
    }

    private void writeFloatRecord(float reel1, float reel2) throws IOException {
        os_.writeReal(reel1);
        os_.writeReal(reel2);
        os_.writeRecord();
    }

    private void writeFloatRecord(float[] tab) throws IOException {
        for (int i = 0; i < tab.length; i++) {
            os_.writeReal(tab[i]);
        }
        os_.writeRecord();
    }

    private static final String to4Char(String chaine) {
        int nbChar = chaine.length();
        if (nbChar > 4) {
            return chaine;
        } else if (nbChar == 4) {
            return chaine;
        } else if (nbChar == 3) {
            return chaine + " ";
        } else if (nbChar == 2) {
            return chaine + "  ";
        } else if (nbChar == 1) {
            return chaine + "   ";
        } else {
            throw new IllegalArgumentException("completeEspace(String chaine) : chaine vide");
        }
    }

    public static void main(String[] args) {
        try {
            String nomFichier = "mascaret727Mo.opt";
            if (args.length > 0) {
                File file = new File(args[0]);
                if (file.exists()) {
                    nomFichier = args[0];
                }
            }
            File file = new File(nomFichier);
            Opthyca1DReader optReader = new Opthyca1DReader(file);
            SResultatsTemporelSpatial res = optReader.read();
            Rubens1DNonPermWriter rubWriter = new Rubens1DNonPermWriter(new File("bis" + nomFichier + ".rub"));
            rubWriter.write(res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
