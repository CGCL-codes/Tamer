package archiveManager.handler.zip;

import archiveManager.handler.ArchiveHandler;

public class IsoZipHandler extends ArchiveHandler {

    public void doProcess() throws Exception {
        zip();
    }

    private void zip() {
        System.out.println("hi, zip to iso");
    }
}
