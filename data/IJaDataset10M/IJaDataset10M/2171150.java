package com.unical.ae.gullivertravels.client;

import java.util.Date;
import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.unical.ae.gullivertravels.dto.Area;
import com.unical.ae.gullivertravels.dto.City;
import com.unical.ae.gullivertravels.dto.Cruise;

@RemoteServiceRelativePath("managecruise")
public interface ManageCruiseService extends RemoteService {

    void deleteCruise(Long id);

    List<Cruise> findAllCruises();

    List<Cruise> findByCity(City city);

    List<Cruise> findByCompany(String company);

    List<Cruise> findByDepartureDate(Date date);

    List<Cruise> findByDuration(int duration);

    List<Cruise> findByName(String name);

    List<Cruise> findByPriceEqual(Double price);

    List<Cruise> findByPriceGreater(Double price);

    List<Cruise> findByPriceLess(Double price);

    Cruise getCruise(Long id);

    boolean insertCruise(Double adultsPrice, Area area, Double childrenPrice, String company, City departureCity, Date departureDate, Integer duration, String name, Integer passengersCapabilities, Integer passengersNumbers);

    boolean updateCruise(Double adultsPrice, Area area, Double childrenPrice, String company, City departureCity, Date departureDate, Integer duration, Long id, String name, Integer passengersCapabilities, Integer passengersNumbers);
}
