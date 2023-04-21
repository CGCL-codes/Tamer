package net.tourbook.device.garmin.fit.listeners;

import net.tourbook.device.garmin.fit.DataConverters;
import net.tourbook.device.garmin.fit.FitActivityContext;
import com.garmin.fit.FileCreatorMesg;
import com.garmin.fit.FileCreatorMesgListener;

public class FileCreatorMesgListenerImpl extends AbstractMesgListener implements FileCreatorMesgListener {

    public FileCreatorMesgListenerImpl(FitActivityContext context) {
        super(context);
    }

    @Override
    public void onMesg(FileCreatorMesg mesg) {
        Integer softwareVersion = mesg.getSoftwareVersion();
        if (softwareVersion != null) {
            context.setSoftwareVersion(DataConverters.convertSoftwareVersion(softwareVersion));
        }
    }
}
