package com.cti.product.workflows;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.jlf.log.*;
import org.jlf.dataMap.*;
import org.jlf.httpRequest.*;
import com.cti.product.*;

/**
 * This class processes work steps to maintain
 * Product objects.
 *
 */
public class MaintainProducts extends Workflow {

    /**
     * View all products in the database.  Stores the
     * vector of all Products in the request under the
     * name "products".
     */
    public void viewAll() {
        Vector allProducts = (new Product()).findAll();
        getRequest().setAttribute("products", allProducts);
    }

    /**
     * View one product in the database.  Finds and updates the
     * product in the HTTP request under the name
     * name "product".
     */
    public void viewOne() {
        Product product = (Product) getSession().getValue("product");
        product = (Product) product.findByPrimaryKey();
        getSession().putValue("product", product);
    }

    /**
     * Creates a product given one in the session under
     * the name "product".
     */
    public void create() {
        Product product = (Product) getRequest().getAttribute("product");
        product.setProductType(ProductType.findById(Long.parseLong(getRequest().getParameter("product.productTypeId"))));
        DataMapper dataMapper = null;
        try {
            dataMapper = product.getDefaultDataMapper();
            product.write(dataMapper);
            dataMapper.commitWrites();
        } finally {
            if (dataMapper != null) dataMapper.close();
        }
        getSession().putValue("product", product);
    }

    /**
     * Updates a product given one in the session under
     * the name "product".
     */
    public void update() {
        Product product = (Product) getSession().getValue("product");
        DataMapper dataMapper = null;
        try {
            dataMapper = product.getDefaultDataMapper();
            product.write(dataMapper);
            dataMapper.commitWrites();
        } finally {
            if (dataMapper != null) dataMapper.close();
        }
    }

    /**
     * Deletes a product given one in the session under
     * the name "product".
     */
    public void delete() {
        Product product = (Product) getSession().getValue("product");
        DataMapper dataMapper = null;
        try {
            dataMapper = product.getDefaultDataMapper();
            product.deleteOnWrite();
            product.write(dataMapper);
            dataMapper.commitWrites();
        } finally {
            if (dataMapper != null) dataMapper.close();
        }
    }
}
