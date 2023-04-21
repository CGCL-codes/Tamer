package com.once;

import org.apache.log4j.Logger;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import com.once.cache.IObjectCacheEntry;
import com.once.log.PerformanceLogger;
import com.once.server.block.BlockCacheManager;
import com.once.server.block.TransformErrorListener;
import com.once.server.config.ConfigManager;
import com.once.server.data.DataAccessException;
import com.once.server.security.IAuthenticator;
import com.once.server.security.ISecurityManager;
import com.once.server.security.SecurityFactory;

public class ActionFastLoad extends Action {

    private static final long serialVersionUID = 4008725446477389744L;

    private static final Logger m_logger = Logger.getLogger(ActionFastLoad.class);

    private static final String ERROR_DISK = "Disk error reading: ";

    private static final String ERROR_INVALID_BLOCK = "Invalid block: ";

    private static final String ERROR_NO_PERMISSION = "No permission for block: ";

    private static final String ERROR_NO_BLOCK = "Missing XML file for block: ";

    private static final String ERROR_NO_STYLE = "Missing CSS file for block: ";

    private static final String EXTENSION_BLOCK = ".xml";

    private static final String EXTENSION_STYLE = ".css";

    private static final String HEADER_BLOCK_NAME = "block";

    private static final String HEADER_SESSION = "session";

    private static final String HEADER_SUBBLOCK_TEMPLATE = "subblocktemplate";

    private static final String HEADER_XSL_STYLESHEET = "xslstylesheet";

    private static final String XHTML_HEADER = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";

    private static final String XHTML_BAD_HEADER = "<!DOCTYPE container PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";

    private static final String XML_BLOCK_GROUP_BEGIN = "<BLOCKGROUP xmlns=\"http://www.w3.org/1999/xhtml\">";

    private static final String XML_BLOCK_GROUP_TIMED_OUT_BEGIN = "<BLOCKGROUP TIMEOUT=\"TRUE\">";

    private static final String XML_BLOCK_GROUP_END = "</BLOCKGROUP>";

    private static final String XML_ERROR_BLOCK_BEGIN = "<BLOCK><ERROR>";

    private static final String XML_ERROR_BLOCK_END = "</ERROR></BLOCK>";

    private static final String XML_STYLE_BEGIN = "<css>";

    private static final String XML_STYLE_END = "</css>";

    private static final String XHML_SUBBLOCK_BEGIN = "<iframe";

    private static final String XHML_SUBBLOCK_BLOCK_NAME_BEGIN = "block=\"";

    private static final String XML_STYLE_INSERT_BEFORE_TAG = "</BLOCK>";

    protected static final String CLIENT_FORMAT = "text/xml";

