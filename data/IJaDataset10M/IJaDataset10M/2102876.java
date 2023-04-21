package br.gov.sample.demoiselle.auction5.business;

import java.util.List;
import br.gov.framework.demoiselle.core.layer.IBusinessController;
import br.gov.sample.demoiselle.auction5.bean.Category;
import br.gov.sample.demoiselle.auction5.bean.Item;

public interface IItemBC extends IBusinessController {

    public Item save(Item item);

    public void delete(Item item);

    public List<Item> filterByCategory(Item item);

    public List<Category> listAvailableCategories();
}
