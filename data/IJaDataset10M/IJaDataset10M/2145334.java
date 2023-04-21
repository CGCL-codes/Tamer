package net.solosky.maplefetion.net;

import net.solosky.maplefetion.FetionException;

/**
 *
 * 传输异常，在连接关闭的时候抛出
 *
 * @author solosky <solosky772@qq.com>
 */
public class TransferException extends FetionException {

    private static final long serialVersionUID = -2452131552662054932L;

    /**
     * @param e
     */
    public TransferException(Throwable e) {
        super(e);
    }

    /**
     * @param string
     */
    public TransferException(String msg) {
        super(msg);
    }

    public TransferException() {
        super();
    }
}
