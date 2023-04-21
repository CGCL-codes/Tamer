package com.monad.homerun.uiutl;

import java.rmi.RemoteException;
import com.monad.homerun.base.LimiterResolver;

/**
 * @author richard
 *
 * SpecResolver describes classes that can resolve specifiers & icons
 * Currently, Setup, ActionBuilder, ObjectEditor, and Modeler
 * are implementations
 */
public interface SpecResolver extends LimiterResolver {

    public byte[] getIconBytes(String category, String iconName) throws RemoteException;
}
