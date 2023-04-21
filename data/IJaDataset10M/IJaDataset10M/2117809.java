package org.identifylife.key.store.workflow.upload.taxonomy;

/**
 * @author dbarnier
 *
 */
@SuppressWarnings("serial")
public class HierarchyExistsException extends Exception {

    public HierarchyExistsException(String uuid) {
        super("hierarchy exists: " + uuid);
    }
}
