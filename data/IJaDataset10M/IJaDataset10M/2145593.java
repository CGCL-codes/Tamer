package org.monet.docservice.core.log.impl;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Mike Mirzayanov (mirzayanovmr@gmail.com)
 */
public class ThrowableUtil {

    public static String getStacktrace(Throwable e) {
        Set<Throwable> used = new HashSet<Throwable>();
        StringBuilder sb = new StringBuilder();
        while (e != null && !used.contains(e)) {
            if (used.size() > 0) {
                sb.append("\n");
            }
            used.add(e);
            sb.append(e.getClass().getName()).append(": ").append(e.getMessage()).append("\n");
            StackTraceElement[] elements = e.getStackTrace();
            for (StackTraceElement element : elements) {
                sb.append("    ").append(element.toString()).append("\n");
            }
            e = e.getCause();
        }
        return sb.toString();
    }
}
