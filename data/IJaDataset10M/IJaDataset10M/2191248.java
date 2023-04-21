package com.kenstevens.stratdom.site;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;
import com.kenstevens.stratdom.main.Constants;
import com.kenstevens.stratdom.model.Sector;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class TestGetAllImages {

    private final WebConversation wc = new WebConversation();

    public void login() throws Exception {
        try {
            for (Sector.Type sectorType : Sector.Type.values()) {
                getImage(sectorType.toString().toLowerCase() + ".jpg");
            }
            getImage("activebox.gif");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getImage(String filename) throws MalformedURLException, IOException, SAXException, FileNotFoundException {
        String url = Constants.STRATEGICDOMINATION_URL + "/images/gameimages/" + filename;
        WebRequest req = new GetMethodWebRequest(url);
        WebResponse response = wc.getResponse(req);
        File file = new File("etc/images/" + filename);
        FileOutputStream outputStream = new FileOutputStream(file);
        IOUtils.copy(response.getInputStream(), outputStream);
    }
}
