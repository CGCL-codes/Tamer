package com.francetelecom.rd.maps.semeuse.t31d2_slachecking.serializable_objects;

import java.io.Serializable;
import java.util.Date;

/**
 * ---------------------------------------------------------
 * 
 * @Software_Name : SLO Monitoring
 * @Version : 1.0.0
 * 
 * @Copyright � 2009 France Telecom
 * @License: This software is distributed under the GNU Lesser General Public
 *           License (Version 2.1) as published by the Free Software Foundation,
 *           the text of which is available at
 *           http://www.gnu.org/licenses/lgpl-2.1.html or see the "license.txt"
 *           file for more details.
 * 
 * @--------------------------------------------------------
 * 
 * @Created : 02/2009
 * @Author(s) : Antonin CHAZALET
 * @Contact: antonin.chazalet@gmail.com
 * 
 * @Description :
 * 
 * @--------------------------------------------------------
 */
@SuppressWarnings({ "serial" })
public class ProcessedReport_DTO implements Serializable {

    private String messageIdentifier;

    private long t2MinusT1;

    private long serviceLatencyT3MinusT2;

    private long t4MinusT3;

    private long esbLatencyT2MinusT1PlusT4MinusT3;

    private Date requestIn_dateInGMT;

    private String requestIn_consumerName;

    private String requestIn_serviceName;

    private String requestIn_operationName;

    private String requestIn_serviceProviderName;

    private String requestIn_endPoint;

    private long requestIn_contentLength;

    private boolean requestIn_isThisSemeuseRequestInAnException;

    private Object requestIn_exception;

    private Date requestOut_dateInGMT;

    private String requestOut_consumerName;

    private String requestOut_serviceName;

    private String requestOut_operationName;

    private String requestOut_serviceProviderName;

    private String requestOut_endPoint;

    private long requestOut_contentLength;

    private boolean requestOut_isThisSemeuseRequestOutAnException;

    private Object requestOut_exception;

    private Date responseIn_dateInGMT;

    private String responseIn_consumerName;

    private String responseIn_serviceName;

    private String responseIn_operationName;

    private String responseIn_serviceProviderName;

    private String responseIn_endPoint;

    private long responseIn_contentLength;

    private boolean responseIn_isThisSemeuseResponseInAnException;

    private Object responseIn_exception;

    private Date responseOut_dateInGMT;

    private String responseOut_consumerName;

    private String responseOut_serviceName;

    private String responseOut_operationName;

    private String responseOut_serviceProviderName;

    private String responseOut_endPoint;

    private long responseOut_contentLength;

    private boolean responseOut_isThisSemeuseResponseOutAnException;

    private Object responseOut_exception;

    ProcessedReport_DTO() {
    }

