package jcox.jplc.ibios.response;

import java.util.Arrays;
import java.util.Date;
import jcox.jplc.ibios.Acknowledgeable;
import jcox.jplc.ibios.Acknowledgement;
import jcox.jplc.ibios.FlatMemoryMapAddress;
import jcox.jplc.ibios.request.DownloadRequest;
import jcox.jplc.ibios.request.IBIOSRequest;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DownloadResponse implements Acknowledgeable, JoinableIBIOSResponse {

    private final byte[] value;

    private final Date dateReceived;

    DownloadResponse(byte[] in) {
        value = Arrays.copyOf(in, in.length);
        ;
        dateReceived = new Date();
    }

    @Override
    public Date getDateReceived() {
        return new Date(dateReceived.getTime());
    }

    public FlatMemoryMapAddress getToFlatMemoryModelAddress() {
        return FlatMemoryMapAddress.getFlatMemoryMapAddress(Arrays.copyOfRange(value, 0, 2));
    }

    public int getDataLength() {
        return value[3] + (((int) value[2] << 8) & 0x0000FF00);
    }

    @Override
    public boolean matchesRequest(IBIOSRequest request) {
        if (!(request instanceof DownloadRequest)) return false;
        DownloadRequest dlRequest = (DownloadRequest) request;
        return getToFlatMemoryModelAddress().equals(dlRequest.getToFlatMemoryModelAddress()) && dlRequest.getRequestData().length == getDataLength() && request.getDateRequested().getTime() <= getDateReceived().getTime();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("FlatMemoryMapAddress", getToFlatMemoryModelAddress()).append("DataLength", getDataLength()).append("Acknowledgement", getAcknowledgement()).append("getDateReceived", getDateReceived()).toString();
    }

    @Override
    public byte[] toByteArray() {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public Acknowledgement getAcknowledgement() {
        return Acknowledgement.getAcknowledgement(value[4]);
    }
}
