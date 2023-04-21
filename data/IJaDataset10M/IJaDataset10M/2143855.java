package eteg.sinon.core;

/**
 * Class that represents the configuration of a page in Sinon.
 *
 * @author <a href="mailto:thiagohp at users.sourceforge.net">Thiago H. de Paula Figueiredo</a>
 * @author Last modified by $Author: thiagohp $
 * @version $Revision: 1.2 $
 */
public class PageConfiguration {

    /**
     * HTTP parameters set.
     */
    private ParameterSet parameterSet;

    /**
     * Constructor with no parameters.
     */
    public PageConfiguration() {
        parameterSet = new ParameterSet();
    }

    /**
     * Returns the value of the <code>parameterSet</code> property.
     * @return a <code>String</code>.
     */
    public ParameterSet getParameterSet() {
        return parameterSet;
    }

    /**
     * Sets the value of the <code>parameterSet</code> property.
     * @param parameterSet the new <code>parameterSet</code> value.
     */
    public void setParameterSet(ParameterSet parameterSet) {
        this.parameterSet = parameterSet;
    }
}
