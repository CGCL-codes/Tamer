package com.rif.common.serializer;

import com.rif.common.naming.INamingObject;

/**
 * 序列化接口.<br>
 * @author bruce.liu (mailto:jxta.liu@gmail.com)
 *
 */
public interface IDataSerializer extends INamingObject {

    String getName();

    int getIndex();

    /**
	 *  序列化操作
	 * @param obj
	 * @return
	 */
    byte[] marshall(Object obj);

    /**
	 *  反序列化操作
	 * @param bytes
	 * @return
	 */
    Object unmarshall(byte[] bytes);
}
