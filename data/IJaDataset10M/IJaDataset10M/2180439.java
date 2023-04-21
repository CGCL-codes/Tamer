package com.taobao.remote.common.core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * ������������
 * 
 * TS
 * 
 * һ�����������Ҫ2��parser(request parser, response parser)
 * 
 * @author zjf2308
 * @author lin.wangl
 * 
 */
public interface Parser<E> {

    /**
	 * д�����
	 * 
	 * @param out
	 * @throws IOException
	 */
    void write(E value, DataOutput out) throws IOException;

    /**
	 * �������
	 * 
	 * @param in
	 * @throws IOException
	 */
    E read(DataInput in) throws IOException;
}
