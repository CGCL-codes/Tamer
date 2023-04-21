package net.kodeninja.DMAP.DataTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import net.kodeninja.DMAP.ParameterFactory;
import net.kodeninja.DMAP.ParameterList;

/**
 * Base class for DMAP list tags.
 * @author Charles Ikeson
 */
public abstract class DMAPListParameter extends DMAPParameter implements ParameterList {

    protected LinkedList<DMAPParameter> params = new LinkedList<DMAPParameter>();

    /**
	 * Constructs a list data type parameter with the passed long name.
	 * @param name The long name of the parameter.
	 */
    public DMAPListParameter(String name) {
        super(DMAPParameter.DATATYPE_LIST, name);
    }

    public void addParameter(DMAPParameter param) {
        params.add(param);
    }

    public void removeParameter(DMAPParameter param) {
        params.remove(param);
    }

    public void clear() {
        params.clear();
    }

    public Iterator<DMAPParameter> getParameters() {
        return params.iterator();
    }

    public DMAPParameter findParamByTag(String tag, boolean recurse) {
        Iterator<DMAPParameter> it = getParameters();
        while (it.hasNext()) {
            DMAPParameter tmpParam = it.next();
            if (tmpParam.getTag().equals(tag)) return tmpParam; else if ((recurse == true) && (tmpParam instanceof ParameterList)) if ((tmpParam = ((ParameterList) tmpParam).findParamByTag(tag, true)) != null) return tmpParam;
        }
        return null;
    }

    @Override
    public int dataLength() {
        Iterator<DMAPParameter> it = getParameters();
        int totalSize = 0;
        while (it.hasNext()) totalSize += it.next().length();
        return totalSize;
    }

    @Override
    public void readDataFromStream(InputStream in, int Length) throws IOException {
        ParameterFactory.readFromStream(this, in, Length);
    }

    @Override
    protected void writeDataToBuffer(OutputStream out) throws IOException {
        Iterator<DMAPParameter> it = getParameters();
        while (it.hasNext()) it.next().writeToStream(out);
    }

    @Override
    public String toString() {
        String retVal = "";
        Iterator<DMAPParameter> it = getParameters();
        while (it.hasNext()) if (retVal.length() == 0) retVal += it.next(); else retVal += ", " + it.next();
        return "{" + retVal + "}";
    }
}
