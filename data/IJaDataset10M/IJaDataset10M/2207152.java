package nl.hu.verbrandendephoenix.view;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

public class AddAuction {

    private Grid panelGrid = new Grid(6, 2);

    private Label title = new Label("Nieuwe veiling");

    private Label lab1 = new Label("Naam voor veiling:");

    private Label lab2 = new Label("Categorie:");

    private Label lab3 = new Label("Korte beschrijving product:");

    private Label lab4 = new Label("Minimum bod in credits:");

    private TextBox auctionName = new TextBox();

    private TextBox iniPrice = new TextBox();

    private ListBox category = new ListBox();

    private TextBox description = new TextBox();

    private Button confirm = new Button("Start veiling");

    public AddAuction() {
    }

    public Panel getAddAuctionForm() {
        title.setStyleName("h1");
        panelGrid.setWidget(0, 0, title);
        panelGrid.setWidget(1, 0, lab1);
        panelGrid.setWidget(2, 0, lab2);
        panelGrid.setWidget(3, 0, lab3);
        panelGrid.setWidget(4, 0, lab4);
        fillCategoryList();
        panelGrid.setWidget(1, 1, auctionName);
        panelGrid.setWidget(2, 1, category);
        panelGrid.setWidget(3, 1, description);
        panelGrid.setWidget(4, 1, iniPrice);
        panelGrid.setWidget(5, 1, confirm);
        return panelGrid;
    }

    public Button getButtonConfirm() {
        return confirm;
    }

    public TextBox getAuctName() {
        return auctionName;
    }

    public ListBox getCategory() {
        return category;
    }

    public TextBox getIniPrice() {
        return iniPrice;
    }

    public TextBox getDescription() {
        return description;
    }

    public void setAuctName(String text) {
        auctionName.setText(text);
    }

    public void setIniPrice(String text) {
        iniPrice.setText(text);
    }

    public void setDescription(String text) {
        description.setText(text);
    }

    public void resetCategory() {
        category.setItemSelected(0, true);
    }

    private void fillCategoryList() {
        category.addItem(null);
        category.addItem("Boeken");
        category.addItem("Drogist");
        category.addItem("Elektronica");
        category.addItem("Huishoudelijk");
        category.addItem("Meubilair");
        category.addItem("Multimedia");
        category.addItem("Voertuigen");
    }

    public void reset() {
        title.setText("Nieuwe veiling");
        lab1.setText("Naam voor veiling:");
        lab2.setText("Categorie:");
        lab3.setText("Korte beschrijving product:");
        lab4.setText("Minimum bod:");
        auctionName.setText("");
        iniPrice.setText("");
        category.setItemSelected(0, true);
        description.setText("");
    }
}
