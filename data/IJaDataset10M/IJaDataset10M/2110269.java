package estructuras;

import java.util.Vector;
import java.util.Hashtable;
import javax.swing.DefaultComboBoxModel;
import callbacks.InstrMinMaxChanged;

public class Instrumento extends IDable implements Comparable {

    public String name = null, info = null;

    public int ID;

    public Vector<Double> mintheta = null, maxtheta = null;

    public Vector<Vector<Double>> P = null, T = null, volP = null, volT = null, Q = null, U = null, alfas = null, D = null;

    public Vector<Vector<Fecha>> fechas = null;

    public Vector<Integer> IDTable = null;

    public Vector<Integer> MonedaIDtable = null;

    public DefaultComboBoxModel modelo = null;

    public Double defMin = null, defMax = null;

    public DefaultComboBoxModel modeloLimites = null;

    public String cmbInstrSelecEstrLastSelected = "null";

    public InstrMinMaxChanged callback = null;

    public Proyecto proy = null;

    public Instrumento() {
    }

    public Instrumento(InstrMinMaxChanged event, Proyecto proy) {
        this(new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Double>(), new Vector<Double>(), "default", "", new Vector<Integer>(), event, proy);
    }

    public Instrumento(String name, String info, InstrMinMaxChanged event, Proyecto proy) {
        this(new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Double>(), new Vector<Double>(), name, info, new Vector<Integer>(), event, proy);
    }

    public Instrumento(String name, String info, Double min, Double max, InstrMinMaxChanged event, Proyecto proy) {
        this(new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Vector<Double>>(), new Vector<Double>(), new Vector<Double>(), name, info, new Vector<Integer>(), event, proy);
        this.defMin = min;
        this.defMax = max;
    }

    public Instrumento(Vector<Vector<Double>> P, Vector<Vector<Double>> T, Vector<Vector<Double>> volP, Vector<Vector<Double>> volT, Vector<Vector<Double>> Q, Vector<Vector<Double>> U, Vector<Vector<Double>> alfas, Vector<Vector<Double>> D, Vector<Double> mintheta, Vector<Double> maxtheta, String nombre, String info, Vector<Integer> IDTable, InstrMinMaxChanged event, Proyecto proy) {
        super();
        this.ID = IDable.getID();
        this.P = P;
        this.T = T;
        this.volP = volP;
        this.volT = volT;
        this.Q = Q;
        this.U = U;
        this.alfas = alfas;
        this.D = D;
        this.mintheta = mintheta;
        this.maxtheta = maxtheta;
        this.name = nombre;
        this.info = info;
        this.IDTable = IDTable;
        this.modelo = new DefaultComboBoxModel();
        this.defMin = 0.0;
        this.defMax = 1.0;
        this.modeloLimites = new DefaultComboBoxModel();
        this.callback = event;
        this.fechas = new Vector<Vector<Fecha>>();
        this.MonedaIDtable = new Vector<Integer>();
        this.proy = proy;
    }

    public Instrumento(String name, String info, Vector<Vector<Double>> alfas, String cmbInstrSelecEstrLastSelected, Vector<Vector<Double>> D, Double defMax, Double defMin, Vector<Vector<Fecha>> fechas, Vector<Integer> IDTable, Vector<Double> maxtheta, Vector<Double> mintheta, DefaultComboBoxModel modelo, DefaultComboBoxModel modeloLimites, Vector<Integer> MonedaIDTable, Vector<Vector<Double>> P, Vector<Vector<Double>> Q, Vector<Vector<Double>> T, Vector<Vector<Double>> U, Vector<Vector<Double>> volP, Vector<Vector<Double>> volT, int ID, Proyecto proy) {
        super();
        this.ID = ID;
        this.P = P;
        this.T = T;
        this.volP = volP;
        this.volT = volT;
        this.Q = Q;
        this.U = U;
        this.alfas = alfas;
        this.D = D;
        this.mintheta = mintheta;
        this.maxtheta = maxtheta;
        this.name = name;
        this.info = info;
        this.IDTable = IDTable;
        this.modelo = modelo;
        this.defMin = defMin;
        this.defMax = defMax;
        this.modeloLimites = modeloLimites;
        this.fechas = fechas;
        this.MonedaIDtable = MonedaIDTable;
        this.cmbInstrSelecEstrLastSelected = cmbInstrSelecEstrLastSelected;
        this.proy = proy;
    }

