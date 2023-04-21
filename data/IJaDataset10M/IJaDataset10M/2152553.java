package edina.chalice.ws;

import edina.chalice.service.plugins.ChaliceFormat;

/**
 * Abstract command object for CHALICE Web Services
 * 
 * @author Joe Vernon
 * 
 */
public abstract class AbstractChaliceCommand {

    private ChaliceFormat format = ChaliceFormat.RDF;

    /**
	 * Gets the format
	 * 
	 * @return the format
	 */
    public ChaliceFormat getFormat() {
        return format;
    }

    /**
	 * Sets the format
	 * 
	 * @param format the format to set
	 */
    public void setFormat(ChaliceFormat format) {
        this.format = format;
    }
}
