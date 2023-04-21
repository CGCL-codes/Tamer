package org.semtinel.core.experiments.api;

import java.awt.Image;

/**
 * Factory instances are registered in the {@link Registry} to make
 * a new {@link Visualisation}, {@link Dataprovider} or {@link Analysis}
 * available to Semtinel.
 *
 * @see VisualisationFactory
 * @see DataproviderFactory
 * @see AnalysisFactory
 * 
 * @author Kai Eckert
 */
public interface Factory {

    /**
     * Returns the display name that is shown in the experiment
     * palette and the data panel.
     * @return The display name.
     */
    String getDisplayName();

    /**
     * Returns the icon that is shown in the experiment palette.
     * @param size The size of the icon: 1=16x16px, 2=32x32px
     * @return The icon in the desired size.
     */
    Image getIcon(int size);

    /**
     * A unique id for this factory.
     * @return The id.
     */
    String getId();

    /**
     * The {@link Flavor} of this factory.
     * @see Flavor
     * @return The flavor.
     */
    Flavor getFlavor();

    /**
     * Create a new instance of the {@link Analysis}, {@link Visualisation}
     * or {@link Dataprovider}.
     * @return The new instance.
     */
    Object getInstance();
}
