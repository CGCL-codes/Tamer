package evs.idl;

import java.io.*;
import javax.xml.xpath.*;
import org.xml.sax.InputSource;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;

/**
 * A
 */
public class IDLCompiler {

    /**
     * Accept one command line argument: the name of an XML IDL Description File
     */
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.err.println("Usage:");
                System.err.println("  java " + IDLCompiler.class.getName() + " xmlFileName [resourcesDir] [outputDir]");
                System.exit(1);
            }
            File xmlFile = new File(args[0]);
            String basedir = (args[1].length() == 0) ? "resources" : args[1];
            String outDir = (args[2].length() == 0) ? "src" : args[2];
            XPath xpath = XPathFactory.newInstance().newXPath();
            String packageExp = "/idl/bean/@package";
            String nameExp = "/idl/bean/@name";
            InputSource inputSource = new InputSource(args[0]);
            String beanName = (String) xpath.evaluate(nameExp, inputSource);
            String beanPackage = (String) xpath.evaluate(packageExp, inputSource);
            File IFaceXSLT = new File(basedir + "/idl2iface.xsl");
            File ProxyXSLT = new File(basedir + "/idl2proxy.xsl");
            File StubXSLT = new File(basedir + "/idl2stub.xsl");
            System.out.println(outDir);
            outDir = outDir.concat("/" + beanPackage.replace('.', '/'));
            boolean success = (new File(outDir)).mkdirs();
            System.out.println(outDir);
            if (success) {
                System.out.println("Directories: " + outDir + " created");
            } else {
                System.out.println("Coud not create Directory:" + outDir);
            }
            File IFaceFile = new File(outDir + "/" + beanName + "Interface.java");
            File ProxyFile = new File(outDir + "/" + beanName + "Proxy.java");
            File StubFile = new File(outDir + "/" + beanName + ".java");
            transform(xmlFile, IFaceXSLT, IFaceFile);
            System.out.println("created:\t" + IFaceFile.getCanonicalPath());
            transform(xmlFile, ProxyXSLT, ProxyFile);
            System.out.println("created:\t" + ProxyFile.getCanonicalPath());
            if (StubFile.exists()) {
                System.out.println("stub allready exists:\t" + StubFile.getCanonicalPath());
            } else {
                transform(xmlFile, StubXSLT, StubFile);
                System.out.println("created:\t" + StubFile.getCanonicalPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void transform(File xmlFile, File xslFile, File Output) throws javax.xml.transform.TransformerException {
        Source xmlSource = new javax.xml.transform.stream.StreamSource(xmlFile);
        Source xsltSource = new javax.xml.transform.stream.StreamSource(xslFile);
        Result result = new javax.xml.transform.stream.StreamResult(Output);
        TransformerFactory transFact = javax.xml.transform.TransformerFactory.newInstance();
        Transformer trans = transFact.newTransformer(xsltSource);
        trans.transform(xmlSource, result);
    }
}
