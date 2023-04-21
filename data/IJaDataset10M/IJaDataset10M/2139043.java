package org.broadleafcommerce.gwt.client.presenter.order;

import java.util.HashMap;
import java.util.Map;
import org.broadleafcommerce.gwt.client.Main;
import org.broadleafcommerce.gwt.client.datasource.dynamic.AbstractDynamicDataSource;
import org.broadleafcommerce.gwt.client.datasource.dynamic.DynamicEntityDataSource;
import org.broadleafcommerce.gwt.client.datasource.dynamic.ListGridDataSource;
import org.broadleafcommerce.gwt.client.datasource.dynamic.operation.AsyncCallbackAdapter;
import org.broadleafcommerce.gwt.client.datasource.order.BundledOrderItemListDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.CountryListDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.FulfillmentGroupAdjustmentListDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.FulfillmentGroupListDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.OfferCodeListDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.OrderAdjustmentListDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.OrderItemAdjustmentListDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.OrderItemListDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.OrderListDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.PaymentAdditionalAttributesDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.PaymentInfoListDataSourceFactory;
import org.broadleafcommerce.gwt.client.datasource.order.StateListDataSourceFactory;
import org.broadleafcommerce.gwt.client.presenter.dynamic.entity.DynamicEntityPresenter;
import org.broadleafcommerce.gwt.client.presenter.dynamic.entity.SubPresentable;
import org.broadleafcommerce.gwt.client.presenter.dynamic.entity.SubPresenter;
import org.broadleafcommerce.gwt.client.presenter.dynamic.structure.CreateBasedListStructurePresenter;
import org.broadleafcommerce.gwt.client.presenter.dynamic.structure.MapStructurePresenter;
import org.broadleafcommerce.gwt.client.reflection.Instantiable;
import org.broadleafcommerce.gwt.client.view.dynamic.dialog.EntitySearchDialog;
import org.broadleafcommerce.gwt.client.view.dynamic.dialog.MapStructureEntityEditDialog;
import org.broadleafcommerce.gwt.client.view.dynamic.form.DynamicFormDisplay;
import org.broadleafcommerce.gwt.client.view.order.OrderDisplay;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

public class OrderPresenter extends DynamicEntityPresenter implements Instantiable {

    protected SubPresentable orderItemPresenter;

    protected SubPresentable fulfillmentGroupPresenter;

    protected SubPresentable paymentInfoPresenter;

    protected MapStructurePresenter additionalPaymentAttributesPresenter;

    protected SubPresentable offerCodePresenter;

    protected SubPresentable orderAdjustmentPresenter;

    protected SubPresentable orderItemAdjustmentPresenter;

    protected SubPresentable fulfillmentGroupAdjustmentPresenter;

    @Override
    protected void changeSelection(final Record selectedRecord) {
        orderItemPresenter.load(selectedRecord, (AbstractDynamicDataSource) display.getListDisplay().getGrid().getDataSource(), null);
        fulfillmentGroupPresenter.load(selectedRecord, (AbstractDynamicDataSource) display.getListDisplay().getGrid().getDataSource(), null);
        paymentInfoPresenter.load(selectedRecord, (AbstractDynamicDataSource) display.getListDisplay().getGrid().getDataSource(), null);
        offerCodePresenter.load(selectedRecord, (AbstractDynamicDataSource) display.getListDisplay().getGrid().getDataSource(), null);
        orderAdjustmentPresenter.load(selectedRecord, (AbstractDynamicDataSource) display.getListDisplay().getGrid().getDataSource(), null);
    }

