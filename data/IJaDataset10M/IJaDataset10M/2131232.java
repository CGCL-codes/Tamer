package hu.sztaki.lpds.storage.service.carmen.server.receiver.plugins;

import com.oreilly.servlet.MultipartRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface class of the storage receiver service.
 * 
 * @author lpds
 */
public interface ReceiverService {

    /**
 * Receiving data
 * @param multipartRequest multipart request
 * @param response response
 * @throws java.lang.Exception processing error
 */
    public abstract void receive(MultipartRequest multipartRequest, HttpServletResponse response) throws Exception;
}
