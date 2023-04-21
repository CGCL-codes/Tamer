package org.geoforge.io.serial;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public interface IGfrSerializer {

    public void loadUnserialized() throws Exception;

    public void releaseUnserialized() throws Exception;
}
