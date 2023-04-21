package wicket.in.action.chapter13.dbdiscounts;

import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import wicket.in.action.chapter13.dbdiscounts.domain.Cheese;
import wicket.in.action.chapter13.dbdiscounts.domain.Discount;
import wicket.in.action.chapter13.dbdiscounts.domain.User;

public final class TestDataInitializer implements InitializingBean, ApplicationContextAware {

    private SessionFactory sessionFactory;

    private ApplicationContext context;

    public TestDataInitializer() {
    }

    public void afterPropertiesSet() throws Exception {
        LocalSessionFactoryBean sessionFactoryBean = findSessionFactoryBean(context);
        sessionFactoryBean.createDatabaseSchema();
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Cheese gouda = new Cheese("Gouda", "Gouda is a yellowish Dutch cheese named after the city of Gouda. The cheese is made from cow's milk. Exported Gouda is usually the young variety (aged between 1 and 6 months, rich yellow in colour and with a red or yellow paraffin wax coating). This cheese is easily sliced on bread.", 1.65);
            session.save(gouda);
            Cheese edam = new Cheese("Edam", "Edam (Dutch Edammer) is a Dutch cheese named after the town of Edam. Edam cheese has a very mild taste, slightly salty or nutty and almost no smell when compared to other cheeses. Mild Edam is good with fruit such as peaches, melons, apricots and cherries. Aged Edammer is good with traditional \"cheese fruits\" like pears and apples. Like most cheeses, it is also good on crackers and bread. Pinot Noir is a recommended wine to accompany this cheese.", 1.05);
            session.save(edam);
            Cheese maasdam = new Cheese("Maasdam", "Maasdam cheese is a Dutch cheese in a Swiss-style. Made from cow's milk, it is aged for at least 4 weeks. It ripens faster than other Dutch cheeses. Maasdam has internal holes from the ripening process, and a smooth yellow rind. Sometimes it is waxed like Gouda. It is nutty and sweet, but softer than Emmental due to a higher moisture content.", 2.35);
            session.save(maasdam);
            Cheese brie = new Cheese("Brie", "Brie is a soft cows' milk cheese named after Brie, the French province.  It is pale in colour with a slight greyish tinge under crusty white mould; very soft and savoury with a hint of ammonia. The white mouldy rind is moderately tasteful and edible.", 3.15);
            session.save(brie);
            Cheese buxtonBlue = new Cheese("Buxton Blue", "Buxton Blue cheese is an English blue cheese that is a close relative of Blue Stilton. It is made from cow's milk and is lightly veined with a deep russet colouring. It is usually made in a cylindrical shape. This cheese is complimented with a chilled glass of sweet dessert wine or ruby port.", 0.99);
            session.save(buxtonBlue);
            Cheese parmesan = new Cheese("Parmesan", "Parmesan is a grana, a hard, granular cheese, cooked but not pressed, named after the producing areas of Parma and Reggio Emilia, in Emilia-Romagna, Italy. Uses of the cheese include being grated over pasta, stirred into soup and risotto, and eaten in chunks with balsamic vinegar. It is also a key ingredient in alfredo sauce and pesto.", 1.99);
            session.save(parmesan);
            Cheese cheddar = new Cheese("Cheddar", "Cheddar cheese is a hard, pale yellow to orange, sharp-tasting cheese originally made in the English village of Cheddar, in Somerset.", 2.95);
            session.save(cheddar);
            Cheese roquefort = new Cheese("Roquefort", "Roquefort is a ewe's-milk blue cheese from the south of France, and is one of the most famous of all French cheeses. The cheese is white, crumbly and slightly moist, with distinctive veins of blue mold. It has characteristic odor and flavor with a notable taste of butyric acid; the blue veins provide a sharp tang. The overall flavor sensation begins slightly mild, then waxing sweet, then smoky, and fading to a salty finish.", 1.67);
            session.save(roquefort);
            Cheese boursin = new Cheese("Boursin", "Boursin Cheese is a soft creamy cheese available in a variety of flavors. Its flavor and texture is somewhat similar to American cream cheese.", 1.33);
            session.save(boursin);
            Cheese camembert = new Cheese("Camembert", "Camembert is a soft, creamy French cheese. When fresh, it is quite crumbly and relatively hard, but it characteristically ripens and becomes more runny and strongly flavoured as it ages. Camembert can be used in many dishes, but it is popularly eaten uncooked on bread or with wine or meat, to enjoy the subtle flavour and texture which does not survive heating. It is usually served at room temperature.", 1.69);
            session.save(camembert);
            Cheese emmental = new Cheese("Emmental", "Emmental is a yellow, medium-hard cheese, with characteristic large holes from Switzerland. It has a piquant, but not really sharp taste. It is often put on top of gratins, dishes which are then put in the oven to let the cheese melt and become golden-brown and crusty. It is also used for fondue.", 2.39);
            session.save(emmental);
            Cheese reblochon = new Cheese("Reblochon", "Reblochon is a French cheese from the Alps region of Savoie. Reblochon has a nutty taste that remains in mouth after its soft and uniform centre has been enjoyed. It is an essential ingredient of tartiflette, a Savoyard gratin made from potatoes, cream, onions, and bacon.", 2.99);
            session.save(reblochon);
            Discount goudaDiscount = new Discount(gouda, 0.1, "Special season's offer");
            session.save(goudaDiscount);
            Discount edamDiscount = new Discount(edam, 0.15, "Fresh from the cow");
            session.save(edamDiscount);
            User regularUser = new User("user", "user", "Regular User", false);
            session.save(regularUser);
            User adminUser = new User("admin", "admin", "Administrator", true);
            session.save(adminUser);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            session.close();
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private LocalSessionFactoryBean findSessionFactoryBean(ApplicationContext context) {
        Map beans = context.getBeansOfType(LocalSessionFactoryBean.class);
        if (beans.size() > 1) {
            throw new IllegalStateException("more than one local session factory bean found");
        } else if (beans.size() == 0) {
            throw new IllegalStateException("session factory bean not found");
        }
        return (LocalSessionFactoryBean) beans.values().iterator().next();
    }
}
