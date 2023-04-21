package org.nakedobjects.remoting.shared.encoding.object.data;

import org.nakedobjects.metamodel.adapter.version.Version;

public interface ClientActionResultData {

    /**
     * Return the ObjectDatas for the persisted objects (in the same seqence as passed to the server). This is
     * used to update the client's copies with the new OIDs and Versions
     */
    ReferenceData[] getPersisted();

    /**
     * Return the Versions for the objects that were saved by the server for the action. These are used to
     * update the versions of the client's copies so they align with the servers copies.
     */
    Version[] getChanged();

    /**
     * Return the set of ObjectData for any objects that where changed by the server while executing the
     * action.
     */
    ObjectData[] getUpdates();
}
