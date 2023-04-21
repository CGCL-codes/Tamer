package com.alexmcchesney.poster.templates.style;

import java.io.*;
import javax.xml.bind.*;
import com.alexmcchesney.poster.LoadConfigException;
import com.alexmcchesney.poster.SaveConfigException;
import com.alexmcchesney.poster.templates.style.jaxb.*;

public class StyleTemplateFile implements IStyleTemplate {

    /** Default body tag */
    private static final String DEFAULT_BODY_TAG = "<body>";

    /** Default content style */
    private static final String DEFAULT_CONTENT_STYLE = "class=\"content\"";

    /** Default inline stylesheet content */
    private static final String DEFAULT_INLINE_STYLE = "body\n" + "{\n" + "	background-color: #FFFFEE;\n" + "}\n" + ".content\n" + "{\n" + "	font-family: arial;\n" + "	color: #004e63;\n" + "	font-size:10pt;\n" + "}\n" + ".title\n" + "{\n" + "	font-family: times new roman;\n" + "	color: #000000;\n" + "	font-size:12pt;\n" + "	font-weight:bold;\n" + "}\n";

    /** Default stylesheet url */
    private static final String DEFAULT_STYLESHEET_URL = "http://myserver/mystylesheet.css";

    /** Default title style */
    private static final String DEFAULT_TITLE_STYLE = "class=\"title\"";

    /** Body tag */
    private String m_sBodyTag = DEFAULT_BODY_TAG;

    /** Title style */
    private String m_sTitleStyle = DEFAULT_TITLE_STYLE;

    /** Content style */
    private String m_sContentStyle = DEFAULT_CONTENT_STYLE;

    /** Inline stylesheet */
    private String m_sInlineStyleSheet = DEFAULT_INLINE_STYLE;

    /** Stylesheet url */
    private String m_sStyleSheetURL = DEFAULT_STYLESHEET_URL;

    /** Flag indicating whether or not to use the inline style sheet */
    private boolean m_bUseInlineStyleSheet = true;

    /** File to save to */
    private File m_file = null;

    /** Singleton jaxb context */
    private static JAXBContext m_context;

    /**
	 * Constructor
	 * @param file	File to load template from
	 * @throws LoadConfigException	Thrown if we cannot load the file.
	 * Note that if the file is not present, no error will be thrown.
	 * Instead we will initialise with default values.  Saving will
	 * create the file.
	 */
    public StyleTemplateFile(File file) throws LoadConfigException {
        m_file = file;
        loadConfig();
    }

    /**
	 * Loads the configuration from the file.
	 * @throws LoadConfigException thrown if something goes wrong.
	 * If the file does not exist, no exception is thrown.  Instead,
	 * the configuration is initialised with default values.
	 */
    private void loadConfig() throws LoadConfigException {
        if (!m_file.exists() || m_file.length() == 0) {
            return;
        }
        StyleTemplate template = null;
        try {
            if (m_context == null) {
                m_context = JAXBContext.newInstance("com.alexmcchesney.poster.templates.style.jaxb", this.getClass().getClassLoader());
            }
            Unmarshaller u = m_context.createUnmarshaller();
            template = (StyleTemplate) u.unmarshal(new FileInputStream(m_file.getAbsolutePath()));
        } catch (JAXBException jaxbEx) {
            throw new LoadConfigException(m_file, jaxbEx);
        } catch (FileNotFoundException fileEx) {
            throw new LoadConfigException(m_file, fileEx);
        }
        m_sBodyTag = template.getBody();
        m_sTitleStyle = template.getTitleStyle();
        m_sContentStyle = template.getContentStyle();
        StyleSheet styleSheet = template.getStyleSheet();
        m_bUseInlineStyleSheet = styleSheet.isUseInline();
        m_sStyleSheetURL = styleSheet.getLink();
        m_sInlineStyleSheet = styleSheet.getInline();
    }

