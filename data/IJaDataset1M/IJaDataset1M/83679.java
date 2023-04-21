package mapshared.carte.route;

import mapshared.IOID;
import mapshared.Mapper;
import org.jdom.Element;

/**
 *
 * @author Cody Stoutenburg
 */
public class RouteMapper extends Mapper<RouteDTO> {

    public static String COLUMN_ID = "id";

    public static String COLUMN_TYPE = "type";

    public RouteMapper() {
        this.TABLE_NAME = "route";
    }

    @Override
    public RouteDTO getDTO(Element el) {
        RouteDTO dto = new RouteDTO();
        dto.setId(IOID.valueOf(el.getAttributeValue(COLUMN_ID)));
        dto.setType(el.getAttributeValue(COLUMN_TYPE));
        return dto;
    }
}
