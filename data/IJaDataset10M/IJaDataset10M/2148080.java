package com.google.gwt.user.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.InitializeEvent;
import com.google.gwt.event.logical.shared.InitializeHandler;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RichTextArea.BasicFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the {@link RichTextArea} widget.
 */
public class RichTextAreaTest extends GWTTestCase {

    static final int RICH_TEXT_ASYNC_DELAY = 3000;

    private static final String html = "<b>hello</b><i>world</i>";

    @Override
    public String getModuleName() {
        return "com.google.gwt.user.User";
    }

    /**
   * Test that removing and re-adding an RTA doesn't destroy its contents (Only
   * IE actually preserves dynamically-created iframe contents across DOM
   * removal/re-adding).
   */
    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testAddEditRemoveAdd() {
        final RichTextArea area = new RichTextArea();
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        area.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                RootPanel.get().remove(area);
                RootPanel.get().add(area);
                assertEquals("foo", area.getHTML());
                finishTest();
            }
        });
        RootPanel.get().add(area);
        area.setHTML("foo");
    }

    public void testBlurAfterAttach() {
        final RichTextArea rta = new RichTextArea();
        final List<String> actual = new ArrayList<String>();
        rta.addFocusHandler(new FocusHandler() {

            public void onFocus(FocusEvent event) {
                actual.add("test");
            }
        });
        RootPanel.get().add(rta);
        rta.setFocus(true);
        rta.setFocus(false);
        this.delayTestFinish(3000);
        new Timer() {

            @Override
            public void run() {
                assertEquals(0, actual.size());
                RootPanel.get().remove(rta);
                finishTest();
            }
        }.schedule(2000);
    }

    public void testFocusAfterAttach() {
        final RichTextArea rta = new RichTextArea();
        final List<String> actual = new ArrayList<String>();
        rta.addFocusHandler(new FocusHandler() {

            public void onFocus(FocusEvent event) {
                actual.add("test");
            }
        });
        RootPanel.get().add(rta);
        rta.setFocus(true);
    }

    /**
   * Test that adding and removing an RTA before initialization completes
   * doesn't throw an exception.
   */
    public void testAddRemoveBeforeInit() {
        final RichTextArea richTextArea = new RichTextArea();
        RootPanel.get().add(richTextArea);
        RootPanel.get().remove(richTextArea);
    }

    public void testFormatAfterAttach() {
        final RichTextArea area = new RichTextArea();
        BasicFormatter formatter = area.getBasicFormatter();
        RootPanel.get().add(area);
        if (formatter != null) {
            try {
                formatter.toggleBold();
                if (!GWT.isScript()) {
                    fail("Expected AssertionError");
                }
            } catch (AssertionError e) {
                return;
            }
            if (!GWT.isScript()) {
                fail("Expected AssertionError");
            }
        }
    }

    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testFormatAfterInitialize() {
        final RichTextArea area = new RichTextArea();
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        area.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                BasicFormatter formatter = area.getBasicFormatter();
                if (formatter != null) {
                    formatter.toggleBold();
                }
                RootPanel.get().remove(area);
                finishTest();
            }
        });
        RootPanel.get().add(area);
    }

    public void testFormatBeforeAttach() {
        final RichTextArea area = new RichTextArea();
        BasicFormatter formatter = area.getBasicFormatter();
        if (formatter != null) {
            try {
                formatter.toggleBold();
                if (!GWT.isScript()) {
                    fail("Expected AssertionError");
                }
            } catch (AssertionError e) {
                return;
            }
            if (!GWT.isScript()) {
                fail("Expected AssertionError");
            }
        }
    }

    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testFormatWhenHidden() {
        final RichTextArea area = new RichTextArea();
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        area.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                area.setVisible(false);
                BasicFormatter formatter = area.getBasicFormatter();
                if (formatter != null) {
                    formatter.toggleBold();
                }
                RootPanel.get().remove(area);
                finishTest();
            }
        });
        RootPanel.get().add(area);
    }

    /**
   * See that the custom InitializeEvent fires.
   */
    @DoNotRunWith({ Platform.HtmlUnitUnknown })
    public void testRichTextInitializeEvent() {
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        final RichTextArea richTextArea = new RichTextArea();
        richTextArea.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                finishTest();
            }
        });
        RootPanel.get().add(richTextArea);
    }

    /**
   * Test that a delayed call to setEnable is reflected.
   */
    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testSetEnabledAfterInit() {
        final RichTextArea richTextArea = new RichTextArea();
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        richTextArea.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                richTextArea.setEnabled(false);
                assertEquals(false, richTextArea.isEnabled());
                richTextArea.setEnabled(true);
                assertEquals(true, richTextArea.isEnabled());
                finishTest();
            }
        });
        RootPanel.get().add(richTextArea);
    }

    /**
   * Test that a call to setEnable is reflected immediately, and after the area
   * loads.
   */
    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testSetEnabledBeforeInit() {
        final RichTextArea richTextArea = new RichTextArea();
        richTextArea.setEnabled(false);
        assertEquals(false, richTextArea.isEnabled());
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        richTextArea.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                assertEquals(false, richTextArea.isEnabled());
                finishTest();
            }
        });
        RootPanel.get().add(richTextArea);
        assertEquals(false, richTextArea.isEnabled());
    }

    /**
   * Test that events are dispatched correctly to handlers.
   */
    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testEventDispatch() {
        final RichTextArea rta = new RichTextArea();
        RootPanel.get().add(rta);
        final List<String> actual = new ArrayList<String>();
        rta.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                assertNotNull(Event.getCurrentEvent());
                actual.add("test");
            }
        });
        delayTestFinish(1000);
        new Timer() {

            @Override
            public void run() {
                assertEquals(0, actual.size());
                NativeEvent event = getDocument(rta).createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
                getBodyElement(rta).dispatchEvent(event);
                assertEquals(1, actual.size());
                RootPanel.get().remove(rta);
                finishTest();
            }
        }.schedule(500);
    }

    /**
   * Test that a delayed set of HTML is reflected. Some platforms have timing
   * subtleties that need to be tested.
   */
    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testSetHTMLAfterInit() {
        final RichTextArea richTextArea = new RichTextArea();
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        richTextArea.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                richTextArea.setHTML("<b>foo</b>");
                assertEquals("<b>foo</b>", richTextArea.getHTML().toLowerCase());
                finishTest();
            }
        });
        RootPanel.get().add(richTextArea);
    }

    /**
   * Test that an immediate set of HTML is reflected immediately and after the
   * area loads. Some platforms have timing subtleties that need to be tested.
   */
    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testSetHTMLBeforeInit() {
        final RichTextArea richTextArea = new RichTextArea();
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        richTextArea.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                new Timer() {

                    @Override
                    public void run() {
                        assertEquals("<b>foo</b>", richTextArea.getHTML().toLowerCase());
                        finishTest();
                    }
                }.schedule(100);
            }
        });
        richTextArea.setHTML("<b>foo</b>");
        RootPanel.get().add(richTextArea);
        assertEquals("<b>foo</b>", richTextArea.getHTML().toLowerCase());
    }

    /**
   * Test that a delayed set of safe html is reflected. Some platforms have
   * timing subtleties that need to be tested.
   */
    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testSetSafeHtmlAfterInit() {
        final RichTextArea richTextArea = new RichTextArea();
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        richTextArea.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                richTextArea.setHTML(SafeHtmlUtils.fromSafeConstant(html));
                assertEquals(html, richTextArea.getHTML().toLowerCase());
                finishTest();
            }
        });
        RootPanel.get().add(richTextArea);
    }

    /**
   * Test that an immediate set of safe html is reflected immediately and after
   * the area loads. Some platforms have timing subtleties that need to be
   * tested.
   */
    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testSetSafeHtmlBeforeInit() {
        final RichTextArea richTextArea = new RichTextArea();
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        richTextArea.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                new Timer() {

                    @Override
                    public void run() {
                        assertEquals(html, richTextArea.getHTML().toLowerCase());
                        finishTest();
                    }
                }.schedule(100);
            }
        });
        richTextArea.setHTML(SafeHtmlUtils.fromSafeConstant(html));
        RootPanel.get().add(richTextArea);
        assertEquals(html, richTextArea.getHTML().toLowerCase());
    }

    /**
   * Test that delayed set of text is reflected. Some platforms have timing
   * subtleties that need to be tested.
   */
    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testSetTextAfterInit() {
        final RichTextArea richTextArea = new RichTextArea();
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        richTextArea.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                richTextArea.setText("foo");
                assertEquals("foo", richTextArea.getText());
                finishTest();
            }
        });
        RootPanel.get().add(richTextArea);
    }

    /**
   * Test that an immediate set of text is reflected immediately and after the
   * area loads. Some platforms have timing subtleties that need to be tested.
   */
    @DoNotRunWith(Platform.HtmlUnitUnknown)
    public void testSetTextBeforeInit() {
        final RichTextArea richTextArea = new RichTextArea();
        richTextArea.setText("foo");
        delayTestFinish(RICH_TEXT_ASYNC_DELAY);
        richTextArea.addInitializeHandler(new InitializeHandler() {

            public void onInitialize(InitializeEvent event) {
                assertEquals("foo", richTextArea.getText());
                finishTest();
            }
        });
        RootPanel.get().add(richTextArea);
        assertEquals("foo", richTextArea.getText());
    }

    /**
   * Get the body element from a RichTextArea.
   * 
   * @param rta the {@link RichTextArea}
   * @return the body element
   */
    private Element getBodyElement(RichTextArea rta) {
        return getDocument(rta).getBody().cast();
    }

    /**
   * Get the iframe's Document. This is useful for creating events, which must
   * be created in the iframe's document to work correctly.
   * 
   * @param rta the {@link RichTextArea}
   * @return the document element
   */
    private Document getDocument(RichTextArea rta) {
        return getDocumentImpl(rta.getElement());
    }

    private native Document getDocumentImpl(Element iframe);
}
