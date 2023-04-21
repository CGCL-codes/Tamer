package org.xsocket.stream.io.grizzly;

import java.nio.ByteBuffer;

/**
 * memory manager 
 * 
 * @author grro
 */
interface IMemoryManager {

    /**
	 * acquire ByteBuffer with free memory
	 * 
	 * @return the ByteBuffer with free memory 
	 */
    public ByteBuffer acquireMemory(int minSize);

    /**
	 * recycle a ByteBuffer (if remaining larger than min size) 
	 * 
	 * @param buffer  the ByteBuffer to recycle 
	 * @param minSize the min preallocated size
	 */
    public void recycleMemory(ByteBuffer buffer, int minSize);

    /**
	 * preallocate, if preallocated size is smaller the given minSize
	 * 
	 * @param minSize the min preallocated size
	 */
    public void preallocate(int minSize);

    /**
	 * get the current free preallocated buffer size 
	 * @return the current free preallocated buffe size
	 */
    public int getFreeBufferSize();
}
