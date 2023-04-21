package antifetto.food.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import antifetto.food.domain.BasicFood;
import antifetto.food.domain.Source;
import antifetto.food.web.FoodSearchCriteria;

public class FoodDao extends HibernateDaoSupport {

    public BasicFood getFood(Long id) {
        return (BasicFood) getHibernateTemplate().get(BasicFood.class, id);
    }

    public List<BasicFood> findFoodByMaxCalories(float maxCalories) {
        return getHibernateTemplate().find("from BasicFood where calories < ?", maxCalories);
    }

    public List<BasicFood> findFoodByName(FoodSearchCriteria foodCriteria) {
        return getHibernateTemplate().find("from BasicFood where name = ?", foodCriteria.getName());
    }

    public List<BasicFood> findFoodByCriteria(FoodSearchCriteria foodCriteria) {
        String sQuery = "from BasicFood where id = id";
        if (foodCriteria.getName().compareTo("") != 0) {
            sQuery = sQuery + " and name = '" + foodCriteria.getName() + "'";
        }
        if (foodCriteria.getCalories() != null) {
            sQuery = sQuery + " and calories < " + foodCriteria.getCalories();
        }
        if (foodCriteria.getProtein() != null) {
            sQuery = sQuery + " and protein < " + foodCriteria.getProtein();
        }
        if (foodCriteria.getCarbs() != null) {
            sQuery = sQuery + " and carbs < " + foodCriteria.getCarbs();
        }
        if (foodCriteria.getFat() != null) {
            sQuery = sQuery + " and fat < " + foodCriteria.getFat();
        }
        return getHibernateTemplate().find(sQuery);
    }

    public void saveFood(BasicFood food) {
        getHibernateTemplate().saveOrUpdate(food);
    }

    public void saveSource(Source source) {
        getHibernateTemplate().saveOrUpdate(source);
    }
}
