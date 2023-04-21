package org.springframework.webflow.samples.fileupload;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class FileUploadAction extends AbstractAction {

    protected Event doExecute(RequestContext context) throws Exception {
        MultipartFile file = context.getRequestParameters().getRequiredMultipartFile("file");
        if (file.getSize() > 0) {
            context.getFlashScope().put("file", new String(file.getBytes()));
            return success();
        } else {
            return error();
        }
    }
}
