package client.game.state.creation.xmlParser;

import java.util.logging.Level;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import com.jme.light.DirectionalLight;
import com.jme.scene.state.LightState;

public class DirectionalLightXMLTagParser extends XMLTagParser {

    private static final String id = "directionalLight";

    public DirectionalLightXMLTagParser() {
        super(id);
    }

    @Override
    public Object processElement(Element tag, Object parent) {
        try {
            Attribute enabledAttr = tag.getAttribute("enabled");
            if (enabledAttr != null) {
                boolean enabled;
                enabled = enabledAttr.getBooleanValue();
                DirectionalLight dLight = new DirectionalLight();
                dLight.setEnabled(enabled);
                processAllInternTags(tag, dLight);
                ((LightState) parent).attach(dLight);
            } else log.log(Level.SEVERE, "El Tag \"" + id + "\" no tiene el argumento enabled");
        } catch (DataConversionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
