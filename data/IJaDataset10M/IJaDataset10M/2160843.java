package uk.co.koeth.brotzeit.android;

import java.util.List;
import uk.co.koeth.brotzeit.android.model.FoodRecordProxy;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FoodRecordsCellRendererView extends RelativeLayout {

    List<FoodRecordProxy> lFoodRecords = null;

    public FoodRecordsCellRendererView(Context context, ViewGroup parent, List<FoodRecordProxy> lFoodRecords) {
        super(context);
        inflate(context, R.layout.food_records_cell_layout, this);
        this.lFoodRecords = lFoodRecords;
    }

    public void display(int i) {
        TextView tvFirst = (TextView) findViewById(R.id.FoodRecordsCellFirstText);
        TextView tvSecond = (TextView) findViewById(R.id.FoodRecordsCellSecondText);
        if (tvFirst != null && tvSecond != null) {
            tvFirst.setText(lFoodRecords.get(i).getName());
            tvSecond.setText(lFoodRecords.get(i).getServing() + ", " + lFoodRecords.get(i).getCalories() + " kcal.");
        }
    }
}
