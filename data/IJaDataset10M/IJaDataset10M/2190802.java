package de.fau.cs.osr.dosis.android;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import de.fau.cs.dosis.dto.DrugDto;
import de.fau.cs.dosis.dto.DrugRetrieveSet;
import de.fau.cs.dosis.restlet.DrugIdsResource;
import de.fau.cs.dosis.restlet.DrugListResource;

public class UpdateHelper extends Activity implements Runnable {

    private static final int STEP_SIZE = 25;

    private ProgressDialog pDia;

    private ProgressDialog pBar;

    private boolean updateProcessStopped;

    private boolean updateProcessError;

    private UpdateHelper activity;

    private Thread updateThread;

    private Thread saveThread;

    private LinkedBlockingQueue<DrugDto> drugSaveQueue = new LinkedBlockingQueue<DrugDto>();

    private DrugDto drugPoison = new DrugDto();

    private enum StatusCode {

        ConnectionError, DatabaseError, DownloadStarted, DownloadProgress, UpdateStopped, Done, PersonalNote, UploadStarted, UploadProgress
    }

    private class ProgressInformation {

        public StatusCode status;

        public String str = "";

        public int max = 0;

        public ProgressInformation(StatusCode status) {
            this.status = status;
        }

        public ProgressInformation(StatusCode status, int max) {
            this.status = status;
            this.max = max;
        }

        public ProgressInformation(StatusCode status, String str) {
            this.status = status;
            this.str = new String(str);
        }
    }

