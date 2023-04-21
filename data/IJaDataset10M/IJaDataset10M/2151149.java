package org.ouobpo.tools.amazonchecker.domain;

import java.util.List;
import com.domainlanguage.money.Money;
import com.domainlanguage.time.TimePoint;
import com.domainlanguage.timeutil.SystemClock;

/**
 * @author SATO, Tadayosi
 * @version $Id: BookPrice.java 54 2009-01-01 08:19:50Z tadayosi $
 */
public class BookPrice implements Comparable<BookPrice> {

    private enum Type {

        LIST, USED
    }

    ;

    private Type fType;

    private Money fPrice;

    private TimePoint fCreatedTime;

    public static BookPrice newListPrice(Money price) {
        return new BookPrice(Type.LIST, price, SystemClock.now());
    }

    public static BookPrice newUsedPrice(Money price) {
        return new BookPrice(Type.USED, price, SystemClock.now());
    }

    public BookPrice() {
    }

    private BookPrice(Type type, Money price, TimePoint createdTime) {
        fType = type;
        fPrice = price;
        fCreatedTime = createdTime;
    }

    public String toString() {
        return fPrice == null ? null : fPrice.toString();
    }

    public int compareTo(BookPrice other) {
        if (other == null) {
            throw new IllegalArgumentException("parameter 'other' must not be null.");
        }
        if (fPrice == null || other.getPrice() == null) {
            if (fPrice == null && other.getPrice() == null) {
                return 0;
            }
            if (fPrice == null) {
                return 1;
            }
            if (other.getPrice() == null) {
                return -1;
            }
        }
        return fPrice.breachEncapsulationOfAmount().compareTo(other.getPrice().breachEncapsulationOfAmount());
    }

    public static BookPrice lowestPrice(List<BookPrice> prices) {
        BookPrice lowest = null;
        for (BookPrice price : prices) {
            if (price == null) {
                continue;
            }
            if (lowest == null) {
                lowest = price;
                continue;
            }
            if (lowest.compareTo(price) > 0) {
                lowest = price;
            }
        }
        return lowest;
    }

    public Type getType() {
        return fType;
    }

    public Money getPrice() {
        return fPrice;
    }

    public TimePoint getCreatedTime() {
        return fCreatedTime;
    }

    public void setType(Type type) {
        fType = type;
    }

    public void setPrice(Money price) {
        fPrice = price;
    }

    public void setCreatedTime(TimePoint createdTime) {
        fCreatedTime = createdTime;
    }
}
