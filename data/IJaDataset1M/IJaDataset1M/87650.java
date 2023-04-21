package org.ouobpo.tools.amazonchecker.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.ouobpo.tools.amazonchecker.exception.DomainException;
import org.ouobpo.tools.amazonchecker.exception.ServiceException;
import org.ouobpo.tools.amazonchecker.service.AmazonServiceFactory;
import org.ouobpo.tools.amazonchecker.service.IAmazonService;
import org.ouobpo.tools.amazonchecker.service.SystemService;
import org.ouobpo.tools.amazonchecker.service.IAmazonService.BookData;
import org.ouobpo.tools.amazonchecker.util.SortUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.domainlanguage.time.TimePoint;
import com.domainlanguage.timeutil.SystemClock;

/**
 * @author SATO, Tadayosi
 * @version $Id: Book.java 63 2009-01-17 13:51:29Z tadayosi $
 */
public class Book {

    private static final Logger LOGGER = LoggerFactory.getLogger(Book.class);

    private static final String AMAZON_PAGE_URL_TEMPLATE = "http://www.amazon.co.jp/gp/offer-listing/%s?tag=ouobpo-22";

    private String fAsin;

    private String fTitle;

    private boolean fActive;

    private TimePoint fCreatedTime;

    private List<BookPrice> fListPriceHistory = new ArrayList<BookPrice>();

    private List<BookPrice> fUsedPriceHistory = new ArrayList<BookPrice>();

    /**
   * ファクトリメソッド
   * @throws DomainException 該当書籍が存在しなかった場合
   */
    public static Book createFromAmazon(String asin) throws DomainException {
        IAmazonService amazonService = AmazonServiceFactory.create();
        try {
            BookData data = amazonService.getBookData(asin);
            Book book = new Book(asin, data.getTitle(), true, SystemClock.now());
            book.addListPriceHistory(BookPrice.newListPrice(data.getListPrice()));
            book.addUsedPriceHistory(BookPrice.newUsedPrice(data.getUsedPrice()));
            return book;
        } catch (ServiceException e) {
            throw new DomainException("ASIN=" + asin + " の書籍を取得できません", e);
        }
    }

    public Book() {
    }

    private Book(String asin, String title, boolean active, TimePoint createdTime) {
        fAsin = asin;
        fTitle = title;
        fActive = active;
        fCreatedTime = createdTime;
    }

    /**
   * @return 書籍情報が更新された場合
   */
    public boolean update() throws DomainException {
        IAmazonService amazonService = AmazonServiceFactory.create();
        try {
            BookData data = amazonService.getBookData(fAsin);
            if (!isUpdated(data)) {
                return false;
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("更新実行:");
                LOGGER.debug("  前: {}, {}, {}", new Object[] { fTitle, getLatestListPrice(), getLatestUsedPrice() });
                LOGGER.debug("  後: {}, {}, {}", new Object[] { data.getTitle(), data.getListPrice(), data.getUsedPrice() });
            }
            fTitle = data.getTitle();
            if (!ObjectUtils.equals(ObjectUtils.toString(getLatestListPrice().getPrice(), null), ObjectUtils.toString(data.getListPrice(), null))) {
                addListPriceHistory(BookPrice.newListPrice(data.getListPrice()));
            }
            if (!ObjectUtils.equals(ObjectUtils.toString(getLatestUsedPrice().getPrice(), null), ObjectUtils.toString(data.getUsedPrice(), null))) {
                addUsedPriceHistory(BookPrice.newUsedPrice(data.getUsedPrice()));
            }
            return true;
        } catch (ServiceException e) {
            throw new DomainException("ASIN=" + fAsin + " の書籍を取得できません", e);
        }
    }

