package android.tickethunter.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.tickethunter.entities.Seat;
import android.tickethunter.entities.SelectedSeats;
import android.tickethunter.soap.SeatParser;
import android.tickethunter.soap.SoapRequester;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class SeatsActivity extends ListActivity {

    private List<Seat> seatList = new ArrayList<Seat>();

    private ProgressDialog dialog;

    private String selectedEventID;

    private String selectedDiscountID;

    private double selectedDiscountPercent;

    private String selectedEventName;

    private String selectedEventDate;

    private String selectedEventPlace;

    private SoapRequester sRequester;

    private String errorMessage = "An error occured during the webservice call. See error message below\n";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seatlist);
        Bundle extras = getIntent().getExtras();
        selectedEventID = extras.getString("selectedEventID");
        selectedDiscountID = extras.getString("selectedDiscountID");
        selectedDiscountPercent = extras.getDouble("selectedDiscountPercent");
        selectedEventName = extras.getString("selectedEventName");
        selectedEventDate = extras.getString("selectedEventDate");
        selectedEventPlace = extras.getString("selectedEventPlace");
        setListAdapter(new SeatListItemAdapter(this, seatList));
        sRequester = new SoapRequester();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.seatsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.SeatsMenuReserveSeats:
                SelectedSeats seats = SelectedSeats.getInstance();
                seats.setTotalPrice(0);
                seats.getSelectedSeats().clear();
                for (int i = 0; i < seatList.size(); i++) {
                    if (seatList.get(i).isSelected()) {
                        seats.addSeat(seatList.get(i));
                        seats.setCurrency(seatList.get(i).getCurrency());
                    }
                }
                Intent myIntent = new Intent();
                myIntent.setClassName("android.tickethunter.client", "android.tickethunter.client.ReservationActivity");
                myIntent.putExtra("selectedEventID", selectedEventID);
                myIntent.putExtra("selectedDiscountID", selectedDiscountID);
                myIntent.putExtra("selectedDiscountPercent", selectedDiscountPercent);
                myIntent.putExtra("selectedEventName", selectedEventName);
                myIntent.putExtra("selectedEventDate", selectedEventDate);
                myIntent.putExtra("selectedEventPlace", selectedEventPlace);
                startActivity(myIntent);
                break;
        }
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case 0:
                {
                    dialog = new ProgressDialog(this);
                    dialog.setMessage("Getting data from server...");
                    dialog.setIndeterminate(true);
                    dialog.setCancelable(true);
                    return dialog;
                }
        }
        return null;
    }

    private void loadData() {
        final Handler uiThreadCallback = new Handler();
        final Runnable runInUIThread = new Runnable() {

            public void run() {
                showResultsInUI();
            }
        };
        final Runnable errorhandlingInUIThread = new Runnable() {

            public void run() {
                showErrorInUI();
            }
        };
        new Thread() {

            @Override
            public void run() {
                try {
                    SoapObject s = sRequester.soapGetSeatList(selectedEventID);
                    if (s.getPropertyCount() == 0) {
                        errorMessage += "No data returned by the server";
                        uiThreadCallback.post(errorhandlingInUIThread);
                    } else {
                        seatList.clear();
                        seatList = SeatParser.RetrieveFromSoap(s);
                        uiThreadCallback.post(runInUIThread);
                    }
                } catch (IOException e) {
                    errorMessage += e.getMessage();
                    uiThreadCallback.post(errorhandlingInUIThread);
                } catch (XmlPullParserException e) {
                    errorMessage += e.getMessage();
                    uiThreadCallback.post(errorhandlingInUIThread);
                }
            }
        }.start();
        showDialog(0);
    }

    protected void showResultsInUI() {
        removeDialog(0);
        setListAdapter(new SeatListItemAdapter(this, seatList));
    }

    protected void showErrorInUI() {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG * 20).show();
    }
}
