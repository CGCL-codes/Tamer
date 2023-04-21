package net.sf.jlue.struts.chain;

import java.util.List;
import java.util.Stack;

/** 
 * 
 * @author Sun Yat-ton (Mail:PubTlk@Hotmail.com)
 * @version 1.00 2006-3-25
 */
public class RequestChain {

    private Stack chain;

    private Stack requests;

    public RequestChain() {
        chain = new Stack();
        requests = new Stack();
    }

    public RequestChain push(Context ctx) {
        chain.push(ctx);
        requests.push(ctx.getName());
        return this;
    }

    public Context pop() {
        requests.pop();
        return (Context) chain.pop();
    }

    public Context pop(int i) {
        requests.remove(i);
        return (Context) chain.remove(i);
    }

    public List getRequestNames() {
        return requests;
    }
}
