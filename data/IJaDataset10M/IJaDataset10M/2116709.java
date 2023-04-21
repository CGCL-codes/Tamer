package org.sulweb.infustore;

import org.sulweb.infumon.common.Converter;
import org.sulweb.infumon.common.Sample;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public interface SampleHolder {

    Sample getSample();

    Stateful getStatusHolder();

    void setSampleData(byte[] data);

    Converter getConverter();
}
