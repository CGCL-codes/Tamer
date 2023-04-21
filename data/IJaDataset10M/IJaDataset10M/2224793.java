package v201201.forecastservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201201.DateTimeUtils;
import com.google.api.ads.dfp.v201201.CostType;
import com.google.api.ads.dfp.v201201.CreativePlaceholder;
import com.google.api.ads.dfp.v201201.Forecast;
import com.google.api.ads.dfp.v201201.ForecastServiceInterface;
import com.google.api.ads.dfp.v201201.InventoryTargeting;
import com.google.api.ads.dfp.v201201.LineItem;
import com.google.api.ads.dfp.v201201.LineItemType;
import com.google.api.ads.dfp.v201201.Size;
import com.google.api.ads.dfp.v201201.StartDateTimeType;
import com.google.api.ads.dfp.v201201.Targeting;
import com.google.api.ads.dfp.v201201.UnitType;

/**
 * This example gets a forecast for a prospective line item. To determine which
 * placements exist, run GetAllPlacementsExample.java.
 *
 * Tags: ForecastService.getForecast
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetForecastExample {

    public static void main(String[] args) throws Exception {
        DfpServiceLogger.log();
        DfpUser user = new DfpUser();
        ForecastServiceInterface forecastService = user.getService(DfpService.V201201.FORECAST_SERVICE);
        long[] targetedPlacementIds = new long[] { Long.parseLong("INSERT_PLACEMENT_ID_HERE") };
        InventoryTargeting inventoryTargeting = new InventoryTargeting();
        inventoryTargeting.setTargetedPlacementIds(targetedPlacementIds);
        Targeting targeting = new Targeting();
        targeting.setInventoryTargeting(inventoryTargeting);
        String endDateTime = "INSERT_END_DATE_TIME_HERE";
        LineItem lineItem = new LineItem();
        lineItem.setLineItemType(LineItemType.SPONSORSHIP);
        lineItem.setTargeting(targeting);
        CreativePlaceholder creativePlaceholder = new CreativePlaceholder();
        creativePlaceholder.setSize(new Size(300, 250, false));
        lineItem.setCreativePlaceholders(new CreativePlaceholder[] { creativePlaceholder });
        lineItem.setStartDateTimeType(StartDateTimeType.IMMEDIATELY);
        lineItem.setEndDateTime(DateTimeUtils.fromString(endDateTime));
        lineItem.setUnitType(UnitType.IMPRESSIONS);
        lineItem.setUnitsBought(50L);
        lineItem.setCostType(CostType.CPM);
        Forecast forecast = forecastService.getForecast(lineItem);
        long matched = forecast.getMatchedUnits();
        double availablePercent = (forecast.getAvailableUnits() / (matched * 1.0)) * 100;
        String unitType = forecast.getUnitType().toString().toLowerCase();
        System.out.println(matched + " " + unitType + " matched.\n" + availablePercent + "% " + unitType + " available.");
        if (forecast.getPossibleUnits() != null) {
            double possiblePercent = (forecast.getPossibleUnits() / (matched * 1.0)) * 100;
            System.out.println(possiblePercent + "% " + unitType + " possible.\n");
        }
    }
}
