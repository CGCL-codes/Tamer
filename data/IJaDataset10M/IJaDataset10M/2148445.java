package eu.pouvesle.nicolas.dpp.code;

import java.awt.List;
import java.util.StringTokenizer;

/**
 * Provide a parent class for the different format implementation.
 *
 * @author Nicolas Pouvesle
 * @version 1.00
 * @since dpp 1.04.00
 *
 */
public class MSCode {

    String params;

    /**
	 *	Default constructor.
	 *
	 * @param params Line of parameters.
	 */
    public MSCode(String line) {
        setParams(line);
    }

    /**
	 *	Get Params.
	 *
	 * @return Line of parameters.
	 */
    public String getParams() {
        return params;
    }

    /**
	 *	Set Params.
	 *
	 * @param line Line of parameters.
	 */
    public void setParams(String line) {
        params = line;
    }
}
