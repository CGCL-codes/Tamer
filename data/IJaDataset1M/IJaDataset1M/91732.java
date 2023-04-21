package org.monkeypuzzler.css;

import org.json.JSONException;
import org.json.JSONObject;

class SimpleTestElement extends JSONObject {

    static String simple;

    static {
        StringBuilder json = new StringBuilder("{");
        json.append("id : 'Test',").append("height : 30,").append("width : 14,").append("position : {x:172, y:23},").append("attributes : {},").append("style : {").append("position : null,").append("color : [125, 0, 10],").append("backgroundColor : [125, 0, 10],").append("borderColor : null,").append("paddingLeft : 0,").append("paddingRight : 0,").append("paddingTop : 0,").append("paddingBottom : 0").append("}").append("}");
        simple = json.toString();
    }

    public SimpleTestElement() throws JSONException {
        super(simple);
    }
}
