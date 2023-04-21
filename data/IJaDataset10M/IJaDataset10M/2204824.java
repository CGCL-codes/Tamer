package filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.exception.NeoException;

public class TransactionFilter extends OncePerRequestFilter {

    private TransactionTemplate transactionTemplate;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        transactionTemplate = Neo.getObject(TransactionTemplate.class);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        transactionTemplate.execute(new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
                try {
                    filterChain.doFilter(request, response);
                } catch (IOException e) {
                    throw new NeoException(e.getMessage(), e);
                } catch (ServletException e) {
                    throw new NeoException(e.getMessage(), e);
                }
                return null;
            }
        });
    }
}
