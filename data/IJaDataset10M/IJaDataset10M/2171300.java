package pt.utl.ist.lucene.utils.extractors;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import pt.utl.ist.lucene.analyzer.LgteAnalyzer;
import pt.utl.ist.lucene.utils.Files;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Creator: Jorge Machado
 * Email: jmachado@ext.bn.pt
 * User: Utilizador
 * DateMonthYearArticles: 19/Set/2005
 * Time: 14:12:46
 * To change this template use File | Settings | File Templates.
 */
public class ParserCallBack extends HTMLEditorKit.ParserCallback {

    private static final Logger logger = Logger.getLogger(ParserCallBack.class);

    private boolean garbage = false;

    private Writer out;

    HashMap<String, String> metaTags = new HashMap<String, String>();

    HashMap<String, String> outLinks = new HashMap<String, String>();

    HashMap<String, WebImage> images = new HashMap<String, WebImage>();

    List<WebImage> imagesLst = new ArrayList<WebImage>();

    String coments = "";

    StringBuilder text = new StringBuilder();

    String title = "";

    String author = "";

    String keywords = "";

    String abstractTxt = "";

    String description = "";

    String script = "";

    String bannedTextStart = "";

    String bannedTextEnd = "";

    StringBuilder anchor = new StringBuilder();

    StringBuilder h1 = new StringBuilder();

    StringBuilder h2 = new StringBuilder();

    StringBuilder h3 = new StringBuilder();

    StringBuilder h4 = new StringBuilder();

    StringBuilder h5 = new StringBuilder();

    StringBuilder h6 = new StringBuilder();

    StringBuilder h7 = new StringBuilder();

    StringBuilder h = new StringBuilder();

    boolean body = false;

    boolean inAnchor = false;

    boolean inH1 = false;

    boolean inH2 = false;

    boolean inH3 = false;

    boolean inH4 = false;

    boolean inH5 = false;

    boolean inH6 = false;

    boolean inH7 = false;

    boolean inHead = false;

    boolean inTitle = false;

    boolean inScript = false;

    boolean inHeaderTag = false;

    String headerTag = "";

    String textTag = "";

    boolean startExtracting = true;

    boolean stopExtracting = false;

    public ParserCallBack(Writer out) {
        this.out = out;
    }

    public void handleText(char[] text, int position) {
        String str = new String(text);
        while (str.length() > 0 && str.charAt(0) == '>') str = str.substring(1);
        if (inAnchor) anchor.append(' ').append(text);
        if (inH1) {
            h1.append(' ').append(text);
            h.append(' ').append(text);
        } else if (inH2) {
            h2.append(' ').append(text);
            h.append(' ').append(text);
        } else if (inH3) {
            h3.append(' ').append(text);
            h.append(' ').append(text);
        } else if (inH4) {
            h4.append(' ').append(text);
            h.append(' ').append(text);
        } else if (inH5) {
            h5.append(' ').append(text);
            h.append(' ').append(text);
        } else if (inH6) {
            h6.append(' ').append(text);
            h.append(' ').append(text);
        } else if (inH7) {
            h7.append(' ').append(text);
            h.append(' ').append(text);
        }
        if (!inTitle && !garbage && !inScript) {
            try {
                if (out != null) {
                    out.write(' ');
                    out.write(text);
                    out.flush();
                }
            } catch (IOException e) {
                System.err.println(e);
            }
            if (startExtracting && !stopExtracting && body) this.text.append(' ').append(str); else if (!startExtracting) bannedTextStart += " " + str; else if (stopExtracting) bannedTextEnd += " " + str;
        }
        if (inTitle && inHead) title += " " + new String(text);
        if (inScript) script += " " + new String(text);
    }

