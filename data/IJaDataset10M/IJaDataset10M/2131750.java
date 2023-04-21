package net.sf.revolver.sample.service.book.leasestart;

import java.sql.Timestamp;
import net.sf.bulletlib.authentication.core.LoginUser;
import net.sf.bulletlib.core.logging.Logging;
import net.sf.bulletlib.dbutil.s2jdbc.time.TimeUtilityDao;
import net.sf.revolver.core.BusinessLogicException;
import net.sf.revolver.core.Result;
import net.sf.revolver.core.RvContext;
import net.sf.revolver.s2.RvDoS2;
import net.sf.revolver.sample.dao.BookDao;
import net.sf.revolver.sample.dao.LeaseBookDao;
import net.sf.revolver.sample.entity.Book;
import net.sf.revolver.sample.entity.LeaseBook;

/**
 *  book lease開始処理ビジネスロジック処理Doクラス.
 *
 * @author bose999
 *
 */
public class LeaseStartDo extends RvDoS2 {

    /**
     * BookDao.
     */
    public BookDao bookDao;

    /**
     * LeaseBookDao.
     */
    public LeaseBookDao leaseBookDao;

    /**
     * TimeUtilityDao.
     */
    public TimeUtilityDao timeUtilityDao;

    /**
     * book lease開始処理.
     *
     * @param rvContext RvContext
     * @return Result
     * @throws BusinessLogicException BusinessLogicException
     */
    @Override
    protected Result doLogic(RvContext rvContext) throws BusinessLogicException {
        String bookIdString = rvContext.getInValue("id");
        Integer bookId = Integer.valueOf(bookIdString);
        if (isLease(bookId)) {
            addErrorMessage(rvContext, "viewValues.error", "leaseStartDo.leaseState01");
            return Result.FAILURE;
        }
        Integer count = insertLeaseBook(rvContext, bookId);
        Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), ":LeaseBook insert:" + count.toString());
        return Result.SUCCESS;
    }

    /**
     * 貸出確認.<br />
     * 貸出中ならtrue.
     *
     * @param bookId Integer
     * @return boolean
     */
    protected boolean isLease(Integer bookId) {
        Book book = bookDao.searchId(bookId);
        if (book.leaseState.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * LeaseBook返却Update処理.
     *
     * @param rvContext RvContext
     * @param bookId Integer
     * @return Integer
     */
    protected Integer insertLeaseBook(RvContext rvContext, Integer bookId) {
        LoginUser loginUser = getSessionLoginUser(rvContext);
        Integer userTableId = loginUser.userTableId;
        Timestamp nowTimestamp = timeUtilityDao.getCurrentTimestamp();
        LeaseBook leaseBook = new LeaseBook();
        leaseBook.bookId = bookId;
        leaseBook.leaseState = "1";
        leaseBook.createId = userTableId;
        leaseBook.createDate = nowTimestamp;
        leaseBook.editId = userTableId;
        leaseBook.editDate = nowTimestamp;
        leaseBookDao.insert(leaseBook);
        return bookDao.updateLeaseState(bookId, "1", userTableId, nowTimestamp);
    }
}