    public ProcessedReport_DTO(final String messageIdentifier_, final long t2MinusT1_, final long serviceLatencyT3MinusT2_, final long t4MinusT3_, final long esbLatencyT2MinusT1PlusT4MinusT3_, final Date requestIn_dateInGMT_, final String requestIn_consumerName_, final String requestIn_serviceName_, final String requestIn_operationName_, final String requestIn_serviceProviderName_, final String requestIn_endPoint_, final long requestIn_contentLength_, final boolean requestIn_isThisSemeuseRequestInAnException_, final Object requestIn_exception_, final Date requestOut_dateInGMT_, final String requestOut_consumerName_, final String requestOut_serviceName_, final String requestOut_operationName_, final String requestOut_serviceProviderName_, final String requestOut_endPoint_, final long requestOut_contentLength_, final boolean requestOut_isThisSemeuseRequestOutAnException_, final Object requestOut_exception_, final Date responseIn_dateInGMT_, final String responseIn_consumerName_, final String responseIn_serviceName_, final String responseIn_operationName_, final String responseIn_serviceProviderName_, final String responseIn_endPoint_, final long responseIn_contentLength_, final boolean responseIn_isThisSemeuseResponseInAnException_, final Object responseIn_exception_, final Date responseOut_dateInGMT_, final String responseOut_consumerName_, final String responseOut_serviceName_, final String responseOut_operationName_, final String responseOut_serviceProviderName_, final String responseOut_endPoint_, final long responseOut_contentLength_, final boolean responseOut_isThisSemeuseResponseOutAnException_, final Object responseOut_exception_) {
        this();
        setMessageIdentifier(messageIdentifier_);
        setT2MinusT1(t2MinusT1_);
        setServiceLatencyT3MinusT2(serviceLatencyT3MinusT2_);
        setT4MinusT3(t4MinusT3_);
        setEsbLatencyT2MinusT1PlusT4MinusT3(esbLatencyT2MinusT1PlusT4MinusT3_);
        setRequestIn_dateInGMT(requestIn_dateInGMT_);
        setRequestIn_consumerName(requestIn_consumerName_);
        setRequestIn_serviceName(requestIn_serviceName_);
        setRequestIn_operationName(requestIn_operationName_);
        setRequestIn_serviceProviderName(requestIn_serviceProviderName_);
        setRequestIn_endPoint(requestIn_endPoint_);
        setRequestIn_contentLength(requestIn_contentLength_);
        setRequestIn_isThisSemeuseRequestInAnException(requestIn_isThisSemeuseRequestInAnException_);
        setRequestIn_exception(requestIn_exception_);
        setRequestOut_dateInGMT(requestOut_dateInGMT_);
        setRequestOut_consumerName(requestOut_consumerName_);
        setRequestOut_serviceName(requestOut_serviceName_);
        setRequestOut_operationName(requestOut_operationName_);
        setRequestOut_serviceProviderName(requestOut_serviceProviderName_);
        setRequestOut_endPoint(requestOut_endPoint_);
        setRequestOut_contentLength(requestOut_contentLength_);
        setRequestOut_isThisSemeuseRequestOutAnException(requestOut_isThisSemeuseRequestOutAnException_);
        setRequestOut_exception(requestOut_exception_);
        setResponseIn_dateInGMT(responseIn_dateInGMT_);
        setResponseIn_consumerName(responseIn_consumerName_);
        setResponseIn_serviceName(responseIn_serviceName_);
        setResponseIn_operationName(responseIn_operationName_);
        setResponseIn_serviceProviderName(responseIn_serviceProviderName_);
        setResponseIn_endPoint(responseIn_endPoint_);
        setResponseIn_contentLength(responseIn_contentLength_);
        setResponseIn_isThisSemeuseResponseInAnException(responseIn_isThisSemeuseResponseInAnException_);
        setResponseIn_exception(responseIn_exception_);
        setResponseOut_dateInGMT(responseOut_dateInGMT_);
        setResponseOut_consumerName(responseOut_consumerName_);
        setResponseOut_serviceName(responseOut_serviceName_);
        setResponseOut_operationName(responseOut_operationName_);
        setResponseOut_serviceProviderName(responseOut_serviceProviderName_);
        setResponseOut_endPoint(responseOut_endPoint_);
        setResponseOut_contentLength(responseOut_contentLength_);
        setResponseOut_isThisSemeuseResponseOutAnException(responseOut_isThisSemeuseResponseOutAnException_);
        setResponseOut_exception(responseOut_exception_);
    }

    public String getMessageIdentifier() {
        return messageIdentifier;
    }

    public void setMessageIdentifier(final String messageIdentifier_) {
        this.messageIdentifier = messageIdentifier_;
    }

    public long getT2MinusT1() {
        return t2MinusT1;
    }

    public void setT2MinusT1(final long t2MinusT1_) {
        t2MinusT1 = t2MinusT1_;
    }

    public long getServiceLatencyT3MinusT2() {
        return serviceLatencyT3MinusT2;
    }

    public void setServiceLatencyT3MinusT2(final long serviceLatencyT3MinusT2_) {
        serviceLatencyT3MinusT2 = serviceLatencyT3MinusT2_;
    }

    public long getT4MinusT3() {
        return t4MinusT3;
    }

    public void setT4MinusT3(final long t4MinusT3_) {
        t4MinusT3 = t4MinusT3_;
    }

    public final long getEsbLatencyT2MinusT1PlusT4MinusT3() {
        return esbLatencyT2MinusT1PlusT4MinusT3;
    }

    public final void setEsbLatencyT2MinusT1PlusT4MinusT3(final long esbLatencyT2MinusT1PlusT4MinusT3_) {
        esbLatencyT2MinusT1PlusT4MinusT3 = esbLatencyT2MinusT1PlusT4MinusT3_;
    }

    public Date getRequestIn_dateInGMT() {
        return requestIn_dateInGMT;
    }

    public void setRequestIn_dateInGMT(Date requestIn_dateInGMT) {
        this.requestIn_dateInGMT = requestIn_dateInGMT;
    }

    public String getRequestIn_consumerName() {
        return requestIn_consumerName;
    }

    public void setRequestIn_consumerName(String requestIn_consumerName) {
        this.requestIn_consumerName = requestIn_consumerName;
    }

    public String getRequestIn_serviceName() {
        return requestIn_serviceName;
    }

