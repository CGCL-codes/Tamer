package uk.ac.lkl.common.util.restlet.client;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import uk.ac.lkl.common.util.collections.WeakObjectWrapper;
import uk.ac.lkl.common.util.collections.WeakObjectWrapper2;
import uk.ac.lkl.common.util.restlet.EntityMapper;
import uk.ac.lkl.common.util.restlet.server.EntityId;

class MultiEntityMapper extends EntityMapper {

    private Map<WeakObjectWrapper, Integer> entityMap;

    private ReferenceQueue<Object> referenceQueue;

    private Map<WeakReference<Object>, WeakObjectWrapper> referenceMap;

    private Map<WeakObjectWrapper2, Integer> lookupMap;

    public MultiEntityMapper() {
        lookupMap = new HashMap<WeakObjectWrapper2, Integer>();
        entityMap = new HashMap<WeakObjectWrapper, Integer>();
        referenceQueue = new ReferenceQueue<Object>();
        referenceMap = new HashMap<WeakReference<Object>, WeakObjectWrapper>();
        startThread();
    }

    private void startThread() {
        Runnable task = new Runnable() {

            public void run() {
                Reference<? extends Object> reference;
                while (true) {
                    try {
                        reference = referenceQueue.remove();
                        WeakObjectWrapper wrapper = referenceMap.remove(reference);
                        Object result = entityMap.remove(wrapper);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public synchronized void addMapping(Object object, int id) {
        WeakObjectWrapper2 wrappedObject2 = new WeakObjectWrapper2(object, null);
        lookupMap.put(wrappedObject2, id);
        WeakObjectWrapper wrappedObject = new WeakObjectWrapper(object, referenceQueue);
        entityMap.put(wrappedObject, id);
        WeakReference<Object> weakReference = wrappedObject.getWeakReference();
        referenceMap.put(weakReference, wrappedObject);
    }

    public Integer getId(Object object) {
        return lookupMap.get(new WeakObjectWrapper2(object, null));
    }

    public <O> EntityId<O> getEntityId(O object) {
        Integer id = getId(object);
        if (id == null) return null;
        return new EntityId<O>(entityMap.get(object));
    }

    public static void main(String[] args) {
        MultiEntityMapper mapper = new MultiEntityMapper();
        for (int i = 1; i <= 1000; i++) {
            Integer object1 = new Integer(i + 3);
            Integer object2 = new Integer(i + 3);
            mapper.addMapping(object1, i);
            mapper.addMapping(object2, i);
            Integer object1Id = mapper.getId(object1);
            System.out.println("object1Id: " + object1Id);
        }
        System.gc();
        while (true) {
            try {
                System.out.println("Sleeping");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }
    }
}
