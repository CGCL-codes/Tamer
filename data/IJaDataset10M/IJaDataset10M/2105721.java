package it.unibo.deis.infrastructure;

import java.io.InputStream;
import java.net.URL;

/**
 */
public interface ITransportSender {

    public void outAnswer(String msg) throws Exception;

    public InputStream outRequest(URL url, String msg) throws Exception;
}