    protected void serve(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        resetStartTime();
        String session;
        String blockName;
        ISecurityManager security;
        IAuthenticator authentication;
        PrintWriter page;
        ConfigManager settings;
        String blockText;
        String styleText;
        byte[] fileData;
        int start;
        ArrayList<String> blockList;
        int blockIndex;
        String currentBlock;
        int blockPointer;
        int userMemberId = -1;
        String ownerOrg = null;
        String username;
        String xslStylesheet;
        String subblockTemplate;
        request.getSession();
        response.setContentType(CLIENT_FORMAT);
        response.setCharacterEncoding(CLIENT_ENCODING);
        page = getOutputPage(response);
        session = decodeHeader(HEADER_SESSION, request, true);
        PerformanceLogger plog = PerformanceLogger.getLogger(session, this.getClass().getName());
        plog.logStart("Get block");
        blockName = decodeHeader(HEADER_BLOCK_NAME, request, true);
        security = SecurityFactory.getInstance().getSecurityManager();
        authentication = SecurityFactory.getInstance().getAuthenticator();
        xslStylesheet = decodeHeader(HEADER_XSL_STYLESHEET, request, false);
        subblockTemplate = decodeHeader(HEADER_SUBBLOCK_TEMPLATE, request, false);
        settings = ConfigManager.getInstance();
        blockList = new ArrayList<String>();
        blockList.add(blockList.size(), blockName);
        blockText = "";
        page.print(XHTML_HEADER + "\n");
        if (m_logger.isInfoEnabled()) {
            try {
                username = authentication.getUserLogin(session);
            } catch (DataAccessException except) {
                username = "<unknown>";
            }
            m_logger.info("Fast load request. (User: " + username + " / IP: " + getClientIP(request) + ")");
            m_logger.info("Load \"" + blockName + "\".");
        }
        try {
            if (authentication.isUserLoggedIn(session) == false) {
                page.print(XML_BLOCK_GROUP_TIMED_OUT_BEGIN);
            } else {
                page.print(XML_BLOCK_GROUP_BEGIN);
            }
            userMemberId = authentication.getUserMemberId(session);
            ownerOrg = authentication.getUserOwnerOrganisation(session);
        } catch (DataAccessException except) {
            m_logger.error(except, except);
            page.print(XML_ERROR_BLOCK_BEGIN + except.toString() + XML_ERROR_BLOCK_END);
        }
        if (ownerOrg == null) {
            ownerOrg = "";
        }
        if (ownerOrg.length() > 0) {
            ownerOrg = ownerOrg + "/";
        }
        for (blockIndex = 0; blockIndex < blockList.size(); blockIndex++) {
            currentBlock = (String) blockList.get(blockIndex);
            try {
                if (security.canBlockRead(userMemberId, currentBlock) == true) {
                    plog.logStart("Read xml", "Read block content");
                    fileData = null;
                    IObjectCacheEntry entry = BlockCacheManager.getCache().getEntry(ownerOrg + currentBlock + EXTENSION_BLOCK);
                    if (entry != null) {
                        fileData = (byte[]) entry.getValue();
                    } else {
                        File file = new File(settings.getBlocksRepository() + ownerOrg + currentBlock + EXTENSION_BLOCK);
                        if (file.exists()) {
                            try {
                                plog.logStart("Trasform xml to xhtml", "Perform transformation from xml tp xhtml");
                                fileData = transformToXHTML(readFile(file), settings, xslStylesheet, subblockTemplate);
                                plog.logFinish("Trasform xml to xhtml");
                                BlockCacheManager.getCache().put(ownerOrg + currentBlock + EXTENSION_BLOCK, fileData);
                            } catch (Exception except) {
                                m_logger.error(except, except);
                                page.print(XML_ERROR_BLOCK_BEGIN + ERROR_DISK + ownerOrg + currentBlock + XML_ERROR_BLOCK_END);
                                continue;
                            }
                        } else {
                            entry = BlockCacheManager.getCache().getEntry(currentBlock + EXTENSION_BLOCK);
                            if (entry != null) {
                                fileData = (byte[]) entry.getValue();
                            } else {
                                file = new File(settings.getBlocksRepository() + currentBlock + EXTENSION_BLOCK);
                                if (file.exists()) {
                                    try {
                                        plog.logStart("Trasform xml to xhtml", "Perform transformation from xml tp xhtml");
                                        fileData = transformToXHTML(readFile(file), settings, xslStylesheet, subblockTemplate);
                                        plog.logFinish("Trasform xml to xhtml");
                                        BlockCacheManager.getCache().put(currentBlock + EXTENSION_BLOCK, fileData);
                                    } catch (Exception except) {
                                        m_logger.error(except, except);
                                        page.print(XML_ERROR_BLOCK_BEGIN + ERROR_DISK + currentBlock + XML_ERROR_BLOCK_END);
                                        continue;
                                    }
                                } else {
                                    page.print(XML_ERROR_BLOCK_BEGIN + ERROR_NO_BLOCK + currentBlock + XML_ERROR_BLOCK_END);
                                }
                            }
                        }
                    }
                    plog.logFinish("Read xml");
                    plog.logStart("Read css", "Read stylesheet");
                    if (fileData != null) {
                        blockText = new String(fileData, "UTF-8");
                        fileData = null;
                        entry = BlockCacheManager.getCache().getEntry(ownerOrg + currentBlock + EXTENSION_STYLE);
                        if (entry != null) {
                            fileData = (byte[]) entry.getValue();
                        } else {
                            File file = new File(settings.getBlocksRepository() + ownerOrg + currentBlock + EXTENSION_STYLE);
                            if (file.exists()) {
                                try {
                                    plog.logStart("Trasform css", "Perform css transformation");
                                    fileData = transformCSS(readFile(file));
                                    plog.logFinish("Trasform css");
                                    BlockCacheManager.getCache().put(ownerOrg + currentBlock + EXTENSION_STYLE, fileData);
                                } catch (Exception except) {
                                    m_logger.error(except, except);
                                    page.print(XML_ERROR_BLOCK_BEGIN + ERROR_NO_STYLE + ownerOrg + currentBlock + XML_ERROR_BLOCK_END);
                                    continue;
                                }
                            } else {
                                entry = BlockCacheManager.getCache().getEntry(currentBlock + EXTENSION_STYLE);
                                if (entry != null) {
                                    fileData = (byte[]) entry.getValue();
                                } else {
                                    file = new File(settings.getBlocksRepository() + currentBlock + EXTENSION_STYLE);
                                    if (file.exists()) {
                                        try {
                                            plog.logStart("Trasform css", "Perform css transformation");
                                            fileData = transformCSS(readFile(file));
                                            plog.logFinish("Trasform css");
                                            BlockCacheManager.getCache().put(currentBlock + EXTENSION_STYLE, fileData);
                                        } catch (Exception except) {
                                            m_logger.error(except, except);
                                            page.print(XML_ERROR_BLOCK_BEGIN + ERROR_DISK + currentBlock + XML_ERROR_BLOCK_END);
                                            continue;
                                        }
                                    } else {
                                        page.print(XML_ERROR_BLOCK_BEGIN + ERROR_NO_STYLE + currentBlock + XML_ERROR_BLOCK_END);
                                    }
                                }
                            }
                        }
                        plog.logFinish("Read css");
                        if (fileData != null) {
                            styleText = XML_STYLE_BEGIN + new String(fileData, "UTF-8") + XML_STYLE_END;
                            if ((start = blockText.indexOf(XML_STYLE_INSERT_BEFORE_TAG)) != -1) {
                                page.print(blockText.substring(0, start) + styleText + blockText.substring(start, blockText.length()));
                            } else {
                                if (((start = blockText.indexOf(XML_STYLE_INSERT_BEFORE_TAG.toLowerCase())) != -1)) page.print(blockText.substring(0, start) + styleText + blockText.substring(start, blockText.length())); else page.print(XML_ERROR_BLOCK_BEGIN + ERROR_INVALID_BLOCK + currentBlock + XML_ERROR_BLOCK_END + blockText);
                            }
                        }
                    }
                } else {
                    page.print(XML_ERROR_BLOCK_BEGIN + ERROR_NO_PERMISSION + currentBlock + "\nSession: " + (authentication.isUserLoggedIn(session) == true ? "{" + session + "}" : "[invalid]") + XML_ERROR_BLOCK_END);
                }
            } catch (DataAccessException except) {
                m_logger.error(except, except);
                page.print(XML_ERROR_BLOCK_BEGIN + except.toString() + XML_ERROR_BLOCK_END);
                continue;
            } catch (UnsupportedEncodingException except) {
                m_logger.error(except, except);
                page.print(XML_ERROR_BLOCK_BEGIN + except.toString() + XML_ERROR_BLOCK_END);
                continue;
            }
            if (blockIndex == 0) {
                blockPointer = 0;
                while ((blockPointer = blockText.indexOf(XHML_SUBBLOCK_BEGIN, blockPointer)) != -1) {
                    if ((blockPointer = blockText.indexOf(XHML_SUBBLOCK_BLOCK_NAME_BEGIN, blockPointer)) != -1) {
                        blockPointer += XHML_SUBBLOCK_BLOCK_NAME_BEGIN.length();
                        blockList.add(blockList.size(), blockText.substring(blockPointer, blockPointer = blockText.indexOf("\"", blockPointer + 1)));
                    }
                }
            }
        }
        page.print(XML_BLOCK_GROUP_END);
        page.close();
        plog.logFinish();
    }

