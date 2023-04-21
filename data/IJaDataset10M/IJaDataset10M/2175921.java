package com.googlecode.projeto1.client.panels.manage.poi;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.projeto1.client.beans.Type;
import com.googlecode.projeto1.client.panels.maps.LineMap;
import com.googlecode.projeto1.client.panels.maps.PointMap;
import com.googlecode.projeto1.client.panels.maps.PolygonMap;
import com.gwtext.client.widgets.MessageBox;

public class PoiTab extends AbsolutePanel {

    private RadioButton rdbtnEscola;

    private RadioButton rdbtnUniversidade;

    private RadioButton rdbtnViaPrincipalDe;

    private RadioButton rdbtnAreaVerde;

    private RadioButton rdbtnShoppingCenter;

    private RadioButton rdbtnSetorIndustrial;

    public PoiTab() {
        super();
        HTML explanationHtml = new HTML("New HTML", true);
        explanationHtml.setText("Escolha o tipo de ponto de interesse você deseja adicionar ou remover");
        this.add(explanationHtml, 5, 5);
        rdbtnEscola = new RadioButton("POI", "Escola");
        this.add(rdbtnEscola, 5, 54);
        rdbtnUniversidade = new RadioButton("POI", "Universidade");
        this.add(rdbtnUniversidade, 5, 86);
        rdbtnViaPrincipalDe = new RadioButton("POI", "Via Principal de Acesso");
        this.add(rdbtnViaPrincipalDe, 5, 118);
        rdbtnAreaVerde = new RadioButton("POI", "Area Verde");
        this.add(rdbtnAreaVerde, 5, 150);
        rdbtnShoppingCenter = new RadioButton("POI", "Shopping Center");
        this.add(rdbtnShoppingCenter, 5, 182);
        rdbtnSetorIndustrial = new RadioButton("POI", "Setor Industrial");
        this.add(rdbtnSetorIndustrial, 5, 214);
        this.add(getCreateButton(), 5, 246);
        this.add(getRemoveButton(), 110, 246);
    }

    private void showSavePointMap(Type type, Widget widget) {
        PointMap map = new PointMap(type);
        map.setSavePointMap();
        map.show(widget.getElement());
    }

    private void showRemovePointMap(Type type, Widget widget) {
        PointMap map = new PointMap(type);
        map.setRemovePointMap();
        map.show(widget.getElement());
    }

    private void showSaveLineMap(Type type, Widget widget) {
        LineMap map = new LineMap(type);
        map.setSaveLineMap();
        map.show(widget.getElement());
    }

    private void showRemoveLineMap(Type type, Widget widget) {
        LineMap map = new LineMap(type);
        map.setRemoveLineMap();
        map.show(widget.getElement());
    }

    private void showSaveAreaMap(Type type, Widget widget) {
        PolygonMap map = new PolygonMap(type);
        map.setSavePolygonMap();
        map.show(widget.getElement());
    }

    private void showRemoveAreaMap(Type type, Widget widget) {
        PolygonMap map = new PolygonMap(type);
        map.setRemovePolygonMap();
        map.show(widget.getElement());
    }

    private Button getCreateButton() {
        Button button = new Button("Criar");
        button.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                if (rdbtnEscola.isChecked()) {
                    showSavePointMap(Type.SCHOOL, arg0);
                } else if (rdbtnUniversidade.isChecked()) {
                    showSavePointMap(Type.UNIVERSITY, arg0);
                } else if (rdbtnViaPrincipalDe.isChecked()) {
                    showSaveLineMap(Type.ACCESS_ROAD, arg0);
                } else if (rdbtnShoppingCenter.isChecked()) {
                    showSavePointMap(Type.SHOPPING_CENTER, arg0);
                } else if (rdbtnAreaVerde.isChecked()) {
                    showSaveAreaMap(Type.GREEN_AREA, arg0);
                } else if (rdbtnSetorIndustrial.isChecked()) {
                    showSaveAreaMap(Type.INDUSTRIAL, arg0);
                } else {
                    MessageBox.alert("Favor selecionar alguma das opções");
                }
            }
        });
        button.setSize("100px", "34px");
        return button;
    }

    private Button getRemoveButton() {
        Button button = new Button("Remover");
        button.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                if (rdbtnEscola.isChecked()) {
                    showRemovePointMap(Type.SCHOOL, arg0);
                } else if (rdbtnUniversidade.isChecked()) {
                    showRemovePointMap(Type.UNIVERSITY, arg0);
                } else if (rdbtnViaPrincipalDe.isChecked()) {
                    showRemoveLineMap(Type.ACCESS_ROAD, arg0);
                } else if (rdbtnShoppingCenter.isChecked()) {
                    showRemovePointMap(Type.SHOPPING_CENTER, arg0);
                } else if (rdbtnAreaVerde.isChecked()) {
                    showRemoveAreaMap(Type.GREEN_AREA, arg0);
                } else if (rdbtnSetorIndustrial.isChecked()) {
                    showRemoveAreaMap(Type.INDUSTRIAL, arg0);
                } else {
                    MessageBox.alert("Favor selecionar alguma das opções");
                }
            }
        });
        button.setSize("100px", "34px");
        return button;
    }
}
