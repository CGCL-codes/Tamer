package commons;

import java.awt.Cursor;
import java.awt.Image;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.swing.Icon;
import org.makagiga.commons.BooleanProperty;
import org.makagiga.commons.MComponent;
import org.makagiga.commons.MIcon;
import org.makagiga.commons.MLabel;
import org.makagiga.commons.MPanel;
import org.makagiga.commons.MTextField;
import org.makagiga.commons.UI;
import org.makagiga.test.Test;
import org.makagiga.test.TestConstructor;
import org.makagiga.test.TestMethod;
import org.makagiga.test.Tester;
import org.makagiga.test.UITest;
import commons.style.TestStyle;

@Test(className = MLabel.class, flags = Test.NO_DEFAULT_EQUALS_WARNING, methods = { @TestMethod(name = "createSmall"), @TestMethod(name = "html", parameters = "String"), @TestMethod(name = "setHTML", parameters = "String, Object[]"), @TestMethod(name = "setText", parameters = "String, Object[]") })
public final class TestMLabel extends UITest {

    @Test
    public void test_example() {
        MLabel label = new MLabel("Hello");
        label.setIconName("ui/ok");
    }

    @Test(constructors = { @TestConstructor, @TestConstructor(parameters = "Icon"), @TestConstructor(parameters = "Image"), @TestConstructor(parameters = "String"), @TestConstructor(parameters = "String, Icon"), @TestConstructor(parameters = "String, String") })
    public void test_constructor() {
        Image image = MIcon.getImage("ui/ok");
        assert image != null;
        MIcon icon = MIcon.stock("ui/ok");
        assert icon != null;
        MLabel l;
        l = new MLabel();
        assert l.getIcon() == null;
        assert l.getText() == null;
        testConstructor(l);
        l = new MLabel((Icon) null);
        assert l.getIcon() == null;
        assert l.getText() == null;
        testConstructor(l);
        l = new MLabel(icon);
        assert l.getIcon() == icon;
        assert l.getText() == null;
        testConstructor(l);
        l = new MLabel((Image) null);
        assert l.getIcon() == null;
        assert l.getText() == null;
        testConstructor(l);
        l = new MLabel(image);
        assert MIcon.class.cast(l.getIcon()).getImage() == image;
        assert l.getText() == null;
        testConstructor(l);
        l = new MLabel((String) null);
        assert l.getIcon() == null;
        assert l.getText() == null;
        testConstructor(l);
        l = new MLabel("");
        assert l.getIcon() == null;
        assert l.getText().isEmpty();
        testConstructor(l);
        l = new MLabel("Foo");
        assert l.getIcon() == null;
        assert l.getText().equals("Foo");
        testConstructor(l);
        l = new MLabel(null, (Icon) null);
        assert l.getIcon() == null;
        assert l.getText() == null;
        testConstructor(l);
        l = new MLabel("", (Icon) null);
        assert l.getIcon() == null;
        assert l.getText().isEmpty();
        testConstructor(l);
        l = new MLabel("Foo", (Icon) null);
        assert l.getIcon() == null;
        assert l.getText().equals("Foo");
        testConstructor(l);
        l = new MLabel(null, icon);
        assert l.getIcon() == icon;
        assert l.getText() == null;
        testConstructor(l);
        l = new MLabel("", icon);
        assert l.getIcon() == icon;
        assert l.getText().isEmpty();
        testConstructor(l);
        l = new MLabel("Foo", icon);
        assert l.getIcon() == icon;
        assert l.getText().equals("Foo");
        testConstructor(l);
        l = new MLabel(null, (String) null);
        assert l.getIcon() == null;
        assert l.getText() == null;
        testConstructor(l);
        l = new MLabel("", (String) null);
        assert l.getIcon() == null;
        assert l.getText().isEmpty();
        testConstructor(l);
        l = new MLabel("Foo", (String) null);
        assert l.getIcon() == null;
        assert l.getText().equals("Foo");
        testConstructor(l);
        l = new MLabel(null, "ui/ok");
        assert l.getIcon() != null;
        assert l.getIcon().getIconWidth() == UI.iconSize.get();
        assert l.getIcon().getIconHeight() == UI.iconSize.get();
        assert l.getText() == null;
        testConstructor(l);
        l = new MLabel("", "ui/ok");
        assert l.getIcon() != null;
        assert l.getIcon().getIconWidth() == UI.iconSize.get();
        assert l.getIcon().getIconHeight() == UI.iconSize.get();
        assert l.getText().isEmpty();
        testConstructor(l);
        l = new MLabel("Foo", "ui/ok");
        assert l.getIcon() != null;
        assert l.getIcon().getIconWidth() == UI.iconSize.get();
        assert l.getIcon().getIconHeight() == UI.iconSize.get();
        assert l.getText().equals("Foo");
        testConstructor(l);
    }

