package org.liris.schemerger.ui;

import org.liris.schemerger.chronicle.IChrMinerRequest;
import org.liris.schemerger.core.event.ISimEvent;
import org.liris.schemerger.core.pattern.ITypeDec;
import org.liris.schemerger.core.persistence.ChrRequestReader;
import org.liris.schemerger.core.persistence.TypeAdapter;
import org.liris.schemerger.ui.model.ObservableChrMinerRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ObservableRequestReader extends ChrRequestReader<ISimEvent, ITypeDec> {

    public ObservableRequestReader() {
        super(ISimEvent.class, ITypeDec.class);
    }

    @Override
    protected IChrMinerRequest<ISimEvent, ITypeDec> extractObject(Document document, TypeAdapter adapter) throws Exception {
        ObservableChrMinerRequest request = new ObservableChrMinerRequest(super.extractObject(document, adapter));
        Element sequencePathEl = (Element) document.getElementsByTagName("sequence-path").item(0);
        String sequencePath = sequencePathEl.getAttribute("value");
        Element cstdbPathEl = (Element) document.getElementsByTagName("cstdb-path").item(0);
        String cstdbPath = cstdbPathEl.getAttribute("value");
        request.setSequencePath(sequencePath);
        request.setCstdbPath(cstdbPath);
        return request;
    }
}
