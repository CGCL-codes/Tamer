package net.videgro.oma.services;

import java.util.List;
import net.videgro.oma.domain.Function;
import net.videgro.oma.domain.Member;
import net.videgro.oma.managers.FunctionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("unchecked")
public class FunctionService implements IFunctionService {

    protected final Log logger = LogFactory.getLog(getClass());

    private FunctionManager functionManager = null;

    public FunctionService() {
        super();
    }

    public FunctionManager getFunctionManager() {
        return functionManager;
    }

    public void setFunctionManager(FunctionManager functionManager) {
        this.functionManager = functionManager;
    }

    public List getFunctionList() {
        List list = null;
        try {
            list = functionManager.getFunctionList(false);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return list;
    }

    public List getFunctionListByMember(Member member) {
        List list = null;
        try {
            list = functionManager.getFunctionList(member);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return list;
    }

    public Function getFunction(int id) {
        Function result = null;
        try {
            result = functionManager.getFunction(id);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public Function getFunctionByName(String name) {
        Function result = null;
        try {
            result = functionManager.getFunction(name);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public int setFunction(Function m) {
        int result = -1;
        try {
            result = functionManager.setFunction(m);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public void deleteFunction(int id) {
        try {
            functionManager.deleteFunction(id);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    public void setFunctionList(Function[] l, int who) {
        try {
            functionManager.setFunctionList(l, who);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    public void setMemberList(int functionId, String[] memberIds, int who) {
        try {
            functionManager.setMemberList(functionId, memberIds, who);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }
}
