package hu.genius.view.old;

import hu.genius.CoreApplication;
import hu.genius.R;
import hu.genius.model.BasketManager;
import hu.genius.model.DBUtil;
import hu.genius.model.entity.Product;
import hu.genius.view.utils.ViewUtils;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Soups extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("zs", "create " + this.getClass().getName() + " product list.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        CoreApplication application = (CoreApplication) this.getApplication();
        BasketManager basketManager = application.getBasketManager();
        Log.d("zs", this.getClass().getName() + ": " + basketManager.toString());
        List<Product> products = DBUtil.listProductsByCategory(this, 4);
        TextView productsTextView = (TextView) findViewById(R.id.productsLabelTextView);
        productsTextView.setText(getText(R.string.products_label) + " - " + getString(R.string.soups_text));
        ScrollView actionView = (ScrollView) findViewById(R.id.productsScrollView);
        actionView.addView(ViewUtils.buildProductsView(this, products));
    }
}
