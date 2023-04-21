package com.sobek.web.shop.template;

import com.sobek.web.shop.template.ReplacementValue;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Alexandra Sobek
 */
public class SVGImageConverter {

    public static final String VALUE = "VALUE";

    public void convertImage(String svgFilePath, List<ReplacementValue> replacementValues, String destination, String fileType) throws Exception {
        File svgFile = new File(svgFilePath);
        File tempSvgFile = new File(System.getProperty("java.io.tmpdir") + "/" + svgFile.getName());
        FileUtils.copyFile(svgFile, tempSvgFile);
        Document svgDocument = loadSVGDocument(tempSvgFile.getAbsolutePath());
        processReplacing(svgDocument, replacementValues);
        saveSVGDocument(svgDocument, tempSvgFile.getAbsolutePath());
        SVG2PNG(tempSvgFile.getAbsolutePath(), destination, fileType);
    }

    private Document loadSVGDocument(String svgFile) throws Exception {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        Document doc = f.createDocument("file://" + svgFile);
        return doc;
    }

    private void saveSVGDocument(Document doc, String desitination) throws Exception {
        XMLSerializer serializer = new XMLSerializer();
        serializer.setOutputCharStream(new java.io.FileWriter(desitination));
        serializer.serialize(doc);
    }

    private void processReplacing(Document doc, List<ReplacementValue> placeHolders) throws Exception {
        for (int i = 0; i < placeHolders.size(); i++) {
            ReplacementValue replacementValue = placeHolders.get(i);
            Element contextElement = doc.getElementById(replacementValue.getElementId());
            if (replacementValue.getReplaceType().equals(ReplacementValue.ReplaceType.REPLACE_TEXT_NODE)) {
                contextElement.setTextContent(replacementValue.getValue());
            } else if (replacementValue.getReplaceType().equals(ReplacementValue.ReplaceType.REPLACE_STYLE_ATTRIBUTE)) {
                String styleAttribute = contextElement.getAttribute("style");
                int indexOf = styleAttribute.indexOf(replacementValue.getName());
                int indexOf2 = styleAttribute.indexOf(";", indexOf);
                String replacedStyle = styleAttribute.substring(0, indexOf) + replacementValue.getName() + ":" + replacementValue.getValue() + styleAttribute.substring(indexOf2, styleAttribute.length());
                contextElement.setAttribute("style", replacedStyle);
            }
        }
    }

    private void SVG2PNG(String svgsource, String destfile, String fileType) throws TranscoderException, IOException {
        ImageTranscoder t = null;
        if (fileType.equals("png")) t = new PNGTranscoder(); else if (fileType.equals("jpg")) t = new JPEGTranscoder();
        String svgURI = new File(svgsource).toURL().toString();
        TranscoderInput input = new TranscoderInput(svgURI);
        OutputStream ostream = new FileOutputStream(destfile);
        TranscoderOutput output = new TranscoderOutput(ostream);
        t.transcode(input, output);
        ostream.flush();
        ostream.close();
    }

    public static void main(String[] args) throws Exception {
        SVGImageConverter imageConverter = new SVGImageConverter();
        ReplacementContainer replacementContainer = DynamicImageFactory.loadReplacementContainer("src/main/test/dynamic-images.xml");
        imageConverter.convertImage(replacementContainer.getReplacements().get(0).getPath(), replacementContainer.getReplacements().get(0).getReplacementValues(), "/tmp/default-button.png", "png");
    }
}
