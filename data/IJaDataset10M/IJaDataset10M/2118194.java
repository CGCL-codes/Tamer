package es.rvp.java.simpletag.gui.configuration;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import es.rvp.java.simpletag.gui.fonts.FontApplication;

/**
 * Configuración de las fuentes de la aplicación.
 *
 * @author Rodrigo Villamil Pérez
 */
public class FontConfig {

    protected static final Logger LOGGER = Logger.getLogger(ApplicationConfigLoader.class);

    private final ArrayList<FontApplication> fonts = new ArrayList<FontApplication>();

    /**
	 * Aniade una fuente a la configuracion
	 */
    public void addFont(final FontApplication font) {
        this.fonts.add(font);
    }

    @Override
    public String toString() {
        return "FontConfig [fonts=" + this.fonts + "]";
    }

    public ArrayList<FontApplication> getFonts() {
        return this.fonts;
    }
}
