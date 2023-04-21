package net.sf.brightside.overlord.implementations;

import java.util.ArrayList;
import net.sf.brightside.overlord.domain.Client;
import net.sf.brightside.overlord.domain.InspReport;
import net.sf.brightside.overlord.domain.Inspector;
import net.sf.brightside.overlord.domain.Laboratory;
import net.sf.brightside.overlord.domain.Product;

public class InspReportImpl implements InspReport {

    private ArrayList<Product> products;

    private Client client;

    private Laboratory laboratory;

    private Inspector inspector;

    private String conclusion;

    private int productIndex = 0;

    public InspReportImpl() {
        products = new ArrayList<Product>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Client getClient() {
        return client;
    }

    public String getConclusion() {
        return conclusion;
    }

    public Inspector getInspector() {
        return inspector;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public Product getProduct() {
        productIndex++;
        return products.get(productIndex - 1);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public void setInspector(Inspector inspector) {
        this.inspector = inspector;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }
}
