package net.da.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import net.da.gwt.client.dto.AA01GwtRequest;
import net.da.gwt.client.dto.AA01GwtResponse;
import net.da.gwt.client.dto.AA02GwtRequest;
import net.da.gwt.client.dto.AA02GwtResponse;

public interface AAService extends RemoteService {

    public AA01GwtResponse a01(AA01GwtRequest request);

    public AA02GwtResponse a02(AA02GwtRequest request);
}
