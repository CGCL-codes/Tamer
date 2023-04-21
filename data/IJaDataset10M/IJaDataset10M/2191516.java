package com.commonsware.android.EMusicDownloader;

import java.net.URL;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.util.Log;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class SearchListWindow extends ListActivity implements AdapterView.OnItemSelectedListener {

    public SearchListWindow thisActivity;

    private ImageView nextButton;

    private ImageView previousButton;

    private TextView txtinfo;

    public ListView list;

    private String urlAddress;

    private String urlAddress_orig;

    private String title;

    private String typeString;

    private String query;

    private String[] albums;

    private String[] artists;

    private String[] dates;

    private String[] aurls;

    private String[] images;

    private String[] albumIds;

    private String[] books;

    private String[] authors;

    private String[] tracks;

    private String[] listText;

    private HashMap<String, Bitmap> coverBitmapHash = new HashMap<String, Bitmap>();

    private int nResultsPerPage = 20;

    private int nResultsOnPage;

    private int nTotalResults;

    private int iListTypeFlag;

    private int iPageNumber = 1;

    private int iFirstResultOnPage = 1;

    private int statuscode = 200;

    private int iSelected = 0;

    private Spinner sortSpinner;

    private Boolean vLoaded = false;

    private Boolean vChart = false;

    LazyAdapter adapter;

    public static final int FAVORITE_ID = Menu.FIRST + 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.searchlist);
        Intent myIntent = getIntent();
        title = myIntent.getStringExtra("keytitle");
        typeString = myIntent.getStringExtra("keytype");
        query = myIntent.getStringExtra("keyquery");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String countryCode = prefs.getString("localelist", "US");
        urlAddress = myIntent.getStringExtra("keyurl") + "&country=" + countryCode;
        urlAddress_orig = urlAddress;
        if (typeString.contains("album")) {
            iListTypeFlag = 0;
        } else if (typeString.contains("book")) {
            iListTypeFlag = 1;
        } else if (typeString.contains("track")) {
            iListTypeFlag = 3;
        } else if (typeString.contains("label")) {
            iListTypeFlag = 4;
        } else {
            iListTypeFlag = 2;
        }
        nextButton = (ImageView) findViewById(R.id.nextbutton);
        previousButton = (ImageView) findViewById(R.id.previousbutton);
        sortSpinner = (Spinner) findViewById(R.id.sortspinner);
        sortSpinner.setOnItemSelectedListener(this);
        String[] spinnerText = { "Popular", "Recent" };
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerText);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(aa);
        thisActivity = this;
        nResultsPerPage = Integer.parseInt(prefs.getString("listsizelist", "20"));
        urlAddress = urlAddress + "&perPage=" + nResultsPerPage;
        iSelected = Integer.parseInt(prefs.getString("listsortlist", "0"));
        Log.d("EMD", "Selected sort " + iSelected);
        sortSpinner.setSelection(iSelected);
        if (urlAddress.contains("chart")) {
            vChart = true;
            if (iSelected == 0) {
                urlAddress = urlAddress_orig + "&primarySort=" + typeString + "DownloadsWeek";
            } else {
                urlAddress = urlAddress_orig + "&primarySort=daysNew";
            }
        }
        if (vChart) {
            sortSpinner.setVisibility(0);
        }
        Log.d("EMusic", "listsizelist" + nResultsPerPage);
        txtinfo = (TextView) findViewById(R.id.txt);
        getInfoFromXML();
    }

    private void getInfoFromXML() {
        final ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.loading), true, true);
        setProgressBarIndeterminateVisibility(true);
        Thread t3 = new Thread() {

            public void run() {
                waiting(200);
                txtinfo.post(new Runnable() {

                    public void run() {
                        txtinfo.setText(R.string.searching);
                    }
                });
                try {
                    if (iListTypeFlag < 2) {
                        URL url = new URL(urlAddress);
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();
                        XMLReader xr = sp.getXMLReader();
                        XMLHandlerAlbums myXMLHandler = new XMLHandlerAlbums(iListTypeFlag);
                        xr.setContentHandler(myXMLHandler);
                        xr.parse(new InputSource(url.openStream()));
                        nResultsOnPage = myXMLHandler.nItems;
                        statuscode = myXMLHandler.statuscode;
                        if (statuscode != 200 && statuscode != 206) {
                            throw new Exception();
                        }
                        if (iListTypeFlag == 0) {
                            albums = new String[nResultsOnPage];
                            albumIds = new String[nResultsOnPage];
                            artists = new String[nResultsOnPage];
                            dates = new String[nResultsOnPage];
                            aurls = new String[nResultsOnPage];
                            images = new String[nResultsOnPage];
                            listText = new String[nResultsOnPage];
                            for (int i = 0; i < nResultsOnPage; i++) {
                                albumIds[i] = myXMLHandler.albumIds[i];
                                albums[i] = myXMLHandler.albums[i];
                                dates[i] = myXMLHandler.dates[i];
                                artists[i] = myXMLHandler.artists[i];
                                images[i] = myXMLHandler.images[i];
                                aurls[i] = myXMLHandler.urls[i] + "?FREF=400062";
                                listText[i] = albums[i] + " - " + artists[i] + " - " + getString(R.string.added) + " " + dates[i];
                            }
                        } else if (iListTypeFlag == 1) {
                            books = new String[nResultsOnPage];
                            authors = new String[nResultsOnPage];
                            aurls = new String[nResultsOnPage];
                            images = new String[nResultsOnPage];
                            albumIds = new String[nResultsOnPage];
                            listText = new String[nResultsOnPage];
                            dates = new String[nResultsOnPage];
                            for (int i = 0; i < nResultsOnPage; i++) {
                                books[i] = myXMLHandler.albums[i];
                                albumIds[i] = myXMLHandler.albumIds[i];
                                images[i] = myXMLHandler.images[i];
                                dates[i] = myXMLHandler.dates[i];
                                authors[i] = myXMLHandler.artists[i];
                                aurls[i] = myXMLHandler.urls[i] + "?FREF=400062";
                                listText[i] = books[i] + " - " + authors[i] + " - " + getString(R.string.added) + " " + dates[i];
                            }
                        }
                        nTotalResults = myXMLHandler.nTotalItems;
                    } else if (iListTypeFlag == 2) {
                        URL url = new URL(urlAddress);
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();
                        XMLReader xr = sp.getXMLReader();
                        XMLHandlerArtists myXMLHandler = new XMLHandlerArtists();
                        xr.setContentHandler(myXMLHandler);
                        xr.parse(new InputSource(url.openStream()));
                        nResultsOnPage = myXMLHandler.nItems;
                        statuscode = myXMLHandler.statuscode;
                        if (statuscode != 200) {
                            throw new Exception();
                        }
                        artists = new String[nResultsOnPage];
                        aurls = new String[nResultsOnPage];
                        albums = new String[nResultsOnPage];
                        listText = new String[nResultsOnPage];
                        for (int i = 0; i < nResultsOnPage; i++) {
                            artists[i] = myXMLHandler.artists[i];
                            aurls[i] = myXMLHandler.urls[i];
                            albums[i] = myXMLHandler.artistsId[i];
                            listText[i] = artists[i];
                        }
                        nTotalResults = myXMLHandler.nTotalItems;
                    } else if (iListTypeFlag == 4) {
                        URL url = new URL(urlAddress);
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();
                        XMLReader xr = sp.getXMLReader();
                        XMLHandlerLabels myXMLHandler = new XMLHandlerLabels();
                        xr.setContentHandler(myXMLHandler);
                        xr.parse(new InputSource(url.openStream()));
                        nResultsOnPage = myXMLHandler.nItems;
                        statuscode = myXMLHandler.statuscode;
                        if (statuscode != 200) {
                            throw new Exception();
                        }
                        artists = new String[nResultsOnPage];
                        aurls = new String[nResultsOnPage];
                        albums = new String[nResultsOnPage];
                        listText = new String[nResultsOnPage];
                        for (int i = 0; i < nResultsOnPage; i++) {
                            artists[i] = myXMLHandler.labels[i];
                            aurls[i] = myXMLHandler.urls[i];
                            albums[i] = myXMLHandler.labelsId[i];
                            listText[i] = artists[i];
                        }
                        nTotalResults = myXMLHandler.nTotalItems;
                    } else {
                        URL url = new URL(urlAddress);
                        SAXParserFactory spf = SAXParserFactory.newInstance();
                        SAXParser sp = spf.newSAXParser();
                        XMLReader xr = sp.getXMLReader();
                        XMLHandlerTracks myXMLHandler = new XMLHandlerTracks();
                        xr.setContentHandler(myXMLHandler);
                        xr.parse(new InputSource(url.openStream()));
                        nResultsOnPage = myXMLHandler.nItems;
                        statuscode = myXMLHandler.statuscode;
                        if (statuscode != 200) {
                            throw new Exception();
                        }
                        tracks = new String[nResultsOnPage];
                        albums = new String[nResultsOnPage];
                        albumIds = new String[nResultsOnPage];
                        artists = new String[nResultsOnPage];
                        images = new String[nResultsOnPage];
                        aurls = new String[nResultsOnPage];
                        listText = new String[nResultsOnPage];
                        for (int i = 0; i < nResultsOnPage; i++) {
                            albumIds[i] = myXMLHandler.albumIds[i];
                            albums[i] = myXMLHandler.albums[i];
                            images[i] = myXMLHandler.images[i];
                            tracks[i] = myXMLHandler.tracks[i];
                            artists[i] = myXMLHandler.artists[i];
                            aurls[i] = myXMLHandler.urls[i] + "?FREF=400062";
                            listText[i] = tracks[i] + " - " + artists[i] + " (Album: " + albums[i] + ")";
                        }
                        nTotalResults = myXMLHandler.nTotalItems;
                    }
                    final int fnmin = iFirstResultOnPage;
                    final int fnmax = iFirstResultOnPage + nResultsOnPage - 1;
                    final int fntotalitems = nTotalResults;
                    if (nTotalResults > fnmax) {
                        nextButton.post(new Runnable() {

                            public void run() {
                                nextButton.setVisibility(0);
                            }
                        });
                    } else {
                        nextButton.post(new Runnable() {

                            public void run() {
                                nextButton.setVisibility(8);
                            }
                        });
                    }
                    if (iFirstResultOnPage > 1) {
                        previousButton.post(new Runnable() {

                            public void run() {
                                previousButton.setVisibility(0);
                            }
                        });
                    } else if (nTotalResults > fnmax) {
                        previousButton.post(new Runnable() {

                            public void run() {
                                previousButton.setVisibility(8);
                            }
                        });
                    } else {
                        previousButton.post(new Runnable() {

                            public void run() {
                                previousButton.setVisibility(4);
                            }
                        });
                    }
                    txtinfo.post(new Runnable() {

                        public void run() {
                            if (title != null && title != "") {
                                txtinfo.setText(title + "\n" + getString(R.string.showing) + " " + fnmin + " " + getString(R.string.through) + " " + fnmax + " " + getString(R.string.of) + " " + fntotalitems);
                            } else {
                                txtinfo.setText(getString(R.string.showing) + " " + fnmin + " " + getString(R.string.through) + " " + fnmax + " " + getString(R.string.of) + " " + fntotalitems);
                            }
                        }
                    });
                    handlerSetList.sendEmptyMessage(0);
                } catch (Exception e) {
                    final Exception ef = e;
                    txtinfo.post(new Runnable() {

                        public void run() {
                            txtinfo.setText(R.string.search_failed);
                        }
                    });
                }
                dialog.dismiss();
                handlerDoneLoading.sendEmptyMessage(0);
            }
        };
        t3.start();
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (iSelected != position) {
            iSelected = position;
            if (iSelected == 0) {
                urlAddress = urlAddress_orig + "&primarySort=" + typeString + "DownloadsWeek";
            } else {
                urlAddress = urlAddress_orig + "&primarySort=daysNew";
            }
            getInfoFromXML();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        if (iListTypeFlag == 0 || iListTypeFlag == 3) {
            Intent myIntent = new Intent(this, SingleAlbum.class);
            myIntent.putExtra("keyalbumid", albumIds[position]);
            myIntent.putExtra("keyalbum", albums[position]);
            myIntent.putExtra("keyartist", artists[position]);
            myIntent.putExtra("keyexturl", aurls[position]);
            startActivity(myIntent);
        } else if (iListTypeFlag == 1) {
            Intent myIntent = new Intent(this, SingleBook.class);
            myIntent.putExtra("keyalbumid", albumIds[position]);
            myIntent.putExtra("keyalbum", books[position]);
            myIntent.putExtra("keyartist", authors[position]);
            myIntent.putExtra("keyexturl", aurls[position]);
            startActivity(myIntent);
        } else if (iListTypeFlag == 4) {
            Intent myIntent = new Intent(this, SearchListWindow.class);
            String stringtype = "album";
            myIntent.putExtra("keytype", stringtype);
            String stringtitle = "Label: " + artists[position];
            myIntent.putExtra("keytitle", stringtitle);
            String urlad = "http://api.emusic.com/album/charts?" + Secrets.apikey + "&labelId=" + albums[position];
            myIntent.putExtra("keyurl", urlad);
            String totalsearch = "labelId=" + albums[position];
            myIntent.putExtra("keyquery", totalsearch);
            startActivity(myIntent);
        } else {
            Intent myIntent = new Intent(this, SingleArtist.class);
            myIntent.putExtra("keyartistid", albums[position]);
            startActivity(myIntent);
        }
    }

    private Handler handlerSetList = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            vLoaded = true;
            if (iListTypeFlag != 2 && iListTypeFlag != 4) {
                try {
                    adapter.imageLoader.stopThread();
                    adapter = null;
                } catch (Exception ef) {
                }
                adapter = new LazyAdapter(thisActivity, images, listText, coverBitmapHash);
                setListAdapter(adapter);
            } else {
                setListAdapter(new ArrayAdapter<String>(thisActivity, R.layout.artistlist_item, R.id.text, listText));
            }
        }
    };

    private static void waiting(int n) {
        long t0, t1;
        t0 = System.currentTimeMillis();
        do {
            t1 = System.currentTimeMillis();
        } while (t1 - t0 < n);
    }

    public void nextPressed(View button) {
        iFirstResultOnPage = iFirstResultOnPage + nResultsPerPage;
        iPageNumber = iPageNumber + 1;
        if (vChart) {
            if (iSelected == 0) {
                urlAddress = urlAddress_orig + "&primarySort=" + typeString + "DownloadsWeek&page=" + iPageNumber + "&perPage=" + nResultsPerPage;
            } else {
                urlAddress = urlAddress_orig + "&primarySort=daysNew&page=" + iPageNumber + "&perPage=" + nResultsPerPage;
            }
        } else {
            urlAddress = urlAddress_orig + "&page=" + iPageNumber + "&perPage=" + nResultsPerPage;
        }
        getInfoFromXML();
    }

    public void previousPressed(View button) {
        iFirstResultOnPage = iFirstResultOnPage - nResultsPerPage;
        iPageNumber = iPageNumber - 1;
        if (vChart) {
            if (iSelected == 0) {
                urlAddress = urlAddress_orig + "&primarySort=" + typeString + "DownloadsWeek&page=" + iPageNumber + "&perPage=" + nResultsPerPage;
            } else {
                urlAddress = urlAddress_orig + "&primarySort=daysNew&page=" + iPageNumber + "&perPage=" + nResultsPerPage;
            }
        } else {
            urlAddress = urlAddress_orig + "&page=" + iPageNumber + "&perPage=" + nResultsPerPage;
        }
        getInfoFromXML();
    }

    private Handler handlerDoneLoading = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            setProgressBarIndeterminateVisibility(false);
        }
    };

    public void logoPressed(View buttoncover) {
        String browseurl = "http://www.emusic.com?fref=400062";
        Intent browseIntent = new Intent(this, WebWindowBrowse.class);
        browseIntent.putExtra("keyurl", browseurl);
        startActivity(browseIntent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.searchlist);
        nextButton = (ImageView) findViewById(R.id.nextbutton);
        previousButton = (ImageView) findViewById(R.id.previousbutton);
        txtinfo = (TextView) findViewById(R.id.txt);
        sortSpinner = (Spinner) findViewById(R.id.sortspinner);
        sortSpinner.setOnItemSelectedListener(this);
        String[] spinnerText = { "Popular", "Recent" };
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerText);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(aa);
        if (vChart) {
            sortSpinner.setVisibility(0);
            sortSpinner.setSelection(iSelected);
        }
        if (vLoaded) {
            final int fnmin = iFirstResultOnPage;
            final int fnmax = iFirstResultOnPage + nResultsOnPage - 1;
            final int fntotalitems = nTotalResults;
            if (nTotalResults > fnmax) {
                nextButton.setVisibility(0);
            } else {
                nextButton.setVisibility(8);
            }
            if (iFirstResultOnPage > 1) {
                previousButton.setVisibility(0);
            } else if (nTotalResults > fnmax) {
                previousButton.setVisibility(8);
            } else {
                previousButton.setVisibility(4);
            }
            if (title != null && title != "") {
                txtinfo.setText(title + "\n" + getString(R.string.showing) + " " + fnmin + " " + getString(R.string.through) + " " + fnmax + " " + getString(R.string.of) + " " + fntotalitems);
            } else {
                txtinfo.setText(getString(R.string.showing) + " " + fnmin + " " + getString(R.string.through) + " " + fnmax + " " + getString(R.string.of) + " " + fntotalitems);
            }
            handlerSetList.sendEmptyMessage(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            adapter.imageLoader.stopThread();
            adapter = null;
        } catch (Exception ef) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        populateMenu(menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (applyMenuChoice(item) || super.onOptionsItemSelected(item));
    }

    private void populateMenu(Menu menu) {
        menu.add(Menu.NONE, FAVORITE_ID, Menu.NONE, "Add to Favorites");
    }

    private boolean applyMenuChoice(MenuItem item) {
        Log.d("EMD - ", "Attempting favorite " + title);
        switch(item.getItemId()) {
            case FAVORITE_ID:
                Log.d("EMD - ", "Inserting favorite " + title);
                favoriteDB droidDB = new favoriteDB(this);
                Boolean vStatus = droidDB.insertFavorite(title, typeString, query);
                droidDB.close();
                if (vStatus) {
                    Log.d("EMD - ", "Inserted favorite " + title + " " + typeString + " " + query);
                } else {
                    Log.d("EMD - ", "Failed favorite " + title + " " + typeString + " " + query);
                }
                return (true);
        }
        return (false);
    }
}
