package org.jfge.games.mk2.fighter;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.jfge.api.fighter.Fighter;
import org.jfge.api.fighter.FighterParser;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * The Class JohnnyCage.
 */
public final class JohnnyCage implements Provider<Fighter> {

    /** The kano. */
    private Fighter johnnyCage;

    /** The fighter factory. */
    private FighterParser fighterFactory;

    /**
	 * Instantiates a new liu kang.
	 *
	 * @param fighterFactory the fighter factory
	 */
    @Inject
    public JohnnyCage(FighterParser fighterFactory) {
        this.fighterFactory = fighterFactory;
    }

    @Override
    public Fighter get() {
        if (johnnyCage == null) {
            try {
                johnnyCage = fighterFactory.parseFromXmlFile("/org/jfge/games/mk2/fighter/johnnycage/johnnycage.xml");
            } catch (DOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
        return johnnyCage;
    }
}
