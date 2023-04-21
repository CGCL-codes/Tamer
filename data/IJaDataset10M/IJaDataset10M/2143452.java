package com.itextpdf.demo.json;

import java.io.IOException;
import com.itextpdf.devoxx.dao.RestConnection;

public class JSONGetFile {

    public static final String URI1 = "https://cfp.devoxx.com/rest/v1/events/presentations/60";

    public static final String URI2 = "https://cfp.devoxx.com/rest/v1/events/4/speakers";

    public static final String URI3 = "file:/C:/itext-core/devoxxguide/demo/example.json";

    public static void main(String[] args) throws IOException {
        String json = RestConnection.getStringFromRest(URI1);
        System.out.println(json);
    }
}