    private boolean isUpdated(BookData data) {
        if (!ObjectUtils.equals(fTitle, data.getTitle())) {
            return true;
        }
        if (!ObjectUtils.equals(ObjectUtils.toString(getLatestListPrice().getPrice(), null), ObjectUtils.toString(data.getListPrice(), null))) {
            return true;
        }
        if (!ObjectUtils.equals(ObjectUtils.toString(getLatestUsedPrice().getPrice(), null), ObjectUtils.toString(data.getUsedPrice(), null))) {
            return true;
        }
        return false;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public void addListPriceHistory(BookPrice listPrice) {
        fListPriceHistory.add(listPrice);
    }

    public void addUsedPriceHistory(BookPrice usedPrice) {
        fUsedPriceHistory.add(usedPrice);
    }

    public void activate() {
        fActive = true;
    }

    public void deactivate() {
        fActive = false;
    }

    /**
   * Amazonページを表示
   */
    public void showAmazonPage() {
        String url = String.format(AMAZON_PAGE_URL_TEMPLATE, fAsin);
        SystemService systemService = SystemService.create();
        systemService.launchBrowser(url);
    }

    public String listPriceIndicator() {
        return priceIndicator(fListPriceHistory);
    }

    public String usedPriceIndicator() {
        return priceIndicator(fUsedPriceHistory);
    }

    private static String priceIndicator(List<BookPrice> priceHistory) {
        if (priceHistory.size() < 2) {
            return "";
        }
        BookPrice latest = priceHistory.get(priceHistory.size() - 1);
        BookPrice past = priceHistory.get(priceHistory.size() - 2);
        switch(latest.compareTo(past)) {
            case 1:
                return "↑";
            case -1:
                return "↓";
            default:
                return "-";
        }
    }

    public static List<Book> sortByAsin(List<Book> books) {
        return SortUtils.sort(books, new Comparator<Book>() {

            public int compare(Book book1, Book book2) {
                return book1.getAsin().compareTo(book2.getAsin());
            }
        });
    }

    public static List<Book> sortByTitle(List<Book> books) {
        return SortUtils.sort(books, new Comparator<Book>() {

            public int compare(Book book1, Book book2) {
                if (StringUtils.equals(book1.getTitle(), book2.getTitle())) {
                    return 0;
                }
                if (book1.getTitle() == null) {
                    return -1;
                }
                if (book2.getTitle() == null) {
                    return 1;
                }
                return book1.getTitle().compareTo(book2.getTitle());
            }
        });
    }

    public String getAsin() {
        return fAsin;
    }

    public String getTitle() {
        return fTitle;
    }

    public boolean isActive() {
        return fActive;
    }

    public String getCreatedTimeInString(String pattern) {
        return fCreatedTime.toString(pattern, TimeZone.getDefault());
    }

    public List<BookPrice> getListPriceHistory() {
        return fListPriceHistory;
    }

    public List<BookPrice> getUsedPriceHistory() {
        return fUsedPriceHistory;
    }

    public BookPrice getLatestListPrice() {
        return fListPriceHistory.get(fListPriceHistory.size() - 1);
    }

    public BookPrice getLatestUsedPrice() {
        return fUsedPriceHistory.get(fUsedPriceHistory.size() - 1);
    }

    public BookPrice getLowestListPrice() {
        return BookPrice.lowestPrice(fListPriceHistory);
    }

    public BookPrice getLowestUsedPrice() {
        return BookPrice.lowestPrice(fUsedPriceHistory);
    }

    public void setAsin(String asin) {
        fAsin = asin;
    }

    public void setTitle(String title) {
        fTitle = title;
    }

    public void setActive(boolean active) {
        fActive = active;
    }

    public void setCreatedTime(TimePoint createdTime) {
        fCreatedTime = createdTime;
    }

    public void setListPriceHistory(List<BookPrice> listPriceHistory) {
        fListPriceHistory = listPriceHistory;
    }

    public void setUsedPriceHistory(List<BookPrice> usedPriceHistory) {
        fUsedPriceHistory = usedPriceHistory;
    }
}
