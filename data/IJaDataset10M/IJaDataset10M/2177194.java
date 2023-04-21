package com.cameocontrol.cameo.control;

public class CameoSettings extends BasicSettings {

    public String getIDValue() {
        return "cameo";
    }

    public CameoSettings() {
        _mode = RecordMode.TRACKING;
        _totalChannels = 10;
        _totalDimmers = 512;
        _upTime = 4999;
        _downTime = 4999;
        _gotoCueTime = 1000;
        _ChannelsPerLine = 20;
        _ChannelGrouping = 5;
        _LineGrouping = 5;
        _showTitle = "";
        _showComment = "";
    }
}
