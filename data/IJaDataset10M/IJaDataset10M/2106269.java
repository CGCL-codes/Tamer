package gvo.amazon;

import gvo.basicDatatypes.AutorenDat;
import gvo.basicDatatypes.GVOall;
import gvo.presets.Presets;
import gvo.sql.SQLWizard;
import java.awt.Image;
import java.util.ArrayList;

public class AmazonEntry {

    public String asin;

    public String largeImageURL;

    public Image image = null;

    public ArrayList<String> autoren = new ArrayList<String>();

    public String isbn;

    public String edition;

    public String binding;

    public String publisher;

    public int numberOfPages;

    public String publicationDate;

    public String titel;

    public GVOall gvoall = new GVOall();

    public boolean isFilled = false;

    public AmazonEntry() {
    }

    public static final String[] Titel = { "ASIN", "LargeImageURL", "Autoren", "ISBN", "Edition", "Binding", "Publisher", "NumberOfPages", "PublicationDate", "Titel" };

    public String info() {
        return String.format("AmazonEntry %10.10s | %10.10s | %55.55s | %10.10s | %10.10s | %10.10s | %20.20s | %5d | %10.10s | %30.30s ", asin, largeImageURL, getAutoren(), isbn, edition, binding, publisher, numberOfPages, publicationDate, titel);
    }

    public static String infoTitel() {
        return String.format("AmazonEntry %10.10s | %10.10s | %55.55s | %10.10s | %10.10s | %10.10s | %20.20s | %5.5s | %10.10s | %30.30s ", (Object[]) Titel);
    }

    public void fillGVOall(SQLWizard sqlTool) {
        gvoall.buch.setTitel(titel);
        gvoall.buch.setIsbnPreis(isbn);
        gvoall.buch.setOrtJahr(publisher + ", " + publicationDate);
        gvoall.buch.setKollation(numberOfPages + " S., " + binding);
        gvoall.buch.setAusgabeVersion(edition);
        for (String autor : autoren) {
            gvoall.autoren.add(new AutorenDat(prepareAutoren(autor)));
        }
        gvoall.buch.isVerified = true;
        gvoall.buch.isInDB = sqlTool.checkBuchDat(titel, isbn);
        isFilled = true;
    }

    public String prepareAutoren(String in) {
        if (in == null) {
            return Presets.EMPTYTXT;
        }
        int ind = in.lastIndexOf(" ");
        if (ind == -1) {
            return in;
        }
        return in.substring(ind + 1) + ", " + in.substring(0, ind);
    }

    public String getAutoren() {
        String res = "";
        boolean first = true;
        for (String autor : autoren) {
            if (first) {
                res += prepareAutoren(autor);
                first = false;
            } else {
                res += " / " + prepareAutoren(autor);
            }
        }
        return res;
    }
}
