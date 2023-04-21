package com.szxys.mhub.subsystem.mets.db;

import com.szxys.mhub.subsystem.mets.bean.Drinkandurine;
import com.szxys.mhub.subsystem.mets.bean.Sysconfig;
import com.szxys.mhub.subsystem.mets.utils.TimeUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * 数据操作类(执行增、删、改、查)
 * 业务需要的数据均由【刀】com.szxys.mhub.subsystem.mets.dao提供
 * @author Administrator
 *
 */
public class DrinkAndUrine {

    private static final String TableName = MetsData.Tables.DrinkUrine;

    public static ArrayList<Drinkandurine> Select(Context context, String where, String[] whereArgs, String groupBy, String having, String orderBy) {
        ArrayList<Drinkandurine> list = new ArrayList<Drinkandurine>();
        MetsDbHelper dbHelper = new MetsDbHelper(context);
        Drinkandurine entity = null;
        String[] columns = { "c_Id", "c_Units", "c_DateTime", "c_Quantity", "c_Type", "c_IsUpload", "c_Proportion", "c_UfrId", "c_CollectType", "c_UniqueId", "c_Status" };
        Cursor cursor = dbHelper.Select(TableName, columns, where, whereArgs, groupBy, having, orderBy);
        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                entity = new Drinkandurine();
                entity.setC_Id(cursor.getInt(0));
                entity.setC_Units(cursor.getInt(1));
                entity.setC_DateTime(cursor.getString(2));
                entity.setC_Quantity(cursor.getFloat(3));
                entity.setC_Type(cursor.getInt(4));
                entity.setC_IsUpload(cursor.getInt(5));
                entity.setC_Proportion(cursor.getFloat(6));
                entity.setC_UfrId(cursor.getInt(7));
                entity.setC_CollectType(cursor.getInt(8));
                entity.setC_UniqueId(cursor.getString(9));
                entity.setC_Status(cursor.getInt(10));
                list.add(entity);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            dbHelper.close();
        }
        return list;
    }

    public static long Insert(Context context, Drinkandurine objInAndOut) {
        long result = -2;
        if (objInAndOut.getC_Type() == 1) {
            if (objInAndOut.getC_UfrId() > 0) {
                if (!existSameRecord(context, objInAndOut.getC_DateTime())) {
                    new UrineProportion();
                    String latestProportionString = UrineProportion.getLatestProportion(context, objInAndOut.getC_DateTime());
                    if (latestProportionString.trim().length() > 0) {
                        objInAndOut.setC_Proportion(Float.parseFloat(latestProportionString));
                    }
                    result = new MetsDbHelper(context).Add(TableName, getContentValues(objInAndOut));
                }
            } else {
                result = new MetsDbHelper(context).Add(TableName, getContentValues(objInAndOut));
            }
        } else {
            result = new MetsDbHelper(context).Add(TableName, getContentValues(objInAndOut));
        }
        Log.d(TableName, "INSERT into[" + TableName + "]@" + result);
        return result;
    }

    public static int Update(Context context, ContentValues values, String where, String[] whereArgs) {
        int result = new MetsDbHelper(context).Update(TableName, values, where, whereArgs);
        Log.d(TableName, "MODIFY success");
        return result;
    }

    public static int Delete(Context context, String where, String[] whereArgs) {
        int result = new MetsDbHelper(context).Delete(TableName, where, whereArgs);
        Log.d(TableName, "DELETE success");
        return result;
    }

    public static int deleteByCid(Context context, int cid) {
        String where = "c_Id=?";
        String[] whereArgs = { String.valueOf(cid) };
        int result = new MetsDbHelper(context).Delete(TableName, where, whereArgs);
        Log.d(TableName, "DELETE success @c_Id=" + cid);
        return result;
    }

    public static long getDataCount(Context context, String where, String[] whereArgs, String orderBy) {
        return new MetsDbHelper(context).getTableCount(TableName, where, whereArgs, orderBy);
    }

    private static ContentValues getContentValues(Drinkandurine objInAndOut) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("c_DateTime", objInAndOut.getC_DateTime());
        contentValues.put("c_Quantity", objInAndOut.getC_Quantity());
        contentValues.put("c_Type", objInAndOut.getC_Type());
        contentValues.put("c_Proportion", objInAndOut.getC_Proportion());
        contentValues.put("c_UfrId", objInAndOut.getC_UfrId());
        contentValues.put("c_UniqueId", objInAndOut.getC_UniqueId());
        return contentValues;
    }

    public static boolean existSameRecord(Context context, String datetimeString) {
        boolean existRecord = false;
        if (datetimeString.trim().length() > 10) {
            String[] columns = { "c_Id", "c_DateTime", "c_Type" };
            String selection = "c_DateTime=? and c_Type=1";
            String[] selectionArgs = { datetimeString };
            MetsDbHelper dbHelper = new MetsDbHelper(context);
            Cursor cursor = dbHelper.Select(TableName, columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                existRecord = true;
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                dbHelper.close();
            }
        }
        return existRecord;
    }

    public static ArrayList<String> getDrinkAndUrineInfoByCid(Context context, String Cid) {
        ArrayList<String> urinedetails = new ArrayList<String>();
        MetsDbHelper dbHelper = new MetsDbHelper(context);
        String[] columns = { "c_Id", "c_Units", "c_DateTime", "c_Quantity", "c_Type" };
        String where = "c_Id=?";
        String[] whereArgs = { Cid.trim() };
        Cursor cursor = dbHelper.Select(TableName, columns, where, whereArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                StringBuilder sbDetails = new StringBuilder();
                sbDetails.append(cursor.getString(2));
                sbDetails.append("#");
                sbDetails.append(cursor.getString(3));
                sbDetails.append("#");
                sbDetails.append(cursor.getString(4));
                urinedetails.add(sbDetails.toString());
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            dbHelper.close();
        }
        return urinedetails;
    }

    public static Drinkandurine getInfoByCid(Context context, int cid) {
        Drinkandurine objInAndOut = null;
        MetsDbHelper dbHelper = new MetsDbHelper(context);
        String[] columns = { "c_Id", "c_Units", "c_DateTime", "c_Quantity", "c_Type", "c_IsUpload", "c_Proportion", "c_UfrId", "c_CollectType", "c_UniqueId", "c_Status" };
        String where = "c_Id=?";
        String[] whereArgs = { String.valueOf(cid) };
        Cursor cursor = dbHelper.Select(TableName, columns, where, whereArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            objInAndOut = new Drinkandurine();
            objInAndOut.setC_Id(cid);
            objInAndOut.setC_Units(cursor.getInt(1));
            objInAndOut.setC_DateTime(cursor.getString(2));
            objInAndOut.setC_Quantity(cursor.getFloat(3));
            objInAndOut.setC_Type(cursor.getInt(4));
            objInAndOut.setC_IsUpload(cursor.getInt(5));
            objInAndOut.setC_Proportion(cursor.getFloat(6));
            objInAndOut.setC_UfrId(cursor.getInt(7));
            objInAndOut.setC_CollectType(cursor.getInt(8));
            objInAndOut.setC_UniqueId(cursor.getString(9));
            objInAndOut.setC_Status(cursor.getInt(10));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            dbHelper.close();
        }
        return objInAndOut;
    }

    public static ArrayList<String> getMetsStatistics(Context context, String dateFrom, String dateEnd) {
        ArrayList<String> urineStatistics = new ArrayList<String>();
        ArrayList<String> urineCalendars = new ArrayList<String>();
        ArrayList<Float> urinFloats = new ArrayList<Float>();
        ArrayList<String> urineCategories = new ArrayList<String>();
        String[] columns = { "c_Id", "c_DateTime", "c_Quantity", "c_Type", "c_Proportion" };
        String selection = "c_DateTime>=? and c_DateTime<=? and c_Type>0";
        String[] selectionArgs = { dateFrom + " 00:00:00", dateEnd + " 23:59:59" };
        StringBuilder stringBuilder = null;
        String getUpDateString = "";
        String gotobedDateString = "";
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDt = new Date();
        Date endDt = new Date();
        try {
            startDt = curFormater.parse(dateFrom);
            endDt = curFormater.parse(dateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calStart.setTime(startDt);
        calEnd.setTime(endDt);
        Calendar calEndNext = Calendar.getInstance();
        calEndNext.setTime(endDt);
        calEndNext.add(Calendar.DATE, 1);
        String stopString = curFormater.format(calEndNext.getTime());
        String endNextday = GotobedGetup.getGetupTimeByDateString(context, stopString);
        selectionArgs[1] = endNextday;
        MetsDbHelper dbHelper = new MetsDbHelper(context);
        Cursor cursor = dbHelper.Select(TableName, columns, selection, selectionArgs, null, null, null);
        Calendar calTmp = (Calendar) calStart.clone();
        Sysconfig objConfig = SysConfig.getSysConfigObj(context);
        String[] defaultTimes = new String[] { " 12:00:00", " 23:59:59" };
        if (objConfig != null) {
            if (objConfig.getC_GetUpAlarm() != null && objConfig.getC_GetUpAlarm().trim().length() > 0) {
                defaultTimes[0] = " " + objConfig.getC_GetUpAlarm().trim();
            }
            if (objConfig.getC_GotoBedAlarm() != null && objConfig.getC_GotoBedAlarm().trim().length() > 0) {
                defaultTimes[1] = " " + objConfig.getC_GotoBedAlarm().trim();
            }
        }
        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                urineCalendars.add(cursor.getString(1));
                urinFloats.add(cursor.getFloat(2));
                urineCategories.add(cursor.getString(3));
            }
            while (calTmp.getTime().compareTo(calEnd.getTime()) <= 0) {
                int dayUrineCount = 0;
                int nightUrineCount = 0;
                int totalUrineCount = 0;
                int dayUrineQuantity = 0;
                int nightUrineQuantity = 0;
                int UrineTotalQuantity = 0;
                float nightOccupation = 0;
                int urineEmergentTimes = 0;
                int urineLossTimes = 0;
                Date recordDate = calTmp.getTime();
                stringBuilder = new StringBuilder();
                String tmpDay = new SimpleDateFormat("yyyy/MM/dd").format(recordDate);
                stringBuilder.append(tmpDay);
                stringBuilder.append("#");
                String[] GetupGotobedStrings = GotobedGetup.getThedayTimes(context, new SimpleDateFormat("yyyy-MM-dd").format(recordDate));
                Calendar calNextDay = (Calendar) calTmp.clone();
                calNextDay.add(Calendar.DATE, 1);
                String nextDateString = new SimpleDateFormat("yyyy-MM-dd").format(calNextDay.getTime().getTime());
                String nextGetupDate = GotobedGetup.getGetupTimeByDateString(context, nextDateString);
                if (nextGetupDate.trim().length() < 10) {
                    nextGetupDate = nextDateString + defaultTimes[0];
                } else {
                    nextGetupDate = changeDateTime(nextGetupDate, "min", -10);
                }
                if (GetupGotobedStrings[0].trim().length() > 10 || GetupGotobedStrings[1].trim().length() > 10) {
                    Log.d("UrineAndDrink", "Emergent=" + urineEmergentTimes + " && Losss=" + urineLossTimes);
                    for (String currentDate : urineCalendars) {
                        int currentIndex = urineCalendars.indexOf(currentDate);
                        String currentCategory = urineCategories.get(currentIndex);
                        if (GetupGotobedStrings[0].trim().length() > 10 && GetupGotobedStrings[1].trim().length() > 10) {
                            if (isInTimeRange(currentDate, changeDateTime(GetupGotobedStrings[1].trim(), "min", 10), changeDateTime(nextGetupDate, "min", -10))) {
                                if (currentCategory.equalsIgnoreCase("2")) {
                                    urineEmergentTimes++;
                                } else {
                                    nightUrineCount++;
                                    totalUrineCount++;
                                    nightUrineQuantity += urinFloats.get(currentIndex);
                                    UrineTotalQuantity += urinFloats.get(currentIndex);
                                    if (currentCategory.equalsIgnoreCase("3")) {
                                        urineLossTimes++;
                                    }
                                }
                            }
                            if (isInTimeRange(currentDate, changeDateTime(GetupGotobedStrings[0].trim(), "min", -10), changeDateTime(GetupGotobedStrings[1].trim(), "min", 10))) {
                                if (currentCategory.equalsIgnoreCase("2")) {
                                    urineEmergentTimes++;
                                } else {
                                    dayUrineCount++;
                                    totalUrineCount++;
                                    dayUrineQuantity += urinFloats.get(currentIndex);
                                    UrineTotalQuantity += urinFloats.get(currentIndex);
                                    if (currentCategory.equalsIgnoreCase("3")) {
                                        urineLossTimes++;
                                    }
                                }
                            }
                        } else {
                            if (GetupGotobedStrings[0].trim().length() > 10) {
                                if (isInTimeRange(currentDate, GetupGotobedStrings[0].trim(), nextGetupDate)) {
                                    if (currentCategory.equalsIgnoreCase("2")) {
                                        urineEmergentTimes++;
                                        Log.d("UrineEmergent", "Emergent=" + urineEmergentTimes + " @" + currentDate);
                                    } else {
                                        dayUrineCount++;
                                        totalUrineCount++;
                                        dayUrineQuantity += urinFloats.get(currentIndex);
                                        UrineTotalQuantity += urinFloats.get(currentIndex);
                                        if (currentCategory.equalsIgnoreCase("3")) {
                                            urineLossTimes++;
                                            Log.d("UrineLoss", "LossQ=" + urineLossTimes + " @" + currentDate);
                                        }
                                    }
                                }
                            }
                            if (GetupGotobedStrings[1].trim().length() > 10) {
                                if (isInTimeRange(currentDate, gotobedDateString, nextGetupDate)) {
                                    if (currentCategory.equalsIgnoreCase("2")) {
                                        urineEmergentTimes++;
                                    } else {
                                        nightUrineCount++;
                                        totalUrineCount++;
                                        nightUrineQuantity += urinFloats.get(currentIndex);
                                        UrineTotalQuantity += urinFloats.get(currentIndex);
                                        if (currentCategory.equalsIgnoreCase("3")) {
                                            urineLossTimes++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.e("UrineAndDrink", "Getup && Sleep time null");
                }
                stringBuilder.append(dayUrineCount);
                stringBuilder.append("/");
                stringBuilder.append(nightUrineCount);
                stringBuilder.append("/");
                stringBuilder.append(totalUrineCount);
                stringBuilder.append("#");
                stringBuilder.append(dayUrineQuantity);
                stringBuilder.append("/");
                stringBuilder.append(nightUrineQuantity);
                stringBuilder.append("/");
                stringBuilder.append(UrineTotalQuantity);
                stringBuilder.append("#");
                if (nightUrineQuantity > 0 && UrineTotalQuantity > 0) {
                    nightOccupation = 100 * nightUrineQuantity / UrineTotalQuantity;
                } else {
                    Log.e("UrineAndDrink", "nightUrineQuantity or UrineTotalQuantity=0");
                }
                stringBuilder.append(nightOccupation);
                stringBuilder.append("%");
                stringBuilder.append("#");
                stringBuilder.append(urineEmergentTimes);
                stringBuilder.append("#");
                stringBuilder.append(urineLossTimes);
                urineStatistics.add(stringBuilder.toString());
                calTmp.add(Calendar.DATE, 1);
            }
        } else {
            while (calTmp.getTime().compareTo(calEnd.getTime()) <= 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(new SimpleDateFormat("yyyy/MM/dd").format(calTmp.getTime()));
                stringBuilder.append("#");
                stringBuilder.append(0);
                stringBuilder.append("/");
                stringBuilder.append(0);
                stringBuilder.append("/");
                stringBuilder.append(0);
                stringBuilder.append("#");
                stringBuilder.append(0);
                stringBuilder.append("/");
                stringBuilder.append(0);
                stringBuilder.append("/");
                stringBuilder.append(0);
                stringBuilder.append("#");
                stringBuilder.append(0);
                stringBuilder.append("%");
                stringBuilder.append("#");
                stringBuilder.append(0);
                stringBuilder.append("#");
                stringBuilder.append(0);
                urineStatistics.add(stringBuilder.toString());
                calTmp.add(Calendar.DATE, 1);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            dbHelper.close();
        }
        return urineStatistics;
    }

    private static boolean isInTimeRange(String currentDate, String startDate, String endDate) {
        boolean isInRange = false;
        Date thisDate = new Date();
        Date startDt = new Date();
        Date endDt = new Date();
        if (startDate.trim().length() > 0 && endDate.trim().length() > 0) {
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                thisDate = curFormater.parse(currentDate.trim());
                startDt = curFormater.parse(startDate.trim());
                endDt = curFormater.parse(endDate.trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (thisDate.compareTo(startDt) >= 0 && thisDate.compareTo(endDt) <= 0) {
                isInRange = true;
            }
        }
        return isInRange;
    }

    private static String changeDateTime(String datetime, String timespan, int trans) {
        String nextDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateNext;
        try {
            dateNext = formatter.parse(datetime);
            Calendar calNextGetup = Calendar.getInstance();
            calNextGetup.setTime(dateNext);
            if (timespan.equalsIgnoreCase("month")) {
                calNextGetup.add(Calendar.MONTH, trans);
            }
            if (timespan.equalsIgnoreCase("day")) {
                calNextGetup.add(Calendar.DATE, trans);
            }
            if (timespan.equalsIgnoreCase("minute") || timespan.equalsIgnoreCase("min")) {
                calNextGetup.add(Calendar.MINUTE, trans);
            }
            if (timespan.equalsIgnoreCase("second")) {
                calNextGetup.add(Calendar.SECOND, trans);
            }
            nextDate = formatter.format(calNextGetup.getTime().getTime());
        } catch (ParseException e) {
            nextDate = datetime;
            e.printStackTrace();
        }
        return nextDate;
    }
}
