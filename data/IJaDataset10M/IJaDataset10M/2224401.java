package org.objectwiz.plugin.uibuilder;

/**
 * Exception indicating that there is a problem in the metadata of
 * the UIBuilder that prevents a module from performing its task.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class UIBuilderMetadataException extends UIBuilderRuntimeException {

    public UIBuilderMetadataException(String message) {
        super(message);
    }
}
