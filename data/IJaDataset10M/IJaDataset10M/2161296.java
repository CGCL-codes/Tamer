package finderXMLObject;

import java.io.File;
import java.net.URL;
import javax.accessibility.AccessibleRole;
import org.jdom.Document;
import org.jdom.Element;
import services.W3CSerivcesJDOM;
import finder.SearchingSettingsModel;
import finder.SearchingStrategyPlugin;
import finder.exception.DuplicatePluginException;
import finder.exception.NoSuchPluginException;
import finderXMLObject.Exception.MultipleObjectFoundException;
import finderXMLObject.Exception.ObjectNotFoundException;

public abstract class Finder {

    protected finder.Finder finder;

    public Finder() {
        try {
            finder = new finder.Finder();
        } catch (DuplicatePluginException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDocument(Document doc) {
        finder.setDocument(doc);
    }

    public Document getDocument() {
        return finder.getDocument();
    }

    public void setPlugInConfiguration(Document pluginConfiguration) throws DuplicatePluginException {
        SearchingSettingsModel searchingSettingsModel = new SearchingSettingsModel(pluginConfiguration);
        SearchingStrategyPlugin strategy = new SearchingStrategyPlugin(searchingSettingsModel);
        finder.addSearchingStrategyPlugin(strategy);
    }

    public void updateSearchingStrategyPlugin(Document pluginConfiguration) throws DuplicatePluginException, NoSuchPluginException {
        SearchingSettingsModel searchingSettingsModel = new SearchingSettingsModel(pluginConfiguration);
        SearchingStrategyPlugin strategy = new SearchingStrategyPlugin(searchingSettingsModel);
        finder.updateSearchingStrategyPlugin(strategy);
    }

    public abstract Element find(String name) throws MultipleObjectFoundException, ObjectNotFoundException;

    public abstract Element find(String name, AccessibleRole type) throws MultipleObjectFoundException, ObjectNotFoundException;

    public abstract Element find(String name, AccessibleRole type, String nameParent) throws MultipleObjectFoundException, ObjectNotFoundException;

    public abstract Element find(String name, AccessibleRole type, String nameParent, AccessibleRole typeParent) throws MultipleObjectFoundException, ObjectNotFoundException;
}
