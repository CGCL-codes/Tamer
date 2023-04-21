package com.google.code.sagetvaddons.sre.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.google.code.gwtsrwc.client.ColumnDescriptor;
import com.google.code.gwtsrwc.client.TableModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

final class SRECommandEngine {

    static final String BASE_URL = initBaseURL();

    static final String GET_URL = BASE_URL + "?sub=";

    static final String POST_URL = BASE_URL;

    private static SRECommandEngine instance = null;

    static SRECommandEngine getInstance() {
        if (instance == null) instance = new SRECommandEngine();
        return instance;
    }

    private static final String initBaseURL() {
        return "/sre/SRE";
    }

    private SRECommandEngine() {
    }

    void loadLogs() {
        String url = GET_URL + "log";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            Request r = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    SREClient.SELF.statusError(exception.getLocalizedMessage());
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) SREClient.SELF.getLogLabel().setText(response.getText()); else SREClient.SELF.statusError(SREClient.messages.jsonErrWithCode(response.getStatusText(), Integer.toString(response.getStatusCode())));
                }
            });
        } catch (RequestException e) {
            SREClient.SELF.statusError(SREClient.messages.jsonError(e.getLocalizedMessage()));
        }
    }

    void getLastRun(final boolean suppressErrorMessages) {
        String url = GET_URL + "lastRun";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            Request r = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    if (!suppressErrorMessages) SREClient.SELF.statusError(exception.getLocalizedMessage());
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) SREClient.SELF.getAboutGrid().setLastRun(response.getText()); else if (!suppressErrorMessages) SREClient.SELF.statusError(SREClient.messages.jsonErrWithCode(response.getStatusText(), Integer.toString(response.getStatusCode())));
                }
            });
        } catch (RequestException e) {
            if (!suppressErrorMessages) SREClient.SELF.statusError(SREClient.messages.jsonError(e.getLocalizedMessage()));
        }
    }

    void loadProgramDetails(final String id, final int left, final int top) {
        String url = GET_URL + "echoProg&id=" + URL.encodeComponent(id);
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            Request r = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    SREClient.SELF.statusError(exception.getLocalizedMessage());
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) {
                        JSONObject jobj = JSONParser.parse(response.getText()).isObject();
                        PopupPanel p = new PopupPanel(true);
                        VerticalPanel v = new VerticalPanel();
                        v.add(new HTML("<b>" + SREClient.constants.title() + ":</b> " + jobj.get("title").isString().stringValue()));
                        v.add(new HTML("<b>" + SREClient.constants.subtitle() + ":</b> " + jobj.get("subtitle").isString().stringValue()));
                        p.setWidget(v);
                        p.setPopupPosition(left, top);
                        p.show();
                    } else SREClient.SELF.statusError(SREClient.messages.jsonErrWithCode(response.getStatusText(), Integer.toString(response.getStatusCode())));
                }
            });
        } catch (RequestException e) {
            SREClient.SELF.statusError(SREClient.messages.jsonError(e.getLocalizedMessage()));
        }
    }

    void loadOverrideTable() {
        String url = GET_URL + "echoOverride";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            Request r = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    SREClient.SELF.statusError(exception.getLocalizedMessage());
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) {
                        JSONArray jarr = JSONParser.parse(response.getText()).isObject().get("overrideTbl").isArray();
                        List<ColumnDescriptor> cols = new ArrayList<ColumnDescriptor>();
                        cols.add(new ColumnDescriptor("id", "Airing ID", false));
                        cols.add(new ColumnDescriptor("title", "Title", true));
                        cols.add(new ColumnDescriptor("subtitle", "Subtitle", false));
                        cols.add(new ColumnDescriptor("recording", "Recording", true));
                        TableModel<TvAiring> model = new TableModel<TvAiring>(cols.toArray(new ColumnDescriptor[4]));
                        for (int i = 0; i < jarr.size(); ++i) model.addRow(new TvAiring(jarr.get(i).isObject()));
                        SREClient.SELF.setOverrideTable(model);
                    } else SREClient.SELF.statusError(SREClient.messages.jsonErrWithCode(response.getStatusText(), Integer.toString(response.getStatusCode())));
                }
            });
        } catch (RequestException e) {
            SREClient.SELF.statusError(SREClient.messages.jsonError(e.getLocalizedMessage()));
        }
    }

    void loadConfigForm() {
        String url = GET_URL + "echoFrm";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            Request r = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    SREClient.SELF.getConfigGrid().setSaveBtnState(false);
                    SREClient.SELF.statusError(exception.getLocalizedMessage());
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) {
                        JSONObject jObj = JSONParser.parse(response.getText()).isObject();
                        ConfigGrid g = SREClient.SELF.getConfigGrid();
                        g.getEnabled().setState(jObj.get("status").isBoolean().booleanValue());
                        g.getFrequency().setText(Integer.toString((int) (jObj.get("run_freq").isNumber().doubleValue())));
                        g.getEndEarly().setState(jObj.get("end_early").isBoolean().booleanValue());
                        g.getMaxExtension().setText(Integer.toString((int) (jObj.get("max_ext_time").isNumber().doubleValue())));
                        g.getPadding().setText(Integer.toString((int) (jObj.get("default_padding").isNumber().doubleValue())));
                        g.getSafetyNet().setState(jObj.get("safety_net").isBoolean().booleanValue());
                        g.getDelManualFlag().setState(jObj.get("unmark_favs").isBoolean().booleanValue());
                        g.getB2b().setState(jObj.get("ignore_b2b").isBoolean().booleanValue());
                        String href = jObj.get("exit_url").isString().stringValue();
                        if (href.length() > 0) SREClient.SELF.setExitLink(href); else SREClient.SELF.setExitLink("#");
                        g.getNotifySageAlert().setState(jObj.get("notify_sage_alert").isBoolean().booleanValue());
                        g.getSageAlertUrl().setText(jObj.get("sage_alert_url").isString().stringValue());
                        g.getSageAlertId().setText(jObj.get("sage_alert_id").isString().stringValue());
                        g.getSageAlertPwd().setValue(jObj.get("sage_alert_pwd").isString().stringValue());
                        SREClient.SELF.getConfigGrid().setSaveBtnState(true);
                    } else {
                        SREClient.SELF.getConfigGrid().setSaveBtnState(false);
                        SREClient.SELF.statusError(SREClient.messages.jsonErrWithCode(response.getStatusText(), Integer.toString(response.getStatusCode())));
                    }
                }
            });
        } catch (RequestException e) {
            SREClient.SELF.getConfigGrid().setSaveBtnState(false);
            SREClient.SELF.statusError(e.getLocalizedMessage());
        }
        return;
    }

    void saveConfigForm(Map<String, String> args) {
        args.put("sub", "conf");
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, POST_URL);
        builder.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        try {
            Request r = builder.sendRequest(encodeArgs(args), new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    SREClient.SELF.statusError(exception.getLocalizedMessage());
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) loadConfigForm(); else SREClient.SELF.statusError(SREClient.messages.jsonErrWithCode(response.getStatusText(), Integer.toString(response.getStatusCode())));
                }
            });
        } catch (RequestException e) {
            SREClient.SELF.statusError(e.getLocalizedMessage());
        }
    }

    void createOverride(String id, final String title, final String subtitle) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, POST_URL);
        builder.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        String data = "sub=addOverride&title=" + URL.encodeComponent(title) + "&id=" + URL.encodeComponent(id) + "&subtitle=" + URL.encodeComponent(subtitle);
        final LoadingPopupPanel p = new LoadingPopupPanel("Verifying override with server");
        p.center();
        try {
            Request r = builder.sendRequest(data, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    p.hide();
                    SREClient.SELF.statusError(exception.getLocalizedMessage());
                }

                public void onResponseReceived(Request request, Response response) {
                    p.hide();
                    GWT.log(response.getText(), null);
                    if (response.getStatusCode() == Response.SC_OK) {
                        JSONObject jobj = JSONParser.parse(response.getText()).isObject();
                        if (jobj.get("error") == null && jobj.size() > 0) {
                            SREClient.SELF.statusMessage(SREClient.constants.overrideCreated());
                            SREClient.SELF.getOverrideTable().updateRow(new TvAiring(jobj));
                            if (Window.confirm("Would you like to add this override to the global SRE override map?\nDoing so means you won't have to create this override in the future.")) {
                                new GlobalOverrideEditor(title, jobj.get("origSubtitle").isString().stringValue(), subtitle).center();
                            }
                        } else if (jobj.size() == 0) SREClient.SELF.statusError(SREClient.constants.diffSubtitle()); else SREClient.SELF.statusError(jobj.get("error").isString().stringValue());
                    } else SREClient.SELF.statusError(SREClient.messages.jsonErrWithCode(response.getStatusText(), Integer.toString(response.getStatusCode())));
                }
            });
        } catch (RequestException e) {
            p.hide();
            SREClient.SELF.statusError(e.getLocalizedMessage());
        }
    }

    void deleteOverride(String id) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, POST_URL);
        builder.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        String data = "sub=rmOverride&id=" + URL.encodeComponent(id);
        try {
            Request r = builder.sendRequest(data, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    SREClient.SELF.statusError(exception.getLocalizedMessage());
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) {
                        SREClient.SELF.statusMessage(SREClient.constants.overrideRemoved());
                        SREClient.SELF.getOverrideTable().updateRow(new TvAiring(JSONParser.parse(response.getText()).isObject()));
                    } else SREClient.SELF.statusError(SREClient.messages.jsonErrWithCode(response.getStatusText(), Integer.toString(response.getStatusCode())));
                }
            });
        } catch (RequestException e) {
            SREClient.SELF.statusError(e.getLocalizedMessage());
        }
    }

    void saveDebug(boolean state, boolean isDone) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, POST_URL);
        builder.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        String data = "sub=saveDebug&test_mode=" + URL.encodeComponent(Boolean.toString(state)) + "&test_monitor=" + URL.encodeComponent(Boolean.toString(isDone));
        try {
            Request r = builder.sendRequest(data, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    SREClient.SELF.statusError(exception.getLocalizedMessage());
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) SREClient.SELF.statusMessage(SREClient.constants.savingConfig()); else SREClient.SELF.statusError(SREClient.messages.jsonErrWithCode(response.getStatusText(), Integer.toString(response.getStatusCode())));
                }
            });
        } catch (RequestException e) {
            SREClient.SELF.statusError(e.getLocalizedMessage());
        }
    }

    void loadDebug() {
        String url = GET_URL + "debug";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            Request r = builder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    SREClient.SELF.statusError(exception.getLocalizedMessage());
                }

                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) {
                        JSONObject jobj = JSONParser.parse(response.getText()).isObject();
                        SREClient.SELF.getDebugTable().load(jobj);
                        SREClient.SELF.statusMessage(SREClient.constants.savingConfig());
                    } else SREClient.SELF.statusError(SREClient.messages.jsonErrWithCode(response.getStatusText(), Integer.toString(response.getStatusCode())));
                }
            });
        } catch (RequestException e) {
            SREClient.SELF.statusError(e.getLocalizedMessage());
        }
    }

    private String encodeArgs(Map<String, String> args) {
        String encodedData = new String();
        if (args == null) return encodedData;
        Iterator<String> iter = args.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (encodedData.length() > 0) encodedData = encodedData.concat("&");
            encodedData = encodedData.concat(URL.encodeComponent(key) + '=' + URL.encodeComponent(args.get(key)));
        }
        return encodedData;
    }
}