    private byte[] transformCSS(String css) {
        String styleText;
        byte[] styleByte;
        int rule;
        int property;
        try {
            styleText = (css + "\ninput { -moz-box-sizing: border-box; }\ntextarea { -moz-box-sizing" + ": border-box; margin-top: 0px; }\nbody { -moz-user-select: none; }" + "\ntable { text-align: start; white-space: normal; line-height: nor" + "mal; font-size: -moz-initial; font-weight: -moz-initial; font-styl" + "e: -moz-initial; font-variant: -moz-initial; }\n.hiddenObject { vi" + "sibility: hidden; display: none; -moz-user-focus: ignore; }\n.hidd" + "enOverflow { overflow-x: hidden; }\n.hiddenScript { display: none;" + " }.isDisabled { -moz-user-input: disabled; }").replaceAll("cursor: default;", "").replaceAll("-moz-background-clip: initial; -moz-background-origin: initial; -moz" + "-background-inline-policy: initial;", "");
            if ((rule = styleText.indexOf(".field ")) > -1) {
                property = styleText.indexOf("-moz-user-select: none;", rule);
                if (property > 0 && property < styleText.indexOf("}", rule)) styleText = styleText.substring(0, property) + styleText.substring(property + "-moz-user-select: none;".length(), styleText.length());
                property = styleText.indexOf("-moz-user-focus: ignore;", rule);
                if (property > 0 && property < styleText.indexOf("}", rule)) styleText = styleText.substring(0, property) + styleText.substring(property + "-moz-user-focus: ignore;".length(), styleText.length());
            }
            if ((rule = styleText.indexOf(".block")) > -1 && (property = styleText.indexOf("}", rule)) > -1) {
                styleText = (styleText.substring(0, rule) + styleText.substring(rule, property).replaceAll("\\sheight:.*?;", "").replaceAll("\\width:.*?;", "") + "border-bottom-width: 0px; border-right-width: 0px;" + styleText.substring(property));
            }
            styleByte = styleText.getBytes("UTF-8");
        } catch (UnsupportedEncodingException except) {
            styleByte = new byte[0];
        }
        return (styleByte);
    }

