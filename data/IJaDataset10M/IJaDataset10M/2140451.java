package com.google.api.chart;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.google.api.chart.ChartMaker.*;

public class MyDataSeriesMaker implements DataSeriesMaker {

    public static void main(String[] args) {
        String url = simple(line()).data(new MyDataSeriesMaker(0), new MyDataSeriesMaker(1)).getURL();
        System.out.println("url = " + url);
    }

    private int i;

    public MyDataSeriesMaker(int i) {
        this.i = i;
    }

    @Override
    public BigDecimal[] getData() {
        List<BigDecimal> data = new ArrayList<BigDecimal>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int j = 0;
        while (true) {
            System.out.print("enter data element " + i + ":" + j + " (ENTER to end)> ");
            j++;
            try {
                String tmp = br.readLine();
                if (tmp == null || "".equals(tmp)) break;
                data.add(new BigDecimal(tmp));
            } catch (Exception e) {
                break;
            }
        }
        return data.toArray(new BigDecimal[data.size()]);
    }
}
