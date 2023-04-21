package cn.poco.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	public  static String getDate(String date)throws Exception{
		
		String dateBefore = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		long l1 =  sdf.parse(date).getTime();
		long l2 = System.currentTimeMillis();
		long l3 = (l2-l1)/(1000*60);
		String currentDate = sdf.format(new Date(l2));
		if(l3 <= 0){
			dateBefore = "1分钟前";
		
		}
		else if(l3 >0 && l3<60){
			dateBefore = l3 + "分钟前";
			
		}
		else if(l3 >= 60 && l3 < (60*24)){
			l3 = l3/60;
			dateBefore = l3 + "小时前";
			
		}else if(l3 >= (60*24) && l3 < (60*24*3)){
			int ii = new Integer(currentDate.substring(8, 10)) - new Integer(date.substring(8, 10));
			if(ii == 1){
				dateBefore = "昨天";
			}else if(ii == 2){
				dateBefore = "前天";
			}else{
				dateBefore = date.substring(0, 10);
			}			
		}else{
			dateBefore = date.substring(0, 10);
		}
		return dateBefore;
	}
	public  static long parseTimeToLong(String time)throws Exception{		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		long longTime =  sdf.parse(time).getTime();
		long ltime = longTime/1000;
		return ltime;		
	}
	public static String topicTimeFormat(String time)throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		long longTime =  sdf.parse(time).getTime();
		long now = System.currentTimeMillis();
		if(longTime - now > 0){
			return "征集中";
		}else{
			return "已过时";
		}
	}
	
	public static String getCurrentDate(long currentDate){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(currentDate);
		return simpleDateFormat.format(date);
	}
}
