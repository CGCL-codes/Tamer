package com.ubb.damate.service;

import java.util.Map;
import javax.ejb.Local;
import com.ubb.damate.model.RunTimesModel;
import com.ubb.damate.model.User;

@Local
public interface RunTimesServiceLocal {

    Map<User, RunTimesModel> getStats();

    RunTimesModel getStats(String username);
}
