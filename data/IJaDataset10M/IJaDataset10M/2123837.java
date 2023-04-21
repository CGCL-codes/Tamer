package gc3d.test;

import java.util.Calendar;
import org.json.simple.JSONObject;

public class TestJSON1 {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        JSONObject obj = new JSONObject();
        obj.put("name", "foo");
        obj.put("num", new Integer(100));
        obj.put("balance", new Double(1000.21));
        obj.put("is_vip", new Boolean(true));
        obj.put("nickname", null);
        System.out.print(obj);
        System.out.print("time\":\"" + Calendar.getInstance().getTimeInMillis());
    }
}
