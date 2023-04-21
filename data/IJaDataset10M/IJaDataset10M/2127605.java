package org.xmlhammer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import org.apache.log4j.Logger;
import org.bounce.util.URIUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xmlhammer.DefaultErrorHandler.Error;
import org.xmlhammer.DefaultErrorHandler.Fatal;
import org.xmlhammer.DefaultErrorHandler.Problem;
import org.xmlhammer.DefaultErrorHandler.Warning;
import org.xmlhammer.gui.status.StatusModel;
import org.xmlhammer.gui.status.ValidationStatusModel;
import org.xmlhammer.model.preferences.Preferences;
import org.xmlhammer.model.project.Project;

/**
 * Put comment...
 * 
 * @version $Revision: 1.17 $, $Date: 2007/07/04 19:42:48 $
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class XMLValidatorModule extends Module {

    private SAXParser parser = null;

    private DocumentBuilder builder = null;

    public XMLValidatorModule(Preferences preferences, Project project, Logger logger, boolean logSettings) throws SAXException, ParserConfigurationException {
        super(preferences, project, logger, logSettings);
        if ("dom".equals(getProject().getParser().getType())) {
            DocumentBuilderFactory factory = getDocumentBuilderFactory();
            builder = factory.newDocumentBuilder();
        } else {
            parser = getSAXParser();
        }
    }

    /**
     * Sets-up an XML Validator Module.
     * 
     * @param preferences the global preferences.
     * @param project the project specific properties;
     * @throws ParserConfigurationException 
     * @throws SAXException 
     */
    public XMLValidatorModule(Preferences preferences, Project project, Logger logger) throws SAXException, ParserConfigurationException {
        this(preferences, project, logger, true);
    }

    /**
     * Execute the XPath expressio.
     * @throws MalformedURLException 
     * @throws ParserConfigurationException 
     */
    @Override
    public void execute(StatusModel status, ResultModel result, URI uri) {
        if (uri != null) {
            getLogger().info("validate: " + URIUtils.toString(uri));
            DefaultErrorHandler errorHandler = new DefaultErrorHandler(uri);
            InputSource in = new InputSource(uri.toString());
            try {
                if ("dom".equals(getProject().getParser().getType())) {
                    builder.setErrorHandler(errorHandler);
                    builder.setEntityResolver(getCatalogResolver());
                    builder.parse(in);
                } else {
                    parser.getXMLReader().setErrorHandler(errorHandler);
                    parser.getXMLReader().setEntityResolver(getCatalogResolver());
                    parser.getXMLReader().parse(in);
                }
            } catch (SAXException e) {
            } catch (IOException e) {
                errorHandler.fatalError(e);
            }
            ArrayList<Problem> list = errorHandler.getProblems();
            if (list.size() > 0) {
                if (status instanceof ValidationStatusModel) {
                    ((ValidationStatusModel) status).setValid(false);
                }
                for (Problem problem : list) {
                    if (problem instanceof Warning) {
                        logWarning(uri, (SAXParseException) problem.getException());
                        if (result != null) {
                            result.addWarning(uri, (SAXParseException) problem.getException());
                        }
                    } else if (problem instanceof Error) {
                        logError(uri, (SAXParseException) problem.getException());
                        if (result != null) {
                            result.addError(uri, (SAXParseException) problem.getException());
                        }
                    } else if (problem instanceof Fatal) {
                        logFatal(uri, problem.getException());
                        if (result != null) {
                            Exception exception = problem.getException();
                            if (exception instanceof IOException) {
                                result.addFatal(uri, (IOException) problem.getException());
                            } else {
                                result.addFatal(uri, (SAXParseException) problem.getException());
                            }
                        }
                    }
                }
            } else {
                getLogger().info("\t[valid] Valid Document");
                if (result != null) {
                    result.addValid(uri);
                }
            }
        }
    }
}
