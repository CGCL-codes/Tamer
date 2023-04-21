package pl.alipiec.stockview.master;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

/**
 * Klasa notowania
 * 
 * @author Aleksandra Lipiec
 * 
 */
public class Stock {

    private List<String> indexes;

    private StockData data;

    public Stock(String stockCSV, String index) {
        data = new StockData(stockCSV);
        indexes = new ArrayList<String>();
        indexes.add(index);
    }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public List<String> getIndexes() {
        return indexes;
    }

    public void addIndex(String index) {
        indexes.add(index);
    }
}
