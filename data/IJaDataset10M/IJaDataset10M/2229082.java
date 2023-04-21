package wolf.mobile.ws;

public class ProblemDescriptionArrayVO {

    protected wolf.mobile.ws.ProblemDescriptionVO[] array;

    public ProblemDescriptionArrayVO() {
    }

    public ProblemDescriptionArrayVO(wolf.mobile.ws.ProblemDescriptionVO[] array) {
        this.array = array;
    }

    public wolf.mobile.ws.ProblemDescriptionVO[] getArray() {
        return array;
    }

    public void setArray(wolf.mobile.ws.ProblemDescriptionVO[] array) {
        this.array = array;
    }
}
