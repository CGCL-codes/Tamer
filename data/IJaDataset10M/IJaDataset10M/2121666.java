package jexxe.mapper;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import jexxe.exception.JexxeException;
import org.apache.log4j.Logger;
import jexxe.schemas.xlstoxmlmappingschema.XlsToXmlMapping;

/**
 * Class representing xls to xml mapping rules.
 * These class sources from an xml mapping file and delivers "readable" methods to perform the mapping transformation.
 * Usually used by XlsToXmlTransformer. 
 */
public class XlsToXmlMapper {

    /**
	 * Logger
	 */
    static Logger mLog = Logger.getLogger(XlsToXmlMapper.class);

    /**
	 * Element representing the root of the java objects mapping 
	 * Initialized by the constructor.
	 */
    XlsToXmlMapping mMapping;

    /**
	 * Constructor
	 * @param pXlsToXmlMappingFile Xml mapping configuration file
	 * @throws IOException 
	 */
    public XlsToXmlMapper(File pXlsToXmlMappingFile) throws JexxeException {
        if (!pXlsToXmlMappingFile.exists()) {
            String _msg = "File not found : " + pXlsToXmlMappingFile.getPath();
            mLog.error(_msg);
            throw (new JexxeException(_msg));
        }
        try {
            JAXBContext _jc = JAXBContext.newInstance("jexxe.schemas.xlstoxmlmappingschema", getClass().getClassLoader());
            Unmarshaller _u = _jc.createUnmarshaller();
            mMapping = (XlsToXmlMapping) _u.unmarshal(pXlsToXmlMappingFile);
        } catch (JAXBException e) {
            mLog.error(e.getMessage());
            throw (new JexxeException(e.getMessage()));
        }
    }

    public XlsToXmlMapping getMMapping() {
        return mMapping;
    }

    public void setMMapping(XlsToXmlMapping mapping) {
        mMapping = mapping;
    }
}
