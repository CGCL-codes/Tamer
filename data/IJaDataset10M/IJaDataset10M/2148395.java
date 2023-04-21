package my.learning.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import my.learning.domain.Group;
import my.learning.domain.User;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import net.sf.json.xml.XMLSerializer;

public class JsonTest extends TestCase {

    public void testArray2Json() {
        boolean[] boolArray = new boolean[] { true, false, true };
        JSONArray jsonArray = JSONArray.fromObject(boolArray);
        System.out.println(jsonArray);
    }

    public void testList2Json() {
        List<String> list = new ArrayList<String>();
        list.add("first");
        list.add("second");
        list.add("third");
        JSONArray jsonArray = JSONArray.fromObject(list);
        System.out.println(jsonArray);
    }

    public void testMap2Json() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "json");
        map.put("bool", Boolean.TRUE);
        map.put("int", new Integer(1));
        map.put("arr", new String[] { "a", "b" });
        map.put("function", "function(i){ return this.arr[i]; }");
        JSONObject jsonObject = JSONObject.fromObject(map);
        System.out.println(jsonObject);
    }

    public void testObject2Json() {
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setPassword("admin");
        user.setRegistrationDate(new Date());
        JSONObject jsonObject = JSONObject.fromObject(user);
        System.out.println(jsonObject);
    }

    public void testObject2Json2() {
        String[] formats = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor(formats[1]));
        jsonConfig.registerJsonValueProcessor("createDate", new DateJsonValueProcessor(formats[0]));
        jsonConfig.registerJsonValueProcessor("registrationDate", new DateJsonValueProcessor(formats[1]));
        jsonConfig.registerJsonValueProcessor(Group.class, Date.class, new DateJsonValueProcessor(formats[1]));
        jsonConfig.registerJsonValueProcessor(User.class, Date.class, new DateJsonValueProcessor(formats[0]));
        jsonConfig.registerJsonValueProcessor(Group.class, "createDate", new DateJsonValueProcessor(formats[0]));
        jsonConfig.registerJsonValueProcessor(User.class, "registrationDate", new DateJsonValueProcessor(formats[1]));
        Group group = new Group();
        group.setId(1);
        group.setName("manager");
        group.setCreateDate(new Date());
        JSONObject jsonObject = JSONObject.fromObject(group, jsonConfig);
        System.out.println(jsonObject);
        User user = new User();
        user.setId(2);
        user.setUsername("manager");
        user.setPassword("manager");
        user.setRegistrationDate(new Date());
        jsonObject = JSONObject.fromObject(user, jsonConfig);
        System.out.println(jsonObject);
    }

    public void testJson2Object() {
        String json = "{\"id\":1,\"username\":\"admin\",\"password\":\"admin\",\"registrationDate\":{\"date\":23,\"day\":6,\"hours\":23,\"minutes\":40,\"month\":0,\"seconds\":10,\"time\":1264261210500,\"timezoneOffset\":-480,\"year\":110}}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        User user = (User) JSONObject.toBean(jsonObject, User.class);
        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getRegistrationDate());
    }

    public void testJson2Object2() {
        String[] formats = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };
        String json = "{\"id\":2,\"username\":\"manager\",\"password\":\"manager\",\"registrationDate\":\"2010-01-29 23:40:21\"}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(formats));
        User user = (User) JSONObject.toBean(jsonObject, User.class);
        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getRegistrationDate());
    }

    public void testJson2Object3() {
        String[] formats = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };
        String json = "{\"id\":3,\"username\":\"user\",\"password\":\"user\",\"registrationDate\":\"2010-01-29\"}";
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(formats));
        User user = (User) JSONObject.toBean(jsonObject, User.class);
        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getRegistrationDate());
    }

    public void testJson2Xml() {
        JSONObject jsonObject = new JSONObject(true);
        XMLSerializer xmlSerializer = new XMLSerializer();
        String xml = xmlSerializer.write(jsonObject);
        System.out.println(xml);
        jsonObject = JSONObject.fromObject("{\"name\":\"json\",\"bool\":true,\"int\":1}");
        xml = xmlSerializer.write(jsonObject);
        System.out.println(xml);
        JSONArray jsonArray = JSONArray.fromObject("[1,2,3]");
        xml = xmlSerializer.write(jsonArray);
        System.out.println(xml);
    }

    public void testXml2Json() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<a class=\"array\">" + "<e type=\"function\" params=\"i,j\">return matrix[i][j];</e>" + "</a>";
        XMLSerializer xmlSerializer = new XMLSerializer();
        JSONArray jsonArray = (JSONArray) xmlSerializer.read(xml);
        System.out.println(jsonArray);
    }
}
