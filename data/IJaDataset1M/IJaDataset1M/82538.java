package net.sf.webwarp.reports.itext.renderer;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.webwarp.reports.itext.ElementRenderer;
import net.sf.webwarp.reports.itext.dao.DocumentBean;
import net.sf.webwarp.reports.itext.dao.SectionBean;
import net.sf.webwarp.reports.itext.dao.SectionElement;
import org.springframework.context.MessageSource;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class ParagraphRenderer implements ElementRenderer {

    public void render(DocumentBean documentBean, SectionBean sectionBean, SectionElement sectionElement, Document doc, Object model, Map<String, String> properties, MessageSource messageSource, Locale locale, PdfWriter writer) {
        try {
            List<String> texts = (List<String>) model;
            String prefixPart = "text_";
            int i = 0;
            for (String text : texts) {
                String prefix = prefixPart + i + ".";
                Font font = RendererUtils.getFont(documentBean, properties, prefix);
                Paragraph paragraph = new Paragraph(text, font);
                ParagraphConfigurator.configureParagraph(properties, prefix, paragraph);
                doc.add(paragraph);
                i++;
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
