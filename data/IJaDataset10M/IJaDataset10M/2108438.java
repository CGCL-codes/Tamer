package pl.ehotelik.portal.web.ui.hotel;

import pl.ehotelik.portal.web.ui.DataUI;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mkr
 * Date: Aug 20, 2010
 * Time: 2:23:14 AM
 * This is a representation of CountryUI object.
 */
public class CountryUI extends DataUI implements Serializable {

    public CountryUI() {
        super();
    }

    public CountryUI(String id, String name) {
        super(id, name);
    }
}
