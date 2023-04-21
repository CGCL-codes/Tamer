package com.garmin.fit;

public class DeviceSettingsMesg extends Mesg {

    public DeviceSettingsMesg() {
        super(Factory.createMesg(MesgNum.DEVICE_SETTINGS));
    }

    public DeviceSettingsMesg(final Mesg mesg) {
        super(mesg);
    }

    /**
    * Get utc_offset field
    * Comment: Offset from system time. Required to convert timestamp from system time to UTC.
    *
    * @return utc_offset
    */
    public Long getUtcOffset() {
        return getFieldLongValue(1);
    }

    /**
    * Set utc_offset field
    * Comment: Offset from system time. Required to convert timestamp from system time to UTC.
    *
    * @param utcOffset
    */
    public void setUtcOffset(Long utcOffset) {
        setFieldValue("utc_offset", utcOffset);
    }
}
