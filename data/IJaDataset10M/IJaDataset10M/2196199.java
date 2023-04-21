package wicket.in.action.chapter13.dbdiscounts.services;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import wicket.in.action.chapter13.dbdiscounts.domain.Cheese;
import wicket.in.action.chapter13.dbdiscounts.domain.Discount;
import wicket.in.action.chapter13.dbdiscounts.domain.User;

public interface DiscountsService {

    <T> T load(Class<T> type, long id);

    List<Cheese> findAllCheeses();

    List<User> findAllUsers();

    List<Discount> findAllDiscounts();

    void saveCheese(Cheese cheese);

    void saveUser(User user);

    void saveDiscount(Discount discount);

    @Transactional
    void saveDiscounts(List<Discount> discounts);

    void deleteCheese(Cheese cheese);

    void deleteUser(User user);

    void deleteDiscount(Discount discount);
}
