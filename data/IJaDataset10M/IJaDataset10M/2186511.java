package org.fudaa.fudaa.lido.tableau;

import java.util.Comparator;
import javax.swing.event.TableModelEvent;
import org.fudaa.dodico.corba.lido.SParametresSectionLigneCAL;
import org.fudaa.fudaa.commun.projet.FudaaParamEvent;
import org.fudaa.fudaa.commun.projet.FudaaParamEventProxy;
import org.fudaa.fudaa.lido.LidoResource;
import org.fudaa.fudaa.lido.ihmhelper.gestion.LidoParamsHelper;

/**
 * @version      $Revision: 1.10 $ $Date: 2006-09-19 15:05:00 $ by $Author: deniger $
 * @author       Axel von Arnim 
 */
public class LidoTableauPermSection3s extends LidoTableauBase {

    public LidoTableauPermSection3s() {
        super();
        init();
    }

    private void init() {
        setModel(new LidoTableauPermSection3sModel(new SParametresSectionLigneCAL[0]));
    }

    public void reinitialise() {
        final SParametresSectionLigneCAL[] pers = (SParametresSectionLigneCAL[]) getObjects(false);
        if (pers == null) {
            return;
        }
        ((LidoTableauPermSection3sModel) getModel()).setObjects(pers);
        tableChanged(new TableModelEvent(getModel()));
    }

    protected String getPropertyName() {
        return "permSection3s";
    }

    protected static String getObjectFieldNameByColumn(final int col) {
        String r = "";
        switch(col) {
            case 0:
                r = "absSect";
                break;
        }
        return r;
    }

    protected Comparator getComparator() {
        return LidoParamsHelper.PERMSECTION3_COMPARATOR();
    }
}

class LidoTableauPermSection3sModel extends LidoTableauBaseModel {

    public LidoTableauPermSection3sModel(final SParametresSectionLigneCAL[] _pers) {
        super(_pers);
    }

    protected Object[] getTableauType(final int taille) {
        return new SParametresSectionLigneCAL[taille];
    }

    public Class getColumnClass(final int column) {
        switch(column) {
            case 0:
                return Double.class;
            case 1:
                return Boolean.class;
            default:
                return null;
        }
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(final int column) {
        String r = "";
        switch(column) {
            case 0:
                r = "Abscisse";
                break;
            case 1:
                r = "s�lection";
                break;
        }
        return r;
    }

    public Object getValueAt(final int row, final int column) {
        Object r = null;
        final SParametresSectionLigneCAL[] pers = (SParametresSectionLigneCAL[]) getObjects();
        if ((low_ + row) < high_) {
            switch(column) {
                case 0:
                    r = new Double(pers[low_ + row].absSect);
                    break;
                case 1:
                    r = new Boolean(dirty_[low_ + row]);
                    break;
            }
        }
        return r;
    }

    public boolean isCellEditable(final int row, final int column) {
        return ((column != 1));
    }

    public void setValueAt(final Object value, final int row, final int column) {
        if ((low_ + row) < high_) {
            final SParametresSectionLigneCAL[] pers = (SParametresSectionLigneCAL[]) getObjects();
            final Object old = getValueAt(row, column);
            switch(column) {
                case 0:
                    pers[low_ + row].absSect = ((Double) value).doubleValue();
                    break;
            }
            if (!old.equals(value)) {
                dirty_[low_ + row] = true;
                FudaaParamEventProxy.FUDAA_PARAM.fireParamStructModified(new FudaaParamEvent(this, 0, LidoResource.CAL, pers[low_ + row], "section " + pers[low_ + row].absSect));
            }
        }
    }
}
