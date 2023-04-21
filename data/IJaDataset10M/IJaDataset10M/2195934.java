package org.pdfclown.samples.cli;

import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.interaction.navigation.page.Transition;
import org.pdfclown.files.File;

/**
  This sample demonstrates <b>how to apply visual transitions to the pages</b> of a PDF document.
  <h3>Remarks</h3>
  <p>To watch the transition effects applied to the document, you typically have to select
  the presentation (full screen) view mode on your PDF viewer (for example Adobe Reader).</p>

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.0.7
  @version 0.1.2, 01/29/12
*/
public class PageTransitionSample extends Sample {

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
        Transition.StyleEnum[] transitionStyles = Transition.StyleEnum.values();
        int transitionStylesLength = transitionStyles.length;
        for (Page page : document.getPages()) {
            page.setTransition(new Transition(document, transitionStyles[(int) (Math.random() * (transitionStylesLength))], .5));
            page.setDuration(2);
        }
        serialize(file, "Transition", "applying visual transitions to pages");
        return true;
    }
}
