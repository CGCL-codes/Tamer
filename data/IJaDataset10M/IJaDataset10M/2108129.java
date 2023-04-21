package de.tvstaufia.kneiphelfer.gui;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import de.tvstaufia.kneiphelfer.Kunde;
import de.tvstaufia.kneiphelfer.Postenabrechnung;

public class Berichtersteller {

    public static void Kundenbericht(Kunde kunde) {
        EntityManager em = KneiphelferGUI.getInstance().getEm();
        synchronized (em) {
            List<?> results = em.createQuery("SELECT pa FROM Postenabrechnung pa WHERE pa.kunde.kundennummer=" + kunde.getKundennummer()).getResultList();
            LinkedList<Postenabrechnung> posten = new LinkedList<Postenabrechnung>();
            for (Object o : results) {
                posten.add((Postenabrechnung) o);
            }
            Collections.sort(posten, Postenabrechnung.getComparatorByDate());
            System.out.println("Datum;Beschreibung/Ware;Anzahl;Einzelpreis;Gesamtpreis");
            for (Postenabrechnung pa : posten) {
                if (pa.isGutschrift()) {
                    if (pa.getEreignis() != null) {
                        System.out.println("\"" + pa.getEreignis() + "\"" + ";" + "\"" + pa.getBeschreibung() + "\"" + ";; -" + pa.getGutschrift());
                    } else {
                        System.out.println("\"" + KneiphelferHelper.formatDate(pa.getDatum()) + "\"" + ";" + "\"" + pa.getBeschreibung() + "\"" + ";;-" + pa.getGutschrift());
                    }
                } else if (pa.getWare() != null) {
                    if (pa.getEreignis() != null) {
                        System.out.println("\"" + pa.getEreignis() + "\"" + ";" + "\"" + pa.getWare() + "\"" + ";" + pa.getAnzahl() + ";" + pa.getEinzelpreis() + ";" + (pa.getAnzahl() * pa.getEinzelpreis()));
                    } else {
                        System.out.println("\"" + KneiphelferHelper.formatDate(pa.getDatum()) + "\"" + ";" + "\"" + pa.getWare() + "\"" + ";" + pa.getAnzahl() + ";" + pa.getEinzelpreis() + ";" + (pa.getAnzahl() * pa.getEinzelpreis()));
                    }
                } else {
                    if (pa.getEreignis() != null) {
                        System.out.println("\"" + pa.getEreignis() + "\"" + ";" + "\"" + pa.getBeschreibung() + "\"" + ";" + pa.getAnzahl() + ";" + pa.getEinzelpreis() + ";" + (pa.getAnzahl() * pa.getEinzelpreis()));
                    } else {
                        System.out.println("\"" + KneiphelferHelper.formatDate(pa.getDatum()) + "\"" + ";" + "\"" + pa.getBeschreibung() + "\"" + ";" + pa.getAnzahl() + ";" + pa.getEinzelpreis() + ";" + (pa.getAnzahl() * pa.getEinzelpreis()));
                    }
                }
            }
            FileOutputStream fos = null;
            Document document = null;
            try {
                document = new Document();
                fos = new FileOutputStream("SimplePdf.pdf");
                PdfWriter.getInstance(document, fos);
                document.open();
                document.addTitle("Buchungsposten für " + kunde);
                document.addCreator("Kneiphelfer");
                document.add(new Chapter("Testkapitel", 1));
                Paragraph testParagraph = new Paragraph("Das ist nur ein Test");
                document.add(testParagraph);
                document.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } finally {
                if (document != null) {
                    document.close();
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
            }
            for (Postenabrechnung pa : posten) {
                if (pa.isGutschrift()) {
                    if (pa.getEreignis() != null) {
                        System.out.println("\"" + pa.getEreignis() + "\"" + ";" + "\"" + pa.getBeschreibung() + "\"" + ";; -" + pa.getGutschrift());
                    } else {
                        System.out.println("\"" + KneiphelferHelper.formatDate(pa.getDatum()) + "\"" + ";" + "\"" + pa.getBeschreibung() + "\"" + ";;-" + pa.getGutschrift());
                    }
                } else if (pa.getWare() != null) {
                    if (pa.getEreignis() != null) {
                        System.out.println("\"" + pa.getEreignis() + "\"" + ";" + "\"" + pa.getWare() + "\"" + ";" + pa.getAnzahl() + ";" + pa.getEinzelpreis() + ";" + (pa.getAnzahl() * pa.getEinzelpreis()));
                    } else {
                        System.out.println("\"" + KneiphelferHelper.formatDate(pa.getDatum()) + "\"" + ";" + "\"" + pa.getWare() + "\"" + ";" + pa.getAnzahl() + ";" + pa.getEinzelpreis() + ";" + (pa.getAnzahl() * pa.getEinzelpreis()));
                    }
                } else {
                    if (pa.getEreignis() != null) {
                        System.out.println("\"" + pa.getEreignis() + "\"" + ";" + "\"" + pa.getBeschreibung() + "\"" + ";" + pa.getAnzahl() + ";" + pa.getEinzelpreis() + ";" + (pa.getAnzahl() * pa.getEinzelpreis()));
                    } else {
                        System.out.println("\"" + KneiphelferHelper.formatDate(pa.getDatum()) + "\"" + ";" + "\"" + pa.getBeschreibung() + "\"" + ";" + pa.getAnzahl() + ";" + pa.getEinzelpreis() + ";" + (pa.getAnzahl() * pa.getEinzelpreis()));
                    }
                }
            }
        }
    }

    public static byte[] createKundenRechnung(Kunde kunde, Date datum, String rechnungstext) {
        if (kunde.getKontostand() >= 0.00) {
            return null;
        }
        ByteArrayOutputStream bos = null;
        Document document = null;
        try {
            document = new Document();
            bos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, bos);
            document.open();
            document.addTitle("Rechnung für " + kunde);
            document.addCreator("Kneiphelfer");
            Chapter chapterRechnung = new Chapter("Rechnung", 1);
            chapterRechnung.setNumberDepth(0);
            document.add(chapterRechnung);
            String rechnungsParagraph = rechnungstext;
            if (rechnungsParagraph == null) {
                rechnungsParagraph = "Lieber BB " + kunde.getKneipname() + ",\n\n" + "Es haben sich wieder einige offene Posten angesammelt und ich möchte Dich bitten mindestens " + (kunde.getKontostand() * -1) + " EUR zu überweisen um die offenen Beträge auszugleichen.\n\nMit farbigem Gruß" + "\n\nDer Kneipwart";
            } else {
                rechnungsParagraph = "Lieber BB " + kunde.getKneipname() + ",\n\n" + rechnungsParagraph;
            }
            document.add(new Paragraph(rechnungsParagraph));
            document.close();
            try {
                bos.close();
            } catch (IOException e) {
            }
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        return bos.toByteArray();
    }
}