    public void setRequestIn_serviceName(String requestIn_serviceName) {
        this.requestIn_serviceName = requestIn_serviceName;
    }

    public String getRequestIn_operationName() {
        return requestIn_operationName;
    }

    public void setRequestIn_operationName(String requestIn_operationName) {
        this.requestIn_operationName = requestIn_operationName;
    }

    public String getRequestIn_serviceProviderName() {
        return requestIn_serviceProviderName;
    }

    public void setRequestIn_serviceProviderName(String requestIn_serviceProviderName) {
        this.requestIn_serviceProviderName = requestIn_serviceProviderName;
    }

    public String getRequestIn_endPoint() {
        return requestIn_endPoint;
    }

    public void setRequestIn_endPoint(String requestIn_endPoint) {
        this.requestIn_endPoint = requestIn_endPoint;
    }

    public long getRequestIn_contentLength() {
        return requestIn_contentLength;
    }

    public void setRequestIn_contentLength(long requestIn_contentLength) {
        this.requestIn_contentLength = requestIn_contentLength;
    }

    public boolean getRequestIn_isThisSemeuseRequestInAnException() {
        return requestIn_isThisSemeuseRequestInAnException;
    }

    public void setRequestIn_isThisSemeuseRequestInAnException(boolean requestIn_isThisSemeuseRequestInAnException) {
        this.requestIn_isThisSemeuseRequestInAnException = requestIn_isThisSemeuseRequestInAnException;
    }

    public Object getRequestIn_exception() {
        return requestIn_exception;
    }

    public void setRequestIn_exception(Object requestIn_exception_) {
        this.requestIn_exception = requestIn_exception_;
    }

    public Date getRequestOut_timeStampExpressedInMilliSec() {
        return requestOut_dateInGMT;
    }

    public void setRequestOut_dateInGMT(Date requestOut_dateInGMT_) {
        this.requestOut_dateInGMT = requestOut_dateInGMT_;
    }

    public String getRequestOut_consumerName() {
        return requestOut_consumerName;
    }

    public void setRequestOut_consumerName(String requestOut_consumerName) {
        this.requestOut_consumerName = requestOut_consumerName;
    }

    public String getRequestOut_serviceName() {
        return requestOut_serviceName;
    }

    public void setRequestOut_serviceName(String requestOut_serviceName) {
        this.requestOut_serviceName = requestOut_serviceName;
    }

    public String getRequestOut_operationName() {
        return requestOut_operationName;
    }

    public void setRequestOut_operationName(String requestOut_operationName) {
        this.requestOut_operationName = requestOut_operationName;
    }

    public String getRequestOut_serviceProviderName() {
        return requestOut_serviceProviderName;
    }

    public void setRequestOut_serviceProviderName(String requestOut_serviceProviderName) {
        this.requestOut_serviceProviderName = requestOut_serviceProviderName;
    }

    public String getRequestOut_endPoint() {
        return requestOut_endPoint;
    }

    public void setRequestOut_endPoint(String requestOut_endPoint) {
        this.requestOut_endPoint = requestOut_endPoint;
    }

    public long getRequestOut_contentLength() {
        return requestOut_contentLength;
    }

    public void setRequestOut_contentLength(long requestOut_contentLength) {
        this.requestOut_contentLength = requestOut_contentLength;
    }

    public boolean getRequestOut_isThisSemeuseRequestOutAnException() {
        return requestOut_isThisSemeuseRequestOutAnException;
    }

    public void setRequestOut_isThisSemeuseRequestOutAnException(boolean requestOut_isThisSemeuseRequestOutAnException) {
        this.requestOut_isThisSemeuseRequestOutAnException = requestOut_isThisSemeuseRequestOutAnException;
    }

    public Object getRequestOut_exception() {
        return requestOut_exception;
    }

    public void setRequestOut_exception(Object requestOut_exception_) {
        this.requestOut_exception = requestOut_exception_;
    }

    public Date getResponseIn_dateInGMT() {
        return responseIn_dateInGMT;
    }

    public void setResponseIn_dateInGMT(Date responseIn_dateInGMT_) {
        this.responseIn_dateInGMT = responseIn_dateInGMT_;
    }

    public String getResponseIn_consumerName() {
        return responseIn_consumerName;
    }

    public void setResponseIn_consumerName(String responseIn_consumerName) {
        this.responseIn_consumerName = responseIn_consumerName;
    }

    public String getResponseIn_serviceName() {
        return responseIn_serviceName;
    }

