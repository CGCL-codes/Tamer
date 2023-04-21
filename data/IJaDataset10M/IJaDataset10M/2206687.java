package com.javable.dataview.analysis;

import com.javable.dataview.DataChannel;
import com.javable.dataview.DataGroup;
import com.javable.dataview.DataStorage;
import com.javable.dataview.TimeChannel;

/**
 * Class that performes an averaging of several channels.
 */
public class ChannelAvrg {

    /**
     * Returns the average and standard deviation of the number of channels
     * 
     * @param storage DataStorage to operate on
     * @param chn channel number in every sweep to average
     */
    public static final void averageTraces(DataStorage storage, int chn) {
        double recs = 0;
        double n = 0;
        double sum = 0;
        double sumofsq = 0;
        double dt = 1.0;
        DataGroup group = null;
        DataChannel channel = null;
        try {
            for (int i = 0; i < storage.getGroupsSize(); i++) {
                channel = storage.getChannel(i, chn);
                if ((channel != null) && (channel.getAttribute().isNormal())) if (i == 0) dt = ((TimeChannel) storage.getXChannel(channel)).getRate();
                recs = Math.max(recs, channel.size());
            }
            DataChannel meanChannel = new DataChannel("Average", (int) recs);
            DataChannel deviationChannel = new DataChannel("Standard Deviation", (int) recs);
            for (int k = 0; k < recs; k++) {
                for (int i = 0; i < storage.getGroupsSize(); i++) {
                    group = storage.getGroup(i);
                    for (int j = 0; j < storage.getChannelsSize(i); j++) {
                        channel = storage.getChannel(i, chn);
                        if ((channel != null) && (channel.getAttribute().isNormal())) {
                            if (k < channel.size()) {
                                sum += channel.getData(k);
                                sumofsq += (channel.getData(k) * channel.getData(k));
                                n++;
                            }
                        }
                    }
                }
                meanChannel.setData(k, sum / n);
                deviationChannel.setData(k, Math.sqrt((sumofsq - sum / n * sum) / n));
                n = 0;
                sum = 0;
                sumofsq = 0;
            }
            group = new DataGroup("Average", "Channel average");
            group.addChannel(new TimeChannel(dt, (int) recs));
            group.addChannel(meanChannel);
            group.addChannel(deviationChannel);
            storage.addGroup(group);
        } catch (Exception e) {
        }
    }
}
