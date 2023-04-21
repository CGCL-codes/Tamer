package de.tu_dortmund.cni.peper.businessobjects.jxta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.MessageElement;
import de.tu_dortmund.cni.peper.businessobjects.services.Area;

public class GetValuesRequestMessageElement extends MessageElement {

    protected List<Area> interestedArea;

    protected GregorianCalendar fromDate;

    protected GregorianCalendar toDate;

    protected static final Long DATE_NULL_VALUE = Long.MIN_VALUE;

    public GetValuesRequestMessageElement(String name, MimeMediaType type, MessageElement sig, List<Area> interestedArea, GregorianCalendar fromDate, GregorianCalendar toDate) {
        super(name, type, sig);
        if (interestedArea == null) interestedArea = new ArrayList<Area>();
        this.interestedArea = interestedArea;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public GetValuesRequestMessageElement(ByteArrayMessageElement bame) {
        super(bame.getElementName(), bame.getMimeType(), bame.getSignature());
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bame.getBytes()));
            int arraySize = dis.readInt();
            interestedArea = new ArrayList<Area>(arraySize);
            for (int i = 0; i < arraySize; i++) {
                Area a = new Area();
                a.setLatitude(dis.readDouble());
                a.setLongitude(dis.readDouble());
                a.setRadius(dis.readDouble());
                interestedArea.add(a);
            }
            fromDate = new GregorianCalendar();
            fromDate.setTimeInMillis(dis.readLong());
            if (fromDate.getTimeInMillis() == DATE_NULL_VALUE) fromDate = null;
            toDate = new GregorianCalendar();
            toDate.setTimeInMillis(dis.readLong());
            if (toDate.getTimeInMillis() == DATE_NULL_VALUE) toDate = null;
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Area> getInterestedArea() {
        if (interestedArea == null) {
            interestedArea = new ArrayList<Area>();
        }
        return this.interestedArea;
    }

    public GregorianCalendar getFromDate() {
        return fromDate;
    }

    public void setFromDate(GregorianCalendar value) {
        this.fromDate = value;
    }

    public GregorianCalendar getToDate() {
        return toDate;
    }

    public void setToDate(GregorianCalendar value) {
        this.toDate = value;
    }

    public InputStream getStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(interestedArea.size());
        for (Area a : interestedArea) {
            dos.writeDouble(a.getLatitude());
            dos.writeDouble(a.getLongitude());
            dos.writeDouble(a.getRadius());
        }
        if (fromDate == null) dos.writeLong(DATE_NULL_VALUE); else dos.writeLong(fromDate.getTimeInMillis());
        if (toDate == null) dos.writeLong(DATE_NULL_VALUE); else dos.writeLong(toDate.getTimeInMillis());
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        dos.close();
        return bais;
    }
}
