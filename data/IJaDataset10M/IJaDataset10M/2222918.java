package org.dev2live.process;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import junit.framework.Assert;
import org.dev2live.arguments.Argument;
import org.junit.Test;

/**
 * @author bertram
 *
 */
public class JoinTest {

    /**
	 * Test method for {@link org.dev2live.process.Join#call()}.
	 */
    @Test
    public void testCall() {
        Element join = new Join();
        try {
            Assert.assertEquals("join", join.call().getValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
