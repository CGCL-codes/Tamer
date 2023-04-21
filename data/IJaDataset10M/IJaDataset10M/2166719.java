package org.subrecord.kit.fluent;

import java.io.Serializable;
import org.subrecord.kit.SubRecordApiException;

/**
 * @author przemek
 * 
 */
public interface ICache extends IAspects {

    public ICache put(String domain, String key, Serializable value) throws SubRecordApiException;

    public ICache get(String domain, String key, Wrapper result) throws SubRecordApiException;
}
