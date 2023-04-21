package net.videgro.oma.services;

import java.util.List;
import net.videgro.oma.domain.Function;
import net.videgro.oma.domain.Member;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

@SuppressWarnings("unchecked")
public class FunctionServiceEP extends ServletEndpointSupport implements IFunctionService {

    private IFunctionService service;

    protected void onInit() {
        this.service = (IFunctionService) getWebApplicationContext().getBean("functionService");
    }

    public List getFunctionList() {
        return service.getFunctionList();
    }

    public List getFunctionListByMember(Member member) {
        return service.getFunctionListByMember(member);
    }

    public Function getFunction(int id) {
        return service.getFunction(id);
    }

    public Function getFunctionByName(String name) {
        return service.getFunctionByName(name);
    }

    public int setFunction(Function m) {
        return service.setFunction(m);
    }

    public void deleteFunction(int id) {
        service.deleteFunction(id);
    }

    public void setFunctionList(Function[] l, int who) {
        service.setFunctionList(l, who);
    }

    public void setMemberList(int functionId, String[] memberIds, int who) {
        service.setMemberList(functionId, memberIds, who);
    }
}
