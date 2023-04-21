package ua.od.lonewolf.Crow.Export.PlainText;

import java.util.Map;
import org.hibernate.Session;
import ua.od.lonewolf.Crow.Model.Element.IElement;

public class CodeVariableFiller extends DefaultVariableFiller implements IVariableFiller {

    @Override
    public Map<String, String> prepareFillMap(Session session, IElement el) {
        return super.prepareFillMap(session, el);
    }
}
