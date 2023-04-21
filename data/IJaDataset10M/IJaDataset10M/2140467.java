package com.kenstevens.stratinit.server.gwtservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.kenstevens.stratinit.client.gwtservice.GWTNone;
import com.kenstevens.stratinit.client.gwtservice.GWTRegisterService;
import com.kenstevens.stratinit.client.gwtservice.GWTResult;
import com.kenstevens.stratinit.server.daoservice.PlayerDaoService;
import com.kenstevens.stratinit.ws.Result;

@SuppressWarnings("serial")
@Service("registerService")
public class GWTRegisterServiceImpl extends RemoteServiceServlet implements GWTRegisterService {

    @Autowired
    PlayerDaoService playerDaoService;

    public GWTResult<GWTNone> register(String username, String password, String email) {
        Result<Void> result = playerDaoService.register(username, password, email);
        return new GWTResult<GWTNone>(result.getLastMessage(), result.isSuccess());
    }
}
