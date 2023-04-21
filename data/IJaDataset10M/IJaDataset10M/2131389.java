package hu.sztaki.lpds.wfs.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author krisztian
 */
public class Check_boinc implements ConfigureValidatorFace {

    /**
 * @see ConfigureValidatorFace#getJobErrors(java.util.HashMap)
 */
    @Override
    public List<String> getJobErrors(HashMap<String, String> pProps) {
        List<String> res = new ArrayList<String>();
        if (pProps.get("grid") == null) res.add("error.executejob.grid");
        if (pProps.get("resource") == null) res.add("error.executejob.grid");
        return res;
    }
}
