package at.gp.web.jsf.extval.label.interceptor;

import java.io.IOException;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import org.apache.myfaces.extensions.validator.PropertyValidationModuleStartupListener;
import at.gp.web.jsf.extval.label.AbstractTest;
import at.gp.web.jsf.extval.label.DefaultRequiredLabelAddonConfiguration;
import at.gp.web.jsf.extval.label.RequiredLabelAddonConfiguration;
import at.gp.web.jsf.extval.label.initializer.RequiredLabelInitializer;
import at.gp.web.jsf.extval.label.interceptor.model.DataBean;
import at.gp.web.jsf.extval.label.startup.LabelAwareInitializationStartupListener;

public class PropertyValidationAwareLabelRendererInterceptorTest extends AbstractTest {

    private HtmlOutputLabel label;

    private HtmlInputText input;

    public PropertyValidationAwareLabelRendererInterceptorTest(String name) {
        super(name);
    }

    @Override
    protected void invokeStartupListeners() {
        new PropertyValidationModuleStartupListener() {

            private static final long serialVersionUID = -3274169061804065986L;

            @Override
            protected void init() {
                super.initModuleConfig();
                super.init();
            }
        }.init();
        new LabelAwareInitializationStartupListener() {

            private static final long serialVersionUID = 7271421984062276284L;

            @Override
            protected void init() {
                super.initModuleConfig();
                super.init();
            }
        }.init();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        HtmlForm form = new HtmlForm();
        form.setId("form");
        facesContext.getViewRoot().getChildren().add(form);
        label = new HtmlOutputLabel();
        label.setId("label");
        label.setFor("field");
        form.getChildren().add(label);
        input = new HtmlInputText();
        input.setId("field");
        form.getChildren().add(input);
    }

    public void testWithDefaults() throws IOException {
        label.setValue("label :");
        DataBean bean = new DataBean();
        facesContext.getExternalContext().getRequestMap().put("dataBean", bean);
        createValueExpression(input, "value", "#{dataBean.property}");
        label.encodeAll(facesContext);
        verifyOutput(getPageContents(), "* label :", "form:label", "ExtValRequiredLabel");
    }

    public void testCustomImplementation() throws IOException {
        RequiredLabelAddonConfiguration.use(new DefaultRequiredLabelAddonConfiguration() {

            @Override
            public RequiredLabelInitializer getRequiredLabelInitializer() {
                return new CustomRequiredLabelInitializer();
            }
        }, true);
        label.setValue("label :");
        DataBean bean = new DataBean();
        facesContext.getExternalContext().getRequestMap().put("dataBean", bean);
        createValueExpression(input, "value", "#{dataBean.property}");
        label.encodeAll(facesContext);
        assertTrue(getPageContents().indexOf(">required</label>") > -1);
    }
}
