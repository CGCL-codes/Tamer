package foo.bar.client.service;

import foo.bar.shared.model.Slim3Model;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service.s3gwt")
public interface GwtSlim3Service extends RemoteService {

    void newAndPut(String prop1);

    Slim3Model[] queryAll();
}
