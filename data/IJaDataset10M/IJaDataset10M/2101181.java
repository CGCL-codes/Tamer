package jsbsim.simgear.props;

/**
This class is a pointer proxy doing reference counting on the object
it is pointing to.
The SGSharedPtr class handles reference counting and possible
destruction if no nore references are in use automatically.
Classes derived from @SGReferenced can be handled with SGSharedPtr.
Once you have a SGSharedPtr available you can use it just like
a usual pointer with the exception that you don't need to delete it.
Such a reference is initialized by zero if not initialized with a
class pointer.
One thing you need to avoid are cyclic loops with such pointers.
As long as such a cyclic loop exists the reference count never drops
to zero and consequently the objects will never be destroyed.
Always try to use directed graphs where the references away from the
top node are made with SGSharedPtr's and the back references are done with
ordinary pointers.
There is a very good description of OpenSceneGraphs ref_ptr which is
pretty much the same than this one at
http://dburns.dhs.org/OSG/Articles/RefPointers/RefPointers.html
 */
public class SGSharedPtr<T> {

    private T _ptr;

    public SGSharedPtr() {
        _ptr = null;
    }

    public SGSharedPtr(T ptr) {
        _ptr = ptr;
        get(_ptr);
    }

    public SGSharedPtr(final SGSharedPtr<T> p) {
        _ptr = p.ptr();
        get(_ptr);
    }

    public void DestroySGSharedPtr() {
        put();
    }

    public T ptr() {
        return _ptr;
    }

    public boolean isShared() {
        return SGReferenced.shared((SGReferenced) _ptr);
    }

    public int getNumRefs() {
        return SGReferenced.count((SGReferenced) _ptr);
    }

    public boolean valid() {
        return null != _ptr;
    }

    public void assign(T p) {
        get(p);
        put();
        _ptr = p;
    }

    private void get(final T p) {
        SGReferenced.get((SGReferenced) p);
    }

    private void put() {
        if (0 == SGReferenced.put((SGReferenced) _ptr)) {
            _ptr = null;
        }
    }
}
