package org.jsmpp.bean;

/**
 * @author uudashr
 *
 */
public interface DataCodingFactory {

    /**
     * @param dataCoding
     * @return
     */
    boolean isRecognized(byte dataCoding);

    /**
     * @param dataCoding
     * @return
     */
    DataCoding newInstance(byte dataCoding);
}
