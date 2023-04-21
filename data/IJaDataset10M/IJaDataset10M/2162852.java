package com.psycho.rtb;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import com.psycho.rtb.data.DataEntry;
import com.psycho.rtb.data.DataGroup;
import com.psycho.rtb.data.Stream;

public class SelfConnection extends BeanConnection {

    public SelfConnection(ConnectionPool pool) {
        super(null, ConnectionPool.SELF_KEY);
        List<Integer> fromSendBuild = new ArrayList<Integer>();
        List<Integer> toReceiveBuild = new ArrayList<Integer>();
        Stream stream = getEnv().streams.get(StreamType.SENDING);
        sEntryMaps = new EnumMap[stream.groups.length];
        rEntryMaps = new EnumMap[stream.groups.length];
        EnumMap<BasicType, int[]> sGroupEntryMaps = new EnumMap<BasicType, int[]>(BasicType.class);
        EnumMap<BasicType, int[]> rGroupEntryMaps = new EnumMap<BasicType, int[]>(BasicType.class);
        for (DataGroup group : stream.groups) {
            boolean empty = true;
            for (BasicType type : BasicType.values()) {
                DataEntry[] entries = group.entries.get(type);
                for (int iEntry = 0; iEntry < entries.length; iEntry++) {
                    if (!entries[iEntry].isSelfConnected()) {
                        continue;
                    }
                    fromSendBuild.add(entries[iEntry].getIndex());
                    toReceiveBuild.add(entries[iEntry].getSelfConnection());
                }
                if (!fromSendBuild.isEmpty()) {
                    empty = false;
                    int[] fromIndexes = new int[fromSendBuild.size()];
                    for (int i = 0; i < fromIndexes.length; i++) {
                        fromIndexes[i] = fromSendBuild.get(i);
                    }
                    fromSendBuild.clear();
                    sGroupEntryMaps.put(type, fromIndexes);
                    int[] toIndexes = new int[toReceiveBuild.size()];
                    for (int i = 0; i < toIndexes.length; i++) {
                        toIndexes[i] = toReceiveBuild.get(i);
                    }
                    toReceiveBuild.clear();
                    rGroupEntryMaps.put(type, toIndexes);
                }
            }
            if (!empty) {
                sEntryMaps[group.id] = sGroupEntryMaps;
                sGroupEntryMaps = new EnumMap<BasicType, int[]>(BasicType.class);
                rEntryMaps[group.id] = rGroupEntryMaps;
                rGroupEntryMaps = new EnumMap<BasicType, int[]>(BasicType.class);
            }
        }
    }

    public void connect() {
        throw new UnsupportedOperationException();
    }

    public void startConnect() {
        throw new UnsupportedOperationException();
    }

    Environment getEnv() {
        return parent.getEnv();
    }

    public void send(int group) {
        if (sEntryMaps[group] == null) {
            return;
        }
        Stream sStream = getEnv().streams.get(StreamType.SENDING);
        Stream rStream = getEnv().streams.get(StreamType.RECEIVING);
        for (BasicType type : BasicType.values()) {
            int[] fromIndexes = sEntryMaps[group].get(type);
            if (fromIndexes == null) {
                return;
            }
            type.getFactory().copy(fromIndexes, sStream, rEntryMaps[group].get(type), rStream);
        }
    }
}
