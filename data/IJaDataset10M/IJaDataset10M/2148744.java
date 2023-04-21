package jsesh.mdc.model;

import jsesh.mdc.model.operations.ModelOperation;

/**
 * Interface for object which contain model elements and listen to their modifications.
 * @author rosmord
 *
 */
public interface ModelElementObserver {

    void observedElementChanged(ModelOperation operation);
}
