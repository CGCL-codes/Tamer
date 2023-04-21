package otservices.translator.strategy.impl;

import otservices.translator.mapperserverclient.ws.MapperClientWebServiceInterface;
import otservices.translator.strategy.TranslationStrategyInterface;

public class ZeroInputNAll implements TranslationStrategyInterface {

    /**
	 * 
	 */
    public String[] run(String concept, String[] translations) {
        String[] result = new String[1];
        if (translations == null) {
            result[0] = concept;
        } else if (translations.length > 0) {
            result = translations;
        }
        return result;
    }

    /**
	 * 
	 */
    public void setWebServices(String ontInterchangeName, Integer ontInterchangeVersion, String ontNativeName, Integer ontNativeVersion, MapperClientWebServiceInterface mapperClient) {
    }
}
