package com.manning.aip.portfolio;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.manning.aip.portfolio.service.IStockService;

/**
 * The main <code>Activity</code> of the application. This presents a simple
 * form for entering in stocks that the user wants to monitor, along with a 
 * list of the stocks that the user is monitoring. This uses the
 * {@link com.manning.aip.portfolio.PortfolioManagerService PortfolioManagerService}
 * retrieve the portfolio and add stocks to it.
 * 
 * @author Michael Galpin
 *
 */
public class ViewStocks extends ListActivity {

    private static final String LOGGING_TAG = "ViewStocks";

    private ArrayList<Stock> stocks;

    private IStockService stockService;

    private boolean bound = false;

    private ServiceConnection connection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            stockService = IStockService.Stub.asInterface(service);
            Log.d(LOGGING_TAG, "Connected to service");
            try {
                stocks = (ArrayList<Stock>) stockService.getPortfolio();
                if (stocks == null) {
                    stocks = new ArrayList<Stock>(0);
                    Log.d(LOGGING_TAG, "No stocks returned from service");
                } else {
                    Log.d(LOGGING_TAG, "Got " + stocks.size() + " stocks from service");
                }
                refresh();
            } catch (RemoteException e) {
                Log.e(LOGGING_TAG, "Exception retrieving portfolio from service", e);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            stockService = null;
            Log.d(LOGGING_TAG, "Disconnected from service");
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (!bound) {
            bound = bindService(new Intent(ViewStocks.this, PortfolioManagerService.class), connection, Context.BIND_AUTO_CREATE);
            Log.d(LOGGING_TAG, "Bound to service: " + bound);
        }
        if (!bound) {
            Log.e(LOGGING_TAG, "Failed to bind to service");
            throw new RuntimeException("Failed to find to service");
        }
        setListAdapter(new BaseAdapter() {

            public int getCount() {
                if (stocks == null) {
                    return 0;
                }
                return stocks.size();
            }

            public Object getItem(int position) {
                if (stocks == null) {
                    return null;
                }
                return stocks.get(position);
            }

            public long getItemId(int position) {
                if (stocks == null) {
                    return 0L;
                }
                return stocks.get(position).getId();
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = inflater.inflate(R.layout.stock, parent, false);
                }
                TextView rowTxt = (TextView) convertView.findViewById(R.id.rowTxt);
                rowTxt.setText(stocks.get(position).toString());
                return convertView;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bound) {
            bound = false;
            unbindService(connection);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final EditText symbolIn = (EditText) findViewById(R.id.inputSymbol);
        final EditText maxIn = (EditText) findViewById(R.id.inputMax);
        final EditText minIn = (EditText) findViewById(R.id.inputMin);
        final EditText priceIn = (EditText) findViewById(R.id.inputPrice);
        final EditText quantIn = (EditText) findViewById(R.id.inputQuant);
        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String symbol = symbolIn.getText().toString();
                symbolIn.setText("");
                double max = Double.parseDouble(maxIn.getText().toString());
                maxIn.setText("");
                double min = Double.parseDouble(minIn.getText().toString());
                minIn.setText("");
                double pricePaid = Double.parseDouble(priceIn.getText().toString());
                priceIn.setText("");
                int quantity = Integer.parseInt(quantIn.getText().toString());
                quantIn.setText("");
                Stock stock = new Stock(symbol, pricePaid, quantity);
                stock.setMaxPrice(max);
                stock.setMinPrice(min);
                new AsyncTask<Stock, Void, Stock>() {

                    @Override
                    protected Stock doInBackground(Stock... newStocks) {
                        try {
                            return stockService.addToPortfolio(newStocks[0]);
                        } catch (RemoteException e) {
                            Log.e(LOGGING_TAG, "Exception adding stock " + "to portfolio", e);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Stock s) {
                        Log.d(LOGGING_TAG, "Stock returned from service: " + s);
                        if (s == null) {
                            Log.w(LOGGING_TAG, "Stock returned from Service " + "was null or invalid");
                            Toast.makeText(ViewStocks.this, "Stock not found", Toast.LENGTH_SHORT);
                        } else {
                            refreshStockData();
                        }
                    }
                }.execute(stock);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private void refreshStockData() {
        if (stocks != null && stocks.size() > 0) {
            new AsyncTask<Void, Void, ArrayList<Stock>>() {

                @Override
                protected void onPostExecute(ArrayList<Stock> result) {
                    Log.d(LOGGING_TAG, "Got new stock data from service");
                    if (result != null) {
                        stocks = result;
                        refresh();
                    } else {
                        Toast.makeText(ViewStocks.this, "Exception getting " + "latest stock data", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                protected ArrayList<Stock> doInBackground(Void... nada) {
                    try {
                        return (ArrayList<Stock>) stockService.getPortfolio();
                    } catch (Exception e) {
                        Log.e(LOGGING_TAG, "Exception getting stock data", e);
                    }
                    return null;
                }
            }.execute();
        }
    }

    private void refresh() {
        Log.d(LOGGING_TAG, "Refreshing UI with new data");
        for (Stock s : stocks) {
            Log.d(LOGGING_TAG, "Got stock: " + s.toString());
        }
        BaseAdapter adapter = (BaseAdapter) this.getListAdapter();
        adapter.notifyDataSetChanged();
    }
}
