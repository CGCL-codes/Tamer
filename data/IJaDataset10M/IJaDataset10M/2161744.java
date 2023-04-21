package com.ideo.sweetdevria.taglib.reader;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.MapUtils;
import com.ideo.sweetdevria.servlet.Action;

/**
 * Main control action entry point.
 * This action is stateless, created on demand, and handles a specific request  
 * sent by the client control, launching the action through the launchAction method. 
 * This class should not be directly extending by a custom user's action.
 * Some abstract ones have been defined to give some better information about the return types and method parameters.
 * 

 */
public class UpdateReader extends Action {

    public final Object execute(HttpServletRequest request, String pageId, Map params) throws Exception {
        String itemId = (String) params.get("itemId");
        if (itemId != null) {
            if (itemId.equals("reader_n2") || itemId.equals("reader2_n2") || itemId.equals("reader3_n2") || itemId.equals("reader4_n2")) {
                return MapUtils.putAll(new HashMap(), new Object[][] { { "result", "blablablalba 2 from server !!!!!!!!!!!!!!!" } });
            } else if (itemId.equals("reader_n8") || itemId.equals("reader2_n8") || itemId.equals("reader3_n8") || itemId.equals("reader4_n8")) {
                return MapUtils.putAll(new HashMap(), new Object[][] { { "result", "blablablalba 8 from server !!!!!!!!!!!!!!!" } });
            } else if (itemId.equals("reader_t3") || itemId.equals("reader2_t3") || itemId.equals("reader3_t3") || itemId.equals("reader4_t3")) {
                Object[][] result = { { "n7", "news 7", "blblablabla 7" }, { "n8", "news 8", null }, { "n9", "news 9", "blblablabla 9" } };
                return result;
            }
        }
        Integer pageNumber = (Integer) params.get("pageNumber");
        if (pageNumber != null) {
            int i = pageNumber.intValue();
            int i1 = (3 * i) - 2;
            int i2 = (3 * i) - 1;
            int i3 = (3 * i);
            int n1 = (3 * i1) - 2;
            int n2 = (3 * i1) - 1;
            int n3 = (3 * i1);
            int n4 = (3 * i2) - 2;
            int n5 = (3 * i2) - 1;
            int n6 = (3 * i2);
            int n7 = (3 * i3) - 2;
            int n8 = (3 * i3) - 1;
            int n9 = (3 * i3);
            if (i == 1) {
                Object[][] result = { { "t" + (i1), "Title " + (i1), new Object[][] { { "n" + (n1), "news " + (n1), "blblablabla " + (n1) }, { "n" + (n2), "news " + (n2), null }, { "n" + (n3), "news " + (n3), "blblablabla " + (n3) } } }, { "t" + (i2), "Title " + (i2), new Object[][] { { "n" + (n4), "news " + (n4), "blblablabla " + (n4) }, { "n" + (n5), "news " + (n5), "blblablabla " + (n5) }, { "n" + (n6), "news " + (n6), "blblablabla " + (n6) } } }, { "t" + (i3), "Title " + (i3), null } };
                return result;
            } else {
                Object[][] result = { { "t" + (i1), "Title " + (i1), new Object[][] { { "n" + (n1), "news " + (n1), "blblablabla " + (n1) }, { "n" + (n2), "news " + (n2), "blblablabla " + (n2) }, { "n" + (n3), "news " + (n3), "blblablabla " + (n3) } } }, { "t" + (i2), "Title " + (i2), new Object[][] { { "n" + (n4), "news " + (n4), "blblablabla " + (n4) }, { "n" + (n5), "news " + (n5), "blblablabla " + (n5) }, { "n" + (n6), "news " + (n6), "blblablabla " + (n6) } } }, { "t" + (i3), "Title " + (i3), new Object[][] { { "n" + (n7), "news " + (n7), "blblablabla " + (n7) }, { "n" + (n8), "news " + (n8), "blblablabla " + (n8) }, { "n" + (n9), "news " + (n9), "blblablabla " + (n9) } } } };
                return result;
            }
        }
        return null;
    }
}
