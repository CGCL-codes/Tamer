package de.uniwue.tm.textmarker.kernel.rule;

import java.util.List;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import de.uniwue.tm.textmarker.kernel.TextMarkerBlock;
import de.uniwue.tm.textmarker.kernel.TextMarkerStream;
import de.uniwue.tm.textmarker.kernel.expression.TextMarkerExpression;
import de.uniwue.tm.textmarker.kernel.type.TextMarkerBasic;

public interface TextMarkerMatcher {

    List<TextMarkerBasic> getMatchingBasics(TextMarkerStream stream, TextMarkerBlock parent);

    FSIterator<AnnotationFS> getMatchingBasics2(TextMarkerStream stream, TextMarkerBlock parent);

    boolean match(TextMarkerBasic currentBasic, TextMarkerStream stream, TextMarkerBlock parent);

    Type getType(TextMarkerBlock parent, TextMarkerStream stream);

    TextMarkerExpression getExpression();
}
