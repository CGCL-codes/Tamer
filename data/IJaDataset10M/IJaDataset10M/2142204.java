package deprecated;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import com.minfo.common.StringUtil;
import com.minfo.dao.hibernate.PoolHibernateDAO;
import com.minfo.mgr.PoolManager;
import com.minfo.model.Answer;
import com.minfo.model.Pool;

public class AddEditPoolController {

    private static Logger log = Logger.getLogger(AddEditPoolController.class);

    private PoolManager poolManager;

    private Pool pool;

    private Long poolId;

    private boolean init = false;

    public AddEditPoolController() {
        log.debug("AddEditPoolController()");
        FacesContext context = FacesContext.getCurrentInstance();
        String poolIdStr = (String) context.getExternalContext().getRequestParameterMap().get("poolId");
        log.debug("poolId=" + poolIdStr);
        if (!StringUtil.emptyString(poolIdStr)) {
            this.poolId = new Long(poolIdStr);
        }
    }

    public String performPool() {
        log.debug("enter performPool");
        log.debug("pool=" + pool);
        if (pool == null || pool.getId() == 0) {
            poolManager.addPool(pool);
        } else {
            poolManager.updatePool(pool);
        }
        return "success";
    }

    public String addAnswer() {
        log.debug("enter addAnswer");
        init();
        pool.getAnswers().add(new Answer());
        return "success";
    }

    public void setPoolManager(PoolManager poolManager) {
        this.poolManager = poolManager;
    }

    public Long getId() {
        init();
        return pool.getId();
    }

    public String getQuestion() {
        init();
        return pool.getQuestion();
    }

    public void setQuestion(String question) {
        log.debug("setQuestion(" + question + ")");
        pool.setQuestion(question);
    }

    public void setId(Long id) {
        log.debug("setId(" + id + ")");
        pool.setId(id);
        poolId = id;
    }

    public List<Answer> getAnswers() {
        log.debug("enter getAnswers()");
        init();
        List l = new LinkedList<Answer>();
        if (pool.getAnswers() != null) {
            l.addAll(pool.getAnswers());
        }
        return l;
    }

    private void init() {
        if (!init) {
            log.debug("initializing...");
            log.debug("poolId=" + poolId);
            if (poolId != null) {
                pool = poolManager.getPool(new Long(poolId));
            } else {
                pool = new Pool();
            }
            log.debug("Got pool:" + pool);
            if (pool.getAnswers() == null) {
                pool.setAnswers(new LinkedList<Answer>());
            }
            init = true;
        }
    }
}