    @Override
    public void bind() {
        super.bind();
        orderItemPresenter.bind();
        fulfillmentGroupPresenter.bind();
        paymentInfoPresenter.bind();
        additionalPaymentAttributesPresenter.bind();
        offerCodePresenter.bind();
        orderAdjustmentPresenter.bind();
        orderItemAdjustmentPresenter.bind();
        fulfillmentGroupAdjustmentPresenter.bind();
        selectionChangedHandlerRegistration.removeHandler();
        display.getListDisplay().getGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            public void onSelectionChanged(SelectionEvent event) {
                ListGridRecord selectedRecord = event.getSelectedRecord();
                if (event.getState()) {
                    if (!selectedRecord.equals(lastSelectedRecord)) {
                        lastSelectedRecord = selectedRecord;
                        if (selectedRecord.getAttributeAsStringArray("_type") == null) {
                            formPresenter.disable();
                            display.getListDisplay().getRemoveButton().disable();
                        } else {
                            formPresenter.setStartState();
                            ((DynamicEntityDataSource) display.getListDisplay().getGrid().getDataSource()).resetFieldVisibilityBasedOnType(selectedRecord.getAttributeAsStringArray("_type"));
                            display.getDynamicFormDisplay().getFormOnlyDisplay().buildFields(display.getListDisplay().getGrid().getDataSource(), false, false);
                            display.getDynamicFormDisplay().getFormOnlyDisplay().getForm().editRecord(selectedRecord);
                            display.getListDisplay().getRemoveButton().enable();
                        }
                        changeSelection(selectedRecord);
                    }
                }
            }
        });
        ((OrderDisplay) display).getPaymentInfoDisplay().getGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            public void onSelectionChanged(SelectionEvent event) {
                ListGridRecord selectedRecord = event.getSelectedRecord();
                if (event.getState()) {
                    additionalPaymentAttributesPresenter.load(selectedRecord, (AbstractDynamicDataSource) ((OrderDisplay) display).getPaymentInfoDisplay().getGrid().getDataSource(), null);
                }
            }
        });
        ((OrderDisplay) display).getOrderItemsDisplay().getGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            public void onSelectionChanged(SelectionEvent event) {
                ListGridRecord selectedRecord = event.getSelectedRecord();
                if (event.getState()) {
                    orderItemAdjustmentPresenter.load(selectedRecord, (AbstractDynamicDataSource) ((OrderDisplay) display).getOrderItemsDisplay().getGrid().getDataSource(), null);
                }
            }
        });
        ((OrderDisplay) display).getFulfillmentGroupDisplay().getGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            public void onSelectionChanged(SelectionEvent event) {
                ListGridRecord selectedRecord = event.getSelectedRecord();
                if (event.getState()) {
                    fulfillmentGroupAdjustmentPresenter.load(selectedRecord, (AbstractDynamicDataSource) ((OrderDisplay) display).getFulfillmentGroupDisplay().getGrid().getDataSource(), null);
                }
            }
        });
        setReadOnly(true);
    }

    @Override
    protected void addClicked() {
    }

    @Override
    protected void removeClicked() {
    }

    @Override
    public void go(final Canvas container) {
        Main.NON_MODAL_PROGRESS.startProgress();
        if (loaded) {
            OrderPresenter.super.go(container);
            return;
        }
        OrderListDataSourceFactory.createDataSource("orderDS", new AsyncCallbackAdapter() {

            public void onSuccess(DataSource top) {
                setupDisplayItems(top);
                ((ListGridDataSource) top).setupGridFields(new String[] { "customer.firstName", "customer.lastName", "name", "orderNumber", "status" }, new Boolean[] { false, false, false, false, false });
                OrderItemListDataSourceFactory.createDataSource("orderItemDS", new AsyncCallbackAdapter() {

                    public void onSuccess(final DataSource orderItemDS) {
                        BundledOrderItemListDataSourceFactory.createDataSource("bundleOrderItemDS", new AsyncCallbackAdapter() {

                            public void onSuccess(DataSource bundleOrderItemDS) {
                                orderItemPresenter = new OrderItemPresenter(((OrderDisplay) getDisplay()).getOrderItemsDisplay());
                                ((OrderItemPresenter) orderItemPresenter).setDataSource((ListGridDataSource) orderItemDS, (ListGridDataSource) bundleOrderItemDS, new String[] { "name", "quantity", "price", "retailPrice", "salePrice" }, new Boolean[] { false, false, false, false, false });
                                orderItemPresenter.setReadOnly(true);
                                CountryListDataSourceFactory.createDataSource("countryDS", new AsyncCallbackAdapter() {

                                    public void onSuccess(final DataSource countryDS) {
                                        ((ListGridDataSource) countryDS).resetFieldVisibility("abbreviation", "name");
                                        final EntitySearchDialog countrySearchView = new EntitySearchDialog((ListGridDataSource) countryDS);
                                        ((DynamicEntityDataSource) ((OrderDisplay) getDisplay()).getListDisplay().getGrid().getDataSource()).getFormItemCallbackHandlerManager().addSearchFormItemCallback("address.country", countrySearchView, "Search For A Country", ((OrderDisplay) getDisplay()).getFulfillmentGroupDisplay().getGrid(), (DynamicFormDisplay) ((OrderDisplay) getDisplay()).getFulfillmentGroupDisplay());
                                        StateListDataSourceFactory.createDataSource("stateDS", new AsyncCallbackAdapter() {

                                            public void onSuccess(final DataSource stateDS) {
                                                ((ListGridDataSource) stateDS).resetFieldVisibility("abbreviation", "name");
                                                final EntitySearchDialog stateSearchView = new EntitySearchDialog((ListGridDataSource) stateDS);
                                                ((DynamicEntityDataSource) ((OrderDisplay) getDisplay()).getListDisplay().getGrid().getDataSource()).getFormItemCallbackHandlerManager().addSearchFormItemCallback("address.state", stateSearchView, "Search For A State", ((OrderDisplay) getDisplay()).getFulfillmentGroupDisplay().getGrid(), (DynamicFormDisplay) ((OrderDisplay) getDisplay()).getFulfillmentGroupDisplay());
                                                FulfillmentGroupListDataSourceFactory.createDataSource("fulfillmentGroupDS", new AsyncCallbackAdapter() {

                                                    public void onSuccess(DataSource fgDS) {
                                                        fulfillmentGroupPresenter = new SubPresenter(((OrderDisplay) getDisplay()).getFulfillmentGroupDisplay());
                                                        ((SubPresenter) fulfillmentGroupPresenter).setDataSource((ListGridDataSource) fgDS, new String[] { "referenceNumber", "method", "service", "shippingPrice", "status", "address.postalCode" }, new Boolean[] { false, false, false, false, false, false });
                                                        fulfillmentGroupPresenter.setReadOnly(true);
                                                        PaymentInfoListDataSourceFactory.createDataSource("paymentInfoDS", new AsyncCallbackAdapter() {

                                                            public void onSuccess(DataSource paymentInfoDS) {
                                                                paymentInfoPresenter = new SubPresenter(((OrderDisplay) getDisplay()).getPaymentInfoDisplay());
                                                                ((SubPresenter) paymentInfoPresenter).setDataSource((ListGridDataSource) paymentInfoDS, new String[] { "referenceNumber", "type", "amount" }, new Boolean[] { false, false, false });
                                                                paymentInfoPresenter.setReadOnly(true);
                                                                ((DynamicEntityDataSource) ((OrderDisplay) getDisplay()).getPaymentInfoDisplay().getGrid().getDataSource()).getFormItemCallbackHandlerManager().addSearchFormItemCallback("address.country", countrySearchView, "Search For A Country", ((OrderDisplay) getDisplay()).getFulfillmentGroupDisplay().getGrid(), (DynamicFormDisplay) ((OrderDisplay) getDisplay()).getFulfillmentGroupDisplay());
                                                                ((DynamicEntityDataSource) ((OrderDisplay) getDisplay()).getPaymentInfoDisplay().getGrid().getDataSource()).getFormItemCallbackHandlerManager().addSearchFormItemCallback("address.state", stateSearchView, "Search For A State", ((OrderDisplay) getDisplay()).getFulfillmentGroupDisplay().getGrid(), (DynamicFormDisplay) ((OrderDisplay) getDisplay()).getFulfillmentGroupDisplay());
                                                                PaymentAdditionalAttributesDataSourceFactory.createDataSource("paymentAdditionalAttributesDS", ((OrderDisplay) getDisplay()).getAdditionalAttributesDisplay().getGrid(), new AsyncCallbackAdapter() {

                                                                    public void onSuccess(DataSource attributeDS) {
                                                                        Map<String, Object> initialValues = new HashMap<String, Object>();
                                                                        initialValues.put("key", "Untitled");
                                                                        initialValues.put("value", "Untitled");
                                                                        additionalPaymentAttributesPresenter = new MapStructurePresenter(((OrderDisplay) getDisplay()).getAdditionalAttributesDisplay(), new MapStructureEntityEditDialog(), "Add New Attribute", initialValues);
                                                                        additionalPaymentAttributesPresenter.setDataSource((ListGridDataSource) attributeDS, new String[] { "key", "value" }, new Boolean[] { true, true });
                                                                        additionalPaymentAttributesPresenter.setReadOnly(true);
                                                                        OfferCodeListDataSourceFactory.createDataSource("offerCodeDS", new AsyncCallbackAdapter() {

                                                                            public void onSuccess(DataSource attributeDS) {
                                                                                offerCodePresenter = new SubPresenter(((OrderDisplay) getDisplay()).getOfferCodeDisplay());
                                                                                ((SubPresenter) offerCodePresenter).setDataSource((ListGridDataSource) attributeDS, new String[] { "offerCode", "startDate", "endDate", "offer.name", "offer.type", "offer.value" }, new Boolean[] { false, false, false, false, false, false });
                                                                                offerCodePresenter.setReadOnly(true);
                                                                                OrderAdjustmentListDataSourceFactory.createDataSource("orderAdjustmentDS", new AsyncCallbackAdapter() {

                                                                                    public void onSuccess(DataSource orderAdjustmentDS) {
                                                                                        orderAdjustmentPresenter = new CreateBasedListStructurePresenter(((OrderDisplay) getDisplay()).getOrderAdjustmentDisplay(), "Add An Order Ajustment");
                                                                                        ((CreateBasedListStructurePresenter) orderAdjustmentPresenter).setDataSource((ListGridDataSource) orderAdjustmentDS, new String[] { "reason", "value", "offer.name", "offer.type" }, new Boolean[] { false, false, false, false });
                                                                                        orderAdjustmentPresenter.setReadOnly(true);
                                                                                        OrderItemAdjustmentListDataSourceFactory.createDataSource("orderItemAdjustmentDS", new AsyncCallbackAdapter() {

                                                                                            public void onSuccess(DataSource orderItemAdjustmentDS) {
                                                                                                orderItemAdjustmentPresenter = new CreateBasedListStructurePresenter(((OrderDisplay) getDisplay()).getOrderItemAdjustmentDisplay(), "Add An Order Item Adjustment");
                                                                                                ((CreateBasedListStructurePresenter) orderItemAdjustmentPresenter).setDataSource((ListGridDataSource) orderItemAdjustmentDS, new String[] { "reason", "value", "offer.type" }, new Boolean[] { false, false, false });
                                                                                                orderItemAdjustmentPresenter.setReadOnly(true);
                                                                                                FulfillmentGroupAdjustmentListDataSourceFactory.createDataSource("fulfillmentGroupAdjustmentDS", new AsyncCallbackAdapter() {

                                                                                                    public void onSuccess(DataSource fulfillmentGroupAdjustmentDS) {
                                                                                                        fulfillmentGroupAdjustmentPresenter = new CreateBasedListStructurePresenter(((OrderDisplay) getDisplay()).getFulfillmentGroupAdjustmentDisplay(), "Add A Fulfillment Group Adjustment");
                                                                                                        ((CreateBasedListStructurePresenter) fulfillmentGroupAdjustmentPresenter).setDataSource((ListGridDataSource) fulfillmentGroupAdjustmentDS, new String[] { "reason", "value", "offer.type" }, new Boolean[] { false, false, false });
                                                                                                        fulfillmentGroupAdjustmentPresenter.setReadOnly(true);
                                                                                                        OrderPresenter.super.go(container);
                                                                                                        Main.NON_MODAL_PROGRESS.stopProgress();
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}
