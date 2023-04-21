package ca.ericslandry.client.widget;

import ca.ericslandry.client.resource.MyConstants;
import ca.ericslandry.client.resource.MyImages;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A sample toolbar for use with {@link RichTextArea}. It provides a simple UI
 * for all rich text formatting, dynamically displayed only for the available
 * functionality.
 */
public class RichTextToolbar extends Composite {

    /**
	 * We use an inner EventHandler class to avoid exposing event methods on the
	 * RichTextToolbar itself.
	 */
    private class EventHandler implements ClickHandler, KeyUpHandler {

        public void onClick(ClickEvent event) {
            Widget sender = (Widget) event.getSource();
            if (sender == bold) {
                formatter.toggleBold();
            } else if (sender == italic) {
                formatter.toggleItalic();
            } else if (sender == underline) {
                formatter.toggleUnderline();
            } else if (sender == subscript) {
                formatter.toggleSubscript();
            } else if (sender == superscript) {
                formatter.toggleSuperscript();
            } else if (sender == strikethrough) {
                formatter.toggleStrikethrough();
            } else if (sender == indent) {
                formatter.rightIndent();
            } else if (sender == outdent) {
                formatter.leftIndent();
            } else if (sender == justifyLeft) {
                formatter.setJustification(RichTextArea.Justification.LEFT);
            } else if (sender == justifyCenter) {
                formatter.setJustification(RichTextArea.Justification.CENTER);
            } else if (sender == justifyRight) {
                formatter.setJustification(RichTextArea.Justification.RIGHT);
            } else if (sender == insertImage) {
                String url = Window.prompt("Enter an image URL:", "http://");
                if (url != null) {
                    formatter.insertImage(url);
                }
            } else if (sender == createLink) {
                String url = Window.prompt("Enter a link URL:", "http://");
                if (url != null) {
                    formatter.createLink(url);
                }
            } else if (sender == removeLink) {
                formatter.removeLink();
            } else if (sender == hr) {
                formatter.insertHorizontalRule();
            } else if (sender == ol) {
                formatter.insertOrderedList();
            } else if (sender == ul) {
                formatter.insertUnorderedList();
            } else if (sender == removeFormat) {
                formatter.removeFormat();
            } else if (sender == richText) {
                updateStatus();
            }
        }

        public void onKeyUp(KeyUpEvent event) {
            Widget sender = (Widget) event.getSource();
            if (sender == richText) {
                updateStatus();
            }
        }
    }

    private EventHandler handler = new EventHandler();

    private RichTextArea richText;

    private RichTextArea.Formatter formatter;

    private VerticalPanel outer = new VerticalPanel();

    private HorizontalPanel topPanel = new HorizontalPanel();

    private ToggleButton bold;

    private ToggleButton italic;

    private ToggleButton underline;

    private ToggleButton subscript;

    private ToggleButton superscript;

    private ToggleButton strikethrough;

    private PushButton indent;

    private PushButton outdent;

    private PushButton justifyLeft;

    private PushButton justifyCenter;

    private PushButton justifyRight;

    private PushButton hr;

    private PushButton ol;

    private PushButton ul;

    private PushButton insertImage;

    private PushButton createLink;

    private PushButton removeLink;

    private PushButton removeFormat;

    /**
	 * Creates a new toolbar that drives the given rich text area.
	 * 
	 * @param richText
	 *            the rich text area to be controlled
	 */
    public RichTextToolbar(RichTextArea richText) {
        this.richText = richText;
        this.formatter = richText.getFormatter();
        outer.add(topPanel);
        initWidget(outer);
        setStyleName("gwt-RichTextToolbar");
        richText.addStyleName("hasRichTextToolbar");
        if (formatter != null) {
            topPanel.add(bold = createToggleButton(MyImages.INSTANCE.bold(), MyConstants.INSTANCE.bold()));
            topPanel.add(italic = createToggleButton(MyImages.INSTANCE.italic(), MyConstants.INSTANCE.italic()));
            topPanel.add(underline = createToggleButton(MyImages.INSTANCE.underline(), MyConstants.INSTANCE.underline()));
            topPanel.add(subscript = createToggleButton(MyImages.INSTANCE.subscript(), MyConstants.INSTANCE.subscript()));
            topPanel.add(superscript = createToggleButton(MyImages.INSTANCE.superscript(), MyConstants.INSTANCE.superscript()));
            topPanel.add(justifyLeft = createPushButton(MyImages.INSTANCE.justifyLeft(), MyConstants.INSTANCE.justifyLeft()));
            topPanel.add(justifyCenter = createPushButton(MyImages.INSTANCE.justifyCenter(), MyConstants.INSTANCE.justifyCenter()));
            topPanel.add(justifyRight = createPushButton(MyImages.INSTANCE.justifyRight(), MyConstants.INSTANCE.justifyRight()));
        }
        if (formatter != null) {
            topPanel.add(strikethrough = createToggleButton(MyImages.INSTANCE.strikeThrough(), MyConstants.INSTANCE.strikeThrough()));
            topPanel.add(indent = createPushButton(MyImages.INSTANCE.indent(), MyConstants.INSTANCE.indent()));
            topPanel.add(outdent = createPushButton(MyImages.INSTANCE.outdent(), MyConstants.INSTANCE.outdent()));
            topPanel.add(hr = createPushButton(MyImages.INSTANCE.hr(), MyConstants.INSTANCE.hr()));
            topPanel.add(ol = createPushButton(MyImages.INSTANCE.ol(), MyConstants.INSTANCE.ol()));
            topPanel.add(ul = createPushButton(MyImages.INSTANCE.ul(), MyConstants.INSTANCE.ul()));
            topPanel.add(insertImage = createPushButton(MyImages.INSTANCE.insertImage(), MyConstants.INSTANCE.insertImage()));
            topPanel.add(createLink = createPushButton(MyImages.INSTANCE.createLink(), MyConstants.INSTANCE.createLink()));
            topPanel.add(removeLink = createPushButton(MyImages.INSTANCE.removeLink(), MyConstants.INSTANCE.removeLink()));
            topPanel.add(removeFormat = createPushButton(MyImages.INSTANCE.removeFormat(), MyConstants.INSTANCE.removeFormat()));
        }
        if (formatter != null) {
            richText.addKeyUpHandler(handler);
            richText.addClickHandler(handler);
        }
    }

    private PushButton createPushButton(ImageResource imageResource, String tip) {
        PushButton pb = new PushButton(new Image(imageResource));
        pb.addClickHandler(handler);
        pb.setTitle(tip);
        return pb;
    }

    private ToggleButton createToggleButton(ImageResource imageResource, String tip) {
        ToggleButton tb = new ToggleButton(new Image(imageResource));
        tb.addClickHandler(handler);
        tb.setTitle(tip);
        return tb;
    }

    /**
	 * Updates the status of all the stateful buttons.
	 */
    private void updateStatus() {
        if (formatter != null) {
            bold.setDown(formatter.isBold());
            italic.setDown(formatter.isItalic());
            underline.setDown(formatter.isUnderlined());
            subscript.setDown(formatter.isSubscript());
            superscript.setDown(formatter.isSuperscript());
        }
        if (formatter != null) {
            strikethrough.setDown(formatter.isStrikethrough());
        }
    }
}
