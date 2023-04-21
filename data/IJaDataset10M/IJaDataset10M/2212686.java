package mars.mp3player.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;

/**
 * 
 * @author Administrator
 *
 */
public class LrcProcessor {

    public ArrayList<Queue> process(InputStream inputStream) {
        Queue<Long> timeMills = new LinkedList<Long>();
        Queue<String> messages = new LinkedList<String>();
        ArrayList<Queue> queues = new ArrayList<Queue>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String temp = null;
            Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]");
            String result = null;
            while ((temp = reader.readLine()) != null) {
                Matcher match = pattern.matcher(temp);
                if (match.find()) {
                    String timeStr = match.group();
                    Long timeMill = time2long(timeStr.substring(1, timeStr.length() - 1));
                    timeMills.offer(timeMill);
                    String message = temp.substring(10);
                    result = message + "\n";
                    messages.add(result);
                }
            }
            messages.add(result);
            queues.add(timeMills);
            queues.add(messages);
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return queues;
    }

    /**
	 * @param timeStr
	 * @return
	 */
    private Long time2long(String timeStr) {
        String[] strs = timeStr.split(":");
        int minutes = Integer.parseInt(strs[0]);
        String[] millss = strs[1].split("\\.");
        int seconds = Integer.parseInt(millss[0]);
        int mill = Integer.parseInt(millss[1]);
        return minutes * 60 * 1000 + seconds * 1000 + mill * 10L;
    }
}
