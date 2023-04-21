package eu.soa4all.wp6.composer.run.wp9;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import eu.soa4all.wp6.composer.exception.DTComposerException;
import eu.soa4all.wp6.composer.run.DesignComposerDemonstration;

public class Test {

    public static void main(String[] args) throws FileNotFoundException, IOException, URISyntaxException, DTComposerException {
        URL location = new URL("http://localhost/dtcomposer/output/11d1def534ea1be0-70173015-12520966573-7e9e.xml");
        new DesignComposerDemonstration("composerconfiguration-wp9.xml").copyRemoteModel(location);
    }
}
