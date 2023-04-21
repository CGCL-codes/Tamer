package balmysundaycandy.more.low.level.operations.datastore.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import balmysundaycandy.core.test.EnvironmentConfiguration;
import balmysundaycandy.core.test.TestEnvironmentUtils;
import balmysundaycandy.more.low.level.operations.datastore.DatastoreOperations;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.DatastorePb.Cursor;
import com.google.apphosting.api.DatastorePb.NextRequest;
import com.google.apphosting.api.DatastorePb.Query;
import com.google.apphosting.api.DatastorePb.QueryResult;

public class NextOperationTest {

    EnvironmentConfiguration environmentConfiguration = new EnvironmentConfiguration("", false, true);

    @Before
    public void setup() {
        TestEnvironmentUtils.setupEnvironment(environmentConfiguration);
    }

    @After
    public void teardown() {
        TestEnvironmentUtils.teardownEnvironment(environmentConfiguration);
    }

    @Test
    public void testCallNextRequest() {
        Query query = new Query();
        QueryResult queryResult = DatastoreOperations.RUN_QUERY.call(query);
        Cursor cursor = new Cursor();
        cursor.setCursor(queryResult.getCursor().getCursor());
        NextRequest request = new NextRequest();
        request.setCursor(cursor);
        QueryResult response = DatastoreOperations.NEXT.call(request);
        assertThat(response, is(not(nullValue())));
    }

    @Test
    public void testCallAsyncNextRequestApiConfig() throws InterruptedException, ExecutionException {
        Query query = new Query();
        QueryResult queryResult = DatastoreOperations.RUN_QUERY.call(query);
        Cursor cursor = new Cursor();
        cursor.setCursor(queryResult.getCursor().getCursor());
        NextRequest request = new NextRequest();
        request.setCursor(cursor);
        Future<QueryResult> response = DatastoreOperations.NEXT.callAsync(request, new ApiConfig());
        assertThat(response, is(not(nullValue())));
        assertThat(response.get(), is(not(nullValue())));
    }
}
