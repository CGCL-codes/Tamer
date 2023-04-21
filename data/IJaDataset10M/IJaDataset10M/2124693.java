package chinese;

import gate.creole.PackagedController;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import java.net.URL;
import java.util.List;

@CreoleResource(name = "Chinese IE System", icon = "ChineseLanguage", autoinstances = @AutoInstance)
public class ChineseIE extends PackagedController {

    private static final long serialVersionUID = 3163023140886167369L;

    @Override
    @CreoleParameter(defaultValue = "resources/chinese.gapp")
    public void setPipelineURL(URL url) {
        this.url = url;
    }

    @Override
    @CreoleParameter(defaultValue = "Chinese")
    public void setMenu(List<String> menu) {
        super.setMenu(menu);
    }
}
