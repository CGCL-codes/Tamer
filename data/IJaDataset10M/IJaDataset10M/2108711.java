package mapshared.carte.route;

import java.io.Serializable;
import mapshared.BasicDTO;

/**
 *
 * @author Cody Stoutenburg
 */
public class RouteDTO extends BasicDTO implements Serializable {

    private String _type;

    public RouteDTO() {
        this._type = "aucun";
    }

    public RouteDTO(final String type) {
        this._type = type;
    }

    public String getType() {
        return _type;
    }

    public void setType(final String type) {
        this._type = type;
    }
}
