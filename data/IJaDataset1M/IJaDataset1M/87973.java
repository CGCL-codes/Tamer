package clubmixerdatabase.importer;

import clubmixer.library.persistence.entities.SongInfo;

/**
 *
 * @author sly
 */
public class ImportScheduler {

    private final ImportWebserviceData wsImport;

    public ImportScheduler() {
        wsImport = new ImportWebserviceData();
        wsImport.start();
    }

    public void importData(SongInfo sinfo) {
        wsImport.addImport(sinfo);
    }
}
