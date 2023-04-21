package simple.page.translate;

/**
 * The <code>Reference</code> object is used to determine whether 
 * any of the files used to compose the JSP have changed. This will
 * allow background compilation of the sources. Once the files 
 * have expired the JSP sources will be translated and compiled
 * with the most up to date version.
 *  
 * @author Niall Gallagher
 */
public interface Reference {

    /**
    * This checks if the source or its includes have expired. This 
    * will check the JSP and its includes to determine if the 
    * source has changed in any way. For background compilation 
    * this method can be used to refresh the pages to ensure that
    * the most up to date code is active.
    *
    * @return this returns true if any of the JSP files changed
    */
    public boolean isModified();
}
