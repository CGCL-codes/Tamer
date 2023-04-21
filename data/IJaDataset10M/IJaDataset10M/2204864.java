package de.wieger.aspectj.runtime.reflect;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import de.wieger.aspectj.runtime.reflect.ReflectionUtil;

@Test
public class TestReflectionUtil {
    public void test_get_private_itd_method() throws NoSuchMethodException {
        assertNotNull(ReflectionUtil.getAccessibleMethod(Sample.class, "getName"));
        assertNotNull(ReflectionUtil.getAccessibleMethod(Sample.class, "setName", new Class<?>[] { String.class }));
    }

    public void test_get_private_itd_method_for_inner_class() throws NoSuchMethodException {
        assertNotNull(ReflectionUtil.getAccessibleMethod(InnerSample.class, "getName"));
        assertNotNull(ReflectionUtil.getAccessibleMethod(InnerSample.class, "setName", new Class<?>[] { String.class }));
    }

    static class InnerSample {
    }

    static aspect InnerSample_ITD {
        @SuppressWarnings("unused")
        private String InnerSample.getName() {
            return "Alfred E. Neumann";
        }

        @SuppressWarnings("unused")
        private void InnerSample.setName(String pName) {
            // intentionally left blank
        }
    }
}

class Sample {
}

aspect Sample_ITD {
    @SuppressWarnings("unused")
    private String Sample.getName() {
        return "Alfred E. Neumann";
    }

    @SuppressWarnings("unused")
    private void Sample.setName(String pName) {
        // intentionally left blank
    }
}
