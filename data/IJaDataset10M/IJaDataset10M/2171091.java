package functional.type;

import java.util.ArrayList;
import java.util.List;

public class Match {

    private boolean mMatched = true;

    private List<ParameterMapping> mMappings = new ArrayList<ParameterMapping>();

    private Match(boolean matched) {
        mMatched = matched;
    }

    public Match(Parameter parameter, Type type) {
        map(parameter, type);
    }

    protected Match(List<ParameterMapping> mappings) {
        mMappings.addAll(mappings);
    }

    public boolean matches() {
        return mMatched;
    }

    public List<ParameterMapping> mappings() {
        return mMappings;
    }

    public void map(Parameter parameter, Type type) {
        if (type.involves(parameter)) {
            mMatched = false;
        } else {
            boolean added = add(parameter, type);
            if (!added) {
                mMatched = false;
            }
        }
    }

    private boolean add(Parameter parameter, Type type) {
        return add(new ParameterMapping(parameter, type));
    }

    private ParameterMapping find(Parameter parameter) {
        for (ParameterMapping map : mMappings) {
            if (parameter == map.parameter()) {
                return map;
            }
        }
        return null;
    }

    private boolean add(ParameterMapping toAdd) {
        ParameterMapping same = find(toAdd.parameter());
        if (same == null) {
            return addAndSubstitute(toAdd);
        }
        if (toAdd.equals(same)) {
            return true;
        }
        Match match = same.type().match(toAdd.type());
        if (!match.matches()) {
            match = toAdd.type().match(same.type());
            if (!match.matches()) {
                return false;
            }
        }
        for (ParameterMapping newMap : match.mappings()) {
            if (!add(newMap)) {
                return false;
            }
        }
        return true;
    }

    private boolean addAndSubstitute(ParameterMapping toAdd) {
        Type addType = toAdd.type().substitute(mMappings);
        if (addType.involves(toAdd.parameter())) {
            return false;
        }
        for (ParameterMapping map : mMappings) {
            if (!map.substitute(toAdd)) {
                return false;
            }
        }
        mMappings.add(toAdd);
        return true;
    }

    public Match combine(Match other) {
        assert (other != null);
        if (!mMatched || !other.mMatched) {
            return NO_MATCH;
        }
        if (mMappings.isEmpty()) {
            return other;
        }
        if (other.mMappings.isEmpty()) {
            return this;
        }
        Match combined = new Match(other.mMappings);
        for (ParameterMapping map : mMappings) {
            if (!combined.add(map)) {
                return NO_MATCH;
            }
        }
        return combined;
    }

    public static Match result(boolean matched) {
        return matched ? MATCHED : NO_MATCH;
    }

    public static final Match MATCHED = new Match(true);

    public static final Match NO_MATCH = new Match(false);
}
