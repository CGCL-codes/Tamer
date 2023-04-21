package com.requestApplication.client.view;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.*;
import java.util.LinkedHashMap;

public class CreateRequestDisplay extends VStack {

    private Button saveButton;

    private Button cancelButton;

    private TextItem clientLastName;

    private TextItem productCod;

    private SpinnerItem countItem;

    private TextAreaItem comment;

    private ComboBoxItem assignedTo;

    private TextItem targetUserName;

    private TextItem targetUserLastName;

    private SpinnerItem costItem;

    private TextItem clientName;

    public CreateRequestDisplay() {
        init();
    }

    protected void init() {
        SectionStack mainPanel = new SectionStack();
        mainPanel.setHeight("100%");
        mainPanel.setShowResizeBar(false);
        mainPanel.setVisibilityMode(VisibilityMode.MULTIPLE);
        mainPanel.setAnimateSections(false);
        SectionStackSection menuSection = new SectionStackSection("Nuevo Pedido");
        menuSection.setExpanded(true);
        VStack main = new VStack();
        mainPanel.setSections(menuSection);
        saveButton = new Button("Guardar");
        cancelButton = new Button("Cancelar");
        final DynamicForm form = new DynamicForm();
        clientName = new TextItem();
        clientName.setTitle("Nombre");
        clientName.setHint("<nobr>Nombre del cliente</nobr>");
        clientLastName = new TextItem();
        clientLastName.setTitle("Apellido");
        clientLastName.setHint("<nobr>Apellido del cliente</nobr>");
        productCod = new TextItem();
        productCod.setTitle("Codigo");
        productCod.setHint("<nobr>Codigo del producto</nobr>");
        countItem = new SpinnerItem();
        countItem.setTitle("Cantidad");
        countItem.setDefaultValue(1);
        countItem.setMin(0);
        comment = new TextAreaItem();
        comment.setTitle("Comentario");
        comment.setHint("<nobr>Comentario</nobr>");
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put("wonky@mundoelefante.com", "<b>Wonky</b>");
        valueMap.put("paula@mundoelefante.com", "<b>Pau</b>");
        assignedTo = new ComboBoxItem();
        assignedTo.setTitle("Asigando");
        assignedTo.setHint("<nobr>Quien va a hacer el pedido.</nobr>");
        assignedTo.setType("comboBox");
        assignedTo.setValueMap(valueMap);
        targetUserName = new TextItem();
        targetUserName.setTitle("Para (Nombre)");
        targetUserName.setHint("<nobr>Nombre del destinatario</nobr>");
        targetUserLastName = new TextItem();
        targetUserLastName.setTitle("Para (Apellido)");
        targetUserLastName.setHint("<nobr>Apellido del destinatario</nobr>");
        costItem = new SpinnerItem();
        costItem.setTitle("Importe");
        costItem.setDefaultValue(1);
        costItem.setMin(0);
        form.setItems(clientName, clientLastName, productCod, countItem, comment, assignedTo, targetUserName, targetUserLastName, costItem);
        HStack okButtonContainer = new HStack(10);
        okButtonContainer.setAlign(Alignment.CENTER);
        okButtonContainer.addMember(saveButton);
        okButtonContainer.addMember(cancelButton);
        okButtonContainer.setHeight(20);
        main.addMember(form);
        main.addMember(okButtonContainer);
        menuSection.setItems(main);
        addMember(mainPanel);
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public TextItem getClientLastName() {
        return clientLastName;
    }

    public TextItem getProductCod() {
        return productCod;
    }

    public SpinnerItem getCountItem() {
        return countItem;
    }

    public TextAreaItem getComment() {
        return comment;
    }

    public ComboBoxItem getAssignedTo() {
        return assignedTo;
    }

    public TextItem getTargetUserName() {
        return targetUserName;
    }

    public TextItem getTargetUserLastName() {
        return targetUserLastName;
    }

    public SpinnerItem getCostItem() {
        return costItem;
    }

    public TextItem getClientName() {
        return clientName;
    }
}
