package net.sf.dynxform.form;

import junit.framework.TestCase;
import net.sf.dynxform.container.TestModuleProvider;
import net.sf.dynxform.form.model.FormDefinition;
import net.sf.dynxform.form.schema.Form;
import net.sf.dynxform.form.schema.Styling;

/**
 * @version $Revision: 1.3 $ $Date: 2004/08/11 17:32:58 $
 */
public class FieldStytlingTest extends TestCase {

    FormDefinition definition;

    DefaultFormManager defaultFormManager;

    TestModuleProvider moduleFacade = TestModuleProvider.getInstance();

    private static final String FORM_ID = "FieldStytlingTest";

    public FieldStytlingTest(final String name) {
        super(name);
    }

    public void testInitBuildSelectionList() throws Exception {
        defaultFormManager = new DefaultFormManager(moduleFacade);
        final Form form = defaultFormManager.loadForm(FORM_ID);
        assertEquals(FORM_ID, form.getGuid());
        final Styling styling = form.getField(0).getStyling();
        assertEquals("enabling flag", styling.getEnable().getContent());
        assertEquals("visibility flag", styling.getVisible().getContent());
    }
}
