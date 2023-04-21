package com.intel.gpe.client2.common.requests;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import com.intel.gpe.client2.requests.BaseRequest;

/**
 * @version $Id: LoadJobRequest.java,v 1.1 2006/10/25 07:15:33 vashorin Exp $
 * @author Valery Shorin
 */
public class LoadJobRequest extends BaseRequest {

    private String fileName;

    private JAXBContext jaxbContext;

    public LoadJobRequest(String fileName, JAXBContext jaxbContext) {
        super(fileName);
        this.fileName = fileName;
        this.jaxbContext = jaxbContext;
    }

    public Object perform() throws Throwable {
        Unmarshaller m = jaxbContext.createUnmarshaller();
        File inputFile = new File(fileName);
        return m.unmarshal(inputFile);
    }
}
