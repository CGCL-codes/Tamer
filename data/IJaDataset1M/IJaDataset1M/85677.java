package net.solosky.maplefetion;

/**
 *
 * 异常处理器
 *
 * @author solosky <solosky772@qq.com>
 */
public interface ExceptionHandler {

    /**
	 * 处理异常接口
	 * @param e
	 */
    public void handleException(FetionException e);
}
