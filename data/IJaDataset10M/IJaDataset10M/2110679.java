package org.jdna.bmt.web.client.ui.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SearchBoxPanel extends Composite implements HasText {

    private static SearchBoxPanelUiBinder uiBinder = GWT.create(SearchBoxPanelUiBinder.class);

    interface SearchBoxPanelUiBinder extends UiBinder<Widget, SearchBoxPanel> {
    }

    public interface SearchHandler {

        public void onSearch(SearchBoxPanel widget, String text);
    }

    @UiField
    HorizontalPanel panel;

    @UiField
    TextBox text;

    @UiField
    Image icon;

    @UiField
    Image help;

    private String hint = null;

    private Widget helpWidget = null;

    private SearchHandler handler = null;

    public SearchBoxPanel() {
        this(null);
    }

    public SearchBoxPanel(SearchHandler handler) {
        initWidget(uiBinder.createAndBindUi(this));
        help.setVisible(false);
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel.setCellVerticalAlignment(icon, HasVerticalAlignment.ALIGN_MIDDLE);
        panel.setCellVerticalAlignment(help, HasVerticalAlignment.ALIGN_MIDDLE);
        this.handler = handler;
        icon.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                search();
            }
        });
        help.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Dialogs.showAsDialog("Search Help", helpWidget);
            }
        });
        text.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    search();
                }
            }
        });
        text.addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                if (hint != null && hint.equals(text.getText())) {
                    text.setText("");
                }
            }
        });
    }

    public void search() {
        if (SearchBoxPanel.this.handler != null) {
            if (getText() != null && getText().trim().length() > 0) {
                SearchBoxPanel.this.handler.onSearch(SearchBoxPanel.this, getText());
            }
        }
    }

    public void setSearchHandler(SearchHandler h) {
        this.handler = h;
    }

    @Override
    public String getText() {
        return text.getText();
    }

    @Override
    public void setText(String text) {
        this.text.setText(text);
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
        text.setText(hint);
    }

    public void setHelpWidget(Widget help) {
        this.helpWidget = help;
        this.help.setVisible(true);
    }
}
