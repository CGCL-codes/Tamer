package lrf.pdf;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import lrf.epub.EPUBMetaData;
import lrf.html.HtmlDoc;
import lrf.html.HtmlOptimizer;
import org.apache.pdfbox.pdfviewer.PageDrawer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import com.lowagie.text.pdf.PdfReader;

public class PDFSerializer {

    public static File dirOrig = new File("D:\\tmp\\booksPDF");

    public static File dirDest = new File("D:\\tmp\\booksPDF");

    public static void recurse(File dir, String lang) {
        File list[] = dir.listFiles();
        for (int i = 0; i < list.length; i++) {
            if (list[i].isDirectory()) {
                recurse(list[i], lang);
            } else if (list[i].getName().toLowerCase().endsWith(".pdf")) {
                procPDF(list[i], lang);
            }
        }
    }

    public static void procPDF(File pdfFile, File ori, File des, boolean otfem, String lang) {
        EPUBMetaData.doNotEmbedOTFFonts = otfem;
        dirOrig = ori;
        dirDest = des;
        procPDF(pdfFile, lang);
    }

    @SuppressWarnings("unchecked")
    public static void procPDF(File pdfFile, String lang) {
        GraphicsHook gh = null;
        boolean ok = true;
        String x = null, y = null;
        File dest = null;
        try {
            x = pdfFile.getCanonicalPath();
            y = x.substring(dirOrig.getCanonicalPath().length());
            dest = new File(dirDest, y.substring(0, y.length() - 4) + ".xml");
            dest.getParentFile().mkdirs();
            gh = new GraphicsHook();
            PDFHack pageDrawer = new PDFHack(gh.getFlower());
            PdfReader pdfReader = new PdfReader(pdfFile.getCanonicalPath());
            HashMap<String, String> info = pdfReader.getInfo();
            String title = info.get("Title");
            String author = info.get("Author");
            pdfReader.close();
            System.out.print("Loading '" + pdfFile.getName() + "'");
            PDDocument doc = PDDocument.load(pdfFile);
            List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
            System.out.print(" " + pages.size() + " pages ");
            Dimension dim = new Dimension(600, 800);
            File fh = new File(dirDest, y.substring(1, y.length() - 4));
            File tmp = new File(dirDest, y.substring(1, y.length() - 4) + "-tmp");
            tmp.mkdirs();
            HtmlDoc htm = new HtmlDoc(fh.getName(), title, author, "LRFTools", EPUBMetaData.createRandomIdentifier(), tmp);
            EPUBMetaData epubmd = new PDF2EPUB_HTML(title, author, lang);
            epubmd.init(fh.getCanonicalPath() + ".epub");
            for (int i = 0; i < pages.size(); i++) {
                PDRectangle pdrect = pages.get(i).getMediaBox();
                if (pdrect != null) gh.newPage((int) pdrect.getWidth(), (int) pdrect.getHeight()); else gh.newPage(600, 800);
                pageDrawer.drawPage(gh, pages.get(i), dim);
                if (i % 30 == 0) {
                    System.out.print("-");
                    System.out.flush();
                }
                gh.getFlower().managePieces(htm);
            }
            gh.getFlower().dumpPieces(htm);
            doc.close();
            System.out.print("/ sorting...");
            gh.close();
            htm.createEPUB(epubmd, "");
            HtmlOptimizer opt = new HtmlOptimizer(htm, tmp);
            opt.setPaginateKB(150);
            int numPages = opt.optimize(true);
            epubmd.buildCSS(fh.getName() + ".css", opt.getStyles(), false);
            for (int i = 0; i < numPages; i++) {
                File f = new File(tmp, fh.getName() + "-" + (1 + i) + ".html");
                epubmd.processFile(f, fh.getName() + "-" + (1 + i) + ".html");
            }
            File list[] = tmp.listFiles();
            for (int i = 0; i < list.length; i++) {
                while (!list[i].delete()) ;
            }
            tmp.delete();
            epubmd.close();
            System.out.println("End.");
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        } finally {
            if (gh != null) {
                gh.close();
            }
            if (!ok) {
                dest.renameTo(new File(dirDest, y.substring(0, y.length() - 4) + ".err"));
            }
        }
    }

    public static void showMetaData(PDDocument doc) {
        PDDocumentOutline root = doc.getDocumentCatalog().getDocumentOutline();
        if (root == null) return;
        PDOutlineItem item = root.getFirstChild();
        while (item != null) {
            try {
                System.out.println("Item:" + item.getTitle() + ":" + item.findDestinationPage(doc));
            } catch (IOException e) {
                e.printStackTrace();
            }
            PDOutlineItem child = item.getFirstChild();
            while (child != null) {
                System.out.println("    Child:" + child.getTitle());
                child = child.getNextSibling();
            }
            item = item.getNextSibling();
        }
    }
}
