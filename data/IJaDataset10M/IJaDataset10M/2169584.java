package form;

import org.makagiga.commons.MURLButton;
import org.makagiga.form.Factory;
import org.makagiga.form.Form;
import org.makagiga.form.Link;
import org.makagiga.test.Test;

public final class TestLink {

    @Test
    public void test_linkName() {
        Factory.Content content = TestFactory.newPanel(new FormLinkName());
        MURLButton link = content.getElementByName("test");
        assert link.getName().equals("test");
    }

    @Test
    public void test_linkText() {
        Factory.Content content = TestFactory.newPanel(new FormLinkText());
        MURLButton link;
        link = content.getElementByName("linkWithDefaultText");
        assert link.getText() == null;
        assert link.getURL() == null;
        content.setLabel("linkWithDefaultText", "Bar");
        assert link.getText().equals("Bar");
        assert link.getURL() == null;
        content.setLabel("linkWithDefaultText", null);
        assert link.getText() == null;
        assert link.getURL() == null;
        link = content.getElementByName("linkWithText");
        assert link.getText().equals("Foo");
        assert link.getURL() == null;
    }

    @Test
    public void test_linkURL() {
        Factory.Content content = TestFactory.newPanel(new FormLinkURL());
        MURLButton link;
        link = content.getElementByName("nullURL");
        assert link.getText() == null;
        assert link.getURL() == null;
        link = content.getElementByName("emptyURL");
        assert link.getText().equals("");
        assert link.getURL().equals("");
        link = content.getElementByName("normalURL");
        assert link.getText().equals("http://example.com");
        assert link.getURL().equals("http://example.com");
        link = content.getElementByName("normalURLWithLabel");
        assert link.getText().equals("Example");
        assert link.getURL().equals("http://example.com");
    }

    @Test
    public void test_panelContent() {
        Factory.Content content;
        content = TestFactory.newPanel(new FormWithOneLink());
        assert content.getPanel().getComponentCount() == 1;
        assert content.getPanel().getComponent(0).getName().equals("link1");
        assert content.getElementByName("link1") instanceof MURLButton;
        content = TestFactory.newPanel(new FormWithTwoLinks());
        assert content.getPanel().getComponentCount() == 3;
        assert content.getPanel().getComponent(0).getName().equals("link1");
        assert content.getPanel().getComponent(1) instanceof javax.swing.Box.Filler;
        content.getPanel().getComponent(2).getName().equals("link2");
        assert content.getElementByName("link1") instanceof MURLButton;
        assert content.getElementByName("link2") instanceof MURLButton;
    }

    @Test
    public void test_unsupportedFieldType() {
        try {
            TestFactory.newPanel(new FormUnsupportedFieldType());
            assert false : "IllegalArgumentException expected";
        } catch (IllegalArgumentException exception) {
        }
    }

    @Form
    public static final class FormLinkName {

        @Link
        private String test;
    }

    @Form
    public static final class FormLinkText {

        @Link
        private String linkWithDefaultText;

        @Link(label = "Foo")
        private String linkWithText;
    }

    @Form
    public static final class FormLinkURL {

        @Link
        private String nullURL;

        @Link
        private String emptyURL = "";

        @Link
        private String normalURL = "http://example.com";

        @Link(label = "Example")
        private String normalURLWithLabel = "http://example.com";
    }

    @Form
    public static final class FormUnsupportedFieldType {

        @Link
        private int link;
    }

    @Form
    public static final class FormWithOneLink {

        @Link
        private String link1;
    }

    @Form
    public static final class FormWithTwoLinks {

        @Link
        private String link1;

        @Link
        private String link2;
    }
}
