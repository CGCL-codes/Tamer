package com.pcmsolutions.device.EMU.E4.preset;

import com.pcmsolutions.device.EMU.E4.remote.SampleHeader;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 13-Sep-2003
 * Time: 22:57:51
 * To change this template use Options | File Templates.
 */
public interface SampleDescriptor extends SampleHeader, Serializable {

    public short getPitch();

    public short getPitchFraction();

    public int getSizeInBytes();

    public String getFormattedSize();

    public double getSampleRateInKhz();

    public String getFormattedSampleRateInKhz();

    public String getFormattedDurationInSeconds();

    public String getChannelDescription();

    public String getName();
}
