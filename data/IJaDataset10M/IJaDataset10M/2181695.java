package ru.highcraft.apis.littlesms;

import java.util.Date;
import java.util.TimeZone;

/**
 * User: neoasket
 * Date: 2010-09-19
 * Time: 15:08:19
 */
public interface LittleSms {

    public final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone("UTC");

    public LittleSmsResponse sendSms(String recipients, String message, String sender, Boolean test) throws Exception;

    public LittleSmsResponse checkBalance() throws Exception;

    public LittleSmsResponse getStatus(int[] messages_id) throws Exception;

    public LittleSmsResponse getPrice(String recipients, String message) throws Exception;

    public LittleSmsResponse getHistory(Integer history_id, String recipient, String sender, String status, Date date_from, Date date_to, Integer id) throws Exception;
}
