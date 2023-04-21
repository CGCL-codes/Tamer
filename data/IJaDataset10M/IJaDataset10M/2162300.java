package magoffin.matt.tidbits.aop;

import java.lang.reflect.Method;
import java.util.List;
import magoffin.matt.tidbits.domain.Tidbit;
import magoffin.matt.tidbits.lucene.LuceneBiz;
import org.springframework.aop.AfterReturningAdvice;

/**
 * Aspect to index a Tidbit after it has changed.
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 1.2 $ $Date: 2006/07/09 05:53:57 $
 */
public class TidbitIndexInterceptor implements AfterReturningAdvice {

    private LuceneBiz luceneBiz;

    @SuppressWarnings("unchecked")
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        if (returnValue instanceof Tidbit) {
            Long tidbitId = ((Tidbit) returnValue).getTidbitId();
            if (tidbitId != null) {
                luceneBiz.indexTidbit(tidbitId);
            }
        } else if (returnValue instanceof List) {
            List<Long> resultIds = (List<Long>) returnValue;
            for (Long tidbitId : resultIds) {
                luceneBiz.indexTidbit(tidbitId);
            }
        }
    }

    /**
	 * @return the luceneBiz
	 */
    public LuceneBiz getLuceneBiz() {
        return luceneBiz;
    }

    /**
	 * @param luceneBiz the luceneBiz to set
	 */
    public void setLuceneBiz(LuceneBiz luceneBiz) {
        this.luceneBiz = luceneBiz;
    }
}
