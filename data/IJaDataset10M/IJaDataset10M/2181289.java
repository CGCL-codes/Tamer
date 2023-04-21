package com.hyk.serializer.impl;

import java.io.IOException;
import java.io.NotSerializableException;
import java.lang.reflect.Array;
import com.hyk.io.buffer.ChannelDataBuffer;
import com.hyk.serializer.io.Type;
import com.hyk.serializer.reflect.ReflectionCache;
import com.hyk.serializer.util.ContextUtil;

/**
 * @author qiying.wang
 * 
 */
public class ArraySerializerStream<T> extends SerailizerStream<T> {

    @Override
    protected T unmarshal(Class<T> type, ChannelDataBuffer data) throws NotSerializableException, IOException {
        try {
            Class componentTypeClass = type.getComponentType();
            if (componentTypeClass.equals(byte.class)) {
                byte[] retValue = readBytes(data);
                return (T) retValue;
            } else {
                int len = readInt(data);
                int index = 0;
                Object array = Array.newInstance(componentTypeClass, len);
                ContextUtil.addDeserializeThreadLocalObject(array);
                Type componentType = ReflectionCache.getType(componentTypeClass);
                while (index < len) {
                    Object element = readObject(data, componentTypeClass);
                    Array.set(array, index, element);
                    index++;
                }
                return (T) array;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    @Override
    protected ChannelDataBuffer marshal(Object value, ChannelDataBuffer data) throws NotSerializableException, IOException {
        int len = Array.getLength(value);
        writeInt(data, len);
        Class componentType = value.getClass().getComponentType();
        if (componentType.equals(byte.class)) {
            byte[] rawValue = (byte[]) value;
            writeBytes(data, rawValue);
        } else {
            int index = 0;
            while (index < len) {
                Object element = Array.get(value, index);
                writeObject(data, element, componentType);
                index++;
            }
        }
        return data;
    }
}
