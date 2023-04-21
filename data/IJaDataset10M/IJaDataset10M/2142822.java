package it.paolomind.pwge.mock.xml;

import it.paolomind.pwge.concrete.xml.MapLoader;
import it.paolomind.pwge.interfaces.manager.IResourceManager;
import it.paolomind.pwge.xml.game.Game;
import it.paolomind.pwge.xml.map.MapDocument;
import java.io.IOException;
import java.io.InputStream;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

public class MapLoaderMock extends MapLoader {

    private final MapDocument xmldoc;

    public MapLoaderMock(Game game, final IResourceManager resources, final MapDocument xmldoc) {
        super(game, resources);
        this.xmldoc = xmldoc;
    }

    @Override
    protected void validate(final XmlObject xmldoc) throws XmlException {
        if (!xmldoc.validate()) {
            super.validate(xmldoc);
        }
    }

    @Override
    protected final MapDocument xmlParsing(final InputStream in) throws XmlException, IOException {
        return this.xmldoc;
    }
}