    private Message progressMessage(ProgressInformation inf) {
        Message msg = new Message();
        msg.what = RESULT_OK;
        msg.obj = inf;
        return msg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_view);
        DbHelper.createInstance(this);
        updateProcessStopped = false;
        updateProcessError = false;
        pDia = new ProgressDialog(this);
        pDia.setCancelable(false);
        pDia.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    pDia.hide();
                    stopUpdate();
                    return true;
                }
                return false;
            }
        });
        pBar = new ProgressDialog(this);
        pBar.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    pBar.hide();
                    stopUpdate();
                    return true;
                }
                return false;
            }
        });
        activity = this;
        drugSaveQueue.clear();
        updateThread = new Thread(activity);
        saveThread = new Thread() {

            public void run() {
                DrugManager drugManager = DrugManager.getInstance();
                DrugDto drug = null;
                try {
                    while ((drug = drugSaveQueue.take()) != drugPoison) {
                        drugManager.saveDrugDto(drug);
                        handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.DownloadProgress)));
                        if (updateProcessStopped) break;
                    }
                } catch (InterruptedException e) {
                    handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.DatabaseError, "Einträge konnten nicht gespeichert werden.")));
                    return;
                } finally {
                    drugSaveQueue.clear();
                }
                if (!updateProcessStopped) {
                    handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.Done)));
                }
            }
        };
        updateThread.start();
        saveThread.start();
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == RESULT_OK) {
                ProgressInformation inf;
                if (msg.obj == null) return;
                inf = (ProgressInformation) msg.obj;
                switch(inf.status) {
                    case ConnectionError:
                    case DatabaseError:
                        pDia.hide();
                        pBar.hide();
                        updateProcessError = true;
                        showMessage(inf.str, updateProcessError);
                        break;
                    case UpdateStopped:
                        showMessage(inf.str, updateProcessStopped);
                        break;
                    case PersonalNote:
                        pDia.setMessage("Lade persönliche Notizen");
                        break;
                    case DownloadStarted:
                        if (inf.max == -1) {
                            pBar.hide();
                            pDia.setMessage("Lade Medikamente");
                            pDia.setCancelable(false);
                            pDia.show();
                        } else {
                            pDia.hide();
                            pBar.setMessage("Hole Medikamente");
                            pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            pBar.setMax(inf.max);
                            pBar.setCancelable(false);
                            pBar.show();
                        }
                        break;
                    case UploadStarted:
                        if (inf.max == -1) {
                            pDia.setMessage("Suche nach geänderten persönlichen Notizen");
                            pDia.setCancelable(false);
                            pDia.show();
                        } else {
                            pDia.hide();
                            pBar.setMessage("Synchronisieren der persönlichen Notizen");
                            pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            pBar.setMax(inf.max);
                            pBar.setCancelable(false);
                            pBar.show();
                        }
                        break;
                    case UploadProgress:
                        pBar.incrementProgressBy(1);
                        break;
                    case DownloadProgress:
                        pBar.incrementProgressBy(1);
                        break;
                    case Done:
                        pDia.hide();
                        pBar.hide();
                        showMessage("Update wurde erfolgreich durchgeführt!", !updateProcessError & !updateProcessStopped);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void showMessage(String message, boolean show) {
        if (show) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(message).setCancelable(false).setNeutralButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void stopSaveThreat() {
        try {
            drugSaveQueue.put(drugPoison);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopUpdate() {
        updateProcessStopped = true;
        handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.UpdateStopped, "Update wurde gestoppt.")));
        updateThread.interrupt();
    }

    private static String partJoin(int[] numbers, int start, int end, String split) {
        end = end > numbers.length ? numbers.length : end;
        if (start >= 0 && start < end) {
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                builder.append(split);
                builder.append(numbers[i]);
            }
            builder.replace(0, split.length(), "");
            return builder.toString();
        } else {
            return "";
        }
    }

    /**
	 * Loads all drugs with the ids in drugIds from restinterface
	 * and enqueues it for the saveThread in drugSaveQueue
	 * 
	 * @param drugIds
	 */
    private void processRetrieveDrugs(int[] drugIds) {
        int drugIdslength = drugIds.length;
        for (int i = 0; i < drugIdslength; i += STEP_SIZE) {
            DrugListResource resource = RestletHelper.getInstance().getClientResource("drug/" + partJoin(drugIds, i, i + STEP_SIZE, "-"), DrugListResource.class);
            try {
                DrugRetrieveSet drugSet = resource.retrieve();
                for (DrugDto dto : drugSet.getDtos()) {
                    drugSaveQueue.add(dto);
                }
            } catch (Exception e) {
                drugSaveQueue.clear();
                if (!updateProcessStopped) {
                    Log.w(UpdateHelper.class.toString(), "Connection error: " + e.toString());
                    handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.ConnectionError, e.toString())));
                }
                return;
            }
        }
    }

    private void processUploadNotes(List<DrugDto> changedNotes) {
        Log.i("DosIS", "got " + changedNotes.size() + " changed notes to upload");
        int changeNotesSize = changedNotes.size();
        for (int i = 0; i < changeNotesSize; i += STEP_SIZE) {
            int start = i;
            int end = changedNotes.size() - i > STEP_SIZE ? STEP_SIZE : changedNotes.size() - i;
            int index = end - start;
            int[] ids = new int[index];
            DrugDto[] upload = new DrugDto[index];
            for (int j = 0; j < index; j++) {
                upload[j] = changedNotes.get(start + j);
                ids[j] = upload[j].getId();
            }
            Log.i("DosIS", "upload");
            RestletHelper.getInstance().getClientResource("drug/" + partJoin(ids, 0, end - start, "-"), DrugListResource.class).store(upload);
            handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.UploadProgress)));
        }
    }

    @Override
    public void run() {
        try {
            handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.UploadStarted, -1)));
            List<DrugDto> changedNotes = DrugManager.getInstance().getChangedPersonalNotes();
            if (changedNotes.size() > 0) {
                int steps = (int) Math.ceil((changedNotes.size() / STEP_SIZE));
                handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.UploadStarted, steps)));
                processUploadNotes(changedNotes);
            }
            handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.DownloadStarted, -1)));
            DrugManager.getInstance().deleteAllDrugs();
            int[] ids;
            try {
                ids = RestletHelper.getInstance().getClientResource("drugIds", DrugIdsResource.class).getAll();
                if (ids.length != 0 && !updateProcessStopped) {
                    handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.DownloadStarted, ids.length)));
                    processRetrieveDrugs(ids);
                } else {
                    if (!updateProcessStopped) {
                        handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.ConnectionError, "Auf dem Server wurden keine Einträge gefunden.")));
                    }
                }
            } catch (Exception e) {
                if (!updateProcessStopped) {
                    handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.ConnectionError, "Verbindung zum Server fehlgeschlagen: \n" + e.toString())));
                }
                return;
            }
        } catch (Exception e) {
            if (!updateProcessStopped) {
                handler.sendMessage(progressMessage(new ProgressInformation(StatusCode.DatabaseError, "Einträge konnten nicht gespeichert werden.")));
            }
            return;
        } finally {
            stopSaveThreat();
        }
    }
}
