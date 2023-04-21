package cn.tworen.demou.cmd;

import java.util.List;

/**
 * Created on  2007-1-21
 *
 * Title       : IInvokeCmd.java
 * Description : 
 * 
 * @author     : LuJinYi
 * @version    : 1.0
 * @Date       : 2007-1-21
 * History     : 
 * 
 */
public interface IInvokeCmd<T> extends ICmd {

    void setCmdName(CmdName cmdName);

    CmdName getCmdName();

    double getTrxId();

    void setTrxId(double txId);

    List<T> getParams();

    void addParam(T param);

    void setFunctionCall(String name);

    String getFunctionCall();
}
