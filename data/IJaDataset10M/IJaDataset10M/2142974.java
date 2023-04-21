package com.prossys.util;

import com.prossys.dao.*;
import com.prossys.model.*;
import com.prossys.facade.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Test class
 * @author Victor
 */
public class Test {

    public void insertType() {
        try {
            Type newType = new Type("name", "description");
            new AdministratorDAO().insertType(newType);
            Type type = new AdministratorDAO().searchTypeById(1);
            List<Type> typeList = type.getTypes();
            typeList.add(newType);
            new AdministratorDAO().updateType(type);
        } catch (PersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void printType(Type type, int level) {
        if (type.getTypes().isEmpty()) {
            System.out.println(type + "Level = " + level);
            return;
        }
        Iterator it = type.getTypes().iterator();
        System.out.println(type + "Level = " + level);
        level++;
        while (it.hasNext()) {
            Type thisType = (Type) it.next();
            printType(thisType, level);
        }
    }

    public List<Type> listTypes = new ArrayList();

    public List<Type> orderTypes(Type type, Integer level) {
        if (type.getTypes().isEmpty()) {
            type.setLevel(level);
            listTypes.add(type);
            return listTypes;
        }
        Iterator it = type.getTypes().iterator();
        type.setLevel(level);
        listTypes.add(type);
        level++;
        while (it.hasNext()) {
            Type thisType = (Type) it.next();
            orderTypes(thisType, level);
        }
        return listTypes;
    }

    public void listTypes() {
        try {
            AdministratorDAO dao = new AdministratorDAO();
            Type root = dao.searchTypeById(1);
            List<Type> types = orderTypes(root, 1);
            Collections.sort(types, new Comparator<Type>() {

                public int compare(Type type1, Type type2) {
                    return type1.getName().compareToIgnoreCase(type2.getName());
                }
            });
            Iterator it = types.iterator();
            while (it.hasNext()) {
                Type thisType = (Type) it.next();
                System.out.println(thisType.getName() + " Level: " + thisType.getLevel());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void listTypes2() {
        try {
            AdministratorDAO dao = new AdministratorDAO();
            Type root = dao.searchTypeById(1);
            List<Type> types = orderTypes(root, 1);
            Collections.sort(types, new Comparator<Type>() {

                public int compare(Type type1, Type type2) {
                    return type1.getName().compareToIgnoreCase(type2.getName());
                }
            });
            Iterator it = types.iterator();
            while (it.hasNext()) {
                Type thisType = (Type) it.next();
                System.out.println(thisType.getName() + " Level: " + thisType.getLevel());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void listPricesOrdered() {
        try {
            Customer customer = new Customer();
            customer.setName("Customer 1");
            customer.setCpf("0001");
            customer.setUsername("username");
            customer.setPassword("password");
            new CustomerDAO().insertCustomer(customer);
        } catch (PersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void insertRate() {
        try {
            Rate rate = new Rate(new RatePk(new CompanyDAO().searchProductById(1), new CustomerDAO().searchCustomerById(1)), 9F, "comment");
            new CustomerDAO().insertRate(rate);
        } catch (PersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void usernameInUse() {
        try {
            if (new CompanyDAO().isUsernameInUse("aline")) System.out.println("Username já em uso."); else System.out.println("Username ainda não registrado.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getCompany() {
        try {
            User user = new User();
            user.setUsername("mcdonaqlds");
            user.setPassword("password");
            user = new AdministratorFacade().login(user);
            if (user == null) {
                System.out.println("Usuario nao cadastrado");
                return;
            }
            if (user.getClass().isAssignableFrom(Company.class)) {
                Company company = (Company) user;
                System.out.println("Nome da empresa: " + company.getCompanyName());
            } else {
                System.out.println("Class: " + user.getClass().getSimpleName());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void function1() {
        try {
            User user = new User();
            user.setUsername("mcdonalds");
            user.setPassword("password");
            user = new AdministratorFacade().login(user);
            if (user.getClass().isAssignableFrom(Company.class)) {
                Company company = (Company) user;
                company.setTradingName("Mc Café");
                new CompanyFacade().updateCompanyData(company);
            } else {
                System.out.println("Class: " + user.getClass().getSimpleName());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void registerCompany() {
        try {
            Company company = new Company();
            company.setUsername("mcdonalds");
            company.setPassword("password");
            company.setTradingName("Mc Donalds");
            company.setCompanyName("Mc Donalds");
            company.setCnpj("3831928120921");
            new CompanyFacade().requestCompanyValidation(company);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addProduct() {
        try {
            User user = new User();
            user.setUsername("mcdonalds");
            user.setPassword("password");
            user = new AdministratorFacade().login(user);
            Company company = (Company) user;
            Type type = new AdministratorDAO().searchTypeById(1);
            Product product = new Product();
            product.setCompany(company);
            product.setName("Sorvete");
            product.setType(type);
            product.setDescription("delicioso");
            new CompanyFacade().addProduct(product);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void removeProduct() {
        try {
            Product product = new CompanyDAO().searchProductById(2);
            System.out.println("Empresa do produto: " + product.getCompany().getCompanyName());
            new CompanyFacade().removeProduct(product);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void updateProduct() {
        try {
            Product product = new CompanyDAO().searchProductById(5);
            System.out.println("Empresa do produto: " + product.getCompany().getCompanyName());
            product.setName("Novo nome da empresa");
            new CompanyFacade().updateProduct(product);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void listProduct() {
        try {
            User user = new User();
            user.setUsername("mcdonalds");
            user.setPassword("password");
            user = new AdministratorFacade().login(user);
            Company company = (Company) user;
            Iterator it = new CompanyFacade().getListOfProducts(company).iterator();
            while (it.hasNext()) {
                Product product = (Product) it.next();
                System.out.println("Nome Produto: " + product.getName());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void insertStore() {
        try {
            User user = new User();
            user.setUsername("mcdonalds");
            user.setPassword("password");
            user = new AdministratorFacade().login(user);
            Company company = (Company) user;
            Store store = new Store();
            store.setUsername("magazine2");
            store.setPassword("password");
            store.setCnpj("4209422402240");
            store.setCompany(company);
            store.setTradingName("Magazine2");
            new CompanyFacade().insertStore(store);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void removeStore() {
        try {
            User user = new User();
            user.setUsername("magazine2");
            user.setPassword("password");
            user = new AdministratorFacade().login(user);
            Store store = (Store) user;
            new CompanyFacade().removeStore(store);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void listStore() {
        try {
            User user = new User();
            user.setUsername("mcdonalds");
            user.setPassword("password");
            user = new AdministratorFacade().login(user);
            Company company = (Company) user;
            Iterator it = new CompanyFacade().getListOfStores(company).iterator();
            while (it.hasNext()) {
                Store store = (Store) it.next();
                System.out.println("Nome Store: " + store.getTradingName());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void insertPrice() {
        try {
            User user = new User();
            user.setUsername("magazine");
            user.setPassword("password");
            user = new AdministratorFacade().login(user);
            Store store = (Store) user;
            Product product = new CompanyDAO().searchProductById(5);
            Price price = new Price(new PricePk(store, product), 17.70F);
            new StoreFacade().insertProductAndPriceOfStore(price);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getPrice() {
        try {
            User user = new User();
            user.setUsername("estabelecimento");
            user.setPassword("admin");
            user = new AdministratorFacade().login(user);
            Store store = (Store) user;
            Product product = new CompanyDAO().searchProductById(5);
            Price price = new StoreDAO().getPriceById(new PricePk(store, product));
            System.out.println("Preço: " + price.getPrice());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void updateStore() {
        try {
            User user = new User();
            user.setUsername("magazine");
            user.setPassword("password");
            user = new AdministratorFacade().login(user);
            Store store = (Store) user;
            store.setTradingName("New Trading name");
            new StoreFacade().updateStoreData(store);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void listPrices() {
        try {
            User user = new User();
            user.setUsername("estabelecimento");
            user.setPassword("admin");
            user = new AdministratorFacade().login(user);
            Store store = (Store) user;
            List<Price> list = new StoreFacade().getListOfStoreProducts(store);
            new CustomerFacade().orderPricesByPrice(list);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Price price = (Price) it.next();
                System.out.println("Nome produto: " + price.getId().getProduct().getName());
                System.out.println("Preco produto: " + price.getPrice());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void listAvailableProducts() {
        try {
            User user = new User();
            user.setUsername("estabelecimento");
            user.setPassword("admin");
            user = new AdministratorFacade().login(user);
            Store store = (Store) user;
            Iterator it = new StoreFacade().getListOfAvailableProducts(store).iterator();
            while (it.hasNext()) {
                Product product = (Product) it.next();
                System.out.println("Nome produto: " + product.getName());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void listProducts() {
        try {
            Type type = new AdministratorDAO().searchTypeById(17);
            Iterator it = new CustomerFacade().getPricesByType(type).iterator();
            while (it.hasNext()) {
                Price price = (Price) it.next();
                System.out.println("Nome produto: " + price.getId().getProduct().getName() + " Preço: " + price.getPrice());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void removePrice() {
        try {
            User user = new User();
            user.setUsername("magazine");
            user.setPassword("password");
            user = new AdministratorFacade().login(user);
            Store store = (Store) user;
            Iterator it = new StoreFacade().getListOfStoreProducts(store).iterator();
            while (it.hasNext()) {
                Price price = (Price) it.next();
                if (price.getId().getProduct().getName().equals("Casquinha")) {
                    new StoreFacade().removePrice(price);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Test().listPrices();
    }
}
