package fi.tuska.jalkametri.dao;

import java.util.Date;
import fi.tuska.jalkametri.gui.GraphView.Point;

public interface DailyDrinkStatistics extends Point {

    Date getDay();

    double getPortions();

    int getNumberOfDrinks();
}
