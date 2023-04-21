package es.usc.citius.servando.android.medim.Drivers.Connectivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import es.usc.citius.servando.android.medim.exceptions.ConnectionException;

/**
 * 
 * @author tarasco
 */
public interface IConnectivity {

    /**
	 * 
	 * @return
	 */
    public boolean isAvaliable();

    /**
	 * 
	 * @return
	 */
    public boolean findDevice(boolean forceRediscover);

    /**
	 * 
	 * @throws ConnectionException
	 */
    public void connect() throws ConnectionException;

    /**
	 * 
	 * @return
	 */
    public void disconnect() throws IOException;

    /**
	 * 
	 * @return
	 * @throws IOException
	 */
    public InputStream getReader() throws IOException;

    /**
	 * 
	 * @return
	 * @throws IOException
	 */
    public OutputStream getWriter() throws IOException;

    /**
	 * 
	 * @return
	 */
    public String getDescription();
}
