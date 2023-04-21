package org.rubypeople.rdt.internal.debug.core.parsing;

import java.io.IOException;
import org.rubypeople.rdt.internal.debug.core.RdtDebugCorePlugin;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class XmlStreamReader {

    private AbstractReadStrategy readStrategy;

    public XmlStreamReader(XmlPullParser xpp) {
        this(new SingleReaderStrategy(xpp));
    }

    public XmlStreamReader(AbstractReadStrategy readStrategy) {
        this.readStrategy = readStrategy;
    }

    public void read() throws XmlPullParserException, IOException, XmlStreamReaderException {
        this.readStrategy.readElement(this);
    }

    protected abstract boolean processStartElement(XmlPullParser xpp) throws XmlStreamReaderException;

    protected boolean processEndElement(XmlPullParser xpp) {
        String name = xpp.getName();
        RdtDebugCorePlugin.debug("Reader " + this.getClass().getName() + " received End element: " + name);
        return true;
    }
}
