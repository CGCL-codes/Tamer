package green.search.component;

import green.search.query.SVDQuery;
import java.io.IOException;
import org.apache.solr.handler.component.QueryComponent;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;

public class TestComponent extends QueryComponent {

    @Override
    public String getDescription() {
        return "svd_query";
    }

    @Override
    public String getSource() {
        return "(^o^;) < getSource";
    }

    @Override
    public String getSourceId() {
        return "(^o^;) < getSourceId";
    }

    @Override
    public String getVersion() {
        return "(^o^;) < getVersion";
    }

    @Override
    public void prepare(ResponseBuilder rb) throws IOException {
        System.out.println("### prepare");
        super.prepare(rb);
        System.out.println("### Query" + rb.getQuery().getClass());
    }

    @Override
    public void process(ResponseBuilder rb) throws IOException {
        System.out.println("### process");
        System.out.println("### Query = " + rb.getQuery().getClass());
        rb.setQuery(new SVDQuery(rb.getQuery()));
        super.process(rb);
        System.out.println("### Query = " + rb.getQuery().getClass());
    }
}
