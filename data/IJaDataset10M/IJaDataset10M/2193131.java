package org.pdfclown.samples.cli;

import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.contents.ContentScanner;
import org.pdfclown.documents.contents.fonts.Font;
import org.pdfclown.documents.contents.objects.ContainerObject;
import org.pdfclown.documents.contents.objects.ContentObject;
import org.pdfclown.documents.contents.objects.ShowText;
import org.pdfclown.documents.contents.objects.Text;
import org.pdfclown.files.File;

/**
  This sample demonstrates the <b>low-level way to extract text</b> from a PDF document.
  <h3>Remarks</h3>
  <p>In order to obtain richer information about the extracted text content,
  see the other available samples ({@link TextInfoExtractionSample}, {@link AdvancedTextExtractionSample}).</p>

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.0.8
  @version 0.1.1, 11/01/11
*/
public class BasicTextExtractionSample extends Sample {

    @Override
    public boolean run() {
        File file;
        {
            String filePath = promptPdfFileChoice("Please select a PDF file");
            try {
                file = new File(filePath);
            } catch (Exception e) {
                throw new RuntimeException(filePath + " file access error.", e);
            }
        }
        Document document = file.getDocument();
        for (Page page : document.getPages()) {
            if (!promptNextPage(page, false)) return false;
            extract(new ContentScanner(page));
        }
        return true;
    }

    private void extract(ContentScanner level) {
        if (level == null) return;
        while (level.moveNext()) {
            ContentObject content = level.getCurrent();
            if (content instanceof ShowText) {
                Font font = level.getState().getFont();
                System.out.println(font.decode(((ShowText) content).getText()));
            } else if (content instanceof Text || content instanceof ContainerObject) {
                extract(level.getChildLevel());
            }
        }
    }
}
