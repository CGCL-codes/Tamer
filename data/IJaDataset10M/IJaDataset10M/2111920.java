package model;

import controller.ProfileController;

public class ProfileModel extends AbstractModel {

    private int multiplier = 3;

    public void setMAD(Integer m) {
        int oldM = this.multiplier;
        this.multiplier = m;
        firePropertyChange(ProfileController.PROFILE_MAD_PROPERTY, oldM, m);
    }
}
