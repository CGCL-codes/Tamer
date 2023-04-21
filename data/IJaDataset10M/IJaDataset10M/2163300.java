package org.spantus.exp.segment.services.impl;

import java.util.List;
import org.spantus.core.extractor.IExtractorInputReader;
import org.spantus.core.marker.MarkerSetHolder;
import org.spantus.extractor.impl.ExtractorEnum;
import org.spantus.segment.online.OnlineDecisionSegmentatorParam;

public interface ComarisionFacade {

    /**
	 * 
	 * @param wavName
	 * @param extractors
	 * @return
	 */
    public abstract IExtractorInputReader readSignal(List<String> wavName, ExtractorEnum[] extractors);

    /**
	 * 
	 */
    public abstract MarkerSetHolder calculateMarkers(List<String> wavName, ExtractorEnum[] extractors, OnlineDecisionSegmentatorParam param);
}
