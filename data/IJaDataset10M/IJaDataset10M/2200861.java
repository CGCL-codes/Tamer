package org.vardb.lists;

import java.util.HashMap;
import java.util.Map;
import org.vardb.alignment.CMafftLocalParams;
import org.vardb.sequences.CSequenceType;
import org.vardb.tasks.CAbstractTaskParams;
import org.vardb.util.CStringHelper;

public class CAlignSequenceListParams extends CAbstractTaskParams {

    protected String list_id;

    protected String ids = null;

    protected String filter = null;

    protected String name = null;

    protected CMafftLocalParams params = new CMafftLocalParams();

    protected Map<String, Integer> identifiersToIds = new HashMap<String, Integer>();

    public String getList_id() {
        return this.list_id;
    }

    public void setList_id(final String list_id) {
        this.list_id = list_id;
    }

    public String getIds() {
        return this.ids;
    }

    public void setIds(final String ids) {
        this.ids = ids;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(final String filter) {
        this.filter = filter;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public CMafftLocalParams getParams() {
        return this.params;
    }

    public Map<String, Integer> getIdentifiersToIds() {
        return this.identifiersToIds;
    }

    public void setIdentifiersToIds(final Map<String, Integer> identifiersToIds) {
        this.identifiersToIds = identifiersToIds;
    }

    public CAlignSequenceListParams() {
        this.redirect = "/alignments/%s.html";
    }

    public CAlignSequenceListParams(String user_id, String list_id) {
        this();
        this.user_id = user_id;
        this.list_id = list_id;
    }

    public void setSequenceType(String sequenceType) {
        this.params.setSequenceType(CSequenceType.valueOf(sequenceType));
    }

    public void setStrategy(String strategy) {
        this.params.setStrategy(CMafftLocalParams.Strategy.valueOf(strategy));
    }

    public void setScorematrix(String scorematrix) {
        this.params.setScorematrix(CMafftLocalParams.ScoreMatrix.valueOf(scorematrix));
    }

    public void setOp(String op) {
        if (CStringHelper.hasContent(op)) this.params.setOp(op);
    }

    public void setEp(String ep) {
        if (CStringHelper.hasContent(ep)) this.params.setEp(ep);
    }

    public String getTaskType() {
        return "ALIGN_SEQUENCE_LIST";
    }
}
