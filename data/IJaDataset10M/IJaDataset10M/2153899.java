package net.sf.bib2db5.ant;

import java.io.File;
import java.util.Collection;
import net.sf.bib2db5.AbstractConverter;
import net.sf.bib2db5.DB5Converter;
import org.apache.tools.ant.BuildException;

/**
 * The bib2db5 ant task implementation
 * 
 * @author Michiel Hendriks
 */
public class Bib2DB5Task extends AbstractConverterTask {

    /**
     * Do not include doctype
     */
    protected boolean noDTD;

    /**
     * Do not include xmlns
     */
    protected boolean noXMLNS;

    /**
     * Do not include revision info
     */
    protected boolean noRev;

    public Bib2DB5Task() {
        super();
    }

    /**
     * @param value
     *                the noDTD to set
     */
    public void setNoDTD(boolean value) {
        noDTD = value;
    }

    /**
     * @param value
     *                the noXMLNS to set
     */
    public void setNoXMLNS(boolean value) {
        noXMLNS = value;
    }

    /**
     * @param value
     *                the noRev to set
     */
    public void setNoRev(boolean value) {
        noRev = value;
    }

    /**
     * Get the converter instance (for easier subclassing).
     * 
     * @param files
     * @return
     * @throws BuildException
     */
    @Override
    protected AbstractConverter getConverter(Collection<File> files) throws BuildException {
        DB5Converter conv = new DB5Converter(files, dest, mergeoutput);
        conv.setNoDTD(noDTD);
        conv.setNoXMLNS(noXMLNS);
        conv.setNoRevInfo(noRev);
        return conv;
    }
}
