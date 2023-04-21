package com.mdps.mactive;

import java.util.Date;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The MactiveAd class represents ad information from Mactive.
 *
 * It is a utility class to handle some common transformations.
 *
 * @author coordt
 *
 */
public class MactiveAd {

    public String adOrderNumber;

    public String adNumber;

    public String extAdNumber;

    public String pickupNumber;

    public String adType;

    public String adCategory;

    public Integer numColumns;

    public Integer adWidth;

    public Integer adDepth;

    public String modSize;

    public Boolean couponFlag;

    public String placement;

    public String position;

    public String sortText;

    public String invoiceText;

    public Integer colorCount;

    public String colorName;

    public String productionColors;

    public Integer standbyFlag;

    public Date pubDate;

    public Date startDate;

    public Date endDate;

    public String accountNumber;

    public Integer companyFlag;

    public String customerType;

    public String sicCode;

    public String customerName1;

    public String customerName2;

    public String addr1;

    public String addr2;

    public String city;

    public String state;

    public String zip;

    public String salesRep;

    public String adTaker;

    public String prodMethod;

    public Integer prodMethodType;

    public String prodComments;

    public Integer EPSBlobSize;

    public String EPSBlobData;

    public Integer HTMLBlobSize;

    public String HTMLBlobData;

    public Vector<String> pubnames = new Vector<String>(0);

    public Float latitude;

    public Float longitude;

    MactiveAd() {
    }

    /**
	 * A Constructor using a result set. It requests the field by name.
	 *
	 * @param rs
	 *            The ad query result set.
	 */
    MactiveAd(ResultSet rs) {
        try {
            adOrderNumber = rs.getString("adOrderNumber");
            adNumber = rs.getString("adNumber");
            extAdNumber = rs.getString("extAdNumber");
            pickupNumber = rs.getString("pickupNumber");
            adType = rs.getString("adType");
            adCategory = rs.getString("adCategory");
            numColumns = rs.getInt("numColumns");
            adWidth = rs.getInt("adWidth");
            adDepth = rs.getInt("adDepth");
            modSize = rs.getString("modSize");
            couponFlag = rs.getBoolean("couponFlag");
            placement = rs.getString("placement");
            position = rs.getString("position");
            sortText = rs.getString("sortText");
            invoiceText = rs.getString("invoiceText");
            colorCount = rs.getInt("colorCount");
            colorName = rs.getString("colorName");
            productionColors = rs.getString("productionColors");
            standbyFlag = rs.getInt("standbyFlag");
            pubDate = rs.getDate("pubDate");
            startDate = rs.getDate("startDate");
            endDate = rs.getDate("endDate");
            accountNumber = rs.getString("accountNumber");
            companyFlag = rs.getInt("companyFlag");
            customerType = rs.getString("customerType");
            sicCode = rs.getString("sicCode");
            customerName1 = rs.getString("customerName1");
            customerName2 = rs.getString("customerName2");
            addr1 = rs.getString("addr1");
            addr2 = rs.getString("addr2");
            city = rs.getString("city");
            state = rs.getString("state");
            zip = rs.getString("zip");
            salesRep = rs.getString("salesRep");
            adTaker = rs.getString("adTaker");
            prodMethod = rs.getString("prodMethod");
            prodMethodType = rs.getInt("prodMethodType");
            prodComments = rs.getString("prodComments");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            EPSBlobSize = rs.getInt("EPSBlobSize");
            EPSBlobData = rs.getString("EPSBlobData");
        } catch (SQLException e) {
        }
        try {
            HTMLBlobSize = rs.getInt("HTMLBlobSize");
            HTMLBlobData = rs.getString("HTMLBlobData");
        } catch (SQLException e) {
        }
        try {
            String pub = rs.getString("pubname");
            if (pub != null) pubnames.add(pub);
        } catch (SQLException e) {
        }
        try {
            latitude = rs.getFloat("latitude");
            longitude = rs.getFloat("longitude");
        } catch (SQLException e) {
        }
    }

    /**
	 * Return the width of the ad in inches instead of TWIPs
	 *
	 * @return the width in inches as a double
	 */
    public Double GetWidthAsInches() {
        double thewidth = (double) adWidth / 1440;
        return thewidth;
    }

    /**
	 * Return the depth of the ad in inches instead of TWIPs
	 *
	 * @return the depth in inches as a double
	 */
    public Double GetDepthAsInches() {
        double theDepth = (double) adDepth / 1440;
        return theDepth;
    }

    public static String StripLeadingZeros(String theString) {
        String out = theString;
        if (theString.length() != 0) while (out.substring(0, 1).equals("0")) {
            out = out.substring(1);
        }
        return out;
    }

    public String GetAdOrderNumber() {
        if (!extAdNumber.equals("") && extAdNumber != null) return extAdNumber; else return adNumber;
    }

    public String GetAdOrderNumber(Boolean stripLeadingZeros) {
        String adNumber = GetAdOrderNumber();
        if (stripLeadingZeros) {
            adNumber = StripLeadingZeros(adNumber);
        }
        return adNumber;
    }

    public Boolean IsInternalAd() {
        return (prodMethodType == 2);
    }

    public Boolean IsExternalAd() {
        return (prodMethodType == 4);
    }
}
