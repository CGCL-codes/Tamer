package it.hotel.model.price.manager;

import it.hotel.model.abstrakt.manager.IhManager;
import it.hotel.model.price.Price;
import it.hotel.model.typology.Typology;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public interface IPriceManager extends IhManager {

    public ArrayList<Price> getPricesOnDateRange(GregorianCalendar startDate, GregorianCalendar endDate, Typology typology);
}
