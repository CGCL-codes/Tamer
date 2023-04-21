package com.doculibre.intelligid.extraction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import com.doculibre.intelligid.entites.UniteAdministrative;
import com.doculibre.intelligid.utils.FGDConfig;
import com.doculibre.intelligid.xml.ConvertisseurXMLUnitesAdministratives;

public class ExtracteurUnitesAdministratives extends Extracteur {

    public List<UniteAdministrative> extraireUnitesAdministratives(File fichierCoba) {
        List<UniteAdministrative> unitesAdministratives = new ArrayList<UniteAdministrative>();
        DocumentTexte documentTexte = new DocumentTexte(fichierCoba.getAbsolutePath());
        int i = 0;
        while (documentTexte.getLigne(i++) != null) {
            String code = getCode(i, documentTexte);
            if (code != null) {
                String nom = getNom(i, documentTexte);
                UniteAdministrative uniteAdministrative = new UniteAdministrative();
                uniteAdministrative.setCode(code);
                uniteAdministrative.setNom(nom);
                unitesAdministratives.add(uniteAdministrative);
            }
        }
        return unitesAdministratives;
    }

    private void xmlDocument(List<UniteAdministrative> unitesAdministratives, File fichierOuput) {
        ConvertisseurXMLUnitesAdministratives convertisseur = new ConvertisseurXMLUnitesAdministratives();
        Document doc = convertisseur.toJdomDocument(unitesAdministratives);
        saveInXmlFile(doc, fichierOuput);
    }

    /**
	 * Entre colonnes 3 (incl.) et 9 (excl.)
	 * 
	 * @param indexLigne
	 * @param documentTexte
	 * @return
	 */
    private String getCode(int indexLigne, DocumentTexte documentTexte) {
        String code;
        int indexOfFirstChar = documentTexte.indexOfFirstChar(indexLigne);
        if (indexOfFirstChar == 3) {
            String premiersChars = documentTexte.getTexte(indexLigne, 3, 9);
            try {
                if (premiersChars != null) {
                    Integer.parseInt(premiersChars.trim());
                    code = premiersChars.trim();
                } else {
                    code = null;
                }
            } catch (NumberFormatException e) {
                if ("XXXXXX".equals(premiersChars.trim())) {
                    code = UniteAdministrative.UNITE_ADMINISTRATIVE_RESPONSABLE;
                } else {
                    code = null;
                }
            }
        } else {
            code = null;
        }
        return code;
    }

    /**
	 * Entre colonnes 9 (incl.) et 80 (excl.)
	 * 
	 * @param indexLigneCode
	 * @param documentTexte
	 * @return
	 */
    private String getNom(int indexLigneCode, DocumentTexte documentTexte) {
        return documentTexte.getTexte(indexLigneCode, 9, 80);
    }

    public static void main(String[] args) {
        File repertoireRacine = FGDConfig.getInstance().getRepertoireRacine();
        File repertoireChargement = FGDConfig.getInstance().getRepertoireChargementInitial();
        File fichierInput = new File(repertoireRacine, "srcExtraction/calendrierConservation.txt");
        File fichierOutput = new File(repertoireChargement, "calendrierConservation.xml");
        ExtracteurUnitesAdministratives extracteur = new ExtracteurUnitesAdministratives();
        List<UniteAdministrative> unitesAdministratives = extracteur.extraireUnitesAdministratives(fichierInput);
        extracteur.xmlDocument(unitesAdministratives, fichierOutput);
    }
}
