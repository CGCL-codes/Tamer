package k9;

class Option {

    static final int OptionSubtypeOption = 0;

    static final int OptionSubtypeAlternatePlan = 1;

    private int subtype;

    static Symbol id_default;

    static String comment_default;

    static double utility_default;

    static ListOfConditions eligible_conditions_default;

    static Node node_default;

    Symbol id;

    String comment;

    double utility;

    ListOfConditions eligible_conditions;

    Node node;

    TimeCondition absoluteEligibleBounds;

    TimeCondition relativeEligibleBounds;

    ListOfConditions eligibleResourceConditions;

    int unpredictableEligibleConditions;

    void initCompiledConditions() {
        absoluteEligibleBounds = relativeEligibleBounds = null;
        eligibleResourceConditions = null;
        unpredictableEligibleConditions = 0;
    }

    public Option() {
        subtype = OptionSubtypeOption;
        initCompiledConditions();
        id = id_default;
        comment = comment_default;
        utility = utility_default;
        eligible_conditions = eligible_conditions_default;
        node = node_default;
    }

    public Option(int xsubtype, Symbol xid, String xcomment, double xutility, ListOfConditions xeligible_conditions, Node xnode) {
        subtype = xsubtype;
        id = xid;
        comment = xcomment;
        utility = xutility;
        eligible_conditions = xeligible_conditions;
        node = xnode;
        initCompiledConditions();
    }

    public Option(Option o) {
        subtype = o.subtype;
        id = o.id;
        comment = o.comment;
        utility = o.utility;
        eligible_conditions = o.eligible_conditions;
        node = o.node;
        initCompiledConditions();
    }

    Symbol getId() {
        return id;
    }

    Node getNode() {
        return node;
    }

    Node getNode_nonconst() {
        return node;
    }

    ListOfConditions getEligibleConditions() {
        return eligible_conditions;
    }

    double getUtility() {
        return utility;
    }
}
