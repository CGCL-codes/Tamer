package gov.nist.atlas.io;

import java.net.URL;

/**
 * Created: Apr 1, 2003 3:07:21 PM
 * @author <a href='mailto:nradde@users.sf.net'>Nicolas Radde</a>
 * @version $Revision: 1.5 $
 * @since 2.0 Beta 6
 */
public abstract class SingleURLImportedElementLoader extends ImportedElementLoader {

    /**
   * FIX-ME: use Resource instead and move error handling somewhere else...
   * <p>Gets the root ImportedElement for the Corpus defined by the resource this
   * ImportedElementLoader is associated to.</p>
   *
   * <p>Note that this method is not supposed to be called by client code directly.
   * It is used by CorporaManager and assumes that the proper checks to see if the
   * corpus associated with the specified resource has not alredy been loaded.</p>
   *
   * @param source the resource from where the ImportedElement is supposed to be
   *               loaded
   * @return the root ImportedElement of the hierarchy associated with the corpus
   *         to be loaded
   * @throws ATLASIOException if the specified cannot be resolved to anything
   *         useful to this ImportedElementLoader.
   */
    public ImportedElement getImportedCorpus(Object source) throws ATLASIOException {
        URL from = URIManager.getAsURL(source);
        if (from == null) throw new ATLASIOException("Couldn't resolve (" + source + ") as a URL.");
        return getImportedCorpusFromURL(from);
    }

    /**
   * It is assumed that the Corpus associated with the specified URL has NOT
   * already been loaded (proper checks are supposedly made by CorporaManager
   * before calling this method). Implementers should NOT, therefore, check
   * CorporaManager first.
   *
   * @param url the URL from which the Corpus is supposed to be imported
   * @return the root ImportedElement of the hierarchy associated with the corpus
   *         to be loaded
   */
    protected abstract ImportedElement getImportedCorpusFromURL(URL url);
}
