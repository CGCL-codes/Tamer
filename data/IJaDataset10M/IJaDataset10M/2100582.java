package net.sf.bimbo.demo.webshop;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import net.sf.bimbo.Action;
import net.sf.bimbo.Authenticated;
import net.sf.bimbo.Output;
import net.sf.bimbo.SessionAttribute;
import net.sf.bimbo.Title;
import net.sf.bimbo.demo.SourceCodePage;

@Title("Shopping Cart")
public class ViewShoppingCartPage {

    @Output
    List<BasketProduct> shoppingCart;

    @SessionAttribute("shoppingCart")
    List<BasketProduct> sessionShoppingCart;

    @Output("Total")
    private double total;

    @PostConstruct
    public void postConstruct() {
        this.shoppingCart = this.sessionShoppingCart;
        this.total = 0;
        if (null == this.sessionShoppingCart) {
            return;
        }
        for (BasketProduct basketProduct : this.sessionShoppingCart) {
            this.total += basketProduct.totalPrice;
        }
    }

    public ViewShoppingCartPage() {
    }

    @Action("Back")
    public MainPage back() {
        return new MainPage();
    }

    @Action(value = "Clear", confirmation = "Do you really want to clear your shopping cart?")
    public ViewShoppingCartPage clear() {
        this.sessionShoppingCart = new LinkedList<BasketProduct>();
        return this;
    }

    @Action("Source Code")
    public SourceCodePage showSourceCode() {
        return new SourceCodePage(this);
    }

    @Action("Check Out")
    @Authenticated
    public CheckOutPage checkOut() {
        if (null == this.sessionShoppingCart || this.sessionShoppingCart.isEmpty()) {
            throw new RuntimeException("Cannot check out when shopping cart is empty.");
        }
        return new CheckOutPage();
    }
}
