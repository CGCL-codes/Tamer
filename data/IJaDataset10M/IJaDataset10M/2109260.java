package iwallet.common.account;

import iwallet.common.account.util.AccountVisitor;
import iwallet.common.account.util.VisitorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户账号
 * 
 * @author 黄源河
 * 
 */
public class IwalletAccount extends BaseAccountObject implements AccountUser {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String password = null;

    private RegisterInfo info = null;

    private transient List<AccountBook> accountBooks = new ArrayList<AccountBook>();

    @Override
    public void setID(int id) {
    }

    public IwalletAccount(String username) {
        this(username, "", new RegisterInfo());
    }

    public IwalletAccount(String username, String password, RegisterInfo info) {
        super(username, -1, -1, -1);
        this.password = password;
        this.info = info;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RegisterInfo getRegisterInfo() {
        return info;
    }

    public void setRegisterInfo(RegisterInfo info) {
        this.info = info;
    }

    @Override
    public void addAccountBook(AccountBook book) {
        this.accountBooks.add(book);
        book.setOwner(this);
    }

    @Override
    public boolean delAccountBook(AccountBook book) {
        return this.accountBooks.remove(book);
    }

    @Override
    public AccountBook getAccountBook(int bookid) {
        for (AccountBook book : accountBooks) {
            if (book.getBookID() == bookid) {
                return book;
            }
        }
        return null;
    }

    @Override
    public AccountBook[] getAccountBooks() {
        if (this.accountBooks == null) {
            this.accountBooks = new ArrayList<AccountBook>();
        }
        return accountBooks.toArray(new AccountBook[0]);
    }

    @Override
    public void accept(AccountVisitor visitor) throws VisitorException {
        visitor.visit(this);
    }

    @Override
    public String getTypeName() {
        return "用户账号";
    }

    @Override
    public void loadInfoFrom(AccountObject obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLeaves(Object obj) {
        AccountBook[] books = (AccountBook[]) obj;
        this.accountBooks = Arrays.asList(books);
        for (AccountBook book : this.accountBooks) {
            book.setOwner(this);
        }
    }
}
