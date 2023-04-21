package com.tysanclan.site.projectewok.imports.tango;

import org.apache.wicket.spring.injection.annot.SpringBean;
import com.tysanclan.site.projectewok.beans.RegulationService;
import com.tysanclan.site.projectewok.entities.Regulation;

/**
 * @author Jeroen Steenbeeke
 */
public class RegulationRecordHandler implements RecordHandler {

    @SpringBean
    private RegulationService regulationService;

    /**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
    @Override
    public void cleanup() {
        regulationService = null;
    }

    /**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
    @Override
    public String getRecordDescriptor() {
        return "RG";
    }

    /**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#handle(java.lang.String[],
	 *      com.tysanclan.site.projectewok.imports.tango.TangoImporterCallback)
	 */
    @Override
    public boolean handle(String[] data, TangoImporterCallback callback) {
        long key = Long.parseLong(data[1]);
        String name = data[2], contents = data[3];
        Regulation regulation = regulationService.importRegulation(name, contents);
        if (regulation != null) {
            callback.getImportedObject(key, Regulation.class);
            return true;
        }
        return false;
    }
}
