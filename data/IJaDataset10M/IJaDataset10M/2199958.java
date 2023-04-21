package cn;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gdata.data.Feed;
import com.google.gdata.data.*;
import com.google.gdata.client.calendar.*;
import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.data.spreadsheet.*;

public class _________a_28_exel {

    public static String qq = "";

    public static void main(String[] args) throws Exception {
        System.out.println(".");
        SpreadsheetService myService = new SpreadsheetService("exampleCo-exampleApp-1");
        myService.setUserCredentials("33@quicklydone.com", "tverskoy");
        SpreadsheetFeed myFeed = myService.getFeed(new URL("http://spreadsheets.google.com/feeds/spreadsheets/private/full"), SpreadsheetFeed.class);
        System.out.println("> " + myFeed.getEntries().size());
        SpreadsheetEntry se = myFeed.getEntries().get(0);
        WorksheetEntry myWorksheet = se.getWorksheets().get(0);
        ListEntry myEntry = new ListEntry();
        myEntry.getCustomElements().setValueLocal("name1", "zzzz");
        myService.insert(myWorksheet.getListFeedUrl(), myEntry);
        System.out.println("ok");
    }
}
