package net.sf.brightside.bonko.usecases.getProductByName;

import java.util.List;
import net.sf.brightside.bonko.facade.GetFacade;
import net.sf.brightside.bonko.metamodel.Customer;
import net.sf.brightside.bonko.metamodel.Product;

public class GetProductByNameCommandImpl implements GetProductByNameCommand {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private GetFacade getFacade;

    public GetFacade getGetFacade() {
        return getFacade;
    }

    public void setGetFacade(GetFacade getFacade) {
        this.getFacade = getFacade;
    }

    public List<Product> execute() {
        return getFacade.getByName(Product.class, name);
    }
}
