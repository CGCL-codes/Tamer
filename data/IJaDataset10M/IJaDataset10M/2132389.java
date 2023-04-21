package org.liris.schemerger.core.persistence;

import org.liris.schemerger.chronicle.IChrMinerRequest;
import org.liris.schemerger.core.event.ISimEvent;
import org.liris.schemerger.core.pattern.ITypeDec;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Damien Cram
 * 
 */
public class RequestWriter<E extends ISimEvent, T extends ITypeDec> extends AbstractWriter<E, T, IChrMinerRequest<E, T>> {

    @Override
    protected Element createRootElement(IChrMinerRequest<E, T> request, Document document, TypeAdapter adapter) {
        Element requestEl = WriterFactory.getInstance().createChrRequest(request, document, adapter);
        return requestEl;
    }
}
