package screen.tools.sbs.targets;

public class TargetCall {

    public String targetName;

    public TargetCall() {
        targetName = null;
    }

    public boolean hasValidTarget() {
        return targetName != null;
    }

    public String getTarget() {
        return targetName;
    }

    public void setTarget(String target) {
        targetName = target;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TargetCall) {
            TargetCall call = (TargetCall) obj;
            if (!hasValidTarget()) return false;
            if (!call.hasValidTarget()) return false;
            return getTarget().equals(call.getTarget());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return targetName.hashCode();
    }
}
