package com.rapidminer.gui.new_plotter.templates.style;

import java.awt.Font;
import java.util.Observable;
import javax.swing.JPanel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.rapidminer.gui.new_plotter.templates.style.ColorScheme.ColorRGB;

/**
 * Abstract class which all style providers for the new plotter templates have to extend.
 * 
 * @author Marco Boeck
 *
 */
public abstract class PlotterStyleProvider extends Observable {

    public static final String STYLE_ELEMENT = "style";

    /**
	 * Return the {@link JPanel} where the user can change the color/font settings.
	 * @return
	 */
    public abstract JPanel getStyleProviderPanel();

    /**
	 * Returns the {@link String} which the user chose as the chart title.
	 * @return
	 */
    public abstract String getTitleText();

    /**
	 * Returns the {@link Font} which the user chose for the axes.
	 * @return
	 */
    public abstract Font getAxesFont();

    /**
	 * Returns the {@link Font} which the user chose for the legend.
	 * @return
	 */
    public abstract Font getLegendFont();

    /**
	 * Returns the {@link Font} which the user chose for the title.
	 * @return
	 */
    public abstract Font getTitleFont();

    /**
	 * Returns the {@link ColorRGB} of the background frame of the chart.
	 * @return
	 */
    public abstract ColorRGB getFrameBackgroundColor();

    /**
	 * Returns the {@link ColorRGB} of the plot background.
	 * @return
	 */
    public abstract ColorRGB getPlotBackgroundColor();

    /**
	 * Returns a {@link ColorScheme} instance which will be used to color the plot(s).
	 * Each plot will use one of the colors in the order provided, if more plots than
	 * colors exist, it will start from the beginning.
	 * @return
	 */
    public abstract ColorScheme getColorScheme();

    /**
	 * Creates an {@link Element} which contains all {@link PlotterStyleProvider} settings.
	 * @param document the {@link Document} where the {@link Element} will be written to
	 * @return the styleElement {@link Element}
	 */
    public abstract Element createXML(Document document);

    /**
	 * Loads all {@link PlotterStyleProvider} settings from the given {@link Element}.
	 * @param styleElement the {@link Element} where all settings are loaded from
	 */
    public abstract void loadFromXML(Element styleElement);

    /**
	 * Uses all settings from the given {@link PlotterStyleProvider} to overwrite his own settings.
	 * Does not overwrite the title text.
	 * @param provider
	 */
    public abstract void copySettingsFromPlotterStyleProvider(PlotterStyleProvider provider);
}
