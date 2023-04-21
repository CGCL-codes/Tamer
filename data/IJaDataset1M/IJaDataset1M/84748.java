package test;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import sagex.SageAPI;
import sagex.remote.rmi.RMISageAPI;
import sagex.stub.StubSageAPI;
import sagex.stub.WidgetAPIProxy;
import sagex.util.LogProvider;
import sagex.widgets.WidgetException;
import sagex.widgets.WidgetImporter;

public class TestWidgets {

    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, WidgetException {
        LogProvider.useSystemOut();
        StubSageAPI api = new StubSageAPI();
        WidgetAPIProxy wapi = api.getWidgetAPIProxy();
        wapi.newWidget("");
        SageAPI.setProvider(new StubSageAPI());
        WidgetImporter importer = new WidgetImporter();
        importer.importWidgets("001d098ac46c", "resources/test/layouts/Sample.xml");
    }
}
