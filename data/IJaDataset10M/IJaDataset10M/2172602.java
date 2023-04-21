package au.com.gworks.jump.app.wiki.server.mockimpl;

import java.security.Principal;
import org.javaongems.server.GemServe;
import org.javaongems.server.rpc.io.BaseHttpPostPickupRpcImpl;
import org.javaongems.server.rpc.io.HttpPostPayload;
import com.google.gwt.user.client.rpc.SerializableException;

public class ExplorerUploadRpcImpl extends BaseHttpPostPickupRpcImpl {

    protected void handlePayload(HttpPostPayload order) throws SerializableException {
        Principal principal = GemServe.getThreadLocalPrincipal();
        String namespace = (String) order.formData.get("namespace");
        ExplorerSvrController ctrlr = new ExplorerSvrController(namespace, principal);
        String path = (String) order.formData.get("path");
        String changeComment = (String) order.formData.get("changeComment");
        ctrlr.uploadFiles(changeComment, namespace, path, order.formFiles);
    }
}
