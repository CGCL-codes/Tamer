package newsatort.file.xml;

import java.io.File;
import java.io.IOException;
import newsatort.Application;
import newsatort.exception.XmlParserException;
import newsatort.log.ILogManager;
import newsatort.log.LogLevel;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public abstract class AbstractXmlParser<TYPE> implements IXmlParser<TYPE> {

    protected final Digester digester;

    protected final TYPE xmlObject;

    public AbstractXmlParser(TYPE xmlObject) {
        digester = new Digester();
        this.xmlObject = xmlObject;
    }

    public TYPE parse(File xmlFile) throws XmlParserException {
        Application.getInstance().getManager(ILogManager.class).addLog(LogLevel.VERBOSE, "Fichier ", xmlFile);
        digester.push(xmlObject);
        addRules();
        try {
            digester.parse(xmlFile);
        } catch (IOException exception) {
            throw new XmlParserException(exception);
        } catch (SAXException exception) {
            throw new XmlParserException(exception);
        }
        Application.getInstance().getManager(ILogManager.class).addLog(LogLevel.VERBOSE, "done", xmlFile);
        return xmlObject;
    }

    protected abstract void addRules();
}
