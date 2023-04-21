package com.bluemarsh.graphmaker.core.model;

import java.io.IOException;

/**
 * A ModelAccess provides the means for reading a Model from persistent
 * storage, and subsequently writing a modified Model back to storage.
 *
 * @author Nathan Fiedler
 */
public interface ModelAccess {

    /**
     * Returns the model source assigned to this model access instance.
     *
     * @return  model source.
     */
    ModelSource getSource();

    /**
     * Deserializes the model from the assigned model source, including
     * its vertices, edges, and properties.
     *
     * @return  the model restored from the model source.
     * @throws  IOException
     *          if there was a problem reading the data.
     */
    Model read() throws IOException;

    /**
     * Assign a model source to this access instance.
     *
     * @param  source  the model source.
     */
    void setSource(ModelSource source);

    /**
     * Writes the model elements to the storage device represented by
     * the assigned model source.
     *
     * @param  model  from which elements are gathered.
     * @throws  IOException
     *          if there was a problem writing the data.
     */
    void write(Model model) throws IOException;
}
