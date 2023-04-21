package cdox.data;

import cdox.CDox;
import java.awt.*;

/**
 *<p>
 * This is a DataSource that can be used to read a directory in several ways and generate
 * GenericData from it.
 *</p>
 * @author <a href="mailto:cdox@gmx.net">Rutger Bezema, Andreas Schmitz</a>
 * @version June 22nd 2002
 */
public class FileSource implements DataSource {

    private Data data = null;

    public String getDescription() {
        return CDox.getLocalizer().get("filesystem");
    }

    public Class getDataType() {
        return GenericData.class;
    }

    public boolean hasConfigurationDialog() {
        return true;
    }

    public void showConfigurationDialog(Frame parent) {
        data = new FileSourceDialog(parent).showAndGetData();
    }

    public Data retrieveData() {
        return data;
    }
}
