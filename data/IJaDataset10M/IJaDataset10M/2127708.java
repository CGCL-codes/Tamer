package org.objectwiz.fxclient.rendering;

import org.objectwiz.core.ui.rendering.DatasetRendererConfiguration;

/**
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public interface FXRendererGateway {

    public void reset();

    public void refresh();

    public void refresh(Object object);

    public void setConfiguration(DatasetRendererConfiguration condig);
}
