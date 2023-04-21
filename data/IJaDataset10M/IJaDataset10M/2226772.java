package org.simpleframework.http.core;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class PerformanceError {

    @Element(data = true)
    private String overflow;

    public PerformanceError() {
        super();
    }

    public PerformanceError(String overflow) {
        this.overflow = overflow;
    }
}
