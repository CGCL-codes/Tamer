package org.metatemplate;

import java.util.List;
import java.util.Map;

public interface ProcessorWithHeredoc extends Processor {

    public Content invoke(Map<String, List<Content>> args, List<Content> heredoc);
}