    private byte[] transformToXHTML(String content, ConfigManager settings, String stylesheet, String subblockTemplate) throws TransformerFactoryConfigurationError, TransformerException, UnsupportedEncodingException {
        Transformer XMLtoXHTML;
        StringReader blockInput;
        StringWriter blockOutput;
        int docTypeIndex;
        ErrorListener trace;
        TransformerFactory factory;
        content = prepareServerSideScript(content);
        blockInput = new StringReader(content);
        blockOutput = new StringWriter();
        trace = new TransformErrorListener();
        factory = TransformerFactory.newInstance();
        factory.setErrorListener(trace);
        XMLtoXHTML = factory.newTransformer(new StreamSource(settings.getXSLRepository() + stylesheet + ".xsl"));
        if (XMLtoXHTML != null) {
            XMLtoXHTML.setParameter("subblockTemplate", "?".equals(subblockTemplate) ? "data:text/html;charset=utf-8,%3C%3Fxml%20version%3D%221.0%22%20enc" + "oding%3D%22UTF-8%22%3F%3E%0D%0A%3C!DOCTYPE%20html%20PUBLIC%20%22-%" + "2F%2FW3C%2F%2FDTD%20XHTML%201.0%20Strict%2F%2FEN%22%20%22http%3A%2" + "F%2Fwww.w3.org%2FTR%2Fxhtml1%2FDTD%2Fxhtml1-strict.dtd%22%3E%3Chtm" + "l%3E%3Chead%3E%3Ctitle%3ETemplate%20Page%3C%2Ftitle%3E%3Cstyle%3E%" + "20body%20%7B%20-moz-user-select%3A%20none%3B%20%7D%20input%20%7B%2" + "0-moz-box-sizing%3A%20border-box%3B%20%7D%20textarea%20%7B%20-moz-" + "box-sizing%3A%20border-box%3B%20%7D%20%3C%2Fstyle%3E%3C%2Fhead%3E%" + "3Cbody%20onload=%22isLoaded=true%22%3E%20%3C%2Fbody%3E%3C%2Fhtml%3" + "E" : subblockTemplate);
            XMLtoXHTML.setParameter("blockCounter", "0");
            XMLtoXHTML.transform(new StreamSource(blockInput), new StreamResult(blockOutput));
            content = blockOutput.toString();
            if ((docTypeIndex = content.indexOf(XHTML_BAD_HEADER)) > -1) content = content.substring(0, docTypeIndex) + content.substring(docTypeIndex + XHTML_BAD_HEADER.length());
            if (content.indexOf("<") > -1) content = content.substring(content.indexOf("<"));
        } else {
            content = new String("Bad XSL. Can't create transformer.\n\n" + trace);
        }
        return (content.getBytes("UTF-8"));
    }

