package org.goda.chronic.handlers;

import java.util.List;
import org.goda.chronic.Options;
import org.goda.util.CollectionUtils;
import org.goda.chronic.utils.Token;
import org.goda.time.MutableInterval;

public class ORGRHandler extends ORRHandler {

    public MutableInterval handle(List<Token> tokens, Options options) {
        MutableInterval outerMutableInterval = Handler.getAnchor(CollectionUtils.subList(tokens, 2, 4), options);
        return handle(CollectionUtils.subList(tokens, 0, 2), outerMutableInterval, options);
    }
}