    @Test(methods = { @TestMethod(name = "createFor", parameters = "Component, String"), @TestMethod(name = "createFor", parameters = "Component, String, int") })
    public void test_createFor() {
        MComponent buddy = new MComponent();
        MTextField nameTextField = new MTextField();
        MLabel label = MLabel.createFor(nameTextField, "Name:");
        assert label.getLabelFor() == nameTextField;
        MLabel l1;
        MLabel l2;
        l1 = MLabel.createFor(null, null);
        assert l1.getHorizontalAlignment() == MLabel.LEADING;
        assert l1.getLabelFor() == null;
        assert l1.getText() == null;
        testConstructor(l1);
        l2 = MLabel.createFor(buddy, "Foo");
        assert l2.getHorizontalAlignment() == MLabel.LEADING;
        assert l2.getLabelFor() == buddy;
        assert l2.getText().equals("Foo");
        testConstructor(l2);
        assert l1 != l2;
        l1 = MLabel.createFor(null, null, MLabel.TRAILING);
        assert l1.getHorizontalAlignment() == MLabel.TRAILING;
        assert l1.getLabelFor() == null;
        assert l1.getText() == null;
        testConstructor(l1);
        l2 = MLabel.createFor(buddy, "Foo", MLabel.TRAILING);
        assert l2.getHorizontalAlignment() == MLabel.TRAILING;
        assert l2.getLabelFor() == buddy;
        assert l2.getText().equals("Foo");
        testConstructor(l2);
        assert l1 != l2;
    }

    @Test(methods = @TestMethod(name = "createSmall", parameters = "String, Icon"))
    public void test_createSmall() {
        int baseFontSize = new MLabel().getFont().getSize();
        MIcon icon = MIcon.stock("ui/ok");
        assert icon != null;
        MLabel l1;
        MLabel l2;
        l1 = MLabel.createSmall(null, null);
        assert l1.getFont().getSize() == (baseFontSize - 1);
        assert l1.getIcon() == null;
        assert l1.getText() == null;
        testConstructor(l1);
        l2 = MLabel.createSmall("Foo", icon);
        assert l2.getFont().getSize() == (baseFontSize - 1);
        assert l2.getIcon() == icon;
        assert l2.getText().equals("Foo");
        testConstructor(l2);
        assert l1 != l2;
    }

    @Test(methods = @TestMethod(name = "getLabel", parameters = "JComponent"))
    public void test_getLabel() {
        Tester.testNullPointerException(new Tester.Code() {

            public void run() throws Throwable {
                MLabel.getLabel(null);
            }
        });
        MLabel l = new MLabel();
        MComponent c = new MComponent();
        assert MLabel.getLabel(c) == null;
        l.setLabelFor(c);
        assert MLabel.getLabel(c) == l;
    }

    @Test(methods = @TestMethod(name = "makeLargeMessage"))
    public void test_makeLargeMessage() {
        MLabel l = new MLabel();
        l.setHorizontalAlignment(MLabel.LEFT);
        l.setHorizontalTextPosition(MLabel.LEFT);
        l.setVerticalAlignment(MLabel.TOP);
        l.setVerticalTextPosition(MLabel.TOP);
        l.setIconTextGap(0);
        l.makeLargeMessage();
        assert l.getHorizontalAlignment() == MLabel.CENTER;
        assert l.getHorizontalTextPosition() == MLabel.CENTER;
        assert l.getVerticalAlignment() == MLabel.CENTER;
        assert l.getVerticalTextPosition() == MLabel.BOTTOM;
        assert l.getIconTextGap() == MPanel.DEFAULT_CONTENT_MARGIN;
    }

    @Test(methods = @TestMethod(name = "paintComponent", parameters = "Graphics"))
    public void test_paint() {
        testPaint(new MLabel());
        testPaint(new MLabel(""));
        testPaint(new MLabel("Foo"));
        testPaint(new MLabel(null, "ui/ok"));
        testPaint(new MLabel("", "ui/ok"));
        testPaint(new MLabel("Foo", "ui/ok"));
        testPropertiesPaint(new MLabel());
        testPropertiesPaint(new MLabel(""));
        testPropertiesPaint(new MLabel("Foo"));
        testPropertiesPaint(new MLabel(null, "ui/ok"));
        testPropertiesPaint(new MLabel("", "ui/ok"));
        testPropertiesPaint(new MLabel("Foo", "ui/ok"));
    }

