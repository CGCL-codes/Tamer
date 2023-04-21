package net.sf.stump.eclipse.editor;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import com.laughingpanda.mocked.MockFactory;

public abstract class ResourceChangeEventStub implements IResourceChangeEvent {

    final IResourceDelta delta;

    int type = IResourceChangeEvent.POST_CHANGE;

    public ResourceChangeEventStub(IResourceDelta delta) {
        this.delta = delta;
    }

    public IResourceDelta getDelta() {
        return delta;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static ResourceChangeEventStub event(IResourceDelta delta) {
        return (ResourceChangeEventStub) MockFactory.makeMock(ResourceChangeEventStub.class, new Object[] { delta });
    }
}
