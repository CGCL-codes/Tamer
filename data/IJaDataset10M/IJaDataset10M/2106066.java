package tests3;

import com.curl.orb.security.RemoteService;

@RemoteService
public class RemoteObjectCallee {

    private String status;

    public RemoteObjectCallee(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
