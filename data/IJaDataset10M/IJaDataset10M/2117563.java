package com.ubb.damate.service;

import java.util.Map;
import javax.ejb.Local;
import com.ubb.damate.model.EclipseUsageModel;
import com.ubb.damate.model.User;

@Local
public interface EclipseUsageServiceLocal {

    Map<User, EclipseUsageModel> getStats();

    EclipseUsageModel getStats(User user);
}
