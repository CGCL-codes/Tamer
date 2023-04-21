package org.kalypso.nofdpidss.core.common.utils.modules;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.NotImplementedException;

/**
 * @author Dirk Kuch
 */
public interface IStyleReplacements {

    public enum TYPES {

        eLanduseCorine, eCities, eCatchments, eConflictArea, eInnundationArea, eRiverNetwork, ePhysicalRiverQuality, eNull, eVegetationStructure, eSoilNutrient, eSoilMoisture, eSalinity;

        public static TYPES getType(final String templateName) {
            if ("template_catchments.sld".equals(templateName)) return eCatchments; else if ("template_cities.sld".equals(templateName)) return eCities; else if ("template_conflictareas.sld".equals(templateName)) return eConflictArea; else if ("template_inundationarea.sld".equals(templateName)) return eInnundationArea; else if ("template_landuse_corine.sld".equals(templateName)) return eLanduseCorine; else if ("template_rivernetwork.sld".equals(templateName)) return eRiverNetwork; else if ("template_struka_nofdp_def.sld".equals(templateName)) return ePhysicalRiverQuality; else if (templateName.contains("template_struka_nofdp_mea")) return ePhysicalRiverQuality; else if ("template_rivers.sld".equals(templateName)) return ePhysicalRiverQuality; else if ("template_vegetation_structure.sld".equals(templateName)) return eVegetationStructure; else if ("template_soilnutrients.sld".equals(templateName)) return eSoilNutrient; else if ("template_soilhumidity.sld".equals(templateName)) return eSoilMoisture; else if ("template_salinity.sld".equals(templateName)) return eSalinity;
            throw new NotImplementedException();
        }
    }

    public File getSld();

    public boolean replace() throws IOException;
}
