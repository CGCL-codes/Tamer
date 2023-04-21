package com.tg.xmlgenerater;

import java.io.ByteArrayOutputStream;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import com.tg.filehandler.TgCommonFile;
import com.tg.filehandler.TgFile;

public class DescGenerator {

    public static final String ENCODING = "utf-8";

    public TgFile generate(String fileName, List<TgFile> files) {
        TgFile descFile = new TgCommonFile(fileName);
        try {
            XMLWriter writer = null;
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(DescGenerator.ENCODING);
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("tg");
            for (TgFile file : files) {
                Element image = root.addElement("image");
                image.addAttribute("name", file.getName());
                image.addAttribute("url", file.getPath());
            }
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            writer = new XMLWriter(bas, format);
            writer.write(document);
            writer.close();
            byte[] bytes = bas.toByteArray();
            descFile.setData(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return descFile;
    }
}
