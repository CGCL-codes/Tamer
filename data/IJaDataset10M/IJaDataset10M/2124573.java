package PatchConversion;

/**
 * Copies a parm morph value directly to a generic patch value
 * or vice versa; no translation is performed.  This shouldn't
 * be used much - most values will need to be converted in some way
 * to match the generic format.
 *
 * @author Kenneth L. Martinez
 */
public class MorphTranslatorDirect implements ParmTranslator {

    private SynthParmRange sp;

    private SynthParmRange bp;

    private ModuleParm mp;

    private ParmValidatorRange pv;

    private ParmMorph morph;

    MorphTranslatorDirect(SynthParmRange pSp, SynthParmRange pBp, ModuleParm pMp) {
        sp = pSp;
        bp = pBp;
        sp.getClass();
        bp.getClass();
        mp = pMp;
        pv = (ParmValidatorRange) mp.getPv();
        morph = mp.getMorph();
    }

    public void toGeneric() {
        String s = sp.getValue();
        if (s.equals("0")) {
            morph.setUsed(false);
        } else {
            morph.setUsed(true);
            int i = new Integer(s).intValue();
            i += bp.getIntValue();
            morph.setValue(Integer.toString(i));
        }
    }

    public void fromGeneric() {
        if (morph.isUsed() == false) {
            sp.setValue(0);
            return;
        }
        String valp, valm;
        valp = mp.getValue();
        valm = morph.getValue();
        if (valp.equals(valm)) {
            sp.setValue(0);
            return;
        }
        int i = new Integer(valm).intValue() - new Integer(valp).intValue();
        sp.setValue(i);
    }
}