    private String prepareServerSideScript(String content) {
        try {
            Pattern ptn = Pattern.compile("(<SCRIPT .*?>)(.*?)(</SCRIPT)", Pattern.DOTALL);
            Matcher match = ptn.matcher(content);
            StringBuffer sb = new StringBuffer();
            while (match.find()) {
                String beforeScript = match.group(1);
                String script = match.group(2);
                String afterScript = match.group(3);
                Pattern subPtn = Pattern.compile("(\\$\\s*?\\(\".+?\n\\s*?\"\\);?)", Pattern.DOTALL);
                Matcher subMatch = subPtn.matcher(script);
                StringBuffer subSb = new StringBuffer();
                while (subMatch.find()) {
                    String serverScript = subMatch.group(1);
                    serverScript = serverScript.replaceAll("\\r\\n", "\\\\n");
                    serverScript = serverScript.replaceAll("\\r", "\\\\n");
                    serverScript = serverScript.replaceAll("\\n", "\\\\n");
                    serverScript = serverScript.replaceAll("\\\\", "\\\\\\\\");
                    serverScript = serverScript.replaceAll("\\$", "\\\\\\$");
                    subMatch.appendReplacement(subSb, serverScript);
                }
                subMatch.appendTail(subSb);
                script = subSb.toString();
                script = script.replaceAll("\\\\", "\\\\\\\\");
                script = script.replaceAll("\\$", "\\\\\\$");
                beforeScript = beforeScript.replaceAll("\\\\", "\\\\\\\\");
                beforeScript = beforeScript.replaceAll("\\$", "\\\\\\$");
                afterScript = afterScript.replaceAll("\\\\", "\\\\\\\\");
                afterScript = afterScript.replaceAll("\\$", "\\\\\\$");
                match.appendReplacement(sb, beforeScript + script + afterScript);
            }
            match.appendTail(sb);
            match = Pattern.compile("(if\\(_getEnvironment\\(\\) == \"client\"\\)\\{)", Pattern.DOTALL).matcher(sb.toString());
            sb = new StringBuffer();
            while (match.find() == true) {
                match.appendReplacement(sb, match.group(1) + "if(arguments[0]) client.currentEventObject = arguments[0];");
            }
            match.appendTail(sb);
            content = sb.toString();
        } catch (Throwable except) {
            m_logger.error(except, except);
        }
        return (content);
    }
}
