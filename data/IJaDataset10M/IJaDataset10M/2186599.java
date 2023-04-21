package com.erlang4j.internal.basicIo;

/**
 * This allows you to make {@link IBasicMailBox}.
 * 
 * <p>
 * Design notes
 * <ul>
 * <li>As you need a new mail box for each process, this is a mechanism to inject the code to make a new one.
 * </ul>
 * </p>
 * 
 * @author Phil Rice
 * */
public interface IBasicMailBoxFactory {

    IBasicMailBox operations();
}
