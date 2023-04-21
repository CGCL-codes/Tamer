package org.eclipse.swt.internal.mozilla;

public class nsIWebBrowserStream extends nsISupports {

    static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 3;

    public static final String NS_IWEBBROWSERSTREAM_IID_STR = "86d02f0e-219b-4cfc-9c88-bd98d2cce0b8";

    public static final nsID NS_IWEBBROWSERSTREAM_IID = new nsID(NS_IWEBBROWSERSTREAM_IID_STR);

    public nsIWebBrowserStream(int address) {
        super(address);
    }

    public int OpenStream(int aBaseURI, int aContentType) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aBaseURI, aContentType);
    }

    public int AppendToStream(int aData, int aLen) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aData, aLen);
    }

    public int CloseStream() {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress());
    }
}
