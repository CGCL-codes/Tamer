package de.gruessing.gwtsports.shared.mappeddata.tcdata;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FirstSportDTO implements IsSerializable {

    protected ActivityDTO activity;

    public FirstSportDTO() {
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO value) {
        this.activity = value;
    }
}