    /**
	 * Saves the configuration back to the file.  If it does not
	 * exist, it will be created.
	 * @throws SaveConfigException if we are unable to save
	 *
	 */
    public synchronized void save() throws SaveConfigException {
        if (!m_file.exists()) {
            try {
                m_file.createNewFile();
            } catch (IOException e) {
                throw new SaveConfigException(m_file, e);
            }
        }
        ObjectFactory factory = new ObjectFactory();
        FileOutputStream outStream = null;
        try {
            StyleTemplate template = factory.createStyleTemplate();
            template.setBody(m_sBodyTag);
            template.setContentStyle(m_sContentStyle);
            template.setTitleStyle(m_sTitleStyle);
            StyleSheet styleSheet = factory.createStyleSheet();
            styleSheet.setInline(m_sInlineStyleSheet);
            styleSheet.setLink(m_sStyleSheetURL);
            styleSheet.setUseInline(m_bUseInlineStyleSheet);
            template.setStyleSheet(styleSheet);
            if (m_context == null) {
                m_context = JAXBContext.newInstance("com.alexmcchesney.poster.templates.style.jaxb", this.getClass().getClassLoader());
            }
            Marshaller marshaller = m_context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            outStream = new FileOutputStream(m_file);
            marshaller.marshal(template, outStream);
        } catch (JAXBException jaxbEx) {
            throw new SaveConfigException(m_file, jaxbEx);
        } catch (FileNotFoundException fileEx) {
            throw new SaveConfigException(m_file, fileEx);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    /**
	 * Builds html for the given post content with this style
	 * applied.
	 * @param sTitle	Title of the post, if any.
	 * @param sContent	Content for the post
	 * @return	String containing post html.
	 */
    public String formatPost(String sTitle, String sContent) {
        sContent = sContent.replaceAll("\r", "<br/>");
        StringBuffer sPreviewHTML = new StringBuffer("<html><head>");
        if (useInlineStyleSheet()) {
            sPreviewHTML.append("<style>");
            sPreviewHTML.append(getInlineStyleSheet());
            sPreviewHTML.append("</style>");
        } else {
            sPreviewHTML.append("<link rel=\"stylesheet\" href=\"" + getStyleSheetURL() + "\" type=\"text/css\" media=\"screen\" title=\"default\" />");
        }
        sPreviewHTML.append("</head>");
        sPreviewHTML.append(getBodyTag());
        sPreviewHTML.append("<div " + getTitleStyle() + ">");
        sPreviewHTML.append(sTitle);
        sPreviewHTML.append("</div>");
        sPreviewHTML.append("<br/>");
        sPreviewHTML.append("<div " + getContentStyle() + ">");
        sPreviewHTML.append(sContent);
        sPreviewHTML.append("</div></body></html>");
        return sPreviewHTML.toString();
    }

    public String getBodyTag() {
        return m_sBodyTag;
    }

    public String getContentStyle() {
        return m_sContentStyle;
    }

    public String getInlineStyleSheet() {
        return m_sInlineStyleSheet;
    }

    public String getStyleSheetURL() {
        return m_sStyleSheetURL;
    }

    public String getTitleStyle() {
        return m_sTitleStyle;
    }

    public void setBodyTag(String sTag) {
        m_sBodyTag = sTag;
    }

    public void setContentStyle(String sStyle) {
        m_sContentStyle = sStyle;
    }

    public void setInlineStyleSheet(String sContent) {
        m_sInlineStyleSheet = sContent;
    }

    public void setStyleSheetURL(String sURL) {
        m_sStyleSheetURL = sURL;
    }

    public void setTitleStyle(String sStyle) {
        m_sTitleStyle = sStyle;
    }

    public boolean useInlineStyleSheet() {
        return m_bUseInlineStyleSheet;
    }

    public void setUseInlineStyleSheet(boolean bUse) {
        m_bUseInlineStyleSheet = bUse;
    }
}
