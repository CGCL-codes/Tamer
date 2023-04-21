package org.jrest4guice.sample.contact.resource;

import org.jrest4guice.commons.fileupload.UploadInfo;
import org.jrest4guice.commons.fileupload.UploadMonitor;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;
import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Path("/monitor")
public class FileuploadMonitor {

    @Inject
    private UploadMonitor monitor;

    @Get
    public UploadInfo getUploadInfo() {
        return this.monitor.getUploadInfo();
    }
}
