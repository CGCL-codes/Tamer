package uk.icat3.io.beans;

import javax.jws.WebService;
import uk.icat3.io.entity.ExtractedDataBean;

@WebService(name = "ICATIOWs", targetNamespace = "io.icat3.uk")
public interface ICATIOWs {

    public ExtractedDataBean downloadData(String query);

    void uploadData(String sessionId, ExtractedDataBean data) throws Exception;
}
