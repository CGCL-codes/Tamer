package com.idna.gav.rules.international.postgav.chain.manager;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import com.idna.gav.exceptions.ErrorCodes;
import com.idna.gav.exceptions.GavApplicationException;
import com.idna.gav.rules.international.postgav.chain.data.ProcessingData;

/**
 * A class to get the desired key using an xpath to a 
 * country code in the payload xml so it does
 * not need to be set in the {@link ProcessingData}.
 * 
 * @author gawain.hammond
 *
 */
public class CountryCodeProcessingManager extends AbstractProcessorManager {

    @Override
    public String getDataKey(ProcessingData data) {
        Document doc = (Document) data.getData();
        String countryCode = doc.valueOf("//xOasis/Search/@Country");
        if (StringUtils.isNotBlank(countryCode)) {
            return countryCode;
        } else {
            return "";
        }
    }
}
