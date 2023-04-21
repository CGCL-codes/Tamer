package mapshared.carte.terrain;

import mapshared.IOID;
import mapshared.Mapper;
import org.jdom.Element;

/**
 *
 * @author Cody Stoutenburg
 */
public class TerrainMapper extends Mapper<TerrainDTO> {

    public static String COLUMN_ID = "id";

    public static String COLUMN_TYPE = "type";

    public TerrainMapper() {
        this.TABLE_NAME = "terrain";
    }

    @Override
    public TerrainDTO getDTO(Element el) {
        TerrainDTO dto = new TerrainDTO();
        dto.setId(IOID.valueOf(el.getAttributeValue(COLUMN_ID)));
        dto.setType(el.getAttributeValue(COLUMN_TYPE));
        return dto;
    }
}
