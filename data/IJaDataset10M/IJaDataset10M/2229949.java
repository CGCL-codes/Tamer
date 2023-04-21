package ua.od.lonewolf.Crow.View.Wizard.TraceWizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ua.od.lonewolf.Crow.Model.Element.CRS;
import ua.od.lonewolf.Crow.Model.Element.IDependent;
import ua.od.lonewolf.Crow.Model.Element.IElement;
import ua.od.lonewolf.Crow.Model.Element.RegisterFactory;
import ua.od.lonewolf.Crow.Model.Element.Trace.SimpleTraceabilityInfo;
import ua.od.lonewolf.Crow.Model.Element.Trace.TraceUtilitites;
import ua.od.lonewolf.Crow.Util.Pair;
import ua.od.lonewolf.Crow.Util.TypeUtilities;

public class TraceMatrixTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private TraceReportParameters params = null;

    private List<? extends IElement> fromElements;

    private List<? extends IDependent> toElements;

    private Map<Pair<Long, Long>, SimpleTraceabilityInfo> traceInfo;

    private Map<IElement, Boolean> isTraced;

    private Session session = null;

    public TraceMatrixTableModel(TraceReportParameters params, Session session, RegisterFactory factory) {
        super();
        this.params = params;
        this.session = session;
        refresh();
    }

    public TraceReportParameters getParameters() {
        return params;
    }

    public void refresh() {
        fromElements = fetchRows();
        toElements = fetchCols();
        prefetchTraces();
        fireTableStructureChanged();
    }

    @SuppressWarnings("unchecked")
    protected List<? extends IElement> fetchRows() {
        session.beginTransaction();
        Criteria crit = session.createCriteria(getParameters().getFromClass());
        crit.add(Restrictions.eq("productId", getParameters().getProductId()));
        List<? extends IElement> list = crit.list();
        session.getTransaction().commit();
        return list;
    }

    @SuppressWarnings("unchecked")
    protected List<? extends IDependent> fetchCols() {
        session.beginTransaction();
        Criteria crit = session.createCriteria(getParameters().getToClass());
        crit.add(Restrictions.eq("productId", getParameters().getProductId()));
        List<? extends IDependent> list = crit.list();
        session.getTransaction().commit();
        return list;
    }

    protected void prefetchTraces() {
        traceInfo = new HashMap<Pair<Long, Long>, SimpleTraceabilityInfo>();
        isTraced = new HashMap<IElement, Boolean>();
        Iterator<? extends IElement> itFrom = fromElements.iterator();
        while (itFrom.hasNext()) {
            IElement elFrom = itFrom.next();
            ArrayList<Long> arrToExpr = new ArrayList<Long>();
            Iterator<? extends IDependent> itTo = toElements.iterator();
            while (itTo.hasNext()) {
                IDependent elTo = itTo.next();
                arrToExpr.add(TraceUtilitites.getExpression(elTo.getId(), TypeUtilities.classToEnclosureType(((IElement) elTo).getClass())));
            }
            List<Long> res = TraceUtilitites.findTrace(TraceUtilitites.getExpression(elFrom.getId(), TypeUtilities.classToEnclosureType(((IElement) elFrom).getClass())), arrToExpr, session);
            Iterator<Long> it = res.iterator();
            while (it.hasNext()) {
                long toExpr = it.next();
                long toId = TraceUtilitites.getIdFromExpression(toExpr);
                SimpleTraceabilityInfo info = new SimpleTraceabilityInfo(elFrom.getId(), toId);
                traceInfo.put(new Pair<Long, Long>(elFrom.getId(), toId), info);
                isTraced.put(elFrom, Boolean.TRUE);
            }
        }
    }

    public boolean isTracedByAnything(int row) {
        return (isTraced.get(fromElements.get(row)) != null && isTraced.get(fromElements.get(row)).equals(Boolean.TRUE));
    }

    public boolean isInactive(int row) {
        IElement el = fromElements.get(row);
        if (el instanceof CRS) {
            return ((CRS) el).getInactive();
        }
        return false;
    }

    public IElement getRowElement(int row) {
        return fromElements.get(row);
    }

    @Override
    public Class<?> getColumnClass(int arg0) {
        return SimpleTraceabilityInfo.class;
    }

    @Override
    public int getColumnCount() {
        return toElements.size();
    }

    @Override
    public int getRowCount() {
        return fromElements.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        IElement fromEl = fromElements.get(row);
        IDependent toEl = toElements.get(col);
        SimpleTraceabilityInfo info = traceInfo.get(new Pair<Long, Long>(fromEl.getId(), toEl.getId()));
        return info;
    }

    @Override
    public String getColumnName(int column) {
        IDependent el = toElements.get(column);
        String code = ((IElement) el).getCode();
        if (code != null && code.length() > 0) {
            return code;
        } else {
            return el.toString();
        }
    }

    public String getRowName(int row) {
        IElement el = fromElements.get(row);
        String code = el.getCode();
        if (code != null && code.length() > 0) {
            return code;
        } else {
            return el.toString();
        }
    }

    public String getLongestRow() {
        String longestRow = "";
        Iterator<? extends IElement> itFrom = fromElements.iterator();
        while (itFrom.hasNext()) {
            IElement elFrom = itFrom.next();
            String strRep = elFrom.getCode();
            if (strRep == null || strRep.length() == 0) {
                strRep = elFrom.toString();
            }
            if (strRep.length() > longestRow.length()) {
                longestRow = strRep;
            }
        }
        return longestRow;
    }
}
