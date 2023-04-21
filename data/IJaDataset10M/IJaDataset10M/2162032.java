package lv.webkursi.klucis;

import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import lv.webkursi.klucis.mvc.VelocityMerge;
import org.junit.Test;

public class VelocityViewTest {

    @Test
    public void rendering() {
        VelocityMerge view = new VelocityMerge();
        view.setTemplateName("TopComponent");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("_content", new Renderable());
        view.setContextParams(model);
        System.out.println("render = " + view.render());
    }

    public static class Renderable {

        public String render() {
            return "XXX";
        }
    }
}
