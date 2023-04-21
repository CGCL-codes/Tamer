package schemacrawler.tools.lint;

import java.io.Serializable;

public interface Lint<V extends Serializable> extends Serializable, Comparable<Lint<?>> {

    String getId();

    String getMessage();

    String getObjectName();

    LintSeverity getSeverity();

    V getValue();

    String getValueAsString();
}