    public void setResponseIn_serviceName(String responseIn_serviceName) {
        this.responseIn_serviceName = responseIn_serviceName;
    }

    public String getResponseIn_operationName() {
        return responseIn_operationName;
    }

    public void setResponseIn_operationName(String responseIn_operationName) {
        this.responseIn_operationName = responseIn_operationName;
    }

    public String getResponseIn_serviceProviderName() {
        return responseIn_serviceProviderName;
    }

    public void setResponseIn_serviceProviderName(String responseIn_serviceProviderName) {
        this.responseIn_serviceProviderName = responseIn_serviceProviderName;
    }

    public String getResponseIn_endPoint() {
        return responseIn_endPoint;
    }

    public void setResponseIn_endPoint(String responseIn_endPoint) {
        this.responseIn_endPoint = responseIn_endPoint;
    }

    public long getResponseIn_contentLength() {
        return responseIn_contentLength;
    }

    public void setResponseIn_contentLength(long responseIn_contentLength) {
        this.responseIn_contentLength = responseIn_contentLength;
    }

    public boolean getResponseIn_isThisSemeuseResponseInAnException() {
        return responseIn_isThisSemeuseResponseInAnException;
    }

    public void setResponseIn_isThisSemeuseResponseInAnException(boolean responseIn_isThisSemeuseResponseInAnException) {
        this.responseIn_isThisSemeuseResponseInAnException = responseIn_isThisSemeuseResponseInAnException;
    }

    public Object getResponseIn_exception() {
        return responseIn_exception;
    }

    public void setResponseIn_exception(Object responseIn_exception_) {
        this.responseIn_exception = responseIn_exception_;
    }

    public Date getResponseOut_dateInGMT() {
        return responseOut_dateInGMT;
    }

    public void setResponseOut_dateInGMT(Date responseOut_dateInGMT_) {
        this.responseOut_dateInGMT = responseOut_dateInGMT_;
    }

    public String getResponseOut_consumerName() {
        return responseOut_consumerName;
    }

    public void setResponseOut_consumerName(String responseOut_consumerName) {
        this.responseOut_consumerName = responseOut_consumerName;
    }

    public String getResponseOut_serviceName() {
        return responseOut_serviceName;
    }

    public void setResponseOut_serviceName(String responseOut_serviceName) {
        this.responseOut_serviceName = responseOut_serviceName;
    }

    public String getResponseOut_operationName() {
        return responseOut_operationName;
    }

    public void setResponseOut_operationName(String responseOut_operationName) {
        this.responseOut_operationName = responseOut_operationName;
    }

    public String getResponseOut_serviceProviderName() {
        return responseOut_serviceProviderName;
    }

    public void setResponseOut_serviceProviderName(String responseOut_serviceProviderName) {
        this.responseOut_serviceProviderName = responseOut_serviceProviderName;
    }

    public String getResponseOut_endPoint() {
        return responseOut_endPoint;
    }

    public void setResponseOut_endPoint(String responseOut_endPoint) {
        this.responseOut_endPoint = responseOut_endPoint;
    }

    public long getResponseOut_contentLength() {
        return responseOut_contentLength;
    }

    public void setResponseOut_contentLength(long responseOut_contentLength) {
        this.responseOut_contentLength = responseOut_contentLength;
    }

    public boolean getResponseOut_isThisSemeuseResponseOutAnException() {
        return responseOut_isThisSemeuseResponseOutAnException;
    }

    public void setResponseOut_isThisSemeuseResponseOutAnException(boolean responseOut_isThisSemeuseResponseOutAnException) {
        this.responseOut_isThisSemeuseResponseOutAnException = responseOut_isThisSemeuseResponseOutAnException;
    }

    public Object getResponseOut_exception() {
        return responseOut_exception;
    }

    public void setResponseOut_exception(Object responseOut_exception_) {
        this.responseOut_exception = responseOut_exception_;
    }

    @Override
    public String toString() {
        String result = this.getClass().getSimpleName() + ": [\n";
        result = result + "    messageIdentifier: " + getMessageIdentifier() + ",\n";
        result = result + "    t2MinusT1: " + getT2MinusT1() + ",\n";
        result = result + "    serviceLatencyT3MinusT2: " + getServiceLatencyT3MinusT2() + ",\n";
        result = result + "    t4MinusT3: " + getT4MinusT3() + ",\n";
        result = result + "    esbLatencyT2MinusT1PlusT4MinusT3: " + getEsbLatencyT2MinusT1PlusT4MinusT3() + ",\n";
        result = result + "]\n";
        return result;
    }
}
