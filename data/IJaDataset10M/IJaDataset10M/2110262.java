package net.sourceforge.mazix.persistence.dto.profiles;

import static net.sourceforge.mazix.components.constants.CharacterConstants.QUESTION_MARK_CHAR;
import static net.sourceforge.mazix.components.constants.CommonConstants.BLANK_STRING;
import net.sourceforge.mazix.components.DeepCloneableObjectTest;
import org.junit.experimental.theories.DataPoint;

/**
 * JUnit test classes for {@link ProfileDTO}.
 * 
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * 
 * @since 0.8
 * @version 0.8
 */
public class ProfileDTOTest extends DeepCloneableObjectTest<ProfileDTO> {

    /** Profile test 1 instance. */
    @DataPoint
    public static final ProfileDTO PROFILE1 = new ProfileDTO(QUESTION_MARK_CHAR);

    /** Profile test 2 instance. */
    @DataPoint
    public static final ProfileDTO PROFILE2 = new ProfileDTO(BLANK_STRING);

    /** Profile test 3 instance. */
    @DataPoint
    public static final ProfileDTO PROFILE3 = null;
}
