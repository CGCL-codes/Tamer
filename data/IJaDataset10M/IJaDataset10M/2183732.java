package aga.jitracker.client.views.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import aga.jitracker.client.ui.editors.EditorBase;
import aga.jitracker.client.ui.editors.EditorsFactory;
import aga.jitracker.client.ui.editors.MultiValueEditor;
import aga.jitracker.client.ui.editors.SelectorEditor;
import aga.jitracker.shared.domain.Attribute;
import aga.jitracker.shared.domain.Issue.SystemAttributes;
import aga.jitracker.shared.domain.FilterInfo;
import aga.jitracker.shared.domain.FilterInfo.FilterOperator;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class FiltersView implements IsWidget {

    ArrayList<Attribute> attributes = new ArrayList<Attribute>();

    List<String> names = new ArrayList<String>();

    List<FilterRow> rows = new ArrayList<FilterRow>();

    FlexTable table;

    ArrayList<FilterInfo> filters;

    private static class FilterRow {

        SelectorEditor<String> attributeEditor;

        SelectorEditor<FilterOperator> operatorEdtior;

        EditorBase valueEditor;
    }

    public FiltersView(List<Attribute> attributes) {
        this.attributes.addAll(attributes);
        for (SystemAttributes a : SystemAttributes.values()) {
            Attribute sa = new Attribute();
            sa.setName(a.name());
            this.attributes.add(sa);
        }
        for (Attribute a : this.attributes) {
            names.add(a.getName());
        }
        table = new FlexTable();
        table.getColumnFormatter().setWidth(0, "200px");
        table.getColumnFormatter().setWidth(1, "200px");
        table.getColumnFormatter().setWidth(2, "200px");
        table.setWidth("100%");
        Button add = new Button("+");
        add.setWidth("25px");
        add.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addRow();
            }
        });
        table.setWidget(0, 3, add);
    }

    @Override
    public Widget asWidget() {
        return table;
    }

    public void setList(ArrayList<FilterInfo> filters) {
        this.filters = filters;
        for (FilterInfo fi : filters) {
            FilterRow fr = addRow();
            displayFilterInfo(fr, fi);
        }
    }

    public boolean flush() {
        for (FilterRow fr : rows) {
            if (!fr.valueEditor.validate()) return false;
        }
        filters.clear();
        for (FilterRow fr : rows) {
            FilterInfo fi = new FilterInfo();
            fi.setAttributeName(fr.attributeEditor.getSelectedValue());
            fi.setOperator(fr.operatorEdtior.getSelectedValue());
            fi.setValue(fr.valueEditor.getValue());
            filters.add(fi);
        }
        return true;
    }

    private FilterRow addRow() {
        final FilterRow fr = new FilterRow();
        rows.add(fr);
        int row = table.insertRow(table.getRowCount() - 1);
        table.getRowFormatter().setVerticalAlign(row, HasVerticalAlignment.ALIGN_TOP);
        fr.attributeEditor = new SelectorEditor<String>(names);
        fr.attributeEditor.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                int tr = getTableRow(event.getRelativeElement());
                setValueEditor(tr, fr);
            }
        });
        table.setWidget(row, 0, fr.attributeEditor.asWidget());
        fr.attributeEditor.asWidget().setWidth("100%");
        fr.operatorEdtior = new SelectorEditor<FilterOperator>(Arrays.asList(FilterOperator.values()));
        fr.operatorEdtior.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                int tr = getTableRow(event.getRelativeElement());
                setValueEditor(tr, fr);
            }
        });
        table.setWidget(row, 1, fr.operatorEdtior.asWidget());
        fr.operatorEdtior.asWidget().setWidth("100%");
        setValueEditor(row, fr);
        Button remove = new Button("-");
        remove.setWidth("25px");
        table.setWidget(row, 3, remove);
        remove.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                int tr = getTableRow(event.getRelativeElement());
                table.removeRow(tr);
                rows.remove(fr);
            }
        });
        return fr;
    }

    private int getTableRow(Element element) {
        while (element != null) {
            String tagName = element.getTagName();
            if ("tr".equalsIgnoreCase(tagName)) {
                TableRowElement tr = element.cast();
                return tr.getRowIndex();
            }
            element = element.getParentElement();
        }
        throw new RuntimeException();
    }

    private void setValueEditor(int row, FilterRow fr) {
        String attrName = fr.attributeEditor.getSelectedValue();
        for (Attribute a : attributes) {
            if (attrName.equals(a.getName())) {
                if (fr.operatorEdtior.getSelectedValue() == FilterOperator.IN) fr.valueEditor = new MultiValueEditor(a); else fr.valueEditor = EditorsFactory.INSTANCE.createEditor(a);
                break;
            }
        }
        fr.valueEditor.asWidget().setWidth("100%");
        table.setWidget(row, 2, fr.valueEditor.asWidget());
    }

    private void displayFilterInfo(FilterRow fr, FilterInfo fi) {
        fr.attributeEditor.setValue(fi.getAttributeName());
        fr.operatorEdtior.setValue(fi.getOperator());
        int tr = getTableRow(fr.attributeEditor.asWidget().getElement());
        setValueEditor(tr, fr);
        fr.valueEditor.setValue(fi.getValue());
    }
}
