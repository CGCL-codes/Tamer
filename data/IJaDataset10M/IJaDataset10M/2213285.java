package guitarscales.options.models;

import guitarscales.options.xmlhandlers.OptionsXMLReader;
import guitarscales.options.xmlhandlers.OptionsXMLWriter;
import org.apache.log4j.Category;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Keeps track of the application options. Since these settings should be available everywhere
 * and only one set of settings should exist at all times, this is a Singleton
 */
public class OptionsModel {

    static Category cat = Category.getInstance(OptionsModel.class.getName());

    private static OptionsModel _instance;

    private static List _optionsModelListeners = new ArrayList();

    private int _dotSize = 5;

    private int _dotType = OptionsModelTypes.DOTS_ONLY;

    private String _saveDir;

    /**
	 *
	 */
    private OptionsModel() {
    }

    /**
	 * The classes that want to read the application settings should always call this
	 * method to obtain the current version of the settings.
	 */
    public static synchronized OptionsModel getInstance() {
        if (_instance == null) {
            _instance = new OptionsModel();
        }
        return _instance;
    }

    /**
	 * applys the given model to the current one
	 * @param model the new model that will become the current one for the whole application
	 */
    public static synchronized void setInstance(OptionsModel model) {
        _instance = (OptionsModel) model.clone();
        notifyListeners(new OptionsEvent(new Object()));
    }

    /**
	 * Loads the options from disk
	 */
    public void loadOptions() throws ConfigurationNotFoundException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(true);
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            OptionsXMLReader optionsXMLHandler = new OptionsXMLReader();
            xmlReader.setContentHandler(optionsXMLHandler);
            xmlReader.setErrorHandler(optionsXMLHandler);
            File optionsXMLFile = new File("options.xml");
            cat.debug("looking for " + optionsXMLFile.getAbsolutePath());
            xmlReader.parse(new InputSource(new FileReader(optionsXMLFile)));
            OptionsModel.setInstance(optionsXMLHandler.getOptionsModel());
        } catch (FactoryConfigurationError factoryConfigurationError) {
            cat.error(factoryConfigurationError);
            throw new ConfigurationNotFoundException();
        } catch (ParserConfigurationException e) {
            cat.error(e);
            throw new ConfigurationNotFoundException();
        } catch (SAXException e) {
            cat.error(e);
            throw new ConfigurationNotFoundException();
        } catch (IOException e) {
            cat.error(e);
            throw new ConfigurationNotFoundException();
        }
    }

    /**
	 * Saves the options to disk
	 */
    public void saveOptions() {
        File optionsFile = new File("options.xml");
        OptionsXMLWriter writer = new OptionsXMLWriter(optionsFile, this);
        writer.write();
    }

    public int getDotSize() {
        return _dotSize;
    }

    public void setDotSize(int dotSize) {
        _dotSize = dotSize;
    }

    public int getDotType() {
        return _dotType;
    }

    public void setDotType(int dotType) {
        _dotType = dotType;
    }

    public String getSaveDir() {
        return _saveDir;
    }

    public void setSaveDir(String dir) {
        _saveDir = dir;
    }

    public Object clone() {
        OptionsModel model = new OptionsModel();
        model._dotSize = _dotSize;
        model._dotType = _dotType;
        model._saveDir = _saveDir;
        return model;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof OptionsModel) {
            OptionsModel optionsModel = (OptionsModel) obj;
            if (optionsModel._dotSize != _dotSize) return false;
            if (optionsModel._dotType != _dotType) return false;
            if (!optionsModel._saveDir.equals(_saveDir)) return false;
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(super.toString());
        buffer.append("\ndot size: " + _dotSize);
        buffer.append("\ndot type: " + _dotType);
        buffer.append("\nsave dir: " + _saveDir);
        return buffer.toString();
    }

    /**
	 * Register yourself as a listener for the OptionsModel
	 * @param listener
	 */
    public static void addOptionsModelListener(OptionsModelListener listener) {
        if (!_optionsModelListeners.contains(listener)) {
            _optionsModelListeners.add(listener);
        }
    }

    /**
	 * Remove yourself as a listener for the OptionsModel
	 * @param listener
	 */
    public static void removeOptionsModelListener(OptionsModelListener listener) {
        _optionsModelListeners.remove(listener);
    }

    /**
	 * Notifies the listeners that something has changed
	 * @param event
	 */
    private static void notifyListeners(OptionsEvent event) {
        for (Iterator iterator = _optionsModelListeners.iterator(); iterator.hasNext(); ) {
            OptionsModelListener optionsModelListener = (OptionsModelListener) iterator.next();
            optionsModelListener.optionsChanged(event);
        }
    }
}
