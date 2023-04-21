package de.beas.explicanto.distribution.services.remote;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author Dorel
 */
public class RemoteCtx {

    private static org.springframework.context.ApplicationContext ctx;

    private static final RemoteCtx INSTANCE = new RemoteCtx();

    private RemoteCtx() {
        try {
            ctx = new ClassPathXmlApplicationContext("remote-appCtx.xml");
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    public static final RemoteExplicantoService getRemoteExplicantoService() {
        return (RemoteExplicantoService) ctx.getBean("remoteService");
    }
}
