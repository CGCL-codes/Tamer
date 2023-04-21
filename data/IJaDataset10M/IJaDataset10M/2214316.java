package net.sf.joafip.meminspector;

import java.lang.ref.ReferenceQueue;
import net.sf.joafip.meminspector.entity.NodeForObject;
import net.sf.joafip.meminspector.entity.WeakObjectIdentityKey;
import net.sf.joafip.store.service.objectfortest.Bob1;

public class MaintTestAddChild {

    private final ReferenceQueue<? super Object> referenceQueue = new ReferenceQueue<Object>();

    public static void main(final String[] args) {
        final MaintTestAddChild maintTestAddChild = new MaintTestAddChild();
        maintTestAddChild.run();
    }

    private MaintTestAddChild() {
        super();
    }

    private void run() {
        WeakObjectIdentityKey weakObject = new WeakObjectIdentityKey(this, referenceQueue);
        final NodeForObject nodeForObject = new NodeForObject(null, weakObject);
        final Bob1 bob1 = new Bob1();
        weakObject = new WeakObjectIdentityKey(bob1, referenceQueue);
        NodeForObject childNode = new NodeForObject(nodeForObject, weakObject);
        System.out.println(nodeForObject.addChild(childNode));
        weakObject = new WeakObjectIdentityKey(bob1, referenceQueue);
        childNode = new NodeForObject(nodeForObject, weakObject);
        System.out.println(nodeForObject.addChild(childNode));
        childNode = new NodeForObject(nodeForObject, weakObject);
        System.out.println(nodeForObject.addChild(childNode));
        System.out.println(nodeForObject.addChild(childNode));
    }
}
