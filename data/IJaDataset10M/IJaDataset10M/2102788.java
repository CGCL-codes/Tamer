package com.manning.aip.dealdroid.export;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import android.content.Context;
import com.manning.aip.dealdroid.model.Item;

public class DealExporter {

    private Context context;

    private List<Item> deals;

    public DealExporter(Context context, List<Item> deals) {
        this.context = context;
        this.deals = deals;
    }

    public void export() throws IOException {
        FileOutputStream fos = context.openFileOutput("deals.txt", Context.MODE_PRIVATE);
        for (Item item : deals) {
            fos.write(item.toString().getBytes());
        }
        fos.close();
    }
}
