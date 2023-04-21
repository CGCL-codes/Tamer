package com.goodcodeisbeautiful.archtea.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import com.goodcodeisbeautiful.archtea.io.WikiText2HtmlReader;
import com.goodcodeisbeautiful.archtea.io.data.FileDataContainerAdapter;
import com.goodcodeisbeautiful.archtea.io.data.filter.BIN2TXTDataFilter;
import com.goodcodeisbeautiful.archtea.io.data.filter.DataFilter;
import com.goodcodeisbeautiful.archtea.io.data.filter.DataFilterAdapter;
import com.goodcodeisbeautiful.archtea.io.data.filter.WIKITXT2HTMLDataFilter;

public class ManualGenerator {

    private static final String RET = "\r\n";

    private static final String PAGE_TOP = "<?xml version=\"1.0\" encoding=\"${charset}\"?>" + RET + "<html xml:lang=\"${xml.lang}\" lang=\"${html.lang}\">" + RET + "<head>" + RET + "  <title>${title}</title>" + RET + "</head>" + RET + "<body>" + RET;

    private static final String OUTPUT_ENCODING = "utf-8";

    private File _baseFile;

    private File _outDir;

    private String _encoding;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Please set base manual file.");
            System.exit(1);
        }
        if (args.length == 1) {
            System.out.println("Please set output dir.");
            System.exit(1);
        }
        File baseFile = new File(args[0]);
        String encoding = "UTF-8";
        if (baseFile.getName().endsWith(".ja.txt")) encoding = "Windows-31J";
        File outDir = new File(args[1]);
        ManualGenerator generator = new ManualGenerator(baseFile.getCanonicalFile(), encoding, outDir.getCanonicalFile());
        generator.generate();
    }

    public ManualGenerator(File baseFile, String encoding, File outDir) {
        super();
        _baseFile = baseFile;
        _encoding = encoding;
        _outDir = outDir;
    }

    public void generate() throws IOException, TransformerConfigurationException, TransformerException {
        InputStream in = null;
        Reader r = null;
        try {
            System.out.println("Run Manual generator ...");
            r = getReader();
            StringBuffer strBuff = new StringBuffer();
            char[] buff = new char[2048];
            int len = r.read(buff, 0, buff.length);
            while (len != -1) {
                strBuff.append(new String(buff, 0, len));
                System.out.print(new String(buff, 0, len));
                len = r.read(buff, 0, buff.length);
            }
            StreamSource xsltSrc = new StreamSource(getContentXSL());
            StreamSource source = new StreamSource(new StringReader(strBuff.toString()));
            StreamResult result = new StreamResult(new FileOutputStream(getMainOutFile()));
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(xsltSrc);
            transformer.transform(source, result);
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (Exception e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            System.out.println("Done.");
        }
    }

    private Reader getReader() throws IOException {
        DataFilterAdapter dataFilter = new DataFilterAdapter(new FileDataContainerAdapter(_baseFile.getCanonicalPath()));
        BIN2TXTDataFilter bin2txtFilter = new BIN2TXTDataFilter();
        Properties props = new Properties();
        props.setProperty(BIN2TXTDataFilter.PROP_DEFAULT_CHARSET, _encoding);
        bin2txtFilter.setProperties(props);
        WIKITXT2HTMLDataFilter txt2htmlFilter = new WIKITXT2HTMLDataFilter();
        txt2htmlFilter.setPageTop(PAGE_TOP);
        txt2htmlFilter.setProperties(props);
        bin2txtFilter.setSource(dataFilter);
        txt2htmlFilter.setSource(bin2txtFilter);
        return txt2htmlFilter.getReader();
    }

    private File getMainOutFile() throws IOException {
        String name = _baseFile.getName();
        name = name.substring(0, name.length() - 3) + "html";
        return new File(_outDir.getCanonicalPath() + File.separator + name);
    }

    private String getContentXSL() throws IOException {
        String name = _baseFile.getName();
        if (name.endsWith(".ja.txt")) {
            name = name.substring(0, name.length() - ".ja.txt".length()) + "-content.xsl";
        } else {
            name = name.substring(0, name.length() - ".txt".length()) + "-content.xsl";
        }
        return _baseFile.getParent() + File.separator + name;
    }
}