    @Test(methods = { @TestMethod(name = "getIconName"), @TestMethod(name = "setIconName", parameters = "String") })
    public void test_property_iconName() {
        MLabel l = new MLabel();
        assert l.getIcon() == null;
        assert l.getIconName() == null;
        l.setIconName("ui/ok");
        assert l.getIcon() != null;
        assert l.getIcon().getIconWidth() == UI.iconSize.get();
        assert l.getIcon().getIconHeight() == UI.iconSize.get();
        assert MIcon.class.cast(l.getIcon()).getImage() != null;
        assert l.getIconName().equals("ui/ok");
        l.setIconName(null);
        assert l.getIcon() == null;
        assert l.getIconName() == null;
        l.setIconName("");
        assert l.getIcon() != null;
        assert l.getIcon().getIconWidth() == UI.iconSize.get();
        assert l.getIcon().getIconHeight() == UI.iconSize.get();
        assert MIcon.class.cast(l.getIcon()).getImage() == null;
        assert l.getIconName().isEmpty();
        l.setIconName("INVALID");
        assert l.getIcon() != null;
        assert l.getIcon().getIconWidth() == UI.iconSize.get();
        assert l.getIcon().getIconHeight() == UI.iconSize.get();
        assert MIcon.class.cast(l.getIcon()).getImage() == null;
        assert l.getIconName().equals("INVALID");
    }

    @Test(methods = { @TestMethod(name = "isStrikeThrough"), @TestMethod(name = "setStrikeThrough", parameters = "boolean") })
    public void test_property_strikeThrough() {
        final BooleanProperty repaintInvoked = new BooleanProperty();
        MLabel l = new MLabel() {

            @Override
            public void repaint() {
                super.repaint();
                repaintInvoked.yes();
            }
        };
        assert !l.isStrikeThrough();
        repaintInvoked.no();
        l.setStrikeThrough(true);
        assert l.isStrikeThrough();
        assert repaintInvoked.get();
        repaintInvoked.no();
        l.setStrikeThrough(true);
        assert l.isStrikeThrough();
        assert !repaintInvoked.get();
        repaintInvoked.no();
        l.setStrikeThrough(false);
        assert !l.isStrikeThrough();
        assert repaintInvoked.get();
    }

    @Test(methods = { @TestMethod(name = "isTextAntialiasing"), @TestMethod(name = "setTextAntialiasing", parameters = "boolean") })
    public void test_property_textAntialiasing() {
        final BooleanProperty repaintInvoked = new BooleanProperty();
        MLabel l = new MLabel() {

            @Override
            public void repaint() {
                super.repaint();
                repaintInvoked.yes();
            }
        };
        assert !l.isTextAntialiasing();
        repaintInvoked.no();
        l.setTextAntialiasing(true);
        assert l.isTextAntialiasing();
        assert repaintInvoked.get();
        repaintInvoked.no();
        l.setTextAntialiasing(true);
        assert l.isTextAntialiasing();
        assert !repaintInvoked.get();
        repaintInvoked.no();
        l.setTextAntialiasing(false);
        assert !l.isTextAntialiasing();
        assert repaintInvoked.get();
    }

    @Test(methods = @TestMethod(name = "setCursor", parameters = "int"))
    public void test_setCursor() {
        Tester.testIllegalArgumentException(new Tester.Code() {

            public void run() throws Throwable {
                MLabel l = new MLabel();
                l.setCursor(666);
            }
        });
        MLabel l = new MLabel();
        assert l.getCursor().getType() != Cursor.HAND_CURSOR;
        l.setCursor(Cursor.HAND_CURSOR);
        assert l.getCursor().getType() == Cursor.HAND_CURSOR;
    }

    @Test(methods = @TestMethod(name = "setHTML", parameters = "String"))
    public void test_setHTML() {
        MLabel label = new MLabel();
        label.setHTML("<b>Bold</b> Text");
        assert label.getText().equals("<html><body><b>Bold</b> Text</body></html>");
        MLabel l = new MLabel();
        l.setHTML(null);
        assert l.getText() == null;
        l.setHTML("");
        assert l.getText().equals("<html><body></body></html>");
        l.setHTML("Foo");
        assert l.getText().equals("<html><body>Foo</body></html>");
        l.setHTML("<b>Bold</b>");
        assert l.getText().equals("<html><body><b>Bold</b></body></html>");
    }

    @Test(methods = @TestMethod(name = "setImage", parameters = "Image"))
    public void test_setImage() {
        Image image = MIcon.getImage("ui/ok");
        assert image != null;
        MLabel l = new MLabel();
        l.setImage(image);
        assert MIcon.class.cast(l.getIcon()).getImage() == image;
        l.setImage(null);
        assert l.getIcon() == null;
    }

