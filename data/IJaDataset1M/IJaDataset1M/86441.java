#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId}.client.view;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

import ${package}.common.utils.ValUtils;
import ${package}.common.wrappers.OrderWrapper;
import ${package}.${artifactId}.client.model.Model;
import ${package}.${artifactId}.client.presenter.OrderDetailsPresenter;
import ${package}.${artifactId}.client.utils.ValueItem;
import ${package}.${artifactId}.client.view.widgets.BatchListBox;

/**
 * Panel for presenting order details
 * @author pstepaniak
 *
 */
public class OrderDetailsPanel extends FlowPanel implements OrderDetailsPresenter.Display {

	private final FlexTable mainTable = new FlexTable();
	private final Label nameLabel = new Label("Description:");
	private final TextBox name = new TextBox();
	private final Label createdLabel = new Label("Created: ");
	private final DateBox created = new DateBox();
	private final Label priorityLabel = new Label("Priority: ");
	private final TextBox priority = new TextBox();

	private final Label customerLabel = new Label("Customer: ");
	private final BatchListBox customerListBox = new BatchListBox("customerListBoxId");
	
	private final Label orderLinesLabel = new Label("Items:");
	private final FlexTable orderLinesTable = new FlexTable();
	
	private final HorizontalPanel buttonPanel = new HorizontalPanel();
	private final Button saveButton = new Button("Save");
	private final Button addButton = new Button("Add");
	private final Button removeButton = new Button("Remove");
	private final Button backButton = new Button("Back to list");
	
	public OrderDetailsPanel() {
		super();
		initView();
	}
	
	public final void initView() {
		mainTable.setWidget(0, 0, nameLabel);
		mainTable.setWidget(0, 1, name);
		
		mainTable.setWidget(1, 0, createdLabel);
		mainTable.setWidget(1, 1, created);

		mainTable.setWidget(2, 0, priorityLabel);
		mainTable.setWidget(2, 1, priority);

		mainTable.setWidget(3, 0, customerLabel);
		mainTable.setWidget(3, 1, customerListBox);

		mainTable.setWidget(4, 0, orderLinesLabel);
		mainTable.setWidget(4, 1, orderLinesTable);
		
		buttonPanel.add(saveButton);
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(backButton);
		
		this.add(mainTable);
		this.add(buttonPanel);
	}
	
	public void setOrder(final OrderWrapper order) {
		name.setText( order.getDescription() );
		created.setValue( order.getCreated() );
		priority.setValue( ""+order.getPriority() );
		
		if (order.getOrderCustomer()==null) {
			customerListBox.setValue("");
		} else {
			customerListBox.setValue(""+order.getOrderCustomer().getId());
		}
		
		orderLinesTable.removeAllRows();
		if (order.getOrderLines()!=null) {
			for (int i=0; i < order.getOrderLines().size(); ++i) {
				orderLinesTable.setWidget(i, 0, new Label("Product: "+order.getOrderLines().get(i).getProductName() + ", quantity: " + order.getOrderLines().get(i).getQuantity() ));
			}
		}
	}
	
	public void fillOrder(final OrderWrapper order) {
		if (order!=null) {
			order.setDescription( ValUtils.getVal(name.getText()) );
			order.setCreated( created.getValue());
			order.setPriority( ValUtils.toIntObj(priority.getText()) );
			order.setOrderCustomer( Model.getInstance().getCustomerById(customerListBox.getValue()) );
		}
	}

	public void initCustomers(final ArrayList<ValueItem> items) {
		customerListBox.batchSet(items);
	}
	
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}

	public HasClickHandlers getBackButton() {
		return backButton;
	}

	public Button getAddButton() {
		return addButton;
	}

	public Button getRemoveButton() {
		return removeButton;
	}
	
}
