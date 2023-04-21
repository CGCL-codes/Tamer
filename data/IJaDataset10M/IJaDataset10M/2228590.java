package org.easy.eao.spring.jpa;

import java.util.List;
import org.easy.eao.annotations.Target;
import org.easy.eao.spring.jpa.model.Exam;

public interface TargetEao {

    @Target("targetBean")
    List<Exam> method(String student);
}
