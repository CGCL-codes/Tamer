package com.armatiek.infofuze.source.filesystem.webcrawl;

import java.io.File;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.armatiek.infofuze.config.Config;
import com.armatiek.infofuze.config.Definitions;
import com.armatiek.infofuze.error.InfofuzeException;
import com.armatiek.infofuze.source.filesystem.local.LocalFileSystemSource;
import com.armatiek.infofuze.stream.filesystem.FileIf;
import com.armatiek.infofuze.utils.XPathUtils;
import com.armatiek.infofuze.utils.XmlUtils;

/**
 * JAXP based Source class that can be used to stream an XML representation of
 * the result of a website spider/crawl.
 * 
 * @author Maarten Kroon
 */
public class WebCrawlSourceHTTrack extends LocalFileSystemSource {

    private static final Logger logger = LoggerFactory.getLogger(WebCrawlSourceHTTrack.class);

    protected String executable;

    protected String cachePath;

    protected Templates commandArgsTemplates;

    public WebCrawlSourceHTTrack(Element configElem) {
        super(configElem);
        XPath xpath = XPathFactory.newInstance().newXPath();
        executable = XPathUtils.getStringValue(xpath, "@executable", configElem, null);
        cachePath = XPathUtils.getStringValue(xpath, "@cachePath", configElem, null);
    }

    @Override
    public FileIf getFile(String location) {
        try {
            URI uri = new URI(location);
            String pathStr = (uri.getPath() != null) ? File.separatorChar + uri.getPath().replace('/', File.separatorChar) : "";
            File pathFile = new File(cachePath, uri.getHost() + pathStr);
            return super.getFile(pathFile.getAbsolutePath());
        } catch (Exception e) {
            throw new InfofuzeException("Could not create LocalFile for \"" + location + "\"");
        }
    }

    protected void mirrorSites() {
        logger.info("Mirroring websites ...");
        try {
            if (commandArgsTemplates == null) {
                TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
                String xslPath = new File(Config.getInstance().getHomeDir(), Definitions.DIRNAME_XSLT + File.separatorChar + "httrack" + File.separatorChar + "httrack-cli.xsl").getAbsolutePath();
                commandArgsTemplates = factory.newTemplates(new SAXSource(new InputSource(xslPath)));
            }
            Transformer transformer = commandArgsTemplates.newTransformer();
            transformer.setParameter("{" + Definitions.INFOFUZE_NAMESPACE + "}source-name", getName());
            if (cachePath == null) {
                cachePath = new File(Config.getInstance().getHomeDir(), "webcrawl-cache").getAbsolutePath();
            }
            File cacheDir = new File(cachePath);
            if (!cacheDir.isDirectory()) {
                cacheDir.mkdirs();
            }
            transformer.setParameter("{" + Definitions.INFOFUZE_NAMESPACE + "}cache-path", cachePath);
            transformer.setParameter("{" + Definitions.INFOFUZE_NAMESPACE + "}transform-mode", transformMode.toString());
            DOMSource source = new DOMSource(configElem);
            Document argsDoc = XmlUtils.getEmptyDOM();
            DOMResult result = new DOMResult(argsDoc);
            transformer.transform(source, result);
            CommandLine cmdLine = new CommandLine(executable);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList argNodes = (NodeList) xpath.evaluate("/args/arg/text()", argsDoc, XPathConstants.NODESET);
            for (int i = 0; i < argNodes.getLength(); i++) {
                cmdLine.addArgument(argNodes.item(i).getNodeValue());
            }
            DefaultExecutor executor = new DefaultExecutor();
            executor.setWorkingDirectory(new File(executable).getParentFile());
            OutputStream systemOut = new SystemLogOutputStream(1);
            executor.setStreamHandler(new PumpStreamHandler(systemOut, systemOut));
            executor.execute(cmdLine);
        } catch (Exception e) {
            throw new InfofuzeException("Could not mirror sites for Source \"" + name + "\"", e);
        }
    }

    @Override
    public Reader getInternalReader() {
        mirrorSites();
        return super.getInternalReader();
    }

    private class SystemLogOutputStream extends LogOutputStream {

        private SystemLogOutputStream(int level) {
            super(level);
        }

        protected void processLine(String line, int level) {
            System.out.println(line);
        }
    }
}
