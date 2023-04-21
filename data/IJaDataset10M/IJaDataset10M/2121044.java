package br.net.woodstock.rockframework.domain.spring;

import org.springframework.core.type.filter.AssignableTypeFilter;
import br.net.woodstock.rockframework.domain.Entity;

public class AssignableEntityDetector extends AbstractEntityDetector {

    public AssignableEntityDetector() {
        super(new AssignableTypeFilter(Entity.class));
    }
}
