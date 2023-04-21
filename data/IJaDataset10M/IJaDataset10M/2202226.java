package gov.nasa.gsfc.visbard.model.threadtask;

import gov.nasa.gsfc.visbard.repository.resource.Resource;
import gov.nasa.gsfc.visbard.repository.resource.ResourceInfo;
import gov.nasa.gsfc.visbard.repository.resource.ResourceWriter;
import gov.nasa.gsfc.visbard.repository.resource.ResourceWriterFactory;
import gov.nasa.gsfc.visbard.util.VisbardException;
import java.io.File;

public class ResourceSaver extends ThreadTask {

    private static final org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(ResourceSaver.class.getName());

    private Resource fRes;

    private File fFile;

    private ResourceInfo fOutputInfo;

    private String fFillVal;

    private boolean fSuccess;

    public ResourceSaver(Resource res, File file, ResourceInfo outputinfo, String fillVal) {
        super("Saving Data");
        fOutputInfo = outputinfo;
        fFile = file;
        fRes = res;
        fFillVal = fillVal;
        fSuccess = false;
    }

    public void execute() {
        try {
            ResourceWriter writer = ResourceWriterFactory.getInstance().createResourceWriter(fOutputInfo);
            writer.writeResource(fFile, fRes, (ProgressReporter) this, fFillVal);
        } catch (VisbardException e) {
            e.showErrorMessage();
            return;
        }
        fSuccess = true;
        return;
    }

    public boolean isSuccess() {
        return fSuccess;
    }
}
