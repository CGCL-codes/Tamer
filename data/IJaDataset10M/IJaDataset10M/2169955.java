package data;

import java.util.List;
import logic.Product;
import logic.User;

public interface IFinderDAO {

    List<Product> lastProductsFind(Integer n);

    List<List<Product>> defaultFind(User u);

    List<Product> newFind(List<String> l);

    List<Product> randomFind();

    List<String> getCategories();
}
