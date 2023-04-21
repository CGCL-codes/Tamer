package net.sf.breed.orbiter.ui;

import net.sf.breed.orbiter.model.OrbitParameter;
import net.sf.breed.util.joglib.JoglibPanel;

/**
 * Influences all parts of the UI programmatically.
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since 4 Mar 2009
 */
public interface IOrbitController {

    /**
     * @param paramPanel The orbital parameter panel.
     */
    public void setParamPanel(IOrbitParameterPanel paramPanel);

    /**
     * @param orbitPanel The OpenGL orbit panel.
     */
    public void setOrbitPanel(JoglibPanel orbitPanel);

    /**
     * Put the focus on the OpenGL window.
     */
    public void focusOpenGL();

    /**
     * Gets the values of all text fields from the model.
     */
    public void getAllOrbitParameters();

    /**
     * Puts all text field values into the model.
     */
    public void putAllOrbitParameters();

    /**
     * Backs up the current orbit.
     */
    public void saveCustomOrbit();

    /**
     * Restores the last save orbit.
     */
    public void restoreCustomOrbit();

    /**
     * @param param The orbit parameter to animate for.
     */
    public void setOrbitAnimation(OrbitParameter param);

    /**
     * Sets a standard orbit: LEO.
     */
    public void setStandardOrbitLEO();

    /**
     * Sets a standard orbit: GEO.
     */
    public void setStandardOrbitGEO();

    /**
     * Sets a standard orbit: SSO (taken from Envisat).
     */
    public void setStandardOrbitSSO();

    /**
     * Unconditionally shuts down the application.
     */
    public void shutdown();
}
