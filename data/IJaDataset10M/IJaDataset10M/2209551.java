package de.icehorsetools.iceoffice.state.person;

import java.util.Collection;
import java.util.HashMap;
import org.ugat.wiser.language.Lang;
import org.ugat.wiser.state.AUnState;
import de.icehorsetools.constants.PersonSexCo;
import de.icehorsetools.constants.PersonStatusCo;
import de.icehorsetools.dataAccess.objects.Person;

/**
 * @author tkr
 * @version $Id: PersonBasicSt.java 321 2009-03-28 02:19:57Z kruegertom $
 */
public class PersonBasicSt extends AUnState {

    private Object data = null;

    public void initialize() {
        this.setContainerComponentByPluginName("personBasic");
    }

    public Object getData() {
        if (this.data instanceof Person) return this.getContainerHandler().getData();
        return null;
    }

    public void setData(Object xData) {
        this.data = xData;
        if (this.data instanceof Person) this.getContainerHandler().setData(this.data);
    }

    public void setElementData(Collection xExcludes, HashMap xFilter) {
        this.getContainerHandler().getComponentHandler("sex").setElementData(PersonSexCo.getElements());
        this.getContainerHandler().getComponentHandler("status").setElementData(PersonStatusCo.getElements());
    }

    public String getTitle() {
        return Lang.get(this.getClass(), "title");
    }
}
