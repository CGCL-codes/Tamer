package net.sf.yahoocsv.download;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import net.sf.yahoocsv.common.spring.BeanAttributeRequiredException;
import net.sf.yahoocsv.domain.Stock;
import net.sf.yahoocsv.logic.StockLogic;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Sample implementation of the library to download stocks 
 */
public class StockDownloader implements InitializingBean {

    private StockLogic stockLogic;

    private Long waitTime;

    private String url = "http://ichart.finance.yahoo.com/table.csv?s=";

    private String downloadDirectory;

    private Log LOG = LogFactory.getLog(StockDownloader.class.getName());

    /**
	 * Download all available stocks and store to disk
	 */
    public void downloadAllStocks() {
        List<Stock> stocks = stockLogic.getStocks();
        for (Stock stock : stocks) {
            LOG.info("Downloading Stock: " + stock);
            downloadStock(stock);
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Download a single stock and store it to disk
	 * @param stock
	 */
    public void downloadStock(Stock stock) {
        String responseBody = null;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url + stock.getTicker());
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }
            responseBody = method.getResponseBodyAsString();
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        try {
            FileWriter fw = new FileWriter(downloadDirectory + stock.getTicker() + ".csv");
            PrintWriter pw = new PrintWriter(fw);
            pw.print(responseBody);
            pw.flush();
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void afterPropertiesSet() throws Exception {
        if (stockLogic == null) {
            throw new BeanAttributeRequiredException(this, "stockLogic");
        }
        if (waitTime == null) {
            throw new BeanAttributeRequiredException(this, "waitTime");
        }
        if (downloadDirectory == null) {
            throw new BeanAttributeRequiredException(this, "downloadDirectory");
        }
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    public String getDownloadDirectory() {
        return downloadDirectory;
    }

    public void setDownloadDirectory(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    public void setStockLogic(StockLogic stockLogic) {
        this.stockLogic = stockLogic;
    }
}
