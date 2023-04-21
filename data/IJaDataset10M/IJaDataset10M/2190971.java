package verinec.data.repository;

import verinec.VerinecException;

/** Test case for the IVerinecRepository interface. 
  * 
  * @author david.buchmann at unifr.ch
  * @version $Revision: 675 $
  */
public class FileRepositoryTest extends RepositoryTest {

    /** @see FileRepositoryTest
     * @throws VerinecException if the SAXBuilder can't be instanciated
     */
    public FileRepositoryTest() throws VerinecException {
        super();
        repName = "testrepository";
    }

    /** Create a new empty repository.
     * 
	 * @throws VerinecException if creating a FileRepository fails.
	 */
    protected void createNewRepository() throws VerinecException {
        new FileRepository(repName).drop();
        openRepository();
    }

    /** Open a repository. If it is existing, that will be opened.
     * @throws VerinecException
     */
    protected void openRepository() throws VerinecException {
        rep = new FileRepository(repName);
    }
}
