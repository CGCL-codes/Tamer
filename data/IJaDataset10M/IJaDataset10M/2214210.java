package net.sf.balm.web;

import org.apache.struts.action.ActionMessages;

/**
 * 异常处理，用于TODO:ADD REMARKS
 * 
 * @author dz
 */
public interface ExceptionHandler {

    /**
     * 处理异常，根据业务需求将要处理的异常转化为需要展现给用户的消息，对于不处理的异常则继续抛出
     * 
     * @param exception
     * @return
     * @throws Exception
     */
    public ActionMessages handleException(Exception exception) throws Exception;
}
