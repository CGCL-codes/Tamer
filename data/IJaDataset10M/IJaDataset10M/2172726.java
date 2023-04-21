package org.ouobpo.tools.amazonchecker.service;

import org.ouobpo.tools.amazonchecker.exception.ServiceException;
import com.domainlanguage.money.Money;

/**
 * @author SATO, Tadayosi
 * @version $Id: IAmazonService.java 46 2008-12-29 14:41:23Z tadayosi $
 */
public interface IAmazonService {

    /**
   * @throws ServiceException 該当書籍を取得できなかった場合
   */
    BookData getBookData(String asin) throws ServiceException;

    /**
   * サービスが返す書籍データ
   */
    class BookData {

        private String fTitle;

        private Money fListPrice;

        private Money fUsedPrice;

        public BookData(String title, Money listPrice, Money usedPrice) {
            fTitle = title;
            fListPrice = listPrice;
            fUsedPrice = usedPrice;
        }

        public String getTitle() {
            return fTitle;
        }

        public Money getListPrice() {
            return fListPrice;
        }

        public Money getUsedPrice() {
            return fUsedPrice;
        }
    }
}
