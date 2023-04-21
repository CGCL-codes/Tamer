package gawky.json;

public class Mapbuilder {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        String json = "{aaa : {erster: 11, zweiter: 22}}";
        JSONObject obj = new JSONObject(json);
        System.out.println(obj);
        System.out.println(obj.getJSONObject("aaa").getString("erster"));
        String path = "aaa.erster";
    }
}
