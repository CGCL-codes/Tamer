package org.openconcerto.erp.core.finance.accounting.report;

import org.openconcerto.erp.config.ComptaPropsConfiguration;
import org.openconcerto.erp.preferences.TemplateNXProps;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.model.SQLRow;

public class PdfGenerator_2033B extends PdfGenerator {

    public PdfGenerator_2033B() {
        super("2033B.pdf", "result_2033B.pdf", TemplateNXProps.getInstance().getStringProperty("Location2033BPDF"));
        setTemplateOffset(0, 0);
        setOffset(0, 0);
        setMargin(32, 32);
    }

    public void generate() {
        setFontRoman(8);
        SQLRow rowSociete = ((ComptaPropsConfiguration) Configuration.getInstance()).getRowSociete();
        addText("NOM", rowSociete.getString("TYPE") + " " + rowSociete.getString("NOM"), 228, 827);
        setFontRoman(12);
        addSplittedText("CLOS1", "08202006", 412, 809, 9.7);
        addSplittedText("CLOS2", "08202006", 502, 809, 9.7);
        setFontRoman(9);
        String cc = "Document généré par le logiciel Bloc, (c) Front Software 2006";
        addText("", cc, getWidth() - 2, 460, 90);
        setFontRoman(10);
        long t = 143785123456L;
        int yy = 0;
        int y = 786;
        for (; y > 740; y -= 18) {
            addTextRight("PRODUIT1." + yy, insertCurrencySpaces("" + t), 400, y);
            addTextRight("PRODUIT2." + yy, insertCurrencySpaces("" + t), 491, y);
            addTextRight("PRODUIT3." + yy, insertCurrencySpaces("" + t), 580, y);
            yy++;
        }
        for (; y > 650; y -= 18) {
            addTextRight("PRODUIT2." + yy, insertCurrencySpaces("" + t), 491, y);
            addTextRight("PRODUIT3." + yy, insertCurrencySpaces("" + t), 580, y);
            yy++;
        }
        for (int i = 0; i < 5; i++) {
            addTextRight("CHARGES3." + yy, insertCurrencySpaces("" + t), 491, y);
            addTextRight("CHARGES4." + yy, insertCurrencySpaces("" + t), 608 - 28, y);
            yy++;
            y -= 18;
        }
        y += 18;
        t = t / 100;
        yy--;
        addTextRight("CBAIL_MO" + yy, insertCurrencySpaces("" + t), 280, y + 1);
        addTextRight("CBAIL_IMMO" + yy, insertCurrencySpaces("" + t), 396, y + 1);
        yy++;
        y -= 18;
        addTextRight("CHARGES1." + yy, insertCurrencySpaces("" + t), 392, y);
        addTextRight("CHARGES2." + yy, insertCurrencySpaces("" + t), 491, y);
        addTextRight("CHARGES3." + yy, insertCurrencySpaces("" + t), 580, y);
        yy++;
        y -= 18;
        for (int i = 0; i < 4; i++) {
            addTextRight("CHARGES3." + yy, insertCurrencySpaces("" + t), 491, y);
            addTextRight("CHARGES4." + yy, insertCurrencySpaces("" + t), 608 - 28, y);
            yy++;
            y -= 18;
        }
        addTextRight("CHARGES1." + yy, insertCurrencySpaces("" + t), 392, y);
        addTextRight("CHARGES2." + yy, insertCurrencySpaces("" + t), 491, y);
        addTextRight("CHARGES3." + yy, insertCurrencySpaces("" + t), 580, y);
        yy++;
        y -= 18;
        for (int i = 0; i < 9; i++) {
            addTextRight("PCHARGES3." + yy, insertCurrencySpaces("" + t), 491, y);
            addTextRight("PCHARGES4." + yy, insertCurrencySpaces("" + t), 580, y);
            yy++;
            y -= 18;
        }
        for (int i = 0; i < 4; i++) {
            addTextRight("REINT3." + yy, insertCurrencySpaces("" + t), 491, y);
            yy++;
            y -= 18;
        }
        t = t / 100;
        addTextRight("REINT1." + yy, insertCurrencySpaces("" + t), 220, y);
        addTextRight("REINT2." + yy, insertCurrencySpaces("" + t), 401, y);
        addTextRight("REINT3." + yy, insertCurrencySpaces("" + t), 491, y);
        yy++;
        y -= 18;
        for (int i = 0; i < 2; i++) {
            addTextRight("DEDUC1." + yy, insertCurrencySpaces("" + t), 148, y);
            addTextRight("DEDUC2." + yy, insertCurrencySpaces("" + t), 275, y);
            addTextRight("DEDUC3." + yy, insertCurrencySpaces("" + t), 401, y);
            yy++;
            y -= 18;
        }
        yy--;
        addTextRight("DEDUC4." + yy, insertCurrencySpaces("" + t), 580, y + 25);
        yy++;
        addTextRight("DEDUC2." + yy, insertCurrencySpaces("" + t), 237, y);
        addTextRight("DEDUC3." + yy, insertCurrencySpaces("" + t), 392, y);
        addTextRight("DEDUC4." + yy, insertCurrencySpaces("" + t), 580, y);
        yy++;
        y -= 18;
        addTextRight("RES3." + yy, insertCurrencySpaces("" + t), 491, y);
        addTextRight("RES4." + yy, insertCurrencySpaces("" + t), 580, y);
        yy++;
        y -= 18;
        addTextRight("DEF3." + yy, insertCurrencySpaces("" + t), 491, y);
        yy++;
        y -= 18;
        addTextRight("DEF4." + yy, insertCurrencySpaces("" + t), 580, y);
        yy++;
        y -= 18;
        addTextRight("RES3." + yy, insertCurrencySpaces("" + t), 491, y);
        addTextRight("RES4." + yy, insertCurrencySpaces("" + t), 580, y);
        yy++;
        y -= 18;
        addTextRight("COT1." + yy, insertCurrencySpaces("" + t), 195, y);
        addTextRight("COT2." + yy, insertCurrencySpaces("" + t), 401, y);
        addSplittedText("COT3." + yy, "876543", 514, y, 11.5);
        yy++;
        y -= 18;
        addTextRight("T1." + yy, insertCurrencySpaces("" + t), 242, y);
        addSplittedText("T2." + yy, "88", 333, y, 14.4);
        addSplittedText("T3." + yy, "88", 406, y, 10);
        addSplittedText("T4." + yy, "88", 464, y, 10);
        yy++;
        y -= 18;
        addTextRight("T1." + yy, insertCurrencySpaces("" + t), 242, y);
        addTextRight("T2." + yy, insertCurrencySpaces("" + t), 461, y);
    }
}
