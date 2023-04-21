package net.transparent.ccjni.ClearCase;

import com.develop.jawin.*;
import com.develop.jawin.constants.*;
import com.develop.jawin.marshal.*;
import com.develop.io.*;
import java.io.*;

public class ICCLabels extends DispatchPtr {

    public static final GUID proxyIID = new GUID("{B22C7EE6-5A5E-11D3-B1CD-00C04F8ECE2F}");

    public static final int iidToken;

    static {
        iidToken = IdentityManager.registerProxy(proxyIID, ICCLabels.class);
    }

    public int getGuidToken() {
        return iidToken;
    }

    public ICCLabels() throws COMException {
        super();
    }

    public ICCLabels(String progid) throws COMException {
        super(progid);
    }

    public ICCLabels(IUnknown other) throws COMException {
        super(other);
    }

    public ICCLabels(GUID ClsID) throws COMException {
        super(ClsID);
    }

    public ICCLabel getItem(int index) throws COMException {
        return new ICCLabel((DispatchPtr) getN("Item", new Object[] { new java.lang.Integer(index) }));
    }

    public void Add(ICCLabel pLabel) throws COMException {
        invokeN("Add", new Object[] { pLabel }, 1);
    }

    public int getCount() throws COMException {
        return ((java.lang.Integer) get("Count")).intValue();
    }

    public void Remove(int index) throws COMException {
        invokeN("Remove", new Object[] { new java.lang.Integer(index) }, 1);
    }

    public DispatchPtr get_NewEnum() throws COMException {
        return (DispatchPtr) get("_NewEnum");
    }
}
