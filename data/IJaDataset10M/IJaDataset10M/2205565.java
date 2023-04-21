package watij.iwshruntimelibrary;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import watij.iwshruntimelibrary.impl.*;

/**
 * Represents Java interface for COM interface IWshNetwork2.
 */
public interface IWshNetwork2 extends IWshNetwork {

    public static final String INTERFACE_IDENTIFIER = "{24BE5A31-EDFE-11D2-B933-00104B365C9F}";

    void addWindowsPrinterConnection(BStr PrinterName, BStr DriverName, BStr Port) throws ComException;
}