    @Test(methods = @TestMethod(name = "setLabelFor", parameters = "Component"))
    public void test_setLabelFor() {
        MComponent c = new MComponent();
        MLabel l = new MLabel();
        assert l.getMouseListeners().length == 0;
        l.setLabelFor(null);
        assert l.getMouseListeners().length == 0;
        l.setLabelFor(c);
        assert l.getLabelFor() == c;
        assert l.getMouseListeners().length == 1;
        l.setLabelFor(c);
        assert l.getLabelFor() == c;
        assert l.getMouseListeners().length == 1;
        l.setLabelFor(null);
        assert l.getMouseListeners().length == 0;
    }

    @Test(methods = @TestMethod(name = "setMultilineText", parameters = "String"))
    public void test_setMultilineText() {
        MLabel label = new MLabel();
        label.setMultilineText("Line 1\nLine 2");
        MLabel l = new MLabel();
        l.setMultilineText("");
        assert l.getText().isEmpty();
        l.setMultilineText("\n");
        assert l.getText().equals("<html>\n" + "<head>\n" + "</head>\n" + "<body>\n" + "</body>\n" + "</html>\n");
        l.setMultilineText("Line 1");
        assert l.getText().equals("Line 1");
        l.setMultilineText("Line 1\n");
        assert l.getText().equals("<html>\n" + "<head>\n" + "</head>\n" + "<body>\n" + "Line 1" + "</body>\n" + "</html>\n");
        l.setMultilineText("Line 1\nLine 2");
        assert l.getText().equals("<html>\n" + "<head>\n" + "</head>\n" + "<body>\n" + "Line 1<br>" + "Line 2" + "</body>\n" + "</html>\n");
        l.setMultilineText("<Line 1>\n\n<Line 2>");
        assert l.getText().equals("<html>\n" + "<head>\n" + "</head>\n" + "<body>\n" + "&lt;Line 1&gt;<br>" + "<br>" + "&lt;Line 2&gt;" + "</body>\n" + "</html>\n");
        l.setMultilineText(null);
        assert l.getText() == null;
    }

    @Test(methods = @TestMethod(name = "setNumber", parameters = "Number"))
    public void test_setNumber() {
        Tester.testNullPointerException(new Tester.Code() {

            public void run() throws Throwable {
                MLabel l = new MLabel();
                l.setNumber(null);
            }
        });
        MLabel l = new MLabel();
        l.setNumber(BigDecimal.ONE);
        assert l.getText().equals(BigDecimal.ONE.toString());
        l.setNumber(BigInteger.ONE);
        assert l.getText().equals(BigInteger.ONE.toString());
        l.setNumber(Byte.MAX_VALUE);
        assert l.getText().equals(Byte.toString(Byte.MAX_VALUE));
        l.setNumber(Double.MAX_VALUE);
        assert l.getText().equals(Double.toString(Double.MAX_VALUE));
        l.setNumber(Float.MAX_VALUE);
        assert l.getText().equals(Float.toString(Float.MAX_VALUE));
        l.setNumber(Short.MAX_VALUE);
        assert l.getText().equals(Short.toString(Short.MAX_VALUE));
        l.setNumber(Integer.MAX_VALUE);
        assert l.getText().equals(Integer.toString(Integer.MAX_VALUE));
        l.setNumber(Long.MAX_VALUE);
        assert l.getText().equals(Long.toString(Long.MAX_VALUE));
    }

    @Test(methods = @TestMethod(name = "setStyle", parameters = "String"))
    public void test_setStyle() {
        TestStyle.test(new MLabel());
    }

    @Test(methods = @TestMethod(name = "updateUI"))
    public void test_updateUI() {
        setFakeLookAndFeelType(UI.LookAndFeelType.UNKNOWN);
        MLabel l = new MLabel();
        assert l.getClientProperty("substancelaf.useThemedDefaultIcons") == null;
        setFakeLookAndFeelType(UI.LookAndFeelType.SUBSTANCE);
        l.updateUI();
        assert Boolean.FALSE.equals(l.getClientProperty("substancelaf.useThemedDefaultIcons"));
    }

    private void testConstructor(final MLabel l) {
        assert !l.isStrikeThrough();
        assert !l.isTextAntialiasing();
        Tester.testSerializable(l);
    }

    private void testPropertiesPaint(final MLabel l) {
        l.setStrikeThrough(true);
        l.setTextAntialiasing(true);
        testPaint(l);
    }
}
