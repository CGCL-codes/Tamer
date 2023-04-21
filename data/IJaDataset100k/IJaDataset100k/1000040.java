package org.fudaa.dodico.telemac.io;

/**
 * Classe qui d�finit les parametres li�s au format ScopT,ScopS et ScopGEN
 * 
 * @author Adrien Hadoux
 * 
 */
public class ScopeKeyWord {

    private final String version_ = "1.0";

    private final String blocCommentaireSorT_ = "'";

    private final String blocCommentaireGene_ = "/";

    public static final String TYPE_COURBE_TEMPORELLE = "ScopS";

    public static final String TYPE_COURBE_TEMPORELLE2 = "scops";

    public static final String TYPE_COURBE_TEMPORELLE3 = "SCOPS";

    public static final String TYPE_COURBE_TEMPORELLE4 = "SCOPES";

    public static final String TYPE_COURBE_TEMPORELLE5 = "scopes";

    public static final String TYPE_COURBE_SPATIALE = "ScopT";

    public static final String TYPE_COURBE_SPATIALE2 = "scopt";

    public static final String TYPE_COURBE_SPATIALE3 = "scopet";

    public static final String TYPE_COURBE_SPATIALE4 = "SCOPT";

    public static final String TYPE_COURBE_SPATIALE5 = "SCOPET";

    public static final String TYPE_COURBE_MIXTE = "SCOPGENE";

    public static final String TYPE_COURBE_MIXTE2 = "scopgene";

    public static final String TYPE_COURBE_MIXTE3 = "scopegene";

    public static final String TYPE_COURBE_MIXTE4 = "SCOPEGENE";

    public static final String TYPE_COURBE_MIXTE5 = "ScopGEn";

    public static final String SYMBOL_VALUE_UNDEFINED = "*";

    public static final long VALUE_UNDEFINED = 9999999;

    public ScopeKeyWord() {
    }

    /**
   * @return Version
   */
    public String getVersion() {
        return version_;
    }

    public boolean isUndefined(String val) {
        return SYMBOL_VALUE_UNDEFINED.equals(val);
    }

    /**
   * @return BlocCommentaire
   */
    public String getBlocCommentaireSorT() {
        return blocCommentaireSorT_;
    }

    public String getBlocCommentaireGENE() {
        return blocCommentaireGene_;
    }

    /**
   * Verifie que lon est pas en presence d un commentaire a la fois pour les
   * formats s et t
   * 
   * @param _t
   * @return BlocCommentaire
   */
    public boolean isBlocCommentaireSorT(final String _t) {
        if (_t == null) return false;
        return _t.startsWith(blocCommentaireSorT_);
    }

    public boolean isBlocCommentaireGENE(final String _t) {
        if (_t == null) return false;
        return _t.startsWith(blocCommentaireGene_);
    }

    /**
   * retourne la bonen chaine separator en fonction de la valeur de iparam
   * envoy�e.
   * 
   * @param IPARAM
   * @return
   */
    public String getScopeGENESeparator(int IPARAM) {
        switch(IPARAM) {
            case 0:
                return "TEMPS =";
            case 1:
                return "X =";
            case 2:
                return "Y =";
            case 3:
                return "Z =";
        }
        return null;
    }

    public int getScopeGENESeparator(String debutLigne) {
        if (debutLigne.startsWith("TEMPS =")) return 0; else if (debutLigne.startsWith("X =")) return 1; else if (debutLigne.startsWith("Y =")) return 2; else if (debutLigne.startsWith("Z =")) return 3; else return 0;
    }
}
