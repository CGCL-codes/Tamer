package com.sanctuary.services;

import java.util.HashMap;
import java.util.Map;
import com.sanctuary.interfaces.Product;
import com.sanctuary.products.HtmlProduct;

public class PrintException extends BaseExceptionService {

    private String productArg;

    public PrintException(String productArg) {
        this.productArg = productArg;
    }

    @Override
    public Product execute(Exception ex) throws Exception {
        Product p = new HtmlProduct();
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("exception", ex);
        p.setWriter(writer);
        p.setCompleted(productArg, request, response, template, content);
        return p;
    }

    public boolean isWriter() {
        return true;
    }
}
