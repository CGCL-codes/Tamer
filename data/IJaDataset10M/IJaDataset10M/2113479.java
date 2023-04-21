package com.googlecode.gwt_control.client.form;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListenerAdapter;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Olafur Gauti Gudmundsson
 */
public class SuggestField<T extends Suggestion> extends Field {

    private final SuggestBox box;

    private T suggestion;

    public SuggestField(String id, String title, SuggestOracle suggestOracle) {
        this(id, title, suggestOracle, null);
    }

    public SuggestField(String id, String title, SuggestOracle suggestOracle, FieldValidator validator) {
        super(id, title, validator);
        box = new SuggestBox(suggestOracle);
        box.setStyleName("suggestField");
        box.setLimit(5);
        box.addFocusListener(new FocusListenerAdapter() {

            @Override
            public void onLostFocus(Widget w) {
                if (box.getText().trim().length() == 0) {
                    suggestion = null;
                }
                fireFieldChanged();
            }
        });
        box.addEventHandler(new SuggestionHandler() {

            public void onSuggestionSelected(SuggestionEvent event) {
                suggestion = (T) event.getSelectedSuggestion();
                fireFieldChanged();
            }
        });
    }

    @Override
    public void addToTable(int rowNumber, FlexTable table) {
        super.addToTable(rowNumber, table, box);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        box.setAnimationEnabled(enabled);
        if (enabled) {
            box.removeStyleName("disabled");
        } else {
            box.addStyleName("disabled");
        }
    }

    public SuggestBox getBox() {
        return box;
    }

    public T getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(T suggestion) {
        this.suggestion = suggestion;
        box.setText(suggestion.getReplacementString());
    }

    @Override
    public boolean isEmpty() {
        return box.getText().trim().length() == 0 || suggestion == null;
    }
}
