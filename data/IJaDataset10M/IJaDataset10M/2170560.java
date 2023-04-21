package com.incendiaryblue.cmslite.transfer;

import com.incendiaryblue.appframework.AppComponentBase;
import com.incendiaryblue.appframework.UserAppComponent;
import com.incendiaryblue.config.XMLConfigurable;
import com.incendiaryblue.config.XMLContext;
import org.w3c.dom.Element;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class TransferManager extends AppComponentBase implements UserAppComponent, XMLConfigurable {

    private TransferImpl transferImpl = null;

    public TransferManager() {
        super(TransferManager.class);
    }

    public TransferManager(TransferImpl transferImpl) {
        this();
        this.transferImpl = transferImpl;
    }

    public boolean DoImport(String filename) throws IOException, SQLException, ParserConfigurationException, ParseException, TransferException {
        boolean success = transferImpl.DoImport(filename);
        return success;
    }

    public TransferImpl getTransferImpl() {
        return transferImpl;
    }

    public void setTransferImpl(TransferImpl ti) {
        transferImpl = ti;
    }

    public Object configure(Element element, XMLContext context) {
        setName(element.getAttribute("name"));
        return this;
    }

    public void registerChild(Object child) {
        setTransferImpl((TransferImpl) child);
    }
}
