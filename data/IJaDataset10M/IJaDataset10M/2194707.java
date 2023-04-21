package org.apache.webdav.lib.methods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;

/**
 * Implements the UNSUBSCRIBE method.
 * 
 * @see <a href="http://msdn2.microsoft.com/en-us/library/aa143150.aspx">Reference</a>
 */
public class UnsubscribeMethod extends XMLResponseMethodBase {

    private static final String HEADER_SUBSCRIPTION_ID = "Subscription-Id";

    private List subscriptionIds = new ArrayList();

    public UnsubscribeMethod() {
    }

    public UnsubscribeMethod(String path) {
        super(path);
    }

    /**
    * Adds an ID for a subscription that is to be withdrawn.
    */
    public void addSubscriptionId(int id) {
        this.subscriptionIds.add(new Integer(id));
    }

    public String getName() {
        return "UNSUBSCRIBE";
    }

    public void recycle() {
        super.recycle();
        this.subscriptionIds.clear();
    }

    protected void addRequestHeaders(HttpState state, HttpConnection conn) throws IOException, HttpException {
        super.addRequestHeaders(state, conn);
        if (this.subscriptionIds.size() > 0) {
            StringBuffer b = new StringBuffer();
            boolean first = true;
            for (Iterator i = this.subscriptionIds.iterator(); i.hasNext(); ) {
                if (first) first = false; else b.append(", ");
                b.append(i.next());
            }
            super.addRequestHeader(HEADER_SUBSCRIPTION_ID, b.toString());
        }
    }

    /**
    * Adds special checking of header values of the UNSUBSCRIBE method to
    * the super class implementation.
    */
    public void setRequestHeader(String headerName, String headerValue) {
        if (headerName.equalsIgnoreCase(HEADER_SUBSCRIPTION_ID)) {
            StringTokenizer t = new StringTokenizer(headerValue, ", ");
            try {
                for (; t.hasMoreTokens(); ) {
                    addSubscriptionId(Integer.parseInt(t.nextToken()));
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid header value '" + headerValue + "' for header " + headerName + "!");
            }
        } else {
            super.setRequestHeader(headerName, headerValue);
        }
    }
}
