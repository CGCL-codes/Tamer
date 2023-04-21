package sketch.random;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import randoop.util.Timer;
import sketch.ast.ASTUtils;
import sketch.ounit.fuzz.SequencePool;
import sketch.specs.RandomValueExtractor;
import sketch.specs.RandomValueInfo;
import sketch.specs.SketchTestProcessor;
import junit.framework.TestCase;

public class RandomValueExampleTest extends TestCase {

    private String sketch_path = "./testfiles/sketch/random/OnlyRandomValueExamples.java";

    private int pool_gen_time_limit = 10;

    private int test_limit = 500;

    private int test_gen_time_limit = 10;

    public void testFillinRandomValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        SketchTestProcessor processor = new SketchTestProcessor(sketch_path);
        CompilationUnit newUnit = processor.getProcessedCompilationUnit();
        RandomValueExtractor extractor = new RandomValueExtractor(newUnit);
        Map<MethodDeclaration, List<RandomValueInfo>> random_values = extractor.extract_random_values();
        assertEquals(4, random_values.size());
        Collection<Class<?>> all_classes = new LinkedHashSet<Class<?>>();
        for (List<RandomValueInfo> list : random_values.values()) {
            for (RandomValueInfo info : list) {
                all_classes.addAll(info.class_list);
            }
        }
        SequencePool pool = new SequencePool(this.pool_gen_time_limit, (Class<?>[]) all_classes.toArray(new Class<?>[0]));
        Class<?> junit_test_class = OnlyRandomValueExamples.class;
        Object junit_test_obj = null;
        try {
            junit_test_obj = junit_test_class.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Timer t = new Timer();
        t.startTiming();
        int count = 0;
        while (t.getTimeElapsedMillis() < 1000 * this.test_gen_time_limit && count < this.test_limit) {
            for (MethodDeclaration method : random_values.keySet()) {
                String methodName = method.getName().toString();
                Method junit_method = junit_test_class.getMethod(methodName);
                junit_method.invoke(junit_test_obj);
            }
        }
        t.stopTiming();
        System.out.println(newUnit);
    }
}
