package cgl.shindig.layoutmanager.data;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cgl.shindig.common.JSONHelper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/** 
 * This class builds LayoutSuite instance from data in DB.
 * 
 * @author 
 * @version 
 */
@Singleton
public class LayoutBuilder {

    private boolean restrict = false;

    private static final String errorTxtLSNonExist = "The layout of the user cannot be retrieved. Make sure the parameters are correct";

    private LayoutTabBuilder layoutTabBuilder;

    @Inject
    public void setLayoutTabBuilder(LayoutTabBuilder layoutTabBuilder) {
        this.layoutTabBuilder = layoutTabBuilder;
    }

    /**
    Sample raw data:
    {
      "layouttype":"js-tab-layout",
      "activetabidx":9,
      "properties":{
         sync:"auto",
         theme:"default_theme"
      },
      "layoutdata":[
         {
            "tabname":"My Gadgets",
            "tabid":"_tab_0",
            "column":3,
            "content":[
               {
                  "columnname":"undefined",
                  "columnid":"_tab_0_col_0",
                  "content":[
                     {
                        "gadgetname":"RSS Feeds",
                        "gadgetspecsrc":"https://gf18.ucs.indiana.edu:7443/gadgets-repo/rssreader.xml",
                        "gadgetrendersrc":"undefined",
                        "gadgetid":"_tab_0_col_0_gadget_0",
                        "userpref":{

                        },
                        "status":"normal"
                     }]
               }, {
                  "columnname":"undefined",
                  "columnid":"_tab_0_col_1",
                  "content":[ ]
               }, {
                  "columnname":"undefined",
                  "columnid":"_tab_0_col_2",
                  "content":[ ]
               }
            ]
         }
      ]
    }
    */
    public static String keyLayoutType = "layouttype";

    public static String keyActiveTabIdx = "activetabidx";

    public static String keyProperties = "properties";

    public static String keyLayoutData = "layoutdata";

    public static String keyLayoutName = "layoutname";

    public Layout build(String wholeLayoutRawdata) throws JSONException {
        JSONObject wholeLayoutObj = new JSONObject(wholeLayoutRawdata);
        return build(wholeLayoutObj);
    }

    public Layout build(JSONObject wholeLayoutObj) throws JSONException {
        Layout layout = new Layout();
        String layoutType = wholeLayoutObj.optString(keyLayoutType, "js-tab-layout");
        layout.setLayoutType(layoutType);
        int activetabidx = wholeLayoutObj.optInt(keyActiveTabIdx, -1);
        layout.setActiveTabIdx(activetabidx);
        JSONObject propertiesObj = wholeLayoutObj.optJSONObject(keyProperties);
        if (propertiesObj != null) layout.setProperties(JSONHelper.jsonObj2Map(propertiesObj));
        JSONArray tabArray = wholeLayoutObj.getJSONArray(keyLayoutData);
        for (int i = 0; i < tabArray.length(); ++i) {
            if (tabArray.isNull(i)) {
                continue;
            } else {
                JSONObject tabObj = tabArray.getJSONObject(i);
                LayoutTab tab = layoutTabBuilder.build(tabObj);
                layout.addATab(tab);
            }
        }
        return layout;
    }
}
