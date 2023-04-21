package br.unb.bioagents.behaviours;

import java.io.File;
import java.util.List;
import java.util.Vector;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import br.unb.bioagents.agents.BioAgent;
import br.unb.bioagents.hibernate.DatabasePoints;
import br.unb.bioagents.hibernate.HibernateUtil;
import br.unb.bioagents.ontology.RequestSugestion;
import br.unb.bioagents.util.PointsUtil;

public abstract class AnalysisResponderBehaviour<T_BIOAGENT extends BioAgent<?, ?>> extends ResponderBehaviour<T_BIOAGENT> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 439674361293106319L;

    private List<String> filesToDelete;

    public AnalysisResponderBehaviour(T_BIOAGENT a, T_BIOAGENT.WorkingGroup workingGroup, RequestSugestion requestSugestion) {
        super(a, workingGroup, requestSugestion);
        setFilesToDelete(new Vector<String>());
    }

    protected List<String> getFilesToDelete() {
        return filesToDelete;
    }

    private void setFilesToDelete(List<String> filesToDelete) {
        this.filesToDelete = filesToDelete;
    }

    protected void deleteFiles() {
        for (String file : getFilesToDelete()) {
            File f = new File(file);
            if (f.exists()) {
                f.delete();
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected Integer getQtdSugestions() {
        int totalPoints = 0;
        Session session = HibernateUtil.getSession();
        Criteria criteria = session.createCriteria(DatabasePoints.class).add(Restrictions.eq("database", getRequestSugestion().getConfigsWrapper().get(0).getDataBase()));
        List<DatabasePoints> data = criteria.list();
        if (data.size() >= 1) {
            for (DatabasePoints points : data) {
                totalPoints += points.getPoints();
            }
        }
        return new Integer(PointsUtil.normalizePoints(totalPoints));
    }
}
