package com.dushyant.framework;

import java.sql.ResultSet;
import java.util.ArrayList;
import com.arm.framework.sql.MySql;
import com.dushyant.framework.bean.Log;
import com.dushyant.framework.bean.Tweets;

public class Test {

    public static void main(String[] args) {
        try {
            MySql<Tweets> sql = new MySql<Tweets>();
            ResultSet selectall = sql.select("SELECT * FROM tweets where tweet not like '%@%'");
            ArrayList<Tweets> convert = Tweets.create().convert(selectall);
            int i = 0;
            for (Tweets tweets : convert) {
                Log.put(tweets.getTweet());
                System.out.println(i++ + "");
            }
        } catch (Exception e) {
        }
    }
}
