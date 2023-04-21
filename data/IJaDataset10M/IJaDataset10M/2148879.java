package org.rydia.client;

import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <u><b><font color="red">FOR INTERNAL USE ONLY.</font></b></u>
 * Copyright (c)2007, Daniel Kaplan
 *
 * @author Daniel Kaplan
 * @since 7.11.6
 */
public class Survey {

    private static final String PAGE = "P";

    private static final int PAGE_NUM = 1;

    private static final String LABEL = "L";

    private static final String QUESTION = "Q";

    private static final String RADIO_BUTTONS = "RB";

    private static final String FILE_CHOOSER = "FC";

    private static final String CHECKBOX = "CB";

    private static final String TEXT_AREA = "TA";

    private static final String TEXT_FIELD = "TF";

    private static final String DROPDOWN = "DD";

    private Rydia.ChangeListener changeListener;

    private FlexTable table;

    private List surveyElements = new ArrayList();

    public Survey(Rydia.ChangeListener changeListener) {
        table = new FlexTable();
        table.setStyleName("rydia_page0");
        this.changeListener = changeListener;
    }

    public Panel getPanel() {
        return table;
    }

    public void setState(String[][] states) {
        int row = 0;
        for (int i = 0; i < states.length; ++i) {
            if (isSurveyElement(states[i])) {
                if (!isSurveyElementAlreadyThere(row, states[i])) {
                    clearRow(row);
                    if (isLabel(states[i])) {
                        addLabel(new Label(states[i], changeListener), row);
                    }
                    if (isType(states[i], RADIO_BUTTONS)) {
                        addQuestion(new RadioButtonsRenderer(states[i], row, changeListener), row);
                    }
                    if (isType(states[i], FILE_CHOOSER)) {
                        addQuestion(new FileChooserRenderer(states[i], row, changeListener), row);
                    }
                    if (isType(states[i], CHECKBOX)) {
                        addQuestion(new CheckboxRenderer(states[i], row, changeListener), row);
                    }
                    if (isType(states[i], TEXT_AREA)) {
                        addQuestion(new TextAreaRenderer(states[i], row, changeListener), row);
                    }
                    if (isType(states[i], TEXT_FIELD)) {
                        addQuestion(new TextFieldRenderer(states[i], row, changeListener), row);
                    }
                    if (isType(states[i], DROPDOWN)) {
                        addQuestion(new DropdownRenderer(states[i], row, changeListener), row);
                    }
                }
                row++;
            } else if (isPage(states[i])) {
                if (!table.getStyleName().equals(pageStyle(states[i]))) {
                    table.setStyleName(pageStyle(states[i]));
                }
            }
        }
        removeExtraRows(row);
        styleRows();
    }

    private void styleRows() {
        for (int i = 0; i < table.getRowCount(); ++i) {
            table.getRowFormatter().setStyleName(i, "row");
        }
    }

    private String pageStyle(String[] page) {
        return "rydia_page" + page[PAGE_NUM];
    }

    private boolean isPage(String[] state) {
        return PAGE.equals(state[0]);
    }

    public boolean isSurveyElementAlreadyThere(int row, String[] state) {
        if (row < surveyElements.size()) {
            SurveyElement currentElement = (SurveyElement) surveyElements.get(row);
            if (currentElement.isSame(state)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSurveyElement(String[] state) {
        return state[0].equals(QUESTION) || state[0].equals(LABEL);
    }

    private boolean isType(String[] state, String type) {
        return state[0].equals(QUESTION) && state[1].equals(type);
    }

    private void removeExtraRows(int row) {
        while (row < table.getRowCount()) {
            clearRow(row);
            table.removeRow(row);
        }
    }

    private void addLabel(Label label, int row) {
        addToSurveyElementsList(row, label);
        Widget text = label.getWiget();
        text.setStyleName("rydia_label");
        table.setWidget(row, 0, text);
        table.getFlexCellFormatter().setColSpan(row, 0, 3);
        table.getRowFormatter().setVisible(row, true);
    }

    private void addQuestion(Question q, int row) {
        addToSurveyElementsList(row, q);
        if (q.getColumn1() != null) {
            table.setWidget(row, 0, q.getColumn1());
            table.getFlexCellFormatter().setStyleName(row, 0, "rydia_column1");
        }
        if (q.getColumn2() != null) {
            table.setWidget(row, 1, q.getColumn2());
            table.getFlexCellFormatter().setStyleName(row, 1, "rydia_column2");
        }
        if (q.getColumn3() != null) {
            table.setWidget(row, 2, q.getColumn3());
            table.getFlexCellFormatter().setStyleName(row, 2, "rydia_column3");
        }
        table.getRowFormatter().setVisible(row, q.isVisible());
    }

    private void addToSurveyElementsList(int row, SurveyElement se) {
        if (surveyElements.size() == row) {
            surveyElements.add(row, se);
        } else {
            surveyElements.set(row, se);
        }
    }

    private void clearRow(int row) {
        if (table.isCellPresent(row, 0)) {
            table.removeCell(row, 0);
        }
        if (table.isCellPresent(row, 1)) {
            table.removeCell(row, 1);
        }
        if (table.isCellPresent(row, 2)) {
            table.removeCell(row, 2);
        }
    }

    private boolean isLabel(String[] state) {
        return state[0].equals(LABEL);
    }

    public void newPage() {
        surveyElements.clear();
    }
}
