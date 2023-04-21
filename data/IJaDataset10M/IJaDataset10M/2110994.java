package com.esri.gpt.server.csw.client;

/**
 * CswSearchResponse class.
 * 
 * CswSearchResponse class is used to store the response for CSW search
 * request.
 */
public class CswSearchResponse {

    private CswRecords _records = new CswRecords();

    private String _responseXML = "";

    private String _requestStr = "";

    /**
	 * Constructor
	 */
    public CswSearchResponse() {
    }

    /**
	 * set CSW Records
	 */
    public void setRecords(CswRecords records) {
        _records = records;
    }

    /**
	 * CSW Records returned
	 */
    public CswRecords getRecords() {
        return _records;
    }

    /**
	 * set responseXML string. reponseXML string is raw response from a service
	 */
    public void setResponseXML(String responseXML) {
        _responseXML = responseXML;
    }

    /**
	 * get responseXML string.
	 */
    public String getResponseXML() {
        return _responseXML;
    }

    public String get_requestStr() {
        return _requestStr;
    }

    public void set_requestStr(String str) {
        _requestStr = str;
    }
}
