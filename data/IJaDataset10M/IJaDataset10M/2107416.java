package org.tripcom.query.client.view.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.tripcom.query.client.QueryToolView;
import org.tripcom.query.client.sir.sparql.core.OrderElement;
import org.tripcom.query.client.sir.sparql.core.QueryNode;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Dialog for defining SPARQL ORDER BY clauses 
 * @author Pasi Tiitinen
 *
 */
public class OrderDialog extends DialogBox {

    /**
	 * Listbox containing all variables
	 */
    ListBox lbVariable = new ListBox();

    /**
	 * Listbox containing variables chosen for ordering
	 */
    ListBox lbChosen = new ListBox();

    RadioButton rbOrderAsc = new RadioButton("orderGroup", "Ascending");

    RadioButton rbOrderDesc = new RadioButton("orderGroup", "Descending");

    HashMap<String, OrderElement> orderBy = new HashMap<String, OrderElement>();

    public OrderDialog(final QueryToolView view) {
        setText("Define ordering");
        VerticalPanel panel = new VerticalPanel();
        setWidget(panel);
        center();
        FlexTable table = new FlexTable();
        lbVariable.setWidth("14em");
        rbOrderAsc.setChecked(true);
        List<String> allVariables = view.getVariables();
        Set<String> variables = new HashSet<String>();
        for (String variable : allVariables) {
            if (variables.add(variable)) lbVariable.addItem(variable);
            ;
        }
        List<OrderElement> oeList = view.getOrderBy();
        for (OrderElement oe : oeList) {
            QueryNode qn = oe.getNode();
            orderBy.put(qn.getName(), oe);
        }
        refreshChosen();
        Button btnAdd = new Button("Add");
        btnAdd.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                String variable = lbVariable.getItemText(lbVariable.getSelectedIndex());
                int type;
                if (rbOrderAsc.isChecked()) type = 0; else type = 1;
                OrderElement oe = new OrderElement(variable, type);
                orderBy.put(variable, oe);
                refreshChosen();
            }
        });
        lbChosen.setWidth("14em");
        lbChosen.setVisibleItemCount(5);
        table.setText(0, 0, "Field");
        table.setWidget(0, 1, lbVariable);
        table.setText(1, 0, "Order");
        table.setWidget(1, 1, rbOrderAsc);
        table.setWidget(1, 2, rbOrderDesc);
        table.setWidget(2, 0, btnAdd);
        table.setWidget(3, 0, lbChosen);
        table.getFlexCellFormatter().setColSpan(3, 0, 2);
        Button btnClose = new Button("Ok");
        btnClose.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                if (orderBy.size() > 0) {
                    List<OrderElement> orderByList = new ArrayList<OrderElement>();
                    Iterator<String> i = orderBy.keySet().iterator();
                    while (i.hasNext()) {
                        OrderElement oe = orderBy.get(i.next());
                        orderByList.add(oe);
                    }
                    view.setOrderby(orderByList);
                }
                hide();
            }
        });
        Button btnCancel = new Button("Cancel");
        btnCancel.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                hide();
            }
        });
        table.setWidget(4, 0, btnClose);
        table.setWidget(4, 1, btnCancel);
        panel.add(table);
    }

    /**
	 * Refreshes the list of ORDER BY elements
	 */
    protected void refreshChosen() {
        lbChosen.clear();
        Iterator<String> i = orderBy.keySet().iterator();
        while (i.hasNext()) {
            OrderElement oe = orderBy.get(i.next());
            QueryNode qn = oe.getNode();
            lbChosen.addItem(qn.getName() + " (" + oe.getTypeAsString() + ")", qn.getName());
        }
    }
}
