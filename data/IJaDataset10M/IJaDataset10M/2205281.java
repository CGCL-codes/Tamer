package realtimetrading.trader;

import java.math.BigDecimal;
import java.util.Random;
import realtimetrading.book.Book;
import realtimetrading.market.Offer;
import realtimetrading.marketcontroller.MarketController;

public class TraderCrazy extends Trader {

    Random rand = new Random();

    Long totalStock = 0L;

    BigDecimal totalMoney = new BigDecimal(0);

    BigDecimal gain;

    public void operate(Book book, MarketController controller) {
        Offer o = new Offer(this);
        o.setQuantity(Math.abs(rand.nextLong() % 50));
        if (rand.nextBoolean() == true) o.setPrice(book.lastPrice.subtract(book.tick)); else o.setPrice(book.lastPrice.add(book.tick));
        totalStock = 0L;
        for (int i = 0; i < sellOffer.size(); i++) {
            if (sellOffer.get(i).quantity > 0) totalStock += sellOffer.get(i).quantity;
        }
        totalMoney = new BigDecimal(0);
        for (int i = 0; i < buyOffer.size(); i++) {
            if (buyOffer.get(i).quantity > 0) totalMoney = totalMoney.add(buyOffer.get(i).price.multiply(BigDecimal.valueOf(buyOffer.get(i).quantity)));
        }
        if (rand.nextDouble() < 0.5) {
            if ((o.price.multiply(BigDecimal.valueOf(o.quantity))).compareTo(this.getMoney().subtract(totalMoney)) <= 0) {
                buyOffer.add(o);
                controller.buy(o);
            }
        } else {
            if (o.quantity <= this.getStock() - totalStock) {
                sellOffer.add(o);
                controller.sell(o);
            }
        }
    }
}
