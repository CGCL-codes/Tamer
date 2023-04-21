package org.redwood.business.report.documents.reportdocuments.reportdocument;

import org.redwood.business.report.documents.document.*;
import org.redwood.business.report.documents.reportdocuments.reporttable.*;
import org.redwood.business.report.documents.exceptions.*;
import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * The  interface.
 *
 * @author  Gerrit Franke
 * @version 1.0
 */
public interface ReportDocument extends Document {

    public void writeTable(ReportTable reportTable) throws RemoteException, DocumentInsertException;

    public void addImage(byte[] b, String filename) throws RemoteException, DocumentInsertException;

    public void addText(String text) throws RemoteException, DocumentInsertException;

    public void addLink(String title, String name, String reference) throws RemoteException, DocumentInsertException;

    public void setHeader(String text, boolean numbered) throws RemoteException;

    public void setFooter(String text, boolean numbered) throws RemoteException;

    public void openChapter(String title, int number) throws RemoteException;

    public void closeChapter() throws RemoteException, DocumentInsertException;

    public void addMeasureHeader(String measureheader) throws RemoteException;

    public void addMeasureHeaderTime(String measureheadertime) throws RemoteException;
}
