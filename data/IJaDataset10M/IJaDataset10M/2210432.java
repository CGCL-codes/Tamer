package org.gvsig.gpe.gml.warnings;

import org.gvsig.exceptions.BaseException;
import org.gvsig.exceptions.ListBaseException;
import org.gvsig.gpe.gml.exceptions.GMLExceptionList;

/************************************************************************
 * Class < GMLFileParseInfo >											*
 * 																		*
 * This Class gets all the warnings from the GML Parser, the GML parser	*
 * always tries parse the GML file even though it isn't a standard but	*
 * it has to info the user. 											*
 * 																		*
 * @author Carlos S�nchez Peri��n (sanchez_carper@gva.es)				*
 ************************************************************************/
public class GMLWarningInfo {

    private ListBaseException warning;

    public GMLWarningInfo() {
        warning = new GMLExceptionList();
    }

    /**
	 * This method return true if there aren't warnings else returns false 
	 * @return boolean
	 */
    public boolean areWarnings() {
        if (warning.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
	 * This method return the list of warnings parsing the GML 
	 * @return ArrayList of warnings
	 */
    public ListBaseException getGMLWarningList() {
        return warning;
    }

    /**
	 * This method inserts a new warning into the list 
	 * @args int new warning
	 */
    public void setElement(BaseException war) {
        if (warning.contains(war) == false) {
            warning.add((war));
        }
    }

    /**
	 * This method returns a warning from the list 
	 * @args int index
	 * @return int warning
	 */
    public BaseException getElement(int index, ListBaseException war) {
        if (war.isEmpty() == false) {
            if (index < war.size()) {
                return (BaseException) war.get(index);
            }
        }
        return null;
    }

    /**
	 * This method inserts a new warning list 
	 * @args int new warning
	 */
    public void setGMLWarningList(ListBaseException war) {
        int i = 0;
        BaseException info;
        while (i < war.size()) {
            info = this.getElement(i, war);
            if (info != null) {
                this.setElement(info);
            }
            i++;
        }
    }
}
