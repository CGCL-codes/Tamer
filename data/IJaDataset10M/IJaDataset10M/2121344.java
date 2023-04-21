package examples.debugger;

import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.Properties;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jato.Interpreter;
import org.jato.JatoIntegrator;
import org.jato.JatoScript;
import org.jato.ScriptBuilder;
import org.jato.debug.JatoDebugger;

/**
 * To run this example type the following on the command line:<p>
 * <pre>
 * % java examples.debugger.SimpleJavaToXml
 * </pre>
 *
 * @author Andy Krumel
 */
public class SimpleJavaToXml implements JatoIntegrator {

    public Object getObject(String key, Properties parms) {
        Properties props = System.getProperties();
        return props.keys();
    }

    public void publish(String key, Object obj, int state) {
        throw new IllegalStateException("publish() is not supported by integrator");
    }

    public static void main(String args[]) throws Exception {
        ScriptBuilder.loadDefaultJatoDefs();
        Document scriptDoc = new SAXBuilder().build(new StringReader(sJatoXML));
        JatoScript script = ScriptBuilder.createJatoScript(scriptDoc.getRootElement(), true);
        Interpreter jato = new Interpreter(script, new SimpleJavaToXml());
        Document xmlOut = new Document(new Element("system-properties"));
        JatoDebugger debug = new JatoDebugger(jato);
        jato.attachDebugger(debug);
        debug.transform(null, xmlOut.getRootElement());
        XMLOutputter out = new XMLOutputter();
        out.setNewlines(true);
        System.out.println("Jato script generated:\n");
        out.output(xmlOut, System.out);
        FileOutputStream fos = new FileOutputStream("simple.xml");
        out.output(xmlOut, fos);
        fos.close();
    }

    static String sJatoXML = "<?xml version='1.0' encoding='UTF-8'?>                  \n" + "<Jato:script xmlns:Jato='http://jato.sourceforge.net'>  \n" + "  <Jato:translate key='prop-names'>                     \n" + "    <property>                                          \n" + "      <Jato:attribute name='name' key='this'/>          \n" + "      <Jato:attribute name='value' invoke='getProperty' \n" + "                                   class='System'>      \n" + "        <Jato:param type='this'/>                       \n" + "      </Jato:attribute>                                 \n" + "    </property>                                         \n" + "  </Jato:translate>                                     \n" + "</Jato:script>";
}
