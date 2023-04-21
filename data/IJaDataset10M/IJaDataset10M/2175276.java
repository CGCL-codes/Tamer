package vehikel.recorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import vehikel.IProcessDataConsumer;
import vehikel.datamodel.VehikelSchemaProvider;
import vehikel.schema.IOListType;
import vehikel.schema.IOType;
import vehikel.schema.VehikelType;

public class RecorderModel implements IProcessDataConsumer {

    private final List<RecorderChannel> channels = new ArrayList<RecorderChannel>();

    public RecorderChannel addChannel(String name, int capacity) {
        RecorderChannel recorderChannel = new RecorderChannel(name, capacity);
        channels.add(recorderChannel);
        return recorderChannel;
    }

    public RecorderChannel getChannelByName(String publicName) {
        RecorderChannel channel = null;
        int cx = size();
        while (--cx >= 0) {
            channel = channels.get(cx);
            if (channel.getName().equals(publicName)) {
                break;
            }
        }
        return channel;
    }

    public int size() {
        return channels.size();
    }

    public void monitorAllChannels() {
        VehikelSchemaProvider provider = new VehikelSchemaProvider();
        VehikelType vehikelType = provider.getRoot("ctBot", "simpleCtBot");
        List<IOType> list = new ArrayList<IOType>();
        IOListType ioListDiagnostic = vehikelType.getDiagnostic();
        EList<IOType> ioElementDiagnostic = ioListDiagnostic.getIOElement();
        for (Iterator<IOType> iterator = ioElementDiagnostic.iterator(); iterator.hasNext(); ) {
            list.add(iterator.next());
        }
        Collections.sort(list, new Comparator<IOType>() {

            public int compare(IOType io1, IOType io2) {
                if (io1.getPort().getAdr() > io2.getPort().getAdr()) return 1;
                if (io1.getPort().getAdr() < io2.getPort().getAdr()) return -1;
                if (io1.getPort().getBitAdr() > io2.getPort().getBitAdr()) return 1;
                if (io1.getPort().getBitAdr() < io2.getPort().getBitAdr()) return -1;
                return 0;
            }
        });
        for (Iterator<IOType> iterator = list.iterator(); iterator.hasNext(); ) {
            addChannel(iterator.next().getPublicName(), 1000);
        }
        IOListType ioListSensor = vehikelType.getSensors();
        if (ioListSensor != null) {
            EList<IOType> ioElementSensor = ioListSensor.getIOElement();
            for (Iterator<IOType> iterator = ioElementSensor.iterator(); iterator.hasNext(); ) {
                addChannel(iterator.next().getPublicName(), 1000);
            }
        }
        IOListType ioListActor = vehikelType.getActors();
        if (ioListActor != null) {
            EList<IOType> ioElementActor = ioListActor.getIOElement();
            for (Iterator<IOType> iterator = ioElementActor.iterator(); iterator.hasNext(); ) {
                addChannel(iterator.next().getPublicName(), 1000);
            }
        }
    }

    public void consumeRaw(int length, int[] data) {
        VehikelSchemaProvider provider = new VehikelSchemaProvider();
        VehikelType vehikelType = provider.getRoot("ctBot", "simpleCtBot");
        int dataIndex = 0;
        IOListType ioListDiagnostic = vehikelType.getDiagnostic();
        if (ioListDiagnostic != null) {
            EList<IOType> ioElement = ioListDiagnostic.getIOElement();
            int bitmask = 1;
            int raw = data[dataIndex++];
            for (Iterator<IOType> iterator = ioElement.iterator(); iterator.hasNext(); ) {
                IOType io = iterator.next();
                consumeByte(io, (raw & bitmask) != 0 ? 1 : 0);
                bitmask = bitmask << 1;
            }
        }
        IOListType ioListSensor = vehikelType.getSensors();
        if (ioListSensor != null) {
            EList<IOType> ioElement = ioListSensor.getIOElement();
            for (Iterator<IOType> iterator = ioElement.iterator(); iterator.hasNext(); ) {
                consume2Bytes(iterator.next(), data[dataIndex++], data[dataIndex++]);
            }
        }
        IOListType ioListActor = vehikelType.getActors();
        if (ioListActor != null) {
            EList<IOType> ioElement = ioListActor.getIOElement();
            for (Iterator<IOType> iterator = ioElement.iterator(); iterator.hasNext(); ) {
                consume2Bytes(iterator.next(), data[dataIndex++], data[dataIndex++]);
            }
        }
    }

    private void consume2Bytes(IOType io, int low, int high) {
        RecorderChannel channel = getChannelByName(io.getPublicName());
        if (channel != null) {
            int value = 256 * high + low;
            channel.append(value > 0xefff ? value - 0xffff - 1 : value);
        }
    }

    private void consumeByte(IOType io, int raw) {
        RecorderChannel channel = getChannelByName(io.getPublicName());
        if (channel != null) {
            channel.append(raw);
        }
    }
}
