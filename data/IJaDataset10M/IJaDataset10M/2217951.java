package org.pprun.hjpetstore.persistence.jaxb;

import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.http.HttpStatus;

/**
 * POJO represents an exception response from REST service.
 *
 * <b>Please note that, in order to let the jaxb2 automatically bind work, the xml root element and its elements
 * can not be {@literal static} class</b>.
 * @author <a href="mailto:quest.run@gmail.com">pprun</a>
 */
@XmlRootElement(name = "CommonError")
public class CommonError {

    private String path;

    private HttpStatus status;

    private String message;

    private String Exception;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getException() {
        return Exception;
    }

    public void setException(String Exception) {
        this.Exception = Exception;
    }
}
