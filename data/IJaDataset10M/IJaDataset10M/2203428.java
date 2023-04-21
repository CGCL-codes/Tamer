package cn.ac.ntarl.umt.api.server;

import cn.ac.ntarl.umt.api.ServiceException;

public class EchoService implements ServiceAction {

    public Object doAction(Object msg) throws ServiceException {
        return msg;
    }
}