    public void handleComment(char[] data, int pos) {
        String dataStr = new String(data);
        if (inScript) script += " " + dataStr; else {
            coments += " " + dataStr;
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        HtmlTag htmlTag = HtmlTag.parse(t.toString());
        if (htmlTag == HtmlTag.HEAD) inHead = false; else if (htmlTag == HtmlTag.TITLE) inTitle = false; else if (htmlTag == HtmlTag.H1) inH1 = false; else if (htmlTag == HtmlTag.H2) inH2 = false; else if (htmlTag == HtmlTag.H3) inH3 = false; else if (htmlTag == HtmlTag.H4) inH4 = false; else if (htmlTag == HtmlTag.H5) inH5 = false; else if (htmlTag == HtmlTag.H6) inH6 = false; else if (htmlTag == HtmlTag.H7) inH7 = false; else if (htmlTag == HtmlTag.SCRIPT) inScript = false; else if (htmlTag == HtmlTag.STYLE) garbage = false; else if (htmlTag == HtmlTag.A) inAnchor = false;
    }

    public void handleError(String errorMsg, int pos) {
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        String content = null;
        String name = null;
        String http_equiv = null;
        if (t.toString().toLowerCase().equals("meta")) {
            Enumeration<?> enumeration = a.getAttributeNames();
            while (enumeration.hasMoreElements()) {
                Object obj = enumeration.nextElement();
                if (obj instanceof Attribute) {
                    Attribute attribute = (Attribute) obj;
                    if (attribute.toString().compareTo("content") == 0) content = (String) a.getAttribute(attribute); else if (attribute.toString().compareTo("name") == 0) {
                        name = (String) a.getAttribute(attribute);
                        if (name.compareTo("author") == 0 || name.compareTo("description") == 0 || name.compareTo("abstract") == 0 || name.compareTo("keywords") == 0 || name.compareTo("title") == 0) {
                        }
                    } else if (attribute.toString().compareTo("http-equiv") == 0) {
                        http_equiv = (String) a.getAttribute(attribute);
                    }
                }
                if (http_equiv != null && http_equiv.toLowerCase().compareTo("title") == 0 && content != null) title += " " + content; else {
                    if (name != null && content != null) {
                        String last = metaTags.get(name);
                        if (last != null) metaTags.put(name.toLowerCase(), last + " " + content); else metaTags.put(name.toLowerCase(), content);
                    }
                }
            }
        } else if (!inScript && t.toString().toLowerCase().equals("img")) {
            WebImage wimage = new WebImage();
            wimage.setTextOffSet(text.toString().length());
            Enumeration<?> enumeration = a.getAttributeNames();
            while (enumeration.hasMoreElements()) {
                Object obj = enumeration.nextElement();
                if (obj instanceof Attribute) {
                    Attribute attribute = (Attribute) obj;
                    if (attribute.toString().compareTo("src") == 0) {
                        String src = (String) a.getAttribute(attribute);
                        if (src.length() > 0) wimage.setSrc(src);
                    } else if (attribute.toString().compareTo("alt") == 0) {
                        String alt = (String) a.getAttribute(attribute);
                        if (alt.length() > 0) wimage.setAlt(alt);
                    }
                }
            }
            if (wimage.getSrc() != null && wimage.getSrc().length() > 0) {
                images.put(wimage.getSrc(), wimage);
                imagesLst.add(wimage);
            }
        }
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        HtmlTag htmlTag = HtmlTag.parse(t.toString());
        if (htmlTag == HtmlTag.BODY) body = true; else if (htmlTag == HtmlTag.A) inAnchor = true; else if (htmlTag == HtmlTag.HEAD) inHead = true; else if (htmlTag == HtmlTag.TITLE) inTitle = true; else if (htmlTag == HtmlTag.SCRIPT) inScript = true; else if (htmlTag == HtmlTag.STYLE) garbage = true; else if (htmlTag == HtmlTag.H1) inH1 = true; else if (htmlTag == HtmlTag.H2) inH2 = true; else if (htmlTag == HtmlTag.H3) inH3 = true; else if (htmlTag == HtmlTag.H4) inH4 = true; else if (htmlTag == HtmlTag.H5) inH5 = true; else if (htmlTag == HtmlTag.H6) inH6 = true; else if (htmlTag == HtmlTag.H7) inH7 = true; else {
            inScript = false;
            garbage = false;
            inTitle = false;
        }
    }

    public Writer getOut() {
        return out;
    }

    public String getBannedTextEnd() {
        return bannedTextEnd;
    }

    public String getBannedTextStart() {
        return bannedTextStart;
    }

    public HashMap<String, String> getMetaTags() {
        return metaTags;
    }

    public String getComments() {
        return coments;
    }

    public String getAnchor() {
        return anchor.toString();
    }

    public String getH() {
        return h.toString();
    }

    public String getH1() {
        return h1.toString();
    }

    public String getH2() {
        return h2.toString();
    }

    public String getH3() {
        return h3.toString();
    }

    public String getH4() {
        return h4.toString();
    }

    public String getH5() {
        return h5.toString();
    }

    public String getH6() {
        return h6.toString();
    }

    public String getH7() {
        return h7.toString();
    }

    public String getText() {
        return text.toString();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return metaTags.get("author");
    }

    public String getAbstract() {
        return metaTags.get("abstract");
    }

    public String getKeywords() {
        return metaTags.get("keywords");
    }

    public String getDescription() {
        return metaTags.get("description");
    }

    public Set<String> getOutLinks(String urlStr) {
        String[] url = getMainSite(urlStr);
        String mainSite = url[0];
        String mainSiteAndPath = url[1];
        Set<String> links = new HashSet<String>();
        for (String href : outLinks.keySet()) {
            String link;
            if (href.startsWith("http://") || href.startsWith("https://")) link = UrlUtil.normalizeUrlEnd(href); else {
                if (href.startsWith("/")) link = UrlUtil.normalizeUrlEnd(mainSite + href); else link = UrlUtil.normalizeUrlEnd(mainSiteAndPath + "/" + href);
            }
            if (link != null) links.add(link);
        }
        return links;
    }

    public String[] getMainSite(String urlStr) {
        if (urlStr == null) {
            logger.error("url must respect: [protocol]domain[/path] --> is null");
            return null;
        }
        if (urlStr.startsWith("/")) {
            logger.error("url must respect: [protocol]domain[/path] --> starts with /");
            return null;
        }
        if (!urlStr.startsWith("http://") && !urlStr.startsWith("https://")) {
            urlStr = "http://" + urlStr;
        }
        String mainSite;
        String mainSiteAndPath;
        int slashDomain = urlStr.indexOf("/", 7);
        if (slashDomain > 0) {
            mainSite = urlStr.substring(0, slashDomain);
            int questionMark = urlStr.indexOf("?");
            String path;
            if (questionMark > 0) path = urlStr.substring(slashDomain, questionMark); else path = urlStr.substring(slashDomain);
            if (!path.endsWith("/")) path = path.substring(0, path.lastIndexOf("/"));
            mainSiteAndPath = mainSite + path;
            if (mainSiteAndPath.endsWith("/")) mainSiteAndPath = mainSiteAndPath.substring(0, mainSiteAndPath.length() - 1);
        } else {
            mainSite = urlStr;
            mainSiteAndPath = urlStr;
        }
        return new String[] { mainSite, mainSiteAndPath };
    }

    private String getStrTokens(List<Token> list, int from, int to) {
        String toReturn = "";
        for (int i = from; i < list.size() && i < to; i++) toReturn += " " + (list.get(i)).termText();
        return toReturn;
    }

    private List<Token> fullBuffer(TokenStream stream) throws IOException {
        List<Token> tokenBuffer = new ArrayList<Token>();
        Token token;
        while (tokenBuffer.size() < WebImage.SURROUND_IMAGE_WORDS) {
            if ((token = stream.next()) != null) tokenBuffer.add(token); else break;
        }
        return tokenBuffer;
    }

    private Set<WebImage> processHalfBuffer(List<Token> tokenBuffer, List<WebImage> images, String urlString) throws IOException {
        Set<WebImage> processedImages = new HashSet<WebImage>();
        Token token;
        int half = WebImage.SURROUND_IMAGE_WORDS / 2;
        for (int i = 0; i <= half && i < tokenBuffer.size() && images.size() > 0; i++) {
            token = tokenBuffer.get(i);
            WebImage image;
            while (images.size() > 0 && (image = images.get(0)).getTextOffSet() < token.endOffset()) {
                String imageText = getStrTokens(tokenBuffer, 0, i);
                imageText += getStrTokens(tokenBuffer, i, i + half);
                image = processImage(image, urlString, imageText);
                processedImages.add(image);
                images.remove(0);
            }
        }
        return processedImages;
    }

    private Set<WebImage> processBuffer(TokenStream stream, List<Token> tokenBuffer, List<WebImage> images, String urlString) throws IOException {
        Set<WebImage> processedImages = new HashSet<WebImage>();
        Token token;
        int half = WebImage.SURROUND_IMAGE_WORDS / 2;
        while (tokenBuffer.size() == WebImage.SURROUND_IMAGE_WORDS && images.size() > 0) {
            tokenBuffer.remove(0);
            if ((token = stream.next()) == null) {
                break;
            } else tokenBuffer.add(token);
            token = tokenBuffer.get(half);
            WebImage image;
            while (images.size() > 0 && (image = images.get(0)).getTextOffSet() < token.endOffset()) {
                String imageText = getStrTokens(tokenBuffer, 0, WebImage.SURROUND_IMAGE_WORDS);
                image = processImage(image, urlString, imageText);
                processedImages.add(image);
                images.remove(0);
            }
        }
        return processedImages;
    }

    private Set<WebImage> processLastHalfBuffer(List<Token> tokenBuffer, List<WebImage> images, String urlString) throws IOException {
        Set<WebImage> processedImages = new HashSet<WebImage>();
        Token token;
        int half = WebImage.SURROUND_IMAGE_WORDS / 2;
        while (half < tokenBuffer.size() && images.size() > 0) {
            token = tokenBuffer.get(half);
            WebImage image;
            while (images.size() > 0 && (image = images.get(0)).getTextOffSet() < token.endOffset()) {
                String imageText = getStrTokens(tokenBuffer, 0, WebImage.SURROUND_IMAGE_WORDS);
                image = processImage(image, urlString, imageText);
                processedImages.add(image);
                images.remove(0);
            }
            tokenBuffer.remove(0);
        }
        if (half >= tokenBuffer.size()) {
            for (WebImage image : images) {
                String imageText = getStrTokens(tokenBuffer, 0, WebImage.SURROUND_IMAGE_WORDS);
                image = processImage(image, urlString, imageText);
                processedImages.add(image);
            }
            images.clear();
        }
        return processedImages;
    }

    public List<WebImage> getImages(String urlStr) {
        try {
            List<WebImage> toReturn = new ArrayList<WebImage>();
            Analyzer analyzer = LgteAnalyzer.defaultAnalyzer;
            TokenStream stream = analyzer.tokenStream(null, new StringReader(text.toString()));
            List<Token> tokenBuffer = fullBuffer(stream);
            toReturn.addAll(processHalfBuffer(tokenBuffer, imagesLst, urlStr));
            toReturn.addAll(processBuffer(stream, tokenBuffer, imagesLst, urlStr));
            toReturn.addAll(processLastHalfBuffer(tokenBuffer, imagesLst, urlStr));
            return toReturn;
        } catch (IOException e) {
            logger.error("cant get images for:" + urlStr);
        }
        return null;
    }

    public WebImage processImage(WebImage image, String urlStr, String imagetext) {
        String[] url = getMainSite(urlStr);
        String mainSite = url[0];
        String mainSiteAndPath = url[1];
        image.setParentUrl(urlStr);
        String link;
        if (image.getSrc().startsWith("http://") || image.getSrc().startsWith("https://")) link = UrlUtil.normalizeUrlEnd(image.getSrc()); else {
            if (image.getSrc().startsWith("/")) link = UrlUtil.normalizeUrlEnd(mainSite + image.getSrc()); else link = UrlUtil.normalizeUrlEnd(mainSiteAndPath + "/" + image.getSrc());
        }
        image.setSurroundText(imagetext);
        String extension = Files.getExtension(image.getSrc());
        if (extension != null) {
            image.setExtension(extension);
        }
        image.setCompleteUrl(link);
        try {
            URL urlFinal = new URL(image.getCompleteUrl());
            image.setFilename(urlFinal.getFile());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (link != null) return image; else return null;
    }

    public String getScript() {
        return script;
    }

    public void clear() {
        metaTags.clear();
        garbage = false;
        out = null;
        coments = "";
        text = new StringBuilder();
        title = "";
        script = "";
        anchor = new StringBuilder();
        h = new StringBuilder();
        h1 = new StringBuilder();
        h2 = new StringBuilder();
        h3 = new StringBuilder();
        h4 = new StringBuilder();
        h5 = new StringBuilder();
        h6 = new StringBuilder();
        h7 = new StringBuilder();
        inTitle = false;
        inScript = false;
    }
}
