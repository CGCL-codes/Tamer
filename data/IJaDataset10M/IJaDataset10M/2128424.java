package org.s3b.service.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.s3b.query.ExpandedHistoryInfo;
import org.s3b.service.HistoryInfo;
import org.s3b.service.HistoryInfoType;
import org.s3b.service.QueryParameterType;
import org.s3b.service.Resource;
import org.s3b.service.ResultInfo;
import org.s3b.service.ResultObject;
import org.s3b.service.SortingParam;

/**
 * 
 * The simple implementation of the result object concept. Based on the <tt>String</tt>s as a values of authors,
 * categories and keywords lists
 * 
 * @author Sebastian Ryszard Kruk, Krystian Samp, Adam Westerski,
 * @deprecated
 */
public class ResultObjectImpl implements ResultObject<String, String, String> {

    /**
	 * The list with the history of query processing information objects
	 */
    protected List<HistoryInfo> history = new ArrayList<HistoryInfo>();

    /**
	 * The list with the additional information
	 */
    protected List<ResultInfo> info = new ArrayList<ResultInfo>();

    /**
	 * The list of the resources-results
	 */
    protected List<Resource<String, String, String>> results = new ArrayList<Resource<String, String, String>>();

    /**
	 * The list of the banned resources-results (removed during searching)
	 */
    protected List<Resource<String, String, String>> bannedResults = new ArrayList<Resource<String, String, String>>();

    /**
	 * Hold reference list of expansions history
	 */
    protected Map<QueryParameterType, List<ExpandedHistoryInfo>> expansionHistory = new HashMap<QueryParameterType, List<ExpandedHistoryInfo>>();

    /**
	 * The list with the sorting parameters
	 */
    protected List<SortingParam> sortingParams = new ArrayList<SortingParam>();

    @SuppressWarnings("unchecked")
    public void addEntry(Object entry) {
        if (entry instanceof HistoryInfo) {
            addHistoryInfo((HistoryInfo) entry);
        } else if (entry instanceof ResultInfo) {
            addInfo((ResultInfo) entry);
        } else if (entry instanceof SortingParam) {
            addSortingParam((SortingParam) entry);
        } else if (entry instanceof Resource) {
            addResult((Resource<String, String, String>) entry);
        }
    }

