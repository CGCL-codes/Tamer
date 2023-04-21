package indiji.ml.impl;

import indiji.io.Log;
import indiji.ml.MLTree;
import indiji.ml.Model;
import indiji.struct.Dat;
import indiji.struct.IFloat;
import indiji.struct.ISet;
import indiji.struct.IVec;
import indiji.struct.Mat;
import indiji.struct.Params;
import indiji.struct.Dat.Flavor;
import java.io.File;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map.Entry;

public class OneVsRestModel implements Model {

    public String getName() {
        return "onevsrest";
    }

    @Override
    public ISet createChildren(MLTree mlc, Integer expid, Params dataparams, Params modelparams, Dat dat) {
        if (dat == null) return null;
        Vector<Params> dp = new Vector<Params>();
        for (Integer classid : dat.getY().getColumnIDs()) dp.add(new Params(dataparams.toString() + "&posclass=" + classid));
        Params mp = modelparams.getSubParams("model");
        return mlc.createModelChildren(expid, dp, mp);
    }

    @Override
    public Dat joinChildren(MLTree mlc, Integer expid, Params dataparams, Params modelparams, Dat dat, ISet modelchildren) {
        try {
            ResultSet r = mlc.getDB().sqlreq("select dataparams,predfile from ml_experiments where parentexp=" + expid + " and parenttype=1");
            HashMap<Integer, String> tmp = new HashMap<Integer, String>();
            while (r.next()) tmp.put(new Params(r.getString(1)).getInt("posclass"), r.getString(2));
            r.close();
            Mat preds = new Mat();
            for (Entry<Integer, String> e : tmp.entrySet()) {
                preds.add(mlc.loadDat(expid, new File(e.getValue())).getX());
            }
            if (dataparams.getBool("atleastone", false)) for (IVec e : preds) if (e.value.maxIFloat().value < 0) e.value.maxIFloat().value = 1;
            Dat result = new Dat();
            result.setX(preds);
            result.setY(dat.getY());
            result.setPartitions(dat.getPartitions().clone());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.err(e.getMessage());
            mlc.putError(expid, e.getMessage());
        }
        return null;
    }

    @Override
    public Dat learn(MLTree mlc, Integer expid, Params dataparams, Params modelparams, Dat dat) {
        return null;
    }

    @Override
    public boolean autoJoin() {
        return false;
    }
}
