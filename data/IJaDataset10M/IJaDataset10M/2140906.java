package de.icehorsetools.constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.ugat.interfaces.IUnItemDescriptor;
import org.ugat.wiser.interfaces.IUnComponentHandler;
import org.ugat.wiser.language.Lang;
import de.icehorsetools.dataAccess.objects.Horse;

/**
 * the constant possible values for {@link Test).status
 *
 * @author kruegertom
 * @version $Id$
 */
public class FinancearticleAdditionalinfoHorseCo implements IUnItemDescriptor {

    /**
     * Only used if entity is {@link Horse}. at this time 0 = no additional info
     */
    public static final FinancearticleAdditionalinfoHorseCo NO_ADDITIONAL_INFO = new FinancearticleAdditionalinfoHorseCo(new Integer(0), "no_additional_info");

    public static final FinancearticleAdditionalinfoHorseCo IS_STABLE = new FinancearticleAdditionalinfoHorseCo(new Integer(1), "is_stable");

    private static final List<FinancearticleAdditionalinfoHorseCo> elements = new ArrayList<FinancearticleAdditionalinfoHorseCo>(1);

    private String display;

    private Serializable id;

    static {
        elements.add(NO_ADDITIONAL_INFO);
        elements.add(IS_STABLE);
    }

    private FinancearticleAdditionalinfoHorseCo(Serializable xIdentifier, String xDisplayString) {
        this.id = xIdentifier;
        this.display = xDisplayString;
    }

    public static List<FinancearticleAdditionalinfoHorseCo> getElements() {
        return elements;
    }

    public String toDisplayString() {
        return Lang.get(this.getClass(), this.display);
    }

    public Serializable toIdentifier() {
        return this.id;
    }

    public Integer toInteger() {
        return (Integer) this.id;
    }

    public String toText(IUnComponentHandler xContext) {
        return Lang.get(this.getClass(), this.display);
    }
}