    public void addEntries(List<Object> entries) {
        for (Object o : entries) {
            addEntry(o);
        }
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#addHistoryInfo(org.s3b.service.search.HistoryInfo)
	 */
    public void addHistoryInfo(HistoryInfo _historyInfo) {
        this.history.add(_historyInfo);
        if (_historyInfo.getType() == HistoryInfoType.HIT_RESOURCE_QUERY_EXPANDED && _historyInfo instanceof ExpandedHistoryInfo) {
            ExpandedHistoryInfo ehi = (ExpandedHistoryInfo) _historyInfo;
            List<ExpandedHistoryInfo> lehi = this.expansionHistory.get(ehi.getParamtype());
            if (lehi == null) {
                lehi = new ArrayList<ExpandedHistoryInfo>();
            }
            lehi.add(ehi);
            this.expansionHistory.put(ehi.getParamtype(), lehi);
        }
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#addInfo(org.s3b.service.search.ResultInfo)
	 */
    public void addInfo(ResultInfo _info) {
        this.info.add(_info);
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#addResult(org.s3b.service.Resource)
	 */
    public void addResult(Resource<String, String, String> _result) {
        this.results.add(_result);
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#addSortingParam(org.s3b.service.SortingParam)
	 */
    public void addSortingParam(SortingParam _param) {
        this.sortingParams.add(_param);
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#clearAllHistoryInfo()
	 */
    public void clearAllHistoryInfo() {
        this.history.clear();
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#clearAllInfo()
	 */
    public void clearAllInfo() {
        this.info.clear();
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#clearResults()
	 */
    public void clearResults() {
        this.results.clear();
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#clearSortingParams()
	 */
    public void clearSortingParams() {
        this.sortingParams.clear();
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#getHistoryInfo()
	 */
    public HistoryInfo[] getHistoryInfo() {
        return this.history.toArray(new HistoryInfo[this.history.size()]);
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#getInfo()
	 */
    public ResultInfo[] getInfo() {
        return this.info.toArray(new ResultInfo[this.info.size()]);
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#getResults()
	 */
    @SuppressWarnings("unchecked")
    public Resource<String, String, String>[] getResults() {
        return this.results.toArray(new Resource[0]);
    }

    /**
	 * This method has been overriden.
	 * 
	 * @see org.s3b.service.ResultObject#getSortingParams()
	 */
    public SortingParam[] getSortingParams() {
        return this.sortingParams.toArray(new SortingParam[this.sortingParams.size()]);
    }

    public ExpandedHistoryInfo[] getExpandedHistory() {
        List<ExpandedHistoryInfo> lehi = new ArrayList<ExpandedHistoryInfo>();
        for (QueryParameterType qtp : this.expansionHistory.keySet()) {
            lehi.addAll(this.expansionHistory.get(qtp));
        }
        return lehi.toArray(new ExpandedHistoryInfo[lehi.size()]);
    }

    public ExpandedHistoryInfo[] getExpandedHistory(QueryParameterType _paramType) {
        List<ExpandedHistoryInfo> exp = this.expansionHistory.get(_paramType);
        return (exp != null) ? exp.toArray(new ExpandedHistoryInfo[0]) : new ExpandedHistoryInfo[0];
    }

    @SuppressWarnings("unchecked")
    public void addResults(ResultObject _results) {
        if (_results != null && _results.getResults() != null) {
            for (Resource<String, String, String> resource : _results.getResults()) {
                int pos = this.results.indexOf(resource);
                int posb = this.bannedResults.indexOf(resource);
                if (pos >= 0 && posb < 0) {
                    Resource<String, String, String> r = this.results.get(pos);
                    r.setRank(r.getRank() + resource.getRank());
                } else if (posb < 0) {
                    this.results.add(resource);
                } else if (pos >= 0 && posb >= 0) {
                    this.results.remove(pos);
                }
            }
            this.addHistoryInfo(_results.getHistoryInfo());
            this.addInfo(_results.getInfo());
            this.addSortingParam(_results.getSortingParams());
        }
    }

    @SuppressWarnings("unchecked")
    public void intersectResults(ResultObject _aresults) {
        List<Resource> _results = Arrays.asList(_aresults.getResults());
        if (!this.results.isEmpty()) {
            for (Resource<String, String, String> resource : _results) {
                int pos = this.results.indexOf(resource);
                int posb = this.bannedResults.indexOf(resource);
                if (pos >= 0 && posb < 0) {
                    Resource r = this.results.get(pos);
                    Float oRankRes = resource.getRank();
                    Float oRankQue = r.getRank();
                    r.setRank(oRankQue + oRankRes);
                }
                if (pos >= 0 || posb >= 0) {
                    _results.remove(resource);
                }
            }
            for (Resource<String, String, String> resource : _results) {
                this.bannedResults.add(resource);
            }
        } else {
            for (Resource<String, String, String> resource : _results) {
                this.bannedResults.add(resource);
            }
        }
        this.addHistoryInfo(_aresults.getHistoryInfo());
        this.addInfo(_aresults.getInfo());
        this.addSortingParam(_aresults.getSortingParams());
    }

    @SuppressWarnings("unchecked")
    public void removeResults(ResultObject _results) {
        for (Resource<String, String, String> resource : _results.getResults()) {
            int pos = this.results.indexOf(resource);
            if (pos >= 0) {
                this.results.remove(pos);
                this.bannedResults.add(resource);
            }
        }
        this.addHistoryInfo(_results.getHistoryInfo());
        this.addInfo(_results.getInfo());
        this.addSortingParam(_results.getSortingParams());
    }

    public void addHistoryInfo(HistoryInfo... _historyInfo) {
        this.history.addAll(Arrays.asList(_historyInfo));
    }

    public void addInfo(ResultInfo... _info) {
        this.info.addAll(Arrays.asList(_info));
    }

    public void addSortingParam(SortingParam... _param) {
        this.sortingParams.addAll(Arrays.asList(_param));
    }

    public boolean isFresh() {
        return this.results.isEmpty() && this.bannedResults.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public Resource[] getTopResults(int count) {
        return null;
    }

    public Resource[] getSortedResults() {
        return null;
    }
}
