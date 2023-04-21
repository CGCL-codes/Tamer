package cz.kibo.photoGalleryCreator.workers;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JOptionPane;
import cz.kibo.photoGalleryCreator.workers.images.IdentifyImageType;

/**
 * Load xhtml template. 
 * Replace this "_VALUE_" in template and 
 * return replacement string.
 */
public class MakeXhtmlFile {

    public final String PATTERN_TITLE = "_TITLE_";

    public final String PATTERN_CHARSET = "_CHARSET_";

    public final String PATTERN_CSS_PATH = "_CSS_PATH_";

    public final String PATTERN_CONTEXT_PATH = "_CONTEXT_PATH_";

    public final String PATTERN_IMAGE = "_IMAGE_";

    public final String PATTERN_PREVIEW = "_PREVIEW_";

    public final String PATTERN_IMAGE_WIDHT = "_WIDHT_";

    public final String PATTERN_IMAGE_HEIGHT = "_HEIGHT_";

    public final String PATTERN_HEADER = "_END_HEADER_";

    public final String PATTERN_FOOTER = "_START_FOOTER_";

    private Messenger messenger;

    Map replacementDictionary;

    private String message;

    private String fotoTemplate;

    private String previewTemplate;

    private String headerOfPreviewTemplate;

    private String contentOfPreviewTemplate;

    private String footerOfPreviewTemplate;

    /**
	 * @param previewTemplate - template file for generate previewXhtmlFile
	 * @param fotoTemplate - template file for generate fotoXhtmlFile
	 */
    public MakeXhtmlFile(String inputPreviewTemplate, String inputFotoTemplate) {
        messenger = Messenger.getInstance();
        replacementDictionary = new HashMap();
        replacementDictionary.put(PATTERN_TITLE, messenger.getTitle());
        replacementDictionary.put(PATTERN_CHARSET, messenger.getOutputCharset());
        replacementDictionary.put(PATTERN_CSS_PATH, messenger.getCssContextPath());
        previewTemplate = inputPreviewTemplate;
        fotoTemplate = inputFotoTemplate;
        try {
            separatePreviewTemplate(previewTemplate);
        } catch (Exception e) {
            message = "Error in read templates file.";
            JOptionPane.showMessageDialog(null, message);
            return;
        }
    }

    public String getHeaderOfPreviewTemplate() {
        return headerOfPreviewTemplate;
    }

    public String getFooterOfPreviewTemplate() {
        return footerOfPreviewTemplate;
    }

    /**
	 * From template make xhtml file with image foto
	 * @param image - name source image
	 * @return - destination XHTML file
	 */
    public String makeFotoXhtmlFile(File image) {
        return createXhtmlFile(image, fotoTemplate);
    }

    /**
	 * From template make xhtml file with preview
	 * Preview template is first separate in three part : header, content, footer.
	 * This method work only with content part.
	 * Part header get the method getHeaderOfPreviewTemplate
	 * Part footer get the method getFootetOfPreviewTemplate
	 * @param image - name source image
	 * @return - destination XHTML file
	 */
    public String makeFotoXhtmPreview(File image) {
        return createXhtmlFile(image, contentOfPreviewTemplate);
    }

    /**
	 * Helping method for makeFotoXhtmlFile and makePreviewXhtmlFile
	 * Accordance with template create replace string
	 * @param image
	 * @param template - content template file into String 
	 * @return  replaceString
	 */
    private String createXhtmlFile(File image, String template) {
        Dimension dimension = IdentifyImageType.getDimensionImage(image);
        int imageWidth = dimension.width;
        int imageHeight = dimension.height;
        replacementDictionary.put(PATTERN_CONTEXT_PATH, messenger.getContextPathWithFileName());
        replacementDictionary.put(PATTERN_IMAGE_WIDHT, imageWidth + "");
        replacementDictionary.put(PATTERN_IMAGE_HEIGHT, imageHeight + "");
        replacementDictionary.put(PATTERN_IMAGE, messenger.getDirectoryForFoto() + File.separator + image.getName());
        replacementDictionary.put(PATTERN_PREVIEW, messenger.getContextPath() + messenger.getDirectoryForPreview() + File.separator + image.getName());
        return createXhtmlFile(template);
    }

    /**
	 * Replace values(_VALUE_) in input string to values with replacementDictionary
	 */
    private String createXhtmlFile(String template) {
        String replaceContent = template;
        for (Iterator it = replacementDictionary.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry e = (Map.Entry) it.next();
            replaceContent = MyHelpingMethods.replace(replaceContent, (String) e.getKey(), (String) e.getValue());
        }
        return replaceContent;
    }

    private void separatePreviewTemplate(String previewTemplate) throws Exception {
        int endHeaderIndex = previewTemplate.indexOf(PATTERN_HEADER);
        int beginContentIndex = endHeaderIndex + PATTERN_HEADER.length();
        int endContentIndex = previewTemplate.indexOf(PATTERN_FOOTER);
        int beginFooterIndex = endContentIndex + PATTERN_FOOTER.length();
        try {
            headerOfPreviewTemplate = previewTemplate.substring(0, endHeaderIndex);
            headerOfPreviewTemplate = createXhtmlFile(headerOfPreviewTemplate);
            contentOfPreviewTemplate = previewTemplate.substring(beginContentIndex, endContentIndex);
            footerOfPreviewTemplate = previewTemplate.substring(beginFooterIndex);
            footerOfPreviewTemplate = createXhtmlFile(footerOfPreviewTemplate);
        } catch (NullPointerException e) {
            message = "Exception in method MakeXhtmlFile.separatePreviewTemplate(String)";
            System.err.print(message);
            throw new Exception();
        }
    }
}
