package ch.intertec.storybook.toolkit.odf;

import java.util.List;
import org.hibernate.Session;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.dom.style.props.OdfTextProperties;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeAutomaticStyles;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeStyles;
import org.odftoolkit.odfdom.incubator.doc.style.OdfDefaultStyle;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.odfdom.pkg.OdfFileDom;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.field.ChapterField;
import org.odftoolkit.simple.common.field.Fields;
import org.odftoolkit.simple.style.DefaultStyleHandler;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle;
import org.odftoolkit.simple.text.Paragraph;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import ch.intertec.storybook.model.DocumentModel;
import ch.intertec.storybook.model.hbn.dao.ChapterDAOImpl;
import ch.intertec.storybook.model.hbn.dao.PartDAOImpl;
import ch.intertec.storybook.model.hbn.dao.SceneDAOImpl;
import ch.intertec.storybook.model.hbn.entity.Chapter;
import ch.intertec.storybook.model.hbn.entity.Part;
import ch.intertec.storybook.model.hbn.entity.Scene;
import ch.intertec.storybook.view.MainFrame;

/**
 * @author martin
 * 
 */
public class OdfBookCreator {

    public static void createBook(MainFrame mainFrame) {
        try {
            TextDocument odt = TextDocument.newTextDocument();
            OdfOfficeStyles stylesOfficeStyles = odt.getOrCreateDocumentStyles();
            OdfDefaultStyle defaultStyle = stylesOfficeStyles.getDefaultStyle(OdfStyleFamily.Paragraph);
            OdfFileDom contentDom = odt.getContentDom();
            OdfStyle style = stylesOfficeStyles.newStyle("Cast_20_Para", OdfStyleFamily.Paragraph);
            style.setProperty(OdfTextProperties.FontSize, "14pt");
            DocumentModel model = mainFrame.getDocumentModel();
            Session session = model.beginTransaction();
            PartDAOImpl partDao = new PartDAOImpl(session);
            ChapterDAOImpl chapterDao = new ChapterDAOImpl(session);
            SceneDAOImpl sceneDao = new SceneDAOImpl(session);
            List<Part> parts = partDao.findAll();
            for (Part part : parts) {
                List<Chapter> chapters = chapterDao.findAll(part);
                for (Chapter chapter : chapters) {
                    Paragraph para = odt.addParagraph(chapter.getChapternoStr());
                    Font font = para.getFont();
                    font.setFontStyle(FontStyle.BOLD);
                    para.setFont(font);
                    ChapterField chapterField = Fields.createChapterField(odt.addParagraph("Chapter:").getOdfElement());
                    List<Scene> scenes = sceneDao.findByChapter(chapter);
                    for (Scene scene : scenes) {
                        odt.addParagraph(scene.getText());
                    }
                }
            }
            odt.save("/home/martin/tmp/demo_book.odt");
            model.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
