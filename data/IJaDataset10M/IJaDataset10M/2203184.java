package fr.xebia.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 * Processeur FlexJSON. Utilise un {@link JSONDeserializer} pour mapper les chaines JSON vers les beans et, un {@link JSONSerializer} pour
 * convertir les beans en chaine JSON.
 * 
 * Le processeur assure la conversion des flux en {@link String}
 * 
 * @author slm
 * 
 */
public class FlexJsonProcessor implements JSONProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(FlexJsonProcessor.class);

    @SuppressWarnings("unchecked")
    private final JSONDeserializer dser = new JSONDeserializer();

    private final JSONSerializer ser = new JSONSerializer();

    @SuppressWarnings("unchecked")
    public Object fromJSON(InputStream in, Class dst) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")), 200);
            StringBuilder sbuff = new StringBuilder(200);
            String nbc = null;
            while ((nbc = reader.readLine()) != null) {
                sbuff.append(nbc);
            }
            LOG.debug("La chaine json est: {}", sbuff);
            return dser.use(null, dst).deserialize(sbuff.toString());
        } catch (IOException e) {
            LOG.warn("error on sojo API", e);
        }
        return null;
    }

    public void toJSON(OutputStream out, Object src) {
        String res = ser.serialize(src);
        LOG.debug("La chaine JSON est : {}", res);
        try {
            OutputStreamWriter o = new OutputStreamWriter(out, Charset.forName("UTF-8"));
            o.write(res);
            o.flush();
        } catch (IOException e) {
            LOG.warn("error on sojo API", e);
        }
    }
}
