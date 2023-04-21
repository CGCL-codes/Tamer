package com.vsetec.mety.core;

import java.util.List;
import com.vsetec.mety.Met;
import com.vsetec.mety.Query;
import com.vsetec.mety.Source;
import com.vsetec.util.DumbList;
import java.util.Arrays;

/**
 *
 * @author fedd
 */
public class ResFiltered extends Res<CollatingStructure, Met> implements Query.Result {

    private final Qry _qry;

    private final Qry _storedHelper;

    private final List<Qry.FilteringPart> _filtParts;

    private final NexterCloseable<CollatingStructure>[] _iters;

    private int _currentFilt = 0;

    private final int _lastFilt;

    private CollatingStructure _outerResult = null;

    private boolean _reachedEnd = false;

    public ResFiltered(Qry qry, List<Qry.FilteringPart> filtParts) {
        super(filtParts.size() > 1 ? Store.STORE.getStoredResOuter(qry.getStoredHelper().storedHelperQuery.key.getKeyB(), filtParts.get(0).sort, filtParts.get(0).filt, filtParts.get(0).coll, filtParts.get(0).val, filtParts.get(0).valTo, 0, null) : Store.STORE.getStoredRes(qry.getStoredHelper().storedHelperQuery.key.getKeyB(), filtParts.get(0).sort, filtParts.get(0).filt, filtParts.get(0).coll, filtParts.get(0).val, filtParts.get(0).valTo, 0, null), qry._limitSkip, qry._limitRows);
        _filtParts = filtParts;
        _iters = new NexterCloseable[filtParts.size()];
        _iters[0] = this.wrapperNexter;
        _qry = qry;
        _lastFilt = _iters.length - 1;
        _storedHelper = qry.getStoredHelper().storedHelperQuery;
    }

    @Override
    Met realNext() {
        if (_reachedEnd) return null;
        MetImpl ret;
        while (_moveNextIter()) {
            ret = getReturnFromIter();
            if (skipThis(ret)) {
                continue;
            }
            _startLimit--;
            if (_startLimit >= _stopSkip) continue;
            if (_startLimit < 0) {
                _iters[_currentFilt].close();
                _reachedEnd = true;
                return null;
            }
            return ret;
        }
        _reachedEnd = true;
        return null;
    }

    @Override
    public void close() {
        if (!_reachedEnd) {
            _reachedEnd = true;
            for (int i = 0; i < _iters.length; i++) {
                _iters[i].close();
            }
        }
    }

    private boolean _moveNextIter() {
        do {
            while (_iters[_currentFilt].next()) {
                if (_currentFilt >= _lastFilt) return true;
                _outerResult = (CollatingStructure) _iters[_currentFilt].getCurrent();
                _currentFilt++;
                Qry.FilteringPart fp = _filtParts.get(_currentFilt);
                if (_currentFilt < _lastFilt) {
                    _iters[_currentFilt] = Store.STORE.getStoredResOuter(_storedHelper.key.getKeyB(), fp.sort, fp.filt, fp.coll, fp.val, fp.valTo, _currentFilt, _outerResult);
                } else {
                    _iters[_currentFilt] = Store.STORE.getStoredRes(_storedHelper.key.getKeyB(), fp.sort, fp.filt, fp.coll, fp.val, fp.valTo, _currentFilt, _outerResult);
                }
                continue;
            }
            _iters[_currentFilt].close();
            _currentFilt--;
        } while (_currentFilt >= 0);
        return false;
    }

    @Override
    MetImpl getReturnFromIter() {
        Wrapper key = Wrapper.getWrapperByKeyB(_iters[_lastFilt].getCurrent().getKeyB());
        MetImpl ret = (MetImpl) ((Src) Source.SRC).getMetByKey(key);
        return ret;
    }

    private class IntHolder {

        int _int = 0;
    }

    @Override
    boolean skipThis(Met test) {
        return !_isResultOf(_qry, (MetImpl) test, this.new IntHolder());
    }

    private boolean _isResultOf(Qry qry, MetImpl test, IntHolder checkedCollStruct) {
        boolean checkedIncreasedHere = false;
        Qry sh = qry.getStoredHelper().storedHelperQuery;
        MetImpl[] checkMets = qry._mets;
        if (qry._ord != null || qry._filt != null) {
            checkedCollStruct._int++;
            checkedIncreasedHere = true;
        } else {
            boolean[] dontCheck = new boolean[checkMets.length];
            MetImpl[] dchk = sh._mets;
            checks: for (int i = 0; i < checkMets.length; i++) {
                for (int j = 0; j < dchk.length; j++) {
                    if (checkMets[i].equals(dchk[j])) {
                        dontCheck[i] = true;
                        continue checks;
                    }
                }
                dontCheck[i] = false;
            }
            for (int i = 0; i < checkMets.length; i++) {
                if (!dontCheck[i]) {
                    if (!checkMets[i].isRel(test)) return false;
                }
            }
        }
        final Qry[] noFilt;
        final Qry[] yaFilt;
        {
            final Qry[] noFilt2 = new Qry[qry._qrys.length];
            final Qry[] yaFilt2 = new Qry[qry._qrys.length];
            int noF = 0;
            int yaF = 0;
            for (int i = 0; i < qry._qrys.length; i++) {
                if (qry._qrys[i].hasSortSearch) {
                    yaFilt2[yaF] = qry._qrys[i];
                    yaF++;
                } else {
                    noFilt2[noF] = qry._qrys[i];
                    noF++;
                }
            }
            noFilt = Arrays.copyOf(noFilt2, noF);
            yaFilt = Arrays.copyOf(yaFilt2, yaF);
        }
        for (int i = 0; i < noFilt.length; i++) {
            if (!test.connectedWithResultsOfAllThru(noFilt, DumbList.ME)) return false;
        }
        for (int i = 0; i < yaFilt.length; i++) {
            CollatingStructure coll = _iters[checkedCollStruct._int].getCurrent();
            NexterCloseable<byte[]> froms = Store.STORE.getResultOfStoredThru(sh.key.getKeyB(), test.key.getKeyB(), yaFilt[i].getStoredHelper().storedHelperQuery.key.getKeyB(), coll);
            boolean haveFrom = false;
            while (froms.next()) {
                MetImpl cur = (MetImpl) ((Src) Source.SRC).getMetByKey(froms.getCurrent());
                if (!_isResultOf(yaFilt[i], cur, checkedCollStruct)) {
                    if (yaFilt[i]._ord != null || yaFilt[i]._filt != null) {
                        checkedCollStruct._int--;
                        if (yaFilt[i]._qrys.length < 0) {
                            haveFrom = true;
                            break;
                        }
                    }
                } else {
                    haveFrom = true;
                    break;
                }
            }
            froms.close();
            if (!haveFrom) {
                return false;
            }
        }
        return true;
    }
}
