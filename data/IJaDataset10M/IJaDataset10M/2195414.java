package com.centropresse.util.pdf;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import com.centropresse.dto.Macchina;
import com.centropresse.util.Constants;
import com.centropresse.util.LogFactory;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfLayer;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.XmlParser;
import com.lowagie.text.xml.XmlPeer;

public class PdfDettaglioMacchina extends PdfPageEventHelper {

    private static final String nameClass = "PdfDettaglioMacchina";

    public static Logger logger = LogFactory.getWebLogger();

    private static String prefixLogClass = Constants.APPLICATION_CODE_WEB + "." + nameClass + ".class";

    protected PdfImportedPage paper;

    protected PdfLayer not_printed;

    protected OutputStream stream;

    protected Macchina macchina;

    protected MessageResources messages;

    protected Locale locale;

    public PdfDettaglioMacchina(OutputStream stream, Macchina macchina, MessageResources messages, Locale locale) {
        this.stream = stream;
        this.macchina = macchina;
        this.messages = messages;
        this.locale = locale;
    }

    public PdfDettaglioMacchina() {
    }

    public void createMailPaper() {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("template.pdf"));
            document.open();
            Image img = null;
            img = Image.getInstance(Constants.URL_IMG + Constants.LOGO);
            Image img1 = null;
            try {
                img1 = Image.getInstance(Constants.URL_FOTO + macchina.getFoto());
            } catch (Throwable t) {
                t.printStackTrace();
                img1 = Image.getInstance(Constants.URL_FOTO + Constants.DEFAULT_FOTO);
            }
            PdfContentByte cb = writer.getDirectContent();
            cb.setColorStroke(Color.orange);
            cb.setLineWidth(2);
            cb.rectangle(20, 20, document.getPageSize().getWidth() - 40, document.getPageSize().getHeight() - 40);
            cb.stroke();
            PdfPTable table = new PdfPTable(2);
            Phrase p = new Phrase();
            Chunk ck = new Chunk("www.centropresse.net\n", new Font(Font.TIMES_ROMAN, 16, Font.BOLDITALIC, Color.blue));
            p.add(ck);
            ck = new Chunk("Via Santa Maria Mazzarello,35\n10015 Venaria(TO)", new Font(Font.HELVETICA, 12, Font.NORMAL, Color.darkGray));
            p.add(ck);
            table.getDefaultCell().setBackgroundColor(Color.yellow);
            table.getDefaultCell().setBorderWidth(0);
            table.addCell(p);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(new Phrase(new Chunk(img, 0, 0)));
            table.setTotalWidth(document.right() - document.left());
            table.writeSelectedRows(0, -1, document.left(), document.getPageSize().getHeight() - 50, cb);
            PdfPTable table1 = new PdfPTable(2);
            table1.getDefaultCell().setBorderWidth(0);
            p = new Phrase();
            table1.addCell(p);
            table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            img1.scaleAbsolute(130, 130);
            img1.setAlignment(Image.ALIGN_RIGHT);
            Chunk fox = new Chunk(img1, 0, -15);
            table1.addCell(new Phrase(fox));
            table1.setTotalWidth(document.right() - document.left());
            table1.writeSelectedRows(0, -1, document.left(), document.getPageSize().getHeight() - 100, cb);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creaPdfDettaglio() {
        try {
            createMailPaper();
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, stream);
            writer.setPdfVersion(PdfWriter.VERSION_1_5);
            writer.setViewerPreferences(PdfWriter.PrintScalingNone);
            writer.setPageEvent(new PdfDettaglioMacchina());
            String label_costruttore = messages.getMessage(locale, Constants.LABEL_COSTRUTTORE);
            String label_tipo = messages.getMessage(locale, Constants.LABEL_TIPO);
            String label_ubicazione = messages.getMessage(locale, Constants.LABEL_UBICAZIONE);
            String label_peso = messages.getMessage(locale, Constants.LABEL_PESO);
            String label_dimensioni = messages.getMessage(locale, Constants.LABEL_DIMENSIONI);
            String label_anno = messages.getMessage(locale, Constants.LABEL_ANNO);
            String label_num_motori = messages.getMessage(locale, Constants.LABEL_NUM_MOTORI);
            String label_potenza = messages.getMessage(locale, Constants.LABEL_POTENZA);
            String label_fronte = messages.getMessage(locale, Constants.LABEL_FRONTE);
            String label_profondita = messages.getMessage(locale, Constants.LABEL_PROFONDITA);
            String label_descrizione = messages.getMessage(locale, Constants.LABEL_DESCRIZIONE);
            XmlParser.parse(document, Constants.URL_RESOURCES + "dettaglio.xml", getTagMap(label_costruttore, macchina.getCostruttore().getDesc_costruttore(), label_tipo, macchina.getTipo().getDesc_tipo(), label_ubicazione, macchina.getUbicazione().getDesc_ubicazione(), label_peso, macchina.getPeso(), label_dimensioni, macchina.getAltezza() + "x" + macchina.getProfondita(), label_anno, macchina.getId_anno(), label_num_motori, macchina.getNum_motori(), label_potenza, macchina.getPotenza(), label_fronte, macchina.getFronte(), label_profondita, macchina.getProfondita(), macchina.getDesc_macchina(), "http://www.centropresse.com"));
            stream.flush();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        try {
            PdfReader reader = new PdfReader("template.pdf");
            paper = writer.getImportedPage(reader, 1);
            not_printed = new PdfLayer("template", writer);
            not_printed.setOnPanel(false);
            not_printed.setPrint("Print", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onStartPage(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
    public void onStartPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.beginLayer(not_printed);
        cb.addTemplate(paper, 0, 0);
        cb.endLayer();
    }

    /**
	 * Returns a HashMap that can be used as tagmap.
	 */
    public HashMap getTagMap(String costruttore_desc, String costruttore_value, String tipo_desc, String tipo_value, String ubicazione_desc, String ubicazione_value, String peso_desc, String peso_value, String dimensioni_desc, String dimensioni_value, String anno_desc, String anno_value, String num_motori_desc, String num_motori_value, String potenza_desc, String potenza_value, String fronte_desc, String fronte_value, String profondita_desc, String profondita_value, String desc, String site) {
        HashMap tagmap = new HashMap();
        XmlPeer peer = new XmlPeer(ElementTags.ITEXT, "letter");
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "costruttore_desc");
        peer.setContent(costruttore_desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "costruttore_value");
        peer.setContent(costruttore_value);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "tipo_desc");
        peer.setContent(tipo_desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "tipo_value");
        peer.setContent(tipo_value);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "ubicazione_desc");
        peer.setContent(ubicazione_desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "ubicazione_value");
        peer.setContent(ubicazione_value);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "anno_desc");
        peer.setContent(anno_desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "anno");
        peer.setContent(anno_value);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "num_motori_desc");
        peer.setContent(num_motori_desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "num_motori_value");
        peer.setContent(num_motori_value);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "potenza_desc");
        peer.setContent(potenza_desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "potenza_value");
        peer.setContent(potenza_value);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "fronte_desc");
        peer.setContent(fronte_desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "fronte_value");
        peer.setContent(fronte_value);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "profondita_desc");
        peer.setContent(profondita_desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "profondita_value");
        peer.setContent(profondita_value);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "peso_desc");
        peer.setContent(peso_desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "peso_value");
        peer.setContent(peso_value);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "dimensioni_desc");
        peer.setContent(dimensioni_desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "dimensioni_value");
        peer.setContent(dimensioni_value);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.CHUNK, "desc");
        peer.setContent(desc);
        tagmap.put(peer.getAlias(), peer);
        peer = new XmlPeer(ElementTags.ANCHOR, "website");
        peer.setContent(site);
        peer.addValue(ElementTags.REFERENCE, site);
        peer.addValue(ElementTags.COLOR, "#0000FF");
        tagmap.put(peer.getAlias(), peer);
        return tagmap;
    }

    public static void main(String arg[]) {
        try {
            PdfDettaglioMacchina dettaglio = new PdfDettaglioMacchina(new FileOutputStream("simple_letter_1.pdf"), new Macchina(), null, null);
            dettaglio.creaPdfDettaglio();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
