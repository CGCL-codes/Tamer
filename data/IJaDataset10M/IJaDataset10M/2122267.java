package org.jacp.demo.entity;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

public class ContactDTO {

    private String parentName;

    private int amount;

    private ObservableList<Contact> contacts = FXCollections.observableArrayList();

    private List<XYChart.Data<String, Number>> seriesOneData;

    private List<XYChart.Data<String, Number>> seriesTwoData;

    private List<XYChart.Data<String, Number>> seriesThreeData;

    private List<XYChart.Data<String, Number>> seriesFourData;

    public ContactDTO(String parentName, int amount) {
        this.parentName = parentName;
        this.amount = amount;
    }

    public ContactDTO() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ObservableList<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<XYChart.Data<String, Number>> getSeriesOneData() {
        return seriesOneData;
    }

    public void setSeriesOneData(List<XYChart.Data<String, Number>> seriesOneData) {
        this.seriesOneData = seriesOneData;
    }

    public List<XYChart.Data<String, Number>> getSeriesTwoData() {
        return seriesTwoData;
    }

    public void setSeriesTwoData(List<XYChart.Data<String, Number>> seriesTwoData) {
        this.seriesTwoData = seriesTwoData;
    }

    public List<XYChart.Data<String, Number>> getSeriesThreeData() {
        return seriesThreeData;
    }

    public void setSeriesThreeData(List<XYChart.Data<String, Number>> seriesThreeData) {
        this.seriesThreeData = seriesThreeData;
    }

    public List<XYChart.Data<String, Number>> getSeriesFourData() {
        return seriesFourData;
    }

    public void setSeriesFourData(List<XYChart.Data<String, Number>> seriesFourData) {
        this.seriesFourData = seriesFourData;
    }
}
