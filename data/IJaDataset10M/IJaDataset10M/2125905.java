package net.wimpi.text;

import java.util.*;

/**
 * Class that implements a pool for <code>ProcessingResource</code>
 * instances.
 * <br>
 * The factory method is used for creating pools of the
 * given resource and size, using the resource's factory method
 * to create the requested amount of instances.
 *
 * @author Dieter Wimberger
 * @version 0.2 19/11/2002
 *
 * @see net.wimpi.text.ProcessingResource#createResource()
 */
public class ResourcePool {

    private List m_Pool;

    private boolean m_Serving = false;

    private ProcessingResource m_Resource;

    private int m_Size;

    /**
	 * Private, and empty to disallow any form of construction
	 * via new.
	 */
    private ResourcePool() {
    }

    /**
	 * Constructs a <code>ResourcePool</code> instance.
	 */
    private ResourcePool(int size, ProcessingResource resource) {
        m_Size = size;
        m_Resource = resource;
    }

    /**
     * Returns a leased <code>ProcessingResource</code> instance from this
     * pool.
     * 
	 * @return the leased <code>ProcessingResource</code> from this pool. 
	 */
    public synchronized ProcessingResource leaseResource() {
        if (!m_Serving) {
            return null;
        }
        boolean served = false;
        ProcessingResource freeResource = null;
        while (!served) {
            if (m_Pool.size() == 0) {
                try {
                    wait();
                } catch (Exception ex) {
                }
            } else {
                freeResource = (ProcessingResource) m_Pool.get(0);
                m_Pool.remove(0);
                served = true;
            }
        }
        return freeResource;
    }

    /**
	 * Releases a formerly leased <code>ProcessingResource</code>, returning 
	 * it into this pool.
	 * 
	 * @param resource the formerly leased <code>ProcessingResource</code>
	 *        to be released. 
	 */
    public synchronized void releaseResource(ProcessingResource resource) {
        m_Pool.add(resource);
        notifyAll();
    }

    /**
	 * Returns the ceiling (in terms of size) of this pool.
	 * 
	 * @return the ceiling size of this pool as <code>int</code>.
	 */
    public int getCeiling() {
        return m_Size;
    }

    /** 
	 * Returns the size of this pool.
	 * 
	 * @return the actual size of this pool as <code>int</code>.
	 */
    public int size() {
        return m_Pool.size();
    }

    /**
	 * Resizes the pool.
	 * 
	 * @param size the new size as <code>int</code>.
	 */
    public void resize(int size) {
        synchronized (m_Pool) {
            clear();
            m_Size = size;
            initPool();
        }
    }

    /**
	 * Removes all <code>ProcessingResource</code> references from
	 * this pool.
	 * Note that it will do so gracefully, waiting for
	 * leased <code>ProcessingResources</code>. 	 
	 */
    public void clear() {
        m_Serving = false;
        while (m_Pool.size() != m_Size) {
            try {
                wait();
            } catch (Exception ex) {
            }
        }
        m_Pool.clear();
    }

    /**
	 * Initializes a pool of <code>ProcessingResource</code> instances.
	 */
    protected void initPool() {
        m_Pool = new ArrayList(m_Size);
        for (int i = 0; i < m_Size; i++) {
            m_Pool.add(m_Resource.createResource());
        }
        m_Serving = true;
    }

    /**
	 * Creates a ResourcePool with the given size, containing the given
	 * <code>ProcessingResource</code> instances.
	 * 
	 * @param size the size as <code>int</code>.
	 * @param resource the <code>ProcessingResource</code> to be pooled.
	 */
    public static ResourcePool createResourcePool(int size, ProcessingResource resource) {
        ResourcePool pool = new ResourcePool(size, resource);
        pool.initPool();
        return pool;
    }
}
