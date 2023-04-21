package net.sf.lightbound.demos.offline;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import net.sf.lightbound.Request;
import net.sf.lightbound.annotations.ActionMethod;
import net.sf.lightbound.components.forms.Form;
import net.sf.lightbound.components.forms.FormField;
import net.sf.lightbound.components.forms.RadioButton;
import net.sf.lightbound.components.links.WebLink;
import net.sf.lightbound.content.tree.AssociationTree;
import net.sf.lightbound.content.tree.XMLBinding;
import net.sf.lightbound.util.LightBoundUtil;
import net.sf.lightbound.util.XMLUtil;

public class OfflineDemoPage {

    private static final Object FIRST_RADIO_GROUP = new Object();

    private static final Object SECOND_RADIO_GROUP = new Object();

    private String testTextValue = "Initial value";

    private int testRadioValue = 1;

    public AssociationTree getXmlBindingTest() throws DocumentException, IOException {
        InputStream in = OfflineDemoPage.class.getResourceAsStream("/net/sf/lightbound/demos/offline/test.xml");
        if (in == null) {
            throw new FileNotFoundException();
        }
        Document doc;
        try {
            doc = XMLUtil.getDocument(in);
        } finally {
            LightBoundUtil.closeQuietly(in);
        }
        return new XMLBinding(doc.getRootElement());
    }

    public void setTestTextValue(String value) {
        testTextValue = value;
    }

    public String getTestTextValue() {
        return testTextValue;
    }

    public FormField getTestTextField() {
        return new FormField("testTextValueExternalName", null, this);
    }

    public String getTestText() {
        return testTextValue;
    }

    public void setTestText(String value) {
        System.out.println("&& Set value to: " + value);
        testTextValue = value;
    }

    public Form getTestForm() {
        Form form = new Form(this, new WebLink("foo.html"), null);
        form.setAutoRenameFormElements(true);
        return form;
    }

    public RadioButton<Object> getTestRadio() {
        return new RadioButton<Object>(FIRST_RADIO_GROUP);
    }

    public RadioButton<Object> getTestSecondRadio() {
        return new RadioButton<Object>(SECOND_RADIO_GROUP);
    }

    @ActionMethod(id = "testAction")
    public void doSubmit(Request request) {
        System.out.println("Value: " + testTextValue);
    }

    public int getTestRadioValue() {
        return testRadioValue;
    }

    public void setTestRadioValue(int testRadioValue) {
        System.out.println("&& Radio value set to: " + testRadioValue);
        this.testRadioValue = testRadioValue;
    }
}
