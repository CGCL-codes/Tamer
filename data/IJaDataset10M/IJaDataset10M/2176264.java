package com.sun.java.help.search;

import java.util.*;
import javax.help.search.SearchQuery;
import javax.help.search.SearchItem;

class Query {

    private double _currentStandard;

    private final int _nColumns;

    private double[] _missingPenalty;

    private double[] _upperboundTemplate;

    private double[] _penalties;

    private final SearchEnvironment _env;

    private int _hitCounter;

    private boolean _vote;

    private HitStore _store;

    public Query(SearchEnvironment env, int nColumns, double[] missingPenalties) {
        _env = env;
        _nColumns = nColumns;
        _missingPenalty = new double[nColumns];
        _upperboundTemplate = new double[nColumns];
        _penalties = missingPenalties;
        _hitCounter = 0;
        _vote = false;
        _currentStandard = (nColumns - 1) * 10.0 + 9.9999;
        _store = new HitStore(_currentStandard);
        for (int i = 0; i < _nColumns; i++) _missingPenalty[i] = missingPenalties != null ? missingPenalties[i] : 10.0;
        makePenaltiesTable();
    }

    public void makeEvent(int n, SearchQuery searchQuery) {
        Vector hits = new Vector(n);
        if (n > 0) {
            int N = n;
            QueryHit qh = _store.firstBestQueryHit();
            n = N;
            for (; qh != null; qh = --n > 0 ? _store.nextBestQueryHit() : null) try {
                hits.addElement(_env.makeItem(qh));
            } catch (Exception e) {
                System.err.println(e + "hit not translated");
            }
        }
        searchQuery.itemsFound(true, hits);
    }

    public double lookupPenalty(int pattern) {
        return _penalties[pattern];
    }

    public double getOutOufOrderPenalty() {
        return 0.25;
    }

    public double getGapPenalty() {
        return 0.005;
    }

    public int getNColumns() {
        return _nColumns;
    }

    public boolean goodEnough(double penalty) {
        return penalty <= _currentStandard;
    }

    public int[] getConceptArrayOfNewHit(double penalty, Location loc) {
        QueryHit hit = new QueryHit(loc, penalty, _nColumns);
        _store.addQueryHit(hit);
        _hitCounter++;
        return hit.getArray();
    }

    public void resetForNextDocument() {
        _currentStandard = _store.getCurrentStandard();
        for (int i = 0; i < _nColumns; i++) _upperboundTemplate[i] = _missingPenalty[i];
        _vote = false;
    }

    public boolean vote() {
        double sum = 0.0;
        for (int i = 0; i < _nColumns; i++) sum += _upperboundTemplate[i];
        return _vote = (sum <= _currentStandard);
    }

    public void updateEstimate(int role, double penalty) {
        if (penalty < _upperboundTemplate[role]) _upperboundTemplate[role] = penalty;
    }

    public void printHits(int n) {
        if (n > 0) {
            int N = n;
            QueryHit qh = _store.firstBestQueryHit();
            n = N;
            for (; qh != null; qh = --n > 0 ? _store.nextBestQueryHit() : null) try {
                System.out.println(_env.hitToString(qh));
            } catch (Exception e) {
                System.err.println(e + "hit not translated");
            }
        }
    }

    private void makePenaltiesTable() {
        int nPatterns = 1 << _nColumns;
        _penalties = new double[nPatterns];
        for (int i = 0; i < nPatterns; i++) _penalties[i] = computePenalty(i);
    }

    private double computePenalty(int n) {
        double penalty = 0.0;
        for (int i = 0; i < _nColumns; i++) if ((n & 1 << i) == 0) penalty += _missingPenalty[i];
        return penalty;
    }
}
