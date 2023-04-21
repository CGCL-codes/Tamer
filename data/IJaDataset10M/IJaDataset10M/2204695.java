package com.clican.pluto.common.resource;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The Resource interface
 * @author wei.zhang
 *
 */
public interface Resource {

    /**
	 * 返回资源的二进制输入流
	 * 
	 * @return
	 */
    public InputStream getInputStream() throws FileNotFoundException;

    /**
	 * 输出资源的二进制输出源
	 * @return
	 * @throws FileNotFoundException
	 */
    public OutputStream getOutputStream() throws FileNotFoundException;
}
