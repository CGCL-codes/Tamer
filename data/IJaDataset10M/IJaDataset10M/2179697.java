package v201201.lineitemservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201201.DateTimeUtils;
import com.google.api.ads.dfp.v201201.CostType;
import com.google.api.ads.dfp.v201201.CreativePlaceholder;
import com.google.api.ads.dfp.v201201.CreativeRotationType;
import com.google.api.ads.dfp.v201201.DeviceManufacturerTargeting;
import com.google.api.ads.dfp.v201201.InventoryTargeting;
import com.google.api.ads.dfp.v201201.LineItem;
import com.google.api.ads.dfp.v201201.LineItemServiceInterface;
import com.google.api.ads.dfp.v201201.LineItemType;
import com.google.api.ads.dfp.v201201.MobileDeviceSubmodelTargeting;
import com.google.api.ads.dfp.v201201.MobileDeviceTargeting;
import com.google.api.ads.dfp.v201201.Money;
import com.google.api.ads.dfp.v201201.Size;
import com.google.api.ads.dfp.v201201.StartDateTimeType;
import com.google.api.ads.dfp.v201201.TargetPlatform;
import com.google.api.ads.dfp.v201201.Targeting;
import com.google.api.ads.dfp.v201201.Technology;
import com.google.api.ads.dfp.v201201.TechnologyTargeting;
import com.google.api.ads.dfp.v201201.UnitType;

/**
 * This example create a new line item to serve to the mobile platform. Mobile
 * features needs to be enabled in your account to use mobile targeting. To
 * determine which line items exist, run GetAllLineItemsExample.java. To
 * determine which orders exist, run GetAllOrdersExample.java. To determine
 * which placements exist, run GetAllPlacementsExample.java.
 *
 * Tags: LineItemService.createLineItem
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class CreateMobileLineItemExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            LineItemServiceInterface lineItemService = user.getService(DfpService.V201201.LINEITEM_SERVICE);
            Long orderId = Long.parseLong("INSERT_ORDER_ID_HERE");
            long[] targetedPlacementIds = new long[] { Long.parseLong("INSERT_MOBILE_PLACEMENT_ID_HERE") };
            InventoryTargeting inventoryTargeting = new InventoryTargeting();
            inventoryTargeting.setTargetedPlacementIds(targetedPlacementIds);
            TechnologyTargeting technologyTargeting = new TechnologyTargeting();
            DeviceManufacturerTargeting deviceManufacturerTargeting = new DeviceManufacturerTargeting();
            deviceManufacturerTargeting.setIsTargeted(true);
            Technology deviceManufacturerTechnology = new Technology();
            deviceManufacturerTechnology.setId(40100L);
            deviceManufacturerTargeting.setDeviceManufacturers(new Technology[] { deviceManufacturerTechnology });
            technologyTargeting.setDeviceManufacturerTargeting(deviceManufacturerTargeting);
            MobileDeviceTargeting mobileDeviceTargeting = new MobileDeviceTargeting();
            Technology mobileDeviceTechnology = new Technology();
            mobileDeviceTechnology.setId(604046L);
            mobileDeviceTargeting.setExcludedMobileDevices(new Technology[] { mobileDeviceTechnology });
            technologyTargeting.setMobileDeviceTargeting(mobileDeviceTargeting);
            MobileDeviceSubmodelTargeting mobileDeviceSubmodelTargeting = new MobileDeviceSubmodelTargeting();
            Technology mobileDeviceSubmodelTechnology = new Technology();
            mobileDeviceSubmodelTechnology.setId(640003L);
            mobileDeviceSubmodelTargeting.setTargetedMobileDeviceSubmodels(new Technology[] { mobileDeviceSubmodelTechnology });
            technologyTargeting.setMobileDeviceSubmodelTargeting(mobileDeviceSubmodelTargeting);
            Targeting targeting = new Targeting();
            targeting.setInventoryTargeting(inventoryTargeting);
            targeting.setTechnologyTargeting(technologyTargeting);
            LineItem lineItem = new LineItem();
            lineItem.setName("Mobile line item");
            lineItem.setOrderId(orderId);
            lineItem.setTargeting(targeting);
            lineItem.setLineItemType(LineItemType.STANDARD);
            lineItem.setAllowOverbook(true);
            lineItem.setTargetPlatform(TargetPlatform.MOBILE);
            lineItem.setCreativeRotationType(CreativeRotationType.EVEN);
            CreativePlaceholder creativePlaceholder = new CreativePlaceholder();
            creativePlaceholder.setSize(new Size(300, 250, false));
            lineItem.setCreativePlaceholders(new CreativePlaceholder[] { creativePlaceholder });
            lineItem.setStartDateTimeType(StartDateTimeType.IMMEDIATELY);
            lineItem.setEndDateTime(DateTimeUtils.fromString("2012-09-01T00:00:00"));
            lineItem.setCostType(CostType.CPM);
            lineItem.setCostPerUnit(new Money("USD", 2000000L));
            lineItem.setUnitsBought(500000L);
            lineItem.setUnitType(UnitType.IMPRESSIONS);
            lineItem = lineItemService.createLineItem(lineItem);
            if (lineItem != null) {
                System.out.println("A line item with ID \"" + lineItem.getId() + "\", belonging to order ID \"" + lineItem.getOrderId() + "\", and named \"" + lineItem.getName() + "\" was created.");
            } else {
                System.out.println("No line item created.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
