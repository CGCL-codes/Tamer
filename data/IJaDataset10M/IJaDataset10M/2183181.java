package fr.cnes.sitools.xml;

import org.restlet.data.MediaType;
import fr.cnes.sitools.AbstractEditUserProfileTestCase;
import fr.cnes.sitools.api.DocAPI;

/**
 * Test Editing user profile
 * 
 * @since UserStory : Edition profil utilisateur - Release 4 - Sprint : 3
 * 
 * @author b.fiorito (AKKA Technologies)
 */
public class EditUserProfileTestCase extends AbstractEditUserProfileTestCase {

    static {
        setMediaTest(MediaType.APPLICATION_XML);
        docAPI = new DocAPI(ResetPasswordTestCase.class, "Edit user profile API with XML format");
        docAPI.setActive(true);
        docAPI.setMediaTest(MediaType.APPLICATION_XML);
    }

    /**
   * Default constructor
   */
    public EditUserProfileTestCase() {
        super();
    }
}
