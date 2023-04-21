package org.nakedobjects.object;

import org.nakedobjects.NakedObjectsComponent;
import org.nakedobjects.object.reflect.ActionPeer;
import org.nakedobjects.object.reflect.OneToManyPeer;
import org.nakedobjects.object.reflect.OneToOnePeer;

public interface ReflectionPeerFactory extends NakedObjectsComponent {

    ActionPeer createAction(ActionPeer peer);

    OneToManyPeer createField(OneToManyPeer peer);

    OneToOnePeer createField(OneToOnePeer peer);
}
