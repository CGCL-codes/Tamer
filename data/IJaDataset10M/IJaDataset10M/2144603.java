package tony;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.example.possessed.SerializableRunnable;

public class BatchQueue {

    private static Executor exec = Executors.newSingleThreadExecutor();

    public static void submit(SerializableRunnable runable) {
        exec.execute(runable);
    }
}
