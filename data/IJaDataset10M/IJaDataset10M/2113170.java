package cvut.fel.mobilevoting.murinrad.gui;

import cvut.fel.mobilevoting.murinrad.R;
import cvut.fel.mobilevoting.murinrad.communications.ConnectionInterface;
import cvut.fel.mobilevoting.murinrad.datacontainers.ServerData;
import cvut.fel.mobilevoting.murinrad.views.ServerListView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * A class that represents a server(Access point) in clickable form
 * 
 * @author Radovan Murin
 * 
 */
public class ServerButton extends DefaultButton {

    final ServerData server;

    final ServerButton me = this;

    final ServerListView parent;

    boolean tst = false;

    ConnectionInterface con = null;

    public ServerButton(final Context context, final ServerData server, ServerListView parent) {
        super(context, server.getFriendlyName());
        this.server = server;
        this.parent = parent;
    }

    @Override
    public void onClickAction() {
        if (server.getId() == -2) {
            editMe();
        } else {
            connectMe();
        }
    }

    private void showChoices() {
        final CharSequence[] items = { me.context.getString(R.string.EditServerTag), me.context.getString(R.string.deleteServerTag), me.context.getString(R.string.serverMenuJustConnect) };
        AlertDialog.Builder builder = new AlertDialog.Builder(me.context);
        builder.setTitle(me.context.getString(R.string.pickChallenge));
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                switch(item) {
                    case 0:
                        editMe();
                        break;
                    case 1:
                        deleteMe();
                        break;
                    case 2:
                        if (server.getId() == -2) {
                            editMe();
                            break;
                        }
                        connectMe();
                        break;
                    default:
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteMe() {
        parent.deleteServer(server.getId());
    }

    private void connectMe() {
        Intent i = new Intent(me.context, cvut.fel.mobilevoting.murinrad.views.QuestionsView.class);
        i.putExtra("ServerData", me.server);
        me.context.startActivity(i);
    }

    private void editMe() {
        Intent i = new Intent(me.context, cvut.fel.mobilevoting.murinrad.views.ChangeServerView.class);
        i.putExtra("id", me.server.getId());
        if (me.server.getId() == -2) i.putExtra("server", me.server);
        me.context.startActivity(i);
    }

    @Override
    public void onLongClickAction() {
        showChoices();
    }
}
