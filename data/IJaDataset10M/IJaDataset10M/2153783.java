package com.chauhai.sequencelogvisualizer.datafile.displaydata;

import com.chauhai.sequencelogvisualizer.display.Utils;
import com.chauhai.sequencelogvisualizer.display.displaydata.IDisplayData;
import com.chauhai.sequencelogvisualizer.display.displaydata.IMessageData;
import com.chauhai.sequencelogvisualizer.display.displaydata.ITargetObjectData;

/**
 * Class that contain sequence data to be displayed by Displayer.
 */
public class DisplayData implements IDisplayData {

    private ITargetObjectData[] targetObjectData;

    private IMessageData[] messageData;

    public DisplayData(ITargetObjectData[] targetObjectData, IMessageData[] messageData) {
        this.messageData = messageData;
        this.targetObjectData = targetObjectData;
    }

    public ITargetObjectData[] getTargetObjectData() {
        return targetObjectData;
    }

    public IMessageData[] getMessageData() {
        return messageData;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        String[] nameArray = new String[targetObjectData.length];
        for (int i = 0; i < targetObjectData.length; i++) {
            nameArray[i] = targetObjectData[i].getName();
        }
        str.append(Utils.join(nameArray, ", ")).append("\n");
        nameArray = new String[messageData.length];
        for (int i = 0; i < messageData.length; i++) {
            nameArray[i] = messageData[i].toString();
        }
        str.append(Utils.join(nameArray, "\n"));
        return str.toString();
    }
}
