package websiteschema.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import websiteschema.common.amqp.Message;
import org.junit.Test;
import static websiteschema.utils.PojoMapper.*;

/**
 *
 * @author ray
 */
public class JSONTest {

    @Test
    public void testMessage() {
        try {
            String json = "{\"url\":\"http://localhost:8080/docs\",\"depth\":0,\"createTime\":1323147713158,\"jobId\":4,\"startURLId\":1,\"configure\":\"b=c\\n# Test Info\",\"wrapperId\":1}";
            Message msg = fromJson(json, Message.class);
            System.out.println(msg.getConfigure());
            assert (msg.getJobId() == 4L);
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void testMap() {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("a", "b");
            map.put("c", "d");
            List list = new ArrayList();
            list.add(map);
            String json = toJson(list);
            System.out.println(json);
            List<Map<String, String>> res = fromJson(json, List.class);
            System.out.println(res);
            assert (res.get(0).get("a").equals("b"));
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void testPrettyPrint() {
        try {
            String json = "[{\"ExcludeInvalidNode\": \"false\",\"Cluster\": \"[\\\"1\\\"]\",\"StartXPath\": \"html/body/div[@class='endArea']/div[@class='colL']/div[@class='bg_endPage_blue clearfix']/div[@class='endContent']/p[@class='summary']\",\"KeepHTMLTag\": \"false\",\"IncludeValidNodeOnly\": \"true\"}]";
            List<Map<String, String>> res = fromJson(json, List.class);
            System.out.println(res);
            json = toJson(res, true);
            System.out.println(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void testCustomPrettyPrint() {
        class pp extends org.codehaus.jackson.impl.DefaultPrettyPrinter {

            @Override
            public void writeArrayValueSeparator(JsonGenerator jg) throws IOException, JsonGenerationException {
                super.writeArrayValueSeparator(jg);
                jg.writeRaw('\n');
            }
        }
        try {
            String json = "[\"1\",\"2\"]";
            List res = fromJson(json, List.class);
            System.out.println(res);
            json = toJson(res, new pp());
            System.out.println(json);
            List res2 = fromJson(json, List.class);
            for (int i = 0; i < res.size(); i++) {
                assert (res.get(i).equals(res2.get(i)));
            }
            String obj = "\"abc\"";
            String o = fromJson(obj, String.class);
            System.out.println(o);
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }
}
