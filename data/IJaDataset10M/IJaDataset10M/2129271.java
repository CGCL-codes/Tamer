package org.cmsuite2.business.form;

import java.util.List;
import org.cmsuite2.model.customer.Customer;
import org.cmsuite2.model.employee.Employee;
import org.cmsuite2.model.payment.Payment;
import org.cmsuite2.model.product.Product;
import org.cmsuite2.model.product.ProductItem;
import org.cmsuite2.model.quote.Quote;
import org.cmsuite2.model.quote.QuoteRate;
import org.cmsuite2.model.store.Store;
import org.cmsuite2.model.vector.Vector;

public class QuoteForm {

    private Quote quote;

    private QuoteRate quoteRate;

    private long number;

    private long customerId;

    private long employeeId;

    private long paymentId;

    private long storeId;

    private long vectorId;

    private List<Customer> customers;

    private List<Employee> employees;

    private List<Payment> payments;

    private List<Product> availableProducts;

    private List<ProductItem> productItems;

    private List<Store> stores;

    private List<Vector> vectors;

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public QuoteRate getQuoteRate() {
        return quoteRate;
    }

    public void setQuoteRate(QuoteRate quoteRate) {
        this.quoteRate = quoteRate;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getVectorId() {
        return vectorId;
    }

    public void setVectorId(long vectorId) {
        this.vectorId = vectorId;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Product> getAvailableProducts() {
        return availableProducts;
    }

    public void setAvailableProducts(List<Product> availableProducts) {
        this.availableProducts = availableProducts;
    }

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<Vector> getVectors() {
        return vectors;
    }

    public void setVectors(List<Vector> vectors) {
        this.vectors = vectors;
    }
}
