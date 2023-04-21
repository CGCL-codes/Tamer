package com.cincosoft.project.security.userGestion;

import java.util.List;
import javax.ejb.Local;
import javax.faces.model.SelectItem;

@Local
public interface UserGestion {

    public boolean isShowUser();

    public boolean isChangePass();

    public String getVerify();

    public void setVerify(String prueba);

    public String getClearPassword();

    public void setClearPassword(String clearPassword);

    public void getUsers();

    public void selectUser();

    public void changePassword();

    public void update();

    public void cancelUpdates();

    public void newUser();

    public void deleteUser();

    public Long getSystemProfile();

    public void setSystemProfile(Long s);

    public List<SelectItem> getSystemProfiles();

    public Long getUserProfile();

    public void setUserProfile(Long s);

    public List<SelectItem> getUserProfiles();

    public void addProfile();

    public void deleteProfile();

    public void destroy();
}
