package taylorAgile;

import net.taylor.selenium.dsl.RenderTestCase;
import org.junit.Test;

/**
 * This test will render all the facelets.
 * 
 * System property: net.taylor.selenium.url.root, must be initialized.
 * 
 * @author jgilbert
 * @generated
 */
public class FaceletRenderTest extends RenderTestCase {

    /** @generated */
    @Test
    public void testBacklog() {
        doTest("Backlog");
    }

    /** @generated */
    @Test
    public void testBacklogType() {
        doTest("BacklogType");
    }

    /** @generated */
    @Test
    public void testBurn() {
        doTest("Burn");
    }

    /** @generated */
    @Test
    public void testCard() {
        doTest("Card");
    }

    /** @generated */
    @Test
    public void testCardType() {
        doTest("CardType");
    }
}
