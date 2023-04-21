package oss.net.pstream.rewrite;

import java.util.HashMap;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class RewriteVariableHandlerMap extends HashMap {

    public RewriteVariableHandlerMap(boolean Debug) {
        super();
        RewriteVariableHandler _temp;
        _temp = new ResponseBodyRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new HostProtocolRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new HostNameRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new HostPortRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new HostPathRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new TargetProtocolRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new TargetNameRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new TargetPortRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new TargetPathRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new TargetQueryRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new SessionRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new RequestHeaderRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new ResponseHeaderRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new UservarRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new RequestContextRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
        _temp = new RequestHostRewriteVariableHandler(Debug);
        super.put(_temp.getVariableName(), _temp);
    }

    public RewriteVariableHandler getVariableHandler(String Name) throws RewriteException {
        Object result = super.get(Name);
        if (result == null) throw new RewriteException("There is no rewrite variable handler named '" + Name + "'!");
        return (RewriteVariableHandler) super.get(Name);
    }
}
