package com.watchthelan.service.administrator;

import java.util.List;
import com.watchthelan.domain.Administrator;

public interface AdministratorManager {

    public List<Administrator> getAdministrators();

    public void addAdministrator(Administrator administrator);

    public void deleteAdministratorById(Long id);

    public void updateAdministrator(Administrator administrator);

    public Administrator getAdministratorById(Long id);

    public Administrator getAdministratorByUsername(String username);
}
