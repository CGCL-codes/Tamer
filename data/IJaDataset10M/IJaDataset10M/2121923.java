package de.rowbuddy.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import de.rowbuddy.boundary.dtos.MonthsStatisticDTO;
import de.rowbuddy.exceptions.RowBuddyException;

public interface StatisticRemoteService extends RemoteService {

    public MonthsStatisticDTO getMonthsStatistic(int year) throws RowBuddyException;
}