    public void setInstrMinMaxChanged(InstrMinMaxChanged in) {
        this.callback = in;
    }

    public void addEstrategia(int ID, String name, Fecha fech_ini, int MonedaID) {
        if (this.indexOfEstrategia(ID) == -1) {
            this.IDTable.add(new Integer(ID));
            this.alfas.add(new Vector<Double>());
            this.D.add(new Vector<Double>());
            this.maxtheta.add(this.defMax);
            this.mintheta.add(this.defMin);
            this.P.add(new Vector<Double>());
            this.Q.add(new Vector<Double>());
            this.T.add(new Vector<Double>());
            this.U.add(new Vector<Double>());
            this.volP.add(new Vector<Double>());
            this.volT.add(new Vector<Double>());
            this.modelo.addElement(name);
            this.modeloLimites.addElement(name);
            this.fechas.addElement(new Vector<Fecha>());
            this.setFechaInicial(ID, fech_ini);
            Integer monedota = new Integer(MonedaID);
            this.MonedaIDtable.add(monedota);
        }
    }

    public void setMoneda(int ID, int MonedaID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.MonedaIDtable.set(index, MonedaID);
        }
    }

    public int getMoneda(int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            return this.MonedaIDtable.get(index);
        } else return -1;
    }

    public String getMonedaS(int ID) {
        int monID = getMoneda(ID);
        for (int i = 0; i < proy.monedas.size(); i++) if (proy.monedas.elementAt(i).ID == monID) return proy.monedas.elementAt(i).name;
        return null;
    }

    public void setFechaInicial(int ID, Fecha ini) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            if (this.fechas.elementAt(index).isEmpty()) this.fechas.elementAt(index).add(ini); else this.fechas.elementAt(index).set(0, ini);
        }
    }

    public Vector<Fecha> getFechas(int ID) {
        int indexEstr = this.indexOfEstrategia(ID);
        return this.fechas.elementAt(indexEstr);
    }

    public Fecha getFecha(int ID, int index) {
        int indexEstr = this.indexOfEstrategia(ID);
        return this.fechas.elementAt(indexEstr).elementAt(index);
    }

    public void setFecha(int ID, int index, Fecha fech) {
        int indexEstr = this.indexOfEstrategia(ID);
        this.fechas.elementAt(indexEstr).set(index, fech);
    }

    public void addFecha(int ID, Fecha fech) {
        int indexEstr = this.indexOfEstrategia(ID);
        this.alfas.elementAt(indexEstr).add(null);
        this.P.elementAt(indexEstr).add(new Double(1));
        this.Q.elementAt(indexEstr).add(new Double(0));
        this.U.elementAt(indexEstr).add(new Double(0));
        this.volP.elementAt(indexEstr).add(new Double(0));
        this.volT.elementAt(indexEstr).add(new Double(0));
        this.fechas.elementAt(indexEstr).add(fech);
    }

    public void removeFecha(int ID, int index) {
        int indexEstr = this.indexOfEstrategia(ID);
        this.alfas.elementAt(indexEstr).remove(index);
        this.P.elementAt(indexEstr).remove(index - 1);
        this.Q.elementAt(indexEstr).remove(index - 1);
        this.U.elementAt(indexEstr).remove(index - 1);
        this.volP.elementAt(indexEstr).remove(index - 1);
        this.volT.elementAt(indexEstr).remove(index - 1);
        this.fechas.elementAt(indexEstr).remove(index);
    }

    public void setP(Vector<Double> P, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.P.elementAt(index).clear();
            this.P.elementAt(index).addAll(P);
        }
    }

    public void setT(Vector<Double> T, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.T.elementAt(index).clear();
            this.T.elementAt(index).addAll(T);
        }
    }

    public void setD(Vector<Double> D, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.D.elementAt(index).clear();
            this.D.elementAt(index).addAll(D);
        }
    }

    public Vector<Double> getP(int ID) {
        int index = this.indexOfEstrategia(ID);
        return this.P.elementAt(index);
    }

    public Vector<Double> getT(int ID) {
        int index = this.indexOfEstrategia(ID);
        return this.T.elementAt(index);
    }

    public Vector<Double> getD(int ID) {
        int index = this.indexOfEstrategia(ID);
        return this.D.elementAt(index);
    }

    public void addVolP(Vector<Double> volP, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.volP.elementAt(index).clear();
            this.volP.elementAt(index).addAll(volP);
        }
    }

    public void setVolP(Vector<Double> volP, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.volP.elementAt(index).clear();
            this.volP.elementAt(index).addAll(volP);
        }
    }

    public void setVolT(Vector<Double> volT, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.volT.elementAt(index).clear();
            this.volT.elementAt(index).addAll(volT);
        }
    }

    public Vector<Double> getVolP(int ID) {
        int index = this.indexOfEstrategia(ID);
        return this.volP.elementAt(index);
    }

    public Vector<Double> getVolT(int ID) {
        int index = this.indexOfEstrategia(ID);
        return this.volT.elementAt(index);
    }

    public void setQ(Vector<Double> Q, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.Q.elementAt(index).clear();
            this.Q.elementAt(index).addAll(Q);
        }
    }

    public void setFechas(Vector<Fecha> fechas, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.fechas.elementAt(index).clear();
            this.fechas.elementAt(index).addAll(fechas);
        }
    }

    public Vector<Double> getQ(int ID) {
        int index = this.indexOfEstrategia(ID);
        return this.Q.elementAt(index);
    }

    public void setU(Vector<Double> U, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.U.elementAt(index).clear();
            this.U.elementAt(index).addAll(U);
        }
    }

    public Vector<Double> getU(int ID) {
        int index = this.indexOfEstrategia(ID);
        return this.U.elementAt(index);
    }

    public void setAlfas(Vector<Double> alfas, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.alfas.elementAt(index).clear();
            this.alfas.elementAt(index).addAll(alfas);
        }
    }

    public Vector<Double> getAlfas(int ID) {
        int index = this.indexOfEstrategia(ID);
        return this.alfas.elementAt(index);
    }

    public void setMaxTheta(Double mt, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.maxtheta.set(index, mt);
            this.doNotify(ID);
        }
    }

    public Double getMaxTheta(int ID) {
        int index = this.indexOfEstrategia(ID);
        return this.maxtheta.elementAt(index);
    }

    public void setMinTheta(Double mt, int ID) {
        int index = this.indexOfEstrategia(ID);
        if (index != -1) {
            this.mintheta.set(index, mt);
            this.doNotify(ID);
        }
    }

    public Double getMinTheta(int ID) {
        int index = this.indexOfEstrategia(ID);
        return this.mintheta.elementAt(index);
    }

    public void removeEstrategia(int ID) {
        int index;
        if ((index = this.indexOfEstrategia(ID)) != -1) {
            this.IDTable.remove(index);
            this.alfas.remove(index);
            this.D.remove(index);
            this.maxtheta.remove(index);
            this.mintheta.remove(index);
            this.P.remove(index);
            this.Q.remove(index);
            this.T.remove(index);
            this.U.remove(index);
            this.volP.remove(index);
            this.volT.remove(index);
            this.modelo.removeElementAt(index);
        }
    }

    public int indexOfEstrategia(int ID) {
        for (int i = 0; i < this.IDTable.size(); i++) {
            if (ID == IDTable.elementAt(i).intValue()) return i;
        }
        return -1;
    }

    public int compareTo(Object o) {
        if (o instanceof Instrumento) {
            return this.name.compareTo(((Instrumento) o).name);
        }
        return this.name.compareTo(o.toString());
    }

    public void doNotify(int estrID) {
        callback.instrMinMaxChangedEvent(this.ID, estrID);
    }
}
