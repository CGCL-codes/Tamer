package org.simpleframework.http.transport;

import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The <code>PacketManager</code> object is used to create buffers
 * used to buffer output. Buffers are created lazily so that they
 * are allocated only on demand. Typically buffers are only created
 * when small chunks of data are written to the transport and the
 * socket is blocking. This ensures that writing can continue 
 * without waiting for the data to be fully drained. 
 * 
 * @author Niall Gallagher
 */
class PacketManager implements Recycler {

    /**
    * This is the queue that is used to recycle the buffers.
    */
    private Queue queue;

    /**
    * Determines how many buffers can be lazily created.
    */
    private int allow;

    /**
    * Determines the size of the buffers that are created.
    */
    private int size;

    /**
    * Constructor for the <code>PacketManager</code> object. This
    * requires the size of the buffers that will be allocated and
    * the number of buffers that can be lazily created before it
    * will block waiting for the next buffer to be returned.
    */
    public PacketManager() {
        this(4096, 3);
    }

    /**
    * Constructor for the <code>PacketManager</code> object. This
    * requires the size of the buffers that will be allocated and
    * the number of buffers that can be lazily created before it
    * will block waiting for the next buffer to be returned.
    * 
    * @param size this is the size of the buffers to be allocated
    * @param allow this is the number of buffers to be created
    */
    public PacketManager(int size, int allow) {
        this.queue = new Queue();
        this.allow = allow;
        this.size = size;
    }

    /** 
    * This checks to see if there is a buffer ready within the queue. 
    * If there is one ready then this returns it, if not then this
    * checks how many buffers have been created. If we can create one
    * then return a newly instantiated buffer, otherwise block and 
    * wait for one to be recycled.
    * 
    * @return this returns the next ready buffer within the manager
    */
    public ByteBuffer allocate() throws Exception {
        ByteBuffer next = queue.poll();
        if (next != null) {
            return next;
        }
        return create();
    }

    /** 
    * This checks to see if there is a buffer ready within the queue. 
    * If there is one ready then this returns it, if not then this
    * checks how many buffers have been created. If we can create one
    * then return a newly instantiated buffer, otherwise block and 
    * wait for one to be enqueued.
    * 
    * @return this returns the next ready buffer within the queue
    */
    private ByteBuffer create() throws Exception {
        if (allow-- < 0) {
            return queue.take();
        }
        return build();
    }

    /**
    * This method is used to recycle the buffer. Invoking this with
    * a buffer instance will pass the buffer back in to the pool.
    * Once passed back in to the pool the buffer should no longer
    * be used as it may affect future uses of the buffer.
    *
    * @param buffer this is the buffer that is to be recycled
    */
    public void recycle(ByteBuffer buffer) {
        buffer.clear();
        queue.offer(buffer);
    }

    /**
    * This is used to allocate a buffer if there is no buffer ready
    * within the queue. The size of the buffer is determined from
    * the size specified when the buffer queue is created.
    * 
    * @return this returns a newly allocated byte buffer
    */
    private ByteBuffer build() {
        return ByteBuffer.allocateDirect(size);
    }

    /**
    * The <code>Queue</code> is used to transfer recycled buffers
    * back in to the main memory store. When recycled they can be
    * allocated again and used to transfer data to the socket.
    * 
    * @author Niall Gallagher
    */
    private class Queue extends LinkedBlockingQueue<ByteBuffer> {

        /**
       * Constructor for the <code>Queue</code> object. This is
       * used to create a queue that will transfer buffers back
       * in to the main memory store so they can be reused.
       */
        public Queue() {
            super();
        }
    }
}
