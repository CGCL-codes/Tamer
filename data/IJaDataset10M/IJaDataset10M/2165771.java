package br.ita.autowidget.widgetbuilder;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.security.InvalidParameterException;
import org.junit.Before;
import org.junit.Test;
import br.ita.autowidget.ComponentBuilder;
import br.ita.autowidget.widgetbuilder.HtmlWidgetBuilder;

public class HtmlWidgetBuilderTest {

    private HtmlWidgetBuilder wb;

    @Before
    public void setup() {
        wb = new HtmlWidgetBuilder();
    }

    @Test
    public void componentMetadataNotInitialized() {
        try {
            wb.buildEditorWidget(null);
            fail("InvalidParameterException expected when trying to build an editor for a null component metadata");
        } catch (Exception e) {
            assertTrue("InvalidParameterException expected when trying to build an editor for a null component metadata", e instanceof InvalidParameterException);
        }
        try {
            wb.buildDisplayWidget(null);
            fail("InvalidParameterException expected when trying to build an display for a null component metadata");
        } catch (Exception e) {
            assertTrue("InvalidParameterException expected when trying to build an display for a null component metadata", e instanceof InvalidParameterException);
        }
    }

    @Test
    public void componentMetadataInitialized() {
        try {
            ComponentBuilder<String> cb = new ComponentBuilder<String>(wb);
            cb.buildComponentMetadata(new MockBean());
            wb.buildEditorWidget(cb.getBeanMetadata());
            wb.buildDisplayWidget(cb.getBeanMetadata());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
}
