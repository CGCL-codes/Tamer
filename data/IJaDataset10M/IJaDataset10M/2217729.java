package org.crawlware.job;

/**
 *
 * @author Zhi Le Zou (zouzhile@gmail.com)
 */
public class InvalidJobException extends Exception {

    private static final long serialVersionUID = -6244036247451386668L;

    private Exception bang;

    public InvalidJobException(Exception e) {
        this.bang = e;
    }

    @Override
    public String getMessage() {
        return this.bang.getMessage();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return this.bang.getStackTrace();
    }
}
