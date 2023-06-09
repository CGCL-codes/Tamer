package org.pdfclown.samples.cli;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.EnumSet;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.PageFormat;
import org.pdfclown.documents.Pages;
import org.pdfclown.documents.contents.composition.PrimitiveComposer;
import org.pdfclown.documents.contents.composition.XAlignmentEnum;
import org.pdfclown.documents.contents.composition.YAlignmentEnum;
import org.pdfclown.documents.contents.fonts.StandardType1Font;
import org.pdfclown.files.File;

/**
  This sample generates a series of PDF pages from the <b>default page formats available</b>,
  <i>varying both in size and orientation</i>.

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @version 0.1.2, 01/29/12
*/
public class PageFormatSample extends Sample {

    @Override
    public boolean run() {
        File file = new File();
        Document document = file.getDocument();
        populate(document);
        serialize(file, "Page Format", "page formats");
        return true;
    }

    private void populate(Document document) {
        StandardType1Font bodyFont = new StandardType1Font(document, StandardType1Font.FamilyEnum.Courier, true, false);
        Pages pages = document.getPages();
        EnumSet<PageFormat.SizeEnum> pageFormats = EnumSet.allOf(PageFormat.SizeEnum.class);
        EnumSet<PageFormat.OrientationEnum> pageOrientations = EnumSet.allOf(PageFormat.OrientationEnum.class);
        for (PageFormat.SizeEnum pageFormat : pageFormats) {
            for (PageFormat.OrientationEnum pageOrientation : pageOrientations) {
                Page page = new Page(document, PageFormat.getSize(pageFormat, pageOrientation));
                pages.add(page);
                Dimension2D pageSize = page.getSize();
                PrimitiveComposer composer = new PrimitiveComposer(page);
                composer.setFont(bodyFont, 32);
                composer.showText(pageFormat + " (" + pageOrientation + ")", new Point2D.Double(pageSize.getWidth() / 2, pageSize.getHeight() / 2), XAlignmentEnum.Center, YAlignmentEnum.Middle, 45);
                composer.flush();
            }
        }
    }
}
